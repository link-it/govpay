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
@Table(name = "iban_accredito", 
	uniqueConstraints = {
		@UniqueConstraint(name = "unique_iban_accredito_1", columnNames = {"cod_iban", "id_dominio"})
		})
public class IbanAccreditoEntity {
	
	@Id
	@SequenceGenerator(name="seq_iban_accredito",sequenceName="seq_iban_accredito", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy= GenerationType.SEQUENCE, generator="seq_iban_accredito")
	private Long id;
	
	@Column(name = "cod_iban", nullable = false)
	private String codIban;
	
	@Column(name = "bic_accredito", nullable = false)
	private String bicAccredito;
	
	@Column(name = "postale", nullable = false)
	private Boolean postale;
	
	@Column(name = "abilitato", nullable = false)
	private Boolean abilitato;
	
	@Column(name = "descrizione")
	private String descrizione;
	
	@Column(name = "intestatario")
	private String intestatario;
	
	@Column(name = "aut_stampa_poste")
	private String autStampaPoste;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_dominio", nullable = false)
	private DominioEntity dominio;
		
}

/*
 CREATE SEQUENCE seq_iban_accredito start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE iban_accredito
(
	cod_iban VARCHAR(255) NOT NULL,
	bic_accredito VARCHAR(255),
	postale BOOLEAN NOT NULL,
	abilitato BOOLEAN NOT NULL,
	descrizione VARCHAR(255),
	intestatario VARCHAR(255),
	aut_stampa_poste VARCHAR(255),
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_iban_accredito') NOT NULL,
	id_dominio BIGINT NOT NULL,
	-- unique constraints
	CONSTRAINT unique_iban_accredito_1 UNIQUE (cod_iban,id_dominio),
	-- fk/pk keys constraints
	CONSTRAINT fk_iba_id_dominio FOREIGN KEY (id_dominio) REFERENCES domini(id),
	CONSTRAINT pk_iban_accredito PRIMARY KEY (id)
);
 */

