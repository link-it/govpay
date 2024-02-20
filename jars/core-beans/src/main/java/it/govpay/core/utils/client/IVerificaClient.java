/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2024 Link.it srl (http://www.link.it).
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
package it.govpay.core.utils.client;

import it.govpay.core.beans.EventoContext;
import it.govpay.core.beans.commons.Versamento;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.exceptions.VersamentoAnnullatoException;
import it.govpay.core.exceptions.VersamentoDuplicatoException;
import it.govpay.core.exceptions.VersamentoNonValidoException;
import it.govpay.core.exceptions.VersamentoScadutoException;
import it.govpay.core.exceptions.VersamentoSconosciutoException;
import it.govpay.core.utils.client.exception.ClientException;
/**
 * Interfaccia che descrive le funzioni di verifica di una pendenza verso l'applicativo gestore.
 * 
 * In sede di pagamento, la piattaforma GovPay potrebbe aver necessità di richiedere all'applicativo gestore i dati aggiornati della pendenza oggetto del pagamento. Questo può accadere in due scenari:
 *
 * La pendenza associata al pagamento non è disponibile in GovPay
 * La data di validità presente nella pendenza oggetto del pagamento è decorsa.
 * Le informazioni restituite dal servizio vanno ad aggiornare la pendenza negli archivi di GovPay ed utilizzate per perfezionare il pagamento.
 * 
 * @author Pintori Giuliano (pintori@link.it)
 * 
 */
public interface IVerificaClient {
	
	/**
	 * Verifica della pendenza tramite le API configurate nel connettore integrazione dell'applicazione.
	 * 
	 * 
	 * @param codVersamentoEnte Identificativo della pendenza
	 * @param bundlekey 
	 * @param codUnivocoDebitore Identificativo del debitore (CF o Partita IVA)
	 * @param codDominio Identificativo Ente Creditore
	 * @param iuv IUV/NumeroAvviso
	 * @return
	 * @throws ClientException 
	 * @throws VersamentoAnnullatoException 
	 * @throws VersamentoDuplicatoException
	 * @throws VersamentoScadutoException
	 * @throws VersamentoSconosciutoException
	 * @throws VersamentoNonValidoException
	 * @throws GovPayException
	 */
	public Versamento verificaPendenza(String codVersamentoEnte, String bundlekey, String codUnivocoDebitore, String codDominio, String iuv) 
			throws ClientException, VersamentoAnnullatoException, VersamentoDuplicatoException, VersamentoScadutoException, 
				VersamentoSconosciutoException, VersamentoNonValidoException, GovPayException;
	
	
	/**
	 * Verifica di una pendenza tramite le API configurate nel connettore integrazione dell'applicazione.
	 * Il contenuto inviato e' un JSON contenente dei dati personalizzati che verranno risolti all'interno dell'applicativo destinazione.
	 * 
	 * @param codDominio Identificativo Ente Creditore
	 * @param codTipoVersamento Identificativo Tipo Pendenza
	 * @param codUnitaOperativa Identificativo Unita' Operativa
	 * @param jsonBody Body della richiesta in formato JSON
	 * @return
	 * @throws ClientException
	 * @throws VersamentoAnnullatoException
	 * @throws VersamentoDuplicatoException
	 * @throws VersamentoScadutoException
	 * @throws VersamentoSconosciutoException
	 * @throws VersamentoNonValidoException
	 * @throws GovPayException
	 */
	public Versamento inoltroPendenza(String codDominio, String codTipoVersamento, String codUnitaOperativa, String jsonBody) 
			throws ClientException, VersamentoAnnullatoException, VersamentoDuplicatoException, VersamentoScadutoException,
				VersamentoSconosciutoException, VersamentoNonValidoException, GovPayException; 
	
	
	/**
	 * Resistuisce le informazioni registrate per il giornale degli eventi
	 * 
	 * @return {@link EventoContext}
	 */
	public EventoContext getEventoCtx();
}
