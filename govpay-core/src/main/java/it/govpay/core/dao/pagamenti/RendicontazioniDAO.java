package it.govpay.core.dao.pagamenti;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.model.Fr;
import it.govpay.bd.pagamento.FrBD;
import it.govpay.bd.pagamento.filters.FrFilter;
import it.govpay.core.dao.pagamenti.dto.LeggiRendicontazioneDTO;
import it.govpay.core.dao.pagamenti.dto.LeggiRendicontazioneDTOResponse;
import it.govpay.core.dao.pagamenti.dto.ListaRendicontazioniDTO;
import it.govpay.core.dao.pagamenti.dto.ListaRendicontazioniDTOResponse;
import it.govpay.core.dao.pagamenti.exception.RendicontazioneNonTrovataException;
import it.govpay.core.utils.GpThreadLocal;

public class RendicontazioniDAO{

	public RendicontazioniDAO() {
	}

	public ListaRendicontazioniDTOResponse listaRendicontazioni(ListaRendicontazioniDTO listaRendicontazioniDTO) throws ServiceException{
		BasicBD bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());

		try {
			FrBD rendicontazioniBD = new FrBD(bd);
			FrFilter filter = rendicontazioniBD.newFilter();

			filter.setOffset(listaRendicontazioniDTO.getOffset());
			filter.setLimit(listaRendicontazioniDTO.getLimit());
			filter.setCodDominio(Arrays.asList(listaRendicontazioniDTO.getIdDominio()));
			filter.setFilterSortList(listaRendicontazioniDTO.getFieldSortList());

			long count = rendicontazioniBD.count(filter);

			List<LeggiRendicontazioneDTOResponse> resList = new ArrayList<LeggiRendicontazioneDTOResponse>();
			if(count > 0) {
				List<Fr> findAll = rendicontazioniBD.findAll(filter);

				for (Fr fr : findAll) {
					LeggiRendicontazioneDTOResponse elem = new LeggiRendicontazioneDTOResponse();
					elem.setFr(fr);
					elem.setRendicontazioni(fr.getRendicontazioni(bd));
					resList.add(elem);
				}
			} 

			return new ListaRendicontazioniDTOResponse(count, resList);
		}finally {
			bd.closeConnection();
		}
	}

	public LeggiRendicontazioneDTOResponse leggiRendicontazione(LeggiRendicontazioneDTO leggiRendicontazioniDTO) throws ServiceException,RendicontazioneNonTrovataException{
		BasicBD bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
		LeggiRendicontazioneDTOResponse response = new LeggiRendicontazioneDTOResponse();

		FrBD rendicontazioniBD = new FrBD(bd);
		Fr rendicontazione;
		try {

			rendicontazione = rendicontazioniBD.getFr(leggiRendicontazioniDTO.getIdFlusso());

			response.setFr(rendicontazione);
			response.setRendicontazioni(rendicontazione.getRendicontazioni(bd));

		} catch (NotFoundException e) {
			throw new RendicontazioneNonTrovataException(e.getMessage(), e);
		} finally {
			bd.closeConnection();
		}
		return response;
	}
	

}
