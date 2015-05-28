/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2015 Link.it srl (http://www.link.it).
 * Copyright (c) 2014-2015 TAS S.p.A. (http://www.tasgroup.it).
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
package it.govpay.ejb.utils;

import it.govpay.ejb.exception.GovPayException;
import it.govpay.ejb.exception.GovPayException.GovPayExceptionEnum;

import java.net.URL;

public class GovpayConfiguration extends ConfigurationUtils {
	
	private static String filePropName = "govpay.properties";
	private static URL defaultBackUrl;
	
	private static GovpayConfiguration instance;
	
	public static void newInstance() throws GovPayException {
		instance = new GovpayConfiguration();
	}
	
	public static GovpayConfiguration getInstance() {
		return instance;
	}
	
	public GovpayConfiguration() throws GovPayException{
		super(filePropName);

		String defaultBackUrlString = getStringValue("govpay.web.defaultBackUrl");
		try {
			defaultBackUrl = new URL(defaultBackUrlString);
		} catch (Exception e) {
			throw new GovPayException(GovPayExceptionEnum.ERRORE_CONFIGURAZIONE, "Il valore della proprieta govpay.web.defaultBackUrl non e' una URL valida.", e);
		}
		
		instance = this;
	}
    
	public static URL getDefaultBackUrl(){
		return defaultBackUrl;
	}
}
