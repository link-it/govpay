package it.govpay.pagopa.v2.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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
		name = "tipi_versamento", 
		uniqueConstraints = {
				@UniqueConstraint(name="unique_tipi_versamento_1", columnNames = "cod_tipo_versamento")
		}
)
public class TipoVersamentoEntity {

	@Id
	@SequenceGenerator(name="seq_tipi_versamento",sequenceName="seq_tipi_versamento", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy= GenerationType.SEQUENCE, generator="seq_tipi_versamento")
	private Long id;
	
	@Column(name = "cod_tipo_versamento", nullable = false)
	private String codTipoVersamento;
	
	@Column(name = "descrizione", nullable = false)
	private String descrizione;
	
	@Column(name = "codifica_iuv")
	private String codificaIuv;
	
	@Column(name = "paga_terzi", nullable = false)
	private Boolean pagaTerzi;
	
	@Column(name = "abilitato", nullable = false)
	private Boolean abilitato;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tipoVersamento")
	private Set<TipoVersamentoDominioEntity> tipiVersamentoDominio;
}

/*
CREATE SEQUENCE seq_tipi_versamento start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE tipi_versamento
(
	cod_tipo_versamento VARCHAR(35) NOT NULL,
	descrizione VARCHAR(255) NOT NULL,
	codifica_iuv VARCHAR(4),
	paga_terzi BOOLEAN NOT NULL DEFAULT false,
	abilitato BOOLEAN NOT NULL,
	
	bo_form_tipo VARCHAR(35),
	bo_form_definizione TEXT,
	bo_validazione_def TEXT,
	bo_trasformazione_tipo VARCHAR(35),
	bo_trasformazione_def TEXT,
	bo_cod_applicazione VARCHAR(35),
	bo_abilitato BOOLEAN NOT NULL DEFAULT false,
	pag_form_tipo VARCHAR(35),
	pag_form_definizione TEXT,
	pag_form_impaginazione TEXT,
	pag_validazione_def TEXT,
	pag_trasformazione_tipo VARCHAR(35),
	pag_trasformazione_def TEXT,
	pag_cod_applicazione VARCHAR(35),
	pag_abilitato BOOLEAN NOT NULL DEFAULT false,
	avv_mail_prom_avv_abilitato BOOLEAN NOT NULL DEFAULT false,
	avv_mail_prom_avv_pdf BOOLEAN,
	avv_mail_prom_avv_tipo VARCHAR(35),
	avv_mail_prom_avv_oggetto TEXT,
	avv_mail_prom_avv_messaggio TEXT,
	avv_mail_prom_ric_abilitato BOOLEAN NOT NULL DEFAULT false,
	avv_mail_prom_ric_pdf BOOLEAN,
	avv_mail_prom_ric_tipo VARCHAR(35),
	avv_mail_prom_ric_oggetto TEXT,
	avv_mail_prom_ric_messaggio TEXT,
	avv_mail_prom_ric_eseguiti BOOLEAN,
	avv_mail_prom_scad_abilitato BOOLEAN NOT NULL DEFAULT false,
	avv_mail_prom_scad_preavviso INT,
	avv_mail_prom_scad_tipo VARCHAR(35),
	avv_mail_prom_scad_oggetto TEXT,
	avv_mail_prom_scad_messaggio TEXT,
	visualizzazione_definizione TEXT,
	trac_csv_tipo VARCHAR(35),
	trac_csv_header_risposta TEXT,
	trac_csv_template_richiesta TEXT,
	trac_csv_template_risposta TEXT,
	avv_app_io_prom_avv_abilitato BOOLEAN NOT NULL DEFAULT false,
	avv_app_io_prom_avv_tipo VARCHAR(35),
	avv_app_io_prom_avv_oggetto TEXT,
	avv_app_io_prom_avv_messaggio TEXT,
	avv_app_io_prom_ric_abilitato BOOLEAN NOT NULL DEFAULT false,
	avv_app_io_prom_ric_tipo VARCHAR(35),
	avv_app_io_prom_ric_oggetto TEXT,
	avv_app_io_prom_ric_messaggio TEXT,
	avv_app_io_prom_ric_eseguiti BOOLEAN,
	avv_app_io_prom_scad_abilitato BOOLEAN NOT NULL DEFAULT false,
	avv_app_io_prom_scad_preavviso INT,
	avv_app_io_prom_scad_tipo VARCHAR(35),
	avv_app_io_prom_scad_oggetto TEXT,
	avv_app_io_prom_scad_messaggio TEXT,
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_tipi_versamento') NOT NULL,
	-- unique constraints
	CONSTRAINT unique_tipi_versamento_1 UNIQUE (cod_tipo_versamento),
	-- fk/pk keys constraints
	CONSTRAINT pk_tipi_versamento PRIMARY KEY (id)
); 
 */