package it.govpay.pagamento.v3.beans.writer;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.TimeZone;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBException;

import org.openspcoop2.utils.jaxrs.JacksonJsonProvider;
import org.openspcoop2.utils.service.fault.jaxrs.FaultCode;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.gov.digitpa.schemas._2011.pagamenti.CtRicevutaTelematica;
import it.govpay.core.utils.JaxbUtils;

@Provider
public class RtWriter implements javax.ws.rs.ext.MessageBodyWriter<byte[]>{
	
	private TimeZone timeZone = TimeZone.getDefault();
    private String timeZoneId = null;
    public String getTimeZoneId() {
            return this.timeZoneId;
    }
    public void setTimeZoneId(String timeZoneId) {
            this.timeZoneId = timeZoneId;
            this.timeZone = TimeZone.getTimeZone(timeZoneId);
    }
    
	@Override
	public boolean isWriteable(Class<?> paramClass, Type genericType, Annotation[] annotations, MediaType mediaType) {
		if(paramClass.getName().equals(byte[].class.getName())) {
			return true;
		}
		else {
			return false; // il tipo viene gestito correttamente
		}
	}

	@Override
	public void writeTo(byte[] t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream)
			throws IOException, WebApplicationException {
		
		if(mediaType.equals(MediaType.APPLICATION_JSON_TYPE)) {
			ObjectMapper objectMapper = JacksonJsonProvider.getObjectMapper(true,this.timeZone);
			
			try {
				CtRicevutaTelematica rt = JaxbUtils.toRT(t, false);
				objectMapper.writeValue(entityStream, rt);
			} catch (JAXBException | SAXException e) {
				throw FaultCode.ERRORE_INTERNO.toException(e);
			}
		} else {
			entityStream.write(t);
		}
	}

}
