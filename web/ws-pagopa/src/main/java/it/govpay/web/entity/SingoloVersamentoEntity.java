package it.govpay.web.entity;

import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import it.govpay.web.entity.VersamentoEntity.StatoPagamento;
import it.govpay.web.entity.VersamentoEntity.StatoVersamento;
import it.govpay.web.entity.VersamentoEntity.TipologiaTipoVersamento;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter	
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "singoli_versamenti", 
	uniqueConstraints = {
		@UniqueConstraint(name = "unique_versamenti_1", columnNames = {"cod_versamento_ente", "id_applicazione"})
		})
public class SingoloVersamentoEntity {

	@Id
	private Long id;
	
	@Column(name = "cod_singolo_versamento_ente", nullable = false)
	private String codSingoloVersamentoEnte;
}
