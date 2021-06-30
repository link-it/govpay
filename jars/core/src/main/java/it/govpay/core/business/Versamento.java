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

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.UtilsException;
import org.openspcoop2.utils.json.ValidationException;
import org.openspcoop2.utils.logger.beans.Property;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.openspcoop2.utils.service.context.IContext;
import org.slf4j.Logger;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.NotificaAppIo;
import it.govpay.bd.model.Promemoria;
import it.govpay.bd.model.TipoVersamentoDominio;
import it.govpay.bd.pagamento.NotificheAppIoBD;
import it.govpay.bd.pagamento.PromemoriaBD;
import it.govpay.bd.pagamento.VersamentiBD;
import it.govpay.core.beans.EsitoOperazione;
import it.govpay.core.business.model.AnnullaVersamentoDTO;
import it.govpay.core.dao.pagamenti.dto.PagamentiPortaleDTO.RefVersamentoAvviso;
import it.govpay.core.dao.pagamenti.dto.PagamentiPortaleDTO.RefVersamentoPendenza;
import it.govpay.core.exceptions.EcException;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.core.exceptions.VersamentoAnnullatoException;
import it.govpay.core.exceptions.VersamentoDuplicatoException;
import it.govpay.core.exceptions.VersamentoNonValidoException;
import it.govpay.core.exceptions.VersamentoScadutoException;
import it.govpay.core.exceptions.VersamentoSconosciutoException;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.IuvUtils;
import it.govpay.core.utils.VersamentoUtils;
import it.govpay.core.utils.client.exception.ClientException;
import it.govpay.model.Iuv.TipoIUV;
import it.govpay.model.NotificaAppIo.TipoNotifica;
import it.govpay.model.Versamento.StatoPagamento;
import it.govpay.model.Versamento.StatoVersamento;
import it.govpay.model.Versamento.TipologiaTipoVersamento;

public class Versamento  {

	private static final String ECCEZIONE_NON_SPECIFICATA = "- Non specificata -";
	private static final String LOG_KEY_VERSAMENTO_ANNULLA_KO = "versamento.annullaKo";
	private static Logger log = LoggerWrapperFactory.getLogger(Versamento.class);

	public Versamento() {
	}

