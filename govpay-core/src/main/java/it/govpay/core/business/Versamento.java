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
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.GovpayConfig;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.model.Dominio;
import it.govpay.model.Acl.Servizio;
import it.govpay.model.Applicazione;
import it.govpay.model.Iuv.TipoIUV;
import it.govpay.model.Portale;
import it.govpay.model.Versamento.StatoVersamento;
import it.govpay.bd.pagamento.IuvBD;
import it.govpay.bd.pagamento.VersamentiBD;
import it.govpay.bd.pagamento.filters.VersamentoFilter;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.exceptions.VersamentoAnnullatoException;
import it.govpay.core.exceptions.VersamentoDuplicatoException;
import it.govpay.core.exceptions.VersamentoScadutoException;
import it.govpay.core.exceptions.VersamentoSconosciutoException;
import it.govpay.core.utils.AclEngine;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.core.utils.VersamentoUtils;
import it.govpay.core.utils.client.BasicClient.ClientException;
import it.govpay.servizi.commons.EsitoOperazione;

public class Versamento extends BasicBD {

	private static Logger log = LogManager.getLogger();
	
	public Versamento(BasicBD basicBD) {
		super(basicBD);
	}
	
	@Deprecated
	public it.govpay.model.Iuv caricaVersamento(Applicazione applicazioneAutenticata, it.govpay.bd.model.Versamento versamentoModel, boolean generaIuv, boolean aggiornaSeEsiste) throws GovPayException { 
		try {
			return caricaVersamento(versamentoModel, generaIuv, aggiornaSeEsiste);
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
			VersamentoUtils.validazioneSemantica(versamento, generaIuv, this);
			ctx.log("versamento.validazioneSemanticaOk", versamento.getApplicazione(this).getCodApplicazione(), versamento.getCodVersamentoEnte());
			
			VersamentiBD versamentiBD = new VersamentiBD(this);
			
			if(isAutoCommit()) {
				setAutoCommit(false);
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
					iuv = iuvBusiness.generaIUV(versamento.getApplicazione(this), versamento.getUo(this).getDominio(this), versamento.getCodVersamentoEnte());
				}
			}
			
			
			try {
				it.govpay.bd.model.Versamento versamentoLetto = versamentiBD.getVersamento(versamento.getIdApplicazione(), versamento.getCodVersamentoEnte());
				// Versamento presente. Verifico e aggiorno
				
				if(!aggiornaSeEsiste)
					throw new GovPayException(EsitoOperazione.VER_015, versamento.getApplicazione(this).getCodApplicazione(), versamento.getCodVersamentoEnte());
				
				ctx.log("versamento.validazioneSemanticaAggiornamento", versamento.getApplicazione(this).getCodApplicazione(), versamento.getCodVersamentoEnte());
				VersamentoUtils.validazioneSemanticaAggiornamento(versamentoLetto, versamento, this);
				ctx.log("versamento.validazioneSemanticaAggiornamentoOk", versamento.getApplicazione(this).getCodApplicazione(), versamento.getCodVersamentoEnte());
				
				versamentiBD.updateVersamento(versamento, true);
				ctx.log("versamento.aggioramentoOk", versamento.getApplicazione(this).getCodApplicazione(), versamento.getCodVersamentoEnte());
				
				log.info("Versamento (" + versamento.getCodVersamentoEnte() + ") dell'applicazione (" + versamento.getApplicazione(this).getCodApplicazione() + ") aggiornato");
			} catch (NotFoundException e) {
				// Versamento nuovo. Inserisco
				versamentiBD.insertVersamento(versamento);
				ctx.log("versamento.inserimentoOk", versamento.getApplicazione(this).getCodApplicazione(), versamento.getCodVersamentoEnte());
				
				log.info("Versamento (" + versamento.getCodVersamentoEnte() + ") dell'applicazione (" + versamento.getApplicazione(this).getCodApplicazione() + ") inserito");
			}
			if(doCommit) commit();
			return iuv;
		} catch (Exception e) {
			if(doCommit) rollback();
			if(e instanceof GovPayException)
				throw (GovPayException) e;
			else 
				throw new GovPayException(e);
		}
	}

	@Deprecated
	public void annullaVersamento(Applicazione applicazione, String codApplicazione, String codVersamentoEnte) throws GovPayException {
		try {
			VersamentiBD versamentiBD = new VersamentiBD(this);
			
			setAutoCommit(false);
			enableSelectForUpdate();
			
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
				commit();
			}
		} catch (Exception e) {
			rollback();
			if(e instanceof GovPayException)
				throw (GovPayException) e;
			else 
				throw new GovPayException(e);
		} finally {
			try {
				disableSelectForUpdate();
			} catch (Exception e) {}
		}
	}
	
	public void notificaPagamento(Applicazione applicazione, String codApplicazione, String codVersamentoEnte) throws GovPayException {
		try {
			VersamentiBD versamentiBD = new VersamentiBD(this);
			
			setAutoCommit(false);
			enableSelectForUpdate();
			
			try {
				it.govpay.bd.model.Versamento versamentoLetto = versamentiBD.getVersamento(applicazione.getId(), codVersamentoEnte);
			
				// Se è già ESEGUITO_SENZA_RPT non devo far nulla.
				if(versamentoLetto.getStatoVersamento().equals(StatoVersamento.ESEGUITO_SENZA_RPT)) {
					log.info("Versamento (" + versamentoLetto.getCodVersamentoEnte() + ") dell'applicazione (" + applicazione.getCodApplicazione() + ") gia' pagato senza rpt. Aggiornamento non necessario.");
					return;
				}
				
				// Se è in stato NON_ESEGUITO lo annullo
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
				commit();
			}
		} catch (Exception e) {
			rollback();
			if(e instanceof GovPayException)
				throw (GovPayException) e;
			else 
				throw new GovPayException(e);
		}
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
				throw new GovPayException(EsitoOperazione.VER_008, codApplicazione, codVersamentoEnte);
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
				codApplicazione = GovpayConfig.getInstance().getDefaultCustomIuvGenerator().getCodApplicazione(dominio, iuv, dominio.getApplicazioneDefault(this));
				
				if(codApplicazione == null) {
					throw new GovPayException("L'avviso di pagamento [Dominio:" + codDominio + " Iuv:" + iuv + "] non risulta registrato, ne associabile ad un'applicazione censita.", EsitoOperazione.VER_008);
				}
			}

			// A questo punto ho sicuramente il codApplicazione. Se ho anche il codVersamentoEnte lo cerco localmente
			if(codVersamentoEnte != null) {
				try {
					versamentoModel = versamentiBD.getVersamento(AnagraficaManager.getApplicazione(this, codApplicazione).getId(), iuvModel.getCodVersamentoEnte());
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
				versamentoModel = VersamentoUtils.acquisisciVersamento(AnagraficaManager.getApplicazione(this, codApplicazione), codVersamentoEnte, bundlekey, codUnivocoDebitore, codDominio, iuv, this);
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
	
	
	public it.govpay.bd.model.Versamento chiediVersamento(Portale portale, String codApplicazione, String codVersamentoEnte, String bundlekey, String codUnivocoDebitore, String codDominio, String iuv) throws ServiceException, GovPayException {
		if(codDominio != null && !AclEngine.isAuthorized(portale, Servizio.PAGAMENTI_ATTESA, codDominio, null)) {
			throw new GovPayException(EsitoOperazione.PRT_005);
		}
		
		it.govpay.bd.model.Versamento v = chiediVersamento(codApplicazione, codVersamentoEnte, bundlekey, codUnivocoDebitore, codDominio, iuv);
		
		if(AclEngine.isAuthorized(portale, Servizio.PAGAMENTI_ATTESA, v.getUo(this).getDominio(this).getCodDominio(), null)) {
			return v;
		} else {
			throw new GovPayException(EsitoOperazione.PRT_005);
		}	
	}
	
	public List<it.govpay.bd.model.Versamento> chiediVersamenti(Portale portaleAutenticato, String codPortale, String codUnivocoDebitore, List<StatoVersamento> statiVersamento, VersamentoFilter.SortFields filterSortList) throws GovPayException, ServiceException {
		VersamentiBD versamentiBD = new VersamentiBD(this);
		VersamentoFilter filter = versamentiBD.newFilter();
		filter.setCodUnivocoDebitore(codUnivocoDebitore);
		filter.setStatiPagamento(statiVersamento);
		filter.addSortField(filterSortList);
		
		List<Long> domini = new ArrayList<Long>();
		Set<Long> dominiSet = AclEngine.getIdDominiAutorizzati(portaleAutenticato, Servizio.PAGAMENTI_ONLINE);
		if(dominiSet != null) {
			domini.addAll(dominiSet);
			filter.setIdDomini(domini);
		}
		
		List<it.govpay.bd.model.Versamento> versamenti = versamentiBD.findAll(filter);
		for(it.govpay.bd.model.Versamento versamento : versamenti)
			try {
				VersamentoUtils.aggiornaVersamento(versamento, this);
			} catch (Exception e) {
				// Aggiornamento andato male. risultera' scaduto.
			} 
		return versamenti;
	}
}
