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
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.SortOrder;

import it.govpay.bd.BasicBD;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.bd.anagrafica.DominiBD;
import it.govpay.bd.anagrafica.OperatoriBD;
import it.govpay.bd.anagrafica.filters.DominioFilter;
import it.govpay.model.Acl;
import it.govpay.bd.model.Dominio;
import it.govpay.model.Operatore;
import it.govpay.model.Acl.Servizio;
import it.govpay.model.Acl.Tipo;
import it.govpay.web.rs.dars.anagrafica.operatori.OperatoriHandler;
import it.govpay.web.rs.dars.model.RawParamValue;
import it.govpay.web.rs.dars.model.Voce;
import it.govpay.web.rs.dars.model.input.dinamic.MultiSelectList;
import it.govpay.web.utils.Utils;

public class Domini extends MultiSelectList<Long, List<Long>>{

	private String profiloId= null;
	private String operatoreId = null;
	private String nomeServizio = null;
	private Servizio servizio = Servizio.CRUSCOTTO;
	private Tipo tipo = Tipo.DOMINIO;

	public Domini(String nomeServizio,String id, String label, URI refreshUri, List<RawParamValue> paramValues,
			 Object... objects) {
		super(id, label, refreshUri, paramValues, objects);
		Locale locale = objects[1] != null ? (Locale) objects[1] : null;
		this.nomeServizio = nomeServizio;
		this.profiloId = Utils.getInstance(locale).getMessageFromResourceBundle(this.nomeServizio + ".profilo.id");
		this.operatoreId = Utils.getInstance(locale).getMessageFromResourceBundle(this.nomeServizio + ".id.id");
	}

	@Override
	protected List<Voce<Long>> getValues(List<RawParamValue> paramValues, Object... objects) throws ServiceException {
		String profiloValue = Utils.getValue(paramValues, this.profiloId);
		List<Voce<Long>> lst = new ArrayList<Voce<Long>>();

		if(StringUtils.isNotEmpty(profiloValue) && profiloValue.equals(OperatoriHandler.PROFILO_OPERATORE_VALUE_ADMIN)){
			return lst;
		}

		try {
			BasicBD bd = (BasicBD) objects[0];
			DominiBD applicazioniBD = new DominiBD(bd);

			DominioFilter filter = applicazioniBD.newFilter();
			FilterSortWrapper fsw = new FilterSortWrapper();
			fsw.setField(it.govpay.orm.Dominio.model().COD_DOMINIO);
			fsw.setSortOrder(SortOrder.ASC);
			filter.getFilterSortList().add(fsw);
			List<Dominio> findAll = applicazioniBD.findAll(filter);

			lst.add(Utils.getVoceTutti());
			for(Dominio dominio : findAll) {
				StringBuilder sb = new StringBuilder();

				sb.append(dominio.getRagioneSociale());
				sb.append(" (").append(dominio.getCodDominio()).append(")");
				
				lst.add(new Voce<Long>(sb.toString(), dominio.getId()));
			}
		} catch (Exception e) {
			throw new ServiceException(e);
		}

		return lst;
	}

	@Override
	protected List<Long> getDefaultValue(List<RawParamValue> values, Object... objects) {
		String profiloValue = Utils.getValue(values, this.profiloId);
		String idOperatore = Utils.getValue(values, this.operatoreId);
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
			List<Acl> acls = operatore.getAcls();
			
			for (Acl acl : acls) {
				Tipo tipo = acl.getTipo();
				if(acl.getServizio().equals(this.servizio) && tipo.equals(this.tipo)){
					if(acl.getIdDominio() == null){
						lst.clear();
						lst.add(-1L);
						break;
					}else{
						lst.add(acl.getIdDominio());
					}
				}
			}
			
		} catch (Exception e) {
		}

		return lst;
	}
	@Override
	protected boolean isRequired(List<RawParamValue> values, Object... objects) {
		String profiloValue = Utils.getValue(values, this.profiloId);

		if(StringUtils.isNotEmpty(profiloValue) && profiloValue.equals(OperatoriHandler.PROFILO_OPERATORE_VALUE_ADMIN)){
			return false;
		}

		return true;
	}
	@Override
	protected boolean isHidden(List<RawParamValue> values, Object... objects) {
		String profiloValue = Utils.getValue(values, this.profiloId);
		if(StringUtils.isNotEmpty(profiloValue) && profiloValue.equals(OperatoriHandler.PROFILO_OPERATORE_VALUE_ADMIN)){
			return true;
		}

		return false;
	}
	@Override
	protected boolean isEditable(List<RawParamValue> values, Object... objects) {
		String profiloValue = Utils.getValue(values, this.profiloId);
		if(StringUtils.isNotEmpty(profiloValue) && profiloValue.equals(OperatoriHandler.PROFILO_OPERATORE_VALUE_ADMIN)){
			return false;
		}
		return true;
	}

}