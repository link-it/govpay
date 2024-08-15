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
package it.govpay.backoffice.v1.beans.converter;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.backoffice.v1.beans.ContoAddebito;
import it.govpay.backoffice.v1.beans.ModelloPagamento;
import it.govpay.backoffice.v1.beans.Pagamento;
import it.govpay.backoffice.v1.beans.PagamentoIndex;
import it.govpay.backoffice.v1.beans.PagamentoPost;
import it.govpay.backoffice.v1.beans.Rpp;
import it.govpay.backoffice.v1.beans.StatoPagamento;
import it.govpay.bd.model.Evento;
import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.dao.pagamenti.dto.LeggiPagamentoPortaleDTOResponse;
import it.govpay.core.dao.pagamenti.dto.LeggiRptDTOResponse;
import it.govpay.core.exceptions.IOException;
import it.govpay.core.utils.UriBuilderUtils;

public class PagamentiPortaleConverter {
	
	private PagamentiPortaleConverter() {}

	public static final String PENDENZE_KEY = "pendenze";
	public static final String VOCI_PENDENZE_KEY = "voci";
	public static final String ID_A2A_KEY = "idA2A";
	public static final String ID_PENDENZA_KEY = "idPendenza";
	public static final String ID_DOMINIO_KEY = "idDominio";
	public static final String IUV_KEY = "iuv";

	public static Pagamento toRsModel(LeggiPagamentoPortaleDTOResponse dto) throws ServiceException, IOException {
		return toRsModel(dto.getPagamento(), dto.getListaRpp());
	}

