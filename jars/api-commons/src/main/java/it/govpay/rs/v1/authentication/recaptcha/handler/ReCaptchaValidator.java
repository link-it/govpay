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
package it.govpay.rs.v1.authentication.recaptcha.handler;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.MessageFormat;
import java.util.Arrays;

import org.openspcoop2.utils.LoggerWrapperFactory;
import org.slf4j.Logger;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import it.govpay.core.beans.Costanti;
import it.govpay.core.exceptions.ValidationException;
import it.govpay.core.utils.LogUtils;
import it.govpay.core.utils.validator.ValidatoreIdentificativi;
import it.govpay.model.configurazione.Hardening;
import it.govpay.rs.v1.authentication.recaptcha.beans.CaptchaCostanti;
import it.govpay.rs.v1.authentication.recaptcha.beans.CaptchaResponse;
import it.govpay.rs.v1.authentication.recaptcha.exception.ReCaptchaConfigurazioneNonValidaException;
import it.govpay.rs.v1.authentication.recaptcha.exception.ReCaptchaInvalidException;
import it.govpay.rs.v1.authentication.recaptcha.exception.ReCaptchaParametroResponseInvalidException;
import it.govpay.rs.v1.authentication.recaptcha.exception.ReCaptchaScoreNonValidoException;
import it.govpay.rs.v1.authentication.recaptcha.exception.ReCaptchaUnavailableException;
import jakarta.servlet.http.HttpServletRequest;

public class ReCaptchaValidator {

	private Hardening captchaSettings;

	private Logger logger = LoggerWrapperFactory.getLogger(ReCaptchaValidator.class);

	public ReCaptchaValidator(Hardening settings) throws ReCaptchaConfigurazioneNonValidaException {
		this.captchaSettings = settings;

		if(this.captchaSettings.getGoogleCatpcha() == null)
			throw new ReCaptchaConfigurazioneNonValidaException("Configurazione del servizio Google Recaptcha non presente.");

		// validazione URL
		if(org.apache.commons.lang3.StringUtils.isBlank(this.captchaSettings.getGoogleCatpcha().getServerURL()))
			throw new ReCaptchaConfigurazioneNonValidaException("URL servizio di verifica Google Recaptcha non presente.");

		try {
			new URI(this.captchaSettings.getGoogleCatpcha().getServerURL()).toURL();
		}catch(MalformedURLException | URISyntaxException e) {
			throw new ReCaptchaConfigurazioneNonValidaException("URL servizio di verifica Google Recaptcha non valida.");
		}

		// validazione SiteKey
		if(org.apache.commons.lang3.StringUtils.isBlank(this.captchaSettings.getGoogleCatpcha().getSiteKey()))
			throw new ReCaptchaConfigurazioneNonValidaException("Site Key servizio di verifica Google Recaptcha non presente.");

		// validazione secretKey
		if(org.apache.commons.lang3.StringUtils.isBlank(this.captchaSettings.getGoogleCatpcha().getSecretKey()))
			throw new ReCaptchaConfigurazioneNonValidaException("Secret Key servizio di verifica Google Recaptcha non presente.");

		// validazione Connection Timeout
		if(this.captchaSettings.getGoogleCatpcha().getConnectionTimeout() <= 0)
			throw new ReCaptchaConfigurazioneNonValidaException("ConnectionTimeout servizio di verifica Google Recaptcha deve essere > 0.");

		// validazione Read Timeout
		if(this.captchaSettings.getGoogleCatpcha().getReadTimeout() <= 0)
			throw new ReCaptchaConfigurazioneNonValidaException("ReadTimeout servizio di verifica Google Recaptcha deve essere > 0.");

		// validazione Parameter Name
		if(org.apache.commons.lang3.StringUtils.isBlank(this.captchaSettings.getGoogleCatpcha().getResponseParameter()))
			throw new ReCaptchaConfigurazioneNonValidaException("Il nome del parametro da cui estrarre la Response da inviare al servizio di verifica Google Recaptcha non presente.");

		// validazione Soglia
		if(this.captchaSettings.getGoogleCatpcha().getSoglia() <= 0 || this.captchaSettings.getGoogleCatpcha().getSoglia() > 1)
			throw new ReCaptchaConfigurazioneNonValidaException("Soglia accettazione risposta del servizio di verifica Google Recaptcha deve essere compresa tra 0 e 1.");
	}


