# GovPay - Porta di accesso al sistema pagoPA
GovPay implementa il protocollo di colloquio con l'infrastruttura tecnologia Nodo dei Pagamenti SPC del progetto PagoPa per l'integrazione degli enti pubblici con la rete interbancaria.

| Versione | Documentazione | Novità principali |
|:--------:|----------------|-------------------|
| [3.0-RC1](https://github.com/link-it/GovPay/releases/latest) | [Manuale di Installazione](./resources/doc/pdf/GovPay-ManualeInstallazione.pdf)<br/>[Manuale di Integrazione](./resources/doc/pdf/GovPay-ManualeIntegrazione.pdf)<br/>[Manuale Utente](./resources/doc/pdf/GovPay-ManualeUtente.pdf) | Nuove API di integrazione REST e cruscotto di monitoraggio.<br/> Supporto al avvisatura digitale push.  
| [2.5.7](https://github.com/link-it/GovPay/releases/tag/2.5.7) | [Manuale di Installazione](https://github.com/link-it/GovPay/blob/2.5.7/resources/doc/pdf/GovPay-ManualeInstallazione.pdf)<br/>[Manuale di Integrazione](https://github.com/link-it/GovPay/blob/2.5.7/resources/doc/pdf/GovPay-ManualeIntegrazione.pdf)<br/>[Manuale Utente](https://github.com/link-it/GovPay/blob/2.5.7/resources/doc/pdf/GovPay-ManualeUtente.pdf) | Supporto WISP 2.0

### GovPay 3.x 

Ultimo rilascio: [v3.0-RC1](https://github.com/link-it/GovPay/releases/latest)

Documentazione: 
* [Manuale di Intallazione](./resources/doc/pdf/GovPay-ManualeInstallazione.pdf)
* [Manuale di Integrazione](./resources/doc/pdf/GovPay-ManualeIntegrazione.pdf)
* [Manuale Utente](./resources/doc/pdf/GovPay-ManualeUtente.pdf)

Questa versione rivede le API di integrazione a valle delle esperienze maturate nei vari scenari d'uso di GovPay ed introduce il paradigma REST come protocollo di comunicazione con gli applicativi dell'Ente.
Inoltre realizza una profonda riorganizzazione dell'informazione nella console di gestione e monitoraggio per rendere immediata e semplice la diagnosi dei vari casi d'uso.

### GovPay 2.x

Ultimo rilascio: [GovPay v2.5.7](https://github.com/link-it/GovPay/releases/tag/2.5.7)

Documentazione: 
* [Manuale di Intallazione](https://github.com/link-it/GovPay/blob/2.5.7/resources/doc/pdf/GovPay-ManualeInstallazione.pdf)
* [Manuale di Integrazione](https://github.com/link-it/GovPay/blob/2.5.7/resources/doc/pdf/GovPay-ManualeIntegrazione.pdf)
* [Manuale Utente](https://github.com/link-it/GovPay/blob/2.5.7/resources/doc/pdf/GovPay-ManualeUtente.pdf)

## Contatti

- Segnalazioni: [GitHub Issues](https://github.com/link-it/GovPay/issues)
- Mailing list: [Utenti GovPay](http://www.gov4j.it/mailman/listinfo/utenti-govpay)

## Principali funzionalità

Implementazione delle ultime specifiche pagoPA:
* Specifiche Attuative dei Codici di Versamento, riversamento e rendicontazione (SACIV), v1.3.1 
* Specifiche Attuative del Nodo dei Pagamenti-SPC (SANP), v2.1  
* Wizard Interattivo per la Scelta dei PSP (WISP), v2.0
* Avviso di pagamento analogico nel sistema pagoPA, v1.2.3

### Configurabilità

* Supporto per tutte le modalità di adesione: diretta, intermediario tecnologico e partner tecnologico
* Multitenant: possibilità di ospitare più domini, intermediari e partner sulla stessa installazione
* Supporto alla generazione di IUV conformi alla specifica, in accordo a politiche configurabili da utente
* Integrazione utenze e ruoli da Sistemi esterni di Identity Management

### API per l'integrazione applicativa

* API orientata ai portali per l'attivazione dei pagamenti
* API orientata ai Gestionali per la gestione delle posizioni debitorie
* API orientata agli Uffici contabili per le operazioni di riconciliazione
* Profilazione degli applicativi interni al dominio, per l'autorizzazione mirata su operazioni, domini e tributi di competenza

### Cruscotto di gestione e monitoraggio dei pagamenti

* Implementazione delle funzionalita di Tavolo Operativo richieste dalla specifica AgID
* Profilazione degli utenti console, per l'autorizzazione mirata su operazioni, domini e tributi di competenza
* Gestione archivio pagamenti in attesa (APA)
* Consultazione delle pendenze e dei pagamenti eseguiti
* Gestione della riconciliazione
* Reportistica nei formati PDF e CSV
* Registrazione e consultazione del Giornale degli Eventi

## Licenza

GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
http://www.gov4j.it/govpay

Copyright (c) 2014-2018 Link.it srl (http://www.link.it).

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

