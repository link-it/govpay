package it.govpay.core.dao.pagamenti;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.model.Fr;
import it.govpay.bd.model.Pagamento;
import it.govpay.bd.model.Rendicontazione;
import it.govpay.bd.model.Utenza;
import it.govpay.bd.pagamento.FrBD;
import it.govpay.bd.pagamento.filters.FrFilter;
import it.govpay.core.dao.commons.BaseDAO;
import it.govpay.core.dao.pagamenti.dto.LeggiRendicontazioneDTO;
import it.govpay.core.dao.pagamenti.dto.LeggiRendicontazioneDTOResponse;
import it.govpay.core.dao.pagamenti.dto.ListaRendicontazioniDTO;
import it.govpay.core.dao.pagamenti.dto.ListaRendicontazioniDTOResponse;
import it.govpay.core.dao.pagamenti.exception.RendicontazioneNonTrovataException;
import it.govpay.core.exceptions.NotAuthenticatedException;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.core.utils.AclEngine;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.model.Acl.Diritti;
import it.govpay.model.Acl.Servizio;

public class RendicontazioniDAO extends BaseDAO{

	public RendicontazioniDAO() {
	}

	public ListaRendicontazioniDTOResponse listaRendicontazioni(ListaRendicontazioniDTO listaRendicontazioniDTO) throws ServiceException, NotAuthorizedException, NotAuthenticatedException, NotFoundException{
		List<String> listaDominiFiltro = null;
		BasicBD bd = null;

		try {
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			this.autorizzaRichiesta(listaRendicontazioniDTO.getUser(), Servizio.RENDICONTAZIONI_E_INCASSI, Diritti.LETTURA, bd);

			// Autorizzazione sui domini
			listaDominiFiltro = AclEngine.getDominiAutorizzati((Utenza) listaRendicontazioniDTO.getUser(), Servizio.RENDICONTAZIONI_E_INCASSI, Diritti.LETTURA);
			if(listaDominiFiltro == null) {
				throw new NotAuthorizedException("L'utenza autenticata ["+listaRendicontazioniDTO.getUser().getPrincipal()+"] non e' autorizzata ai servizi " + Servizio.RENDICONTAZIONI_E_INCASSI + " per alcun dominio");
			}

			FrBD rendicontazioniBD = new FrBD(bd);
			FrFilter filter = rendicontazioniBD.newFilter();

			filter.setOffset(listaRendicontazioniDTO.getOffset());
			filter.setLimit(listaRendicontazioniDTO.getLimit());
			if(listaRendicontazioniDTO.getIdDominio() != null) {
				listaDominiFiltro.add(listaRendicontazioniDTO.getIdDominio());
			}
			if(listaDominiFiltro != null && listaDominiFiltro.size() > 0) {
				filter.setCodDominio(listaDominiFiltro);
			}
			filter.setFilterSortList(listaRendicontazioniDTO.getFieldSortList());
			filter.setDatainizio(listaRendicontazioniDTO.getDataDa());
			filter.setDataFine(listaRendicontazioniDTO.getDataA()); 

			long count = rendicontazioniBD.count(filter);

			List<LeggiRendicontazioneDTOResponse> resList = new ArrayList<LeggiRendicontazioneDTOResponse>();
			if(count > 0) {
				List<Fr> findAll = rendicontazioniBD.findAll(filter);

				for (Fr fr : findAll) {
					LeggiRendicontazioneDTOResponse elem = new LeggiRendicontazioneDTOResponse();
					populateRendicontazione(fr, bd);
					elem.setFr(fr);
					resList.add(elem);
				}
			} 

			return new ListaRendicontazioniDTOResponse(count, resList);
		}finally {
			if(bd != null)
				bd.closeConnection();
		}
	}

	public LeggiRendicontazioneDTOResponse leggiRendicontazione(LeggiRendicontazioneDTO leggiRendicontazioniDTO) throws ServiceException,RendicontazioneNonTrovataException, NotAuthorizedException, NotAuthenticatedException{
		LeggiRendicontazioneDTOResponse response = new LeggiRendicontazioneDTOResponse();
		BasicBD bd = null;

		try {
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			this.autorizzaRichiesta(leggiRendicontazioniDTO.getUser(), Servizio.RENDICONTAZIONI_E_INCASSI, Diritti.LETTURA, bd);

			FrBD rendicontazioniBD = new FrBD(bd);	
			Fr flussoRendicontazione = rendicontazioniBD.getFr(leggiRendicontazioniDTO.getIdFlusso());

			// controllo che il dominio sia autorizzato
			this.autorizzaRichiesta(leggiRendicontazioniDTO.getUser(), Servizio.RENDICONTAZIONI_E_INCASSI, Diritti.LETTURA, flussoRendicontazione.getDominio(bd).getCodDominio(), null, bd);
			populateRendicontazione(flussoRendicontazione, bd);
			response.setFr(flussoRendicontazione);

		} catch (NotFoundException e) {
			throw new RendicontazioneNonTrovataException(e.getMessage(), e);
		} finally {
			if(bd != null)
				bd.closeConnection();
		}
		return response;
	}

	private Fr populateRendicontazione(Fr flussoRendicontazione, BasicBD bd)
			throws ServiceException, NotFoundException {

		List<Rendicontazione> rendicontazioni = flussoRendicontazione.getRendicontazioni(bd);

		if(rendicontazioni != null) {
			for(Rendicontazione rend: rendicontazioni) {
				Pagamento pagamento = rend.getPagamento(bd);
				if(pagamento != null) {
					pagamento.getSingoloVersamento(bd).getVersamento(bd).getApplicazione(bd);
					pagamento.getDominio(bd);
					pagamento.getRpt(bd);
				}
			}
		}
		flussoRendicontazione.getDominio(bd);
		return flussoRendicontazione;
	}


}
