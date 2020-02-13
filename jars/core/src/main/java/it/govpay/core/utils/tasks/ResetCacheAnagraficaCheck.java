package it.govpay.core.utils.tasks;

import org.openspcoop2.utils.service.context.IContext;

public class ResetCacheAnagraficaCheck extends AbstractTask {

	public ResetCacheAnagraficaCheck() {
		super(org.slf4j.LoggerFactory.getLogger(ResetCacheAnagraficaCheck.class), CostantiTask.RESET_CACHE_ANAGRAFICA_CHECK);
	} 

	@Override
	protected void execTask(IContext ctx) throws Exception {
		it.govpay.core.business.Operazioni.resetCacheAnagraficaCheck(ctx);
	}

}
