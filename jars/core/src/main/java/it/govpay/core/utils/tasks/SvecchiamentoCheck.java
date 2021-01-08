package it.govpay.core.utils.tasks;

import org.openspcoop2.utils.service.context.IContext;

import it.govpay.core.utils.GovpayConfig;

public class SvecchiamentoCheck extends AbstractTask {

	public SvecchiamentoCheck() {
		super(org.slf4j.LoggerFactory.getLogger(SvecchiamentoCheck.class), CostantiTask.SVECCHIAMENTO_CHECK);
	} 

	@Override
	protected void execTask(IContext ctx) throws Exception {
		if(GovpayConfig.getInstance().isBatchSvecchiamento()) {
			if(it.govpay.core.business.Operazioni.getEseguiSvecchiamento()) {
				it.govpay.core.business.Operazioni.eseguiSvecchiamento(ctx);
				it.govpay.core.business.Operazioni.resetEseguiSvecchiamento();
			}
		}
	}
}
