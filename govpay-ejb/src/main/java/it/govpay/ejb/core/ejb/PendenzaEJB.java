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
package it.govpay.ejb.core.ejb;

import it.govpay.ejb.core.builder.PendenzaBuilder;
import it.govpay.ejb.core.model.DestinatarioPendenzaModel;
import it.govpay.ejb.core.model.PendenzaModel;
import it.govpay.ejb.core.utils.OrmUtils;
import it.govpay.orm.pagamenti.DistintaPagamento;
import it.govpay.orm.pagamenti.Pagamento;
import it.govpay.orm.posizionedebitoria.Condizione;
import it.govpay.orm.posizionedebitoria.DatiAnagraficiDestinatario;
import it.govpay.orm.posizionedebitoria.DestinatarioPendenza;
import it.govpay.orm.posizionedebitoria.Pendenza;
import it.govpay.orm.profilazione.CategoriaTributo;
import it.govpay.orm.profilazione.Ente;
import it.govpay.orm.profilazione.EnteId;
import it.govpay.orm.profilazione.TributoEnte;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;


@Stateless
public class PendenzaEJB {
	
	@PersistenceContext(unitName = "GovPayJta")
	private EntityManager entityManager;
	
	/**
	 * Crea le pendenze e i relativi debiti
	 * 
	 * Il booleano hidden indica se la creazione deve essere in modalita' hidden o meno.
	 * 
	 * Una volta create imposta nell'oggetto Pendenza 
	 * l'idPendenza e gli idCondizione assegnati.
	 * 
	 * @param pendenza
	 */
	public void inserisciPendenze(List<PendenzaModel> pendenzeModel, boolean hidden) {

		for (PendenzaModel pendenzaModel : pendenzeModel) {
			//
			// recupero anagrafica ente e tributo
			//
			// ente creditore
			Ente ente = entityManager.find(Ente.class, pendenzaModel.getIdEnteCreditore());
			// tributo Ente
			TributoEnte tributoEnte = entityManager.find(TributoEnte.class, new EnteId(pendenzaModel.getIdEnteCreditore(), pendenzaModel.getCodiceTributo()));
			// categoria tributo
			CategoriaTributo categoriaTributo = entityManager.find(CategoriaTributo.class, pendenzaModel.getIdTributo());
			// CREAZIONE PENDENZA
			Pendenza pendenza = OrmUtils.buildPendenza(pendenzaModel, ente, tributoEnte, categoriaTributo, hidden);
			entityManager.persist(pendenza);
			
			// Anagrafica debitore
			if (pendenza.getDestinatari() != null && !pendenza.getDestinatari().isEmpty()) {
				DestinatarioPendenza destinatario = pendenza.getDestinatari().iterator().next();

				DestinatarioPendenzaModel debitoreModel = pendenzaModel.getDebitore();

				DatiAnagraficiDestinatario datiAnagraficiDestinatario = new DatiAnagraficiDestinatario();
				datiAnagraficiDestinatario.setAnagrafica(debitoreModel.getAnagrafica());
				datiAnagraficiDestinatario.setCap(debitoreModel.getCap());
				datiAnagraficiDestinatario.setEmail(debitoreModel.geteMail());
				datiAnagraficiDestinatario.setIndirizzo(debitoreModel.getIndirizzo());
				datiAnagraficiDestinatario.setLocalita(debitoreModel.getLocalita());
				datiAnagraficiDestinatario.setNazione(debitoreModel.getNazione());
				datiAnagraficiDestinatario.setNumeroCivico(debitoreModel.getCivico());
				datiAnagraficiDestinatario.setProvincia(debitoreModel.getProvincia());
				// TODO:MINO aggiungere il tipoSoggetto nel model?
				datiAnagraficiDestinatario.setTipoSoggetto(debitoreModel.getIdFiscale().length() == 16 ? 'F' : 'G'); 
				
				datiAnagraficiDestinatario.setDestinatario(destinatario);
				
				entityManager.persist(datiAnagraficiDestinatario);
			}
			
			// setto l'id pendenza creata nel pojo
			pendenzaModel.setIdPendenza(pendenza.getIdPendenza()); 
		} // fine ciclo su pendenzePojo
	}

