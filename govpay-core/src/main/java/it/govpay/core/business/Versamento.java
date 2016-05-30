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
import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Portale;
import it.govpay.bd.model.Stazione;
import it.govpay.bd.model.Versamento.StatoVersamento;
import it.govpay.bd.pagamento.IuvBD;
import it.govpay.bd.pagamento.VersamentiBD;
import it.govpay.bd.pagamento.filters.VersamentoFilter;
import it.govpay.core.exceptions.GovPayException;
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
			if(!versamentoModel.getApplicazione(this).isAbilitato())
				throw new GovPayException(EsitoOperazione.APP_001, versamentoModel.getApplicazione(this).getCodApplicazione());
			
			if(!versamentoModel.getApplicazione(this).getCodApplicazione().equals(applicazioneAutenticata.getCodApplicazione()))
				throw new GovPayException(EsitoOperazione.APP_002, applicazioneAutenticata.getCodApplicazione(), versamentoModel.getApplicazione(this).getCodApplicazione());
			
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

			it.govpay.bd.model.Iuv iuv = null;
			if(generaIuv) {
				Stazione stazione = AnagraficaManager.getStazione(this, versamento.getUo(this).getDominio(this).getIdStazione());
				IuvBD iuvBD = new IuvBD(this);
				iuv = iuvBD.generaIuv(versamento.getApplicazione(this), versamento.getUo(this).getDominio(this), versamento.getCodVersamentoEnte(), it.govpay.bd.model.Iuv.AUX_DIGIT, stazione.getApplicationCode(), it.govpay.bd.model.Iuv.TipoIUV.NUMERICO);
				log.info("Generato IUV [CodDominio: " + versamento.getUo(this).getDominio(this).getCodDominio() + "][CodIuv: " + iuv.getIuv() + "]");
			}
			
			try {
				it.govpay.bd.model.Versamento versamentoLetto = versamentiBD.getVersamento(versamento.getIdApplicazione(), versamento.getCodVersamentoEnte());
				// Versamento presente. Verifico e aggiorno
				if(!aggiornaSeEsiste)
					throw new GovPayException(EsitoOperazione.VER_015, versamento.getApplicazione(this).getCodApplicazione(), versamento.getCodVersamentoEnte());
				VersamentoUtils.validazioneSemanticaAggiornamento(versamentoLetto, versamento, this);
				versamentiBD.updateVersamento(versamento, true);
				log.info("Versamento (" + versamento.getCodVersamentoEnte() + ") dell'applicazione (" + versamento.getApplicazione(this).getCodApplicazione() + ") aggiornato");
			} catch (NotFoundException e) {
				// Versamento nuovo. Inserisco
				versamentiBD.insertVersamento(versamento);
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

	public void annullaVersamento(Applicazione applicazioneAutenticata, String codApplicazione, String codVersamentoEnte) throws GovPayException {
		try {
			Applicazione applicazione = null;
			try {
				applicazione = AnagraficaManager.getApplicazione(this, codApplicazione);
			} catch (NotFoundException e) {
				throw new GovPayException(EsitoOperazione.APP_000, codApplicazione);
			}
			
			if(!applicazione.isAbilitato())
				throw new GovPayException(EsitoOperazione.APP_001, codApplicazione);
			
			if(!applicazione.getCodApplicazione().equals(applicazioneAutenticata.getCodApplicazione()))
				throw new GovPayException(EsitoOperazione.APP_002, applicazioneAutenticata.getCodApplicazione(), codApplicazione);
			
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
	
	public void notificaPagamento(Applicazione applicazioneAutenticata, String codApplicazione, String codVersamentoEnte) throws GovPayException {
		try {
			Applicazione applicazione = null;
			try {
				applicazione = AnagraficaManager.getApplicazione(this, codApplicazione);
			} catch (NotFoundException e) {
				throw new GovPayException(EsitoOperazione.APP_000, codApplicazione);
			}
			
			if(!applicazione.isAbilitato())
				throw new GovPayException(EsitoOperazione.APP_001, codApplicazione);
			
			if(!applicazione.getCodApplicazione().equals(applicazioneAutenticata.getCodApplicazione()))
				throw new GovPayException(EsitoOperazione.APP_002, applicazioneAutenticata.getCodApplicazione(), codApplicazione);
			
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

	public it.govpay.bd.model.Versamento chiediVersamento(Applicazione applicazioneAutenticata, String codApplicazione, String codVersamentoEnte) throws ServiceException, GovPayException {
		Applicazione applicazione = null;
		try {
			applicazione = AnagraficaManager.getApplicazione(this, codApplicazione);
		} catch (NotFoundException e) {
			throw new GovPayException(EsitoOperazione.APP_000, codApplicazione);
		}
		
		if(!applicazione.isAbilitato())
			throw new GovPayException(EsitoOperazione.APP_001, codApplicazione);
		
		if(!applicazione.getCodApplicazione().equals(applicazioneAutenticata.getCodApplicazione()))
			throw new GovPayException(EsitoOperazione.APP_002, applicazioneAutenticata.getCodApplicazione(), codApplicazione);
		
		VersamentiBD versamentiBD = new VersamentiBD(this);
		try {
			return versamentiBD.getVersamento(applicazione.getId(), codVersamentoEnte);
		} catch (NotFoundException e) {
			throw new GovPayException(EsitoOperazione.VER_008, codApplicazione, codVersamentoEnte);
		}
	}

	public List<it.govpay.bd.model.Versamento> chiediVersamenti(Portale portaleAutenticato, String codPortale, String codUnivocoDebitore) throws GovPayException, ServiceException {
		if(!portaleAutenticato.isAbilitato())
			throw new GovPayException(EsitoOperazione.PRT_001, portaleAutenticato.getCodPortale());
		
		if(!portaleAutenticato.getCodPortale().equals(codPortale))
			throw new GovPayException(EsitoOperazione.PRT_002, portaleAutenticato.getCodPortale(), codPortale);
		
		VersamentiBD versamentiBD = new VersamentiBD(this);
		VersamentoFilter filter = versamentiBD.newFilter();
		filter.setCodUnivocoDebitore(codUnivocoDebitore);
		filter.setIdApplicazioni(portaleAutenticato.getIdApplicazioni());
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
