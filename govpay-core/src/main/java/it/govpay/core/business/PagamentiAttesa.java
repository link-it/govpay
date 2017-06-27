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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.model.Versamento;
import it.govpay.bd.pagamento.IuvBD;
import it.govpay.bd.pagamento.VersamentiBD;
import it.govpay.core.business.model.AnnullaVersamentoDTO;
import it.govpay.core.business.model.CaricaVersamentoDTO;
import it.govpay.core.business.model.CaricaVersamentoDTOResponse;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.core.utils.AclEngine;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.core.utils.IuvUtils;
import it.govpay.core.utils.VersamentoUtils;
import it.govpay.model.Acl.Servizio;
import it.govpay.model.Iuv.TipoIUV;
import it.govpay.model.Versamento.StatoVersamento;
import it.govpay.servizi.commons.EsitoOperazione;

public class PagamentiAttesa extends BasicBD {

	private static Logger log = LogManager.getLogger();
	
	public PagamentiAttesa(BasicBD basicBD) {
		super(basicBD);
	}
	
	public CaricaVersamentoDTOResponse caricaVersamento(CaricaVersamentoDTO caricaVersamentoDTO) throws GovPayException, NotAuthorizedException, ServiceException {
		
		// AUTORIZZAZIONE
		if(caricaVersamentoDTO.getApplicazione() != null && !caricaVersamentoDTO.getApplicazione().equals(caricaVersamentoDTO.getVersamento().getApplicazione(this).getCodApplicazione())) {
			throw new NotAuthorizedException();
		}
		
		if(caricaVersamentoDTO.getOperatore() != null && AclEngine.getTopDirittiOperatore(caricaVersamentoDTO.getOperatore().getRuoli(this), Servizio.Gestione_Pagamenti, caricaVersamentoDTO.getVersamento().getUo(this).getDominio(this).getCodDominio()) == 2) {
			throw new NotAuthorizedException();
		}
		
		Versamento versamento = caricaVersamentoDTO.getVersamento();
		boolean generaIuv = caricaVersamentoDTO.isGeneraIuv();
		boolean aggiornaSeEsiste = caricaVersamentoDTO.isAggiornaSeEsiste();
		CaricaVersamentoDTOResponse response = new CaricaVersamentoDTOResponse();
		
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
			
			it.govpay.core.business.model.Iuv iuvGenerato = IuvUtils.toIuv(versamento.getApplicazione(this), versamento.getUo(this).getDominio(this), iuv, versamento.getImportoTotale());
			response.setBarCode(iuvGenerato.getBarCode());
			response.setCodApplicazione(iuvGenerato.getCodApplicazione());
			response.setCodDominio(iuvGenerato.getCodDominio());
			response.setCodVersamentoEnte(iuvGenerato.getCodVersamentoEnte());
			response.setIuv(iuvGenerato.getIuv());
			response.setNumeroAvviso(iuvGenerato.getNumeroAvviso());
			response.setQrCode(iuvGenerato.getQrCode());
			return response;
		} catch (Exception e) {
			if(doCommit) rollback();
			if(e instanceof GovPayException)
				throw (GovPayException) e;
			else 
				throw new GovPayException(e);
		}
	}
	

	public void annullaVersamento(AnnullaVersamentoDTO annullaVersamentoDTO) throws GovPayException, NotAuthorizedException, ServiceException{
		
		if(annullaVersamentoDTO.getApplicazione() != null && !annullaVersamentoDTO.getApplicazione().equals(annullaVersamentoDTO.getCodApplicazione())) {
			throw new NotAuthorizedException();
		}
		
		String codApplicazione = annullaVersamentoDTO.getCodApplicazione();
		String codVersamentoEnte = annullaVersamentoDTO.getCodVersamentoEnte();
		
		try {
			VersamentiBD versamentiBD = new VersamentiBD(this);
			
			setAutoCommit(false);
			enableSelectForUpdate();
			
			try {
				it.govpay.bd.model.Versamento versamentoLetto = versamentiBD.getVersamento(AnagraficaManager.getApplicazione(this, codApplicazione).getId(), codVersamentoEnte);
			
				if(annullaVersamentoDTO.getOperatore() != null && AclEngine.getTopDirittiOperatore(annullaVersamentoDTO.getOperatore().getRuoli(this), Servizio.Gestione_Pagamenti, versamentoLetto.getUo(this).getDominio(this).getCodDominio()) == 2) {
					throw new NotAuthorizedException();
				}
				// Se è già annullato non devo far nulla.
				if(versamentoLetto.getStatoVersamento().equals(StatoVersamento.ANNULLATO)) {
					log.info("Versamento (" + versamentoLetto.getCodVersamentoEnte() + ") dell'applicazione (" + codApplicazione + ") gia' annullato. Aggiornamento non necessario.");
					return;
				}
				
				// Se è in stato NON_ESEGUITO lo annullo
				if(versamentoLetto.getStatoVersamento().equals(StatoVersamento.NON_ESEGUITO)) {
					versamentoLetto.setStatoVersamento(StatoVersamento.ANNULLATO);
					versamentiBD.updateVersamento(versamentoLetto);
					log.info("Versamento (" + versamentoLetto.getCodVersamentoEnte() + ") dell'applicazione (" + codApplicazione + ") annullato.");
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
}