	public static Pagamento toRsModel(it.govpay.bd.model.PagamentoPortale pagamentoPortale,List<LeggiRptDTOResponse> listaRpp) throws ServiceException, IOException {
		Pagamento rsModel = new Pagamento();

		PagamentoPost pagamentiPortaleRequest = null;
		if(pagamentoPortale.getJsonRequest() != null) {
			try {
				pagamentiPortaleRequest = JSONSerializable.parse(pagamentoPortale.getJsonRequest(), PagamentoPost.class);

				if(pagamentiPortaleRequest.getContoAddebito()!=null) {
					ContoAddebito contoAddebito = new ContoAddebito();
					contoAddebito.setBic(pagamentiPortaleRequest.getContoAddebito().getBic());
					contoAddebito.setIban(pagamentiPortaleRequest.getContoAddebito().getIban());
					rsModel.setContoAddebito(contoAddebito);
				}
				rsModel.setDataEsecuzionePagamento(pagamentiPortaleRequest.getDataEsecuzionePagamento());
				rsModel.setCredenzialiPagatore(pagamentiPortaleRequest.getCredenzialiPagatore());
				rsModel.setSoggettoVersante(AnagraficaConverter.toSoggettoRsModel(AnagraficaConverter.toAnagrafica(pagamentiPortaleRequest.getSoggettoVersante())));
				rsModel.setAutenticazioneSoggetto(it.govpay.backoffice.v1.beans.Pagamento.AutenticazioneSoggettoEnum.fromValue(pagamentiPortaleRequest.getAutenticazioneSoggetto()));
			} catch (IOException e) {
				// donothing
			}
		}

		rsModel.setId(pagamentoPortale.getIdSessione());
		rsModel.setIdSessionePortale(pagamentoPortale.getIdSessionePortale());
		rsModel.setIdSessionePsp(pagamentoPortale.getIdSessionePsp());
		rsModel.setNome(pagamentoPortale.getNome());
		rsModel.setStato(StatoPagamento.valueOf(pagamentoPortale.getStato().toString()));
		rsModel.setDescrizioneStato(pagamentoPortale.getDescrizioneStato());
		rsModel.setPspRedirectUrl(pagamentoPortale.getPspRedirectUrl());
		rsModel.setUrlRitorno(pagamentoPortale.getUrlRitorno());
		rsModel.setDataRichiestaPagamento(pagamentoPortale.getDataRichiesta());
		rsModel.setImporto(pagamentoPortale.getImporto());

		if(listaRpp !=null) {
			List<Rpp> rpp = new ArrayList<>();
			for(LeggiRptDTOResponse leggiRptDtoResponse: listaRpp) {
				rpp.add(RptConverter.toRsModel(leggiRptDtoResponse.getRpt()));
			}
			rsModel.setRpp(rpp);
		}

		rsModel.setVerificato(pagamentoPortale.isAck());

		if(pagamentoPortale.getTipo() == 1) {
			rsModel.setModello(ModelloPagamento.ENTE);
		} else if(pagamentoPortale.getTipo() == 3) {
			rsModel.setModello(ModelloPagamento.PSP);
		}

		rsModel.setSeverita(pagamentoPortale.getSeverita() != null ? pagamentoPortale.getSeverita().intValue() : null);

		return rsModel;
	}
	public static PagamentoIndex toRsModelIndex(LeggiPagamentoPortaleDTOResponse dto) {
		it.govpay.bd.model.PagamentoPortale pagamentoPortale = dto.getPagamento();
		PagamentoIndex rsModel = new PagamentoIndex();

		PagamentoPost pagamentiPortaleRequest = null;

		if(pagamentoPortale.getJsonRequest() != null) {
			try {
				pagamentiPortaleRequest = JSONSerializable.parse(pagamentoPortale.getJsonRequest(), PagamentoPost.class);
				if(pagamentiPortaleRequest.getContoAddebito()!=null) {
					ContoAddebito contoAddebito = new ContoAddebito();
					contoAddebito.setBic(pagamentiPortaleRequest.getContoAddebito().getBic());
					contoAddebito.setIban(pagamentiPortaleRequest.getContoAddebito().getIban());
					rsModel.setContoAddebito(contoAddebito);
				}
				rsModel.setDataEsecuzionePagamento(pagamentiPortaleRequest.getDataEsecuzionePagamento());
				rsModel.setCredenzialiPagatore(pagamentiPortaleRequest.getCredenzialiPagatore());
				rsModel.setSoggettoVersante(AnagraficaConverter.toSoggettoRsModel(AnagraficaConverter.toAnagrafica(pagamentiPortaleRequest.getSoggettoVersante())));
				rsModel.setAutenticazioneSoggetto(it.govpay.backoffice.v1.beans.PagamentoIndex.AutenticazioneSoggettoEnum.fromValue(pagamentiPortaleRequest.getAutenticazioneSoggetto()));
			} catch (IOException e) {
				// donothing
			}
		}
		rsModel.setId(pagamentoPortale.getIdSessione());
		rsModel.setIdSessionePortale(pagamentoPortale.getIdSessionePortale());
		rsModel.setIdSessionePsp(pagamentoPortale.getIdSessionePsp());
		rsModel.setNome(pagamentoPortale.getNome());
		rsModel.setStato(StatoPagamento.valueOf(pagamentoPortale.getStato().toString()));
		rsModel.setDescrizioneStato(pagamentoPortale.getDescrizioneStato());
		rsModel.setPspRedirectUrl(pagamentoPortale.getPspRedirectUrl());
		rsModel.setUrlRitorno(pagamentoPortale.getUrlRitorno());
		rsModel.setDataRichiestaPagamento(pagamentoPortale.getDataRichiesta());
		rsModel.setRpp(UriBuilderUtils.getRptsByPagamento(pagamentoPortale.getIdSessione()));

		rsModel.setImporto(pagamentoPortale.getImporto());
		rsModel.setVerificato(pagamentoPortale.isAck());

		if(pagamentoPortale.getTipo() == 1) {
			rsModel.setModello(it.govpay.backoffice.v1.beans.ModelloPagamento.ENTE);
		} else if(pagamentoPortale.getTipo() == 3) {
			rsModel.setModello(it.govpay.backoffice.v1.beans.ModelloPagamento.PSP);
		}

		rsModel.setSeverita(pagamentoPortale.getSeverita() != null ? pagamentoPortale.getSeverita().intValue() : null);

		return rsModel;

	}
}
