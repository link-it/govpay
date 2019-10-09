package it.govpay.rs.v1.authentication.recaptcha.handler;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;

import org.openspcoop2.utils.LoggerWrapperFactory;
import org.slf4j.Logger;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import it.govpay.bd.configurazione.model.Hardening;
import it.govpay.rs.v1.authentication.recaptcha.beans.CaptchaCostanti;
import it.govpay.rs.v1.authentication.recaptcha.beans.CaptchaResponse;
import it.govpay.rs.v1.authentication.recaptcha.exception.ReCaptchaConfigurazioneNonValidaException;
import it.govpay.rs.v1.authentication.recaptcha.exception.ReCaptchaInvalidException;
import it.govpay.rs.v1.authentication.recaptcha.exception.ReCaptchaParametroResponseInvalidException;
import it.govpay.rs.v1.authentication.recaptcha.exception.ReCaptchaScoreNonValidoException;
import it.govpay.rs.v1.authentication.recaptcha.exception.ReCaptchaUnavailableException;

public class ReCaptchaValidator {

	private Hardening captchaSettings;
	
	private Logger logger = LoggerWrapperFactory.getLogger(ReCaptchaValidator.class);
	
	public ReCaptchaValidator(Hardening settings) throws ReCaptchaConfigurazioneNonValidaException {
		this.captchaSettings = settings;
		try {
			if(this.captchaSettings.getGoogleCatpcha() == null)
				throw new Exception("Configurazione del servizio Google Recaptcha non presente.");
			
			// validazione URL
			if(org.apache.commons.lang.StringUtils.isBlank(this.captchaSettings.getGoogleCatpcha().getServerURL()))
				throw new Exception("URL servizio di verifica Google Recaptcha non presente.");
			
			try {
				new URL(this.captchaSettings.getGoogleCatpcha().getServerURL());
			}catch(MalformedURLException e) {
				throw new Exception("URL servizio di verifica Google Recaptcha non valida.");
			}
						
			// validazione SiteKey
			if(org.apache.commons.lang.StringUtils.isBlank(this.captchaSettings.getGoogleCatpcha().getSiteKey()))
				throw new Exception("Site Key servizio di verifica Google Recaptcha non presente.");
			
			// validazione secretKey
			if(org.apache.commons.lang.StringUtils.isBlank(this.captchaSettings.getGoogleCatpcha().getSecretKey()))
				throw new Exception("Secret Key servizio di verifica Google Recaptcha non presente.");
			
			// validazione Connection Timeout
			if(this.captchaSettings.getGoogleCatpcha().getConnectionTimeout() <= 0)
				throw new Exception("ConnectionTimeout servizio di verifica Google Recaptcha deve essere > 0.");
			
			// validazione Read Timeout
			if(this.captchaSettings.getGoogleCatpcha().getReadTimeout() <= 0)
				throw new Exception("ReadTimeout servizio di verifica Google Recaptcha deve essere > 0.");
			
			// validazione Parameter Name
			if(org.apache.commons.lang.StringUtils.isBlank(this.captchaSettings.getGoogleCatpcha().getResponseParameter()))
				throw new Exception("Il nome del parametro da cui estrarre la Response da inviare al servizio di verifica Google Recaptcha non presente.");
			
			// validazione Soglia
			if(this.captchaSettings.getGoogleCatpcha().getSoglia() <= 0 || this.captchaSettings.getGoogleCatpcha().getSoglia() > 1)
				throw new Exception("Soglia accettazione risposta del servizio di verifica Google Recaptcha deve essere compresa tra 0 e 1.");
			
		} catch(Exception e) {
			throw new ReCaptchaConfigurazioneNonValidaException(e.getMessage());
		}
	}
	
	
	public boolean validateCaptcha(HttpServletRequest request) throws ReCaptchaInvalidException, ReCaptchaScoreNonValidoException, ReCaptchaUnavailableException, ReCaptchaParametroResponseInvalidException {
		String reCaptchaResponse = getCaptchaResponse(request);
		if (!responseSanityCheck(reCaptchaResponse)) {
            throw new ReCaptchaParametroResponseInvalidException("Il parametro '"+this.captchaSettings.getGoogleCatpcha().getResponseParameter()+"' e' vuoto o contiene caratteri non validi.");
        }
		
		// "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s&remoteip=%s"
		
		String payload = String.format(CaptchaCostanti.PAYLOAD_TEMPLATE, this.captchaSettings.getGoogleCatpcha().getSecretKey(), reCaptchaResponse, getClientIP(request));
		
		StringBuilder sbUrl = new StringBuilder();
		sbUrl.append(this.captchaSettings.getGoogleCatpcha().getServerURL());
		if(this.captchaSettings.getGoogleCatpcha().getServerURL().contains("?")) {
			sbUrl.append("&");
		} else{
			sbUrl.append("?");
		}
		sbUrl.append(payload);
		
		final URI verifyUri = URI.create(sbUrl.toString());
        try {
        	logger.debug("Richiesta validazione Captcha alla URL ["+verifyUri.toString()+"], Payload ["+payload+"]");
        	
        	// imposto i timeout di connessione
        	HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        	factory.setConnectTimeout(this.captchaSettings.getGoogleCatpcha().getConnectionTimeout());
        	factory.setReadTimeout(this.captchaSettings.getGoogleCatpcha().getReadTimeout());
        	
           	RestTemplate restTemplate = new RestTemplate(factory);
           	
           	final  CaptchaResponse googleResponse = restTemplate.getForObject(verifyUri, CaptchaResponse.class);
           	
            logger.debug("Verifica reCaptcha completata, ricevuto messaggio dal servizio di verifica: {} ", googleResponse.toString());

            if (!googleResponse.isSuccess()) {
//                if (googleResponse.hasClientError()) {
//                  //  reCaptchaAttemptService.reCaptchaFailed(getClientIP(request));
//                }
                throw new ReCaptchaInvalidException("Verifica reCaptcha completata con insuccesso: Errori trovati ["+googleResponse.getErrorCodes()+"]");
            }
            else {
            	// controllo soglia score valido solo nella versione V3, nella V2 non e' presente nella risposta
            	if(googleResponse.getScore() != null) {
            		logger.debug("reCaptcha validato correttamente, controllo dello score: Ricevuto["+googleResponse.getScore()+"], Richiesto["+this.captchaSettings.getGoogleCatpcha().getSoglia()+"]");
            		if(googleResponse.getScore().doubleValue() < this.captchaSettings.getGoogleCatpcha().getSoglia()) {
            			throw new ReCaptchaScoreNonValidoException("Verifica reCaptcha completata con insuccesso: score non sufficiente");
            		}
            	}
            }
            
            return googleResponse.isSuccess();
        } catch (RestClientException rce) {
        	if(this.captchaSettings.getGoogleCatpcha().isDenyOnFail())        	
        		throw new ReCaptchaUnavailableException("Servizio non raggiungibile. Riprovare piu' tardi.", rce);
        	else 
        		return true;
        } 
	}


	private String getCaptchaResponse(HttpServletRequest request) {
		String reCaptchaResponse = request.getParameter(this.captchaSettings.getGoogleCatpcha().getResponseParameter());
		
		// se non ricevo l'header come parametro provo a cercarlo come header
		if(org.apache.commons.lang.StringUtils.isBlank(reCaptchaResponse)) {
			reCaptchaResponse = request.getHeader(this.captchaSettings.getGoogleCatpcha().getResponseParameter());
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
