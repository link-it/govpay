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
package it.govpay.ejb.ndp.ejb;

import it.govpay.ejb.core.builder.ConnettoreBuilder;
import it.govpay.ejb.core.builder.CreditoreBuilder;
import it.govpay.ejb.core.builder.ScadenzarioBuilder;
import it.govpay.ejb.core.ejb.AnagraficaEJB;
import it.govpay.ejb.core.exception.GovPayException;
import it.govpay.ejb.core.exception.GovPayException.GovPayExceptionEnum;
import it.govpay.ejb.core.model.ConnettoreModel;
import it.govpay.ejb.core.model.ConnettorePddModel;
import it.govpay.ejb.core.model.ScadenzarioModel;
import it.govpay.ejb.core.model.ScadenzarioModelId;
import it.govpay.ejb.core.utils.QueryUtils;
import it.govpay.ejb.ndp.ejb.filter.IntermediarioFilter;
import it.govpay.ejb.ndp.ejb.filter.ScadenzarioFilter;
import it.govpay.ejb.ndp.model.DominioEnteModel;
import it.govpay.ejb.ndp.model.IntermediarioModel;
import it.govpay.ejb.ndp.model.StazioneModel;
import it.govpay.ejb.ndp.pojo.NdpFaultCode;
import it.govpay.ejb.ndp.util.builder.IntermediarioBuilder;
import it.govpay.ejb.ndp.util.builder.StazioneBuilder;
import it.govpay.ejb.ndp.util.exception.GovPayNdpException;
import it.govpay.orm.profilazione.Dominio;
import it.govpay.orm.profilazione.Ente;
import it.govpay.orm.profilazione.Intermediario;
import it.govpay.orm.profilazione.ProprietaConnettore;
import it.govpay.orm.profilazione.SistemaEnte;
import it.govpay.orm.profilazione.SistemaEnteId;
import it.govpay.orm.profilazione.StazioneIntermediario;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

@Stateless
public class AnagraficaDominioEJB {

	@PersistenceContext(unitName = "GovPayJta")
	private EntityManager entityManager;

	@Inject
	AnagraficaEJB anagraficaEjb;

	public DominioEnteModel getDominioEnte(String idDominio, String idIntermediarioPA, String idStazioneIntermediarioPA) throws GovPayException {

		Dominio dominio = entityManager.find(Dominio.class, idDominio);

		if (dominio == null)
			throw new GovPayNdpException(GovPayExceptionEnum.ERRORE_VALIDAZIONE_NDP, NdpFaultCode.PAA_ID_DOMINIO_ERRATO, null);

		StazioneIntermediario stazione = entityManager.find(StazioneIntermediario.class, idStazioneIntermediarioPA);

		if (stazione == null)
			throw new GovPayNdpException(GovPayExceptionEnum.ERRORE_VALIDAZIONE_NDP, NdpFaultCode.PAA_STAZIONE_INT_ERRATA, null);

		Intermediario intermediario = entityManager.find(Intermediario.class, idIntermediarioPA);

		if (intermediario == null)
			throw new GovPayNdpException(GovPayExceptionEnum.ERRORE_VALIDAZIONE_NDP, NdpFaultCode.PAA_ID_INTERMEDIARIO_ERRATO, null);


		DominioEnteModel dominioEnteModel = new DominioEnteModel();
		dominioEnteModel.setIdDominio(dominio.getIdDominio());
		dominioEnteModel.setIntermediario(IntermediarioBuilder.fromIntermediario(intermediario, entityManager));
		dominioEnteModel.setStazione(StazioneBuilder.fromStazioneIntermediario(stazione, entityManager));
		dominioEnteModel.setEnteCreditore(CreditoreBuilder.fromEnte(dominio.getEnte()));

		return dominioEnteModel;

	}

