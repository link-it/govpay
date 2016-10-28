package it.govpay.model;

import org.apache.commons.lang.ArrayUtils;
import org.openspcoop2.generic_project.exception.ServiceException;

public class Versionabile extends BasicModel {
	
	private static final long serialVersionUID = 1L;
	
	public enum Versione {
		// ATTENZIONE!!!! Per il funzionamento del Compare To, 
		// le versioni DEVONO essere indicate in ordine crescente.
		// Se aggiunta una nuova versione, ricordarsi di aggiornare
		// il metodo getUltimaVersione.
		
		GP_02_01_00("2.1"),
		GP_02_02_00("2.2"),
		GP_02_02_01("2.2.1"),
		GP_02_02_02("2.2.2");
		private String label;
		
		Versione(String label){
			this.label = label;
		}
		
		public String getLabel(){
			return label;
		}
		
		public int getVersione() {
			switch (this) {
			case GP_02_01_00:
				return 020100;
			case GP_02_02_00:
				return 020200;
			case GP_02_02_01:
				return 020201;
			case GP_02_02_02:
				return 020202;
			default:
				break;
			}
			return 0;
		} 
		
		public static Versione getUltimaVersione(){
			return GP_02_02_02;
		}
		
		public static Versione toEnum(String label) throws ServiceException {
			for(Versione p : Versione.values()){
				if(p.getLabel().equals(label))
					return p;
			}
			
			throw new ServiceException("Codifica inesistente per Versione. Valore fornito [" + label + "] valori possibili " + ArrayUtils.toString(Versione.values()));
		}
	}
	
	private Versione versione;

	public Versione getVersione() {
		return versione;
	}

	public void setVersione(Versione versione) {
		this.versione = versione;
	}

}
