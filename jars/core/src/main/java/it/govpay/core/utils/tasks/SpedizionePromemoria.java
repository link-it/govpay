package it.govpay.core.utils.tasks;

import org.openspcoop2.utils.service.context.IContext;

import it.govpay.core.utils.GovpayConfig;

public class SpedizionePromemoria extends AbstractTask {

	public SpedizionePromemoria() {
		super(org.slf4j.LoggerFactory.getLogger(SpedizionePromemoria.class), CostantiTask.SPEDIZIONE_PROMEMORIA);
	} 

	@Override
	protected void execTask(IContext ctx) throws Exception {
		if(GovpayConfig.getInstance().isBatchOn()) {
			it.govpay.core.business.Operazioni.spedizionePromemoria(ctx);
		}
	}

}
