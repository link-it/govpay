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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.govpay.stampe.model.v2.AvvisoPagamentoInput;
import it.govpay.stampe.model.v2.Etichette;
import it.govpay.stampe.model.v2.PaginaAvvisoDoppia;
import it.govpay.stampe.model.v2.PaginaAvvisoSingola;
import it.govpay.stampe.model.v2.PagineAvviso;
import it.govpay.stampe.model.v2.RataAvviso;
import it.govpay.stampe.pdf.avvisoPagamento.AvvisoPagamentoPdf;
import it.govpay.stampe.pdf.avvisoPagamento.utils.AvvisoPagamentoProperties;

/**
 * Issue #918: avviso bilingue con la data di scadenza nascosta (#898).
 * Gli input riproducono quelli che AvvisoPagamentoV2Utils passa al template:
 * senza data si stampa solo il nome della rata, senza le etichette "... entro il".
 */
class AvvisoPagamentoV2DataNascostaTest {

	private static final Logger log = LoggerFactory.getLogger(AvvisoPagamentoV2DataNascostaTest.class);
	private static final String COD_DOMINIO = "01234567890";
	private static final String DATA = "31/12/2026";

	private static AvvisoPagamentoProperties properties;
	private static Properties labels;

	@BeforeAll
	static void init() throws Exception {
		properties = AvvisoPagamentoProperties.newInstance(null);
		labels = new Properties();
		try (InputStream is = Files.newInputStream(Path.of("../../ear/src/main/application/properties/avvisi.properties"))) {
			labels.load(is);
		}
	}

	@ParameterizedTest(name = "{0} rate, postale {1}")
	@CsvSource({ "1, false", "2, false", "3, false", "3, true" })
	void dataNascosta(int numeroRate, boolean poste) throws Exception {
		String testo = testo(avviso(numeroRate, poste, null));

		for (String lingua : new String[] { "it", "sl" }) {
			assertFalse(testo.contains(l(lingua, "rata_unica_entro_il")), testo);
			for (int n = 1; n <= numeroRate; n++)
				assertFalse(testo.contains(l(lingua, "rata_entro_il", n)), testo);
		}
		assertFalse(testo.contains("null"), testo);
		if (numeroRate == 1) {
			assertTrue(testo.contains(l("it", "rata_unica") + " · " + l("sl", "rata_unica")), testo);
		} else {
			for (int n = 1; n <= numeroRate; n++) {
				assertTrue(testo.contains(l("it", "numero_rata", n) + " · " + l("sl", "numero_rata", n)), testo);
				assertFalse(testo.contains(l("it", "numero_rata", n) + " " + l("it", "numero_rata", n)), testo);
			}
		}
	}

	@ParameterizedTest(name = "{0} rate, postale {1}")
	@CsvSource({ "1, false", "2, false", "3, false", "3, true" })
	void dataPresente(int numeroRate, boolean poste) throws Exception {
		String testo = testo(avviso(numeroRate, poste, DATA));

		assertTrue(testo.contains(DATA), testo);
		assertTrue(testo.contains(numeroRate == 1 ? l("it", "rata_unica_entro_il") : l("it", "prima_rata")), testo);
	}

	private static String testo(AvvisoPagamentoInput input) throws Exception {
		byte[] pdf = AvvisoPagamentoPdf.getInstance().creaAvvisoV2(log, input, COD_DOMINIO, properties);
		try (PDDocument documento = Loader.loadPDF(pdf)) {
			return new PDFTextStripper().getText(documento);
		}
	}

	private static String l(String lingua, String nome, Object... parametri) {
		String valore = labels.getProperty(lingua + "." + nome);
		return parametri.length > 0 ? MessageFormat.format(valore, parametri) : valore;
	}

