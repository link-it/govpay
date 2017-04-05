--GP-513
CREATE SEQUENCE seq_batch start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE batch
(
       cod_batch VARCHAR(255) NOT NULL,
       nodo INT,
       inizio TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
       aggiornamento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
       -- fk/pk columns
       id BIGINT DEFAULT nextval('seq_batch') NOT NULL,
       -- unique constraints
       CONSTRAINT unique_batch_1 UNIQUE (cod_batch),
       -- fk/pk keys constraints
       CONSTRAINT pk_batch PRIMARY KEY (id)
);


