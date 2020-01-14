package it.govpay.core.utils.client;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.logger.beans.Property;
import org.openspcoop2.utils.transport.http.HttpRequestMethod;
import org.slf4j.Logger;

import it.govpay.bd.BasicBD;
import it.govpay.bd.configurazione.model.AppIO;
import it.govpay.bd.configurazione.model.Giornale;
import it.govpay.bd.configurazione.model.MessageAppIO;
import it.govpay.bd.model.TipoVersamentoDominio;
import it.govpay.bd.model.Versamento;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.utils.EventoContext.Componente;
import it.govpay.core.utils.appio.AppIOUtils;
import it.govpay.core.utils.appio.model.MessageWithCF;
import it.govpay.core.utils.rawutils.ConverterUtils;
import it.govpay.model.Connettore;
import it.govpay.model.Connettore.EnumAuthType;

public class AppIoClient extends BasicClient {
	
	private static Logger log = LoggerWrapperFactory.getLogger(AppIoClient.class);
	private AppIoOperazione operazioneAppIO;
	
	protected AppIoClient(AppIoOperazione operazioneAppIO, AppIO appIo, String operationID, Giornale giornale, BasicBD bd) throws ClientException { 
		super(operazioneAppIO, getConnettore(appIo)); 
		
		this.operazioneAppIO = operazioneAppIO;
		this.operationID = operationID;
		this.componente = Componente.API_PAGOPA;
		this.setGiornale(giornale);
		this.getEventoCtx().setComponente(this.componente);
	}
	
	public byte[] invoke(AppIO appIo, Versamento versamento, TipoVersamentoDominio tipoVersamentoDominio,  BasicBD bd) throws ClientException, ServiceException, GovPayException {
		log.debug("Chiamata all'operazione " + this.operationID + " di AppIO alla URL ("+this.url+")");
		
		List<Property> headerProperties = this.getHeaderProperties(this.operazioneAppIO, tipoVersamentoDominio, bd);
		headerProperties.add(new Property("Accept", "application/json"));
		String jsonBody = this.getPayload(this.operazioneAppIO, appIo, versamento, tipoVersamentoDominio, bd);
		StringBuilder sb = this.getServicePath(this.operazioneAppIO, versamento, bd);
		HttpRequestMethod httpMethod = this.getHttpMethod(this.operazioneAppIO, bd);
		String swaggerOperationID = this.getSwaggerOperationId(this.operazioneAppIO, bd);

		return this.sendJson(sb.toString(), jsonBody, headerProperties, httpMethod, swaggerOperationID);
	}
	
	private String getPayload(AppIoOperazione operazioneAppIO2, AppIO appIo, Versamento versamento, TipoVersamentoDominio tipoVersamentoDominio, BasicBD bd)
			throws ServiceException, GovPayException {
		switch (operazioneAppIO2) {
		case GET_PROFILE:
			return "";
		case MESSAGE:
			String appIOMessaggio = tipoVersamentoDominio.getAppIOMessaggio();
			String appIOOggetto = tipoVersamentoDominio.getAppIOOggetto();
			String appIOTipo = tipoVersamentoDominio.getAppIOTipo();
			
			MessageAppIO appIOMessage = null;
			
			if(appIOMessaggio != null && appIOOggetto != null && appIOTipo != null) {
				appIOMessage = new MessageAppIO();
				appIOMessage.setBody(appIOMessaggio);
				appIOMessage.setSubject(appIOOggetto);
				appIOMessage.setTipo(appIOTipo);
				appIOMessage.setTimeToLive(appIo.getMessage().getTimeToLive());
			}
			
			if(appIOMessage == null) {
				appIOMessage = appIo.getMessage();
			}
			
			MessageWithCF messageWithCF = AppIOUtils.getPostMessage(log, appIOMessage, versamento, bd);
			return ConverterUtils.toJSON(messageWithCF, null);
		}
		
		return null;
	}

	private String getSwaggerOperationId(AppIoOperazione operazioneAppIO2, BasicBD bd) {
		switch (operazioneAppIO2) {
		case GET_PROFILE:
			return AppIoOperazione.GET_PROFILE.toString();
		case MESSAGE:
			return AppIoOperazione.MESSAGE.toString();
		}
		
		return null;
	}

	private List<Property> getHeaderProperties(AppIoOperazione operazioneAppIO, TipoVersamentoDominio tipoVersamentoDominio, BasicBD bd) {
		List<Property> headerProperties = new ArrayList<>();
		
		switch (operazioneAppIO) {
		case GET_PROFILE:
			headerProperties.add(new Property("Ocp-Apim-Subscription-Key", tipoVersamentoDominio.getAppIOAPIKey()));
			break;
		case MESSAGE:
			headerProperties.add(new Property("Ocp-Apim-Subscription-Key", tipoVersamentoDominio.getAppIOAPIKey()));
			break;
		}
		
		return headerProperties;
	}

	private HttpRequestMethod getHttpMethod(AppIoOperazione operazioneAppIO, BasicBD bd) {
		switch (operazioneAppIO) {
		case GET_PROFILE:
			return HttpRequestMethod.GET;
		case MESSAGE:
			return HttpRequestMethod.POST;
		}
		
		return null;
	}

	private StringBuilder getServicePath(AppIoOperazione operazioneAppIO, Versamento versamento, BasicBD bd) {
		StringBuilder sb = new StringBuilder();
		
		switch (operazioneAppIO) {
		case GET_PROFILE:
			sb.append("/profile/").append(versamento.getAnagraficaDebitore().getCodUnivoco());
			break;
		case MESSAGE:
			sb.append("/message");			
			break;
		}
				
		return sb;
	}

	@Override
	public String getOperationId() {
		return this.operationID;
	}

	private static Connettore getConnettore(AppIO appIo) {
		Connettore connettore = new Connettore();
		
		connettore.setUrl(appIo.getUrl());
		connettore.setTipoAutenticazione(EnumAuthType.NONE);
		connettore.setAzioneInUrl(false);
		
		return connettore;
	}
}