	/** Documento come lo compone AvvisoPagamentoV2Utils.fromDocumento (numeroRate 1: pendenza senza rate). */
	private static AvvisoPagamentoInput avviso(int numeroRate, boolean poste, String data) throws Exception {
		AvvisoPagamentoInput input = intestazione(poste);
		List<Object> pagine = input.getPagine().getSingolaOrDoppia();

		if (numeroRate == 1) {
			RataAvviso rata = rata(null, data, poste);
			pagine.add(singola(rata));
			return input;
		}

		input.getEtichette().getItaliano().setNota1(l("it", "nota_prima_rata", numeroRate));
		input.getEtichette().getTraduzione().setNota1(l("sl", "nota_prima_rata", numeroRate));

		List<RataAvviso> rate = new ArrayList<>();
		for (int n = 1; n <= numeroRate; n++)
			rate.add(rata(n, data, poste));

		if (numeroRate % 2 == 0) {
			// pagina principale con la data della prima rata e l'importo totale
			RataAvviso principale = new RataAvviso();
			principale.setData(data);
			principale.setImporto(100.0 * numeroRate);
			pagine.add(singola(principale));
		} else {
			// la pagina principale coincide con la prima rata
			RataAvviso prima = rate.remove(0);
			if (data != null) {
				prima.setScadenza(l("it", "prima_rata"));
				prima.setScadenzaTra(l("sl", "prima_rata"));
			}
			pagine.add(singola(prima));
		}

		for (int i = 0; i < rate.size(); i += 2) {
			PaginaAvvisoDoppia pagina = new PaginaAvvisoDoppia();
			for (RataAvviso rata : rate.subList(i, Math.min(i + 2, rate.size()))) {
				if (data == null) {
					rata.setScadenza(null);
					rata.setScadenzaTra(null);
				}
				pagina.getRata().add(rata);
			}
			pagine.add(pagina);
		}
		return input;
	}

	private static PaginaAvvisoSingola singola(RataAvviso rata) {
		PaginaAvvisoSingola pagina = new PaginaAvvisoSingola();
		pagina.setRata(rata);
		return pagina;
	}

	/** Come AvvisoPagamentoV2Utils.getRata. */
	private static RataAvviso rata(Integer n, String data, boolean poste) {
		RataAvviso rata = new RataAvviso();
		if (n == null) {
			rata.setScadenza(l("it", data != null ? "rata_unica_entro_il" : "rata_unica"));
			rata.setScadenzaTra(l("sl", data != null ? "rata_unica_entro_il" : "rata_unica"));
		} else {
			rata.setNumeroRata(l("it", "numero_rata", n));
			rata.setNumeroRataTra(l("sl", "numero_rata", n));
			rata.setScadenza(data != null ? l("it", "rata_entro_il", n) : rata.getNumeroRata());
			rata.setScadenzaTra(data != null ? l("sl", "rata_entro_il", n) : rata.getNumeroRataTra());
			rata.setScadenzaUnica(l("it", data != null ? "rata_unica_entro_il" : "rata_unica"));
			rata.setScadenzaUnicaTra(l("sl", data != null ? "rata_unica_entro_il" : "rata_unica"));
		}
		int k = n == null ? 0 : n;
		rata.setCodiceAvviso("3010 0000 0000 0000 0" + k);
		rata.setQrCode("PAGOPA|002|30100000000000000" + k + "|" + COD_DOMINIO + "|10000");
		if (poste) {
			rata.setNumeroCcPostale("123456789012");
			rata.setCodiceAvvisoPostale(rata.getCodiceAvviso());
			rata.setDataMatrix("codfase=NBPA;1830100000000000000" + k + "12123456789012100000003896" + COD_DOMINIO + "RSSMRA30A01H501IMario RossiOggettoA");
			rata.setAutorizzazione("AUT. 123");
		}
		rata.setImporto(100.0);
		rata.setData(data);
		return rata;
	}

	private static AvvisoPagamentoInput intestazione(boolean poste) throws Exception {
		AvvisoPagamentoInput input = new AvvisoPagamentoInput();
		AvvisoPagamentoInput.Etichette etichette = new AvvisoPagamentoInput.Etichette();
		Etichette it = valorizza(new Etichette());
		Etichette sl = valorizza(new Etichette());
		it.setOggettoDelPagamento("Oggetto del pagamento");
		sl.setOggettoDelPagamento("Predmet placila");
		it.setPrimaRata(l("it", "prima_rata"));
		sl.setPrimaRata(l("sl", "prima_rata"));
		it.setEntro(l("it", "entro_il"));
		sl.setEntro(l("sl", "entro_il"));
		etichette.setItaliano(it);
		etichette.setTraduzione(sl);
		input.setEtichette(etichette);
		input.setCfEnte(COD_DOMINIO);
		input.setEnteCreditore("Ente di prova");
		input.setCfDestinatario("RSSMRA30A01H501I");
		input.setNomeCognomeDestinatario("Mario Rossi");
		input.setCbill("AAAAA");
		input.setIntestatarioContoCorrentePostale("Ente di prova");
		if (poste)
			input.setPoste(true);
		input.setPagine(new PagineAvviso());
		return input;
	}

	/** Valorizza tutte le etichette con il proprio nome, come segnaposto. */
	private static <T> T valorizza(T bean) throws Exception {
		for (Method m : bean.getClass().getMethods()) {
			if (m.getName().startsWith("set") && m.getParameterCount() == 1 && m.getParameterTypes()[0] == String.class)
				m.invoke(bean, m.getName().substring(3));
		}
		return bean;
	}
}
