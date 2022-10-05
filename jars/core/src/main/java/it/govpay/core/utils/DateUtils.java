/**
 * 
 */
package it.govpay.core.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.openspcoop2.utils.LoggerWrapperFactory;
import org.slf4j.Logger;

/**
 * @author Bussu Giovanni (bussu@link.it)
 * @author  $Author: bussu $
 * @version $ Rev: 12563 $, $Date: 30 mag 2018 $
 * 
 */
public class DateUtils {
	
	public static final String CONTROLLO_SCADENZA = "scadenza";
	public static final String CONTROLLO_VALIDITA = "validita'";

	private static Logger log = LoggerWrapperFactory.getLogger(DateUtils.class);
	
	public static boolean isDataDecorsa(Date daVerificare, String tipoControllo) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 999);
		cal.add(Calendar.DATE, -1);
		Date oggi = cal.getTime();
		
		boolean esito = daVerificare.before(oggi);
		log.debug("Controllo " +tipoControllo + ": data da verificare ["+daVerificare+"] is before oggi ["+oggi+"]: " + esito + ".");
		return esito;

	}
}
