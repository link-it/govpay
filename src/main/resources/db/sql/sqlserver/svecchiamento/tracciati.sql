-- =============================================================================
-- Svecchiamento TRACCIATI - SQL Server
--
-- Elimina i tracciati completati da piu' di retention_tracciati mesi, con le
-- operazioni e gli eventi che li referenziano, nell'ordine imposto dalle chiavi
-- esterne.
--
-- Uso: sqlcmd -b -S <host> -U <utente> -d <database> -i tracciati.sql
--
-- Il valore qui sotto e' il default. svecchiamento-db.sh lo sostituisce quando
-- gli si passa --retention-tracciati.
-- =============================================================================

DECLARE @retention_tracciati INT = 1;

DECLARE @end_tracciati DATE = DATEADD(MONTH, -@retention_tracciati, GETDATE());

PRINT '--- Svecchiamento TRACCIATI ---';
PRINT 'Retention: ' + CAST(@retention_tracciati AS VARCHAR) + ' mesi';

BEGIN TRANSACTION;

PRINT 'Cancellazione eventi collegati ai tracciati...';
DELETE FROM eventi WHERE id_tracciato IN (SELECT id FROM tracciati WHERE data_completamento < @end_tracciati);

PRINT 'Cancellazione operazioni collegate ai tracciati...';
DELETE FROM operazioni WHERE id_tracciato IN (SELECT id FROM tracciati WHERE data_completamento < @end_tracciati);

PRINT 'Cancellazione tracciati...';
DELETE FROM tracciati WHERE data_completamento < @end_tracciati;

COMMIT TRANSACTION;
