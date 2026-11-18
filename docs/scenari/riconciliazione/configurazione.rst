.. _govpay_scenari_riconciliazione_configurazione:

Configurazione
--------------


Oltre alla configurazione di base di intermediario e stazione (:ref:`govpay_configurazione_intermediari`) e dell'ente creditore con i relativi conti di accredito (:ref:`govpay_configurazione_enti`), procedere con le seguenti configurazioni per predisporre l'ambiente necessario alla realizzazione dello scenario descritto:

    1. **Autorizzazione dell'applicazione di contabilità**
        Affinché vengano accettate le richieste inviate dall'applicativo di contabilità dell'ente creditore, è necessario che:

        - L'applicazione registrata su GovPay sia associata al dominio indicato nelle operazioni di riconciliazione

        - L'applicazione sia autorizzata all'invocazione delle *API Ragioneria*

        Per maggiori dettagli sulla configurazione delle applicazioni fare riferimento a :ref:`govpay_configurazione_applicazioni`.


