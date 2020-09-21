package it.govpay.core.dao.pagamenti;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.SortOrder;
import org.openspcoop2.utils.service.context.ContextThreadLocal;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.BasicBD;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Incasso;
import it.govpay.bd.model.Operatore;
import it.govpay.bd.model.Pagamento;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.Versamento;
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
		IncassiBD incassiBD = null;
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), this.useCacheData);
		try {
			incassiBD = new IncassiBD(configWrapper);

			incassiBD.setupConnection(configWrapper.getTransactionID());

			incassiBD.setAtomica(false);

			IncassoFilter newFilter = incassiBD.newFilter();
			
			newFilter.setEseguiCountConLimit(listaIncassoDTO.isEseguiCountConLimit());

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

			Long count = null;
			
			if(listaIncassoDTO.isEseguiCount()) {
				 count = incassiBD.count(newFilter);
			}
			
			List<Incasso> findAll = new ArrayList<>();
			if(listaIncassoDTO.isEseguiFindAll()) {
				findAll = incassiBD.findAll(newFilter);
			}
			
			ListaIncassiDTOResponse listaIncassiDTOResponse = new ListaIncassiDTOResponse(count, findAll);

			if(listaIncassiDTOResponse.getResults() != null && listaIncassiDTOResponse.getResults().size() > 0) {
				for (Incasso incasso : listaIncassiDTOResponse.getResults()) {
					// popolo valori
					List<Pagamento> pagamenti = incasso.getPagamenti(incassiBD);

					if(pagamenti != null) {
						for(Pagamento pagamento: pagamenti) {
							pagamento.getDominio(configWrapper);
							SingoloVersamento singoloVersamento = pagamento.getSingoloVersamento(incassiBD);
							Versamento versamento = singoloVersamento.getVersamento(incassiBD);
							versamento.getApplicazione(configWrapper);
							versamento.getDominio(configWrapper);
							versamento.getUo(configWrapper);
							singoloVersamento.getIbanAccredito(configWrapper);
							pagamento.getRpt(incassiBD);
							pagamento.getIncasso(incassiBD);
						}
					}

					incasso.getApplicazione(configWrapper);
					incasso.getDominio(configWrapper);
				}
			}

			return listaIncassiDTOResponse;
		} finally {
			if(incassiBD != null)
				incassiBD.closeConnection();
		}
	} 

	public LeggiIncassoDTOResponse leggiIncasso(LeggiIncassoDTO leggiIncassoDTO) throws IncassoNonTrovatoException, NotAuthorizedException, ServiceException, NotAuthenticatedException{
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), this.useCacheData);
		LeggiIncassoDTOResponse response = new LeggiIncassoDTOResponse();

		IncassiBD incassiBD = null;

		try {
			incassiBD = new IncassiBD(configWrapper);

			incassiBD.setupConnection(configWrapper.getTransactionID());

			incassiBD.setAtomica(false);

			Incasso incasso = incassiBD.getIncasso(leggiIncassoDTO.getIdDominio(), leggiIncassoDTO.getIdIncasso());

			response.setIncasso(incasso);
			List<Pagamento> pagamenti = response.getIncasso().getPagamenti(incassiBD);

			if(pagamenti != null) {
				for(Pagamento pagamento: pagamenti) {
					this.populatePagamento(pagamento, incassiBD, configWrapper);
				}
			}

			response.getIncasso().getApplicazione(configWrapper);
			response.getIncasso().getDominio(configWrapper);

		} catch (NotFoundException e) {
			throw new IncassoNonTrovatoException(e.getMessage(), e);
		} finally {
			if(incassiBD != null)
				incassiBD.closeConnection();
		}
		return response;
	}

	public RichiestaIncassoDTOResponse richiestaIncasso(RichiestaIncassoDTO richiestaIncassoDTO) throws NotAuthorizedException, ServiceException, IncassiException, GovPayException{
		RichiestaIncassoDTOResponse richiestaIncassoDTOResponse = new RichiestaIncassoDTOResponse();
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), this.useCacheData);
		IncassiBD incassiBD = null;

		try {
			it.govpay.core.business.Incassi incassi = new it.govpay.core.business.Incassi();

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
			
			incassiBD = new IncassiBD(configWrapper);
			
			incassiBD.setupConnection(configWrapper.getTransactionID());
			
			incassiBD.setAtomica(false); 
			
			List<Pagamento> pagamenti = richiestaIncassoDTOResponse.getIncasso().getPagamenti(incassiBD);

			if(pagamenti != null) {
				for(Pagamento pagamento: pagamenti) {
					try {
						this.populatePagamento(pagamento, incassiBD, configWrapper);
					} catch (NotFoundException e) { 

					}
				}
			}

			richiestaIncassoDTOResponse.getIncasso().getApplicazione(configWrapper);
			richiestaIncassoDTOResponse.getIncasso().getOperatore(configWrapper);
			richiestaIncassoDTOResponse.getIncasso().getDominio(configWrapper);
		}finally {
			if(incassiBD != null)
				incassiBD.closeConnection();
		}
		return richiestaIncassoDTOResponse;
	}

	private void populatePagamento(Pagamento pagamento, BasicBD bd, BDConfigWrapper configWrapper)
			throws ServiceException, NotFoundException {
		SingoloVersamento singoloVersamento = pagamento.getSingoloVersamento(bd); 
		Versamento versamento = singoloVersamento.getVersamento(bd); 
		versamento.getApplicazione(configWrapper);
		versamento.getUo(configWrapper);
		versamento.getDominio(configWrapper);
		versamento.getTipoVersamento(configWrapper);
		versamento.getTipoVersamentoDominio(configWrapper);
		singoloVersamento.getTributo(configWrapper);
		singoloVersamento.getCodContabilita(configWrapper);
		singoloVersamento.getIbanAccredito(configWrapper);
		singoloVersamento.getIbanAppoggio(configWrapper);
		singoloVersamento.getTipoContabilita(configWrapper);
		pagamento.getRpt(bd);
		pagamento.getDominio(configWrapper);
		pagamento.getRendicontazioni(bd);
		pagamento.getIncasso(bd);
	}
}
