package it.govpay.core.utils.tasks;

import org.openspcoop2.utils.service.context.IContext;

import it.govpay.core.utils.GovpayConfig;

public class AcquisizioneRendicontazioni extends AbstractTask {

	public AcquisizioneRendicontazioni() {
		super(org.slf4j.LoggerFactory.getLogger(AcquisizioneRendicontazioni.class), CostantiTask.ACQUISIZIONE_RENDICONTAZIONI);
	} 

	@Override
	protected void execTask(IContext ctx) throws Exception {
		this.log.debug("Execuzione task [{}] {}abilitata", this.name, (this.isAbilitato() ? "":"non "));
		if(this.isAbilitato()) {
			it.govpay.core.business.Operazioni.acquisizioneRendicontazioni(ctx);
		}
	}
	
	@Override
	protected boolean isAbilitato() {
		return GovpayConfig.getInstance().isBatchOn() && GovpayConfig.getInstance().isBatchAcquisizioneRendicontazioni();
	}
}
