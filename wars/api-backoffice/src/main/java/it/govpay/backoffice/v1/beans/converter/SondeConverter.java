/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2025 Link.it srl (http://www.link.it).
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
package it.govpay.backoffice.v1.beans.converter;

import it.govpay.backoffice.v1.beans.Sonda;
import it.govpay.backoffice.v1.beans.Sonda.StatoSonda;
import it.govpay.backoffice.v1.beans.Sonda.TipoSonda;
import it.govpay.backoffice.v1.sonde.Costanti;
import it.govpay.core.exceptions.IOException;
import it.govpay.core.utils.GovpayConfig;
import it.govpay.model.configurazione.AppIOBatch;
import it.govpay.model.configurazione.MailBatch;

public class SondeConverter {

	private SondeConverter() {}

	public static Sonda toRsModel(org.openspcoop2.utils.sonde.Sonda sonda, org.openspcoop2.utils.sonde.ParametriSonda parametri, it.govpay.bd.model.Configurazione configurazione) throws IOException {
		Sonda rsModel = new Sonda(sonda.getClass());
		rsModel.setId(parametri.getNome());
		rsModel.setNome(SondeConverter.getNomeSonda(parametri.getNome()));

		org.openspcoop2.utils.sonde.Sonda.StatoSonda statoSonda = sonda.getStatoSonda();

		rsModel.setStato(SondeConverter.getStatoSonda(statoSonda.getStato()));
		rsModel.setDataUltimoCheck(parametri.getDataUltimoCheck());

		if(rsModel.getTipo().equals(TipoSonda.Batch)) {
			// controllare che le funzionalita' di batch corrispondenti siano abilitate nel sistema
			// altrimenti modificare la stringa di descrizione stato per indicare che il batch e' spento

			impostaDescrizioneSondaBatch(parametri, rsModel, statoSonda, configurazione);

		} else {
			rsModel.setDescrizioneStato(statoSonda.getDescrizione());

			if(statoSonda.getStato() == 0) rsModel.setDurataStato(parametri.getDataOk());
			if(statoSonda.getStato() == 1) rsModel.setDurataStato(parametri.getDataWarn());
			if(statoSonda.getStato() == 2) rsModel.setDurataStato(parametri.getDataError());
			rsModel.setSogliaError(parametri.getSogliaError());
			rsModel.setSogliaWarn(parametri.getSogliaWarn());
		}

		return rsModel;
	}

	private static void impostaDescrizioneSondaBatch(org.openspcoop2.utils.sonde.ParametriSonda parametri, Sonda rsModel,
			org.openspcoop2.utils.sonde.Sonda.StatoSonda statoSonda, it.govpay.bd.model.Configurazione configurazione) throws IOException {

		if(Costanti.NTFY.equals(rsModel.getId())) {
			if(GovpayConfig.getInstance().isBatchOn() && GovpayConfig.getInstance().isBatchSpedizioneNotifiche()) {
				rsModel.setDescrizioneStato(statoSonda.getDescrizione());

				if(statoSonda.getStato() == 0) rsModel.setDurataStato(parametri.getDataOk());
				if(statoSonda.getStato() == 1) rsModel.setDurataStato(parametri.getDataWarn());
				if(statoSonda.getStato() == 2) rsModel.setDurataStato(parametri.getDataError());
				rsModel.setSogliaError(parametri.getSogliaError());
				rsModel.setSogliaWarn(parametri.getSogliaWarn());
			} else {
				// batch disabilitato
				rsModel.setStato(StatoSonda.ERROR);
				rsModel.setDescrizioneStato(Costanti.NTFY_DISABILITATO);
			}
		} else if(Costanti.BATCH_SPEDIZIONE_PROMEMORIA.equals(rsModel.getId())) {
			if(GovpayConfig.getInstance().isBatchOn() && GovpayConfig.getInstance().isBatchSpedizionePromemoria()) {

				MailBatch batchSpedizioneEmail = configurazione.getBatchSpedizioneEmail();
				if(!batchSpedizioneEmail.isAbilitato()) {
					// batch disabilitato nella configurazione
					rsModel.setStato(StatoSonda.ERROR);
					rsModel.setDescrizioneStato(Costanti.BATCH_SPEDIZIONE_PROMEMORIA_DISABILITATO_IMPOSTAZIONI);
				} else {
					rsModel.setDescrizioneStato(statoSonda.getDescrizione());

					if(statoSonda.getStato() == 0) rsModel.setDurataStato(parametri.getDataOk());
					if(statoSonda.getStato() == 1) rsModel.setDurataStato(parametri.getDataWarn());
					if(statoSonda.getStato() == 2) rsModel.setDurataStato(parametri.getDataError());
					rsModel.setSogliaError(parametri.getSogliaError());
					rsModel.setSogliaWarn(parametri.getSogliaWarn());
				}
			} else {
				// batch disabilitato
				rsModel.setStato(StatoSonda.ERROR);
				rsModel.setDescrizioneStato(Costanti.BATCH_SPEDIZIONE_PROMEMORIA_DISABILITATO);
			}
		} else if(Costanti.NTFY_APP_IO.equals(rsModel.getId())) {
			if(GovpayConfig.getInstance().isBatchOn() && GovpayConfig.getInstance().isBatchSpedizioneNotificheAppIO()) {


				if(!configurazione.getBatchSpedizioneAppIo().isAbilitato()) {
					// batch disabilitato nella configurazione
					rsModel.setStato(StatoSonda.ERROR);
					rsModel.setDescrizioneStato(Costanti.NTFY_APP_IO_DISABILITATO_IMPOSTAZIONI);
				}


				rsModel.setDescrizioneStato(statoSonda.getDescrizione());

				if(statoSonda.getStato() == 0) rsModel.setDurataStato(parametri.getDataOk());
				if(statoSonda.getStato() == 1) rsModel.setDurataStato(parametri.getDataWarn());
				if(statoSonda.getStato() == 2) rsModel.setDurataStato(parametri.getDataError());
				rsModel.setSogliaError(parametri.getSogliaError());
				rsModel.setSogliaWarn(parametri.getSogliaWarn());
			} else {
				// batch disabilitato
				rsModel.setStato(StatoSonda.ERROR);
				rsModel.setDescrizioneStato(Costanti.NTFY_APP_IO_DISABILITATO);
			}
		} else if(Costanti.BATCH_GESTIONE_PROMEMORIA.equals(rsModel.getId())) {
			if(GovpayConfig.getInstance().isBatchOn() && GovpayConfig.getInstance().isBatchGestionePromemoria()) {

				MailBatch batchSpedizioneEmail = configurazione.getBatchSpedizioneEmail();
				AppIOBatch batchSpedizioneAppIO = configurazione.getBatchSpedizioneAppIo();

				if(!batchSpedizioneEmail.isAbilitato() && !batchSpedizioneAppIO.isAbilitato()) {
					// batch disabilitato nella configurazione
					rsModel.setStato(StatoSonda.ERROR);
					rsModel.setDescrizioneStato(Costanti.BATCH_GESTIONE_PROMEMORIA_DISABILITATO_IMPOSTAZIONI);
				}


				rsModel.setDescrizioneStato(statoSonda.getDescrizione());

				if(statoSonda.getStato() == 0) rsModel.setDurataStato(parametri.getDataOk());
				if(statoSonda.getStato() == 1) rsModel.setDurataStato(parametri.getDataWarn());
				if(statoSonda.getStato() == 2) rsModel.setDurataStato(parametri.getDataError());
				rsModel.setSogliaError(parametri.getSogliaError());
				rsModel.setSogliaWarn(parametri.getSogliaWarn());
			} else {
				// batch disabilitato
				rsModel.setStato(StatoSonda.ERROR);
				rsModel.setDescrizioneStato(Costanti.BATCH_GESTIONE_PROMEMORIA_DISABILITATO);
			}
		} else if(Costanti.BATCH_ELABORAZIONE_TRACCIATI_NOTIFICA_PAGAMENTI.equals(rsModel.getId())) {
			if(GovpayConfig.getInstance().isBatchOn() && GovpayConfig.getInstance().isBatchCaricamentoTracciatiNotificaPagamenti()) {
				rsModel.setDescrizioneStato(statoSonda.getDescrizione());

				if(statoSonda.getStato() == 0) rsModel.setDurataStato(parametri.getDataOk());
				if(statoSonda.getStato() == 1) rsModel.setDurataStato(parametri.getDataWarn());
				if(statoSonda.getStato() == 2) rsModel.setDurataStato(parametri.getDataError());
				rsModel.setSogliaError(parametri.getSogliaError());
				rsModel.setSogliaWarn(parametri.getSogliaWarn());
			} else {
				// batch disabilitato
				rsModel.setStato(StatoSonda.ERROR);
				rsModel.setDescrizioneStato(Costanti.BATCH_ELABORAZIONE_TRACCIATI_NOTIFICA_PAGAMENTI_DISABILITATO);
			}
		} else if(Costanti.BATCH_SPEDIZIONE_TRACCIATI_NOTIFICA_PAGAMENTI.equals(rsModel.getId())) {
			if(GovpayConfig.getInstance().isBatchOn() && GovpayConfig.getInstance().isBatchCaricamentoTracciatiNotificaPagamenti()) {
				rsModel.setDescrizioneStato(statoSonda.getDescrizione());

				if(statoSonda.getStato() == 0) rsModel.setDurataStato(parametri.getDataOk());
				if(statoSonda.getStato() == 1) rsModel.setDurataStato(parametri.getDataWarn());
				if(statoSonda.getStato() == 2) rsModel.setDurataStato(parametri.getDataError());
				rsModel.setSogliaError(parametri.getSogliaError());
				rsModel.setSogliaWarn(parametri.getSogliaWarn());
			} else {
				// batch disabilitato
				rsModel.setStato(StatoSonda.ERROR);
				rsModel.setDescrizioneStato(Costanti.BATCH_SPEDIZIONE_TRACCIATI_NOTIFICA_PAGAMENTI_DISABILITATO);
			}
		} else if(Costanti.BATCH_CHIUSURA_RPT_SCADUTE.equals(rsModel.getId())) {
			if(GovpayConfig.getInstance().isBatchOn() && GovpayConfig.getInstance().isBatchChiusuraRPTScadute()) {
				rsModel.setDescrizioneStato(statoSonda.getDescrizione());

				if(statoSonda.getStato() == 0) rsModel.setDurataStato(parametri.getDataOk());
				if(statoSonda.getStato() == 1) rsModel.setDurataStato(parametri.getDataWarn());
				if(statoSonda.getStato() == 2) rsModel.setDurataStato(parametri.getDataError());
				rsModel.setSogliaError(parametri.getSogliaError());
				rsModel.setSogliaWarn(parametri.getSogliaWarn());
			} else {
				// batch disabilitato
				rsModel.setStato(StatoSonda.ERROR);
				rsModel.setDescrizioneStato(Costanti.BATCH_CHIUSURA_RPT_SCADUTE_DISABILITATO);
			}
		} else if(Costanti.BATCH_RICONCILIAZIONI.equals(rsModel.getId())) {
			if(GovpayConfig.getInstance().isBatchOn() && GovpayConfig.getInstance().isBatchElaborazioneRiconciliazioni()) {
				rsModel.setDescrizioneStato(statoSonda.getDescrizione());

				if(statoSonda.getStato() == 0) rsModel.setDurataStato(parametri.getDataOk());
				if(statoSonda.getStato() == 1) rsModel.setDurataStato(parametri.getDataWarn());
				if(statoSonda.getStato() == 2) rsModel.setDurataStato(parametri.getDataError());
				rsModel.setSogliaError(parametri.getSogliaError());
				rsModel.setSogliaWarn(parametri.getSogliaWarn());
			} else {
				// batch disabilitato
				rsModel.setStato(StatoSonda.ERROR);
				rsModel.setDescrizioneStato(Costanti.BATCH_RICONCILIAZIONI_DISABILITATO);
			}
		} else if(Costanti.BATCH_RECUPERO_RT.equals(rsModel.getId())) {
			if(GovpayConfig.getInstance().isBatchOn() && GovpayConfig.getInstance().isBatchRecuperoRT()) {
				rsModel.setDescrizioneStato(statoSonda.getDescrizione());

				if(statoSonda.getStato() == 0) rsModel.setDurataStato(parametri.getDataOk());
				if(statoSonda.getStato() == 1) rsModel.setDurataStato(parametri.getDataWarn());
				if(statoSonda.getStato() == 2) rsModel.setDurataStato(parametri.getDataError());
				rsModel.setSogliaError(parametri.getSogliaError());
				rsModel.setSogliaWarn(parametri.getSogliaWarn());
			} else {
				// batch disabilitato
				rsModel.setStato(StatoSonda.ERROR);
				rsModel.setDescrizioneStato(Costanti.BATCH_RECUPERO_RT_DISABILITATO);
			}
		} else if(Costanti.RND.equals(rsModel.getId())) {
			if(GovpayConfig.getInstance().isBatchOn() && GovpayConfig.getInstance().isBatchAcquisizioneRendicontazioni()) {
				rsModel.setDescrizioneStato(statoSonda.getDescrizione());

				if(statoSonda.getStato() == 0) rsModel.setDurataStato(parametri.getDataOk());
				if(statoSonda.getStato() == 1) rsModel.setDurataStato(parametri.getDataWarn());
				if(statoSonda.getStato() == 2) rsModel.setDurataStato(parametri.getDataError());
				rsModel.setSogliaError(parametri.getSogliaError());
				rsModel.setSogliaWarn(parametri.getSogliaWarn());
			} else {
				// batch disabilitato
				rsModel.setStato(StatoSonda.ERROR);
				rsModel.setDescrizioneStato(Costanti.RND_DISABILITATO);
			}
		} else if(Costanti.BATCH_TRACCIATI.equals(rsModel.getId())) {
			if(GovpayConfig.getInstance().isBatchOn() && GovpayConfig.getInstance().isBatchCaricamentoTracciati()) {
				rsModel.setDescrizioneStato(statoSonda.getDescrizione());

				if(statoSonda.getStato() == 0) rsModel.setDurataStato(parametri.getDataOk());
				if(statoSonda.getStato() == 1) rsModel.setDurataStato(parametri.getDataWarn());
				if(statoSonda.getStato() == 2) rsModel.setDurataStato(parametri.getDataError());
				rsModel.setSogliaError(parametri.getSogliaError());
				rsModel.setSogliaWarn(parametri.getSogliaWarn());
			} else {
				// batch disabilitato
				rsModel.setStato(StatoSonda.ERROR);
				rsModel.setDescrizioneStato(Costanti.BATCH_TRACCIATI_DISABILITATO);
			}
		}  else {
			rsModel.setDescrizioneStato(statoSonda.getDescrizione());

			if(statoSonda.getStato() == 0) rsModel.setDurataStato(parametri.getDataOk());
			if(statoSonda.getStato() == 1) rsModel.setDurataStato(parametri.getDataWarn());
			if(statoSonda.getStato() == 2) rsModel.setDurataStato(parametri.getDataError());
			rsModel.setSogliaError(parametri.getSogliaError());
			rsModel.setSogliaWarn(parametri.getSogliaWarn());
		}
	}

	public static StatoSonda getStatoSonda(int stato) {
		switch (stato) {
		case 0:
			return StatoSonda.OK;
		case 1:
			return StatoSonda.WARN;
		case 2:
		default:
			return StatoSonda.ERROR;
		}
	}

	public static String getNomeSonda(String id) {
		if(id != null) {
			switch (id) {
			case Costanti.CHECK_DB:
				return Costanti.CHECK_DB_NOME;
			case Costanti.RND:
				return Costanti.RND_NOME;
			case Costanti.NTFY:
				return Costanti.NTFY_NOME;
			case Costanti.CHECK_NTFY:
				return Costanti.CHECK_NTFY_NOME;
			case Costanti.BATCH_TRACCIATI:
				return Costanti.BATCH_TRACCIATI_NOME;
			case Costanti.CHECK_TRACCIATI:
				return Costanti.CHECK_TRACCIATI_NOME;
			case Costanti.CHECK_PROMEMORIA:
				return Costanti.CHECK_PROMEMORIA_NOME;
			case Costanti.BATCH_SPEDIZIONE_PROMEMORIA:
				return Costanti.BATCH_SPEDIZIONE_PROMEMORIA_NOME;
			case Costanti.NTFY_APP_IO:
				return Costanti.NTFY_APP_IO_NOME;
			case Costanti.CHECK_NTFY_APP_IO:
				return Costanti.CHECK_NTFY_APP_IO_NOME;
			case Costanti.BATCH_GESTIONE_PROMEMORIA:
				return Costanti.BATCH_GESTIONE_PROMEMORIA_NOME;
			case Costanti.CHECK_GESTIONE_PROMEMORIA:
				return Costanti.CHECK_GESTIONE_PROMEMORIA_NOME;
			case Costanti.BATCH_ELABORAZIONE_TRACCIATI_NOTIFICA_PAGAMENTI:
				return Costanti.BATCH_ELABORAZIONE_TRACCIATI_NOTIFICA_PAGAMENTI_NOME;
			case Costanti.CHECK_ELABORAZIONE_TRACCIATI_NOTIFICA_PAGAMENTI:
				return Costanti.CHECK_ELABORAZIONE_TRACCIATI_NOTIFICA_PAGAMENTI_NOME;
			case Costanti.BATCH_SPEDIZIONE_TRACCIATI_NOTIFICA_PAGAMENTI:
				return Costanti.BATCH_SPEDIZIONE_TRACCIATI_NOTIFICA_PAGAMENTI_NOME;
			case Costanti.CHECK_SPEDIZIONE_TRACCIATI_NOTIFICA_PAGAMENTI:
				return Costanti.CHECK_SPEDIZIONE_TRACCIATI_NOTIFICA_PAGAMENTI_NOME;
			case Costanti.BATCH_RICONCILIAZIONI:
				return Costanti.BATCH_RICONCILIAZIONI_NOME;
			case Costanti.CHECK_RICONCILIAZIONI:
				return Costanti.CHECK_RICONCILIAZIONI_NOME;
			case Costanti.BATCH_CHIUSURA_RPT_SCADUTE:
				return Costanti.BATCH_CHIUSURA_RPT_SCADUTE_NOME;
			case Costanti.CHECK_CHIUSURA_RPT_SCADUTE:
				return Costanti.CHECK_CHIUSURA_RPT_SCADUTE_NOME;
			case Costanti.BATCH_RECUPERO_RT:
				return Costanti.BATCH_RECUPERO_RT_NOME;
			case Costanti.CHECK_RECUPERO_RT:
				return Costanti.CHECK_RECUPERO_RT_NOME;
			}
		}
		return id;
	}
}
