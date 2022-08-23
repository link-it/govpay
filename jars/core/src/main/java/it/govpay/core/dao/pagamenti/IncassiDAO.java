package it.govpay.core.dao.pagamenti;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.SortOrder;
import org.openspcoop2.utils.service.context.ContextThreadLocal;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.BasicBD;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Fr;
import it.govpay.bd.model.Incasso;
import it.govpay.bd.model.Operatore;
import it.govpay.bd.model.Pagamento;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.Versamento;
import it.govpay.bd.pagamento.FrBD;
import it.govpay.bd.pagamento.IncassiBD;
import it.govpay.bd.pagamento.PagamentiBD;
import it.govpay.bd.pagamento.filters.FrFilter;
import it.govpay.bd.pagamento.filters.IncassoFilter;
import it.govpay.bd.pagamento.filters.PagamentoFilter;
import it.govpay.core.autorizzazione.AuthorizationManager;
import it.govpay.core.autorizzazione.beans.GovpayLdapUserDetails;
import it.govpay.core.autorizzazione.utils.AutorizzazioneUtils;
import it.govpay.core.business.Incassi;
import it.govpay.core.dao.commons.BaseDAO;
import it.govpay.core.dao.pagamenti.dto.LeggiIncassoDTO;
import it.govpay.core.dao.pagamenti.dto.LeggiIncassoDTOResponse;
import it.govpay.core.dao.pagamenti.dto.ListaIncassiDTO;
import it.govpay.core.dao.pagamenti.dto.ListaIncassiDTOResponse;
import it.govpay.core.dao.pagamenti.dto.RichiestaIncassoDTO;
import it.govpay.core.dao.pagamenti.dto.RichiestaIncassoDTOResponse;
import it.govpay.core.dao.pagamenti.exception.IncassoNonTrovatoException;
import it.govpay.core.exceptions.EcException;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.exceptions.IncassiException;
import it.govpay.core.exceptions.NotAuthenticatedException;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.model.Incasso.StatoIncasso;

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
			newFilter.setSct(listaIncassoDTO.getSct());
			newFilter.setCodFlusso(listaIncassoDTO.getCodFlusso());
			
			List<Incasso> findAll = new ArrayList<>();
			if(listaIncassoDTO.getIuv() != null ) {
				PagamentiBD pagamentiBD = new PagamentiBD(incassiBD);
				pagamentiBD.setAtomica(false);
				
				try {
					List<Long> idIncassoByIuv = pagamentiBD.getIdIncassoByIuv(listaIncassoDTO.getIuv());
					newFilter.setIdIncasso(idIncassoByIuv);
				} catch (NotFoundException e) {
					return new ListaIncassiDTOResponse(0L, findAll);
				}
			}

			FilterSortWrapper fsw = new FilterSortWrapper();
			fsw.setField(it.govpay.orm.Incasso.model().DATA_ORA_INCASSO);
			fsw.setSortOrder(SortOrder.DESC);
			newFilter.getFilterSortList().add(fsw);

			Long count = null;
			
			if(listaIncassoDTO.isEseguiCount()) {
				 count = incassiBD.count(newFilter);
			}
			
			
			if(listaIncassoDTO.isEseguiFindAll()) {
				findAll = incassiBD.findAll(newFilter);
			}
			
			ListaIncassiDTOResponse listaIncassiDTOResponse = new ListaIncassiDTOResponse(count, findAll);

			if(listaIncassoDTO.isIncludiPagamenti()) {
				if(listaIncassiDTOResponse.getResults() != null && listaIncassiDTOResponse.getResults().size() > 0) {
					PagamentiBD pagamentiBD = new PagamentiBD(incassiBD);
					pagamentiBD.setAtomica(false);
					
					for (Incasso incasso : listaIncassiDTOResponse.getResults()) {
						
						PagamentoFilter filter = pagamentiBD.newFilter();
						filter.setIdIncasso(incasso.getId());
						List<Pagamento> pagamenti = pagamentiBD.findAll(filter);
						
						// popolo valori
						if(pagamenti != null) {
							for(Pagamento pagamento: pagamenti) {
								pagamento.getDominio(configWrapper);
								SingoloVersamento singoloVersamento = pagamento.getSingoloVersamento(incassiBD);
								Versamento versamento = singoloVersamento.getVersamentoBD(incassiBD);
								versamento.getApplicazione(configWrapper);
								versamento.getDominio(configWrapper);
								versamento.getUo(configWrapper);
								singoloVersamento.getIbanAccredito(configWrapper);
								pagamento.getRpt(incassiBD);
								pagamento.setIncasso(incasso);
							}
						}
	
						incasso.setPagamenti(pagamenti);
						
						
						incasso.getApplicazione(configWrapper);
						incasso.getDominio(configWrapper);
					}
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

			Incasso incasso = incassiBD.getIncasso(leggiIncassoDTO.getIdDominio(), leggiIncassoDTO.getIdRiconciliazione());

			response.setIncasso(incasso);
			
			PagamentiBD pagamentiBD = new PagamentiBD(incassiBD);
			pagamentiBD.setAtomica(false);
			PagamentoFilter filter = pagamentiBD.newFilter();
			filter.setIdIncasso(response.getIncasso().getId());
			filter.setTipo(leggiIncassoDTO.getTipoRiscossioni()); 
			List<Pagamento> pagamenti = pagamentiBD.findAll(filter);
			
			if(pagamenti != null) {
				for(Pagamento pagamento: pagamenti) {
					this.populatePagamento(pagamento, incassiBD, configWrapper);
					pagamento.setIncasso(incasso);
				}
			}
			
			response.getIncasso().setPagamenti(pagamenti);

			response.getIncasso().getApplicazione(configWrapper);
			response.getIncasso().getDominio(configWrapper);

		} catch (NotFoundException e) {
			throw new IncassoNonTrovatoException(e.getMessage(), e);
		} catch (MultipleResultException e) {
			throw new ServiceException(e);
		} finally {
			if(incassiBD != null)
				incassiBD.closeConnection();
		}
		return response;
	}
	
	public RichiestaIncassoDTOResponse addRiconciliazione(RichiestaIncassoDTO richiestaIncassoDTO) throws NotAuthorizedException, GovPayException, IncassiException {
		RichiestaIncassoDTOResponse richiestaIncassoDTOResponse = new RichiestaIncassoDTOResponse();
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), this.useCacheData);
		IncassiBD incassiBD = null;
		
		try {
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
			
			// verifica sintassi / semantica incasso
			Incassi incassi = new Incassi();
			incassi.verificaRiconciliazione(richiestaIncassoDTO);
			
			incassiBD = new IncassiBD(configWrapper);
			
			incassiBD.setupConnection(configWrapper.getTransactionID());
			
			try {
				Incasso incasso = incassiBD.getIncasso(richiestaIncassoDTO.getCodDominio(), richiestaIncassoDTO.getIdRiconciliazione());
				
				StatoIncasso stato = incasso.getStato();

				// Se l'incasso si trova in stato errore allora provo ad acquisirlo nuovamente.
				if(!stato.equals(StatoIncasso.ERRORE)) {
					// informazioni sui pagamenti
					PagamentiBD pagamentiBD = new PagamentiBD(incassiBD);
					pagamentiBD.setAtomica(false);
					PagamentoFilter filter = pagamentiBD.newFilter();
					filter.setIdIncasso(incasso.getId());
					List<Pagamento> pagamenti = pagamentiBD.findAll(filter);
					
					if(pagamenti != null) {
						for(Pagamento pagamento: pagamenti) {
							try {
								this.populatePagamento(pagamento, incassiBD, configWrapper);
								pagamento.setIncasso(incasso);
							} catch (NotFoundException e) { 
	
							}
						}
					}
					
					incasso.setPagamenti(pagamenti);
	
					richiestaIncassoDTOResponse.setIncasso(incasso);
					richiestaIncassoDTOResponse.setCreated(false);
					richiestaIncassoDTOResponse.getIncasso().getApplicazione(configWrapper);
					richiestaIncassoDTOResponse.getIncasso().getOperatore(configWrapper);
					richiestaIncassoDTOResponse.getIncasso().getDominio(configWrapper);
					return richiestaIncassoDTOResponse;
				}
			} catch (NotFoundException e) {
				// Incasso non registrato.
				richiestaIncassoDTOResponse.setCreated(true);
			} catch (MultipleResultException e) {
				throw new GovPayException(e);
			}
			
			Incasso incasso = richiestaIncassoDTO.toIncassoModel();
			// incasso non registrato
			if(richiestaIncassoDTOResponse.isCreated()) {
				incassiBD.insertIncasso(incasso);
			} else {
				// incasso reiterato in caso di errore
				try {
					incassiBD.updateIncasso(incasso);
				} catch (NotFoundException e) {
				}
			}
			// Incasso non registrato o reiterato, devo aspettare l'esecuzione del batch.
			richiestaIncassoDTOResponse.setCreated(true);
			richiestaIncassoDTOResponse.setIncasso(incasso);
			richiestaIncassoDTOResponse.getIncasso().getApplicazione(configWrapper);
			richiestaIncassoDTOResponse.getIncasso().getOperatore(configWrapper);
			richiestaIncassoDTOResponse.getIncasso().getDominio(configWrapper);
			
			// avvio elaborazione riconciliazioni
			it.govpay.core.business.Operazioni.setEseguiElaborazioneRiconciliazioni();
		} catch (ServiceException e) {
			throw new GovPayException(e);
		}finally {
			if(incassiBD != null)
				incassiBD.closeConnection();
		}
		return richiestaIncassoDTOResponse;
	}

	public RichiestaIncassoDTOResponse richiestaIncasso(RichiestaIncassoDTO richiestaIncassoDTO) throws NotAuthorizedException, ServiceException, IncassiException, GovPayException, EcException{
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
			
			PagamentiBD pagamentiBD = new PagamentiBD(incassiBD);
			pagamentiBD.setAtomica(false);
			PagamentoFilter filter = pagamentiBD.newFilter();
			filter.setIdIncasso(richiestaIncassoDTOResponse.getIncasso().getId());
			List<Pagamento> pagamenti = pagamentiBD.findAll(filter);
			
			if(pagamenti != null) {
				for(Pagamento pagamento: pagamenti) {
					try {
						this.populatePagamento(pagamento, incassiBD, configWrapper);
						pagamento.setIncasso(richiestaIncassoDTOResponse.getIncasso());
					} catch (NotFoundException e) { 

					}
				}
			}
			
			FrBD frBD = new FrBD(incassiBD);
			frBD.setAtomica(false);
			FrFilter frFilter = frBD.newFilter();
			frFilter.setIdIncasso(richiestaIncassoDTOResponse.getIncasso().getId());
			List<Fr> frs = frBD.findAll(frFilter);
			if(frs.size() > 0) {
				Fr fr = frs.get(0);
				fr.getRendicontazioni(frBD);
				richiestaIncassoDTOResponse.setFr(fr);
			}
			
			richiestaIncassoDTOResponse.getIncasso().setPagamenti(pagamenti);
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
		Versamento versamento = singoloVersamento.getVersamentoBD(bd); 
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
	}
}