	/**
	 * Per ciascuna condizione ritornata, costruisce una Pendenza con isuoi dati e quella della pendenza genitore.
	 * 
	 * @param idCondizione
	 * @return
	 */
	// TODO: [RS] verificare se è meglio usare la condizione al posto della pendenza
	public List<PendenzaModel> getPendenze(List<String> idCondizioni) {
		
		List<PendenzaModel> listaPendenze = new ArrayList<PendenzaModel>();
		for (String idCondizione : idCondizioni) {
			
			Condizione cond = entityManager.find(Condizione.class, idCondizione);
			
			Pendenza pend = cond.getPendenza();
			PendenzaModel pendenzaModel = PendenzaBuilder.fromPendenza(pend);

//			PendenzaModel pendenzaModel = new PendenzaModel();
//			
//			//
//			// per ora solo i dati minimi che vengono usati
//			//
//			pendenzaModel.setIdPendenza(pend.getIdPendenza());
//			pendenzaModel.setIdDebitoEnte(pend.getIdPendenzaente());
			
			listaPendenze.add(pendenzaModel);
		}
		return listaPendenze;

	}
	
	/**
	 * Ritorna la lista delle Pendenze associate alla distinta passata
	 * 
	 * @param idCondizione
	 * @return
	 */
	// TODO: [RS] verificare se è meglio usare la condizione al posto della pendenza
	public List<PendenzaModel> getPendenze(Long idDistinta) {

		List<PendenzaModel> listaPendenze = new ArrayList<PendenzaModel>();
		try {
			DistintaPagamento distintaPagamento = entityManager.find(DistintaPagamento.class, idDistinta);
			
			if (distintaPagamento != null && distintaPagamento.getPagamenti() != null) {
			
				for (Pagamento pagamento : distintaPagamento.getPagamenti()) {
					Pendenza pend = pagamento.getCondPagamento().getPendenza();
					PendenzaModel pendenzaModel = PendenzaBuilder.fromPendenza(pend);
				
//				PendenzaModel pendenzaModel = new PendenzaModel();
//				//
//				// per ora solo i dati minimi che vengono usati
//				//
//				pendenzaModel.setIdPendenza(pend.getIdPendenza());
//				pendenzaModel.setImportoTotale(pend.getImTotale());
//				pendenzaModel.setCondizioniPagamento(new ArrayList<CondizionePagamentoModel>());
//				
//				
//				Set<DestinatarioPendenza> destinatari = pend.getDestinatari();
//				if(destinatari != null && !destinatari.isEmpty()) {
//					DestinatarioPendenza destinatario = destinatari.iterator().next();
//					
//					DestinatarioPendenzaModel debitore = new DestinatarioPendenzaModel();
//					debitore.setAnagrafica(destinatario.getDeDestinatario());
//					debitore.setIdFiscale(destinatario.getCoDestinatario());
//					
//					pendenzaModel.setDebitore(debitore);
//				}
//				
//				for(Condizione cond : pend.getCondizioni()) {
//					CondizionePagamentoModel condizionePagamentoModel = new CondizionePagamentoModel();
//					//
//					// per ora solo i dati minimi che vengono usati
//					//
//					condizionePagamentoModel.setIdCondizione(cond.getIdCondizione());
//					condizionePagamentoModel.setIbanBeneficiario(cond.getIbanBeneficiario());
//					
//					pendenzaModel.getCondizioniPagamento().add(condizionePagamentoModel);
//				}
					if(!listaPendenze.contains(pendenzaModel))
						listaPendenze.add(pendenzaModel);
					
				}
			}
		} catch (NoResultException e) {
			// nada
		}
		return listaPendenze;

	}

}
