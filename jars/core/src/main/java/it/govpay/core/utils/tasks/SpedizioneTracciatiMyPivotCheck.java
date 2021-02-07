package it.govpay.core.utils.tasks;

import org.openspcoop2.utils.service.context.IContext;

import it.govpay.core.utils.GovpayConfig;

public class SpedizioneTracciatiMyPivotCheck extends AbstractTask {

	public SpedizioneTracciatiMyPivotCheck() {
		super(org.slf4j.LoggerFactory.getLogger(SpedizioneTracciatiMyPivotCheck.class), CostantiTask.SPEDIZIONE_NOTIFICHE_MY_PIVOT_CHECK);
	} 

	@Override
	protected void execTask(IContext ctx) throws Exception {
		if(GovpayConfig.getInstance().isBatchCaricamentoTracciatiMyPivot()) {
			if(it.govpay.core.business.Operazioni.getEseguiInvioTracciatiMyPivot()) {
				it.govpay.core.business.Operazioni.spedizioneTracciatiMyPivot(ctx);
				it.govpay.core.business.Operazioni.resetEseguiInvioTracciatiMyPivot();
			}
		}
		
	}
}
