package it.govpay.core.dao.pagamenti;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.SortOrder;

import it.govpay.bd.BasicBD;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Incasso;
import it.govpay.bd.model.Pagamento;
import it.govpay.bd.pagamento.IncassiBD;
import it.govpay.bd.pagamento.filters.IncassoFilter;
import it.govpay.core.autorizzazione.AuthorizationManager;
import it.govpay.core.autorizzazione.beans.GovpayLdapUserDetails;
import it.govpay.core.autorizzazione.utils.AutorizzazioneUtils;
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

	public ListaIncassiDTOResponse listaIncassi(ListaIncassiDTO listaIncassoDTO) throws NotAuthenticatedException, NotAuthorizedException, ServiceException{
		BasicBD bd = null;

		try {
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			this.autorizzaRichiesta(listaIncassoDTO.getUser(), Servizio.RENDICONTAZIONI_E_INCASSI, Diritti.LETTURA);

			List<String> domini = null;
			domini = AuthorizationManager.getDominiAutorizzati(listaIncassoDTO.getUser(), Servizio.RENDICONTAZIONI_E_INCASSI, Diritti.LETTURA); 
			if(domini == null) {
				throw AuthorizationManager.toNotAuthorizedExceptionNessunDominioAutorizzato(listaIncassoDTO.getUser(), Servizio.RENDICONTAZIONI_E_INCASSI, Diritti.LETTURA);
			}

			IncassiBD incassiBD = new IncassiBD(bd);
			IncassoFilter newFilter = incassiBD.newFilter();
			if(domini != null)
				newFilter.setCodDomini(new ArrayList<>(domini));
			newFilter.setDataInizio(listaIncassoDTO.getInizio());
			newFilter.setDataFine(listaIncassoDTO.getFine());
			newFilter.setOffset(listaIncassoDTO.getOffset());
			newFilter.setLimit(listaIncassoDTO.getLimit());

			FilterSortWrapper fsw = new FilterSortWrapper();
			fsw.setField(it.govpay.orm.Incasso.model().DATA_ORA_INCASSO);
			fsw.setSortOrder(SortOrder.DESC);
			newFilter.getFilterSortList().add(fsw);

			List<Incasso> findAll = incassiBD.findAll(newFilter);
			long count = incassiBD.count(newFilter);

			ListaIncassiDTOResponse listaIncassiDTOResponse = new ListaIncassiDTOResponse(count, findAll);

			if(listaIncassiDTOResponse.getResults() != null && listaIncassiDTOResponse.getResults().size() > 0) {
				for (Incasso incasso : listaIncassiDTOResponse.getResults()) {
					// popolo valori
					List<Pagamento> pagamenti = incasso.getPagamenti(bd);

					if(pagamenti != null) {
						for(Pagamento pagamento: pagamenti) {
							try { pagamento.getDominio(bd); } catch (NotFoundException e) {	}
							pagamento.getSingoloVersamento(bd).getVersamento(bd).getApplicazione(bd);
							pagamento.getSingoloVersamento(bd).getVersamento(bd).getDominio(bd);
							pagamento.getSingoloVersamento(bd).getVersamento(bd).getUo(bd);
							pagamento.getSingoloVersamento(bd).getIbanAccredito(bd);
							pagamento.getRpt(bd);
							pagamento.getIncasso(bd);
						}
					}

					incasso.getApplicazione(bd);
					incasso.getDominio(bd);
				}
			}

			return listaIncassiDTOResponse;
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

			IncassiBD incassiBD = new IncassiBD(bd);
			List<Diritti> diritti = new ArrayList<>();
			diritti.add(Diritti.LETTURA);

			boolean isAuthorized = AuthorizationManager.isAuthorized(leggiIncassoDTO.getUser(), Servizio.RENDICONTAZIONI_E_INCASSI, leggiIncassoDTO.getIdDominio(), null, diritti);
			if(!isAuthorized) {
				throw AuthorizationManager.toNotAuthorizedException(leggiIncassoDTO.getUser(), Servizio.RENDICONTAZIONI_E_INCASSI, diritti, false, leggiIncassoDTO.getIdDominio(), null);
			}
			Incasso incasso = incassiBD.getIncasso(leggiIncassoDTO.getIdDominio(), leggiIncassoDTO.getIdIncasso());

			response.setIncasso(incasso);
			List<Pagamento> pagamenti = response.getIncasso().getPagamenti(bd);

			if(pagamenti != null) {
				for(Pagamento pagamento: pagamenti) {
					pagamento.getSingoloVersamento(bd).getVersamento(bd).getApplicazione(bd);
					pagamento.getSingoloVersamento(bd).getVersamento(bd).getDominio(bd);
					pagamento.getSingoloVersamento(bd).getVersamento(bd).getUo(bd);
					pagamento.getSingoloVersamento(bd).getIbanAccredito(bd);
					pagamento.getDominio(bd);
					pagamento.getRpt(bd);
					pagamento.getIncasso(bd);
				}
			}

			response.getIncasso().getApplicazione(bd);
			response.getIncasso().getDominio(bd);

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
			GovpayLdapUserDetails authenticationDetails = AutorizzazioneUtils.getAuthenticationDetails(richiestaIncassoDTO.getUser());
			Applicazione applicazione = authenticationDetails.getApplicazione();
			if(applicazione == null)
				throw new NotFoundException("Applicazione non riconosciuta");
			richiestaIncassoDTO.setApplicazione(applicazione);

			richiestaIncassoDTOResponse = incassi.richiestaIncasso(richiestaIncassoDTO);
			List<Pagamento> pagamenti = richiestaIncassoDTOResponse.getIncasso().getPagamenti(bd);

			if(pagamenti != null) {
				for(Pagamento pagamento: pagamenti) {
					pagamento.getSingoloVersamento(bd).getVersamento(bd).getApplicazione(bd);
					pagamento.getSingoloVersamento(bd).getVersamento(bd).getDominio(bd);
					pagamento.getSingoloVersamento(bd).getVersamento(bd).getUo(bd);
					pagamento.getSingoloVersamento(bd).getIbanAccredito(bd);
					pagamento.getDominio(bd);
					pagamento.getRpt(bd);
					pagamento.getIncasso(bd);
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
