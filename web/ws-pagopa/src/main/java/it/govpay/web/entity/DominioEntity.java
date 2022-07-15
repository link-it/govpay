package it.govpay.web.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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
@Table(name = "domini")
public class DominioEntity {

	@Id
	private Long id;
	
	@Column(name = "cod_dominio", unique=true, nullable = false)
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
	private String iuv_prefix;
	
	@Column(name = "segregation_code")
	private Integer segregation_code;
	
	@Column(name = "cbill")
	private String cbill;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_stazione")
	private StazioneEntity stazione;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_applicazione_default")
	private ApplicazioneEntity applicazioneDefault;
	
	@Column(name = "intermediato", nullable = false)
	private Boolean intermediato;
	
	@Column(name = "tassonomia_pago_pa")
	private String tassonomiaPagoPa;
	
	@Column(name = "aut_stampa_poste")
	private String autStampaPoste;
}
