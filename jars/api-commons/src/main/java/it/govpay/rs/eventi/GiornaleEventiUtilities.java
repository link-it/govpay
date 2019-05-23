package it.govpay.rs.eventi;

import org.apache.commons.lang.StringUtils;
import org.apache.cxf.message.Message;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.service.beans.HttpMethodEnum;
import org.openspcoop2.utils.service.context.IContext;

import it.govpay.bd.configurazione.model.GdeEvento;
import it.govpay.bd.configurazione.model.GdeInterfaccia;
import it.govpay.bd.configurazione.model.Giornale;
import it.govpay.core.dao.configurazione.ConfigurazioneDAO;
import it.govpay.core.dao.configurazione.dto.LeggiConfigurazioneDTO;
import it.govpay.core.dao.configurazione.dto.LeggiConfigurazioneDTOResponse;
import it.govpay.core.dao.configurazione.exception.ConfigurazioneNonTrovataException;
import it.govpay.core.exceptions.NotAuthenticatedException;
import it.govpay.core.exceptions.NotAuthorizedException;

public class GiornaleEventiUtilities {
	
	public static GdeInterfaccia getConfigurazioneGiornaleEventi (IContext context, ConfigurazioneDAO configurazioneDAO, GiornaleEventiConfig giornaleEventiConfig) throws ConfigurazioneNonTrovataException, NotAuthorizedException, NotAuthenticatedException, ServiceException {
		LeggiConfigurazioneDTO leggiConfigurazioneDTO = new LeggiConfigurazioneDTO(context.getAuthentication());
		LeggiConfigurazioneDTOResponse configurazione = configurazioneDAO.getConfigurazione(leggiConfigurazioneDTO);
		Giornale giornale = configurazione.getConfigurazione().getGiornale();
		
		switch(giornaleEventiConfig.getApiNameEnum()) {
		case API_BACKOFFICE:
			return giornale.getApiBackoffice();
		case API_ENTE:
			return giornale.getApiEnte();
		case API_PAGAMENTO:
			return giornale.getApiPagamento();
		case API_PAGOPA:
			return giornale.getApiPagoPA();
		case API_RAGIONERIA:
			return giornale.getApiRagioneria();
		case API_PENDENZE:
			return giornale.getApiPendenze();
		}
		
		return null;
	}
	
    public static String safeGet(Message message, String key) {
        if (message == null || !message.containsKey(key)) {
            return null;
        }
        Object value = message.get(key);
        return (value instanceof String) ? value.toString() : null;
    }
    
    public static String getEsito(Integer responseCode) {
    	if(responseCode != null)
    		return responseCode > 399 ? "KO" : "OK";
    		
    	return "KO";
    }

	public static boolean dumpEvento(GdeEvento evento, Integer responseCode) {
		switch (evento.getDump()) {
		case MAI:
			return false;
		case SEMPRE:
			return true;
		case SOLO_ERRORE:
			return responseCode > 399;
		}
		
		return false;
	}
	
	public static boolean logEvento(GdeEvento evento, Integer responseCode) {
		switch (evento.getLog()) {
		case MAI:
			return false;
		case SEMPRE:
			return true;
		case SOLO_ERRORE:
			return responseCode > 399;
		}
		
		return false;
	}
	
	public static HttpMethodEnum getHttpMethod(String httpMethod) {
		if(StringUtils.isNotEmpty(httpMethod)) {
			HttpMethodEnum http = HttpMethodEnum.fromValue(httpMethod);
			return http;
		}
		return null;
	}
	
	public static boolean isRequestLettura(HttpMethodEnum httpMethod) {
		if(httpMethod != null ) {
			switch (httpMethod) {
			case GET:
			case OPTIONS:
			case HEAD:
			case TRACE:
				return true;
			case DELETE:
			case LINK:
			case PATCH:
			case POST:
			case PUT:
			case UNLINK:
				return false;
			}
		}
		return false;
	}
	
	public static boolean isRequestScrittura(HttpMethodEnum httpMethod) {
		if(httpMethod != null ) {
			switch (httpMethod) {
			case PUT:
			case POST:
			case DELETE:
			case PATCH:
			case LINK:
			case UNLINK:
				return true;
			case GET:
			case OPTIONS:
			case HEAD:
			case TRACE:
				return false;
			}
		}
		return false;
	}
}
