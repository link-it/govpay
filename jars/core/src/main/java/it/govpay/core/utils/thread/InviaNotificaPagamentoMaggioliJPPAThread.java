package it.govpay.core.utils.thread;

import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.bind.JAXBElement;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.UtilsException;
import org.openspcoop2.utils.logger.beans.Property;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.openspcoop2.utils.service.context.IContext;
import org.openspcoop2.utils.service.context.MD5Constants;
import org.slf4j.Logger;
import org.slf4j.MDC;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.configurazione.model.Giornale;
import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.PagamentoPortale;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.Versamento;
import it.govpay.bd.pagamento.EventiBD;
import it.govpay.core.beans.EsitoOperazione;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.utils.EventoContext.Esito;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.MaggioliJPPAUtils;
import it.govpay.core.utils.client.MaggioliJPPAClient;
import it.govpay.core.utils.client.MaggioliJPPAClient.Azione;
import it.govpay.core.utils.client.exception.ClientException;
import it.govpay.model.ConnettoreNotificaPagamenti;
import it.maggioli.informatica.jcitygov.pagopa.payservice.pdp.connector.jppapdp.internal.CtRichiestaStandard;
import it.maggioli.informatica.jcitygov.pagopa.payservice.pdp.connector.jppapdp.internal.CtRispostaStandard;
import it.maggioli.informatica.jcitygov.pagopa.payservice.pdp.connector.jppapdp.internal.StEsito;
import it.maggioli.informatica.jcitygov.pagopa.payservice.pdp.connector.jppapdp.internal.StOperazione;
import it.maggioli.informatica.jcitygov.pagopa.payservice.pdp.connector.jppapdp.internal.schema._1_0.InviaEsitoPagamentoRichiesta;
import it.maggioli.informatica.jcitygov.pagopa.payservice.pdp.connector.jppapdp.internal.schema._1_0.ObjectFactory;

public class InviaNotificaPagamentoMaggioliJPPAThread implements Runnable {
	
	// root element elemento di input
	public static final String INVIA_ESITO_PAGAMENTO_RICHIESTA_ROOT_ELEMENT_NAME = "InviaEsitoPagamentoRichiesta"; 
	public static final String CDATA_TOKEN_START = "<![CDATA["; 
	public static final String CDATA_TOKEN_END = "]]>"; 

	private Rpt rpt;
	private static Logger log = LoggerWrapperFactory.getLogger(InviaRptThread.class);
	private IContext ctx = null;
	private Giornale giornale;
	private Dominio dominio;
	private Versamento versamento = null;
	private Applicazione applicazione = null;
	private PagamentoPortale pagamentoPortale = null;
	private String esito = null;
	private String descrizioneEsito = null;
	private boolean completed = false;
	private boolean commit = false;
	private String nomeThread = "";
	private Exception exception = null;
	ObjectFactory objectFactory = null;

	public InviaNotificaPagamentoMaggioliJPPAThread(Rpt rpt, Dominio dominio, String id, IContext ctx) throws ServiceException {
		this.ctx = ctx;
		BDConfigWrapper configWrapper = new BDConfigWrapper(this.ctx.getTransactionId(), true);
		this.rpt = rpt;
		this.versamento = this.rpt.getVersamento(configWrapper);
		this.applicazione = this.versamento.getApplicazione(configWrapper);
		this.giornale = new it.govpay.core.business.Configurazione().getConfigurazione().getGiornale();
		this.pagamentoPortale = this.rpt.getPagamentoPortale(configWrapper);
		this.dominio = dominio;
		this.nomeThread = id;
		this.objectFactory = new ObjectFactory();
	}


