/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2025 Link.it srl (http://www.link.it).
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
package it.govpay.core.utils.tracciati;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.openspcoop2.utils.LoggerWrapperFactory;
import org.slf4j.Logger;

import it.govpay.core.utils.LogUtils;

public class TracciatiPendenzeManager {

	private Set<String> listaPendenze = null;
	private Set<String> listaDocumenti = null;
	private List<String> listaNumeriAvviso = null;
	
	private static Logger log = LoggerWrapperFactory.getLogger(TracciatiPendenzeManager.class);
	
	public TracciatiPendenzeManager() {
		this.listaPendenze = new HashSet<>();
		this.listaDocumenti = new HashSet<>();
		this.listaNumeriAvviso = new ArrayList<>();
	}
	
	public synchronized void addPendenza(String nomeThread, String idA2A, String idPendenza) {
		LogUtils.logTrace(log, nomeThread + " ADD Pendenza [IdA2A:"+idA2A+", IdPendenza:"+idPendenza+"]");
		this.listaPendenze.add(idA2A + "@" + idPendenza);
	}
	
	public synchronized boolean checkPendenza(String nomeThread, String idA2A, String idPendenza) {
		LogUtils.logTrace(log, nomeThread + " CHECK Pendenza [IdA2A:"+idA2A+", IdPendenza:"+idPendenza+"]");
		return this.listaPendenze.contains(idA2A + "@" + idPendenza);
	}
	
	public synchronized boolean getDocumento(String nomeThread, String idA2A, String codDocumento) {
		LogUtils.logDebug(log, nomeThread + ": acquisizione lock sul documento ["+codDocumento+"]");
		
		while(this.listaDocumenti.contains(idA2A + "@" + codDocumento)) {
			LogUtils.logDebug(log, nomeThread + ": Documento gia' in uso in un altro thread ["+codDocumento+"], wait");
			try {
				wait();
			} catch (InterruptedException e) {
				log.error(nomeThread + ": Errore durante la wait: " + e.getMessage(),e); 
				// Restore interrupted state...
			    Thread.currentThread().interrupt();
			}
		}
		
		LogUtils.logDebug(log, nomeThread + " acquisizione lock sul documento ["+codDocumento+"], ok");
		this.listaDocumenti.add(idA2A + "@" + codDocumento);
		return true;
	}
	
	public synchronized void releaseDocumento(String nomeThread, String idA2A, String codDocumento) {
		LogUtils.logDebug(log, nomeThread + " rilascio documento ["+codDocumento+"]");
		this.listaDocumenti.remove((idA2A + "@" + codDocumento));
		notifyAll();
	}
	
	public synchronized void addNumeroAvviso(String numeroAvviso) {
		this.listaNumeriAvviso.add(numeroAvviso);
	}
	
	public List<String> getListaNumeriAvviso() {
		return listaNumeriAvviso;
	}
}
