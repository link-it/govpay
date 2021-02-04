package it.govpay.core.utils.tasks;

import org.openspcoop2.utils.service.context.IContext;

import it.govpay.core.utils.GovpayConfig;

public class ElaborazioneTracciatiMyPivotCheck extends AbstractTask {

	public ElaborazioneTracciatiMyPivotCheck() {
		super(org.slf4j.LoggerFactory.getLogger(ElaborazioneTracciatiMyPivotCheck.class), CostantiTask.ELABORAZIONE_TRACCIATI_MY_PIVOT_CHECK);
	} 

	@Override
	protected void execTask(IContext ctx) throws Exception {
		if(GovpayConfig.getInstance().isBatchCaricamentoTracciatiMyPivot()) {
			if(it.govpay.core.business.Operazioni.getEseguiElaborazioneTracciatiMyPivot()) {
				it.govpay.core.business.Operazioni.elaborazioneTracciatiMyPivot(ctx);
				it.govpay.core.business.Operazioni.resetEseguiElaborazioneTracciatiMyPivot();
			}
		}
		
	}
}
