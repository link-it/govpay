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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.logger.beans.Property;
import org.slf4j.Logger;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.pagamento.IuvBD;
import it.govpay.bd.pagamento.VersamentiBD;
import it.govpay.bd.pagamento.filters.VersamentoFilter;
import it.govpay.core.business.model.AnnullaVersamentoDTO;
import it.govpay.core.business.model.CaricaVersamentoDTO;
import it.govpay.core.business.model.CaricaVersamentoDTOResponse;
import it.govpay.core.dao.pagamenti.dto.PagamentiPortaleDTO.RefVersamentoAvviso;
import it.govpay.core.dao.pagamenti.dto.PagamentiPortaleDTO.RefVersamentoPendenza;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.core.exceptions.VersamentoAnnullatoException;
import it.govpay.core.exceptions.VersamentoDuplicatoException;
import it.govpay.core.exceptions.VersamentoScadutoException;
import it.govpay.core.exceptions.VersamentoSconosciutoException;
import it.govpay.core.rs.v1.costanti.EsitoOperazione;
import it.govpay.core.utils.AclEngine;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.core.utils.IuvUtils;
import it.govpay.core.utils.client.BasicClient.ClientException;
import it.govpay.model.Acl.Diritti;
import it.govpay.model.Acl.Servizio;
import it.govpay.model.Iuv.TipoIUV;
import it.govpay.model.Versamento.StatoVersamento;

public class Versamento extends BasicBD {

	private static final String ECCEZIONE_NON_SPECIFICATA = "- Non specificata -";
	private static final String LOG_KEY_VERSAMENTO_ANNULLA_KO = "versamento.annullaKo";
	private static Logger log = LoggerWrapperFactory.getLogger(Versamento.class);
	
	public Versamento(BasicBD basicBD) {
		super(basicBD);
	}
	
	@Deprecated
	public it.govpay.model.Iuv caricaVersamento(Applicazione applicazioneAutenticata, it.govpay.bd.model.Versamento versamentoModel, boolean generaIuv, boolean aggiornaSeEsiste) throws GovPayException { 
		try {
			return this.caricaVersamento(versamentoModel, generaIuv, aggiornaSeEsiste);
		} catch (Exception e) {
			if(e instanceof GovPayException)
				throw (GovPayException) e;
			else 
				throw new GovPayException(e);
		}
	}
	
