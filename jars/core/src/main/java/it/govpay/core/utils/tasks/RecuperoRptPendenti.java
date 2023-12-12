package it.govpay.core.utils.tasks;

import org.openspcoop2.utils.service.context.IContext;

import it.govpay.core.utils.GovpayConfig;

public class RecuperoRptPendenti extends AbstractTask {

	public RecuperoRptPendenti() {
		super(org.slf4j.LoggerFactory.getLogger(RecuperoRptPendenti.class), CostantiTask.RECUPERO_RPT_PENDENTI);
	} 

	@Override
	protected void execTask(IContext ctx) throws Exception {
		this.log.debug("Execuzione task [{}] {}abilitata", this.name, (this.isAbilitato() ? "":"non "));
		if(GovpayConfig.getInstance().isBatchOn() && GovpayConfig.getInstance().isBatchRecuperoRPTPendenti()) {
			it.govpay.core.business.Operazioni.recuperoRptPendenti(ctx);
		}
	}

	@Override
	protected boolean isAbilitato() {
		return GovpayConfig.getInstance().isBatchOn() && GovpayConfig.getInstance().isBatchRecuperoRPTPendenti();
	}
}
