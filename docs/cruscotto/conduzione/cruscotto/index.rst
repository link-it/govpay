.. _utente_cruscotto:

Cruscotto
=========

La sezione *Cruscotto* è la pagina d'accesso della console.

Prima dell'autenticazione la pagina propone i riquadri di accesso dei metodi abilitati (vedi :ref:`govpay_console`).
Se abilitati nel file di configurazione della console ``assets/Config.js``, la pagina mostra inoltre:

*  il riquadro *GovPay*, con la versione del prodotto e i collegamenti al manuale utente, alla licenza e al progetto
   (blocco ``INFO.DOCUMENTS``, proprietà ``it.govpay.backoffice.gui.info.enabled``, attiva per default);
*  il riquadro *Notizie*, con le note di rilascio pubblicate nel repository GitHub di GovPay
   (blocco ``INFO.NEWS``, proprietà ``it.govpay.backoffice.gui.news.enabled``, disattiva per default).

Dalla versione 3.10 il Cruscotto non mostra più le caselle dei pagamenti rifiutati e sospesi, che dipendevano dalla
sezione Pagamenti, non più disponibile.
