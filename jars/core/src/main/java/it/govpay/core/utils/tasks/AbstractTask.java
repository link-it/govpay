/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2025 Link.it srl (http://www.link.it).
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3, as published by
 * the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package it.govpay.core.utils.tasks;

import java.text.MessageFormat;

import org.openspcoop2.utils.UtilsException;
import org.openspcoop2.utils.logger.beans.context.core.Operation;
import org.openspcoop2.utils.logger.beans.context.core.Service;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.openspcoop2.utils.service.context.IContext;
import org.openspcoop2.utils.service.context.MD5Constants;
import org.slf4j.Logger;
import org.slf4j.MDC;

import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.logger.MessaggioDiagnosticoUtils;
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
	
	protected AbstractTask(Logger log, String name) {
		this.log = log;
		this.name = name;
	}

	protected abstract void execTask(IContext ctx) throws Exception;
	
	protected abstract boolean isAbilitato();


	public void exec() {
		try {
			this.log.trace("Execuzione task [{}] ...", this.name);
			IContext ctx = this.initBatchContext();
			execTask(ctx);
			this.log.trace("Execuzione task [{}] completata con successo", this.name);
		} catch(Throwable e) {
			this.log.error(MessageFormat.format("Execuzione task [{0}] completata con errore: {1}", this.name, e.getMessage()), e);
		} finally {
			this.logContext(ContextThreadLocal.get());
			ContextThreadLocal.unset();
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
			ContextThreadLocal.set(ctx);
		return ctx;
	}
	
	protected void logContext(IContext ctx) {
		MessaggioDiagnosticoUtils.log(log, ctx);
	} 
}