package it.govpay.model.reportistica.statistiche;

import java.io.Serializable;
import java.util.Date;

public class DistribuzioneEsiti implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L; 
	private Date data;
	private long eseguiti;
	private long errori;
	private long in_corso;

	public DistribuzioneEsiti(Date data, Long eseguiti, Long errori, Long in_corso) {
		this.data = data;
		this.eseguiti = eseguiti;
		this.errori = errori;
		this.in_corso = in_corso;
	}

	public Date getData() {
		return data;
	}

	public long getEseguiti() {
		return eseguiti;
	}

	public long getErrori() {
		return errori;
	}

	public long getIn_corso() {
		return in_corso;
	}
}