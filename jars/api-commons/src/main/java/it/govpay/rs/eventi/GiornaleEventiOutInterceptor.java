package it.govpay.rs.eventi;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.cxf.ext.logging.AbstractLoggingInterceptor;
import org.apache.cxf.ext.logging.event.DefaultLogEventMapper;
import org.apache.cxf.ext.logging.event.LogEvent;
import org.apache.cxf.ext.logging.event.LogEventSender;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.io.CacheAndWriteOutputStream;
import org.apache.cxf.io.CachedOutputStream;
import org.apache.cxf.io.CachedOutputStreamCallback;
import org.apache.cxf.io.CachedWriter;
import org.apache.cxf.message.Exchange;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.service.beans.HttpMethodEnum;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.openspcoop2.utils.service.context.IContext;
import org.slf4j.Logger;

import it.govpay.bd.configurazione.model.GdeInterfaccia;
import it.govpay.bd.model.eventi.DettaglioRichiesta;
import it.govpay.bd.model.eventi.DettaglioRisposta;
import it.govpay.core.dao.configurazione.ConfigurazioneDAO;
import it.govpay.core.dao.configurazione.exception.ConfigurazioneNonTrovataException;
import it.govpay.core.dao.eventi.EventiDAO;
import it.govpay.core.dao.eventi.dto.PutEventoDTO;
import it.govpay.core.dao.eventi.dto.PutEventoDTOResponse;
import it.govpay.core.exceptions.NotAuthenticatedException;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.core.utils.EventoContext.Componente;
import it.govpay.core.utils.GpContext;

public class GiornaleEventiOutInterceptor  extends AbstractPhaseInterceptor<Message>  {

	public static final String CONTENT_SUPPRESSED = AbstractLoggingInterceptor.CONTENT_SUPPRESSED;
	private Logger log = LoggerWrapperFactory.getLogger(GiornaleEventiOutInterceptor.class);
	private GiornaleEventiConfig giornaleEventiConfig = null;
	private EventiDAO eventiDAO = null; 
	private ConfigurazioneDAO configurazioneDAO = null;
	protected boolean logBinary = false;
	protected boolean logMultipart = true;
	protected int limit = AbstractLoggingInterceptor.DEFAULT_LIMIT;
	protected long threshold = AbstractLoggingInterceptor.DEFAULT_THRESHOLD;
	protected LogEventSender sender;

	public GiornaleEventiOutInterceptor() {
		super(Phase.SETUP_ENDING);
		this.eventiDAO = new EventiDAO(); 
		this.configurazioneDAO = new ConfigurazioneDAO();
	}

	protected boolean shouldLogContent(LogEvent event) {
		return event.isBinaryContent() && logBinary
				|| event.isMultipartContent() && logMultipart
				|| !event.isBinaryContent() && !event.isMultipartContent();
	}

	public void setLogBinary(boolean logBinary) {
		this.logBinary = logBinary;
	}

	public void setLogMultipart(boolean logMultipart) {
		this.logMultipart = logMultipart;
	}

