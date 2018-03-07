package it.govpay.core.dao.pagamenti;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.model.Versamento;
import it.govpay.bd.pagamento.VersamentiBD;
import it.govpay.bd.pagamento.filters.VersamentoFilter;
import it.govpay.core.dao.pagamenti.dto.LeggiPendenzaDTO;
import it.govpay.core.dao.pagamenti.dto.LeggiPendenzaDTOResponse;
import it.govpay.core.dao.pagamenti.dto.ListaPendenzeDTO;
import it.govpay.core.dao.pagamenti.dto.ListaPendenzeDTOResponse;
import it.govpay.core.dao.pagamenti.exception.PendenzaNonTrovataException;
import it.govpay.core.utils.GpThreadLocal;

public class PendenzeDAO{

	public PendenzeDAO() {
	}

	public ListaPendenzeDTOResponse listaPendenze(ListaPendenzeDTO listaPendenzaDTO) throws ServiceException,PendenzaNonTrovataException{
		BasicBD bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());

		try {
			VersamentiBD versamentiBD = new VersamentiBD(bd);
			VersamentoFilter filter = versamentiBD.newFilter();

			filter.setOffset(listaPendenzaDTO.getOffset());
			filter.setLimit(listaPendenzaDTO.getLimit());
			filter.setDataInizio(listaPendenzaDTO.getDataDa());
			filter.setDataFine(listaPendenzaDTO.getDataA());
			filter.setStatoVersamento(listaPendenzaDTO.getStato());
			filter.setCodDominio(listaPendenzaDTO.getIdDominio() );
			filter.setCodPagamentoPortale(listaPendenzaDTO.getIdPagamento());
			filter.setCodUnivocoDebitore(listaPendenzaDTO.getIdDebitore());
			filter.setCodApplicazione(listaPendenzaDTO.getIdA2A());
			filter.setFilterSortList(listaPendenzaDTO.getFieldSortList());

			long count = versamentiBD.count(filter);

			List<LeggiPendenzaDTOResponse> resList = new ArrayList<LeggiPendenzaDTOResponse>();
			if(count > 0) {
				List<Versamento> findAll = versamentiBD.findAll(filter);

				for (Versamento versamento : findAll) {
					LeggiPendenzaDTOResponse elem = new LeggiPendenzaDTOResponse();
					elem.setVersamento(versamento);
					elem.setApplicazione(versamento.getApplicazione(versamentiBD));
					elem.setDominio(versamento.getDominio(versamentiBD));
					elem.setUnitaOperativa(versamento.getUo(versamentiBD));
					elem.setLstSingoliVersamenti(versamento.getSingoliVersamenti(versamentiBD));

					resList.add(elem);
				}
			} 

			return new ListaPendenzeDTOResponse(count, resList);
		}finally {
			bd.closeConnection();
		}
	}

	public LeggiPendenzaDTOResponse leggiPendenza(LeggiPendenzaDTO leggiPendenzaDTO) throws ServiceException,PendenzaNonTrovataException{
		BasicBD bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
		LeggiPendenzaDTOResponse response = new LeggiPendenzaDTOResponse();

		VersamentiBD versamentiBD = new VersamentiBD(bd);
		Versamento versamento;
		try {

			versamento = versamentiBD.getVersamento(AnagraficaManager.getApplicazione(versamentiBD, leggiPendenzaDTO.getCodA2A()).getId(), leggiPendenzaDTO.getCodPendenza());

			response.setVersamento(versamento);
			response.setApplicazione(versamento.getApplicazione(versamentiBD));
			response.setDominio(versamento.getDominio(versamentiBD));
			response.setUnitaOperativa(versamento.getUo(versamentiBD));
			response.setLstSingoliVersamenti(versamento.getSingoliVersamenti(versamentiBD));

		} catch (NotFoundException e) {
			throw new PendenzaNonTrovataException(e.getMessage(), e);
		} finally {
			bd.closeConnection();
		}
		return response;
	}
}
