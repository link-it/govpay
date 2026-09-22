-- =============================================================================
-- Svecchiamento TRACCIATI - MySQL/MariaDB
--
-- Elimina i tracciati completati da piu' di retention_tracciati giorni, con le
-- operazioni e gli eventi che li referenziano, nell'ordine imposto dalle chiavi
-- esterne.
--
-- Uso: mysql -h <host> -u <utente> -D <database> < tracciati.sql
--
-- Il valore qui sotto e' il default. svecchiamento-db.sh lo sostituisce quando
-- gli si passa --retention-tracciati.
-- =============================================================================

SET @retention_tracciati = 7;

SET @end_tracciati = CURRENT_DATE - INTERVAL @retention_tracciati DAY;

SELECT '--- Svecchiamento TRACCIATI ---' AS stato;
SELECT CONCAT('Retention: ', @retention_tracciati, ' giorni') AS stato;

START TRANSACTION;

SELECT 'Cancellazione eventi collegati ai tracciati...' AS stato;
DELETE FROM eventi WHERE id_tracciato IN (SELECT id FROM tracciati WHERE data_completamento < @end_tracciati);

SELECT 'Cancellazione operazioni collegate ai tracciati...' AS stato;
DELETE FROM operazioni WHERE id_tracciato IN (SELECT id FROM tracciati WHERE data_completamento < @end_tracciati);

SELECT 'Cancellazione tracciati...' AS stato;
DELETE FROM tracciati WHERE data_completamento < @end_tracciati;

COMMIT;