	public DominioEnteModel getDominioEnte(String idEnteCreditore, String idStazione) throws GovPayException {
		StazioneIntermediario stazione = entityManager.find(StazioneIntermediario.class, idStazione);
		Intermediario intermediario = stazione.getIntermediario();

		TypedQuery<Dominio> queryDominio = entityManager.createQuery(
				"select d from Dominio d where d.ente.idEnte = :idEnte", Dominio.class);
		queryDominio.setParameter("idEnte", idEnteCreditore);

		Dominio dominio;
		try {
			dominio = queryDominio.getSingleResult();
		} catch (NoResultException e) {
			throw new GovPayException(GovPayExceptionEnum.ERRORE_CONFIGURAZIONE, "dominio non presente in anagrafica [idEnte: " + idEnteCreditore +"]");
		}

		DominioEnteModel dominioEnteModel = new DominioEnteModel();
		dominioEnteModel.setIdDominio(dominio.getIdDominio());
		dominioEnteModel.setIntermediario(IntermediarioBuilder.fromIntermediario(intermediario, entityManager));
		dominioEnteModel.setStazione(StazioneBuilder.fromStazioneIntermediario(stazione, entityManager));
		dominioEnteModel.setEnteCreditore(CreditoreBuilder.fromEnte(dominio.getEnte()));
		return dominioEnteModel;
	}

	public DominioEnteModel getDominioEnte(String idEnteCreditore) throws GovPayException {
		TypedQuery<Dominio> queryDominio = entityManager.createQuery(
				"select d from Dominio d where d.ente.idEnte = :idEnte", Dominio.class);
		queryDominio.setParameter("idEnte", idEnteCreditore);

		Dominio dominio;
		try {
			dominio = queryDominio.getSingleResult();
		} catch (NoResultException e) {
			//non  necessario quando leggo il dominio relativo all'ente, potrebbe essere stato salvato un ente senza dominio.
			//throw new GovPayException(GovPayExceptionEnum.ERRORE_CONFIGURAZIONE, "dominio non presente in anagrafica [idEnte: " + idEnteCreditore +"]");
			return null;
		}

		DominioEnteModel dominioEnteModel = new DominioEnteModel();
		dominioEnteModel.setIdDominio(dominio.getIdDominio());
		dominioEnteModel.setEnteCreditore(CreditoreBuilder.fromEnte(dominio.getEnte()));
		return dominioEnteModel;
	}



	public void salvaDominioEnte(String idDominioEnte, DominioEnteModel model) {

		Dominio dominio = entityManager.find(Dominio.class, model.getIdDominio());
		if (dominio == null) {
			//
			// insert
			//
			dominio = new Dominio();
			Ente e = entityManager.find(Ente.class, model.getEnteCreditore().getIdEnteCreditore());
			dominio.setEnte(e);
			dominio.setIdDominio(model.getIdDominio());
			entityManager.persist(dominio);
		} 
	}

	public void salvaIntermediarioNdp(String idIntermediarioNdp, IntermediarioModel m) {

		if (idIntermediarioNdp == null) {
			//
			// insert
			//
			Intermediario i = new Intermediario();
			i.setIdIntermediario(m.getIdIntermediarioPA()); // PK
			i.setNomeSoggettoSPC(m.getNomeSoggettoSPC());
			// mi aspetto che il connettore sia già stato salvato su DB in precedenza (o sia null)
			i.setIdConnettorePDD(m.getConnettoreServizioRPT().getIdConnettore());
			entityManager.persist(i);

		} else {
			//
			// update
			//
			Intermediario i = entityManager.find(Intermediario.class, idIntermediarioNdp);
			i.setNomeSoggettoSPC(m.getNomeSoggettoSPC());
			// mi aspetto che il connettore sia già stato salvato su DB in precedenza (o sia null)
			i.setIdConnettorePDD(m.getConnettoreServizioRPT().getIdConnettore());
		}
	}	

	public List<ConnettoreModel> findAllConnettori() {

		TypedQuery<ProprietaConnettore> query = entityManager.createQuery("select p from ProprietaConnettore p", ProprietaConnettore.class);
		List<ProprietaConnettore> listaProprietaConnettori = query.getResultList();
		// raggruppo per id
		Map<Long, List<ProprietaConnettore>> mappaProprieta = ConnettoreBuilder.mappaProprietaConnettori(listaProprietaConnettori);

		List<ConnettoreModel> connettori = new ArrayList<ConnettoreModel>();
		for (List<ProprietaConnettore> listaProprietaConnettore : mappaProprieta.values()) {
			connettori.add(ConnettoreBuilder.fromProprietaConnettore(listaProprietaConnettore));
		}
		return connettori;
	}

