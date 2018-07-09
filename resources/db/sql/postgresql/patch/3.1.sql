ALTER TABLE versamenti ALTER COLUMN tassonomia_avviso DROP NOT NULL;

ALTER TABLE pagamenti_portale ADD COLUMN ack BOOLEAN NOT NULL DEFAULT FALSE;
ALTER TABLE pagamenti_portale ADD COLUMN note TEXT;

ALTER TABLE pagamenti_portale ADD COLUMN tipo INT;
UPDATE pagamenti_portale set tipo=1;
ALTER TABLE pagamenti_portale ALTER COLUMN tipo SET NOT NULL;

ALTER TABLE pagamenti_portale ALTER COLUMN url_ritorno DROP NOT NULL;

DROP TABLE operazioni;
DROP SEQUENCE seq_operazioni;
DROP TABLE tracciati;  
DROP SEQUENCE seq_tracciati;
CREATE SEQUENCE seq_tracciati start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE tracciati
(
	tipo VARCHAR(10) NOT NULL,
	stato VARCHAR(10) NOT NULL,
	descrizione_stato VARCHAR(256),
	data_caricamento TIMESTAMP NOT NULL,
	data_completamento TIMESTAMP NOT NULL,
	bean_dati TEXT,
	file_name_richiesta VARCHAR(256),
	raw_richiesta BYTEA NOT NULL,
	file_name_esito VARCHAR(256),
	raw_esito BYTEA NOT NULL,
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_tracciati') NOT NULL,
	-- fk/pk keys constraints
	CONSTRAINT pk_tracciati PRIMARY KEY (id)
);

ALTER TABLE intermediari ADD COLUMN cod_connettore_ftp VARCHAR(35);
ALTER TABLE versamenti ADD COLUMN da_avvisare BOOLEAN NOT NULL DEFAULT false;
ALTER TABLE versamenti ADD COLUMN cod_avvisatura VARCHAR(20);
ALTER TABLE versamenti ADD COLUMN id_tracciato BIGINT;
ALTER TABLE versamenti ADD CONSTRAINT fk_vrs_id_tracciato FOREIGN KEY (id_tracciato) REFERENCES tracciati(id);

CREATE SEQUENCE seq_esiti_avvisatura start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;
CREATE TABLE esiti_avvisatura
(
	cod_dominio VARCHAR(35) NOT NULL,
	identificativo_avvisatura VARCHAR(20) NOT NULL,
	tipo_canale INT NOT NULL,
	cod_canale VARCHAR(35),
	data TIMESTAMP NOT NULL,
	cod_esito INT NOT NULL,
	descrizione_esito VARCHAR(140) NOT NULL,
	id BIGINT DEFAULT nextval('seq_esiti_avvisatura') NOT NULL,
	id_tracciato BIGINT NOT NULL,
	CONSTRAINT fk_sta_id_tracciato FOREIGN KEY (id_tracciato) REFERENCES tracciati(id),
	CONSTRAINT pk_esiti_avvisatura PRIMARY KEY (id)
);
