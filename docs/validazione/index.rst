.. _govpay_validazione:

Processo di validazione
#######################

Ogni nuova versione di GovPay viene sottoposta alle seguenti verifiche di sicurezza al fine di assicurarne la stabilità e l’assenza di vulnerabilità note:

- Static Code Analysis: identifica possibili vulnerabilità all’interno del codice sorgente utilizzando gli strumenti `SpotBugs <https://spotbugs.github.io/>`_ e `Sonarqube <https://www.sonarsource.com/products/sonarqube/>`_;
- Dynamic Analysis: cerca vulnerabilità del software durante l’effettiva esecuzione del prodotto. L’analisi viene eseguita attraverso l’esecuzione di estese batterie di test realizzate con `Karate <https://github.com/karatelabs/karate>`_;
- Third Party Dependency Analysis: assicura che tutte le librerie terza parte utilizzate non siano soggette a vulnerabilità di sicurezza note, utilizzando il tool `OWASP Dependency-Check </https://owasp.org/www-project-dependency-check/>`_.

Le verifiche sono eseguite automaticamente ad ogni modifica del codice di GovPay sul branch `master` dal sistema di `Continuous Integration Jenkins di GovPay <https://jenkins.link.it/govpay/blue/organizations/jenkins/govpay/activity/>`_.

Falsi positivi
**************

Di seguito le segnalazioni emerse dagli strumenti utilizzati nel processo di validazione che sono stati classificati come Falsi Positivi

CVE-2022-45688
==============

file name: json-20230227.jar

La vulnerabilità indicata viene descritta come segue: `A stack overflow in the XML.toJSONObject component of hutool-json v5.8.10 allows attackers to cause a Denial of Service (DoS) via crafted JSON or XML data.`

La versione della libreria ultilizzata risolve il bug indicato, ma il tool di validazione continua a segnalare la vulnerabilità. Risulta quindi un falso positivo.

CVE-2022-40152
==============

file name: stax2-api-4.2.1.jar

La vulnerabilità 'CVE-2022-40152' è relativa alla dipendenza transitiva 'woodstox-core'. In GovPay non viene utilizzata la versione definita nella dipendenza transitiva, ma bensì la versione woodstox-core-6.4.0.jar che non presenta la vulnerabilità.

CVE-2020-5408
=============

file name: spring-security-crypto-5.8.1.jar

La vulnerabilità indicata viene descritta come segue: `Spring Security versions 5.3.x prior to 5.3.2, 5.2.x prior to 5.2.4, 5.1.x prior to 5.1.10, 5.0.x prior to 5.0.16 and 4.2.x prior to 4.2.16 use a fixed null initialization vector with CBC Mode in the implementation of the queryable text encryptor. A malicious user with access to the data that has been encrypted using such an encryptor may be able to derive the unencrypted values using a dictionary attack.`

