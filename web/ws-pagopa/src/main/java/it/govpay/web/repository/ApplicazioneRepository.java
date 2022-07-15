package it.govpay.web.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

import it.govpay.web.entity.ApplicazioneEntity;

public interface ApplicazioneRepository extends JpaRepositoryImplementation<ApplicazioneEntity, Long> {

	
	@Query("SELECT codApplicazione FROM ApplicazioneEntity applicazioni")
	List<String> findListaCodApplicazioni();
}
