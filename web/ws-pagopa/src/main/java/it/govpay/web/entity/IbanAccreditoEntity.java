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
@Table(name = "iban_accredito", 
	uniqueConstraints = {
		@UniqueConstraint(name = "unique_iban_accredito_1", columnNames = {"cod_iban", "id_dominio"})
		})
public class IbanAccreditoEntity {
	
	@Id
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
