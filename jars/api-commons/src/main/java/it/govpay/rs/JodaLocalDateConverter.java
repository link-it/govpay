package it.govpay.rs;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.ParamConverter;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.slf4j.Logger;

/**	
 * JodaLocalDateConverter
 *
 * @author Poli Andrea (apoli@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class JodaLocalDateConverter implements ParamConverter<org.joda.time.LocalDate> {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(JodaLocalDateConverter.class);

    @Override
    public org.joda.time.LocalDate fromString(String date) {
        try {
        	DateTimeFormatter formatter = ISODateTimeFormat.dateTimeParser();
        	LocalDate ld = formatter.parseLocalDate(date);
        	return ld;
        } catch (Exception ex) {
        	log.error(ex.getMessage(),ex);
            throw new WebApplicationException("Converter '"+date+"' to org.joda.time.DateTime failed: "+ex,ex,400);
        }
    }

    @Override
    public String toString(org.joda.time.LocalDate t) {
    	 try {
    		 DateTimeFormatter formatter = ISODateTimeFormat.dateTimeParser();
    		 return formatter.print(t);
         } catch (Exception ex) {
         	log.error(ex.getMessage(),ex);
         	throw new WebApplicationException("Converter org.joda.time.DateTime to String failed: "+ex,ex,400);
         }
    }

}
