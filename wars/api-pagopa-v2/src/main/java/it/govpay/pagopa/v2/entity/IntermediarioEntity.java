package it.govpay.pagopa.v2.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter	
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = { "stazioni" })
@Entity
@Table(
		name = "intermediari", 
		uniqueConstraints = {
				@UniqueConstraint(name="unique_intermediari_1", columnNames = "cod_intermediario")
		}
)
public class IntermediarioEntity {

	@Id
	@SequenceGenerator(name="seq_intermediari",sequenceName="seq_intermediari", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy= GenerationType.SEQUENCE, generator="seq_intermediari")
	private Long id;
	
	@Column(name = "cod_intermediario", nullable = false)
	private String codIntermediario;

	@Column(name = "cod_connettore_pdd", nullable = false)
	private String codConnettorePdd;
	
	@Column(name = "cod_connettore_ftp")
	private String codConnettoreFtp;
	
	@Column(name = "denominazione", nullable = false)
	private String denominazione;
	
	@Column(name = "principal", nullable = false)
	private String principal;
	
	@Column(name = "principal_originale", nullable = false)
	private String principalOriginale;
	
	@Column(name = "abilitato", nullable = false)
	private Boolean abilitato;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "stazione")
	private Set<StazioneEntity> stazioni;

}

/*
CREATE SEQUENCE seq_intermediari start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE intermediari
(
	cod_intermediario VARCHAR(35) NOT NULL,
	cod_connettore_pdd VARCHAR(35) NOT NULL,
	cod_connettore_ftp VARCHAR(35),
	denominazione VARCHAR(255) NOT NULL,
	principal VARCHAR(4000) NOT NULL,
	principal_originale VARCHAR(4000) NOT NULL,
	abilitato BOOLEAN NOT NULL,
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_intermediari') NOT NULL,
	-- unique constraints
	CONSTRAINT unique_intermediari_1 UNIQUE (cod_intermediario),
	-- fk/pk keys constraints
	CONSTRAINT pk_intermediari PRIMARY KEY (id)
);
 * */
