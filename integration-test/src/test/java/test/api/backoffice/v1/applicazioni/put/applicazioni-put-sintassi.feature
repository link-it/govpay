Feature: Validazione sintattica applicazioni

Background:

* call read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica_estesa.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* def loremIpsum = 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus non neque vestibulum, porta eros quis, fringilla enim. Nam sit amet justo sagittis, pretium urna et, convallis nisl. Proin fringilla consequat ex quis pharetra. Nam laoreet dignissim leo. Ut pulvinar odio et egestas placerat. Quisque tincidunt egestas orci, feugiat lobortis nisi tempor id. Donec aliquet sed massa at congue. Sed dictum, elit id molestie ornare, nibh augue facilisis ex, in molestie metus enim finibus arcu. Donec non elit dictum, dignissim dui sed, facilisis enim. Suspendisse nec cursus nisi. Ut turpis justo, fermentum vitae odio et, hendrerit sodales tortor. Aliquam varius facilisis nulla vitae hendrerit. In cursus et lacus vel consectetur.'
* def loremIpsum4050 = 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris ullamcorper feugiat elementum. Nunc ultrices mauris ut diam ultrices, quis luctus ipsum maximus. Phasellus feugiat cursus placerat. Donec sit amet porta nisl. Sed molestie auctor magna, at lacinia orci rutrum sit amet. Phasellus malesuada molestie dolor. Suspendisse mattis, diam finibus dapibus rhoncus, lacus sem placerat sem, sit amet semper arcu nibh a leo. Vivamus a mollis felis. Quisque dolor metus, euismod id nisi eu, rutrum vestibulum odio. Suspendisse sodales, ipsum at sodales viverra, libero sapien varius dolor, nec bibendum eros lacus quis lectus. Ut ornare interdum ex et ornare. Quisque ut ultrices enim. In dapibus et quam eu dictum. Quisque eu ipsum vestibulum, semper enim id, euismod tortor. Donec in rutrum est. Mauris a diam pellentesque neque condimentum consequat. Nulla eget volutpat dui. Sed ipsum lacus, maximus eu orci vitae, pharetra maximus risus. Nulla sodales ornare feugiat. Sed ullamcorper velit pulvinar velit pellentesque, sit amet auctor urna sodales. Pellentesque pulvinar, quam rhoncus semper tempus, diam mi commodo erat, vitae rhoncus metus nisl non odio. Sed convallis venenatis turpis. Donec pretium elit nec orci faucibus, in fringilla eros rutrum. Nam rhoncus enim in convallis accumsan. Pellentesque suscipit porttitor est, et pretium magna vehicula at. Maecenas mattis tellus vel augue tempus, a porttitor nulla facilisis. Cras tincidunt ultrices sapien nec auctor. Curabitur vel pulvinar metus. Fusce dignissim blandit pellentesque. Quisque dapibus ligula metus, ut egestas lectus consectetur in. Nullam sit amet elit tempus, dapibus lacus ac, tristique eros. Proin vestibulum, mauris id pretium congue, lectus nisi ornare erat, a porta diam lectus sed quam. Aliquam rhoncus iaculis metus in luctus. Aenean convallis enim diam, at scelerisque eros facilisis vitae. Ut ut sem vitae urna convallis fringilla et quis tellus. Curabitur accumsan dolor blandit rutrum dignissim. Fusce vitae elit eu nunc malesuada vestibulum id sit amet leo. Aenean at nibh ultricies, porttitor nisi eu, condimentum ipsum. Sed pulvinar turpis eu nibh convallis pretium. Suspendisse potenti. Nullam maximus quam id erat tincidunt consectetur. Morbi eros libero, ultricies pellentesque neque ac, laoreet accumsan ex. Donec pretium pharetra orci sed venenatis. Vivamus congue lobortis vestibulum. Suspendisse gravida mauris vitae velit vestibulum, at finibus elit sodales. Duis ornare maximus blandit. Cras at nulla eu tellus tristique posuere ac a augue. Nullam nisi sapien, congue in mi id, egestas vestibulum lectus. Quisque interdum felis metus, posuere auctor neque pretium sed. In hac habitasse platea dictumst. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Vestibulum eget mattis lectus, cursus pulvinar ipsum. Integer eu interdum neque. Vestibulum at metus at diam viverra tempus. Aliquam quis pretium dolor. In in diam cursus, laoreet lectus id, bibendum nisi. Integer a neque in neque ultrices porttitor ut vitae nisi. Morbi mollis tincidunt bibendum. Ut egestas enim non tellus sollicitudin lobortis. Nulla tristique lacinia neque quis feugiat. Donec condimentum massa justo, quis sodales enim luctus et. Donec congue, justo sed pretium sodales, dui purus blandit mauris, ut rutrum nisi ipsum vel augue. Donec lacus odio, pellentesque nec nisi vel, molestie maximus odio. Etiam quis turpis magna. In convallis turpis in mauris ornare, sed fringilla orci blandit. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Praesent velit sem, consequat non laoreet in, pretium in nibh. Duis est massa, facilisis et mollis et, fermentum ac orci. Fusce vel enim laoreet, pellentesque purus fermentum, rutrum neque. Donec a efficitur urna. Etiam ac mi eu metus vestibulum commodo. Etiam in neque dolor. Nunc ac ex sed nisl sagittis posuere sed elementum metus. Interdum et malesuada fames ac ante ipsum primis in faucibus. Nam fermentum urna vel nulla facilisis, a ornare est faucibus.'
* callonce read('classpath:configurazione/v1/anagrafica_unita.feature')

