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
package it.govpay.model.configurazione;

import java.io.Serializable;

public class Hardening implements Serializable{

	public static final String AUTORIZZAZIONE_UTENZE_ANONIME_HARDENING_ENABLED = "it.govpay.autorizzazione.utenzaAnonima.hardening.enabled";
	public static final String AUTORIZZAZIONE_UTENZE_ANONIME_CAPTCHA_SERVER_URL = "it.govpay.autorizzazione.utenzaAnonima.googleCaptcha.serverURL";
	public static final String AUTORIZZAZIONE_UTENZE_ANONIME_CAPTCHA_SITE_KEY = "it.govpay.autorizzazione.utenzaAnonima.googleCaptcha.siteKey";
	public static final String AUTORIZZAZIONE_UTENZE_ANONIME_CAPTCHA_SECRET_KEY = "it.govpay.autorizzazione.utenzaAnonima.googleCaptcha.secretKey";
	public static final String AUTORIZZAZIONE_UTENZE_ANONIME_CAPTCHA_SOGLIA = "it.govpay.autorizzazione.utenzaAnonima.googleCaptcha.soglia";
	public static final String AUTORIZZAZIONE_UTENZE_ANONIME_CAPTCHA_RESPONSE_PARAMETER = "it.govpay.autorizzazione.utenzaAnonima.googleCaptcha.responseParameter";
	public static final String AUTORIZZAZIONE_UTENZE_ANONIME_CAPTCHA_DENY_ON_FAIL = "it.govpay.autorizzazione.utenzaAnonima.googleCaptcha.denyOnFail";
	public static final String AUTORIZZAZIONE_UTENZE_ANONIME_CAPTCHA_READ_TIMEOUT = "it.govpay.autorizzazione.utenzaAnonima.googleCaptcha.readTimeout";
	public static final String AUTORIZZAZIONE_UTENZE_ANONIME_CAPTCHA_CONNECTION_TIMEOUT = "it.govpay.autorizzazione.utenzaAnonima.googleCaptcha.connectionTimeout";

	private static final long serialVersionUID = 1L;

	private boolean abilitato;
	private GoogleCaptcha googleCatpcha;

	public boolean isAbilitato() {
		return abilitato;
	}
	public void setAbilitato(boolean abilitato) {
		this.abilitato = abilitato;
	}

	public GoogleCaptcha getGoogleCatpcha() {
		return googleCatpcha;
	}
	public void setGoogleCatpcha(GoogleCaptcha googleCatpcha) {
		this.googleCatpcha = googleCatpcha;
	}
}
