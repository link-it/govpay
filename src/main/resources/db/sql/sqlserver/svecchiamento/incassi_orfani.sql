-- =============================================================================
-- Svecchiamento INCASSI ORFANI - SQL Server
--
-- Elimina gli incassi piu' vecchi di retention_incassi giorni non piu'
-- referenziati da nessun flusso di rendicontazione ne' da nessun pagamento,
-- con i loro eventi.
--
-- Un incasso resta orfano quando il suo flusso o i suoi pagamenti sono stati
-- cancellati senza di lui: da pendenze_pagate, che cancella i pagamenti ma non
-- gli incassi, o da una rimozione manuale delle rendicontazioni o dei flussi.
-- Gli incassi dei flussi svecchiati li cancella gia' flussi_rendicontazione,
-- insieme al flusso.
--
-- Il riferimento e' data_ora_incasso. Gli eventi dell'incasso vengono
-- cancellati e non archiviati, come in tutte le sezioni.
--
-- Prima di essere cancellate, le righe vengono copiate nella tabella di
-- archivio incassi_aaaammgg, creata se non esiste e riempita nella stessa
-- transazione della cancellazione: l'archivio contiene esattamente le righe
-- cancellate. svecchiamento-db.sh sostituisce aaaammgg con la data
-- dell'esecuzione; eseguito da solo, lo script archivia nella tabella
-- incassi_aaaammgg. Salvare ed eliminare la tabella di archivio e' compito del
-- DBA.
--
-- Uso: sqlcmd -b -S <host> -U <utente> -d <database> -i incassi_orfani.sql
--
-- Il valore qui sotto e' il default. svecchiamento-db.sh lo sostituisce quando
-- gli si passa --retention-incassi_orfani.
-- =============================================================================

DECLARE @retention_incassi INT = 730;

DECLARE @end_incassi DATE = DATEADD(DAY, -@retention_incassi, GETDATE());

PRINT '--- Svecchiamento INCASSI ORFANI ---';
PRINT 'Retention: ' + CAST(@retention_incassi AS VARCHAR) + ' giorni';

PRINT 'Creazione della tabella di archivio...';
-- SELECT INTO copierebbe la proprieta' IDENTITY degli id, e l'archivio non
-- accetterebbe gli id originali: con UNION ALL la tabella nasce senza.
IF OBJECT_ID('incassi_aaaammgg', 'U') IS NULL
    SELECT * INTO incassi_aaaammgg FROM incassi WHERE 1 = 0 UNION ALL SELECT * FROM incassi WHERE 1 = 0;

BEGIN TRANSACTION;

PRINT 'Cancellazione eventi degli incassi...';
DELETE FROM eventi WHERE id_incasso IN (SELECT id FROM incassi WHERE data_ora_incasso < @end_incassi
  AND NOT EXISTS (SELECT 1 FROM fr WHERE fr.id_incasso = incassi.id)
  AND NOT EXISTS (SELECT 1 FROM pagamenti p WHERE p.id_incasso = incassi.id));

PRINT 'Archiviazione...';
INSERT INTO incassi_aaaammgg SELECT * FROM incassi WHERE data_ora_incasso < @end_incassi
  AND NOT EXISTS (SELECT 1 FROM fr WHERE fr.id_incasso = incassi.id)
  AND NOT EXISTS (SELECT 1 FROM pagamenti p WHERE p.id_incasso = incassi.id);

PRINT 'Cancellazione...';
DELETE FROM incassi WHERE data_ora_incasso < @end_incassi
  AND NOT EXISTS (SELECT 1 FROM fr WHERE fr.id_incasso = incassi.id)
  AND NOT EXISTS (SELECT 1 FROM pagamenti p WHERE p.id_incasso = incassi.id);

COMMIT TRANSACTION;
