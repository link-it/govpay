package it.govpay.rs.v1.authentication.hardening.matcher;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.security.web.util.matcher.RequestVariablesExtractor;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import it.govpay.bd.BasicBD;
import it.govpay.bd.configurazione.model.Hardening;
import it.govpay.bd.model.Configurazione;
import it.govpay.rs.v1.authentication.recaptcha.exception.ReCaptchaConfigurazioneNonValidaException;
import it.govpay.rs.v1.authentication.recaptcha.exception.ReCaptchaInvalidException;
import it.govpay.rs.v1.authentication.recaptcha.exception.ReCaptchaParametroResponseInvalidException;
import it.govpay.rs.v1.authentication.recaptcha.exception.ReCaptchaScoreNonValidoException;
import it.govpay.rs.v1.authentication.recaptcha.exception.ReCaptchaUnavailableException;
import it.govpay.rs.v1.authentication.recaptcha.handler.ReCaptchaValidator;

/***
 * 
 * Classe che effettua alcuni controlli supplementari in caso di matching della url chiamata
 * based on {@link AntPathRequestMatcher}
 * 
 * 
 * @author pintori
 *
 */
public class HardeningAntPathRequestMatcher implements RequestMatcher, RequestVariablesExtractor {
	private static final Log logger = LogFactory.getLog(HardeningAntPathRequestMatcher.class);
	private static final String MATCH_ALL = "/**";

	private final Matcher matcher;
	private final String pattern;
	private final HttpMethod httpMethod;
	private final boolean caseSensitive;
	
	/**
	 * Creates a matcher with the specific pattern which will match all HTTP methods in a
	 * case insensitive manner.
	 *
	 * @param pattern the ant pattern to use for matching
	 */
	public HardeningAntPathRequestMatcher(String pattern) {
		this(pattern, null);
	}

	/**
	 * Creates a matcher with the supplied pattern and HTTP method in a case insensitive
	 * manner.
	 *
	 * @param pattern the ant pattern to use for matching
	 * @param httpMethod the HTTP method. The {@code matches} method will return false if
	 * the incoming request doesn't have the same method.
	 */
	public HardeningAntPathRequestMatcher(String pattern, String httpMethod) {
		this(pattern, httpMethod, true);
	}

	/**
	 * Creates a matcher with the supplied pattern which will match the specified Http
	 * method
	 *
	 * @param pattern the ant pattern to use for matching
	 * @param httpMethod the HTTP method. The {@code matches} method will return false if
	 * the incoming request doesn't doesn't have the same method.
	 * @param caseSensitive true if the matcher should consider case, else false
	 */
	public HardeningAntPathRequestMatcher(String pattern, String httpMethod,
			boolean caseSensitive) {
		Assert.hasText(pattern, "Pattern cannot be null or empty");
		this.caseSensitive = caseSensitive;

		if (pattern.equals(MATCH_ALL) || pattern.equals("**")) {
			pattern = MATCH_ALL;
			this.matcher = null;
		}
		else {
			// If the pattern ends with {@code /**} and has no other wildcards or path
			// variables, then optimize to a sub-path match
			if (pattern.endsWith(MATCH_ALL)
					&& (pattern.indexOf('?') == -1 && pattern.indexOf('{') == -1
							&& pattern.indexOf('}') == -1)
					&& pattern.indexOf("*") == pattern.length() - 2) {
				this.matcher = new SubpathMatcher(
						pattern.substring(0, pattern.length() - 3), caseSensitive);
			}
			else {
				this.matcher = new SpringAntMatcher(pattern, caseSensitive);
			}
		}

		this.pattern = pattern;
		this.httpMethod = StringUtils.hasText(httpMethod) ? HttpMethod.valueOf(httpMethod)
				: null;
	}

	/**
	 * Returns true if the configured pattern (and HTTP-Method) match those of the
	 * supplied request.
	 *
	 * @param request the request to match against. The ant pattern will be matched
	 * against the {@code servletPath} + {@code pathInfo} of the request.
	 */
	@Override
	public boolean matches(HttpServletRequest request) {
		boolean matches = this.doMatches(request);
		
		if(matches)
			matches = matches && this.applyHardening(request);
		
		return matches;
	}
	
	public boolean doMatches(HttpServletRequest request) {
		if (this.httpMethod != null && StringUtils.hasText(request.getMethod())
				&& this.httpMethod != valueOf(request.getMethod())) {
			if (logger.isDebugEnabled()) {
				logger.debug("Request '" + request.getMethod() + " "
						+ getRequestPath(request) + "'" + " doesn't match '"
						+ this.httpMethod + " " + this.pattern);
			}

			return false;
		}

		if (this.pattern.equals(MATCH_ALL)) {
			if (logger.isDebugEnabled()) {
				logger.debug("Request '" + getRequestPath(request)
						+ "' matched by universal pattern '/**'");
			}

			return true;
		}

		String url = getRequestPath(request);

		if (logger.isDebugEnabled()) {
			logger.debug("Checking match of request : '" + url + "'; against '"
					+ this.pattern + "'");
		}
		
		return this.matcher.matches(url);
	}
	

	public boolean applyHardening(HttpServletRequest request){
		boolean authorized = false;
		Hardening setting = readSettings();
		
		if(setting.isAbilitato()) {
			logger.debug("Applico regole di hardening per l'accesso alla risorsa ["+request.getPathInfo()+"]...");
			// Applico regole di controllo Google Captcha
			authorized = applicaControlloReCaptcha(request, setting);
			logger.debug("Controllo accesso alla risorsa ["+request.getPathInfo()+"], regole di hardening applicate con esito ["+(authorized ? "OK" : "KO")+"]");
		} else {
			authorized = true; // se il controllo e' disabilitato passo
			logger.debug("Regole di hardening disabilitate per l'accesso alla risorsa ["+request.getPathInfo()+"], accesso consentito.");
		}
		
		return authorized;
	}

