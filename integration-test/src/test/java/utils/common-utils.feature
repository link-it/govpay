@ignore
Feature:

Scenario:
  * def getCurrentTimeMillis =
    """
    function(){ 
	   return java.lang.System.currentTimeMillis() 
    }
    """
    
  * def getBasicAuthenticationHeader =
    """
		function(param) {
		  var temp = param.username + ':' + param.password;
		  var Base64 = Java.type('java.util.Base64');
		  var encoded = Base64.getEncoder().encodeToString(temp.bytes);
		  return {'Authorization': 'Basic ' + encoded};
		}
    """
      
  * def getGovPayApiBaseUrl =
    """
		function(param) {
			if (param.api == 'pagamento')
				return govpay_url + '/govpay/frontend/api/' + param.api + '/rs/' + param.autenticazione + '/' + param.versione;
			else
				return govpay_url + '/govpay/backend/api/' + param.api + '/rs/' + param.autenticazione + '/' + param.versione;
		}
    """
    
  * def getIuvFromNumeroAvviso =
    """
		function(numeroAvviso) {
				if(numeroAvviso.startsWith("3"))
					return numeroAvviso.substring(1);
				else
					return numeroAvviso.substring(3);
		}
    """

  * def buildNumeroAvviso =
  	"""
  	function(dominio, applicazione) {
  		var NumeroAvvisoBuilder = Java.type('utils.java.NumeroAvvisoBuilder');
  		return NumeroAvvisoBuilder.newNumeroAvviso(dominio.stazione, dominio.auxDigit, dominio.segregationCode, applicazione.codificaAvvisi.codificaIuv);
		}
		"""
		
	* def getDate =
		"""
		function(pattern) {
		  var SimpleDateFormat = Java.type('java.text.SimpleDateFormat');
		  var sdf = new SimpleDateFormat('yyyy-MM-dd');
		  var date = new java.util.Date();
		  return sdf.format(date);
		} 
		"""		
		
	* def getDateTime =
		"""
		function(pattern) {
		  var SimpleDateFormat = Java.type('java.text.SimpleDateFormat');
		  var sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
		  var date = new java.util.Date();
		  return sdf.format(date);
		} 
		"""	
	
  * def getRandomFiscalNumber =
    """
    function(baseCode){ 
    	// 65B03A112N
    	var anno = Math.floor(Math.random() * (99 - 60 + 1) ) + 60;
    	var giorno = Math.floor(Math.random() * (31 - 1 + 1) ) + 1;
    	var comune = Math.floor(Math.random() * (999 - 100 + 1) ) + 100;
    	return baseCode + anno + 'B'+ giorno +'A' + comune + 'N'; 
    }
    """
    
  * def sleep =
    """
    function(pause){ java.lang.Thread.sleep(pause) }
    """    
    
	* def isImportoConDueDecimali  =
    """
		function(param) {
		  return param.toString().split(".").length == 2 ? param.toString().split(".")[1].length <= 2 : true;  
		}
    """
    
    
	* def decodeBase64 = 
	"""
	function(encodedString){ 
		var Base64 = Java.type('java.util.Base64');
		var String = Java.type('java.lang.String');
		return new String(Base64.getDecoder().decode(encodedString));
	}
	"""
	
	* def encodeBase64 = 
	"""
	function(string){ 
		var Base64 = Java.type('java.util.Base64');
		var String = Java.type('java.lang.String');
		return Base64.getEncoder().encodeToString(new String(string).getBytes());
	}
	"""
    
	* def encodeBase64InputStream = 
	"""
	function(inputstream){ 
	  var IOUtils = Java.type('org.apache.commons.io.IOUtils');
		var Base64 = Java.type('java.util.Base64');
		return Base64.getEncoder().encodeToString(IOUtils.toByteArray(inputstream));
	}
	"""
	
  * def encodeBase64Bytes = 
	"""
	function(bytes){ 
		var Base64 = Java.type('java.util.Base64');
		return Base64.getEncoder().encodeToString(bytes);
	}
	"""
	
  * def replace =
  """
	function(text,placeholder,value) {
	  return text.replace(new RegExp(placeholder, 'g'),value)
	}
  """
  
  * def tail =
  """
	function(string,chars) {
	  var String = Java.type('java.lang.String');
	  var s = new String(string);
	  return s.substring(s.length() - chars, s.length());
	}
  """
  
  * def toUpperCase = 
	"""
	function(string){ 
		var String = Java.type('java.lang.String');
		var s = new String(string);
		return s.toUpperCase();
	}
	"""
	
	* def estraiIdFlussoDallaCausale = 
	"""
	function(string){ 
		var IncassoUtils = Java.type('utils.java.IncassoUtils');
		var String = Java.type('java.lang.String');
		var s = new String(string);
		return IncassoUtils.getRiferimentoIncassoCumulativo(s);
	}
	"""
	
		* def estraiIuvDallaCausale = 
	"""
	function(string){ 
		var IncassoUtils = Java.type('utils.java.IncassoUtils');
		var String = Java.type('java.lang.String');
		var s = new String(string);
		return IncassoUtils.getRiferimentoIncassoSingolo(s);
	}
	"""
	
