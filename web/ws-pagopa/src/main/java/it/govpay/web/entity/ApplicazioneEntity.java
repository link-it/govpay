package it.govpay.web.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
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
@Table(name = "applicazioni")
public class ApplicazioneEntity {

	@Id
	private Long id;
	
	@Column(name = "cod_applicazione", unique=true, nullable = false)
	private String codApplicazione;
	
	@Column(name = "auto_iuv", nullable = false)
	private Boolean autoIuv;
	
	@Column(name = "firma_ricevuta", nullable = false)
	private String firmaRicevuta;
	
	@Column(name = "cod_connettore_integrazione", nullable = false)
	private String codConnettoreIntegrazione;
	
	@Column(name = "trusted", nullable = false)
	private Boolean trusted;
	
	@Column(name = "cod_applicazione_iuv", nullable = false)
	private String codApplicazioneIuv;
	
	@Column(name = "reg_exp", nullable = false)
	private String regExp;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_utenza", unique=true, nullable = false)
	private UtenzaEntity utenza;
}
