package it.govpay.netpay.v1.verifica;

import java.math.BigDecimal;
import java.util.Date;
import java.util.stream.Collectors;

import org.apache.commons.lang.NotImplementedException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import it.govpay.core.beans.EventoContext;
import it.govpay.core.beans.EventoContext.Categoria;
import it.govpay.core.beans.EventoContext.Componente;
import it.govpay.core.beans.commons.Versamento;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.exceptions.VersamentoAnnullatoException;
import it.govpay.core.exceptions.VersamentoDuplicatoException;
import it.govpay.core.exceptions.VersamentoNonValidoException;
import it.govpay.core.exceptions.VersamentoScadutoException;
import it.govpay.core.exceptions.VersamentoSconosciutoException;
import it.govpay.core.utils.client.IVerificaClient;
import it.govpay.core.utils.client.exception.ClientException;
import it.govpay.model.Connettore;
import it.govpay.model.Dominio;
import it.govpay.model.Evento.RuoloEvento;
import it.govpay.model.Stazione;
import it.govpay.model.configurazione.Giornale;
import it.govpay.netpay.v1.api.VerificaPendenzeApi;
import it.govpay.netpay.v1.api.impl.ApiClient;
import it.govpay.netpay.v1.client.BasicClient;
import it.govpay.netpay.v1.model.CheckPaymentNotify;
import it.govpay.netpay.v1.model.CheckPaymentNotify.CheckPaymentTypeEnum;
import it.govpay.netpay.v1.model.CheckPaymentResponse;
import it.govpay.netpay.v1.model.CheckPaymentResponse.ResultEnum;
import it.govpay.netpay.v1.model.ErrorReason;
import it.govpay.netpay.v1.model.ErrorReasonAgID;
import it.govpay.netpay.v1.verifica.converter.VersamentoConverter;

public class VerificaClient extends BasicClient implements IVerificaClient {
	
	private static final String VERIFICA_PENDENZE_NETPAY_VERIFY_PENDENZA_OPERATION_ID = "checkPayment";

	private ApiClient apiClient;
	private VerificaPendenzeApi verificaPendenzeApi;
	
	private String valoreHeaderXCompany;
	private String valoreHeaderXRole;
	private Stazione stazione;
	private Dominio dominio;
	

	public VerificaClient(String codApplicazione, Connettore connettore, String tipoConnettore, Componente componente, Giornale giornale, Dominio dominio, Stazione stazione, boolean logEvento) {
		this.codApplicazione = codApplicazione;
		this.connettore = connettore;
		this.tipoConnettore = tipoConnettore;
		this.giornale = giornale;
		this.componente = componente;
		this.logEvento = logEvento;

		this.eventoCtx = new EventoContext();
		this.getEventoCtx().setCategoriaEvento(Categoria.INTERFACCIA);
		this.getEventoCtx().setRole(RuoloEvento.CLIENT);
		this.getEventoCtx().setDataRichiesta(new Date());
		this.getEventoCtx().setComponente(this.componente); 
		this.getEventoCtx().setPrincipal(this.connettore.getHttpUser());
		this.getEventoCtx().setUrl(this.connettore.getUrl()); 
		this.getEventoCtx().setTipoEvento(VERIFICA_PENDENZE_NETPAY_VERIFY_PENDENZA_OPERATION_ID);

		this.dominio = dominio;
		this.stazione = stazione;
		
		this.valoreHeaderXCompany = this.connettore.getHeaders().stream().filter(e -> e.getName().equalsIgnoreCase("X-COMPANY")).collect(Collectors.toList()).get(0).getValue();
		this.valoreHeaderXRole = this.connettore.getHeaders().stream().filter(e -> e.getName().equalsIgnoreCase("X-ROLE")).collect(Collectors.toList()).get(0).getValue();

		// configurazione del client verso Net@Pay
		this.apiClient = new ApiClient();
		this.apiClient.setUsername(this.connettore.getHttpUser());
		this.apiClient.setPassword(this.connettore.getHttpPassw());
		this.apiClient.setBasePath(this.connettore.getUrl());

		this.verificaPendenzeApi = new VerificaPendenzeApi(this.apiClient);
	}

