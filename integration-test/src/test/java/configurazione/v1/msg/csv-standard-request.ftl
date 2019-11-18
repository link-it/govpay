<#assign csvUtils = class["it.govpay.core.utils.CSVUtils"].getInstance()>
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
 	<#if !csvUtils.isEmpty(csvRecord, 8)>"datiAllegati": ${csvRecord.get(8)},</#if>
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
			<#elseif !csvUtils.isEmpty(csvRecord, 34)>
			"tipoBollo": ${csvUtils.toJsonValue(csvRecord, 34)},
       		"hashDocumento": ${csvUtils.toJsonValue(csvRecord, 35)},
      		"provinciaResidenza": ${csvUtils.toJsonValue(csvRecord, 36)}
			<#else>
			"ibanAccredito": ${csvUtils.toJsonValue(csvRecord, 29)},
			"ibanAppoggio": ${csvUtils.toJsonValue(csvRecord, 30)},
			"tipoContabilita": ${csvUtils.toJsonValue(csvRecord, 31)},
			"codiceContabilita": ${csvUtils.toJsonValue(csvRecord, 32)}
			</#if>

		}
		<#if !csvUtils.isEmpty(csvRecord, 37)>
		,{
			"idVocePendenza": ${csvUtils.toJsonValue(csvRecord, 37)},
			"importo": ${csvUtils.toJsonValue(csvRecord, 38)},
			"descrizione": ${csvUtils.toJsonValue(csvRecord, 39)},
			<#if !csvUtils.isEmpty(csvRecord, 44)>
			"tipoEntrata": ${csvUtils.toJsonValue(csvRecord, 44)}
			<#elseif !csvUtils.isEmpty(csvRecord, 45)>
			"tipoBollo": ${csvUtils.toJsonValue(csvRecord, 45)},
       		"hashDocumento": ${csvUtils.toJsonValue(csvRecord, 46)},
      		"provinciaResidenza": ${csvUtils.toJsonValue(csvRecord, 47)}
			<#else>
			"ibanAccredito": ${csvUtils.toJsonValue(csvRecord, 40)},
			"ibanAppoggio": ${csvUtils.toJsonValue(csvRecord, 41)},
			"tipoContabilita": ${csvUtils.toJsonValue(csvRecord, 42)},
			"codiceContabilita": ${csvUtils.toJsonValue(csvRecord, 43)}
			</#if>
		}
		</#if>
		<#if !csvUtils.isEmpty(csvRecord, 48)>
		,{
			"idVocePendenza": ${csvUtils.toJsonValue(csvRecord, 48)},
			"importo": ${csvUtils.toJsonValue(csvRecord, 49)},
			"descrizione": ${csvUtils.toJsonValue(csvRecord, 50)},
			<#if !csvUtils.isEmpty(csvRecord, 55)>
			"tipoEntrata": ${csvUtils.toJsonValue(csvRecord, 55)}
			<#elseif !csvUtils.isEmpty(csvRecord, 56)>
			"tipoBollo": ${csvUtils.toJsonValue(csvRecord, 56)},
       		"hashDocumento": ${csvUtils.toJsonValue(csvRecord, 57)},
      		"provinciaResidenza": ${csvUtils.toJsonValue(csvRecord, 58)}
			<#else>
			"ibanAccredito": ${csvUtils.toJsonValue(csvRecord, 51)},
			"ibanAppoggio": ${csvUtils.toJsonValue(csvRecord, 52)},
			"tipoContabilita": ${csvUtils.toJsonValue(csvRecord, 53)},
			"codiceContabilita": ${csvUtils.toJsonValue(csvRecord, 54)}
			</#if>
		}
		</#if>
		<#if !csvUtils.isEmpty(csvRecord, 59)>
		,{
			"idVocePendenza": ${csvUtils.toJsonValue(csvRecord, 59)},
			"importo": ${csvUtils.toJsonValue(csvRecord, 60)},
			"descrizione": ${csvUtils.toJsonValue(csvRecord, 61)},
			<#if !csvUtils.isEmpty(csvRecord, 66)>
			"tipoEntrata": ${csvUtils.toJsonValue(csvRecord, 66)}
			<#elseif !csvUtils.isEmpty(csvRecord, 67)>
			"tipoBollo": ${csvUtils.toJsonValue(csvRecord, 67)},
       		"hashDocumento": ${csvUtils.toJsonValue(csvRecord, 68)},
      		"provinciaResidenza": ${csvUtils.toJsonValue(csvRecord, 69)}
			<#else>
			"ibanAccredito": ${csvUtils.toJsonValue(csvRecord, 62)},
			"ibanAppoggio": ${csvUtils.toJsonValue(csvRecord, 63)},
			"tipoContabilita": ${csvUtils.toJsonValue(csvRecord, 64)},
			"codiceContabilita": ${csvUtils.toJsonValue(csvRecord, 65)}
			</#if>
		}
		</#if>
		<#if !csvUtils.isEmpty(csvRecord, 70)>
		,{
			"idVocePendenza": ${csvUtils.toJsonValue(csvRecord, 70)},
			"importo": ${csvUtils.toJsonValue(csvRecord, 71)},
			"descrizione": ${csvUtils.toJsonValue(csvRecord, 72)},
			<#if !csvUtils.isEmpty(csvRecord, 77)>
			"tipoEntrata": ${csvUtils.toJsonValue(csvRecord, 77)}
			<#elseif !csvUtils.isEmpty(csvRecord, 78)>
			"tipoBollo": ${csvUtils.toJsonValue(csvRecord, 78)},
       		"hashDocumento": ${csvUtils.toJsonValue(csvRecord, 79)},
      		"provinciaResidenza": ${csvUtils.toJsonValue(csvRecord, 80)}
			<#else>
			"ibanAccredito": ${csvUtils.toJsonValue(csvRecord, 73)},
			"ibanAppoggio": ${csvUtils.toJsonValue(csvRecord, 74)},
			"tipoContabilita": ${csvUtils.toJsonValue(csvRecord, 75)},
			"codiceContabilita": ${csvUtils.toJsonValue(csvRecord, 76)}
			</#if>
		}
		</#if>
	]
}