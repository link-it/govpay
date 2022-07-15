package it.govpay.web.entity;

import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
	
	@Column(name = "aggiornabile", nullable = false)
	private Boolean aggiornabile;
	
	@Column(name = "data_creazione", nullable = false)
	private LocalDateTime dataCreazione;
	
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
	private TipoVersamentoDominioEntity tipoVersamentoDominio;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_uo")
	private UoEntity uo;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_documento")
	private DocumentoEntity documento;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "versamento")
	private Set<SingoloVersamentoEntity> singoliVersamenti;
}
