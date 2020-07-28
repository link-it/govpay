.. _govpay_scenari_riconciliazione:

Riconciliazione dei pagamenti con la tesoreria
==============================================

Con l'esecuzione del pagamento, il debitore acquisisce la ricevuta telematica e l'ente le somme versate che gli vengono accreditate dal PSP. Periodicamente la banca tesoriera fornisce all'ente creditore la documentazione di rendicontazione delle somme accreditate e sarà compito della ragioneria dell'ente stesso riconciliare tali elementi con i singoli pagamenti pagoPA cui fanno riferimento.

Lo scenario si basa sul seguente passaggio:

    1. **Riconciliazione Entrate**
        A partire dal *Giornale di Cassa*, fornito dalla banca tesoriera, il sistema amministrativo contabile dell'ente creditore registra le entrate "pagoPA" e ottiene la riconciliazione dei movimenti bancari con i pagamenti di origine.

La descrizione dello scenario si compone dei seguenti elementi:

    1. La :ref:`govpay_scenari_riconciliazione_realizzazione`, che descrive l'integrazione tra il gestionale di contabilità, che effettua la riconciliazione, e GovPay:

        - La riconciliazione degli incassi in banca tesoriera avviene con la chiamata a *POST /riconciliazioni/{idDominio}*

    2. La :ref:`govpay_scenari_riconciliazione_configurazione` di GovPay per supportare lo scenario descritto.

.. toctree::
   :hidden:

   realizzazione
   configurazione
