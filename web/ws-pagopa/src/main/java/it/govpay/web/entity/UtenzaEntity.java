package it.govpay.web.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
@Table(name = "utenze")
public class UtenzaEntity {

	@Id
	private Long id;
	
	@Column(name = "principal", unique=true, nullable = false)
	private String principal;
	
	@Column(name = "principal_originale", nullable = false)
	private String principal_originale;
	
	@Column(name = "abilitato", nullable = false)
	private Boolean abilitato;
	
	@Column(name = "autorizzazione_domini_star", nullable = false)
	private Boolean autorizzazioneDominiStar;
	
	@Column(name = "autorizzazione_tipi_vers_star", nullable = false)
	private Boolean autorizzazioneTipiVersStar;
	
	@Column(name = "ruoli")
	private String ruoli;
	
	@Column(name = "password")
	private String password;
	
	
	public boolean isIdDominioAutorizzato(Long idDominio) {
		if(this.dominiUo != null) {
			for(IdUnitaOperativa id: this.dominiUo) {
				if(id.getIdDominio() != null) {
					if(id.getIdDominio().longValue() == idDominio.longValue())
						return true;
				}
			}
		}
		
		return false;
	}
	
}
