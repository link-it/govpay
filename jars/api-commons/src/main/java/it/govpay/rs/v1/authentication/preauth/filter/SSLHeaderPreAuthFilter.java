/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2024 Link.it srl (http://www.link.it).
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
package it.govpay.rs.v1.authentication.preauth.filter;

import java.util.Properties;

import jakarta.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.UtilsException;
import org.openspcoop2.utils.certificate.Certificate;
import org.openspcoop2.utils.certificate.CertificateDecodeConfig;
import org.openspcoop2.utils.certificate.CertificateInfo;
import org.openspcoop2.utils.certificate.CertificatePrincipal;
import org.openspcoop2.utils.certificate.CertificateUtils;
import org.slf4j.Logger;

import it.govpay.core.utils.GovpayConfig;
import it.govpay.core.utils.LogUtils;

public class SSLHeaderPreAuthFilter extends org.openspcoop2.utils.service.authentication.preauth.filter.HeaderPreAuthFilter {

	public static final String AUTENTICAZIONE_SSL_HEADER_REPLACE_ENABLED = "replaceCharacters.enabled";
	public static final String AUTENTICAZIONE_SSL_HEADER_BASE64_DECODE = "base64Decode";
	public static final String AUTENTICAZIONE_SSL_HEADER_URL_DECODE = "urlDecode";
	public static final String AUTENTICAZIONE_SSL_HEADER_NOME_HEADER = "nomeHeader";
	public static final String AUTENTICAZIONE_SSL_HEADER_REPLACE_SRC_CHARACTER = "replaceCharacters.source";
	public static final String AUTENTICAZIONE_SSL_HEADER_REPLACE_DEST_CHARACTER = "replaceCharacters.dest";

    public static final String X509_BEGIN = "-----BEGIN CERTIFICATE-----";
    public static final String X509_END = "-----END CERTIFICATE-----";

	private static Logger log = LoggerWrapperFactory.getLogger(SSLHeaderPreAuthFilter.class);

	public SSLHeaderPreAuthFilter() {
		super();
	}

	@Override
	protected String getPrincipalHeaderName() {
		return GovpayConfig.getInstance().getAutenticazioneSSLHeaderProperties().getProperty(AUTENTICAZIONE_SSL_HEADER_NOME_HEADER);
	}

	@Override
	protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
		this.logConfigurazione();
		String headerValue = request.getHeader(this.getPrincipalHeaderName());
		log.debug("Letto Principal: [{}]", headerValue);
		return decodePrincipal(headerValue);
	}

	private String decodePrincipal(String headerValue) {
		Properties autenticazioneSSLHeaderProperties = GovpayConfig.getInstance().getAutenticazioneSSLHeaderProperties();
		boolean urlDecode = this.getPropertyBooleanValue(autenticazioneSSLHeaderProperties.getProperty(AUTENTICAZIONE_SSL_HEADER_URL_DECODE));
		boolean base64Decode = this.getPropertyBooleanValue(autenticazioneSSLHeaderProperties.getProperty(AUTENTICAZIONE_SSL_HEADER_BASE64_DECODE));
		boolean replaceEnabled = this.getPropertyBooleanValue(autenticazioneSSLHeaderProperties.getProperty(AUTENTICAZIONE_SSL_HEADER_REPLACE_ENABLED));

		CertificateDecodeConfig config = new CertificateDecodeConfig();

		// la libreria non esegue correttamente il replace dello spazio bianco.
		// per evitare lo schianto effettuo qui il replace dei caratteri e poi invoco l'utility senza impostare il replace
		if(replaceEnabled) {
			config.setReplace(replaceEnabled);
			config.setReplaceSource(this.getReplaceSource(autenticazioneSSLHeaderProperties));
			config.setReplaceDest(this.getReplaceDestination(autenticazioneSSLHeaderProperties));

			StringBuilder sbNewCertificate = new StringBuilder();
			boolean forceEnrichPEMBeginEnd = replaceCharacters(config, headerValue, sbNewCertificate);
			headerValue = sbNewCertificate.toString();
			
			if(config.isEnrichPEMBeginEnd() || forceEnrichPEMBeginEnd) {
				headerValue = addPEMDeclaration(headerValue, forceEnrichPEMBeginEnd);
			}

			log.debug("Replace caratteri completato, nuovo valore principal: [{}]", headerValue);
		}

		// reset della configurazione dopo un eventuale replace
		config = new CertificateDecodeConfig();
		config.setBase64Decode(base64Decode);
		config.setUrlDecode(urlDecode);

		try {
			Certificate certificate = CertificateUtils.readCertificate(config , headerValue);
			CertificateInfo certificateInfo = certificate.getCertificate();
			CertificatePrincipal subject = certificateInfo.getSubject();
			LogUtils.logDebug(log, "Estratto subject : [{}]", subject.toString());
			return subject.toString();
		} catch (UtilsException e) {
			log.error("Errore durante la decodifica del valore contenuto nell'header: " + e.getMessage(), e);
		}

		return headerValue;
	}

	private String getReplaceSource(Properties autenticazioneSSLHeaderProperties) {
		String replaceSrc = autenticazioneSSLHeaderProperties.getProperty(AUTENTICAZIONE_SSL_HEADER_REPLACE_SRC_CHARACTER);
		String carattereReplace = this.decodificaCarattereReplace(replaceSrc);
		return carattereReplace != null ? carattereReplace : "\t";
	}

	private String getReplaceDestination(Properties autenticazioneSSLHeaderProperties) {
		String replaceDest = autenticazioneSSLHeaderProperties.getProperty(AUTENTICAZIONE_SSL_HEADER_REPLACE_DEST_CHARACTER);
		String carattereReplace = this.decodificaCarattereReplace(replaceDest);
		return carattereReplace != null ? carattereReplace : "\n";
	}

	private String decodificaCarattereReplace(String input) {
		if(input!=null){
			if(StringUtils.isNotEmpty(input)) {
				if("\\t".equals(input)) {
					return "\t";
				}
				else if("\\r".equals(input)) {
					return "\r";
				}
				else if("\\n".equals(input)) {
					return "\n";
				}
				else if("\\r\\n".equals(input)) {
					return "\r\n";
				}
				else if("\\s".equals(input)) {
					return " "; // viene codificato in spazio
				}
			}
		}

		return null;
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
		boolean replaceEnabled = this.getPropertyBooleanValue(autenticazioneSSLHeaderProperties.getProperty(AUTENTICAZIONE_SSL_HEADER_REPLACE_ENABLED));
		sb.append("\nAbilita Replace ["+ replaceEnabled +"]");
		if(replaceEnabled) {
			String replaceSrc = autenticazioneSSLHeaderProperties.getProperty(AUTENTICAZIONE_SSL_HEADER_REPLACE_SRC_CHARACTER);
			String replaceDest = autenticazioneSSLHeaderProperties.getProperty(AUTENTICAZIONE_SSL_HEADER_REPLACE_DEST_CHARACTER);
			sb.append("\nReplace Source ["+ (replaceSrc != null ? replaceSrc : "non valorizzato, verra' utilizzato il default '\\t'") +"]");
			sb.append("\nReplace Destination ["+ (replaceDest != null ? replaceDest : "non valorizzato, verra' utilizzato il default '\\n'")+"]");
		}

		LogUtils.logDebug(log, sb.toString());
	}

	private static boolean replaceCharacters(CertificateDecodeConfig config, String certificate, StringBuilder sbNewCertificate) {
		boolean forceEnrichPEMBeginEnd = false;
		// per evitare di sovrascrivere 'BEGIN' e 'END'
		if(certificate.startsWith(SSLHeaderPreAuthFilter.X509_BEGIN) && certificate.length()>SSLHeaderPreAuthFilter.X509_BEGIN.length()) {
			certificate = certificate.substring(SSLHeaderPreAuthFilter.X509_BEGIN.length());
			forceEnrichPEMBeginEnd = true;
		}
		if(certificate.endsWith(SSLHeaderPreAuthFilter.X509_END) && certificate.length()>SSLHeaderPreAuthFilter.X509_END.length()) {
			certificate = certificate.substring(0, certificate.length()-SSLHeaderPreAuthFilter.X509_END.length());
		}

		int index = 0; // per evitare bug di cicli infiniti
		while(certificate.contains(config.getReplaceSource()) && index<10000) {
			certificate = certificate.replace(config.getReplaceSource(), config.getReplaceDest());
			index++;
		}
		sbNewCertificate.append(certificate);
		return forceEnrichPEMBeginEnd;
	}

	private static String addPEMDeclaration(String certificate, boolean forceEnrichPEMBeginEnd) {
		if(!certificate.startsWith(SSLHeaderPreAuthFilter.X509_BEGIN)) {
			certificate = SSLHeaderPreAuthFilter.X509_BEGIN+ (forceEnrichPEMBeginEnd?"":"\n")+ certificate;
		}
		if(!certificate.endsWith(SSLHeaderPreAuthFilter.X509_END)) {
			certificate = certificate+ (forceEnrichPEMBeginEnd?"":"\n") +SSLHeaderPreAuthFilter.X509_END;
		}
		return certificate;
	}
}
