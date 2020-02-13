package it.govpay.core.dao.pagamenti;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.service.context.ContextThreadLocal;

import it.govpay.bd.BasicBD;
import it.govpay.bd.model.Fr;
import it.govpay.bd.model.IdUnitaOperativa;
import it.govpay.bd.model.Pagamento;
import it.govpay.bd.model.Rendicontazione;
import it.govpay.bd.pagamento.FrBD;
import it.govpay.bd.pagamento.filters.FrFilter;
import it.govpay.bd.viste.RendicontazioniBD;
import it.govpay.bd.viste.filters.RendicontazioneFilter;
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

public class RendicontazioniDAO extends BaseDAO{

	public RendicontazioniDAO() {
	}

	public ListaFrDTOResponse listaFlussiRendicontazioni(ListaFrDTO listaRendicontazioniDTO) throws ServiceException, NotAuthorizedException, NotAuthenticatedException, NotFoundException{
		BasicBD bd = null;

		try {
			bd = BasicBD.newInstance(ContextThreadLocal.get().getTransactionId());

			FrBD rendicontazioniBD = new FrBD(bd);
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
			filter.setDominiUOAutorizzati(listaRendicontazioniDTO.getUnitaOperative());
			filter.setStato(listaRendicontazioniDTO.getStato());

			long count = rendicontazioniBD.count(filter);

			List<LeggiFrDTOResponse> resList = new ArrayList<>();
			if(count > 0) {
				List<Fr> findAll = rendicontazioniBD.findAll(filter);

				for (Fr fr : findAll) {
					LeggiFrDTOResponse elem = new LeggiFrDTOResponse();
					try{ fr.getDominio(bd); } catch (NotFoundException e) {}
					elem.setFr(fr);
					resList.add(elem);
				}
			} 

			return new ListaFrDTOResponse(count, resList);
		}finally {
			if(bd != null)
				bd.closeConnection();
		}
	}

	public LeggiFrDTOResponse leggiFlussoRendicontazione(LeggiFrDTO leggiRendicontazioniDTO) throws ServiceException,RendicontazioneNonTrovataException, NotAuthorizedException, NotAuthenticatedException{
		LeggiFrDTOResponse response = new LeggiFrDTOResponse();
		BasicBD bd = null;

		try {
			bd = BasicBD.newInstance(ContextThreadLocal.get().getTransactionId());

			FrBD rendicontazioniBD = new FrBD(bd);	
			Fr flussoRendicontazione = rendicontazioniBD.getFr(leggiRendicontazioniDTO.getIdFlusso());

			this.populateFlussoRendicontazione(flussoRendicontazione, bd);
			flussoRendicontazione.getIncasso(bd);
			response.setFr(flussoRendicontazione);
			response.setDominio(flussoRendicontazione.getDominio(bd));

		} catch (NotFoundException e) {
			throw new RendicontazioneNonTrovataException(e.getMessage(), e);
		} finally {
			if(bd != null)
				bd.closeConnection();
		}
		return response;
	}

	public LeggiFrDTOResponse checkAutorizzazioneFlussoRendicontazione(LeggiFrDTO leggiRendicontazioniDTO) throws ServiceException,RendicontazioneNonTrovataException, NotAuthorizedException, NotAuthenticatedException{
		LeggiFrDTOResponse response = new LeggiFrDTOResponse();
		BasicBD bd = null;

		try {
			bd = BasicBD.newInstance(ContextThreadLocal.get().getTransactionId());

			FrBD rendicontazioniBD = new FrBD(bd);	
			FrFilter filter = rendicontazioniBD.newFilter();

			filter.setOffset(0);
			filter.setLimit(BasicFindRequestDTO.DEFAULT_LIMIT);

			filter.setSearchModeEquals(true);
			filter.setCodFlusso(leggiRendicontazioniDTO.getIdFlusso());
			filter.setDominiUOAutorizzati(leggiRendicontazioniDTO.getUnitaOperative());

			long count = rendicontazioniBD.count(filter);
			response.setAuthorized(count > 0);
		} finally {
			if(bd != null)
				bd.closeConnection();
		}
		return response;
	}

