-- 04/08/2022 Aggiunta colonna versione alla tabella stazioni
ALTER TABLE stazioni ADD versione VARCHAR2(35 CHAR);
UPDATE stazioni SET versione = 'V1' WHERE versione IS NULL;
ALTER TABLE stazioni MODIFY (versione NOT NULL);