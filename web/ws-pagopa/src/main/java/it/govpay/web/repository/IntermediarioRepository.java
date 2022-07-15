package it.govpay.web.repository;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

import it.govpay.web.entity.IntermediarioEntity;

public interface IntermediarioRepository extends JpaRepositoryImplementation<IntermediarioEntity, Long> {

}
