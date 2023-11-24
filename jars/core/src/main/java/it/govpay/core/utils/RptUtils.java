/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2025 Link.it srl (http://www.link.it).
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
package it.govpay.core.utils;

import java.util.List;
import java.util.UUID;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.UtilsException;
import org.openspcoop2.utils.service.context.IContext;

import gov.telematici.pagamenti.ws.rpt.NodoInviaCarrelloRPT;
import gov.telematici.pagamenti.ws.rpt.TipoElementoListaRPT;
import gov.telematici.pagamenti.ws.rpt.TipoListaRPT;
import it.gov.digitpa.schemas._2011.pagamenti.CtEnteBeneficiario;
import it.gov.digitpa.schemas._2011.pagamenti.CtIdentificativoUnivocoPersonaG;
import it.gov.digitpa.schemas._2011.pagamenti.StTipoIdentificativoUnivocoPersG;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.Stazione;
import it.govpay.bd.model.UnitaOperativa;
import it.govpay.core.beans.EventoContext;
import it.govpay.core.exceptions.IOException;
import it.govpay.core.utils.client.NodoClient;
import it.govpay.core.utils.client.exception.ClientException;
import it.govpay.core.utils.thread.InviaRptThread;
import it.govpay.core.utils.thread.ThreadExecutorManager;
import it.govpay.model.Anagrafica;
import it.govpay.model.Intermediario;
import it.govpay.model.eventi.DatiPagoPA;

public class RptUtils {

	private RptUtils() {}

	public static String buildUUID35() {
		return UUID.randomUUID().toString().replace("-", "");
	}

	public static CtEnteBeneficiario buildEnteBeneficiario(Dominio dominio, UnitaOperativa uo) {

		CtEnteBeneficiario enteBeneficiario = new CtEnteBeneficiario();
		CtIdentificativoUnivocoPersonaG idUnivocoBeneficiario = new CtIdentificativoUnivocoPersonaG();
		idUnivocoBeneficiario.setCodiceIdentificativoUnivoco(dominio.getCodDominio());
		idUnivocoBeneficiario.setTipoIdentificativoUnivoco(StTipoIdentificativoUnivocoPersG.G);
		enteBeneficiario.setIdentificativoUnivocoBeneficiario(idUnivocoBeneficiario);
		enteBeneficiario.setDenominazioneBeneficiario(dominio.getRagioneSociale());

		Anagrafica anagrafica = dominio.getAnagrafica();
		enteBeneficiario.setCapBeneficiario(getNotEmpty(anagrafica.getCap()));
		enteBeneficiario.setCivicoBeneficiario(getNotEmpty(anagrafica.getCivico()));
		enteBeneficiario.setIndirizzoBeneficiario(getNotEmpty(anagrafica.getIndirizzo()));
		enteBeneficiario.setLocalitaBeneficiario(getNotEmpty(anagrafica.getLocalita()));
		enteBeneficiario.setNazioneBeneficiario(getNotEmpty(anagrafica.getNazione()));
		enteBeneficiario.setProvinciaBeneficiario(getNotEmpty(anagrafica.getProvincia()));

		if(!uo.getCodUo().equals(it.govpay.model.Dominio.EC) && uo.getAnagrafica() != null) {
			enteBeneficiario.setCodiceUnitOperBeneficiario(getNotEmpty(uo.getAnagrafica().getCodUnivoco()));
			enteBeneficiario.setDenomUnitOperBeneficiario(getNotEmpty(uo.getAnagrafica().getRagioneSociale()));
			enteBeneficiario.setIndirizzoBeneficiario(getNotEmpty(uo.getAnagrafica().getIndirizzo()));
			enteBeneficiario.setCivicoBeneficiario(getNotEmpty(uo.getAnagrafica().getCivico()));
			enteBeneficiario.setCapBeneficiario(getNotEmpty(uo.getAnagrafica().getCap()));
			enteBeneficiario.setLocalitaBeneficiario(getNotEmpty(uo.getAnagrafica().getLocalita()));
			enteBeneficiario.setProvinciaBeneficiario(getNotEmpty(uo.getAnagrafica().getProvincia()));
			enteBeneficiario.setNazioneBeneficiario(getNotEmpty(uo.getAnagrafica().getNazione()));
		}
		return enteBeneficiario;
	}

	private static String getNotEmpty(String text) {
		if(text == null || text.trim().isEmpty())
			return null;
		else
			return text;
	}

	public static it.govpay.core.business.model.Risposta inviaCarrelloRPT(
			NodoClient client, 
			Intermediario intermediario, 
			Stazione stazione, 
			List<Rpt> rpts, 
			String codiceConvenzione) throws ClientException, UtilsException {
		it.govpay.core.business.model.Risposta risposta = null;
		NodoInviaCarrelloRPT inviaCarrelloRpt = new NodoInviaCarrelloRPT();
		inviaCarrelloRpt.setIdentificativoCanale(rpts.get(0).getCodCanale());
		inviaCarrelloRpt.setIdentificativoIntermediarioPSP(rpts.get(0).getCodIntermediarioPsp());
		inviaCarrelloRpt.setIdentificativoPSP(rpts.get(0).getCodPsp());
		inviaCarrelloRpt.setPassword(stazione.getPassword());
		TipoListaRPT listaRpt = new TipoListaRPT();
		for(Rpt rpt : rpts) {
			TipoElementoListaRPT elementoListaRpt = new TipoElementoListaRPT();
			elementoListaRpt.setCodiceContestoPagamento(rpt.getCcp());
			elementoListaRpt.setIdentificativoDominio(rpt.getCodDominio());
			elementoListaRpt.setIdentificativoUnivocoVersamento(rpt.getIuv());
			elementoListaRpt.setRpt(rpt.getXmlRpt());
			listaRpt.getElementoListaRPT().add(elementoListaRpt);
		}
		inviaCarrelloRpt.setListaRPT(listaRpt);
		inviaCarrelloRpt.setCodiceConvenzione(codiceConvenzione);
		risposta = new it.govpay.core.business.model.Risposta(client.nodoInviaCarrelloRPT(intermediario, stazione, inviaCarrelloRpt, rpts.get(0).getCodCarrello())); 
		return risposta;
	}

	public static void popolaEventoCooperazione(Rpt rpt, Intermediario intermediario, Stazione stazione, EventoContext eventoContext) {
		DatiPagoPA datiPagoPA = new DatiPagoPA();
		datiPagoPA.setCodCanale(rpt.getCodCanale());
		datiPagoPA.setCodPsp(rpt.getCodPsp());
		datiPagoPA.setCodStazione(stazione.getCodStazione());
		datiPagoPA.setCodIntermediario(intermediario.getCodIntermediario());
		datiPagoPA.setErogatore(it.govpay.model.Evento.NDP);
		datiPagoPA.setFruitore(intermediario.getCodIntermediario());
		datiPagoPA.setTipoVersamento(rpt.getTipoVersamento());
		datiPagoPA.setModelloPagamento(rpt.getModelloPagamento().getCodifica()+"");
		datiPagoPA.setCodIntermediarioPsp(rpt.getCodIntermediarioPsp());
		datiPagoPA.setCodDominio(rpt.getCodDominio());
		eventoContext.setDatiPagoPA(datiPagoPA);
	}

	public static void inviaRPTAsync(Rpt rpt, IContext ctx) throws ServiceException, IOException {
		InviaRptThread t = new InviaRptThread(rpt, ctx);
		ThreadExecutorManager.getClientPoolExecutorRPT().execute(t);
	}

	public static String getRptKey(Rpt rpt) {
		if(rpt != null)
			return rpt.getCodDominio() + "@" + rpt.getIuv() + "@" + rpt.getCcp();
		return "";
	}
}