	@Deprecated
	public it.govpay.model.Iuv caricaVersamento(it.govpay.bd.model.Versamento versamento, boolean generaIuv, boolean aggiornaSeEsiste) throws GovPayException {
		// Indica se devo gestire la transazione oppure se e' gestita dal chiamante
		boolean doCommit = false;
		GpContext ctx = GpThreadLocal.get();
		try {
			ctx.log("versamento.validazioneSemantica", versamento.getApplicazione(this).getCodApplicazione(), versamento.getCodVersamentoEnte());
			it.govpay.core.utils.VersamentoUtils.validazioneSemantica(versamento, generaIuv, this);
			ctx.log("versamento.validazioneSemanticaOk", versamento.getApplicazione(this).getCodApplicazione(), versamento.getCodVersamentoEnte());
			
			VersamentiBD versamentiBD = new VersamentiBD(this);
			
			if(this.isAutoCommit()) {
				this.setAutoCommit(false);
				doCommit = true;
			}

			IuvBD iuvBD = new IuvBD(this);
			
			it.govpay.model.Iuv iuv = null;
			try {
				ctx.getPagamentoCtx().loadVersamentoContext(versamento, this);
				iuv = iuvBD.getIuv(versamento.getIdApplicazione(), versamento.getCodVersamentoEnte(), TipoIUV.NUMERICO);
			} catch (NotFoundException e) {
				if(generaIuv) {
					Iuv iuvBusiness = new Iuv(this);
					iuv = iuvBusiness.generaIUV(versamento.getApplicazione(this), versamento.getUo(this).getDominio(this), versamento.getCodVersamentoEnte(), TipoIUV.NUMERICO);
					// imposto iuv calcolato
					versamento.setIuvVersamento(iuv.getIuv());
					versamento.setIuvProposto(iuv.getIuv()); 
					// calcolo il numero avviso
					it.govpay.core.business.model.Iuv iuv2 = IuvUtils.toIuv(versamento, versamento.getApplicazione(this), versamento.getUo(this).getDominio(this));
					versamento.setNumeroAvviso(iuv2.getNumeroAvviso());
				}
			}
			
			
			try {
				it.govpay.bd.model.Versamento versamentoLetto = versamentiBD.getVersamento(versamento.getIdApplicazione(), versamento.getCodVersamentoEnte());
				// Versamento presente. Verifico e aggiorno
				
				versamento.setCodAvvisatura(versamentoLetto.getCodAvvisatura());
				versamento.setDaAvvisare(versamentoLetto.isDaAvvisare());

				
				if(!aggiornaSeEsiste)
					throw new GovPayException(EsitoOperazione.VER_015, versamento.getApplicazione(this).getCodApplicazione(), versamento.getCodVersamentoEnte());
				
				ctx.log("versamento.validazioneSemanticaAggiornamento", versamento.getApplicazione(this).getCodApplicazione(), versamento.getCodVersamentoEnte());
				it.govpay.core.utils.VersamentoUtils.validazioneSemanticaAggiornamento(versamentoLetto, versamento, this);
				ctx.log("versamento.validazioneSemanticaAggiornamentoOk", versamento.getApplicazione(this).getCodApplicazione(), versamento.getCodVersamentoEnte());
				
				versamentiBD.updateVersamento(versamento, true);
				if(versamento.getId()==null)
					versamento.setId(versamentoLetto.getId());
				
				ctx.log("versamento.aggioramentoOk", versamento.getApplicazione(this).getCodApplicazione(), versamento.getCodVersamentoEnte());
				
				log.info("Versamento (" + versamento.getCodVersamentoEnte() + ") dell'applicazione (" + versamento.getApplicazione(this).getCodApplicazione() + ") aggiornato");
			} catch (NotFoundException e) {
				if(versamento.getNumeroAvviso()!=null) {
					try {
						// 	verifica univocita dell'avviso pagamento prima di inserire il nuovo versamento
						it.govpay.bd.model.Versamento versamentoFromDominioNumeroAvviso = versamentiBD.getVersamentoFromDominioNumeroAvviso(versamento.getDominio(this).getCodDominio(), versamento.getNumeroAvviso());
					
						// due pendenze non possono avere lo stesso numero avviso
						if(!versamentoFromDominioNumeroAvviso.getCodVersamentoEnte().equals(versamento.getCodVersamentoEnte()))
							throw new GovPayException(EsitoOperazione.VER_025, versamento.getApplicazione(this).getCodApplicazione(), versamento.getCodVersamentoEnte(), 
									versamentoFromDominioNumeroAvviso.getApplicazione(this).getCodApplicazione(), versamentoFromDominioNumeroAvviso.getCodVersamentoEnte(),versamento.getNumeroAvviso());
						
					}catch(NotFoundException e2) {
						// ignore
					}
				}
				
				// Versamento nuovo. Inserisco
				versamentiBD.insertVersamento(versamento);
				ctx.log("versamento.inserimentoOk", versamento.getApplicazione(this).getCodApplicazione(), versamento.getCodVersamentoEnte());
				
				log.info("Versamento (" + versamento.getCodVersamentoEnte() + ") dell'applicazione (" + versamento.getApplicazione(this).getCodApplicazione() + ") inserito");
			}
			if(doCommit) this.commit();
			return iuv;
		} catch (Exception e) {
			if(doCommit) this.rollback();
			if(e instanceof GovPayException)
				throw (GovPayException) e;
			else 
				throw new GovPayException(e);
		}
	}
	
