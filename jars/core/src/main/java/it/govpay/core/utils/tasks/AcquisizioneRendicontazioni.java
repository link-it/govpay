package it.govpay.core.utils.tasks;

import org.openspcoop2.utils.service.context.IContext;

import it.govpay.core.utils.GovpayConfig;

public class AcquisizioneRendicontazioni extends AbstractTask {

	public AcquisizioneRendicontazioni() {
		super(org.slf4j.LoggerFactory.getLogger(AcquisizioneRendicontazioni.class), CostantiTask.ACQUISIZIONE_RENDICONTAZIONI);
	} 

	@Override
	protected void execTask(IContext ctx) throws Exception {
		if(GovpayConfig.getInstance().isBatchOn()) {
			it.govpay.core.business.Operazioni.acquisizioneRendicontazioni(ctx);
		}
	}
}
