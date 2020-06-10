package it.govpay.core.utils.tasks;

import org.openspcoop2.utils.service.context.IContext;

public class SpedizioneNotificheCheck extends AbstractTask {

	public SpedizioneNotificheCheck() {
		super(org.slf4j.LoggerFactory.getLogger(SpedizioneNotificheCheck.class), CostantiTask.SPEDIZIONE_NOTIFICHE_CHECK);
	} 

	@Override
	protected void execTask(IContext ctx) throws Exception {
		if(it.govpay.core.business.Operazioni.getEseguiInvioNotifiche()) {
			it.govpay.core.business.Operazioni.spedizioneNotifiche(ctx);
			it.govpay.core.business.Operazioni.resetEseguiInvioNotifiche();
		}
	}

}
