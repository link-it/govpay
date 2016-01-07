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
package it.govpay.thread;

import it.govpay.bd.BasicBD;
import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Esito;
import it.govpay.bd.model.Esito.StatoSpedizione;
import it.govpay.bd.pagamento.EsitiBD;
import it.govpay.utils.IPolicyRispedizione;
import it.govpay.utils.PolicyParameter;
import it.govpay.utils.PolicyRispedizioneTentativi;
import it.govpay.web.wsclient.EsitiClient;
import it.govpay.web.wsclient.EsitiClient.SendEsitoResponse;

import java.util.Date;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;

public class InviaEsitoThread implements Runnable {

	private static Logger log = LogManager.getLogger();
	private Applicazione applicazione;
	private long idEsito;
	private Esito esito;
	private Map<String, String> context;

	public InviaEsitoThread(Applicazione applicazione, long idEsito) {
		this.applicazione = applicazione;
		this.idEsito = idEsito;
	}
	
	public InviaEsitoThread(Applicazione applicazione, Esito esito, Map<String, String> context) {
		this.applicazione = applicazione;
		this.esito = esito;
		this.idEsito = esito.getId();
		this.context = context;
	}

	@Override
	public void run() {
		if(context != null)
			for(String key : context.keySet()) ThreadContext.put(key,  context.get(key));
		
		log.info("Spedisco l'esito [IdEsito: "+this.idEsito+"] all'applicazione [CodApplicazione: " + applicazione.getCodApplicazione() + "]");
		BasicBD bd = null;
		EsitiBD esitiBD = null;
		if(esito == null) {
			try {
				bd = BasicBD.newInstance();
				esitiBD = new EsitiBD(bd);
				esito = esitiBD.getEsito(idEsito);
			} catch (Exception e) {
				log.error("Impossibile stabilire una connessione con il database", e);
				return;
			} finally {
				if(esitiBD != null){
					esitiBD.closeConnection();
				}
			}
		}
		boolean inviato = false;
		try {
			EsitiClient client = new EsitiClient(applicazione);
			SendEsitoResponse response = null;
			try {
				response = client.invoke(applicazione.getVersione(), esito);
			} catch (Exception e) {
				log.error("Errore durante la spedizione dell'esito [idEsito: " + esito.getId() + "]: " + e.getMessage(), e);
				return;
			}
			if(response.getResponseCode() <= 299) {
				log.info("Esito consegnato con successo");
				inviato = true;
			} else {
				log.error("Errore durante la spedizione dell'esito [idEsito: " + esito.getId() + "]. HTTP Response: " + response.getResponseCode());
				inviato = false;
			}
		} catch(Exception e) {
			log.error("Errore durante la spedizione dell'esito [idEsito: " + esito.getId() + "]: " + e.getMessage(), e);
		} finally {
			try {
				if(esito != null) {
					esito.setDataOraUltimaSpedizione(new Date());
					if(inviato) {
						esito.setStatoSpedizione(StatoSpedizione.SPEDITO);
					} else {
						long tentativiSpedizione = esito.getTentativiSpedizione() != null ? esito.getTentativiSpedizione(): 0; 
						esito.setTentativiSpedizione(tentativiSpedizione + 1);
						PolicyParameter parameter = new PolicyParameter();
						parameter.setTentativiSpedizione(esito.getTentativiSpedizione());
						IPolicyRispedizione policy = getPolicyRispedizione(applicazione);
						Date date = new Date(new Date().getTime() + policy.getDelay(parameter));
						esito.setDataOraProssimaSpedizione(date);
						log.info("Spedizione dell'esito [IdEsito: "+esito.getId()+"] rischedulata il " + date);
					}
					bd = BasicBD.newInstance();
					esitiBD = new EsitiBD(bd);
					esitiBD.updateEsito(esito);
				}
			} catch (Exception e) {
				log.error("Errore durante la spedizione dell'esito [idEsito: " + esito.getId() + "]: " + e.getMessage(), e);
			}
			if(esitiBD != null){
				esitiBD.closeConnection();
			}
		}
	}

	private IPolicyRispedizione getPolicyRispedizione(Applicazione applicazione) {
		if(applicazione.getPolicyRispedizione() != null) {
			try {
				Class<? extends IPolicyRispedizione> policy = Class.forName(applicazione.getPolicyRispedizione()).asSubclass(IPolicyRispedizione.class);
				return policy.newInstance();
			} catch(Exception e) {
				log.warn("Impossibile instanziare la policy configurata [idApplicazione: " + applicazione.getId() + "]: " + e, e);
			}
		}
		return new PolicyRispedizioneTentativi();
	}
}
