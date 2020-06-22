package it.govpay.core.utils.tasks;

import org.openspcoop2.utils.service.context.IContext;

public class GestionePromemoriaCheck extends AbstractTask {

	public GestionePromemoriaCheck() {
		super(org.slf4j.LoggerFactory.getLogger(GestionePromemoriaCheck.class), CostantiTask.GESTIONE_PROMEMORIA_CHECK);
	} 

	@Override
	protected void execTask(IContext ctx) throws Exception {
		if(it.govpay.core.business.Operazioni.getEseguiGestionePromemoria()) {
			it.govpay.core.business.Operazioni.gestionePromemoria(ctx);
			it.govpay.core.business.Operazioni.resetEseguiGestionePromemoria();
		}
	}
}
