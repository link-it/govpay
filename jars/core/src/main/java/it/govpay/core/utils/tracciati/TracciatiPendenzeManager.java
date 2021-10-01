package it.govpay.core.utils.tracciati;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.openspcoop2.utils.LoggerWrapperFactory;
import org.slf4j.Logger;

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
//		log.debug(nomeThread + " ADD Pendenza [IdA2A:"+idA2A+", IdPendenza:"+idPendenza+"]");
		this.listaPendenze.add(idA2A + "@" + idPendenza);
	}
	
	public synchronized boolean checkPendenza(String nomeThread, String idA2A, String idPendenza) {
//		log.debug(nomeThread + " CHECK Pendenza [IdA2A:"+idA2A+", IdPendenza:"+idPendenza+"]");
		return this.listaPendenze.contains(idA2A + "@" + idPendenza);
	}
	
	public synchronized boolean getDocumento(String nomeThread, String idA2A, String codDocumento) {
		log.debug(nomeThread + ": acquisizione lock sul documento ["+codDocumento+"]");
		
		while(this.listaDocumenti.contains(idA2A + "@" + codDocumento)) {
			log.debug(nomeThread + ": Documento gia' in uso in un altro thread ["+codDocumento+"], wait");
			try {
				wait();
			} catch (Exception e) {
				log.error(nomeThread + ": Errore durante la wait: " + e.getMessage(),e); 
			}
		}
		
		log.debug(nomeThread + " acquisizione lock sul documento ["+codDocumento+"], ok");
		this.listaDocumenti.add(idA2A + "@" + codDocumento);
		return true;
	}
	
	public synchronized void releaseDocumento(String nomeThread, String idA2A, String codDocumento) {
		log.debug(nomeThread + " rilascio documento ["+codDocumento+"]");
		this.listaDocumenti.remove((idA2A + "@" + codDocumento));
		notify();
	}
	
	public synchronized void addNumeroAvviso(String numeroAvviso) {
		this.listaNumeriAvviso.add(numeroAvviso);
	}
	
	public List<String> getListaNumeriAvviso() {
		return listaNumeriAvviso;
	}
}
