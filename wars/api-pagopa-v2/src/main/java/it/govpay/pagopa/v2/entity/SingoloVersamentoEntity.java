package it.govpay.pagopa.v2.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
@Table(name = "singoli_versamenti", 
	uniqueConstraints = {
		@UniqueConstraint(name = "idx_sng_id_voce", columnNames = {"id_versamento", "indice_dati"})
		})
public class SingoloVersamentoEntity {
	
	public enum StatoSingoloVersamento {
		ESEGUITO,
		NON_ESEGUITO;
	}

	@Id
	@SequenceGenerator(name="seq_singoli_versamenti",sequenceName="seq_singoli_versamenti", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy= GenerationType.SEQUENCE, generator="seq_singoli_versamenti")
	private Long id;
	
	@Column(name = "cod_singolo_versamento_ente", nullable = false)
	private String codSingoloVersamentoEnte;
	
	@Column(name = "stato_singolo_versamento", nullable = false)
	@Enumerated(EnumType.STRING)
	private StatoSingoloVersamento statoSingoloVersamento;
	
	@Column(name = "importo_singolo_versamento", nullable = false)
	private Double importoSingoloVersamento;
	
	@Column(name = "tipo_bollo")
	private String tipoBollo;

	@Column(name = "hash_documento")
	private String hashDocumento;
	
	@Column(name = "provincia_residenza")
	private String provinciaResidenza;
	
	@Column(name = "tipo_contabilita")
	private String tipoContabilita;
	
	@Column(name = "codice_contabilita")
	private String codiceContabilita;
	
	@Column(name = "descrizione")
	private String descrizione;
	
	@Column(name = "dati_allegati")
	private String datiAllegati;
	
	@Column(name = "indice_dati", nullable = false)
	private Integer indiceDati;
	
	@Column(name = "descrizione_causale_rpt")
	private String descrizioneCcausaleRpt;
	
	@Column(name = "contabilita")
	private String contabilita;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_versamento", nullable = false)
	private VersamentoEntity versamento;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_dominio")
	private DominioEntity dominio;
	
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "id_tipo_tributo")
//	private TipoTributoEntity tipoTributo;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_tributo")
	private TributoEntity tributo;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_iban_accredito")
	private IbanAccreditoEntity ibanAccredito;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_iban_appoggio")
	private IbanAccreditoEntity ibanAppoggio;
}

/*
 CREATE SEQUENCE seq_singoli_versamenti start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE singoli_versamenti
(
	cod_singolo_versamento_ente VARCHAR(70) NOT NULL,
	stato_singolo_versamento VARCHAR(35) NOT NULL,
	importo_singolo_versamento DOUBLE PRECISION NOT NULL,
	-- MARCA BOLLO Valori possibili:\n01: Imposta di bollo
	tipo_bollo VARCHAR(2),
	-- MARCA BOLLO: Digest in Base64 del documento da bollare
	hash_documento VARCHAR(70),
	-- MARCA BOLLO: Sigla automobilistica della provincia di residenza
	provincia_residenza VARCHAR(2),
	tipo_contabilita VARCHAR(1),
	codice_contabilita VARCHAR(255),
	descrizione VARCHAR(256),
	dati_allegati TEXT,
	indice_dati INT NOT NULL,
	descrizione_causale_rpt VARCHAR(140),
	contabilita TEXT,
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_singoli_versamenti') NOT NULL,
	id_versamento BIGINT NOT NULL,
	id_tributo BIGINT,
	id_iban_accredito BIGINT,
	id_iban_appoggio BIGINT,
	id_dominio BIGINT,
	-- fk/pk keys constraints
	CONSTRAINT fk_sng_id_versamento FOREIGN KEY (id_versamento) REFERENCES versamenti(id),
	CONSTRAINT fk_sng_id_tributo FOREIGN KEY (id_tributo) REFERENCES tributi(id),
	CONSTRAINT fk_sng_id_iban_accredito FOREIGN KEY (id_iban_accredito) REFERENCES iban_accredito(id),
	CONSTRAINT fk_sng_id_iban_appoggio FOREIGN KEY (id_iban_appoggio) REFERENCES iban_accredito(id),
	CONSTRAINT fk_sng_id_dominio FOREIGN KEY (id_dominio) REFERENCES domini(id),
	CONSTRAINT pk_singoli_versamenti PRIMARY KEY (id)
);

-- index
CREATE UNIQUE INDEX idx_sng_id_voce ON singoli_versamenti (id_versamento, indice_dati);
ALTER TABLE singoli_versamenti ADD CONSTRAINT unique_sng_id_voce UNIQUE USING INDEX idx_sng_id_voce;
-- L'esecuzione viene completata con esito: NOTICE:  ALTER TABLE / ADD CONSTRAINT USING INDEX will rename index "idx_sng_id_voce" to "unique_sng_id_voce"


 */


