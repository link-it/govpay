.. _inst_monitoraggio:

Servizi di Monitoraggio
=======================

Per consentire l'integrazione con i sistemi di monitoraggio, GovPay
mette a disposizione servizi interrogabili per verificare il
funzionamento del sistema.

Monitoraggio GovPay
-------------------

Sono implementati dei check sui servizi gestiti da GovPay per
verificarne il corretto funzionamento. Lo stato dei check è consultabile
tramite servizi REST.

 GET /govpay-api-backoffice/rs/basic/v1/sonde

Il servizio restituisce una panoramica dei check attivi sul sistema e
del loro stato attuale. Per ciascuno è possibile acquisirne il
dettaglio:

 GET /govpay-api-backoffice/rs/basic/v1/sonde/{id}

dove *id* può assumere i seguenti valori:

+---------------------------------+-------------------------------------------------+
| check-db                        | Controllo operativita' del database             |
+---------------------------------+-------------------------------------------------+
| check-ntfy                      | Coda notifiche                                  |
+---------------------------------+-------------------------------------------------+
| check-tracciati                 | Coda Tracciati pendenze                         |
+---------------------------------+-------------------------------------------------+
| spedizione-promemoria           | Stato spedizione promemoria                     |
+---------------------------------+-------------------------------------------------+
| check-promemoria                | Coda spedizione promemoria                      |
+---------------------------------+-------------------------------------------------+
| check-ntfy-appio                | Coda notifiche AppIO                            |
+---------------------------------+-------------------------------------------------+
| check-gestione-promemoria       | Coda elaborazione promemoria                    |
+---------------------------------+-------------------------------------------------+
| check-elab-trac-notif-pag       | Coda tracciati notifica pagamenti               |
+---------------------------------+-------------------------------------------------+
| check-spedizione-trac-notif-pag | Coda spedizione tracciati notifica pagamenti    |
+---------------------------------+-------------------------------------------------+
| check-riconciliazioni           | Coda elaborazione riconciliazioni               |
+---------------------------------+-------------------------------------------------+
| check-rpt-scadute               | Numero RPT SANP 2.4 scadute da chiudere         |
+---------------------------------+-------------------------------------------------+
| check-recupero-rt               | Numero RT mancanti                              |
+---------------------------------+-------------------------------------------------+
| spedizione-trac-notif-pag       | Stato spedizione tracciati notifica pagamenti   |
+---------------------------------+-------------------------------------------------+
| elaborazione-trac-notif-pag     | Stato elaborazione tracciati notifica pagamenti |
+---------------------------------+-------------------------------------------------+
| riconciliazioni                 | Stato elaborazione riconciliazioni              |
+---------------------------------+-------------------------------------------------+
| caricamento-tracciati           | Stato caricamento tracciati pendenze            |
+---------------------------------+-------------------------------------------------+
| update-rnd                      | Acquisizione rendicontazioni                    |
+---------------------------------+-------------------------------------------------+
| gestione-promemoria             | Stato elaborazione promemoria                   |
+---------------------------------+-------------------------------------------------+
| update-ntfy-appio               | Stato spedizione notifiche AppIO                |
+---------------------------------+-------------------------------------------------+
| update-ntfy                     | Stato spedizione notifiche                      |
+---------------------------------+-------------------------------------------------+
| rpt-scadute                     | Stato chiusura RPT SANP 2.4 scadute             |
+---------------------------------+-------------------------------------------------+
| recupero-rt                     | Recupero RT mancanti                            |
+---------------------------------+-------------------------------------------------+

in ritorno si ha un messaggio con questo formato:

{

 "id":"check-ntfy",

 "nome":"Coda notifiche",

 "stato":"ok",

 "descrizioneStato":null,

 "durataStato":null,

 "sogliaWarn":"Numero di elementi accodati: 10",

 "sogliaError":"Numero di elementi accodati: 100",

 "sogliaWarnValue":10,

 "sogliaErrorValue":100,

 "dataUltimoCheck":"2025-05-21T11:46:33.104+0200",

 "tipo":"Coda"

}

con la seguente semantica:

+-----------------------------------+-----------------------------------+
| id                                | Identificativo della sonda        |
+-----------------------------------+-----------------------------------+
| nome                              | Denominazione della sonda         |
+-----------------------------------+-----------------------------------+
| stato                             | null: stato non verificato        |
|                                   |                                   |
|                                   | ok                                |
|                                   |                                   |
|                                   | warning                           |
|                                   |                                   |
|                                   | error                             |
+-----------------------------------+-----------------------------------+
| descrizioneStato                  | Descrizione informativa sullo     |
|                                   | stato assunto dal check           |
+-----------------------------------+-----------------------------------+
| durataStato                       | Tempo in millisecondi in cui il   |
|                                   | check è nello stato attuale       |
+-----------------------------------+-----------------------------------+
| sogliaWarn                        | Soglia di Warning in forma        |
|                                   | descrittiva                       |
+-----------------------------------+-----------------------------------+
| sogliaError                       | Soglia di Error in forma          |
|                                   | descrittiva                       |
+-----------------------------------+-----------------------------------+
| sogliaWarnValue                   | Valore di soglia per lo stato di  |
|                                   | warning. La semantica del valore  |
|                                   | dipende dal tipo di check:        |
+-----------------------------------+-----------------------------------+
| sogliaError                       | Come *sogliaWarnValue* ma per lo  |
|                                   | stato di error                    |
+-----------------------------------+-----------------------------------+
| dataUltimoCheck                   | Data dell'ultima verifica del     |
|                                   | check                             |
+-----------------------------------+-----------------------------------+
| tipo                              | Tipologia di check:               |
|                                   |                                   |
|                                   | Batch: elaborazioni in esecuzione |
|                                   |                                   |
|                                   | Coda: numero degli elementi       |
|                                   | attualmente in coda               |
|                                   |                                   |
+-----------------------------------+-----------------------------------+
