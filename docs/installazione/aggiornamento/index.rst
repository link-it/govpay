.. _update_ambiente:

Aggiornamento di versione
=========================

GovPay viene costantemente aggiornato per la risoluzione di problemi o l'implementazione
di nuove funzionalità, pertanto può risultare necessario effettuare l'installazione di una nuova
versione nella propria piattaforma.

GovPay segue il `versionamento semantico <https://semver.org/lang/it/>`_, pertanto si presentano solitamente due casistiche:

- Aggiornamento di una patch version
- Aggiornamento di una minor version

Non tratteremo il caso di aggiornamento di una major version poichè attualmente non previste.

Aggiornamento di una patch version
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Nel caso di aggiornamento di una patch version, ad esempio per aggiornare la versione 3.1.1 alla 3.1.3,
è sufficiente sostituire l'archivio EAR dispiegato nell'Application Server. 

Si consiglia di effettuare l'aggiornamento dell'EAR con l'Application Server spento.

Aggiornamento di una minor version
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Nel caso di aggiornamento di una minor version oltre a sostituire l'EAR può essere necessario:

- Applicare una o più patch al database
- Aggiungere i file di Spring security alla directory di lavoro la configurazione usata lo prevede

Ad esempio, nel caso di aggiornamento dalla versione 3.4.1 alla versione 3.6.0 è necessario

- Applicare le patch 3.5.sql e 3.6.sql
- Copiare il file di configurazione api-jppapdp-applicationContext-security.xml nella directory di lavoro
 
Si consiglia di effettuare le operazioni di aggiornamento con l'Application Server spento
e di eseguire un backup del DB prima di applicare le patch.

Aggiornamento da versioni precedenti la 3.5
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Il nuovo client HTTP utilizzato da GovPay ignora la configurazione del keystore e trustore della JVM. 
Risulta quindi necessario verificare che i parametri per l'autenticazione SSL, ove prevista, siano
specificati tramite Cruscotto di Backoffice nella configurazione delle Applicazioni e degli Intermediari.

Aggiornamento da versioni precedenti la 3.7
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

La distribuzione standard di GovPay prevede il seguente stack applicativo:

- Java 11
- Wildfly 26

Aggiornamento da versioni precedenti la 3.8
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

La distribuzione standard di GovPay dalla versione 3.8.0 prevede il seguente stack applicativo:

- Java 21
- Tomcat 10/11

I servizi sono esposti utilizzando nuove BaseUrl, di seguito il mapping con le vecchie versioni:

+---------------------------------+-------------------------------------------------+
| 3.7.x                           |  3.8.0                                          |
+---------------------------------+-------------------------------------------------+
| /govpay/frontend/api/pagopa     | /govpay-api-pagopa                              |
+---------------------------------+-------------------------------------------------+
| /govpay/frontend/api/pagamento  | /govpay-api-pagamento                           |
+---------------------------------+-------------------------------------------------+
| /govpay/frontend/api/user       | /govpay-api-user                                |
+---------------------------------+-------------------------------------------------+
| /govpay/frontend/web/connector  | /govpay-web-connector                           |
+---------------------------------+-------------------------------------------------+
| /govpay/backend/api/pendenze    | /govpay-api-pendenze                            |
+---------------------------------+-------------------------------------------------+
| /govpay/backend/api/ragioneria  | /govpay-api-ragioneria                          |
+---------------------------------+-------------------------------------------------+
| /govpay/backend/api/jppapdp     | /govpay-api-jppapdp                             |
+---------------------------------+-------------------------------------------------+
| /govpay/backend/api/backoffice  | /govpay-api-backoffice                          |
+---------------------------------+-------------------------------------------------+
| /govpay/backend/gui/backoffice  | /govpay-console                                 |
+---------------------------------+-------------------------------------------------+

