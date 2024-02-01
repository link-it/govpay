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
