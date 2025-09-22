/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2025 Link.it srl (http://www.link.it).
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
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.UtilsException;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.slf4j.Logger;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.BasicBD;
import it.govpay.bd.model.Dominio;
import it.govpay.core.beans.EsitoOperazione;
import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.beans.tracciati.AnnullamentoPendenza;
import it.govpay.core.beans.tracciati.Avviso;
import it.govpay.core.beans.tracciati.Avviso.StatoEnum;
import it.govpay.core.beans.tracciati.FaultBean;
import it.govpay.core.beans.tracciati.FaultBean.CategoriaEnum;
import it.govpay.core.beans.tracciati.PendenzaPost;
import it.govpay.core.business.Versamento;
import it.govpay.core.business.model.AnnullaVersamentoDTO;
import it.govpay.core.business.model.tracciati.CostantiCaricamento;
import it.govpay.core.business.model.tracciati.TrasformazioneDTOResponse;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.core.exceptions.ValidationException;
import it.govpay.core.utils.DateUtils;
import it.govpay.core.utils.IuvUtils;
import it.govpay.core.utils.LogUtils;
import it.govpay.core.utils.SimpleDateFormatUtils;
import it.govpay.core.utils.VersamentoUtils;
import it.govpay.core.utils.tracciati.TracciatiPendenzeManager;
import it.govpay.core.utils.tracciati.TracciatiUtils;
import it.govpay.core.utils.tracciati.validator.PendenzaPostValidator;
import it.govpay.model.Operazione.StatoOperazioneType;
import it.govpay.model.Operazione.TipoOperazioneType;
import it.govpay.model.Versamento.TipologiaTipoVersamento;


public class OperazioneFactory {
	
	private static final String FAULT_CODE_400000 = "400000";
	private static final String FAULT_CODE_500000 = "500000";
	private static final String FAULT_ERRORE_INTERNO = "Errore Interno";
	private static final String FAULT_RICHIESTA_NON_VALIDA = "Richiesta non valida";
	
	private static Logger log = LoggerWrapperFactory.getLogger(OperazioneFactory.class);

	public CaricamentoResponse caricaVersamento(CaricamentoRequest request, TracciatiPendenzeManager manager, BasicBD basicBD) throws ServiceException {

		CaricamentoResponse caricamentoResponse = new CaricamentoResponse();
		caricamentoResponse.setIdA2A(request.getCodApplicazione());
		caricamentoResponse.setIdPendenza(request.getCodVersamentoEnte());
		caricamentoResponse.setNumero(request.getLinea());
		caricamentoResponse.setTipo(TipoOperazioneType.ADD);
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		try {
			it.govpay.bd.model.Versamento versamentoModel = VersamentoUtils.toVersamentoModel(request.getVersamento());
			
			//inserisco il tipo
			versamentoModel.setTipo(TipologiaTipoVersamento.DOVUTO);

			Versamento versamento = new Versamento();
			
			boolean generaIuv = VersamentoUtils.generaIUV(versamentoModel);
			versamentoModel = versamento.caricaVersamento(versamentoModel, generaIuv, true, null, null, null);
			Dominio dominio = versamentoModel.getDominio(configWrapper);
			it.govpay.core.business.model.Iuv iuvGenerato = IuvUtils.toIuv(versamentoModel,versamentoModel.getApplicazione(configWrapper), dominio);
			caricamentoResponse.setBarCode(iuvGenerato.getBarCode());
			caricamentoResponse.setIuv(iuvGenerato.getIuv());
			caricamentoResponse.setQrCode(iuvGenerato.getQrCode());
			caricamentoResponse.setStato(StatoOperazioneType.ESEGUITO_OK);
			caricamentoResponse.setEsito(AbstractOperazioneResponse.ESITO_ADD_OK);
			caricamentoResponse.setIdVersamento(versamentoModel.getId());
			
			Avviso avviso = new Avviso();
			
			if(versamentoModel.getCausaleVersamento()!= null) {
				try {
					avviso.setDescrizione(versamentoModel.getCausaleVersamento().getSimple());
				} catch (UnsupportedEncodingException e) {
					throw new ServiceException(e);
				}
			}
			
			manager.addNumeroAvviso(versamentoModel.getNumeroAvviso());
			
			avviso.setDataScadenza(versamentoModel.getDataScadenza());
			avviso.setDataValidita(versamentoModel.getDataValidita());
			avviso.setIdDominio(dominio.getCodDominio()); 
			avviso.setImporto(versamentoModel.getImportoTotale());
			avviso.setNumeroAvviso(versamentoModel.getNumeroAvviso());
			avviso.setTassonomiaAvviso(versamentoModel.getTassonomiaAvviso());
			avviso.setBarcode(iuvGenerato.getBarCode() != null ? new String(iuvGenerato.getBarCode()) : null);
			avviso.setQrcode(iuvGenerato.getQrCode() != null ? new String(iuvGenerato.getQrCode()) : null);
			
			StatoEnum statoPendenza = this.getStatoPendenza(versamentoModel);

			avviso.setStato(statoPendenza);
			if(versamentoModel.getNumeroAvviso() != null) {
				if(versamentoModel.getDocumento(basicBD) != null) {
					avviso.setNumeroDocumento(versamentoModel.getDocumento(basicBD).getCodDocumento());
				}
			}
			
			caricamentoResponse.setAvviso(avviso);
			
		} catch(GovPayException e) {
			LogUtils.logDebug(log, "Impossibile eseguire il caricamento della pendenza [Id: "+request.getCodVersamentoEnte()+", CodApplicazione: "+request.getCodApplicazione()+"]: "+ e.getMessage(),e);
			caricamentoResponse.setStato(StatoOperazioneType.ESEGUITO_KO);
			caricamentoResponse.setEsito(AbstractOperazioneResponse.ESITO_ADD_KO);
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
			caricamentoResponse.setEsito(AbstractOperazioneResponse.ESITO_ADD_KO);
			caricamentoResponse.setDescrizioneEsito(e.getMessage());
			
			FaultBean respKo = new FaultBean();
			respKo.setCategoria(CategoriaEnum.INTERNO);
			respKo.setCodice(FAULT_CODE_500000);
			respKo.setDescrizione(FAULT_ERRORE_INTERNO);
			respKo.setDettaglio(FAULT_ERRORE_INTERNO);
			caricamentoResponse.setFaultBean(respKo);
		}

		return caricamentoResponse;
	}
	
	public CaricamentoResponse caricaVersamentoCSV(CaricamentoRequest request, TracciatiPendenzeManager manager, BasicBD basicBD) {
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		CaricamentoResponse caricamentoResponse = new CaricamentoResponse();
		caricamentoResponse.setNumero(request.getLinea());
		caricamentoResponse.setTipo(TipoOperazioneType.ADD);
		
		caricamentoResponse.setJsonRichiesta(request.getDati() == null || request.getDati().length == 0 ? "" : new String(request.getDati()));
		try {
			if(request.getDati() == null || request.getDati().length == 0) throw new ValidationException("Record vuoto");
			
			TrasformazioneDTOResponse trasformazioneResponse = TracciatiUtils.trasformazioneInputCSV(log, request.getCodDominio(), request.getCodTipoVersamento(), new String(request.getDati()), request.getTipoTemplateTrasformazioneRichiesta() , request.getTemplateTrasformazioneRichiesta() );

			caricamentoResponse.setJsonRichiesta(trasformazioneResponse.getOutput());
			PendenzaPost pendenzaPost = JSONSerializable.parse(trasformazioneResponse.getOutput(), PendenzaPost.class);
			caricamentoResponse.setIdA2A(pendenzaPost.getIdA2A()); 
			caricamentoResponse.setIdPendenza(pendenzaPost.getIdPendenza());
			
			new PendenzaPostValidator(pendenzaPost).validate();
			
			it.govpay.core.beans.commons.Versamento versamentoToAdd = it.govpay.core.utils.TracciatiConverter.getVersamentoFromPendenza(pendenzaPost);
			
			// 12/12/2019 codDominio e codTipoVersamento sono settati nella trasformazione
			
			it.govpay.bd.model.Versamento versamentoModel = VersamentoUtils.toVersamentoModel(versamentoToAdd);
			
			//inserisco il tipo
			versamentoModel.setTipo(TipologiaTipoVersamento.DOVUTO);

			Versamento versamento = new Versamento();

			boolean generaIuv = VersamentoUtils.generaIUV(versamentoModel);
			Boolean avvisatura = trasformazioneResponse.getAvvisatura();
			Date dataAvvisatura = trasformazioneResponse.getDataAvvisatura();
			versamentoModel = versamento.caricaVersamento(versamentoModel, generaIuv, true, avvisatura,dataAvvisatura, null);
			Dominio dominio = versamentoModel.getDominio(configWrapper);
			it.govpay.core.business.model.Iuv iuvGenerato = IuvUtils.toIuv(versamentoModel,versamentoModel.getApplicazione(configWrapper), dominio);
			caricamentoResponse.setBarCode(iuvGenerato.getBarCode());
			caricamentoResponse.setIuv(iuvGenerato.getIuv());
			caricamentoResponse.setQrCode(iuvGenerato.getQrCode());
			caricamentoResponse.setStato(StatoOperazioneType.ESEGUITO_OK);
			caricamentoResponse.setEsito(AbstractOperazioneResponse.ESITO_ADD_OK);
			caricamentoResponse.setIdVersamento(versamentoModel.getId());
			
			manager.addNumeroAvviso(versamentoModel.getNumeroAvviso());
			
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
			avviso.setIdDominio(dominio.getCodDominio()); 
			avviso.setImporto(versamentoModel.getImportoTotale());
			avviso.setNumeroAvviso(versamentoModel.getNumeroAvviso());
			avviso.setTassonomiaAvviso(versamentoModel.getTassonomiaAvviso());
			avviso.setBarcode(iuvGenerato.getBarCode() != null ? new String(iuvGenerato.getBarCode()) : null);
			avviso.setQrcode(iuvGenerato.getQrCode() != null ? new String(iuvGenerato.getQrCode()) : null);
			
			StatoEnum statoPendenza = this.getStatoPendenza(versamentoModel);

			avviso.setStato(statoPendenza);
			
			if(versamentoModel.getNumeroAvviso() != null) {
				if(versamentoModel.getDocumento(basicBD) != null) {
					avviso.setNumeroDocumento(versamentoModel.getDocumento(basicBD).getCodDocumento());
				}
			}
			
			caricamentoResponse.setAvviso(avviso);
			
		} catch(GovPayException e) {
			LogUtils.logDebug(log, "Impossibile eseguire il caricamento linea ("+request.getLinea()+"): "+ e.getMessage(),e);
			caricamentoResponse.setStato(StatoOperazioneType.ESEGUITO_KO);
			caricamentoResponse.setEsito(AbstractOperazioneResponse.ESITO_ADD_KO);
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
		} catch(ValidationException e) {
			LogUtils.logDebug(log, "Impossibile eseguire il caricamento linea ("+request.getLinea()+"): "+ e.getMessage(),e);
			
			caricamentoResponse.setStato(StatoOperazioneType.ESEGUITO_KO);
			caricamentoResponse.setEsito(AbstractOperazioneResponse.ESITO_ADD_KO);
			caricamentoResponse.setDescrizioneEsito(e.getMessage());
			
			FaultBean respKo = new FaultBean();
			respKo.setCategoria(CategoriaEnum.RICHIESTA);
			respKo.setCodice(FAULT_CODE_400000);
			respKo.setDescrizione(FAULT_RICHIESTA_NON_VALIDA);
			respKo.setDettaglio(e.getMessage());
			caricamentoResponse.setFaultBean(respKo);
		} catch(Throwable e) {
			log.error("Impossibile eseguire il caricamento linea ("+request.getLinea()+"): "+ e.getMessage(),e);
			caricamentoResponse.setStato(StatoOperazioneType.ESEGUITO_KO);
			caricamentoResponse.setEsito(AbstractOperazioneResponse.ESITO_ADD_KO);
			caricamentoResponse.setDescrizioneEsito(e.getMessage());
			
			FaultBean respKo = new FaultBean();
			respKo.setCategoria(CategoriaEnum.INTERNO);
			respKo.setCodice(FAULT_CODE_500000);
			respKo.setDescrizione(FAULT_ERRORE_INTERNO);
			respKo.setDettaglio(FAULT_ERRORE_INTERNO);
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
		case NON_ESEGUITO: if(versamentoModel.getDataScadenza() != null && DateUtils.isDataDecorsa(versamentoModel.getDataScadenza(), DateUtils.CONTROLLO_SCADENZA)) {statoPendenza = StatoEnum.SCADUTO;} else { statoPendenza = StatoEnum.NON_PAGATO;}
			break;
		case PARZIALMENTE_ESEGUITO:  statoPendenza = StatoEnum.PAGATO;
			break;
		default:
			break;
		
		}
		return statoPendenza;
	}
	
	public AnnullamentoResponse annullaVersamento(AnnullamentoRequest request) {


		AnnullamentoResponse annullamentoResponse = new AnnullamentoResponse();
		annullamentoResponse.setIdA2A(request.getCodApplicazione());
		annullamentoResponse.setIdPendenza(request.getCodVersamentoEnte());
		annullamentoResponse.setNumero(request.getLinea()); 
		annullamentoResponse.setTipo(TipoOperazioneType.DEL);
		
		try {
			Versamento versamento = new Versamento();
			AnnullaVersamentoDTO annullaVersamentoDTO = null;
			annullaVersamentoDTO = new AnnullaVersamentoDTO(request.getOperatore(), request.getCodApplicazione(), request.getCodVersamentoEnte());
			annullaVersamentoDTO.setMotivoAnnullamento(request.getMotivoAnnullamento()); 
			versamento.annullaVersamento(annullaVersamentoDTO);

			annullamentoResponse.setStato(StatoOperazioneType.ESEGUITO_OK);
			annullamentoResponse.setEsito(AbstractOperazioneResponse.ESITO_DEL_OK);
			annullamentoResponse.setDescrizioneEsito("Versamento [CodApplicazione:" + request.getCodApplicazione() + " Id:" + request.getCodVersamentoEnte() + "] eliminato con successo");
		} catch(GovPayException e) {
			LogUtils.logDebug(log, "Impossibile eseguire l'annullamento della pendenza [Id: "+request.getCodVersamentoEnte()+", CodApplicazione: "+request.getCodApplicazione()+"]: "+ e.getMessage(),e);
			annullamentoResponse.setStato(StatoOperazioneType.ESEGUITO_KO);
			annullamentoResponse.setEsito(AbstractOperazioneResponse.ESITO_DEL_KO);
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
			LogUtils.logDebug(log, "Impossibile eseguire l'annullamento della pendenza [Id: "+request.getCodVersamentoEnte()+", CodApplicazione: "+request.getCodApplicazione()+"]: "+ e.getMessage(),e);
			annullamentoResponse.setStato(StatoOperazioneType.ESEGUITO_KO);
			annullamentoResponse.setEsito(AbstractOperazioneResponse.ESITO_DEL_KO);
			annullamentoResponse.setEsito(CostantiCaricamento.NOT_AUTHORIZED);
			annullamentoResponse.setDescrizioneEsito(StringUtils.isNotEmpty(CostantiCaricamento.NOT_AUTHORIZED + ": " + e.getMessage()) ? e.getMessage() : "");
			
			FaultBean respKo = new FaultBean();
			respKo.setCategoria(FaultBean.CategoriaEnum.fromValue(e.getCategoria().name()));
			respKo.setCodice(e.getCode());
			respKo.setDescrizione(e.getMessage());
			respKo.setDettaglio(e.getDetails());
			annullamentoResponse.setFaultBean(respKo);
		} catch (UtilsException e) {
			LogUtils.logDebug(log, "Impossibile eseguire l'annullamento della pendenza [Id: "+request.getCodVersamentoEnte()+", CodApplicazione: "+request.getCodApplicazione()+"]: "+ e.getMessage(),e);
			annullamentoResponse.setStato(StatoOperazioneType.ESEGUITO_KO);
			annullamentoResponse.setEsito(AbstractOperazioneResponse.ESITO_DEL_KO);
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

	
	public AbstractOperazioneResponse elaboraLineaCSV(CaricamentoRequest request, TracciatiPendenzeManager manager, String threadName, BasicBD basicBD) {
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		AbstractOperazioneResponse operazioneResponse = new CaricamentoResponse();
		operazioneResponse.setNumero(request.getLinea());
		
		// di default le linee sono tutte add finche' non le decodifico
		TipoOperazioneType tipoOperazioneType = TipoOperazioneType.ADD;
		operazioneResponse.setTipo(tipoOperazioneType);
		operazioneResponse.setJsonRichiesta(request.getDati() == null || request.getDati().length == 0 ? "" : new String(request.getDati()));
		
		try {
			LogUtils.logDebug(log, threadName + ": Numero Linea ["+request.getLinea() +"], Dati ["+operazioneResponse.getJsonRichiesta()+"]");
			if(request.getDati() == null || request.getDati().length == 0) throw new ValidationException("Record vuoto");
			
			TrasformazioneDTOResponse trasformazioneResponse = TracciatiUtils.trasformazioneInputCSV(log, request.getCodDominio(), request.getCodTipoVersamento(), new String(request.getDati()), request.getTipoTemplateTrasformazioneRichiesta() , request.getTemplateTrasformazioneRichiesta() );

			LogUtils.logTrace(log, threadName + ": Response della trasformazione: " + trasformazioneResponse.getOutput());
			
			operazioneResponse.setJsonRichiesta(trasformazioneResponse.getOutput());
			
			LogUtils.logDebug(log, threadName + ": Operazione da eseguire ["+trasformazioneResponse.getTipoOperazione()+"]");
			
			Versamento versamento = new Versamento();
			if(trasformazioneResponse.getTipoOperazione() == null || trasformazioneResponse.getTipoOperazione().equals(TipoOperazioneType.ADD)) {
				CaricamentoResponse caricamentoResponse = (CaricamentoResponse) operazioneResponse;
				
				PendenzaPost pendenzaPost = JSONSerializable.parse(trasformazioneResponse.getOutput(), PendenzaPost.class);
				caricamentoResponse.setIdA2A(pendenzaPost.getIdA2A()); 
				caricamentoResponse.setIdPendenza(pendenzaPost.getIdPendenza());
				
				new PendenzaPostValidator(pendenzaPost).validate();
				
				if(manager.checkPendenza(threadName, pendenzaPost.getIdA2A(), pendenzaPost.getIdPendenza())) {
					throw new ValidationException("Pendenza [IdA2A:"+pendenzaPost.getIdA2A()+", IdPendenza:"+pendenzaPost.getIdPendenza()+"], e' duplicata all'interno del tracciato.");
				}
				
				manager.addPendenza(threadName, pendenzaPost.getIdA2A(), pendenzaPost.getIdPendenza()); 
				
				it.govpay.core.beans.commons.Versamento versamentoToAdd = it.govpay.core.utils.TracciatiConverter.getVersamentoFromPendenza(pendenzaPost);
				
				// 12/12/2019 codDominio e codTipoVersamento sono settati nella trasformazione
				it.govpay.bd.model.Versamento versamentoModel = VersamentoUtils.toVersamentoModel(versamentoToAdd);
				
				//inserisco il tipo
				versamentoModel.setTipo(TipologiaTipoVersamento.DOVUTO);

				boolean generaIuv = VersamentoUtils.generaIUV(versamentoModel);
				Boolean avvisatura = trasformazioneResponse.getAvvisatura();
				Date dataAvvisatura = trasformazioneResponse.getDataAvvisatura();
					
				try {
					if(pendenzaPost.getDocumento() != null) { //attesa primo inserimento documento
						manager.getDocumento(threadName, pendenzaPost.getIdA2A(), pendenzaPost.getDocumento().getIdentificativo()); 
					}	
					
					versamentoModel = versamento.caricaVersamento(versamentoModel, generaIuv, true, avvisatura,dataAvvisatura, null);
				
				} catch(GovPayException e) {
					LogUtils.logDebug(log, threadName + ": Riscontrato errore durante elaborazione della linea ("+request.getLinea()+"): "+ e.getMessage(),e);
					throw e;
				} catch(Throwable e) {
					LogUtils.logDebug(log, threadName + ": Riscontrato errore durante elaborazione della linea ("+request.getLinea()+"): "+ e.getMessage(),e);
					throw e;
				} finally {
					if(pendenzaPost.getDocumento() != null) { // sblocco thread che attendono il documento
						manager.releaseDocumento(threadName, pendenzaPost.getIdA2A(), pendenzaPost.getDocumento().getIdentificativo()); 
					}	
				}
				
				Dominio dominio = versamentoModel.getDominio(configWrapper);
				it.govpay.core.business.model.Iuv iuvGenerato = IuvUtils.toIuv(versamentoModel,versamentoModel.getApplicazione(configWrapper), dominio);
				caricamentoResponse.setBarCode(iuvGenerato.getBarCode());
				caricamentoResponse.setIuv(iuvGenerato.getIuv());
				caricamentoResponse.setQrCode(iuvGenerato.getQrCode());
				caricamentoResponse.setStato(StatoOperazioneType.ESEGUITO_OK);
				caricamentoResponse.setEsito(AbstractOperazioneResponse.ESITO_ADD_OK);
				caricamentoResponse.setIdVersamento(versamentoModel.getId());
				
				manager.addNumeroAvviso(versamentoModel.getNumeroAvviso());		
				
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
				avviso.setIdDominio(dominio.getCodDominio()); 
				avviso.setImporto(versamentoModel.getImportoTotale());
				avviso.setNumeroAvviso(versamentoModel.getNumeroAvviso());
				avviso.setTassonomiaAvviso(versamentoModel.getTassonomiaAvviso());
				avviso.setBarcode(iuvGenerato.getBarCode() != null ? new String(iuvGenerato.getBarCode()) : null);
				avviso.setQrcode(iuvGenerato.getQrCode() != null ? new String(iuvGenerato.getQrCode()) : null);
				
				StatoEnum statoPendenza = this.getStatoPendenza(versamentoModel);

				avviso.setStato(statoPendenza);
				
				if(versamentoModel.getNumeroAvviso() != null) {
					if(versamentoModel.getDocumento(basicBD) != null) {
						avviso.setNumeroDocumento(versamentoModel.getDocumento(basicBD).getCodDocumento());
					}
				}
				
				caricamentoResponse.setAvviso(avviso);
			} else { // delete modifico il tipo della risposta
				tipoOperazioneType = TipoOperazioneType.DEL;
				operazioneResponse = new AnnullamentoResponse();
				operazioneResponse.setNumero(request.getLinea());
				operazioneResponse.setTipo(tipoOperazioneType);
				operazioneResponse.setJsonRichiesta(trasformazioneResponse.getOutput());
				AnnullamentoPendenza annullamento = JSONSerializable.parse(trasformazioneResponse.getOutput(), AnnullamentoPendenza.class);
				
				operazioneResponse.setIdA2A(annullamento.getIdA2A()); 
				operazioneResponse.setIdPendenza(annullamento.getIdPendenza());
				
				
				AnnullaVersamentoDTO annullaVersamentoDTO = null;
				annullaVersamentoDTO = new AnnullaVersamentoDTO(request.getOperatore(), annullamento.getIdA2A(), annullamento.getIdPendenza()); 
				annullaVersamentoDTO.setMotivoAnnullamento("Pendenza annullata in data "+SimpleDateFormatUtils.newSimpleDateFormatSoloData().format(new Date())+" tramite caricamento massivo."); 
				versamento.annullaVersamento(annullaVersamentoDTO);

				operazioneResponse.setStato(StatoOperazioneType.ESEGUITO_OK);
				operazioneResponse.setEsito(AbstractOperazioneResponse.ESITO_DEL_OK);
				operazioneResponse.setDescrizioneEsito("Versamento [CodApplicazione:" + annullamento.getIdA2A() + " Id:" + annullamento.getIdPendenza() + "] eliminato con successo");
			}
			
		} catch(GovPayException e) {
			LogUtils.logDebug(log, threadName + ": Impossibile eseguire elaborazione della linea ("+request.getLinea()+"): "+ e.getMessage(),e);
			operazioneResponse.setStato(StatoOperazioneType.ESEGUITO_KO);
			if(tipoOperazioneType.equals(TipoOperazioneType.ADD))
				operazioneResponse.setEsito(AbstractOperazioneResponse.ESITO_ADD_KO);
			else 
				operazioneResponse.setEsito(AbstractOperazioneResponse.ESITO_DEL_KO);
			operazioneResponse.setEsito(e.getCodEsito().name());
			operazioneResponse.setDescrizioneEsito(e.getCodEsito().name() + ": " + e.getMessage());
			
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
			operazioneResponse.setFaultBean(respKo);
		} catch(ValidationException e) {
			LogUtils.logDebug(log, threadName + ": Impossibile eseguire elaborazione della linea ("+request.getLinea()+"): "+ e.getMessage(),e);
			
			operazioneResponse.setStato(StatoOperazioneType.ESEGUITO_KO);
			if(tipoOperazioneType.equals(TipoOperazioneType.ADD))
				operazioneResponse.setEsito(AbstractOperazioneResponse.ESITO_ADD_KO);
			else 
				operazioneResponse.setEsito(AbstractOperazioneResponse.ESITO_DEL_KO);
			operazioneResponse.setDescrizioneEsito(e.getMessage());
			
			FaultBean respKo = new FaultBean();
			respKo.setCategoria(CategoriaEnum.RICHIESTA);
			respKo.setCodice(FAULT_CODE_400000);
			respKo.setDescrizione(FAULT_RICHIESTA_NON_VALIDA);
			respKo.setDettaglio(e.getMessage());
			operazioneResponse.setFaultBean(respKo);
		} catch(Throwable e) {
			log.error(threadName + ": Impossibile eseguire elaborazione della linea ("+request.getLinea()+"): "+ e.getMessage(),e);
			operazioneResponse.setStato(StatoOperazioneType.ESEGUITO_KO);
			if(tipoOperazioneType.equals(TipoOperazioneType.ADD))
				operazioneResponse.setEsito(AbstractOperazioneResponse.ESITO_ADD_KO);
			else 
				operazioneResponse.setEsito(AbstractOperazioneResponse.ESITO_DEL_KO);
			operazioneResponse.setDescrizioneEsito(e.getMessage());
			
			FaultBean respKo = new FaultBean();
			respKo.setCategoria(CategoriaEnum.INTERNO);
			respKo.setCodice(FAULT_CODE_500000);
			respKo.setDescrizione(FAULT_ERRORE_INTERNO);
			respKo.setDettaglio(FAULT_ERRORE_INTERNO);
			operazioneResponse.setFaultBean(respKo);
		}

		return operazioneResponse;
	}
}
