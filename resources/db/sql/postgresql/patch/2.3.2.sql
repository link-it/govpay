--GP-513
CREATE SEQUENCE seq_batch start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE batch
(
       cod_batch VARCHAR(255) NOT NULL,
       nodo INT,
       inizio TIMESTAMP,
       aggiornamento TIMESTAMP,
       -- fk/pk columns
       id BIGINT DEFAULT nextval('seq_batch') NOT NULL,
       -- unique constraints
       CONSTRAINT unique_batch_1 UNIQUE (cod_batch),
       -- fk/pk keys constraints
       CONSTRAINT pk_batch PRIMARY KEY (id)
);

--DEV-238
CREATE SEQUENCE seq_incassi start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE incassi
(
       trn VARCHAR(35) NOT NULL,
       cod_dominio VARCHAR(35) NOT NULL,
       causale VARCHAR(512) NOT NULL,
       importo DOUBLE PRECISION NOT NULL,
       cod_applicazione VARCHAR(35) NOT NULL,
       data_valuta DATE,
       data_contabile DATE,
       data_ora_incasso TIMESTAMP NOT NULL,
       nome_dispositivo VARCHAR(512),
       -- fk/pk columns
       id BIGINT DEFAULT nextval('seq_incassi') NOT NULL,
       -- fk/pk keys constraints
       CONSTRAINT pk_incassi PRIMARY KEY (id)
);

ALTER TABLE pagamenti ADD COLUMN stato VARCHAR(35);
ALTER TABLE pagamenti ADD COLUMN id_incasso BIGINT;
ALTER TABLE pagamenti ADD CONSTRAINT fk_pagamenti_4 FOREIGN KEY (id_incasso) REFERENCES incassi(id) ON DELETE CASCADE; 
