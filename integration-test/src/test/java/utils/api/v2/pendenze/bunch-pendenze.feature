Feature: Setup pagamenti

Background: 

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica_estesa.feature')

# idPendenza_Rossi_DOM1_SEGRETERIA_NONESEGUITO_idA2A
# idPendenza_Rossi_DOM1_SEGRETERIA_ESEGUITO_idA2A
# idPendenza_Rossi_DOM1_LIBERO_NONESEGUITO_idA2A
# idPendenza_Rossi_DOM1_LIBERO_ESEGUITO_idA2A
# idPendenza_Rossi_DOM1_SPONTANEO_NONESEGUITO_idA2A
# idPendenza_Rossi_DOM1_SPONTANEO_ESEGUITO_idA2A
# idPendenza_Verdi_DOM2_LIBERO_NONESEGUITO_idA2A
# idPendenza_Verdi_DOM2_LIBERO_ESEGUITO_idA2A
# idPendenza_Verdi_DOM1_SPONTANEO_NONESEGUITO_idA2A
# idPendenza_Verdi_DOM1_SPONTANEO_ESEGUITO_idA2A
# idPendenza_Rossi_DOM1_SEGRETERIA_NONESEGUITO_idA2A2
# idPendenza_Rossi_DOM1_SEGRETERIA_ESEGUITO_idA2A2
# idPendenza_Rossi_DOM1_LIBERO_NONESEGUITO_idA2A2
# idPendenza_Rossi_DOM1_LIBERO_ESEGUITO_idA2A2
# idPendenza_Rossi_DOM1_SPONTANEO_NONESEGUITO_idA2A2
# idPendenza_Rossi_DOM1_SPONTANEO_ESEGUITO_idA2A2
# idPendenza_Verdi_DOM2_LIBERO_NONESEGUITO_idA2A2
# idPendenza_Verdi_DOM2_LIBERO_ESEGUITO_idA2A2
# idPendenza_Verdi_DOM1_SPONTANEO_NONESEGUITO_idA2A2
# idPendenza_Verdi_DOM1_SPONTANEO_ESEGUITO_idA2A2

Scenario: Caricamento pendenze

* def autenticationHeader = idA2ABasicAutenticationHeader
* def idA2APendenza = idA2A

# riscaldamento
* def idDominioPendenza = idDominio
* def soggettoPagatore = { tipo: "F", identificativo: "RSSMRA30A01H501I", anagrafica: "Mario Rossi" }
* def vociPendenza = { idVocePendenza: 1, importo: 100.99, descrizione: "Diritti e segreteria", codEntrata: "#(codEntrataSegreteria)" }
* call read('classpath:utils/api/v2/pendenze/caricamento-pendenza-generico.feature')

* def dataInizio = getDateTime()
* call sleep(1000)

# idPendenza_Rossi_DOM1_SEGRETERIA_NONESEGUITO_idA2A

* def idDominioPendenza = idDominio
* def soggettoPagatore = { tipo: "F", identificativo: "RSSMRA30A01H501I", anagrafica: "Mario Rossi" }
* def vociPendenza = { idVocePendenza: 1, importo: 100.99, descrizione: "Diritti e segreteria", codEntrata: "#(codEntrataSegreteria)" }
* call read('classpath:utils/api/v2/pendenze/caricamento-pendenza-generico.feature')
* def idPendenza_Rossi_DOM1_SEGRETERIA_NONESEGUITO_idA2A = idPendenza 

# idPendenza_Rossi_DOM1_SEGRETERIA_ESEGUITO_idA2A

* def idDominioPendenza = idDominio
* def soggettoPagatore = { tipo: "F", identificativo: "RSSMRA30A01H501I", anagrafica: "Mario Rossi" }
* def vociPendenza = { idVocePendenza: 1, importo: 100.99, descrizione: "Diritti e segreteria", codEntrata: "#(codEntrataSegreteria)" }
* call read('classpath:utils/api/v2/pendenze/caricamento-pendenza-generico.feature')
* def idPendenza_Rossi_DOM1_SEGRETERIA_ESEGUITO_idA2A = idPendenza 
* def tipoRicevuta = "R01"
* call read('classpath:utils/workflow/modello3/v2/modello3-solopagamento.feature')

# idPendenza_Rossi_DOM1_LIBERO_NONESEGUITO_idA2A

* def idDominioPendenza = idDominio
* def soggettoPagatore = { tipo: "F", identificativo: "RSSMRA30A01H501I", anagrafica: "Mario Rossi" }
* def vociPendenza = { idVocePendenza: 1, importo: 100.99, descrizione: "Servizio spontaneo", ibanAccredito: "#(ibanAccredito)", tipoContabilita: "ALTRO", codiceContabilita: "CodiceContabilita" }
* call read('classpath:utils/api/v2/pendenze/caricamento-pendenza-generico.feature')
* def idPendenza_Rossi_DOM1_LIBERO_NONESEGUITO_idA2A = idPendenza 

