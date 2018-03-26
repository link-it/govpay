# GovPay - Porta di accesso al sistema PagoPA
GovPay implementa il protocollo di colloquio con l'infrastruttura tecnologia Nodo dei Pagamenti SPC del progetto PagoPa per l'integrazione degli enti pubblici con la rete interbancaria.

## Documentazione

* [Introduzione a GovPay](./resources/doc/pdf/GovPay-PagoPA.pdf)
* [Manuale Integrazione](./resources/doc/pdf/GovPay-ManualeIntegrazioneSOAP.pdf)
* [Manuale Utente](./resources/doc/pdf/GovPay-ManualeUtente.pdf)

## Contatti

- Mailing list: [Utenti GovPay](http://www.gov4j.it/mailman/listinfo/utenti-govpay)
- Segnalazioni: [GitHub Issues](https://github.com/link-it/GovPay/issues)

## Conformità con la specifica PagoPA

* Specifiche Attuative dei Codici di Versamento, riversamento e rendicontazione (SACIV), v1.3 
* Specifiche Attuative del Nodo dei Pagamenti-SPC (SANP), v1.7.1  
* Wizard Interattivo per la Scelta dei PSP (WISP), v2.0
* Avviso di pagamento analogico nel sistema pagoPA, v1.2.3
* Transazioni MyBank attraverso il Nodo dei Pagamenti-SPC, v1.2.1

## Configurabilità

* Supporto per tutte le modalità di adesione: diretta, intermediario tecnologico e partner tecnologico
* Multitenant: possibilità di ospitare più domini, intermediari e partner sulla stessa installazione
* Supporto alla generazione di IUV conformi alla specifica, in accordo a politiche configurabili da utente
* Integrazione utenze e ruoli da Sistemi esterni di Identity Management

## API per l'integrazione applicativa

* API orientata ai portali per l'attivazione dei pagamenti
* API orientata ai Gestionali per la gestione delle posizioni debitorie
* API orientata agli Uffici contabili per le operazioni di riconciliazione
* Profilazione degli applicativi interni al dominio, per l'autorizzazione mirata su operazioni, domini e tributi di competenza

## Cruscotto di gestione e monitoraggio dei pagamenti

* Implementazione delle funzionalita di Tavolo Operativo richieste dalla specifica AgID
* Profilazione degli utenti console, per l'autorizzazione mirata su operazioni, domini e tributi di competenza
* Gestione archivio pagamenti in attesa (APA)
* Consultazione dei pagamenti effettuati
* Consultazione dati di riconciliazione
* Reportistica nei formati PDF e CSV
* Giornale degli Eventi

## Licenza

GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
http://www.gov4j.it/govpay

Copyright (c) 2014-2017 Link.it srl (http://www.link.it).

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
