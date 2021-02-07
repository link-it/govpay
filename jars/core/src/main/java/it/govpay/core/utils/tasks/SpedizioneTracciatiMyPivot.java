package it.govpay.core.utils.tasks;

import org.openspcoop2.utils.service.context.IContext;

import it.govpay.core.utils.GovpayConfig;

public class SpedizioneTracciatiMyPivot extends AbstractTask {

	public SpedizioneTracciatiMyPivot() {
		super(org.slf4j.LoggerFactory.getLogger(SpedizioneTracciatiMyPivot.class), CostantiTask.SPEDIZIONE_NOTIFICHE_MY_PIVOT);
	} 

	@Override
	protected void execTask(IContext ctx) throws Exception {
		if(GovpayConfig.getInstance().isBatchOn() && GovpayConfig.getInstance().isBatchCaricamentoTracciatiMyPivot()) {
			it.govpay.core.business.Operazioni.spedizioneTracciatiMyPivot(ctx);
		}
	}
}
