.. _govpay_configurazione_tipipendenze:

Tipi Pendenze
-------------

Ogni importo che costituisce un versamento deve essere associato ad una pendenza censita sul sistema. La configurazione di questo oggetto determina quindi le coordinate di pagamento e quelle di rendicontazione. Si noti come le pendenze siano associate a un dominio, determinando quindi il tipo di pagamenti che ad esso fanno riferimento.
La gestione dei tipi di pendenza permette la generazione di maschere automatiche per l'immissione dei dati, semplificando in modo notevole lo sviluppo di interfacce e ottimizzando i tempi generali di progetto.
Le modalità per la creazione di una nuova pendenza sono sempre le medesime (tasto più in basso a destra) e la maschera presentata è la seguente:

.. figure:: ../../_images/24NuovaPendenza.png
   :align: center
   :name: CampiPerNuovaPendenza

   Maschera di creazione di una Nuova Pendenza

Vediamo come modificare una pendenza esistente; ciò ci permetterà di illustrare il dettaglio dei campi presenti. Selezioniamo, ad esempio, la Pendenza *Sanzione Amministrativa*.

.. figure:: ../../_images/27SelezioneDellaPendenzaPerModifica.png
   :align: center
   :name: SelezionePendenza

   Selezione della Pendenza *Sanzione Amministrativa*

Il sistema mostra la seguente maschera

.. figure:: ../../_images/30ModificaSanzioneAmministrativa.png
   :align: center
   :name: ModificaTipoPendenzaSanzioneAmministrativa

   Modifica del tipo pendenza *Sanzione Amministrativa*

Possiamo identificare i seguenti raggruppamenti di informazioni:

* Riepilogo Informazioni
* Layout form dati
* Elaborazione
* Promemoria avviso pagamento
* Promemoria ricevuta telematica

A ciascuno di essi è dedicata una sezione di dettaglio, come segue.

Riepilogo Informazioni
~~~~~~~~~~~~~~~~~~~~~~
La sottosezione si presenta nel seguente modo:

.. figure:: ../../_images/34EntrataRiepilogoInformazioni.png
   :align: center
   :name: RiepilogoInformazioni

   Sezione Riepilogo Informazioni

.. csv-table:: Campi modificabili della prima sezione
   :header: "Campo", "Significato", "Note"
   :widths: 40,40,20

   "Descrizione", "Descrizione sintetica del tipo di pendenza", ""
   "Id Tipo Pendenza", "Codice tecnico che indica in modo univoco la pendenza", "Non modificabile"
   "Tipologia", "Tipo di pendenza: dovuta o spontanea", ""
   "Codifica IUV", "Identificatore della struttura del codice IUV", ""
   "Abilitato", "Indica se la Sanzione Amministrativa sia abilitata o meno, quindi sia o meno associabile a domini esistenti", ""
   "Pagabile da terzi", "Indica se la sanzione possa o meno essere pagata non dal debitore", ""


Layout form dati
~~~~~~~~~~~~~~~~

.. figure:: ../../_images/31ModificaFormSanzioneAmministrativa.png
   :align: center
   :name: ModificaSanzioneAmministrativa_Form

   Sezione form della *Sanzione Amministrativa*


.. csv-table:: Campi modificabili della sezione *Layout Form dati*
   :header: "Campo", "Significato", "Note"
   :widths: 40,40,20

   "Tipo layout", "Indica il motore di interpretazione della descrizione formale della maschera di immissione del pagamento da parte del debitore", " Al momento solo *Angular Json schema form*"
   "Definizione", "Mostra il menu di caricamento e visualizzazione della descrizione formale dell'interfaccia di pagamento", ""

.. figure:: ../../_images/32MenuDefinizioneForm.png
   :align: center
   :name: MenuDefinizioneForm

   Funzionalità selezionabili per la definizione form

Sono presenti le voci:

* *Carica*: carica un nuovo file di definizione del form
* *Visualizza*: visualizza la definizione del form
* *Ripristina*: ripristina la definizione originaria del form

Vediamo un esempio di un file di definizione dell'interfaccia:

.. figure:: ../../_images/28SchemaFormEntrata1.jpg
   :align: center
   :name: MenuDefinizioneForm1

.. figure:: ../../_images/29SchemaFormEntrata2.jpg
   :align: center
   :name: MenuDefinizioneForm2

   Funzionalità selezionabili per la definizione form

Elaborazione
~~~~~~~~~~~~

La sezione *Elaborazione* consente a GovPay di descrivere in modo formale come elaborare quanto immesso nella sezione `Layout Form Dati`_ al fine di trasformare e inoltrare le informazioni del pagamento alle applicazioni (anche esterne) che ne abbisognino. Si pensi a uno scenario in cui, ad esempio, sia necessario informare un sistema di recupero crediti del fatto che una pendenza abbia superato la data di scadenza.

