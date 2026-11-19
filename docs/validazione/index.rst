.. _govpay_validazione:

Processo di validazione
#######################

Ogni nuova versione di GovPay viene sottoposta alle seguenti verifiche di sicurezza al fine di assicurarne la stabilità e l’assenza di vulnerabilità note:

- Static Code Analysis: identifica possibili vulnerabilità all’interno del codice sorgente utilizzando gli strumenti `SpotBugs <https://spotbugs.github.io/>`_ e `SonarCloud (SonarQube Cloud) <https://sonarcloud.io/>`_, con quality gate bloccante e copertura del codice misurata con `JaCoCo <https://www.jacoco.org/jacoco/>`_ durante i test di integrazione;
- Dynamic Analysis: cerca vulnerabilità del software durante l’effettiva esecuzione del prodotto, con una scansione DAST delle applicazioni web in esecuzione tramite `OWASP ZAP <https://www.zaproxy.org/>`_; il funzionamento del prodotto è verificato da estese batterie di test funzionali realizzate con `Karate <https://github.com/karatelabs/karate>`_ (vedi :ref:`govpay_validazione_testsuite`);
- Accessibilità: verifica dell'accessibilità delle applicazioni web in esecuzione con lo strumento gov-a11y;
- Third Party Dependency Analysis: assicura che tutte le librerie terza parte utilizzate non siano soggette a vulnerabilità di sicurezza note, utilizzando il tool `OWASP Dependency-Check <https://owasp.org/www-project-dependency-check/>`_;
- OSV Analysis: verifica le dipendenze del progetto rispetto al database `OSV <https://osv.dev/>`_ tramite `OSV-Scanner <https://github.com/google/osv-scanner>`_, identificando vulnerabilità note nelle librerie terza parte utilizzate.

Le verifiche sono eseguite automaticamente attraverso due sistemi di Continuous Integration:

Pipeline GitHub Actions
***********************

La `pipeline GitHub Actions <https://github.com/link-it/govpay/actions>`_ viene eseguita ad ogni push sul repository e ad ogni pull request verso il branch `master` e le linee di manutenzione (branch `x.y.x`) ed effettua le seguenti analisi:

