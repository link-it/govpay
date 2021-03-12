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
	
	public synchronized void addPendenza(String idA2A, String idPendenza, String numeroAvviso) {
		//log.debug("AAAAAA " + Thread.currentThread().getName() + " ADD Pendenza [IdA2A:"+idA2A+", IdPendenza:"+idPendenza+"]");
		this.listaPendenze.add(idA2A + "@" + idPendenza);
		this.listaNumeriAvviso.add(numeroAvviso);
	}
	
	public synchronized boolean checkPendenza(String idA2A, String idPendenza) {
		//log.debug("AAAAAA " + Thread.currentThread().getName() + " CHECK Pendenza [IdA2A:"+idA2A+", IdPendenza:"+idPendenza+"]");
		return this.listaPendenze.contains(idA2A + "@" + idPendenza);
	}
	
	public synchronized boolean getDocumento(String idA2A, String codDocumento) {
//		log.debug("AAAAAA " + Thread.currentThread().getName() + " GET Documento ["+codDocumento+"]");
		
		while(this.listaDocumenti.contains(idA2A + "@" + codDocumento)) {
//			log.debug("AAAAAA " + Thread.currentThread().getName() + " GET Documento ["+codDocumento+"], wait");
			try {
				wait();
			} catch (Exception e) {
				log.error("Errore durante la wait: " + e.getMessage(),e); 
			}
		}
		
//		log.debug("AAAAAA " + Thread.currentThread().getName() + " GET Documento ["+codDocumento+"], resume");
		this.listaDocumenti.add(idA2A + "@" + codDocumento);
		return true;
	}
	
	public synchronized void releaseDocumento(String idA2A, String codDocumento) {
//		log.debug("AAAAAA " + Thread.currentThread().getName() + " RELEASE Documento ["+codDocumento+"]");
		this.listaDocumenti.remove((idA2A + "@" + codDocumento));
		notify();
	}
	
	public List<String> getListaNumeriAvviso() {
		return listaNumeriAvviso;
	}
}
