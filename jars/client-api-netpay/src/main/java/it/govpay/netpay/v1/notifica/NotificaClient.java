package it.govpay.netpay.v1.notifica;

import java.util.Date;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBException;

import org.apache.commons.codec.binary.Base64;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.xml.sax.SAXException;

import it.gov.digitpa.schemas._2011.pagamenti.CtRicevutaTelematica;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaSendRTReq;
import it.govpay.core.beans.EventoContext;
import it.govpay.core.beans.EventoContext.Categoria;
import it.govpay.core.beans.EventoContext.Componente;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.utils.SimpleDateFormatUtils;
import it.govpay.core.utils.client.INotificaClient;
import it.govpay.core.utils.client.exception.ClientException;
import it.govpay.model.Connettore;
import it.govpay.model.Evento.RuoloEvento;
import it.govpay.model.Notifica;
import it.govpay.model.Rpt;
import it.govpay.model.configurazione.Giornale;
import it.govpay.netpay.v1.api.NotificaRicevuteApi;
import it.govpay.netpay.v1.api.impl.ApiClient;
import it.govpay.netpay.v1.client.BasicClient;
import it.govpay.netpay.v1.model.ErrorReason;
import it.govpay.netpay.v1.model.ErrorReasonAgID;
import it.govpay.netpay.v1.model.RegisterPaymentNotify;
import it.govpay.netpay.v1.model.RegisterPaymentNotify.PayStatusEnum;
import it.govpay.netpay.v1.model.RegisterPaymentReceipt;
import it.govpay.netpay.v1.model.RegisterPaymentResponse;
import it.govpay.netpay.v1.model.RegisterPaymentResponse.ResultEnum;
import it.govpay.pagopa.beans.utils.JaxbUtils;

public class NotificaClient extends BasicClient implements INotificaClient {

	private static final String NOTIFICHE_PENDENZE_NETPAY_NOTIFICA_RICEVUTA_OPERATION_ID = "registerPayment";

	private ApiClient apiClient;
	private NotificaRicevuteApi notificaRicevuteApi;
	private Rpt rpt;

	private String valoreHeaderXCompany;
	private String valoreHeaderXRole;

	public NotificaClient(String codApplicazione, Rpt rpt, Connettore connettore, String tipoConnettore, Componente componente, Giornale giornale, boolean logEvento) {
		this.codApplicazione = codApplicazione;
		this.connettore = connettore;
		this.tipoConnettore = tipoConnettore;
		this.giornale = giornale;
		this.componente = componente;
		this.logEvento = logEvento;
		this.rpt = rpt;

		this.eventoCtx = new EventoContext();
		this.getEventoCtx().setCategoriaEvento(Categoria.INTERFACCIA);
		this.getEventoCtx().setRole(RuoloEvento.CLIENT);
		this.getEventoCtx().setDataRichiesta(new Date());
		this.getEventoCtx().setComponente(this.componente); 
		this.getEventoCtx().setPrincipal(this.connettore.getHttpUser());
		this.getEventoCtx().setUrl(this.connettore.getUrl()); 
		this.getEventoCtx().setTipoEvento(NOTIFICHE_PENDENZE_NETPAY_NOTIFICA_RICEVUTA_OPERATION_ID);

		this.valoreHeaderXCompany = this.connettore.getHeaders().stream().filter(e -> e.getName().equalsIgnoreCase("X-COMPANY")).collect(Collectors.toList()).get(0).getValue();
		this.valoreHeaderXRole = this.connettore.getHeaders().stream().filter(e -> e.getName().equalsIgnoreCase("X-ROLE")).collect(Collectors.toList()).get(0).getValue();

		// configurazione del client verso Net@Pay
		this.apiClient = new ApiClient();
		this.apiClient.setUsername(this.connettore.getHttpUser());
		this.apiClient.setPassword(this.connettore.getHttpPassw());
		this.apiClient.setBasePath(this.connettore.getUrl());

		this.notificaRicevuteApi = new NotificaRicevuteApi(this.apiClient);
	}

