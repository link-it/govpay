package it.govpay.core.dao.pagamenti;

import java.util.List;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Incasso;
import it.govpay.bd.model.Pagamento;
import it.govpay.core.dao.commons.BaseDAO;
import it.govpay.core.dao.pagamenti.dto.LeggiIncassoDTO;
import it.govpay.core.dao.pagamenti.dto.LeggiIncassoDTOResponse;
import it.govpay.core.dao.pagamenti.dto.ListaIncassiDTO;
import it.govpay.core.dao.pagamenti.dto.ListaIncassiDTOResponse;
import it.govpay.core.dao.pagamenti.dto.RichiestaIncassoDTO;
import it.govpay.core.dao.pagamenti.dto.RichiestaIncassoDTOResponse;
import it.govpay.core.dao.pagamenti.exception.IncassoNonTrovatoException;
import it.govpay.core.exceptions.IncassiException;
import it.govpay.core.exceptions.NotAuthenticatedException;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.model.Acl.Diritti;
import it.govpay.model.Acl.Servizio;

public class IncassiDAO extends BaseDAO{

	public ListaIncassiDTOResponse listaIncassi(ListaIncassiDTO listaIncassoDTO) throws NotAuthorizedException, ServiceException{
		BasicBD bd = null;

		try {
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			this.autorizzaRichiesta(listaIncassoDTO.getUser(), Servizio.RENDICONTAZIONI_E_INCASSI, Diritti.LETTURA);
			it.govpay.core.business.Incassi incassi = new it.govpay.core.business.Incassi(bd);
			ListaIncassiDTOResponse listaIncassiDTOResponse = incassi.listaIncassi(listaIncassoDTO);

			if(listaIncassiDTOResponse.getResults() != null && listaIncassiDTOResponse.getResults().size() > 0) {
				for (Incasso incasso : listaIncassiDTOResponse.getResults()) {
					// popolo valori
					List<Pagamento> pagamenti = incasso.getPagamenti(bd);

					if(pagamenti != null) {
						for(Pagamento pagamento: pagamenti) {
							pagamento.getSingoloVersamento(bd).getVersamento(bd).getApplicazione(bd);
							pagamento.getRpt(bd);
						}
					}

					incasso.getApplicazione(bd);
					incasso.getDominio(bd);
				}
			}

			return listaIncassiDTOResponse;
		}catch (NotAuthorizedException e) {
			// TODO
			throw e;
		} catch (Exception e) {
			// TODO
			throw new ServiceException(e);
		} finally {
			if(bd != null)
				bd.closeConnection();
		}
	}

	public LeggiIncassoDTOResponse leggiIncasso(LeggiIncassoDTO leggiIncassoDTO) throws IncassoNonTrovatoException, NotAuthorizedException, ServiceException, NotAuthenticatedException{

		LeggiIncassoDTOResponse response = new LeggiIncassoDTOResponse();

		BasicBD bd = null;

		try {
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			this.autorizzaRichiesta(leggiIncassoDTO.getUser(), Servizio.RENDICONTAZIONI_E_INCASSI, Diritti.LETTURA);
			
			it.govpay.core.business.Incassi incassi = new it.govpay.core.business.Incassi(bd);
			response = incassi.leggiIncasso(leggiIncassoDTO);
			List<Pagamento> pagamenti = response.getIncasso().getPagamenti(bd);

			if(pagamenti != null) {
				for(Pagamento pagamento: pagamenti) {
					pagamento.getSingoloVersamento(bd).getVersamento(bd).getApplicazione(bd);
					pagamento.getRpt(bd);
				}
			}

			response.getIncasso().getApplicazione(bd);
			response.getIncasso().getDominio(bd);

		}catch (NotAuthorizedException e) {
			// TODO
			throw e;
		} catch (NotFoundException e) {
			throw new IncassoNonTrovatoException(e.getMessage(), e);
		} finally {
			if(bd != null)
				bd.closeConnection();
		}
		return response;
	}

	public RichiestaIncassoDTOResponse richiestaIncasso(RichiestaIncassoDTO richiestaIncassoDTO) throws NotAuthorizedException, ServiceException, IncassiException{
		RichiestaIncassoDTOResponse richiestaIncassoDTOResponse = new RichiestaIncassoDTOResponse();
		BasicBD bd = null;

		try {
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			this.autorizzaRichiesta(richiestaIncassoDTO.getUser(), Servizio.RENDICONTAZIONI_E_INCASSI, Diritti.SCRITTURA);
			it.govpay.core.business.Incassi incassi = new it.govpay.core.business.Incassi(bd);
			Applicazione applicazione = this.getApplicazioneFromUser(richiestaIncassoDTO.getUser(), bd); 
			richiestaIncassoDTO.setApplicazione(applicazione);

			richiestaIncassoDTOResponse = incassi.richiestaIncasso(richiestaIncassoDTO);
			List<Pagamento> pagamenti = richiestaIncassoDTOResponse.getIncasso().getPagamenti(bd);

			if(pagamenti != null) {
				for(Pagamento pagamento: pagamenti) {
					pagamento.getSingoloVersamento(bd).getVersamento(bd).getApplicazione(bd);
					pagamento.getRpt(bd);
				}
			}

			richiestaIncassoDTOResponse.getIncasso().getApplicazione(bd);
			richiestaIncassoDTOResponse.getIncasso().getDominio(bd);
		} catch (NotAuthorizedException e) {
			// TODO
			throw e;
		} catch (IncassiException e) {
			throw e;
			// TODO
			//						throw new ServiceException(e);
			//			Errore errore = new Errore(e.getCode(),e.getMessage(),e.getDetails());
		} catch (Exception e) {
			// TODO
			throw new ServiceException(e);
		}finally {
			if(bd != null)
				bd.closeConnection();
		}
		return richiestaIncassoDTOResponse;
	}
}
