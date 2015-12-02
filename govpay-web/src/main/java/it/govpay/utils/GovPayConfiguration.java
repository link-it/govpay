/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2015 Link.it srl (http://www.link.it).
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
package it.govpay.utils;

import java.util.ArrayList;
import java.util.List;

import it.govpay.exception.GovPayException;

public class GovPayConfiguration extends ConfigurationUtils {
	
	private static final String filePropName = "govpay.properties";
	private int 
		threadPoolSize,
		esiti_limit,
		mail_serverPort,
		mail_limit,
		mail_maxRetries;
	
	private String 
		versioneRPT,
		sourceCodeDisclaimer,
		httpHeader_tipoSoggettoFruitore,
		httpHeader_soggettoFruitore,
		httpHeader_tipoSoggettoErogatore,
		httpHeader_soggettoErogatore,
		httpHeader_tipoServizio,
		httpHeader_servizio,
		httpHeader_azione,
		httpHeader_idEgov,
	 	mail_serverHost,
		mail_username,
		mail_password;
	
	private List<String> mail_listErroriSpedizione = new ArrayList<String>();
	
	private static GovPayConfiguration instance;
	
	public static GovPayConfiguration newInstance() throws GovPayException {
		if(instance == null) 
			instance = new GovPayConfiguration();
		
		return instance;
	}
	
	private GovPayConfiguration() throws GovPayException{
		super(filePropName);
		versioneRPT =  getStringValue("govpay.ndp.rpt.versione", "6.0.1");
		sourceCodeDisclaimer = getStringValue("govpay.sourceCodeDisclaimer");
		threadPoolSize = getIntValue("govpay.threadPoolSize");
		
		httpHeader_tipoSoggettoFruitore =  getStringValue("govpay.ndp.spcoopheader.tipoSoggettoFruitore", "X-OpenSPCoop2-TipoMittente");
		httpHeader_soggettoFruitore =  getStringValue("govpay.ndp.spcoopheader.soggettoFruitore", "X-OpenSPCoop2-Mittente");
		httpHeader_tipoSoggettoErogatore =  getStringValue("govpay.ndp.spcoopheader.tipoSoggettoErogatore", "X-OpenSPCoop2-TipoDestinatario");
		httpHeader_soggettoErogatore =  getStringValue("govpay.ndp.spcoopheader.soggettoErogatore", "X-OpenSPCoop2-Destinatario");
		httpHeader_tipoServizio =  getStringValue("govpay.ndp.spcoopheader.tipoServizio", "X-OpenSPCoop2-TipoServizio");
		httpHeader_servizio =  getStringValue("govpay.ndp.spcoopheader.servizio", "X-OpenSPCoop2-Servizio");
		httpHeader_azione =  getStringValue("govpay.ndp.spcoopheader.azione", "X-OpenSPCoop2-Azione");
		httpHeader_idEgov =  getStringValue("govpay.ndp.spcoopheader.idEgov", "X-OpenSPCoop2-IdMessaggio");
		
		esiti_limit = getIntValue("govpay.batch.spedizioneEsiti.limit", 5);
		
		mail_serverHost = getNullableStringValue("govpay.batch.notificaMail.serverHost");
		mail_serverPort = getNullableIntValue("govpay.batch.notificaMail.serverPort");
		mail_username = getNullableStringValue("govpay.batch.notificaMail.username");
		mail_password = getNullableStringValue("govpay.batch.notificaMail.password");
		mail_limit = getNullableIntValue("govpay.batch.notificaMail.limit");
		mail_maxRetries = getNullableIntValue("govpay.batch.notificaMail.maxRetries");
		String listErroriSpedizioneString = getNullableStringValue("govpay.batch.notificaMail.erroriSpedizione");
		if(listErroriSpedizioneString != null) {
			String[] splitList = listErroriSpedizioneString.split("\\|");
		
			for(String error: splitList) {
				this.mail_listErroriSpedizione.add(error);
			}
		}
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

	public String getSourceCodeDisclaimer() {
		return sourceCodeDisclaimer;
	}

	public int getThreadPoolSize() {
		return threadPoolSize;
	}
	
	public int getEsiti_limit() {
		return esiti_limit;
	}

	public int getMail_serverPort() {
		return mail_serverPort;
	}

	public int getMail_limit() {
		return mail_limit;
	}

	public int getMail_maxRetries() {
		return mail_maxRetries;
	}

	public String getMail_serverHost() {
		return mail_serverHost;
	}

	public String getMail_username() {
		return mail_username;
	}

	public String getMail_password() {
		return mail_password;
	}

	public List<String> getMail_listErroriSpedizione() {
		return mail_listErroriSpedizione;
	}

	public GovPayConfiguration getInstance() {
		return instance;
	}
}
