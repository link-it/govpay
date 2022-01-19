<#assign jsonUtilities = class["org.openspcoop2.utils.json.JSONUtils"].getInstance()>
<#assign request = jsonUtilities.getAsNode(jsonPath.read("$"))>
<#assign calendar = class["java.util.Calendar"]>
<#assign now = new("java.util.Date")>
<#assign calendarInstance = calendar.getInstance()>
<#assign xxx = calendarInstance.setTime(now)!>
<#assign yyy = calendarInstance.set(calendar.MONTH, 1)!>
<#assign zzz = calendarInstance.set(calendar.DATE, calendarInstance.getMaximum(calendar.DATE))!>
<#assign dataValidita = calendarInstance.getTime()?string("yyyy-MM-dd")>
<#setting locale="en_US">
{
	"idA2A": "IDA2A01",
	"idPendenza": "${request.get("idPendenza").asText()}",
	"idDominio": "${pathParams["idDominio"]}",
	<#if queryParams??><#if queryParams["idUnitaOperativa"]??>"idUnitaOperativa": "${queryParams["idUnitaOperativa"]}",</#if></#if>
	"idTipoPendenza": "${pathParams["idTipoPendenza"]}",
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