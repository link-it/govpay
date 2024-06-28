package it.govpay.core.utils;

import org.openspcoop2.utils.UtilsException;
import org.openspcoop2.utils.crypt.CryptFactory;
import org.openspcoop2.utils.crypt.CryptType;
import org.openspcoop2.utils.crypt.ICrypt;

public class CryptoUtils {
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
		return cryptPw(plainPw).equals(encodedPw);
	}
}
