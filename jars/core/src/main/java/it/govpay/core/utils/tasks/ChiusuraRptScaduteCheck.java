package it.govpay.core.utils.tasks;

import org.openspcoop2.utils.service.context.IContext;

public class ChiusuraRptScaduteCheck extends AbstractTask {

	public ChiusuraRptScaduteCheck() {
		super(org.slf4j.LoggerFactory.getLogger(ChiusuraRptScaduteCheck.class), CostantiTask.CHIUSURA_RPT_SCADUTE_CHECK);
	} 

	@Override
	protected void execTask(IContext ctx) throws Exception {
		if(it.govpay.core.business.Operazioni.getEseguiElaborazioneChiusuraRptScadute()) {
			it.govpay.core.business.Operazioni.chiusuraRptScadute(ctx);
			it.govpay.core.business.Operazioni.resetEseguiElaborazioneChiusuraRptScadute();
		}
	}
}
