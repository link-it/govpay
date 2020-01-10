package it.govpay.core.dao.pagamenti;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.service.context.ContextThreadLocal;

import it.govpay.bd.BasicBD;
import it.govpay.bd.model.Fr;
import it.govpay.bd.model.Pagamento;
import it.govpay.bd.model.Rendicontazione;
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

public class RendicontazioniDAO extends BaseDAO{

	public RendicontazioniDAO() {
	}

	public ListaRendicontazioniDTOResponse listaRendicontazioni(ListaRendicontazioniDTO listaRendicontazioniDTO) throws ServiceException, NotAuthorizedException, NotAuthenticatedException, NotFoundException{
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
			if(listaRendicontazioniDTO.getStato() != null)
				filter.setStato(listaRendicontazioniDTO.getStato().toString());
			
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
			bd = BasicBD.newInstance(ContextThreadLocal.get().getTransactionId());

			FrBD rendicontazioniBD = new FrBD(bd);	
			Fr flussoRendicontazione = rendicontazioniBD.getFr(leggiRendicontazioniDTO.getIdFlusso());

			this.populateRendicontazione(flussoRendicontazione, bd);
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

	private Fr populateRendicontazione(Fr flussoRendicontazione, BasicBD bd) throws ServiceException, NotFoundException {

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
}
