/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2015 Link.it srl (http://www.link.it).
 * Copyright (c) 2014-2015 TAS S.p.A. (http://www.tasgroup.it).
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
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
package it.govpay.ejb.ndp.util;

import java.util.List;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import gov.telematici.pagamenti.ws.ppthead.IntestazionePPT;
import it.govpay.ejb.ndp.model.DominioEnteModel;
import it.govpay.ejb.ndp.model.EventiInterfacciaModel;
import it.govpay.ejb.ndp.model.IntermediarioModel;
import it.govpay.ejb.ndp.model.StazioneModel;
import it.govpay.ejb.ndp.model.EventiInterfacciaModel.Categoria;
import it.govpay.ejb.ndp.model.EventiInterfacciaModel.Componente;
import it.govpay.ejb.ndp.model.EventiInterfacciaModel.Evento;
import it.govpay.ejb.ndp.model.EventiInterfacciaModel.Infospcoop;
import it.govpay.ejb.ndp.model.EventiInterfacciaModel.SottoTipo;

public class GdeUtils {

	public enum Azione {
		nodoInviaRPT, 
		nodoInviaCarrelloRPT,
		nodoInviaRichiestaStorno,
		paaInviaRT,
		paaVerificaRPT,
		paaAttivaRPT,
		paaInviaEsitoStorno;
	}
	
	public static Evento creaEventoRichiesta(String nomeSoggettoEnte, IntestazionePPT header, Azione azione) {
		DominioEnteModel ente = new DominioEnteModel();
		ente.setIdDominio(header.getIdentificativoDominio());
		IntermediarioModel intermediario = new IntermediarioModel();
		intermediario.setIdIntermediarioPA(header.getIdentificativoIntermediarioPA());
		intermediario.setNomeSoggettoSPC(nomeSoggettoEnte);
		ente.setIntermediario(intermediario);
		StazioneModel stazione = new StazioneModel();
		stazione.setIdStazioneIntermediarioPA(header.getIdentificativoStazioneIntermediarioPA());
		ente.setStazione(stazione);
		return creaEventoRichiesta(ente, header.getIdentificativoUnivocoVersamento(), header.getCodiceContestoPagamento(), "-", "-", "-", azione);
	}

	public static Evento creaEventoRichiesta(DominioEnteModel dominioEnte, String iuv, String ccp, String tipoVersamento, String psp, String canale, Azione azione) {
		boolean enteFruitore = false;
		switch (azione) {
		case nodoInviaRPT:
		case nodoInviaCarrelloRPT:
		case nodoInviaRichiestaStorno:
			enteFruitore = true;
			break;
		case paaInviaRT:
		case paaVerificaRPT:
		case paaAttivaRPT:
		case paaInviaEsitoStorno:
			enteFruitore = false;
		}

		String soggettoErogatore, soggettoFruitore;

		soggettoErogatore = enteFruitore ? "NodoDeiPagamentiSPC" : dominioEnte.getIntermediario().getNomeSoggettoSPC();
		soggettoFruitore = enteFruitore ? dominioEnte.getIntermediario().getNomeSoggettoSPC() : "NodoDeiPagamentiSPC";

		return buildEvento(dominioEnte, iuv, ccp, psp, canale, tipoVersamento, soggettoErogatore, soggettoFruitore, azione);
	}

	public static Infospcoop creaInfoSPCoop(Map<String, List<String>> httpHeaders) {
		NdpConfiguration config = NdpConfiguration.getInstance();
		
		if(getHeaderValue(config.getHttpHeader_idEgov(), httpHeaders) == null) return null;
		Infospcoop infospcoop = new EventiInterfacciaModel().new Infospcoop();
		
		infospcoop.setTipoSoggettoFruitore(getHeaderValue(config.getHttpHeader_tipoSoggettoFruitore(),httpHeaders));
		infospcoop.setSoggettoFruitore(getHeaderValue(config.getHttpHeader_soggettoFruitore(),httpHeaders));
		infospcoop.setTipoSoggettoErogatore(getHeaderValue(config.getHttpHeader_tipoSoggettoErogatore(),httpHeaders));
		infospcoop.setSoggettoErogatore(getHeaderValue(config.getHttpHeader_soggettoErogatore(),httpHeaders));
		infospcoop.setTipoServizio(getHeaderValue(config.getHttpHeader_tipoServizio(),httpHeaders));
		infospcoop.setServizio(getHeaderValue(config.getHttpHeader_servizio(),httpHeaders));
		infospcoop.setAzione(getHeaderValue(config.getHttpHeader_azione(),httpHeaders));
		infospcoop.setIdEgov(getHeaderValue(config.getHttpHeader_idEgov(),httpHeaders));
		return infospcoop;
	}

	private static String getHeaderValue(String headerName, Map<String, List<String>> headers) {
		Set<String> headerNames = headers.keySet();
		for(String hn : headerNames) {
			if(hn != null && hn.toLowerCase().equals(headerName.toLowerCase())){
				return headers.get(hn).get(0);
			}
		}
		return null;
	}

	private static Evento buildEvento(DominioEnteModel dominioEnte, String iuv, String ccp, String psp, String canale, String tipoVersamento, String soggettoErogatore, String soggettoFruitore, Azione azione) {
		Evento eventoRichiesta = new EventiInterfacciaModel().new Evento();
		eventoRichiesta.setCanalePagamento(canale);
		eventoRichiesta.setCategoria(Categoria.INTERFACCIA);
		eventoRichiesta.setCcp(ccp);
		eventoRichiesta.setComponente(Componente.GOVPAY);
		eventoRichiesta.setData(new Date());
		eventoRichiesta.setDominio(dominioEnte.getIdDominio());
		eventoRichiesta.setErogatore(soggettoErogatore);
		eventoRichiesta.setFruitore(soggettoFruitore);
		eventoRichiesta.setIuv(iuv);
		eventoRichiesta.setParametri(null);
		eventoRichiesta.setPsp(psp);
		eventoRichiesta.setSottoTipo(SottoTipo.REQ);
		eventoRichiesta.setStazioneIntermediarioPA(dominioEnte.getStazione().getIdStazioneIntermediarioPA());
		eventoRichiesta.setTipo(azione.name());
		eventoRichiesta.setTipoVersamento(tipoVersamento);
		return eventoRichiesta;
	}



}
