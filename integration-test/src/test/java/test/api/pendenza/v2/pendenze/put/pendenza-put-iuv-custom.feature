Feature: Caricamento pagamento dovuto con avviso

Background: 

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('msg/pendenza-put_monovoce_riferimento.json')
* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'pendenze', versione: 'v2', autenticazione: 'basic'})

* def getYear2 =
  """
  function() {
    var SimpleDateFormat = Java.type('java.text.SimpleDateFormat');
    var sdf = new SimpleDateFormat('yy');
    var date = new java.util.Date();
    return sdf.format(date);
  } 
  """
* def currentYear2 = getYear2()

* def getYear4 =
  """
  function() {
    var SimpleDateFormat = Java.type('java.text.SimpleDateFormat');
    var sdf = new SimpleDateFormat('yyyy');
    var date = new java.util.Date();
    return sdf.format(date);
  } 
  """
* def currentYear4 = getYear4()

Scenario Outline: IUV custom per <scenariodescr>

* set dominio.iuvPrefix = <iuvprefix>

Given url backofficeBaseurl
And path 'domini', idDominio 
And headers gpAdminBasicAutenticationHeader
And request dominio
When method put
Then assert responseStatus == 200

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

* def isValidNumeroAvviso = function(x){ return new RegExp(<regex>).test(x) }

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPut
When method put
Then status 201
And match response == { idDominio: '#(idDominio)', numeroAvviso: '#? isValidNumeroAvviso(_)', UUID: '#notnull' }

Examples:
| scenariodescr | iuvprefix | regex |
| Pendenza | '%(t)' | "[0-9]{3}89[0-9]{11}" |
| Anno di due cifre | '%(y)' | "[0-9]{3}" + currentYear2 + "[0-9]{13}" |
| Anno di quattro cifre | '%(Y)' | "[0-9]{3}" + currentYear4 + "[0-9]{11}" |
| Applicazione | '%(a)' | "[0-9]{3}34[0-9]{13}" | 
| Applicazione, Anno e Entrata | '%(a)%(y)%(t)' | "[0-9]{3}34"+ currentYear2 +"89[0-9]{9}" |



