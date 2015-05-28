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

import it.govpay.ejb.builder.CreditoreBuilder;
import it.govpay.ejb.builder.GatewayPagamentoBuilder;
import it.govpay.ejb.builder.OperatoreBuilder;
import it.govpay.ejb.builder.ScadenzarioBuilder;
import it.govpay.ejb.builder.TributoBuilder;
import it.govpay.ejb.exception.GovPayException;
import it.govpay.ejb.exception.GovPayException.GovPayExceptionEnum;
import it.govpay.ejb.filter.EnteCreditoreFilter;
import it.govpay.ejb.model.DistintaModel;
import it.govpay.ejb.model.EnteCreditoreModel;
import it.govpay.ejb.model.GatewayPagamentoModel;
import it.govpay.ejb.model.GatewayPagamentoModel.EnumFornitoreGateway;
import it.govpay.ejb.model.GatewayPagamentoModel.EnumModalitaPagamento;
import it.govpay.ejb.model.OperatoreModel;
import it.govpay.ejb.model.ScadenzarioModel;
import it.govpay.ejb.model.TributoModel;
import it.govpay.ejb.utils.CryptUtils;
import it.govpay.ejb.utils.GeneratoreIdUnivoci;
import it.govpay.ejb.utils.GovPayConstants;
import it.govpay.ejb.utils.QueryUtils;
import it.govpay.orm.configurazione.CfgGatewayPagamento;
import it.govpay.orm.profilazione.CategoriaEnte;
import it.govpay.orm.profilazione.Ente;
import it.govpay.orm.profilazione.EnteId;
import it.govpay.orm.profilazione.IndirizzoPostale;
import it.govpay.orm.profilazione.Intestatario;
import it.govpay.orm.profilazione.IntestatarioOperatore;
import it.govpay.orm.profilazione.IntestatarioOperatoreId;
import it.govpay.orm.profilazione.Operatore;
import it.govpay.orm.profilazione.SistemaEnte;
import it.govpay.orm.profilazione.SistemaEnteId;
import it.govpay.orm.profilazione.TributoEnte;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.logging.log4j.Logger;

@Stateless
public class AnagraficaEJB {

	@Inject  
	private transient Logger log;

	@PersistenceContext(unitName = "GovPayJta")
	private EntityManager entityManager;


	/************************************************************************************/
	/************************************************************************************/
	/**************************   ENTE CREDITORE MODEL   ********************************/
	/************************************************************************************/
	/************************************************************************************/

	/**
	 * Ricerca l'ente in base al codice fiscale (cdEnte) beneficiario fornito in input.
	 * @param idFiscale
	 * @return
	 */
	public EnteCreditoreModel getCreditoreByIdLogico(String idFiscale) {
		TypedQuery<Ente> query = entityManager.createNamedQuery("getEnteFromLapl", Ente.class);
		query.setParameter("lapl", idFiscale);
		Ente ente = null;
		try {
			ente = query.getSingleResult();
			return CreditoreBuilder.fromEnte(ente);
		} catch (NoResultException e) {
			return null;
		}
	}

	/**
	 * Ricerca l'ente in base al l'id fisico (idEnte) beneficiario fornito in input.
	 * @param idFiscale
	 * @return
	 */
	public EnteCreditoreModel getCreditoreByIdFisico(String idEnteCreditore) {
		try {
			Ente ente = entityManager.find(Ente.class, idEnteCreditore);
			return CreditoreBuilder.fromEnte(ente);
		} catch (NoResultException e) {
			return null;
		}
	}

