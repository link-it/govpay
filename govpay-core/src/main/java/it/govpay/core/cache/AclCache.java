package it.govpay.core.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;

import it.govpay.model.Acl;

public class AclCache {

	private Logger log = null;
	private static AclCache instance = null;
	
	private Map<String, List<Acl>> mapAcls = null;
	
	public AclCache(Logger log) {
		this.log = log;
		this.mapAcls = new HashMap<String,List<Acl>>();
	}
	
	public static synchronized void newInstance(Logger log) {
		if(AclCache.instance == null)
			AclCache.instance = new AclCache(log);
	}
	
	public static AclCache getInstance() {
		return AclCache.instance;
	}
	
	public List<String> getListaRuoli(){
		return new ArrayList<String>();
	}
	
	public Set<String> getChiavi(){
		return this.mapAcls.keySet();
	}
	
	public String  getRuolo(String key){
		if(this.mapAcls.containsKey(key)) {
			return key;
		}
		
		return null;
	}
}
