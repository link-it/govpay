-- =============================================================================
-- Svecchiamento AUDIT - MySQL/MariaDB
--
-- Elimina le registrazioni dell'audit delle operazioni degli operatori
-- (gp_audit) piu' vecchie di retention_audit giorni. Nessun'altra tabella le
-- referenzia.
--
-- Prima di essere cancellate, le righe vengono copiate nella tabella di
-- archivio gp_audit_aaaammgg, creata se non esiste e riempita nella stessa
-- transazione della cancellazione: l'archivio contiene esattamente le righe
-- cancellate. svecchiamento-db.sh sostituisce aaaammgg con la data
-- dell'esecuzione; eseguito da solo, lo script archivia nella tabella
-- gp_audit_aaaammgg. Salvare ed eliminare la tabella di archivio e' compito
-- del DBA.
--
-- Uso: mysql -h <host> -u <utente> -D <database> < audit.sql
--
-- Il valore qui sotto e' il default. svecchiamento-db.sh lo sostituisce quando
-- gli si passa --retention-audit.
-- =============================================================================

SET @retention_audit = 365;

SET @end_audit = CURRENT_DATE - INTERVAL @retention_audit DAY;

SELECT '--- Svecchiamento AUDIT ---' AS stato;
SELECT CONCAT('Retention: ', @retention_audit, ' giorni') AS stato;

SELECT 'Creazione della tabella di archivio...' AS stato;
CREATE TABLE IF NOT EXISTS gp_audit_aaaammgg LIKE gp_audit;

START TRANSACTION;

SELECT 'Archiviazione...' AS stato;
INSERT INTO gp_audit_aaaammgg SELECT * FROM gp_audit WHERE data < @end_audit;

SELECT 'Cancellazione...' AS stato;
DELETE FROM gp_audit WHERE data < @end_audit;

COMMIT;
