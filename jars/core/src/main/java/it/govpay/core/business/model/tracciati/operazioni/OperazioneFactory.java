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
package it.govpay.core.business.model.tracciati.operazioni;

import java.io.UnsupportedEncodingException;

import org.apache.commons.lang.StringUtils;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.UtilsException;
import org.slf4j.Logger;

import it.govpay.bd.BasicBD;
import it.govpay.core.beans.EsitoOperazione;
import it.govpay.core.beans.tracciati.Avviso;
import it.govpay.core.beans.tracciati.Avviso.StatoEnum;
import it.govpay.core.beans.tracciati.FaultBean;
import it.govpay.core.beans.tracciati.FaultBean.CategoriaEnum;
import it.govpay.core.business.Tracciati;
import it.govpay.core.business.Versamento;
import it.govpay.core.business.model.AnnullaVersamentoDTO;
import it.govpay.core.business.model.tracciati.CostantiCaricamento;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.core.utils.DateUtils;
import it.govpay.core.utils.IuvUtils;
import it.govpay.model.Operazione.StatoOperazioneType;
import it.govpay.model.Operazione.TipoOperazioneType;


public class OperazioneFactory {
	
	private static Logger log = LoggerWrapperFactory.getLogger(Tracciati.class);

	public CaricamentoResponse caricaVersamento(CaricamentoRequest request, BasicBD basicBD) throws ServiceException {

		CaricamentoResponse caricamentoResponse = new CaricamentoResponse();
		caricamentoResponse.setIdA2A(request.getCodApplicazione());
		caricamentoResponse.setIdPendenza(request.getCodVersamentoEnte());
		caricamentoResponse.setNumero(request.getLinea());
		caricamentoResponse.setTipo(TipoOperazioneType.ADD);
		
		try {
			it.govpay.bd.model.Versamento versamentoModel = it.govpay.core.business.VersamentoUtils.toVersamentoModel(request.getVersamento(), basicBD);

			Versamento versamento = new Versamento(basicBD);
			
			versamento.caricaVersamento(versamentoModel, versamentoModel.getNumeroAvviso() == null, true);
			it.govpay.core.business.model.Iuv iuvGenerato = IuvUtils.toIuv(versamentoModel,versamentoModel.getApplicazione(basicBD), versamentoModel.getUo(basicBD).getDominio(basicBD));
			caricamentoResponse.setBarCode(iuvGenerato.getBarCode());
			caricamentoResponse.setIuv(iuvGenerato.getIuv());
			caricamentoResponse.setQrCode(iuvGenerato.getQrCode());
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
			avviso.setBarcode(iuvGenerato.getBarCode() != null ? new String(iuvGenerato.getBarCode()) : null);
			avviso.setQrcode(iuvGenerato.getQrCode() != null ? new String(iuvGenerato.getQrCode()) : null);
			
			StatoEnum statoPendenza = this.getStatoPendenza(versamentoModel);

			avviso.setStato(statoPendenza);
			caricamentoResponse.setAvviso(avviso);
			
		} catch(GovPayException e) {
			log.debug("Impossibile eseguire il caricamento della pendenza [Id: "+request.getCodVersamentoEnte()+", CodApplicazione: "+request.getCodApplicazione()+"]: "+ e.getMessage(),e);
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
		} catch(Throwable e) {
			log.error("Si e' verificato un errore durante il caricamento della pendenza [Id: "+request.getCodVersamentoEnte()+", CodApplicazione: "+request.getCodApplicazione()+"]: "+ e.getMessage(),e);
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
		case ESEGUITO: statoPendenza = StatoEnum.PAGATO;
			break;
		case ESEGUITO_ALTRO_CANALE:  statoPendenza = StatoEnum.PAGATO;
			break;
		case NON_ESEGUITO: if(versamentoModel.getDataScadenza() != null && DateUtils.isDataDecorsa(versamentoModel.getDataScadenza())) {statoPendenza = StatoEnum.SCADUTO;} else { statoPendenza = StatoEnum.NON_PAGATO;}
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
			log.debug("Impossibile eseguire l'annullamento della pendenza [Id: "+request.getCodVersamentoEnte()+", CodApplicazione: "+request.getCodApplicazione()+"]: "+ e.getMessage(),e);
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
			log.debug("Impossibile eseguire l'annullamento della pendenza [Id: "+request.getCodVersamentoEnte()+", CodApplicazione: "+request.getCodApplicazione()+"]: "+ e.getMessage(),e);
			annullamentoResponse.setStato(StatoOperazioneType.ESEGUITO_KO);
			annullamentoResponse.setEsito("DEL_KO");
			annullamentoResponse.setEsito(CostantiCaricamento.NOT_AUTHORIZED);
			annullamentoResponse.setDescrizioneEsito(StringUtils.isNotEmpty(CostantiCaricamento.NOT_AUTHORIZED + ": " + e.getMessage()) ? e.getMessage() : "");
			
			FaultBean respKo = new FaultBean();
			respKo.setCategoria(FaultBean.CategoriaEnum.fromValue(e.getCategoria().name()));
			respKo.setCodice(e.getCode());
			respKo.setDescrizione(e.getMessage());
			respKo.setDettaglio(e.getDetails());
			annullamentoResponse.setFaultBean(respKo);
		} catch (UtilsException e) {
			log.debug("Impossibile eseguire l'annullamento della pendenza [Id: "+request.getCodVersamentoEnte()+", CodApplicazione: "+request.getCodApplicazione()+"]: "+ e.getMessage(),e);
			annullamentoResponse.setStato(StatoOperazioneType.ESEGUITO_KO);
			annullamentoResponse.setEsito("DEL_KO");
			annullamentoResponse.setEsito(CostantiCaricamento.NOT_AUTHORIZED);
			annullamentoResponse.setDescrizioneEsito(StringUtils.isNotEmpty(CostantiCaricamento.NOT_AUTHORIZED + ": " + e.getMessage()) ? e.getMessage() : "");
			
			FaultBean respKo = new FaultBean();
			respKo.setCategoria(FaultBean.CategoriaEnum.INTERNO);
			respKo.setCodice(EsitoOperazione.INTERNAL.name());
			respKo.setDescrizione(e.getMessage());
			respKo.setDettaglio(e.getMessage());
			annullamentoResponse.setFaultBean(respKo);
		}
		return annullamentoResponse;

	}

}