	/***
	 * Restituisce la lista degli enti associati all'operatore
	 * @param offset
	 * @param limit
	 * @param operatore
	 * @return
	 */
	public List<EnteCreditoreModel> findAllEntiCreditori(EnteCreditoreFilter filtro) {


		StringBuilder qlStringBuilder = new StringBuilder("select e from Ente e");
		Map<String, Object> parmetersMap = new HashMap<String, Object>();
		if(filtro != null && filtro.getOperatore() != null && filtro.getOperatore().getUsername() != null) {
			// join con operatore
			qlStringBuilder.append(" join e.intestatario.intestatariOperatori iop where iop.operatore.username = :username");
			parmetersMap.put("username", filtro.getOperatore().getUsername());
		} else {
			// senza join
			qlStringBuilder.append(" where 1 = 1");
		}

		appendConstraintEnti(qlStringBuilder, parmetersMap, filtro);
		TypedQuery<Ente> query = entityManager.createQuery(qlStringBuilder.toString(), Ente.class);
		QueryUtils.setParameters(query, parmetersMap);
		QueryUtils.setPagingParameters(query, filtro);
		List<Ente> enti = query.getResultList();
		List<EnteCreditoreModel> lst = new ArrayList<EnteCreditoreModel>();
		for (Ente ente : enti) {
			lst.add(CreditoreBuilder.fromEnte(ente));
		}
		return lst;
	}

	/***
	 * Restitisce il numero di enti associati all'operatore
	 * @param operatore
	 * @return
	 */
	public int countEntiCreditori(EnteCreditoreFilter filtro){
		StringBuilder qlStringBuilder = new StringBuilder("select count(*) from Ente e");
		Map<String, Object> parmetersMap = new HashMap<String, Object>();
		if(filtro != null && filtro.getOperatore() != null && filtro.getOperatore().getUsername() != null) {
			// join con operatore
			qlStringBuilder.append(" join e.intestatario.intestatariOperatori iop where iop.operatore.username = :username");
			parmetersMap.put("username", filtro.getOperatore().getUsername());
		} else {
			// senza join
			qlStringBuilder.append(" where 1 = 1");
		}

		appendConstraintEnti(qlStringBuilder, parmetersMap, filtro);
		Query query = entityManager.createQuery(qlStringBuilder.toString());
		QueryUtils.setParameters(query, parmetersMap);
		Long count = (Long)query.getSingleResult();
		return count.intValue();
	}

	private void appendConstraintEnti(StringBuilder qlStringBuilder, Map<String, Object> parmetersMap, EnteCreditoreFilter filtro) {
		if (filtro != null) {
			// if (filtro.getOperatore() != null) {
			// qlStringBuilder.append(" and e.codiceEnte = :codiceEnte");
			// parmetersMap.put("codiceEnte", filtro.getOperatore());
			// }
		}
	}

