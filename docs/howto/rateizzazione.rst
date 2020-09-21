.. _howto_rateizzazione:

Pagamenti rateali o ridotti
===========================

In questa sezione vengono indicate le linee guida per gestire il caso di 
forme di pagamento rateizzate o in forma ridotta. L'obiettivo è quello di
ottenere da GovPay le stampe degli avvisi di pagamento previste da pagoPA
per questi scenari, mirati a ridurre il numero di pagine 
da stampare e postalizzare all'utente finale. 

Pagamenti rateizzati
~~~~~~~~~~~~~~~~~~~~

In caso di rateizzazione dei pagamenti, la specifica pagoPA per l'avvisatura 
analogica prevede il pagamento in un'unica soluzione nella prima pagina
seguita dalle rate in gruppi da due o tre pagamenti per pagina. 

Per realizzare questo scenario occorre associare le pendenze che determinano
il pagamento in unica soluzione e le pendenze corrispondenti alle rate al 
medesimo documento (o ruolo/cartella/fascicolo, a seconda della terminologia a cui si è abituati). 
Per farlo è sufficiente valorizzare in ciascuna pendenza l'elemento **documento**:

{
  ...
  "documento": {
    "identificativo": "IMU-12345",
    "descrizione": "IMU 2020",
    "rata": 2
  }
  ...
}

Di seguito le indicazioni sulla valorizzazione dei campi:

  - **identificativo**: deve essere univoco per l'ente creditore a cui afferisce la pendenza
  - **descrizione**: viene utilizzata per la visualizzazione sulle console di gestione
  - **rata**: numero di rata nel caso di pendenza relativa ad una rata. Non valorizzare nel caso di pagamento in soluzione unica.

Le medesime informazioni possono essere indicate anche nel caricamento pendenze tramite 
tracciato CSV.

Una volta caricate le posizioni, è possibile acquisire la stampa degli avvisi
del documento con l'operazione **GET /documenti/{idDominio}/{numeroDocumento}/avvisi**



Pagamento in forma ridotta
~~~~~~~~~~~~~~~~~~~~~~~~~~

Alcune tipologie di pendenza prevedono il pagamento in forma ridotta, 
come nel caso delle sanzioni del codice della strada. Il template degli avvisi pagoPA
prevede l'indicazione *entro XX giorni* o *oltre XX giorni* per gestire questa casistica. 

Per realizzare questo scenario è sufficiente le varie pendenze allo stesso documento come 
nel caso dei *Pagamenti rateizzati* visti in precedenza:

{
  ...
  "documento": {
    "identificativo": "CDS-12345",
    "descrizione": "Sanzione CDS",
    "soglia": {
      "tipo": "ENTRO",
      "giorni": 30
  }
  ...
}

Una volta caricate le posizioni, è possibile acquisire la stampa degli avvisi
del documento con l'operazione **GET /documenti/{idDominio}/{numeroDocumento}/avvisi**