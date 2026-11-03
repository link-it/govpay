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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Issue #902: verifica del logo dell'ente prima del salvataggio.
 */
class LogoEnteTest {

	private static final Logger log = LoggerFactory.getLogger(LogoEnteTest.class);

	static List<LoghiDiProva.Logo> loghi() {
		return LoghiDiProva.tutti();
	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("loghi")
	void verifica(LoghiDiProva.Logo logo) {
		if (logo.stampabile()) {
			assertDoesNotThrow(() -> LogoEnte.verifica(logo.valore()));
		} else {
			LogoEnteNonStampabileException e = assertThrows(LogoEnteNonStampabileException.class, () -> LogoEnte.verifica(logo.valore()));
			assertFalse(e.getMessage().isBlank(), "La causa deve essere descritta");
			assertTrue(e.getMessage().length() <= 303, "La causa non deve riportare il logo: " + e.getMessage());
		}
	}

	@Test
	void logoVuotoNonStampabile() {
		assertThrows(LogoEnteNonStampabileException.class, () -> LogoEnte.verifica(""));
		assertThrows(LogoEnteNonStampabileException.class, () -> LogoEnte.verifica(null));
	}

	@Test
	void usaLogoDiDefaultSoloSeIlLogoNonEStampabile() {
		String stampabile = logo("png-rgb").valore();
		String nonStampabile = logo("svg-immagine-esterna").valore();

		assertFalse(LogoEnte.usaLogoDiDefault(log, "test", "01234567890", stampabile, null));
		assertTrue(LogoEnte.usaLogoDiDefault(log, "test", "01234567890", stampabile, nonStampabile));
	}

	@Test
	void causaSenzaDataUriNePosizioneBatik() {
		String causa = LogoEnte.descriviCausa(new RuntimeException("null:0\nThe URI \"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAoAAAAGu\"\non element <image> can't be opened."));

		assertEquals("The URI \"data:...\" on element <image> can't be opened", causa);
	}

	private static LoghiDiProva.Logo logo(String nome) {
		return LoghiDiProva.tutti().stream().filter(l -> l.nome().equals(nome)).findFirst().orElseThrow();
	}
}
