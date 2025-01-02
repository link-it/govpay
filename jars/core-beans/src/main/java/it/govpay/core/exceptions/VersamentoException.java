/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2025 Link.it srl (http://www.link.it).
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

/**
 * Contiene la definizione base delle eccezioni che possono accadere in fase di lettura di una Pendenza.
 * 
 *  @author Pintori Giuliano (pintori@link.it)
 *
 */
public class VersamentoException extends Exception {

	private static final long serialVersionUID = 1L;

	private String codVersamentoEnte;
	private String codApplicazione;
	private String bundlekey;
	private String codUnivocoDebitore;
	private String codDominio;
	private String iuv;

	public VersamentoException() {
		super();
	}

	public VersamentoException(String codApplicazione, String codVersamentoEnte, String bundlekey, String codUnivocoDebitore,
			String codDominio, String iuv, String message) {
		super(message);
		this.codApplicazione = codApplicazione;
		this.codVersamentoEnte = codVersamentoEnte;
		this.bundlekey = bundlekey;
		this.codUnivocoDebitore = codUnivocoDebitore;
		this.codDominio = codDominio;
		this.iuv = iuv;
	}

	public VersamentoException(String codApplicazione, String codVersamentoEnte, String bundlekey, String codUnivocoDebitore,
			String codDominio, String iuv, Throwable t) {
		super(t);
		this.codApplicazione = codApplicazione;
		this.codVersamentoEnte = codVersamentoEnte;
		this.bundlekey = bundlekey;
		this.codUnivocoDebitore = codUnivocoDebitore;
		this.codDominio = codDominio;
		this.iuv = iuv;
	}

	public VersamentoException(String codApplicazione, String codVersamentoEnte, String bundlekey, String codUnivocoDebitore,
			String codDominio, String iuv, String message, Throwable t) {
		super(message,t);
		this.codApplicazione = codApplicazione;
		this.codVersamentoEnte = codVersamentoEnte;
		this.bundlekey = bundlekey;
		this.codUnivocoDebitore = codUnivocoDebitore;
		this.codDominio = codDominio;
		this.iuv = iuv;
	}


	public String getCodVersamentoEnte() {
		return codVersamentoEnte;
	}

	public void setCodVersamentoEnte(String codVersamentoEnte) {
		this.codVersamentoEnte = codVersamentoEnte;
	}

	public String getCodApplicazione() {
		return codApplicazione;
	}

	public void setCodApplicazione(String codApplicazione) {
		this.codApplicazione = codApplicazione;
	}

	public String getBundlekey() {
		return bundlekey;
	}

	public void setBundlekey(String bundlekey) {
		this.bundlekey = bundlekey;
	}

	public String getCodUnivocoDebitore() {
		return codUnivocoDebitore;
	}

	public void setCodUnivocoDebitore(String codUnivocoDebitore) {
		this.codUnivocoDebitore = codUnivocoDebitore;
	}

	public String getCodDominio() {
		return codDominio;
	}

	public void setCodDominio(String codDominio) {
		this.codDominio = codDominio;
	}

	public String getIuv() {
		return iuv;
	}

	public void setIuv(String iuv) {
		this.iuv = iuv;
	}

}
