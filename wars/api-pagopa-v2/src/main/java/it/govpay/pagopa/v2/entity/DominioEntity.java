package it.govpay.pagopa.v2.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
		name = "domini", 
		uniqueConstraints = {
				@UniqueConstraint(name="unique_domini_1", columnNames = "cod_dominio")
		}
)
public class DominioEntity {
	
	public static final String EC = "EC"; 
	
	@Id
	@SequenceGenerator(name="seq_domini",sequenceName="seq_domini", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy= GenerationType.SEQUENCE, generator="seq_domini")
	private Long id;
	
	@Column(name = "cod_dominio", nullable = false)
	private String codDominio;
	
	@Column(name = "gln")
	private String gln;
	
	@Column(name = "abilitato", nullable = false)
	private Boolean abilitato;
	
	@Column(name = "ragione_sociale", nullable = false)
	private String ragioneSociale;
	
	@Column(name = "aux_digit", nullable = false)
	private Integer auxDigit;
	
	@Column(name = "iuv_prefix")
	private String iuvPrefix;

	@Column(name = "segregationCode", nullable = false)
	private Integer segregation_code;
	
	@Column(name = "cbill")
	private String cbill;
	
	@Column(name = "aut_stampa_poste")
	private String autStampaPoste;
	
	@Column(name = "cod_connettore_my_pivot")
	private String codConnettoreMyPivot;
	
	@Column(name = "cod_connettore_secim")
	private String codConnettoreSecim;
	
	@Column(name = "cod_connettore_gov_pay")
	private String codConnettoreGovPay;
	
	@Column(name = "cod_connettore_hyper_sic_apk")
	private String codConnettore_hyperSicApk;
	
	@Column(name = "cod_connettore_maggioli_jppa")
	private String codConnettoreMaggioliJppa;
	
	@Column(name = "intermediato", nullable = false)
	private Boolean intermediato;
	
	@Column(name = "tassonomia_pago_pa")
	private String tassonomiaPagoPa;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_stazione")
	private StazioneEntity stazione;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_applicazione_default")
	private ApplicazioneEntity applicazioneDefault;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "dominio")
	private Set<TipoVersamentoDominioEntity> tipiVersamentoDominio;
}

/*
 CREATE SEQUENCE seq_domini start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE domini
(
	cod_dominio VARCHAR(35) NOT NULL,
	gln VARCHAR(35),
	abilitato BOOLEAN NOT NULL,
	ragione_sociale VARCHAR(70) NOT NULL,
	aux_digit INT NOT NULL DEFAULT 0,
	iuv_prefix VARCHAR(255),
	segregation_code INT,
	logo BYTEA,    -- non usato
	cbill VARCHAR(255),
	aut_stampa_poste VARCHAR(255),
	cod_connettore_my_pivot VARCHAR(255),
	cod_connettore_secim VARCHAR(255),
	cod_connettore_gov_pay VARCHAR(255),
	cod_connettore_hyper_sic_apk VARCHAR(255),
	cod_connettore_maggioli_jppa VARCHAR(255),
	intermediato BOOLEAN NOT NULL,
	tassonomia_pago_pa VARCHAR(35),
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_domini') NOT NULL,
	id_stazione BIGINT,
	id_applicazione_default BIGINT,
	-- unique constraints
	CONSTRAINT unique_domini_1 UNIQUE (cod_dominio),
	-- fk/pk keys constraints
	CONSTRAINT fk_dom_id_stazione FOREIGN KEY (id_stazione) REFERENCES stazioni(id),
	CONSTRAINT fk_dom_id_applicazione_default FOREIGN KEY (id_applicazione_default) REFERENCES applicazioni(id),
	CONSTRAINT pk_domini PRIMARY KEY (id)
);
 */
