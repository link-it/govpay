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
package it.govpay.core.dao.reportistica;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.service.context.ContextThreadLocal;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.reportistica.statistiche.StatisticaRendicontazioniBD;
import it.govpay.bd.reportistica.statistiche.filters.StatisticaRendicontazioniFilter;
import it.govpay.bd.reportistica.statistiche.model.StatisticaRendicontazione;
import it.govpay.core.dao.commons.BaseDAO;
import it.govpay.core.dao.reportistica.dto.ListaRendicontazioniDTO;
import it.govpay.core.dao.reportistica.dto.ListaRendicontazioniDTO.GROUP_BY;
import it.govpay.core.dao.reportistica.dto.ListaRendicontazioniDTOResponse;

public class StatisticaRendicontazioniDAO extends BaseDAO{

	public StatisticaRendicontazioniDAO() {
		// donothing
	}

	public ListaRendicontazioniDTOResponse listaRendicontazioni(ListaRendicontazioniDTO listaRiscossioniDTO) throws ServiceException {
		StatisticaRendicontazioniBD statisticaRiscossioniBD = null;
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), this.useCacheData);
		try {
			statisticaRiscossioniBD = new StatisticaRendicontazioniBD(configWrapper);
			
			StatisticaRendicontazioniFilter filter = statisticaRiscossioniBD.newFilter();

			filter.setOffset(listaRiscossioniDTO.getOffset());
			filter.setLimit(listaRiscossioniDTO.getLimit());
			filter.setFiltro(listaRiscossioniDTO.getFiltro());

			List<IField> gruppiDaFare = new ArrayList<>();

			for (GROUP_BY gruppo : listaRiscossioniDTO.getGroupBy()) {
				switch (gruppo) {
				case DIR:
					gruppiDaFare.add(it.govpay.orm.Rendicontazione.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.DIREZIONE);
					break;
				case DIV:
					gruppiDaFare.add(it.govpay.orm.Rendicontazione.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.DIVISIONE);
					break;
				case FLUSSO_RENDICONTAZIONE:
					gruppiDaFare.add(it.govpay.orm.Rendicontazione.model().ID_FR.COD_FLUSSO);
					gruppiDaFare.add(it.govpay.orm.Rendicontazione.model().ID_FR.DATA_ORA_FLUSSO); 
					gruppiDaFare.add(it.govpay.orm.Rendicontazione.model().ID_FR.IUR);
					gruppiDaFare.add(it.govpay.orm.Rendicontazione.model().ID_FR.DATA_REGOLAMENTO);
					gruppiDaFare.add(it.govpay.orm.Rendicontazione.model().ID_FR.COD_PSP);
					gruppiDaFare.add(it.govpay.orm.Rendicontazione.model().ID_FR.COD_BIC_RIVERSAMENTO);
					gruppiDaFare.add(it.govpay.orm.Rendicontazione.model().ID_FR.COD_DOMINIO);
					gruppiDaFare.add(it.govpay.orm.Rendicontazione.model().ID_FR.NUMERO_PAGAMENTI);
					gruppiDaFare.add(it.govpay.orm.Rendicontazione.model().ID_FR.IMPORTO_TOTALE_PAGAMENTI);
					gruppiDaFare.add(it.govpay.orm.Rendicontazione.model().ID_FR.STATO);
					gruppiDaFare.add(it.govpay.orm.Rendicontazione.model().ID_FR.RAGIONE_SOCIALE_DOMINIO);
					gruppiDaFare.add(it.govpay.orm.Rendicontazione.model().ID_FR.RAGIONE_SOCIALE_PSP);
					break;
				}
			}

			long count = statisticaRiscossioniBD.count(filter, gruppiDaFare);

			List<StatisticaRendicontazione> findAll = new ArrayList<>();

			if(count > 0) {
				findAll = statisticaRiscossioniBD.statisticaNumeroRendicontazioni(filter, gruppiDaFare);
			}
			return new ListaRendicontazioniDTOResponse(count, findAll);
		}finally {
			if(statisticaRiscossioniBD != null)
				statisticaRiscossioniBD.closeConnection();
		}
	}
}
