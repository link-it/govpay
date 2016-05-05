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
package it.govpay.web.rs.dars.anagrafica.applicazioni.input;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.SortOrder;

import it.govpay.bd.BasicBD;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.bd.anagrafica.ApplicazioniBD;
import it.govpay.bd.anagrafica.DominiBD;
import it.govpay.bd.anagrafica.filters.DominioFilter;
import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Dominio;
import it.govpay.web.rs.dars.anagrafica.domini.DominiHandler;
import it.govpay.web.rs.dars.model.Elemento;
import it.govpay.web.rs.dars.model.RawParamValue;
import it.govpay.web.rs.dars.model.Voce;
import it.govpay.web.rs.dars.model.input.dinamic.MultiSelectList;
import it.govpay.web.utils.Utils;

public class Domini extends MultiSelectList<Long, List<Long>>{

	private String trustedId= null;
	private String applicazioneId = null;
	private String nomeServizio = null;

	public Domini(String nomeServizio,String id, String label, URI refreshUri, List<RawParamValue> paramValues,
			 Object... objects) {
		super(id, label, refreshUri, paramValues, objects);
		this.nomeServizio = nomeServizio;
		this.trustedId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".trusted.id");
		this.applicazioneId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".id.id");
		this.log = LogManager.getLogger();
	}

	@Override
	protected List<Voce<Long>> getValues(List<RawParamValue> paramValues, Object... objects) throws ServiceException {
		String trustedValue = Utils.getValue(paramValues, this.trustedId);
		List<Voce<Long>> lst = new ArrayList<Voce<Long>>();

		if(StringUtils.isNotEmpty(trustedValue) && trustedValue.equalsIgnoreCase("false")){
			return lst;
		}

		try {
			BasicBD bd = (BasicBD) objects[0];
			DominiBD dominiBD = new DominiBD(bd); 

			DominioFilter filter = dominiBD.newFilter();
			FilterSortWrapper fsw = new FilterSortWrapper();
			fsw.setField(it.govpay.orm.Dominio.model().COD_DOMINIO);
			fsw.setSortOrder(SortOrder.ASC);
			filter.getFilterSortList().add(fsw);
			List<Dominio> findAll = dominiBD.findAll(filter);

			it.govpay.web.rs.dars.anagrafica.domini.Domini dominiDars = new it.govpay.web.rs.dars.anagrafica.domini.Domini();
			DominiHandler dominiDarsHandler = (DominiHandler) dominiDars.getDarsHandler();
			
			for(Dominio dominio : findAll) {
				Elemento elemento = dominiDarsHandler.getElemento(dominio, dominio.getId(), null,bd);
				lst.add(new Voce<Long>(elemento.getTitolo(), dominio.getId())); 
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			throw new ServiceException(e);
		}

		return lst;
	}

	@Override
	protected List<Long> getDefaultValue(List<RawParamValue> values, Object... objects) {
		String trustedValue = Utils.getValue(values, this.trustedId);
		String idapplicazione = Utils.getValue(values, this.applicazioneId);
		List<Long> lst = new ArrayList<Long>();

		if(StringUtils.isNotEmpty(trustedValue) && trustedValue.equalsIgnoreCase("false")){
			return lst;
		}
		if(StringUtils.isEmpty(idapplicazione)){
			return lst;
		}

		try {
			BasicBD bd = (BasicBD) objects[0];
			ApplicazioniBD applicazioniBD = new ApplicazioniBD(bd);
			Applicazione applicazione = applicazioniBD.getApplicazione(Long.parseLong(idapplicazione));
			lst.addAll(applicazione.getIdDomini());
		} catch (Exception e) {
			log.error(e.getMessage(),e); 
		}

		return lst;
	}
	@Override
	protected boolean isRequired(List<RawParamValue> values, Object... objects) {
		String trustedValue = Utils.getValue(values, this.trustedId);

		if(StringUtils.isNotEmpty(trustedValue) && trustedValue.equalsIgnoreCase("false")){
			return false;
		}

		return true;
	}
	@Override
	protected boolean isHidden(List<RawParamValue> values, Object... objects) {
		String trustedValue = Utils.getValue(values, this.trustedId);
		if(StringUtils.isNotEmpty(trustedValue) && trustedValue.equalsIgnoreCase("false")){
			return true;
		}

		return false;
	}
	@Override
	protected boolean isEditable(List<RawParamValue> values, Object... objects) {
		String trustedValue = Utils.getValue(values, this.trustedId);
		if(StringUtils.isNotEmpty(trustedValue) && trustedValue.equalsIgnoreCase("false")){
			return false;
		}
		return true;
	}

}