	@Deprecated
	public it.govpay.bd.model.Versamento caricaVersamento(it.govpay.bd.model.Versamento versamento, boolean generaIuv, boolean aggiornaSeEsiste, Boolean avvisatura, Date dataAvvisatura, BasicBD bd) throws GovPayException {
		// Indica se devo gestire la transazione oppure se e' gestita dal chiamante
		boolean doCommit = false;
		IContext ctx = ContextThreadLocal.get();
		GpContext appContext = (GpContext) ctx.getApplicationContext();
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		VersamentiBD versamentiBD = null;
		try {
			ctx.getApplicationLogger().log("versamento.validazioneSemantica", versamento.getApplicazione(configWrapper).getCodApplicazione(), versamento.getCodVersamentoEnte());
			VersamentoUtils.validazioneSemantica(versamento, generaIuv);
			ctx.getApplicationLogger().log("versamento.validazioneSemanticaOk", versamento.getApplicazione(configWrapper).getCodApplicazione(), versamento.getCodVersamentoEnte());

			if(bd == null) {
				versamentiBD = new VersamentiBD(configWrapper);
			} else {
				versamentiBD = new VersamentiBD(bd);
				versamentiBD.setAtomica(false);
			}

			appContext.getPagamentoCtx().loadVersamentoContext(versamento);

			try {
				it.govpay.bd.model.Versamento versamentoLetto = versamentiBD.getVersamento(versamento.getIdApplicazione(), versamento.getCodVersamentoEnte(), true);

				if(!aggiornaSeEsiste)
					throw new GovPayException(EsitoOperazione.VER_015, versamento.getApplicazione(configWrapper).getCodApplicazione(), versamento.getCodVersamentoEnte());

				// Versamento presente.
				versamento.setCreated(false);
				this.copiaPropertiesNonModificabiliVersamento(versamento, versamentoLetto);

				// se non erano stati assegnati o proposti iuv e numero avviso e ne e' richiesta l'assegnazione, li assegno
				if(versamento.getIuvVersamento() == null && generaIuv) {
					Iuv iuvBusiness = new Iuv();
					String iuv = iuvBusiness.generaIUV(versamento.getApplicazione(configWrapper), versamento.getDominio(configWrapper), versamento.getCodVersamentoEnte(), TipoIUV.NUMERICO, bd); 
					// imposto iuv calcolato
					versamento.setIuvVersamento(iuv);
					// calcolo il numero avviso
					it.govpay.core.business.model.Iuv iuvModel = IuvUtils.toIuv(versamento, versamento.getApplicazione(configWrapper), versamento.getDominio(configWrapper));
					versamento.setNumeroAvviso(iuvModel.getNumeroAvviso());
				}

				//				if(versamento.checkEsecuzioneUpdate(versamentoLetto)) {	}

				ctx.getApplicationLogger().log("versamento.validazioneSemanticaAggiornamento", versamento.getApplicazione(configWrapper).getCodApplicazione(), versamento.getCodVersamentoEnte());
				VersamentoUtils.validazioneSemanticaAggiornamento(versamentoLetto, versamento);
				ctx.getApplicationLogger().log("versamento.validazioneSemanticaAggiornamentoOk", versamento.getApplicazione(configWrapper).getCodApplicazione(), versamento.getCodVersamentoEnte());

				if(bd == null) {
					// creo connessione
					versamentiBD.setupConnection(versamentiBD.getIdTransaction());
	
					versamentiBD.setAutoCommit(false);
	
					doCommit = true;
	
					versamentiBD.setAtomica(false);
				}

				versamentiBD.updateVersamento(versamento, true);
				if(versamento.getId()==null)
					versamento.setId(versamentoLetto.getId());

				ctx.getApplicationLogger().log("versamento.aggioramentoOk", versamento.getApplicazione(configWrapper).getCodApplicazione(), versamento.getCodVersamentoEnte());

				log.info("Versamento (" + versamento.getCodVersamentoEnte() + ") dell'applicazione (" + versamento.getApplicazione(configWrapper).getCodApplicazione() + ") aggiornato");
			} catch (NotFoundException e) {
				if(versamento.getNumeroAvviso()!=null) {
					try {
						// 	verifica univocita dell'avviso pagamento prima di inserire il nuovo versamento
						it.govpay.bd.model.Versamento versamentoFromDominioNumeroAvviso = versamentiBD.getVersamentoByDominioIuv(versamento.getDominio(configWrapper).getId(), IuvUtils.toIuv(versamento.getNumeroAvviso()));

						// due pendenze non possono avere lo stesso numero avviso
						//if(!versamentoFromDominioNumeroAvviso.getCodVersamentoEnte().equals(versamento.getCodVersamentoEnte()))
							throw new GovPayException(EsitoOperazione.VER_025, versamento.getApplicazione(configWrapper).getCodApplicazione(), versamento.getCodVersamentoEnte(), 
									versamentoFromDominioNumeroAvviso.getApplicazione(configWrapper).getCodApplicazione(), versamentoFromDominioNumeroAvviso.getCodVersamentoEnte(),versamento.getNumeroAvviso());

					}catch(NotFoundException e2) {
						// ignore
					}
				} else if(generaIuv) {
					// Non ha numero avviso, ma e' richiesto che lo abbia
					Iuv iuvBusiness = new Iuv();
					String iuv = iuvBusiness.generaIUV(versamento.getApplicazione(configWrapper), versamento.getDominio(configWrapper), versamento.getCodVersamentoEnte(), TipoIUV.NUMERICO, bd);
					// imposto iuv calcolato
					versamento.setIuvVersamento(iuv);
					// calcolo il numero avviso
					it.govpay.core.business.model.Iuv iuvModel = IuvUtils.toIuv(versamento, versamento.getApplicazione(configWrapper), versamento.getDominio(configWrapper));
					versamento.setNumeroAvviso(iuvModel.getNumeroAvviso());
				}

				if(bd == null) {
					// creo connessione
					versamentiBD.setupConnection(versamentiBD.getIdTransaction());
	
					versamentiBD.setAutoCommit(false);
	
					doCommit = true;
	
					versamentiBD.setAtomica(false);
				}

				// Versamento nuovo. Inserisco versamento ed eventuale promemoria avviso
				versamento.setCreated(true);
				TipoVersamentoDominio tipoVersamentoDominio = versamento.getTipoVersamentoDominio(configWrapper);
				//				Promemoria promemoria = null;

				boolean inserisciNotificaAvviso = false;
				boolean inserisciNotificaPromemoriaScadenzaMail = false;
				boolean inserisciNotificaPromemoriaScadenzaAppIO = false;
				BigDecimal giorniPreavvisoMail = null;
				BigDecimal giorniPreavvisoAppIO = null;

				if((tipoVersamentoDominio.getAvvisaturaMailPromemoriaAvvisoAbilitato() || 
						tipoVersamentoDominio.getAvvisaturaAppIoPromemoriaAvvisoAbilitato()) && !(avvisatura != null && avvisatura.booleanValue()==false)) {
					log.debug("Schedulazione invio avviso di pagamento impostata.");
					inserisciNotificaAvviso = true;
				}

				if(tipoVersamentoDominio.getAvvisaturaAppIoPromemoriaScadenzaAbilitato() && !(avvisatura != null && avvisatura.booleanValue()==false)) {
					log.debug("Schedulazione invio scadenza avviso di pagamento impostata.");
					inserisciNotificaPromemoriaScadenzaAppIO = true;
					giorniPreavvisoAppIO = tipoVersamentoDominio.getAvvisaturaAppIoPromemoriaScadenzaPreavviso();

					if(giorniPreavvisoAppIO == null)
						giorniPreavvisoAppIO = new BigDecimal(new it.govpay.core.business.Configurazione().getConfigurazione().getAvvisaturaViaAppIo().getPromemoriaScadenza().getPreavviso());
				}

				if(tipoVersamentoDominio.getAvvisaturaMailPromemoriaScadenzaAbilitato() && !(avvisatura != null && avvisatura.booleanValue()==false)) {
					log.debug("Schedulazione invio scadenza avviso di pagamento impostata.");
					inserisciNotificaPromemoriaScadenzaMail = true;
					giorniPreavvisoMail = tipoVersamentoDominio.getAvvisaturaMailPromemoriaScadenzaPreavviso();

					if(giorniPreavvisoMail == null)
						giorniPreavvisoMail = new BigDecimal(new it.govpay.core.business.Configurazione().getConfigurazione().getAvvisaturaViaMail().getPromemoriaScadenza().getPreavviso());
				}

				// dataNotificaAvviso e avvisoNotificato
				if(inserisciNotificaAvviso) {
					if(versamento.getDataNotificaAvviso() == null)
						versamento.setDataNotificaAvviso(versamento.getDataCreazione());
				}

				if(versamento.getDataNotificaAvviso() != null)
					versamento.setAvvisoNotificato(false);

				// dataPromemoriaScadenza e promemoriaNotificato
				if(inserisciNotificaPromemoriaScadenzaMail) {
					if(versamento.getAvvMailDataPromemoriaScadenza() == null) {
						Date dataValidita = versamento.getDataValidita() != null ? versamento.getDataValidita() : versamento.getDataScadenza();
						if(dataValidita != null) {
							Calendar c = Calendar.getInstance();
							c.setTime(dataValidita);
							c.add(Calendar.DATE, -(giorniPreavvisoMail.intValue()));
							versamento.setAvvMailDataPromemoriaScadenza(c.getTime());
						}
					}
				}

				if(versamento.getAvvMailDataPromemoriaScadenza() != null)
					versamento.setAvvMailPromemoriaScadenzaNotificato(false);

				if(inserisciNotificaPromemoriaScadenzaAppIO) {
					if(versamento.getAvvAppIODataPromemoriaScadenza() == null) {
						Date dataValidita = versamento.getDataValidita() != null ? versamento.getDataValidita() : versamento.getDataScadenza();
						if(dataValidita != null) {
							Calendar c = Calendar.getInstance();
							c.setTime(dataValidita);
							c.add(Calendar.DATE, -(giorniPreavvisoAppIO.intValue()));
							versamento.setAvvAppIODataPromemoriaScadenza(c.getTime());
						}
					}
				}

				if(versamento.getAvvAppIODataPromemoriaScadenza() != null)
					versamento.setAvvAppIOPromemoriaScadenzaNotificato(false);

				// generazione UUID creazione
				versamento.setIdSessione(UUID.randomUUID().toString().replace("-", ""));
				if(versamento.getStatoPagamento() == null) {
					versamento.setStatoPagamento(StatoPagamento.NON_PAGATO);
					versamento.setImportoIncassato(BigDecimal.ZERO);
					versamento.setImportoPagato(BigDecimal.ZERO);
				}

				versamentiBD.insertVersamento(versamento);
				ctx.getApplicationLogger().log("versamento.inserimentoOk", versamento.getApplicazione(configWrapper).getCodApplicazione(), versamento.getCodVersamentoEnte());
				log.info("Versamento (" + versamento.getCodVersamentoEnte() + ") dell'applicazione (" + versamento.getApplicazione(configWrapper).getCodApplicazione() + ") inserito");

				// avvio il batch di gestione dei promemoria
				Operazioni.setEseguiGestionePromemoria();
			}
			if(doCommit) versamentiBD.commit();

			return versamento;
		} catch (Exception e) {
			if(doCommit) {
				if(versamentiBD != null)
					versamentiBD.rollback();
			}
			if(e instanceof GovPayException)
				throw (GovPayException) e;
			else 
				throw new GovPayException(e);
		} finally {
			if(versamentiBD != null  && bd == null)
				versamentiBD.closeConnection();
		}
	}

