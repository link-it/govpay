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
@Table(name = "tipi_tributo", 
	uniqueConstraints = {
		@UniqueConstraint(name = "unique_tipi_tributo_1", columnNames = {"cod_tributo"})
		})
public class TipoTributoEntity {

	@Id
	@SequenceGenerator(name="seq_tipi_tributo",sequenceName="seq_tipi_tributo", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy= GenerationType.SEQUENCE, generator="seq_tipi_tributo")
	private Long id;
	
	@Column(name = "cod_tributo", unique=true, nullable = false)
	private String cod_tributo;
	
	@Column(name = "descrizione")
	private String descrizione;
	
	@Column(name = "tipo_contabilita")
	private String tipoContabilita;
	
	@Column(name = "cod_contabilita")
	private String codContabilita;
}

/*
 CREATE SEQUENCE seq_tipi_tributo start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE tipi_tributo
(
	cod_tributo VARCHAR(255) NOT NULL,
	descrizione VARCHAR(255),
	tipo_contabilita VARCHAR(1),
	cod_contabilita VARCHAR(255),
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_tipi_tributo') NOT NULL,
	-- unique constraints
	CONSTRAINT unique_tipi_tributo_1 UNIQUE (cod_tributo),
	-- fk/pk keys constraints
	CONSTRAINT pk_tipi_tributo PRIMARY KEY (id)
);
 * */
