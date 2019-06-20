package it.govpay.core.dao.pagamenti;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.json.ValidationException;
import org.openspcoop2.utils.service.context.ContextThreadLocal;

import it.govpay.bd.BasicBD;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.pagamento.RptBD;
import it.govpay.bd.pagamento.filters.RptFilter;
import it.govpay.bd.viste.model.VersamentoIncasso;
import it.govpay.core.autorizzazione.AuthorizationManager;
import it.govpay.core.dao.anagrafica.utils.UtenzaPatchUtils;
import it.govpay.core.dao.commons.BaseDAO;
import it.govpay.core.dao.pagamenti.dto.LeggiRicevutaDTO;
import it.govpay.core.dao.pagamenti.dto.LeggiRicevutaDTO.FormatoRicevuta;
import it.govpay.core.dao.pagamenti.dto.LeggiRicevutaDTOResponse;
import it.govpay.core.dao.pagamenti.dto.LeggiRptDTO;
import it.govpay.core.dao.pagamenti.dto.LeggiRptDTOResponse;
import it.govpay.core.dao.pagamenti.dto.ListaRptDTO;
import it.govpay.core.dao.pagamenti.dto.ListaRptDTOResponse;
import it.govpay.core.dao.pagamenti.dto.PatchRptDTO;
import it.govpay.core.dao.pagamenti.dto.PatchRptDTOResponse;
import it.govpay.core.dao.pagamenti.exception.PagamentoPortaleNonTrovatoException;
import it.govpay.core.dao.pagamenti.exception.RicevutaNonTrovataException;
import it.govpay.core.exceptions.NotAuthenticatedException;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.core.utils.GpContext;
import it.govpay.model.PatchOp;
import it.govpay.model.PatchOp.OpEnum;

public class RptDAO extends BaseDAO{
	
	private static final String PATH_BLOCCANTE = "/bloccante";

	public RptDAO() {
	}

	public LeggiRptDTOResponse leggiRpt(LeggiRptDTO leggiRptDTO) throws ServiceException,RicevutaNonTrovataException, NotAuthorizedException, NotAuthenticatedException{
		LeggiRptDTOResponse response = new LeggiRptDTOResponse();

		BasicBD bd = null;

		try {
			bd = BasicBD.newInstance(ContextThreadLocal.get().getTransactionId());

			// controllo che il dominio sia autorizzato
			String idDominio = leggiRptDTO.getIdDominio();
			String iuv = leggiRptDTO.getIuv();
			String ccp = leggiRptDTO.getCcp();
			
			((GpContext) (ContextThreadLocal.get()).getApplicationContext()).getEventoCtx().setCodDominio(idDominio);
			((GpContext) (ContextThreadLocal.get()).getApplicationContext()).getEventoCtx().setIuv(iuv);
			((GpContext) (ContextThreadLocal.get()).getApplicationContext()).getEventoCtx().setCcp(ccp);
			
			RptBD rptBD = new RptBD(bd);
			Rpt	rpt = rptBD.getRpt(idDominio, iuv, ccp);
			
			response.setRpt(rpt);
			rpt.getPagamentoPortale(bd).getApplicazione(bd);
			VersamentoIncasso versamento = rpt.getVersamento(bd);
			response.setVersamento(versamento);
			response.setApplicazione(versamento.getApplicazione(bd)); 
			response.setDominio(versamento.getDominio(bd));
			response.setUnitaOperativa(versamento.getUo(bd));
			versamento.getTipoVersamentoDominio(bd);
			versamento.getTipoVersamento(bd);
			List<SingoloVersamento> singoliVersamenti = versamento.getSingoliVersamenti(bd);
			response.setLstSingoliVersamenti(singoliVersamenti);
			for (SingoloVersamento singoloVersamento : singoliVersamenti) {
				singoloVersamento.getCodContabilita(bd);
				singoloVersamento.getIbanAccredito(bd);
				singoloVersamento.getTipoContabilita(bd);
				singoloVersamento.getTributo(bd);
			}
		} catch (NotFoundException e) {
			throw new RicevutaNonTrovataException(e.getMessage(), e);
		} finally {
			if(bd != null)
				bd.closeConnection();
		}
		return response;
	}

