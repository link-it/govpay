.. _govpay_configurazione_applicazioni:

Applicazioni
------------

Le Applicazioni in GovPay rappresentano i portali di pagamento e i sistemi applicativi gestionali dei debiti che si interfacciano tramite le Web API di integrazione.
Accedendo alla sezione *Configurazioni > Applicazioni*, viene visualizzato l'elenco delle applicazioni già censite. Sul lato sinistro
della pagina è presente un form che consente di filtrare i dati visualizzati nella pagina, come di seguito mostrato:

.. figure:: ../../_images/36Applicazioni.png
   :align: center
   :name: Applicazioni

   Vista generale delle applicazioni censite e criterio di filtro


Nuova Applicazione
~~~~~~~~~~~~~~~~~~

Per aggiungere una nuova applicazione, premere il pulsante posizionato, come sempre, in basso a destra. Analizzeremo questa funzionalità che è del tutto analoga, dal punto di vista delle informazioni richieste, a quella della modifica di un'applicazione già censita nel sistema.

.. figure:: ../../_images/37NuovaApplicazioneVistaDiInsieme.png
   :align: center
   :name: NuovaApplicazione

   Vista generale dei campi di una nuova applicazione

Analizziamo le sottosezioni in cui è strutturata l'applicazione, ovvero:
* Informazioni di riepilogo
* Codifica avvisi
* API integrazione
* Autorizzazioni API
* Autorizzazioni Backoffice


Informazioni di riepilogo
^^^^^^^^^^^^^^^^^^^^^^^^^
In questa sottosezione sono contenute le informazioni che definiscono un'applicazione in tutti i suoi aspetti di interazione con il sistema dei pagamenti.

.. figure:: ../../_images/38ApplicazioneRiepilogoDelleInformazioni.png
   :align: center
   :name: ApplicazioneInformazioniDiRiepilogo

   Informazioni di riepilogo di un'applicazione


.. csv-table:: Dettagli della sezione *Informazioni di riepilogo* di una nuova Applicazione
   :header: "Campo", "Significato", "Note"
   :widths: 40,40,20

   "Id A2A", "identificativo dell'applicazione", "Obbligatorio"
   "Principal", "Identificativo del principal autenticato nelle chiamate alle Web API di integrazione", ""
   "Abilitato", "se disabilitato, tutte le nuove richieste all'applicazione saranno negate", ""


Codifica avvisi
^^^^^^^^^^^^^^^
In questa sottosezione sono contenute le informazioni che definiscono un'applicazione in tutti i suoi aspetti di interazione con il sistema dei pagamenti.

.. figure:: ../../_images/39ApplicazioneCodificaAvvisi.png
   :align: center
   :name: ApplicazioneCodificaAvvisi

   Sezione Codifica Avvisi di un'applicazione


.. csv-table:: Dettagli della sezione *Codifica avvisi* di una nuova Applicazione
   :header: "Campo", "Significato", "Note"
   :widths: 40,40,20

   "Codifica IUV", "Numero identificativo dell'applicazione nel prefisso IUV, se configurato", ""
   "RegEx IUV", "Espressione regolare che consente di effettuare la validazione dei codici IUV inviati dall'applicazione", "es. 99[0-9]*"
   "Generazione IUV interna", "Se il flag è attivo l'applicazione genera autonomamente i codici IUV relativi alle proprie pendenze, altrimenti detti codici saranno generati da GovPay", ""


API Integrazione
^^^^^^^^^^^^^^^^

La piattaforma GovPay utilizza le API di Notifica e Verifica esposte dagli applicativi integrati
per completare gli scenari d'uso descritti nelle sezioni di integrazione. In questa sezione si
definiscono i dettagli del connettore per l'invocazione delle API.

.. figure:: ../../_images/40ApplicazioneAPIIntegrazione.png
   :align: center
   :name: ApplicazioneAPIIntegrazione

   Connettore alle API di Integrazione

.. csv-table:: Dettagli della sezione *API Integrazione* di una Applicazione
   :header: "Campo", "Significato"
   :widths: 40,60

   "API Integrazione", "URL di invocazione del servizio esposto dall'applicazione"
   "Versione API", "Versione delle API da invocare"
   "Tipo Autenticazione", "Modalità di autenticazione di GovPay verso le API"


Autorizzazioni
^^^^^^^^^^^^^^

In questa sezione è possibile specificare le autorizzazioni dell'Applicazione sulle API di integrazione:

.. figure:: ../../_images/41ApplicazioneAutorizzazioni.png
	:align: center
	:name: ApplicazioneAutorizzazioni

	Sezione Autorizzazioni di un'applicazione
	
.. csv-table:: Dettagli della sezione *API Integrazione* di una Applicazione
   :header: "Campo", "Significato"
   :widths: 40,60

   "Tipi pendenza", "Lista delle tipologie di pendenza su cui l'applicazione può operare"
   "Ruoli", "Ruoli autorizzativi sulle API di Backoffice"
   "API Pagamenti", "Abilita/disabilita l'utilizzo delle Api di pagamento"
   "API Pendenze", "Abilita/disabilita l'utilizzo delle Api di pendenze"
   "API Ragioneria", "Abilita/disabilita l'utilizzo delle Api di ragioneria"
   "Enti creditori", "Lista degli Enti ed unità operative su cui l'applicazione può operare"

