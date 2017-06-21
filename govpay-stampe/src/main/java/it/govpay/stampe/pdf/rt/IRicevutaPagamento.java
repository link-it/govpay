package it.govpay.stampe.pdf.rt;

import java.io.OutputStream;
import java.util.Properties;

import org.apache.logging.log4j.Logger;

import it.govpay.model.RicevutaPagamento;

public interface IRicevutaPagamento {

	/****
	 * 
	 * @param pathLoghi // directory dove si trova il logo dell'ente creditore;
	 * @param rt dati della ricevuta di pagamento da inserire nel pdf
	 * @param causale causale del pagamento
	 * @param os outputStream dove scrivere il pdf
	 * @param log logger
	 * @return eventuale messaggio di errore/warnig da visualizzare;
	 * 
	 * @throws Exception
	 */
	public String getPdfRicevutaPagamento(RicevutaPagamento ricevuta, Properties properties, OutputStream os, Logger log) throws Exception;
		
}
