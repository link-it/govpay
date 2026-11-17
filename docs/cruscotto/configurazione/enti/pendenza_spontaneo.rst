.. _govpay_configurazione_enti_spontaneo:

Configurazione del tipo pendenza per il pagamento spontaneo.
------------------------------------------------------------

È possibile predisporre delle form personalizzate nel Portale di Pagamento per consentire
il pagamento spontaneo di pendenze richiedendo al cittadino debitore un set minimale di informazioni
e codificando la logica di creazione della pendenza.

La form è pubblicata sul portale solo se il campo *Abilitato* della sezione vale *Si*, oppure vale *Default* e la funzione è abilitata nella configurazione di base del tipo pendenza.

.. figure:: ../../_images/31TipoPendenza_Formspontaneo.png
   :align: center
   :name: 31TipoPendenza_Formspontaneo

   Pagamento spontaneo da portale

Form inserimento Dati
~~~~~~~~~~~~~~~~~~~~~

In questa sezione viene definito il layout della form di acquisizione dei dati necessari all'istruttoria della posizione che
saranno poi l'input alla successiva fase di validazione. Il Portale deve essere in grado di acquisire le informazioni dei servizi
e visualizzarle al cittadino. Un esempio di implementazione è il `Portale di Pagamento GovPay <https://github.com/link-it/govpay-portal>`_, che legge tipi pendenza
e definizioni delle form e crea le pendenze di pagamento spontaneo tramite `govpay-portal-api <https://github.com/link-it/govpay-portal-api>`_:

.. csv-table:: *Form inserimento Dati*
   :header: "Campo", "Descrizione"
   :widths: 40,60

   "Tipo layout", "Indica il motore di interpretazione della definizione della form"
   "Definizione", "File che definisce la form nel formalismo scelto in `Tipo Layout`"
   "Impaginazione", "Definisce testi ed immagini a corredo della form di input"

I tipi di layout selezionabili in console sono i seguenti:

	-  *Angular json schema form*: `Progetto GitHub <https://github.com/dschnelldavis/angular2-json-schema-form>`_ - `Playground <https://angular2-json-schema-form.firebaseapp.com/>`_.
	-  *SurveyJS*: `Sito del progetto <https://surveyjs.io/>`_.

Il Portale di Pagamento GovPay visualizza entrambi i formalismi.

La struttura del documento di impaginazione dipende dall'implementazione del Portale al cittadino. Nel caso del Portale di Pagamento GovPay
il JSON deve rispettare il :download:`seguente schema <paginazione.json>`

Validazione
~~~~~~~~~~~

È possibile configurare in questa sezione il JSON Schema con cui validare i dati inviati dalla form definita nella sezione precedente.

Trasformazione dati
~~~~~~~~~~~~~~~~~~~

In questa sezione può essere configurato il processo che, dai dati inseriti nella form, produce una pendenza. Il dato di Input è il JSON prodotto
dalla form di inserimento dati, mentre l'output deve essere un JSON che rispetta lo schema di una `PendenzaPost <https://github.com/link-it/govpay/blob/3.10.x/wars/api-backoffice/src/main/webapp/v1/govpay-api-backoffice-v1.yaml>`__ (schema ``pendenzaPost`` delle API Backoffice)
oppure un JSON compatibile con il successivo servizio di inoltro, se configurato.

Inoltro
~~~~~~~

È possibile inviare i dati ricevuti ed eventualmente trasformati ad una applicazione per il processo di istruttoria. Il JSON ritornato da tale
servizio deve rispettare lo schema di una `PendenzaPost <https://github.com/link-it/govpay/blob/3.10.x/wars/api-backoffice/src/main/webapp/v1/govpay-api-backoffice-v1.yaml>`__ (schema ``pendenzaPost`` delle API Backoffice).
