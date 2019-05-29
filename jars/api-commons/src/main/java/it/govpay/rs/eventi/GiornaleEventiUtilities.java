package it.govpay.rs.eventi;

import org.apache.cxf.message.Message;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.service.beans.HttpMethodEnum;
import org.openspcoop2.utils.service.context.IContext;

import it.govpay.bd.configurazione.model.GdeEvento;
import it.govpay.bd.configurazione.model.GdeInterfaccia;
import it.govpay.bd.configurazione.model.Giornale;
import it.govpay.core.business.GiornaleEventi;
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
		
		return GiornaleEventi.getConfigurazioneComponente(giornaleEventiConfig.getApiNameEnum(), giornale);
	}


	
    public static String safeGet(Message message, String key) {
        if (message == null || !message.containsKey(key)) {
            return null;
        }
        Object value = message.get(key);
        return (value instanceof String) ? value.toString() : null;
    }

	public static boolean dumpEvento(GdeEvento evento, Integer responseCode) {
		return GiornaleEventi.dumpEvento(evento, responseCode);
	}
	
	public static boolean logEvento(GdeEvento evento, Integer responseCode) {
		return GiornaleEventi.logEvento(evento, responseCode);
	}
	
	public static HttpMethodEnum getHttpMethod(String httpMethod) {
		return GiornaleEventi.getHttpMethod(httpMethod);
	}
	
	public static boolean isRequestLettura(HttpMethodEnum httpMethod) {
		return GiornaleEventi.isRequestLettura(httpMethod);
	}
	
	public static boolean isRequestScrittura(HttpMethodEnum httpMethod) {
		return GiornaleEventi.isRequestScrittura(httpMethod);
	}
}
