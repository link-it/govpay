-- 22/05/2019 Configurazione Giornale Eventi
UPDATE configurazione SET giornale_eventi = '{"apiEnte":{"letture":{"log":"MAI","dump":"MAI"},"scritture":{"log":"SOLO_ERRORE","dump":"SOLO_ERRORE"}},"apiPagamento":{"letture":{"log":"MAI","dump":"MAI"},"scritture":{"log":"SOLO_ERRORE","dump":"SOLO_ERRORE"}},"apiRagioneria":{"letture":{"log":"MAI","dump":"MAI"},"scritture":{"log":"SOLO_ERRORE","dump":"SOLO_ERRORE"}},"apiBackoffice":{"letture":{"log":"MAI","dump":"MAI"},"scritture":{"log":"SOLO_ERRORE","dump":"SOLO_ERRORE"}},"apiPagoPA":{"letture":{"log":"SEMPRE","dump":"SOLO_ERRORE"},"scritture":{"log":"SEMPRE","dump":"SOLO_ERRORE"}},"apiPendenze":{"letture":{"log":"MAI","dump":"MAI"},"scritture":{"log":"SOLO_ERRORE","dump":"SOLO_ERRORE"}}}';

-- 24/05/2019 nuova tabella eventi
DROP TABLE eventi;

CREATE TABLE eventi
(
	componente VARCHAR(35),
	ruolo VARCHAR(1),
	categoria_evento VARCHAR(1),
	tipo_evento VARCHAR(70),
	sottotipo_evento VARCHAR(35),
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
	data TIMESTAMP(3) DEFAULT 0,
	intervallo BIGINT,
	esito VARCHAR(4),
	sottotipo_esito VARCHAR(35),
	dettaglio_esito LONGTEXT,
	parametri_richiesta MEDIUMBLOB,
	parametri_risposta MEDIUMBLOB,
	dati_pago_pa LONGTEXT,
	cod_versamento_ente VARCHAR(35),
	cod_applicazione VARCHAR(35),
	iuv VARCHAR(35),
	ccp VARCHAR(35),
	cod_dominio VARCHAR(35),
	id_sessione VARCHAR(35),
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT,
	-- fk/pk keys constraints
	CONSTRAINT pk_eventi PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;


-- 18/06/2019 Configurazione avanzata dei tipi pendenza
ALTER TABLE tipi_versamento DROP COLUMN json_schema;
ALTER TABLE tipi_versamento DROP COLUMN dati_allegati;
ALTER TABLE tipi_versamento ADD COLUMN form_tipo VARCHAR(35);
ALTER TABLE tipi_versamento ADD COLUMN form_definizione LONGTEXT;
ALTER TABLE tipi_versamento ADD COLUMN validazione_definizione LONGTEXT;
ALTER TABLE tipi_versamento ADD COLUMN trasformazione_tipo VARCHAR(35);
ALTER TABLE tipi_versamento ADD COLUMN trasformazione_definizione LONGTEXT;
ALTER TABLE tipi_versamento ADD COLUMN cod_applicazione VARCHAR(35);
ALTER TABLE tipi_versamento ADD COLUMN promemoria_avviso BOOLEAN NOT NULL DEFAULT false;
ALTER TABLE tipi_versamento ADD COLUMN promemoria_oggetto LONGTEXT;
ALTER TABLE tipi_versamento ADD COLUMN promemoria_messaggio LONGTEXT;

ALTER TABLE tipi_vers_domini DROP COLUMN json_schema;
ALTER TABLE tipi_vers_domini DROP COLUMN dati_allegati;
ALTER TABLE tipi_vers_domini ADD COLUMN form_tipo VARCHAR(35);
ALTER TABLE tipi_vers_domini ADD COLUMN form_definizione LONGTEXT;
ALTER TABLE tipi_vers_domini ADD COLUMN validazione_definizione LONGTEXT;
ALTER TABLE tipi_vers_domini ADD COLUMN trasformazione_tipo VARCHAR(35);
ALTER TABLE tipi_vers_domini ADD COLUMN trasformazione_definizione LONGTEXT;
ALTER TABLE tipi_vers_domini ADD COLUMN cod_applicazione VARCHAR(35);
ALTER TABLE tipi_vers_domini ADD COLUMN promemoria_avviso BOOLEAN;
ALTER TABLE tipi_vers_domini ADD COLUMN promemoria_oggetto LONGTEXT;
ALTER TABLE tipi_vers_domini ADD COLUMN promemoria_messaggio LONGTEXT;

-- 24/06/2019 Tabella per la spedizione dei promemoria via mail
CREATE TABLE promemoria
(
	tipo VARCHAR(16) NOT NULL,
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
	data_creazione TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
	stato VARCHAR(16) NOT NULL,
	descrizione_stato VARCHAR(255),
	debitore_email VARCHAR(256) NOT NULL,
	oggetto VARCHAR(512) NOT NULL,
	messaggio LONGTEXT NOT NULL,
	allega_avviso BOOLEAN NOT NULL DEFAULT false,
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
	data_aggiornamento_stato TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
	-- DATETIME invece che TIMESTAMP(3) per supportare la data di default 31-12-9999
	data_prossima_spedizione TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
	tentativi_spedizione BIGINT,
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT,
	id_versamento BIGINT NOT NULL,
	-- fk/pk keys constraints
	CONSTRAINT fk_ntf_id_versamento FOREIGN KEY (id_versamento) REFERENCES versamenti(id),
	CONSTRAINT pk_promemoria PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;

insert into sonde(nome, classe, soglia_warn, soglia_error) values ('spedizione-promemoria', 'org.openspcoop2.utils.sonde.impl.SondaBatch', 86400000, 172800000);