	@Override
	public void handleMessage(Message message) throws Fault {
		try {
			if(this.giornaleEventiConfig.isAbilitaGiornaleEventi()) {
				boolean logEvento = false;
				boolean dumpEvento = false;
				IContext context = ContextThreadLocal.get();
				GpContext appContext = (GpContext) context.getApplicationContext();
				String httpMethodS = appContext.getEventoCtx().getMethod();

				Exchange exchange = message.getExchange();

				Message inMessage = exchange.getInMessage();
				HttpMethodEnum httpMethod = GiornaleEventiUtilities.getHttpMethod(httpMethodS);

				Integer responseCode = 200;
				if (!Boolean.TRUE.equals(message.get(Message.DECOUPLED_CHANNEL_MESSAGE))) {
					// avoid logging the default responseCode 200 for the decoupled responses
					responseCode = (Integer)message.get(Message.RESPONSE_CODE);
				}
				
				if(this.giornaleEventiConfig.getApiNameEnum().equals(Componente.API_PAGOPA)) {
				}

				this.log.debug("Log Evento API: ["+this.giornaleEventiConfig.getApiName()+"] Method ["+httpMethodS+"], StatusCode ["+responseCode+"]");

				GdeInterfaccia configurazioneInterfaccia = GiornaleEventiUtilities.getConfigurazioneGiornaleEventi(context, this.configurazioneDAO, this.giornaleEventiConfig);

				if(configurazioneInterfaccia != null) {
					if(GiornaleEventiUtilities.isRequestLettura(httpMethod)) {
						logEvento = GiornaleEventiUtilities.logEvento(configurazioneInterfaccia.getLetture(), responseCode);
						dumpEvento = GiornaleEventiUtilities.dumpEvento(configurazioneInterfaccia.getLetture(), responseCode);
					}

					if(GiornaleEventiUtilities.isRequestScrittura(httpMethod)) {
						logEvento = GiornaleEventiUtilities.logEvento(configurazioneInterfaccia.getScritture(), responseCode);
						dumpEvento = GiornaleEventiUtilities.dumpEvento(configurazioneInterfaccia.getScritture(), responseCode);
					}

					if(logEvento) {
						Date dataIngresso = appContext.getEventoCtx().getDataRichiesta();
						Date dataUscita = new Date();
						// lettura informazioni dalla richiesta
						final LogEvent eventRequest = new DefaultLogEventMapper().map(inMessage);
						DettaglioRichiesta dettaglioRichiesta = new DettaglioRichiesta();
						
						dettaglioRichiesta.setPrincipal(appContext.getEventoCtx().getPrincipal());
						dettaglioRichiesta.setUtente(appContext.getEventoCtx().getUtente());
						dettaglioRichiesta.setUrl(appContext.getEventoCtx().getUrl());
						dettaglioRichiesta.setMethod(appContext.getEventoCtx().getMethod());
						dettaglioRichiesta.setDataOraRichiesta(dataIngresso);
						dettaglioRichiesta.setHeaders(eventRequest.getHeaders());
						

						// lettura informazioni dalla response
						final LogEvent eventResponse = new DefaultLogEventMapper().map(message);
						DettaglioRisposta dettaglioRisposta = new DettaglioRisposta();
						dettaglioRisposta.setHeaders(eventResponse.getHeaders());
						dettaglioRisposta.setStatus(responseCode);
						dettaglioRisposta.setDataOraRisposta(dataUscita);

						appContext.getEventoCtx().setDataRisposta(dataUscita);
						appContext.getEventoCtx().setStatus(responseCode);

						if(dumpEvento) {
							// dump richiesta
							if (shouldLogContent(eventRequest)) {
								addContent(inMessage, eventRequest);
							} else {
								eventRequest.setPayload(AbstractLoggingInterceptor.CONTENT_SUPPRESSED);
							}
							dettaglioRichiesta.setPayload(eventRequest.getPayload());

							// dump risposta
							final OutputStream os = message.getContent(OutputStream.class);
							if (os != null) {
								LoggingCallback callback = new LoggingCallback(this.sender, message, eventResponse, os, this.limit);
								message.setContent(OutputStream.class, createCachingOut(message, os, callback));
								dettaglioRisposta.setPayload(eventResponse.getPayload());
							}
						} 

						appContext.getEventoCtx().setDettaglioRichiesta(dettaglioRichiesta);
						appContext.getEventoCtx().setDettaglioRisposta(dettaglioRisposta);

						// salvataggio evento
						PutEventoDTO putEventoDTO = new PutEventoDTO(context.getAuthentication());
						putEventoDTO.setEvento(appContext.getEventoCtx());
						PutEventoDTOResponse putEventoDTOResponse = this.eventiDAO.inserisciEvento(putEventoDTO);
						putEventoDTOResponse.isCreated(); 
					}
				} else {
					this.log.warn("La configurazione per l'API ["+this.giornaleEventiConfig.getApiName()+"] non e' corretta, salvataggio evento non eseguito."); 
				}
			}
		} catch (ConfigurazioneNonTrovataException e) {
			this.log.error(e.getMessage(),e);
		} catch (NotAuthorizedException e) {
			this.log.error(e.getMessage(),e);
		} catch (NotAuthenticatedException e) {
			this.log.error(e.getMessage(),e);
		} catch (ServiceException e) {
			this.log.error(e.getMessage(),e);
		} finally {

		}
	}

	private void addContent(Message message, final LogEvent event) {
		try {
			CachedOutputStream cos = message.getContent(CachedOutputStream.class);
			if (cos != null) {
				handleOutputStream(event, message, cos);
			} else {
				CachedWriter writer = message.getContent(CachedWriter.class);
				if (writer != null) {
					handleWriter(event, writer);
				}
			}
		} catch (IOException e) {
			throw new Fault(e);
		}
	}

