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
@Table(name = "documenti", 
	uniqueConstraints = {
		@UniqueConstraint(name = "unique_documenti_1", columnNames = {"cod_documento", "id_applicazione", "id_dominio"})
		})
public class DocumentoEntity {

	@Id
	private Long id;
	
	@Column(name = "cod_documento", nullable = false)
	private String codDocumento;
	
	@Column(name = "descrizione", nullable = false)
	private String descrizione;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_applicazione", nullable = false)
	private ApplicazioneEntity applicazione;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_dominio", nullable = false)
	private DominioEntity dominio;
}