	@Override
	public void run() {
		ContextThreadLocal.set(this.ctx);
		//		BasicBD bd = null;
		IContext ctx = ContextThreadLocal.get();
		GpContext appContext = (GpContext) ctx.getApplicationContext();
		MDC.put(MD5Constants.TRANSACTION_ID, ctx.getTransactionId());
		MaggioliJPPAClient client = null;
		BDConfigWrapper configWrapper = new BDConfigWrapper(this.ctx.getTransactionId(), true);
		//		RptBD rptBD = null;
		ConnettoreNotificaPagamenti connettoreMaggioliJPPA = dominio.getConnettoreMaggioliJPPA();
		String rptKey = this.rpt.getCodDominio() + "@" + this.rpt.getIuv() + "@" + this.rpt.getCcp();
		try {
			String operationId = appContext.setupInvioNotificaPagamentiMaggioliJPPAClient(Azione.InviaEsitoPagamento.toString(), connettoreMaggioliJPPA.getUrl());
			log.info("Id Server: [" + operationId + "]");
			log.info("Spedizione Notifica Pagamento Maggioli [RPT: " + rptKey + "]");

			appContext.getServerByOperationId(operationId).addGenericProperty(new Property("codDominio", this.rpt.getCodDominio()));
			appContext.getServerByOperationId(operationId).addGenericProperty(new Property("iuv", this.rpt.getIuv()));
			appContext.getServerByOperationId(operationId).addGenericProperty(new Property("ccp", this.rpt.getCcp()));

			ctx.getApplicationLogger().log("jppapdp.invioNotificaPagamento");

			client = new it.govpay.core.utils.client.MaggioliJPPAClient(this.dominio,connettoreMaggioliJPPA, operationId, this.giornale);
			// salvataggio id Rpt/ versamento/ pagamento
			client.getEventoCtx().setCodDominio(this.rpt.getCodDominio());
			client.getEventoCtx().setIuv(this.rpt.getIuv());
			client.getEventoCtx().setCcp(this.rpt.getCcp());
			client.getEventoCtx().setIdA2A(this.applicazione.getCodApplicazione());
			client.getEventoCtx().setIdPendenza(this.versamento.getCodVersamentoEnte());
			if(this.pagamentoPortale != null)
				client.getEventoCtx().setIdPagamento(this.pagamentoPortale.getIdSessione());

			CtRichiestaStandard richiestaStandard = new CtRichiestaStandard();
			
			InviaEsitoPagamentoRichiesta inviaEsitoPagamentoRichiesta = this.objectFactory.createInviaEsitoPagamentoRichiesta();
			inviaEsitoPagamentoRichiesta.setIdentificativoUnivocoVersamento(this.rpt.getIuv());
			inviaEsitoPagamentoRichiesta.setCodiceContestoPagamento(this.rpt.getCcp());
			
			richiestaStandard.setOperazione(StOperazione.INVIA_ESITO_PAGAMENTO);
			richiestaStandard.setIdentificativoDominio(this.rpt.getCodDominio());
			XMLGregorianCalendar dataOperazione = MaggioliJPPAUtils.impostaDataOperazione(this.rpt.getDataMsgRicevuta());
			richiestaStandard.setDataOperazione(dataOperazione );
			richiestaStandard.setVersion("1.0");
			switch (this.rpt.getEsitoPagamento()) {
			case PAGAMENTO_ESEGUITO:
				inviaEsitoPagamentoRichiesta.setEsitoPagamento("ESEGUITO");
				break;
			case PAGAMENTO_PARZIALMENTE_ESEGUITO:
				inviaEsitoPagamentoRichiesta.setEsitoPagamento("ESEGUITO_PARZIALMENTE");
				break;
			default:
				log.warn("RPT in stato "+this.rpt.getEsitoPagamento()+" non valido per la spedizione");
				
				this.esito = StEsito.ERROR.toString();
				this.descrizioneEsito = "RPT in stato "+ this.rpt.getEsitoPagamento()+" non valido per la spedizione";
				client.getEventoCtx().setEsito(Esito.KO);
				client.getEventoCtx().setDescrizioneEsito(this.descrizioneEsito);
				ctx.getApplicationLogger().log("jppapdp.invioNotificaPagamentoStatoRptNonValido", this.rpt.getEsitoPagamento());
				return;
			}
			
			JAXBElement<InviaEsitoPagamentoRichiesta> jaxbElement = new JAXBElement<InviaEsitoPagamentoRichiesta>(new QName("", INVIA_ESITO_PAGAMENTO_RICHIESTA_ROOT_ELEMENT_NAME), 
					InviaEsitoPagamentoRichiesta.class, null, inviaEsitoPagamentoRichiesta);
			
			String xmlDettaglioRichiesta = MaggioliJPPAUtils.getBodyAsString(false, jaxbElement, null);
			richiestaStandard.setXmlDettaglioRichiesta(CDATA_TOKEN_START + xmlDettaglioRichiesta + CDATA_TOKEN_END);
			CtRispostaStandard rispostaStandard = client.maggioliJPPAInviaEsitoPagamentoRichiesta(richiestaStandard);

			this.esito = rispostaStandard.getEsito().toString();

			log.info("Notifica Pagamento Maggioli inviata correttamente");
			ctx.getApplicationLogger().log("jppapdp.invioNotificaPagamentoOk");
			client.getEventoCtx().setEsito(Esito.OK);

		}  catch (ClientException e) {
			log.error("Errore nella spedizione della Notifica Pagamento", e);
			if(client != null) {
				client.getEventoCtx().setSottotipoEsito(e.getResponseCode() + "");
				client.getEventoCtx().setEsito(Esito.FAIL);
				client.getEventoCtx().setDescrizioneEsito(e.getMessage());
			}	
			try {
				ctx.getApplicationLogger().log("jppapdp.invioNotificaPagamentoFail", e.getMessage());
			} catch (UtilsException e1) {
				log.error("Errore durante il log dell'operazione: " + e.getMessage(), e);
			}

			this.esito = StEsito.ERROR.toString();
			this.descrizioneEsito = e.getMessage();
			this.exception = e;
		} catch (GovPayException | UtilsException | DatatypeConfigurationException e) {
			log.error("Errore nella spedizione della Notifica Pagamento", e);
			if(client != null) {
				if(e instanceof GovPayException) {
					client.getEventoCtx().setSottotipoEsito(((GovPayException)e).getCodEsito().toString());
				} else {
					client.getEventoCtx().setSottotipoEsito(EsitoOperazione.INTERNAL.toString());
				}
				client.getEventoCtx().setEsito(Esito.FAIL);
				client.getEventoCtx().setDescrizioneEsito(e.getMessage());
				client.getEventoCtx().setException(e);
			}	
			try {
				ctx.getApplicationLogger().log("jppapdp.invioNotificaPagamentoFail", e.getMessage());
			} catch (UtilsException e1) {
				log.error("Errore durante il log dell'operazione: " + e.getMessage(), e);
			}
			this.esito = StEsito.ERROR.toString();
			this.descrizioneEsito = e.getMessage();
			this.exception = e;
		} finally {
			if(client != null && client.getEventoCtx().isRegistraEvento()) {
				try {
					EventiBD eventiBD = new EventiBD(configWrapper);
					eventiBD.insertEvento(client.getEventoCtx().toEventoDTO());

				} catch (ServiceException e) {
					log.error("Errore: " + e.getMessage(), e);
				}
			}
			ContextThreadLocal.unset();
			this.completed = true;
		}
	}

	public String getEsito() {
		return esito;
	}
	public void setEsito(String esito) {
		this.esito = esito;
	}
	public String getDescrizioneEsito() {
		return descrizioneEsito;
	}
	public void setDescrizioneEsito(String descrizioneEsito) {
		this.descrizioneEsito = descrizioneEsito;
	}
	public boolean isCompleted() {
		return completed;
	}
	public void setCompleted(boolean completed) {
		this.completed = completed;
	}
	public boolean isCommit() {
		return commit;
	}
	public void setCommit(boolean commit) {
		this.commit = commit;
	}
	public String getNomeThread() {
		return nomeThread;
	}
	public Exception getException() {
		return exception;
	}
	public Rpt getRpt() {
		return rpt;
	}
}
