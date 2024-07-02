package it.govpay.core.utils;

import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.UtilsException;
import org.openspcoop2.utils.crypt.CryptFactory;
import org.openspcoop2.utils.crypt.CryptType;
import org.openspcoop2.utils.crypt.ICrypt;

public class CryptoUtils {
	private static org.slf4j.Logger log = LoggerWrapperFactory.getLogger(CryptoUtils.class);

	// TODO da verificare visto che la classe Password di openspcoop2 non ha più la funzione cryptPw
	public static String cryptPw(String plainPw) {
		try {
			ICrypt crypt = CryptFactory.getCrypt(CryptType.SHA2_BASED_UNIX_CRYPT_SHA512);
			return crypt.crypt(plainPw);
		} catch (UtilsException excp) {
			// TODO da rivedere in base all'uso che ne viene fatto
			throw new RuntimeException(excp);
		}
	}

	// TODO da verificare visto che la classe Password di openspcoop2 non ha più la funzione checkPw
	public static boolean checkPw(String plainPw, String encodedPw) {
		log.debug("Check password encoded: [" + encodedPw + "]");
		String sha2CryptedPw = cryptPw(plainPw);
		log.debug("SHA2 password encoded: [" + sha2CryptedPw + "]");
		if (!sha2CryptedPw.equals(encodedPw)) {
			String md5CryptedPw = cryptLibcMD5(plainPw);
			log.debug("MD5 password encoded: [" + md5CryptedPw + "]");
			return md5CryptedPw.equals(encodedPw);
		}
		return false;
	}

	private static String cryptLibcMD5(String plainPw) {
		try {
			ICrypt crypt = CryptFactory.getCrypt(CryptType.LIBC_CRYPT_MD5);
			return crypt.crypt(plainPw);
		} catch (UtilsException excp) {
			// TODO da rivedere in base all'uso che ne viene fatto
			throw new RuntimeException(excp);
		}
	}
}
