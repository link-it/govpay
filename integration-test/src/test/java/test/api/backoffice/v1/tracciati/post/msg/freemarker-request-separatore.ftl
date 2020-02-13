<#assign csvFormat = class["org.apache.commons.csv.CSVFormat"].newFormat(';')>
<#assign csvFormat = csvFormat.withQuote('"')>
<#assign csvUtils = class["it.govpay.core.utils.CSVUtils"].getInstance(csvFormat)>
<#assign csvRecord = csvUtils.getCSVRecord(lineaCsvRichiesta)>
{
	"idA2A": ${csvUtils.toJsonValue(csvRecord, 0)},
	"idPendenza": ${csvUtils.toJsonValue(csvRecord, 1)},
	"idDominio": ${csvUtils.toJsonValue(csvRecord, 2)},
	"idTipoPendenza": ${csvUtils.toJsonValue(csvRecord, 3)},
	"idUnitaOperativa": ${csvUtils.toJsonValue(csvRecord, 4)},
 	"causale": ${csvUtils.toJsonValue(csvRecord, 5)},
 	"annoRiferimento": ${csvUtils.toJsonValue(csvRecord, 6)},
 	"cartellaPagamento": ${csvUtils.toJsonValue(csvRecord, 7)},
 	"datiAllegati" : {
 		"testAccenti": "òàáâãäåæçèéêëìíîï",
 		"testCaratteri" : "!#$%&'()*+,-./",
 		<#if !csvUtils.isEmpty(csvRecord, 8)>"datiAllegatiCSV": ${csvRecord.get(8)}</#if>
 	},
 	
 	"direzione": ${csvUtils.toJsonValue(csvRecord, 9)},
 	"divisione": ${csvUtils.toJsonValue(csvRecord, 10)},
 	"importo": ${csvUtils.toJsonValue(csvRecord, 11)},
	"dataValidita": ${csvUtils.toJsonValue(csvRecord, 12)},
	"dataScadenza": ${csvUtils.toJsonValue(csvRecord, 13)},
	"tassonomiaAvviso": ${csvUtils.toJsonValue(csvRecord, 14)},
	"soggettoPagatore": {
		"tipo": ${csvUtils.toJsonValue(csvRecord, 15)},
		"identificativo": ${csvUtils.toJsonValue(csvRecord, 16)},
		"anagrafica": ${csvUtils.toJsonValue(csvRecord, 17)},
		"indirizzo": ${csvUtils.toJsonValue(csvRecord, 18)},
		"civico": ${csvUtils.toJsonValue(csvRecord, 19)},
		"cap": ${csvUtils.toJsonValue(csvRecord, 20)},
		"localita": ${csvUtils.toJsonValue(csvRecord, 21)},
		"provincia": ${csvUtils.toJsonValue(csvRecord, 22)},
		"nazione": ${csvUtils.toJsonValue(csvRecord, 23)},
		"email": ${csvUtils.toJsonValue(csvRecord, 24)},
		"cellulare": ${csvUtils.toJsonValue(csvRecord, 25)}
	},
	"voci": [
		{
			"idVocePendenza": ${csvUtils.toJsonValue(csvRecord, 26)},
			"importo": ${csvUtils.toJsonValue(csvRecord, 27)},
			"descrizione": ${csvUtils.toJsonValue(csvRecord, 28)},
			<#if !csvUtils.isEmpty(csvRecord, 33)>
			"tipoEntrata": ${csvUtils.toJsonValue(csvRecord, 33)}
			<#else>
			"ibanAccredito": ${csvUtils.toJsonValue(csvRecord, 29)},
			"ibanAppoggio": ${csvUtils.toJsonValue(csvRecord, 30)},
			"tipoContabilita": ${csvUtils.toJsonValue(csvRecord, 31)},
			"codiceContabilita": ${csvUtils.toJsonValue(csvRecord, 32)}
			</#if>
		}
		<#if !csvUtils.isEmpty(csvRecord, 34)>
		,{
			"idVocePendenza": ${csvUtils.toJsonValue(csvRecord, 34)},
			"importo": ${csvUtils.toJsonValue(csvRecord, 35)},
			"descrizione": ${csvUtils.toJsonValue(csvRecord, 36)},
			<#if !csvUtils.isEmpty(csvRecord, 41)>
			"tipoEntrata": ${csvUtils.toJsonValue(csvRecord, 41)}
			<#else>
			"ibanAccredito": ${csvUtils.toJsonValue(csvRecord, 37)},
			"ibanAppoggio": ${csvUtils.toJsonValue(csvRecord, 38)},
			"tipoContabilita": ${csvUtils.toJsonValue(csvRecord, 39)},
			"codiceContabilita": ${csvUtils.toJsonValue(csvRecord, 40)}
			</#if>
		}
		</#if>
		<#if !csvUtils.isEmpty(csvRecord, 42)>		
		,{
			"idVocePendenza": ${csvUtils.toJsonValue(csvRecord, 42)},
			"importo": ${csvUtils.toJsonValue(csvRecord, 43)},
			"descrizione": ${csvUtils.toJsonValue(csvRecord, 44)},
			<#if !csvUtils.isEmpty(csvRecord, 49)>
			"tipoEntrata": ${csvUtils.toJsonValue(csvRecord, 49)}
			<#else>
			"ibanAccredito": ${csvUtils.toJsonValue(csvRecord, 45)},
			"ibanAppoggio": ${csvUtils.toJsonValue(csvRecord, 46)},
			"tipoContabilita": ${csvUtils.toJsonValue(csvRecord, 47)},
			"codiceContabilita": ${csvUtils.toJsonValue(csvRecord, 48)}
			</#if>
		}
		</#if>
		<#if !csvUtils.isEmpty(csvRecord, 50)>
		,{
			"idVocePendenza": ${csvUtils.toJsonValue(csvRecord, 50)},
			"importo": ${csvUtils.toJsonValue(csvRecord, 51)},
			"descrizione": ${csvUtils.toJsonValue(csvRecord, 52)},
			<#if !csvUtils.isEmpty(csvRecord, 57)>
			"tipoEntrata": ${csvUtils.toJsonValue(csvRecord, 57)}
			<#else>
			"ibanAccredito": ${csvUtils.toJsonValue(csvRecord, 53)},
			"ibanAppoggio": ${csvUtils.toJsonValue(csvRecord, 54)},
			"tipoContabilita": ${csvUtils.toJsonValue(csvRecord, 55)},
			"codiceContabilita": ${csvUtils.toJsonValue(csvRecord, 56)}
			</#if>
		}
		</#if>
		<#if !csvUtils.isEmpty(csvRecord, 58)>
		,{
			"idVocePendenza": ${csvUtils.toJsonValue(csvRecord, 58)},
			"importo": ${csvUtils.toJsonValue(csvRecord, 59)},
			"descrizione": ${csvUtils.toJsonValue(csvRecord, 60)},
			<#if !csvUtils.isEmpty(csvRecord, 85)>
			"tipoEntrata": ${csvUtils.toJsonValue(csvRecord, 65)}
			<#else>
			"ibanAccredito": ${csvUtils.toJsonValue(csvRecord, 61)},
			"ibanAppoggio": ${csvUtils.toJsonValue(csvRecord, 62)},
			"tipoContabilita": ${csvUtils.toJsonValue(csvRecord, 63)},
			"codiceContabilita": ${csvUtils.toJsonValue(csvRecord, 64)}
			</#if>
		}
		</#if>
	]
}