# idPendenza_Rossi_DOM1_LIBERO_ESEGUITO_idA2A

* def idDominioPendenza = idDominio
* def soggettoPagatore = { tipo: "F", identificativo: "RSSMRA30A01H501I", anagrafica: "Mario Rossi" }
* def vociPendenza = { idVocePendenza: 1, importo: 100.99, descrizione: "Servizio spontaneo", ibanAccredito: "#(ibanAccredito)", tipoContabilita: "ALTRO", codiceContabilita: "CodiceContabilita" }
* call read('classpath:utils/api/v2/pendenze/caricamento-pendenza-generico.feature')
* def idPendenza_Rossi_DOM1_LIBERO_ESEGUITO_idA2A = idPendenza 
* def tipoRicevuta = "R01"
* call read('classpath:utils/workflow/modello3/v2/modello3-solopagamento.feature')

# idPendenza_Rossi_DOM1_SPONTANEO_NONESEGUITO_idA2A

* def idDominioPendenza = idDominio
* def soggettoPagatore = { tipo: "F", identificativo: "RSSMRA30A01H501I", anagrafica: "Mario Rossi" }
* def vociPendenza = { idVocePendenza: 1, importo: 100.99, descrizione: "Diritti e segreteria", codEntrata: "#(codSpontaneo)" }
* call read('classpath:utils/api/v2/pendenze/caricamento-pendenza-generico.feature')
* def idPendenza_Rossi_DOM1_SPONTANEO_NONESEGUITO_idA2A = idPendenza 

# idPendenza_Rossi_DOM1_SPONTANEO_ESEGUITO_idA2A

* def idDominioPendenza = idDominio
* def soggettoPagatore = { tipo: "F", identificativo: "RSSMRA30A01H501I", anagrafica: "Mario Rossi" }
* def vociPendenza = { idVocePendenza: 1, importo: 100.99, descrizione: "Diritti e segreteria", codEntrata: "#(codSpontaneo)" }
* call read('classpath:utils/api/v2/pendenze/caricamento-pendenza-generico.feature')
* def idPendenza_Rossi_DOM1_SPONTANEO_ESEGUITO_idA2A = idPendenza 
* def tipoRicevuta = "R01"
* call read('classpath:utils/workflow/modello3/v2/modello3-solopagamento.feature')

# idPendenza_Verdi_DOM2_LIBERO_NONESEGUITO_idA2A

* def idDominioPendenza = idDominio_2
* def soggettoPagatore = { tipo: "F", identificativo: "VRDGPP65B03A112N", anagrafica: "Giuseppe Verdi" }
* def vociPendenza = { idVocePendenza: 1, importo: 100.99, descrizione: "Servizio spontaneo", ibanAccredito: "#(ibanAccredito_2)", tipoContabilita: "ALTRO", codiceContabilita: "CodiceContabilita" }
* call read('classpath:utils/api/v2/pendenze/caricamento-pendenza-generico.feature')
* def idPendenza_Verdi_DOM2_LIBERO_NONESEGUITO_idA2A = idPendenza 

# idPendenza_Verdi_DOM2_LIBERO_ESEGUITO_idA2A

* def idDominioPendenza = idDominio_2
* def soggettoPagatore = { tipo: "F", identificativo: "VRDGPP65B03A112N", anagrafica: "Giuseppe Verdi" }
* def vociPendenza = { idVocePendenza: 1, importo: 100.99, descrizione: "Servizio spontaneo", ibanAccredito: "#(ibanAccredito_2)", tipoContabilita: "ALTRO", codiceContabilita: "CodiceContabilita" }
* call read('classpath:utils/api/v2/pendenze/caricamento-pendenza-generico.feature')
* def idPendenza_Verdi_DOM2_LIBERO_ESEGUITO_idA2A = idPendenza 
* def tipoRicevuta = "R01"
* call read('classpath:utils/workflow/modello3/v2/modello3-solopagamento.feature')

# idPendenza_Verdi_DOM1_SPONTANEO_NONESEGUITO_idA2A

* def idDominioPendenza = idDominio
* def soggettoPagatore = { tipo: "F", identificativo: "VRDGPP65B03A112N", anagrafica: "Giuseppe Verdi" }
* def vociPendenza = { idVocePendenza: 1, importo: 100.99, descrizione: "Diritti e segreteria", codEntrata: "#(codSpontaneo)" }
* call read('classpath:utils/api/v2/pendenze/caricamento-pendenza-generico.feature')
* def idPendenza_Verdi_DOM1_SPONTANEO_NONESEGUITO_idA2A = idPendenza 

