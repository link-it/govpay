/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2015 Link.it srl (http://www.link.it).
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
package it.govpay.business;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Iuv;
import it.govpay.bd.model.Portale;
import it.govpay.bd.pagamento.VersamentiBD;
import it.govpay.exception.GovPayException;
import it.govpay.exception.GovPayException.GovPayExceptionEnum;

import java.security.Principal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;

public class Autorizzazione {
	
	private BasicBD bd;
	Logger log = LogManager.getLogger(Autorizzazione.class);
	
	public Autorizzazione(BasicBD bd) {
		this.bd = bd;
	}

	public Applicazione authApplicazione(Principal ctxPrincipal, String codApplicazione) throws GovPayException {
		
		Applicazione applicazione = authApplicazione(ctxPrincipal);
		
		if(!applicazione.getCodApplicazione().equals(codApplicazione)) {
			log.error("L'applicazione richiedente [" + applicazione.getCodApplicazione() + "] differisce da quella specificata nella richiesta [" + codApplicazione + "]");
			throw new GovPayException(GovPayExceptionEnum.ERRORE_AUTORIZZAZIONE);
		}
		
		return applicazione;
	}
	
	public Portale authPortale(Principal ctxPrincipal, String codPortale) throws GovPayException {
		Portale portale = authPortale(ctxPrincipal);
		
		if(!portale.getCodPortale().equals(codPortale)) {
			log.error("Il portale richiedente [" + portale.getCodPortale() + "] differisce da quello specificata nella richiesta [" + codPortale + "]");
			throw new GovPayException(GovPayExceptionEnum.ERRORE_AUTORIZZAZIONE);
		}
		
		return portale;
	}
	
	
	private Applicazione authApplicazione(Principal ctxPrincipal) throws GovPayException {
		
		Applicazione applicazione = null;
		
		if(ctxPrincipal == null) {
			log.error("Autenticazione fallita nessun Principal fornito.");
			throw new GovPayException(GovPayExceptionEnum.ERRORE_AUTENTICAZIONE);
		}
		
		try {
			applicazione = AnagraficaManager.getApplicazioneByPrincipal(bd, ctxPrincipal.getName());
		} catch (NotFoundException e){
			log.error("Nessuna applicazione e' identificata dal Principal [" + ctxPrincipal.getName() + "] non censito in Anagrafica Applicazioni.");
			throw new GovPayException(GovPayExceptionEnum.APPLICAZIONE_NON_TROVATA);
		} catch (Exception e) {
			log.error("Errore interno");
			throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, e);
		} 
		
		if(!applicazione.isAbilitato()) {
			log.error("Applicazione richiedente disabilitata.");
			throw new GovPayException(GovPayExceptionEnum.APPLICAZIONE_DISABILITATA);
		}
		
		return applicazione;
	}
	
	private Portale authPortale(Principal ctxPrincipal) throws GovPayException {
		
		Portale portale = null;
		
		if(ctxPrincipal == null) {
			log.error("Autenticazione fallita nessun Principal fornito.");
			throw new GovPayException(GovPayExceptionEnum.ERRORE_AUTENTICAZIONE);
		}
		
		try {
			portale = AnagraficaManager.getPortaleByPrincipal(bd, ctxPrincipal.getName());
		} catch (NotFoundException e){
			log.error("Nessuna portale e' identificato dal Principal [" + ctxPrincipal.getName() + "] non censito in Anagrafica Portali.");
			throw new GovPayException(GovPayExceptionEnum.APPLICAZIONE_NON_TROVATA);
		} catch (Exception e) {
			log.error("Errore interno");
			throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, e);
		} 
		
		if(!portale.isAbilitato()) {
			log.error("Portale richiedente disabilitato.");
			throw new GovPayException(GovPayExceptionEnum.APPLICAZIONE_DISABILITATA);
		}
		
		return portale;
	}
	
	public void authApplicazione(long idApplicazione, String codDominio, String iuv) throws GovPayException {
		VersamentiBD versamentiBD = new VersamentiBD(bd);
		try {
			Iuv iuvM = versamentiBD.getIuv(codDominio, iuv);
			if(iuvM.getIdApplicazione() != idApplicazione) {
				log.error("Lo iuv fornito non e' associato all'applicazione richiedente");
				throw new GovPayException(GovPayExceptionEnum.ERRORE_AUTORIZZAZIONE);
			}
		} catch (NotFoundException e) {
			log.error("Lo iuv fornito non esiste");
			throw new GovPayException(GovPayExceptionEnum.IUV_NON_TROVATO);
		} catch (ServiceException e) {
			log.error("Errore interno");
			throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, e);
		} catch (MultipleResultException e) {
			log.error("Errore interno");
			throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, e);
		} 
	}

	public void authApplicazione(long idApplicazione, long idTributo) throws GovPayException {
		try {
			Applicazione applicazione = AnagraficaManager.getApplicazione(bd, idApplicazione);
			if(applicazione.getIdTributi().contains(idTributo)) return;
			else {
				log.error("L'applicazione non puo' gestire il tributo del versamento: [codApplicazione: " + applicazione.getCodApplicazione() + "] [idTributo + " + idTributo + "]" );
				throw new GovPayException(GovPayExceptionEnum.ERRORE_AUTORIZZAZIONE);
			}
		} catch (NotFoundException e) {
			log.error("L'applicazione non esite,");
			throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, e);
		} catch (ServiceException e) {
			log.error("Errore interno");
			throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, e);
		} catch (MultipleResultException e) {
			log.error("Errore interno");
			throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, e);
		} 
		
	}


}
