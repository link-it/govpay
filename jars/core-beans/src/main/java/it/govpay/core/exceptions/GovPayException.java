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
package it.govpay.core.exceptions;

import java.io.Serializable;

import it.govpay.core.beans.EsitoOperazione;
import it.govpay.core.beans.GovPayExceptionProperties;
import it.govpay.core.utils.client.exception.ClientException;
import it.govpay.model.Evento.EsitoEvento;

/**
 * Eccezione lanciata in caso di errore durante i processi interni al sistema.
 * Il dettaglio dell'errore rilevato viene specificato attraverso il field codEsito di tipo {@link EsitoOperazione}.  
 * 
 * @author Pintori Giuliano (pintori@link.it)
 *
 */
public class GovPayException extends Exception {

	private static final long serialVersionUID = 1L;
	private String[] params;
	private EsitoOperazione codEsito;
	private String causa;
	private FaultBean faultBean;
	private final Serializable param;

	public GovPayException(FaultBean faultBean) {
		super();
		this.param = null;
		this.faultBean = faultBean;
		this.setCodEsitoOperazione(EsitoOperazione.NDP_001);
	}

	public GovPayException(String causa, EsitoOperazione codEsito, String ... params) {
		this.params = params;
		this.setCodEsitoOperazione(codEsito);
		this.setCausa(causa);
		this.param = null;
	}

	public GovPayException(String causa, EsitoOperazione codEsito, Throwable e, String ... params) {
		super(e);
		this.params = params;
		this.setCodEsitoOperazione(codEsito);
		this.setCausa(causa);
		this.param = null;
	}

	public GovPayException(Throwable e, String descrizione) {
		super(e);
		this.params = new String[1];
		this.params[0] = descrizione;
		this.setCodEsitoOperazione(EsitoOperazione.INTERNAL);
		this.setCausa(org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace(e));
		this.param = null;
	}

	public GovPayException(EsitoOperazione codEsito, String ... params) {
		this.params = params;
		this.setCodEsitoOperazione(codEsito);
		this.param = null;
	}

	public GovPayException(EsitoOperazione codEsito, Throwable e, String ... params) {
		super(e);
		this.params = params;
		this.setCodEsitoOperazione(codEsito);
		this.setCausa(org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace(e));
		this.param = null;
	}

	public GovPayException(Throwable e) {
		super(e);
		this.setCodEsitoOperazione(EsitoOperazione.INTERNAL);
		this.param = null;
	}

	public GovPayException(GovPayException e, Serializable param) {
		super(e);
		this.param = param;
		this.causa = e.causa;
		this.codEsito = e.codEsito;
		this.faultBean = e.faultBean;
		this.params = e.params;
	}

	public String[] getParams() {
		return this.params;
	}

	public void setParams(String[] params) {
		this.params = params;
	}

	public EsitoOperazione getCodEsito() {
		return this.codEsito;
	}

	public void setCodEsitoOperazione(EsitoOperazione codEsito) {
		this.codEsito = codEsito;
	}

	@Override
	public String getMessage() {
		// Casi speciali da mantenere per retro-compatibilita'
		if (this.codEsito == EsitoOperazione.INTERNAL) {
			if (this.params != null && this.params.length > 0) return this.params[0];
			return super.getMessage();
		}
		if (this.codEsito == EsitoOperazione.NDP_000) {
			return (this.params != null && this.params.length > 0) ? this.params[0] : "";
		}
		if (this.codEsito == EsitoOperazione.NDP_001 && this.faultBean != null) {
			Object[] fb = new Object[]{
					this.faultBean.getFaultCode(),
					this.faultBean.getFaultString(),
					this.faultBean.getDescription() != null ? ": " + this.faultBean.getDescription() : ""
			};
			return GovPayExceptionProperties.getInstance().getMessage(this.codEsito, fb);
		}
		return GovPayExceptionProperties.getInstance().getMessage(this.codEsito, (Object[]) (this.params == null ? new String[0] : this.params));
	}

	public String getCausa() {
		return this.causa;
	}

	public void setCausa(String causa) {
		this.causa = causa;
	}

	public FaultBean getFaultBean() {
		return this.faultBean;
	}

	public void setFaultBean(FaultBean faultBean) {
		this.faultBean = faultBean;
	}

	public String getCodEsitoV3() {
		if (EsitoOperazione.NDP_000.equals(this.codEsito)) {
			return "PAA_NODO_INDISPONIBILE";
		}

		return this.codEsito.name();
	}

	public String getMessageV3() {
		if (EsitoOperazione.NDP_000.equals(this.codEsito)) {
			if(super.getCause() instanceof ClientException)
				return super.getCause().getMessage();
			else 
				return super.getMessage();
		}

		return this.getMessage();
	}

	public int getStatusCode() {
		return GovPayExceptionProperties.getInstance().getStatusCode(this.codEsito, 500);
	}


	public String getDescrizioneEsito() {
		return GovPayExceptionProperties.getInstance().getDescrizioneEsito(this.codEsito);
	}

	public enum CategoriaEnum {
		AUTORIZZAZIONE, RICHIESTA, OPERAZIONE, PAGOPA, INTERNO;
	}

	public CategoriaEnum getCategoria() {
		switch (this.codEsito) {
		case INTERNAL: return CategoriaEnum.INTERNO;
		case AUT_000, AUT_001, AUT_002: return CategoriaEnum.AUTORIZZAZIONE;
		case NDP_000, NDP_001: return CategoriaEnum.PAGOPA; 
		case VER_014: return CategoriaEnum.OPERAZIONE;
		case VAL_000, VAL_001, VAL_003: return CategoriaEnum.INTERNO;
		default: return CategoriaEnum.RICHIESTA;
		}
	}

	public EsitoEvento getTipoNota() {
		if (EsitoOperazione.OK.equals(this.codEsito)) {
			return EsitoEvento.OK;
		}
		return EsitoEvento.FAIL;
	}

	public String getMessageNota() {
		if (EsitoOperazione.NDP_001.equals(this.codEsito)) {
			return "[" + this.faultBean.getFaultCode() + "] " + this.faultBean.getFaultString() + (this.faultBean.getDescription() != null ? ": " + this.faultBean.getDescription() : "");
		}
		return this.getMessageV3();
	}

	public Serializable getParam() {
		return this.param;
	}
}