	public Long salvaConnettore(Long idConnettore, ConnettoreModel m) {

		if(idConnettore == null) {
			idConnettore = (Long)entityManager.createQuery("select max(p.idConnettore) from ProprietaConnettore p").getSingleResult();
			if(idConnettore==null) idConnettore = 0l;
			idConnettore = idConnettore + 1;
			//
			// inserimento
			//
			inserisciProprietaConnettore(idConnettore, ConnettoreModel.P_SSLKSTYPE_NAME, m.getSslKsType());
			inserisciProprietaConnettore(idConnettore, ConnettoreModel.P_SSLKSLOCATION_NAME, m.getSslKsLocation());
			inserisciProprietaConnettore(idConnettore, ConnettoreModel.P_SSLKSPASSWD_NAME, m.getSslKsPasswd());
			inserisciProprietaConnettore(idConnettore, ConnettoreModel.P_SSLPKEYPASSWD_NAME, m.getSslPKeyPasswd());
			inserisciProprietaConnettore(idConnettore, ConnettoreModel.P_SSLTSTYPE_NAME, m.getSslTsType());
			inserisciProprietaConnettore(idConnettore, ConnettoreModel.P_SSLTSLOCATION_NAME, m.getSslTsLocation());
			inserisciProprietaConnettore(idConnettore, ConnettoreModel.P_SSLTSPASSWD_NAME, m.getSslTsPasswd());
			inserisciProprietaConnettore(idConnettore, ConnettoreModel.P_SSLTYPE_NAME, m.getSslType());
			inserisciProprietaConnettore(idConnettore, ConnettoreModel.P_HTTPUSER_NAME, m.getHttpUser());
			inserisciProprietaConnettore(idConnettore, ConnettoreModel.P_HTTPPASSW_NAME, m.getHttpPassw());
			inserisciProprietaConnettore(idConnettore, ConnettoreModel.P_URL_NAME, m.getUrl());
			inserisciProprietaConnettore(idConnettore, ConnettoreModel.P_TIPOAUTENTICAZIONE_NAME, m.getTipoAutenticazione() != null ? m.getTipoAutenticazione().name() : null);
			inserisciProprietaConnettore(idConnettore, ConnettoreModel.P_TIPOSSL_NAME, m.getTipoSsl() != null ? m.getTipoSsl().name() : null);

			if(m instanceof ConnettorePddModel)
				inserisciProprietaConnettore(idConnettore, ConnettorePddModel.P_AZIONEINURL_NAME, Boolean.toString(((ConnettorePddModel) m).isAzioneInUrl()));

		} else {
			//
			// update
			//
			TypedQuery<ProprietaConnettore> query = entityManager.createNamedQuery("listaProprietaConnettore", ProprietaConnettore.class);
			query.setParameter("idConnettore", idConnettore);
			List<ProprietaConnettore> proprietaConnettore = query.getResultList();
			Map<String, ProprietaConnettore> mappaProprieta = ConnettoreBuilder.mappaProprietaConnettore(proprietaConnettore);

			if(m == null) {
				m = new ConnettoreModel(); // così cancello tutti i record
				idConnettore = null; // e restituisco null come id
			}

			modificaProprietaConnettore(mappaProprieta, idConnettore, ConnettoreModel.P_SSLKSTYPE_NAME, m.getSslKsType());
			modificaProprietaConnettore(mappaProprieta, idConnettore, ConnettoreModel.P_SSLKSLOCATION_NAME, m.getSslKsLocation());
			modificaProprietaConnettore(mappaProprieta, idConnettore, ConnettoreModel.P_SSLKSPASSWD_NAME, m.getSslKsPasswd());
			modificaProprietaConnettore(mappaProprieta, idConnettore, ConnettoreModel.P_SSLPKEYPASSWD_NAME, m.getSslPKeyPasswd());
			modificaProprietaConnettore(mappaProprieta, idConnettore, ConnettoreModel.P_SSLTSTYPE_NAME, m.getSslTsType());
			modificaProprietaConnettore(mappaProprieta, idConnettore, ConnettoreModel.P_SSLTSLOCATION_NAME, m.getSslTsLocation());
			modificaProprietaConnettore(mappaProprieta, idConnettore, ConnettoreModel.P_SSLTSPASSWD_NAME, m.getSslTsPasswd());
			modificaProprietaConnettore(mappaProprieta, idConnettore, ConnettoreModel.P_SSLTYPE_NAME, m.getSslType());
			modificaProprietaConnettore(mappaProprieta, idConnettore, ConnettoreModel.P_HTTPUSER_NAME, m.getHttpUser());
			modificaProprietaConnettore(mappaProprieta, idConnettore, ConnettoreModel.P_HTTPPASSW_NAME, m.getHttpPassw());
			modificaProprietaConnettore(mappaProprieta, idConnettore, ConnettoreModel.P_URL_NAME, m.getUrl());
			modificaProprietaConnettore(mappaProprieta, idConnettore, ConnettoreModel.P_TIPOAUTENTICAZIONE_NAME, m.getTipoAutenticazione() != null ? m.getTipoAutenticazione().name() : null);
			modificaProprietaConnettore(mappaProprieta, idConnettore, ConnettoreModel.P_TIPOSSL_NAME, m.getTipoSsl() != null ? m.getTipoSsl().name() : null);

			if(m instanceof ConnettorePddModel)
				modificaProprietaConnettore(mappaProprieta, idConnettore, ConnettorePddModel.P_AZIONEINURL_NAME, Boolean.toString(((ConnettorePddModel) m).isAzioneInUrl()));
		}

		return idConnettore;
	}

