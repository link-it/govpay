package it.govpay.pagopa.v2.entity;

import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
@Table(name = "versamenti", 
	uniqueConstraints = {
		@UniqueConstraint(name = "unique_versamenti_1", columnNames = {"cod_versamento_ente", "id_applicazione"})
		}, 
	indexes = {
			@Index(name="idx_vrs_id_pendenza", columnList = "cod_versamento_ente,id_applicazione"),
			@Index(name="idx_vrs_data_creaz", columnList = "data_creazione DESC"),
			@Index(name="idx_vrs_stato_vrs", columnList = "stato_versamento"),
			@Index(name="idx_vrs_deb_identificativo", columnList = "src_debitore_identificativo"),
			@Index(name="idx_vrs_iuv", columnList = "src_iuv"),
			@Index(name="idx_vrs_auth", columnList = "id_dominio,id_tipo_versamento,id_uo"),
			@Index(name="idx_vrs_prom_avviso", columnList = "avviso_notificato,data_notifica_avviso DESC"),
			@Index(name="idx_vrs_avv_mail_prom_scad", columnList = "avv_mail_prom_scad_notificato,avv_mail_data_prom_scadenza DESC"),
			@Index(name="idx_vrs_avv_io_prom_scad", columnList = "avv_app_io_prom_scad_notificat,avv_app_io_data_prom_scadenza DESC")
	})
public class VersamentoEntity {
	
	public enum TipologiaTipoVersamento { SPONTANEO, DOVUTO }
	
	public enum StatoPagamento { PAGATO, INCASSATO, NON_PAGATO }

	public enum StatoVersamento {
		NON_ESEGUITO,
		ESEGUITO,
		PARZIALMENTE_ESEGUITO,
		ANNULLATO,
		ESEGUITO_ALTRO_CANALE,
		ANOMALO,
		ESEGUITO_SENZA_RPT,
		INCASSATO;
	}

	@Id
	@SequenceGenerator(name="seq_versamenti",sequenceName="seq_versamenti", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy= GenerationType.SEQUENCE, generator="seq_versamenti")
	private Long id;
	
	@Column(name = "cod_versamento_ente", nullable = false)
	private String codVersamentoEnte;
	
	@Column(name = "nome")
	private String nome;
	
	@Column(name = "importo_totale", nullable = false)
	private Double importoTotale;
	
	@Column(name = "stato_versamento", nullable = false)
	@Enumerated(EnumType.STRING)
	private StatoVersamento statoVersamento;
	
	@Column(name = "descrizione_stato")
	private String descrizioneStato;
	
	@Column(name = "aggiornabile", nullable = false)
	private Boolean aggiornabile;
	
	@Column(name = "data_creazione", nullable = false)
	private LocalDateTime datacreazione;

	@Column(name = "data_validita")
	private LocalDateTime dataValidita;
	
	@Column(name = "data_scadenza")
	private LocalDateTime dataScadenza;
	
	@Column(name = "data_ora_ultimo_aggiornamento", nullable = false)
	private LocalDateTime dataOraUltimoAggiornamento;
	
	@Column(name = "causale_versamento")
	private String causaleVersamento;
	
	@Column(name = "debitore_tipo")
	private String debitoreTipo;
	
	@Column(name = "debitore_identificativo", nullable = false)
	private String debitoreIdentificativo;
	
	@Column(name = "debitore_anagrafica", nullable = false)
	private String debitoreAnagrafica;
	
	@Column(name = "debitore_indirizzo")
	private String debitoreIndirizzo;
	
	@Column(name = "debitore_civico")
	private String debitoreCivico;
	
	@Column(name = "debitore_cap")
	private String debitoreCap;
	
	@Column(name = "debitore_localita")
	private String debitoreLocalita;
	
	@Column(name = "debitore_provincia")
	private String debitoreProvincia;
	
	@Column(name = "debitore_nazione")
	private String debitoreNazione;
	
	@Column(name = "debitore_email")
	private String debitoreEmail;
	
	@Column(name = "debitore_telefono")
	private String debitoreTelefono;
	
	@Column(name = "debitore_cellulare")
	private String debitoreCellulare;
	
	@Column(name = "debitore_fax")
	private String debitoreFax;
	
	@Column(name = "tassonomia_avviso")
	private String tassonomiaAvviso;
	
