package it.govpay.features;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

import org.apache.cxf.common.injection.NoJSR250Annotations;
import org.apache.cxf.common.util.StringUtils;
import org.apache.cxf.ext.logging.AbstractLoggingInterceptor;
import org.apache.cxf.ext.logging.event.DefaultLogEventMapper;
import org.apache.cxf.ext.logging.event.LogEvent;
import org.apache.cxf.ext.logging.event.LogEventSender;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.io.CachedOutputStream;
import org.apache.cxf.io.CachedWriter;
import org.apache.cxf.message.Message;
import org.openspcoop2.utils.UtilsException;
import org.openspcoop2.utils.logger.beans.Property;
import org.openspcoop2.utils.logger.beans.proxy.Client;
import org.openspcoop2.utils.logger.beans.proxy.Operation;
import org.openspcoop2.utils.logger.beans.proxy.Role;
import org.openspcoop2.utils.logger.beans.proxy.Transaction;
import org.openspcoop2.utils.logger.constants.MessageType;
import org.openspcoop2.utils.logger.constants.proxy.FlowMode;

import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpContext.Context;
import it.govpay.core.utils.GpThreadLocal;

/**
 * 
 */
@NoJSR250Annotations
public class LoggingInInterceptor extends org.apache.cxf.ext.logging.LoggingInInterceptor {

	public LoggingInInterceptor() {
		super();
	}

	public LoggingInInterceptor(PrintWriter writer) {
		super(writer);
	}

	public LoggingInInterceptor(LogEventSender sender) {
		super(sender);
	}

	protected int version;

	@Override
	public void handleMessage(Message message) throws Fault {
		if (isLoggingDisabledNow(message)) {
			return;
		}
		createExchangeId(message);
		final LogEvent event = new DefaultLogEventMapper().map(message);
		if (shouldLogContent(event)) {
			addContent(message, event);
		} else {
			event.setPayload(AbstractLoggingInterceptor.CONTENT_SUPPRESSED);
		}

		//
		// Realizzo la stessa logica di MessageLoggingHandlerUtils
		//

		GpContext ctx = GpThreadLocal.get();

		org.openspcoop2.utils.logger.beans.Message msg = new org.openspcoop2.utils.logger.beans.Message();
		msg.setContent(event.getPayload().getBytes());
		msg.setType(MessageType.REQUEST_IN);
		msg.setContentType(event.getContentType());

		Map<String, String> headers = event.getHeaders();
		for (Map.Entry<String, String> entry : headers.entrySet())
		{
			String key = entry.getKey();
			String value = entry.getValue();
			msg.addHeader(new Property(key, value));
		}

		try {
			ctx.getLogger().log(msg);
		} catch (UtilsException ue) {
			throw new Fault(ue); 
		}

		String servizio = null;
		if (event.getServiceName() != null) {
			servizio = event.getServiceName().getLocalPart();
		}

		Transaction transaction = ctx.getTransaction();
		transaction.setRole(Role.SERVER);

		if (event.getServiceName() != null) 
			transaction.setProtocol("REST");
		else
			transaction.setProtocol("SOAP");

		org.openspcoop2.utils.logger.beans.proxy.Service service = new org.openspcoop2.utils.logger.beans.proxy.Service();
		service.setName(servizio);
		service.setVersion(this.version);
		service.setType(event.getHttpMethod());
		transaction.setService(service);

		Operation operation = new Operation();
		operation.setMode(FlowMode.INPUT_OUTPUT);
		operation.setName(event.getOperationName());
		transaction.setOperation(operation);

		Client client = new Client();
		client.setInvocationEndpoint(event.getAddress());
		if(event.getPortName() != null) client.setInterfaceName(event.getPortName().getLocalPart());
		client.setPrincipal(event.getPrincipal());
		transaction.setClient(client);

		Context context = ctx.getContext();
		context.getRequest().setInDate(new Date());
		if(event.getPayload() != null)
			context.getRequest().setInSize(new Long(event.getPayload().getBytes().length));
		else
			context.getRequest().setInSize(0l);
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
		cos.writeCacheTo(payload, encoding, limit);
		cos.close();
		event.setPayload(payload.toString());
		boolean isTruncated = cos.size() > limit && limit != -1;
		event.setTruncated(isTruncated);
		event.setFullContentFile(cos.getTempFile());
	}

	private void handleWriter(final LogEvent event, CachedWriter writer) throws IOException {
		boolean isTruncated = writer.size() > limit && limit != -1;
		StringBuilder payload = new StringBuilder();
		writer.writeCacheTo(payload, limit);
		event.setPayload(payload.toString());
		event.setTruncated(isTruncated);
		event.setFullContentFile(writer.getTempFile());
	}

}

