-- 04/02/2026 Nuova tabella configurazione connettore maggioli
CREATE SEQUENCE seq_jppa_config start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE jppa_config
(
	cod_dominio VARCHAR(35) NOT NULL,
	cod_connettore VARCHAR(255),
	abilitato BOOLEAN NOT NULL,
	data_ultima_rt TIMESTAMP,
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_jppa_config') NOT NULL,
	id_dominio BIGINT NOT NULL,
	-- unique constraints
	CONSTRAINT unique_jppa_config_1 UNIQUE (id_dominio),
	-- fk/pk keys constraints
	CONSTRAINT fk_jpc_id_dominio FOREIGN KEY (id_dominio) REFERENCES domini(id),
	CONSTRAINT pk_jppa_config PRIMARY KEY (id)
);

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
ALTER TABLE rendicontazioni ALTER COLUMN esegui_recupero_rt SET NOT NULL;

-- 11/02/2026 Nuova colonna cod_connettore_backoffice_ec su intermediari
ALTER TABLE intermediari ADD COLUMN cod_connettore_backoffice_ec VARCHAR(35);
