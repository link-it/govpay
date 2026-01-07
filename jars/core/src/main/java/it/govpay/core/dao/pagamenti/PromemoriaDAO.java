/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2026 Link.it srl (http://www.link.it).
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3, as published by
 * the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package it.govpay.core.dao.pagamenti;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.service.context.ContextThreadLocal;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.model.Promemoria;
import it.govpay.bd.pagamento.PromemoriaBD;
import it.govpay.bd.pagamento.filters.PromemoriaFilter;
import it.govpay.core.dao.commons.BaseDAO;
import it.govpay.core.dao.pagamenti.dto.ListaPromemoriaDTO;
import it.govpay.core.dao.pagamenti.dto.ListaPromemoriaDTOResponse;

public class PromemoriaDAO extends BaseDAO{

	public ListaPromemoriaDTOResponse listaPromemoria(ListaPromemoriaDTO listaPromemoriaDTO) throws ServiceException { 
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
				return new ListaPromemoriaDTOResponse(count, lst);
			} else {
				return new ListaPromemoriaDTOResponse(count, new ArrayList<>());
			}
		}finally {
			promemoriaBD.closeConnection();
		}
	}
}
