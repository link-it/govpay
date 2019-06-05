Feature: Validazione sintattica unita operative

Background:


* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* def unita = read('classpath:test/api/backoffice/domini/v1/put/msg/unita.json')
* def loremIpsum = 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus non neque vestibulum, porta eros quis, fringilla enim. Nam sit amet justo sagittis, pretium urna et, convallis nisl. Proin fringilla consequat ex quis pharetra. Nam laoreet dignissim leo. Ut pulvinar odio et egestas placerat. Quisque tincidunt egestas orci, feugiat lobortis nisi tempor id. Donec aliquet sed massa at congue. Sed dictum, elit id molestie ornare, nibh augue facilisis ex, in molestie metus enim finibus arcu. Donec non elit dictum, dignissim dui sed, facilisis enim. Suspendisse nec cursus nisi. Ut turpis justo, fermentum vitae odio et, hendrerit sodales tortor. Aliquam varius facilisis nulla vitae hendrerit. In cursus et lacus vel consectetur.'
* def string71 = '1GLqJdabGYFpRi4RbM8gWlnpCzVvMyeKC2qoCYkqfvTyGZ1eovAxsFqpGfVqzzXXjCfMsKi'
* def string17 = 'LS2wIWYPN0QPsgTbX'
* def string36 = 'VTnniDMiQ2ngyoDMBnfzeGUPKTbhx2U7fMO1'
* def idUnitaOperativa = '12345678900'

Scenario Outline: <field> non valida

* set <fieldRequest> = <fieldValue>

Given url backofficeBaseurl
And path 'domini', idDominio, 'unitaOperative', idUnitaOperativa
And headers basicAutenticationHeader
And request unita
When method put
Then status 400

* match response == { categoria: 'RICHIESTA', codice: 'SINTASSI', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }
* match response.dettaglio contains <fieldResponse>

Examples:
| field | fieldRequest | fieldValue | fieldResponse |
| ragioneSociale | unita.ragioneSociale | null | 'ragioneSociale' |
| ragioneSociale | unita.ragioneSociale | string71 | 'ragioneSociale' |
| indirizzo | unita.indirizzo | string71 | 'indirizzo' |
| civico | unita.civico | string17 | 'civico' |
| cap | unita.cap | string17 | 'cap' |
| localita | unita.localita | string36 | 'localita' |
| provincia | unita.provincia | string36 | 'provincia' |
| nazione | unita.nazione | '' | 'nazione' |
| nazione | unita.nazione | 'it' | 'nazione' |
| nazione | unita.nazione | 'ITA' | 'nazione' |
| email | unita.email | 'mail@errata@it' | 'email' |
| email | unita.email | 'mJ4A5V6Omqas78sWJJ9tfNGunuxQLc5oaG0WkIx7irRHcQVOTlLjurg0dnsfvzaDa6Y3BbFyPgbvP0mviHX9sS0SJ4NSDW79mr0G4rJ5yrt60zyg4IOUuOJqG7Rpb9tjl6kHv8Nbskm0cBj7g4UyzdR3dA6LVxBq34lNzoLXaU5j42hIBuOHjlfEkXZ8pztc59NPku4yAYMlbz9ToMI7ern7MfYU5hNMHl1zwhZGMB9L0D8uQT5KuU3nFpLP7nPj@lunga.it' | 'email' |
| pec | unita.pec | 'mail@errata@it' | 'pec' |
| pec | unita.pec | 'mJ4A5V6Omqas78sWJJ9tfNGunuxQLc5oaG0WkIx7irRHcQVOTlLjurg0dnsfvzaDa6Y3BbFyPgbvP0mviHX9sS0SJ4NSDW79mr0G4rJ5yrt60zyg4IOUuOJqG7Rpb9tjl6kHv8Nbskm0cBj7g4UyzdR3dA6LVxBq34lNzoLXaU5j42hIBuOHjlfEkXZ8pztc59NPku4yAYMlbz9ToMI7ern7MfYU5hNMHl1zwhZGMB9L0D8uQT5KuU3nFpLP7nPj@lunga.it' | 'pec' |
| tel | unita.tel | loremIpsum | 'tel' |
| fax | unita.fax | loremIpsum | 'fax' |
| web | unita.web | loremIpsum | 'web' |
| abilitato | unita.abilitato | 'boh' |  'abilitato' | 
| abilitato | unita.abilitato | null |  'abilitato' |
| area | unita.area | loremIpsum | 'area' |