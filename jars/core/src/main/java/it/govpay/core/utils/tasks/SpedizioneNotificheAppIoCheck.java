package it.govpay.core.utils.tasks;

import org.openspcoop2.utils.service.context.IContext;

public class SpedizioneNotificheAppIoCheck extends AbstractTask {

	public SpedizioneNotificheAppIoCheck() {
		super(org.slf4j.LoggerFactory.getLogger(SpedizioneNotificheAppIoCheck.class), CostantiTask.SPEDIZIONE_NOTIFICHE_APP_IO_CHECK);
	} 

	@Override
	protected void execTask(IContext ctx) throws Exception {
		if(it.govpay.core.business.Operazioni.getEseguiInvioNotificheAppIO()) {
			it.govpay.core.business.Operazioni.spedizioneNotificheAppIO(ctx);
			it.govpay.core.business.Operazioni.resetEseguiInvioNotificheAppIO();
		}
	}
}
