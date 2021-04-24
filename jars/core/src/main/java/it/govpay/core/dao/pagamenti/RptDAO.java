package it.govpay.core.dao.pagamenti;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.UtilsException;
import org.openspcoop2.utils.json.ValidationException;
import org.openspcoop2.utils.logger.beans.Property;
import org.openspcoop2.utils.service.context.ContextThreadLocal;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.Versamento;
import it.govpay.bd.model.eventi.DatiPagoPA;
import it.govpay.bd.pagamento.RptBD;
import it.govpay.bd.pagamento.filters.RptFilter;
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
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.exceptions.NdpException;
import it.govpay.core.exceptions.NdpException.FaultPa;
import it.govpay.core.exceptions.NotAuthenticatedException;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.core.exceptions.UnprocessableEntityException;
import it.govpay.core.utils.EventoContext.Componente;
import it.govpay.core.utils.EventoContext.Esito;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.RtUtils;
import it.govpay.model.PatchOp;
import it.govpay.model.PatchOp.OpEnum;

public class RptDAO extends BaseDAO{
	
	private static final String PATH_BLOCCANTE = "/bloccante";
	private static final String PATH_RT = "/rt";

	public RptDAO() {
	}

	public LeggiRptDTOResponse leggiRpt(LeggiRptDTO leggiRptDTO) throws ServiceException,RicevutaNonTrovataException, NotAuthorizedException, NotAuthenticatedException{
		LeggiRptDTOResponse response = new LeggiRptDTOResponse();
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), this.useCacheData);
		RptBD rptBD = null;

		try {
			// controllo che il dominio sia autorizzato
			String idDominio = leggiRptDTO.getIdDominio();
			String iuv = leggiRptDTO.getIuv();
			String ccp = leggiRptDTO.getCcp();
			
			((GpContext) (ContextThreadLocal.get()).getApplicationContext()).getEventoCtx().setCodDominio(idDominio);
			((GpContext) (ContextThreadLocal.get()).getApplicationContext()).getEventoCtx().setIuv(iuv);
			((GpContext) (ContextThreadLocal.get()).getApplicationContext()).getEventoCtx().setCcp(ccp);
			
			rptBD = new RptBD(configWrapper);
			
			
			Rpt	rpt = rptBD.getRpt(idDominio, iuv, ccp, true);
			
			response.setRpt(rpt);
			rpt.getPagamentoPortale().getApplicazione(configWrapper);
			Versamento versamento = rpt.getVersamento();
			response.setVersamento(versamento);
			response.setApplicazione(versamento.getApplicazione(configWrapper)); 
			response.setDominio(versamento.getDominio(configWrapper));
			response.setUnitaOperativa(versamento.getUo(configWrapper));
			versamento.getTipoVersamentoDominio(configWrapper);
			versamento.getTipoVersamento(configWrapper);
			List<SingoloVersamento> singoliVersamenti = versamento.getSingoliVersamenti();
			response.setLstSingoliVersamenti(singoliVersamenti);
			for (SingoloVersamento singoloVersamento : singoliVersamenti) {
				singoloVersamento.getCodContabilita(configWrapper);
				singoloVersamento.getIbanAccredito(configWrapper);
				singoloVersamento.getTipoContabilita(configWrapper);
				singoloVersamento.getTributo(configWrapper);
			}
		} catch (NotFoundException e) {
			throw new RicevutaNonTrovataException(e.getMessage(), e);
		} finally {
			if(rptBD != null)
				rptBD.closeConnection();
		}
		return response;
	}

	public LeggiRicevutaDTOResponse leggiRt(LeggiRicevutaDTO leggiRicevutaDTO) throws ServiceException,RicevutaNonTrovataException, NotAuthorizedException, NotAuthenticatedException{
		LeggiRicevutaDTOResponse response = new LeggiRicevutaDTOResponse();
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), this.useCacheData);
		RptBD rptBD = null;

		try {
			((GpContext) (ContextThreadLocal.get()).getApplicationContext()).getEventoCtx().setCodDominio(leggiRicevutaDTO.getIdDominio());
			((GpContext) (ContextThreadLocal.get()).getApplicationContext()).getEventoCtx().setIuv(leggiRicevutaDTO.getIuv());
			((GpContext) (ContextThreadLocal.get()).getApplicationContext()).getEventoCtx().setCcp(leggiRicevutaDTO.getCcp());
			
			rptBD = new RptBD(configWrapper);
			Rpt rpt = rptBD.getRpt(leggiRicevutaDTO.getIdDominio(), leggiRicevutaDTO.getIuv(), leggiRicevutaDTO.getCcp(), true);
			rpt.getPagamentoPortale().getApplicazione(configWrapper);
			Versamento versamento = rpt.getVersamento();
			response.setVersamento(versamento);
			versamento.getTipoVersamentoDominio(configWrapper);
			versamento.getTipoVersamento(configWrapper);
			List<SingoloVersamento> singoliVersamenti = versamento.getSingoliVersamenti();
			for (SingoloVersamento singoloVersamento : singoliVersamenti) {
				singoloVersamento.getCodContabilita(configWrapper);
				singoloVersamento.getIbanAccredito(configWrapper);
				singoloVersamento.getTipoContabilita(configWrapper);
				singoloVersamento.getTributo(configWrapper);
			}
			
			if(rpt.getXmlRt() == null)
				throw new RicevutaNonTrovataException(null);

			if(leggiRicevutaDTO.getFormato().equals(FormatoRicevuta.PDF)) {
				it.govpay.core.business.RicevutaTelematica avvisoBD = new it.govpay.core.business.RicevutaTelematica();
				response = avvisoBD.creaPdfRicevuta(leggiRicevutaDTO,rpt);
			}

			response.setRpt(rpt);
			response.setDominio(rpt.getDominio(configWrapper));
		} catch (NotFoundException e) {
			throw new RicevutaNonTrovataException(e.getMessage(), e);
		} finally {
			if(rptBD != null)
				rptBD.closeConnection();
		}
		return response;
	}
	
	public ListaRptDTOResponse countRpt(ListaRptDTO listaRptDTO) throws ServiceException,PagamentoPortaleNonTrovatoException, NotAuthorizedException, NotAuthenticatedException{
		RptBD rptBD = null;
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), this.useCacheData);
		
		try {
			rptBD = new RptBD(configWrapper);
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
			
			filter.setDataRtDa(listaRptDTO.getDataRtDa());
			filter.setDataRtA(listaRptDTO.getDataRtA());
			filter.setIdDebitore(listaRptDTO.getIdDebitore());
			filter.setDivisione(listaRptDTO.getDivisione());
			filter.setDirezione(listaRptDTO.getDirezione());
			filter.setTassonomia(listaRptDTO.getTassonomia());
			filter.setIdTipoPendenza(listaRptDTO.getIdTipoPendenza());
			filter.setIdUnita(listaRptDTO.getIdUnita());
			filter.setAnagraficaDebitore(listaRptDTO.getAnagraficaDebitore());
			filter.setEseguiCountConLimit(listaRptDTO.isEseguiCountConLimit());

			long count = rptBD.count(filter);

			return new ListaRptDTOResponse(count, new ArrayList<>());
		} finally {
			if(rptBD != null)
				rptBD.closeConnection();
		}
	}

	public ListaRptDTOResponse listaRpt(ListaRptDTO listaRptDTO) throws ServiceException,PagamentoPortaleNonTrovatoException, NotAuthorizedException, NotAuthenticatedException{
		it.govpay.bd.viste.RptBD rptBD = null;
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), this.useCacheData);
		try {
			rptBD = new it.govpay.bd.viste.RptBD(configWrapper);

			return this.listaRpt(listaRptDTO, rptBD);
		} finally {
			if(rptBD != null)
				rptBD.closeConnection();
		}
	}

	public ListaRptDTOResponse listaRpt(ListaRptDTO listaRptDTO, it.govpay.bd.viste.RptBD rptBD) throws NotAuthenticatedException, NotAuthorizedException, ServiceException {
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), this.useCacheData);
		it.govpay.bd.viste.filters.RptFilter filter = rptBD.newFilter();

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
		
		filter.setDataRtDa(listaRptDTO.getDataRtDa());
		filter.setDataRtA(listaRptDTO.getDataRtA());
		filter.setIdDebitore(listaRptDTO.getIdDebitore());
		filter.setDivisione(listaRptDTO.getDivisione());
		filter.setDirezione(listaRptDTO.getDirezione());
		filter.setTassonomia(listaRptDTO.getTassonomia());
		filter.setIdTipoPendenza(listaRptDTO.getIdTipoPendenza());
		filter.setIdUnita(listaRptDTO.getIdUnita());
		filter.setAnagraficaDebitore(listaRptDTO.getAnagraficaDebitore());
		filter.setEseguiCountConLimit(listaRptDTO.isEseguiCountConLimit());
		
		Long count = null;
		
		if(listaRptDTO.isEseguiCount()) {
			 count = rptBD.count(filter);
		}

		List<LeggiRptDTOResponse> resList = new ArrayList<>();
		if(listaRptDTO.isEseguiFindAll()) {
			List<Rpt> findAll = rptBD.findAll(filter);

			for (Rpt rpt : findAll) {
				LeggiRptDTOResponse elem = new LeggiRptDTOResponse();
				elem.setRpt(rpt);
				Versamento versamento = rpt.getVersamento(configWrapper);
				versamento.getDominio(configWrapper);
				versamento.getUo(configWrapper);
				versamento.getTipoVersamentoDominio(configWrapper);
				versamento.getTipoVersamento(configWrapper);
				elem.setVersamento(versamento);
				elem.setApplicazione(versamento.getApplicazione(configWrapper)); 
				resList.add(elem);
			}
		} 

		return new ListaRptDTOResponse(count, resList);
	}

	public PatchRptDTOResponse patch(PatchRptDTO patchRptDTO) throws ServiceException, RicevutaNonTrovataException, NotAuthorizedException, NotAuthenticatedException, ValidationException, UnprocessableEntityException {
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), this.useCacheData);
		PatchRptDTOResponse response = new PatchRptDTOResponse();

		RptBD rptBD = null;

		try {
			// patch
			String idDominio = patchRptDTO.getIdDominio();
			String iuv = patchRptDTO.getIuv();
			String ccp = patchRptDTO.getCcp();
			GpContext appContext = (GpContext) (ContextThreadLocal.get()).getApplicationContext();
			appContext.getEventoCtx().setCodDominio(idDominio);
			appContext.getEventoCtx().setIuv(iuv);
			appContext.getEventoCtx().setCcp(ccp);
			
			rptBD = new RptBD(configWrapper);
			Rpt	rpt = rptBD.getRpt(idDominio, iuv, ccp, true);
			
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
				} else if(PATH_RT.equals(op.getPath())) {
					if(!op.getOp().equals(OpEnum.REPLACE)) {
						throw new ValidationException(MessageFormat.format(UtenzaPatchUtils.OP_XX_NON_VALIDO_PER_IL_PATH_YY, op.getOp(), op.getPath()));
					}
					
					appContext.getRequest().addGenericProperty(new Property("ccp", ccp));
					appContext.getRequest().addGenericProperty(new Property("codDominio", idDominio));
					appContext.getRequest().addGenericProperty(new Property("iuv", iuv));
					
					try {
						(ContextThreadLocal.get()).getApplicationLogger().log("pagamento.ricezioneRt");
					} catch (UtilsException e) {
						log.error("Errore durante il log dell'operazione: " + e.getMessage(),e);
					}
					
					DatiPagoPA datiPagoPA = new DatiPagoPA();
					datiPagoPA.setCodStazione(null);
					datiPagoPA.setFruitore(Componente.API_BACKOFFICE.name());
					datiPagoPA.setCodDominio(idDominio);
					datiPagoPA.setErogatore(GpContext.GovPay);
					datiPagoPA.setCodIntermediario(null);
					appContext.getEventoCtx().setDatiPagoPA(datiPagoPA);
					
					appContext.getEventoCtx().setIdA2A(rpt.getVersamento().getApplicazione(configWrapper).getCodApplicazione());
					appContext.getEventoCtx().setIdPendenza(rpt.getVersamento().getCodVersamentoEnte());
					if(rpt.getPagamentoPortale() != null)
						appContext.getEventoCtx().setIdPagamento(rpt.getPagamentoPortale().getIdSessione());
					
					try {
						// decodifica del base64 contenuto nel value della patch
						

						byte [] rtByte = Base64.getDecoder().decode(((String) op.getValue()).getBytes());
						
						log.debug("Nuova RT: " + new String(rtByte));
						
						rpt = RtUtils.acquisisciRT(idDominio, iuv, ccp, rtByte, false, true);
						
						appContext.getResponse().addGenericProperty(new Property("esitoPagamento", rpt.getEsitoPagamento().toString()));
						(ContextThreadLocal.get()).getApplicationLogger().log("pagamento.acquisizioneRtOk");
						datiPagoPA.setCodCanale(rpt.getCodCanale());
						datiPagoPA.setTipoVersamento(rpt.getTipoVersamento());
						
						appContext.getEventoCtx().setDescrizioneEsito("Acquisita ricevuta di pagamento [IUV: " + rpt.getIuv() + " CCP:" + rpt.getCcp() + "] emessa da " + rpt.getDenominazioneAttestante());
						appContext.getEventoCtx().setEsito(Esito.OK);
						
						(ContextThreadLocal.get()).getApplicationLogger().log("rt.ricezioneOk");
					}catch (NdpException e) {
//						if(bd != null) bd.rollback();
						String faultDescription = e.getDescrizione() == null ? "<Nessuna descrizione>" : e.getDescrizione(); 
						try {
							(ContextThreadLocal.get()).getApplicationLogger().log("rt.ricezioneKo", e.getFaultCode(), e.getFaultString(), faultDescription);
						} catch (UtilsException e1) {
							log.error("Errore durante il log dell'operazione: " + e1.getMessage(),e1);
						}
						if(e.getFaultCode().equals(FaultPa.PAA_SYSTEM_ERROR.name()))
							appContext.getEventoCtx().setEsito(Esito.FAIL);
						else 
							appContext.getEventoCtx().setEsito(Esito.KO);
						appContext.getEventoCtx().setDescrizioneEsito(faultDescription);
						appContext.getEventoCtx().setSottotipoEsito(e.getFaultCode());
						appContext.getEventoCtx().setException(e);
						
						throw new UnprocessableEntityException("RT non valida: " + faultDescription);
					} catch (ServiceException | UtilsException | GovPayException e) {
//						if(bd != null) bd.rollback();
						NdpException ndpe = new NdpException(FaultPa.PAA_SYSTEM_ERROR, idDominio, e.getMessage(), e);
						String faultDescription = ndpe.getDescrizione() == null ? "<Nessuna descrizione>" : ndpe.getDescrizione(); 
						try {
							(ContextThreadLocal.get()).getApplicationLogger().log("rt.ricezioneKo", ndpe.getFaultCode(), ndpe.getFaultString(), faultDescription);
						} catch (UtilsException e1) {
							log.error("Errore durante il log dell'operazione: " + e1.getMessage(),e1);
						}
						appContext.getEventoCtx().setSottotipoEsito(ndpe.getFaultCode());
						appContext.getEventoCtx().setEsito(Esito.FAIL);
						appContext.getEventoCtx().setDescrizioneEsito(faultDescription);
						appContext.getEventoCtx().setException(e);
						
						throw new UnprocessableEntityException("RT non valida: " + faultDescription);
					} 

				} else {
					throw new ServiceException("Path '"+op.getPath()+"' non valido");
				}
			}

			// ricarico l'RPT
			rpt = rptBD.getRpt(idDominio, iuv, ccp, true);

			rpt.getPagamentoPortale().getApplicazione(configWrapper);
			response.setRpt(rpt);
			response.setVersamento(rpt.getVersamento());
			response.setApplicazione(rpt.getVersamento().getApplicazione(configWrapper)); 
			response.setDominio(rpt.getVersamento().getDominio(configWrapper));
			response.setUnitaOperativa(rpt.getVersamento().getUo(configWrapper));
			rpt.getVersamento().getTipoVersamentoDominio(configWrapper);
			rpt.getVersamento().getTipoVersamento(configWrapper);
			List<SingoloVersamento> singoliVersamenti = rpt.getVersamento().getSingoliVersamenti();
			response.setLstSingoliVersamenti(singoliVersamenti);
			for (SingoloVersamento singoloVersamento : singoliVersamenti) {
				singoloVersamento.getCodContabilita(configWrapper);
				singoloVersamento.getIbanAccredito(configWrapper);
				singoloVersamento.getTipoContabilita(configWrapper);
				singoloVersamento.getTributo(configWrapper);
			}
		} catch (NotFoundException e) {
			throw new RicevutaNonTrovataException(e.getMessage(), e);
		} finally {
			if(rptBD != null)
				rptBD.closeConnection();
		}
		return response;
	}
}
