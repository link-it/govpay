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
package it.govpay.stampe.pdf;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

/**
 * Logo dell'ente nei documenti stampati (Issue #902).
 *
 * I template leggono il logo con onErrorType="Blank", ma un SVG viene interpretato da Batik solo
 * durante l'export del PDF: un SVG che Batik rifiuta (risorse esterne, riferimenti mancanti) fa
 * fallire la stampa invece di lasciare il logo vuoto. Questa classe permette di verificare un logo
 * prima di salvarlo e di ripetere la stampa con il logo di default quando quello del dominio non
 * si puo' usare.
 */
public final class LogoEnte {

	private static final String TEMPLATE_VERIFICA = "/logo/VerificaLogoEnte.jrxml";
	private static final String PARAMETRO_LOGO = "logo_ente";
	private static final int MAX_LUNGHEZZA_CAUSA = 300;
	private static final int MAX_LUNGHEZZA_MESSAGGIO = 4 * MAX_LUNGHEZZA_CAUSA;

	private static JasperReport reportVerifica = null;

	private LogoEnte() {
	}

	/**
	 * Stampa il logo con lo stesso motore dei documenti.
	 *
	 * @param logo il logo come viene salvato nel dominio: base64 o data URI
	 * @throws LogoEnteNonStampabileException se il logo nei documenti non comparirebbe o ne bloccherebbe la stampa
	 */
	public static void verifica(String logo) throws LogoEnteNonStampabileException {
		if (logo == null || logo.isBlank()) {
			throw new LogoEnteNonStampabileException("il logo e' vuoto", null);
		}

		JasperReport report = getReportVerifica();
		DefaultJasperReportsContext context = DefaultJasperReportsContext.getInstance();
		Map<String, Object> parameters = new HashMap<>();
		parameters.put(PARAMETRO_LOGO, logo);
		try {
			JasperPrint jasperPrint = JasperFillManager.getInstance(context).fill(report, parameters, new JREmptyDataSource());
			JasperExportManager.getInstance(context).exportToPdf(jasperPrint);
		} catch (JRException | RuntimeException e) {
			throw new LogoEnteNonStampabileException(descriviCausa(e), e);
		}
	}

	/**
	 * Da chiamare quando la stampa di un documento fallisce: se uno dei loghi del dominio non e'
	 * stampabile registra l'anomalia e restituisce true, e il chiamante ripete la stampa una sola volta
	 * con il logo di default. Se i loghi sono stampabili la causa e' altrove e restituisce false.
	 *
	 * @param loghi i loghi del dominio presenti nel documento; gli elementi null sono ignorati
	 */
	public static boolean usaLogoDiDefault(Logger log, String documento, String codDominio, String... loghi) {
		for (String logo : loghi) {
			if (logo == null) {
				continue;
			}
			try {
				verifica(logo);
			} catch (LogoEnteNonStampabileException e) {
				log.warn("Logo del dominio [{}] non stampabile nel documento [{}]: {}. Il documento viene prodotto con il logo di default.", codDominio, documento, e.getMessage());
				return true;
			}
		}
		return false;
	}

	/**
	 * Messaggio dell'errore piu' interno, senza i data URI e le posizioni prive di significato
	 * (null:0) che Batik antepone ai suoi messaggi.
	 */
	public static String descriviCausa(Throwable e) {
		String messaggio = null;
		for (Throwable t = e; t != null; t = (t.getCause() == t ? null : t.getCause())) {
			if (t instanceof NullPointerException && t.getStackTrace().length > 0 && t.getStackTrace()[0].getClassName().startsWith("org.apache.batik.")) {
				// es. @import in un foglio di stile: Batik non risolve risorse senza l'URL del documento
				return "l'SVG fa riferimento a una risorsa esterna (ad esempio un @import CSS) che non viene caricata";
			}
			if (t.getMessage() != null && !t.getMessage().isBlank()) {
				messaggio = t.getMessage();
			}
		}
		if (messaggio == null) {
			return e.getClass().getSimpleName();
		}

		// il messaggio puo' riportare parti dell'SVG, che arriva dall'ente: si limita prima delle sostituzioni
		if (messaggio.length() > MAX_LUNGHEZZA_MESSAGGIO) {
			messaggio = messaggio.substring(0, MAX_LUNGHEZZA_MESSAGGIO);
		}
		String causa = StringUtils.stripEnd(messaggio
				.replaceAll("data:[^\\s\"']{20,}", "data:...")
				.replaceAll("(?m)^\\S*:-?\\d+\\s*$", "")
				.replaceAll("\\s+", " ")
				.trim(), ".");
		return causa.length() > MAX_LUNGHEZZA_CAUSA ? causa.substring(0, MAX_LUNGHEZZA_CAUSA) + "..." : causa;
	}

	/** Errore di installazione, non del logo: per questo non e' una LogoEnteNonStampabileException. */
	private static synchronized JasperReport getReportVerifica() {
		if (reportVerifica == null) {
			try (InputStream is = LogoEnte.class.getResourceAsStream(TEMPLATE_VERIFICA)) {
				if (is == null) {
					throw new IllegalStateException("Template di verifica del logo non trovato: " + TEMPLATE_VERIFICA);
				}
				reportVerifica = JasperCompileManager.compileReport(is);
			} catch (IOException | JRException e) {
				throw new IllegalStateException("Impossibile compilare il template di verifica del logo " + TEMPLATE_VERIFICA, e);
			}
		}
		return reportVerifica;
	}
}
