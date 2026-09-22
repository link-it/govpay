-- =============================================================================
-- Svecchiamento EVENTI - SQL Server
--
-- Svecchia il giornale degli eventi per eta'. E' la tabella che cresce di piu'.
-- La condizione e' sulla colonna data, coperta dall'indice idx_evt_data; nessuna
-- tabella referenzia eventi, quindi la cancellazione non e' vincolata da chiavi
-- esterne.
--
-- Se si esegue anche lo svecchiamento dei tracciati, farlo prima: elimina gli
-- eventi collegati ai tracciati scaduti a prescindere dalla loro eta'.
--
-- Uso: sqlcmd -b -S <host> -U <utente> -d <database> -i eventi.sql
--
-- Il valore qui sotto e' il default. svecchiamento-db.sh lo sostituisce quando
-- gli si passa --retention-eventi.
-- =============================================================================

DECLARE @retention_eventi INT = 90;

DECLARE @end_eventi DATE = DATEADD(DAY, -@retention_eventi, GETDATE());

PRINT '--- Svecchiamento EVENTI ---';
PRINT 'Retention: ' + CAST(@retention_eventi AS VARCHAR) + ' giorni';

BEGIN TRANSACTION;

PRINT 'Cancellazione eventi...';
DELETE FROM eventi WHERE data < @end_eventi;

COMMIT TRANSACTION;
