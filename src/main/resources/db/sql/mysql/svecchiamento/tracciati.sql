-- =============================================================================
-- Svecchiamento TRACCIATI - MySQL/MariaDB
--
-- Elimina i tracciati completati da piu' di retention_tracciati giorni, con le
-- operazioni e gli eventi che li referenziano, nell'ordine imposto dalle chiavi
-- esterne.
--
-- Prima di essere cancellate, le righe vengono copiate in tabelle di archivio
-- con lo stesso nome e il suffisso _aaaammgg (operazioni, tracciati), create
-- se non esistono con CREATE TABLE ... LIKE, che copia indici e chiave
-- primaria ma non le chiavi esterne (CREATE TABLE ... SELECT non e' ammessa
-- con GTID attivo). Le righe sono copiate nella stessa transazione delle
-- cancellazioni: l'archivio contiene esattamente le righe cancellate.
-- svecchiamento-db.sh sostituisce aaaammgg con la data dell'esecuzione;
-- eseguito da solo, lo script archivia nelle tabelle _aaaammgg. Gli eventi non
-- vengono archiviati. Salvare ed eliminare le tabelle di archivio e' compito
-- del DBA.
--
-- Uso: mysql -h <host> -u <utente> -D <database> < tracciati.sql
--
-- Il valore qui sotto e' il default. svecchiamento-db.sh lo sostituisce quando
-- gli si passa --retention-tracciati.
-- =============================================================================

SET @retention_tracciati = 30;

SET @end_tracciati = CURRENT_DATE - INTERVAL @retention_tracciati DAY;

SELECT '--- Svecchiamento TRACCIATI ---' AS stato;
SELECT CONCAT('Retention: ', @retention_tracciati, ' giorni') AS stato;

SELECT 'Creazione delle tabelle di archivio...' AS stato;
CREATE TABLE IF NOT EXISTS operazioni_aaaammgg LIKE operazioni;
CREATE TABLE IF NOT EXISTS tracciati_aaaammgg LIKE tracciati;

START TRANSACTION;

SELECT 'Cancellazione eventi collegati ai tracciati...' AS stato;
DELETE FROM eventi WHERE id_tracciato IN (SELECT id FROM tracciati WHERE data_completamento < @end_tracciati);

SELECT 'Cancellazione operazioni collegate ai tracciati...' AS stato;
INSERT INTO operazioni_aaaammgg SELECT * FROM operazioni WHERE id_tracciato IN (SELECT id FROM tracciati WHERE data_completamento < @end_tracciati);
DELETE FROM operazioni WHERE id_tracciato IN (SELECT id FROM tracciati WHERE data_completamento < @end_tracciati);

SELECT 'Cancellazione tracciati...' AS stato;
INSERT INTO tracciati_aaaammgg SELECT * FROM tracciati WHERE data_completamento < @end_tracciati;
DELETE FROM tracciati WHERE data_completamento < @end_tracciati;

COMMIT;
