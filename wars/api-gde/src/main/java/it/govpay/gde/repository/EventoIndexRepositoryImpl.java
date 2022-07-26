package it.govpay.gde.repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import it.govpay.gde.entity.EventoIndexEntity;
import it.govpay.gde.entity.EventoIndexEntity_;
import it.govpay.gde.repository.utils.CustomSearchRepository;

public class EventoIndexRepositoryImpl implements CustomSearchRepository<EventoIndexEntity, Long, EventoIndexRepositoryImpl.SearchParam> {

	@PersistenceContext
	private EntityManager entityManager;

	public enum SearchParam { 
			idDominio, iuv, ccp, idA2A, idPendenza, idPagamento, 
			esitoEvento, dataDa, dataA, categoriaEvento, tipoEvento, 
			sottotipoEvento, componenteEvento, ruoloEvento, severitaDa, severitaA 
			}


	@Override
	public Page<EventoIndexEntity> findAll(Pageable pageable, Map<SearchParam, Object> searchParams) {

		// Costruzione della query che recupera i dati da restituire
		CriteriaBuilder qb = entityManager.getCriteriaBuilder();
		CriteriaQuery<EventoIndexEntity> cq = qb.createQuery(EventoIndexEntity.class);
		Root<EventoIndexEntity> evento = cq.from(EventoIndexEntity.class);
		// Costruzione dei predicati di filtro sulla base dei parametri ricevuti
		List<Predicate> predicates = new ArrayList<Predicate>();

		for(SearchParam sp : searchParams.keySet())
			switch (sp) {
			case categoriaEvento:
				predicates.add(
						qb.equal(evento.get(EventoIndexEntity_.categoriaEvento), (EventoIndexEntity.CategoriaEvento) searchParams.get(SearchParam.categoriaEvento)));
				break;
			case esitoEvento:
				predicates.add(
						qb.equal(evento.get(EventoIndexEntity_.esitoEvento), (EventoIndexEntity.EsitoEvento) searchParams.get(SearchParam.esitoEvento)));
				break;
			case ruoloEvento:
				predicates.add(
						qb.equal(evento.get(EventoIndexEntity_.ruoloEvento), (EventoIndexEntity.RuoloEvento) searchParams.get(SearchParam.ruoloEvento)));
				break;
			case idDominio:
				predicates.add(
						qb.equal(evento.get(EventoIndexEntity_.codDominio), (String) searchParams.get(SearchParam.idDominio)));
				break;
			case iuv:
				predicates.add(
						qb.equal(evento.get(EventoIndexEntity_.iuv), (String) searchParams.get(SearchParam.iuv)));
				break;
			case ccp:
				predicates.add(
						qb.like(qb.upper(evento.get(EventoIndexEntity_.ccp)), "%" + ((String) searchParams.get(SearchParam.ccp)).toUpperCase() + "%"));
				break;
			case idA2A:
				predicates.add(
						qb.equal(evento.get(EventoIndexEntity_.codApplicazione), (String) searchParams.get(SearchParam.idA2A)));
				break; 
			case idPendenza:
				predicates.add(
						qb.equal(evento.get(EventoIndexEntity_.codVersamentoEnte), (String) searchParams.get(SearchParam.idPendenza)));
				break;				
			case idPagamento:
				predicates.add(
						qb.equal(evento.get(EventoIndexEntity_.idSessione), (String) searchParams.get(SearchParam.idPagamento)));
				break;
			case dataDa:
				predicates.add(
						qb.greaterThanOrEqualTo(evento.get(EventoIndexEntity_.data), (LocalDateTime) searchParams.get(SearchParam.dataDa)));
				break;
			case dataA:
				predicates.add(
						qb.lessThanOrEqualTo(evento.get(EventoIndexEntity_.data), (LocalDateTime) searchParams.get(SearchParam.dataA)));
				break;
			case severitaDa:
				predicates.add(
						qb.greaterThanOrEqualTo(evento.get(EventoIndexEntity_.severita), (Integer) searchParams.get(SearchParam.severitaDa)));
				break;
			case severitaA:
				predicates.add(
						qb.lessThanOrEqualTo(evento.get(EventoIndexEntity_.severita), (Integer) searchParams.get(SearchParam.severitaA)));
				break;
			case componenteEvento:
				predicates.add(
						qb.equal(evento.get(EventoIndexEntity_.componente), (String) searchParams.get(SearchParam.componenteEvento)));
				break;
			case sottotipoEvento:
				predicates.add(
						qb.equal(evento.get(EventoIndexEntity_.sottotipoEvento), (String) searchParams.get(SearchParam.sottotipoEvento)));
				break;
			case tipoEvento: 
				predicates.add(
						qb.equal(evento.get(EventoIndexEntity_.tipoEvento), (String) searchParams.get(SearchParam.tipoEvento)));
				break;
			}

		// Aggiunta dei predicati come clausola where
		cq.select(evento).where(predicates.toArray(new Predicate[]{}));
		
		// order by id desc
//		cq.orderBy(qb.desc(evento.get(EventoIndexEntity_.id)));

		// Esecuzione della query
		List<EventoIndexEntity> result = entityManager.createQuery(cq).getResultList();

		// Creazione della query count per la paginazione
		CriteriaQuery<Long> countQuery = qb.createQuery(Long.class);
		Root<EventoIndexEntity> paymentsCount = countQuery.from(EventoIndexEntity.class);
		countQuery.select(qb.count(paymentsCount)).where(qb.and(predicates.toArray(new Predicate[predicates.size()])));

		Long count = entityManager.createQuery(countQuery).getSingleResult();

		Page<EventoIndexEntity> result1 = new PageImpl<>(result, pageable, count);
		return result1;
	}




}
