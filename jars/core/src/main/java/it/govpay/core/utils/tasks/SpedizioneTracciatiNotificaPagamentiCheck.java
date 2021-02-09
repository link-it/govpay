package it.govpay.core.utils.tasks;

import org.openspcoop2.utils.service.context.IContext;

import it.govpay.core.utils.GovpayConfig;

public class SpedizioneTracciatiNotificaPagamentiCheck extends AbstractTask {

	public SpedizioneTracciatiNotificaPagamentiCheck() {
		super(org.slf4j.LoggerFactory.getLogger(SpedizioneTracciatiNotificaPagamentiCheck.class), CostantiTask.SPEDIZIONE_TRACCIATI_NOTIFICA_PAGAMENTI_CHECK);
	} 

	@Override
	protected void execTask(IContext ctx) throws Exception {
		if(GovpayConfig.getInstance().isBatchCaricamentoTracciatiNotificaPagamenti()) {
			if(it.govpay.core.business.Operazioni.getEseguiInvioTracciatiNotificaPagamenti()) {
				it.govpay.core.business.Operazioni.spedizioneTracciatiNotificaPagamenti(ctx);
				it.govpay.core.business.Operazioni.resetEseguiInvioTracciatiNotificaPagamenti();
			}
		}
		
	}
}