	private void inserisciProprietaConnettore(Long idConnettore, String nome, String nuovoValore) {
		if (nuovoValore != null) {
			entityManager.persist(new ProprietaConnettore(idConnettore, nome, nuovoValore));
		}
	}

	private void modificaProprietaConnettore(Map<String, ProprietaConnettore> mappaProprieta, Long idConnettore, String nome, String nuovoValore) {
		ProprietaConnettore proprietaDaModificare = mappaProprieta.get(nome);
		if (nuovoValore != null) {
			if (proprietaDaModificare != null) {
				proprietaDaModificare.setValoreProprieta(nuovoValore);
			} else {
				entityManager.persist(new ProprietaConnettore(idConnettore, nome, nuovoValore));
			}
		} else {
			if (proprietaDaModificare != null) {
				entityManager.remove(proprietaDaModificare);
			}
		}
	}

	/**
	 * 		INTERMEDIARI
	 */

	public List<IntermediarioModel> findAllIntermediari(IntermediarioFilter filtro) throws GovPayException {
		List<IntermediarioModel> listaIntermediari = new ArrayList<IntermediarioModel>();
		StringBuilder qlStringBuilder = new StringBuilder("select i from Intermediario i where 1 = 1 order by i.nomeSoggettoSPC asc");
		Map<String, Object> parmetersMap = new HashMap<String, Object>();
		TypedQuery<Intermediario> query = entityManager.createQuery(qlStringBuilder.toString(), Intermediario.class);
		QueryUtils.setParameters(query, parmetersMap);
		QueryUtils.setPagingParameters(query, filtro);
		List<Intermediario> intermediari = query.getResultList();
		for (Intermediario intermediario : intermediari) {
			listaIntermediari.add(IntermediarioBuilder.fromIntermediario(intermediario, entityManager));
		}
		return listaIntermediari;
	}

	public int countAllIntermediari(IntermediarioFilter filtro) throws GovPayException {
		StringBuilder qlStringBuilder = new StringBuilder("select count(*) from Intermediario i where 1 = 1");
		Map<String, Object> parmetersMap = new HashMap<String, Object>();
		Query query = entityManager.createQuery(qlStringBuilder.toString());
		QueryUtils.setParameters(query, parmetersMap);
		Long count = (Long) query.getSingleResult();
		return count.intValue();
	}