	/***
	 * Effettua il salvataggio dell'ente creditore.
	 * @param ente
	 * @param idCreditore idFisico ente.
	 */
	public String salvaEnteCreditore(String idCreditore, EnteCreditoreModel enteCreditoreModel, String idOperatore) throws GovPayException {

		if(idCreditore == null) {
			//
			// insert
			//

			// verifico la presenza di enti con lo stesso idFiscale
			Query query = entityManager.createQuery("select count(*) from Ente e where e.intestatario.lapl = :lapl");
			query.setParameter("lapl", enteCreditoreModel.getIdFiscale());
			Long count = (Long) query.getSingleResult();
			if(count > 0) {
				throw new GovPayException(GovPayExceptionEnum.ERRORE_ENTE_WEB, "ESISTE GIA' UN ENTE CON IDFISCALE: " + enteCreditoreModel.getIdFiscale());
			}


			IndirizzoPostale indirizzo = new IndirizzoPostale();
			indirizzo.setAddress(enteCreditoreModel.getIndirizzo());
			indirizzo.setProvince(enteCreditoreModel.getProvincia());
			indirizzo.setCity(enteCreditoreModel.getLocalita());
			indirizzo.setNumeroCivico(enteCreditoreModel.getCivico());
			indirizzo.setCapCode(enteCreditoreModel.getCap());
			entityManager.persist(indirizzo);

			Intestatario intestatario = new Intestatario();
			// TODO:MINO va bene questo generatore degli id?
			intestatario.setCorporate(GeneratoreIdUnivoci.getInstance().generaId()); 
			intestatario.setIndirizzipostali(indirizzo);
			intestatario.setLapl(enteCreditoreModel.getIdFiscale());
			entityManager.persist(intestatario);

			// TODO:MINO chiedere se in govpay esiste una sola categoria
			CategoriaEnte tipoEnte = entityManager.find(CategoriaEnte.class, "TipoEnte000");

			Ente ente = new Ente();
			// TODO:MINO la PK viene passata o viene generata? - se viene passata posso avere una chiave duplicata. come faccio?
			// ente.setIdEnte(enteCreditoreModel.getIdEnteCreditore()); // PK
			ente.setIdEnte(GeneratoreIdUnivoci.getInstance().generaId()); // PK GENERATA

			ente.setIntestatario(intestatario);
			ente.setTipoEnte(tipoEnte);
			ente.setCodiceEnte(enteCreditoreModel.getIdentificativoUnivoco());
			ente.setDenominazione(enteCreditoreModel.getDenominazione());
			ente.setProvincia(enteCreditoreModel.getProvincia());
			ente.setStato(enteCreditoreModel.getStato().name());

			// TODO:MINO uno solo?
			ente.setMaxNumTributi(1);

			entityManager.persist(ente);

			Operatore operatore = entityManager.find(Operatore.class, idOperatore);
			IntestatarioOperatore intestatarioOperatore = new IntestatarioOperatore(new IntestatarioOperatoreId(intestatario.getCorporate(), operatore.getOperatore()));
			intestatarioOperatore.setLocked(0);
			intestatarioOperatore.setTipoOperatore("AC");
			entityManager.persist(intestatarioOperatore);


			entityManager.flush();
			return ente.getIdEnte();

		} else {
			//
			// update
			//
			Ente ente = entityManager.find(Ente.class, idCreditore);

			ente.setCodiceEnte(enteCreditoreModel.getIdentificativoUnivoco());
			ente.setDenominazione(enteCreditoreModel.getDenominazione());
			ente.setProvincia(enteCreditoreModel.getProvincia());
			ente.setStato(enteCreditoreModel.getStato().name());

			Intestatario intestatario = ente.getIntestatario();
			intestatario.setLapl(enteCreditoreModel.getIdFiscale());

			IndirizzoPostale indirizzo = intestatario.getIndirizzipostali();
			indirizzo.setAddress(enteCreditoreModel.getIndirizzo());
			indirizzo.setProvince(enteCreditoreModel.getProvincia());
			indirizzo.setCity(enteCreditoreModel.getLocalita());
			indirizzo.setCapCode(enteCreditoreModel.getCap());
			indirizzo.setNumeroCivico(enteCreditoreModel.getCivico());

			return ente.getIdEnte();
		}

	}

	public List<EnteCreditoreModel> getEntiCreditori() {
		TypedQuery<Ente> query = entityManager.createQuery("from Ente", Ente.class);
		List<Ente> enti = query.getResultList();
		List<EnteCreditoreModel> creditori = new ArrayList<EnteCreditoreModel>();
		if (enti != null) {
			for (Ente ente : enti) {
				creditori.add(CreditoreBuilder.fromEnte(ente));
			}
		}
		return creditori;
	}

	public void cancellaEnteCreditore(String idEnteCreditore) throws GovPayException {
		Ente e = entityManager.find(Ente.class, idEnteCreditore);
		Set<TributoEnte> tributi = e.getTributiEnte();
		if(tributi != null && !tributi.isEmpty())
			throw new GovPayException(GovPayExceptionEnum.ERRORE_ENTE_WEB, "non posso eliminare l'ente con id: " + idEnteCreditore + " perchè ha dei tributi associati");

		entityManager.remove(e);
	}


	/************************************************************************************/
	/************************************************************************************/
	/**************************  GATEWAY PAGAMENTO MODEL ********************************/
	/************************************************************************************/
	/************************************************************************************/

