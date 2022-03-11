package it.govpay.core.dao.pagamenti;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.service.context.ContextThreadLocal;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.BasicBD;
import it.govpay.bd.model.Fr;
import it.govpay.bd.pagamento.FrBD;
import it.govpay.bd.pagamento.filters.FrFilter;
import it.govpay.bd.viste.RendicontazioniBD;
import it.govpay.bd.viste.filters.RendicontazioneFilter;
import it.govpay.bd.viste.model.Rendicontazione;
import it.govpay.core.dao.anagrafica.dto.BasicFindRequestDTO;
import it.govpay.core.dao.commons.BaseDAO;
import it.govpay.core.dao.pagamenti.dto.LeggiFrDTO;
import it.govpay.core.dao.pagamenti.dto.LeggiFrDTOResponse;
import it.govpay.core.dao.pagamenti.dto.ListaFrDTO;
import it.govpay.core.dao.pagamenti.dto.ListaFrDTOResponse;
import it.govpay.core.dao.pagamenti.dto.ListaRendicontazioniDTO;
import it.govpay.core.dao.pagamenti.dto.ListaRendicontazioniDTOResponse;
import it.govpay.core.dao.pagamenti.exception.RendicontazioneNonTrovataException;
import it.govpay.core.exceptions.NotAuthenticatedException;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.core.exceptions.RequestParamException;
import it.govpay.core.exceptions.UnprocessableEntityException;

public class RendicontazioniDAO extends BaseDAO{

	public RendicontazioniDAO() {
	}

