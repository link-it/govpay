package it.govpay.core.utils.tasks;

import org.openspcoop2.utils.service.context.IContext;

import it.govpay.core.utils.GovpayConfig;

public class GestionePromemoria extends AbstractTask {

	public GestionePromemoria() {
		super(org.slf4j.LoggerFactory.getLogger(GestionePromemoria.class), CostantiTask.GESTIONE_PROMEMORIA);
	} 

	@Override
	protected void execTask(IContext ctx) throws Exception {
		if(GovpayConfig.getInstance().isBatchOn()) {
			it.govpay.core.business.Operazioni.gestionePromemoria(ctx);
		}
	}

}
