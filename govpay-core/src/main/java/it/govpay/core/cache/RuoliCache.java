package it.govpay.core.cache;

import org.slf4j.Logger;

public class RuoliCache {

	private static RuoliCache instance= null;
	
	public static synchronized void newInstance(Logger log) {
		
	}
	
	public static RuoliCache getInstance() {
		return RuoliCache.instance;
	}
}
