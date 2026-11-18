.. _integrazione_riconciliazioni:

Riconciliazione degli incassi
=============================

Il rilascio della ricevuta di pagamento da parte del PSP, tramite la
piattaforma pagoPA, libera il Soggetto Debitore da ogni ulteriore
adempimento. L'Ente Creditore deve invece concludere il
ciclo di vita del pagamento verificando che gli importi riscossi dai PSP
vengano correttamente riversati nei propri conti di accredito, gestiti
dalla Banca Tesoriera.

.. figure:: ../_images/INT05_RiconciliazioneIncassi.png
   :align: center
   :name: RiconciliazioneIncassi

   Riconciliazione degli incassi

Il flusso di questo scenario è il seguente:

1. Le somme riscosse dai PSP tramite il circuito pagoPA sono riversate
   sui conti di accredito degli Enti Creditori.
2. Le Banche Tesoriere forniscono ai Sistemi Amministrativi Contabili,
   nelle modalità concordate con l'Ente Creditore, il Giornale di Cassa,
   contenente il dettaglio dei movimenti avvenuti sui conti di
   accredito.
3. Ciascun riversamento pagoPA viene riconciliato individuando i singoli
   pagamenti da quietanzare.

Riversamento delle somme
------------------------

I movimenti di riversamento delle somme riscosse sono operati dai PSP
secondo i tempi ed i modi indicati dalle specifiche pagoPA (`SANP
<https://docs.pagopa.it/sanp>`__, sezione dedicata alla rendicontazione e al
riversamento). In particolare, la specifica determina che il PSP del pagatore assicuri che
l'importo dell'operazione venga accreditato sul conto dell’Ente
Creditore entro la fine della giornata operativa successiva a quella
indicata nella relativa Ricevuta Telematica, considerando che la
giornata operativa termina alle ore 13,00 (cosiddetta “giornata
operativa del Nodo dei Pagamenti”).

Comunicazione del Giornale di Cassa
-----------------------------------

Quotidianamente, o secondo quanto concordato con la Banca Tesoreria, i
Sistemi Amministrativi Contabili ricevono il Giornale di Cassa relativo
ai conti di accredito dell'Ente Creditore. Tale documento dettaglia i
movimenti contabili avvenuti sui conti di accredito ed in particolare i
movimenti di entrata determinati dai riversamenti pagoPA.

I riversamenti pagoPA sono riconoscibili poiché presentano, nel dato
AT-05 dell’SCT, il riferimento alle riscossioni riversate in uno dei
seguenti formati:

-  /PUR/LGPE-RIVERSAMENTO/URI/<identificativoFlusso>
-  /RFS/<IUV>/<importo>[/TXT/<descrizione>]
-  /RFB/<IUV>[/<importo>][/TXT/<descrizione>]

GovPay gestisce la riscossione ed il riversamento delle singole voci di
pendenza, pertanto rimane a carico dei sistemi gestionali dell’Ente
Creditore verificare la completa riconciliazione delle pendenze prima di
procedere al successivo quietanzamento.

Riconciliazione delle entrate
-----------------------------

Ciascuna entrata riconosciuta come riversamento pagoPA deve essere
registrata dal Sistema Amministrativo Contabile in GovPay tramite
l’operazione *PUT /riconciliazioni/{idDominio}/{id}* delle API Ragioneria v3,
utilizzando i dati acquisiti tramite il Giornale di Cassa. L'identificativo
*id* è assegnato dal chiamante e rende l'operazione idempotente:

-  alla prima invocazione GovPay accetta la richiesta e la elabora in modo
   asincrono, rispondendo **202 Accepted** con l'header *Location* della
   riconciliazione;
-  lo stato dell'elaborazione (*IN_ELABORAZIONE*, *ACQUISITA* o *ERRORE*) e,
   a elaborazione conclusa, la lista delle riscossioni associate al
   riversamento si ottengono con *GET /riconciliazioni/{idDominio}/{id}*;
   una nuova *PUT* con lo stesso *id* restituisce **200 OK** con la
   riconciliazione.

Con queste informazioni il Gestionale dell'ente creditore è in grado di
effettuare la chiusura contabile di ogni pendenza di pagamento. Nelle API
Ragioneria v2 l'operazione era *POST /riconciliazioni/{idDominio}*, sincrona,
con la lista delle riscossioni direttamente nella risposta.

Si può consultare un esempio di invocazione delle API Ragioneria, corrispondente a quando descritto sopra, nella sezione :ref:`Scenario "Riconciliazione dei Pagamenti con la Tesoreria" <govpay_scenari_riconciliazione_realizzazione>`.

In una fase distinta, il Gestionale può effettuare la chiamata a *GET
/riscossioni* delle API Ragioneria. L’operazione viene eseguita
fornendo i parametri di ricerca quali:

-  stato della riscossione impostato a “riscossa”
-  arco temporale di ricerca

Il risultato dell’operazione è l’elenco delle riscossioni, nel periodo
richiesto, che si trovano ancora in stato “riscossa” e quindi tuttora
non incassate, come nel seguente esempio:

.. code-block:: none

    GET /riscossioni?stato=RISCOSSA&tipo=ENTRATA&dataA=2017-12-31T23:59:59
    HTTP 200 OK
    {
        "numRisultati": 10,
        "numPagine": 10,
        "risultatiPerPagina": 1,
        "pagina": 1,
        "prossimiRisultati": "/riscossioni?stato=RISCOSSA&tipo=ENTRATA&dataA=2017-12-31T23:59:59&pagina=2&risultatiPerPagina=1",
        "risultati":
        [
            {
                "dominio":
                {
                    "idDominio": "01234567890",
                    "ragioneSociale": "Ente Creditore"
                },
                "iuv": "RF23567483937849450550875",
                "idRicevuta": "100000000000902038186",
                "iur": "1234acdc",
                "indice": 1,
                "stato": "RISCOSSA",
                "tipo": "ENTRATA",
                "importo": 10.01,
                "data": "2017-12-20T10:15:00",
                "vocePendenza":
                {
                    "idVocePendenza": "abcdef12345_1",
                    "descrizione": "Sanzione CdS n. abc00000",
                    "pendenza":
                    {
                        "idA2A": "GestPag",
                        "idPendenza": "abcdef12345",
                        "dataCaricamento": "2017-12-01"
                    }
                }
            }
        ]
    }

Quest’ultima operazione ha valenza nell’ambito delle verifiche
periodiche atte ad individuare situazioni anomale.