	private Fr populateFlussoRendicontazione(Fr flussoRendicontazione, BasicBD bd) throws ServiceException, NotFoundException {

		List<Rendicontazione> rendicontazioni = flussoRendicontazione.getRendicontazioni(bd);

		if(rendicontazioni != null) {
			for(Rendicontazione rend: rendicontazioni) {
				Pagamento pagamento = rend.getPagamento(bd);
				if(pagamento != null) {
					this.populatePagamento(pagamento, bd);
				}
			}
		}
		try {
			flussoRendicontazione.getDominio(bd);
		} catch (NotFoundException e) {

		}
		return flussoRendicontazione;
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


	public ListaRendicontazioniDTOResponse listaRendicontazioni(ListaRendicontazioniDTO listaRendicontazioniDTO) throws ServiceException, NotAuthorizedException, NotAuthenticatedException, NotFoundException{
		BasicBD bd = null;

		try {
			bd = BasicBD.newInstance(ContextThreadLocal.get().getTransactionId());

			RendicontazioniBD rendicontazioniBD = new RendicontazioniBD(bd);
			RendicontazioneFilter filter = rendicontazioniBD.newFilter();

			filter.setOffset(listaRendicontazioniDTO.getOffset());
			filter.setLimit(listaRendicontazioniDTO.getLimit());

			filter.setIdDomini(listaRendicontazioniDTO.getIdDomini());
			filter.setIdTipiVersamento(listaRendicontazioniDTO.getIdTipiVersamento());

			if(listaRendicontazioniDTO.getUnitaOperative() != null) {
				List<Long> idDomini = new ArrayList<>();
				List<Long> idUO = new ArrayList<>();
				for (IdUnitaOperativa uo : listaRendicontazioniDTO.getUnitaOperative()) {
					if(uo.getIdDominio() != null && !idDomini.contains(uo.getIdDominio())) {
						idDomini.add(uo.getIdDominio());
					}

					if(uo.getIdUnita() != null) {
						idUO.add(uo.getIdUnita());
					}
				}
				filter.setIdDomini(idDomini);
				filter.setIdUo(idUO);
			}

			filter.setCodFlusso(listaRendicontazioniDTO.getCodFlusso());
			filter.setIuv(listaRendicontazioniDTO.getIuv());

			filter.setFilterSortList(listaRendicontazioniDTO.getFieldSortList());

			filter.setDataFlussoDa(listaRendicontazioniDTO.getDataFlussoDa());
			filter.setDataFlussoDa(listaRendicontazioniDTO.getDataFlussoA()); 
			filter.setDataRendicontazioneDa(listaRendicontazioniDTO.getDataRendicontazioneDa());
			filter.setDataRendicontazioneA(listaRendicontazioniDTO.getDataRendicontazioneA());

			filter.setDirezione(listaRendicontazioniDTO.getDirezione());
			filter.setDivisione(listaRendicontazioniDTO.getDivisione());

			long count = rendicontazioniBD.count(filter);

			List<it.govpay.bd.viste.model.Rendicontazione> resList = new ArrayList<>();
			if(count > 0) {
				List<it.govpay.bd.viste.model.Rendicontazione> findAll = rendicontazioniBD.findAll(filter);

				for (it.govpay.bd.viste.model.Rendicontazione rendicontazione : findAll) {
					resList.add(this.popolateRendicontazione(rendicontazione, bd));
				}
			} 

			return new ListaRendicontazioniDTOResponse(count, resList);
		}finally {
			if(bd != null)
				bd.closeConnection();
		}
	}

	private it.govpay.bd.viste.model.Rendicontazione popolateRendicontazione(it.govpay.bd.viste.model.Rendicontazione rendicontazione, BasicBD bd) throws ServiceException {

		rendicontazione.getVersamento().getApplicazione(bd);
		rendicontazione.getVersamento().getUo(bd);
		rendicontazione.getVersamento().getDominio(bd);
		rendicontazione.getVersamento().getTipoVersamento(bd);
		rendicontazione.getVersamento().getTipoVersamentoDominio(bd);


		return rendicontazione;
	}
}
