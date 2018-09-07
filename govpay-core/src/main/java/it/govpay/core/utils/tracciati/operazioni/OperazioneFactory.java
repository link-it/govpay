/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2018 Link.it srl (http://www.link.it).
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
package it.govpay.core.utils.tracciati.operazioni;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.core.business.Versamento;
import it.govpay.core.business.model.AnnullaVersamentoDTO;
import it.govpay.core.business.model.CaricaVersamentoDTO;
import it.govpay.core.business.model.CaricaVersamentoDTOResponse;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.core.rs.v1.beans.base.Avviso;
import it.govpay.core.rs.v1.beans.base.Avviso.StatoEnum;
import it.govpay.core.rs.v1.beans.base.FaultBean;
import it.govpay.core.rs.v1.beans.base.FaultBean.CategoriaEnum;
import it.govpay.core.utils.tracciati.CostantiCaricamento;
import it.govpay.model.Operazione.StatoOperazioneType;
import it.govpay.model.Operazione.TipoOperazioneType;

public class OperazioneFactory {

	public CaricamentoResponse caricaVersamento(CaricamentoRequest request, BasicBD basicBD) throws ServiceException {

		CaricamentoResponse caricamentoResponse = new CaricamentoResponse();
		caricamentoResponse.setIdA2A(request.getCodApplicazione());
		caricamentoResponse.setIdPendenza(request.getCodVersamentoEnte());
		caricamentoResponse.setNumero(request.getLinea());
		caricamentoResponse.setTipo(TipoOperazioneType.ADD);
		
		try {
			it.govpay.bd.model.Versamento versamentoModel = it.govpay.core.business.VersamentoUtils.toVersamentoModel(request.getVersamento(), basicBD);
			CaricaVersamentoDTO caricaVersamentoDTO = new CaricaVersamentoDTO(request.getOperatore(), versamentoModel);
			caricaVersamentoDTO.setAggiornaSeEsiste(true);
			caricaVersamentoDTO.setGeneraIuv(versamentoModel.getNumeroAvviso() == null);

			Versamento versamento = new Versamento(basicBD);
			CaricaVersamentoDTOResponse caricaVersamento = versamento.caricaVersamento(caricaVersamentoDTO);
			caricamentoResponse.setBarCode(caricaVersamento.getBarCode());
			caricamentoResponse.setQrCode(caricaVersamento.getQrCode());
			caricamentoResponse.setIuv(caricaVersamento.getIuv());
			caricamentoResponse.setStato(StatoOperazioneType.ESEGUITO_OK);
			caricamentoResponse.setEsito(CaricamentoResponse.ESITO_ADD_OK);
			
			Avviso avviso = new Avviso();
			
			if(versamentoModel.getCausaleVersamento()!= null) {
				try {
					avviso.setDescrizione(versamentoModel.getCausaleVersamento().getSimple());
				} catch (UnsupportedEncodingException e) {
					throw new ServiceException(e);
				}
			}
			
			avviso.setDataScadenza(versamentoModel.getDataScadenza());
			avviso.setDataValidita(versamentoModel.getDataValidita());
			avviso.setIdDominio(versamentoModel.getDominio(basicBD).getCodDominio()); 
			avviso.setImporto(versamentoModel.getImportoTotale());
			avviso.setNumeroAvviso(versamentoModel.getNumeroAvviso());
			avviso.setTassonomiaAvviso(versamentoModel.getTassonomiaAvviso());
			avviso.setBarcode(caricaVersamento.getBarCode() != null ? new String(caricaVersamento.getBarCode()) : null);
			avviso.setQrcode(caricaVersamento.getQrCode() != null ? new String(caricaVersamento.getQrCode()) : null);
			
			StatoEnum statoPendenza = this.getStatoPendenza(versamentoModel);

			avviso.setStato(statoPendenza);
			caricamentoResponse.setAvviso(avviso);
			
		} catch(GovPayException e) {
			caricamentoResponse.setStato(StatoOperazioneType.ESEGUITO_KO);
			caricamentoResponse.setEsito(CaricamentoResponse.ESITO_ADD_KO);
			caricamentoResponse.setEsito(e.getCodEsito().name());
			caricamentoResponse.setDescrizioneEsito(e.getCodEsito().name() + ": " + e.getMessage());
			
			FaultBean respKo = new FaultBean();
			if(e.getFaultBean()!=null) {
				respKo.setCategoria(CategoriaEnum.PAGOPA);
				respKo.setCodice(e.getFaultBean().getFaultCode());
				respKo.setDescrizione(e.getFaultBean().getFaultString());
				respKo.setDettaglio(e.getFaultBean().getDescription());
			} else {
				respKo.setCategoria(CategoriaEnum.fromValue(e.getCategoria().name()));
				respKo.setCodice(e.getCodEsitoV3());
				respKo.setDescrizione(e.getDescrizioneEsito());
				respKo.setDettaglio(e.getMessageV3());
				
			}
			caricamentoResponse.setFaultBean(respKo);
			
			
		} catch(NotAuthorizedException e) {
			caricamentoResponse.setStato(StatoOperazioneType.ESEGUITO_KO);
			caricamentoResponse.setEsito(CaricamentoResponse.ESITO_ADD_KO);
			caricamentoResponse.setDescrizioneEsito(StringUtils.isNotEmpty(CostantiCaricamento.NOT_AUTHORIZED + ": " + e.getMessage()) ? e.getMessage() : "");
			
			FaultBean respKo = new FaultBean();
			respKo.setCategoria(e.getCategoria());
			respKo.setCodice(e.getCode());
			respKo.setDescrizione(e.getMessage());
			respKo.setDettaglio(e.getDetails());
			caricamentoResponse.setFaultBean(respKo);
		} catch(Throwable e) {
			caricamentoResponse.setStato(StatoOperazioneType.ESEGUITO_KO);
			caricamentoResponse.setEsito(CaricamentoResponse.ESITO_ADD_KO);
			caricamentoResponse.setDescrizioneEsito(e.getMessage());
			
			FaultBean respKo = new FaultBean();
			respKo.setCategoria(CategoriaEnum.INTERNO);
			respKo.setCodice("500000");
			respKo.setDescrizione("Errore Interno");
			respKo.setDettaglio("Errore Interno");
			caricamentoResponse.setFaultBean(respKo);
		}

		return caricamentoResponse;
	}

	private StatoEnum getStatoPendenza(it.govpay.bd.model.Versamento versamentoModel) {
		StatoEnum statoPendenza = null;

		switch(versamentoModel.getStatoVersamento()) {
		case ANNULLATO: statoPendenza = StatoEnum.ANNULLATO;
			break;
		case ANOMALO: statoPendenza = StatoEnum.NON_PAGATO;
			break;
		case ESEGUITO: statoPendenza = StatoEnum.PAGATO;
			break;
		case ESEGUITO_SENZA_RPT:  statoPendenza = StatoEnum.PAGATO;
			break;
		case NON_ESEGUITO: if(versamentoModel.getDataScadenza() != null && versamentoModel.getDataScadenza().before(new Date())) {statoPendenza = StatoEnum.SCADUTO;} else { statoPendenza = StatoEnum.NON_PAGATO;}
			break;
		case PARZIALMENTE_ESEGUITO:  statoPendenza = StatoEnum.PAGATO;
			break;
		default:
			break;
		
		}
		return statoPendenza;
	}
	
	public AnnullamentoResponse annullaVersamento(AnnullamentoRequest request, BasicBD basicBD) throws ServiceException {


		AnnullamentoResponse annullamentoResponse = new AnnullamentoResponse();
		annullamentoResponse.setIdA2A(request.getCodApplicazione());
		annullamentoResponse.setIdPendenza(request.getCodVersamentoEnte());
		annullamentoResponse.setNumero(request.getLinea()); 
		annullamentoResponse.setTipo(TipoOperazioneType.DEL);
		
		try {
			Versamento versamento = new Versamento(basicBD);
			AnnullaVersamentoDTO annullaVersamentoDTO = null;
			annullaVersamentoDTO = new AnnullaVersamentoDTO(request.getOperatore(), request.getCodApplicazione(), request.getCodVersamentoEnte());
			annullaVersamentoDTO.setMotivoAnnullamento(request.getMotivoAnnullamento()); 
			versamento.annullaVersamento(annullaVersamentoDTO);

			annullamentoResponse.setStato(StatoOperazioneType.ESEGUITO_OK);
			annullamentoResponse.setEsito("DEL_OK");
			annullamentoResponse.setDescrizioneEsito("Versamento [CodApplicazione:" + request.getCodApplicazione() + " Id:" + request.getCodVersamentoEnte() + "] eliminato con successo");
		} catch(GovPayException e) {
			annullamentoResponse.setStato(StatoOperazioneType.ESEGUITO_KO);
			annullamentoResponse.setEsito("DEL_KO");
			annullamentoResponse.setEsito(e.getCodEsito().name());
			annullamentoResponse.setDescrizioneEsito(e.getCodEsito().name() + ": " +e.getMessage());
			
			FaultBean respKo = new FaultBean();
			if(e.getFaultBean()!=null) {
				respKo.setCategoria(CategoriaEnum.PAGOPA);
				respKo.setCodice(e.getFaultBean().getFaultCode());
				respKo.setDescrizione(e.getFaultBean().getFaultString());
				respKo.setDettaglio(e.getFaultBean().getDescription());
			} else {
				respKo.setCategoria(CategoriaEnum.fromValue(e.getCategoria().name()));
				respKo.setCodice(e.getCodEsitoV3());
				respKo.setDescrizione(e.getDescrizioneEsito());
				respKo.setDettaglio(e.getMessageV3());
				
			}
			annullamentoResponse.setFaultBean(respKo);
			
		} catch(NotAuthorizedException e) {
			annullamentoResponse.setStato(StatoOperazioneType.ESEGUITO_KO);
			annullamentoResponse.setEsito("DEL_KO");
			annullamentoResponse.setEsito(CostantiCaricamento.NOT_AUTHORIZED);
			annullamentoResponse.setDescrizioneEsito(StringUtils.isNotEmpty(CostantiCaricamento.NOT_AUTHORIZED + ": " + e.getMessage()) ? e.getMessage() : "");
			
			FaultBean respKo = new FaultBean();
			respKo.setCategoria(e.getCategoria());
			respKo.setCodice(e.getCode());
			respKo.setDescrizione(e.getMessage());
			respKo.setDettaglio(e.getDetails());
			annullamentoResponse.setFaultBean(respKo);
		}
		return annullamentoResponse;

	}

}
