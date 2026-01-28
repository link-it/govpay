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
package it.govpay.core.utils;

import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.UtilsException;
import org.openspcoop2.utils.crypt.CryptFactory;
import org.openspcoop2.utils.crypt.CryptType;
import org.openspcoop2.utils.crypt.ICrypt;
import org.slf4j.Logger;

public class CryptoUtils {
	
	private CryptoUtils() {
		// static only
	}

	private static Logger log = LoggerWrapperFactory.getLogger(CryptoUtils.class);

	private static final boolean ABILITA_DEBUG_INFO_CIFRATURA = false;

	public static String cryptPw(String plainPw) throws UtilsException {
		ICrypt crypt = getDefaultCrypt();
		CryptoUtils.logDebug("Cifratura password [{}] con utility di default", plainPw);

		String encodedPw = crypt.crypt(plainPw);

		CryptoUtils.logDebug("Password cifrata [{}]", encodedPw);

		return encodedPw;
	}



	private static ICrypt getDefaultCrypt() {
		return CryptFactory.getCrypt(CryptType.SHA2_BASED_UNIX_CRYPT_SHA512);
	}

	public static boolean checkPw(String plainPw, String encodedPw) {
		CryptoUtils.logDebug("Password da controllare [{}] -> [{}]", plainPw, encodedPw);

		boolean checkOk = getDefaultCrypt().check(plainPw, encodedPw);
		CryptoUtils.logDebug("Check con utility di default: {}", checkOk );

		if(!checkOk && GovpayConfig.getInstance().isControlloPasswordBackwardCompatibilityMD5()) {
			ICrypt oldMD5Crypt = CryptFactory.getOldMD5Crypt(log);
			checkOk = oldMD5Crypt.check(plainPw, encodedPw);
			CryptoUtils.logDebug("Check con utility di backwardcompatibilty: {}", checkOk );
		}

		return checkOk;
	}

	public static void logDebug(String msg, Object ... params) {
		if(ABILITA_DEBUG_INFO_CIFRATURA) {
			log.debug(msg, params);
		}
	}
}
