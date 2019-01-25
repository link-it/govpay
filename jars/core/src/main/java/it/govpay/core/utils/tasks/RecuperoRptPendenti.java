package it.govpay.core.utils.tasks;

import org.openspcoop2.utils.service.context.IContext;

import it.govpay.core.utils.GovpayConfig;

public class RecuperoRptPendenti extends AbstractTask {

	public RecuperoRptPendenti() {
		super(org.slf4j.LoggerFactory.getLogger(RecuperoRptPendenti.class), CostantiTask.RECUPERO_RPT_PENDENTI);
	} 

	@Override
	protected void execTask(IContext ctx) throws Exception {
		if(GovpayConfig.getInstance().isBatchOn()) {
			it.govpay.core.business.Operazioni.recuperoRptPendenti(ctx);
		}
	}

}