	public CaricaVersamentoDTOResponse caricaVersamento(CaricaVersamentoDTO caricaVersamentoDTO) throws GovPayException, NotAuthorizedException, ServiceException {
		
		// AUTORIZZAZIONE
		if(caricaVersamentoDTO.getApplicazione() != null && !caricaVersamentoDTO.getApplicazione().getCodApplicazione().equals(caricaVersamentoDTO.getVersamento().getApplicazione(this).getCodApplicazione())) {
			throw new NotAuthorizedException("Applicazione ["+caricaVersamentoDTO.getApplicazione().getCodApplicazione()+"] non autorizzata a caricare il Versamento ["+caricaVersamentoDTO.getVersamento().getCodVersamentoEnte()+"]");
		}
		
		
		if(caricaVersamentoDTO.getOperatore() != null && 
				AclEngine.isAuthorized(caricaVersamentoDTO.getOperatore().getUtenza(), Servizio.PAGAMENTI_E_PENDENZE, caricaVersamentoDTO.getVersamento().getUo(this).getDominio(this).getCodDominio(), null, Arrays.asList(Diritti.SCRITTURA,Diritti.ESECUZIONE))){
			throw new NotAuthorizedException("Operatore chiamante [" + caricaVersamentoDTO.getOperatore().getPrincipal() + "] non autorizzato in scrittura per il dominio " + caricaVersamentoDTO.getVersamento().getUo(this).getDominio(this).getCodDominio());
		}
		
		it.govpay.bd.model.Versamento versamento = caricaVersamentoDTO.getVersamento();
		boolean generaIuv = caricaVersamentoDTO.isGeneraIuv();
		boolean aggiornaSeEsiste = caricaVersamentoDTO.isAggiornaSeEsiste();
		CaricaVersamentoDTOResponse response = new CaricaVersamentoDTOResponse();
		
		try {
			this.caricaVersamento(versamento, generaIuv, aggiornaSeEsiste);
			it.govpay.core.business.model.Iuv iuvGenerato = IuvUtils.toIuv(versamento,versamento.getApplicazione(this), versamento.getUo(this).getDominio(this));
			response.setBarCode(iuvGenerato.getBarCode());
			response.setCodApplicazione(iuvGenerato.getCodApplicazione());
			response.setCodDominio(iuvGenerato.getCodDominio());
			response.setCodVersamentoEnte(iuvGenerato.getCodVersamentoEnte());
			response.setIuv(iuvGenerato.getIuv());
			response.setNumeroAvviso(iuvGenerato.getNumeroAvviso());
			response.setQrCode(iuvGenerato.getQrCode());
			return response;
		} catch (GovPayException e) {
			throw e;
		}
	}

	@Deprecated
	public void annullaVersamento(Applicazione applicazione, String codApplicazione, String codVersamentoEnte) throws GovPayException {
		try {
			VersamentiBD versamentiBD = new VersamentiBD(this);
			
			this.setAutoCommit(false);
			this.enableSelectForUpdate();
			
			try {
				it.govpay.bd.model.Versamento versamentoLetto = versamentiBD.getVersamento(applicazione.getId(), codVersamentoEnte);
			
				// Se è già annullato non devo far nulla.
				if(versamentoLetto.getStatoVersamento().equals(StatoVersamento.ANNULLATO)) {
					log.info("Versamento (" + versamentoLetto.getCodVersamentoEnte() + ") dell'applicazione (" + applicazione.getCodApplicazione() + ") gia' annullato. Aggiornamento non necessario.");
					return;
				}
				
				// Se è in stato NON_ESEGUITO lo annullo
				if(versamentoLetto.getStatoVersamento().equals(StatoVersamento.NON_ESEGUITO)) {
					versamentoLetto.setStatoVersamento(StatoVersamento.ANNULLATO);
					versamentiBD.updateVersamento(versamentoLetto);
					log.info("Versamento (" + versamentoLetto.getCodVersamentoEnte() + ") dell'applicazione (" + applicazione.getCodApplicazione() + ") annullato.");
					return;
				}
				
				// Se non è ne ANNULLATO ne NON_ESEGUITO non lo posso annullare
				throw new GovPayException(EsitoOperazione.VER_009, codApplicazione, codVersamentoEnte, versamentoLetto.getStatoVersamento().toString());
				
			} catch (NotFoundException e) {
				// Versamento inesistente
				throw new GovPayException(EsitoOperazione.VER_008, codApplicazione, codVersamentoEnte);
			} finally {
				this.commit();
			}
		} catch (Exception e) {
			this.rollback();
			if(e instanceof GovPayException)
				throw (GovPayException) e;
			else 
				throw new GovPayException(e);
		} finally {
			try {
				this.disableSelectForUpdate();
			} catch (Exception e) {}
		}
	}
	