	/**
	 * Recupera la lista dei Psp disponibili
	 * Filtrato per tipologia di versamento supportato 
	 * @return
	 */
	public List<GatewayPagamentoModel> getListaGatewayPagamento(EnumModalitaPagamento... tipiVersamento) {
		Set<Long> listaIdModalita = new HashSet<Long>();
		for (EnumModalitaPagamento tipoVersamento : tipiVersamento) {
			Long idCfgModalitaPagamento = GatewayPagamentoBuilder.modalitaPagamentoToIdModalita(tipoVersamento);
			if (idCfgModalitaPagamento != null) {
				listaIdModalita.add(idCfgModalitaPagamento);
			}
		}
		if (listaIdModalita.isEmpty()) {
			return null;
		}
		Timestamp adesso = new Timestamp(new Date().getTime());
		TypedQuery<CfgGatewayPagamento> query = entityManager.createNamedQuery("getGfgGatewayByListaModatlita", CfgGatewayPagamento.class);
		query.setParameter("listaIdModalita", listaIdModalita);
		query.setParameter("dtInizioValidita", adesso);
		query.setParameter("dtFineValidita", adesso);
		query.setParameter("stRiga", GovPayConstants.ST_RIGA_VISIBILE);
		query.setParameter("stato", GatewayPagamentoModel.EnumStato.ATTIVO.name());

		List<CfgGatewayPagamento> cfgGatewayPagamento;
		try {
			cfgGatewayPagamento = query.getResultList();
		} catch (NoResultException e) {
			return null;
		}

		List<GatewayPagamentoModel> listaGatewayPagamento = new ArrayList<GatewayPagamentoModel>();
		for (CfgGatewayPagamento cfgGtw : cfgGatewayPagamento) {
			listaGatewayPagamento.add(GatewayPagamentoBuilder.fromCfgGatewayPagamento(cfgGtw));
		}
		return listaGatewayPagamento;
	}

	public GatewayPagamentoModel getValidGateway(long idGatewayPagamento) {
		// TODO: [SR] Forse è il caso indirizzare i gateway per chiave logica (Bundle Key)
		Timestamp adesso = new Timestamp(new Date().getTime());
		TypedQuery<CfgGatewayPagamento> query = entityManager.createNamedQuery("getGfgGatewayById", CfgGatewayPagamento.class);
		query.setParameter("id", idGatewayPagamento);
		query.setParameter("dtInizioValidita", adesso);
		query.setParameter("dtFineValidita", adesso);
		query.setParameter("stRiga", GovPayConstants.ST_RIGA_VISIBILE);
		query.setParameter("stato", GatewayPagamentoModel.EnumStato.ATTIVO.name());
		try {
			CfgGatewayPagamento cfgGtw = query.getSingleResult();
			return GatewayPagamentoBuilder.fromCfgGatewayPagamento(cfgGtw);
		} catch (NoResultException e) {
			return null;
		}
	}

