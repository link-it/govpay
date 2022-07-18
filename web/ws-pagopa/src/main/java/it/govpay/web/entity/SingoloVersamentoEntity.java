package it.govpay.web.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_tipo_tributo")
	private TipoTributoEntity tipoTributo;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_tributo")
	private TributoEntity tributo;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_iban_appoggio")
	private IbanAccreditoEntity ibanAppoggio;
}
