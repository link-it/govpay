ALTER TABLE versamenti ADD cod_lotto VARCHAR(35);
ALTER TABLE versamenti ADD cod_versamento_lotto VARCHAR(35);
ALTER TABLE versamenti ADD cod_anno_tributario VARCHAR(35);
ALTER TABLE pagamenti ADD data_acquisizione TIMESTAMP default CURRENT_TIMESTAMP NOT NULL; 
ALTER TABLE pagamenti ADD data_acquisizione_revoca TIMESTAMP;
--GP-288 Aggiungere colonna Note e BundleKey
ALTER TABLE versamenti ADD cod_bundlekey VARCHAR(256);
ALTER TABLE singoli_versamenti ADD note VARCHAR(512);


CREATE SEQUENCE seq_tipi_tributo MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE tipi_tributo
(
        cod_tributo VARCHAR(255) NOT NULL,
        descrizione VARCHAR(255),
        -- fk/pk columns
        id NUMBER NOT NULL,
        -- unique constraints
        CONSTRAINT unique_tipi_tributo_1 UNIQUE (cod_tributo),
        -- fk/pk keys constraints
        CONSTRAINT pk_tipi_tributo PRIMARY KEY (id)
);

CREATE TRIGGER trg_tipi_tributo
BEFORE
insert on tipi_tributo
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_tipi_tributo.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;
/

-- index
-- CREATE INDEX index_tipi_tributo_1 ON tipi_tributo (cod_tributo);

INSERT INTO tipi_tributo (cod_tributo, descrizione) (SELECT cod_tributo, max(descrizione) FROM tributi GROUP BY cod_tributo);
ALTER TABLE tributi ADD id_tipo_tributo NUMBER;
UPDATE tributi SET id_tipo_tributo = (SELECT id FROM tipi_tributo WHERE tipi_tributo.cod_tributo=tributi.cod_tributo);
ALTER TABLE tributi MODIFY (id_tipo_tributo NOT NULL);

ALTER TABLE tributi DROP CONSTRAINT unique_tributi_1;
ALTER TABLE tributi ADD CONSTRAINT unique_tributi_1 UNIQUE (id_dominio,id_tipo_tributo);
ALTER TABLE tributi ADD CONSTRAINT fk_tributi_3 FOREIGN KEY (id_tipo_tributo) REFERENCES tipi_tributo(id) ON DELETE CASCADE;

ALTER TABLE tributi DROP COLUMN cod_tributo;
ALTER TABLE tributi DROP COLUMN descrizione;

CREATE SEQUENCE seq_acl MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE acl
(
	cod_tipo VARCHAR(1) NOT NULL,
	cod_servizio VARCHAR(1) NOT NULL,
	-- fk/pk columns
	id NUMBER NOT NULL,
	id_applicazione NUMBER,
	id_portale NUMBER,
	id_operatore NUMBER,
	id_dominio NUMBER,
	id_tipo_tributo NUMBER,
	-- fk/pk keys constraints
	CONSTRAINT fk_acl_1 FOREIGN KEY (id_applicazione) REFERENCES applicazioni(id) ON DELETE CASCADE,
	CONSTRAINT fk_acl_2 FOREIGN KEY (id_portale) REFERENCES portali(id) ON DELETE CASCADE,
	CONSTRAINT fk_acl_3 FOREIGN KEY (id_operatore) REFERENCES operatori(id) ON DELETE CASCADE,
	CONSTRAINT fk_acl_4 FOREIGN KEY (id_dominio) REFERENCES domini(id) ON DELETE CASCADE,
	CONSTRAINT fk_acl_5 FOREIGN KEY (id_tipo_tributo) REFERENCES tipi_tributo(id) ON DELETE CASCADE,
	CONSTRAINT pk_acl PRIMARY KEY (id)
);

CREATE TRIGGER trg_acl
BEFORE
insert on acl
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_acl.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;
/



DROP TABLE portali_applicazioni;
DROP SEQUENCE seq_portali_applicazioni;

DROP TABLE applicazioni_domini;
DROP SEQUENCE seq_applicazioni_domini;

DROP TABLE applicazioni_tributi;
DROP SEQUENCE seq_applicazioni_tributi;

DROP TABLE operatori_applicazioni;
DROP SEQUENCE seq_operatori_applicazioni;

DROP TABLE operatori_portali;
DROP SEQUENCE seq_operatori_portali;

DROP TABLE operatori_uo;
DROP SEQUENCE seq_operatori_uo;

INSERT INTO acl (cod_tipo, cod_servizio, id_applicazione) (SELECT 'D' as cod_tipo, 'V' as cod_servizio, id from applicazioni);
INSERT INTO acl (cod_tipo, cod_servizio, id_applicazione) (SELECT 'T' as cod_tipo, 'V' as cod_servizio, id from applicazioni);

INSERT INTO acl (cod_tipo, cod_servizio, id_portale) (SELECT 'D' as cod_tipo, 'O' as cod_servizio, id from portali);
INSERT INTO acl (cod_tipo, cod_servizio, id_portale) (SELECT 'T' as cod_tipo, 'O' as cod_servizio, id from portali);

--GP-316
ALTER TABLE fr ADD data_acquisizione TIMESTAMP default CURRENT_TIMESTAMP NOT NULL;
ALTER TABLE portali ADD trusted NUMBER;
UPDATE portali set trusted = 0;
ALTER TABLE portali MODIFY (trusted NOT NULL);

--GP-281
ALTER TABLE applicazioni ADD versione VARCHAR(10) DEFAULT '2.1' NOT NULL;
ALTER TABLE portali ADD versione VARCHAR(10) DEFAULT '2.1' NOT NULL;

--GP-341
ALTER TABLE pagamenti ADD COLUMN iban_accredito VARCHAR(255);
