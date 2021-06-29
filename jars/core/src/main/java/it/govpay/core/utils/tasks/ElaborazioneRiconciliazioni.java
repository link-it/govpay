package it.govpay.core.utils.tasks;

import org.openspcoop2.utils.service.context.IContext;

import it.govpay.core.utils.GovpayConfig;

public class ElaborazioneRiconciliazioni extends AbstractTask {

	public ElaborazioneRiconciliazioni() {
		super(org.slf4j.LoggerFactory.getLogger(ElaborazioneRiconciliazioni.class), CostantiTask.ELABORAZIONE_RICONCILIAZIONI);
	} 

	@Override
	protected void execTask(IContext ctx) throws Exception {
		if(GovpayConfig.getInstance().isBatchOn()) {
			it.govpay.core.business.Operazioni.elaborazioneRiconciliazioni(ctx);
		}
	}
}
