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
package it.govpay.rs.v1.authentication.recaptcha.exception;

public final class ReCaptchaParametroResponseInvalidException extends Exception {

    private static final long serialVersionUID = 5861310537366287163L;

    public ReCaptchaParametroResponseInvalidException() {
        super();
    }

    public ReCaptchaParametroResponseInvalidException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ReCaptchaParametroResponseInvalidException(final String message) {
        super(message);
    }

    public ReCaptchaParametroResponseInvalidException(final Throwable cause) {
        super(cause);
    }

}
