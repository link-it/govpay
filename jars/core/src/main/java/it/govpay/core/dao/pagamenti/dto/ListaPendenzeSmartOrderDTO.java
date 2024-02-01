/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2024 Link.it srl (http://www.link.it).
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
package it.govpay.core.dao.pagamenti.dto;

import org.apache.commons.lang.StringUtils;
import org.openspcoop2.generic_project.expression.SortOrder;
import org.springframework.security.core.Authentication;

import it.govpay.core.autorizzazione.beans.GovpayLdapUserDetails;
import it.govpay.core.autorizzazione.utils.AutorizzazioneUtils;
import it.govpay.core.exceptions.RequestParamException;
import it.govpay.core.exceptions.RequestParamException.FaultType;
import it.govpay.model.Utenza.TIPO_UTENZA;
import it.govpay.orm.VersamentoIncasso;

public class ListaPendenzeSmartOrderDTO extends ListaPendenzeDTO{
	
	
	public ListaPendenzeSmartOrderDTO(Authentication user) {
		super(user);
		this.clearFieldMap();
		this.addSortField("dataCaricamento", VersamentoIncasso.model().DATA_CREAZIONE);
		this.addSortField("dataValidita", VersamentoIncasso.model().DATA_VALIDITA);
		this.addSortField("dataScadenza", VersamentoIncasso.model().DATA_SCADENZA);
		this.addSortField("stato", VersamentoIncasso.model().STATO_VERSAMENTO);
		this.getDefaultSort().clear();
		this.addDefaultSort(VersamentoIncasso.model().DATA_CREAZIONE,SortOrder.DESC);
	}
	
	
	@Override
	public void setOrderBy(String orderBy) throws RequestParamException {
		this.resetSort();
		
		if(orderBy==null || orderBy.trim().isEmpty()) return;
		
		// visualizzazione smart abilitabile solo se ho infoincasso e se sono un cittadino oppure ho scelto un debitore		
		String fieldname = "smart";
		if(orderBy.equals(fieldname) ){ 
			GovpayLdapUserDetails userDetails = AutorizzazioneUtils.getAuthenticationDetails(this.getUser());
			if(userDetails.getTipoUtenza().equals(TIPO_UTENZA.CITTADINO) || StringUtils.isNotEmpty(this.getIdDebitore())) {
				this.addSortField(VersamentoIncasso.model().SMART_ORDER_RANK, SortOrder.ASC);
				this.addSortField(VersamentoIncasso.model().SMART_ORDER_DATE, SortOrder.ASC);
			} else {
				throw new RequestParamException(FaultType.PARAMETRO_ORDERBY_NON_VALIDO, "Il campo " + fieldname + " non e' valido per ordinare la ricerca in corso senza indicare un idDebitore.");
			}
		} else {
			super.setOrderBy(orderBy);
		}
	}
}
