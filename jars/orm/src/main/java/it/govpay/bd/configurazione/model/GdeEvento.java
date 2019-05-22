package it.govpay.bd.configurazione.model;

import java.io.Serializable;

public class GdeEvento implements Serializable{

	private static final long serialVersionUID = 1L;

	public enum LogEnum { SEMPRE, MAI, SOLO_ERRORE; }

	public enum DumpEnum { SEMPRE, MAI, SOLO_ERRORE; }

	private LogEnum log = null;
	private DumpEnum dump = null;
	
	
	public LogEnum getLog() {
		return log;
	}
	public void setLog(LogEnum log) {
		this.log = log;
	}
	public DumpEnum getDump() {
		return dump;
	}
	public void setDump(DumpEnum dump) {
		this.dump = dump;
	}
}
