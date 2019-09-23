<#assign jsonUtilities = class["org.openspcoop2.utils.json.JSONUtils"].getInstance()>
<#assign request = jsonUtilities.getAsNode(jsonPath.read("$"))>
<#assign calendar = class["java.util.Calendar"]>
<#assign now = new("java.util.Date")>
<#assign calendarInstance = calendar.getInstance()>
<#assign xxx = calendarInstance.setTime(now)!>
<#assign yyy = calendarInstance.set(calendar.MONTH, 1)!>
<#assign zzz = calendarInstance.set(calendar.DATE, calendarInstance.getMaximum(calendar.DATE))!>
<#assign zzz1 = calendarInstance.add(calendar.YEAR, 1)!>
<#assign dataValidita = calendarInstance.getTime()?string("yyyy-MM-dd")>
<#assign importo = (2 * request.get("numero").asDouble())?string["#.00"]>
<#assign idPendenza = now.getTime()>
<#setting locale="en_US">
{
	"idA2A": "${request.get("idA2A").asText()}",
	idPendenza: "${request.get("idPendenza").asText()}",
	"soggettoPagatore": {
		"tipo": "F",
		"identificativo": "${request.get("soggettoPagatore").get("identificativo").asText()}",
		"anagrafica": "${request.get("soggettoPagatore").get("anagrafica").asText()}",
		"email": "${request.get("soggettoPagatore").get("email").asText()}"
	}
}