package it.govpay.core.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import it.govpay.model.Acl;
import org.slf4j.Logger;

public class AclCache {

	private Logger log = null;
	private static AclCache instance = null;
	
	private Map<String, Acl> mapAcls = null;
	
	public AclCache(Logger log) {
		this.log = log;
		this.mapAcls = new HashMap<String,Acl>();
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
			return this.mapAcls.get(key).getRuolo();
		}
		
		return null;
	}
}
