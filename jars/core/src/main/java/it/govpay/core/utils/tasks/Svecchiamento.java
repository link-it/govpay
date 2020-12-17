package it.govpay.core.utils.tasks;

import org.openspcoop2.utils.service.context.IContext;

import it.govpay.core.utils.GovpayConfig;

public class Svecchiamento extends AbstractTask {

	public Svecchiamento() {
		super(org.slf4j.LoggerFactory.getLogger(Svecchiamento.class), CostantiTask.SVECCHIAMENTO);
	} 

	@Override
	protected void execTask(IContext ctx) throws Exception {
		if(GovpayConfig.getInstance().isBatchOn()) {
			if(GovpayConfig.getInstance().isBatchSvecchiamento()) {
				it.govpay.core.business.Operazioni.eseguiSvecchiamento(ctx);
			}
		}
	}
}
