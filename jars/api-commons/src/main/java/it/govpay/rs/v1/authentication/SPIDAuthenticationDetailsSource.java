package it.govpay.rs.v1.authentication;

import javax.servlet.http.HttpServletRequest;

import org.openspcoop2.utils.LoggerWrapperFactory;
import org.slf4j.Logger;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import it.govpay.core.autorizzazione.beans.GovpayWebAuthenticationDetails;
import it.govpay.core.utils.GovpayConfig;
 
public class SPIDAuthenticationDetailsSource implements
		AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> {
	
	private static Logger log = LoggerWrapperFactory.getLogger(SPIDAuthenticationDetailsSource.class);
	
	public static final String TINIT_PREFIX = "TINIT-";
	
	public static final String SPID_HEADER_SPID_CODE = "spidCode";
	public static final String SPID_HEADER_NAME = "name";
	public static final String SPID_HEADER_FAMILY_NAME = "familyName";
	public static final String SPID_HEADER_PLACE_OF_BIRTH = "placeOfBirth";
	public static final String SPID_HEADER_DATE_OF_BIRTH = "dateOfBirth";
	public static final String SPID_HEADER_GENDER = "gender";
	public static final String SPID_HEADER_COMPANY_NAME = "companyName";
	public static final String SPID_HEADER_REGISTERED_OFFICE = "registeredOffice";	
	public static final String SPID_HEADER_FISCAL_NUMBER = "fiscalNumber";
	public static final String SPID_HEADER_IVA_CODE = "ivaCode";
	public static final String SPID_HEADER_ID_CARD = "idCard";
	public static final String SPID_HEADER_MOBILE_PHONE = "mobilePhone";
	public static final String SPID_HEADER_EMAIL = "email";
	public static final String SPID_HEADER_ADDRESS = "address";
	public static final String SPID_HEADER_DIGITAL_ADDRESS = "digitalAddress";
	

	// ~ Methods
	// ========================================================================================================

	/**
	 * @param context the {@code HttpServletRequest} object.
	 * @return the {@code WebAuthenticationDetails} containing information about the
	 * current request
	 */
	public WebAuthenticationDetails buildDetails(HttpServletRequest context) {
		log.debug("Lettura Headers SPID in corso...");
		GovpayWebAuthenticationDetails details = new GovpayWebAuthenticationDetails(log, context, GovpayConfig.getInstance().getAutenticazioneSPIDElencoHeadersRequest());
		log.debug("Lettura Headers SPID completata.");
		return details;
	}
}