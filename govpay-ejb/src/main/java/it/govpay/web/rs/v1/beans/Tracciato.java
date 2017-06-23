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
package it.govpay.web.rs.v1.beans;

import java.util.Date;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.model.loader.Tracciato.StatoTracciatoType;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

public class Tracciato {
	
	private static JsonConfig jsonConfig = new JsonConfig();
	
	private long id;
	private Long idApplicazione;
	private Date dataCaricamento;
	private Date dataUltimoAggiornamento;
	private StatoTracciatoType stato;
	private long lineaElaborazione;
	private String descrizioneStato;
	private String nomeFile;
	
	static {
		jsonConfig.setRootClass(Tracciato.class);
	}
	
	public Tracciato() {

	}
	
	public Tracciato(it.govpay.model.loader.Tracciato tracciato) throws ServiceException {
		this.id = tracciato.getId();
		this.idApplicazione = tracciato.getIdApplicazione();
		this.dataCaricamento = tracciato.getDataCaricamento();
		this.dataUltimoAggiornamento = tracciato.getDataUltimoAggiornamento();
		this.stato = tracciato.getStato();
		this.lineaElaborazione = tracciato.getLineaElaborazione();
		this.descrizioneStato = tracciato.getDescrizioneStato();
		this.nomeFile = tracciato.getNomeFile();
	}
	
	public static Tracciato parse(String json) {
		JSONObject jsonObject = JSONObject.fromObject( json );  
		return (Tracciato) JSONObject.toBean( jsonObject, jsonConfig );
	}

	public static JsonConfig getJsonConfig() {
		return jsonConfig;
	}

	public long getId() {
		return id;
	}

	public Long getIdApplicazione() {
		return idApplicazione;
	}

	public Date getDataCaricamento() {
		return dataCaricamento;
	}

	public Date getDataUltimoAggiornamento() {
		return dataUltimoAggiornamento;
	}

	public StatoTracciatoType getStato() {
		return stato;
	}

	public long getLineaElaborazione() {
		return lineaElaborazione;
	}

	public String getDescrizioneStato() {
		return descrizioneStato;
	}

	public String getNomeFile() {
		return nomeFile;
	}
	
	
}
