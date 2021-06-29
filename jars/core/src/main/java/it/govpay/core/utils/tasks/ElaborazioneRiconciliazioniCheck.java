package it.govpay.core.utils.tasks;

import org.openspcoop2.utils.service.context.IContext;

public class ElaborazioneRiconciliazioniCheck extends AbstractTask {

	public ElaborazioneRiconciliazioniCheck() {
		super(org.slf4j.LoggerFactory.getLogger(ElaborazioneRiconciliazioniCheck.class), CostantiTask.ELABORAZIONE_RICONCILIAZIONI_CHECK);
	} 

	@Override
	protected void execTask(IContext ctx) throws Exception {
		if(it.govpay.core.business.Operazioni.getEseguiElaborazioneRiconciliazioni()) {
			it.govpay.core.business.Operazioni.elaborazioneRiconciliazioni(ctx);
			it.govpay.core.business.Operazioni.resetEseguiElaborazioneRiconciliazioni();
		}
	}
}
