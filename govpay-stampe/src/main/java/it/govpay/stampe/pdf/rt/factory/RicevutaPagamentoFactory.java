package it.govpay.stampe.pdf.rt.factory;

import org.apache.logging.log4j.Logger;

import it.govpay.stampe.pdf.avvisoPagamento.IAvvisoPagamento;
import it.govpay.stampe.pdf.avvisoPagamento.utils.AvvisoPagamentoProperties;
import it.govpay.stampe.pdf.rt.IRicevutaPagamento;
import it.govpay.stampe.pdf.rt.utils.RicevutaPagamentoProperties;

public class RicevutaPagamentoFactory {

	public static IRicevutaPagamento getRicevutaPagamentoBuilder(String ricevutaPagamentoImplClass,Logger log) throws Exception{

		IRicevutaPagamento instance = null;
		try{

			if(ricevutaPagamentoImplClass == null) {
				ricevutaPagamentoImplClass = RicevutaPagamentoProperties.getInstance().getDefaultImplClassName();
			}

			Class<?> ricevutaPagamentoClass = Class.forName(ricevutaPagamentoImplClass);

			Object ricevutaPagamentoClassObjectImpl = ricevutaPagamentoClass.newInstance();

			if(!(ricevutaPagamentoClassObjectImpl instanceof IRicevutaPagamento)) {
				throw new Exception("La classe ["+ricevutaPagamentoImplClass+"] dovrebbe implementare l'interfaccia " + IRicevutaPagamento.class);
			}

			instance = (IRicevutaPagamento) ricevutaPagamentoClassObjectImpl;

		}catch(Exception e){
			log.error("Errore durante l'instance della classe ["+ricevutaPagamentoImplClass+"]: "+ e.getMessage(),e); 
			throw e;
		}
		return instance;
	}
	
}