	private void handleOutputStream(final LogEvent event, Message message, CachedOutputStream cos) throws IOException {
		String encoding = (String) message.get(Message.ENCODING);
		if (StringUtils.isEmpty(encoding)) {
			encoding = StandardCharsets.UTF_8.name();
		}
		StringBuilder payload = new StringBuilder();
		cos.writeCacheTo(payload, encoding, this.limit);
		cos.close();
		event.setPayload(payload.toString());
		boolean isTruncated = cos.size() > this.limit && this.limit != -1;
		event.setTruncated(isTruncated);
		event.setFullContentFile(cos.getTempFile());
	}

	private void handleWriter(final LogEvent event, CachedWriter writer) throws IOException {
		boolean isTruncated = writer.size() > this.limit && this.limit != -1;
		StringBuilder payload = new StringBuilder();
		writer.writeCacheTo(payload, this.limit);
		event.setPayload(payload.toString());
		event.setTruncated(isTruncated);
		event.setFullContentFile(writer.getTempFile());
	}

	private OutputStream createCachingOut(Message message, final OutputStream os, CachedOutputStreamCallback callback) {
		final CacheAndWriteOutputStream newOut = new CacheAndWriteOutputStream(os);
		if (this.threshold > 0) {
			newOut.setThreshold(this.threshold);
		}
		if (this.limit > 0) {
			// make the limit for the cache greater than the limit for the truncated payload in the log event, 
			// this is necessary for finding out that the payload was truncated 
			//(see boolean isTruncated = cos.size() > limit && limit != -1;)  in method copyPayload
			newOut.setCacheLimit(getCacheLimit());
		}
		newOut.registerCallback(callback);
		return newOut;
	}

	private int getCacheLimit() {
		if (this.limit == Integer.MAX_VALUE) {
			return this.limit;
		}
		return this.limit + 1;
	}

	public class LoggingCallback implements CachedOutputStreamCallback {

		private final Message message;
		private final LogEvent event;
		private final OutputStream origStream;
		private final int lim;
		@SuppressWarnings("unused")
		private LogEventSender sender;

		public LoggingCallback(final LogEventSender sender, final Message msg, final LogEvent event, final OutputStream os, int limit) {
			this.sender = sender;
			this.message = msg;
			this.origStream = os;
			this.lim = limit == -1 ? Integer.MAX_VALUE : limit;
			this.event = event;
		}

		@Override
		public void onFlush(CachedOutputStream cos) {

		}

		@Override
		public void onClose(CachedOutputStream cos) {

			try {

				if (shouldLogContent(event)) {
					copyPayload(cos, event);
				} else {
					event.setPayload(CONTENT_SUPPRESSED);
				}

				try {
					// empty out the cache
					cos.lockOutputStream();
					cos.resetOut(null, false);
				} catch (Exception ex) {
					// ignore
				}
				this.message.setContent(OutputStream.class, this.origStream);

			} catch (Throwable e) {
				LoggerWrapperFactory.getLogger(GiornaleEventiOutInterceptor.class).error(e.getMessage(),e);
				throw new Fault(e); 
			}
		}

		private void copyPayload(CachedOutputStream cos, final LogEvent event) {
			try {
				String encoding = (String) this.message.get(Message.ENCODING);
				StringBuilder payload = new StringBuilder();
				writePayload(payload, cos, encoding, event.getContentType());
				event.setPayload(payload.toString());
				boolean isTruncated = cos.size() > this.lim && this.lim != -1;
				event.setTruncated(isTruncated);
			} catch (Exception ex) {
				// ignore
			}
		}

		protected void writePayload(StringBuilder builder, CachedOutputStream cos, String encoding, String contentType)
				throws Exception {
			if (StringUtils.isEmpty(encoding)) {
				cos.writeCacheTo(builder, this.lim);
			} else {
				cos.writeCacheTo(builder, encoding, this.lim);
			}
		}
	}

	public GiornaleEventiConfig getGiornaleEventiConfig() {
		return giornaleEventiConfig;
	}

	public void setGiornaleEventiConfig(GiornaleEventiConfig giornaleEventiConfig) {
		this.giornaleEventiConfig = giornaleEventiConfig;
	}

}