	public ListaFrDTOResponse listaFlussiRendicontazioni(ListaFrDTO listaRendicontazioniDTO) throws ServiceException, NotAuthorizedException, NotAuthenticatedException, NotFoundException{
		FrBD rendicontazioniBD = null;
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), this.useCacheData);
		
		try {
			rendicontazioniBD = new FrBD(configWrapper);
			FrFilter filter = rendicontazioniBD.newFilter();

			filter.setOffset(listaRendicontazioniDTO.getOffset());
			filter.setLimit(listaRendicontazioniDTO.getLimit());
			if(listaRendicontazioniDTO.getIdDominio() != null) {
				filter.setCodDominioFiltro(listaRendicontazioniDTO.getIdDominio());
			}
			filter.setCodDominio(listaRendicontazioniDTO.getCodDomini());
			filter.setFilterSortList(listaRendicontazioniDTO.getFieldSortList());
			filter.setDatainizio(listaRendicontazioniDTO.getDataDa());
			filter.setDataFine(listaRendicontazioniDTO.getDataA()); 
			filter.setIncassato(listaRendicontazioniDTO.getIncassato());
			filter.setCodFlusso(listaRendicontazioniDTO.getIdFlusso());
			filter.setRicercaIdFlussoCaseInsensitive(listaRendicontazioniDTO.isRicercaIdFlussoCaseInsensitive());
			filter.setDominiUOAutorizzati(listaRendicontazioniDTO.getUnitaOperative());
			filter.setStato(listaRendicontazioniDTO.getStato());
			filter.setEseguiCountConLimit(listaRendicontazioniDTO.isEseguiCountConLimit());
			filter.setObsoleto(listaRendicontazioniDTO.getObsoleto()); 
			filter.setIuv(listaRendicontazioniDTO.getIuv());
			filter.setRicercaIdFlussoCaseInsensitive(listaRendicontazioniDTO.isRicercaIdFlussoCaseInsensitive());

			Long count = null;
			
			if(listaRendicontazioniDTO.isEseguiCount()) {
				 count = rendicontazioniBD.count(filter);
			}

			List<LeggiFrDTOResponse> resList = new ArrayList<>();
			if(listaRendicontazioniDTO.isEseguiFindAll()) {
				List<Fr> findAll = rendicontazioniBD.findAllNoXml(filter);

				for (Fr fr : findAll) {
					LeggiFrDTOResponse elem = new LeggiFrDTOResponse();
					fr.getDominio(configWrapper);
					elem.setFr(fr);
					resList.add(elem);
				}
			} 

			return new ListaFrDTOResponse(count, resList);
		}finally {
			if(rendicontazioniBD != null)
				rendicontazioniBD.closeConnection();
		}
	}

	public LeggiFrDTOResponse leggiFlussoRendicontazione(LeggiFrDTO leggiRendicontazioniDTO) throws ServiceException,RendicontazioneNonTrovataException, NotAuthorizedException, NotAuthenticatedException, UnprocessableEntityException{
		LeggiFrDTOResponse response = new LeggiFrDTOResponse();
		
		
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), this.useCacheData);
		
		if(leggiRendicontazioniDTO.getAccept().toLowerCase().contains(MediaType.APPLICATION_XML)) {
			FrBD frBD = null;
			try {
				frBD = new FrBD(configWrapper);	
				
				frBD.setupConnection(configWrapper.getTransactionID());
				
				frBD.setAtomica(false);
				
				Fr flussoRendicontazione = frBD.getFr(leggiRendicontazioniDTO.getIdDominio(), leggiRendicontazioniDTO.getIdFlusso(), leggiRendicontazioniDTO.getDataOraFlusso(), leggiRendicontazioniDTO.getObsoleto());
				response.setFr(flussoRendicontazione);
				response.setDominio(flussoRendicontazione.getDominio(configWrapper));
	
			} catch (NotFoundException e) {
				throw new RendicontazioneNonTrovataException(e.getMessage(), e);
			} finally {
				if(frBD != null)
					frBD.closeConnection();
			}
		} else {
			RendicontazioniBD rendicontazioniBD = null;
			try {
				rendicontazioniBD = new RendicontazioniBD(configWrapper);	
				
				rendicontazioniBD.setupConnection(configWrapper.getTransactionID());
				
				rendicontazioniBD.setAtomica(false);
				
				List<it.govpay.bd.viste.model.Rendicontazione> findAll = rendicontazioniBD.getFr(leggiRendicontazioniDTO.getIdDominio(), leggiRendicontazioniDTO.getIdFlusso(), leggiRendicontazioniDTO.getDataOraFlusso(), leggiRendicontazioniDTO.getObsoleto());
				
				// Controllo che tutte le rendicontazioni siano di un solo flusso, altriemnti restituisco errore di risultati multipli
				
				if(findAll != null && !findAll.isEmpty()) {
					Fr flussoRendicontazione = findAll.get(0).getFr();
					
					for (Rendicontazione rendicontazione : findAll) {
						if(rendicontazione.getFr().getId().longValue() != flussoRendicontazione.getId().longValue())
							throw new UnprocessableEntityException("L'identificativoFlusso non individua univocamente un flusso di rendicontazione");
					}
					
					response.setFr(flussoRendicontazione);
					response.setDominio(flussoRendicontazione.getDominio(configWrapper));
					response.setRendicontazioni(findAll);
				} else { // flusso senza rendicontazioni
					FrBD frBD = new FrBD(rendicontazioniBD);
					frBD.setAtomica(false);
					
					Fr flussoRendicontazione = frBD.getFr(leggiRendicontazioniDTO.getIdDominio(), leggiRendicontazioniDTO.getIdFlusso(), leggiRendicontazioniDTO.getDataOraFlusso(), leggiRendicontazioniDTO.getObsoleto());
					response.setFr(flussoRendicontazione);
					response.setDominio(flussoRendicontazione.getDominio(configWrapper));
				}
			} catch (NotFoundException e) {
				throw new RendicontazioneNonTrovataException(e.getMessage(), e);
			} finally {
				if(rendicontazioniBD != null)
					rendicontazioniBD.closeConnection();
			}
		}
		return response;
	}

	public LeggiFrDTOResponse checkAutorizzazioneFlussoRendicontazione(LeggiFrDTO leggiRendicontazioniDTO) throws ServiceException,RendicontazioneNonTrovataException, NotAuthorizedException, NotAuthenticatedException{
		LeggiFrDTOResponse response = new LeggiFrDTOResponse();
		RendicontazioniBD rendicontazioniBD = null;
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), this.useCacheData);

		try {
			rendicontazioniBD = new RendicontazioniBD(configWrapper);	
			RendicontazioneFilter filter = rendicontazioniBD.newFilter();

			filter.setOffset(0);
			filter.setLimit(BasicFindRequestDTO.DEFAULT_LIMIT);

			filter.setRicercaFR(true);
//			filter.setSearchModeEquals(true);
			filter.setCodFlusso(leggiRendicontazioniDTO.getIdFlusso());
			filter.setDominiUOAutorizzati(leggiRendicontazioniDTO.getUnitaOperative());

			long count = rendicontazioniBD.count(filter);
			response.setAuthorized(count > 0);
		} finally {
			if(rendicontazioniBD != null)
				rendicontazioniBD.closeConnection();
		}
		return response;
	}

//	private Fr populateFlussoRendicontazione(Fr flussoRendicontazione, BasicBD bd) throws ServiceException, NotFoundException {
//		List<Rendicontazione> rendicontazioni = flussoRendicontazione.getRendicontazioni(bd);
//		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), this.useCacheData);
//		
//		if(rendicontazioni != null) {
//			for(Rendicontazione rend: rendicontazioni) {
//				Pagamento pagamento = rend.getPagamento(bd);
//				if(pagamento != null) {
//					this.populatePagamento(pagamento, bd, configWrapper);
//				}
//			}
//		}
//		
//		flussoRendicontazione.getDominio(configWrapper);
//		
//		return flussoRendicontazione;
//	}

