.. _inst_monitoraggio:

Servizi di Monitoraggio
=======================

Per consentire l'integrazione con i sistemi di monitoraggio, GovPay
mette a disposizione servizi interrogabili per verificare il
funzionamento del sistema.

I servizi di monitoraggio sono di due tipi:

-  Monitoraggio Domini

   per verificare l'esito delle ultime comunicazioni con il Nodo dei
   Pagamenti, relativamente ad uno specifico dominio.

-  Monitoraggio GovPay

   per verificare il funzionamento delle singole componenti del
   prodotto.

Monitoraggio domini
-------------------

Viene esposto un servizio di monitoraggio per dominio che fornisce
indicazioni di stato inerenti l'esito delle interazioni con il Nodo dei
Pagamenti. Il servizio si interroga con la seguente chiamata HTTP:

GET /govpay/frontend/api/pagopa/rs/check/{id_dominio} HTTP/1.1

Accept: application/json

in ritorno si ha un messaggio con questo formato:

{

"ultimo_aggiornamento":null,

"codice_stato":1,

"operazione_eseguita":null,

"errore_rilevato":"STATO NON VERIFICATO"

}

con la seguente semantica:

+-----------------------------------+-----------------------------------+
| ultimo_aggiornamento              | Data dell'ultimo aggiornamento    |
|                                   | dello stato                       |
+-----------------------------------+-----------------------------------+
| codice_stato                      | 0: ok                             |
|                                   |                                   |
|                                   | 1: stato non verificato           |
|                                   |                                   |
|                                   | 2: fail                           |
+-----------------------------------+-----------------------------------+
| operazione_eseguita               | Operazione richiesta al nodo che  |
|                                   | ha aggiornato lo stato            |
+-----------------------------------+-----------------------------------+
| errore_rilevato                   | Dettaglio dell'errore riscontrato |
+-----------------------------------+-----------------------------------+

Monitoraggio GovPay
-------------------

Sono implementati dei check sui servizi gestiti da GovPay per
verificarne il corretto funzionamento. Lo stato dei check è consultabile
tramite servizi REST.

 GET /govpay/frontend/api/pagopa/rs/check/sonda/

Il servizio restituisce una panoramica dei check attivi sul sistema e
del loro stato attuale. Per ciascuno è possibile acquisirne il
dettaglio:

GET /govpay/frontend/api/pagopa/rs/check/sonda/{nome}

dove *nome* può assumere i seguenti valori:

+--------------+-----------------------------------------------------------+
| update-psp   | Check del servizio di aggiornamento PSP                   |
+--------------+-----------------------------------------------------------+
| update-rnd   | Check del servizio di acquisizione flussi rendicontazione |
+--------------+-----------------------------------------------------------+
| update-pnd   | Check del servizio di risoluzione pagamenti pendenti      |
+--------------+-----------------------------------------------------------+
| update-ntfy  | Check del servizio di spedizione notifiche                |
+--------------+-----------------------------------------------------------+
| update-conto | Check del servizio di generazione estratti conto          |
+--------------+-----------------------------------------------------------+
| check-ntfy   | Check della coda di notifiche da spedire                  |
+--------------+-----------------------------------------------------------+

in ritorno si ha un messaggio con questo formato:

{

 "nome":"check-ntfy",

 "stato":0,

 "descrizioneStato":null,

 "durataStato":null,

 "sogliaWarn":"Numero di elementi accodati: 10",

 "sogliaError":"Numero di elementi accodati: 100",

 "sogliaWarnValue":10,

 "sogliaErrorValue":100,

 "dataUltimoCheck":1489673880116,

 "tipo":"Coda"

}

con la seguente semantica:

+-----------------------------------+-----------------------------------+
| Nome                              | Identificativo della check        |
+-----------------------------------+-----------------------------------+
| stato                             | null: stato non verificato        |
|                                   |                                   |
|                                   | 0: ok                             |
|                                   |                                   |
|                                   | 1: warning                        |
|                                   |                                   |
|                                   | 2: error                          |
+-----------------------------------+-----------------------------------+
| descrizioneStato                  | Descrizione informativa sullo     |
|                                   | stato assunto dal check           |
+-----------------------------------+-----------------------------------+
| durataStato                       | Tempo in millisecondi in cui il   |
|                                   | check e' nello stato attuale      |
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
+-----------------------------------+-----------------------------------+

