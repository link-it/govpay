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
		name = "tipi_vers_domini", 
		uniqueConstraints = {
				@UniqueConstraint(name="unique_tipi_vers_domini_1", columnNames = {"id_dominio", "id_tipo_versamento"})
		}
)
public class TipoVersamentoDominioEntity {

	@Id
	@SequenceGenerator(name="seq_tipi_vers_domini",sequenceName="seq_tipi_vers_domini", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy= GenerationType.SEQUENCE, generator="seq_tipi_vers_domini")
	private Long id;
	
	@Column(name = "codifica_iuv")
	private String codificaIuv;
	
	@Column(name = "paga_terzi")
	private Boolean pagaTerzi;
	
	@Column(name = "abilitato")
	private Boolean abilitato;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_dominio", nullable = false)
	private DominioEntity dominio;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_tipo_versamento", nullable = false)
	private TipoVersamentoEntity tipoVersamento;
}

/*
  CREATE SEQUENCE seq_tipi_vers_domini start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;
 
CREATE TABLE tipi_vers_domini
(
	codifica_iuv VARCHAR(4),
	paga_terzi BOOLEAN,
	abilitato BOOLEAN,
	
	bo_form_tipo VARCHAR(35),
	bo_form_definizione TEXT,
	bo_validazione_def TEXT,
	bo_trasformazione_tipo VARCHAR(35),
	bo_trasformazione_def TEXT,
	bo_cod_applicazione VARCHAR(35),
	bo_abilitato BOOLEAN,
	pag_form_tipo VARCHAR(35),
	pag_form_definizione TEXT,
	pag_form_impaginazione TEXT,
	pag_validazione_def TEXT,
	pag_trasformazione_tipo VARCHAR(35),
	pag_trasformazione_def TEXT,
	pag_cod_applicazione VARCHAR(35),
	pag_abilitato BOOLEAN,
	avv_mail_prom_avv_abilitato BOOLEAN,
	avv_mail_prom_avv_pdf BOOLEAN,
	avv_mail_prom_avv_tipo VARCHAR(35),
	avv_mail_prom_avv_oggetto TEXT,
	avv_mail_prom_avv_messaggio TEXT,
	avv_mail_prom_ric_abilitato BOOLEAN,
	avv_mail_prom_ric_pdf BOOLEAN,
	avv_mail_prom_ric_tipo VARCHAR(35),
	avv_mail_prom_ric_oggetto TEXT,
	avv_mail_prom_ric_messaggio TEXT,
	avv_mail_prom_ric_eseguiti BOOLEAN,
	avv_mail_prom_scad_abilitato BOOLEAN,
	avv_mail_prom_scad_preavviso INT,
	avv_mail_prom_scad_tipo VARCHAR(35),
	avv_mail_prom_scad_oggetto TEXT,
	avv_mail_prom_scad_messaggio TEXT,
	visualizzazione_definizione TEXT,
	trac_csv_tipo VARCHAR(35),
	trac_csv_header_risposta TEXT,
	trac_csv_template_richiesta TEXT,
	trac_csv_template_risposta TEXT,
	app_io_api_key VARCHAR(255),
	avv_app_io_prom_avv_abilitato BOOLEAN,
	avv_app_io_prom_avv_tipo VARCHAR(35),
	avv_app_io_prom_avv_oggetto TEXT,
	avv_app_io_prom_avv_messaggio TEXT,
	avv_app_io_prom_ric_abilitato BOOLEAN,
	avv_app_io_prom_ric_tipo VARCHAR(35),
	avv_app_io_prom_ric_oggetto TEXT,
	avv_app_io_prom_ric_messaggio TEXT,
	avv_app_io_prom_ric_eseguiti BOOLEAN,
	avv_app_io_prom_scad_abilitato BOOLEAN,
	avv_app_io_prom_scad_preavviso INT,
	avv_app_io_prom_scad_tipo VARCHAR(35),
	avv_app_io_prom_scad_oggetto TEXT,
	avv_app_io_prom_scad_messaggio TEXT,
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_tipi_vers_domini') NOT NULL,
	id_tipo_versamento BIGINT NOT NULL,
	id_dominio BIGINT NOT NULL,
	-- unique constraints
	CONSTRAINT unique_tipi_vers_domini_1 UNIQUE (id_dominio,id_tipo_versamento),
	-- fk/pk keys constraints
	CONSTRAINT fk_tvd_id_tipo_versamento FOREIGN KEY (id_tipo_versamento) REFERENCES tipi_versamento(id),
	CONSTRAINT fk_tvd_id_dominio FOREIGN KEY (id_dominio) REFERENCES domini(id),
	CONSTRAINT pk_tipi_vers_domini PRIMARY KEY (id)
);
 */