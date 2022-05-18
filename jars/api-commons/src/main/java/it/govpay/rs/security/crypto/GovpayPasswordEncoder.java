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
		// this.log.debug("Password da controllare ["+rawPassword+"] -> ["+encodedPassword+"]");
		
		if (encodedPassword == null || encodedPassword.length() == 0) {
			this.log.warn("Empty encoded password");
			return false;
		}
		
		return this.passwordManager.checkPw(rawPassword.toString(), encodedPassword);
	}

}
