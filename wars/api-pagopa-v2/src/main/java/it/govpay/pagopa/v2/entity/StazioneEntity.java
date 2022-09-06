package it.govpay.pagopa.v2.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
		name = "stazioni", 
		uniqueConstraints = {
				@UniqueConstraint(name="unique_stazioni_1", columnNames = "cod_stazione")
		}
)
public class StazioneEntity {

	@Id
	@SequenceGenerator(name="seq_intermediari",sequenceName="seq_intermediari", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy= GenerationType.SEQUENCE, generator="seq_intermediari")
	private Long id;
	
	@Column(name = "cod_stazione", nullable = false)
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


/*
 CREATE SEQUENCE seq_stazioni start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE stazioni
(
	cod_stazione VARCHAR(35) NOT NULL,
	password VARCHAR(35) NOT NULL,
	abilitato BOOLEAN NOT NULL,
	application_code INT NOT NULL,
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_stazioni') NOT NULL,
	id_intermediario BIGINT NOT NULL,
	-- unique constraints
	CONSTRAINT unique_stazioni_1 UNIQUE (cod_stazione),
	-- fk/pk keys constraints
	CONSTRAINT fk_stz_id_intermediario FOREIGN KEY (id_intermediario) REFERENCES intermediari(id),
	CONSTRAINT pk_stazioni PRIMARY KEY (id)
);
 */
