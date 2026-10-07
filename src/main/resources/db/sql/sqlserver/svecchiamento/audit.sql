-- =============================================================================
-- Svecchiamento AUDIT - SQL Server
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
-- Uso: sqlcmd -b -S <host> -U <utente> -d <database> -i audit.sql
--
-- Il valore qui sotto e' il default. svecchiamento-db.sh lo sostituisce quando
-- gli si passa --retention-audit.
-- =============================================================================

DECLARE @retention_audit INT = 365;

DECLARE @end_audit DATE = DATEADD(DAY, -@retention_audit, GETDATE());

PRINT '--- Svecchiamento AUDIT ---';
PRINT 'Retention: ' + CAST(@retention_audit AS VARCHAR) + ' giorni';

PRINT 'Creazione della tabella di archivio...';
-- SELECT INTO copierebbe la proprieta' IDENTITY degli id, e l'archivio non
-- accetterebbe gli id originali: con UNION ALL la tabella nasce senza.
IF OBJECT_ID('gp_audit_aaaammgg', 'U') IS NULL
    SELECT * INTO gp_audit_aaaammgg FROM gp_audit WHERE 1 = 0 UNION ALL SELECT * FROM gp_audit WHERE 1 = 0;

BEGIN TRANSACTION;

PRINT 'Archiviazione...';
INSERT INTO gp_audit_aaaammgg SELECT * FROM gp_audit WHERE data < @end_audit;

PRINT 'Cancellazione...';
DELETE FROM gp_audit WHERE data < @end_audit;

COMMIT TRANSACTION;
