package it.govpay.web.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

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
@Table(name = "tributi", 
	uniqueConstraints = {
		@UniqueConstraint(name = "unique_tributi_1", columnNames = {"id_dominio", "id_tipo_tributo"})
		})
public class TributoEntity {

	@Id
	private Long id;
	
	@Column(name = "tipo_contabilita")
	private String tipoContabilita;
	
	@Column(name = "codice_contabilita")
	private String codiceContabilita;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_dominio", nullable = false)
	private DominioEntity dominio;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_tipo_tributo", nullable = false)
	private TipoTributoEntity tipoTributo;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_iban_accredito")
	private IbanAccreditoEntity ibanAccredito;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_iban_appoggio")
	private IbanAccreditoEntity ibanAppoggio;
}
