/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2015 Link.it srl (http://www.link.it).
 * Copyright (c) 2014-2015 TAS S.p.A. (http://www.tasgroup.it).
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
package it.govpay.ejb.controller;

import it.govpay.ejb.exception.GovPayException;
import it.govpay.ejb.model.DistintaModel;
import it.govpay.ejb.model.RevocaDistintaModel;
import it.govpay.ejb.model.RevocaDistintaModel.EnumStatoRevoca;

import java.math.BigDecimal;
import java.util.Date;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class RevocaDistintaEJB {

	@PersistenceContext(unitName = "GovPayJta")
	private EntityManager entityManager;


	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public RevocaDistintaModel creaRevocaDistinta(DistintaModel distinta, String codTransazione, String causale) throws GovPayException {
		RevocaDistintaModel revocaDistintaModel = new RevocaDistintaModel();
		revocaDistintaModel.setCausale(causale);
		revocaDistintaModel.setCodTransazione(codTransazione);
		revocaDistintaModel.setIdDistinta(distinta.getIdDistinta());
		revocaDistintaModel.setStatoRevoca(EnumStatoRevoca.IN_CORSO);
		
		// Memorizza e valorizza l'idFisico
		revocaDistintaModel.setIdRevocaDistinta(123456l);
		return revocaDistintaModel;
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public RevocaDistintaModel getRevocaDistinta(DistintaModel distinta) throws GovPayException {
		//Recupero la RevocaDistintaModel dall'idRevocaDistinta
		RevocaDistintaModel revocaDistintaModel = new RevocaDistintaModel();
		return revocaDistintaModel;
	}

	public RevocaDistintaModel updateStatoRevocaDistinta(Long idRevocaDistinta, EnumStatoRevoca nuovoStato, BigDecimal importoRevocato, String causaleErrore) {
		//TODO: Recupero la RevocaDistintaModel dall'idRevocaDistinta
		RevocaDistintaModel revocaDistintaModel = new RevocaDistintaModel();
		
		//Aggiorno il model
		revocaDistintaModel.setStatoRevoca(nuovoStato);
		
		switch (nuovoStato) {
		case ESEGUITO:
		case NON_ESEGUITO:
		case PARZIALMENTE_ESEGUITO:
			revocaDistintaModel.setDataRisposta(new Date());
			revocaDistintaModel.setImportoRevocato(importoRevocato);
			break;
		case ESEGUITO_SBF:
			revocaDistintaModel.setDataRichiesta(new Date());
			break;
		case IN_ERRORE:
			revocaDistintaModel.setCausaleErrore(causaleErrore);
			break;
		case IN_CORSO:
			// ?? Non dovrebbe mai aggiornarsi cosi...
			break;
		}
		
		// Committo in DB e ritorno
		
		return revocaDistintaModel;
	}

	public void setRevocaDistintaNotificata(Long idDistinta) {
		// TODO: implementare [REVOCA]
		// Imposta una revoca distinta come notificata
	}
	
}
