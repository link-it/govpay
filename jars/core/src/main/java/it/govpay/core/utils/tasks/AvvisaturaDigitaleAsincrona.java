package it.govpay.core.utils.tasks;

import org.openspcoop2.utils.service.context.IContext;

import it.govpay.core.utils.GovpayConfig;

public class AvvisaturaDigitaleAsincrona extends AbstractTask {

	public AvvisaturaDigitaleAsincrona() {
		super(org.slf4j.LoggerFactory.getLogger(AvvisaturaDigitaleAsincrona.class), CostantiTask.AVVISATURA_DIGITALE_ASINCRONA);
	} 

	@Override
	protected void execTask(IContext ctx) throws Exception {
		if(GovpayConfig.getInstance().isBatchOn() && GovpayConfig.getInstance().isAvvisaturaDigitaleAsincronaEnabled()) {
			it.govpay.core.business.Operazioni.avvisaturaDigitale(ctx);
		}
	}
}
