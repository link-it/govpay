[![Build Status](https://jenkins.link.it/govpay/job/govpay/job/master/badge/icon?job=GovPay&style=plastic)](https://jenkins.link.it/govpay/blue/organizations/jenkins/govpay/activity?branch=master)
[![Documentation Status](https://readthedocs.org/projects/govpay/badge/?version=latest&style=plastic)](https://govpay.readthedocs.io/it/latest/?badge=latest)
[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://raw.githubusercontent.com/link-it/govpay/master/LICENSE)

# GovPay - Porta di accesso al sistema pagoPA
Una piattaforma completa per l'integrazione di Enti Pubblici, Intermediari e Partner tecnologici alle componenti centrali del progetto pagoPA.

## Documentazione

- [Read the docs](https://govpay.readthedocs.io/it/latest/) ([download](https://readthedocs.org/projects/govpay/downloads/htmlzip/latest/))

## Contatti

- Segnalazioni: [GitHub Issues](https://github.com/link-it/GovPay/issues)
- Mailing list: [Utenti GovPay](http://www.gov4j.it/mailman/listinfo/utenti-govpay)

## Principali funzionalità

### Conformità alle specifiche PagoPA

* Specifiche Attuative dei Codici di Versamento, riversamento e rendicontazione (SACIV), v1.3.1 
* Specifiche Attuative del Nodo dei Pagamenti-SPC (SANP), v2.2.5  
* Wizard Interattivo per la Scelta dei PSP (WISP), v2.0
* Avviso di pagamento analogico nel sistema pagoPA, v2.2.1

### Configurabilità

* Supporto per tutte le modalità di adesione: diretta, intermediario tecnologico e partner tecnologico
* Possibilità di ospitare più domini, intermediari e partner sulla stessa installazione
* Servizi di integrazione per la realizzazione di un archivio di pagamenti in attesa distribuiti
* Integrazione utenze e ruoli da Sistemi esterni di Identity Management

### API REST per l'integrazione applicativa

* API orientata ai portali per l'attivazione dei pagamenti
* API orientata ai verticali per la gestione delle posizioni debitorie
* API orientata agli uffici contabili per le operazioni di riconciliazione
* API di backoffice per lo sviluppo di cruscotti di gestione personalizzati

### Cruscotto di gestione e monitoraggio dei pagamenti

* Implementazione delle funzionalita di Tavolo Operativo richieste dalla specifica AgID
* Profilazione degli utenti console, per l'autorizzazione mirata su operazioni, domini e tipologie di pagamento di competenza
* Gestione archivio pagamenti in attesa (APA)
* Consultazione delle posizioni debitorie e dei pagamenti eseguiti
* Gestione della riconciliazione
* Reportistica nei formati PDF e CSV
* Registrazione e consultazione del Giornale degli Eventi

## Licenza

GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
http://www.gov4j.it/govpay

Copyright (c) 2014-2019 Link.it srl (http://www.link.it).

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program. If not, see <http://www.gnu.org/licenses/>.