- **Build e OWASP Dependency-Check**: compilazione del progetto con analisi delle dipendenze tramite OWASP Dependency-Check, con consultazione del database NVD (l'analizzatore OSS Index è disattivato per default, proprietà Maven ``owasp.ossindex.analyzer.enabled``). Il database locale di OWASP Dependency-Check viene aggiornato ogni giorno da un workflow dedicato;
- **SpotBugs**: analisi statica del codice sorgente con il plugin `FindSecBugs <https://find-sec-bugs.github.io/>`_ per l'identificazione di vulnerabilità di sicurezza;
- **OSV Scan**: scansione delle dipendenze rispetto al database OSV tramite `OSV-Scanner <https://github.com/google/osv-scanner>`_;
- **License Analysis**: verifica delle licenze delle dipendenze terza parte per garantire la compatibilità con la licenza del progetto;
- **SBOM e Dependency-Track**: generazione della Software Bill of Materials in formato `CycloneDX <https://cyclonedx.org/>`_ e, per i tag, il branch `master` e le linee di manutenzione, caricamento su `Dependency-Track <https://dependencytrack.org/>`_ per il monitoraggio continuo delle vulnerabilità;
- **Scansione delle immagini Docker**: le immagini Docker costruite dalla pipeline sono analizzate con `Trivy <https://trivy.dev/>`_ e `Grype <https://github.com/anchore/grype>`_ prima della pubblicazione; la presenza di vulnerabilità CRITICAL o HIGH con correzione disponibile, rilevate da Trivy, blocca la pubblicazione dell'immagine.

Sulle pull request viene inoltre validato il file ``publiccode.yml`` del repository.

I report prodotti dalle analisi vengono raccolti come artifact della pipeline e, in caso di rilascio (tag), inclusi nello ZIP dei report allegato alla GitHub Release, insieme allo SBOM; i report della scansione delle immagini Docker sono allegati alla stessa release.

Pipeline Jenkins
****************

Il sistema di `Continuous Integration Jenkins di GovPay <https://jenkins.link.it/govpay/job/govpay/>`_ esegue le verifiche ad ogni modifica del codice ed effettua le seguenti analisi:

- **SpotBugs con FindSecBugs**: analisi statica del codice sorgente per l'identificazione di vulnerabilità;
- **OWASP Dependency-Check**: analisi delle dipendenze terza parte per vulnerabilità note;
- **Test di integrazione**: esecuzione delle batterie di test realizzate con `Karate <https://github.com/karatelabs/karate>`_ (vedi :ref:`govpay_validazione_testsuite`), con misura della copertura del codice tramite JaCoCo;
- **OWASP ZAP**: scansione DAST della console di gestione e del portale dei pagamenti in esecuzione;
- **Accessibilità**: verifica dell'accessibilità delle stesse applicazioni web con gov-a11y;
- **SonarCloud**: analisi della qualità del codice e delle vulnerabilità, con la copertura misurata durante i test di integrazione e attesa dell'esito del quality gate;
- **Raccolta delle evidenze**: i report dei test Karate, di SpotBugs e di OWASP Dependency-Check sono archiviati sul sistema di Continuous Integration, distinti per branch, versione e commit verificati.

Falsi positivi
**************

Di seguito le segnalazioni emerse dagli strumenti utilizzati nel processo di validazione che sono stati classificati come Falsi Positivi

CVE-2017-10355
================

file name: xercesImpl-2.12.2.jar

La vulnerabilità rilevata è oggetto di discussione e aperture di segnalazioni, poichè non presente nel database nvd.nist.gov e quindi non può essere una CVE.
Sembra invece rilevata da Sonatype OSSIndex come riportato dal frammento delle discussioni sotto riportate "the intelligence that this CVE (still) applies to version 2.12.2 comes from the security analysts of Sonatype OSSINDEX, not from the NVD datastreams":

- https://github.com/jeremylong/DependencyCheck/issues/4614
- https://github.com/OSSIndex/vulns/issues/316

In particolare 'https://ossindex.sonatype.org/vulnerability/sonatype-2017-0348' non ha poi una evidenza nel blog esistente (il link https://blogs.securiteam.com/index.php/archives/3271 non esiste).
Il contenuto del blog, quando era esistente, viene riportato nell'issue '4614' di jeremylong, dove si può ipotizzare che la problematica rilevata sia sul metodo XMLEntityManager.setupCurrentEntity() che non dispone di un meccanismo di timeout.

Il metodo indicato non è utilizzato su GovPay.
Nella discussione inoltre si fa riferimento alla vulnerabilità descritta in 'https://security.snyk.io/vuln/SNYK-JAVA-XERCES-31497' che consentiva di attuare attacchi DOS.
Per i motivi descritti sopra si ritiene la vulnerabilità un falso positivo.

CVE-2021-4277
=============

file name: openspcoop2_utils-crypt-3.2.1.jar

La vulnerabilità rilevata si riferisce all'utilizzo dell'utility 'fredsmith' nel processamento del file 'screenshot_sync' del componente 'Filename Handler'.

La segnalazione riguarda la versione 3.2.1 della libreria inclusa nell'installer. La libreria openspcoop2_utils-crypt non contiene il componente indicato.

CVE-2025-10492
==============

file name: jasperreports-6.21.5.jar

La vulnerabilità rilevata si riferisce alla funzione di deserializzazione presente nella libreria Jaspersoft.

GovPay non carica né deserializza modelli/report provenienti dagli utenti (JRXML/.jasper) ma solo i template interni.
Per quanto riguarda i dati di input si tratta sempre di dati interni a GovPay.
Per i motivi descritti sopra si ritiene la vulnerabilità un falso positivo.


.. _govpay_validazione_testsuite:

Test di copertura funzionale
****************************

Il corretto funzionamento del prodotto è verificato dalla testsuite di integrazione realizzata con `Karate <https://github.com/karatelabs/karate>`_, il cui codice è consultabile nella directory `integration-test/src/test/java/test <https://github.com/link-it/govpay/tree/3.10.x/integration-test/src/test/java/test>`_ del repository.

Ogni test è descritto in un file ``.feature``; i test sono raggruppati per area in directory, ciascuna con una classe JUnit (``*Test.java``) che esegue le feature della propria directory e delle sottodirectory e avvia un servizio simulato dell'ente sulla porta 8888.

Aree della testsuite
====================

La directory ``api`` contiene i test delle API esposte e invocate da GovPay:

- ``api/appio``: invio dell'avviso di pagamento e notifica della ricevuta tramite l'app IO;
- ``api/backoffice``: API Backoffice; la directory ``v1`` verifica le API esposte dal core, la directory ``v2`` contiene i test di API non incluse nel rilascio 3.10;
- ``api/ente``: API Ente Creditore invocate da GovPay (v1 e v2): verifica di una pendenza a partire dall'avviso e notifica della ricevuta di pagamento;
- ``api/pagopa``: API pagoPA esposte da GovPay: verifica dell'avviso, attivazione del pagamento e ricevuta, e risposta delle primitive dismesse;
- ``api/pendenza``: API Pendenze (v1 e v2): creazione, modifica e consultazione delle pendenze, avvisi, profilo e richieste di pagamento;
- ``api/ragioneria``: API Ragioneria (v1, v2 e v3): flussi di rendicontazione, riscossioni, riconciliazioni, ricevute e profilo.

La directory ``workflow`` contiene i test dei flussi di pagamento completi:

- ``workflow/modellounico``: pagamenti con il modello unico pagoPA (pagamento eseguito, non eseguito, annullato, duplicato, scaduto, con importo errato o data di validità decorsa, verifica della sonda pagoPA, acquisizione delle ricevute in stand-in);
- ``workflow/pubblicazione``: data di pubblicazione della pendenza;
- ``workflow/send``: attualizzazione delle pendenze con le spese di notifica del servizio SEND.

Esecuzione
==========

I test richiedono un'istanza di GovPay in esecuzione e i servizi di supporto (simulatore del Nodo dei Pagamenti, database, server SMTP simulato); gli indirizzi sono configurati in ``integration-test/src/test/java/karate-config.js``.

Per eseguire l'intera testsuite:

.. code-block:: bash

   cd integration-test
   mvn test

Per eseguire una sola area si indica la relativa classe JUnit; per eseguire una singola feature la si indica anche nella proprietà ``karate.options``:

.. code-block:: bash

   mvn test -Dtest=WorkflowTest
   mvn test -Dtest=WorkflowTest -Dkarate.options="classpath:test/workflow/send/v1/send-nav-notificato.feature"

Altre proprietà di sistema lette dalla configurazione:

- ``govpay.db.url``, ``govpay.db.username``, ``govpay.db.password``, ``govpay.db.driverClassName``: connessione al database di GovPay usata da alcuni test (per default ``jdbc:postgresql://localhost:5432/govpay``);
- ``send.importoBase``: importo base delle spese di notifica SEND configurato nell'istanza sotto test (proprietà ``it.govpay.send.speseNotifica.importoBase``), ad esempio ``-Dsend.importoBase=2.00``.

I risultati sono prodotti in ``integration-test/target/surefire-reports``.
