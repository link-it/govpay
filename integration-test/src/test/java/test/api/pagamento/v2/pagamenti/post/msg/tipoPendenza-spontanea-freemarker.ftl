<#setting locale="en_US">
<#setting number_format="0.##">
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
<#assign importo = (10 * request.get("numero").asDouble())>
<#assign idPendenza = now.getTime()>
{
	"idA2A": "IDA2A01",
	"idPendenza": "${idPendenza?string["#"]}",
	"idDominio": "${idDominio}",
	"idTipoPendenza": "${idTipoVersamento}",
 	"causale": "Pagamento n. ${request.get("numero").asText()} buoni pasto",
	"soggettoPagatore": {
		"tipo": "F",
		"identificativo": "${request.get("soggettoPagatore").get("identificativo").asText()}",
		"anagrafica": "${request.get("soggettoPagatore").get("anagrafica").asText()}",
		"email": "${request.get("soggettoPagatore").get("email").asText()}"
	},
	"importo": "${importo}",
	"dataValidita": "${dataValidita}",
	"dataScadenza": "${dataValidita}",
	"tassonomiaAvviso": "Servizi erogati dal comune",
	"voci": [
		{
			"idVocePendenza": "1",
			"importo": "${importo}",
			"descrizione": "Istruttoria",
			"ibanAccredito": "IT02L1234512345123456789012",
			"tipoContabilita": "ALTRO",
			"codiceContabilita": "RINNOVO"
		}
	]
}