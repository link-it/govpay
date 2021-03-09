package it.govpay.core.utils.tasks;

import org.openspcoop2.utils.service.context.IContext;

import it.govpay.core.utils.GovpayConfig;

public class ElaborazioneTracciatiNotificaPagamentiCheck extends AbstractTask {

	public ElaborazioneTracciatiNotificaPagamentiCheck() {
		super(org.slf4j.LoggerFactory.getLogger(ElaborazioneTracciatiNotificaPagamentiCheck.class), CostantiTask.ELABORAZIONE_TRACCIATI_NOTIFICA_PAGAMENTI_CHECK);
	} 

	@Override
	protected void execTask(IContext ctx) throws Exception {
		if(GovpayConfig.getInstance().isBatchCaricamentoTracciatiNotificaPagamenti()) {
			if(it.govpay.core.business.Operazioni.getEseguiElaborazioneTracciatiNotificaPagamenti()) {
				it.govpay.core.business.Operazioni.elaborazioneTracciatiNotificaPagamenti(ctx);
				it.govpay.core.business.Operazioni.resetEseguiElaborazioneTracciatiNotificaPagamenti();
			}
		}
		
	}
}
