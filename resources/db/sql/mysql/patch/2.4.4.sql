--GP-557
CREATE TABLE avvisi
(
       cod_dominio VARCHAR(35) NOT NULL,
       iuv VARCHAR(35) NOT NULL,
       -- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
       data_creazione TIMESTAMP(3) NOT NULL DEFAULT 0,
       stato VARCHAR(255) NOT NULL,
       pdf MEDIUMBLOB,
       -- fk/pk columns
       id BIGINT AUTO_INCREMENT,
       -- check constraints
       CONSTRAINT chk_avvisi_1 CHECK (stato IN ('DA_STAMPARE','STAMPATO')),
       -- fk/pk keys constraints
       CONSTRAINT pk_avvisi PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;

-- index
CREATE INDEX index_avvisi_1 ON avvisi (cod_dominio,iuv);

insert into sonde(nome, classe, soglia_warn, soglia_error) values ('generazione-avvisi', 'org.openspcoop2.utils.sonde.impl.SondaBatch', 3600000, 21600000);

ALTER TABLE domini ADD COLUMN cbill VARCHAR(255);
ALTER TABLE uo ADD COLUMN uo_area VARCHAR(255);
ALTER TABLE uo ADD COLUMN uo_url_sito_web VARCHAR(255);
ALTER TABLE uo ADD COLUMN uo_email VARCHAR(255);
ALTER TABLE uo ADD COLUMN uo_pec VARCHAR(255);
