ALTER TABLE versamenti ADD COLUMN cod_lotto VARCHAR(35);
ALTER TABLE versamenti ADD COLUMN cod_versamento_lotto VARCHAR(35);
ALTER TABLE versamenti ADD COLUMN cod_anno_tributario VARCHAR(35);
-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
ALTER TABLE pagamenti ADD COLUMN data_acquisizione TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3);
-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
ALTER TABLE pagamenti ADD COLUMN data_acquisizione_revoca TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP(3);
--GP-288 Aggiungere colonna Note e BundleKey
ALTER TABLE versamenti ADD COLUMN cod_bundlekey VARCHAR(256);
ALTER TABLE singoli_versamenti ADD COLUMN note VARCHAR(512);

--GP-295 Supporto ACL e nuova struttura tributi
CREATE TABLE tipi_tributo
(
        cod_tributo VARCHAR(255) NOT NULL,
        descrizione VARCHAR(255),
        -- fk/pk columns
        id BIGINT AUTO_INCREMENT,
        -- unique constraints
        CONSTRAINT unique_tipi_tributo_1 UNIQUE (cod_tributo),
        -- fk/pk keys constraints
        CONSTRAINT pk_tipi_tributo PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;

CREATE INDEX index_tipi_tributo_1 ON tipi_tributo (cod_tributo);

INSERT INTO tipi_tributo (cod_tributo, descrizione) (SELECT cod_tributo, max(descrizione) FROM tributi GROUP BY cod_tributo);
ALTER TABLE tributi ADD COLUMN id_tipo_tributo BIGINT;
UPDATE tributi SET id_tipo_tributo = (SELECT id FROM tipi_tributo WHERE tipi_tributo.cod_tributo=tributi.cod_tributo);
ALTER TABLE tributi MODIFY id_tipo_tributo BIGINT NOT NULL;

ALTER TABLE tributi DROP INDEX unique_tributi_1;
ALTER TABLE tributi ADD CONSTRAINT unique_tributi_1 UNIQUE (id_dominio,id_tipo_tributo);
ALTER TABLE tributi ADD CONSTRAINT fk_tributi_3 FOREIGN KEY (id_tipo_tributo) REFERENCES tipi_tributo(id) ON DELETE CASCADE;

ALTER TABLE tributi DROP COLUMN cod_tributo;
ALTER TABLE tributi DROP COLUMN descrizione;

CREATE TABLE acl
(
	cod_tipo VARCHAR(1) NOT NULL,
	cod_servizio VARCHAR(1) NOT NULL,
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT,
	id_applicazione BIGINT,
	id_portale BIGINT,
	id_operatore BIGINT,
	id_dominio BIGINT,
	id_tipo_tributo BIGINT,
	-- fk/pk keys constraints
	CONSTRAINT fk_acl_1 FOREIGN KEY (id_applicazione) REFERENCES applicazioni(id) ON DELETE CASCADE,
	CONSTRAINT fk_acl_2 FOREIGN KEY (id_portale) REFERENCES portali(id) ON DELETE CASCADE,
	CONSTRAINT fk_acl_3 FOREIGN KEY (id_operatore) REFERENCES operatori(id) ON DELETE CASCADE,
	CONSTRAINT fk_acl_4 FOREIGN KEY (id_dominio) REFERENCES domini(id) ON DELETE CASCADE,
	CONSTRAINT fk_acl_5 FOREIGN KEY (id_tipo_tributo) REFERENCES tipi_tributo(id) ON DELETE CASCADE,
	CONSTRAINT pk_acl PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;


DROP TABLE portali_applicazioni;
DROP TABLE applicazioni_domini;
DROP TABLE applicazioni_tributi;
DROP TABLE operatori_applicazioni;
DROP TABLE operatori_portali;
DROP TABLE operatori_uo;

INSERT INTO acl (cod_tipo, cod_servizio, id_applicazione) (SELECT 'D' as cod_tipo, 'V' as cod_servizio, id from applicazioni);
INSERT INTO acl (cod_tipo, cod_servizio, id_applicazione) (SELECT 'T' as cod_tipo, 'V' as cod_servizio, id from applicazioni);

INSERT INTO acl (cod_tipo, cod_servizio, id_portale) (SELECT 'D' as cod_tipo, 'O' as cod_servizio, id from applicazioni);
INSERT INTO acl (cod_tipo, cod_servizio, id_portale) (SELECT 'T' as cod_tipo, 'O' as cod_servizio, id from applicazioni);

--GP-316
-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
ALTER TABLE fr ADD COLUMN data_acquisizione TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3);
ALTER TABLE portali ADD COLUMN trusted BOOLEAN NOT NULL DEFAULT FALSE;

--GP-281
ALTER TABLE applicazioni ADD COLUMN versione VARCHAR(10) NOT NULL DEFAULT '2.1';
ALTER TABLE portali ADD COLUMN versione VARCHAR(10) NOT NULL DEFAULT '2.1';

--GP-341
ALTER TABLE pagamenti ADD COLUMN iban_accredito VARCHAR(255);
