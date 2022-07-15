package it.govpay.web.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

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
@Table(name = "tipi_versamento")
public class TipoVersamentoEntity {

	@Id
	private Long id;
	
	@Column(name = "cod_tipo_versamento", unique=true, nullable = false)
	private String codTipoVersamento;
	
	@Column(name = "descrizione", nullable = false)
	private String descrizione;
}
