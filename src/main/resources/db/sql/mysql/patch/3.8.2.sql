-- 29/10/2025 Aggiunto indice sulla data alla tabella rendicontazioni
CREATE INDEX idx_rnd_data ON rendicontazioni (data);


-- 31/10/2025 Aggiunta colonna scarica_fr alla tabella domini
ALTER TABLE domini ADD COLUMN scarica_fr BOOLEAN;
UPDATE domini SET scarica_fr = TRUE;
ALTER TABLE domini MODIFY COLUMN scarica_fr BOOLEAN NOT NULL;