	@Override
	public Versamento verificaPendenza(String codVersamentoEnte, String bundlekey, String codUnivocoDebitore, String codDominio, String iuv, String pspId, String ccp,  BigDecimal importo, Operazione operazione)
					throws ClientException, VersamentoAnnullatoException, VersamentoDuplicatoException, VersamentoScadutoException, VersamentoSconosciutoException, VersamentoNonValidoException, GovPayException {
		int responseCode = 0;
		
		String importoD = importo != null ? importo.toString() : "-";

		log.info("Richiedo la verifica per la pendenza [Applicazione:" + this.codApplicazione + " Dominio:" + codDominio + " IUV:" + iuv + " CCP:" + ccp + " PSP:" + pspId + " Amount:" + importoD + " Operazione:" + operazione + "] in versione (" + this.versione.toString() + ") alla URL ("+this.connettore.getUrl()+")");

		this.getEventoCtx().setCodDominio(codDominio);
		this.getEventoCtx().setIuv(iuv);
		this.getEventoCtx().setCcp(ccp);

		CheckPaymentNotify checkPaymentNotify = new CheckPaymentNotify();
		checkPaymentNotify.setAmountToPay(importo);

		CheckPaymentTypeEnum checkPaymentType = null;
		switch (operazione) {
		case ATTIVA:
			checkPaymentType = CheckPaymentTypeEnum.CHKPATTIVA;
			break;
		case VERIFICA:
			checkPaymentType = CheckPaymentTypeEnum.CHKPVERIFICA;
			break;
		}

		checkPaymentNotify.setCheckPaymentType(checkPaymentType);
		checkPaymentNotify.setContextId(ccp);
		checkPaymentNotify.setCreditorTxId(iuv);
		checkPaymentNotify.setDomainId(codDominio);
		checkPaymentNotify.setPspId(pspId);

		byte[] dumpRequest = getDumpRequest(checkPaymentNotify);
		byte[] dumpResponse = null;
		HttpHeaders responseHeaders = new HttpHeaders();
		HttpHeaders requestHeaders = new HttpHeaders();
		try {
			requestHeaders.add("x-company", this.valoreHeaderXCompany);
			requestHeaders.add("x-role", this.valoreHeaderXCompany);
			requestHeaders.add("Accept", "application/json");
			requestHeaders.add("Content-Type", "application/json");
			
			ResponseEntity<CheckPaymentResponse> responseEntity = this.verificaPendenzeApi.checkPaymentWithHttpInfo(this.valoreHeaderXCompany, this.valoreHeaderXRole, checkPaymentNotify).block();

			CheckPaymentResponse checkPaymentResponse = responseEntity.getBody();
			responseCode = responseEntity.getStatusCodeValue();
			responseHeaders = responseEntity.getHeaders();

			ResultEnum result = checkPaymentResponse.getResult();

			dumpResponse = getDumpResponse(checkPaymentResponse);
			
			switch (result) {
			case OK:
				Versamento versamento =  VersamentoConverter.getVersamentoFromCheckPaymentResponse(checkPaymentResponse, this.codApplicazione, codDominio, iuv, this.dominio.getAuxDigit(), this.stazione.getApplicationCode());

				this.getEventoCtx().setIdA2A(versamento.getCodApplicazione());
				this.getEventoCtx().setIdPendenza(versamento.getCodVersamentoEnte());

				log.info("Verifica per la pendenza [Applicazione:" + this.codApplicazione + " Dominio:" + codDominio + " IUV:" + iuv + " CCP:" + ccp + " PSP:" + pspId + " Amount:" + importoD + " Operazione:" + operazione + "] conclusa con esito OK");

				return versamento;
			case KO:
			default:
			{ // Gestione dell'eccezione restituita
				ErrorReason errorReason = checkPaymentResponse.getErrorReason();
				ErrorReasonAgID errorReasonAgID = checkPaymentResponse.getErrorReasonAgID();
				String errorMessage = checkPaymentResponse.getErrorMessage();
				log.warn("Verifica per la pendenza [Applicazione:" + this.codApplicazione + " Dominio:" + codDominio + " IUV:" + iuv + " CCP:" + ccp + " PSP:" + pspId + " Amount:" + importoD + " Operazione:" + operazione 
						+ "] conclusa con esito KO: [ErrorRease: "+errorReason+", ErrorReasonAgID: "+errorReasonAgID+", ErrorMessage: "+errorMessage+"]");

				switch (errorReason) {
				case PAY_TX_NOT_PAYABLE1:
				case PAY_TX_CANCELED:{
					VersamentoAnnullatoException e = new VersamentoAnnullatoException(this.codApplicazione, "-", "-", "-", codDominio, iuv, errorMessage);
					e.setErrorReasonAgID(errorReasonAgID.name());
					throw e;
				}
				case PAY_TX_PROCESSED:
				case PAY_TX_NOT_PAYABLE2:{
					VersamentoDuplicatoException e = new VersamentoDuplicatoException(this.codApplicazione, "-", "-", "-", codDominio, iuv, errorMessage);
					e.setErrorReasonAgID(errorReasonAgID.name());
					throw e;
				}
				case PAY_TX_IN_PROGRESS:
				case DOMAIN_ID_NOT_VALID:
				case PAY_AMOUNT_NOT_VALID:
				case INVALID_REQUEST:{
					VersamentoNonValidoException e = new VersamentoNonValidoException(this.codApplicazione, "-", "-", "-", codDominio, iuv, errorMessage);
					e.setErrorReasonAgID(errorReasonAgID.name());
					throw e;
				}
				case PAY_TX_NOT_FOUND: 
				case PSP_NOT_MANAGED:{
					VersamentoSconosciutoException e = new VersamentoSconosciutoException(this.codApplicazione, "-", "-", "-", codDominio, iuv, errorMessage);
					e.setErrorReasonAgID(errorReasonAgID.name());
					throw e;
				}
				case PAY_TX_EXPIRED:
				case INTERNAL_ERROR:
				default:{
					VersamentoNonValidoException e = new VersamentoNonValidoException(this.codApplicazione, "-", "-", "-", codDominio, iuv, errorMessage);
					e.setErrorReasonAgID(errorReasonAgID.name());
					throw e;
				}
				}
			}
			}
		}catch (WebClientResponseException e) {
			// dump risposta per log nel giornale degli eventi
			dumpResponse = e.getResponseBodyAsByteArray();
			responseCode = e.getRawStatusCode();
			responseHeaders = e.getHeaders();
			
			log.warn("Verifica per la pendenza [Applicazione:" + this.codApplicazione + " Dominio:" + codDominio + " IUV:" + iuv + " CCP:" + ccp + " PSP:" + pspId + " Amount:" + importoD + " Operazione:" + operazione 
					+ "] conclusa con errore: ", e.getMessage(),e);
			throw new ClientException(e);
		} finally {
			this.popolaContextEvento(responseCode, dumpRequest, dumpResponse, requestHeaders, responseHeaders);
		}
	}


	@Override
	public Versamento inoltroPendenza(String codDominio, String codTipoVersamento, String codUnitaOperativa,
			String jsonBody) throws ClientException, VersamentoAnnullatoException, VersamentoDuplicatoException,
	VersamentoScadutoException, VersamentoSconosciutoException, VersamentoNonValidoException, GovPayException {
		throw new NotImplementedException("Inoltro pendenza non previsto");
	}

	@Override
	public EventoContext getEventoCtx() {
		return super.getEventoCtx();
	}

}