	public LeggiRicevutaDTOResponse leggiRt(LeggiRicevutaDTO leggiRicevutaDTO) throws ServiceException,RicevutaNonTrovataException, NotAuthorizedException, NotAuthenticatedException{
		LeggiRicevutaDTOResponse response = new LeggiRicevutaDTOResponse();

		BasicBD bd = null;

		try {
			bd = BasicBD.newInstance(ContextThreadLocal.get().getTransactionId());
			
			((GpContext) (ContextThreadLocal.get()).getApplicationContext()).getEventoCtx().setCodDominio(leggiRicevutaDTO.getIdDominio());
			((GpContext) (ContextThreadLocal.get()).getApplicationContext()).getEventoCtx().setIuv(leggiRicevutaDTO.getIuv());
			((GpContext) (ContextThreadLocal.get()).getApplicationContext()).getEventoCtx().setCcp(leggiRicevutaDTO.getCcp());
			
			RptBD rptBD = new RptBD(bd);
			Rpt rpt = rptBD.getRpt(leggiRicevutaDTO.getIdDominio(), leggiRicevutaDTO.getIuv(), leggiRicevutaDTO.getCcp());
			rpt.getPagamentoPortale(bd).getApplicazione(bd);
			
			if(rpt.getXmlRt() == null)
				throw new RicevutaNonTrovataException(null);

			if(leggiRicevutaDTO.getFormato().equals(FormatoRicevuta.PDF)) {
				it.govpay.core.business.RicevutaTelematica avvisoBD = new it.govpay.core.business.RicevutaTelematica(bd);
				response = avvisoBD.creaPdfRicevuta(leggiRicevutaDTO,rpt);
			}

			response.setRpt(rpt);
			response.setDominio(rpt.getDominio(bd));
		} catch (NotFoundException e) {
			throw new RicevutaNonTrovataException(e.getMessage(), e);
		} finally {
			if(bd != null)
				bd.closeConnection();
		}
		return response;
	}

	public ListaRptDTOResponse listaRpt(ListaRptDTO listaRptDTO) throws ServiceException,PagamentoPortaleNonTrovatoException, NotAuthorizedException, NotAuthenticatedException{
		BasicBD bd = null;

		try {
			bd = BasicBD.newInstance(ContextThreadLocal.get().getTransactionId());

			return this.listaRpt(listaRptDTO, bd);
		} finally {
			if(bd != null)
				bd.closeConnection();
		}
	}

	public ListaRptDTOResponse listaRpt(ListaRptDTO listaRptDTO, BasicBD bd) throws NotAuthenticatedException, NotAuthorizedException, ServiceException {
		RptBD rptBD = new RptBD(bd);
		RptFilter filter = rptBD.newFilter();

		filter.setOffset(listaRptDTO.getOffset());
		filter.setLimit(listaRptDTO.getLimit());
		filter.setDataInizio(listaRptDTO.getDataDa());
		filter.setDataFine(listaRptDTO.getDataA());
		filter.setStato(listaRptDTO.getStato());
		filter.setCcp(listaRptDTO.getCcp());
		filter.setIuv(listaRptDTO.getIuv());
		filter.setCodDominio(listaRptDTO.getIdDominio());
		filter.setIdDomini(listaRptDTO.getCodDomini());

		filter.setCodPagamentoPortale(listaRptDTO.getIdPagamento());
		filter.setIdPendenza(listaRptDTO.getIdPendenza());
		filter.setCodApplicazione(listaRptDTO.getIdA2A());
		filter.setFilterSortList(listaRptDTO.getFieldSortList());
		filter.setCfCittadinoPagamentoPortale(listaRptDTO.getCfCittadino());
		filter.setCodApplicazionePagamentoPortale(listaRptDTO.getIdA2APagamentoPortale());
		filter.setEsitoPagamento(listaRptDTO.getEsitoPagamento());

		long count = rptBD.count(filter);

		List<LeggiRptDTOResponse> resList = new ArrayList<>();
		if(count > 0) {
			List<Rpt> findAll = rptBD.findAll(filter);

			for (Rpt rpt : findAll) {
				LeggiRptDTOResponse elem = new LeggiRptDTOResponse();
				elem.setRpt(rpt);
				VersamentoIncasso versamento = rpt.getVersamento(bd);
				versamento.getDominio(bd);
				versamento.getUo(bd);
				versamento.getTipoVersamentoDominio(bd);
				versamento.getTipoVersamento(bd);
				elem.setVersamento(versamento);
				elem.setApplicazione(versamento.getApplicazione(bd)); 
				resList.add(elem);
			}
		} 

		return new ListaRptDTOResponse(count, resList);
	}

