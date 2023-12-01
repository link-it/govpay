package it.govpay.core.utils.tasks;

import org.openspcoop2.utils.service.context.IContext;

import it.govpay.core.utils.GovpayConfig;

public class ChiusuraRptScadute extends AbstractTask {

	public ChiusuraRptScadute() {
		super(org.slf4j.LoggerFactory.getLogger(ChiusuraRptScadute.class), CostantiTask.CHIUSURA_RPT_SCADUTE);
	} 

	@Override
	protected void execTask(IContext ctx) throws Exception {
		this.log.debug("Execuzione task [{}] {}abilitata", this.name, (this.isAbilitato() ? "":"non "));
		if(this.isAbilitato()) {
			it.govpay.core.business.Operazioni.chiusuraRptScadute(ctx);
		}
	}
	
	@Override
	protected boolean isAbilitato() {
		return GovpayConfig.getInstance().isBatchOn() && GovpayConfig.getInstance().isBatchChiusuraRPTScadute();
	}
}