	public boolean validateCaptcha(HttpServletRequest request) throws ReCaptchaInvalidException, ReCaptchaScoreNonValidoException, ReCaptchaUnavailableException, ReCaptchaParametroResponseInvalidException, ValidationException {
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
        	LogUtils.logDebug(logger, "Richiesta validazione Captcha alla URL ["+verifyUri.toString()+"], Payload ["+payload+"]");

        	// imposto i timeout di connessione
        	HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        	factory.setConnectTimeout(this.captchaSettings.getGoogleCatpcha().getConnectionTimeout());
        	
           	RestTemplate restTemplate = new RestTemplate(factory);

           	final  CaptchaResponse googleResponse = restTemplate.getForObject(verifyUri, CaptchaResponse.class);

           	if(googleResponse == null)
           		throw new ReCaptchaInvalidException("Verifica reCaptcha completata con insuccesso: risposta null");

           	LogUtils.logDebug(logger, "Verifica reCaptcha completata, ricevuto messaggio dal servizio di verifica: {} ", googleResponse.toString());

            if (!googleResponse.isSuccess()) {
                throw new ReCaptchaInvalidException(MessageFormat.format("Verifica reCaptcha completata con insuccesso: Errori trovati [{0}]", Arrays.toString(googleResponse.getErrorCodes())));
            }
            else {
            	// controllo soglia score valido solo nella versione V3, nella V2 non e' presente nella risposta
            	BigDecimal scoreRicevuto = googleResponse.getScore();
				if(scoreRicevuto != null) {
            		double scoreRichiesto = this.captchaSettings.getGoogleCatpcha().getSoglia();
            		LogUtils.logDebug(logger, "reCaptcha validato correttamente, controllo dello score: Ricevuto[{}], Richiesto[{}]",	scoreRicevuto, scoreRichiesto);
            		if(scoreRicevuto.doubleValue() < scoreRichiesto) {
            			throw new ReCaptchaScoreNonValidoException("Verifica reCaptcha completata con insuccesso: score non sufficiente");
            		}
            	}
            }

            return googleResponse.isSuccess();
        } catch (RestClientException rce) {
        	if(this.captchaSettings.getGoogleCatpcha().isDenyOnFail())
        		throw new ReCaptchaUnavailableException("Servizio non raggiungibile. Riprovare piu' tardi.", rce);
        	else {
        		logger.warn("Servizio verifica recaptcha non disponibile. Richiesta autorizzata per configurazione denyOnFail=false.");
        		return true;
        	}
        }
	}


	private String getCaptchaResponse(HttpServletRequest request) throws ValidationException {
		String reCaptchaResponse = request.getParameter(this.captchaSettings.getGoogleCatpcha().getResponseParameter());

		// se non ricevo l'header come parametro provo a cercarlo come header
		if(org.apache.commons.lang3.StringUtils.isBlank(reCaptchaResponse)) {
			reCaptchaResponse = request.getHeader(this.captchaSettings.getGoogleCatpcha().getResponseParameter());
		}

		ValidatoreIdentificativi validatoreIdentificativi = ValidatoreIdentificativi.newInstance();
		validatoreIdentificativi.validaParametroOpzionale(this.captchaSettings.getGoogleCatpcha().getResponseParameter(), reCaptchaResponse, 1, null);
		
		return reCaptchaResponse;
	}


    private boolean responseSanityCheck(final String response) {
    	// controllo solo che non sia vuoto
        return StringUtils.hasLength(response); // && CaptchaCostanti.RESPONSE_PATTERN.matcher(response).matches()
    }


    private String getClientIP(HttpServletRequest request) throws ValidationException {
        final String xfHeader = request.getHeader(Costanti.HEADER_NAME_X_FORWARDED_FOR);
        ValidatoreIdentificativi validatoreIdentificativi = ValidatoreIdentificativi.newInstance();
		validatoreIdentificativi.validaParametroOpzionale(Costanti.HEADER_NAME_X_FORWARDED_FOR, xfHeader, 1, null);
        
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
}
