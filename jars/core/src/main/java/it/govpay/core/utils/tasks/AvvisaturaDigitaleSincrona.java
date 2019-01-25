package it.govpay.core.utils.tasks;

import org.openspcoop2.utils.service.context.IContext;

import it.govpay.core.utils.GovpayConfig;

public class AvvisaturaDigitaleSincrona extends AbstractTask {

	public AvvisaturaDigitaleSincrona() {
		super(org.slf4j.LoggerFactory.getLogger(AvvisaturaDigitaleSincrona.class), CostantiTask.AVVISATURA_DIGITALE_SINCRONA);
	} 

	@Override
	protected void execTask(IContext ctx) throws Exception {
		if(GovpayConfig.getInstance().isBatchOn() && GovpayConfig.getInstance().isAvvisaturaDigitaleSincronaEnabled()) {
			it.govpay.core.business.Operazioni.avvisaturaDigitaleModalitaSincrona(ctx);
		}
	}
}
