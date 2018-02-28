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
		
		GP_SOAP_02_01("SOAP","2.1"),
		GP_SOAP_02_02("SOAP","2.2"),
		GP_SOAP_02_03("SOAP","2.3"),
		GP_SOAP_02_05("SOAP","2.5"),
		GP_REST_03_00("REST","3.0");
		
		private String api;
		private String label;
		private static String[] labels = {"SOAP_2.1","SOAP_2.2","SOAP_2.3","SOAP_2.5","REST_3.0"};
		
		Versione(String api, String label){
			this.label = label;
			this.api = api;
		}
		
		public String getLabel(){
			return label;
		}
		public String getApi(){
			return api;
		}
		
		public String getApiLabel(){
			return this.getApi() + "-" + this.getLabel();
		}
		
		public int getVersione() {
			switch (this) {
			case GP_REST_03_00:
				return 020300;
			case GP_SOAP_02_01:
				return 010201;
			case GP_SOAP_02_02:
				return 010202;
			case GP_SOAP_02_03:
				return 010203;
			case GP_SOAP_02_05:
				return 010205;
			default:
				break;
			}
			return 0;
		} 
		
		public static Versione getUltimaVersione(){
			return GP_REST_03_00;
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
		
		public int compareVersione(Versione other) throws ServiceException {
			return compareVersione(other, true);
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
		return versione;
	}

	public void setVersione(Versione versione) {
		this.versione = versione;
	}

}
