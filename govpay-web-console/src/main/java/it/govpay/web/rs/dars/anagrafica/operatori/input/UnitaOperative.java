/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2016 Link.it srl (http://www.link.it).
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
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
package it.govpay.web.rs.dars.anagrafica.operatori.input;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.SortOrder;

import it.govpay.bd.BasicBD;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.bd.anagrafica.OperatoriBD;
import it.govpay.bd.anagrafica.UnitaOperativeBD;
import it.govpay.bd.anagrafica.filters.UnitaOperativaFilter;
import it.govpay.bd.model.Operatore;
import it.govpay.bd.model.UnitaOperativa;
import it.govpay.web.rs.dars.anagrafica.operatori.OperatoriHandler;
import it.govpay.web.rs.dars.model.RawParamValue;
import it.govpay.web.rs.dars.model.Voce;
import it.govpay.web.rs.dars.model.input.dinamic.MultiSelectList;
import it.govpay.web.utils.Utils;

public class UnitaOperative extends MultiSelectList<Long, List<Long>>{

	private String profiloId= null;
	private String operatoreId = null;
	private String nomeServizio = null;

	public UnitaOperative(String nomeServizio,String id, String label, URI refreshUri, List<RawParamValue> paramValues,
			Object... objects) {
		super(id, label, refreshUri, paramValues, objects);
		this.nomeServizio = nomeServizio;
		this.profiloId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".profilo.id");
		this.operatoreId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".id.id");
	}

	@Override
	protected List<Voce<Long>> getValues(List<RawParamValue> paramValues, Object... objects) throws ServiceException {
		String profiloValue = Utils.getValue(paramValues, profiloId);
		List<Voce<Long>> lst = new ArrayList<Voce<Long>>();

		if(StringUtils.isNotEmpty(profiloValue) && profiloValue.equals(OperatoriHandler.PROFILO_OPERATORE_VALUE_ADMIN)){
			return lst;
		}

		try {
			BasicBD bd = (BasicBD) objects[0];
			UnitaOperativeBD uoBD = new UnitaOperativeBD(bd);

			UnitaOperativaFilter filter = uoBD.newFilter();
			FilterSortWrapper fsw = new FilterSortWrapper();
			fsw.setField(it.govpay.orm.Uo.model().COD_UO);
			fsw.setSortOrder(SortOrder.ASC);
			filter.getFilterSortList().add(fsw);
			List<UnitaOperativa> findAll = uoBD.findAll(filter);

			for(UnitaOperativa uo : findAll) {
				lst.add(new Voce<Long>(uo.getCodUo(), uo.getId()));
			}
		} catch (Exception e) {
			throw new ServiceException(e);
		}

		return lst;
	}

	@Override
	protected List<Long> getDefaultValue(List<RawParamValue> values, Object... objects) {
		String profiloValue = Utils.getValue(values, profiloId);
		String idOperatore = Utils.getValue(values, operatoreId);
		List<Long> lst = new ArrayList<Long>();

		if(StringUtils.isNotEmpty(profiloValue) && profiloValue.equals(OperatoriHandler.PROFILO_OPERATORE_VALUE_ADMIN)){
			return lst;
		}
		if(StringUtils.isEmpty(idOperatore)){
			return lst;
		}

		try {
			BasicBD bd = (BasicBD) objects[0];
			OperatoriBD operatoriBD = new OperatoriBD(bd);
			Operatore operatore = operatoriBD.getOperatore(Long.parseLong(idOperatore));
			lst.addAll(operatore.getIdEnti());
		} catch (Exception e) {
			//throw new ServiceException(e);
		}

		return lst;
	}
	@Override
	protected boolean isRequired(List<RawParamValue> values, Object... objects) {
		String profiloValue = Utils.getValue(values, profiloId);

		if(StringUtils.isNotEmpty(profiloValue) && profiloValue.equals(OperatoriHandler.PROFILO_OPERATORE_VALUE_ADMIN)){
			return false;
		}

		return true;
	}
	@Override
	protected boolean isHidden(List<RawParamValue> values, Object... objects) {
		String profiloValue = Utils.getValue(values, profiloId);
		if(StringUtils.isNotEmpty(profiloValue) && profiloValue.equals(OperatoriHandler.PROFILO_OPERATORE_VALUE_ADMIN)){
			return true;
		}

		return false;
	}
	@Override
	protected boolean isEditable(List<RawParamValue> values, Object... objects) {
		String profiloValue = Utils.getValue(values, profiloId);
		if(StringUtils.isNotEmpty(profiloValue) && profiloValue.equals(OperatoriHandler.PROFILO_OPERATORE_VALUE_ADMIN)){
			return false;
		}
		return true;
	}

}