	public void annullaVersamento(AnnullaVersamentoDTO annullaVersamentoDTO) throws GovPayException, NotAuthorizedException {
		log.info("Richiesto annullamento per il Versamento (" + annullaVersamentoDTO.getCodVersamentoEnte() + ") dell'applicazione (" + annullaVersamentoDTO.getCodApplicazione() + ")");
		
		GpContext ctx = GpThreadLocal.get();
		
		if(!ctx.hasCorrelationId()) ctx.setCorrelationId(annullaVersamentoDTO.getCodApplicazione() + annullaVersamentoDTO.getCodVersamentoEnte());
		ctx.getContext().getRequest().addGenericProperty(new Property("codApplicazione", annullaVersamentoDTO.getCodApplicazione()));
		ctx.getContext().getRequest().addGenericProperty(new Property("codVersamentoEnte", annullaVersamentoDTO.getCodVersamentoEnte()));
		ctx.log("versamento.annulla");
		
		if(annullaVersamentoDTO.getApplicazione() != null && !annullaVersamentoDTO.getApplicazione().getCodApplicazione().equals(annullaVersamentoDTO.getCodApplicazione())) {
			throw new NotAuthorizedException("Applicazione chiamante [" + annullaVersamentoDTO.getApplicazione().getCodApplicazione() + "] non e' proprietaria del versamento");
		}
		
		String codApplicazione = annullaVersamentoDTO.getCodApplicazione();
		String codVersamentoEnte = annullaVersamentoDTO.getCodVersamentoEnte();
		
		try {
			VersamentiBD versamentiBD = new VersamentiBD(this);
			
			this.setAutoCommit(false);
			this.enableSelectForUpdate();
			
			try {
				it.govpay.bd.model.Versamento versamentoLetto = versamentiBD.getVersamento(AnagraficaManager.getApplicazione(this, codApplicazione).getId(), codVersamentoEnte);
			
				if(annullaVersamentoDTO.getOperatore() != null && 
						AclEngine.isAuthorized(annullaVersamentoDTO.getOperatore().getUtenza(), Servizio.PAGAMENTI_E_PENDENZE, versamentoLetto.getUo(this).getDominio(this).getCodDominio(), null, Arrays.asList(Diritti.SCRITTURA,Diritti.ESECUZIONE))){
					throw new NotAuthorizedException("Operatore chiamante [" + annullaVersamentoDTO.getOperatore().getPrincipal() + "] non autorizzato in scrittura per il dominio " + versamentoLetto.getUo(this).getDominio(this).getCodDominio());
				}
				// Se è già annullato non devo far nulla.
				if(versamentoLetto.getStatoVersamento().equals(StatoVersamento.ANNULLATO)) {
					log.info("Versamento (" + versamentoLetto.getCodVersamentoEnte() + ") dell'applicazione (" + codApplicazione + ") gia' annullato. Aggiornamento non necessario.");
					ctx.log("versamento.annullaOk");
					return;
				}
				
				// Se è in stato NON_ESEGUITO lo annullo
				if(versamentoLetto.getStatoVersamento().equals(StatoVersamento.NON_ESEGUITO)) {
					versamentoLetto.setStatoVersamento(StatoVersamento.ANNULLATO);
					versamentoLetto.setDescrizioneStato(annullaVersamentoDTO.getMotivoAnnullamento()); 
					versamentiBD.updateVersamento(versamentoLetto);
					log.info("Versamento (" + versamentoLetto.getCodVersamentoEnte() + ") dell'applicazione (" + codApplicazione + ") annullato.");
					ctx.log("versamento.annullaOk");
					return;
				}
				
				// Se non è ne ANNULLATO ne NON_ESEGUITO non lo posso annullare
				throw new GovPayException(EsitoOperazione.VER_009, codApplicazione, codVersamentoEnte, versamentoLetto.getStatoVersamento().toString());
				
			} catch (NotFoundException e) {
				// Versamento inesistente
				throw new GovPayException(EsitoOperazione.VER_008, codApplicazione, codVersamentoEnte);
			} finally {
				this.commit();
			}
		} catch (Exception e) {
			this.rollback();
			this.handleAnnullamentoException(ctx, e);
		} finally {
			try {
				this.disableSelectForUpdate();
			} catch (ServiceException e) {
//				GovPayException gpe = new GovPayException(e);
//				ctx.log(LOG_KEY_VERSAMENTO_ANNULLA_KO, gpe.getCodEsito().toString(), gpe.getDescrizioneEsito(), gpe.getCausa() != null ? gpe.getCausa() : ECCEZIONE_NON_SPECIFICATA);
//				throw gpe;
			}
		}
	}
	