La versione utilizzata è superiore alla '5.3.2' quindi risulta un falso positivo ed in GovPay il metodo oggetto della vulnerabilità (Encryptors#queryableText(CharSequence, CharSequence)) non viene utilizzato.

CVE-2016-1000027
================

file name: spring-web-5.3.25.jar

La vulnerabilità indicata viene descritta come segue: `Pivotal Spring Framework through 5.3.16 suffers from a potential remote code execution (RCE) issue if used for Java deserialization of untrusted data. Depending on how the library is implemented within a product, this issue may or not occur, and authentication may be required. NOTE: the vendor's position is that untrusted data is not an intended use case. The product's behavior will not be changed because some users rely on deserialization of trusted data.`

La versione utilizzata è superiore alla '5.3.16' quindi risulta un falso positivo e la classe oggetto della vulnerabilità (https://docs.spring.io/spring-framework/docs/current/reference/html/integration.html#remoting-httpinvoker) non viene utilizzata.

CVE-2014-125070
===============

file name: pom.xml

La vulnerabilità indicata viene descritta come segue: `A vulnerability has been found in yanheven console and classified as problematic. Affected by this vulnerability is the function get_zone_hosts/AvailabilityZonesTable of the file openstack_dashboard/dashboards/admin/aggregates/tables.py. The manipulation leads to cross site scripting. The attack can be launched remotely. The name of the patch is ba908ae88d5925f4f6783eb234cc4ea95017472b. It is recommended to apply a patch to fix this issue. The associated identifier of this vulnerability is VDB-217651.`

La libreria indicata non viene utilizzata in GovPay, pertanto si puo' concludere che si tratti di un falso positivo.

Librerire installer
===================

Le seguenti segnalazioni riguardano le librerie utilizzate dall'installer di GovPay, utilizzato offline per la configurazione dell'ear, e che non fanno parte degli artefatti dispiegati. Possono pertanto essere ignorate.

- CVE-2007-1036
- CVE-2012-2312
- CVE-2013-4128
- CVE-2014-3488
- CVE-2014-3599
- CVE-2015-2156
- CVE-2016-4978
- CVE-2018-2799
- CVE-2019-16869
- CVE-2019-19343
- CVE-2019-20444
- CVE-2019-20445
- CVE-2020-1945
- CVE-2020-7238
- CVE-2021-4277
- CVE-2021-20318
- CVE-2021-21290
- CVE-2021-21295
- CVE-2021-21409
- CVE-2021-37136
- CVE-2021-37137
- CVE-2021-43797
- CVE-2022-23437
- CVE-2022-24823
- CVE-2022-41881
- CVE-2022-41915 


Test di copertura funzionale
############################

Di seguito le funzionalità del prodotto ed i test che ne verificano il corretto funzionamento

Integrazione AppIO
==================

+-------------------------------------+---------------------------------------------------+
| Descrizione                         | Test                                              |
+=====================================+===================================================+
| Notifica avviso pagoPA              | test.api.appio.avviso_pagamento                   |
| Notifica ricevuta pagamento         | test.api.appio.notifica_ricevuta                  |
| Notifica ricevuta pagamento esito 9 | test.api.appio.notifica_ricevuta_pagamento_no_rpt |
+-------------------------------------+---------------------------------------------------+

API Backoffice
==============

+----------------------------------------------------------------------------+---------------------------------------------------------------------------------+
| Descrizione                                                                | Test                                                                            |
+============================================================================+=================================================================================+
| Ricerca applicazioni                                                       | test.api.backoffice.v1.applicazioni.get.applicazioni-find-byMetadatiPaginazione |
| Modifica password applicazione                                             | test.api.backoffice.v1.applicazioni.patch.applicazioni-patch-password           |
| Impostazione password applicazione                                         | test.api.backoffice.v1.applicazioni.put.applicazioni-put-password               |
| Validazione semantica dei parametri di configurazione di una applicazione  | test.api.backoffice.v1.applicazioni.put.applicazioni-put-semantica              |
| Validazione sintattica dei parametri di configurazione di una applicazione | test.api.backoffice.v1.applicazioni.put.applicazioni-put-sintassi               |
| Registrazione di una applicazione                                          | test.api.backoffice.v1.applicazioni.put.applicazioni-put                        |
| Settaggio delle impostazioni generali AppIO                                | test.api.backoffice.v1.configurazione.patch.configurazione-appIOBatch           |
| Configurazione dei messaggi di default per avvisatura AppIO                | test.api.backoffice.v1.configurazione.patch.configurazione-avvisaturaAppIO      |
| Configurazione dei messaggi di default per avvisatura via mail             | test.api.backoffice.v1.configurazione.patch.configurazione-avvisaturaMail       |
| Configurazione delle impostazioni del Giornale degli Eventi                | test.api.backoffice.v1.configurazione.patch.configurazione-giornaleEventi       |
| Configurazione delle policy di sicurezza delle api pubbliche               | test.api.backoffice.v1.configurazione.patch.configurazione-hardening            |
| Configurazione del mail server                                             | test.api.backoffice.v1.configurazione.patch.configurazione-hardening            |
| Configrazione template trasformazione CSV                                  | test.api.backoffice.v1.configurazione.patch.configurazione-tracciatoCSV         |
| Validazione sintattica dei parametri inviati alle impostazioni             | test.api.backoffice.v1.configurazione.post.configurazione-sintassi              |
| Ricerca Enti Creditori per id parziale                                     | test.api.backoffice.v1.domini.get.domini-find-byIdDominio                       |
| Ricerca Enti Creditori con metadati                                        | test.api.backoffice.v1.domini.get.domini-find-byMetadatiPaginazione             |
| Ricerca Enti Creditori senza metadati                                      | test.api.backoffice.v1.domini.get.domini-find-operatore                         |
| Lettura di un Ente Creditore                                               | test.api.backoffice.v1.domini.get.dominio-get                                   |
| Ricerca entrate con metadati                                               | test.api.backoffice.v1.domini.get.entrate-find-byMetadatiPaginazione            |
+----------------------------------------------------------------------------+---------------------------------------------------------------------------------+
