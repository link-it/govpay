.. _utente_avanzate_csv:

Formato di default del tracciato CSV di caricamento pendenze
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Il tracciato CSV standard di GovPay per il caricamento pendenze
prevede una prima linea di intestazione con i nomi delle campi
ed una linea per ciascuna pendenza da caricare, utilizzando come
separatore la virgola e come carattere di escape il doppio apice.

Il tracciato è il risultato della de-strutturazione della richiesta 
di caricamento pendenze prevista dalle API Pendenze in formato JSON, 
alla quale specifica si rimanda per un maggior formalismo nella definzione
dei vincoli sintattici e semantici.

.. csv-table:: Descrizione dei campi CSV
   :header: "Nome", "Descrizione", "Vincoli"
   :widths: 10 20 70

   "idA2A", "Identificativo del gestionale responsabile della pendenza, come censito in anagrafica", "Obbligatorio"
   "idPendenza","Identificativo della pendenza, univoco per gestionale responsabile","Obbligatorio, max 35 caratteri"
   "idDominio","Codice fiscale dell'ente creditore, come censito in anagrafica","Obbligatorio"
   "numeroAvviso","Identificativo dell'avviso di pagamento. Se non fornito, viene assegnato da GovPay.","Opzionale, sintassi AgID."
   "tipoPendenza","Tipologia della pendenza, come censito in anagrafica","Opzionale, default `LIBERO`"
   "idUnitaOperativa","Identificativo dell'unità interna all'ente creditore beneficiaria del pagamento, come censita in anagrafica","Opzionale"
   "causale","Descrizione della pendenza","Obbligatoria, max 140 caratteri"
   "annoRiferimento","Anno di riferimento della pendenza","Opzionale, numerico 4 cifre"
   "cartellaPagamento","Identificativo della cartella di pagamento a cui afferisce la pendenza","Opzionale, max 35 caratteri"
   "datiAllegati","Dati applicativi allegati dal gestionale secondo un formato proprietario","Opzionale"
   "direzione","Identificativo della direzione interna all'ente creditore","Opzionale, max 35 caratteri"
   "divisione","Identificativo della divisione interna all'ente creditore","Opzionale, max 35 caratteri"
   "importo","Importo della pendenza","Obbligatorio, deve corrispondere alla somma degli importi delle voci. Max 10 cifre compresi due decimali separati dal punto (.)"
   "dataValidita","Data di validità dei dati della pendenza, decorsa la quale la pendenza può subire variazioni","Opzionale, data nella forma yyyy-MM-dd"
   "dataScadenza","Data di scadenza della pendenza, decorsa la quale la pendenza non è più pagabile","Opzionale, data nella forma yyyy-MM-dd"
   "tassonomiaAvviso","Macro categoria della pendenza secondo la classificazione Agid","Opzionale, enumerazione: [ Cartelle esattoriali, Diritti e concessioni, Imposte e tasse, IMU, TASI e altre tasse comunali, Ingressi a mostre e musei, Multe e sanzioni amministrative, Previdenza e infortuni, Servizi erogati dal comune, Servizi erogati da altri enti, Servizi scolastici, Tassa automobilistica, Ticket e prestazioni sanitarie, Trasporti, mobilità e parcheggi ]"
   "tipoSoggettoPagatore","Tipologia del soggetto pagatore","Obbligatorio, Enumerazione: [ F, G ] per Fisica o Giuridica"
   "identificativoPagatore","Identificativo del soggetto pagatore. Codice fiscale, Partita iva o ANONIMO se non identificato","Obbligatorio, max 35 caratteri"
   "anagraficaPagatore","Anagrafica del soggetto pagatore. Nome e Cognome, Ragione sociale o ANONIMO se non identificato","Obbligatorio, max 70 caratteri"
   "indirizzoPagatore","Indirizzo di residenza del soggetto pagatore","Opzionale, max 70 caratteri"
   "civicoPagatore","Numero civico di residenza del soggetto pagatore","Opzionale, max 16 caratteri"
   "capPagatore","Codice di avviamento postale di residenza del soggetto pagatore","Opzionale, max 16 caratteri"
   "localitaPagatore","Località di residenza del soggetto pagatore","Opzionale, max 35 caratteri"
   "provinciaPagatore","Provincia di residenza del soggetto pagatore","Opzionale, max 35 caratteri"
   "nazionePagatore","Nazione di residenza del soggetto pagatore","Opzionale, 2 caratteri"
   "emailPagatore","Email del soggetto pagatore","Opzionale"
   "cellularePagatore","Numero di cellulare del soggetto pagatore","Opzionale, nella forma +39 000 1234567"
   "idVoce*","Identificativo della i-esima voce di pagamento della pendenza, univoco per pendenza.","Obbligatorio, max 35 caratteri"
   "importoVoce*","Importo della i-esima voce di pagamento della pendenza","Obbligatorio, max 10 cifre compresi due decimali separati dal punto (.)"
   "descrizioneVoce*","Descrizione della i-esima voce di pagamento della pendenza","Obbligatorio, max 140 caratteri"
   "ibanAccreditoVoce*","Identificativo del conto di accredito della i-esima voce di pagamento della pendenza, censito in anagrafica","Obbligatorio in alternativa a `tipoEntrataVoce*` o `tipoBolloVoce*`"
   "ibanAppoggioVoce*","Identificativo del conto di appoggio della i-esima voce di pagamento della pendenza, censito in anagrafica","Opzionale se valorizzato `ibanAccreditoVoce*`, altrimenti ignorato"
   "tipoContabilitaVoce*","Tipologia di codifica del capitolo di bilancio della i-esima voce di pagamento della pendenza","Obbligatorio se valorizzato `ibanAccreditoVoce*`, enumerazione: [ CAPITOLO, SPECIALE, SIOPE, ALTRO ], altrimenti ignorato"
   "codiceContabilitaVoce*","Codice del capitolo di bilancio della i-esima voce di pagamento della pendenza","Obbligatorio se valorizzato `ibanAccreditoVoce*`, altrimenti ignorato"
   "tipoEntrataVoce*","Riferimento alla tipologia di entrata della i-esima voce di pagamento della pendenza, censita in anagrafica","Obbligatorio in alternativa a `ibanAccreditoVoce*` o `tipoBolloVoce*`"
   "tipoBolloVoce*","Tipologia di bollo della i-esima voce di pagamento della pendenza","Obbligatorio in alternativa a `ibanAccreditoVoce*` o `tipoEntrataVoce*`, enumerazione: [ 01 ] dove 01 è la Marca da Bollo Telemarica"
   "hashBolloVoce*","Digest in base64 del documento informatico associato alla marca da bollo della i-esima voce di pagamento della pendenza","Obbligatorio se valorizzato `tipoBolloVoce*`, altrimenti ignorato"
   "provinciaBolloVoce*","Sigla automobilistica della provincia di residenza del soggetto pagatore della i-esima voce di pagamento della pendenza","Obbligatorio se valorizzato `tipoBolloVoce*`, altrimenti ignorato. Due caratteri maiuscoli."
   
I campi che determinano una voce di pagamento della pendenza si ripetono
sostituendo l'asterisco con la posizione della voce, ovvero:
idVoce1, importoVoce1, ...., idVoce2, importoVoce2, ... etc...

Si precisa che, per vincoli pagoPA, sono consentite un massimo di 5 voci
di pagamento per una pendenza e che sono pagabili ad iniziativa PSP solo
pendenze con una sola voce di pagamento.   