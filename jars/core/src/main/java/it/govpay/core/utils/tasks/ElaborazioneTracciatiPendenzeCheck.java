package it.govpay.core.utils.tasks;

import org.openspcoop2.utils.service.context.IContext;

import it.govpay.core.utils.GovpayConfig;

public class ElaborazioneTracciatiPendenzeCheck extends AbstractTask {

	public ElaborazioneTracciatiPendenzeCheck() {
		super(org.slf4j.LoggerFactory.getLogger(ElaborazioneTracciatiPendenzeCheck.class), CostantiTask.ELABORAZIONE_TRACCIATI_PENDENZE_CHECK);
	} 

	@Override
	protected void execTask(IContext ctx) throws Exception {
		if(GovpayConfig.getInstance().isBatchCaricamentoTracciati()) {
			if(it.govpay.core.business.Operazioni.getEseguiElaborazioneTracciati()) {
				it.govpay.core.business.Operazioni.elaborazioneTracciatiPendenze(ctx);
				it.govpay.core.business.Operazioni.resetEseguiElaborazioneTracciati();
			}
		}
	}
	
	@Override
	protected boolean isAbilitato() {
		return true;
	}
}
