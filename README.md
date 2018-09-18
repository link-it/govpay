# GovPay - Porta di accesso al sistema pagoPA
Una piattaforma completa per l'integrazione di Enti Pubblici, Intermediari e Partner tecnologici alle componenti centrali del progetto pagoPA.

| Versione | Documentazione&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;| Novità principali  |
|:--------:|:-----------------------------------------|:-------------------|
| [3.0-RC1](https://github.com/link-it/GovPay/releases/latest) | [Manuale di Installazione](./resources/doc/pdf/GovPay-ManualeInstallazione.pdf)<br/>[Manuale di Integrazione](./resources/doc/pdf/GovPay-ManualeIntegrazione.pdf)<br/>[Manuale Utente](./resources/doc/pdf/GovPay-ManualeUtente.pdf) | Nuove API REST di integrazione, revisione completo dei cruscotti di gestione e monitoraggio, supporto per il servizio di Avvisatura Digitale di tipo push.<br>Per maggiori informazioni, consulta le [note di rilascio](https://github.com/link-it/GovPay/releases/tag/3.0.0)
| [2.5.7](https://github.com/link-it/GovPay/releases/tag/2.5.7) | [Manuale di Installazione](https://github.com/link-it/GovPay/blob/2.5.7/resources/doc/pdf/GovPay-ManualeInstallazione.pdf)<br/>[Manuale di Integrazione](https://github.com/link-it/GovPay/blob/2.5.7/resources/doc/pdf/GovPay-ManualeIntegrazione.pdf)<br/>[Manuale Utente](https://github.com/link-it/GovPay/blob/2.5.7/resources/doc/pdf/GovPay-ManualeUtente.pdf) | Supporto WISP 2.0.<br><br>Per maggiori informazioni, consulta le [note di rilascio](https://github.com/link-it/GovPay/releases/tag/2.5.7)

## Contatti

- Segnalazioni: [GitHub Issues](https://github.com/link-it/GovPay/issues)
- Mailing list: [Utenti GovPay](http://www.gov4j.it/mailman/listinfo/utenti-govpay)

## Principali funzionalità

### Conformità alle specifiche PagoPA

* Specifiche Attuative dei Codici di Versamento, riversamento e rendicontazione (SACIV), v1.3.1 
* Specifiche Attuative del Nodo dei Pagamenti-SPC (SANP), v2.1  
* Wizard Interattivo per la Scelta dei PSP (WISP), v2.0
* Avviso di pagamento analogico nel sistema pagoPA, v1.2.3

### Configurabilità

* Supporto per tutte le modalità di adesione: diretta, intermediario tecnologico e partner tecnologico
* Possibilità di ospitare più domini, intermediari e partner sulla stessa installazione
* Servizi di integrazione per la realizzazione di un archivio di pagamenti in attesa distribuiti
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

