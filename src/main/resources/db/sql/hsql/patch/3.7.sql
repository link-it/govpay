-- 04/08/2022 Aggiunta colonna versione alla tabella stazioni
ALTER TABLE stazioni ADD versione VARCHAR(35);
UPDATE stazioni SET versione = 'V1' WHERE versione IS NULL;
ALTER TABLE stazioni ALTER COLUMN versione NOT NULL;