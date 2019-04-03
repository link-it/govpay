package it.govpay.core.dao.pagamenti;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.model.Fr;
import it.govpay.bd.model.Pagamento;
import it.govpay.bd.model.Rendicontazione;
import it.govpay.bd.pagamento.FrBD;
import it.govpay.bd.pagamento.filters.FrFilter;
import it.govpay.core.autorizzazione.AuthorizationManager;
import it.govpay.core.dao.commons.BaseDAO;
import it.govpay.core.dao.pagamenti.dto.LeggiRendicontazioneDTO;
import it.govpay.core.dao.pagamenti.dto.LeggiRendicontazioneDTOResponse;
import it.govpay.core.dao.pagamenti.dto.ListaRendicontazioniDTO;
import it.govpay.core.dao.pagamenti.dto.ListaRendicontazioniDTOResponse;
import it.govpay.core.dao.pagamenti.exception.RendicontazioneNonTrovataException;
import it.govpay.core.exceptions.NotAuthenticatedException;
import it.govpay.core.exceptions.NotAuthorizedException;
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

			// Autorizzazione sui domini
			listaDominiFiltro = AuthorizationManager.getDominiAutorizzati(listaRendicontazioniDTO.getUser());
			if(listaDominiFiltro == null) {
				throw AuthorizationManager.toNotAuthorizedExceptionNessunDominioAutorizzato(listaRendicontazioniDTO.getUser());
			}

			FrBD rendicontazioniBD = new FrBD(bd);
			FrFilter filter = rendicontazioniBD.newFilter();

			filter.setOffset(listaRendicontazioniDTO.getOffset());
			filter.setLimit(listaRendicontazioniDTO.getLimit());
			if(listaRendicontazioniDTO.getIdDominio() != null) {
				filter.setCodDominioFiltro(listaRendicontazioniDTO.getIdDominio());
			}
			if(listaDominiFiltro != null && listaDominiFiltro.size() > 0) {
				filter.setCodDominio(listaDominiFiltro);
			}
			filter.setFilterSortList(listaRendicontazioniDTO.getFieldSortList());
			filter.setDatainizio(listaRendicontazioniDTO.getDataDa());
			filter.setDataFine(listaRendicontazioniDTO.getDataA()); 

			long count = rendicontazioniBD.count(filter);

			List<LeggiRendicontazioneDTOResponse> resList = new ArrayList<>();
			if(count > 0) {
				List<Fr> findAll = rendicontazioniBD.findAll(filter);

				for (Fr fr : findAll) {
					LeggiRendicontazioneDTOResponse elem = new LeggiRendicontazioneDTOResponse();
					try{ fr.getDominio(bd); } catch (NotFoundException e) {}
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

			FrBD rendicontazioniBD = new FrBD(bd);	
			Fr flussoRendicontazione = rendicontazioniBD.getFr(leggiRendicontazioniDTO.getIdFlusso());

			// controllo che il dominio sia autorizzato
			if(!AuthorizationManager.isDominioAuthorized(leggiRendicontazioniDTO.getUser(), flussoRendicontazione.getDominio(bd).getCodDominio())) {
				throw AuthorizationManager.toNotAuthorizedException(leggiRendicontazioniDTO.getUser(),flussoRendicontazione.getDominio(bd).getCodDominio(), null);
			}
			this.populateRendicontazione(flussoRendicontazione, bd);
			response.setFr(flussoRendicontazione);

		} catch (NotFoundException e) {
			throw new RendicontazioneNonTrovataException(e.getMessage(), e);
		} finally {
			if(bd != null)
				bd.closeConnection();
		}
		return response;
	}

	private Fr populateRendicontazione(Fr flussoRendicontazione, BasicBD bd) throws ServiceException, NotFoundException {

		List<Rendicontazione> rendicontazioni = flussoRendicontazione.getRendicontazioni(bd);

		if(rendicontazioni != null) {
			for(Rendicontazione rend: rendicontazioni) {
				Pagamento pagamento = rend.getPagamento(bd);
				if(pagamento != null) {
					pagamento.getSingoloVersamento(bd).getVersamento(bd).getApplicazione(bd);
					pagamento.getDominio(bd);
					pagamento.getRpt(bd);
					pagamento.getIncasso(bd);
				}
			}
		}
		try {
			flussoRendicontazione.getDominio(bd);
		} catch (NotFoundException e) {
			
		}
		return flussoRendicontazione;
	}
}