# idPendenza_Verdi_DOM1_SPONTANEO_ESEGUITO_idA2A

* def idDominioPendenza = idDominio
* def soggettoPagatore = { tipo: "F", identificativo: "VRDGPP65B03A112N", anagrafica: "Giuseppe Verdi" }
* def vociPendenza = { idVocePendenza: 1, importo: 100.99, descrizione: "Diritti e segreteria", codEntrata: "#(codSpontaneo)" }
* call read('classpath:utils/api/v2/pendenze/caricamento-pendenza-generico.feature')
* def idPendenza_Verdi_DOM1_SPONTANEO_ESEGUITO_idA2A = idPendenza 
* def tipoRicevuta = "R01"
* call read('classpath:utils/workflow/modello3/v2/modello3-solopagamento.feature')



* def autenticationHeader = idA2A2BasicAutenticationHeader
* def idA2APendenza = idA2A2

# idPendenza_Rossi_DOM1_SEGRETERIA_NONESEGUITO_idA2A2

* def idDominioPendenza = idDominio
* def soggettoPagatore = { tipo: "F", identificativo: "RSSMRA30A01H501I", anagrafica: "Mario Rossi" }
* def vociPendenza = { idVocePendenza: 1, importo: 100.99, descrizione: "Diritti e segreteria", codEntrata: "#(codEntrataSegreteria)" }
* call read('classpath:utils/api/v2/pendenze/caricamento-pendenza-generico.feature')
* def idPendenza_Rossi_DOM1_SEGRETERIA_NONESEGUITO_idA2A2 = idPendenza 

# idPendenza_Rossi_DOM1_SEGRETERIA_ESEGUITO_idA2A2

* def idDominioPendenza = idDominio
* def soggettoPagatore = { tipo: "F", identificativo: "RSSMRA30A01H501I", anagrafica: "Mario Rossi" }
* def vociPendenza = { idVocePendenza: 1, importo: 100.99, descrizione: "Diritti e segreteria", codEntrata: "#(codEntrataSegreteria)" }
* call read('classpath:utils/api/v2/pendenze/caricamento-pendenza-generico.feature')
* def idPendenza_Rossi_DOM1_SEGRETERIA_ESEGUITO_idA2A2 = idPendenza 
* def tipoRicevuta = "R01"
* call read('classpath:utils/workflow/modello3/v2/modello3-solopagamento.feature')

# idPendenza_Rossi_DOM1_LIBERO_NONESEGUITO_idA2A2

* def idDominioPendenza = idDominio
* def soggettoPagatore = { tipo: "F", identificativo: "RSSMRA30A01H501I", anagrafica: "Mario Rossi" }
* def vociPendenza = { idVocePendenza: 1, importo: 100.99, descrizione: "Servizio spontaneo", ibanAccredito: "#(ibanAccredito)", tipoContabilita: "ALTRO", codiceContabilita: "CodiceContabilita" }
* call read('classpath:utils/api/v2/pendenze/caricamento-pendenza-generico.feature')
* def idPendenza_Rossi_DOM1_LIBERO_NONESEGUITO_idA2A2 = idPendenza 

# idPendenza_Rossi_DOM1_LIBERO_ESEGUITO_idA2A2

* def idDominioPendenza = idDominio
* def soggettoPagatore = { tipo: "F", identificativo: "RSSMRA30A01H501I", anagrafica: "Mario Rossi" }
* def vociPendenza = { idVocePendenza: 1, importo: 100.99, descrizione: "Servizio spontaneo", ibanAccredito: "#(ibanAccredito)", tipoContabilita: "ALTRO", codiceContabilita: "CodiceContabilita" }
* call read('classpath:utils/api/v2/pendenze/caricamento-pendenza-generico.feature')
* def idPendenza_Rossi_DOM1_LIBERO_ESEGUITO_idA2A2 = idPendenza 
* def tipoRicevuta = "R01"
* call read('classpath:utils/workflow/modello3/v2/modello3-solopagamento.feature')

# idPendenza_Rossi_DOM1_SPONTANEO_NONESEGUITO_idA2A2

* def idDominioPendenza = idDominio
* def soggettoPagatore = { tipo: "F", identificativo: "RSSMRA30A01H501I", anagrafica: "Mario Rossi" }
* def vociPendenza = { idVocePendenza: 1, importo: 100.99, descrizione: "Diritti e segreteria", codEntrata: "#(codSpontaneo)" }
* call read('classpath:utils/api/v2/pendenze/caricamento-pendenza-generico.feature')
* def idPendenza_Rossi_DOM1_SPONTANEO_NONESEGUITO_idA2A2 = idPendenza 

