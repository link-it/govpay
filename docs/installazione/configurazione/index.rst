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

Produzione dell'installer dai sorgenti
--------------------------------------

La compilazione dei sorgenti richiede:

- Maven 3.8 o successivo
- Java 21

L'installer può essere prodotto con la seguente procedura:

1. Clonare il repository git di GovPay: `git clone https://github.com/link-it/govpay.git`
2. Compilare con il comando `mvn -Denv=installer_template clean install`
3. Spostarsi nella cartella **src/main/resources/setup/** ed eseguire lo script **prepareSetup.sh**

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

.. figure:: ../_images/INS01_AvvioInstaller.png
   :alt: Pagina introduttiva all'avvio dell'Installer
   :align: center
   :name: PaginaIntroduttivaInstaller

   Pagina introduttiva all'avvio dell'Installer

Informazioni Preliminari
~~~~~~~~~~~~~~~~~~~~~~~~

La schermata "Informazioni Preliminari" consente di inserire i dati sul
contesto di installazione nell'ambiente di esercizio.

.. figure:: ../_images/INS02_InformazioniPreliminari.png
   :alt: Pagina relativa alle informazioni preliminari
   :align: center
   :name: InstallazioneInformazioniPreliminari

   Informazioni preliminari

Devono essere inserite le seguenti informazioni:

-  **DB Platform:** selezionare la piattaforma RDBMS utilizzata
-  **Work Folder:** inserire il path assoluto della *directory*, presente nell'ambiente di destinazione, che sarà utilizzata da GovPay per accedere a dati accessori legati alle funzionalità opzionali, ad esempio:
 -  **file di configurazione personalizzati**
 -  **file di configurazione Log4j personalizzati**
 -  **file di configurazione di Spring Security personalizzati**

-  **Log Folder**: inserire il path assoluto della directory, presente nell'ambiente di destinazione, che sarà utilizzata da GovPay per inserire i diversi file di tracciamento prodotti.
-  **Visualizza Impostazioni Avanzate**: opzioni per abilitare funzionalità avanzate del prodotto, saranno visualizzate in un passaggio più avanti.


Riferimenti Accesso GovPay
~~~~~~~~~~~~~~~~~~~~~~~~~~

Questo passaggio prevede che vengano inserite le credenziali del cruscotto di gestione:

Credenziali dell'utente amministratore:

-  **Nome e Cognome:** nome e cognome da associare alle credenziali di amministrazione
-  **Username:** indicare l'identificativo dell'utenza di amministrazione per l'accesso alla console di gestione e monitoraggio.
-  **Password:** indicare la password seguendo i vincoli "deve contenere almeno una maiuscola, una minuscola, un numero, un carattere speciale ed essere lunga almeno 8 caratteri".

.. figure:: ../_images/INS03_InformazioniApplicative.png
   :alt: Pagina relativa ai Riferimenti Accesso GovPay
   :align: center
   :name: InstallazioneInformazioniApplicative

   Riferimenti Accesso GovPay


Il Database
~~~~~~~~~~~

Nella schermata "Il Database" si devono inserire i riferimenti per
l'accesso al database di esercizio di GovPay.

.. figure:: ../_images/INS04_InformazioniAccessoDatabase.png
   :alt: Pagina relativa alle informazioni di accesso al database
   :align: center
   :name: InstallazioneInformazioniAccessoDB

   Informazioni accesso al DB


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

Servizi Backoffice
~~~~~~~~~~~~~~~~~~

Nella schermata "Servizi Backoffice" si devono inserire i riferimenti alle BaseURL delle risorse utilizzate dal cruscotto.

.. figure:: ../_images/INS07_ConfigurazioneServiziCruscotto.png
   :alt: Pagina relativa alle baseURL dei servizi utilizzati dal cruscotto
   :align: center
   :name: InstallazioneConfigurazioneServiziCruscotto

   Configurazione Servizi Cruscotto


-  **Base path risorse statiche console**: BaseURL dove il cruscotto ricerca le risorse statiche
-  **Base path API Backoffice**: BaseURL delle API-Backoffice

File di configurazione Console Backoffice
~~~~~~~~~~~~~~~~~~

Nella schermata "File di configurazione Console Backoffice" si deve inserire il riferimento al file di configurazione utilizzato dal cruscotto.

.. figure:: ../_images/INS08_ConfigurazioneConsoleBackoffice.png
   :alt: Path relativo al file di configurazione utilizzato dal cruscotto
   :align: center
   :name: InstallazioneConfigurazioneConsoleBackoffice

   File di configurazione Console Backoffice


-  **Path del file di configurazione della console**: Il path del file di configurazione della console


Configurazioni Avanzate
~~~~~~~~~~~~~~~~~~~~~~~

La schermata "Configurazioni Avanzate" è presente solo se nel passaggio "Informazioni Preliminari" è stata selezionata l'opzione "Visualizza Impostazioni Avanzate".

.. figure:: ../_images/INS06_ConfigurazioniAvanzate.png
   :alt: Pagina relativa alle configurazioni avanzate
   :align: center
   :name: InstallazioneConfigurazioniAvanzate

   Configurazioni Avanzate

**Configurazione Spring Security**

-  **Usa file di configurazione esterni:** opzione che abilita i file di configurazione esterni di Spring per l'accesso alle funzionalità avanzate, come la configurazione delle modalità di autenticazione (:ref:`inst_dispiegamento_auth`).


Installazione
~~~~~~~~~~~~~

Premendo il pulsante **Install** il processo di configurazione termina
con la produzione dei files necessari per l’installazione di GovPay che
verranno inseriti nella nuova directory **dist** creata al termine di
questo processo.

.. figure:: ../_images/INS05_InstallazioneTerminata.png
   :alt: Pagina relativa alla fine dell'installazione
   :align: center
   :name: InstallazioneTerminata

   Installazione terminata


I files presenti nella directory **dist** dovranno essere utilizzati
nella fase successiva di dispiegamento di GovPay.
