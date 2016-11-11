package it.govpay.stampe.pdf.avvisoPagamento;

import java.io.OutputStream;
import java.io.Serializable;
import java.util.Properties;

import org.apache.logging.log4j.Logger;

import it.govpay.model.AvvisoPagamento;

public interface IAvvisoPagamento extends Serializable {

	/****
	 * 
	 * 
	 * 
	 * @param pathLoghi // directory dove si trova il logo dell'ente creditore;
	 * @param avviso dati dell'avviso di pagamento da inserire nel pdf
	 * @param os outputStream dove scrivere il pdf
	 * @param log logger
	 * @return eventuale messaggio di errore/warnig da visualizzare;
	 * 
	 * @throws Exception
	 */
	public String getPdfAvvisoPagamento(String pathLoghi, AvvisoPagamento avviso, Properties properties, OutputStream os, Logger log) throws Exception;
		
}
