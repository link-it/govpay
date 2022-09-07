package it.govpay.pagopa.v2.repository;

import java.util.List;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

import it.govpay.pagopa.v2.entity.ConnettoreEntity;

public interface ConnettoreRepository extends JpaRepositoryImplementation<ConnettoreEntity, Long>{

	/*
	 * Ricerca tutte le proprieta' che compongono il connettore identificato col codice
	 */
	public List<ConnettoreEntity> findAllByCodConnettore(String codConnettore);
}
