/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2016 Link.it srl (http://www.link.it).
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
package it.govpay.core.business;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.model.Acl.Servizio;
import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Iuv.TipoIUV;
import it.govpay.bd.model.Portale;
import it.govpay.bd.model.Stazione;
import it.govpay.bd.model.Versamento.StatoVersamento;
import it.govpay.bd.pagamento.IuvBD;
import it.govpay.bd.pagamento.VersamentiBD;
import it.govpay.bd.pagamento.filters.VersamentoFilter;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.utils.AclEngine;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.core.utils.VersamentoUtils;
import it.govpay.servizi.commons.EsitoOperazione;

public class Versamento extends BasicBD {

	private static Logger log = LogManager.getLogger();
	
	public Versamento(BasicBD basicBD) {
		super(basicBD);
	}
	
	public it.govpay.bd.model.Iuv caricaVersamento(Applicazione applicazioneAutenticata, it.govpay.bd.model.Versamento versamentoModel, boolean generaIuv, boolean aggiornaSeEsiste) throws GovPayException { 
		try {
			return caricaVersamento(versamentoModel, generaIuv, aggiornaSeEsiste);
		} catch (Exception e) {
			if(e instanceof GovPayException)
				throw (GovPayException) e;
			else 
				throw new GovPayException(e);
		}
	}
	
	public it.govpay.bd.model.Iuv caricaVersamento(it.govpay.bd.model.Versamento versamento, boolean generaIuv, boolean aggiornaSeEsiste) throws GovPayException {
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
			
			it.govpay.bd.model.Iuv iuv = null;
			try {
				iuv = iuvBD.getIuv(versamento.getIdApplicazione(), versamento.getCodVersamentoEnte(), TipoIUV.NUMERICO);
			} catch (NotFoundException e) {
				if(generaIuv) {
					Stazione stazione = AnagraficaManager.getStazione(this, versamento.getUo(this).getDominio(this).getIdStazione());
					iuv = iuvBD.generaIuv(versamento.getApplicazione(this), versamento.getUo(this).getDominio(this), versamento.getCodVersamentoEnte(), it.govpay.bd.model.Iuv.AUX_DIGIT, stazione.getApplicationCode(), it.govpay.bd.model.Iuv.TipoIUV.NUMERICO);
					log.info("Generato IUV [CodDominio: " + versamento.getUo(this).getDominio(this).getCodDominio() + "][CodIuv: " + iuv.getIuv() + "]");
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

	public it.govpay.bd.model.Versamento chiediVersamento(String codApplicazione, String codVersamentoEnte) throws ServiceException, GovPayException {
		VersamentiBD versamentiBD = new VersamentiBD(this);
		try {
			return versamentiBD.getVersamento(AnagraficaManager.getApplicazione(this, codApplicazione).getId(), codVersamentoEnte);
		} catch (NotFoundException e) {
			throw new GovPayException(EsitoOperazione.VER_008);
		}
	}
	
	
	public it.govpay.bd.model.Versamento chiediVersamento(Portale portale, String codApplicazione, String codVersamentoEnte) throws ServiceException, GovPayException {
		it.govpay.bd.model.Versamento v = chiediVersamento(codApplicazione, codVersamentoEnte);
		
		if(AclEngine.isAuthorized(portale, Servizio.PAGAMENTI_ATTESA, v.getUo(this).getDominio(this).getCodDominio(), null)) {
			return v;
		} else {
			throw new GovPayException(EsitoOperazione.PRT_005);
		}	
	}
	
	public it.govpay.bd.model.Versamento chiediVersamentoByIuv(Portale portale, String codDominio, String iuv) throws ServiceException, GovPayException {
		IuvBD iuvBD = new IuvBD(this);
		it.govpay.bd.model.Iuv iuvModel;
		try {
			iuvModel = iuvBD.getIuv(AnagraficaManager.getDominio(this, codDominio).getId(), iuv);
		} catch (NotFoundException e) {
			log.error("IUV inesistente");
			throw new GovPayException(EsitoOperazione.VER_008);
		}
		
		Applicazione applicazione = AnagraficaManager.getApplicazione(this, iuvModel.getIdApplicazione());
		log.debug("Trovato IUV associato al versamento (" + iuvModel.getCodVersamentoEnte() + ") dell'applicazione (" + applicazione.getCodApplicazione() + ")");
		
		it.govpay.bd.model.Versamento v = null;
		try {
			v = chiediVersamento(applicazione.getCodApplicazione(), iuvModel.getCodVersamentoEnte());
		} catch (GovPayException e) {
			try {
				v = VersamentoUtils.acquisisciVersamento(applicazione, iuvModel.getCodVersamentoEnte(), iuv, this);
			} catch (Exception e1) {
				// TODO Gestire meglio i casi di errore.
				throw new GovPayException(EsitoOperazione.VER_008);
			}
		}
		if(AclEngine.isAuthorized(portale, Servizio.PAGAMENTI_ATTESA, v.getUo(this).getDominio(this).getCodDominio(), null)) {
			return v;
		} else {
			throw new GovPayException(EsitoOperazione.PRT_005);
		}	
	}
	
	public it.govpay.bd.model.Versamento chiediVersamento(Portale portale, String bundleKey) throws ServiceException, GovPayException {
		// TODO
		throw new GovPayException(EsitoOperazione.PRT_005);
	}
	
	public List<it.govpay.bd.model.Versamento> chiediVersamenti(Portale portaleAutenticato, String codPortale, String codUnivocoDebitore) throws GovPayException, ServiceException {
		
		if(!portaleAutenticato.isAbilitato())
			throw new GovPayException(EsitoOperazione.PRT_001, portaleAutenticato.getCodPortale());
		
		if(!portaleAutenticato.getCodPortale().equals(codPortale))
			throw new GovPayException(EsitoOperazione.PRT_002, portaleAutenticato.getCodPortale(), codPortale);
		
		VersamentiBD versamentiBD = new VersamentiBD(this);
		VersamentoFilter filter = versamentiBD.newFilter();
		filter.setCodUnivocoDebitore(codUnivocoDebitore);
		List<it.govpay.bd.model.Versamento> versamenti = versamentiBD.findAll(filter);
		for(it.govpay.bd.model.Versamento versamento : versamenti)
			try {
				VersamentoUtils.aggiornaVersamento(versamento, this);
			} catch (Exception e) {
				// Aggiornamento andato male. risultera' scaduto.
			} 
		return versamentiBD.findAll(filter);
	}
}
