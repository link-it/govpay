package it.govpay.pagopa.v2.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

import it.govpay.pagopa.v2.entity.StazioneEntity;

public interface StazioneRepository extends JpaRepositoryImplementation<StazioneEntity, Long> {

	public Optional<StazioneEntity> findOneByCodStazione(String codStazione);
}