	/**
	 * Aggiorna i dati dei Gateway di pagamento che fanno capo ad un "Fornitore Gateway"
	 * 
	 * I dati nelle tabelle sono aggiornati sulla base della Lista passata al metodo, mantenendo invariati gli ID fisici sulle tabelle
	 * 
	 * @param lista
	 */
	public void aggiornaGatewayPagamento(List<GatewayPagamentoModel> listaGatewayPagamento, EnumFornitoreGateway fornitore) {
		if (listaGatewayPagamento == null) {
			throw new IllegalArgumentException("Oggetto listaGatewayPagamento = null! Aggiornamento PSP Abortito");
		}
		if (fornitore == null) {
			throw new IllegalArgumentException("Oggetto fornitore = null! Aggiornamento PSP Abortito");
		}
		log.info("Gateway target da aggiornare: " + listaGatewayPagamento.size());
		// Recupero dei Gateway Pagamento attualmente presenti in tabella. 
		// Nota: non mi immagino di avere troppi record su questa tabella (ordine al massimo di qualche centinaio). 
		// L'elaborazione è pertanto gestibile in memoria.
		// Eventuali ottimizzazioni potranno essere pensate qualora il numero dei Gateway di pagamento gestiti aumentassero
		// molto.
		TypedQuery<CfgGatewayPagamento> q = entityManager.createQuery("from CfgGatewayPagamento where cfgFornitoreGateway.id = :idFornitoreGtw", CfgGatewayPagamento.class);
		q.setParameter("idFornitoreGtw", GatewayPagamentoBuilder.fornitoreGatewayToIdFornitoreGateway(fornitore));
		List<CfgGatewayPagamento> gatewayAttuali = (List<CfgGatewayPagamento>) q.getResultList();

		log.info("Gateway attualmente gestiti dal fornitore " + fornitore + ": " + gatewayAttuali.size());

		Map<String, CfgGatewayPagamento> mappaGateway = new HashMap<String, CfgGatewayPagamento>();
		// Popolo la mappa dei gateway aggiornata. (BundleKey, CfgGatewayPagamento)
		// Inizialmente la popolo con tutti i gateway attuali, disattivandoli (in prima battuta)
		for (CfgGatewayPagamento c : gatewayAttuali) {
			c.setStato(GatewayPagamentoModel.EnumStato.DISATTIVO.name());
			c.setStRiga(GovPayConstants.ST_RIGA_NON_VISIBILE);
			mappaGateway.put(c.getBundleKey(), c);
		}
		// Scorro la lista dei Gateway da mergiare
		for (GatewayPagamentoModel g : listaGatewayPagamento) {
			log.info("Aggiornamento psp " + g.getBundleKey());
			CfgGatewayPagamento e = mappaGateway.get(g.getBundleKey());
			if (e == null) {
				// Gateway non gestito, lo creo nuovo.
				e = new CfgGatewayPagamento();
				e.setOpInserimento("AggiornaGatewayPagamentoBath");
				GatewayPagamentoBuilder.toCfgGatewayPagamento(g, e, entityManager);
				e.completeForInsert();
				e.setStRiga(GovPayConstants.ST_RIGA_VISIBILE);
				log.info("Psp non presente in banca dati. INSERT " + e.getBundleKey() + "::" + e.getApplicationId() + ":" + e.getSystemId());
				entityManager.persist(e);
				mappaGateway.put(e.getBundleKey(), e);
			} else {
				log.info("Psp gia' presente in banca dati. UPDATE " + e.getBundleKey() + "::" + e.getApplicationId() + ":" + e.getSystemId());
				GatewayPagamentoBuilder.toCfgGatewayPagamento(g, e, entityManager);
				e.completeForUpdate();
				e.setStRiga(GovPayConstants.ST_RIGA_VISIBILE);
			}
		}
		log.trace(mappaGateway);
		// Nota: modificando i dati degli oggetti in  stato managed, in uscita da questo metodo JPA esegue in automatico tutti gli update.
		// Risultato netto di questo metodo è:
		// 1. Aggiunta di tutti i nuovi Gateway
		// 2. Modifica di tutti i gateway esisttenti (mantenendo la chiave fisica di legame con le altre entità
		// 3. Disattivazione di tutti i gateway non più presenti nel directory di riferimento
	}



	private static final String ALL_GATEWAY_QUERY_FROM = " from CfgGatewayPagamento cgp where cgp.dataInizioValidita <= :dtInizioValidita and cgp.dataFineValidita >= :dtFineValidita and cgp.stRiga = :stRiga order by cgp.bundleKey";

	public List<GatewayPagamentoModel> findAllGatewayPagamento() throws GovPayException {

		Timestamp adesso = new Timestamp(new Date().getTime());
		StringBuilder qlStringBuilder = new StringBuilder("select cgp").append(ALL_GATEWAY_QUERY_FROM);
		TypedQuery<CfgGatewayPagamento> query = entityManager.createQuery(qlStringBuilder.toString(), CfgGatewayPagamento.class);
		query.setParameter("dtInizioValidita", adesso);
		query.setParameter("dtFineValidita", adesso);
		query.setParameter("stRiga", GovPayConstants.ST_RIGA_VISIBILE);

		List<GatewayPagamentoModel> listaGatewayModel = new ArrayList<GatewayPagamentoModel>();
		List<CfgGatewayPagamento> listaGateway = query.getResultList();
		for (CfgGatewayPagamento cfgGatewayPagamento : listaGateway) {
			listaGatewayModel.add(GatewayPagamentoBuilder.fromCfgGatewayPagamento(cfgGatewayPagamento));
		}
		return listaGatewayModel;
	}

