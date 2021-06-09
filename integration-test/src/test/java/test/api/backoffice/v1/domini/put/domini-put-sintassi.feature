Feature: Validazione sintattica domini

Background:

* call read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* def dominio = read('classpath:test/api/backoffice/v1/domini/put/msg/dominio.json')
* def loremIpsum = 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus non neque vestibulum, porta eros quis, fringilla enim. Nam sit amet justo sagittis, pretium urna et, convallis nisl. Proin fringilla consequat ex quis pharetra. Nam laoreet dignissim leo. Ut pulvinar odio et egestas placerat. Quisque tincidunt egestas orci, feugiat lobortis nisi tempor id. Donec aliquet sed massa at congue. Sed dictum, elit id molestie ornare, nibh augue facilisis ex, in molestie metus enim finibus arcu. Donec non elit dictum, dignissim dui sed, facilisis enim. Suspendisse nec cursus nisi. Ut turpis justo, fermentum vitae odio et, hendrerit sodales tortor. Aliquam varius facilisis nulla vitae hendrerit. In cursus et lacus vel consectetur.'
* def string71 = '1GLqJdabGYFpRi4RbM8gWlnpCzVvMyeKC2qoCYkqfvTyGZ1eovAxsFqpGfVqzzXXjCfMsKi'
* def string17 = 'LS2wIWYPN0QPsgTbX'
* def string36 = 'VTnniDMiQ2ngyoDMBnfzeGUPKTbhx2U7fMO1'

Scenario Outline: <field> non valida

* set <fieldRequest> = <fieldValue>

Given url backofficeBaseurl
And path 'domini', idDominio
And headers basicAutenticationHeader
And request dominio
When method put
Then status 400

* match response == { categoria: 'RICHIESTA', codice: 'SINTASSI', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }
* match response.dettaglio contains <fieldResponse>

Examples:
| field | fieldRequest | fieldValue | fieldResponse |
| ragioneSociale | dominio.ragioneSociale | null | 'ragioneSociale' |
| ragioneSociale | dominio.ragioneSociale | string71 | 'ragioneSociale' |
| indirizzo | dominio.indirizzo | string71 | 'indirizzo' |
| civico | dominio.civico | string17 | 'civico' |
| cap | dominio.cap | string17 | 'cap' |
| localita | dominio.localita | string36 | 'localita' |
| provincia | dominio.provincia | string36 | 'provincia' |
| nazione | dominio.nazione | '' | 'nazione' |
| nazione | dominio.nazione | 'it' | 'nazione' |
| nazione | dominio.nazione | 'ITA' | 'nazione' |
| email | dominio.email | 'mail@errata@it' | 'email' |
| email | dominio.email | 'mJ4A5V6Omqas78sWJJ9tfNGunuxQLc5oaG0WkIx7irRHcQVOTlLjurg0dnsfvzaDa6Y3BbFyPgbvP0mviHX9sS0SJ4NSDW79mr0G4rJ5yrt60zyg4IOUuOJqG7Rpb9tjl6kHv8Nbskm0cBj7g4UyzdR3dA6LVxBq34lNzoLXaU5j42hIBuOHjlfEkXZ8pztc59NPku4yAYMlbz9ToMI7ern7MfYU5hNMHl1zwhZGMB9L0D8uQT5KuU3nFpLP7nPj@lunga.it' | 'email' |
| pec | dominio.pec | 'mail@errata@it' | 'pec' |
| pec | dominio.pec | 'mJ4A5V6Omqas78sWJJ9tfNGunuxQLc5oaG0WkIx7irRHcQVOTlLjurg0dnsfvzaDa6Y3BbFyPgbvP0mviHX9sS0SJ4NSDW79mr0G4rJ5yrt60zyg4IOUuOJqG7Rpb9tjl6kHv8Nbskm0cBj7g4UyzdR3dA6LVxBq34lNzoLXaU5j42hIBuOHjlfEkXZ8pztc59NPku4yAYMlbz9ToMI7ern7MfYU5hNMHl1zwhZGMB9L0D8uQT5KuU3nFpLP7nPj@lunga.it' | 'pec' |
| tel | dominio.tel | loremIpsum | 'tel' |
| fax | dominio.fax | loremIpsum | 'fax' |
| web | dominio.web | loremIpsum | 'web' |
| gln | dominio.gln | '00000000000000' | 'gln' |
| gln | dominio.gln | '000000000000A' | 'gln' |
| gln | dominio.gln | '' | 'gln' |
| cbill | dominio.cbill | 'abc456' | 'cbill' |
| cbill | dominio.cbill | 'abc' | 'cbill' |
| iuvPrefix | dominio.iuvPrefix | loremIpsum | 'iuvPrefix' |
| iuvPrefix | dominio.iuvPrefix | '%(x)' | '%(x)' |
| stazione | dominio.stazione | null | 'stazione' |
| auxDigit | dominio.auxDigit | 'a' | 'auxDigit' |
| auxDigit | dominio.auxDigit | '9' | 'auxDigit' |
| segregationCode | dominio.segregationCode | 'aa' | 'segregationCode' |
| segregationCode | dominio.segregationCode | '000' | 'segregationCode' |
| abilitato | dominio.abilitato | 'boh' |  'abilitato' |
| abilitato | dominio.abilitato | null |  'abilitato' |
| autStampaPosteItaliane | dominio.autStampaPosteItaliane | loremIpsum | 'autStampaPosteItaliane' |
| area | dominio.area | loremIpsum | 'area' |
| intermediato | dominio.intermediato | 'boh' |  'intermediato' |
| intermediato | dominio.intermediato | null |  'intermediato' |

