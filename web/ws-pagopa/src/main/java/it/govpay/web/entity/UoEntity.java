package it.govpay.web.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "uo", 
	uniqueConstraints = {
		@UniqueConstraint(name = "unique_uo_1", columnNames = {"cod_uo", "id_dominio"})
		})
public class UoEntity {

	@Id
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
