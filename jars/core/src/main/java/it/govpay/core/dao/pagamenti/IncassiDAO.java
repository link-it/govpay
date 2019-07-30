package it.govpay.core.dao.pagamenti;

import java.util.List;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.SortOrder;
import org.openspcoop2.utils.service.context.ContextThreadLocal;

import it.govpay.bd.BasicBD;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Incasso;
import it.govpay.bd.model.Operatore;
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
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.exceptions.IncassiException;
import it.govpay.core.exceptions.NotAuthenticatedException;
import it.govpay.core.exceptions.NotAuthorizedException;

public class IncassiDAO extends BaseDAO{

	public ListaIncassiDTOResponse listaIncassi(ListaIncassiDTO listaIncassoDTO) throws NotAuthenticatedException, NotAuthorizedException, ServiceException{
		BasicBD bd = null;

		try {
			bd = BasicBD.newInstance(ContextThreadLocal.get().getTransactionId());

			IncassiBD incassiBD = new IncassiBD(bd);
			IncassoFilter newFilter = incassiBD.newFilter();

			if(listaIncassoDTO.getIdDominio() != null) {
				newFilter.setCodDominio(listaIncassoDTO.getIdDominio());
			}
			newFilter.setCodDomini(listaIncassoDTO.getCodDomini()); 
			newFilter.setDataInizio(listaIncassoDTO.getDataDa());
			newFilter.setDataFine(listaIncassoDTO.getDataA());
			newFilter.setOffset(listaIncassoDTO.getOffset());
			newFilter.setLimit(listaIncassoDTO.getLimit());
			newFilter.setCodApplicazione(listaIncassoDTO.getIdA2A());

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
							pagamento.getDominio(bd);
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
			bd = BasicBD.newInstance(ContextThreadLocal.get().getTransactionId());

			IncassiBD incassiBD = new IncassiBD(bd);
			Incasso incasso = incassiBD.getIncasso(leggiIncassoDTO.getIdDominio(), leggiIncassoDTO.getIdIncasso());

			response.setIncasso(incasso);
			List<Pagamento> pagamenti = response.getIncasso().getPagamenti(bd);

			if(pagamenti != null) {
				for(Pagamento pagamento: pagamenti) {
					this.populatePagamento(pagamento, bd);
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

	public RichiestaIncassoDTOResponse richiestaIncasso(RichiestaIncassoDTO richiestaIncassoDTO) throws NotAuthorizedException, ServiceException, IncassiException, GovPayException{
		RichiestaIncassoDTOResponse richiestaIncassoDTOResponse = new RichiestaIncassoDTOResponse();
		BasicBD bd = null;

		try {
			bd = BasicBD.newInstance(ContextThreadLocal.get().getTransactionId());
			it.govpay.core.business.Incassi incassi = new it.govpay.core.business.Incassi(bd);
			
			if(!AuthorizationManager.isDominioAuthorized(richiestaIncassoDTO.getUser(), richiestaIncassoDTO.getCodDominio())) {
				throw AuthorizationManager.toNotAuthorizedException(richiestaIncassoDTO.getUser(), richiestaIncassoDTO.getCodDominio(), null);
			}
			
			GovpayLdapUserDetails authenticationDetails = AutorizzazioneUtils.getAuthenticationDetails(richiestaIncassoDTO.getUser());
			Applicazione applicazione = authenticationDetails.getApplicazione();
			Operatore operatore = authenticationDetails.getOperatore();
			boolean isApp = true, isOp = true;
			if(applicazione == null) {
				isApp = false;
			} 
			
			if(operatore == null) {
				isOp = false;
			} 
			
			if(!isApp && !isOp){
				throw new NotAuthorizedException("L'utenza autenticata non e' registrata nel sistema.");
			}
			
			richiestaIncassoDTO.setApplicazione(applicazione);
			richiestaIncassoDTO.setOperatore(operatore);

			richiestaIncassoDTOResponse = incassi.richiestaIncasso(richiestaIncassoDTO);
			List<Pagamento> pagamenti = richiestaIncassoDTOResponse.getIncasso().getPagamenti(bd);

			if(pagamenti != null) {
				for(Pagamento pagamento: pagamenti) {
					try {
						this.populatePagamento(pagamento, bd);
					} catch (NotFoundException e) { 
						
					}
				}
			}

			richiestaIncassoDTOResponse.getIncasso().getApplicazione(bd);
			richiestaIncassoDTOResponse.getIncasso().getOperatore(bd);
			richiestaIncassoDTOResponse.getIncasso().getDominio(bd);
		}finally {
			if(bd != null)
				bd.closeConnection();
		}
		return richiestaIncassoDTOResponse;
	}
	
	private void populatePagamento(Pagamento pagamento, BasicBD bd)
			throws ServiceException, NotFoundException {
		pagamento.getSingoloVersamento(bd).getVersamento(bd).getApplicazione(bd);
		pagamento.getSingoloVersamento(bd).getVersamento(bd).getUo(bd);
		pagamento.getSingoloVersamento(bd).getVersamento(bd).getDominio(bd);
		pagamento.getSingoloVersamento(bd).getVersamento(bd).getTipoVersamento(bd);
		pagamento.getSingoloVersamento(bd).getVersamento(bd).getTipoVersamentoDominio(bd);
		pagamento.getSingoloVersamento(bd).getTributo(bd);
		pagamento.getSingoloVersamento(bd).getCodContabilita(bd);
		pagamento.getSingoloVersamento(bd).getIbanAccredito(bd);
		pagamento.getSingoloVersamento(bd).getIbanAppoggio(bd);
		pagamento.getSingoloVersamento(bd).getTipoContabilita(bd);
		pagamento.getRpt(bd);
		pagamento.getDominio(bd);
		pagamento.getRendicontazioni(bd);
		pagamento.getIncasso(bd);
	}
}
