package it.govpay.pagopa.v2.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

import it.govpay.pagopa.v2.entity.VersamentoEntity;

public interface VersamentoRepository extends JpaRepositoryImplementation<VersamentoEntity, Long> {

	Optional<VersamentoEntity> findOneByCodDominioAndIuv(String codDominio, String iuv);
}