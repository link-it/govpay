-- 04/02/2026 Nuova tabella configurazione connettore maggioli
CREATE TABLE jppa_config
(
	cod_dominio VARCHAR(35) NOT NULL COMMENT 'Identificativo del dominio',
	cod_connettore VARCHAR(255) COMMENT 'Identificativo del connettore',
	abilitato BOOLEAN NOT NULL COMMENT 'Indicazione se e\' abilitato ad operare',
	data_ultima_rt DATETIME(3) DEFAULT 0 COMMENT 'Data ultima RT notificata correttamente',
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT COMMENT 'Identificativo fisico',
	id_dominio BIGINT NOT NULL COMMENT 'Riferimento al dominio',
	-- unique constraints
	CONSTRAINT unique_jppa_config_1 UNIQUE (id_dominio),
	-- fk/pk keys constraints
	CONSTRAINT fk_jpc_1 FOREIGN KEY (id_dominio) REFERENCES domini(id),
	CONSTRAINT pk_jppa_config PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs ROW_FORMAT DYNAMIC;

-- index
CREATE UNIQUE INDEX index_jppa_config_1 ON jppa_config (cod_dominio);

-- Migrazione dati esistenti da domini a jppa_config
INSERT INTO jppa_config (cod_dominio, cod_connettore, abilitato, id_dominio)
SELECT d.cod_dominio, d.cod_connettore_maggioli_jppa, TRUE, d.id
FROM domini d
WHERE d.cod_connettore_maggioli_jppa IS NOT NULL;

-- Rimozione colonna cod_connettore_maggioli_jppa da domini
ALTER TABLE domini DROP COLUMN cod_connettore_maggioli_jppa;

-- 10/02/2026 Nuova colonna esegui_recupero_rt su rendicontazioni
ALTER TABLE rendicontazioni ADD COLUMN esegui_recupero_rt BOOLEAN;
UPDATE rendicontazioni SET esegui_recupero_rt = TRUE;
ALTER TABLE rendicontazioni MODIFY COLUMN esegui_recupero_rt BOOLEAN NOT NULL;