	@Column(name = "tassonomia")
	private String tassonomia;
	
	@Column(name = "cod_lotto")
	private String codLotto;
	
	@Column(name = "cod_versamento_lotto")
	private String codVersamentoLotto;
	
	@Column(name = "cod_anno_tributario")
	private String codAnnoTributario;
	
	@Column(name = "cod_bundlekey")
	private String codBundlekey;
	
	@Column(name = "dati_allegati")
	private String datiAllegati;
	
	@Column(name = "incasso")
	private String incasso;
	
	@Column(name = "anomalie")
	private String anomalie;
	
	@Column(name = "iuv_versamento")
	private String iuvVersamento;
	
	@Column(name = "numero_avviso")
	private String numeroAvviso;
	
	@Column(name = "ack", nullable = false)
	private Boolean ack;
	
	@Column(name = "anomalo", nullable = false)
	private Boolean anomalo;
	
	@Column(name = "divisione")
	private String divisione;
	
	@Column(name = "direzione")
	private String direzione;
	
	@Column(name = "id_sessione")
	private String idSessione;
	
	@Column(name = "data_pagamento")
	private LocalDateTime dataPagamento;
	
	@Column(name = "importo_pagato", nullable = false)
	private Double importoPagato;
	
	@Column(name = "importo_incassato", nullable = false)
	private Double importoIncassato;
	
	@Column(name = "stato_pagamento", nullable = false)
	@Enumerated(EnumType.STRING)
	private StatoPagamento statoPagamento;
	
	@Column(name = "iuv_pagamento")
	private String iuvPagamento;
	
	@Column(name = "src_iuv")
	private String srcIuv;
	
	@Column(name = "src_debitore_identificativo", nullable = false)
	private String srcDebitoreIdentificativo;
	
	@Column(name = "cod_rata")
	private String codRata;
	
	@Column(name = "tipo", nullable = false)
	@Enumerated(EnumType.STRING)
	private TipologiaTipoVersamento tipo;
	
	@Column(name = "data_notifica_avviso")
	private LocalDateTime dataNotificaAvviso;
	
	@Column(name = "avviso_notificato")
	private String avvisoNotificato;
	
	@Column(name = "avv_mail_data_prom_scadenza")
	private String avvMailDataPromScadenza;
	
	@Column(name = "avv_mail_prom_scad_notificato")
	private Boolean avvMailPromScadNotificato;
	
	@Column(name = "avv_app_io_data_prom_scadenza")
	private String avvAppIoDataPromScadenza;
	
	@Column(name = "avv_app_io_prom_scad_notificat")
	private Boolean avvAppIoPromScadNotificat;
	