	private void handleAnnullamentoException(GpContext ctx, Exception e) throws GovPayException, NotAuthorizedException {
		if(e instanceof GovPayException) {
			GovPayException gpe = (GovPayException) e;
			ctx.log(LOG_KEY_VERSAMENTO_ANNULLA_KO, gpe.getCodEsito().toString(), gpe.getDescrizioneEsito(), gpe.getCausa() != null ? gpe.getCausa() : ECCEZIONE_NON_SPECIFICATA);
			throw (GovPayException) e;
		} else if(e instanceof NotAuthorizedException) { 
			NotAuthorizedException nae = (NotAuthorizedException) e;
			ctx.log(LOG_KEY_VERSAMENTO_ANNULLA_KO, "NOT_AUTHORIZED", nae.getDetails(), nae.getMessage() != null ? nae.getMessage() : ECCEZIONE_NON_SPECIFICATA);
			throw nae;
		} else {
			GovPayException gpe = new GovPayException(e);
			ctx.log(LOG_KEY_VERSAMENTO_ANNULLA_KO, gpe.getCodEsito().toString(), gpe.getDescrizioneEsito(), gpe.getCausa() != null ? gpe.getCausa() : ECCEZIONE_NON_SPECIFICATA);
			throw gpe;
		}
	}
	
	public void notificaPagamento(Applicazione applicazione, String codApplicazione, String codVersamentoEnte) throws GovPayException {
		try {
			VersamentiBD versamentiBD = new VersamentiBD(this);
			
			this.setAutoCommit(false);
			this.enableSelectForUpdate();
			
			try {
				it.govpay.bd.model.Versamento versamentoLetto = versamentiBD.getVersamento(applicazione.getId(), codVersamentoEnte);
			
				// Se è già ESEGUITO_SENZA_RPT non devo far nulla.
				if(versamentoLetto.getStatoVersamento().equals(StatoVersamento.ESEGUITO_SENZA_RPT)) {
					log.info("Versamento (" + versamentoLetto.getCodVersamentoEnte() + ") dell'applicazione (" + applicazione.getCodApplicazione() + ") gia' pagato senza rpt. Aggiornamento non necessario.");
					return;
				}
				
				// Se è in stato NON_ESEGUITO lo eseguo senza RPT //TODO aggiungere uno stato opportuno
				if(versamentoLetto.getStatoVersamento().equals(StatoVersamento.NON_ESEGUITO) || versamentoLetto.getStatoVersamento().equals(StatoVersamento.ANNULLATO)) {
					versamentoLetto.setStatoVersamento(StatoVersamento.ESEGUITO_SENZA_RPT);
					versamentiBD.updateVersamento(versamentoLetto);
					log.info("Versamento (" + versamentoLetto.getCodVersamentoEnte() + ") dell'applicazione (" + applicazione.getCodApplicazione() + ") pagato senza rpt.");
					return;
				}
				
				// Se non è ne ANNULLATO ne NON_ESEGUITO non lo posso annullare
				throw new GovPayException(EsitoOperazione.VER_016, codApplicazione, codVersamentoEnte, versamentoLetto.getStatoVersamento().toString());
				
			} catch (NotFoundException e) {
				// Versamento inesistente
				throw new GovPayException(EsitoOperazione.VER_008, codApplicazione, codVersamentoEnte);
			} finally {
				this.commit();
			}
		} catch (Exception e) {
			this.rollback();
			if(e instanceof GovPayException)
				throw (GovPayException) e;
			else 
				throw new GovPayException(e);
		}
	}

