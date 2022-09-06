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
@ToString
@Entity
@Table(
		name = "utenze", 
		uniqueConstraints = {
				@UniqueConstraint(name="unique_utenze_1", columnNames = "principal")
		}
)
public class UtenzaEntity {

	@Id
	@SequenceGenerator(name="seq_utenze",sequenceName="seq_utenze", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy= GenerationType.SEQUENCE, generator="seq_utenze")
	private Long id;
	
	@Column(name = "principal", nullable = false)
	private String principal;
	
	@Column(name = "principal_originale", nullable = false)
	private String principalOriginale;
	
	@Column(name = "abilitato", nullable = false)
	private Boolean abilitato;
	
	@Column(name = "autorizzazione_domini_star", nullable = false)
	private Boolean autorizzazioneDominiStar;
	
	@Column(name = "autorizzazione_tipi_vers_star", nullable = false)
	private Boolean autorizzazioneTipiVersStar;
	
	@Column(name = "ruoli")
	private String ruoli;
	
	@Column(name = "password")
	private String password;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "utenza")
	private Set<UtenzaDominioEntity> utenzaDomini;
}

/*
CREATE SEQUENCE seq_utenze start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE utenze
(
	principal VARCHAR(4000) NOT NULL,
	principal_originale VARCHAR(4000) NOT NULL,
	abilitato BOOLEAN NOT NULL DEFAULT true,
	autorizzazione_domini_star BOOLEAN NOT NULL DEFAULT false,
	autorizzazione_tipi_vers_star BOOLEAN NOT NULL DEFAULT false,
	ruoli VARCHAR(512),
	password VARCHAR(255),
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_utenze') NOT NULL,
	-- unique constraints
	CONSTRAINT unique_utenze_1 UNIQUE (principal),
	-- fk/pk keys constraints
	CONSTRAINT pk_utenze PRIMARY KEY (id)
); 
 */
