package it.govpay.web.repository;

import java.util.List;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

import it.govpay.web.entity.PagamentoEntity;

public interface PagamentoRepository extends JpaRepositoryImplementation<PagamentoEntity, Long> {

	public List<PagamentoEntity> findAllByIdSingoloVersamento(Long idSingoloVersamento);
}
