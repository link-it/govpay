package it.govpay.netpay.v1.client;

import java.util.Date;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;

import it.govpay.core.beans.EventoContext;
import it.govpay.core.beans.EventoContext.Componente;
import it.govpay.core.utils.SimpleDateFormatUtils;
import it.govpay.core.utils.client.HttpMethod;
import it.govpay.core.utils.eventi.EventiUtils;
import it.govpay.model.Connettore;
import it.govpay.model.Versionabile.Versione;
import it.govpay.model.configurazione.GdeInterfaccia;
import it.govpay.model.configurazione.Giornale;
import it.govpay.model.eventi.DettaglioRichiesta;
import it.govpay.model.eventi.DettaglioRisposta;
import it.govpay.netpay.v1.verifica.VerificaClient;

public class BasicClient {

	private static ObjectMapper mapper;
	static {
		mapper = new ObjectMapper();
		mapper.registerModule(new JaxbAnnotationModule());
		mapper.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		mapper.setDateFormat(SimpleDateFormatUtils.newSimpleDateFormatSoloData());
	}
	
	protected Logger log = LoggerFactory.getLogger(VerificaClient.class);
	protected String codApplicazione;
	protected Giornale giornale;
	protected String tipoConnettore;
	protected boolean logEvento;
	protected Componente componente;
	protected Connettore connettore;
	protected Versione versione;
	protected EventoContext eventoCtx;
	
	protected void popolaContextEvento(int responseCode, byte[] dumpRequest, byte[] dumpResponse, HttpHeaders requestHeaders, HttpHeaders responseHeaders) {
		HttpMethod httpMethod = HttpMethod.POST;
		if(this.logEvento) {
			boolean logEvento = false;
			boolean dumpEvento = false;
			GdeInterfaccia configurazioneInterfaccia = giornale.getApiEnte();

			log.debug("Log Evento Client: ["+this.componente +"], Operazione ["+this.getEventoCtx().getTipoEvento()+"], Method ["+httpMethod+"], Url ["+this.connettore.getUrl()+"], StatusCode ["+responseCode+"]");

			if(configurazioneInterfaccia != null) {
				try {
					String msg = mapper.writeValueAsString(configurazioneInterfaccia);
					log.debug("Configurazione Giornale Eventi API: ["+this.componente+"]: " + msg);
				} catch (JsonProcessingException e) {
					log.error("Errore durante il log della configurazione giornale eventi: " +e.getMessage(), e);
				}
				
				if(EventiUtils.isRequestLettura(httpMethod, this.componente, this.getEventoCtx().getTipoEvento())) {
					logEvento = EventiUtils.logEvento(configurazioneInterfaccia.getLetture(), responseCode);
					dumpEvento = EventiUtils.dumpEvento(configurazioneInterfaccia.getLetture(), responseCode);
					log.debug("Tipo Operazione 'Lettura', Log ["+logEvento+"], Dump ["+dumpEvento+"].");
				} else if(EventiUtils.isRequestScrittura(httpMethod, this.componente, this.getEventoCtx().getTipoEvento())) {
					logEvento = EventiUtils.logEvento(configurazioneInterfaccia.getScritture(), responseCode);
					dumpEvento = EventiUtils.dumpEvento(configurazioneInterfaccia.getScritture(), responseCode);
					log.debug("Tipo Operazione 'Scrittura', Log ["+logEvento+"], Dump ["+dumpEvento+"].");
				} else {
					log.debug("Tipo Operazione non riconosciuta, l'evento non verra' salvato.");
				}

				this.getEventoCtx().setRegistraEvento(logEvento);

				if(logEvento) {
					Date dataIngresso = this.getEventoCtx().getDataRichiesta();
					Date dataUscita = new Date();
					// lettura informazioni dalla richiesta
					DettaglioRichiesta dettaglioRichiesta = new DettaglioRichiesta();

					dettaglioRichiesta.setPrincipal(this.getEventoCtx().getPrincipal());
					dettaglioRichiesta.setUtente(this.getEventoCtx().getUtente());
					dettaglioRichiesta.setUrl(this.getEventoCtx().getUrl());
					dettaglioRichiesta.setMethod(httpMethod.toString());
					dettaglioRichiesta.setDataOraRichiesta(dataIngresso);
					dettaglioRichiesta.setHeadersFromMap(responseHeaders.toSingleValueMap());


					// lettura informazioni dalla response
					DettaglioRisposta dettaglioRisposta = new DettaglioRisposta();
					dettaglioRisposta.setHeadersFromMap(responseHeaders.toSingleValueMap());
					dettaglioRisposta.setStatus(responseCode);
					dettaglioRisposta.setDataOraRisposta(dataUscita);

					this.getEventoCtx().setDataRisposta(dataUscita);
					this.getEventoCtx().setStatus(responseCode);
					this.getEventoCtx().setSottotipoEsito(responseCode + "");

					if(dumpEvento) {
						Base64 base = new Base64();
						// dump richiesta
						if(dumpRequest != null && dumpRequest.length > 0)
							dettaglioRichiesta.setPayload(base.encodeToString(dumpRequest));

						// dump risposta
						if(dumpResponse != null && dumpResponse.length > 0)
							dettaglioRisposta.setPayload(base.encodeToString(dumpResponse));
					} 

					this.getEventoCtx().setDettaglioRichiesta(dettaglioRichiesta);
					this.getEventoCtx().setDettaglioRisposta(dettaglioRisposta);
				}
			} else {
				log.warn("La configurazione per l'API ["+this.componente+"] non e' corretta, salvataggio evento non eseguito."); 
			}
		}
	}
	
	public EventoContext getEventoCtx() {
		return this.eventoCtx;
	}
	
	protected byte[] getDumpRequest(Object requestBody) {
		try {
			return mapper.writeValueAsBytes(requestBody);
		} catch (JsonProcessingException e) {
			log.error("Errore durante la serializzazione della request per il giornale degli eventi: " +e.getMessage(), e);
		}
		return null;
	}
	
	protected byte[] getDumpResponse(Object responseBody) {
		try {
			return mapper.writeValueAsBytes(responseBody);
		} catch (JsonProcessingException e) {
			log.error("Errore durante la serializzazione della response per il giornale degli eventi: " +e.getMessage(), e);
		}
		return null;
	}
}