	private void copiaPropertiesNonModificabiliVersamento(it.govpay.bd.model.Versamento versamento, it.govpay.bd.model.Versamento versamentoLetto) {
		// riporto informazioni che non si modificano
		versamento.setTipo(versamentoLetto.getTipo());
		versamento.setAck(versamentoLetto.isAck());
		versamento.setDataCreazione(versamentoLetto.getDataCreazione());
		versamento.setIdSessione(versamentoLetto.getIdSessione());
		versamento.setStatoPagamento(versamentoLetto.getStatoPagamento());
		versamento.setImportoPagato(versamentoLetto.getImportoPagato());
		versamento.setImportoIncassato(versamentoLetto.getImportoIncassato());
		versamento.setDataNotificaAvviso(versamentoLetto.getDataNotificaAvviso());
		versamento.setAvvisoNotificato(versamentoLetto.getAvvisoNotificato());
		versamento.setAvvMailDataPromemoriaScadenza(versamentoLetto.getAvvMailDataPromemoriaScadenza()); 
		versamento.setAvvMailPromemoriaScadenzaNotificato(versamentoLetto.getAvvMailPromemoriaScadenzaNotificato());
		versamento.setAvvAppIODataPromemoriaScadenza(versamentoLetto.getAvvAppIODataPromemoriaScadenza()); 
		versamento.setAvvAppIOPromemoriaScadenzaNotificato(versamentoLetto.getAvvAppIOPromemoriaScadenzaNotificato());
		versamento.setProprietaPendenza(versamentoLetto.getProprietaPendenza());

		// riporto iuv e numero avviso che sono gia' stati assegnati
		if(versamento.getIuvVersamento() == null) {
			versamento.setIuvVersamento(versamentoLetto.getIuvVersamento());
			versamento.setNumeroAvviso(versamentoLetto.getNumeroAvviso());
		}

		// idDocumento
		versamento.setIdDocumento(versamentoLetto.getIdDocumento());
	}