	public IntermediarioModel getIntermediarioById(String idIntermediarioPA) throws GovPayException {
		Intermediario intermediario = entityManager.find(Intermediario.class, idIntermediarioPA);
		if (intermediario == null)
			throw new GovPayException(GovPayExceptionEnum.ERRORE_CONFIGURAZIONE, "intermediario non presente in anagrafica [idIntermediarioPA: " + idIntermediarioPA + "]");
		IntermediarioModel intermediarioModel = IntermediarioBuilder.fromIntermediario(intermediario, entityManager);
		return intermediarioModel;
	}

	public void salvaIntermediario(String idIntermediarioPA, IntermediarioModel intermediario) throws GovPayException{
		if(idIntermediarioPA != null) {
			Intermediario originale = entityManager.find(Intermediario.class, idIntermediarioPA);
			originale.setNomeSoggettoSPC(intermediario.getNomeSoggettoSPC());
			salvaConnettore(originale.getIdConnettorePDD(), intermediario.getConnettoreServizioRPT());
		} else {
			Long idConnettore = salvaConnettore(null, intermediario.getConnettoreServizioRPT());
			Intermediario nuovo = new Intermediario();
			nuovo.setIdConnettorePDD(idConnettore);
			nuovo.setIdIntermediario(intermediario.getIdIntermediarioPA());
			nuovo.setNomeSoggettoSPC(intermediario.getNomeSoggettoSPC());
			entityManager.persist(nuovo);
		}
	}

	public void eliminaIntermediario(String idIntermediarioPA) throws GovPayException{
		Intermediario i = entityManager.find(Intermediario.class, idIntermediarioPA);
		entityManager.remove(i);
	}

	/**
	 * 		SCADENZARI
	 */

	public List<ScadenzarioModel> findAllScadenzari(ScadenzarioFilter filtro)throws GovPayException{
		List<ScadenzarioModel> listaMappe = new ArrayList<ScadenzarioModel>();

		StringBuilder qlStringBuilder = new StringBuilder("select s from SistemaEnte s where 1 = 1");
		Map<String, Object> parmetersMap = new HashMap<String, Object>();
		appendConstraintScadenzario(qlStringBuilder, parmetersMap, filtro);

		TypedQuery<SistemaEnte> query = entityManager.createQuery(qlStringBuilder.toString(), SistemaEnte.class);
		QueryUtils.setParameters(query, parmetersMap);
		QueryUtils.setPagingParameters(query, filtro);
		List<SistemaEnte> sistemi = query.getResultList();
		for (SistemaEnte sistema : sistemi) {
			listaMappe.add(ScadenzarioBuilder.fromSistemaEnte(sistema, entityManager));

			//			Map<ScadenzarioModel, StazioneModel> mappa = new HashMap<ScadenzarioModel, StazioneModel>();



			//			TypedQuery<StazioneIntermediario> queryStazione = entityManager.createQuery(
			//					"select s from StazioneIntermediario s where s.sistemaEnte.sisEntId.idEnte = :idEnte and s.sistemaEnte.sisEntId.idSystem = :idSystem",
			//					StazioneIntermediario.class);
			//			queryStazione.setParameter("idEnte", sistema.getSisEntId().getIdEnte());
			//			queryStazione.setParameter("idSystem", sistema.getSisEntId().getIdSystem());

			//			StazioneIntermediario stazione = null;

			//			StazioneIntermediario stazione = sistema.getStazioneIntermediario();

			//			try {
			//				stazione = queryStazione.getSingleResult();
			//			} catch (NoResultException e) {
			//				// Scadenzario senza stazione.
			//			}

			//			mappa.put(ScadenzarioBuilder.fromSistemaEnte(sistema, entityManager), StazioneBuilder.fromStazioneIntermediario(stazione, entityManager));
			//			listaMappe.add(mappa);
		}
		return listaMappe;
	}

