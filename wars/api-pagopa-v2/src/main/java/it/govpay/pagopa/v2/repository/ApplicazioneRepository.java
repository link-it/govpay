package it.govpay.pagopa.v2.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

import it.govpay.pagopa.v2.entity.ApplicazioneEntity;

public interface ApplicazioneRepository extends JpaRepositoryImplementation<ApplicazioneEntity, Long> {

	
	@Query("SELECT codApplicazione FROM ApplicazioneEntity applicazioni")
	List<String> findListaCodApplicazioni();
	
	Optional<ApplicazioneEntity> findOneByCodApplicazione(String codApplicazione);
}