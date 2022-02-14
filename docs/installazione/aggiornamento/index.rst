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

Nel caso di aggiornamento di una minor version, ad esempio per aggiornare la versione 3.1.1 alla 3.3.3,
è necessario sostituire l'archivio EAR dispiegato nell'Application Server ed applicare in sequenza le patch 3.2.sql e 3.3.sql.

Si consiglia di effettuare le operazioni di aggiornamento con l'Application Server spento
e di eseguire un backup del DB prima di applicare le patch.

Aggiornamento da versioni precedenti alla v3.5.0
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Il nuovo client HTTP utilizzato da GovPay ignora la configurazione del keystore e trustore della JVM. 
Risulta quindi necessario verificare che i parametri per l'autenticazione SSL, ove prevista, siano
specificati tramite Cruscotto di Backoffice nella configurazione delle Applicazioni e degli Intermediari.

