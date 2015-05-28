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
package it.govpay.ejb.ndp.util;

import it.govpay.ejb.core.exception.GovPayException;
import it.govpay.ejb.core.utils.ConfigurationUtils;

public class NdpConfiguration extends ConfigurationUtils {
	
	private static String filePropName = "ndp.properties";
	private static String 
		idDominioPrincipale,
		datiSpecificiRiscossione,
		versioneRPT,
		httpHeader_tipoSoggettoFruitore,
		httpHeader_soggettoFruitore,
		httpHeader_tipoSoggettoErogatore,
		httpHeader_soggettoErogatore,
		httpHeader_tipoServizio,
		httpHeader_servizio,
		httpHeader_azione,
		httpHeader_idEgov;
	
	private static NdpConfiguration instance;
	
	public static void newInstance() throws GovPayException {
		instance = new NdpConfiguration();
	}
	
	public static NdpConfiguration getInstance() {
		return instance;
	}
	
	public NdpConfiguration() throws GovPayException{
		super(filePropName);

		datiSpecificiRiscossione = getStringValue("govpay.ndp.rpt.datiSpecificiRiscossione");
		versioneRPT =  getStringValue("govpay.ndp.rpt.versione", "6.0.1");
		
		httpHeader_tipoSoggettoFruitore =  getStringValue("govpay.ndp.spcoopheader.tipoSoggettoFruitore", "X-OpenSPCoop2-TipoMittente");
		httpHeader_soggettoFruitore =  getStringValue("govpay.ndp.spcoopheader.soggettoFruitore", "X-OpenSPCoop2-Mittente");
		httpHeader_tipoSoggettoErogatore =  getStringValue("govpay.ndp.spcoopheader.tipoSoggettoErogatore", "X-OpenSPCoop2-TipoDestinatario");
		httpHeader_soggettoErogatore =  getStringValue("govpay.ndp.spcoopheader.soggettoErogatore", "X-OpenSPCoop2-Destinatario");
		httpHeader_tipoServizio =  getStringValue("govpay.ndp.spcoopheader.tipoServizio", "X-OpenSPCoop2-TipoServizio");
		httpHeader_servizio =  getStringValue("govpay.ndp.spcoopheader.servizio", "X-OpenSPCoop2-Servizio");
		httpHeader_azione =  getStringValue("govpay.ndp.spcoopheader.azione", "X-OpenSPCoop2-Azione");
		httpHeader_idEgov =  getStringValue("govpay.ndp.spcoopheader.idEgov", "X-OpenSPCoop2-IdMessaggio");
		
		instance = this;
	}
	

	public String getDatiSpecificiRiscossione() {
		return datiSpecificiRiscossione;
	}

	public String getVersioneRPT() {
		return versioneRPT;
	}

	public String getFilePropName() {
		return filePropName;
	}

	public String getHttpHeader_tipoSoggettoFruitore() {
		return httpHeader_tipoSoggettoFruitore;
	}

	public String getHttpHeader_soggettoFruitore() {
		return httpHeader_soggettoFruitore;
	}

	public String getHttpHeader_tipoSoggettoErogatore() {
		return httpHeader_tipoSoggettoErogatore;
	}

	public String getHttpHeader_soggettoErogatore() {
		return httpHeader_soggettoErogatore;
	}

	public String getHttpHeader_tipoServizio() {
		return httpHeader_tipoServizio;
	}

	public String getHttpHeader_servizio() {
		return httpHeader_servizio;
	}

	public String getHttpHeader_azione() {
		return httpHeader_azione;
	}

	public String getHttpHeader_idEgov() {
		return httpHeader_idEgov;
	}
	
	public String getIdDominioPrincipale() {
		return idDominioPrincipale;
	}


}
