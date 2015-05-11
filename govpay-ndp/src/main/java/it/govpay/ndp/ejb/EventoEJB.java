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
package it.govpay.ndp.ejb;

import it.govpay.ejb.utils.QueryUtils;
import it.govpay.ndp.ejb.filter.EventoFilter;
import it.govpay.ndp.model.EventiInterfacciaModel;
import it.govpay.ndp.model.EventiInterfacciaModel.Evento;
import it.govpay.ndp.model.EventiInterfacciaModel.Infospcoop;
import it.govpay.ndp.util.builder.GdeEventoBuilder;
import it.govpay.ndp.util.builder.GdeInfoSpCoopBuilder;
import it.govpay.orm.gde.GdeEvento;
import it.govpay.orm.gde.GdeInfoSpCoop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

@Stateless
public class EventoEJB {

	@PersistenceContext(unitName = "GovPayJta")
	private EntityManager entityManager;

	/**
	 * Crea gli eventi nel GiornaleDegliEventi
	 * 
	 * Se il campo Infospcoop e' valorizzato > inserisce una entry in gde_infospcoop Tutti gli eventi nella lista, vengono inseriti puntando alla Infospcoop
	 * eventualmente creata in precedenza
	 */
	public void inserisciEvento(EventiInterfacciaModel eventiInterfaccia) {

		String idEgov = null;
		Infospcoop infospcoop = eventiInterfaccia.getInfospcoop();
		if (infospcoop != null) {
			GdeInfoSpCoop gdeInfoSpCoop = GdeInfoSpCoopBuilder.toGdeInfoSpCoop(infospcoop);
			entityManager.persist(gdeInfoSpCoop);
			idEgov = gdeInfoSpCoop.getIdEgov();
		}

		for (Evento evento : eventiInterfaccia.getEventi()) {
			evento.setIdEgov(idEgov);
			entityManager.persist(GdeEventoBuilder.toGdeEvento(evento));
		}
	}


	public EventiInterfacciaModel.Evento findById(Long id) {
		GdeEvento gdeEvento = entityManager.find(GdeEvento.class, id);
		return GdeEventoBuilder.fromGdeEvento(gdeEvento);
	}

 
	public EventiInterfacciaModel.Infospcoop findInfospcoopById(String idegov) {
		GdeInfoSpCoop gdeInfoSpCoop = entityManager.find(GdeInfoSpCoop.class, idegov);
		return GdeInfoSpCoopBuilder.fromGdeInfoSpCoop(gdeInfoSpCoop);
	}

	public List<Evento> findAll(EventoFilter filtro) {
		List<Evento> listaEventi = new ArrayList<EventiInterfacciaModel.Evento>();

		StringBuilder qlStringBuilder = new StringBuilder("select e from GdeEvento e where 1 = 1");
		Map<String, Object> parmetersMap = new HashMap<String, Object>();
		appendConstraint(qlStringBuilder, parmetersMap, filtro);

		TypedQuery<GdeEvento> query = entityManager.createQuery(qlStringBuilder.toString(), GdeEvento.class);
		QueryUtils.setParameters(query, parmetersMap);
		QueryUtils.setPagingParameters(query, filtro);

 
		List<GdeEvento> gdeEventi = query.getResultList();

		EventiInterfacciaModel eventiInterfaccia = new EventiInterfacciaModel();
		eventiInterfaccia.setEventi(new ArrayList<EventiInterfacciaModel.Evento>());
		for (GdeEvento evento : gdeEventi) {
			listaEventi.add(GdeEventoBuilder.fromGdeEvento(evento));
		}

		return listaEventi;
	}

	public int count(EventoFilter filtro) {
		StringBuilder qlStringBuilder = new StringBuilder("select count(e.id) from GdeEvento e where 1 = 1");
		Map<String, Object> parmetersMap = new HashMap<String, Object>();
		appendConstraint(qlStringBuilder, parmetersMap, filtro);

		Query query = entityManager.createQuery(qlStringBuilder.toString());
		QueryUtils.setParameters(query, parmetersMap);

		Long count = (Long) query.getSingleResult();
		return count.intValue();
	}

	private void appendConstraint(StringBuilder qlStringBuilder, Map<String, Object> parmetersMap, EventoFilter filtro) {
		if (filtro != null) {
			if (filtro.getIuv() != null) {
				qlStringBuilder.append(" and e.idUnivocoVersamento = :iuv");
				parmetersMap.put("iuv", filtro.getIuv());
			}
		}
		qlStringBuilder.append(" order by e.dtEvento desc");
	}

}
