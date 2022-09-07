package it.govpay.pagopa.v2.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

import it.govpay.pagopa.v2.entity.DominioEntity;

public interface DominioRepository extends JpaRepositoryImplementation<DominioEntity, Long> {

	public Optional<DominioEntity> findOneByCodDominio(String codDominio);
}