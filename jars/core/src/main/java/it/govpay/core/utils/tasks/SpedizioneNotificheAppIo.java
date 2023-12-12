package it.govpay.core.utils.tasks;

import org.openspcoop2.utils.service.context.IContext;

import it.govpay.core.utils.GovpayConfig;

public class SpedizioneNotificheAppIo extends AbstractTask {

	public SpedizioneNotificheAppIo() {
		super(org.slf4j.LoggerFactory.getLogger(SpedizioneNotificheAppIo.class), CostantiTask.SPEDIZIONE_NOTIFICHE_APP_IO);
	} 

	@Override
	protected void execTask(IContext ctx) throws Exception {
		this.log.debug("Execuzione task [{}] {}abilitata", this.name, (this.isAbilitato() ? "":"non "));
		if(this.isAbilitato()) {
			it.govpay.core.business.Operazioni.spedizioneNotificheAppIO(ctx);
		}
	}

	@Override
	protected boolean isAbilitato() {
		return GovpayConfig.getInstance().isBatchOn() && GovpayConfig.getInstance().isBatchSpedizioneNotificheAppIO();
	}
}
