/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2017 Link.it srl (http://www.link.it).
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
package it.govpay.web.rs.dars.anagrafica.portali.input;

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
import it.govpay.bd.anagrafica.PortaliBD;
import it.govpay.bd.anagrafica.filters.DominioFilter;
import it.govpay.model.Acl;
import it.govpay.bd.model.Dominio;
import it.govpay.model.Portale;
import it.govpay.model.Acl.Servizio;
import it.govpay.model.Acl.Tipo;
import it.govpay.web.rs.dars.model.RawParamValue;
import it.govpay.web.rs.dars.model.Voce;
import it.govpay.web.rs.dars.model.input.dinamic.MultiSelectList;
import it.govpay.web.utils.Utils;

public class DominiPA extends MultiSelectList<Long, List<Long>>{

	private String pagamentiAttesaId= null;
	private String portaleId = null;
	private String nomeServizio = null;
	private Servizio servizio = Servizio.PAGAMENTI_ATTESA;
	private Tipo tipo = Tipo.DOMINIO;

	public DominiPA(String nomeServizio,String id, String label, URI refreshUri, List<RawParamValue> paramValues,
			 Object... objects) {
		super(id, label, refreshUri, paramValues, objects);
		Locale locale = objects[1] != null ? (Locale) objects[1] : null;
		this.nomeServizio = nomeServizio;
		this.pagamentiAttesaId = Utils.getInstance(locale).getMessageFromResourceBundle(this.nomeServizio + ".pagamentiAttesa.id");
		this.portaleId = Utils.getInstance(locale).getMessageFromResourceBundle(this.nomeServizio + ".id.id");
	}

	@Override
	protected List<Voce<Long>> getValues(List<RawParamValue> paramValues, Object... objects) throws ServiceException {
		String pagamentiAttesaValue = Utils.getValue(paramValues, this.pagamentiAttesaId);
		List<Voce<Long>> lst = new ArrayList<Voce<Long>>();

		
		if(StringUtils.isNotEmpty(pagamentiAttesaValue) && pagamentiAttesaValue.equalsIgnoreCase("false")){
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
		String pagamentiAttesaValue = Utils.getValue(values, this.pagamentiAttesaId);
		String idPortale = Utils.getValue(values, this.portaleId);
		List<Long> lst = new ArrayList<Long>();

		if(StringUtils.isNotEmpty(pagamentiAttesaValue) && pagamentiAttesaValue.equalsIgnoreCase("false")){
			return lst;
		}
		if(StringUtils.isEmpty(idPortale)){
			return lst;
		}

		try {
			BasicBD bd = (BasicBD) objects[0];
			PortaliBD portaliBD = new PortaliBD(bd);
			Portale portale = portaliBD.getPortale(Long.parseLong(idPortale));
			List<Acl> acls = portale.getAcls();
			
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
		String pagamentiAttesaValue = Utils.getValue(values, this.pagamentiAttesaId);

		if(StringUtils.isNotEmpty(pagamentiAttesaValue) && pagamentiAttesaValue.equalsIgnoreCase("false")){
			return false;
		}

		return true;
	}
	@Override
	protected boolean isHidden(List<RawParamValue> values, Object... objects) {
		String pagamentiAttesaValue = Utils.getValue(values, this.pagamentiAttesaId);
		if(StringUtils.isNotEmpty(pagamentiAttesaValue) && pagamentiAttesaValue.equalsIgnoreCase("false")){
			return true;
		}

		return false;
	}
	@Override
	protected boolean isEditable(List<RawParamValue> values, Object... objects) {
		String pagamentiAttesaValue = Utils.getValue(values, this.pagamentiAttesaId);
		if(StringUtils.isNotEmpty(pagamentiAttesaValue) && pagamentiAttesaValue.equalsIgnoreCase("false")){
			return false;
		}
		return true;
	}

}