package it.govpay.web.endpoint;

import javax.xml.bind.JAXBElement;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import it.gov.pagopa.pagopa_api.pa.pafornode.ObjectFactory;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaGetPaymentReq;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaGetPaymentRes;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaSendRTReq;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaSendRTRes;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaVerifyPaymentNoticeReq;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaVerifyPaymentNoticeRes;
import it.gov.pagopa.pagopa_api.pa.pafornode.StOutcome;

@Endpoint
public class PaForNodeEndpoint {

	private static final String NAMESPACE_URI = "http://pagopa-api.pagopa.gov.it/pa/paForNode.xsd";


	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "paVerifyPaymentNoticeReq")
	@ResponsePayload
	public JAXBElement<PaVerifyPaymentNoticeRes> paVerifyPaymentNotice(@RequestPayload JAXBElement<PaVerifyPaymentNoticeReq> request) {
		PaVerifyPaymentNoticeRes paVerifyPaymentNoticeRes = new PaVerifyPaymentNoticeRes();

		paVerifyPaymentNoticeRes.setOutcome(StOutcome.OK);

		ObjectFactory objectFactory = new ObjectFactory();
		JAXBElement<PaVerifyPaymentNoticeRes> response = objectFactory.createPaVerifyPaymentNoticeRes(paVerifyPaymentNoticeRes);

		return response;
	}
	
	
	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "paGetPaymentReq")
	@ResponsePayload
	public JAXBElement<PaGetPaymentRes> paGetPayment(@RequestPayload JAXBElement<PaGetPaymentReq> request) {
		PaGetPaymentRes paVerifyPaymentNoticeRes = new PaGetPaymentRes();

		paVerifyPaymentNoticeRes.setOutcome(StOutcome.OK);

		ObjectFactory objectFactory = new ObjectFactory();
		JAXBElement<PaGetPaymentRes> response = objectFactory.createPaGetPaymentRes(paVerifyPaymentNoticeRes);

		return response;
	}
	
	
	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "paSendRTReq")
	@ResponsePayload
	public JAXBElement<PaSendRTRes> paSendRT(@RequestPayload JAXBElement<PaSendRTReq> request) {
		PaSendRTRes paVerifyPaymentNoticeRes = new PaSendRTRes();

		paVerifyPaymentNoticeRes.setOutcome(StOutcome.OK);

		ObjectFactory objectFactory = new ObjectFactory();
		JAXBElement<PaSendRTRes> response = objectFactory.createPaSendRTRes(paVerifyPaymentNoticeRes);

		return response;
	}
	
	
}
