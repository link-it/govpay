package it.govpay.rs.v1.authentication.recaptcha.handler;

import java.net.URI;

import javax.servlet.http.HttpServletRequest;

import org.openspcoop2.utils.LoggerWrapperFactory;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import it.govpay.bd.configurazione.model.ReCaptcha;
import it.govpay.rs.v1.authentication.recaptcha.beans.CaptchaCostanti;
import it.govpay.rs.v1.authentication.recaptcha.beans.CaptchaResponse;
import it.govpay.rs.v1.authentication.recaptcha.exception.ReCaptchaInvalidException;
import it.govpay.rs.v1.authentication.recaptcha.exception.ReCaptchaParametroResponseInvalidException;
import it.govpay.rs.v1.authentication.recaptcha.exception.ReCaptchaScoreNonValidoException;
import it.govpay.rs.v1.authentication.recaptcha.exception.ReCaptchaUnavailableException;

public class ReCaptchaValidator {

	private ReCaptcha captchaSettings;
	
	private Logger logger = LoggerWrapperFactory.getLogger(ReCaptchaValidator.class);
	
	public ReCaptchaValidator(ReCaptcha settings) {
		this.captchaSettings = settings;
	}
	
	
	public boolean validateCaptcha(HttpServletRequest request) {

		String reCaptchaResponse = getCaptchaResponse(request);
		if (!responseSanityCheck(reCaptchaResponse)) {
            throw new ReCaptchaParametroResponseInvalidException("Il parametro '"+this.captchaSettings.getResponseParameter()+"' e' vuoto o contiene caratteri non validi.");
        }
		
		// "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s&remoteip=%s"
		
		final URI verifyUri = URI.create(this.captchaSettings.getServerURL());
        try {
        	
        	String payload = String.format(CaptchaCostanti.PAYLOAD_TEMPLATE, this.captchaSettings.getSecret(), reCaptchaResponse, getClientIP(request));
        	logger.debug("Richiesta validazione Captcha alla URL ["+verifyUri.toString()+"], Payload ["+payload+"]");
        	
        	RestTemplate restTemplate = new RestTemplate();
            final ResponseEntity<CaptchaResponse> postResponse = restTemplate.postForEntity(verifyUri, payload, CaptchaResponse.class);
            CaptchaResponse googleResponse = postResponse.getBody();
            logger.debug("Verifica reCaptcha completata, Google's response: {} ", googleResponse.toString());

            if (!googleResponse.isSuccess()) {
//                if (googleResponse.hasClientError()) {
//                  //  reCaptchaAttemptService.reCaptchaFailed(getClientIP(request));
//                }
                throw new ReCaptchaInvalidException("Verifica reCaptcha completata con insuccesso: Errori trovati ["+googleResponse.getErrorCodes()+"]");
            }
            else {
            	// controllo soglia score valido solo nella versione V3, nella V2 non e' presente nella risposta
            	if(googleResponse.getScore() != null) {
            		logger.debug("reCaptcha validato correttamente, controllo dello score: Ricevuto["+googleResponse.getScore()+"], Richiesto["+this.captchaSettings.getSoglia()+"]");
            		if(googleResponse.getScore().doubleValue() < this.captchaSettings.getSoglia()) {
            			throw new ReCaptchaScoreNonValidoException("Verifica reCaptcha completata con insuccesso: score non sufficiente");
            		}
            	}
            }
            
            return googleResponse.isSuccess();
        } catch (RestClientException rce) {
            throw new ReCaptchaUnavailableException("Servizio non raggiungibile. Riprovare piu' tardi.", rce);
        }
	}


	private String getCaptchaResponse(HttpServletRequest request) {
		String reCaptchaResponse = request.getParameter(this.captchaSettings.getResponseParameter());
		
		// se non ricevo l'header come parametro provo a cercarlo come header
		if(org.apache.commons.lang.StringUtils.isBlank(reCaptchaResponse)) {
			reCaptchaResponse = request.getHeader(this.captchaSettings.getResponseParameter());
		}
		
		return reCaptchaResponse;
	}
	
	
    private boolean responseSanityCheck(final String response) {
        return StringUtils.hasLength(response) && CaptchaCostanti.RESPONSE_PATTERN.matcher(response).matches();
    }
	
	
    private String getClientIP(HttpServletRequest request) {
        final String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
}
