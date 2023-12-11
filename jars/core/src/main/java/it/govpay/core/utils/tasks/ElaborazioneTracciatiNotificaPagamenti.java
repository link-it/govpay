package it.govpay.core.utils.tasks;

import org.openspcoop2.utils.service.context.IContext;

import it.govpay.core.utils.GovpayConfig;

public class ElaborazioneTracciatiNotificaPagamenti extends AbstractTask {

	public ElaborazioneTracciatiNotificaPagamenti() {
		super(org.slf4j.LoggerFactory.getLogger(ElaborazioneTracciatiNotificaPagamenti.class), CostantiTask.ELABORAZIONE_TRACCIATI_NOTIFICA_PAGAMENTI);
	} 

	@Override
	protected void execTask(IContext ctx) throws Exception {
		this.log.debug("Execuzione task [{}] {}abilitata", this.name, (this.isAbilitato() ? "":"non "));
		if(this.isAbilitato()) {
			it.govpay.core.business.Operazioni.elaborazioneTracciatiNotificaPagamenti(ctx);
		}
	}
	
	@Override
	protected boolean isAbilitato() {
		return GovpayConfig.getInstance().isBatchOn() && GovpayConfig.getInstance().isBatchCaricamentoTracciatiNotificaPagamenti();
	}
}
