.. _howto_convenzione:

Convenzioni con PSP
===================

La piattaforma pagoPA offre agli EC la possibilità di stipulare convenzioni specifiche con uno 
o più PSP (`SANP cap 5<https://docs.italia.it/italia/pagopa/pagopa-specifichepagamenti-docs/it/master/_docs/sezione3-specifiche-tecniche/3_01_06_convenzioni.html>`)al fine di poter offrire strumenti di pagamento ad un costo di commissioni agevolato.
Un esempio di utilizzo del codice convenzione è quello previsto dalla monografia "Il pagamento 
presso POS fisici nel sistema pagoPA" v.2.0.1 per i "Pagamenti in convenzione".

In GovPay, per poter usufruire di una convenzione in essere tra EC e PSP, è sufficiente valorizzare 
nella richiesta di pagamento il parametro della query string **codiceConvenzione** con il valore 
comunicato dal PSP.