/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2024 Link.it srl (http://www.link.it).
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
package it.govpay.bd.exception;

public class VersamentoException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
    /**
     * Versamento inesistente
     * 
     */
    public static final String VER_008 = "VER_008";

    /**
     * Non è possibile annullare un versamento in stato diverso da NON_ESEGUITO
     * 
     */
    public static final String VER_009 = "VER_009";
	
	private String codEsito;
	private String codVersamentoEnte;

	public VersamentoException(){
		super();
	}
	
	public VersamentoException(String codVersamentoEnte, String codEsito){
		super();
		this.codEsito = codEsito;
		this.codVersamentoEnte = codVersamentoEnte;
	}
	
	public VersamentoException(String codVersamentoEnte, String codEsito, Throwable t){
		super(t);
		this.codEsito = codEsito;
		this.codVersamentoEnte = codVersamentoEnte;
	}
	
	public VersamentoException(String codVersamentoEnte, String codEsito, String message,Throwable t){
		super(message,t);
		this.codEsito = codEsito;
		this.codVersamentoEnte = codVersamentoEnte;
	}

	public String getCodEsito() {
		return this.codEsito;
	}

	public void setCodEsito(String codEsito) {
		this.codEsito = codEsito;
	}

	public String getCodVersamentoEnte() {
		return this.codVersamentoEnte;
	}

	public void setCodVersamentoEnte(String codVersamentoEnte) {
		this.codVersamentoEnte = codVersamentoEnte;
	}
	
}
