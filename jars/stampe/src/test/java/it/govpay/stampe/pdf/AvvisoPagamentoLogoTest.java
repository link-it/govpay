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

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.govpay.stampe.model.v2.AvvisoPagamentoInput;
import it.govpay.stampe.model.v2.Etichette;
import it.govpay.stampe.model.v2.PagineAvviso;
import it.govpay.stampe.model.v2.PaginaAvvisoSingola;
import it.govpay.stampe.model.v2.RataAvviso;
import it.govpay.stampe.pdf.avvisoPagamento.AvvisoPagamentoPdf;
import it.govpay.stampe.pdf.avvisoPagamento.utils.AvvisoPagamentoProperties;

/**
 * Issue #902: l'avviso di pagamento V2 deve essere prodotto con qualunque logo
 * dell'ente, anche quando il logo non si puo' stampare.
 */
class AvvisoPagamentoLogoTest {

	private static final Logger log = LoggerFactory.getLogger(AvvisoPagamentoLogoTest.class);
	private static final String COD_DOMINIO = "01234567890";

	private static AvvisoPagamentoProperties properties;

	@BeforeAll
	static void init() throws Exception {
		properties = AvvisoPagamentoProperties.newInstance(null);
	}

	static List<LoghiDiProva.Logo> loghi() {
		return LoghiDiProva.tutti();
	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("loghi")
	void avvisoStampatoConOgniLogo(LoghiDiProva.Logo logo) throws Exception {
		byte[] pdf = AvvisoPagamentoPdf.getInstance().creaAvvisoV2(log, avviso(logo.valore()), COD_DOMINIO, properties);

		assertTrue(pdf.length > 0 && new String(pdf, 0, 5, StandardCharsets.US_ASCII).equals("%PDF-"), "Il documento prodotto non e' un PDF");
	}

	static AvvisoPagamentoInput avviso(String logo) throws Exception {
		AvvisoPagamentoInput input = new AvvisoPagamentoInput();
		AvvisoPagamentoInput.Etichette etichette = new AvvisoPagamentoInput.Etichette();
		etichette.setItaliano(valorizza(new Etichette()));
		input.setEtichette(etichette);
		input.setLogoEnte(logo);
		input.setCfEnte(COD_DOMINIO);
		input.setEnteCreditore("Ente di prova");
		input.setSettoreEnte("Settore");
		input.setInfoEnte("Via Roma 1, Pisa");
		input.setCfDestinatario("RSSMRA30A01H501I");
		input.setNomeCognomeDestinatario("Mario Rossi");
		input.setIndirizzoDestinatario1("Via Verdi 2");
		input.setIndirizzoDestinatario2("56100 Pisa (PI)");
		input.setCbill("AAAAA");

		RataAvviso rata = valorizza(new RataAvviso());
		rata.setImporto(10.0);
		rata.setData("31/12/2026");
		rata.setCodiceAvviso("3010 0000 0000 0000 00");
		rata.setQrCode("PAGOPA|002|301000000000000000|" + COD_DOMINIO + "|1000");
		rata.setDataMatrix(null);
		rata.setCodiceAvvisoPostale(null);
		rata.setNumeroCcPostale(null);
		PaginaAvvisoSingola pagina = new PaginaAvvisoSingola();
		pagina.setRata(rata);
		input.setPagine(new PagineAvviso());
		input.getPagine().getSingolaOrDoppia().add(pagina);
		return input;
	}

	/** Valorizza tutte le stringhe del bean, come fanno le label degli avvisi. */
	private static <T> T valorizza(T bean) throws Exception {
		for (Method m : bean.getClass().getMethods()) {
			if (m.getName().startsWith("set") && m.getParameterCount() == 1 && m.getParameterTypes()[0] == String.class) {
				m.invoke(bean, m.getName().substring(3));
			}
		}
		return bean;
	}
}
