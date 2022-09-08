package it.govpay.pagopa.v2.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Type;

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
@Table(name = "pagamenti", 
	uniqueConstraints = {
		@UniqueConstraint(name = "idx_pag_id_riscossione", columnNames = {"cod_dominio", "iuv", "iur", "indice_dati"})
		}, 
		indexes = {
			@Index(name="idx_pag_fk_rpt", columnList = "id_rpt"),
			@Index(name="idx_pag_fk_sng", columnList = "id_singolo_versamento")
	})
public class PagamentoEntity {
	
	public enum TipoPagamento {
		ENTRATA, MBT, ALTRO_INTERMEDIARIO, ENTRATA_PA_NON_INTERMEDIATA
	}
	
	public enum TipoAllegato {
		ES, BD
	}
	
	public enum Stato {
		PAGATO, INCASSATO, PAGATO_SENZA_RPT
	}

	@Id
	@SequenceGenerator(name="seq_pagamenti",sequenceName="seq_pagamenti", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy= GenerationType.SEQUENCE, generator="seq_pagamenti")
	private Long id;
	
	@Column(name = "cod_dominio", nullable = false)
	private String codDominio;
	
	@Column(name = "iuv", nullable = false)
	private String iuv;
	
	@Column(name = "indice_dati", nullable = false)
	private Integer indiceDati;
	
	@Column(name = "importo_pagato", nullable = false)
	private Double importoPagato;
	
	@Column(name = "data_acquisizione", nullable = false)
	private LocalDateTime dataAcquisizione;
	
	@Column(name = "iur", nullable = false)
	private String iur;
	
	@Column(name = "data_pagamento", nullable = false)
	private LocalDateTime dataPagamento;
	
	@Column(name = "commissioni_psp")
	private Double commissioniPsp;
	
	@Column(name = "tipo_allegato")
	@Enumerated(EnumType.STRING)
	private TipoAllegato tipoAllegato;
	
	@Lob
	@Type(type="org.hibernate.type.BinaryType")
	@Column(name = "allegato")
	private byte[] allegato;
	
	@Column(name = "stato")
	@Enumerated(EnumType.STRING)
	private Stato stato;
	
	@Column(name = "tipo", nullable = false)
	@Enumerated(EnumType.STRING)
	private TipoPagamento tipo;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_rpt", nullable = false)
	private RptEntity rpt;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_singolo_versamento", nullable = false)
	private SingoloVersamentoEntity singoloVersamento;
}

/*
 CREATE SEQUENCE seq_pagamenti start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE pagamenti
(
	cod_dominio VARCHAR(35) NOT NULL,
	iuv VARCHAR(35) NOT NULL,
	indice_dati INT NOT NULL DEFAULT 1,
	importo_pagato DOUBLE PRECISION NOT NULL,
	data_acquisizione TIMESTAMP NOT NULL,
	iur VARCHAR(35) NOT NULL,
	data_pagamento TIMESTAMP NOT NULL,
	commissioni_psp DOUBLE PRECISION,
	-- Valori possibili:\nES: Esito originario\nBD: Marca da Bollo
	tipo_allegato VARCHAR(2),
	allegato BYTEA,
	data_acquisizione_revoca TIMESTAMP,
	causale_revoca VARCHAR(140),
	dati_revoca VARCHAR(140),
	importo_revocato DOUBLE PRECISION,
	esito_revoca VARCHAR(140),
	dati_esito_revoca VARCHAR(140),
	stato VARCHAR(35),
	tipo VARCHAR(35) NOT NULL,
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_pagamenti') NOT NULL,
	id_rpt BIGINT,
	id_singolo_versamento BIGINT,
	id_rr BIGINT,
	id_incasso BIGINT,
	-- fk/pk keys constraints
	CONSTRAINT fk_pag_id_rpt FOREIGN KEY (id_rpt) REFERENCES rpt(id),
	CONSTRAINT fk_pag_id_singolo_versamento FOREIGN KEY (id_singolo_versamento) REFERENCES singoli_versamenti(id),
	CONSTRAINT fk_pag_id_rr FOREIGN KEY (id_rr) REFERENCES rr(id),
	CONSTRAINT fk_pag_id_incasso FOREIGN KEY (id_incasso) REFERENCES incassi(id),
	CONSTRAINT pk_pagamenti PRIMARY KEY (id)
);

-- index
CREATE INDEX idx_pag_fk_rpt ON pagamenti (id_rpt);
CREATE INDEX idx_pag_fk_sng ON pagamenti (id_singolo_versamento);
CREATE UNIQUE INDEX idx_pag_id_riscossione ON pagamenti (cod_dominio, iuv, iur, indice_dati);
ALTER TABLE pagamenti ADD CONSTRAINT unique_pag_id_riscossione UNIQUE USING INDEX idx_pag_id_riscossione;
-- L'esecuzione viene completata con esito: NOTICE:  ALTER TABLE / ADD CONSTRAINT USING INDEX will rename index "idx_pag_id_riscossione" to "unique_pag_id_riscossione"

 
 */
