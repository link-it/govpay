package it.govpay.core.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;

import it.govpay.model.Ruolo;

public class RuoliCache {

	private static RuoliCache instance= null;
	private Map<String, Ruolo> mappaRuoli = null;
	
	public static synchronized void newInstance(Logger log) {
		RuoliCache.instance = new RuoliCache();
		// [TODO] caricare ruoli dal db
	}
	
	public static RuoliCache getInstance() {
		return RuoliCache.instance;
	}
	
	public RuoliCache () {
		this.mappaRuoli = new HashMap<String,Ruolo>();
	}
	
	public Set<String> getChiavi() {
		return this.mappaRuoli.keySet();
	}
	
	public Ruolo getRuolo(String key) {
		return this.mappaRuoli.get(key);
	}
	
	public List<Ruolo> getListaRuoli(List<String> ruoliS) {
		List<Ruolo> listaRuoli = new ArrayList<>();
		if(ruoliS != null && ruoliS.size() > 0) {
			for (String codiceRuolo : ruoliS) {
				if(this.mappaRuoli.containsKey(codiceRuolo))
					listaRuoli.add(this.mappaRuoli.get(codiceRuolo));
			}
		}
		return listaRuoli;
	}
	
}
