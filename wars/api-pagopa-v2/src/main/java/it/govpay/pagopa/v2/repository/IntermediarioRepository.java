package it.govpay.pagopa.v2.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

import it.govpay.pagopa.v2.entity.IntermediarioEntity;

public interface IntermediarioRepository extends JpaRepositoryImplementation<IntermediarioEntity, Long> {

	public Optional<IntermediarioEntity> findOneByCodIntermediario(String codIntermediario);
}