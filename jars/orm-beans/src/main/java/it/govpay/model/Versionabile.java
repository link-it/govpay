package it.govpay.model;

import org.apache.commons.lang.ArrayUtils;
import org.openspcoop2.generic_project.exception.ServiceException;

public abstract class Versionabile extends BasicModel {
	
	private static final long serialVersionUID = 1L;
	
	public enum Versione {
		// ATTENZIONE!!!! Per il funzionamento del Compare To, 
		// le versioni DEVONO essere indicate in ordine crescente.
		// Se aggiunta una nuova versione, ricordarsi di aggiornare
		// il metodo getUltimaVersione.
		
		GP_SOAP_01("SOAP","1"),
		GP_REST_01("REST","1"),
		GP_REST_02("REST","2");
		
		private String api;
		private String label;
		public static String[] labels = {"SOAP_1","REST_1","REST_2"};
		
		Versione(String api, String label){
			this.label = label;
			this.api = api;
		}
		
		public String getLabel(){
			return this.label;
		}
		public String getApi(){
			return this.api;
		}
		
		public String getApiLabel(){
			return this.getApi() + "_" + this.getLabel();
		}
		
		public int getVersione() {
			switch (this) {
			case GP_REST_01:
				return 020100;
			case GP_REST_02:
				return 020200;
			case GP_SOAP_01:
				return 010100;
			default:
				break;
			}
			return 0;
		} 
		
		public static Versione toEnum(String versione) throws ServiceException {
			return toEnum(versione.split("_")[0],versione.split("_")[1]);
		}
		
		public static Versione toEnum(String api, String label) throws ServiceException {
			for(Versione p : Versione.values()){
				if(p.getLabel().equals(label) && p.getApi().equals(api)) 
					return p;
			}
				
			throw new ServiceException("Codifica inesistente per Versione. Valore fornito [" + api + "-" + label + "] valori possibili " + ArrayUtils.toString(labels));
		}
		
		public int compareVersione(Versione other) {
			try {
				return this.compareVersione(other, true);
			} catch (ServiceException e) {
				return 0;
			}
		}
		
		public int compareVersione(Versione other, boolean ignoreApi) throws ServiceException {
			// controllo tra API
			if(!ignoreApi && !this.getApi().equals(other.getApi())) 
				throw new ServiceException("Impossibile confrontare due Versioni con API diverse. Versione corrente [" + this.getApi() + "-" + this.getLabel() + "], Versione confrontata ["+ other.getApi() + "-" + other.getLabel() + "].");
			// a questo punto sono sicuro di confrontare solo versioni delle stesse API.
			
			Double mineLabel = Double.parseDouble(this.getLabel());
			Double otherLabel = Double.parseDouble(other.getLabel());
			
			return mineLabel.compareTo(otherLabel);
		}
	}
	
	private Versione versione;

	public Versione getVersione() {
		return this.versione;
	}

	public void setVersione(Versione versione) {
		this.versione = versione;
	}

}
