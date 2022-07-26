package it.govpay.gde.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.govpay.gde.entity.EventoIndexEntity;
import it.govpay.gde.repository.utils.CustomSearchRepository;

public interface EventoIndexRepository extends JpaRepository<EventoIndexEntity, Long>, CustomSearchRepository<EventoIndexEntity, Long, EventoIndexRepositoryImpl.SearchParam> {

}
