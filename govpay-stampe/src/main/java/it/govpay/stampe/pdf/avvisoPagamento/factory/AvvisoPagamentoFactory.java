package it.govpay.stampe.pdf.avvisoPagamento.factory;

import org.apache.logging.log4j.Logger;

import it.govpay.stampe.pdf.avvisoPagamento.IAvvisoPagamento;
import it.govpay.stampe.pdf.avvisoPagamento.utils.AvvisoPagamentoProperties;

public class AvvisoPagamentoFactory {
	


	public static IAvvisoPagamento getAvvisoPagamentoBuilder(String avvisoPagamentoImplClass,Logger log) throws Exception{

		IAvvisoPagamento instance = null;
		try{

			if(avvisoPagamentoImplClass == null) {
				avvisoPagamentoImplClass = AvvisoPagamentoProperties.getInstance().getDefaultImplClassName();
			}

			Class<?> avvisoPagamentoClass = Class.forName(avvisoPagamentoImplClass);

			Object avvisoPagamentoClassObjectImpl = avvisoPagamentoClass.newInstance();

			if(!(avvisoPagamentoClassObjectImpl instanceof IAvvisoPagamento)) {
				throw new Exception("La classe ["+avvisoPagamentoImplClass+"] dovrebbe implementare l'interfaccia " + IAvvisoPagamento.class);
			}

			instance = (IAvvisoPagamento) avvisoPagamentoClassObjectImpl;

		}catch(Exception e){
			log.error("Errore durante l'instance della classe ["+avvisoPagamentoImplClass+"]: "+ e.getMessage(),e); 
			throw e;
		}
		return instance;
	}


}
