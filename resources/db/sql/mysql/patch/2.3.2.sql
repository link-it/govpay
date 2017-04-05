--GP-513
CREATE TABLE batch
(
       cod_batch VARCHAR(255) NOT NULL,
       nodo INT,
       -- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
       inizio TIMESTAMP(3),
       -- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
       aggiornamento TIMESTAMP(3),
       -- fk/pk columns
       id BIGINT AUTO_INCREMENT,
       -- unique constraints
       CONSTRAINT unique_batch_1 UNIQUE (cod_batch),
       -- fk/pk keys constraints
       CONSTRAINT pk_batch PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;

-- index
CREATE INDEX index_batch_1 ON batch (cod_batch);

