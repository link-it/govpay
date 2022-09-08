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
		name = "uo", 
		uniqueConstraints = {
				@UniqueConstraint(name="unique_uo_1", columnNames = {"cod_uo","id_dominio"})
		}
)
public class UoEntity {

	@Id
	@SequenceGenerator(name="seq_uo",sequenceName="seq_uo", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy= GenerationType.SEQUENCE, generator="seq_uo")
	private Long id;
	
	@Column(name = "cod_uo", nullable = false)
	private String codUo;
	
	@Column(name = "abilitato", nullable = false)
	private Boolean abilitato;
	
	@Column(name = "uo_codice_identificativo")
	private String uoCodiceIdentificativo;
	
	@Column(name = "uo_denominazione")
	private String uoDenominazione;
	
	@Column(name = "uo_indirizzo")
	private String uoIndirizzo;
	
	@Column(name = "uo_civico")
	private String uoCivico;
	
	@Column(name = "uo_cap")
	private String uoCap;
	
	@Column(name = "uo_localita")
	private String uoLocalita;
	
	@Column(name = "uo_provincia")
	private String uoProvincia;
	
	@Column(name = "uo_nazione")
	private String uoNazione;
	
	@Column(name = "uo_area")
	private String uoArea;
	
	@Column(name = "uo_url_sito_web")
	private String uoUrlSitoWeb;
	
	@Column(name = "uo_email")
	private String uoEmail;
	
	@Column(name = "uo_pec")
	private String uoPec;
	
	@Column(name = "uo_tel")
	private String uoTel;
	
	@Column(name = "uo_fax")
	private String uoFax;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_dominio", nullable = false)
	private DominioEntity dominio;
}
/*
CREATE SEQUENCE seq_uo start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE uo
(
	cod_uo VARCHAR(35) NOT NULL,
	abilitato BOOLEAN NOT NULL,
	uo_codice_identificativo VARCHAR(35),
	uo_denominazione VARCHAR(70),
	uo_indirizzo VARCHAR(70),
	uo_civico VARCHAR(16),
	uo_cap VARCHAR(16),
	uo_localita VARCHAR(35),
	uo_provincia VARCHAR(35),
	uo_nazione VARCHAR(2),
	uo_area VARCHAR(255),
	uo_url_sito_web VARCHAR(255),
	uo_email VARCHAR(255),
	uo_pec VARCHAR(255),
	uo_tel VARCHAR(255),
	uo_fax VARCHAR(255),
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_uo') NOT NULL,
	id_dominio BIGINT NOT NULL,
	-- unique constraints
	CONSTRAINT unique_uo_1 UNIQUE (cod_uo,id_dominio),
	-- fk/pk keys constraints
	CONSTRAINT fk_uo_id_dominio FOREIGN KEY (id_dominio) REFERENCES domini(id),
	CONSTRAINT pk_uo PRIMARY KEY (id)
); 
*/