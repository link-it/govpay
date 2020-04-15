package it.govpay.core.utils.tasks;

import org.openspcoop2.utils.service.context.IContext;

import it.govpay.core.utils.GovpayConfig;

public class SpedizioneNotificheAppIo extends AbstractTask {

	public SpedizioneNotificheAppIo() {
		super(org.slf4j.LoggerFactory.getLogger(SpedizioneNotificheAppIo.class), CostantiTask.SPEDIZIONE_NOTIFICHE_APP_IO);
	} 

	@Override
	protected void execTask(IContext ctx) throws Exception {
		if(GovpayConfig.getInstance().isBatchOn()) {
			it.govpay.core.business.Operazioni.spedizioneNotificheAppIO(ctx);
		}
	}

}
