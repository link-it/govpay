package it.govpay.core.utils.tasks;

import org.openspcoop2.utils.UtilsException;
import org.openspcoop2.utils.logger.beans.context.core.Operation;
import org.openspcoop2.utils.logger.beans.context.core.Service;
import org.openspcoop2.utils.service.context.IContext;
import org.openspcoop2.utils.service.context.MD5Constants;
import org.slf4j.Logger;
import org.slf4j.MDC;

import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.core.utils.service.context.GpContextFactory;

/***
 * 
 * AbstractTask 
 * 
 * @author pintori
 *
 */
public abstract class AbstractTask {

	protected Logger log;
	protected String name;
	public AbstractTask(Logger log, String name) {
		this.log = log;
		this.name = name;
	}

	protected abstract void execTask(IContext ctx) throws Exception;


	public void exec() {
		try {
			this.log.debug("Execuzione task ["+this.name+"] ...");
			IContext ctx = this.initBatchContext();
			execTask(ctx);
			this.log.debug("Execuzione task ["+this.name+"] completata con successo");
		} catch(Throwable e) {
			this.log.error("Execuzione task ["+this.name+"] completata con errore: " + e.getMessage(), e);
		} finally {
			this.log(GpThreadLocal.get());
			GpThreadLocal.unset();
		}
	}

	protected IContext initBatchContext() throws UtilsException {
		return this.initBatchContext(true);
	}
	
	protected IContext initBatchContext(boolean setContext) throws UtilsException {
		GpContextFactory factory = new GpContextFactory();
		IContext ctx = factory.newBatchContext();
		MDC.put(MD5Constants.OPERATION_ID, this.name);
		MDC.put(MD5Constants.TRANSACTION_ID, ctx.getTransactionId());
		Service service = new Service();
		service.setName(CostantiTask.SERVICE_NAME_TASK);
		service.setType(GpContext.TIPO_SERVIZIO_GOVPAY_OPT);
		ctx.getApplicationContext().getTransaction().setService(service);
		Operation opt = new Operation();
		opt.setName(this.name);
		ctx.getApplicationContext().getTransaction().setOperation(opt);
		if(setContext)
			GpThreadLocal.set(ctx);
		return ctx;
	}
	
	protected void log(IContext ctx) {
		if(ctx != null) {
			try {
				ctx.getApplicationLogger().log();
			} catch (UtilsException e) {
				 this.log.error("Errore durante il log di chiusura operazione: "+e.getMessage(),e);
			}
		}
	} 
}