	//	public List<Map<ScadenzarioModel, StazioneModel>> findAllScadenzari(ScadenzarioFilter filtro)throws GovPayException{
	//		List<Map<ScadenzarioModel, StazioneModel>> listaMappe = new ArrayList<Map<ScadenzarioModel, StazioneModel>>();
	//
	//		StringBuilder qlStringBuilder = new StringBuilder("select s from SistemaEnte s where 1 = 1");
	//		Map<String, Object> parmetersMap = new HashMap<String, Object>();
	//		appendConstraintScadenzario(qlStringBuilder, parmetersMap, filtro);
	//
	//		TypedQuery<SistemaEnte> query = entityManager.createQuery(qlStringBuilder.toString(), SistemaEnte.class);
	//		QueryUtils.setParameters(query, parmetersMap);
	//		QueryUtils.setPagingParameters(query, filtro);
	//		List<SistemaEnte> sistemi = query.getResultList();
	//		for (SistemaEnte sistema : sistemi) {
	//			Map<ScadenzarioModel, StazioneModel> mappa = new HashMap<ScadenzarioModel, StazioneModel>();
	//
	//
	//
	//			//			TypedQuery<StazioneIntermediario> queryStazione = entityManager.createQuery(
	//			//					"select s from StazioneIntermediario s where s.sistemaEnte.sisEntId.idEnte = :idEnte and s.sistemaEnte.sisEntId.idSystem = :idSystem",
	//			//					StazioneIntermediario.class);
	//			//			queryStazione.setParameter("idEnte", sistema.getSisEntId().getIdEnte());
	//			//			queryStazione.setParameter("idSystem", sistema.getSisEntId().getIdSystem());
	//
	//			//			StazioneIntermediario stazione = null;
	//
	//			StazioneIntermediario stazione = sistema.getStazioneIntermediario();
	//
	//			//			try {
	//			//				stazione = queryStazione.getSingleResult();
	//			//			} catch (NoResultException e) {
	//			//				// Scadenzario senza stazione.
	//			//			}
	//
	//			mappa.put(ScadenzarioBuilder.fromSistemaEnte(sistema, entityManager), StazioneBuilder.fromStazioneIntermediario(stazione, entityManager));
	//			listaMappe.add(mappa);
	//		}
	//		return listaMappe;
	//	}

	public int countAllScadenzari(ScadenzarioFilter filtro) throws GovPayException {
		StringBuilder qlStringBuilder = new StringBuilder("select count(*) from SistemaEnte s where 1 = 1");
		Map<String, Object> parmetersMap = new HashMap<String, Object>();
		appendConstraintScadenzario(qlStringBuilder, parmetersMap, filtro);

		Query query = entityManager.createQuery(qlStringBuilder.toString());
		QueryUtils.setParameters(query, parmetersMap);
		Long count = (Long) query.getSingleResult();
		return count.intValue();
	}

	private void appendConstraintScadenzario(StringBuilder qlStringBuilder, Map<String, Object> parmetersMap, ScadenzarioFilter filtro) {
		if (filtro != null) {
			if (filtro.getIdEnteCreditore() != null) {
				qlStringBuilder.append(" and s.sisEntId.idEnte = :idEnte");
				parmetersMap.put("idEnte", filtro.getIdEnteCreditore());
			}
			if (filtro.getIdIntermediarioPA() != null) {
				qlStringBuilder.append(" and s.stazioneIntermediario.intermediario.idIntermediario = :idIntermediario");
				parmetersMap.put("idIntermediario", filtro.getIdIntermediarioPA());
			}
		}
	}

	public  ScadenzarioModel  getScadenzarioById(ScadenzarioModelId scadenzarioModelId) throws GovPayException {

		//		Map<ScadenzarioModel, StazioneModel> mappa = new HashMap<ScadenzarioModel, StazioneModel>();
		SistemaEnte sistema = entityManager.find(SistemaEnte.class, new SistemaEnteId(scadenzarioModelId.getIdEnte(), scadenzarioModelId.getIdSystem()));

		if(sistema != null){
			ScadenzarioModel scadenzario = ScadenzarioBuilder.fromSistemaEnte(sistema, entityManager);
			return scadenzario;
			//			StazioneModel stazione = sistema.getStazioneIntermediario() != null ? StazioneBuilder.fromStazioneIntermediario(sistema.getStazioneIntermediario(), entityManager) : null;

			//			mappa.put(scadenzario, stazione);

			//			return mappa;
		}

		return null;
	}


