/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2015 Link.it srl (http://www.link.it).
 * Copyright (c) 2014-2015 TAS S.p.A. (http://www.tasgroup.it).
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
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
package it.govpay.ejb.core.ejb;

import it.govpay.ejb.core.exception.GovPayException;
import it.govpay.ejb.core.exception.GovPayException.GovPayExceptionEnum;
import it.govpay.ejb.core.model.DistintaModel;
import it.govpay.ejb.core.model.EnteCreditoreModel;
import it.govpay.ejb.core.model.ScadenzarioModel;
import it.govpay.ejb.core.utils.rs.EjbUtils;
import it.govpay.ejb.core.utils.rs.client.NotificaClient;
import it.govpay.ejb.core.utils.rs.client.ScadenzarioRemotoClient;
import it.govpay.rs.Pagamento;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.logging.log4j.Logger;

@Stateless
public class ScadenzarioEJB {
	
	@Inject
	Logger log;
	
	@Inject
	DistintaEJB distintaEjb;
	
	@Inject
	RevocaDistintaEJB revocaDistintaEjb;
	
	public Pagamento recuperaPagamento(EnteCreditoreModel ente, ScadenzarioModel scadenzario, String iuv) throws GovPayException {
		
		ScadenzarioRemotoClient client = null;
		try {
			client = new ScadenzarioRemotoClient(ente, scadenzario);
		} catch (Exception e) {
			log.error("Impossibile instanziare il client di accesso allo scadenzario per il creditore " + ente.getDenominazione() + " ("+ ente.getIdEnteCreditore() + ")", e);
			throw new GovPayException(GovPayExceptionEnum.ERRORE_CONFIGURAZIONE, "Errore di configurazione del connettore Scadenzario", e);
		}
		
		Pagamento p = client.ricercaPagamento(ente.getIdFiscale(), iuv);
		log.info("Recuperato pagamento.");
		return p;
	}
	
	public void notificaVerificaPagamento(String idEnteCreditore, ScadenzarioModel scadenzario, DistintaModel distinta) {
		NotificaClient client = null;
		try {
			client = new NotificaClient(idEnteCreditore, scadenzario);
		} catch (Exception e) {
			log.error("Impossibile instanziare il client di notifica per il creditore [IdEnteCreditore: "+ idEnteCreditore + "]", e);
			return;
		}
		
		try {
			client.notificaVerificaPagamento(EjbUtils.toWebVerificaPagamento(distintaEjb.getEsitoPagamentoDistinta(distinta.getIdDistinta())));
			log.info("Pagamento con IUV " + distinta.getIuv() + " e CCP" + distinta.getCodTransazionePsp() + " notificato con successo.");
		} catch (Exception e) {
			log.error("Errore durante la notifica del pagamento con IUV " + distinta.getIuv() + " e CCP" + distinta.getCodTransazionePsp() + ": " + e);
			return;
		}	
		
		try {
			distintaEjb.setDistintaNotificata(distinta.getIdDistinta());
		} catch (Exception e) {
			log.error("Errore durante la memorizzazione dello stato della distinta come \"Notificata\"", e);
		}
	}

	public void notificaEsitoRevoca(String idEnteCreditore, ScadenzarioModel scadenzario, DistintaModel distinta) {
		NotificaClient client = null;
		try {
			client = new NotificaClient(idEnteCreditore, scadenzario);
		} catch (Exception e) {
			log.error("Impossibile instanziare il client di notifica per il creditore [IdEnteCreditore: "+ idEnteCreditore + "]", e);
			return;
		}
		
		try {
			client.notificaEsitoRevoca(EjbUtils.toWebEsitoRevoca(distintaEjb.getEsitoRevocaDistinta(distinta.getIdDistinta())));
			log.info("Pagamento con IUV " + distinta.getIuv() + " e CCP" + distinta.getCodTransazionePsp() + " notificato con successo.");
		} catch (Exception e) {
			log.error("Errore durante la notifica del pagamento con IUV " + distinta.getIuv() + " e CCP" + distinta.getCodTransazionePsp() + ": " + e);
			return;
		}	
		
		try {
			revocaDistintaEjb.setRevocaDistintaNotificata(distinta.getIdDistinta());
		} catch (Exception e) {
			log.error("Errore durante la memorizzazione dello stato della distinta come \"Notificata\"", e);
		}
		
	}
	
	
}
