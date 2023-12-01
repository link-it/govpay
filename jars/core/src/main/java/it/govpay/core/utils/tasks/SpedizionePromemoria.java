package it.govpay.core.utils.tasks;

import org.openspcoop2.utils.service.context.IContext;

import it.govpay.core.utils.GovpayConfig;

public class SpedizionePromemoria extends AbstractTask {

	public SpedizionePromemoria() {
		super(org.slf4j.LoggerFactory.getLogger(SpedizionePromemoria.class), CostantiTask.SPEDIZIONE_PROMEMORIA);
	} 

	@Override
	protected void execTask(IContext ctx) throws Exception {
		this.log.debug("Execuzione task [{}] {}abilitata", this.name, (this.isAbilitato() ? "":"non "));
		if(this.isAbilitato()) {
			it.govpay.core.business.Operazioni.spedizionePromemoria(ctx);
		}
	}

	@Override
	protected boolean isAbilitato() {
		return GovpayConfig.getInstance().isBatchOn() && GovpayConfig.getInstance().isBatchSpedizionePromemoria();
	}
}
