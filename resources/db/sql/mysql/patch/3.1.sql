ALTER TABLE versamenti MODIFY COLUMN tassonomia_avviso VARCHAR(35) NULL;

ALTER TABLE pagamenti_portale ADD COLUMN ack BOOLEAN NOT NULL DEFAULT FALSE;
ALTER TABLE pagamenti_portale ADD COLUMN note LONGTEXT;

ALTER TABLE pagamenti_portale ADD COLUMN tipo INT;
UPDATE pagamenti_portale set tipo=1;
ALTER TABLE pagamenti_portale MODIFY COLUMN tipo INT NOT NULL;

ALTER TABLE pagamenti_portale MODIFY COLUMN url_ritorno VARCHAR(1024) NULL;

ALTER TABLE intermediari ADD COLUMN cod_connettore_ftp VARCHAR(35);
CREATE TABLE tracciati
(
	tipo VARCHAR(10) NOT NULL,
	stato VARCHAR(122) NOT NULL,
	descrizione_stato VARCHAR(256),
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
	data_caricamento TIMESTAMP(3) NOT NULL DEFAULT 0,
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
	data_completamento TIMESTAMP(3) NOT NULL DEFAULT 0,
	bean_dati LONGTEXT,
	file_name_richiesta VARCHAR(256),
	raw_richiesta MEDIUMBLOB NOT NULL,
	file_name_esito VARCHAR(256),
	raw_esito MEDIUMBLOB NOT NULL,
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT,
	-- fk/pk keys constraints
	CONSTRAINT pk_tracciati PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;




ALTER TABLE intermediari ADD COLUMN da_avvisare BOOLEAN NOT NULL DEFAULT false;
ALTER TABLE intermediari ADD COLUMN cod_avvisatura VARCHAR(20);
ALTER TABLE intermediari ADD COLUMN id_tracciato BIGINT;
ALTER TABLE intermediari ADD CONSTRAINT fk_vrs_id_tracciato FOREIGN KEY (id_tracciato) REFERENCES tracciati(id);

CREATE TABLE esiti_avvisatura
(
	cod_dominio VARCHAR(35) NOT NULL,
	identificativo_avvisatura VARCHAR(20) NOT NULL,
	tipo_canale INT NOT NULL,
	cod_canale VARCHAR(35),
	data TIMESTAMP(3) NOT NULL DEFAULT 0,
	cod_esito INT NOT NULL,
	descrizione_esito VARCHAR(140) NOT NULL,
	id_tracciato BIGINT NOT NULL,
	CONSTRAINT fk_sta_id_tracciato FOREIGN KEY (id_tracciato) REFERENCES tracciati(id),
	CONSTRAINT pk_esiti_avvisatura PRIMARY KEY (id)
);