	public void salvaScadenzario(ScadenzarioModelId scadenzarioModelId, ScadenzarioModel scadenzarioModel, String identificativoStazione)
			throws GovPayException {

		// Prelevo la stazione
		StazioneIntermediario stazioneIntermediario = entityManager.find(StazioneIntermediario.class, identificativoStazione);
		SistemaEnte sistemaEnte;
		if(scadenzarioModelId == null) {
			// inserimento
			sistemaEnte = new SistemaEnte();
			sistemaEnte.setSisEntId(new SistemaEnteId(scadenzarioModel.getIdEnte(), scadenzarioModel.getIdSystem()));
			sistemaEnte.setStato("A");
			sistemaEnte.setDeSystem(scadenzarioModel.getDescrizione());
			if(scadenzarioModel.getConnettoreNotifica() != null)
				sistemaEnte.setIdConnettoreNotifica(salvaConnettore(null, scadenzarioModel.getConnettoreNotifica()));
			if(scadenzarioModel.getConnettoreVerifica() != null)
				sistemaEnte.setIdConnettorePagAttesa(salvaConnettore(null, scadenzarioModel.getConnettoreVerifica()));

			sistemaEnte.setStazioneIntermediario(stazioneIntermediario);

			entityManager.persist(sistemaEnte);
			if(stazioneIntermediario != null) {
				stazioneIntermediario.getSistemiEnte().add(sistemaEnte);
				entityManager.persist(stazioneIntermediario);
			}


		} else {
			// modifica
			sistemaEnte = entityManager.find(SistemaEnte.class, new SistemaEnteId(scadenzarioModelId.getIdEnte(), scadenzarioModelId.getIdSystem()));
			sistemaEnte.getSisEntId().setIdEnte(scadenzarioModel.getIdEnte());
			sistemaEnte.getSisEntId().setIdSystem(scadenzarioModel.getIdSystem());

			sistemaEnte.setDeSystem(scadenzarioModel.getDescrizione());


			if(sistemaEnte.getIdConnettoreNotifica() == null) {
				// nessun connettore presente
				if(scadenzarioModel.getConnettoreNotifica() != null)
					// inserimento
					sistemaEnte.setIdConnettoreNotifica(salvaConnettore(null, scadenzarioModel.getConnettoreNotifica()));
			} else {
				// connettore presente
				// modifica/cancellazione
				sistemaEnte.setIdConnettoreNotifica(salvaConnettore(sistemaEnte.getIdConnettoreNotifica(), scadenzarioModel.getConnettoreNotifica()));
			}

			if(sistemaEnte.getIdConnettorePagAttesa() == null) {
				// nessun connettore presente
				if(scadenzarioModel.getConnettoreVerifica() != null)
					// inserimento
					sistemaEnte.setIdConnettorePagAttesa(salvaConnettore(null, scadenzarioModel.getConnettoreVerifica()));
			} else {
				// connettore presente
				// modifica/cancellazione
				sistemaEnte.setIdConnettorePagAttesa(salvaConnettore(sistemaEnte.getIdConnettorePagAttesa(), scadenzarioModel.getConnettoreVerifica()));
			}

			StazioneIntermediario oldStazioneIntermediario = sistemaEnte.getStazioneIntermediario();

			if(oldStazioneIntermediario !=null){
				// L'utente non ha cambiato la stazione
				if(oldStazioneIntermediario.getIdStazione().equals(identificativoStazione)){
					sistemaEnte.setStazioneIntermediario(stazioneIntermediario);
				} else{
					// Set della stazione
					sistemaEnte.setStazioneIntermediario(stazioneIntermediario);

					//rimuovo il sistema ente dalla vecchia stazione 
					StazioneIntermediario oldStazione = entityManager.find(StazioneIntermediario.class, oldStazioneIntermediario.getIdStazione());
					SistemaEnte found= null;
					for(SistemaEnte s : oldStazione.getSistemiEnte())
						if(s.getSisEntId().getIdEnte().equals(sistemaEnte.getSisEntId().getIdEnte()) && 
								s.getSisEntId().getIdSystem().equals(sistemaEnte.getSisEntId().getIdSystem()))
							found = s;

					if(found!= null)
						oldStazione.getSistemiEnte().remove(found);

					//aggiungo il sistema ente alla nuova stazione
					stazioneIntermediario.getSistemiEnte().add(sistemaEnte);

				}
			}
			else {
				// Set della stazione
				sistemaEnte.setStazioneIntermediario(stazioneIntermediario);
				entityManager.persist(sistemaEnte);
				if(stazioneIntermediario != null) {
					stazioneIntermediario.getSistemiEnte().add(sistemaEnte);
					entityManager.persist(stazioneIntermediario);
				}
			}
			
			
			//			if(stazioneModel == null) {
			//				if(sistemaEnte.getStazioneIntermediario() != null) {
			//					// cancellazione stazione
			//					entityManager.remove(sistemaEnte.getStazioneIntermediario());
			//				}
			//
			//			} else {
			//				if(sistemaEnte.getStazioneIntermediario() == null) {
			//					// inserimento stazione
			//					StazioneIntermediario stazioneIntermediario = new StazioneIntermediario();
			//					stazioneIntermediario.setIdIntermediario(stazioneModel.getIdIntermediarioPA());
			//					stazioneIntermediario.setIdStazione(stazioneModel.getIdStazioneIntermediarioPA());
			//					stazioneIntermediario.setPassword(stazioneModel.getPassword());
			//					sistemaEnte.setStazioneIntermediario(stazioneIntermediario);
			//					entityManager.persist(stazioneIntermediario);
			//
			//				} else {
			//					// modifica stazione
			//					StazioneIntermediario stazioneIntermediario = sistemaEnte.getStazioneIntermediario();
			//					stazioneIntermediario.setIdIntermediario(stazioneModel.getIdIntermediarioPA());
			//					stazioneIntermediario.setIdStazione(stazioneModel.getIdStazioneIntermediarioPA());
			//					stazioneIntermediario.setPassword(stazioneModel.getPassword());
			//					sistemaEnte.setStazioneIntermediario(stazioneIntermediario);
			//				}
			//			}
		}
	}


