/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2026 Link.it srl (http://www.link.it).
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
package it.govpay.backoffice.v1.sonde;

import it.govpay.core.business.Operazioni;

public class Costanti {

	private Costanti() {}
	
	/* IDENTIFICATIVI */
	public static final String CHECK_DB = Operazioni.CHECK_DB;
	public static final String RND =  Operazioni.RND;
	public static final String NTFY =  Operazioni.NTFY;
	public static final String CHECK_NTFY =  Operazioni.CHECK_NTFY;
	public static final String BATCH_TRACCIATI =  Operazioni.BATCH_TRACCIATI;
	public static final String CHECK_TRACCIATI =  Operazioni.CHECK_TRACCIATI;
	public static final String CHECK_PROMEMORIA =  Operazioni.CHECK_PROMEMORIA;
	public static final String BATCH_SPEDIZIONE_PROMEMORIA =  Operazioni.BATCH_SPEDIZIONE_PROMEMORIA;
	public static final String NTFY_APP_IO =  Operazioni.NTFY_APP_IO;
	public static final String CHECK_NTFY_APP_IO =  Operazioni.CHECK_NTFY_APP_IO;
	public static final String BATCH_GESTIONE_PROMEMORIA =  Operazioni.BATCH_GESTIONE_PROMEMORIA;
	public static final String CHECK_GESTIONE_PROMEMORIA =  Operazioni.CHECK_GESTIONE_PROMEMORIA;

	public static final String BATCH_ELABORAZIONE_TRACCIATI_NOTIFICA_PAGAMENTI =  Operazioni.BATCH_ELABORAZIONE_TRACCIATI_NOTIFICA_PAGAMENTI;
	public static final String CHECK_ELABORAZIONE_TRACCIATI_NOTIFICA_PAGAMENTI =  Operazioni.CHECK_ELABORAZIONE_TRACCIATI_NOTIFICA_PAGAMENTI;

	public static final String BATCH_SPEDIZIONE_TRACCIATI_NOTIFICA_PAGAMENTI =  Operazioni.BATCH_SPEDIZIONE_TRACCIATI_NOTIFICA_PAGAMENTI;
	public static final String CHECK_SPEDIZIONE_TRACCIATI_NOTIFICA_PAGAMENTI =  Operazioni.CHECK_SPEDIZIONE_TRACCIATI_NOTIFICA_PAGAMENTI;

	public static final String BATCH_RICONCILIAZIONI =  Operazioni.BATCH_RICONCILIAZIONI;
	public static final String CHECK_RICONCILIAZIONI =  Operazioni.CHECK_RICONCILIAZIONI;

	public static final String BATCH_CHIUSURA_RPT_SCADUTE =  Operazioni.BATCH_CHIUSURA_RPT_SCADUTE;
	public static final String CHECK_CHIUSURA_RPT_SCADUTE =  Operazioni.CHECK_CHIUSURA_RPT_SCADUTE;

	public static final String BATCH_RECUPERO_RT = Operazioni.BATCH_RECUPERO_RT;
	public static final String CHECK_RECUPERO_RT = Operazioni.CHECK_RECUPERO_RT;


	/* LABEL */
	public static final String SONDA_IMPOSSIBILE_ACQUISIRE_LO_STATO = "Impossibile acquisire lo stato della sonda: {0}";
	public static final String SONDA_CON_ID_NON_CONFIGURATA = "Sonda con id [{0}] non configurata";

	public static final String CHECK_DB_NOME = "Controllo operativita' del database";
	public static final String CHECK_DB_DESCRIZIONE_STATO_OK = "Servizio database raggiungibile";
	public static final String CHECK_DB_DESCRIZIONE_STATO_DATABASE_NON_DISPONIBILE = "Servizio database non disponibile: {0}";

	public static final String RND_NOME = "Acquisizione rendicontazioni";
	public static final String RND_DISABILITATO = "Servizio acquisizione rendicontazioni disabilitato tramite proprieta' di sistema";

	public static final String NTFY_NOME = "Stato spedizione notifiche";
	public static final String NTFY_DISABILITATO = "Servizio spedizione notifiche disabilitato tramite proprieta' di sistema";

	public static final String NTFY_APP_IO_NOME = "Stato spedizione notifiche AppIO";
	public static final String NTFY_APP_IO_DISABILITATO = "Servizio spedizione notifiche AppIO disabilitato tramite proprieta' di sistema";
	public static final String NTFY_APP_IO_DISABILITATO_IMPOSTAZIONI = "Servizio spedizione notifiche AppIO disabilitato tramite API di configurazione";

	public static final String CHECK_NTFY_NOME = "Coda notifiche";
	public static final String CHECK_NTFY_APP_IO_NOME = "Coda notifiche AppIO";

	public static final String BATCH_TRACCIATI_NOME = "Stato caricamento tracciati pendenze";
	public static final String BATCH_TRACCIATI_DISABILITATO = "Servizio caricamento tracciati pendenze disabilitato tramite proprieta' di sistema";

	public static final String CHECK_TRACCIATI_NOME = "Coda Tracciati pendenze";

	public static final String CHECK_PROMEMORIA_NOME = "Coda spedizione promemoria";
	public static final String BATCH_SPEDIZIONE_PROMEMORIA_NOME = "Stato spedizione promemoria";
	public static final String BATCH_SPEDIZIONE_PROMEMORIA_DISABILITATO = "Servizio spedizione promemoria disabilitato tramite proprieta' di sistema";
	public static final String BATCH_SPEDIZIONE_PROMEMORIA_DISABILITATO_IMPOSTAZIONI = "Servizio spedizione promemoria disabilitato tramite API di configurazione";

	public static final String BATCH_GESTIONE_PROMEMORIA_NOME = "Stato elaborazione promemoria";
	public static final String BATCH_GESTIONE_PROMEMORIA_DISABILITATO = "Servizio elaborazione promemoria disabilitato tramite proprieta' di sistema";
	public static final String BATCH_GESTIONE_PROMEMORIA_DISABILITATO_IMPOSTAZIONI = "Servizio elaborazione promemoria disabilitato tramite API di configurazione";

	public static final String CHECK_GESTIONE_PROMEMORIA_NOME = "Coda elaborazione promemoria";

	public static final String BATCH_ELABORAZIONE_TRACCIATI_NOTIFICA_PAGAMENTI_NOME = "Stato elaborazione tracciati notifica pagamenti";
	public static final String BATCH_ELABORAZIONE_TRACCIATI_NOTIFICA_PAGAMENTI_DISABILITATO = "Servizio elaborazione tracciati notifica pagamenti disabilitato tramite proprieta' di sistema";

	public static final String CHECK_ELABORAZIONE_TRACCIATI_NOTIFICA_PAGAMENTI_NOME = "Coda tracciati notifica pagamenti";

	public static final String BATCH_SPEDIZIONE_TRACCIATI_NOTIFICA_PAGAMENTI_NOME = "Stato spedizione tracciati notifica pagamenti";
	public static final String BATCH_SPEDIZIONE_TRACCIATI_NOTIFICA_PAGAMENTI_DISABILITATO = "Servizio spedizione tracciati notifica pagamenti disabilitato tramite proprieta' di sistema";

	public static final String CHECK_SPEDIZIONE_TRACCIATI_NOTIFICA_PAGAMENTI_NOME = "Coda spedizione tracciati notifica pagamenti";

	public static final String BATCH_RICONCILIAZIONI_NOME = "Stato elaborazione riconciliazioni";
	public static final String BATCH_RICONCILIAZIONI_DISABILITATO = "Servizio elaborazione riconciliazioni disabilitato tramite proprieta' di sistema";

	public static final String CHECK_RICONCILIAZIONI_NOME = "Coda elaborazione riconciliazioni";

	public static final String BATCH_CHIUSURA_RPT_SCADUTE_NOME = "Stato chiusura RPT SANP 2.4 scadute";
	public static final String BATCH_CHIUSURA_RPT_SCADUTE_DISABILITATO = "Servizio chiusura RPT SANP 2.4 scadute disabilitato tramite proprieta' di sistema";

	public static final String CHECK_CHIUSURA_RPT_SCADUTE_NOME = "Numero RPT SANP 2.4 scadute da chiudere";

	public static final String BATCH_RECUPERO_RT_NOME = "Recupero RT mancanti";
	public static final String BATCH_RECUPERO_RT_DISABILITATO = "Servizio Recupero RT mancanti disabilitato tramite proprieta' di sistema";
	public static final String CHECK_RECUPERO_RT_NOME = "Numero RT mancanti";

}
