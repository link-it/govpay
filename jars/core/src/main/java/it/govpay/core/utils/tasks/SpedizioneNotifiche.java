package it.govpay.core.utils.tasks;

import org.openspcoop2.utils.service.context.IContext;

import it.govpay.core.utils.GovpayConfig;

public class SpedizioneNotifiche extends AbstractTask {

	public SpedizioneNotifiche() {
		super(org.slf4j.LoggerFactory.getLogger(SpedizioneNotifiche.class), CostantiTask.SPEDIZIONE_NOTIFICHE);
	} 

	@Override
	protected void execTask(IContext ctx) throws Exception {
		if(GovpayConfig.getInstance().isBatchOn()) {
			it.govpay.core.business.Operazioni.spedizioneNotifiche(ctx);
		}
	}

}
