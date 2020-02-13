<#assign csvUtils = class["it.govpay.core.utils.CSVUtils"].getInstance() />
<#if esitoOperazione == "ESEGUITO_OK">
<#assign idA2A = applicazione.getCodApplicazione() />
<#assign idPendenza = versamento.getCodVersamentoEnte() />
<#assign idDominio = dominio.getCodDominio() />
<#assign tipoPendenza = idTipoVersamento />
<#assign numeroAvviso = versamento.getNumeroAvviso()! />
<#if numeroAvviso?has_content>
	<#assign pdfAvviso = idDominio + "_" + numeroAvviso + ".pdf" />
</#if>
<#assign tipo = versamento.getAnagraficaDebitore().getTipo().toString() />
<#assign identificativo = versamento.getAnagraficaDebitore().getCodUnivoco()! />
<#assign anagrafica = versamento.getAnagraficaDebitore().getRagioneSociale()! />
<#assign indirizzo = versamento.getAnagraficaDebitore().getIndirizzo()! />
<#assign civico = versamento.getAnagraficaDebitore().getCivico()! />
<#assign cap = versamento.getAnagraficaDebitore().getCap()! />
<#assign localita = versamento.getAnagraficaDebitore().getLocalita()! />
<#assign provincia = versamento.getAnagraficaDebitore().getProvincia()! />
<#assign nazione = versamento.getAnagraficaDebitore().getNazione()! />
<#assign email = versamento.getAnagraficaDebitore().getEmail()! />
<#assign cellulare = versamento.getAnagraficaDebitore().getCellulare()! />
<#assign csvRecord = csvUtils.toCsv(idA2A, idPendenza, idDominio, tipoPendenza, numeroAvviso, pdfAvviso, tipo, identificativo, anagrafica, indirizzo, civico, cap, localita, provincia, nazione, email, cellulare) />
${csvRecord}
<#else>
,,,,,,,,,,,,,,,,,${descrizioneEsitoOperazione}
</#if>