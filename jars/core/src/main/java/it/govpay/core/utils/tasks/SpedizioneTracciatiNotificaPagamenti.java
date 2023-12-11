package it.govpay.core.utils.tasks;

import org.openspcoop2.utils.service.context.IContext;

import it.govpay.core.utils.GovpayConfig;

public class SpedizioneTracciatiNotificaPagamenti extends AbstractTask {

	public SpedizioneTracciatiNotificaPagamenti() {
		super(org.slf4j.LoggerFactory.getLogger(SpedizioneTracciatiNotificaPagamenti.class), CostantiTask.SPEDIZIONE_TRACCIATI_NOTIFICA_PAGAMENTI);
	} 

	@Override
	protected void execTask(IContext ctx) throws Exception {
		this.log.debug("Execuzione task [{}] {}abilitata", this.name, (this.isAbilitato() ? "":"non "));
		if(this.isAbilitato()) {
			it.govpay.core.business.Operazioni.spedizioneTracciatiNotificaPagamenti(ctx);
		}
	}
	
	@Override
	protected boolean isAbilitato() {
		return GovpayConfig.getInstance().isBatchOn() && GovpayConfig.getInstance().isBatchCaricamentoTracciatiNotificaPagamenti();
	}
}
