-- 04/02/2026 Nuova tabella configurazione connettore maggioli
CREATE TABLE jppa_config
(
	cod_dominio VARCHAR(35) NOT NULL,
	cod_connettore VARCHAR(255),
	abilitato BIT NOT NULL,
	data_ultima_rt DATETIME2,
	-- fk/pk columns
	id BIGINT IDENTITY,
	id_dominio BIGINT NOT NULL,
	-- unique constraints
	CONSTRAINT unique_jppa_config_1 UNIQUE (id_dominio),
	-- fk/pk keys constraints
	CONSTRAINT fk_jpc_id_dominio FOREIGN KEY (id_dominio) REFERENCES domini(id),
	CONSTRAINT pk_jppa_config PRIMARY KEY (id)
);

-- index
CREATE UNIQUE INDEX index_jppa_config_1 ON jppa_config (cod_dominio);

-- Migrazione dati esistenti da domini a jppa_config
INSERT INTO jppa_config (cod_dominio, cod_connettore, abilitato, id_dominio)
SELECT d.cod_dominio, d.cod_connettore_maggioli_jppa, 1, d.id
FROM domini d
WHERE d.cod_connettore_maggioli_jppa IS NOT NULL;

-- Rimozione colonna cod_connettore_maggioli_jppa da domini
ALTER TABLE domini DROP COLUMN cod_connettore_maggioli_jppa;

-- 10/02/2026 Nuova colonna esegui_recupero_rt su rendicontazioni
ALTER TABLE rendicontazioni ADD esegui_recupero_rt BIT;
UPDATE rendicontazioni SET esegui_recupero_rt = 1;
ALTER TABLE rendicontazioni ALTER COLUMN esegui_recupero_rt BIT NOT NULL;

-- 11/02/2026 Nuova colonna cod_connettore_backoffice_ec su intermediari
ALTER TABLE intermediari ADD cod_connettore_backoffice_ec VARCHAR(35);

-- 04/03/2026 Nuova colonna notifica_inviata su rendicontazioni
ALTER TABLE rendicontazioni ADD notifica_inviata BIT;
UPDATE rendicontazioni SET notifica_inviata = 0;
ALTER TABLE rendicontazioni ALTER COLUMN notifica_inviata BIT NOT NULL;
