/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2017 Link.it srl (http://www.link.it).
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
package it.govpay.core.business;

import it.gov.digitpa.schemas._2011.ws.paa.NodoChiediSceltaWISP;
import it.gov.digitpa.schemas._2011.ws.paa.NodoChiediSceltaWISPRisposta;
import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.core.business.model.SceltaWISP;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.exceptions.NdpException.FaultNodo;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.core.utils.client.BasicClient.ClientException;
import it.govpay.core.utils.client.NodoClient.Azione;
import it.govpay.bd.model.Canale;
import it.govpay.bd.model.Dominio;
import it.govpay.model.Intermediario;
import it.govpay.model.Portale;
import it.govpay.bd.model.Stazione;
import it.govpay.model.Canale.TipoVersamento;
import it.govpay.core.utils.client.NodoClient;
import it.govpay.servizi.commons.EsitoOperazione;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.logger.beans.Property;

public class Wisp extends BasicBD {
	
	public Wisp(BasicBD basicBD) {
		super(basicBD);
	}
	
	public SceltaWISP chiediScelta(Portale portaleAutenticato, Dominio dominio, String codKeyPA, String codKeyWISP) throws ServiceException, GovPayException {
		return chiediScelta(portaleAutenticato, dominio, codKeyPA, codKeyWISP, true);
	}

	public SceltaWISP chiediScelta(Portale portaleAutenticato, Dominio dominio, String codKeyPA, String codKeyWISP, boolean throwExceptionOnFault) throws ServiceException, GovPayException {
		String idTransaction = null;
		GpContext ctx = GpThreadLocal.get();
		NodoClient client = null;
		try {
			idTransaction = ctx.openTransaction();
			ctx.setupNodoClient(dominio.getStazione().getCodStazione(), dominio.getCodDominio(), Azione.nodoChiediSceltaWISP);
			ctx.getContext().getRequest().addGenericProperty(new Property("codDominio", dominio.getCodDominio()));
			ctx.getContext().getRequest().addGenericProperty(new Property("codKeyPA", codKeyPA));
			GpThreadLocal.get().getContext().getRequest().addGenericProperty(new Property("codKeyWISP", codKeyWISP));
			GpThreadLocal.get().log("wisp.risoluzioneWisp");
			
			
			Stazione stazione = AnagraficaManager.getStazione(this, dominio.getIdStazione());
			Intermediario intermediario = AnagraficaManager.getIntermediario(this, stazione.getIdIntermediario());
			closeConnection();
			client = new NodoClient(intermediario, this);
			NodoChiediSceltaWISP nodoChiediSceltaWISP = new NodoChiediSceltaWISP();
			nodoChiediSceltaWISP.setIdentificativoDominio(dominio.getCodDominio());
			nodoChiediSceltaWISP.setIdentificativoIntermediarioPA(intermediario.getCodIntermediario());
			nodoChiediSceltaWISP.setIdentificativoStazioneIntermediarioPA(stazione.getCodStazione());
			nodoChiediSceltaWISP.setKeyPA(codKeyPA);
			nodoChiediSceltaWISP.setKeyWISP(codKeyWISP);
			nodoChiediSceltaWISP.setPassword(stazione.getPassword());
			NodoChiediSceltaWISPRisposta risposta = client.nodoChiediSceltaWISP(nodoChiediSceltaWISP, intermediario.getDenominazione());
			setupConnection(GpThreadLocal.get().getTransactionId());
			
			if(risposta.getFault() != null) {
				FaultNodo fault = FaultNodo.valueOf(risposta.getFault().getFaultCode());
				if(throwExceptionOnFault) {
					switch (fault) {
					case PPT_WISP_SESSIONE_SCONOSCIUTA:
						ctx.log("wisp.risoluzioneWispKoSconosciuta");
						throw new GovPayException(risposta.getFault());
					case PPT_WISP_TIMEOUT_RECUPERO_SCELTA:
						ctx.log("wisp.risoluzioneWispKoTimeout");
						throw new GovPayException(risposta.getFault());
					default:
						ctx.log("wisp.risoluzioneWispKo", risposta.getFault().getFaultCode());
						throw new GovPayException(risposta.getFault());
					}
				} else {
					SceltaWISP scelta = new SceltaWISP();
					scelta.setFault(fault); 
					return scelta;
				}
			} else {
				SceltaWISP scelta = new SceltaWISP();
				switch (risposta.getEffettuazioneScelta()) {
				case SI:
					ctx.getContext().getResponse().addGenericProperty(new Property("codPsp", risposta.getIdentificativoPSP()));
					ctx.getContext().getResponse().addGenericProperty(new Property("codCanale", risposta.getIdentificativoCanale()));
					ctx.getContext().getResponse().addGenericProperty(new Property("tipoVersamento", risposta.getTipoVersamento().toString()));
					ctx.log("wisp.risoluzioneWispOkCanale");
					try {
						Canale canale = AnagraficaManager.getCanale(this,  risposta.getIdentificativoPSP(), risposta.getIdentificativoCanale(), TipoVersamento.toEnum(risposta.getTipoVersamento().toString()));
						canale.setPsp(canale.getPsp(this));
						scelta.setCanale(canale);
					} catch (NotFoundException e) {
						throw new GovPayException(EsitoOperazione.WISP_002,  risposta.getIdentificativoPSP(), risposta.getIdentificativoCanale(), risposta.getTipoVersamento().toString());
					}
					scelta.setPagaDopo(false);
					scelta.setSceltaEffettuata(true);
					return scelta;
				case NO:
					ctx.log("integrazione.risoluzioneWispOkNoScelta");
					scelta.setPagaDopo(false);
					scelta.setSceltaEffettuata(false);
					return scelta;
				case PO:
					ctx.log("integrazione.risoluzioneWispOkPagaDopo");
					scelta.setPagaDopo(true);
					scelta.setSceltaEffettuata(true);
					return scelta;
				}
				return scelta;
			} 
		} 
		catch (ClientException e) {
			throw new GovPayException(EsitoOperazione.NDP_000, e);
		} finally {
			ctx.closeTransaction(idTransaction);
		}
	}
}
