package it.govpay.backoffice.v1.controllers;



import java.text.MessageFormat;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.springframework.security.core.Authentication;

import it.govpay.backoffice.v1.beans.InfoGovPay;
import it.govpay.core.utils.GovpayConfig; 



public class InfoController extends BaseController {
	
	private String govpayBuildNumber = null;
	private String govpayVersione = null;
	
	public InfoController(String nomeServizio,Logger log) {
		super(nomeServizio,log);
	}

	public Response infoGET(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders ) {
		String methodName = "infoGET";  
		String transactionId = this.context.getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try{
			InfoGovPay info = new InfoGovPay();
			info.setAmbiente(GovpayConfig.getInstance().getAmbienteDeploy());
			info.setAppName(GovpayConfig.getInstance().getAppName());
			info.setBuild(this.govpayBuildNumber);
			info.setVersione(this.govpayVersione);
			
			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(info.toJSON(null)),transactionId).build();
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(this.context);
		}
	}

	public String getGovpayBuildNumber() {
		return govpayBuildNumber;
	}

	public void setGovpayBuildNumber(String govpayBuildNumber) {
		this.govpayBuildNumber = govpayBuildNumber;
	}

	public String getGovpayVersione() {
		return govpayVersione;
	}

	public void setGovpayVersione(String govpayVersione) {
		this.govpayVersione = govpayVersione;
	}
	
}


