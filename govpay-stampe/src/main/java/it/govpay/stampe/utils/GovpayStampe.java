package it.govpay.stampe.utils;

import org.slf4j.Logger;

import it.govpay.stampe.pdf.avvisoPagamento.utils.AvvisoPagamentoProperties;
import it.govpay.stampe.pdf.prospettoRiscossioni.utils.ProspettoRiscossioniProperties;
import it.govpay.stampe.pdf.rt.utils.RicevutaTelematicaProperties;

public class GovpayStampe {

	/**
	 * Inizializza l'intero modulo delle stampe
	 * @param govpayResourceDir
	 * @throws Exception
	 */
	public synchronized static void init(Logger log, String govpayResourceDir) throws Exception {
		AvvisoPagamentoProperties.newInstance(govpayResourceDir);
		RicevutaTelematicaProperties.newInstance(govpayResourceDir);
		ProspettoRiscossioniProperties.newInstance(govpayResourceDir);
	}
}
