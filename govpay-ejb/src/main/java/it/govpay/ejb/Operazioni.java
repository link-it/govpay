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
package it.govpay.ejb;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.model.EsitoBase;
import it.govpay.bd.pagamento.EsitiBD;
import it.govpay.business.Mail;
import it.govpay.business.Pagamenti;
import it.govpay.business.RegistroPSP;
import it.govpay.business.Rendicontazioni;
import it.govpay.exception.GovPayException;
import it.govpay.exception.GovPayException.GovPayExceptionEnum;
import it.govpay.thread.InviaEsitoThread;
import it.govpay.thread.ThreadExecutorManager;
import it.govpay.utils.GovPayConfiguration;

import java.util.List;
import java.util.UUID;

import javax.ejb.Schedule;
import javax.ejb.Singleton;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;

@Singleton
public class Operazioni{
	
	private static Logger log = LogManager.getLogger();
	
	@Schedule(hour="*/24", persistent=true)
	public static boolean acquisizioneRendicontazioni(){
		ThreadContext.put("cmd", "AcquisizioneRendicontazioni");
		ThreadContext.put("op", UUID.randomUUID().toString() );
		BasicBD bd = null;
		try {
			bd = BasicBD.getInstance();
			log.info("Acquisizione rendicontazioni");
			new Rendicontazioni(bd).downloadRendicontazioni();
			log.info("Acquisizione rendicontazioni completata");
			return true;
		} catch (Exception e) {
			log.error("Acquisizione rendicontazioni fallita", e);
			return false;
		} finally {
			if(bd != null) bd.closeConnection();
		}
	}
	
	@Schedule(hour="*/24", persistent=true)
	public static boolean aggiornamentoRegistroPsp(){
		ThreadContext.put("cmd", "AggiornamentoRegistroPsp");
		ThreadContext.put("op", UUID.randomUUID().toString() );
		BasicBD bd = null;
		try {
			bd = BasicBD.getInstance();
			new RegistroPSP(bd).aggiornaRegistro();
			return true;
		} catch (Exception e) {
			log.error("Aggiornamento della lista dei PSP fallito", e);
			return false;
		} finally {
			if(bd != null) bd.closeConnection();
		}
	}
	
	@Schedule(hour="*/24", persistent=true)
	public static boolean recuperoRptPendenti(){
		ThreadContext.put("cmd", "RecuperoRptPendenti");
		ThreadContext.put("op", UUID.randomUUID().toString() );
		BasicBD bd = null;
		try {
			bd = BasicBD.getInstance();
			log.info("Recupero Rpt Pendenti");
			new Pagamenti(bd).verificaRptPedenti();
			log.info("Acquisizione Rpt pendenti completata");
			return true;
		} catch (Exception e) {
			log.error("Acquisizione Rpt pendenti fallita", e);
			return false;
		} finally {
			if(bd != null) bd.closeConnection();
		}
	}
	
	@Schedule(hour="*", minute="*/15", persistent=true)
	public static boolean notificheMail(){
		ThreadContext.put("cmd", "NotificheMail");
		ThreadContext.put("op", UUID.randomUUID().toString() );
		log.trace("Avvio batch SpedizioneNotificheMail");
		BasicBD bd = null;
		try {
			bd = BasicBD.getInstance();
			new Mail(bd).notificaMail();
			return true;
		} catch (Exception e) {
			log.error("Spedizione delle notifiche mail fallita", e);
			return false;
		} finally {
			if(bd != null) bd.closeConnection();
		}
	}
	
	@Schedule(hour="*", minute="*/5", persistent=true)
	public static boolean spedizioneEsiti(){
		ThreadContext.put("cmd", "SpedizioneEsiti");
		ThreadContext.put("op", UUID.randomUUID().toString() );
		BasicBD bd = null;
		try {
			bd = BasicBD.getInstance();
			GovPayConfiguration properties = GovPayConfiguration.newInstance();
			log.trace("Spedizione degli esiti (max " + properties.getEsiti_limit() + " esiti)" );
			try {
				EsitiBD esitiBD = new EsitiBD(bd);
				
				List<EsitoBase> esiti = esitiBD.findEsitiDaSpedire(0, properties.getEsiti_limit());
				if(esiti.size() == 0) return true;
				
				log.info("Trovati ["+esiti.size()+"] esiti da spedire");
				
				for(EsitoBase esito: esiti) {
					InviaEsitoThread sender = new InviaEsitoThread(AnagraficaManager.getApplicazione(bd, esito.getIdApplicazione()), esito.getId());
					ThreadExecutorManager.getEsitiPoolExecutor().execute(sender);
				}
			} catch (Exception se) {
				if(bd != null) {
					bd.rollback();
				}
				throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, "Non è stato possibile spedire gli esiti: " + se.getMessage(), se);
			}
			return true;
		} catch (Exception e) {
			log.error("Spedizione degli esiti fallita", e);
			return false;
		} finally {
			if(bd != null) bd.closeConnection();
		}
	}
	
	public static boolean resetCacheAnagrafica(){
		ThreadContext.put("cmd", "ResetCacheAnagrafica");
		ThreadContext.put("op", UUID.randomUUID().toString() );
		try {
			AnagraficaManager.cleanCache();
			return true;
		} catch (Exception e) {
			log.error("Reset cache anagrafica fallita", e);
			return false;
		} 
	}

}