<#if rpt.getEsitoPagamento().getCodifica() = 0>
Notifica pagamento eseguito: ${rpt.getCodDominio()}/${rpt.getIuv()}/${rpt.getCcp()}
<#elseif rpt.getEsitoPagamento().getCodifica() = 1>
Notifica pagamento non eseguito: ${rpt.getCodDominio()}/${rpt.getIuv()}/${rpt.getCcp()}
<#elseif rpt.getEsitoPagamento().getCodifica() = 2>
Notifica pagamento eseguito parzialmente: ${rpt.getCodDominio()}/${rpt.getIuv()}/${rpt.getCcp()}
<#elseif rpt.getEsitoPagamento().getCodifica() = 3>
Notifica decorrenza termini pagamento: ${rpt.getCodDominio()}/${rpt.getIuv()}/${rpt.getCcp()}
<#elseif rpt.getEsitoPagamento().getCodifica() = 4>
Notifica decorrenza termini pagamento: ${rpt.getCodDominio()}/${rpt.getIuv()}/${rpt.getCcp()}
</#if>