	public void annullaVersamento(AnnullaVersamentoDTO annullaVersamentoDTO) throws GovPayException, NotAuthorizedException, UtilsException {
		log.info("Richiesto annullamento per il Versamento (" + annullaVersamentoDTO.getCodVersamentoEnte() + ") dell'applicazione (" + annullaVersamentoDTO.getCodApplicazione() + ")");

		IContext ctx = ContextThreadLocal.get();
		GpContext appContext = (GpContext) ctx.getApplicationContext();
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);

		if(!appContext.hasCorrelationId()) appContext.setCorrelationId(annullaVersamentoDTO.getCodApplicazione() + annullaVersamentoDTO.getCodVersamentoEnte());
		appContext.getRequest().addGenericProperty(new Property("codApplicazione", annullaVersamentoDTO.getCodApplicazione()));
		appContext.getRequest().addGenericProperty(new Property("codVersamentoEnte", annullaVersamentoDTO.getCodVersamentoEnte()));
		ctx.getApplicationLogger().log("versamento.annulla");

		if(annullaVersamentoDTO.getApplicazione() != null && !annullaVersamentoDTO.getApplicazione().getCodApplicazione().equals(annullaVersamentoDTO.getCodApplicazione())) {
			throw new NotAuthorizedException("Applicazione chiamante [" + annullaVersamentoDTO.getApplicazione().getCodApplicazione() + "] non e' proprietaria del versamento");
		}

		String codApplicazione = annullaVersamentoDTO.getCodApplicazione();
		String codVersamentoEnte = annullaVersamentoDTO.getCodVersamentoEnte();

