.. _inst_configurazione:

Configurazione dei moduli applicativi
=====================================

La fase di configurazione dei moduli applicativi consente di impostare i
dati di riferimento del proprio ambiente di installazione, tramite una
procedura basata sul modello wizard.

Download
--------

Scaricare l'ultima versione (binary release) di GovPay dal sito GitHub
https://github.com/link-it/GovPay.

Esecuzione dell'Installer
-------------------------

L'archivio di installazione può essere scompattato e il relativo
installer eseguito su un ambiente che non deve essere necessariamente
quello di destinazione. Infatti l'Installer non installa il prodotto ma
produce tutti gli elementi necessari che dovranno essere dispiegati
nell’ambiente di esercizio.

Per l'esecuzione dell'installer verificare ed eventualmente impostare la
variabile d’ambiente **JAVA_HOME** in modo che riferisca la directory
radice dell'installazione di Java. Eseguire quindi l'installer mandando
in esecuzione il file **install.sh** su Unix/Linux, oppure
**install.cmd** su Windows.

Avvio
~~~~~

L’Installer mostra all’avvio una pagina introduttiva.

Sono mostrate informazioni quali:

-  Nome e versione del prodotto
-  Informazioni sul copyright
-  Informazioni sulla licenza d'uso

Selezionando il pulsante Next si procede con la configurazione del
software.

.. figure:: ../_figure_installazione/100002010000022600000192C7342CEDBB4934E5.png
   :alt: Figura 1: Pagina introduttiva all'avvio dell'Installer
   :width: 14.224cm
   :height: 10.211cm

   Figura 1: Pagina introduttiva all'avvio dell'Installer

Informazioni Preliminari
~~~~~~~~~~~~~~~~~~~~~~~~

La schermata "Informazioni Preliminari" consente di inserire i dati sul
contesto di installazione nell'ambiente di esercizio.

.. figure:: ../_figure_installazione/100002010000022700000192CD0548360449197A.png
   :alt: Figura 2: Informazioni Preliminari
   :width: 14.224cm
   :height: 10.363cm

   Figura 2: Informazioni Preliminari

Devono essere inserite le seguenti informazioni:

-  **Application Server:** la scelta dell'application server è vincolata su "WildFly 11.0"**
-  **Work Folder:** inserire il path assoluto della *directory*, presente nell'ambiente di destinazione, che sarà utilizzata da GovPay per accedere a dati accessori legati alle funzionalità opzionali, ad esempio:
   -  **file di configurazione personalizzati**
   -  **loghi dei psp**

-  **Log Folder**: inserire il path assoluto della directory, presente nell'ambiente di destinazione, che sarà utilizzata da GovPay per
   inserire i diversi file di tracciamento prodotti.

Informazioni Applicative
~~~~~~~~~~~~~~~~~~~~~~~~

-  *Username Amministratore:* indicare l'identificativo dell'utenza di
   amministrazione per l'accesso alla console di gestione e
   monitoraggio. Tipicamente si fornisce il "principal" dell'utenza
   applicativa registrata sull'Application Server, ma è in alternativa
   possibile indicare altre tipologie di utenze, come ad esempio
   identificate dal Certificato Client Digitale.
-  *Nome Dominio:* inserire l'hostname tramite il quale saranno
   raggiungibili i servizi di GovPay (ad esempio la console di
   monitoraggio).

.. figure:: ../_figure_installazione/100002010000022700000192D4FF505CBCE8C644.png
   :alt: Figura 3: Informazioni Applicative
   :width: 14.143cm
   :height: 9.959cm

   Figura 3: Informazioni Applicative

Il Database
~~~~~~~~~~~

Nella schermata "Il Database" si devono inserire i riferimenti per
l'accesso al database di esercizio di GovPay.

.. figure:: ../_figure_installazione/100002010000022600000192A2989B695B3A28EB.png
   :alt: Figura 4: Informazioni Accesso Database
   :width: 14.21cm
   :height: 10.183cm

   Figura 4: Informazioni Accesso Database

-  **DB Platform:** selezionare la piattaforma RDBMS utilizzata
-  **Hostname**: indirizzo per raggiungere il database
-  **Porta**: la porta da associare all’hostname per la connessione al
   database
-  **Nome Database**: il nome dell’istanza del database a supporto di
   GovPay.
-  **Username**: l’utente con diritti di lettura/scrittura sul database
   sopra indicato.
-  **Password**: la password dell’utente del database.

.. note::
    Non è necessario che il database e l'utente indicato esistano in questa fase. Potranno essere creati nella successiva fase di dispiegamento purché i dati relativi coincidano con i valori inseriti in questi campi del wizard.

Installazione
~~~~~~~~~~~~~

Premendo il pulsante **Install** il processo di configurazione termina
con la produzione dei files necessari per l’installazione di GovPay che
verranno inseriti nella nuova directory **dist** creata al termine di
questo processo.

.. figure:: ../_figure_installazione/1000020100000227000001912C8859F6CB3B2892.png
   :alt: Figura 5: Installazione Terminata
   :width: 14.446cm
   :height: 10.53cm

   Figura 5: Installazione Terminata

I files presenti nella directory **dist** dovranno essere utilizzati
nella fase successiva di dispiegamento di GovPay.