	public void eliminaScadenzario(String idEnte, String idSystem) throws GovPayException {
		SistemaEnte sistemaEnte = entityManager.find(SistemaEnte.class, new SistemaEnteId(idEnte, idSystem));
		entityManager.remove(sistemaEnte);
	}


	public List<StazioneModel> getStazioniIntermediario(String idIntermediario) {

		Intermediario intermediario = entityManager.find(Intermediario.class, idIntermediario);
		if(intermediario == null || intermediario.getStazioni() == null) {
			return null;
		} else {
			List<StazioneModel> stazioni = new ArrayList<StazioneModel>();
			for(StazioneIntermediario stazioneIntermediario : intermediario.getStazioni()) {
				stazioni.add(StazioneBuilder.fromStazioneIntermediario(stazioneIntermediario, entityManager));
			}
			return stazioni;
		}
	}

	/***
	 * Salvataggio di una stazione, non vengono toccati gli scadenzari.
	 * 
	 * @param stazioneModel
	 * @param isAdd
	 */
	public void salvaStazione(StazioneModel stazioneModel, boolean isAdd){
		StazioneIntermediario stazioneToSave = new StazioneIntermediario();
		stazioneToSave.setIdIntermediario(stazioneModel.getIdIntermediarioPA());
		stazioneToSave.setIdStazione(stazioneModel.getIdStazioneIntermediarioPA());
		stazioneToSave.setPassword(stazioneModel.getPassword());


		if(isAdd){
			entityManager.persist(stazioneToSave);
		}
		else {
			StazioneIntermediario stazione = entityManager.find(StazioneIntermediario.class, stazioneModel.getIdStazioneIntermediarioPA());
			stazione.setPassword(stazioneModel.getPassword());
		}
	}

	public StazioneModel findStazioneById(String identificativoStazione){
		StazioneIntermediario stazione = entityManager.find(StazioneIntermediario.class, identificativoStazione);

		if(stazione!= null)
			return StazioneBuilder.fromStazioneIntermediario(stazione, entityManager);

		return null;
	}
}

