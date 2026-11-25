/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2026 Link.it srl (http://www.link.it).
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
package it.govpay.web.console.filter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Imposta gli header di sicurezza sulle risposte della console:
 * Content-Security-Policy (o la sua variante Report-Only), X-Frame-Options e X-Content-Type-Options.
 *
 * La policy e la modalita' si leggono da govpay.properties, con la stessa precedenza degli altri war:
 * proprieta' di sistema, poi il file esterno nella cartella it.govpay.resource.path, poi il file interno.
 */
public class SecurityHeadersFilter implements Filter {

	public static final String PROP_CSP = "it.govpay.backoffice.gui.csp";
	public static final String PROP_CSP_MODE = "it.govpay.backoffice.gui.csp.mode";
	private static final String PROP_RESOURCE_PATH = "it.govpay.resource.path";
	private static final String PROPERTIES_FILE_NAME = "govpay.properties";

	// Angular 5 compila i template nel browser (eval) e il bundle degli stili contiene font di Google Fonts
	public static final String DEFAULT_CSP = "default-src 'self'; script-src 'self' 'unsafe-eval'; style-src 'self' 'unsafe-inline'; "
			+ "font-src 'self' data: https://fonts.gstatic.com; img-src 'self' data:; connect-src 'self'; "
			+ "frame-ancestors 'none'; base-uri 'self'; object-src 'none'; form-action 'self'";

	private static final String HEADER_CSP = "Content-Security-Policy";
	private static final String HEADER_CSP_REPORT_ONLY = "Content-Security-Policy-Report-Only";

	enum Modalita { ENFORCE, REPORT_ONLY, OFF }

	private String csp;
	private Modalita modalita;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		ServletContext context = filterConfig.getServletContext();
		Properties[] props = leggiProperties(context);

		String valore = getProperty(PROP_CSP, props);
		this.csp = valore != null ? valore : DEFAULT_CSP;

		String mode = getProperty(PROP_CSP_MODE, props);
		this.modalita = parseModalita(mode);
		if (this.modalita == null) {
			context.log("Valore non valido per " + PROP_CSP_MODE + ": " + mode + ". Uso enforce.");
			this.modalita = Modalita.ENFORCE;
		}

		context.log("Content-Security-Policy della console in modalita' " + this.modalita + (valore != null ? " con policy configurata" : " con policy predefinita"));
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletResponse resp = (HttpServletResponse) response;

		resp.setHeader("X-Frame-Options", "DENY");
		resp.setHeader("X-Content-Type-Options", "nosniff");

		if (this.modalita == Modalita.ENFORCE) {
			resp.setHeader(HEADER_CSP, this.csp);
		} else if (this.modalita == Modalita.REPORT_ONLY) {
			resp.setHeader(HEADER_CSP_REPORT_ONLY, this.csp);
		}

		chain.doFilter(request, response);
	}

	static Modalita parseModalita(String valore) {
		if (valore == null) {
			return Modalita.ENFORCE;
		}
		switch (valore.trim().toLowerCase(Locale.ROOT)) {
		case "enforce":
			return Modalita.ENFORCE;
		case "report-only":
			return Modalita.REPORT_ONLY;
		case "off":
			return Modalita.OFF;
		default:
			return null;
		}
	}

	private static Properties[] leggiProperties(ServletContext context) {
		Properties interne = new Properties();
		try (InputStream is = SecurityHeadersFilter.class.getResourceAsStream("/" + PROPERTIES_FILE_NAME)) {
			if (is != null) {
				interne.load(is);
			}
		} catch (IOException e) {
			context.log("Impossibile leggere " + PROPERTIES_FILE_NAME + " interno: " + e.getMessage());
		}

		Properties esterne = null;
		String resourceDir = getProperty(PROP_RESOURCE_PATH, new Properties[] { interne });
		if (resourceDir != null) {
			File file = new File(resourceDir, PROPERTIES_FILE_NAME);
			if (file.isFile()) {
				esterne = new Properties();
				try (InputStream is = new FileInputStream(file)) {
					esterne.load(is);
				} catch (IOException e) {
					context.log("Impossibile leggere " + file.getAbsolutePath() + ": " + e.getMessage());
					esterne = null;
				}
			}
		}

		return new Properties[] { esterne, interne };
	}

	// Una proprieta' vuota, o non risolta dal filtro Maven, vale come assente
	private static String getProperty(String nome, Properties[] props) {
		String valore = System.getProperty(nome);
		for (int i = 0; !isValorizzata(valore) && i < props.length; i++) {
			valore = props[i] != null ? props[i].getProperty(nome) : null;
		}
		return isValorizzata(valore) ? valore.trim() : null;
	}

	private static boolean isValorizzata(String valore) {
		return valore != null && !valore.trim().isEmpty() && !valore.trim().startsWith("${");
	}

	@Override
	public void destroy() {
		// Nessuna risorsa da rilasciare
	}
}
