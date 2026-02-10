-- 04/02/2026 Nuova tabella configurazione connettore maggioli
CREATE SEQUENCE seq_jppa_config MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE jppa_config
(
	cod_dominio VARCHAR2(35) NOT NULL,
	cod_connettore VARCHAR2(255 CHAR),
	abilitato NUMBER NOT NULL,
	data_ultima_rt TIMESTAMP,
	-- fk/pk columns
	id NUMBER NOT NULL,
	id_dominio NUMBER NOT NULL,
	-- unique constraints
	CONSTRAINT unique_jppa_config_1 UNIQUE (id_dominio),
	-- fk/pk keys constraints
	CONSTRAINT fk_jpc_id_dominio FOREIGN KEY (id_dominio) REFERENCES domini(id),
	CONSTRAINT pk_jppa_config PRIMARY KEY (id)
);

CREATE TRIGGER trg_jppa_config
BEFORE
insert on jppa_config
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_jppa_config.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;
/

-- Migrazione dati esistenti da domini a jppa_config
INSERT INTO jppa_config (cod_dominio, cod_connettore, abilitato, id_dominio)
SELECT d.cod_dominio, d.cod_connettore_maggioli_jppa, 1, d.id
FROM domini d
WHERE d.cod_connettore_maggioli_jppa IS NOT NULL;

-- Rimozione colonna cod_connettore_maggioli_jppa da domini
ALTER TABLE domini DROP COLUMN cod_connettore_maggioli_jppa;

-- 10/02/2026 Nuova colonna esegui_recupero_rt su rendicontazioni
ALTER TABLE rendicontazioni ADD esegui_recupero_rt NUMBER;
UPDATE rendicontazioni SET esegui_recupero_rt = 1;
ALTER TABLE rendicontazioni MODIFY esegui_recupero_rt NOT NULL;
