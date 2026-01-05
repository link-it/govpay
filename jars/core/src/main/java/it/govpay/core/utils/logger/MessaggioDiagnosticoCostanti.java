/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2026 Link.it srl (http://www.link.it).
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3, as published by
 * the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package it.govpay.core.utils.logger;

/**
 * @author Pintori Giuliano (giuliano.pintori@link.it)
 * @author  $Author: pintori $
 *
 */
public class MessaggioDiagnosticoCostanti {

	private MessaggioDiagnosticoCostanti() {/*static only*/}
	
	public static final String MSG_DIAGNOSTICO_RT_ERRORE_AUTORIZZAZIONE = "rt.erroreAutorizzazione";
	public static final String MSG_DIAGNOSTICO_RT_ERRORE_NO_AUTORIZZAZIONE = "rt.erroreNoAutorizzazione";
	public static final String MSG_DIAGNOSTICO_PAGAMENTO_INVIO_RPT_ATTIVATA_OK = "pagamento.invioRptAttivataOk";
	public static final String MSG_DIAGNOSTICO_CCP_VERSAMENTO_IUV_NON_PRESENTE = "ccp.versamentoIuvNonPresente";
	public static final String MSG_DIAGNOSTICO_PAGAMENTO_RICEZIONE_RT = "pagamento.ricezioneRt";
	public static final String MSG_DIAGNOSTICO_CCP_RICEZIONE_VERIFICA = "ccp.ricezioneVerifica";
	public static final String MSG_DIAGNOSTICO_RT_RICEZIONE_KO = "rt.ricezioneKo";
	public static final String MSG_DIAGNOSTICO_RT_RICEZIONE_OK = "rt.ricezioneOk";
	public static final String MSG_DIAGNOSTICO_PAGAMENTO_ACQUISIZIONE_RT_OK = "pagamento.acquisizioneRtOk";
	public static final String MSG_DIAGNOSTICO_CCP_RICEZIONE_VERIFICA_KO = "ccp.ricezioneVerificaKo";
	public static final String MSG_DIAGNOSTICO_CCP_RICEZIONE_VERIFICA_OK = "ccp.ricezioneVerificaOk";
	public static final String MSG_DIAGNOSTICO_CCP_IUV_NON_PRESENTE = "ccp.iuvNonPresente";
	public static final String MSG_DIAGNOSTICO_CCP_IUV_NON_PRESENTE_NO_APP_GESTIRE_IUV = "ccp.iuvNonPresenteNoAppGestireIuv";
	public static final String MSG_DIAGNOSTICO_CCP_IUV_PRESENTE = "ccp.iuvPresente";
	public static final String MSG_DIAGNOSTICO_CCP_RICEZIONE_ATTIVA_KO = "ccp.ricezioneAttivaKo";
	public static final String MSG_DIAGNOSTICO_CCP_RICEZIONE_ATTIVA_OK = "ccp.ricezioneAttivaOk";
	public static final String MSG_DIAGNOSTICO_CCP_ATTIVAZIONE = "ccp.attivazione";
	public static final String MSG_DIAGNOSTICO_CCP_VERSAMENTO_IUV_NON_PRESENTE_OK = "ccp.versamentoIuvNonPresenteOk";
	public static final String MSG_DIAGNOSTICO_CCP_ERRORE_AUTORIZZAZIONE = "ccp.erroreAutorizzazione";
	public static final String MSG_DIAGNOSTICO_CCP_ERRORE_NO_AUTORIZZAZIONE = "ccp.erroreNoAutorizzazione";
	public static final String MSG_DIAGNOSTICO_CCP_RICEZIONE_ATTIVA = "ccp.ricezioneAttiva";
	public static final String MSG_DIAGNOSTICO_RECUPERO_RT_RECUPERO_RT_AVVIO_THREAD_KEY = "recuperoRT.avvioThread";
	public static final String MSG_DIAGNOSTICO_RECUPERO_RT_RECUPERO_RT_OK_KEY = "recuperoRT.Ok";
	public static final String MSG_DIAGNOSTICO_RECUPERO_RT_RECUPERO_RT_FAIL_KEY = "recuperoRT.Fail";
	public static final String MSG_DIAGNOSTICO_PAGAMENTO_INVIO_RPT_ATTIVATA_KEY = "pagamento.invioRptAttivata";
	public static final String MSG_DIAGNOSTICO_PAGAMENTO_INVIO_RPT_ATTIVATA_R_TRICEVUTA_KEY = "pagamento.invioRptAttivataRTricevuta";
	public static final String MSG_DIANGOSTICO_PAGAMENTO_INVIO_RPT_ATTIVATA_KO_KEY = "pagamento.invioRptAttivataKo";
	public static final String MSG_DIAGNOSTICO_PAGAMENTO_INVIO_RPT_ATTIVATA_OK_KEY = "pagamento.invioRptAttivataOk";
	public static final String MSG_DIAGNOSTICO_PAGAMENTO_INVIO_RPT_ATTIVATA_FAIL_KEY = "pagamento.invioRptAttivataFail";
	public static final String MSG_DIAGNOSTICO_VERIFICA_MODELLO4VERIFICA_SCONOSCIUTO = "verifica.modello4verificaSconosciuto";
	public static final String MSG_DIAGNOSTICO_VERIFICA_MODELLO4VERIFICA_SCADUTO = "verifica.modello4verificaScaduto";
	public static final String MSG_DIAGNOSTICO_VERIFICA_MODELLO4VERIFICA_DUPLICATO = "verifica.modello4verificaDuplicato";
	public static final String MSG_DIAGNOSTICO_VERIFICA_MODELLO4VERIFICA_ANNULLATO = "verifica.modello4verificaAnnullato";
	public static final String MSG_DIAGNOSTICO_VERIFICA_MODELLO4VERIFICA_OK = "verifica.modello4verificaOk";
	public static final String MSG_DIAGNOSTICO_VERIFICA_MODELLO4VERIFICA = "verifica.modello4verifica";
	public static final String MSG_DIAGNOSTICO_VERIFICA_VERIFICA_SCONOSCIUTO = "verifica.verificaSconosciuto";
	public static final String MSG_DIAGNOSTICO_VERIFICA_VERIFICA_SCADUTO = "verifica.verificaScaduto";
	public static final String MSG_DIAGNOSTICO_VERIFICA_VERIFICA_DUPLICATO = "verifica.verificaDuplicato";
	public static final String MSG_DIAGNOSTICO_VERIFICA_VERIFICA_ANNULLATO = "verifica.verificaAnnullato";
	public static final String MSG_DIAGNOSTICO_VERIFICA_VERIFICA_OK = "verifica.verificaOk";
	public static final String MSG_DIAGNOSTICO_VERIFICA_VERIFICA = "verifica.verifica";
	public static final String MSG_DIAGNOSTICO_VERIFICA_VERIFICA_KO = "verifica.verificaKo";
	public static final String MSG_DIAGNOSTICO_VERIFICA_MODELLO4_VERIFICA_KO = "verifica.modello4verificaKo";
	public static final String MSG_DIAGNOSTICO_JPPAPDP_INVIO_NOTIFICA_PAGAMENTO = "jppapdp.invioNotificaPagamento";
	public static final String MSG_DIAGNOSTICO_JPPAPDP_INVIO_NOTIFICA_PAGAMENTO_STATO_RPT_NON_VALIDO = "jppapdp.invioNotificaPagamentoStatoRptNonValido";
	public static final String MSG_DIAGNOSTICO_JPPAPDP_INVIO_NOTIFICA_PAGAMENTO_OK = "jppapdp.invioNotificaPagamentoOk";
	public static final String MSG_DIAGNOSTICO_JPPAPDP_INVIO_NOTIFICA_PAGAMENTO_FAIL = "jppapdp.invioNotificaPagamentoFail";
	public static final String MSG_DIAGNOSTICO_TRACCIATO_NOTIFICA_PAGAMENTI_REST_KO_KEY = "tracciatoNotificaPagamenti.restKo";
	public static final String MSG_DIAGNOSTICO_TRACCIATO_NOTIFICA_PAGAMENTI_REST_OK_KEY = "tracciatoNotificaPagamenti.restOk";
	public static final String MSG_DIAGNOSTICO_TRACCIATO_NOTIFICA_PAGAMENTI_REST_CONTENUTO_KO_KEY = "tracciatoNotificaPagamenti.restContenutoKo";
	public static final String MSG_DIAGNOSTICO_TRACCIATO_NOTIFICA_PAGAMENTI_REST_CONTENUTO_OK_KEY = "tracciatoNotificaPagamenti.restContenutoOk";
	public static final String MSG_DIAGNOSTICO_TRACCIATO_NOTIFICA_PAGAMENTI_FILE_SYSTEM_OK = "tracciatoNotificaPagamenti.fileSystemOk";
	public static final String MSG_DIAGNOSTICO_TRACCIATO_NOTIFICA_PAGAMENTI_EMAIL_OK = "tracciatoNotificaPagamenti.emailOk";
	public static final String MSG_DIAGNOSTICO_TRACCIATO_NOTIFICA_PAGAMENTI_REST = "tracciatoNotificaPagamenti.rest";
	public static final String MSG_DIAGNOSTICO_TRACCIATO_NOTIFICA_PAGAMENTI_WEB_SERVICE_OK = "tracciatoNotificaPagamenti.webServiceOk";
	public static final String MSG_DIAGNOSTICO_TRACCIATO_NOTIFICA_PAGAMENTI_WEB_SERVICE = "tracciatoNotificaPagamenti.webService";
	public static final String MSG_DIAGNOSTICO_TRACCIATO_NOTIFICA_PAGAMENTI_FILE_SYSTEM = "tracciatoNotificaPagamenti.fileSystem";
	public static final String MSG_DIAGNOSTICO_TRACCIATO_NOTIFICA_PAGAMENTI_SPEDIZIONE = "tracciatoNotificaPagamenti.spedizione";
	public static final String MSG_DIAGNOSTICO_TRACCIATO_NOTIFICA_PAGAMENTI_EMAIL = "tracciatoNotificaPagamenti.email";
	public static final String MSG_DIAGNOSTICO_TRACCIATO_NOTIFICA_PAGAMENTI_ANNULLATO = "tracciatoNotificaPagamenti.annullato";
	public static final String MSG_DIAGNOSTICO_TRACCIATO_NOTIFICA_PAGAMENTI_REST_RETRY_KO = "tracciatoNotificaPagamenti.restRetryKo";
	public static final String MSG_DIAGNOSTICO_TRACCIATO_NOTIFICA_PAGAMENTI_FILE_SYSTEM_KO = "tracciatoNotificaPagamenti.fileSystemKo";
	public static final String MSG_DIAGNOSTICO_TRACCIATO_NOTIFICA_PAGAMENTI_FILE_SYSTEM_RETRY_KO = "tracciatoNotificaPagamenti.fileSystemRetryKo";
	public static final String MSG_DIAGNOSTICO_TRACCIATO_NOTIFICA_PAGAMENTI_EMAIL_RETRY_KO = "tracciatoNotificaPagamenti.emailRetryKo";
	public static final String MSG_DIAGNOSTICO_TRACCIATO_NOTIFICA_PAGAMENTI_EMAIL_KO = "tracciatoNotificaPagamenti.emailKo";
	public static final String MSG_DIAGNOSTICO_NOTIFICA_RT_RETRYKO = "notifica.rtRetryko";
	public static final String MSG_DIAGNOSTICO_NOTIFICA_FALLIMENTO_RPT_RETRYKO = "notifica.fallimentoRptRetryko";
	public static final String MSG_DIAGNOSTICO_NOTIFICA_ANNULLAMENTO_RPT_RETRYKO = "notifica.annullamentoRptRetryko";
	public static final String MSG_DIAGNOSTICO_NOTIFICA_RPT_RETRYKO = "notifica.rptRetryko";
	public static final String MSG_DIAGNOSTICO_NOTIFICA_CARRELLO_RETRYKO = "notifica.carrelloRetryko";
	public static final String MSG_DIAGNOSTICO_NOTIFICA_RTKO = "notifica.rtko";
	public static final String MSG_DIAGNOSTICO_NOTIFICA_FALLIMENTO_RPTKO = "notifica.fallimentoRptko";
	public static final String MSG_DIAGNOSTICO_NOTIFICA_ANNULLAMENTO_RPTKO = "notifica.annullamentoRptko";
	public static final String MSG_DIAGNOSTICO_NOTIFICA_RPTKO = "notifica.rptko";
	public static final String MSG_DIAGNOSTICO_NOTIFICA_CARRELLOKO = "notifica.carrelloko";
	public static final String MSG_DIAGNOSTICO_NOTIFICA_RTOK = "notifica.rtok";
	public static final String MSG_DIAGNOSTICO_NOTIFICA_FALLIMENTO_RPTOK = "notifica.fallimentoRptok";
	public static final String MSG_DIAGNOSTICO_NOTIFICA_ANNULLAMENTO_RPTOK = "notifica.annullamentoRptok";
	public static final String MSG_DIAGNOSTICO_NOTIFICA_RPTOK = "notifica.rptok";
	public static final String MSG_DIAGNOSTICO_NOTIFICA_CARRELLOOK = "notifica.carrellook";
	public static final String MSG_DIAGNOSTICO_NOTIFICA_SPEDIZIONE = "notifica.spedizione";
	public static final String MSG_DIAGNOSTICO_NOTIFICA_ANNULLATA = "notifica.annullata";
	public static final String MSG_DIAGNOSTICO_NOTIFICA_RT = "notifica.rt";
	public static final String MSG_DIAGNOSTICO_NOTIFICA_FALLIMENTO_RPT = "notifica.fallimentoRpt";
	public static final String MSG_DIAGNOSTICO_NOTIFICA_ANNULLAMENTO_RPT = "notifica.annullamentoRpt";
	public static final String MSG_DIAGNOSTICO_NOTIFICA_RPT = "notifica.rpt";
	public static final String MSG_DIAGNOSTICO_RECUPERO_RT_RECUPERO_RT_KO = "recuperoRT.recuperoRTKo";
	public static final String MSG_DIAGNOSTICO_RECUPERO_RT_RECUPERO_RT_OK = "recuperoRT.recuperoRTOk";
	public static final String MSG_DIAGNOSTICO_NDP_CLIENT_INVIO_RICHIESTA = "ndp_client.invioRichiesta";
	public static final String MSG_DIAGNOSTICO_NDP_CLIENT_INVIO_RICHIESTA_FAULT = "ndp_client.invioRichiestaFault";
	public static final String MSG_DIAGNOSTICO_NDP_CLIENT_INVIO_RICHIESTA_OK = "ndp_client.invioRichiestaOk";
	public static final String MSG_DIAGNOSTICO_NDP_CLIENT_INVIO_RICHIESTA_KO = "ndp_client.invioRichiestaKo";
	public static final String MSG_DIAGNOSTICO_JPPAPDP_CLIENT_INVIO_RICHIESTA_KO = "jppapdp_client.invioRichiestaKo";
	public static final String MSG_DIAGNOSTICO_JPPAPDP_CLIENT_INVIO_RICHIESTA_OK = "jppapdp_client.invioRichiestaOk";
	public static final String MSG_DIAGNOSTICO_JPPAPDP_CLIENT_INVIO_RICHIESTA = "jppapdp_client.invioRichiesta";
	public static final String MSG_DIAGNOSTICO_VERSAMENTO_INSERIMENTO_OK = "versamento.inserimentoOk";
	public static final String MSG_DIAGNOSTICO_VERSAMENTO_AGGIORAMENTO_OK = "versamento.aggioramentoOk";
	public static final String MSG_DIAGNOSTICO_WS_RICEVUTA_RICHIESTA_KO = "ws.ricevutaRichiestaKo";
	public static final String MSG_DIAGNOSTICO_WS_AUTORIZZAZIONE = "ws.autorizzazione";
	public static final String MSG_DIAGNOSTICO_WS_RICEVUTA_RICHIESTA = "ws.ricevutaRichiesta";
	public static final String MSG_DIAGNOSTICO_RPT_ACQUISIZIONE_VERSAMENTO = "rpt.acquisizioneVersamento";
	public static final String MSG_DIAGNOSTICO_INCASSO_PAGAMENTO_GIA_INCASSATO = "incasso.pagamentoGiaIncassato";
	public static final String MSG_DIAGNOSTICO_INCASSO_IDF_NON_TROVATO = "incasso.idfNonTrovato";
	public static final String MSG_DIAGNOSTICO_INCASSO_IMPORTO_ERRATO = "incasso.importoErrato";
	public static final String MSG_DIAGNOSTICO_INCASSO_IUV_PAGAMENTI_MULTIPLI = "incasso.iuvPagamentiMultipli";
	public static final String MSG_DIAGNOSTICO_INCASSO_IUV_NON_TROVATO = "incasso.iuvNonTrovato";
	public static final String MSG_DIAGNOSTICO_INCASSO_CAUSALE_NON_VALIDA = "incasso.causaleNonValida";
	public static final String MSG_DIAGNOSTICO_INCASSO_IBAN_INESISTENTE = "incasso.ibanInesistente";
	public static final String MSG_DIAGNOSTICO_INCASSO_DOMINIO_INESISTENTE = "incasso.dominioInesistente";
	public static final String MSG_DIAGNOSTICO_INCASSO_RICHIESTA = "incasso.richiesta";
	public static final String MSG_DIAGNOSTICO_INCASSO_FR_ANOMALA = "incasso.frAnomala";
	public static final String MSG_DIAGNOSTICO_INCASSO_SINTASSI = "incasso.sintassi";
	public static final String MSG_DIAGNOSTICO_INCASSO_DUPLICATO = "incasso.duplicato";
	public static final String MSG_DIAGNOSTICO_IUV_CHECK_IUV_NUMERICO_WARN = "iuv.checkIUVNumericoWarn";
	public static final String MSG_DIAGNOSTICO_IUV_GENERAZIONE_IUV_PREFIX_FAIL = "iuv.generazioneIUVPrefixFail";
	public static final String MSG_DIAGNOSTICO_IUV_GENERAZIONE_IUV_OK = "iuv.generazioneIUVOk";
	public static final String MSG_DIAGNOSTICO_RECUPERO_RT_RECUPERO_RT_AVVIO_KEY = "recuperoRT.avvio";
	public static final String MSG_DIAGNOSTICO_RECUPERO_RT_RECUPERO_RT_CONCLUSIONE_KEY = "recuperoRT.conclusione";
	public static final String MSG_DIAGNOSTICO_RECUPERO_RT_RECUPERO_RT_ACQUISIZIONE_LISTA_RENDICONTAZIONI_KEY = "recuperoRT.acquisizioneListaRendicontazioni";
	public static final String MSG_DIAGNOSTICO_RECUPERO_RT_RECUPERO_RT_LISTA_RENDICONTAZIONI_GOVPAY_OK_KEY = "recuperoRT.listaRendicontazioniGovPayOk";
	public static final String MSG_DIAGNOSTICO_RECUPERO_RT_RECUPERO_RT_DOMINIO_OK_KEY = "recuperoRT.dominioOk";
	public static final String MSG_DIAGNOSTICO_RECUPERO_RT_RECUPERO_RT_NON_CONFIGURATO_KEY = "recuperoRT.nonConfigurato";
	public static final String MSG_DIAGNOSTICO_PAGAMENTO_AVVIA_TRANSAZIONE_CARRELLO_WISP20 = "pagamento.avviaTransazioneCarrelloWISP20";
	public static final String MSG_DIAGNOSTICO_RPT_VALIDAZIONE_SEMANTICA = "rpt.validazioneSemantica";
	public static final String MSG_DIAGNOSTICO_RPT_VALIDAZIONE_SEMANTICA_OK = "rpt.validazioneSemanticaOk";
	public static final String MSG_DIAGNOSTICO_IUV_ASSEGNAZIONE_IUV_CUSTOM = "iuv.assegnazioneIUVCustom";
	public static final String MSG_DIAGNOSTICO_IUV_ASSEGNAZIONE_IUV_RIUSO = "iuv.assegnazioneIUVRiuso";
	public static final String MSG_DIAGNOSTICO_IUV_ASSEGNAZIONE_IUV_GENERATO = "iuv.assegnazioneIUVGenerato";
	public static final String MSG_DIAGNOSTICO_RPT_CREAZIONE_RPT = "rpt.creazioneRpt";
	public static final String MSG_DIAGNOSTICO_RPT_INVIO_CARRELLO_RPT = "rpt.invioCarrelloRpt";
	public static final String MSG_DIAGNOSTICO_RPT_INVIO_KO = "rpt.invioKo";
	public static final String MSG_DIAGNOSTICO_RPT_INVIO_OK = "rpt.invioOk";
	public static final String MSG_DIAGNOSTICO_RPT_INVIO_OK_NO_REDIRECT = "rpt.invioOkNoRedirect";
	public static final String MSG_DIAGNOSTICO_RPT_INVIO_FAIL = "rpt.invioFail";
	public static final String MSG_DIAGNOSTICO_PAGAMENTO_INVIO_CARRELLO_RPT_NO_REDIRECT = "pagamento.invioCarrelloRptNoRedirect";
	public static final String MSG_DIAGNOSTICO_PAGAMENTO_INVIO_CARRELLO_RPT = "pagamento.invioCarrelloRpt";
	public static final String MSG_DIAGNOSTICO_RPT_ACQUISIZIONE_VERSAMENTO_REF_BUNDLE = "rpt.acquisizioneVersamentoRefBundle";
	public static final String MSG_DIAGNOSTICO_RPT_ACQUISIZIONE_VERSAMENTO_REF_IUV = "rpt.acquisizioneVersamentoRefIuv";
	public static final String MSG_DIAGNOSTICO_RPT_ACQUISIZIONE_VERSAMENTO_REF = "rpt.acquisizioneVersamentoRef";
	public static final String MSG_DIAGNOSTICO_VERSAMENTO_ANNULLA_OK = "versamento.annullaOk";
	public static final String MSG_DIAGNOSTICO_VERSAMENTO_ANNULLA_KO = "versamento.annullaKo";
	public static final String MSG_DIAGNOSTICO_VERSAMENTO_ANNULLA = "versamento.annulla";
	public static final String MSG_DIAGNOSTICO_VERSAMENTO_VALIDAZIONE_SEMANTICA_AGGIORNAMENTO_OK = "versamento.validazioneSemanticaAggiornamentoOk";
	public static final String MSG_DIAGNOSTICO_VERSAMENTO_VALIDAZIONE_SEMANTICA_AGGIORNAMENTO = "versamento.validazioneSemanticaAggiornamento";
	public static final String MSG_DIAGNOSTICO_VERSAMENTO_VALIDAZIONE_SEMANTICA_OK = "versamento.validazioneSemanticaOk";
	public static final String MSG_DIAGNOSTICO_VERSAMENTO_VALIDAZIONE_SEMANTICA = "versamento.validazioneSemantica";
	public static final String MSG_DIAGNOSTICO_RT_ACQUISIZIONE_OK = "rt.acquisizioneOk";
	public static final String MSG_DIAGNOSTICO_RT_AGGIORNAMENTO_PAGAMENTO = "rt.aggiornamentoPagamento";
	public static final String MSG_DIAGNOSTICO_RT_ACQUISIZIONE_PAGAMENTO = "rt.acquisizionePagamento";
	public static final String MSG_DIAGNOSTICO_PAGAMENTO_ACQUISIZIONE_PAGAMENTO_ANOMALO = "pagamento.acquisizionePagamentoAnomalo";
	public static final String MSG_DIAGNOSTICO_PAGAMENTO_RECUPERO_RT_ACQUISIZIONE_PAGAMENTO_ANOMALO = "pagamento.recuperoRtAcquisizionePagamentoAnomalo";
	public static final String MSG_DIAGNOSTICO_RT_ACQUISIZIONE = "rt.acquisizione";
	public static final String MSG_DIAGNOSTICO_RT_RT_RECUPERO_ACQUISIZIONE = "rt.rtRecuperoAcquisizione";
	public static final String MSG_DIAGNOSTICO_PAGAMENTO_VALIDAZIONE_RT_FAIL = "pagamento.validazioneRtFail";
	public static final String MSG_DIAGNOSTICO_PAGAMENTO_RECUPERO_RT_VALIDAZIONE_RT_FAIL = "pagamento.recuperoRtValidazioneRtFail";
	public static final String MSG_DIAGNOSTICO_PAGAMENTO_VALIDAZIONE_RT_WARN = "pagamento.validazioneRtWarn";
	public static final String MSG_DIAGNOSTICO_PAGAMENTO_RECUPERO_RT_VALIDAZIONE_RT_WARN = "pagamento.recuperoRtValidazioneRtWarn";
	public static final String MSG_DIAGNOSTICO_PAGAMENTO_ENTRATA_PA_NON_INTERMEDIATA = "pagamento.entrataPaNonIntermediata";
	public static final String MSG_DIAGNOSTICO_VERIFICA_MODELLO4_SCONOSCIUTO_KEY = "verifica.modello4Sconosciuto";
	public static final String MSG_DIAGNOSTICO_VERIFICA_MODELLO4_DUPLICATO_KEY = "verifica.modello4Duplicato";
	public static final String MSG_DIAGNOSTICO_VERIFICA_MODELLO4_ANNULLATO_KEY = "verifica.modello4Annullato";
	public static final String MSG_DIAGNOSTICO_VERIFICA_MODELLO4_SCADUTO_KEY = "verifica.modello4Scaduto";
	public static final String MSG_DIAGNOSTICO_VERIFICA_MODELLO4_FAIL_KEY = "verifica.modello4Fail";
	public static final String MSG_DIAGNOSTICO_VERIFICA_MODELLO4_OK_KEY = "verifica.modello4Ok";
	public static final String MSG_DIAGNOSTICO_VERIFICA_MODELLO4_AVVIO_KEY = "verifica.modello4Avvio";
	public static final String MSG_DIAGNOSTICO_VERIFICA_AVVIO_KEY = "verifica.avvio";
	public static final String MSG_DIAGNOSTICO_VERIFICA_NON_CONFIGURATA_KEY = "verifica.nonConfigurata";
	public static final String MSG_DIAGNOSTICO_VERIFICA_OK_KEY = "verifica.Ok";
	public static final String MSG_DIAGNOSTICO_VERIFICA_SCONOSCIUTO_KEY = "verifica.Sconosciuto";
	public static final String MSG_DIAGNOSTICO_VERIFICA_DUPLICATO_KEY = "verifica.Duplicato";
	public static final String MSG_DIAGNOSTICO_VERIFICA_ANNULLATO_KEY = "verifica.Annullato";
	public static final String MSG_DIAGNOSTICO_VERIFICA_SCADUTO_KEY = "verifica.Scaduto";
	public static final String MSG_DIAGNOSTICO_VERIFICA_FAIL_KEY = "verifica.Fail";
	public static final String MSG_DIAGNOSTICO_VERIFICA_VALIDITA_KEY = "verifica.validita";
	public static final String MSG_DIAGNOSTICO_JPPAPDP_ERRORE_AUTORIZZAZIONE = "jppapdp.erroreAutorizzazione";
	public static final String MSG_DIAGNOSTICO_JPPAPDP_RICEZIONE_RECUPERA_RT_OK = "jppapdp.ricezioneRecuperaRTOk";
	public static final String MSG_DIAGNOSTICO_JPPAPDP_RICEZIONE_RECUPERA_RT_PARAMETRI = "jppapdp.ricezioneRecuperaRTParametri";
	public static final String MSG_DIAGNOSTICO_JPPAPDP_RICEZIONE_RECUPERA_RT = "jppapdp.ricezioneRecuperaRT";
	public static final String MSG_DIAGNOSTICO_JPPAPDP_RICEZIONE_RECUPERA_RT_KO = "jppapdp.ricezioneRecuperaRTKo";
	public static final String MSG_DIAGNOSTICO_RENDICONTAZIONI_ACQUISIZIONE_OK = "rendicontazioni.acquisizioneOk";
	public static final String MSG_DIAGNOSTICO_RENDICONTAZIONI_ACQUISIZIONE_FLUSSO_OK_ANOMALIA = "rendicontazioni.acquisizioneFlussoOkAnomalia";
	public static final String MSG_DIAGNOSTICO_RENDICONTAZIONI_ACQUISIZIONE_FLUSSO_OK = "rendicontazioni.acquisizioneFlussoOk";
	public static final String MSG_DIAGNOSTICO_RENDICONTAZIONI_NUMERO_RENDICONTAZIONI_ERRATO = "rendicontazioni.numeroRendicontazioniErrato";
	public static final String MSG_DIAGNOSTICO_RENDICONTAZIONI_IMPORTO_TOTALE_ERRATO = "rendicontazioni.importoTotaleErrato";
	public static final String MSG_DIAGNOSTICO_RENDICONTAZIONI_POLI_PAGAMENTO = "rendicontazioni.poliPagamento";
	public static final String MSG_DIAGNOSTICO_RENDICONTAZIONI_NO_PAGAMENTO = "rendicontazioni.noPagamento";
	public static final String MSG_DIAGNOSTICO_RENDICONTAZIONI_SENZA_RPT_VERSAMENTO_MALFORMATO = "rendicontazioni.senzaRptVersamentoMalformato";
	public static final String MSG_DIAGNOSTICO_RENDICONTAZIONI_SENZA_RPT_NO_VERSAMENTO = "rendicontazioni.senzaRptNoVersamento";
	public static final String MSG_DIAGNOSTICO_RENDICONTAZIONI_IMPORTO_ERRATO = "rendicontazioni.importoErrato";
	public static final String MSG_DIAGNOSTICO_RENDICONTAZIONI_IMPORTO_STORNO_ERRATO = "rendicontazioni.importoStornoErrato";
	public static final String MSG_DIAGNOSTICO_RENDICONTAZIONI_ESITO_SCONOSCIUTO = "rendicontazioni.esitoSconosciuto";
	public static final String MSG_DIAGNOSTICO_RENDICONTAZIONI_ACQUISIZIONE_FLUSSO_DOMINIO_NON_CENSITO = "rendicontazioni.acquisizioneFlussoDominioNonCensito";
	public static final String MSG_DIAGNOSTICO_RENDICONTAZIONI_ACQUISIZIONE_FLUSSO = "rendicontazioni.acquisizioneFlusso";
	public static final String MSG_DIAGNOSTICO_RENDICONTAZIONI_ACQUISIZIONE_FLUSSO_KO = "rendicontazioni.acquisizioneFlussoKo";
	public static final String MSG_DIAGNOSTICO_RENDICONTAZIONI_ACQUISIZIONE_FLUSSO_FAIL = "rendicontazioni.acquisizioneFlussoFail";
	public static final String MSG_DIAGNOSTICO_RENDICONTAZIONI_ACQUISIZIONE = "rendicontazioni.acquisizione";
	public static final String MSG_DIAGNOSTICO_RENDICONTAZIONI_ACQUISIZIONE_FLUSSI_OK = "rendicontazioni.acquisizioneFlussiOk";
	public static final String MSG_DIAGNOSTICO_RENDICONTAZIONI_ACQUISIZIONE_FLUSSI_KO = "rendicontazioni.acquisizioneFlussiKo";
	public static final String MSG_DIAGNOSTICO_RENDICONTAZIONI_ACQUISIZIONE_FLUSSI_FAIL = "rendicontazioni.acquisizioneFlussiFail";
	public static final String MSG_DIAGNOSTICO_RENDICONTAZIONI_ACQUISIZIONE_FLUSSI = "rendicontazioni.acquisizioneFlussi";
	
	public static final String PROPERTY_COD_CANALE = "codCanale";
	public static final String PROPERTY_COD_PSP = "codPsp";
	public static final String PROPERTY_IUV = "iuv";
	public static final String PROPERTY_CCP = "ccp";
	public static final String PROPERTY_COD_DOMINIO = "codDominio";
	public static final String PROPERTY_ESITO_PAGAMENTO = "esitoPagamento";
	public static final String PROPERTY_TIPO_NOTIFICA = "tipoNotifica";
	public static final String PROPERTY_COD_CARRELLO = "codCarrello";
	public static final String PROPERTY_EMAIL_INDIRIZZO = "emailIndirizzo";
	public static final String PROPERTY_FILE_SYSTEM_PATH = "fileSystemPath";
	public static final String PROPERTY_WEB_SERVICE_URL = "webServiceUrl";
	public static final String PROPERTY_REST_URL = "restUrl";
	public static final String PROPERTY_TIPO_TRACCIATO = "tipoTracciato";
	public static final String PROPERTY_NAME_FILE_CONTENUTO = "fileContenuto";
	public static final String PROPERTY_NAME_CONTENUTO = "contenuto";
	public static final String PROPERTY_COD_STAZIONE = "codStazione";
	public static final String PROPERTY_ID_FLUSSO = "idFlusso";
	public static final String PROPERTY_COD_PSP_SESSION = "codPspSession";
	public static final String PROPERTY_COD_APPLICAZIONE = "codApplicazione";
	public static final String PROPERTY_COD_VERSAMENTO_ENTE = "codVersamentoEnte";
	public static final String PROPERTY_COD_MESSAGGIO_RICEVUTA = "codMessaggioRicevuta";
	public static final String PROPERTY_IMPORTO = "importo";
	public static final String PROPERTY_COD_ESITO_PAGAMENTO = "codEsitoPagamento";
	
}
