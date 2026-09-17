-- =============================================================================
-- Script di svecchiamento dati GovPay - MySQL/MariaDB
--
-- Uso: mysql -h <host> -u <user> -p <database> < svecchiamento.sql
--
-- Parametri (impostare prima dell'esecuzione):
-- =============================================================================

-- Retention tracciati (in giorni)
SET @retention_tracciati = 7;

-- Retention eventi del giornale (in giorni)
SET @retention_eventi = 90;

-- =============================================================================

SET @end_tracciati = CURRENT_DATE - INTERVAL @retention_tracciati DAY;
SET @end_eventi = CURRENT_DATE - INTERVAL @retention_eventi DAY;

SELECT NOW() AS inizio_svecchiamento;
SELECT CONCAT('Retention tracciati: ', @retention_tracciati, ' giorni') AS parametri;
SELECT CONCAT('Retention eventi: ', @retention_eventi, ' giorni') AS parametri;

-- =====================
-- TRACCIATI
-- =====================

SELECT '--- Svecchiamento TRACCIATI ---' AS stato;

SELECT 'Cancellazione eventi collegati ai tracciati...' AS stato;
DELETE FROM eventi WHERE id_tracciato IN (SELECT id FROM tracciati WHERE data_completamento < @end_tracciati);

SELECT 'Cancellazione operazioni collegate ai tracciati...' AS stato;
DELETE FROM operazioni WHERE id_tracciato IN (SELECT id FROM tracciati WHERE data_completamento < @end_tracciati);

SELECT 'Cancellazione tracciati...' AS stato;
DELETE FROM tracciati WHERE data_completamento < @end_tracciati;

-- =====================
-- EVENTI
-- =====================
-- Gli eventi collegati ai tracciati sono gia' stati eliminati sopra, insieme ai
-- tracciati che li referenziavano. Qui si svecchia il resto del giornale per
-- eta', che e' la parte che cresce di piu'. La condizione e' sulla colonna data,
-- coperta dall'indice idx_evt_data. Nessuna tabella referenzia eventi, quindi la
-- cancellazione non e' vincolata da chiavi esterne.

SELECT '--- Svecchiamento EVENTI ---' AS stato;

SELECT 'Cancellazione eventi...' AS stato;
DELETE FROM eventi WHERE data < @end_eventi;

SELECT NOW() AS fine_svecchiamento;
SELECT '=== Svecchiamento completato ===' AS stato;
