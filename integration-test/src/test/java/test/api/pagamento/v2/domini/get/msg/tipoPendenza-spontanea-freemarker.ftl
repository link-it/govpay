<#assign jsonUtilities = class["org.openspcoop2.utils.json.JSONUtils"].getInstance()>
<#assign body = jsonUtilities.getAsNode(jsonPath.read("$"))>
<#assign calendar = class["java.util.Calendar"]>
<#assign now = new("java.util.Date")>
<#assign calendarInstance = calendar.getInstance()>
<#assign xxx = calendarInstance.setTime(now.getTime())!>
<#assign xxx = calendarInstance.setTime(java.util.Calendar.MONTH, 1)!>
<#assign xxx = calendarInstance.setTime(java.util.Calendar.DATE, calendarInstance.getMaximum(java.util.Calendar.DATE))!>
<#assign dataValidita = calendarInstance?string("yyyy-MM-dd")>
{
	"idA2A": "IDA2A01",
	"idPendenza": "${request.get("idPendenza").asText()}",
	"idDominio": "${request.get("idDominio").asText()}",
	"tipoPendenza": "${request.get("tipoPendenza").asText()}",
 	"causale": "Rinnovo autorizzazione - Pratica n. ${request.get("idPendenza").asText()}",
	"soggettoPagatore": {
		"tipo": "F",
		"identificativo": "${request.get("soggettoPagatore").get("identificativo").asText()}",
		"anagrafica": "${request.get("soggettoPagatore").get("anagrafica").asText()}",
		"email": "${request.get("soggettoPagatore").get("email").asText()}"
	},
	"importo": "${request.get("importo").asDouble()?string["#.00"]}",
	"dataValidita": "${dataValidita}",
	"dataScadenza": "${dataValidita}",
	"tassonomiaAvviso": "Servizi erogati dal comune",
	"voci": [
		{
			"idVocePendenza": "1",
			"importo": "${request.get("importo").asDouble()?string["#.00"]}",
			"descrizione": "Istruttoria",
			"ibanAccredito": "IT02L1234512345123451111111",
			"tipoContabilita": "ALTRO",
			"codiceContabilita": "RINNOVO"
		}
	]
}