	public int countGatewayPagamento() throws GovPayException {

		Timestamp adesso = new Timestamp(new Date().getTime());
		StringBuilder qlStringBuilder = new StringBuilder("select count(*)").append(ALL_GATEWAY_QUERY_FROM);
		Query query = entityManager.createQuery(qlStringBuilder.toString());
		query.setParameter("dtInizioValidita", adesso);
		query.setParameter("dtFineValidita", adesso);
		query.setParameter("stRiga", GovPayConstants.ST_RIGA_VISIBILE);

		Long count = (Long) query.getSingleResult();
		return count.intValue();
	}

	public void aggiornaStatoGateway(GatewayPagamentoModel gateway) throws GovPayException {
		CfgGatewayPagamento cfgGatewayPagamento = entityManager.find(CfgGatewayPagamento.class, gateway.getIdGateway());
		cfgGatewayPagamento.setStato(gateway.getStato().name());
	}	


	/************************************************************************************/
	/************************************************************************************/
	/**************************      OPERATORE MODEL     ********************************/
	/************************************************************************************/
	/************************************************************************************/


	/***
	 * 
	 * Metodo che carica il profilo dell'operatore della console.
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	public OperatoreModel getOperatore(String username, String password) {

		String encryptedPwd = CryptUtils.encryptPwd(password);
		TypedQuery<Operatore> query = entityManager.createQuery("select o from Operatore o where o.username = :username and o.password = :password", Operatore.class);

		query.setParameter("username", username);
		query.setParameter("password", encryptedPwd);

		Operatore operatore;
		try {
			operatore = query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
		return OperatoreBuilder.fromOperatore(operatore);

	}

	/************************************************************************************/
	/************************************************************************************/
	/********************************  SCADENZARIO MODEL ********************************/
	/************************************************************************************/
	/************************************************************************************/


	public ScadenzarioModel getScadenzario(TributoModel tributo) {
		SistemaEnte sistemaEnte = entityManager.find(SistemaEnte.class, new SistemaEnteId(tributo.getIdEnteCreditore(), tributo.getIdSistema()));
		return ScadenzarioBuilder.fromSistemaEnte(sistemaEnte, entityManager);
	}

	public ScadenzarioModel getScadenzario(DistintaModel distinta) {

		// TODO:MINO verificare implementazione.
		// N.B. considero che i pagamenti della distinta siano relativi a pendenze dello stesso tributo.

		String idCondizione = distinta.getPagamenti().get(0).getIdCondizionePagamento();

		TypedQuery<SistemaEnte> query = entityManager.createQuery("select c.pendenza.tributoEnte.sistemaEnte from Condizione c where c.idCondizione = :idCondizione", SistemaEnte.class);
		query.setParameter("idCondizione", idCondizione);

		SistemaEnte sistemaEnte;
		try {
			sistemaEnte = query.getSingleResult();
			return ScadenzarioBuilder.fromSistemaEnte(sistemaEnte, entityManager);
		} catch (NoResultException e) {
			return null;
		}

	}

	public List<ScadenzarioModel> getScadenzari(String idEnteCreditore) {

		TypedQuery<SistemaEnte> query = entityManager.createQuery("select s from SistemaEnte s where s.sisEntId.idEnte = :idEnte", SistemaEnte.class);
		query.setParameter("idEnte", idEnteCreditore);

		List<SistemaEnte> listaSil = query.getResultList();

		List<ScadenzarioModel> scadenzari = new ArrayList<ScadenzarioModel>();
		for (SistemaEnte sistemaEnte : listaSil) {
			scadenzari.add(ScadenzarioBuilder.fromSistemaEnte(sistemaEnte, entityManager));
		}
		return scadenzari;
	}


	/************************************************************************************/
	/************************************************************************************/
	/**************************       TRIBUTO MODEL      ********************************/
	/************************************************************************************/
	/************************************************************************************/