		VersamentiBD versamentiBD = null;
		try {
			versamentiBD = new VersamentiBD(configWrapper);

			versamentiBD.setupConnection(configWrapper.getTransactionID());

			versamentiBD.setAutoCommit(false);

			versamentiBD.enableSelectForUpdate();

			versamentiBD.setAtomica(false);

			try {
				it.govpay.bd.model.Versamento versamentoLetto = versamentiBD.getVersamento(AnagraficaManager.getApplicazione(configWrapper, codApplicazione).getId(), codVersamentoEnte);

				// Il controllo sul dominio disponibile per l'operatore riferito delle pendenze del tracciato e' gia' stato fatto durante l'operazione di caricamento tracciato.
				//				if(annullaVersamentoDTO.getOperatore() != null && 
				//						!AclEngine.isAuthorized(annullaVersamentoDTO.getOperatore().getUtenza(), Servizio.PAGAMENTI_E_PENDENZE, versamentoLetto.getUo(this).getDominio(this).getCodDominio(), null, Arrays.asList(Diritti.SCRITTURA,Diritti.ESECUZIONE))){
				//					throw new NotAuthorizedException("Operatore chiamante [" + annullaVersamentoDTO.getOperatore().getPrincipal() + "] non autorizzato in scrittura per il dominio " + versamentoLetto.getUo(this).getDominio(this).getCodDominio());
				//				}
				// Se è già annullato non devo far nulla.
				if(versamentoLetto.getStatoVersamento().equals(StatoVersamento.ANNULLATO)) {
					log.info("Versamento (" + versamentoLetto.getCodVersamentoEnte() + ") dell'applicazione (" + codApplicazione + ") gia' annullato. Aggiornamento non necessario.");
					ctx.getApplicationLogger().log("versamento.annullaOk");
					return;
				}

				// Se è in stato NON_ESEGUITO lo annullo ed aggiorno lo stato avvisatura
				if(versamentoLetto.getStatoVersamento().equals(StatoVersamento.NON_ESEGUITO)) {
					versamentoLetto.setStatoVersamento(StatoVersamento.ANNULLATO);
					versamentoLetto.setDescrizioneStato(annullaVersamentoDTO.getMotivoAnnullamento()); 
					versamentoLetto.setAvvisoNotificato(null);
					versamentoLetto.setAvvMailPromemoriaScadenzaNotificato(null);
					versamentoLetto.setAvvAppIOPromemoriaScadenzaNotificato(null);
					versamentiBD.updateVersamento(versamentoLetto);
					log.info("Versamento (" + versamentoLetto.getCodVersamentoEnte() + ") dell'applicazione (" + codApplicazione + ") annullato.");
					ctx.getApplicationLogger().log("versamento.annullaOk");
					return;
				}

				// Se non è ne ANNULLATO ne NON_ESEGUITO non lo posso annullare
				throw new GovPayException(EsitoOperazione.VER_009, codApplicazione, codVersamentoEnte, versamentoLetto.getStatoVersamento().toString());

			} catch (NotFoundException e) {
				// Versamento inesistente
				throw new GovPayException(EsitoOperazione.VER_008, codApplicazione, codVersamentoEnte);
			} finally {
				versamentiBD.commit();
			}
		} catch (Exception e) {
			if(versamentiBD != null)
				versamentiBD.rollback();
			this.handleAnnullamentoException(ctx, e);
		} finally {
			if(versamentiBD != null) {
				try {
					versamentiBD.disableSelectForUpdate();
				} catch (ServiceException e) {
					//				GovPayException gpe = new GovPayException(e);
					//				ctx.getApplicationLogger().log(LOG_KEY_VERSAMENTO_ANNULLA_KO, gpe.getCodEsito().toString(), gpe.getDescrizioneEsito(), gpe.getCausa() != null ? gpe.getCausa() : ECCEZIONE_NON_SPECIFICATA);
					//				throw gpe;
				}

				versamentiBD.closeConnection();
			}
		}
	}

	private void handleAnnullamentoException(IContext ctx, Exception e) throws GovPayException, NotAuthorizedException, UtilsException {
		if(e instanceof GovPayException) {
			GovPayException gpe = (GovPayException) e;
			ctx.getApplicationLogger().log(LOG_KEY_VERSAMENTO_ANNULLA_KO, gpe.getCodEsito().toString(), gpe.getDescrizioneEsito(), gpe.getCausa() != null ? gpe.getCausa() : ECCEZIONE_NON_SPECIFICATA);
			throw (GovPayException) e;
		} else if(e instanceof NotAuthorizedException) { 
			NotAuthorizedException nae = (NotAuthorizedException) e;
			ctx.getApplicationLogger().log(LOG_KEY_VERSAMENTO_ANNULLA_KO, "NOT_AUTHORIZED", nae.getDetails(), nae.getMessage() != null ? nae.getMessage() : ECCEZIONE_NON_SPECIFICATA);
			throw nae;
		} else {
			GovPayException gpe = new GovPayException(e);
			ctx.getApplicationLogger().log(LOG_KEY_VERSAMENTO_ANNULLA_KO, gpe.getCodEsito().toString(), gpe.getDescrizioneEsito(), gpe.getCausa() != null ? gpe.getCausa() : ECCEZIONE_NON_SPECIFICATA);
			throw gpe;
		}
	}

	public it.govpay.bd.model.Versamento chiediVersamento(RefVersamentoAvviso ref, Dominio dominio) throws ServiceException, GovPayException, UtilsException, EcException {
		// conversione numeroAvviso in iuv
		String iuv = VersamentoUtils.getIuvFromNumeroAvviso(ref.getNumeroAvviso());
		return this.chiediVersamento(null, null, null, null, ref.getIdDominio(), iuv, TipologiaTipoVersamento.DOVUTO);	
	}

	public it.govpay.bd.model.Versamento chiediVersamento(RefVersamentoPendenza ref) throws ServiceException, GovPayException, UtilsException, EcException {
		return this.chiediVersamento(ref.getIdA2A(), ref.getIdPendenza(), null, null, null, null, TipologiaTipoVersamento.DOVUTO);
	}

	public it.govpay.bd.model.Versamento chiediVersamento(it.govpay.core.dao.commons.Versamento versamento) throws ServiceException, GovPayException, ValidationException { 
		return VersamentoUtils.toVersamentoModel(versamento);
	}

	public it.govpay.bd.model.Versamento chiediVersamento(String codApplicazione, String codVersamentoEnte, String bundlekey, String codUnivocoDebitore, String codDominio, String iuv, TipologiaTipoVersamento tipo) throws ServiceException, GovPayException, UtilsException, EcException {
		IContext ctx = ContextThreadLocal.get();
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		// Versamento per riferimento codApplicazione/codVersamentoEnte
		it.govpay.bd.model.Versamento versamentoModel = null;

		VersamentiBD versamentiBD = new VersamentiBD(configWrapper);

		if(codApplicazione != null && codVersamentoEnte != null) {
			ctx.getApplicationLogger().log("rpt.acquisizioneVersamentoRef", codApplicazione, codVersamentoEnte);
			Applicazione applicazione = null;
			try {
				applicazione = AnagraficaManager.getApplicazione(configWrapper, codApplicazione);
			} catch (NotFoundException e) {
				throw new GovPayException(EsitoOperazione.APP_000, codApplicazione);
			}

			if(!applicazione.getUtenza().isAbilitato())
				throw new GovPayException(EsitoOperazione.APP_001, applicazione.getCodApplicazione());

			try {
				versamentoModel = versamentiBD.getVersamento(applicazione.getId(), codVersamentoEnte);
				versamentoModel.setIuvProposto(iuv);
			} catch (NotFoundException e) {
				// Non e' nel repo interno. vado oltre e lo richiedo all'applicazione gestrice
			}
		}


		// Versamento per riferimento codDominio/iuv
		if(codDominio != null && iuv != null) {
			ctx.getApplicationLogger().log("rpt.acquisizioneVersamentoRefIuv", codDominio, iuv);

			Dominio dominio = null;
			try {
				dominio = AnagraficaManager.getDominio(configWrapper, codDominio);
			} catch (NotFoundException e) {
				throw new GovPayException(EsitoOperazione.DOM_000, codDominio);
			}

			if(!dominio.isAbilitato())
				throw new GovPayException(EsitoOperazione.DOM_001, dominio.getCodDominio());

			try {
				versamentoModel = versamentiBD.getVersamentoByDominioIuv(dominio.getId(), iuv);
				codApplicazione = versamentoModel.getApplicazione(configWrapper).getCodApplicazione();
				codVersamentoEnte = versamentoModel.getCodVersamentoEnte();
			} catch (NotFoundException e) {
				// Iuv non registrato. Vedo se c'e' un'applicazione da interrogare, altrimenti non e' recuperabile.
				Applicazione applicazioneDominio = new it.govpay.core.business.Applicazione().getApplicazioneDominio(configWrapper, dominio, iuv, false);
				if(applicazioneDominio == null) {
					throw new GovPayException("L'avviso di pagamento [Dominio:" + codDominio + " Iuv:" + iuv + "] non risulta registrato, ne associabile ad un'applicazione censita.", EsitoOperazione.VER_008);
				}
				codApplicazione = applicazioneDominio.getCodApplicazione();
			}

			// A questo punto ho sicuramente il codApplicazione. Se ho anche il codVersamentoEnte lo cerco localmente
			if(codVersamentoEnte != null) {
				try {
					versamentoModel = versamentiBD.getVersamento(AnagraficaManager.getApplicazione(configWrapper, codApplicazione).getId(), codVersamentoEnte);
				} catch (NotFoundException e) {
					// Non e' nel repo interno. vado oltre e lo richiedo all'applicazione gestrice
				}
			}
		}

		// Versamento per riferimento codApplicazione/bundlekey
		if(codApplicazione != null && bundlekey != null) {
			ctx.getApplicationLogger().log("rpt.acquisizioneVersamentoRefBundle", codApplicazione, bundlekey, (codDominio != null ? codDominio : GpContext.NOT_SET), (codUnivocoDebitore != null ? codUnivocoDebitore : GpContext.NOT_SET));
			try {
				versamentoModel = versamentiBD.getVersamentoByBundlekey(AnagraficaManager.getApplicazione(configWrapper, codApplicazione).getId(), bundlekey, codDominio, codUnivocoDebitore);
			} catch (NotFoundException e) {
				// Non e' nel repo interno. vado oltre e lo richiedo all'applicazione gestrice
			}
		}

		// Se ancora non ho trovato il versamento, lo chiedo all'applicazione
		if(versamentoModel == null) {
			try {
				versamentoModel = VersamentoUtils.acquisisciVersamento(AnagraficaManager.getApplicazione(configWrapper, codApplicazione), codVersamentoEnte, bundlekey, codUnivocoDebitore, codDominio, iuv, tipo);
			} catch (ClientException e){
				throw new EcException("La verifica del versamento [Versamento: " + codVersamentoEnte != null ? codVersamentoEnte : "-" + " BundleKey:" + bundlekey != null ? bundlekey : "-" + " Debitore:" + codUnivocoDebitore != null ? codUnivocoDebitore : "-" + " Dominio:" + codDominio != null ? codDominio : "-" + " Iuv:" + iuv != null ? iuv : "-" + "] all'applicazione competente [Applicazione:" + codApplicazione + "] e' fallita con errore: " + e.getMessage());
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
			} catch (VersamentoNonValidoException e) { 
				throw new GovPayException(EsitoOperazione.INTERNAL, "verifica del versamento [Versamento: " + codVersamentoEnte != null ? codVersamentoEnte : "-" + " BundleKey:" + bundlekey != null ? bundlekey : "-" + " Debitore:" + codUnivocoDebitore != null ? codUnivocoDebitore : "-" + " Dominio:" + codDominio != null ? codDominio : "-" + " Iuv:" + iuv != null ? iuv : "-" + "] all'applicazione competente [Applicazione:" + codApplicazione + "] e' fallita con errore: " + e.getMessage());
			}
		}

		return versamentoModel;
	}

	public void inserisciPromemoriaScadenzaMail(it.govpay.bd.model.Versamento versamento) throws ServiceException {
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);

		VersamentiBD versamentiBD = null;
		try {
			versamentiBD = new VersamentiBD(configWrapper);

			versamentiBD.setupConnection(configWrapper.getTransactionID());

			versamentiBD.setAutoCommit(false);

			versamentiBD.setAtomica(false);

			String codApplicazione = versamento.getApplicazione(configWrapper).getCodApplicazione();
			String codVersamentoEnte = versamento.getCodVersamentoEnte();
			Promemoria promemoria = null;
			try {
				log.debug("Inserimento promemoria scadenza per il versamento [IdA2A" + codApplicazione +", CodVersamentoEnte "+codVersamentoEnte+"]");
				TipoVersamentoDominio tipoVersamentoDominio = versamento.getTipoVersamentoDominio(configWrapper);

				if(tipoVersamentoDominio.getAvvisaturaMailPromemoriaScadenzaAbilitato()) {
					log.debug("Schedulazione invio avviso di scadenza pagamento in corso...");
					it.govpay.core.business.Promemoria promemoriaBD = new it.govpay.core.business.Promemoria();
					promemoria = promemoriaBD.creaPromemoriaScadenza(versamento, tipoVersamentoDominio, null);
					String msg = "e' stato trovato un destinatario valido, l'invio e' stato schedulato con successo.";
					if(promemoria.getDestinatarioTo() == null) {
						msg = "non e' stato trovato un destinatario valido, l'invio non verra' schedulato.";
						promemoria = null;
					}
					log.debug("Creazione promemoria scadenza completata: "+ msg);
				}

				// promemoria mail
				if(promemoria != null) {
					if(versamento.getIdDocumento() == null)
						promemoria.setIdVersamento(versamento.getId());
					else 
						promemoria.setIdDocumento(versamento.getIdDocumento());

					PromemoriaBD promemoriaBD = new PromemoriaBD(versamentiBD);
					promemoriaBD.setAtomica(false); // riuso connessione
					promemoriaBD.insertPromemoria(promemoria);
				}

				// aggiornamento stato notifica versamento
				versamento.setAvvMailPromemoriaScadenzaNotificato(true);

				versamentiBD.updateStatoPromemoriaScadenzaMailVersamento(versamento.getId(), true, true);

				versamentiBD.commit();

				log.debug("Inserimento promemoria scadenza per il versamento [IdA2A" + codApplicazione +", CodVersamentoEnte "+codVersamentoEnte+"] completato");
			} catch(Throwable e) {
				log.error("Errore durante l'inserimento promemoria scadenza per il versamento [IdA2A" + codApplicazione +", CodVersamentoEnte "+codVersamentoEnte+"]: " + e.getMessage(),e);
				if(versamentiBD != null)
					versamentiBD.rollback();
			} finally {
			} 
		}finally {
			if(versamentiBD != null)
				versamentiBD.closeConnection();
		}
	}

	public void inserisciPromemoriaScadenzaAppIO(it.govpay.bd.model.Versamento versamento) throws ServiceException {
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		VersamentiBD versamentiBD = null;
		try {
			versamentiBD = new VersamentiBD(configWrapper);

			versamentiBD.setupConnection(configWrapper.getTransactionID());

			versamentiBD.setAutoCommit(false);

			versamentiBD.setAtomica(false);

			String codApplicazione = versamento.getApplicazione(configWrapper).getCodApplicazione();
			String codVersamentoEnte = versamento.getCodVersamentoEnte();
			it.govpay.bd.model.NotificaAppIo notificaAppIo = null;
			try {
				log.debug("Inserimento promemoria scadenza per il versamento [IdA2A" + codApplicazione +", CodVersamentoEnte "+codVersamentoEnte+"]");
				TipoVersamentoDominio tipoVersamentoDominio = versamento.getTipoVersamentoDominio(configWrapper);

				if(tipoVersamentoDominio.getAvvisaturaAppIoPromemoriaScadenzaAbilitato()) {
					notificaAppIo = new NotificaAppIo(versamento, TipoNotifica.SCADENZA, configWrapper);
				}

				// notifica AppIO
				if(notificaAppIo != null) {
					notificaAppIo.setIdVersamento(versamento.getId());
					NotificheAppIoBD notificheAppIoBD = new NotificheAppIoBD(versamentiBD);
					notificheAppIoBD.setAtomica(false); // riuso connessione
					notificheAppIoBD.insertNotifica(notificaAppIo);
				}

				// aggiornamento stato notifica versamento
				versamento.setAvvAppIOPromemoriaScadenzaNotificato(true);
				versamentiBD.updateStatoPromemoriaScadenzaAppIOVersamento(versamento.getId(), true, true);

				versamentiBD.commit();

				log.debug("Inserimento promemoria scadenza per il versamento [IdA2A" + codApplicazione +", CodVersamentoEnte "+codVersamentoEnte+"] completato");
			} catch(Throwable e) {
				log.error("Errore durante l'inserimento promemoria scadenza per il versamento [IdA2A" + codApplicazione +", CodVersamentoEnte "+codVersamentoEnte+"]: " + e.getMessage(),e);
				if(versamentiBD != null)
					versamentiBD.rollback();
			} finally {
			}
		}finally {
			if(versamentiBD != null)
				versamentiBD.closeConnection();
		}
	}

	public void inserisciPromemoriaAvviso(it.govpay.bd.model.Versamento versamento) throws ServiceException{
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);

		VersamentiBD versamentiBD = null;
		try {
			versamentiBD = new VersamentiBD(configWrapper);

			versamentiBD.setupConnection(configWrapper.getTransactionID());

			versamentiBD.setAutoCommit(false);

			versamentiBD.setAtomica(false);

			String codApplicazione = versamento.getApplicazione(configWrapper).getCodApplicazione();
			String codVersamentoEnte = versamento.getCodVersamentoEnte();
			Promemoria promemoria = null;
			it.govpay.bd.model.NotificaAppIo notificaAppIo = null;

			try {
				log.debug("Inserimento promemoria avviso per il versamento [IdA2A" + codApplicazione +", CodVersamentoEnte "+codVersamentoEnte+"]");
				TipoVersamentoDominio tipoVersamentoDominio = versamento.getTipoVersamentoDominio(configWrapper);

				if(tipoVersamentoDominio.getAvvisaturaMailPromemoriaAvvisoAbilitato()) {
					log.debug("Schedulazione invio avviso di pagamento in corso...");
					it.govpay.core.business.Promemoria promemoriaBD = new it.govpay.core.business.Promemoria();
					promemoria = promemoriaBD.creaPromemoriaAvviso(versamento, tipoVersamentoDominio, null);

					String msg = "e' stato trovato un destinatario valido, l'invio e' stato schedulato con successo.";
					if(promemoria.getDestinatarioTo() == null) {
						msg = "non e' stato trovato un destinatario valido, l'invio non verra' schedulato.";
						promemoria = null;
					}
					log.debug("Creazione promemoria avviso completata: "+ msg);
				} 

				if(tipoVersamentoDominio.getAvvisaturaAppIoPromemoriaAvvisoAbilitato()) {
					log.debug("Creo notifica avvisatura tramite App IO..."); 
					notificaAppIo = new NotificaAppIo(versamento, TipoNotifica.AVVISO, configWrapper);
					log.debug("Creazione notifica avvisatura tramite App IO completata.");
				}
				// promemoria mail
				if(promemoria != null) {
					if(versamento.getIdDocumento() == null)
						promemoria.setIdVersamento(versamento.getId());
					else 
						promemoria.setIdDocumento(versamento.getIdDocumento());

					PromemoriaBD promemoriaBD = new PromemoriaBD(versamentiBD);
					promemoriaBD.setAtomica(false); // riuso connessione
					promemoriaBD.insertPromemoria(promemoria);
				}

				// notifica AppIO
				if(notificaAppIo != null) {
					notificaAppIo.setIdVersamento(versamento.getId());
					NotificheAppIoBD notificheAppIoBD = new NotificheAppIoBD(versamentiBD);
					notificheAppIoBD.setAtomica(false); // riuso connessione
					notificheAppIoBD.insertNotifica(notificaAppIo);
				}

				// aggiornamento stato notifica versamento
				versamento.setAvvisoNotificato(true);
				versamentiBD.updateStatoPromemoriaAvvisoVersamento(versamento.getId(), true, true);
				versamentiBD.commit();

				log.debug("Inserimento promemoria avviso per il versamento [IdA2A" + codApplicazione +", CodVersamentoEnte "+codVersamentoEnte+"] completato");
			} catch(Throwable e) {
				log.error("Errore durante l'inserimento promemoria avviso per il versamento [IdA2A" + codApplicazione +", CodVersamentoEnte "+codVersamentoEnte+"]: " + e.getMessage(),e);
				if(versamentiBD != null)
					versamentiBD.rollback();
			} finally {
			}
		}finally {
			if(versamentiBD != null)
				versamentiBD.closeConnection();
		}
	}
}
