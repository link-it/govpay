-- =============================================================================
-- Script di svecchiamento dati GovPay - MySQL/MariaDB
--
-- Uso: mysql -h <host> -u <user> -p <database> < svecchiamento.sql
--
-- Parametri (impostare prima dell'esecuzione):
-- =============================================================================

-- Retention tracciati (in giorni)
SET @retention_tracciati = 7;

-- =============================================================================

SET @end_tracciati = CURRENT_DATE - INTERVAL @retention_tracciati DAY;

SELECT NOW() AS inizio_svecchiamento;
SELECT CONCAT('Retention tracciati: ', @retention_tracciati, ' giorni') AS parametri;

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

SELECT NOW() AS fine_svecchiamento;
SELECT '=== Svecchiamento completato ===' AS stato;
