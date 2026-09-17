-- =============================================================================
-- Script di svecchiamento dati GovPay - SQL Server
--
-- Uso: sqlcmd -S <host> -U <user> -d <database> -i svecchiamento.sql
--
-- Parametri (impostare prima dell'esecuzione):
-- =============================================================================

-- Retention tracciati (in giorni)
DECLARE @retention_tracciati INT = 7;

-- Retention eventi del giornale (in giorni)
DECLARE @retention_eventi INT = 90;

-- =============================================================================

DECLARE @end_tracciati DATE = DATEADD(DAY, -@retention_tracciati, GETDATE());
DECLARE @end_eventi DATE = DATEADD(DAY, -@retention_eventi, GETDATE());

PRINT '=== Avvio svecchiamento GovPay ===';
SELECT GETDATE() AS inizio_svecchiamento;

PRINT 'Retention tracciati: ' + CAST(@retention_tracciati AS VARCHAR) + ' giorni';
PRINT 'Retention eventi: ' + CAST(@retention_eventi AS VARCHAR) + ' giorni';

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

-- =====================
-- EVENTI
-- =====================
-- Gli eventi collegati ai tracciati sono gia' stati eliminati sopra, insieme ai
-- tracciati che li referenziavano. Qui si svecchia il resto del giornale per
-- eta', che e' la parte che cresce di piu'. La condizione e' sulla colonna data,
-- coperta dall'indice idx_evt_data. Nessuna tabella referenzia eventi, quindi la
-- cancellazione non e' vincolata da chiavi esterne.
PRINT '';
PRINT '--- Svecchiamento EVENTI ---';

PRINT 'Cancellazione eventi...';
DELETE FROM eventi WHERE data < @end_eventi;

COMMIT TRANSACTION;

PRINT '';
PRINT '=== Svecchiamento completato ===';
SELECT GETDATE() AS fine_svecchiamento;
