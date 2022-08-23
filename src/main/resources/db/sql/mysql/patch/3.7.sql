-- 04/08/2022 Aggiunta colonna versione alla tabella stazioni
ALTER TABLE stazioni ADD COLUMN versione VARCHAR(35);
UPDATE stazioni SET versione = 'V1' WHERE versione IS NULL;
ALTER TABLE stazioni MODIFY COLUMN versione VARCHAR(35) NOT NULL;