.. figure:: ../../_images/33SezioneElaborazioneDellaModificaPendenze.png
   :align: center
   :name: SezioneElaborazioneDellaSanzioneAmministrativa

   Funzionalità della sezione *Elaborazione*


.. csv-table:: Dettagli della sezione *Elaborazione*
   :header: "Campo", "Significato", "Note"
   :widths: 40,40,20

   "Validazione", "Selezione delle funzionalità sulla definizione della validazione in formato Json Schema", "* Carica
   * Visualizza
   * Ripristina"
   "Trasformazione: tipo template", "Motore di trasformazione delle informazioni immesse nel Form Dati", "Freemarker"
   "Trasformazione: Template", "Template di defizione della trasformazione dati", "* Carica
   * Visualizza
   * Ripristina"
   "Inoltro", "Consente di selezionare l'applicazione cui verranno inoltrati i dati", "L'applicazione deve essere censita nella sezione *Applicazioni*"


Promemoria Avviso Pagamento
~~~~~~~~~~~~~~~~~~~~~~~~~~~

La sezione *Avviso di pagamento* permette l'inoltro automatico verso la mail del debitore dell'avviso di pagamento. La tipologia di definizione del *subject* e del corpo della mail è, al momento, basata su `Freemarker <https://freemarker.apache.org/>`_

.. figure:: ../../_images/35EntrataPromemoriaAvvisoDiPagamento.png
   :align: center
   :name: PromemoriaAvvisoDiPagamento

   Informazioni della sezione *Promemoria Avviso Pagamento*


.. csv-table:: Dettagli della sezione *Promemoria Avviso Pagamento*
   :header: "Campo", "Significato", "Note"
   :widths: 40,40,20

   "Tipo template", "Motore di trasformazione delle informazioni immesse nel template *oggetto* e *messaggio* della mail di Avviso Pagamento", "Freemarker"
   "Template Oggetto", "Template di defizione dell'oggetto della mail di Avviso Pagamento", "* Carica
   * Visualizza
   * Ripristina"
   "Template Messaggio", "Template di defizione del messaggio della mail di Avviso Pagamento", "* Carica
   * Visualizza
   * Ripristina"
   "Allega pdf avviso", "Permette di allegare o meno il pdf dell'avviso di pagamento alla mail di promemoria", ""


Promemoria Ricevuta Telematica
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

La sezione *Promemoria Ricevuta Telematica* è del tutto analoga a quella relativa all' *Avviso di pagamento*: essa permette l'inoltro automatico verso la mail del debitore della ricevuta telematica dell'avvenuto pagamento. Anche in questo caso la tipologia di definizione formale del *oggetto* e del corpo della mail è, al momento, basata su `Freemarker <https://freemarker.apache.org/>`_

.. figure:: ../../_images/35EntrataPromemoriaRicevutaTelematica.png
   :align: center
   :name: PromemoriaRicevutaTelematica

   Informazioni della sezione *Promemoria Ricevuta Telematica*


.. csv-table:: Dettagli della sezione *Promemoria Ricevuta Telematica*
   :header: "Campo", "Significato", "Note"
   :widths: 40,40,20

   "Tipo template", "Motore di trasformazione delle informazioni immesse nel template *oggetto* e *messaggio* della mail di Ricevuta Telematica", "Freemarker"
   "Template Oggetto", "Template di defizione dell'oggetto della mail di Ricevuta Telematica", "* Carica
   * Visualizza
   * Ripristina"
   "Template Messaggio", "Template di defizione del messaggio della mail di Ricevuta Telematica", "* Carica
   * Visualizza
   * Ripristina"
   "Allega pdf avviso", "Permette di allegare o meno il pdf della Ricevuta Telematica", ""


Esempio di scenario di utilizzo
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
Come esempio di scenario di utilizzo possiamo cercare di mappare, sui componenti presentati, un semplice processo reale: si supponga di gestire, infatti, il pagamento spontaneo di dieci buoni pasto elettronici con relativo inoltro della codifica elettronica univoca, previo pagamento andato a buon fine, al richiedente.

.. csv-table:: Gestione buoni pasto elettronici
   :header: "#", "Oggetto della pendenza", "Passo di processo"
   :widths: 20,40,40

   "1", "Layout form dati", "Definizione form in cui si chiede il numero di buoni pasto richiesti"
   "2", "Elaborazione.Validazione", "Gestione delle soglie (es. massimo 20 buoni pasti a richiesta)"
   "3", "Elaborazione.Trasformazione", "Creazione della pendenza correlata al numero di buoni mensa effettivamente richiesti (es. determinazione del costo finale, con le varie franchigie, aggravi amministrativi e via dicendo)"
   "4", "Elaborazione.Applicazione", "Interfacciamento con l'applicazione verticale che crea i codici relativi ai buoni mensa richiesti"

E' di tutta evidenza come **questo non sia che uno dei molteplici processi che sono formalmente definibili, quindi implementabili direttamente, con i meccanismi appena visti, da GovPay**.
