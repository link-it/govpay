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
package it.govpay.rs.security.crypto;

import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.UtilsException;
import org.slf4j.Logger;

import it.govpay.core.utils.CryptoUtils;

/**
 *
 * @author pintori
 *
 */
public class GovpayPasswordEncoder implements org.springframework.security.crypto.password.PasswordEncoder{

	private Logger log = LoggerWrapperFactory.getLogger(GovpayPasswordEncoder.class);

	@Override
	public String encode(CharSequence rawPassword) {
		if(rawPassword != null) {
			try {
				return CryptoUtils.cryptPw(rawPassword.toString());
			} catch (UtilsException e) {
				log.error("Errore durante la cifratura della password: " + e.getMessage() ,e);
			}
		}
		return null;
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		if (encodedPassword == null || encodedPassword.isEmpty()) {
			this.log.warn("Empty encoded password");
			return false;
		}
		
		return CryptoUtils.checkPw(rawPassword.toString(), encodedPassword);
	}

}
