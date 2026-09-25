-- =============================================================================
-- Svecchiamento EVENTI - SQL Server
--
-- Svecchia il giornale degli eventi per eta'. E' la tabella che cresce di piu'.
-- La condizione e' sulla colonna data, coperta dall'indice idx_evt_data; nessuna
-- tabella referenzia eventi, quindi la cancellazione non e' vincolata da chiavi
-- esterne.
--
-- Questa sezione va per prima: eventi e' la tabella piu' grande, e sfoltirla
-- rende meno costose le DELETE della sezione tracciati, che su eventi passano da
-- una sottoquery. Gli eventi collegati a un tracciato scaduto ma piu' recenti
-- della retention non vengono toccati qui: li elimina quella sezione, insieme al
-- tracciato che li referenzia.
--
-- Uso: sqlcmd -b -S <host> -U <utente> -d <database> -i eventi.sql
--
-- Il valore qui sotto e' il default. svecchiamento-db.sh lo sostituisce quando
-- gli si passa --retention-eventi.
-- =============================================================================

DECLARE @retention_eventi INT = 3;

DECLARE @end_eventi DATE = DATEADD(MONTH, -@retention_eventi, GETDATE());

PRINT '--- Svecchiamento EVENTI ---';
PRINT 'Retention: ' + CAST(@retention_eventi AS VARCHAR) + ' mesi';

BEGIN TRANSACTION;

PRINT 'Cancellazione eventi...';
DELETE FROM eventi WHERE data < @end_eventi;

COMMIT TRANSACTION;
