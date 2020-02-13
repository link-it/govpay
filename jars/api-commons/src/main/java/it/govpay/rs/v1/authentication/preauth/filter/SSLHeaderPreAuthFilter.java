package it.govpay.rs.v1.authentication.preauth.filter;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.UtilsException;
import org.openspcoop2.utils.certificate.Certificate;
import org.openspcoop2.utils.certificate.CertificateDecodeConfig;
import org.openspcoop2.utils.certificate.CertificateInfo;
import org.openspcoop2.utils.certificate.CertificatePrincipal;
import org.openspcoop2.utils.certificate.CertificateUtils;
import org.slf4j.Logger;

import it.govpay.core.utils.GovpayConfig;

public class SSLHeaderPreAuthFilter extends org.openspcoop2.utils.service.authentication.preauth.filter.HeaderPreAuthFilter {
	
	public static final String AUTENTICAZIONE_SSL_HEADER_TRANSLATE_TAB_NEW_LINE = "translateTabNewLine";
	public static final String AUTENTICAZIONE_SSL_HEADER_BASE64_DECODE = "base64Decode";
	public static final String AUTENTICAZIONE_SSL_HEADER_URL_DECODE = "urlDecode";
	public static final String AUTENTICAZIONE_SSL_HEADER_NOME_HEADER = "nomeHeader";
	
	private static Logger log = LoggerWrapperFactory.getLogger(SSLHeaderPreAuthFilter.class);

	public SSLHeaderPreAuthFilter() {
		super();
	}

	@Override
	protected String getPrincipalHeaderName() {
		String headerAuth = GovpayConfig.getInstance().getAutenticazioneSSLHeaderProperties().getProperty(AUTENTICAZIONE_SSL_HEADER_NOME_HEADER);
		return headerAuth;
	}
	
	@Override
	protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
		String headerValue = request.getHeader(this.getPrincipalHeaderName());
		this.logConfigurazione();
		log.debug("Letto Principal: ["+headerValue+"]");
		return decodePrincipal(headerValue);
	}
	
	private String decodePrincipal(String headerValue) {
		boolean urlDecode = this.getPropertyBooleanValue(GovpayConfig.getInstance().getAutenticazioneSSLHeaderProperties().getProperty(AUTENTICAZIONE_SSL_HEADER_URL_DECODE));
		boolean base64Decode = this.getPropertyBooleanValue(GovpayConfig.getInstance().getAutenticazioneSSLHeaderProperties().getProperty(AUTENTICAZIONE_SSL_HEADER_BASE64_DECODE));
		boolean translateTabNewLine = this.getPropertyBooleanValue(GovpayConfig.getInstance().getAutenticazioneSSLHeaderProperties().getProperty(AUTENTICAZIONE_SSL_HEADER_TRANSLATE_TAB_NEW_LINE));
		
		CertificateDecodeConfig config = new CertificateDecodeConfig();
		config.setBase64Decode(base64Decode);
		config.setUrlDecode(urlDecode);
		config.setReplace(translateTabNewLine);
		
		try {
			Certificate certificate = CertificateUtils.readCertificate(config , headerValue);
			CertificateInfo certificateInfo = certificate.getCertificate();
			CertificatePrincipal subject = certificateInfo.getSubject();
			return subject.toString();
		} catch (UtilsException e) {
			log.error("Errore durante la decodifica del valore contenuto nell'header: " + e.getMessage(), e);
		}
		
		return headerValue;
	}
	
	
	
	private boolean getPropertyBooleanValue(String value) {
		return "true".equalsIgnoreCase(value);
	}
	
	private void logConfigurazione() {
		Properties autenticazioneSSLHeaderProperties = GovpayConfig.getInstance().getAutenticazioneSSLHeaderProperties();
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("Lettura delle informazioni del certificato dall'header ["+ autenticazioneSSLHeaderProperties.getProperty(AUTENTICAZIONE_SSL_HEADER_NOME_HEADER) +"]:");
		sb.append("\nURL Decode ["+ autenticazioneSSLHeaderProperties.getProperty(AUTENTICAZIONE_SSL_HEADER_URL_DECODE) +"]");
		sb.append("\nBase64 Decode ["+ autenticazioneSSLHeaderProperties.getProperty(AUTENTICAZIONE_SSL_HEADER_BASE64_DECODE) +"]");
		sb.append("\nTranslate TabNewLine ["+ autenticazioneSSLHeaderProperties.getProperty(AUTENTICAZIONE_SSL_HEADER_TRANSLATE_TAB_NEW_LINE) +"]");
		
		log.debug(sb.toString());
	}
}
