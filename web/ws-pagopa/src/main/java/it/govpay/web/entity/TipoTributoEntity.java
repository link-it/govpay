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
@Table(name = "tipi_tributo")
public class TipoTributoEntity {

	@Id
	private Long id;
	
	@Column(name = "cod_tributo", unique=true, nullable = false)
	private String cod_tributo;
	
	@Column(name = "descrizione", nullable = false)
	private String descrizione;
	
	@Column(name = "tipo_contabilita")
	private String tipoContabilita;
	
	@Column(name = "cod_contabilita")
	private String codContabilita;
}
