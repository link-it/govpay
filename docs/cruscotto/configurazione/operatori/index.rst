.. _govpay_configurazione_operatori:

Operatori
---------

Gli operatori rappresentano gli utenti autorizzati all'accesso al cruscotto di gestione di GovPay. Accedendo alla sezione *Configurazioni > Operatori*, il sistema visualizza l'elenco degli operatori già censiti. Sul lato sinistro della pagina è presente un form che consente di filtrare gli operatori in relazione al proprio stato.
Gli elementi nell'elenco identificano gli operatori presenti visualizzando i campi *principal* e *nome*.

Nuovo Operatore
~~~~~~~~~~~~~~~

Tramite il pulsante presente nella pagina di elenco è possibile aprire il form di creazione di un operatore:


.. figure:: ../../_images/44Nuovo1Operatore.png
   :align: center
   :name: NuovoOperatore

   Definizione di un nuovo Operatore


.. csv-table:: Informazioni di dettaglio di un nuovo Operatore
   :header: "Campo", "Significato", "Note"
   :widths: 40,40,20

   "Principal", "Identificativo dell'operatore dato da PagoPa", "Obbligatorio"
   "Nome", "Nome e cognome dell'operatore", "Obbligatorio"
   "Abilitato", "Indica se l'operatore ha o meno l'accesso al Cruscotto di gestione", ""
   "Domini", "Indica i domini su cui può svolgere compiti l'Operatore", "E' presente l'opzione *Tutti* che permette a una sola utenza di operare trasversalmente a più domini"
   "Tipi pendenza", "Selezione delle pendenze su cui l'operatore può operare", "Presente l'opzione *Tutti*"
   "Ruoli", "Ruoli cui l'utente è abilitato: ogni ruolo ha un perimetro autorizzativo che l'operatore eredita", ""


Dettaglio Operatore
~~~~~~~~~~~~~~~~~~~

Dalla pagina elenco degli operatori, selezionando uno degli elementi, si giunge alla relativa pagina con le informazioni di sintesi.


.. figure:: ../../_images/45OperatoreVistaSintesi.png
   :align: center
   :name: OperatoreVistaDiSintesi

   Vista di sintesi di un Operatore


Da quest'ultima è possibile, con l'uso delle solite metafore (matita su cerchio verde in basso a destra), accedere alle modifiche puntuali della definizione di un operatore. In tale processo le informazioni rimangono esattamente quelle appena viste per la definizione di una nuova applicazione, con una sola informazione non modificabile, ovvero *principal*.
