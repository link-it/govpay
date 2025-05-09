/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2025 Link.it srl (http://www.link.it).
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
package it.govpay.core.dao.reportistica;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.service.context.ContextThreadLocal;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.reportistica.statistiche.StatisticaRiscossioniBD;
import it.govpay.bd.reportistica.statistiche.filters.StatisticaRiscossioniFilter;
import it.govpay.bd.reportistica.statistiche.model.StatisticaRiscossione;
import it.govpay.core.dao.commons.BaseDAO;
import it.govpay.core.dao.reportistica.dto.ListaRiscossioniDTO;
import it.govpay.core.dao.reportistica.dto.ListaRiscossioniDTO.GROUP_BY;
import it.govpay.core.dao.reportistica.dto.ListaRiscossioniDTOResponse;

public class StatisticaRiscossioniDAO extends BaseDAO{

	public StatisticaRiscossioniDAO() {
		//donothing
	}

	public ListaRiscossioniDTOResponse listaRiscossioni(ListaRiscossioniDTO listaRiscossioniDTO) throws ServiceException {
		StatisticaRiscossioniBD statisticaRiscossioniBD = null;
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), this.useCacheData);
		try {
			statisticaRiscossioniBD = new StatisticaRiscossioniBD(configWrapper);
			StatisticaRiscossioniFilter filter = statisticaRiscossioniBD.newFilter();

			filter.setOffset(listaRiscossioniDTO.getOffset());
			filter.setLimit(listaRiscossioniDTO.getLimit());
			filter.setFiltro(listaRiscossioniDTO.getFiltro());

			List<IField> gruppiDaFare = new ArrayList<>();

			for (GROUP_BY gruppo : listaRiscossioniDTO.getGroupBy()) {
				switch (gruppo) {
				case DIR:
					gruppiDaFare.add(it.govpay.orm.Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.DIREZIONE);
					break;
				case DIV:
					gruppiDaFare.add(it.govpay.orm.Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.DIVISIONE);
					break;
				case TASSONOMIA:
					gruppiDaFare.add(it.govpay.orm.Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.TASSONOMIA);
					break;
				case DOMINIO:
					gruppiDaFare.add(it.govpay.orm.Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_UO.ID_DOMINIO.COD_DOMINIO);
					break;
				case TIPO_PENDENZA:
					gruppiDaFare.add(it.govpay.orm.Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_TIPO_VERSAMENTO.COD_TIPO_VERSAMENTO);
					break;
				case UO:
					gruppiDaFare.add(it.govpay.orm.Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_UO.COD_UO);
					break;
				case APPLICAZIONE:
					gruppiDaFare.add(it.govpay.orm.Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_APPLICAZIONE.COD_APPLICAZIONE);
					break;
				}
			}

			long count = statisticaRiscossioniBD.count(filter, gruppiDaFare);

			List<StatisticaRiscossione> findAll = new ArrayList<>();

			if(count > 0) {
				findAll = statisticaRiscossioniBD.statisticaNumeroPagamenti(filter, gruppiDaFare);

				for (StatisticaRiscossione riscossione: findAll) {
					riscossione.getApplicazione(configWrapper);
					riscossione.getDominio(configWrapper);
					riscossione.getUo(configWrapper);
					riscossione.getTipoVersamento(configWrapper);
				}

			}
			return new ListaRiscossioniDTOResponse(count, findAll);
		}finally {
			if(statisticaRiscossioniBD != null)
				statisticaRiscossioniBD.closeConnection();
		}
	}
}
