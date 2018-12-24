package it.govpay.features;

import java.util.Map;

import org.apache.cxf.ext.logging.event.EventType;
import org.apache.cxf.ext.logging.event.LogEvent;
import org.apache.cxf.ext.logging.event.LogEventSender;
import org.openspcoop2.utils.logger.beans.Property;
import org.openspcoop2.utils.logger.constants.MessageType;

import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;

public class GpLogEventSender implements LogEventSender {

	@Override
	public void send(LogEvent event) {
		GpContext gpContext = GpThreadLocal.get();
		org.openspcoop2.utils.logger.beans.Message msg = new org.openspcoop2.utils.logger.beans.Message();

		if(event.getType().equals(EventType.REQ_IN)) 
			msg.setType(MessageType.REQUEST_IN);
		else
			msg.setType(MessageType.RESPONSE_OUT);

		msg.setContentType(event.getContentType());
		msg.setContent(event.getPayload().getBytes());
		msg.setIdTransaction(event.getExchangeId());
		Map<String, String> headers = event.getHeaders();
		for (Map.Entry<String, String> entry : headers.entrySet())
		{
			String key = entry.getKey();
			String value = entry.getValue();
			msg.addHeader(new Property(key, value));
		}
		gpContext.log(msg);
	}
}
