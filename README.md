[![Build Status](https://jenkins.link.it/govpay/job/govpay/job/master/badge/icon?job=GovPay)](https://jenkins.link.it/govpay/blue/organizations/jenkins/govpay/activity?branch=master)
[![Documentation Status](https://readthedocs.org/projects/govpay/badge/?version=master)](https://govpay.readthedocs.io/it/latest/?badge=master)
[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://raw.githubusercontent.com/link-it/govpay/master/LICENSE)

# GovPay - Porta di accesso al sistema pagoPA
Una piattaforma completa per l'integrazione di Enti Pubblici, Intermediari e Partner tecnologici alle componenti centrali del progetto pagoPA.

## Documentazione

- [Read the docs](https://govpay.readthedocs.io/it/latest/) ([download](https://readthedocs.org/projects/govpay/downloads/htmlzip/latest/))

## Contatti

- Segnalazioni: [GitHub Issues](https://github.com/link-it/GovPay/issues)
- Mailing list: [Utenti GovPay](http://www.gov4j.it/mailman/listinfo/utenti-govpay)

## Principali funzionalità

Di seguito le funzionalita fornite dal prodotto ed individuate dal [quadro sinottico dei servizi](https://www.pagopa.gov.it/it/pubbliche-amministrazioni/quadro-sinottico/) al
quale si rimanda per una descrizione piu' estesa.

### Servizi minimi

* Gestione Archivio Pagamenti in Attesa: sistema funzionale che permette all'EC di creare, visualizzare, modificare le proprie posizioni debitorie tramite scambio dati M2M o manualmente
* Integrazione con e@bollo: integrazione del servizi di @e.bollo dell'Agenzia delle Entrate, per l'acquisto della Marca da Bollo Telematica
* Modelli di pagamento: capacità di innescare pagamenti con ogni modello previsto da linee guida pagoPA e allegati

### Servizi base

* Profilazione personale EC con diversi ruoli e permessi: possibilità di creare e gestire utenti differenti che abbiano visione e capacità di modifica di diverse tab del back office o diversi servizi
* Rateizzazione: capacità di gestire i pagamenti rateali
* Attualizzazione del debito: funzionalità che permette l'attualizzazione in tempo reale dell'importo del debito, nel momento in cui il cittadino sta effettuando il pagamento, in modo che l'importo pagato sia aggiornato di eventuali variazioni
* Disponibilità della ricevuta telematica: servizio per l'acquisizione della stampa delle ricevute di pagamento di un cittadino
* Gestione Carrello multi beneficiario multi pagatore: possibilità di effettuare molteplici pagamenti in unica soluzione, anche a beneficio di diversi EC e/o con diversi soggetti pagatori
* Gestione del giornale degli eventi: gestione delle informazioni previste nella struttura "Giornale degli eventi" di cui alle SANP

### Servizi distintivi

* Servizi di quadratura del flusso di pagamenti: verifica della coerenza dei dati di rendicontazione con quelli di pagamento
* Integrazione con piattaforma App IO: integrazione del back office con l'app IO per la notifica di avvisi, ricevute e promemoria di scadenza
* Riconciliazione: servizi per consentire la riconciliazione del pagamento e la produzione della reversale di incasso

### Servizi a valore aggiunto

* Messa a disposizione dei documenti scambiati con pagoPA: servizi REST per l'acquisizione dei tracciati XML da inviare in conservazione digitale (RT,RPT, flussi di rendicontazione)
* Scarico flussi di rendicontazione: servizi REST per l'acquisizione dei flussi di rendicontazione pubblicati dai diversi PSP
* Invio esito pagamento: servizio di notifica dei pagamenti realizzati
* Possibilità invio PEC: possibilità di collegare il back office al server di posta elettronica della PA così da poter inviare gli avvisi di pagamento via PEC della PA
* Avvisatura massiva: possibilità di creare un unico PDF con più avvisi di pagamento, come nel caso del pagamento rateale
* Avvisatura singola: possibilità di stampare o inviare via email un singolo avviso di pagamento generato da back office
* Codifica dati per analisi pagamenti: possibilità di codificare ogni tipologia di tributo / incasso al fine di analizzare le tipologie di entrate e la relativa diffusione
* Fascicolo del cittadino: possibilità per il cittadino, dopo essersi autenticato, di visionare tutte le posizioni debitorie a proprio carico
* Interoperabilità con contabilità: servizi REST per l'integrazione dei software di contabilità presente nell'Ente per il caricamento/interrogazione delle posizioni e delle relative imputazioni contabili se presenti
* Interoperabilità con gestionali: servizi REST per l'integrazione dei software gestionali presenti nell'Ente Creditore	
* Integrabilità con altri front-end: la piattaforma di pagamento può essere configurata per utilizzare front-end non nativi

### Servizi a valore aggiunto non individuati dal quadro sinottico

* Alimentazione APA via CSV: possibilità di alimentare l'archivio dei pagamenti in attesa tramite tracciati CSV dalla struttura configurabile
* Servizio di notifica email: oltre alla possibilità di notificare gli avvisi di pagamento via email, la piattaforma consente di configurare la notifica delle ricevute di pagamento e dei promemoria di scadenza
* Pagamenti spontanei: supporto alla gestione di pagamenti spontanei
* Integrazione nativa ad applicativi terzi, come MyPIVOT, SECIM ed altri

## Licenza

GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
http://www.gov4j.it/govpay

Copyright (c) 2014-2020 Link.it srl (http://www.link.it).

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