	public TributoModel getTributoById(String idEnteCreditore, String codiceTributo) {
		TributoEnte tributoEnte = entityManager.find(TributoEnte.class, new EnteId(idEnteCreditore, codiceTributo));
		return TributoBuilder.fromTributoEnte(tributoEnte);
	}

	public List<TributoModel> getTributi(String idEnteCreditore) {

		TypedQuery<TributoEnte> query = entityManager.createQuery("select t from TributoEnte t where t.tribEnId.idEntePk = :idEnte", TributoEnte.class);
		query.setParameter("idEnte", idEnteCreditore);
		List<TributoEnte> listaTributi = query.getResultList();

		List<TributoModel> tributi = new ArrayList<TributoModel>();
		for (TributoEnte tributoEnte : listaTributi) {
			tributi.add(TributoBuilder.fromTributoEnte(tributoEnte));
		}		
		return tributi;
	}
	
	public List<TributoModel> getTributi(String idEnteCreditore, String idSistema) {
		TypedQuery<TributoEnte> query = entityManager.createQuery("select t from TributoEnte t where t.tribEnId.idEntePk = :idEnte and t.sistemaEnte.sisEntId.idSystem = :idSistema", TributoEnte.class);
		query.setParameter("idEnte", idEnteCreditore);
		query.setParameter("idSistema", idSistema);
		List<TributoEnte> listaTributi = query.getResultList();

		List<TributoModel> tributi = new ArrayList<TributoModel>();
		for (TributoEnte tributoEnte : listaTributi) {
			tributi.add(TributoBuilder.fromTributoEnte(tributoEnte));
		}		
		return tributi;
	}

	public void salvaTributo(String idEnteCreditore, String codiceTributo, TributoModel tributoModel) throws GovPayException {
		if(idEnteCreditore == null || codiceTributo == null) {
			// inserimento
			TributoEnte tributo = new TributoEnte(new EnteId(tributoModel.getIdEnteCreditore(), tributoModel.getCodiceTributo()));

			tributo.setCdTrbEnte(tributoModel.getCodiceTributo());
			tributo.setDeTrb(tributoModel.getDescrizione());
			tributo.setStato(tributoModel.getStato().name());

			tributo.setIdTributo(GovPayConstants.CATEGORIA_TRIBUTO_DEFAULT);
			tributo.setIBAN(tributoModel.getIbanAccredito() != null ? tributoModel.getIbanAccredito() : "IBAN");
			tributo.setSIL(tributoModel.getIdSistema());
			tributo.setFlIniziativa("Y");
			tributo.setFlNotificaPagamento("N");
			tributo.setFlRicevutaAnonimo("Y");
			tributo.setFlPredeterm("N");
			tributo.setFlNascostoFe("N");

			entityManager.persist(tributo);

		} else {
			// modifica
			TributoEnte tributo = entityManager.find(TributoEnte.class, new EnteId(idEnteCreditore, codiceTributo));

			//			tributo.setCdTrbEnte(tributoModel.getCodiceTributo());
			tributo.setDeTrb(tributoModel.getDescrizione());
			tributo.setIBAN(tributoModel.getIbanAccredito() != null ? tributoModel.getIbanAccredito() : "IBAN");
			tributo.setStato(tributoModel.getStato().name());
			tributo.setSIL(tributoModel.getIdSistema());
		}
	}

	public void cancellaTributo(String idEnteCreditore, String codiceTributo) throws GovPayException {
		TributoEnte tributo = entityManager.find(TributoEnte.class, new EnteId(idEnteCreditore, codiceTributo));

		Query query = entityManager.createQuery("select count(*) from Pendenza p where p.tributoEnte = :tributoEnte");
		query.setParameter("tributoEnte", tributo);
		Long count = (Long) query.getSingleResult();
		if (count > 0)
			throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, "non posso eliminare il tributo con idEnteCreditore: " + idEnteCreditore
					+ " codiceTributo: " + codiceTributo + " perchè ha delle pendenze associate");

		entityManager.remove(tributo);
	}


}
