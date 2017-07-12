/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2017 Link.it srl (http://www.link.it).
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3, as published by
 * the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
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