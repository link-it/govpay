package it.govpay.pagopa.v2.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
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
		name = "applicazioni", 
		uniqueConstraints = {
				@UniqueConstraint(name="unique_applicazioni_1", columnNames = "cod_applicazione"),
				@UniqueConstraint(name="unique_applicazioni_2", columnNames = "id_utenza")
		}
)
public class ApplicazioneEntity {

	@Id
	@SequenceGenerator(name="seq_applicazioni",sequenceName="seq_applicazioni", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy= GenerationType.SEQUENCE, generator="seq_applicazioni")
	private Long id;
	
	@Column(name = "cod_applicazione", nullable = false)
	private String codApplicazione;
	
	@Column(name = "auto_iuv", nullable = false)
	private Boolean autoIuv;
	
	@Column(name = "firma_ricevuta", nullable = false)
	private String firmaRicevuta;
	
	@Column(name = "cod_connettore_integrazione")
	private String codConnettoreIntegrazione;
	
	@Column(name = "trusted", nullable = false)
	private Boolean trusted;
	
	@Column(name = "cod_applicazione_iuv")
	private String codApplicazioneIuv;
	
	@Column(name = "reg_exp")
	private String regExp;
	
	@OneToOne(fetch = FetchType.LAZY, mappedBy = "utenza", optional = false)
	@JoinColumn(name = "id_utenza", nullable = false)
	private UtenzaEntity utenza;
}

 /*
  CREATE SEQUENCE seq_applicazioni start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE applicazioni
(
	cod_applicazione VARCHAR(35) NOT NULL,
	auto_iuv BOOLEAN NOT NULL,
	firma_ricevuta VARCHAR(1) NOT NULL,
	cod_connettore_integrazione VARCHAR(255),
	trusted BOOLEAN NOT NULL,
	cod_applicazione_iuv VARCHAR(3),
	reg_exp VARCHAR(1024),
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_applicazioni') NOT NULL,
	id_utenza BIGINT NOT NULL,
	-- unique constraints
	CONSTRAINT unique_applicazioni_1 UNIQUE (cod_applicazione),
	CONSTRAINT unique_applicazioni_2 UNIQUE (id_utenza),
	-- fk/pk keys constraints
	CONSTRAINT fk_app_id_utenza FOREIGN KEY (id_utenza) REFERENCES utenze(id),
	CONSTRAINT pk_applicazioni PRIMARY KEY (id)
);
   */
  