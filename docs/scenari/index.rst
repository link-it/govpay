.. _govpay_scenari:

=======
Scenari
=======
Questa sezione descrive, con esempi di invocazione delle API, alcuni scenari
tipici di integrazione con GovPay.

Gli scenari presuppongono che sia già stata completata la configurazione di
base della piattaforma: intermediario e stazione
(:ref:`govpay_configurazione_intermediari`), ente creditore con i relativi
conti di accredito (:ref:`govpay_configurazione_enti`) e applicazioni
gestionali autorizzate alle API utilizzate
(:ref:`govpay_configurazione_applicazioni`).

I pagamenti ad iniziativa ente avviati dal portale dei pagamenti non passano
da GovPay: il portale si integra direttamente con il Checkout di pagoPA.

.. toctree::
   :caption: Argomenti trattati
   :maxdepth: 2

   dovuto1/index
   dovuto3/index
   riconciliazione/index
