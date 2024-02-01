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
package it.govpay.core.utils.tasks;

public class CostantiTask {

	public static final String SERVICE_NAME_TASK = "Task";
	public final static String ACQUISIZIONE_RENDICONTAZIONI = "AcquisizioneRendicontazioni";
	public final static String RECUPERO_RPT_PENDENTI = "RecuperoRptPendenti";
	public final static String SPEDIZIONE_NOTIFICHE = "SpedizioneNotifiche";
	public final static String SPEDIZIONE_NOTIFICHE_CHECK = "SpedizioneNotificheCheck";
	public final static String SPEDIZIONE_NOTIFICHE_APP_IO = "SpedizioneNotificheAppIo";
	public final static String SPEDIZIONE_NOTIFICHE_APP_IO_CHECK = "SpedizioneNotificheAppIoCheck";
	public final static String RESET_CACHE_ANAGRAFICA = "ResetCacheAnagrafica";
	public final static String RESET_CACHE_ANAGRAFICA_CHECK = "ResetCacheAnagraficaCheck";
	public final static String AVVISATURA_DIGITALE_ASINCRONA = "AvvisaturaDigitaleAsincrona";
	public final static String ESITO_AVVISATURA_DIGITALE_ASINCRONA = "EsitoAvvisaturaDigitaleAsincrona";
	public final static String AVVISATURA_DIGITALE_SINCRONA = "AvvisaturaDigitaleSincrona";
	public final static String ELABORAZIONE_TRACCIATI_PENDENZE = "ElaborazioneTracciatiPendenze";
	public final static String ELABORAZIONE_TRACCIATI_PENDENZE_CHECK = "ElaborazioneTracciatiPendenzeCheck";
	public final static String SPEDIZIONE_PROMEMORIA = "SpedizionePromemoria";
	public final static String SPEDIZIONE_PROMEMORIA_CHECK = "SpedizionePromemoriaCheck";
	public final static String GESTIONE_PROMEMORIA = "GestionePromemoria";
	public final static String GESTIONE_PROMEMORIA_CHECK = "GestionePromemoriaCheck";
	
	// Batch creazione e spedizione flussi my pivot 
	
	public final static String ELABORAZIONE_TRACCIATI_NOTIFICA_PAGAMENTI = "ElaborazioneTracciatiNotificaPagamenti";
	public final static String ELABORAZIONE_TRACCIATI_NOTIFICA_PAGAMENTI_CHECK = "ElaborazioneTracciatiNotificaPagamentiCheck";
	public final static String SPEDIZIONE_TRACCIATI_NOTIFICA_PAGAMENTI = "SpedizioneTracciatiNotificaPagamenti";
	public final static String SPEDIZIONE_TRACCIATI_NOTIFICA_PAGAMENTI_CHECK = "SpedizioneTracciatiNotificaPagamentiCheck";
	
	public final static String ELABORAZIONE_RICONCILIAZIONI = "ElaborazioneRiconciliazioni";
	public final static String ELABORAZIONE_RICONCILIAZIONI_CHECK = "ElaborazioneRiconciliazioniCheck";
	
	public final static String CHIUSURA_RPT_SCADUTE = "ChiusuraRptScadute";
	public final static String CHIUSURA_RPT_SCADUTE_CHECK = "ChiusuraRptScaduteCheck";
	
}
