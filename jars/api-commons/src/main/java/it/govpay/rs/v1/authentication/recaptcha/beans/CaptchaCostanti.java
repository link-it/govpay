/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2023 Link.it srl (http://www.link.it).
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
package it.govpay.rs.v1.authentication.recaptcha.beans;

import java.util.regex.Pattern;

public class CaptchaCostanti {

	public static final Pattern RESPONSE_PATTERN = Pattern.compile("[A-Za-z0-9_-]+");
	public static final String PAYLOAD_TEMPLATE = "secret=%s&response=%s&remoteip=%s";
	
	public static final String RE_CAPTCHA_ALIAS = "reCaptchaResponse";
	public static final String RE_CAPTCHA_RESPONSE = "g-recaptcha-response";
}
