/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2023 Link.it srl (http://www.link.it).
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3, as published by
 * the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package it.govpay.core.utils.tasks;

import org.openspcoop2.utils.service.context.IContext;

import it.govpay.core.utils.GovpayConfig;

public class ElaborazioneTracciatiPendenzeCheck extends AbstractTask {

	public ElaborazioneTracciatiPendenzeCheck() {
		super(org.slf4j.LoggerFactory.getLogger(ElaborazioneTracciatiPendenzeCheck.class), CostantiTask.ELABORAZIONE_TRACCIATI_PENDENZE_CHECK);
	} 

	@Override
	protected void execTask(IContext ctx) throws Exception {
		if(GovpayConfig.getInstance().isBatchCaricamentoTracciati()) {
			if(it.govpay.core.business.Operazioni.getEseguiElaborazioneTracciati()) {
				it.govpay.core.business.Operazioni.elaborazioneTracciatiPendenze(ctx);
				it.govpay.core.business.Operazioni.resetEseguiElaborazioneTracciati();
			}
		}
	}
	
	@Override
	protected boolean isAbilitato() {
		return true;
	}
}