Scenario Outline: Modifica di una applicazione (<field>)

* def applicazione = read('classpath:test/api/backoffice/v1/applicazioni/put/msg/applicazione.json')
* set applicazione.<field> = <value>

Given url backofficeBaseurl
And path 'applicazioni', idA2A
And headers basicAutenticationHeader
And request applicazione
When method put
Then status 400
And match response == { categoria: 'RICHIESTA', codice: 'SINTASSI', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }
And match response.dettaglio contains <fieldName>

Examples:
| field | value | fieldName | 
| principal | loremIpsum4050 | 'principal' |
| password | loremIpsum4050 | 'password' |
| password | 'ABC' | 'password' |
| password | '123' | 'password' |
| password | 'abc   123' | 'password' |
| codificaAvvisi.codificaIuv | '' | 'codificaIuv' | 
| codificaAvvisi.generazioneIuvInterna | '' | 'generazioneIuvInterna' | 
| codificaAvvisi.generazioneIuvInterna | 'aaaa' | 'generazioneIuvInterna' |
| codificaAvvisi | 'aaa' | 'codificaAvvisi' |
| acl | [	{	servizio: 'AAAAAA', autorizzazioni: [ 'R', 'W', 'X' ] } ] | 'servizio' |
| acl | [	{	servizio: 'Pagamenti', autorizzazioni: [ 'K', 'W', 'X' ] } ] | 'autorizzazioni' |
| acl | [	{	servizio: 'Pagamenti', autorizzazioni: [ ] } ] | 'autorizzazioni' |
| acl | [	{	servizio: 'Pagamenti', autorizzazioni: 'R' } ] | 'autorizzazioni' |
| acl | [	{	servizio: 'Pagamenti' } ] | 'autorizzazioni' |
| servizioIntegrazione.url | 'xxxx' | 'url' |
| servizioIntegrazione | { versioneApi: 'xxxxxxx', url: 'http://prova.it', auth: { username: 'usr', password: 'pwd' } } | 'versioneApi' |
| servizioIntegrazione | { versioneApi: 'REST v1', url: 'xxxx', auth: { username: 'usr', password: 'pwd' } } | 'url' |
| servizioIntegrazione | { versioneApi: 'REST v1', url: 'http://prova.it', auth: { } } | 'tipo' |
| servizioIntegrazione | { versioneApi: 'REST v1', url: 'http://prova.it', auth: { username: null, password: 'pwd' } } | 'username' |
| servizioIntegrazione | { versioneApi: 'REST v1', url: 'http://prova.it', auth: { username: 'usr', password: null } } | 'password' |
| servizioIntegrazione | { versioneApi: 'REST v1', url: 'http://prova.it', auth: { tipo: null, ksLocation: '/tmp/keystore.jks', ksPassword: null, tsLocation: '/tmp/truststore.jks', tsPassword: 'tspwd', tsType: 'JKS', sslType: 'TLSv1.2'	} } | 'tipo' |
| servizioIntegrazione | { versioneApi: 'REST v1', url: 'http://prova.it', auth: { tipo: 'xxx', ksLocation: '/tmp/keystore.jks', ksPassword: 'kspwd', tsLocation: '/tmp/truststore.jks', tsPassword: 'tspwd'	, tsType: 'JKS', sslType: 'TLSv1.2'} } | 'tipo' |
| servizioIntegrazione | { versioneApi: 'REST v1', url: 'http://prova.it', auth: { tipo: 'Client', ksLocation: null, ksPassword: 'kspwd', tsLocation: '/tmp/truststore.jks', tsPassword: 'tspwd', tsType: 'JKS', sslType: 'TLSv1.2'	} } |  'ksLocation' |
| servizioIntegrazione | { versioneApi: 'REST v1', url: 'http://prova.it', auth: { tipo: 'Client', ksLocation: '/tmp/keystore.jks', ksPassword: null, tsLocation: '/tmp/truststore.jks', tsPassword: 'tspwd', tsType: 'JKS', sslType: 'TLSv1.2'	} } |  'ksPassword' |
| servizioIntegrazione | { versioneApi: 'REST v1', url: 'http://prova.it', auth: { tipo: 'Client', ksLocation: '/tmp/keystore.jks', ksPassword: 'kspwd', tsLocation: null, tsPassword: 'tspwd', tsType: 'JKS', sslType: 'TLSv1.2'	} } |  'tsLocation' |
| servizioIntegrazione | { versioneApi: 'REST v1', url: 'http://prova.it', auth: { tipo: 'Client', ksLocation: '/tmp/keystore.jks', ksPassword: 'kspwd', tsLocation: '/tmp/truststore.jks', tsPassword: null, tsType: 'JKS', sslType: 'TLSv1.2'	} } |  'tsPassword' |
| servizioIntegrazione | { versioneApi: 'REST v1', url: 'http://prova.it', auth: { tipo: 'Client', ksLocation: '/tmp/keystore.jks', ksPassword: 'kspwd', tsLocation: '/tmp/truststore.jks', tsPassword: 'tspwd', tsType: null, sslType: 'TLSv1.2'	} } |  'tsType' |
| servizioIntegrazione | { versioneApi: 'REST v1', url: 'http://prova.it', auth: { tipo: 'Client', ksLocation: '/tmp/keystore.jks', ksPassword: 'kspwd', tsLocation: '/tmp/truststore.jks', tsPassword: 'tspwd', tsType: 'XXX', sslType: 'TLSv1.2'	} } |  'tsType' |
| servizioIntegrazione | { versioneApi: 'REST v1', url: 'http://prova.it', auth: { tipo: 'Client', ksLocation: '/tmp/keystore.jks', ksPassword: 'kspwd', tsLocation: '/tmp/truststore.jks', tsPassword: 'tspwd', tsType: 'JKS', sslType: null	} } |  'sslType' |
| servizioIntegrazione | { versioneApi: 'REST v1', url: 'http://prova.it', auth: { tipo: 'Client', ksLocation: '/tmp/keystore.jks', ksPassword: 'kspwd', tsLocation: '/tmp/truststore.jks', tsPassword: 'tspwd', tsType: 'JKS', sslType: 'XXX'	} } |  'sslType' |
| servizioIntegrazione | { versioneApi: 'REST v1', url: 'http://prova.it', auth: { tipo: 'Client', ksLocation: '/tmp/keystore.jks', ksPassword: 'kspwd', tsLocation: '/tmp/truststore.jks', tsPassword: 'tspwd', tsType: 'JKS', sslType: 'TLSv1.2' , ksType: null, ksPKeyPasswd: 'ksPKeyPasswd'	} } |  'ksType' |
| servizioIntegrazione | { versioneApi: 'REST v1', url: 'http://prova.it', auth: { tipo: 'Client', ksLocation: '/tmp/keystore.jks', ksPassword: 'kspwd', tsLocation: '/tmp/truststore.jks', tsPassword: 'tspwd', tsType: 'JKS', sslType: 'TLSv1.2' , ksType: 'XXX', ksPKeyPasswd: 'ksPKeyPasswd'	} } |  'ksType' |
| servizioIntegrazione | { versioneApi: 'REST v1', url: 'http://prova.it', auth: { tipo: 'Client', ksLocation: '/tmp/keystore.jks', ksPassword: 'kspwd', tsLocation: '/tmp/truststore.jks', tsPassword: 'tspwd', tsType: 'JKS', sslType: 'TLSv1.2'	, ksType: 'JKS', ksPKeyPasswd:  null } } |  'ksPKeyPasswd' |
| servizioIntegrazione | { versioneApi: 'REST v1', url: 'http://prova.it', auth: { tipo: 'Server', ksLocation: '/tmp/keystore.jks', ksPassword: 'kspwd', tsLocation: null, tsPassword: 'tspwd', tsType: 'JKS', sslType: 'TLSv1.2'	} } |  'tsLocation' |
| servizioIntegrazione | { versioneApi: 'REST v1', url: 'http://prova.it', auth: { tipo: 'Server', ksLocation: '/tmp/keystore.jks', ksPassword: 'kspwd', tsLocation: '/tmp/truststore.jks', tsPassword: null, tsType: 'JKS', sslType: 'TLSv1.2'	} } |  'tsPassword' |
| servizioIntegrazione | { versioneApi: 'REST v1', url: 'http://prova.it', auth: { tipo: 'Server', ksLocation: '/tmp/keystore.jks', ksPassword: 'kspwd', tsLocation: '/tmp/truststore.jks', tsPassword: 'tspwd', tsType: null, sslType: 'TLSv1.2'	} } |  'tsType' |
| servizioIntegrazione | { versioneApi: 'REST v1', url: 'http://prova.it', auth: { tipo: 'Server', ksLocation: '/tmp/keystore.jks', ksPassword: 'kspwd', tsLocation: '/tmp/truststore.jks', tsPassword: 'tspwd', tsType: 'XXX', sslType: 'TLSv1.2'	} } |  'tsType' |
| servizioIntegrazione | { versioneApi: 'REST v1', url: 'http://prova.it', auth: { tipo: 'Server', ksLocation: '/tmp/keystore.jks', ksPassword: 'kspwd', tsLocation: '/tmp/truststore.jks', tsPassword: 'tspwd', tsType: 'JKS', sslType: null	} } |  'sslType' |
| servizioIntegrazione | { versioneApi: 'REST v1', url: 'http://prova.it', auth: { tipo: 'Server', ksLocation: '/tmp/keystore.jks', ksPassword: 'kspwd', tsLocation: '/tmp/truststore.jks', tsPassword: 'tspwd', tsType: 'JKS', sslType: 'XXX'	} } |  'sslType' |
| abilitato | null | 'abilitato' |
| abilitato | 'sss' | 'abilitato' |
| apiPagamenti | null | 'apiPagamenti' |
| apiPagamenti | 'sss' | 'apiPagamenti' |
| apiPendenze | null | 'apiPendenze' |
| apiPendenze | 'sss' | 'apiPendenze' |
| apiRagioneria | null | 'apiRagioneria' |
| apiRagioneria | 'sss' | 'apiRagioneria' |
| domini | [ { idDominio: null, unitaOperative: [ '#(idUnitaOperativa2)' ] } ] | 'idDominio' |
| domini | [ { idDominio: 'a', unitaOperative: [ '#(idUnitaOperativa2)' ] } ] | 'idDominio' |
| domini | [ { idDominio: '#(loremIpsum)', unitaOperative: [ '#(idUnitaOperativa2)' ] } ] | 'idDominio' |
| domini | [ { idDominio: '#(idDominio)', unitaOperative: 'xxx' } ] | 'unitaOperative' |
| domini | [ { idDominio: '#(idDominio)', unitaOperative: ['#(loremIpsum)'] } ] | 'unitaOperative' |

