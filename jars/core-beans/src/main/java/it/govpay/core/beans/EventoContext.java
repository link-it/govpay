/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2025 Link.it srl (http://www.link.it).
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
package it.govpay.core.beans;

import java.io.Serializable;
import java.util.Date;

import it.govpay.model.Evento.RuoloEvento;
import it.govpay.model.eventi.DatiPagoPA;
import it.govpay.model.eventi.DettaglioRichiesta;
import it.govpay.model.eventi.DettaglioRisposta;

public class EventoContext implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public static final String APIPAGOPA_TIPOEVENTO_PAAVERIFICARPT = "paaVerificaRPT";
	public static final String APIPAGOPA_TIPOEVENTO_PAAATTIVARPT = "paaAttivaRPT";
	public static final String APIPAGOPA_TIPOEVENTO_PAAINVIAESITOSTORNO = "paaInviaEsitoStorno";
	public static final String APIPAGOPA_TIPOEVENTO_PAAINVIARICHIESTAREVOCA = "paaInviaRichiestaRevoca";
	public static final String APIPAGOPA_TIPOEVENTO_PAAINVIART = "paaInviaRT";
	public static final String APIPAGOPA_TIPOEVENTO_PASENDRT = "paSendRT";
	public static final String APIPAGOPA_TIPOEVENTO_PAVERIFYPAYMENTNOTICE = "paVerifyPaymentNotice";
	public static final String APIPAGOPA_TIPOEVENTO_PAGETPAYMENT = "paGetPayment";
	
	public static final String APIPAGOPA_TIPOEVENTO_GETORGANIZATIONRECEIPTIUR = "getOrganizationReceiptIur";
	public static final String APIPAGOPA_TIPOEVENTO_GETORGANIZATIONRECEIPTIUVIUR = "getOrganizationReceiptIuvIur";
	public static final String APIPAGOPA_TIPOEVENTO_HEALTHCHECK = "healthCheck";

	public static final String APIMYPIVOT_TIPOEVENTO_MYPIVOTINVIATRACCIATOEMAIL = "pivotInviaTracciatoEmail";
	public static final String APIMYPIVOT_TIPOEVENTO_MYPIVOTINVIATRACCIATOFILESYSTEM = "pivotInviaTracciatoFileSystem";
	public static final String APIMYPIVOT_TIPOEVENTO_PIVOTSILAUTORIZZAIMPORTFLUSSO = "pivotSILAutorizzaImportFlusso";
	public static final String APIMYPIVOT_TIPOEVENTO_PIVOTSILCHIEDISTATOIMPORTFLUSSO = "pivotSILChiediStatoImportFlusso";
	public static final String APIMYPIVOT_TIPOEVENTO_PIVOTSILINVIAFLUSSO = "pivotSILInviaFlusso";

	public static final String APISECIM_TIPOEVENTO_SECIMINVIATRACCIATOEMAIL = "secimInviaTracciatoEmail";
	public static final String APISECIM_TIPOEVENTO_SECIMINVIATRACCIATOFILESYSTEM = "secimInviaTracciatoFileSystem";

	public static final String APIGOVPAY_TIPOEVENTO_GOVPAYINVIATRACCIATOEMAIL = "govpayInviaTracciatoEmail";
	public static final String APIGOVPAY_TIPOEVENTO_GOVPAYINVIATRACCIATOFILESYSTEM = "govpayInviaTracciatoFileSystem";
	public static final String APIGOVPAY_TIPOEVENTO_GOVPAYINVIATRACCIATOREST = "govpayInviaTracciatoRest";

	public static final String APIHYPERSICAPKAPPA_TIPOEVENTO_HYPERSIC_APKINVIATRACCIATOEMAIL = "hyperSicAPKappaInviaTracciatoEmail";
	public static final String APIHYPERSICAPKAPPA_TIPOEVENTO_HYPERSIC_APKINVIATRACCIATOFILESYSTEM = "hyperSicAPKappaInviaTracciatoFileSystem";

	public static final String APIPAGOPA_SOTTOTIPOEVENTO_FLUSSO_RENDICONTAZIONE_DUPLICATO = "FlussoRendicontazioneDuplicato";

	public static final String APIMAGGIOLI_JPPA_TIPOEVENTO_INVIAESITOPAGAMENTO = "maggioliInviaEsitoPagamento";
	public static final String APIMAGGIOLI_JPPA_TIPOEVENTO_RECUPERART = "maggioliRecuperaRT";
	public static final String APIMAGGIOLI_JPPA_TIPOEVENTO_INVIATRACCIATOEMAIL = "maggioliJppaInviaTracciatoEmail";

	public static final String GOVPAY_TIPOEVENTO_GOVPAYPAGAMENTOESEGUITOSENZARPT = "govpayPagamentoEseguitoSenzaRPT";

	public static final String SOTTOTIPO_EVENTO_NOTA = "nota";

	public enum Componente {API_ENTE, API_PAGAMENTO, API_RAGIONERIA, API_BACKOFFICE, API_PAGOPA, API_PENDENZE, API_WC, API_USER, API_BACKEND_IO, API_MYPIVOT, API_SECIM, API_GOVPAY, API_HYPERSIC_APK, API_MAGGIOLI_JPPA, GOVPAY }
	public enum Esito {OK, KO, FAIL}
	public enum Categoria { INTERFACCIA, INTERNO, UTENTE }

	public enum Azione {
		NODOINVIARPT("nodoInviaRPT"), 
		NODOINVIACARRELLORPT("nodoInviaCarrelloRPT"), 
		NODOCHIEDISTATORPT("nodoChiediStatoRPT"), 
		NODOCHIEDICOPIART("nodoChiediCopiaRT"), 
		NODOCHIEDILISTAPENDENTIRPT("nodoChiediListaPendentiRPT"), 
		NODOINVIARICHIESTASTORNO("nodoInviaRichiestaStorno"), 
		NODOINVIARISPOSTAREVOCA("nodoInviaRispostaRevoca"), 
		NODOCHIEDIELENCOFLUSSIRENDICONTAZIONE("nodoChiediElencoFlussiRendicontazione"), 
		NODOCHIEDIFLUSSORENDICONTAZIONE("nodoChiediFlussoRendicontazione");

		private String value;

		Azione(String value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return String.valueOf(value);
		}

		public static Azione fromValue(String text) {
			for (Azione b : Azione.values()) {
				if (String.valueOf(b.value).equals(text)) {
					return b;
				}
			}
			return null;
		}
	}

	public enum Azione_Ente_Rendicontazioni {

		INVIAFLUSSORENDICONTAZIONE("inviaFlussoRendicontazione"), 
		INVIARPP("inviaRpp"), 
		INVIASINTESIFLUSSIRENDICONTAZIONE("inviaSintesiFlussiRendicontazione"), 
		INVIASINTESIPAGAMENTI("inviaSintesiPagamenti");
		
		private String value;

		Azione_Ente_Rendicontazioni(String value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return String.valueOf(value);
		}

		public static Azione_Ente_Rendicontazioni fromValue(String text) {
			for (Azione_Ente_Rendicontazioni b : Azione_Ente_Rendicontazioni.values()) {
				if (String.valueOf(b.value).equals(text)) {
					return b;
				}
			}
			return null;
		}
	}

	private boolean registraEvento = false;
	private Componente componente;
	private RuoloEvento role;
	private Categoria categoriaEvento;
	private String tipoEvento;
	private String sottotipoEvento;
	private Esito esito;
	private String sottotipoEsito;
	private String descrizioneEsito;

	private String principal;
	private String utente;
	private Date dataRichiesta;
	private Date dataRisposta;

	private String method;
	private String url;

	private Integer status;

	private DettaglioRichiesta dettaglioRichiesta;
	private DettaglioRisposta dettaglioRisposta;
	private DatiPagoPA datiPagoPA;

	private String codDominio;
	private String iuv;
	private String ccp;
	private String idA2A;
	private String idPendenza;
	private String idPagamento;


	private String idTransazione;
	private Long id;

	private Long idFr;
	private Long idIncasso;
	private Long idTracciato;

	private Throwable exception;
	private Integer severita;

	private String clusterId;
	private String transactionId;

	public EventoContext() {
		this(null);
	}
	
	public EventoContext(Componente componente) {
		this.dataRichiesta = new Date();
		this.componente = componente;
	}

	public Componente getComponente() {
		return componente;
	}

	public void setComponente(Componente componente) {
		this.componente = componente;
	}

	public RuoloEvento getRole() {
		return role;
	}

	public void setRole(RuoloEvento role) {
		this.role = role;
	}

	public Categoria getCategoriaEvento() {
		return categoriaEvento;
	}

	public void setCategoriaEvento(Categoria categoriaEvento) {
		this.categoriaEvento = categoriaEvento;
	}

	public String getTipoEvento() {
		return tipoEvento;
	}

	public void setTipoEvento(String tipoEvento) {
		this.tipoEvento = tipoEvento;
	}

	public String getSottotipoEvento() {
		return sottotipoEvento;
	}

	public void setSottotipoEvento(String sottotipoEvento) {
		this.sottotipoEvento = sottotipoEvento;
	}

	public Esito getEsito() {
		return esito;
	}

	public void setEsito(Esito esito) {
		this.esito = esito;
	}

	public String getPrincipal() {
		return principal;
	}

	public void setPrincipal(String principal) {
		this.principal = principal;
	}

	public String getUtente() {
		return utente;
	}

	public void setUtente(String utente) {
		this.utente = utente;
	}

	public Date getDataRichiesta() {
		return dataRichiesta;
	}

	public void setDataRichiesta(Date dataRichiesta) {
		this.dataRichiesta = dataRichiesta;
	}

	public Date getDataRisposta() {
		return dataRisposta;
	}

	public void setDataRisposta(Date dataRisposta) {
		this.dataRisposta = dataRisposta;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public DettaglioRichiesta getDettaglioRichiesta() {
		return dettaglioRichiesta;
	}

	public void setDettaglioRichiesta(DettaglioRichiesta dettaglioRichiesta) {
		this.dettaglioRichiesta = dettaglioRichiesta;
	}

	public DettaglioRisposta getDettaglioRisposta() {
		return dettaglioRisposta;
	}

	public void setDettaglioRisposta(DettaglioRisposta dettaglioRisposta) {
		this.dettaglioRisposta = dettaglioRisposta;
	}
	public String getDescrizioneEsito() {
		return descrizioneEsito;
	}

	public void setDescrizioneEsito(String descrizioneEsito) {
		this.descrizioneEsito = descrizioneEsito;
	}

	public String getCodDominio() {
		return codDominio;
	}

	public void setCodDominio(String codDominio) {
		this.codDominio = codDominio;
	}

	public String getIuv() {
		return iuv;
	}

	public void setIuv(String iuv) {
		this.iuv = iuv;
	}

	public String getCcp() {
		return ccp;
	}

	public void setCcp(String ccp) {
		this.ccp = ccp;
	}

	public String getIdA2A() {
		return idA2A;
	}

	public void setIdA2A(String idA2A) {
		this.idA2A = idA2A;
	}

	public String getIdPendenza() {
		return idPendenza;
	}

	public void setIdPendenza(String idPendenza) {
		this.idPendenza = idPendenza;
	}

	public DatiPagoPA getDatiPagoPA() {
		return datiPagoPA;
	}

	public void setDatiPagoPA(DatiPagoPA datiPagoPA) {
		this.datiPagoPA = datiPagoPA;
	}

	public String getIdTransazione() {
		return idTransazione;
	}

	public void setIdTransazione(String idTransazione) {
		this.idTransazione = idTransazione;
	}

	public boolean isRegistraEvento() {
		return registraEvento;
	}

	public void setRegistraEvento(boolean registraEvento) {
		this.registraEvento = registraEvento;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIdPagamento() {
		return idPagamento;
	}

	public void setIdPagamento(String idPagamento) {
		this.idPagamento = idPagamento;
	}

	public String getSottotipoEsito() {
		return sottotipoEsito;
	}

	public void setSottotipoEsito(String sottotipoEsito) {
		this.sottotipoEsito = sottotipoEsito;
	}

	public Long getIdFr() {
		return idFr;
	}

	public void setIdFr(Long idFr) {
		this.idFr = idFr;
	}

	public Long getIdIncasso() {
		return idIncasso;
	}

	public void setIdIncasso(Long idIncasso) {
		this.idIncasso = idIncasso;
	}

	public Long getIdTracciato() {
		return idTracciato;
	}

	public void setIdTracciato(Long idTracciato) {
		this.idTracciato = idTracciato;
	}

	public Throwable getException() {
		return exception;
	}

	public void setException(Throwable exception) {
		this.exception = exception;
	}

	public Integer getSeverita() {
		return severita;
	}

	public void setSeverita(Integer severita) {
		this.severita = severita;
	}

	public String getClusterId() {
		return clusterId;
	}

	public void setClusterId(String clusterId) {
		this.clusterId = clusterId;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

}
