/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2026 Link.it srl (http://www.link.it).
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
package it.govpay.core.beans.batch;

import java.io.Serializable;
import java.net.URI;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * Classe che rappresenta un errore REST secondo il formato RFC 7807 (Problem Details for HTTP APIs).
 * <p>
 * Esempio di risposta:
 * <pre>
 * {
 *   "type": "https://example.com/problems/job-already-running",
 *   "title": "Job già in esecuzione",
 *   "status": 409,
 *   "detail": "Il job fdrAcquisitionJob è già in esecuzione sul nodo cluster-1",
 *   "instance": "/api/batch/eseguiJob"
 * }
 * </pre>
 */
@JsonInclude(Include.NON_NULL)
public class Problem implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * URI che identifica il tipo di problema.
     */
    private URI type;

    /**
     * Breve descrizione del problema (human-readable).
     */
    private String title;

    /**
     * Codice di stato HTTP.
     */
    private Integer status;

    /**
     * Descrizione dettagliata del problema specifico.
     */
    private String detail;

    /**
     * URI che identifica l'istanza specifica del problema.
     */
    private URI instance;

	public URI getType() {
		return type;
	}

	public void setType(URI type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public URI getInstance() {
		return instance;
	}

	public void setInstance(URI instance) {
		this.instance = instance;
	}


}
