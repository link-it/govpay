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
package it.govpay.web.rs.dars.anagrafica.tributi.input;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.SortOrder;

import it.govpay.bd.BasicBD;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.bd.anagrafica.DominiBD;
import it.govpay.bd.anagrafica.IbanAccreditoBD;
import it.govpay.bd.anagrafica.TributiBD;
import it.govpay.bd.anagrafica.filters.IbanAccreditoFilter;
import it.govpay.bd.model.Tributo;
import it.govpay.web.rs.dars.model.RawParamValue;
import it.govpay.web.rs.dars.model.Voce;
import it.govpay.web.rs.dars.model.input.dinamic.SelectList;
import it.govpay.web.utils.Utils;

public class IbanAccredito extends SelectList<Long>{

	private String idDominioId= null;
	private String tributoId = null;
	private String nomeServizio = null;

	public IbanAccredito(String nomeServizio,String id, String label, URI refreshUri, List<RawParamValue> paramValues,
			Object... objects) {
		super(id, label, refreshUri, paramValues, objects);
		this.nomeServizio = nomeServizio;
		this.idDominioId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".idDominio.id");
		this.tributoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".id.id");
	}

	@Override
	protected List<Voce<Long>> getValues(List<RawParamValue> paramValues, Object... objects) throws ServiceException {
		String idDominioValue = Utils.getValue(paramValues, idDominioId);
		List<Voce<Long>> lst = new ArrayList<Voce<Long>>();

		if(StringUtils.isEmpty(idDominioValue)){
			return lst;
		}
		
		try {
			BasicBD bd = (BasicBD) objects[0];
			DominiBD dominiBD = new DominiBD(bd);
			IbanAccreditoBD ibanAccreditoBD = new IbanAccreditoBD(bd);
			IbanAccreditoFilter filterIban = ibanAccreditoBD.newFilter();
			FilterSortWrapper fsw = new FilterSortWrapper();
			fsw.setField(it.govpay.orm.IbanAccredito.model().COD_IBAN);
			fsw.setSortOrder(SortOrder.ASC);
			filterIban.getFilterSortList().add(fsw);
			filterIban.setCodDominio(dominiBD.getDominio(Long.parseLong(idDominioValue)).getCodDominio());   
			List<it.govpay.bd.model.IbanAccredito> findAll = ibanAccreditoBD.findAll(filterIban);

			if(findAll != null && findAll.size() > 0){
				for (it.govpay.bd.model.IbanAccredito ib : findAll) {
					lst.add(new Voce<Long>(ib.getCodIban(), ib.getId()));  
				}
			}
		 
		} catch (Exception e) {
			throw new ServiceException(e);
		}

		return lst;
	}

	@Override
	protected Long getDefaultValue(List<RawParamValue> values, Object... objects) {
		String idDominioValue = Utils.getValue(values, idDominioId);
		String idTributo = Utils.getValue(values, tributoId);

		if(StringUtils.isEmpty(idDominioValue)){
			return null;
		}
		if(StringUtils.isEmpty(idTributo)){
			return null;
		}

		try {
			BasicBD bd = (BasicBD) objects[0];
			TributiBD tributiBD = new TributiBD(bd);
			Tributo tributo= tributiBD.getTributo(Long.parseLong(idTributo));
			return tributo.getIdIbanAccredito();
		} catch (Exception e) {
			//throw new ServiceException(e);
		}

		return null;
	}
	@Override
	protected boolean isRequired(List<RawParamValue> values, Object... objects) {
		return true;
	}
	@Override
	protected boolean isHidden(List<RawParamValue> values, Object... objects) {
		String idDominioValue = Utils.getValue(values, idDominioId);
		if(StringUtils.isNotEmpty(idDominioValue)){
			return false;
		}

		return true;
	}
	@Override
	protected boolean isEditable(List<RawParamValue> values, Object... objects) {
		String idDominioValue = Utils.getValue(values, idDominioId);
		if(StringUtils.isNotEmpty(idDominioValue)){
			return true;
		}

		return false;
	}

}
