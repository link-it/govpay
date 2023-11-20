package it.govpay.core.utils;

import org.openspcoop2.utils.UtilsException;

public class ExceptionUtils {
	
	private ExceptionUtils() {}

	public static boolean existsInnerException(Throwable e,Class<?> found){
		if(found.isAssignableFrom(e.getClass())){
			return true;
		}else{
			if(e.getCause()!=null){
				return existsInnerException(e.getCause(), found);
			}
			else{
				return false;
			}
		}
	}

	public static Throwable getInnerException(Throwable e,Class<?> found){
		if(found.isAssignableFrom(e.getClass())){
			return e;
		}else{
			if(e.getCause()!=null){
				return getInnerException(e.getCause(), found);
			}
			else{
				return null;
			}
		}
	}
	
	public static Throwable estraiInnerExceptionDaUtilsException(UtilsException e) {
		Throwable innerException = null;
		if(ExceptionUtils.existsInnerException(e, javax.mail.internet.AddressException.class)) {
			innerException = ExceptionUtils.getInnerException(e, javax.mail.internet.AddressException.class);
		} else if(ExceptionUtils.existsInnerException(e, javax.mail.SendFailedException.class)) {
			innerException = ExceptionUtils.getInnerException(e, javax.mail.SendFailedException.class);
		}  else if(ExceptionUtils.existsInnerException(e, com.sun.mail.util.MailConnectException.class)) {
			innerException = ExceptionUtils.getInnerException(e, com.sun.mail.util.MailConnectException.class);
		}  else if(ExceptionUtils.existsInnerException(e, java.net.UnknownHostException.class)) {
			innerException = ExceptionUtils.getInnerException(e, java.net.UnknownHostException.class);
		} else if(ExceptionUtils.existsInnerException(e, com.sun.mail.smtp.SMTPAddressFailedException.class)) {
			innerException = ExceptionUtils.getInnerException(e, com.sun.mail.smtp.SMTPAddressFailedException.class);
		}
		return innerException;
	}
}
