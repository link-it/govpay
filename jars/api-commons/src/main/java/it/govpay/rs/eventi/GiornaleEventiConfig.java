package it.govpay.rs.eventi;

import it.govpay.core.utils.GovpayConfig;
import it.govpay.core.utils.EventoContext.Componente;

public class GiornaleEventiConfig {
	
	private boolean abilitaGiornaleEventi;
	private String apiName;
	
	public GiornaleEventiConfig() {
		this.abilitaGiornaleEventi = GovpayConfig.getInstance().isGiornaleEventiEnabled();
	}

	public String getApiName() {
		return apiName;
	}

	public void setApiName(String apiName) {
		this.apiName = apiName;
	}
	
	public Componente getApiNameEnum() {
		return Componente.valueOf(this.apiName);
	}

	public boolean isAbilitaGiornaleEventi() {
		return abilitaGiornaleEventi;
	}

	public void setAbilitaGiornaleEventi(boolean abilitaGiornaleEventi) {
		this.abilitaGiornaleEventi = abilitaGiornaleEventi;
	}
}
