package it.govpay.features;

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;

import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;

public class ContextOutInterceptor extends AbstractPhaseInterceptor<Message> {

	public ContextOutInterceptor() {
		super(Phase.SETUP_ENDING);
	}

	@Override
	public void handleMessage(Message message) throws Fault {
		GpContext gpContext = GpThreadLocal.get();
		gpContext.log();
		GpThreadLocal.unset();
	}

}
