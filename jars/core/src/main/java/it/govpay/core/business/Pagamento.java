/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2024 Link.it srl (http://www.link.it).
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
package it.govpay.core.business;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.service.context.IContext;
import org.slf4j.Logger;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.anagrafica.DominiBD;
import it.govpay.bd.anagrafica.filters.DominioFilter;
import it.govpay.bd.model.PagamentoPortale;
import it.govpay.bd.model.PagamentoPortale.STATO;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.pagamento.PagamentiPortaleBD;
import it.govpay.bd.pagamento.RptBD;
import it.govpay.core.beans.EsitoOperazione;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.utils.GovpayConfig;
import it.govpay.core.utils.LogUtils;
import it.govpay.model.Rpt.StatoRpt;

public class Pagamento   {

	private static Logger log = LoggerWrapperFactory.getLogger(Pagamento.class);

	public Pagamento() {
		// donothing
	}
	
	public String chiusuraRPTScadute(IContext ctx, Date dataUltimoCheck) throws GovPayException {
		BDConfigWrapper configWrapper = new BDConfigWrapper(ctx.getTransactionId(), true);
		List<String> response = new ArrayList<>();
		RptBD rptBD = null;
		try {
			DominiBD dominiBD = new DominiBD(configWrapper);
			DominioFilter filter = dominiBD.newFilter();
			
			List<String> codDomini  = dominiBD.findAllCodDominio(filter);
			
			rptBD = new RptBD(configWrapper);
			
			rptBD.setupConnection(configWrapper.getTransactionID());
			
			rptBD.setAtomica(false);
			
			PagamentiPortaleBD ppbd = new PagamentiPortaleBD(rptBD);
			
			ppbd.setAtomica(false);

			for (String codDominio : codDomini) {
				int offset = 0;
				int limit = 100;
				List<Rpt> rtList = rptBD.getRptScadute(codDominio, GovpayConfig.getInstance().getTimeoutPendentiModello3SANP24Mins(), offset, limit, dataUltimoCheck);
				log.trace("Identificate su GovPay per il Dominio [{}]: {} transazioni scadute da piu'' di [{}] minuti.", codDominio, rtList.size(), GovpayConfig.getInstance().getTimeoutPendentiModello3SANP24Mins());
				do {
					if(!rtList.isEmpty()) {
						for (Rpt rpt : rtList) {
							try {
								rptBD.setAutoCommit(false);

								rpt.setStato(StatoRpt.RPT_SCADUTA);
								rpt.setDescrizioneStato(MessageFormat.format("Tentativo di pagamento scaduto dopo timeout di {0} minuti.", GovpayConfig.getInstance().getTimeoutPendentiModello3SANP24Mins()));
								PagamentoPortale oldPagamentoPortale = rpt.getPagamentoPortale();
								if(oldPagamentoPortale != null) {
									oldPagamentoPortale.setStato(STATO.NON_ESEGUITO);
									oldPagamentoPortale.setDescrizioneStato(MessageFormat.format("Tentativo di pagamento scaduto dopo timeout di {0} minuti.", GovpayConfig.getInstance().getTimeoutPendentiModello3SANP24Mins()));
								}
								rptBD.updateRpt(rpt.getId(), rpt);
								if(oldPagamentoPortale != null) {
									ppbd.updatePagamento(oldPagamentoPortale, false, true);
								}
								
								rptBD.commit();
								log.info("RPT [idDominio:{}][iuv:{}][ccp:{}] annullata con successo.", rpt.getCodDominio(), rpt.getIuv(), rpt.getCcp());
								
							}catch(ServiceException e) {
								rptBD.rollback();
								LogUtils.logError(log, MessageFormat.format("Errore durante l''annullamento della RPT [idDominio:{0}][iuv:{1}][ccp:{2}]: {3}", rpt.getCodDominio(), rpt.getIuv(), rpt.getCcp(), e .getMessage()), e);
								throw e;
							}finally {
								rptBD.setAutoCommit(false);
							}
						}
						log.trace("Completato annullamento [{}] RT.", rtList.size());
					}

					offset += limit;
					rtList = rptBD.getRptScadute(codDominio, GovpayConfig.getInstance().getTimeoutPendentiModello3SANP24Mins(), offset, limit, dataUltimoCheck);
					log.trace("Identificate su GovPay per il Dominio [{}]: {} transazioni scadute da piu'' di [{}] minuti.", codDominio, rtList.size(), GovpayConfig.getInstance().getTimeoutPendentiModello3SANP24Mins());
				}while(!rtList.isEmpty());
			}
		} catch (ServiceException e) {
			LogUtils.logWarn(log, "Fallito aggiornamento pendenti", e);
			throw new GovPayException(EsitoOperazione.INTERNAL, e);
		} finally {
			if(rptBD != null) {
				rptBD.closeConnection();
			}
		}

		if(response.isEmpty()) {
			return "Chiusura RPT Scadute#Nessuna RPT pendente.";
		} else {
			return StringUtils.join(response,"|");
		}
	}
}
