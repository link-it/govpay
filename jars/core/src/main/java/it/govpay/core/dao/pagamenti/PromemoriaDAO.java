package it.govpay.core.dao.pagamenti;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.service.context.ContextThreadLocal;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.BasicBD;
import it.govpay.bd.model.Promemoria;
import it.govpay.bd.pagamento.PromemoriaBD;
import it.govpay.bd.pagamento.filters.PromemoriaFilter;
import it.govpay.core.dao.commons.BaseDAO;
import it.govpay.core.dao.pagamenti.dto.ListaPromemoriaDTO;
import it.govpay.core.dao.pagamenti.dto.ListaPromemoriaDTOResponse;
import it.govpay.core.exceptions.NotAuthenticatedException;
import it.govpay.core.exceptions.NotAuthorizedException;

public class PromemoriaDAO extends BaseDAO{

	public ListaPromemoriaDTOResponse listaPromemoria(ListaPromemoriaDTO listaPromemoriaDTO) throws ServiceException, NotAuthorizedException, NotAuthenticatedException, NotFoundException{ 
		PromemoriaBD promemoriaBD = null;
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), this.useCacheData);
		
		try {
			promemoriaBD = new PromemoriaBD(configWrapper);
			PromemoriaFilter filter = promemoriaBD.newFilter();
			
			filter.setOffset(listaPromemoriaDTO.getOffset());
			filter.setLimit(listaPromemoriaDTO.getLimit());
			filter.setDataInizio(listaPromemoriaDTO.getDataDa());
			filter.setDataFine(listaPromemoriaDTO.getDataA());
			filter.setEseguiCountConLimit(listaPromemoriaDTO.isEseguiCountConLimit());
			
			if(listaPromemoriaDTO.getStato() != null) {
				try {
					it.govpay.model.Promemoria.StatoSpedizione statoSpedizione = listaPromemoriaDTO.getStato();
					filter.setStato(statoSpedizione.toString());
				} catch(Exception e) {
					return new ListaPromemoriaDTOResponse(0L, new ArrayList<>());
				}
			}
			
			if(listaPromemoriaDTO.getTipo() != null) {
				try {
					it.govpay.model.Promemoria.TipoPromemoria tipoPromemoria = listaPromemoriaDTO.getTipo();
					filter.setTipo(tipoPromemoria.toString());
				} catch(Exception e) {
					return new ListaPromemoriaDTOResponse(0L, new ArrayList<>());
				}
			}
			
			
			Long count = null;
			
			if(listaPromemoriaDTO.isEseguiCount()) {
				 count = promemoriaBD.count(filter);
			}

			if(listaPromemoriaDTO.isEseguiFindAll()) {
				List<Promemoria> lst = promemoriaBD.findAll(filter);
				
				for (Promemoria promemoria : lst) {
					this.populatePromemoria(promemoria, promemoriaBD);
				}
				
				return new ListaPromemoriaDTOResponse(count, lst);
			} else {
				return new ListaPromemoriaDTOResponse(count, new ArrayList<>());
			}
		}finally {
			if(promemoriaBD != null)
				promemoriaBD.closeConnection();
		}
	}
	
	private void populatePromemoria(Promemoria promemoria, BasicBD bd) throws ServiceException, NotFoundException {
	}
}
