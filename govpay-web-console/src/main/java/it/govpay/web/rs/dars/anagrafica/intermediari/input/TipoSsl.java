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
package it.govpay.web.rs.dars.anagrafica.intermediari.input;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.model.Connettore;
import it.govpay.bd.model.Connettore.EnumSslType;
import it.govpay.web.rs.dars.anagrafica.connettori.ConnettoreHandler;
import it.govpay.web.rs.dars.model.RawParamValue;
import it.govpay.web.rs.dars.model.Voce;
import it.govpay.web.rs.dars.model.input.dinamic.SelectList;
import it.govpay.web.utils.Utils;

public class TipoSsl extends SelectList<String> {
	
	private String nomeServizio = null;
	private String nomeConnettore = null;
	public static final String TIPO_SSL_VALUE_SERVER = "SERVER";
	public static final String TIPO_SSL_VALUE_CLIENT = "CLIENT";
	private String tipoAutenticazioneId= null;
	private String idOwnerId = null;
	
	public TipoSsl(String nomeConnettore,String nomeServizio,String id, String label, URI refreshUri, List<RawParamValue> rawvalues, BasicBD bd) {
		super(id, label, refreshUri, rawvalues, bd);
		this.nomeServizio = nomeServizio;
		this.nomeConnettore = nomeConnettore;
		this.tipoAutenticazioneId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeConnettore + ".tipoAutenticazione.id");
		this.idOwnerId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".id.id");
	}

	@Override
	protected List<Voce<String>> getValues(List<RawParamValue> paramValues, Object ... objects) throws ServiceException {
		List<Voce<String>> voci = new ArrayList<Voce<String>>();
		voci.add(new Voce<String>(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeConnettore + ".tipoSsl.client.label"), TIPO_SSL_VALUE_CLIENT));
		voci.add(new Voce<String>(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeConnettore + ".tipoSsl.server.label"), TIPO_SSL_VALUE_SERVER));
		
		return voci; 
	}
	@Override
	protected String getDefaultValue(List<RawParamValue> values, Object... objects) {
		String tipoAutenticazioneValue = Utils.getValue(values, this.tipoAutenticazioneId);
		String idOwner = Utils.getValue(values, this.idOwnerId);
		Connettore c = null;
		
		if(StringUtils.isNotEmpty(tipoAutenticazioneValue) && !tipoAutenticazioneValue.equals(ConnettoreHandler.TIPO_AUTENTICAZIONE_VALUE_SSL)){
			return null;
		}
		
		if(StringUtils.isEmpty(idOwner)){
			return null;
		}
		
		BasicBD bd = (BasicBD) objects[0];
		c= Utils.getConnettore(idOwner, this.nomeConnettore,bd);
		
		if(c!= null){
			EnumSslType tipoSsl2 = (c.getTipoSsl() != null ? c.getTipoSsl() : EnumSslType.CLIENT);
			switch (tipoSsl2) {
			case SERVER:
				return TipoSsl.TIPO_SSL_VALUE_SERVER;
			case CLIENT:
			default:
				return TipoSsl.TIPO_SSL_VALUE_CLIENT;
			}
		}
		
		return null;
	}

	@Override
	protected boolean isRequired(List<RawParamValue> values, Object... objects) {
		String tipoAutenticazioneValue = Utils.getValue(values, this.tipoAutenticazioneId);
		
		if(StringUtils.isNotEmpty(tipoAutenticazioneValue) && tipoAutenticazioneValue.equals(ConnettoreHandler.TIPO_AUTENTICAZIONE_VALUE_SSL)){
			return true;
		}
		
		return false;
	}

	@Override
	protected boolean isHidden(List<RawParamValue> values, Object... objects) {
		String tipoAutenticazioneValue = Utils.getValue(values, this.tipoAutenticazioneId);
		
		if(StringUtils.isNotEmpty(tipoAutenticazioneValue) && tipoAutenticazioneValue.equals(ConnettoreHandler.TIPO_AUTENTICAZIONE_VALUE_SSL)){
			return false;
		}
		
		return true;
	}

	@Override
	protected boolean isEditable(List<RawParamValue> values, Object... objects) {
		String tipoAutenticazioneValue = Utils.getValue(values, this.tipoAutenticazioneId);
		
		if(StringUtils.isNotEmpty(tipoAutenticazioneValue) && tipoAutenticazioneValue.equals(ConnettoreHandler.TIPO_AUTENTICAZIONE_VALUE_SSL)){
			return true;
		}
		
		return false;
	}

}
