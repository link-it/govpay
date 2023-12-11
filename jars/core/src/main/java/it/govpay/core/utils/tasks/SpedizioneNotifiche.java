package it.govpay.core.utils.tasks;

import org.openspcoop2.utils.service.context.IContext;

import it.govpay.core.utils.GovpayConfig;

public class SpedizioneNotifiche extends AbstractTask {

	public SpedizioneNotifiche() {
		super(org.slf4j.LoggerFactory.getLogger(SpedizioneNotifiche.class), CostantiTask.SPEDIZIONE_NOTIFICHE);
	} 

	@Override
	protected void execTask(IContext ctx) throws Exception {
		this.log.debug("Execuzione task [{}] {}abilitata", this.name, (this.isAbilitato() ? "":"non "));
		if(this.isAbilitato()) {
			it.govpay.core.business.Operazioni.spedizioneNotifiche(ctx);
		}
	}

	@Override
	protected boolean isAbilitato() {
		return GovpayConfig.getInstance().isBatchOn() && GovpayConfig.getInstance().isBatchSpedizioneNotifiche();
	}
}
