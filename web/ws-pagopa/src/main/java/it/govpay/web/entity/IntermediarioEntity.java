package it.govpay.web.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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
@Table(name = "intermediari")
public class IntermediarioEntity {

	@Id
	private Long id;
	
	@Column(name = "cod_intermediario", unique=true, nullable = false)
	private String codIntermediario;
	
	@Column(name = "denominazione", nullable = false)
	private String denominazione;
	
	@Column(name = "principal", nullable = false)
	private String principal;
	
	@Column(name = "principal_originale", nullable = false)
	private String principalOriginale;
	
	@Column(name = "abilitato", nullable = false)
	private Boolean abilitato;
	
	@Column(name = "cod_connettore_pdd", nullable = false)
	private String codConnettorePdd;
	
	@Column(name = "cod_connettore_ftp", nullable = false)
	private String codConnettoreFtp;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "intermediario")
	private Set<StazioneEntity> stazioni;
}