# idPendenza_Rossi_DOM1_SPONTANEO_ESEGUITO_idA2A2

* def idDominioPendenza = idDominio
* def soggettoPagatore = { tipo: "F", identificativo: "RSSMRA30A01H501I", anagrafica: "Mario Rossi" }
* def vociPendenza = { idVocePendenza: 1, importo: 100.99, descrizione: "Diritti e segreteria", codEntrata: "#(codSpontaneo)" }
* call read('classpath:utils/api/v2/pendenze/caricamento-pendenza-generico.feature')
* def idPendenza_Rossi_DOM1_SPONTANEO_ESEGUITO_idA2A2 = idPendenza 
* def tipoRicevuta = "R01"
* call read('classpath:utils/workflow/modello3/v2/modello3-solopagamento.feature')

# idPendenza_Verdi_DOM2_LIBERO_NONESEGUITO_idA2A2

* def idDominioPendenza = idDominio_2
* def soggettoPagatore = { tipo: "F", identificativo: "VRDGPP65B03A112N", anagrafica: "Giuseppe Verdi" }
* def vociPendenza = { idVocePendenza: 1, importo: 100.99, descrizione: "Servizio spontaneo", ibanAccredito: "#(ibanAccredito_2)", tipoContabilita: "ALTRO", codiceContabilita: "CodiceContabilita" }
* call read('classpath:utils/api/v2/pendenze/caricamento-pendenza-generico.feature')
* def idPendenza_Verdi_DOM2_LIBERO_NONESEGUITO_idA2A2 = idPendenza 

# idPendenza_Verdi_DOM2_LIBERO_ESEGUITO_idA2A2

* def idDominioPendenza = idDominio_2
* def soggettoPagatore = { tipo: "F", identificativo: "VRDGPP65B03A112N", anagrafica: "Giuseppe Verdi" }
* def vociPendenza = { idVocePendenza: 1, importo: 100.99, descrizione: "Servizio spontaneo", ibanAccredito: "#(ibanAccredito_2)", tipoContabilita: "ALTRO", codiceContabilita: "CodiceContabilita" }
* call read('classpath:utils/api/v2/pendenze/caricamento-pendenza-generico.feature')
* def idPendenza_Verdi_DOM2_LIBERO_ESEGUITO_idA2A2 = idPendenza 
* def tipoRicevuta = "R01"
* call read('classpath:utils/workflow/modello3/v2/modello3-solopagamento.feature')


# idPendenza_Verdi_DOM1_SPONTANEO_NONESEGUITO_idA2A2

* def idDominioPendenza = idDominio
* def soggettoPagatore = { tipo: "F", identificativo: "VRDGPP65B03A112N", anagrafica: "Giuseppe Verdi" }
* def vociPendenza = { idVocePendenza: 1, importo: 100.99, descrizione: "Diritti e segreteria", codEntrata: "#(codSpontaneo)" }
* call read('classpath:utils/api/v2/pendenze/caricamento-pendenza-generico.feature')
* def idPendenza_Verdi_DOM1_SPONTANEO_NONESEGUITO_idA2A2 = idPendenza 

# idPendenza_Verdi_DOM1_SPONTANEO_ESEGUITO_idA2A2

* def idDominioPendenza = idDominio
* def soggettoPagatore = { tipo: "F", identificativo: "VRDGPP65B03A112N", anagrafica: "Giuseppe Verdi" }
* def vociPendenza = { idVocePendenza: 1, importo: 100.99, descrizione: "Diritti e segreteria", codEntrata: "#(codSpontaneo)" }
* call read('classpath:utils/api/v2/pendenze/caricamento-pendenza-generico.feature')
* def idPendenza_Verdi_DOM1_SPONTANEO_ESEGUITO_idA2A2 = idPendenza 
* def tipoRicevuta = "R01"
* call read('classpath:utils/workflow/modello3/v2/modello3-solopagamento.feature')






* def dataFine = getDateTime()
* call sleep(1000)

# rilassamento
* def idDominioPendenza = idDominio_2
* def soggettoPagatore = { tipo: "F", identificativo: "VRDGPP65B03A112N", anagrafica: "Giuseppe Verdi" }
* def vociPendenza = { idVocePendenza: 1, importo: 100.99, descrizione: "Servizio spontaneo", ibanAccredito: "#(ibanAccredito_2)", tipoContabilita: "ALTRO", codiceContabilita: "CodiceContabilita" }
* call read('classpath:utils/api/v2/pendenze/caricamento-pendenza-generico.feature')

