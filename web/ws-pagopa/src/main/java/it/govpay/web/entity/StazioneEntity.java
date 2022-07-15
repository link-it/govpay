package it.govpay.web.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "stazioni")
public class StazioneEntity {

	@Id
	private Long id;
	
	@Column(name = "cod_stazione", unique=true, nullable = false)
	private String codStazione;
	
	@Column(name = "password", nullable = false)
	private String password;
	
	@Column(name = "abilitato", nullable = false)
	private Boolean abilitato;
	
	@Column(name = "application_code", nullable = false)
	private Integer applicationCode;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_intermediario", nullable = false)
	private IntermediarioEntity intermediario;
}
