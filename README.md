# GovPay - Porta di accesso al sistema PagoPA
GovPay implementa il protocollo di colloquio con l'infrastruttura tecnologia Nodo dei Pagamenti SPC del progetto PagoPa per l'integrazione degli enti pubblici con la rete interbancaria.

## Documentazione

* [Introduzione a GovPay](./resources/doc/pdf/GovPay-PagoPA.pdf)
* [Manuale Integrazione](./resources/doc/pdf/GovPay-ManualeIntegrazioneSOAP.pdf)
* [Manuale Utente](./resources/doc/pdf/GovPay-ManualeUtente.pdf)

## Contatti

- Mailing list: [Utenti GovPay](http://www.gov4j.it/mailman/listinfo/utenti-govpay)
- Segnalazioni: [GitHub Issues](https://github.com/link-it/GovPay/issues)

## Funzionalità

Di seguito un elenco delle principali funzionalità del prodotto.
* implementazione delle Specifiche PagoPA, con supporto per pagamenti di tipo immediato, differito e ad iniziativa PSP sia monobeneficiario che multibeneficiario.
* supporto allo storno dei pagamenti.
* supporto ai pagamenti su circuito MyBank.
* supporto al pagamento della Marca da Bollo Telematica.
* aggiornato alla versione 1.7.2 delle interfacce Nodo dei Pagamenti.
* supporto specifica IUV pluri-intermediati
* integrazione con gli applicativi dell'Ente preposti alla gestione delle posizioni creditorie tramite Web API.
* integrazione semplificata del/i portali cittadini dell'Ente.
* implementazione di servizi accessori ai pagamenti.
  * servizio di generazione IUV
  * produzione codici per BarCode e QrCode
  * produzione tracciati di Iban Accredito e Tabella Controparti
  * gestione flussi rendicontazione
  * gestione rendicontazione senza rpt
  * risoluzione pagamenti incompleti
  * giornale degli eventi
  * ...
* completa integrazione con il software di Porta di Dominio OpenSPCoop.
* cruscotto Web di gestione e configurazione.

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
