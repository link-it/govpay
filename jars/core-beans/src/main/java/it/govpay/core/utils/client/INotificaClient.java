package it.govpay.core.utils.client;

import it.govpay.core.beans.EventoContext;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.utils.client.exception.ClientException;
import it.govpay.model.Notifica;

/**
 * La piattaforma GovPay prevede l'invio di una notifica alla conclusione con successo di una transazione di pagamento pagoPA, 
 * ovvero a fronte dell'acquisizione di una RT, indipendentemente dalla modalità di pagamento o dall'esito.
 * 
 * @author Pintori Giuliano (pintori@link.it)
 * 
 */
public interface INotificaClient {

	/***
	 * La notifica di un pagamento corrisponde ad una ricevuta pagoPA, sia questa veicolata nella forma di RT o di recepit, di esito positivo. 
	 * Le notifiche sono inviate contestualmente all'evento che le genera. 
	 * In caso di errori di consegna (ovvero senza risposta HTTP 2xx), le notifiche sono rispedite in tempi successivi. 
	 * Se la ricevuta risulta gia' stata acquisita, puo' essere ignorata ed esitata con successo per interromperne la rispedizione.
	 * 
	 * @param notifica
	 * @return
	 * @throws ClientException
	 * @throws GovPayException
	 */
	public byte[] invoke(Notifica notifica) throws ClientException, GovPayException;
	
	
	/**
	 * Resistuisce le informazioni registrate per il giornale degli eventi
	 * 
	 * @return {@link EventoContext}
	 */
	public EventoContext getEventoCtx();
}
