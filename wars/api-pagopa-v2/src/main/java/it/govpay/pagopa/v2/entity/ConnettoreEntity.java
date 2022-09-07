package it.govpay.pagopa.v2.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
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
@Table(name = "pagamenti", 
	uniqueConstraints = {
		@UniqueConstraint(name = "unique_connettori_1", columnNames = {"cod_connettore", "cod_proprieta"})
		})
public class ConnettoreEntity {

	@Id
	@SequenceGenerator(name="seq_connettori",sequenceName="seq_connettori", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy= GenerationType.SEQUENCE, generator="seq_connettori")
	private Long id;
	
	@Column(name = "cod_connettore", nullable = false)
	private String codConnettore;
	
	@Column(name = "cod_proprieta", nullable = false)
	private String codProprieta;
	
	@Column(name = "valore", nullable = false)
	private String valore;
}
/*
CREATE SEQUENCE seq_connettori start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE connettori
(
	cod_connettore VARCHAR(255) NOT NULL,
	cod_proprieta VARCHAR(255) NOT NULL,
	valore VARCHAR(255) NOT NULL,
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_connettori') NOT NULL,
	-- unique constraints
	CONSTRAINT unique_connettori_1 UNIQUE (cod_connettore,cod_proprieta),
	-- fk/pk keys constraints
	CONSTRAINT pk_connettori PRIMARY KEY (id)
);
*/