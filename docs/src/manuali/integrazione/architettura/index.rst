.. _integrazione_architettura:

L'architettura della piattaforma di pagamento
=============================================

Nella figura seguente è sintetizzato lo scenario architetturale di
riferimento, evidenziando il ruolo di GovPay, dei sistemi dell'Ente
Creditore e dei servizi centrali del progetto pagoPA.

.. figure:: ../_images/INT01_Architettura.png
   :align: center
   :name: ArchitetturaPiattaforma

   Architettura della piattaforma di pagamento


Gli Attori principali del Progetto pagoPA
-----------------------------------------

I componenti principali del progetto pagoPA, erogati centralmente da
AgID, sono:

-  il *Nodo SPC*: componente che funge da gateway tra la rete SPC degli Enti Creditori e la rete interbancaria dei PSP;
-  il *WISP*: interfaccia grafica che guida l’utente nella scelta del PSP con cui perfezionare il pagamento.

Gli attori che interagiscono nell'ambito del progetto sono:

-  l'*Ente Creditore*, aderente a pagoPA e interessato alla
   pubblicazione sulla piattaforma delle proprie posizioni debitorie, a
   governare l'iter del loro pagamento ed alla successiva gestione
   dell'incassato.
-  i *Soggetti Debitori*: cittadini, o altri soggetti, che detengono
   posizioni pendenti o richiedono servizi soggetti a pagamento verso
   l'Ente Creditore;
-  i *PSP*: i Prestatori di Servizi di Pagamento aderenti a pagoPA.
   Ciascun PSP espone un'interfaccia web, il Portale PSP, per permettere
   al cittadino di perfezionare i pagamenti delle posizioni presenti su
   pagoPA;
-  la *Banca Tesoriera*: Istituti gestori dei conti di accredito
   dell'Ente Creditore.

I principali attori interni all'Ente Creditore
----------------------------------------------

Questo documento si concentra sull'organizzazione interna dell'Ente
Creditore, al fine di focalizzare le esigenze di integrazione dei
diversi software applicativi dell'Ente. A tal fine in `Figura
1 <#anchor-3>`__ sono descritti i principali attori interni all'Ente
Creditore:

-  *Helpdesk*: Personale dedicato ai servizi di helpdesk di primo o
   secondo livello inerenti ai pagamenti pagoPA.
-  *Portale di Pagamento*: I portali dedicati ai pagamenti nel dominio
   amministrativo dell’Ente Creditore.
-  *Gestionale Posizioni*: Il verticali competenti per le diverse
   posizioni debitorie afferenti all'Ente Creditore.
-  *Sistema Amministrativo Contabile*: applicazioni verticali che
   ricevono il giornale di cassa dalle Banche Tesoriere che attestano i
   riversamenti dei PSP debitori sui conti di accredito dell'Ente
   Creditore e responsabili della riconciliazione con i pagamenti
   operati sulla piattaforma pagoPA.
-  *GovPay*: gateway di pagamento verso la piattaforma pagoPA che
   realizza le funzioni di backend richieste dalla specifica AgID.
   Consente l’integrazione Application-To-Application dei portali di
   pagamento e dei sistemi verticali dell'Ente creditore tramite le
   seguenti API:

   -  *API Pagamento*: Servizi, ad uso dei portali di pagamento
      dell'Ente, disponibili per la realizzazione di pagamenti ad
      iniziativa Ente e di consultazione della posizione debitoria e
      dello storico dei pagamenti.
   -  *API Pendenze*: Servizi per l'interscambio dei dati relativi alle
      pendenze di pagamento, ad uso dei verticali gestori delle
      posizioni debitorie.
   -  *API Riconciliazione*: Servizi dedicati ai sistemi degli Uffici
      Amministrativi Contabili dell’Ente Creditore responsabili della
      riconciliazione dei pagamenti.
   -  *API Backoffice*: Servizi dedicati all'integrazione di cruscotti
      di gestione e monitoraggio alternativi alla GovPay Console.
   -  *API pagoPA*: le API native previste dal protocollo pagoPA,
      utilizzate internamente dal Connettore pagoPA per le interazioni
      con il Nodo SPC del progetto pagoPA.

-  *GovPay Console*: Interfaccia utente attraverso cui gli operatori
   abilitati dell’Amministrazione possono configurare la piattaforma di
   pagamento e monitorarne l'operatività sia dei processi di pagamento
   che di riconciliazione. Realizza le funzioni di Tavolo Operativo
   richiesti dalle specifiche pagoPA.

Nel seguito saranno descritte le modalità di integrazione richieste ai
sistemi applicativi dell'Ente Creditore, facendo riferimento a diverse
varianti dei seguenti casi d'uso:

1. Pagamenti ad iniziativa ente

Gli scenari d’utilizzo in cui il soggetto debitore utilizza il portale
dei pagamenti dell’ente per effettuare uno o più pagamenti

1. Pagamenti ad iniziativa PSP

Gli scenari d’utilizzo in cui l’utente effettua uno o più pagamenti
presso il PSP tramite gli avvisi di pagamento.

1. Riconciliazione dei pagamenti

Gli scenari di utilizzo di GovPay che coinvolgono i sistemi contabili
dell’ente, responsabili della riconciliazione dei pagamenti realizzati
da pagoPA con le entrate in tesoreria.