	@Column(name = "proprieta")
	private String proprieta;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_applicazione", nullable = false)
	private ApplicazioneEntity applicazione;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_dominio", nullable = false)
	private DominioEntity dominio;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_tipo_versamento", nullable = false)
	private TipoVersamentoEntity tipoVersamento;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_tipo_versamento_dominio", nullable = false)
	private TipoVersamentoDominioEntity tipoVersamentodominio;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_uo")
	private UoEntity uo;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_documento")
	private DocumentoEntity documento;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "versamento")
	private Set<SingoloVersamentoEntity> singoliVersamenti;
}
/*
CREATE SEQUENCE seq_versamenti start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE versamenti
(
	cod_versamento_ente VARCHAR(35) NOT NULL,
	nome VARCHAR(35),
	importo_totale DOUBLE PRECISION NOT NULL,
	stato_versamento VARCHAR(35) NOT NULL,
	descrizione_stato VARCHAR(255),
	-- Indica se, decorsa la dataScadenza, deve essere aggiornato da remoto o essere considerato scaduto
	aggiornabile BOOLEAN NOT NULL,
	data_creazione TIMESTAMP NOT NULL,
	data_validita TIMESTAMP,
	data_scadenza TIMESTAMP,
	data_ora_ultimo_aggiornamento TIMESTAMP NOT NULL,
	causale_versamento VARCHAR(1024),
	debitore_tipo VARCHAR(1),
	debitore_identificativo VARCHAR(35) NOT NULL,
	debitore_anagrafica VARCHAR(70) NOT NULL,
	debitore_indirizzo VARCHAR(70),
	debitore_civico VARCHAR(16),
	debitore_cap VARCHAR(16),
	debitore_localita VARCHAR(35),
	debitore_provincia VARCHAR(35),
	debitore_nazione VARCHAR(2),
	debitore_email VARCHAR(256),
	debitore_telefono VARCHAR(35),
	debitore_cellulare VARCHAR(35),
	debitore_fax VARCHAR(35),
	tassonomia_avviso VARCHAR(35),
	tassonomia VARCHAR(35),
	cod_lotto VARCHAR(35),
	cod_versamento_lotto VARCHAR(35),
	cod_anno_tributario VARCHAR(35),
	cod_bundlekey VARCHAR(256),
	dati_allegati TEXT,
	incasso VARCHAR(1),
	anomalie TEXT,
	iuv_versamento VARCHAR(35),
	numero_avviso VARCHAR(35),
	ack BOOLEAN NOT NULL,
	anomalo BOOLEAN NOT NULL,
	divisione VARCHAR(35),
	direzione VARCHAR(35),
	id_sessione VARCHAR(35),
	data_pagamento TIMESTAMP,
	importo_pagato DOUBLE PRECISION NOT NULL,
	importo_incassato DOUBLE PRECISION NOT NULL,
	stato_pagamento VARCHAR(35) NOT NULL,
	iuv_pagamento VARCHAR(35),
	src_iuv VARCHAR(35),
	src_debitore_identificativo VARCHAR(35) NOT NULL,
	cod_rata VARCHAR(35),
	tipo VARCHAR(35) NOT NULL,
	data_notifica_avviso TIMESTAMP,
	avviso_notificato BOOLEAN,
	avv_mail_data_prom_scadenza TIMESTAMP,
	avv_mail_prom_scad_notificato BOOLEAN,
	avv_app_io_data_prom_scadenza TIMESTAMP,
	avv_app_io_prom_scad_notificat BOOLEAN,
	proprieta TEXT,
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_versamenti') NOT NULL,
	id_tipo_versamento_dominio BIGINT NOT NULL,
	id_tipo_versamento BIGINT NOT NULL,
	id_dominio BIGINT NOT NULL,
	id_uo BIGINT,
	id_applicazione BIGINT NOT NULL,
	id_documento BIGINT,
	-- unique constraints
	CONSTRAINT unique_versamenti_1 UNIQUE (cod_versamento_ente,id_applicazione),
	-- fk/pk keys constraints
	CONSTRAINT fk_vrs_id_tipo_versamento_dominio FOREIGN KEY (id_tipo_versamento_dominio) REFERENCES tipi_vers_domini(id),
	CONSTRAINT fk_vrs_id_tipo_versamento FOREIGN KEY (id_tipo_versamento) REFERENCES tipi_versamento(id),
	CONSTRAINT fk_vrs_id_dominio FOREIGN KEY (id_dominio) REFERENCES domini(id),
	CONSTRAINT fk_vrs_id_uo FOREIGN KEY (id_uo) REFERENCES uo(id),
	CONSTRAINT fk_vrs_id_applicazione FOREIGN KEY (id_applicazione) REFERENCES applicazioni(id),
	CONSTRAINT fk_vrs_id_documento FOREIGN KEY (id_documento) REFERENCES documenti(id),
	CONSTRAINT pk_versamenti PRIMARY KEY (id)
);

-- index
CREATE INDEX idx_vrs_id_pendenza ON versamenti (cod_versamento_ente,id_applicazione);
CREATE INDEX idx_vrs_data_creaz ON versamenti (data_creazione DESC);
CREATE INDEX idx_vrs_stato_vrs ON versamenti (stato_versamento);
CREATE INDEX idx_vrs_deb_identificativo ON versamenti (src_debitore_identificativo);
CREATE INDEX idx_vrs_iuv ON versamenti (src_iuv);
CREATE INDEX idx_vrs_auth ON versamenti (id_dominio,id_tipo_versamento,id_uo);
CREATE INDEX idx_vrs_prom_avviso ON versamenti (avviso_notificato,data_notifica_avviso DESC);
CREATE INDEX idx_vrs_avv_mail_prom_scad ON versamenti (avv_mail_prom_scad_notificato,avv_mail_data_prom_scadenza DESC);
CREATE INDEX idx_vrs_avv_io_prom_scad ON versamenti (avv_app_io_prom_scad_notificat,avv_app_io_data_prom_scadenza DESC); 
*/