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
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
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
@Table(name = "rpt", 
	uniqueConstraints = {
		@UniqueConstraint(name = "idx_rpt_id_transazione", columnNames = {"cod_dominio", "iuv", "ccp"})
		}, 
		indexes = {
			@Index(name="idx_rpt_cod_msg_richiesta", columnList = "cod_msg_richiesta"),
			@Index(name="idx_rpt_stato", columnList = "stato"),
			@Index(name="idx_rpt_fk_vrs", columnList = "id_versamento")
	})
public class RptEntity {
	
	public enum StatoRpt {
		RPT_PARCHEGGIATA_NODO,
		RPT_ATTIVATA,
		RPT_ERRORE_INVIO_A_NODO, 
		RPT_RICEVUTA_NODO, 
		RPT_RIFIUTATA_NODO, 
		RPT_ACCETTATA_NODO, 
		RPT_RIFIUTATA_PSP, 
		RPT_ACCETTATA_PSP, 
		RPT_ERRORE_INVIO_A_PSP, 
		RPT_INVIATA_A_PSP, 
		RPT_DECORSI_TERMINI,
		RPT_ANNULLATA,
		RT_RICEVUTA_NODO,
		RT_RIFIUTATA_NODO,
		RT_ACCETTATA_NODO,
		RT_ACCETTATA_PA,
		RT_RIFIUTATA_PA,
		RT_ESITO_SCONOSCIUTO_PA,
		RT_ERRORE_INVIO_A_PA,
		INTERNO_NODO,
		RPT_SCADUTA;
		
		public static StatoRpt toEnum(String s) {
			try {
				return StatoRpt.valueOf(s);
			} catch (IllegalArgumentException e) {
				return INTERNO_NODO;
			}
		}
	}
	
	public enum Versione {
		
		SANP_240,
		SANP_230;
		
		public static Versione toEnum(String s) {
			try {
				return Versione.valueOf(s);
			} catch (IllegalArgumentException e) {
				return SANP_230;
			}
		}
	}
	
	@Id
	@SequenceGenerator(name="seq_rpt",sequenceName="seq_rpt", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy= GenerationType.SEQUENCE, generator="seq_rpt")
	private Long id;
	
	@Column(name = "cod_carrello")
	private String codCarrello;
	
	@Column(name = "iuv", nullable = false)
	private String iuv;
	
	@Column(name = "ccp", nullable = false)
	private String ccp;
	
	@Column(name = "cod_dominio", nullable = false)
	private String codDominio;
	
	@Column(name = "cod_msg_richiesta", nullable = false)
	private String codMsgRichiesta;
	
	@Column(name = "data_msg_richiesta", nullable = false)
	private LocalDateTime dataMsgRichiesta;
	
	@Column(name = "stato")
	@Enumerated(EnumType.STRING)
	private StatoRpt stato;
	
	@Column(name = "descrizione_stato")
	private String descrizioneStato;
	
	@Column(name = "versione")
	@Enumerated(EnumType.STRING)
	private Versione versione;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_versamento", nullable = false)
	private VersamentoEntity versamento;
}
/*
CREATE SEQUENCE seq_rpt start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE rpt
(
	cod_carrello VARCHAR(35),
	iuv VARCHAR(35) NOT NULL,
	ccp VARCHAR(35) NOT NULL,
	cod_dominio VARCHAR(35) NOT NULL,
	-- Identificativo dell'RPT utilizzato come riferimento nell'RT
	cod_msg_richiesta VARCHAR(35) NOT NULL,
	-- Data di creazione dell'RPT
	data_msg_richiesta TIMESTAMP NOT NULL,
	-- Stato RPT secondo la codifica AgID
	stato VARCHAR(35) NOT NULL,
	descrizione_stato TEXT,
	cod_sessione VARCHAR(255),
	cod_sessione_portale VARCHAR(255),
	-- Indirizzo del portale psp a cui redirigere il cittadino per eseguire il pagamento
	psp_redirect_url VARCHAR(512),
	xml_rpt BYTEA NOT NULL,
	data_aggiornamento_stato TIMESTAMP NOT NULL,
	-- Indirizzo di ritorno al portale dell'ente al termine del pagamento
	callback_url TEXT,
	modello_pagamento VARCHAR(16),
	cod_msg_ricevuta VARCHAR(35),
	data_msg_ricevuta TIMESTAMP,
	-- Esito del pagamento:\n0: Eseguito\n1: Non eseguito\n2: Parzialmente eseguito\n3: Decorrenza\n4: Decorrenza Parziale
	cod_esito_pagamento INT,
	importo_totale_pagato DOUBLE PRECISION,
	xml_rt BYTEA,
	cod_canale VARCHAR(35),
	cod_psp VARCHAR(35),
	cod_intermediario_psp VARCHAR(35),
	tipo_versamento VARCHAR(4),
	tipo_identificativo_attestante VARCHAR(1),
	identificativo_attestante VARCHAR(35),
	denominazione_attestante VARCHAR(70),
	cod_stazione VARCHAR(35) NOT NULL,
	cod_transazione_rpt VARCHAR(36),
	cod_transazione_rt VARCHAR(36),
	stato_conservazione VARCHAR(35),
	descrizione_stato_cons VARCHAR(512),
	data_conservazione TIMESTAMP,
	bloccante BOOLEAN NOT NULL DEFAULT true,
	versione VARCHAR(35) NOT NULL,
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_rpt') NOT NULL,
	id_versamento BIGINT NOT NULL,
	id_pagamento_portale BIGINT,
	-- fk/pk keys constraints
	CONSTRAINT fk_rpt_id_versamento FOREIGN KEY (id_versamento) REFERENCES versamenti(id),
	CONSTRAINT fk_rpt_id_pagamento_portale FOREIGN KEY (id_pagamento_portale) REFERENCES pagamenti_portale(id),
	CONSTRAINT pk_rpt PRIMARY KEY (id)
);

-- index
CREATE INDEX idx_rpt_cod_msg_richiesta ON rpt (cod_msg_richiesta);
CREATE INDEX idx_rpt_stato ON rpt (stato);
CREATE INDEX idx_rpt_fk_vrs ON rpt (id_versamento);
CREATE INDEX idx_rpt_fk_prt ON rpt (id_pagamento_portale);
CREATE UNIQUE INDEX idx_rpt_id_transazione ON rpt (iuv, ccp, cod_dominio);
ALTER TABLE rpt ADD CONSTRAINT unique_rpt_id_transazione UNIQUE USING INDEX idx_rpt_id_transazione;
-- L'esecuzione viene completata con esito: NOTICE:  ALTER TABLE / ADD CONSTRAINT USING INDEX will rename index "idx_rpt_id_transazione" to "unique_rpt_id_transazione"


*/