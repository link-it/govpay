-- =============================================================================
-- Svecchiamento TRACCIATI NOTIFICA PAGAMENTI - SQL Server
--
-- Elimina i tracciati di notifica dei pagamenti (trac_notif_pag) creati da
-- piu' di retention_tnp giorni. Nessun'altra tabella li referenzia.
--
-- Il riferimento e' data_creazione, valorizzata per ogni tracciato: anche
-- quelli mai completati, dopo la retention, non servono piu'.
--
-- Prima di essere cancellate, le righe vengono copiate nella tabella di
-- archivio trac_notif_pag_aaaammgg, creata se non esiste e riempita nella
-- stessa transazione della cancellazione: l'archivio contiene esattamente le
-- righe cancellate. svecchiamento-db.sh sostituisce aaaammgg con la data
-- dell'esecuzione; eseguito da solo, lo script archivia nella tabella
-- trac_notif_pag_aaaammgg. Salvare ed eliminare la tabella di archivio e'
-- compito del DBA.
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

PRINT 'Creazione della tabella di archivio...';
-- SELECT INTO copierebbe la proprieta' IDENTITY degli id, e l'archivio non
-- accetterebbe gli id originali: con UNION ALL la tabella nasce senza.
IF OBJECT_ID('trac_notif_pag_aaaammgg', 'U') IS NULL
    SELECT * INTO trac_notif_pag_aaaammgg FROM trac_notif_pag WHERE 1 = 0 UNION ALL SELECT * FROM trac_notif_pag WHERE 1 = 0;

BEGIN TRANSACTION;

PRINT 'Archiviazione...';
INSERT INTO trac_notif_pag_aaaammgg SELECT * FROM trac_notif_pag WHERE data_creazione < @end_tnp;

PRINT 'Cancellazione...';
DELETE FROM trac_notif_pag WHERE data_creazione < @end_tnp;

COMMIT TRANSACTION;
