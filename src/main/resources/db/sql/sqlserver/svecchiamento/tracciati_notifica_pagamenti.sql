-- =============================================================================
-- Svecchiamento TRACCIATI NOTIFICA PAGAMENTI - SQL Server
--
-- Elimina i tracciati di notifica dei pagamenti (trac_notif_pag) creati da
-- piu' di retention_tnp giorni. Nessun'altra tabella li referenzia.
--
-- Il riferimento e' data_creazione, valorizzata per ogni tracciato: anche
-- quelli mai completati, dopo la retention, non servono piu'.
--
-- Nessun archivio: i tracciati di notifica sono copie di quanto gia' inviato
-- all'ente, e vengono cancellati senza copia.
--
-- Uso: sqlcmd -b -S <host> -U <utente> -d <database> -i tracciati_notifica_pagamenti.sql
--
-- Il valore qui sotto e' il default. svecchiamento-db.sh lo sostituisce quando
-- gli si passa --retention-tracciati_notifica_pagamenti.
-- =============================================================================

DECLARE @retention_tnp INT = 30;

DECLARE @end_tnp DATE = DATEADD(DAY, -@retention_tnp, GETDATE());

PRINT '--- Svecchiamento TRACCIATI NOTIFICA PAGAMENTI ---';
PRINT 'Retention: ' + CAST(@retention_tnp AS VARCHAR) + ' giorni';

BEGIN TRANSACTION;

PRINT 'Cancellazione...';
DELETE FROM trac_notif_pag WHERE data_creazione < @end_tnp;

COMMIT TRANSACTION;
