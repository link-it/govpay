Feature: Validazione semantica inserimento pendenza

Background: 

* call read('classpath:utils/common-utils.feature')
* call read('classpath:configurazione/v1/anagrafica_estesa.feature')
* def idPendenza = getCurrentTimeMillis()
* def pendenzaPutMulti = read('msg/pendenza-put_multivoce_bollo.json')
* def pendenzaPutMono = read('msg/pendenza-put_monovoce_riferimento.json')
* def pendenzaPutMonoDefinito = read('msg/pendenza-put_monovoce_definito.json')
* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'pendenze', versione: 'v2', autenticazione: 'basic'})

Scenario: Caricamento idA2A disabilitato

* set applicazione.abilitato = false

Given url backofficeBaseurl
And path 'applicazioni', idA2A 
And headers gpAdminBasicAutenticationHeader
And request applicazione
When method put
Then status 200

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPutMono
When method put
Then status 403
And match response == 
"""
{ 
	categoria: 'AUTORIZZAZIONE',
	codice: '403000',
	descrizione: 'Operazione non autorizzata',
	dettaglio: '#("Applicazione [" + idA2A + "] disabilitata.")'
}
"""

Scenario: Caricamento idA2A diverso dal chiamante

* set applicazione.principal = 'ALTRO_PRINCIPAL'

Given url backofficeBaseurl
And path 'applicazioni', 'ALTRO' 
And headers gpAdminBasicAutenticationHeader
And request applicazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

Given url pendenzeBaseurl
And path '/pendenze', 'ALTRO', idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPutMono
When method put
Then status 400
And match response == 
"""
{ 
	categoria: 'RICHIESTA',
	codice: 'APP_002',
	descrizione: 'Richiesta non valida',
	dettaglio: '#("Applicazione autenticata (" +idA2A + ") diversa dalla chiamante (ALTRO)")'
}
"""

Scenario: Caricamento idDominio inesistente

* set pendenzaPutMono.idDominio = '00000000000'

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPutMono
When method put
Then status 422
And match response == 
"""
{ 
	categoria: 'RICHIESTA',
	codice: 'DOM_000',
	descrizione: 'Richiesta non valida',
	dettaglio: 'Dominio (00000000000) inesistente'
}
"""

Scenario: Caricamento idDominio disabilitato

* set dominio.abilitato = false

Given url backofficeBaseurl
And path 'domini', idDominio 
And headers gpAdminBasicAutenticationHeader
And request dominio
When method put
Then status 200

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPutMono
When method put
Then status 422
And match response == 
"""
{ 
	categoria: 'RICHIESTA',
	codice: 'DOM_001',
	descrizione: 'Richiesta non valida',
	dettaglio: '#("Dominio (" + idDominio + ") disabilitato")'
}
"""

Scenario: Caricamento idUnitaOperativa inesistente

* set pendenzaPutMono.idUnitaOperativa = '00000000000_00'

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPutMono
When method put
Then status 422
And match response == 
"""
{ 
	categoria: 'RICHIESTA',
	codice: 'UOP_000',
	descrizione: 'Richiesta non valida',
	dettaglio: '#("Unita\' operativa (00000000000_00) del dominio (" + idDominio + ") inesistente")'
}
"""

Scenario: Caricamento idUnitaOperativa disabilitata

* set unitaOperativa.abilitato = false

Given url backofficeBaseurl
And path 'domini', idDominio, 'unitaOperative', idUnitaOperativa
And headers gpAdminBasicAutenticationHeader
And request unitaOperativa
When method put
Then status 200

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

* set pendenzaPutMono.idUnitaOperativa = idUnitaOperativa

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPutMono
When method put
Then status 422
And match response == 
"""
{ 
	categoria: 'RICHIESTA',
	codice: 'UOP_001',
	descrizione: 'Richiesta non valida',
	dettaglio: '#("Unita\' operativa (" + idUnitaOperativa + ") del dominio (" + idDominio + ") disabilitata")'
}
"""

Scenario: Caricamento entrata inesistente

* set pendenzaPutMono.voci[0].codEntrata = 'XXX'

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPutMono
When method put
Then status 422
And match response == 
"""
{ 
	categoria: 'RICHIESTA',
	codice: 'TRB_000',
	descrizione: 'Richiesta non valida',
	dettaglio: '#("Tributo (XXX) inesistente per il dominio (" + idDominio + ")")'
}
"""

Scenario: Caricamento entrata disabilitata

Given url backofficeBaseurl
And path 'domini', idDominio, 'entrate', codEntrataBollo
And headers gpAdminBasicAutenticationHeader
And request { tipoContabilita: 'ALTRO', codiceContabilita: 'MBT', abilitato: false }
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPutMulti
When method put
Then status 422
And match response == 
"""
{ 
	categoria: 'RICHIESTA',
	codice: 'TRB_001',
	descrizione: 'Richiesta non valida',
	dettaglio: '#("Tributo ("+ codEntrataBollo +") disabilitato per il dominio (" + idDominio + ")")'
}
"""

Scenario: Caricamento multivoce con numeroAvviso

* def pendenzaPut = read('msg/pendenza-put_multivoce_bollo.json')
* set pendenzaPut.numeroAvviso = '001000000000000001'

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPut
When method put
Then status 422
And match response == 
"""
{ 
	categoria: 'RICHIESTA',
	codice: 'VER_031',
	descrizione: 'Richiesta non valida',
	dettaglio: 'Non e\' possibile indicare il numero avviso per una pendenza di tipo multivoce.'
}
"""

Scenario: Caricamento multivoce con idVocePendenza non univoco

