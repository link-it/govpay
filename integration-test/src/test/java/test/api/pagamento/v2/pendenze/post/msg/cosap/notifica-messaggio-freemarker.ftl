<#assign dataRichiesta = rpt.getDataMsgRichiesta()?string("yyyy-MM-dd HH:mm:ss")>
Il pagamento di "${versamento.getCausaleVersamento().getSimple()}" effettuato il ${dataRichiesta} risulta concluso con esito ${rpt.getEsitoPagamento().name()}:

Ente creditore: ${dominio.getRagioneSociale()} (${dominio.getCodDominio()})
Istituto attestante: ${rpt.getDenominazioneAttestante()} (${rpt.getIdentificativoAttestante()})
Identificativo univoco versamento (IUV): ${rpt.getIuv()}
Codice contesto pagamento (CCP): ${rpt.getCcp()}
Importo pagato: ${rpt.getImportoTotalePagato()}

Distinti saluti.