.. _howto_bollo:

Avvisi pagoPA bilingue
==========================

La piattaforma GovPay supporta la possibilità di stampare gli Avvisi pagoPA in formato bilingue fornendo l'indicazione
della lingua secondaria desiderata come parametro della pendenza. 

Lingua secondaria nelle richieste json
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

E' possibile indicare la lingua secondaria come proprietà della pendenza:

.. code-block:: json
    :caption: Esempio di pagamento Imposta di bollo
    
    {
      ...
      "proprieta": {
	    "linguaSecondaria": "de",
	    "linguaSecondariaCausale": "Zahlungsgegenstand"
      }
    }  

Lingua secondaria nel tracciato CSV
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

E' possibile indicare la lingua secondaria nel tracciato CSV valorizzando opportunamente il campo **linguaSecondaria**. 
Consultare la sezione specifica per maggiori informazioni

