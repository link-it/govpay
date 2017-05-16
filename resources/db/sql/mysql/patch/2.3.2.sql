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

--DEV-238
CREATE TABLE incassi
(
       trn VARCHAR(35) NOT NULL,
       cod_dominio VARCHAR(35) NOT NULL,
       causale VARCHAR(512) NOT NULL,
       importo DOUBLE NOT NULL,
       data_valuta TIMESTAMP DEFAULT CURRENT_TIMESTAMP(3),
       data_contabile TIMESTAMP DEFAULT  CURRENT_TIMESTAMP(3),
       -- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
       data_ora_incasso TIMESTAMP(3) NOT NULL DEFAULT  CURRENT_TIMESTAMP(3),
       nome_dispositivo VARCHAR(512),
       -- fk/pk columns
       id BIGINT AUTO_INCREMENT,
       id_applicazione BIGINT,
       -- fk/pk keys constraints
       CONSTRAINT fk_incassi_1 FOREIGN KEY (id_applicazione) REFERENCES applicazioni(id) ON DELETE CASCADE,
       CONSTRAINT pk_incassi PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;

ALTER TABLE pagamenti ADD COLUMN stato VARCHAR(35);
UPDATE pagamenti set stato = 'PAGATO';
ALTER TABLE pagamenti ADD COLUMN id_incasso BIGINT;
ALTER TABLE pagamenti ADD CONSTRAINT fk_pagamenti_4 FOREIGN KEY (id_incasso) REFERENCES incassi(id) ON DELETE CASCADE; 
