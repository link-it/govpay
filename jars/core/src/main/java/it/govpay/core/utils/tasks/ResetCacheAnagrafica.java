package it.govpay.core.utils.tasks;

import org.openspcoop2.utils.service.context.IContext;

public class ResetCacheAnagrafica extends AbstractTask {

	public ResetCacheAnagrafica() {
		super(org.slf4j.LoggerFactory.getLogger(ResetCacheAnagrafica.class), CostantiTask.RESET_CACHE_ANAGRAFICA);
	} 

	@Override
	protected void execTask(IContext ctx) throws Exception {
		it.govpay.core.business.Operazioni.resetCacheAnagrafica(ctx);
	}

}
