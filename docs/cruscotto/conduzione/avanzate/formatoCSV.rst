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
   :header: "#", "Nome", "Descrizione", "Vincoli"
   :widths: 3 10 20 67

   "1","idA2A", "Identificativo del gestionale responsabile della pendenza, come censito in anagrafica", "Obbligatorio"
   "2","idPendenza","Identificativo della pendenza, univoco per gestionale responsabile","Obbligatorio, max 35 caratteri"
   "3","idDominio","Codice fiscale dell'ente creditore, come censito in anagrafica","Obbligatorio"
   "4","numeroAvviso","Identificativo dell'avviso di pagamento. Se non fornito, viene assegnato da GovPay.","Opzionale, sintassi AgID."
   "5","tipoPendenza","Tipologia della pendenza, come censito in anagrafica","Opzionale, default `LIBERO`"
   "6","idUnitaOperativa","Identificativo dell'unità interna all'ente creditore beneficiaria del pagamento, come censita in anagrafica","Opzionale"
   "7","causale","Descrizione della pendenza","Obbligatoria, max 140 caratteri"
   "8","annoRiferimento","Anno di riferimento della pendenza","Opzionale, numerico 4 cifre"
   "9","cartellaPagamento","Identificativo della cartella di pagamento a cui afferisce la pendenza","Opzionale, max 35 caratteri"
   "10","datiAllegati","Dati applicativi allegati dal gestionale secondo un formato proprietario","Opzionale"
   "11","direzione","Identificativo della direzione interna all'ente creditore","Opzionale, max 35 caratteri"
   "12","divisione","Identificativo della divisione interna all'ente creditore","Opzionale, max 35 caratteri"
   "13","importo","Importo della pendenza","Obbligatorio, deve corrispondere alla somma degli importi delle voci. Max 10 cifre compresi due decimali separati dal punto (.)"
   "14","dataValidita","Data di validità dei dati della pendenza, decorsa la quale la pendenza può subire variazioni","Opzionale, data nella forma yyyy-MM-dd"
   "15","dataScadenza","Data di scadenza della pendenza, decorsa la quale la pendenza non è più pagabile","Opzionale, data nella forma yyyy-MM-dd"
   "16","tassonomiaAvviso","Macro categoria della pendenza secondo la classificazione Agid","Opzionale, enumerazione: [ Cartelle esattoriali, Diritti e concessioni, Imposte e tasse, IMU, TASI e altre tasse comunali, Ingressi a mostre e musei, Multe e sanzioni amministrative, Previdenza e infortuni, Servizi erogati dal comune, Servizi erogati da altri enti, Servizi scolastici, Tassa automobilistica, Ticket e prestazioni sanitarie, Trasporti, mobilità e parcheggi ]"
   "17","tipoSoggettoPagatore","Tipologia del soggetto pagatore","Obbligatorio, Enumerazione: [ F, G ] per Fisica o Giuridica"
   "18","identificativoPagatore","Identificativo del soggetto pagatore. Codice fiscale, Partita iva o ANONIMO se non identificato","Obbligatorio, max 35 caratteri"
   "19","anagraficaPagatore","Anagrafica del soggetto pagatore. Nome e Cognome, Ragione sociale o ANONIMO se non identificato","Obbligatorio, max 70 caratteri"
   "20","indirizzoPagatore","Indirizzo di residenza del soggetto pagatore","Opzionale, max 70 caratteri"
   "21","civicoPagatore","Numero civico di residenza del soggetto pagatore","Opzionale, max 16 caratteri"
   "22","capPagatore","Codice di avviamento postale di residenza del soggetto pagatore","Opzionale, max 16 caratteri"
   "23","localitaPagatore","Località di residenza del soggetto pagatore","Opzionale, max 35 caratteri"
   "24","provinciaPagatore","Provincia di residenza del soggetto pagatore","Opzionale, max 35 caratteri"
   "25","nazionePagatore","Nazione di residenza del soggetto pagatore","Opzionale, 2 caratteri"
   "26","emailPagatore","Email del soggetto pagatore","Opzionale"
   "27","cellularePagatore","Numero di cellulare del soggetto pagatore","Opzionale, nella forma +39 000 1234567"
   "--","idVoce*","Identificativo della i-esima voce di pagamento della pendenza, univoco per pendenza.","Obbligatorio, max 35 caratteri"
   "--","importoVoce*","Importo della i-esima voce di pagamento della pendenza","Obbligatorio, max 10 cifre compresi due decimali separati dal punto (.)"
   "--","descrizioneVoce*","Descrizione della i-esima voce di pagamento della pendenza","Obbligatorio, max 140 caratteri"
   "--","ibanAccreditoVoce*","Identificativo del conto di accredito della i-esima voce di pagamento della pendenza, censito in anagrafica","Obbligatorio in alternativa a `tipoEntrataVoce*` o `tipoBolloVoce*`"
   "--","ibanAppoggioVoce*","Identificativo del conto di appoggio della i-esima voce di pagamento della pendenza, censito in anagrafica","Opzionale se valorizzato `ibanAccreditoVoce*`, altrimenti ignorato"
   "--","tipoContabilitaVoce*","Tipologia di codifica del capitolo di bilancio della i-esima voce di pagamento della pendenza","Obbligatorio se valorizzato `ibanAccreditoVoce*`, enumerazione: [ CAPITOLO, SPECIALE, SIOPE, ALTRO ], altrimenti ignorato"
   "--","codiceContabilitaVoce*","Codice del capitolo di bilancio della i-esima voce di pagamento della pendenza","Obbligatorio se valorizzato `ibanAccreditoVoce*`, altrimenti ignorato"
   "--","tipoEntrataVoce*","Riferimento alla tipologia di entrata della i-esima voce di pagamento della pendenza, censita in anagrafica","Obbligatorio in alternativa a `ibanAccreditoVoce*` o `tipoBolloVoce*`"
   "--","tipoBolloVoce*","Tipologia di bollo della i-esima voce di pagamento della pendenza","Obbligatorio in alternativa a `ibanAccreditoVoce*` o `tipoEntrataVoce*`, enumerazione: [ 01 ] dove 01 è la Marca da Bollo Telemarica"
   "--","hashBolloVoce*","Digest in base64 del documento informatico associato alla marca da bollo della i-esima voce di pagamento della pendenza","Obbligatorio se valorizzato `tipoBolloVoce*`, altrimenti ignorato"
   "--","provinciaBolloVoce*","Sigla automobilistica della provincia di residenza del soggetto pagatore della i-esima voce di pagamento della pendenza","Obbligatorio se valorizzato `tipoBolloVoce*`, altrimenti ignorato. Due caratteri maiuscoli."
   "82","dataAvvisatura","Data di spedizione dell'avvisatura, se prevista dalla configurazione.","Opzionale, se non impostata si intende immediata. Se valorizzato con `MAI` l'avvisatura viene inibilita"
   "83","idDocumento","Identificativo del documento a cui afferisce la pendenza, se ne esiste uno.","Opzionale, da usare in caso di rateizzazioni."
   "84","descrizioneDocumento","Titolo del documento. Verra' utilizzato per la stampa dell'avviso pagoPA.","Opzionale, se non valorizzato sarà usata la causale della pendenza."
   "85","numeroRata","Numero di rata in caso di pagamento rateale. In caso di pagamenti con soglia temporale, usare la sintassi `ENTROxxx` o `OLTRExxx` dove `xxx` è il numero di giorni previsto.","Opzionale, non valorizzare per il pagamento in soluzione unica."
   
I campi che determinano una voce di pagamento della pendenza si ripetono
sostituendo l'asterisco con la posizione della voce, ovvero:
idVoce1, importoVoce1, ...., idVoce2, importoVoce2, ... etc...

Si suggerisce di valorizzare il parametro `dataAvvisatura` in modo tale
da avere l'opportunità di intervenire prima dell'avvisatura al cittadino 
in caso di caricamenti indesiderati

Si precisa che, per vincoli pagoPA, sono consentite un massimo di 5 voci
di pagamento per una pendenza e che sono pagabili ad iniziativa PSP solo
pendenze con una sola voce di pagamento.   
