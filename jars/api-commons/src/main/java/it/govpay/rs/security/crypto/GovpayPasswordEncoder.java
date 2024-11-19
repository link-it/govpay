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
package it.govpay.rs.security.crypto;

import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.crypt.Password;
import org.slf4j.Logger;

/**
 *
 * @author pintori
 *
 */
public class GovpayPasswordEncoder implements org.springframework.security.crypto.password.PasswordEncoder{

	private Password passwordManager = new Password();
	private Logger log = LoggerWrapperFactory.getLogger(GovpayPasswordEncoder.class);

	@Override
	public String encode(CharSequence rawPassword) {
		if(rawPassword != null) {
			return this.passwordManager.cryptPw(rawPassword.toString());
		}
		return null;
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		// this.log.debug("Password da controllare ["+rawPassword+"] -> ["+encodedPassword+"]")

		if (encodedPassword == null || encodedPassword.length() == 0) {
			this.log.warn("Empty encoded password");
			return false;
		}

		return this.passwordManager.checkPw(rawPassword.toString(), encodedPassword);
	}

}