	public it.govpay.bd.model.Versamento chiediVersamento(RefVersamentoAvviso ref, Dominio dominio) throws ServiceException, GovPayException {
		// conversione numeroAvviso in iuv
		String iuv = it.govpay.core.utils.VersamentoUtils.getIuvFromNumeroAvviso(ref.getNumeroAvviso(),dominio.getCodDominio(),dominio.getStazione().getCodStazione(),dominio.getStazione().getApplicationCode(),dominio.getSegregationCode());
		return this.chiediVersamento(null, null, null, null, ref.getIdDominio(), iuv);	
	}

	public it.govpay.bd.model.Versamento chiediVersamento(RefVersamentoPendenza ref) throws ServiceException, GovPayException {
		return this.chiediVersamento(ref.getIdA2A(), ref.getIdPendenza(), null, null, null, null);
	}

	public it.govpay.bd.model.Versamento chiediVersamento(it.govpay.core.dao.commons.Versamento versamento) throws ServiceException, GovPayException {
		return VersamentoUtils.toVersamentoModel(versamento, this);
	}

	public it.govpay.bd.model.Versamento chiediVersamento(String codApplicazione, String codVersamentoEnte, String bundlekey, String codUnivocoDebitore, String codDominio, String iuv) throws ServiceException, GovPayException {
		GpContext ctx = GpThreadLocal.get();
		// Versamento per riferimento codApplicazione/codVersamentoEnte
		it.govpay.bd.model.Versamento versamentoModel = null;
		
		VersamentiBD versamentiBD = new VersamentiBD(this);
		
		if(codApplicazione != null && codVersamentoEnte != null) {
			ctx.log("rpt.acquisizioneVersamentoRef", codApplicazione, codVersamentoEnte);
			Applicazione applicazione = null;
			try {
				applicazione = AnagraficaManager.getApplicazione(this, codApplicazione);
			} catch (NotFoundException e) {
				throw new GovPayException(EsitoOperazione.APP_000, codApplicazione);
			}

			try {
				versamentoModel = versamentiBD.getVersamento(applicazione.getId(), codVersamentoEnte);
				versamentoModel.setIuvProposto(iuv);
			} catch (NotFoundException e) {
				// Non e' nel repo interno. vado oltre e lo richiedo all'applicazione gestrice
			}
		}


		// Versamento per riferimento codDominio/iuv
		if(codDominio != null && iuv != null) {
			ctx.log("rpt.acquisizioneVersamentoRefIuv", codDominio, iuv);

			Dominio dominio = null;
			try {
				dominio = AnagraficaManager.getDominio(this, codDominio);
			} catch (NotFoundException e) {
				throw new GovPayException(EsitoOperazione.DOM_000, codDominio);
			}

			IuvBD iuvBD = new IuvBD(this);
			
			it.govpay.model.Iuv iuvModel = null;
			try {
				iuvModel = iuvBD.getIuv(dominio.getId(), iuv);
				codApplicazione = AnagraficaManager.getApplicazione(this, iuvModel.getIdApplicazione()).getCodApplicazione();
				codVersamentoEnte = iuvModel.getCodVersamentoEnte();
			} catch (NotFoundException e) {
				// Iuv non registrato. Vedo se c'e' un'applicazione da interrogare, altrimenti non e' recuperabile.
				codApplicazione = new it.govpay.core.business.Applicazione(this).getApplicazioneDominio(dominio, iuv).getCodApplicazione();
				
				if(codApplicazione == null) {
					throw new GovPayException("L'avviso di pagamento [Dominio:" + codDominio + " Iuv:" + iuv + "] non risulta registrato, ne associabile ad un'applicazione censita.", EsitoOperazione.VER_008);
				}
			}

			// A questo punto ho sicuramente il codApplicazione. Se ho anche il codVersamentoEnte lo cerco localmente
			if(codVersamentoEnte != null) {
				try {
					versamentoModel = versamentiBD.getVersamento(AnagraficaManager.getApplicazione(this, codApplicazione).getId(), codVersamentoEnte);
				} catch (NotFoundException e) {
					// Non e' nel repo interno. vado oltre e lo richiedo all'applicazione gestrice
				}
			}
		}
			
		// Versamento per riferimento codApplicazione/bundlekey
		if(codApplicazione != null && bundlekey != null) {
			ctx.log("rpt.acquisizioneVersamentoRefBundle", codApplicazione, bundlekey, (codDominio != null ? codDominio : GpContext.NOT_SET), (codUnivocoDebitore != null ? codUnivocoDebitore : GpContext.NOT_SET));
			try {
				versamentoModel = versamentiBD.getVersamentoByBundlekey(AnagraficaManager.getApplicazione(this, codApplicazione).getId(), bundlekey, codDominio, codUnivocoDebitore);
			} catch (NotFoundException e) {
				// Non e' nel repo interno. vado oltre e lo richiedo all'applicazione gestrice
			}
		}
			
		// Se ancora non ho trovato il versamento, lo chiedo all'applicazione
		if(versamentoModel == null) {
			try {
				versamentoModel = it.govpay.core.utils.VersamentoUtils.acquisisciVersamento(AnagraficaManager.getApplicazione(this, codApplicazione), codVersamentoEnte, bundlekey, codUnivocoDebitore, codDominio, iuv, this);
			} catch (ClientException e){
				throw new GovPayException(EsitoOperazione.INTERNAL, "verifica del versamento [Versamento: " + codVersamentoEnte != null ? codVersamentoEnte : "-" + " BundleKey:" + bundlekey != null ? bundlekey : "-" + " Debitore:" + codUnivocoDebitore != null ? codUnivocoDebitore : "-" + " Dominio:" + codDominio != null ? codDominio : "-" + " Iuv:" + iuv != null ? iuv : "-" + "] all'applicazione competente [Applicazione:" + codApplicazione + "] e' fallita con errore: " + e.getMessage());
			} catch (VersamentoScadutoException e) {
				throw new GovPayException("La verifica del versamento [Versamento: " + codVersamentoEnte != null ? codVersamentoEnte : "-" + " BundleKey:" + bundlekey != null ? bundlekey : "-" + " Debitore:" + codUnivocoDebitore != null ? codUnivocoDebitore : "-" + " Dominio:" + codDominio != null ? codDominio : "-" + " Iuv:" + iuv != null ? iuv : "-" + "] all'applicazione competente [Applicazione:" + codApplicazione + "] ha dato esito PAA_PAGAMENTO_SCADUTO", EsitoOperazione.VER_010);
			} catch (VersamentoAnnullatoException e) {
				throw new GovPayException("La verifica del versamento [Versamento: " + codVersamentoEnte != null ? codVersamentoEnte : "-" + " BundleKey:" + bundlekey != null ? bundlekey : "-" + " Debitore:" + codUnivocoDebitore != null ? codUnivocoDebitore : "-" + " Dominio:" + codDominio != null ? codDominio : "-" + " Iuv:" + iuv != null ? iuv : "-" + "] all'applicazione competente [Applicazione:" + codApplicazione + "] ha dato esito PAA_PAGAMENTO_ANNULLATO", EsitoOperazione.VER_013);
			} catch (VersamentoDuplicatoException e) {
				throw new GovPayException("La verifica del versamento [Versamento: " + codVersamentoEnte != null ? codVersamentoEnte : "-" + " BundleKey:" + bundlekey != null ? bundlekey : "-" + " Debitore:" + codUnivocoDebitore != null ? codUnivocoDebitore : "-" + " Dominio:" + codDominio != null ? codDominio : "-" + " Iuv:" + iuv != null ? iuv : "-" + "] all'applicazione competente [Applicazione:" + codApplicazione + "] ha dato esito PAA_PAGAMENTO_DUPLICATO", EsitoOperazione.VER_012);
			} catch (VersamentoSconosciutoException e) {
				throw new GovPayException("La verifica del versamento [Versamento: " + codVersamentoEnte != null ? codVersamentoEnte : "-" + " BundleKey:" + bundlekey != null ? bundlekey : "-" + " Debitore:" + codUnivocoDebitore != null ? codUnivocoDebitore : "-" + " Dominio:" + codDominio != null ? codDominio : "-" + " Iuv:" + iuv != null ? iuv : "-" + "] all'applicazione competente [Applicazione:" + codApplicazione + "] ha dato esito PAA_PAGAMENTO_SCONOSCIUTO", EsitoOperazione.VER_011);
			} catch (NotFoundException e) {
				throw new GovPayException(EsitoOperazione.INTERNAL, "Il versamento [Versamento: " + codVersamentoEnte != null ? codVersamentoEnte : "-" + " BundleKey:" + bundlekey != null ? bundlekey : "-" + " Debitore:" + codUnivocoDebitore != null ? codUnivocoDebitore : "-" + " Dominio:" + codDominio != null ? codDominio : "-" + " Iuv:" + iuv != null ? iuv : "-" + "] e' gestito da un'applicazione non censita [Applicazione:" + codApplicazione + "]");
			}
		}
		
		return versamentoModel;
	}
	
	
	public it.govpay.bd.model.Versamento chiediVersamento(Applicazione applicazione, String codApplicazione, String codVersamentoEnte, String bundlekey, String codUnivocoDebitore, String codDominio, String iuv) throws ServiceException, GovPayException {
		List<Diritti> diritti = new ArrayList<>();
		diritti.add(Diritti.LETTURA);
		
		if(codDominio != null && !AclEngine.isAuthorized(applicazione.getUtenza(), Servizio.PAGAMENTI_E_PENDENZE, codDominio, null,diritti)) {
			throw new GovPayException(EsitoOperazione.APP_005);
		}
		
		it.govpay.bd.model.Versamento v = this.chiediVersamento(codApplicazione, codVersamentoEnte, bundlekey, codUnivocoDebitore, codDominio, iuv);
		
		if(AclEngine.isAuthorized(applicazione.getUtenza(), Servizio.PAGAMENTI_E_PENDENZE, v.getUo(this).getDominio(this).getCodDominio(), null,diritti)) {
			return v;
		} else {
			throw new GovPayException(EsitoOperazione.APP_005);
		}	
	}
	