	public PatchRptDTOResponse patch(PatchRptDTO patchRptDTO) throws ServiceException, RicevutaNonTrovataException, NotAuthorizedException, NotAuthenticatedException, ValidationException {

		PatchRptDTOResponse response = new PatchRptDTOResponse();

		BasicBD bd = null;

		try {
			// patch
			bd = BasicBD.newInstance(ContextThreadLocal.get().getTransactionId());

			String idDominio = patchRptDTO.getIdDominio();
			String iuv = patchRptDTO.getIuv();
			String ccp = patchRptDTO.getCcp();
			((GpContext) (ContextThreadLocal.get()).getApplicationContext()).getEventoCtx().setCodDominio(idDominio);
			((GpContext) (ContextThreadLocal.get()).getApplicationContext()).getEventoCtx().setIuv(iuv);
			((GpContext) (ContextThreadLocal.get()).getApplicationContext()).getEventoCtx().setCcp(ccp);
			
			RptBD rptBD = new RptBD(bd);
			Rpt	rpt = rptBD.getRpt(idDominio, iuv, ccp);
			
			// controllo che il dominio sia autorizzato
			if(!AuthorizationManager.isDominioAuthorized(patchRptDTO.getUser(), patchRptDTO.getIdDominio())) {
				throw AuthorizationManager.toNotAuthorizedException(patchRptDTO.getUser(),patchRptDTO.getIdDominio(), null);
			}
			
			for(PatchOp op: patchRptDTO.getOp()) {
				if(PATH_BLOCCANTE.equals(op.getPath())) {
					if(!op.getOp().equals(OpEnum.REPLACE)) {
						throw new ValidationException(MessageFormat.format(UtenzaPatchUtils.OP_XX_NON_VALIDO_PER_IL_PATH_YY, op.getOp(), op.getPath()));
					}

					Boolean sbloccoRPT = (Boolean) op.getValue();
					String azione = sbloccoRPT ? "reso bloccante" : "sbloccato";
					String descrizioneStato = "Tentativo di pagamento [idDominio:"+idDominio+", IUV:"+iuv+", CCP:"+ccp+"] "+azione+" via API.";
					rptBD.sbloccaRpt(rpt.getId(), sbloccoRPT, descrizioneStato);
				}
			}

			// ricarico l'RPT
			rpt = rptBD.getRpt(idDominio, iuv, ccp);

			rpt.getPagamentoPortale(bd).getApplicazione(bd);
			response.setRpt(rpt);
			response.setVersamento(rpt.getVersamento(bd));
			response.setApplicazione(rpt.getVersamento(bd).getApplicazione(bd)); 
			response.setDominio(rpt.getVersamento(bd).getDominio(bd));
			response.setUnitaOperativa(rpt.getVersamento(bd).getUo(bd));
			rpt.getVersamento(bd).getTipoVersamentoDominio(bd);
			rpt.getVersamento(bd).getTipoVersamento(bd);
			List<SingoloVersamento> singoliVersamenti = rpt.getVersamento(bd).getSingoliVersamenti(bd);
			response.setLstSingoliVersamenti(singoliVersamenti);
			for (SingoloVersamento singoloVersamento : singoliVersamenti) {
				singoloVersamento.getCodContabilita(bd);
				singoloVersamento.getIbanAccredito(bd);
				singoloVersamento.getTipoContabilita(bd);
				singoloVersamento.getTributo(bd);
			}
		} catch (NotFoundException e) {
			throw new RicevutaNonTrovataException(e.getMessage(), e);
		} finally {
			if(bd != null)
				bd.closeConnection();
		}
		return response;
	}
}