	@Override
	public byte[] invoke(Notifica notifica) throws ClientException, GovPayException {
		int responseCode = 0;

		String codDominio = rpt.getCodDominio();
		String iuv = rpt.getIuv();
		String ccp = rpt.getCcp();

		this.getEventoCtx().setCodDominio(codDominio);
		this.getEventoCtx().setIuv(iuv);
		this.getEventoCtx().setCcp(ccp);

		log.debug("Spedisco la notifica di " + notifica.getTipo() + " PAGAMENTO della transazione (" + codDominio + ")(" + iuv + ")(" + ccp + ") col connettore versione (" + this.versione.toString() + ") alla URL ("+this.connettore.getUrl()+")");

		RegisterPaymentNotify registerPaymentNotify = new RegisterPaymentNotify();

		registerPaymentNotify.setContextId(ccp);
		registerPaymentNotify.setCreditorTxId(iuv);
		registerPaymentNotify.setDomainId(codDominio);
		registerPaymentNotify.setPayStatus(PayStatusEnum.EXECUTED);

		registerPaymentNotify.setPspId(this.rpt.getCodPsp());
		RegisterPaymentReceipt recepit = new RegisterPaymentReceipt();

		registerPaymentNotify.setReceipt(recepit);

		byte[] dumpRequest = getDumpRequest(registerPaymentNotify);
		byte[] dumpResponse = null;
		HttpHeaders responseHeaders = new HttpHeaders();
		HttpHeaders requestHeaders = new HttpHeaders();

		try {
			registerPaymentNotify.setReceiptXML(Base64.encodeBase64String(rpt.getXmlRt()));
			switch (this.rpt.getVersione()) {
			case SANP_230:
				CtRicevutaTelematica ctRt = JaxbUtils.toRT(rpt.getXmlRt(), false);
				recepit.setAmountPaid(ctRt.getDatiPagamento().getImportoTotalePagato());
				recepit.setBankExecDate(SimpleDateFormatUtils.toLocalDatetime(ctRt.getDataOraMessaggioRicevuta()));
				recepit.setBankResultCode(rpt.getEsitoPagamento().getCodifica() + "");
				recepit.setBankResultMessage(rpt.getDescrizioneStato());
				recepit.setBankTXRefNum(rpt.getCodMsgRicevuta());
				break;
			case SANP_240:
			default:
				PaSendRTReq paSendRTReq_RT = JaxbUtils.toPaSendRTReq_RT(rpt.getXmlRt(), false);
				recepit.setAmountPaid(paSendRTReq_RT.getReceipt().getPaymentAmount());
				recepit.setBankExecDate(SimpleDateFormatUtils.toLocalDatetime(paSendRTReq_RT.getReceipt().getPaymentDateTime()));
				recepit.setBankResultCode(rpt.getEsitoPagamento().getCodifica() + "");
				recepit.setBankResultMessage(rpt.getDescrizioneStato());
				recepit.setBankTXRefNum(rpt.getCodMsgRicevuta());
				break;
			}

			requestHeaders.add("x-company", this.valoreHeaderXCompany);
			requestHeaders.add("x-role", this.valoreHeaderXCompany);
			requestHeaders.add("Accept", "application/json");
			requestHeaders.add("Content-Type", "application/json");

			ResponseEntity<RegisterPaymentResponse> responseEntity = this.notificaRicevuteApi.registerPaymentWithHttpInfo(this.valoreHeaderXCompany, this.valoreHeaderXRole, registerPaymentNotify).block();

			RegisterPaymentResponse registerPaymentResponse = responseEntity.getBody();
			responseCode = responseEntity.getStatusCodeValue();
			responseHeaders = responseEntity.getHeaders();

			ResultEnum result = registerPaymentResponse.getResult();

			dumpResponse = getDumpResponse(registerPaymentResponse);

			switch (result) {
			case OK:
				return new byte[0]; // la risposta e' sempre vuota
			case KO:
			default:
			{ // Gestione dell'eccezione restituita
				ErrorReason errorReason = registerPaymentResponse.getErrorReason();
				ErrorReasonAgID errorReasonAgID = registerPaymentResponse.getErrorReasonAgID();
				String errorMessage = registerPaymentResponse.getErrorMessage();
				log.warn("Spedizione della notifica di " + notifica.getTipo() + " PAGAMENTO della transazione (" + codDominio + ")(" + iuv + ")(" + ccp + ") conclusa con esito KO: [ErrorRease: "+errorReason+", ErrorReasonAgID: "+errorReasonAgID+", ErrorMessage: "+errorMessage+"]");

				throw new ClientException(errorMessage, responseCode, dumpResponse);
			}
			}
		}catch (WebClientResponseException e) {
			// dump risposta per log nel giornale degli eventi
			dumpResponse = e.getResponseBodyAsByteArray();
			responseCode = e.getRawStatusCode();
			responseHeaders = e.getHeaders();

			log.warn("Spedizione della notifica di " + notifica.getTipo() + " PAGAMENTO della transazione (" + codDominio + ")(" + iuv + ")(" + ccp + ") conclusa con errore: ", e.getMessage(),e);
			throw new ClientException(e);
		} catch (JAXBException | SAXException e) {
			responseCode = 500;
			log.error("Spedizione della notifica di " + notifica.getTipo() + " PAGAMENTO della transazione (" + codDominio + ")(" + iuv + ")(" + ccp + ") conclusa con errore: ", e.getMessage(),e);
			throw new GovPayException(e);
		} finally {
			this.popolaContextEvento(responseCode, dumpRequest, dumpResponse, requestHeaders, responseHeaders);
		}
	}

	@Override
	public EventoContext getEventoCtx() {
		return super.getEventoCtx();
	}

}
