package it.govpay.features;

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.slf4j.MDC;

import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;

public class ContextInInterceptor extends AbstractPhaseInterceptor<Message> {
	
	public ContextInInterceptor() {
		super(Phase.RECEIVE);
	}

	@Override
	public void handleMessage(Message message) throws Fault {
		try {
			GpContext ctx = new GpContext();
			GpThreadLocal.set(ctx);
			MDC.put("op", ctx.getTransactionId());
		} catch (ServiceException e) {
			throw new Fault(e);
		}
	}
}
