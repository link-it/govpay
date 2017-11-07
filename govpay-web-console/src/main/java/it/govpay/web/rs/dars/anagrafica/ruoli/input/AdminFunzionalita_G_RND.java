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
package it.govpay.web.rs.dars.anagrafica.ruoli.input;

import java.net.URI;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.RuoliBD;
import it.govpay.model.Acl;
import it.govpay.model.Acl.Servizio;
import it.govpay.model.Acl.Tipo;
import it.govpay.model.Ruolo;
import it.govpay.web.rs.dars.model.RawParamValue;
import it.govpay.web.rs.dars.model.input.dinamic.CheckButton;
import it.govpay.web.utils.Utils;

public class AdminFunzionalita_G_RND extends CheckButton<Boolean>{
	
	private String funzionalita_G_PAGId= null;
	private String ruoloId = null;
	private String nomeServizio = null;
	private Servizio servizio = Servizio.Gestione_Pagamenti;
	private Tipo tipo = Tipo.DOMINIO;

	public AdminFunzionalita_G_RND(String nomeServizio,String id, String label, URI refreshUri, List<RawParamValue> paramValues,Locale locale) {
		super(id, label, refreshUri, paramValues);
		this.nomeServizio = nomeServizio;
		this.funzionalita_G_PAGId = Utils.getInstance(locale).getMessageFromResourceBundle(this.nomeServizio + ".funzionalita_G_PAG.id");
		this.ruoloId = Utils.getInstance(locale).getMessageFromResourceBundle(this.nomeServizio + ".id.id");
	}

	@Override
	protected Boolean getDefaultValue(List<RawParamValue> values, Object... objects) {
		String funzionalita_G_PAGVAlue = Utils.getValue(values, this.funzionalita_G_PAGId);
		String idRuolo = Utils.getValue(values, this.ruoloId);

		if(StringUtils.isNotEmpty(funzionalita_G_PAGVAlue) && funzionalita_G_PAGVAlue.equalsIgnoreCase("false")){
			return false;
		}
		if(StringUtils.isEmpty(idRuolo)){
			return false;
		}

		try {
			BasicBD bd = (BasicBD) objects[0];
			RuoliBD ruoliBD = new RuoliBD(bd);
			Ruolo portale = ruoliBD.getRuolo(Long.parseLong(idRuolo));
			List<Acl> acls = portale.getAcls();
			
			for (Acl acl : acls) {
				Tipo tipo = acl.getTipo();
				if(acl.getServizio().equals(this.servizio) && tipo.equals(this.tipo)){
					if(acl.isAdmin())
						return true;
				}
			}
			
		} catch (Exception e) {
		}

		return false;
	}

	@Override
	protected boolean isRequired(List<RawParamValue> values, Object... objects) {
		return false;
	}
	@Override
	protected boolean isHidden(List<RawParamValue> values, Object... objects) {
		String funzionalita_G_PAGVAlue = Utils.getValue(values, this.funzionalita_G_PAGId);
		if(StringUtils.isNotEmpty(funzionalita_G_PAGVAlue) && funzionalita_G_PAGVAlue.equalsIgnoreCase("false")){
			return true;
		}

		return false;
	}
	@Override
	protected boolean isEditable(List<RawParamValue> values, Object... objects) {
		String funzionalita_G_PAGVAlue = Utils.getValue(values, this.funzionalita_G_PAGId);
		if(StringUtils.isNotEmpty(funzionalita_G_PAGVAlue) && funzionalita_G_PAGVAlue.equalsIgnoreCase("false")){
			return false;
		}
		return true;
	}

}
