-- 29/10/2025 Aggiunto indice sulla data alla tabella rendicontazioni
CREATE INDEX idx_rnd_data ON rendicontazioni (data);


-- 31/10/2025 Aggiunta colonna scarica_fr alla tabella domini
ALTER TABLE domini ADD scarica_fr NUMBER;
UPDATE domini SET scarica_fr = 1;
ALTER TABLE domini MODIFY scarica_fr NUMBER NOT NULL;
