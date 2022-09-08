package it.govpay.pagopa.v2.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter	
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(
		name = "documenti", 
		uniqueConstraints = {
				@UniqueConstraint(name="unique_documenti_1", columnNames = {"cod_documento","id_applicazione","id_dominio"})
		}
)
public class DocumentoEntity {

	@Id
	@SequenceGenerator(name="seq_documenti",sequenceName="seq_documenti", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy= GenerationType.SEQUENCE, generator="seq_documenti")
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
/*
CREATE SEQUENCE seq_documenti start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE documenti
(
	cod_documento VARCHAR(35) NOT NULL,
	descrizione VARCHAR(255) NOT NULL,
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_documenti') NOT NULL,
	id_dominio BIGINT NOT NULL,
	id_applicazione BIGINT NOT NULL,
	-- unique constraints
	CONSTRAINT unique_documenti_1 UNIQUE (cod_documento,id_applicazione,id_dominio),
	-- fk/pk keys constraints
	CONSTRAINT fk_doc_id_dominio FOREIGN KEY (id_dominio) REFERENCES domini(id),
	CONSTRAINT fk_doc_id_applicazione FOREIGN KEY (id_applicazione) REFERENCES applicazioni(id),
	CONSTRAINT pk_documenti PRIMARY KEY (id)
);
*/