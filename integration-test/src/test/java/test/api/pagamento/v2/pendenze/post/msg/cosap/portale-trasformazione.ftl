<#assign jsonUtilities = class["org.openspcoop2.utils.json.JSONUtils"].getInstance()>
<#assign request = jsonUtilities.getAsNode(jsonPath.read("$"))>
<#assign calendar = class["java.util.Calendar"]>
<#assign now = new("java.util.Date")>
<#assign calendarInstance = calendar.getInstance()>
<#assign xxx = calendarInstance.setTime(now)!>
<#assign yyy = calendarInstance.add(calendar.MONTH, 1)!>
<#assign zzz = calendarInstance.set(calendar.DATE, calendarInstance.getActualMaximum(calendar.DAY_OF_MONTH))!>
<#assign dataValidita = calendarInstance.getTime()?string("yyyy-MM-dd")>
<#assign dataOraGiuliana = calendarInstance.getTime()?string("yyyyDDDHHmmssSSS")>
<#assign importo = request.get("importo").asText()>
<#setting locale="en_US">
{
	"idA2A": "IDA2A01",
	"idPendenza": "${request.get("idPendenza").asText()}-${dataOraGiuliana}",
	"idDominio": "${pathParams["idDominio"]}",
	"idTipoPendenza": "${pathParams["idTipoPendenza"]}",
 	"causale": "COSAP - Autorizzazione n.${request.get("idPendenza").asText()}",
	"soggettoPagatore": {
		"tipo": <#if request.get("soggettoPagatore").get("identificativo").asText()?length == 11>"G"<#else>"F"</#if>,
		"identificativo": "${request.get("soggettoPagatore").get("identificativo").asText()}",
		"anagrafica": "${request.get("soggettoPagatore").get("anagrafica").asText()}"
	},
   	"importo": "${importo}",
	"dataValidita": "${dataValidita}",
	"dataScadenza": "${dataValidita}",
	"tassonomiaAvviso": "Servizi erogati dal comune",
	"voci": [
		{
			"idVocePendenza": "1",
			"importo": "${importo}",
			"descrizione": "${request.get("tipoSanzione").asText()}",
			"ibanAccredito": "IT02L1234512345123456789012",
			"tipoContabilita": "ALTRO",
			"codiceContabilita": "${pathParams["idTipoPendenza"]}"
		}
	]
}