* def pendenzaPut = read('msg/pendenza-put_multivoce_bollo.json')
* set pendenzaPut.voci[1].idVocePendenza = pendenzaPut.voci[0].idVocePendenza

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPut
When method put
Then status 422
And match response == 
"""
{ 
	categoria: 'RICHIESTA',
	codice: 'VER_001',
	descrizione: 'Richiesta non valida',
	dettaglio: '#("Il versamento ("+ idPendenza +") dell\'applicazione ("+ idA2A +") presenta il codSingoloVersamentoEnte ("+pendenzaPut.voci[1].idVocePendenza+") piu\' di una volta")'
}
"""

Scenario: Caricamento pendenza con importo non congruo alle voci

* def pendenzaPut = read('msg/pendenza-put_multivoce_bollo.json')
* def pendenzaPutImportoOrig = pendenzaPut.importo
* set pendenzaPut.importo = pendenzaPut.importo + 10

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPut
When method put
Then status 422
And match response == 
"""
{ 
	categoria: 'RICHIESTA',
	codice: 'VER_002',
	descrizione: 'Richiesta non valida',
	dettaglio: '#("Il versamento (" + idPendenza + ") dell\'applicazione (" + idA2A + ") ha un importo totale (" + pendenzaPut.importo + ") diverso dalla somma dei singoli importi (" + pendenzaPutImportoOrig + ")")'
}
"""

Scenario: Caricamento conto accredito inesistente

* set pendenzaPutMono.voci = 
"""
[
	{
		idVocePendenza: '1',
		importo: 100.99,
		descrizione: 'Diritti e segreteria',
		ibanAccredito: 'IT02L9999999999123456789012',
		tipoContabilita: 'ALTRO',
		codiceContabilita: 'XXXXX'
	}
]
"""

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPutMono
When method put
Then status 422
And match response == 
"""
{ 
	categoria: 'RICHIESTA',
	codice: 'VER_020',
	descrizione: 'Richiesta non valida',
	dettaglio: '#("Iban di accredito (" + pendenzaPutMono.voci[0].ibanAccredito + ") non censito per il dominio (" + pendenzaPutMono.idDominio + ")")'
}
"""

Scenario: Caricamento conto accredito disabilitata

Given url backofficeBaseurl
And path 'domini', idDominio, 'contiAccredito', ibanAccredito
And headers gpAdminBasicAutenticationHeader
And request {postale:false,mybank:false,abilitato:false}
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPutMonoDefinito
When method put
Then status 422
And match response == 
"""
{ 
	categoria: 'RICHIESTA',
	codice: 'VER_032',
	descrizione: 'Richiesta non valida',
	dettaglio: '#("Iban di accredito (" + ibanAccredito + ") disabilitato per il dominio (" + idDominio + ")")'
}
"""

Scenario: Caricamento conto accredito di altro dominio

* set pendenzaPutMono.voci = 
"""
[
	{
		idVocePendenza: '1',
		importo: 100.99,
		descrizione: 'Diritti e segreteria',
		ibanAccredito: '#(ibanAccredito_2)',
		tipoContabilita: 'ALTRO',
		codiceContabilita: 'XXXXX'
	}
]
"""

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPutMono
When method put
Then status 422
And match response == 
"""
{ 
	categoria: 'RICHIESTA',
	codice: 'VER_020',
	descrizione: 'Richiesta non valida',
	dettaglio: '#("Iban di accredito (" + ibanAccredito_2 + ") non censito per il dominio (" + pendenzaPutMono.idDominio + ")")'
}
"""

Scenario: Caricamento con iuv non univoco

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPutMono
When method put
Then status 201


* def idPendenzaNew = getCurrentTimeMillis()
* set pendenzaPutMono.numeroAvviso = response.numeroAvviso

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenzaNew
And headers idA2ABasicAutenticationHeader
And request pendenzaPutMono
When method put
Then status 422
And match response == 
"""
{ 
	categoria: 'RICHIESTA',
	codice: 'VER_025',
	descrizione: 'Richiesta non valida',
	dettaglio: '#("Il versamento (IdA2A:" + idA2A + ", Id:" + idPendenzaNew + ") ha un numero avviso (" + pendenzaPutMono.numeroAvviso + ") gia\' utilizzato dal versamento (IdA2A:" + idA2A + ", Id:" + idPendenza + ").")'
}
"""

Scenario: Caricamento con iuv non confome agid

* set pendenzaPutMono.numeroAvviso = '000000000000000000'
* def applicationCode = '00'

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPutMono
When method put
Then status 422
And match response == 
"""
{ 
	categoria: 'RICHIESTA',
	codice: 'VER_026',
	descrizione: 'Richiesta non valida',
	dettaglio: '#("Lo IUV ("+ pendenzaPutMono.numeroAvviso +") non e\' conforme alle specifiche agid, application code ("+ applicationCode + ") non valido per la stazione ("+ idStazione +")")'
}
"""

Scenario: Caricamento conto appoggio inesistente

* set pendenzaPutMono.voci = 
"""
[
	{
		idVocePendenza: '1',
		importo: 100.99,
		descrizione: 'Diritti e segreteria',
		ibanAccredito: '#(ibanAccredito)',
		ibanAppoggio: 'IT02L9999999999123456789012',
		tipoContabilita: 'ALTRO',
		codiceContabilita: 'XXXXX'
	}
]
"""

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPutMono
When method put
Then status 422
And match response == 
"""
{ 
	categoria: 'RICHIESTA',
	codice: 'VER_033',
	descrizione: 'Richiesta non valida',
	dettaglio: '#("Iban di appoggio (" + pendenzaPutMono.voci[0].ibanAppoggio + ") non censito per il dominio (" + pendenzaPutMono.idDominio + ")")'
}
"""

