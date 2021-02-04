package it.govpay.core.utils.tasks;

import org.openspcoop2.utils.service.context.IContext;

import it.govpay.core.utils.GovpayConfig;

public class ElaborazioneTracciatiMyPivot extends AbstractTask {

	public ElaborazioneTracciatiMyPivot() {
		super(org.slf4j.LoggerFactory.getLogger(ElaborazioneTracciatiMyPivot.class), CostantiTask.ELABORAZIONE_TRACCIATI_MY_PIVOT);
	} 

	@Override
	protected void execTask(IContext ctx) throws Exception {
		if(GovpayConfig.getInstance().isBatchOn() && GovpayConfig.getInstance().isBatchCaricamentoTracciatiMyPivot()) {
			it.govpay.core.business.Operazioni.elaborazioneTracciatiMyPivot(ctx);
		}
	}
}
