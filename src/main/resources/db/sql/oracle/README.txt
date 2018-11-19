In caso di installazione dal zero bisogna eseguire i file sql in sequenza:

1) 'functions.sql': aggiunge la funzione per il calcolo dei millisecondi necessari per il calcolo del rank della vista 'versamenti_incassi' poiche' non e' presente una funzione equivalente in oracle;

2) 'gov_pay.sql': script di creazione delle tabelle di govpay.