//	private void populatePagamento(Pagamento pagamento, BasicBD bd, BDConfigWrapper configWrapper)
//			throws ServiceException, NotFoundException {
//		SingoloVersamento singoloVersamento = pagamento.getSingoloVersamento(bd);
//		Versamento versamento = singoloVersamento.getVersamento(bd);
//		versamento.getApplicazione(configWrapper); 
//		versamento.getUo(configWrapper);
//		versamento.getDominio(configWrapper);
//		versamento.getTipoVersamento(configWrapper);
//		versamento.getTipoVersamentoDominio(configWrapper);
//		singoloVersamento.getTributo(configWrapper);
//		singoloVersamento.getCodContabilita(configWrapper);
//		singoloVersamento.getIbanAccredito(configWrapper);
//		singoloVersamento.getIbanAppoggio(configWrapper);
//		singoloVersamento.getTipoContabilita(configWrapper);
//		pagamento.getRpt(bd);
//		pagamento.getDominio(configWrapper);
//		pagamento.getRendicontazioni(bd);
//		pagamento.getIncasso(bd);
//	}

	public ListaRendicontazioniDTOResponse listaFr(ListaRendicontazioniDTO listaRendicontazioniDTO) throws ServiceException, NotAuthorizedException, NotAuthenticatedException, NotFoundException, RequestParamException{
		FrBD rendicontazioniBD = null;
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), this.useCacheData);
		
		try {
			rendicontazioniBD = new FrBD(configWrapper);
			FrFilter filter = rendicontazioniBD.newFilter();

			filter.setOffset(listaRendicontazioniDTO.getOffset());
			filter.setLimit(listaRendicontazioniDTO.getLimit());
			if(listaRendicontazioniDTO.getIdDominio() != null) {
				filter.setCodDominioFiltro(listaRendicontazioniDTO.getIdDominio());
			}
			filter.setCodDominio(listaRendicontazioniDTO.getCodDomini());
			// richiesto ordinamento
			ListaFrDTO frDTO = new ListaFrDTO(listaRendicontazioniDTO.getUser());
			if(listaRendicontazioniDTO.isOrderEnabled()) {
				for (String nomeImpostato : listaRendicontazioniDTO.getFieldList()) {
					frDTO.setOrderBy(nomeImpostato);
				}
			} else { // default
				filter.setFilterSortList(frDTO.getFieldSortList());
			}
			filter.setDatainizio(listaRendicontazioniDTO.getDataAcquisizioneFlussoDa());
			filter.setDataFine(listaRendicontazioniDTO.getDataAcquisizioneFlussoA()); 
			filter.setIncassato(listaRendicontazioniDTO.getIncassato());
			filter.setCodFlusso(listaRendicontazioniDTO.getCodFlusso());
			filter.setRicercaIdFlussoCaseInsensitive(listaRendicontazioniDTO.isRicercaIdFlussoCaseInsensitive());
			filter.setDominiUOAutorizzati(listaRendicontazioniDTO.getUnitaOperative());
			filter.setStato(listaRendicontazioniDTO.getStato());
			filter.setEseguiCountConLimit(listaRendicontazioniDTO.isEseguiCountConLimit());
			filter.setObsoleto(listaRendicontazioniDTO.getFrObsoleto()); 
			filter.setIuv(listaRendicontazioniDTO.getIuv());
			filter.setRicercaIdFlussoCaseInsensitive(listaRendicontazioniDTO.isRicercaIdFlussoCaseInsensitive());

			Long count = null;
			
			if(listaRendicontazioniDTO.isEseguiCount()) {
				 count = rendicontazioniBD.count(filter);
			}

			List<it.govpay.bd.viste.model.Rendicontazione> resList = new ArrayList<>();
			if(listaRendicontazioniDTO.isEseguiFindAll()) {
				List<Fr> findAll = rendicontazioniBD.findAllNoXml(filter);

				for (Fr fr : findAll) {
					it.govpay.bd.viste.model.Rendicontazione elem = new it.govpay.bd.viste.model.Rendicontazione();
					fr.getDominio(configWrapper);
					elem.setFr(fr);
					resList.add(elem);
				}
			} 

			return new ListaRendicontazioniDTOResponse(count, resList);
		}finally {
			if(rendicontazioniBD != null)
				rendicontazioniBD.closeConnection();
		}
	}

	public ListaRendicontazioniDTOResponse listaRendicontazioni(ListaRendicontazioniDTO listaRendicontazioniDTO) throws ServiceException, NotAuthorizedException, NotAuthenticatedException, NotFoundException, RequestParamException{
		// la ricerca dei flussi deve utilizzare la tabella se non viene impostato il filtro per iuv.
		if(listaRendicontazioniDTO.isRicercaFR() && listaRendicontazioniDTO.getIuv() == null) {
			return this.listaFr(listaRendicontazioniDTO);
		}
		
		RendicontazioniBD rendicontazioniBD = null;
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), this.useCacheData);
		
		try {
			rendicontazioniBD = new RendicontazioniBD(configWrapper);
			
			rendicontazioniBD.setupConnection(configWrapper.getTransactionID());
			
			rendicontazioniBD.setAtomica(false);
			
			RendicontazioneFilter filter = rendicontazioniBD.newFilter();

			filter.setOffset(listaRendicontazioniDTO.getOffset());
			filter.setLimit(listaRendicontazioniDTO.getLimit());
			filter.setEseguiCountConLimit(listaRendicontazioniDTO.isEseguiCountConLimit());

			filter.setIdTipiVersamento(listaRendicontazioniDTO.getIdTipiVersamento());
			if(listaRendicontazioniDTO.getIdDominio() != null) {
				filter.setCodDominio(listaRendicontazioniDTO.getIdDominio());
			}
			filter.setStatoFlusso(listaRendicontazioniDTO.getStato());
			filter.setIncassato(listaRendicontazioniDTO.getIncassato());
			filter.setDominiUOAutorizzati(listaRendicontazioniDTO.getUnitaOperative());
			filter.setCodFlusso(listaRendicontazioniDTO.getCodFlusso());
			filter.setIuv(listaRendicontazioniDTO.getIuv());

			filter.setFilterSortList(listaRendicontazioniDTO.getFieldSortList());

			filter.setDataAcquisizioneFlussoDa(listaRendicontazioniDTO.getDataAcquisizioneFlussoDa());
			filter.setDataAcquisizioneFlussoA(listaRendicontazioniDTO.getDataAcquisizioneFlussoA()); 
			filter.setDataFlussoDa(listaRendicontazioniDTO.getDataFlussoDa());
			filter.setDataFlussoA(listaRendicontazioniDTO.getDataFlussoA()); 
			filter.setDataRendicontazioneDa(listaRendicontazioniDTO.getDataRendicontazioneDa());
			filter.setDataRendicontazioneA(listaRendicontazioniDTO.getDataRendicontazioneA());

			filter.setDirezione(listaRendicontazioniDTO.getDirezione());
			filter.setDivisione(listaRendicontazioniDTO.getDivisione());
			filter.setFrObsoleto(listaRendicontazioniDTO.getFrObsoleto());
			filter.setRicercaIdFlussoCaseInsensitive(listaRendicontazioniDTO.isRicercaIdFlussoCaseInsensitive());
			filter.setRicercaFR(listaRendicontazioniDTO.isRicercaFR());

			Long count = null;
			
			if(listaRendicontazioniDTO.isEseguiCount()) {
				 count = rendicontazioniBD.count(filter);
			}

			List<it.govpay.bd.viste.model.Rendicontazione> resList = new ArrayList<>();
			if(listaRendicontazioniDTO.isEseguiFindAll()) {
				List<it.govpay.bd.viste.model.Rendicontazione> findAll = rendicontazioniBD.findAll(filter);

				for (it.govpay.bd.viste.model.Rendicontazione rendicontazione : findAll) {
					resList.add(this.popolateRendicontazione(rendicontazione, rendicontazioniBD, configWrapper));
				}
			} 

			return new ListaRendicontazioniDTOResponse(count, resList);
		}finally {
			if(rendicontazioniBD != null)
				rendicontazioniBD.closeConnection();
		}
	}

	private it.govpay.bd.viste.model.Rendicontazione popolateRendicontazione(it.govpay.bd.viste.model.Rendicontazione rendicontazione, BasicBD bd, BDConfigWrapper configWrapper) throws ServiceException {

		if(rendicontazione.getVersamento() != null) {
			rendicontazione.getVersamento().getApplicazione(configWrapper);
			rendicontazione.getVersamento().getUo(configWrapper);
			rendicontazione.getVersamento().getDominio(configWrapper);
			rendicontazione.getVersamento().getTipoVersamento(configWrapper);
			rendicontazione.getVersamento().getTipoVersamentoDominio(configWrapper);
		}

		return rendicontazione;
	}
}
