<#assign csvUtils = class["it.govpay.core.utils.CSVUtils"].getInstance()>
<#assign csvRecord = csvUtils.getCSVRecord(lineaCsvRichiesta)>
{
	"idA2A": ${csvUtils.toJsonValue(csvRecord, 0)},
	"idPendenza": ${csvUtils.toJsonValue(csvRecord, 1)},
	"idDominio": ${csvUtils.toJsonValue(csvRecord, 2)},
	"numeroAvviso": ${csvUtils.toJsonValue(csvRecord, 3)},	
	"idTipoPendenza": ${csvUtils.toJsonValue(csvRecord, 4)},
	"idUnitaOperativa": ${csvUtils.toJsonValue(csvRecord, 5)},
 	"causale": ${csvUtils.toJsonValue(csvRecord, 6)},
 	"annoRiferimento": ${csvUtils.toJsonValue(csvRecord, 7)},
 	"cartellaPagamento": ${csvUtils.toJsonValue(csvRecord, 8)},
 	<#if !csvUtils.isEmpty(csvRecord, 9)>"datiAllegati": ${csvRecord.get(9)},</#if>
 	"direzione": ${csvUtils.toJsonValue(csvRecord, 10)},
 	"divisione": ${csvUtils.toJsonValue(csvRecord, 11)},
 	"importo": ${csvUtils.toJsonValue(csvRecord, 12)},
	"dataValidita": ${csvUtils.toJsonValue(csvRecord, 13)},
	"dataScadenza": ${csvUtils.toJsonValue(csvRecord, 14)},
	"tassonomiaAvviso": ${csvUtils.toJsonValue(csvRecord, 15)},
	"soggettoPagatore": {
		"tipo": ${csvUtils.toJsonValue(csvRecord, 16)},
		"identificativo": ${csvUtils.toJsonValue(csvRecord, 17)},
		"anagrafica": ${csvUtils.toJsonValue(csvRecord, 18)},
		"indirizzo": ${csvUtils.toJsonValue(csvRecord, 19)},
		"civico": ${csvUtils.toJsonValue(csvRecord, 20)},
		"cap": ${csvUtils.toJsonValue(csvRecord, 21)},
		"localita": ${csvUtils.toJsonValue(csvRecord, 22)},
		"provincia": ${csvUtils.toJsonValue(csvRecord, 23)},
		"nazione": ${csvUtils.toJsonValue(csvRecord, 24)},
		"email": ${csvUtils.toJsonValue(csvRecord, 25)},
		"cellulare": ${csvUtils.toJsonValue(csvRecord, 26)}
	},
	"voci": [
		{
			"idVocePendenza": ${csvUtils.toJsonValue(csvRecord, 27)},
			"importo": ${csvUtils.toJsonValue(csvRecord, 28)},
			"descrizione": ${csvUtils.toJsonValue(csvRecord, 29)},
			<#if !csvUtils.isEmpty(csvRecord, 34)>
			"tipoEntrata": ${csvUtils.toJsonValue(csvRecord, 34)}
			<#elseif !csvUtils.isEmpty(csvRecord, 35)>
			"tipoBollo": ${csvUtils.toJsonValue(csvRecord, 35)},
       		"hashDocumento": ${csvUtils.toJsonValue(csvRecord, 36)},
      		"provinciaResidenza": ${csvUtils.toJsonValue(csvRecord, 37)}
			<#else>
			"ibanAccredito": ${csvUtils.toJsonValue(csvRecord, 30)},
			"ibanAppoggio": ${csvUtils.toJsonValue(csvRecord, 31)},
			"tipoContabilita": ${csvUtils.toJsonValue(csvRecord, 32)},
			"codiceContabilita": ${csvUtils.toJsonValue(csvRecord, 33)}
			</#if>

		}
		<#if !csvUtils.isEmpty(csvRecord, 38)>
		,{
			"idVocePendenza": ${csvUtils.toJsonValue(csvRecord, 38)},
			"importo": ${csvUtils.toJsonValue(csvRecord, 39)},
			"descrizione": ${csvUtils.toJsonValue(csvRecord, 40)},
			<#if !csvUtils.isEmpty(csvRecord, 45)>
			"tipoEntrata": ${csvUtils.toJsonValue(csvRecord, 45)}
			<#elseif !csvUtils.isEmpty(csvRecord, 46)>
			"tipoBollo": ${csvUtils.toJsonValue(csvRecord, 46)},
       		"hashDocumento": ${csvUtils.toJsonValue(csvRecord, 47)},
      		"provinciaResidenza": ${csvUtils.toJsonValue(csvRecord, 48)}
			<#else>
			"ibanAccredito": ${csvUtils.toJsonValue(csvRecord, 41)},
			"ibanAppoggio": ${csvUtils.toJsonValue(csvRecord, 42)},
			"tipoContabilita": ${csvUtils.toJsonValue(csvRecord, 43)},
			"codiceContabilita": ${csvUtils.toJsonValue(csvRecord, 44)}
			</#if>
		}
		</#if>
		<#if !csvUtils.isEmpty(csvRecord, 49)>
		,{
			"idVocePendenza": ${csvUtils.toJsonValue(csvRecord, 49)},
			"importo": ${csvUtils.toJsonValue(csvRecord, 50)},
			"descrizione": ${csvUtils.toJsonValue(csvRecord, 51)},
			<#if !csvUtils.isEmpty(csvRecord, 56)>
			"tipoEntrata": ${csvUtils.toJsonValue(csvRecord, 56)}
			<#elseif !csvUtils.isEmpty(csvRecord, 57)>
			"tipoBollo": ${csvUtils.toJsonValue(csvRecord, 57)},
       		"hashDocumento": ${csvUtils.toJsonValue(csvRecord, 58)},
      		"provinciaResidenza": ${csvUtils.toJsonValue(csvRecord, 59)}
			<#else>
			"ibanAccredito": ${csvUtils.toJsonValue(csvRecord, 52)},
			"ibanAppoggio": ${csvUtils.toJsonValue(csvRecord, 53)},
			"tipoContabilita": ${csvUtils.toJsonValue(csvRecord, 54)},
			"codiceContabilita": ${csvUtils.toJsonValue(csvRecord, 55)}
			</#if>
		}
		</#if>
		<#if !csvUtils.isEmpty(csvRecord, 60)>
		,{
			"idVocePendenza": ${csvUtils.toJsonValue(csvRecord, 60)},
			"importo": ${csvUtils.toJsonValue(csvRecord, 61)},
			"descrizione": ${csvUtils.toJsonValue(csvRecord, 62)},
			<#if !csvUtils.isEmpty(csvRecord, 67)>
			"tipoEntrata": ${csvUtils.toJsonValue(csvRecord, 67)}
			<#elseif !csvUtils.isEmpty(csvRecord, 68)>
			"tipoBollo": ${csvUtils.toJsonValue(csvRecord, 68)},
       		"hashDocumento": ${csvUtils.toJsonValue(csvRecord, 69)},
      		"provinciaResidenza": ${csvUtils.toJsonValue(csvRecord, 70)}
			<#else>
			"ibanAccredito": ${csvUtils.toJsonValue(csvRecord, 63)},
			"ibanAppoggio": ${csvUtils.toJsonValue(csvRecord, 64)},
			"tipoContabilita": ${csvUtils.toJsonValue(csvRecord, 65)},
			"codiceContabilita": ${csvUtils.toJsonValue(csvRecord, 66)}
			</#if>
		}
		</#if>
		<#if !csvUtils.isEmpty(csvRecord, 71)>
		,{
			"idVocePendenza": ${csvUtils.toJsonValue(csvRecord, 71)},
			"importo": ${csvUtils.toJsonValue(csvRecord, 72)},
			"descrizione": ${csvUtils.toJsonValue(csvRecord, 73)},
			<#if !csvUtils.isEmpty(csvRecord, 78)>
			"tipoEntrata": ${csvUtils.toJsonValue(csvRecord, 78)}
			<#elseif !csvUtils.isEmpty(csvRecord, 79)>
			"tipoBollo": ${csvUtils.toJsonValue(csvRecord, 79)},
       		"hashDocumento": ${csvUtils.toJsonValue(csvRecord, 80)},
      		"provinciaResidenza": ${csvUtils.toJsonValue(csvRecord, 81)}
			<#else>
			"ibanAccredito": ${csvUtils.toJsonValue(csvRecord, 74)},
			"ibanAppoggio": ${csvUtils.toJsonValue(csvRecord, 75)},
			"tipoContabilita": ${csvUtils.toJsonValue(csvRecord, 76)},
			"codiceContabilita": ${csvUtils.toJsonValue(csvRecord, 77)}
			</#if>
		}
		</#if>
		<#if !csvUtils.isEmpty(csvRecord, 82)>
			
		</#if>
	]
}