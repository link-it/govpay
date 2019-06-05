Feature:

Background:
 
* def isImporto  =
    """
		function(param) {
		  return param.toString().split(".")[1].length == 2;  
		}
    """
 
* def isTrue  =
    """
		function(param) {
		  return true;  
		}
    """
    
Scenario:  

* def actual = { importo: 100.01 }
* def expected = { importo: '#? isImporto(_)' }
* match actual == expected
