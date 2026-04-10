-- =============================================================================
-- Script di svecchiamento dati GovPay - SQL Server
--
-- Uso: sqlcmd -S <host> -U <user> -d <database> -i svecchiamento.sql
--
-- Parametri (impostare prima dell'esecuzione):
-- =============================================================================

-- Retention tracciati (in giorni)
DECLARE @retention_tracciati INT = 7;

-- =============================================================================

DECLARE @end_tracciati DATE = DATEADD(DAY, -@retention_tracciati, GETDATE());

PRINT '=== Avvio svecchiamento GovPay ===';
SELECT GETDATE() AS inizio_svecchiamento;

PRINT 'Retention tracciati: ' + CAST(@retention_tracciati AS VARCHAR) + ' giorni';

-- =====================
-- TRACCIATI
-- =====================

PRINT '';
PRINT '--- Svecchiamento TRACCIATI ---';

BEGIN TRANSACTION;

PRINT 'Cancellazione eventi collegati ai tracciati...';
DELETE FROM eventi WHERE id_tracciato IN (SELECT id FROM tracciati WHERE data_completamento < @end_tracciati);

PRINT 'Cancellazione operazioni collegate ai tracciati...';
DELETE FROM operazioni WHERE id_tracciato IN (SELECT id FROM tracciati WHERE data_completamento < @end_tracciati);

PRINT 'Cancellazione tracciati...';
DELETE FROM tracciati WHERE data_completamento < @end_tracciati;

COMMIT TRANSACTION;

PRINT '';
PRINT '=== Svecchiamento completato ===';
SELECT GETDATE() AS fine_svecchiamento;
