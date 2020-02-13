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

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.jaxrs.JacksonJsonProvider;
import org.openspcoop2.utils.json.ValidationException;
import org.openspcoop2.utils.service.fault.jaxrs.FaultCode;
import org.springframework.security.core.context.SecurityContextHolder;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.govpay.core.dao.anagrafica.dto.GetAvvisoDTO;
import it.govpay.core.dao.anagrafica.dto.GetAvvisoDTO.FormatoAvviso;
import it.govpay.core.dao.anagrafica.dto.GetAvvisoDTOResponse;
import it.govpay.core.dao.pagamenti.AvvisiDAO;
import it.govpay.core.dao.pagamenti.exception.PendenzaNonTrovataException;
import it.govpay.core.exceptions.NotAuthenticatedException;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.pagamento.v3.beans.Avviso;

@Provider
public class AvvisoWriter implements javax.ws.rs.ext.MessageBodyWriter<Avviso> {

	MediaType APPLICATION_PDF_TYPE = new MediaType("application", "pdf");
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
		if(paramClass.getName().equals(Avviso.class.getName())) {
			return true;
		}
		else {
			return false; 
		}
	}

	@Override
	public void writeTo(Avviso t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream)
					throws IOException, WebApplicationException {
		ObjectMapper objectMapper = JacksonJsonProvider.getObjectMapper(true,this.timeZone);
		if(mediaType.equals(APPLICATION_PDF_TYPE)) {
			try {
				GetAvvisoDTO getAvvisoDTO = new GetAvvisoDTO(SecurityContextHolder.getContext().getAuthentication(), t.getDominio().getIdDominio());
				//getAvvisoDTO.setCfDebitore(idPagatore);
				getAvvisoDTO.setNumeroAvviso(t.getNumeroAvviso());
				getAvvisoDTO.setFormato(FormatoAvviso.PDF);

				AvvisiDAO avvisiDAO = new AvvisiDAO();
				GetAvvisoDTOResponse getAvvisoDTOResponse;

				getAvvisoDTOResponse = avvisiDAO.getAvviso(getAvvisoDTO);
				objectMapper.writeValue(entityStream, getAvvisoDTOResponse.getAvvisoPdf());
			} catch (PendenzaNonTrovataException | NotAuthorizedException | NotAuthenticatedException | ServiceException | ValidationException e) {
				throw FaultCode.ERRORE_INTERNO.toException(e);
			}
		} else {
			objectMapper.writeValue(entityStream, t);
		}
	}

}
