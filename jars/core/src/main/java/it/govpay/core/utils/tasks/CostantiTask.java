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
	
	private CostantiTask() {}

	public static final String SERVICE_NAME_TASK = "Task";
	public static final String ACQUISIZIONE_RENDICONTAZIONI = "AcquisizioneRendicontazioni";
	public static final String SPEDIZIONE_NOTIFICHE = "SpedizioneNotifiche";
	public static final String SPEDIZIONE_NOTIFICHE_CHECK = "SpedizioneNotificheCheck";
	public static final String SPEDIZIONE_NOTIFICHE_APP_IO = "SpedizioneNotificheAppIo";
	public static final String SPEDIZIONE_NOTIFICHE_APP_IO_CHECK = "SpedizioneNotificheAppIoCheck";
	public static final String RESET_CACHE_ANAGRAFICA = "ResetCacheAnagrafica";
	public static final String RESET_CACHE_ANAGRAFICA_CHECK = "ResetCacheAnagraficaCheck";
	public static final String AVVISATURA_DIGITALE_ASINCRONA = "AvvisaturaDigitaleAsincrona";
	public static final String ESITO_AVVISATURA_DIGITALE_ASINCRONA = "EsitoAvvisaturaDigitaleAsincrona";
	public static final String AVVISATURA_DIGITALE_SINCRONA = "AvvisaturaDigitaleSincrona";
	public static final String ELABORAZIONE_TRACCIATI_PENDENZE = "ElaborazioneTracciatiPendenze";
	public static final String ELABORAZIONE_TRACCIATI_PENDENZE_CHECK = "ElaborazioneTracciatiPendenzeCheck";
	public static final String SPEDIZIONE_PROMEMORIA = "SpedizionePromemoria";
	public static final String SPEDIZIONE_PROMEMORIA_CHECK = "SpedizionePromemoriaCheck";
	public static final String GESTIONE_PROMEMORIA = "GestionePromemoria";
	public static final String GESTIONE_PROMEMORIA_CHECK = "GestionePromemoriaCheck";
	
	public static final String ELABORAZIONE_TRACCIATI_NOTIFICA_PAGAMENTI = "ElaborazioneTracciatiNotificaPagamenti";
	public static final String ELABORAZIONE_TRACCIATI_NOTIFICA_PAGAMENTI_CHECK = "ElaborazioneTracciatiNotificaPagamentiCheck";
	public static final String SPEDIZIONE_TRACCIATI_NOTIFICA_PAGAMENTI = "SpedizioneTracciatiNotificaPagamenti";
	public static final String SPEDIZIONE_TRACCIATI_NOTIFICA_PAGAMENTI_CHECK = "SpedizioneTracciatiNotificaPagamentiCheck";
	
	public static final String ELABORAZIONE_RICONCILIAZIONI = "ElaborazioneRiconciliazioni";
	public static final String ELABORAZIONE_RICONCILIAZIONI_CHECK = "ElaborazioneRiconciliazioniCheck";
	
	public static final String CHIUSURA_RPT_SCADUTE = "ChiusuraRptScadute";
	public static final String CHIUSURA_RPT_SCADUTE_CHECK = "ChiusuraRptScaduteCheck";
	
	public static final String RECUPERO_RT = "RecuperoRT";
	public static final String RECUPERO_RT_CHECK = "RecuperoRTCheck";
}
