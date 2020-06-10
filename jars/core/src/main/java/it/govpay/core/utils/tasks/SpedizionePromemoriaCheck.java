package it.govpay.core.utils.tasks;

import org.openspcoop2.utils.service.context.IContext;

public class SpedizionePromemoriaCheck extends AbstractTask {

	public SpedizionePromemoriaCheck() {
		super(org.slf4j.LoggerFactory.getLogger(SpedizionePromemoriaCheck.class), CostantiTask.SPEDIZIONE_PROMEMORIA_CHECK);
	} 

	@Override
	protected void execTask(IContext ctx) throws Exception {
		if(it.govpay.core.business.Operazioni.getEseguiInvioPromemoria()) {
			it.govpay.core.business.Operazioni.spedizionePromemoria(ctx);
			it.govpay.core.business.Operazioni.resetEseguiInvioPromemoria();
		}
	}
}
