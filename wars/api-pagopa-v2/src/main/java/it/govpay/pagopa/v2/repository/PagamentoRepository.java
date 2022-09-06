package it.govpay.pagopa.v2.repository;

import java.util.List;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

import it.govpay.pagopa.v2.entity.PagamentoEntity;

public interface PagamentoRepository extends JpaRepositoryImplementation<PagamentoEntity, Long> {

	public List<PagamentoEntity> findAllByIdSingoloVersamento(Long idSingoloVersamento);
}