	public List<it.govpay.bd.model.Versamento> chiediVersamenti(Applicazione applicazioneAutenticata, String codApplicazione, String codUnivocoDebitore, List<StatoVersamento> statiVersamento, VersamentoFilter.SortFields filterSortList) throws GovPayException, ServiceException {
		VersamentiBD versamentiBD = new VersamentiBD(this);
		VersamentoFilter filter = versamentiBD.newFilter();
		filter.setCodUnivocoDebitore(codUnivocoDebitore);
		filter.setStatiPagamento(statiVersamento);
		filter.addSortField(filterSortList);
		
		List<Long> domini = new ArrayList<>();
		List<Diritti> diritti = new ArrayList<>();
		diritti.add(Diritti.SCRITTURA);
		diritti.add(Diritti.ESECUZIONE);
		 List<Long> dominiSet = AclEngine.getIdDominiAutorizzati(applicazioneAutenticata.getUtenza(), Servizio.PAGAMENTI_E_PENDENZE, diritti);
				
		if(dominiSet != null) {
			domini.addAll(dominiSet);
			filter.setIdDomini(domini);
		}
		
		List<it.govpay.bd.model.Versamento> versamenti = versamentiBD.findAll(filter);
		for(it.govpay.bd.model.Versamento versamento : versamenti)
			try {
				it.govpay.core.utils.VersamentoUtils.aggiornaVersamento(versamento, this);
			} catch (Exception e) {
				// Aggiornamento andato male. risultera' scaduto.
			} 
		return versamenti;
	}
}
