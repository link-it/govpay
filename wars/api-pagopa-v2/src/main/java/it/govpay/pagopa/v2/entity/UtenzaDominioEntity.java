package it.govpay.pagopa.v2.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

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
		name = "utenze_domini" 
)
public class UtenzaDominioEntity {

	@Id
	@SequenceGenerator(name="seq_utenze_domini",sequenceName="seq_utenze_domini", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy= GenerationType.SEQUENCE, generator="seq_utenze_domini")
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "id_utenza", nullable = false)
	private UtenzaEntity utenza;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_dominio")
	private DominioEntity dominio;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_uo")
	private UoEntity uo;
}
/*
CREATE SEQUENCE seq_utenze_domini start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE utenze_domini
(
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_utenze_domini') NOT NULL,
	id_utenza BIGINT NOT NULL,
	id_dominio BIGINT,
	id_uo BIGINT,
	-- fk/pk keys constraints
	CONSTRAINT fk_nzd_id_utenza FOREIGN KEY (id_utenza) REFERENCES utenze(id),
	CONSTRAINT fk_nzd_id_dominio FOREIGN KEY (id_dominio) REFERENCES domini(id),
	CONSTRAINT fk_nzd_id_uo FOREIGN KEY (id_uo) REFERENCES uo(id),
	CONSTRAINT pk_utenze_domini PRIMARY KEY (id)
);
*/