<#assign dataRichiesta = rpt.getDataMsgRichiesta()?string("yyyy-MM-dd HH:mm:ss")>
Il pagamento di "${versamento.getCausaleVersamento().getSimple()}" effettuato il ${dataRichiesta} risulta concluso con esito ${rpt.getEsitoPagamento().name()}:

Ente creditore: ${dominio.getRagioneSociale()} (${dominio.getCodDominio()})
<#if rpt.getIdentificativoAttestante()?has_content>
Istituto attestante: ${rpt.getDenominazioneAttestante()} (${rpt.getIdentificativoAttestante()})
<#else>
Istituto attestante: ${rpt.getDenominazioneAttestante()}
</#if>
Identificativo univoco versamento (IUV): ${rpt.getIuv()}
<#if rpt.getVersione() == "SANP_230">
Codice contesto pagamento (CCP): ${rpt.getCcp()}
<#else>
Id Ricevuta: ${rpt.getCcp()}
</#if>
Importo pagato: ${rpt.getImportoTotalePagato()}

Distinti saluti.