	public boolean applicaControlloReCaptcha(HttpServletRequest request, Hardening setting) {
		boolean authorized = false;
		try {
			ReCaptchaValidator validator = new ReCaptchaValidator(setting);
			authorized = validator.validateCaptcha(request);
		}catch(ReCaptchaConfigurazioneNonValidaException e) {
			logger.error("Controllo ReCaptcha terminato con errore, configurazione del servizio non valida: " + e.getMessage(), e);
		}catch(ReCaptchaParametroResponseInvalidException e) {
			logger.warn("Controllo ReCaptcha terminato con esito: accesso non consentito: " + e.getMessage());
		}catch(ReCaptchaUnavailableException e) {
			logger.warn("Controllo ReCaptcha terminato con esito: accesso non consentito: " + e.getMessage(), e);
		}catch(ReCaptchaScoreNonValidoException e) {
			logger.warn("Controllo ReCaptcha terminato con esito: accesso non consentito: " + e.getMessage());
		}catch(ReCaptchaInvalidException e) {
			logger.warn("Controllo ReCaptcha terminato con esito: accesso non consentito: " + e.getMessage());
		}catch(Exception e) {
			logger.error("Controllo ReCaptcha terminato con errore: " + e.getMessage(), e);
		}
		return authorized;
	}
	
	public Hardening readSettings() {
		BasicBD bd = null;
		try {
			String transactionId = UUID.randomUUID().toString();
			logger.debug("Lettura della configurazione di Govpay in corso...");
			bd = BasicBD.newInstance(transactionId, true);
			Configurazione configurazione = new it.govpay.core.business.Configurazione(bd).getConfigurazione();
			Hardening setting = configurazione.getHardening();
			logger.debug("Lettura della configurazione di Govpay completata.");

			return setting; 
		} catch(Exception e){
			throw new RuntimeException("Errore interno, impossibile autenticare l'utenza", e);
		}	finally {
			if(bd != null)
				bd.closeConnection();
		}
	}
	
	

	@Override
	public Map<String, String> extractUriTemplateVariables(HttpServletRequest request) {
		if (this.matcher == null || !matches(request)) {
			return Collections.emptyMap();
		}
		String url = getRequestPath(request);
		return this.matcher.extractUriTemplateVariables(url);
	}

	private String getRequestPath(HttpServletRequest request) {
		String url = request.getServletPath();

		if (request.getPathInfo() != null) {
			url += request.getPathInfo();
		}

		return url;
	}

	public String getPattern() {
		return this.pattern;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof HardeningAntPathRequestMatcher)) {
			return false;
		}

		HardeningAntPathRequestMatcher other = (HardeningAntPathRequestMatcher) obj;
		return this.pattern.equals(other.pattern) && this.httpMethod == other.httpMethod
				&& this.caseSensitive == other.caseSensitive;
	}

	@Override
	public int hashCode() {
		int code = 31 ^ this.pattern.hashCode();
		if (this.httpMethod != null) {
			code ^= this.httpMethod.hashCode();
		}
		return code;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Ant [pattern='").append(this.pattern).append("'");

		if (this.httpMethod != null) {
			sb.append(", ").append(this.httpMethod);
		}

		sb.append("]");

		return sb.toString();
	}

	/**
	 * Provides a save way of obtaining the HttpMethod from a String. If the method is
	 * invalid, returns null.
	 *
	 * @param method the HTTP method to use.
	 *
	 * @return the HttpMethod or null if method is invalid.
	 */
	private static HttpMethod valueOf(String method) {
		try {
			return HttpMethod.valueOf(method);
		}
		catch (IllegalArgumentException e) {
		}

		return null;
	}

	private static interface Matcher {
		boolean matches(String path);

		Map<String, String> extractUriTemplateVariables(String path);
	}

	private static class SpringAntMatcher implements Matcher {
		private final AntPathMatcher antMatcher;

		private final String pattern;

		private SpringAntMatcher(String pattern, boolean caseSensitive) {
			this.pattern = pattern;
			this.antMatcher = createMatcher(caseSensitive);
		}

		@Override
		public boolean matches(String path) {
			return this.antMatcher.match(this.pattern, path);
		}

		@Override
		public Map<String, String> extractUriTemplateVariables(String path) {
			return this.antMatcher.extractUriTemplateVariables(this.pattern, path);
		}

		private static AntPathMatcher createMatcher(boolean caseSensitive) {
			AntPathMatcher matcher = new AntPathMatcher();
			matcher.setTrimTokens(false);
			matcher.setCaseSensitive(caseSensitive);
			return matcher;
		}
	}

	/**
	 * Optimized matcher for trailing wildcards
	 */
	private static class SubpathMatcher implements Matcher {
		private final String subpath;
		private final int length;
		private final boolean caseSensitive;

		private SubpathMatcher(String subpath, boolean caseSensitive) {
			assert!subpath.contains("*");
			this.subpath = caseSensitive ? subpath : subpath.toLowerCase();
			this.length = subpath.length();
			this.caseSensitive = caseSensitive;
		}

		@Override
		public boolean matches(String path) {
			if (!this.caseSensitive) {
				path = path.toLowerCase();
			}
			return path.startsWith(this.subpath)
					&& (path.length() == this.length || path.charAt(this.length) == '/');
		}

		@Override
		public Map<String, String> extractUriTemplateVariables(String path) {
			return Collections.emptyMap();
		}
	}

}
