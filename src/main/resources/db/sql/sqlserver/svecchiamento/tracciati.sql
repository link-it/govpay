-- =============================================================================
-- Svecchiamento TRACCIATI - SQL Server
--
-- Elimina i tracciati completati da piu' di retention_tracciati giorni, con le
-- operazioni e gli eventi che li referenziano, nell'ordine imposto dalle chiavi
-- esterne.
--
-- Prima di essere cancellate, le righe vengono copiate in tabelle di archivio
-- con lo stesso nome e il suffisso _aaaammgg (operazioni, tracciati), create
-- se non esistono, senza vincoli ne' indici, e nella stessa transazione delle
-- cancellazioni: l'archivio contiene esattamente le righe cancellate.
-- svecchiamento-db.sh sostituisce aaaammgg con la data dell'esecuzione;
-- eseguito da solo, lo script archivia nelle tabelle _aaaammgg. Gli eventi non
-- vengono archiviati. Salvare ed eliminare le tabelle di archivio e' compito
-- del DBA.
--
-- Uso: sqlcmd -b -S <host> -U <utente> -d <database> -i tracciati.sql
--
-- Il valore qui sotto e' il default. svecchiamento-db.sh lo sostituisce quando
-- gli si passa --retention-tracciati.
-- =============================================================================

DECLARE @retention_tracciati INT = 30;

DECLARE @end_tracciati DATE = DATEADD(DAY, -@retention_tracciati, GETDATE());

PRINT '--- Svecchiamento TRACCIATI ---';
PRINT 'Retention: ' + CAST(@retention_tracciati AS VARCHAR) + ' giorni';

PRINT 'Creazione delle tabelle di archivio...';
-- SELECT INTO copierebbe la proprieta' IDENTITY degli id, e l'archivio non
-- accetterebbe gli id originali: con UNION ALL la tabella nasce senza.
IF OBJECT_ID('operazioni_aaaammgg', 'U') IS NULL
    SELECT * INTO operazioni_aaaammgg FROM operazioni WHERE 1 = 0 UNION ALL SELECT * FROM operazioni WHERE 1 = 0;
IF OBJECT_ID('tracciati_aaaammgg', 'U') IS NULL
    SELECT * INTO tracciati_aaaammgg FROM tracciati WHERE 1 = 0 UNION ALL SELECT * FROM tracciati WHERE 1 = 0;

BEGIN TRANSACTION;

PRINT 'Cancellazione eventi collegati ai tracciati...';
DELETE FROM eventi WHERE id_tracciato IN (SELECT id FROM tracciati WHERE data_completamento < @end_tracciati);

PRINT 'Cancellazione operazioni collegate ai tracciati...';
INSERT INTO operazioni_aaaammgg SELECT * FROM operazioni WHERE id_tracciato IN (SELECT id FROM tracciati WHERE data_completamento < @end_tracciati);
DELETE FROM operazioni WHERE id_tracciato IN (SELECT id FROM tracciati WHERE data_completamento < @end_tracciati);

PRINT 'Cancellazione tracciati...';
INSERT INTO tracciati_aaaammgg SELECT * FROM tracciati WHERE data_completamento < @end_tracciati;
DELETE FROM tracciati WHERE data_completamento < @end_tracciati;

COMMIT TRANSACTION;
