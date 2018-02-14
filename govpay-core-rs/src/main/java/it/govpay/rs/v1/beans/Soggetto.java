
package it.govpay.rs.v1.beans;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

public class Soggetto extends it.govpay.rs.v1.beans.base.Soggetto {

	private static JsonConfig jsonConfig = new JsonConfig();

	static {
		jsonConfig.setRootClass(Soggetto.class);
	}
	public Soggetto() {}
	
	@Override
	public String getJsonIdFilter() {
		return "soggettoVersante";
	}
	
	public static Soggetto parse(String json) {
		JSONObject jsonObject = JSONObject.fromObject( json );  
		return (Soggetto) JSONObject.toBean( jsonObject, jsonConfig );
	}
	
	public Soggetto(it.govpay.model.Anagrafica anagrafica) {

		if(anagrafica.getTipo() != null)
			this.setTipo(TipoEnum.fromValue(anagrafica.getTipo().toString()));

		this.setIdentificativo(anagrafica.getCodUnivoco());
		this.setAnagrafica(anagrafica.getRagioneSociale());
		this.setIndirizzo(anagrafica.getIndirizzo());
		this.setCivico(anagrafica.getCivico());
		this.setCap(anagrafica.getCap());
		this.setLocalita(anagrafica.getLocalita());
		this.setProvincia(anagrafica.getProvincia());
		this.setNazione(anagrafica.getNazione());
		this.setEmail(anagrafica.getEmail());
		this.setCellulare(anagrafica.getCellulare());
		  
	}


}
