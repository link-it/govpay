-- =============================================================================
-- Svecchiamento PENDENZE SCADUTE NON PAGATE - SQL Server
--
-- Elimina le pendenze in stato NON_ESEGUITO scadute da piu' di
-- retention_pendenze giorni, con tutto cio' che le referenzia, nell'ordine
-- imposto dalle chiavi esterne.
--
-- Sono escluse le pendenze che hanno anche una sola voce rendicontata o un
-- pagamento, sulle voci o sulle RPT: non sono pendenze abbandonate, e le loro
-- righe sono referenziate da flussi e incassi che qui non si toccano.
--
-- I documenti a cui le pendenze appartengono non vengono cancellati: un
-- documento raggruppa piu' pendenze, e non e' detto che siano tutte scadute.
-- Quelli rimasti senza pendenze li cancella la sezione documenti_orfani.
--
-- Gli id da cancellare vengono raccolti una volta sola in una tabella
-- temporanea locale alla sessione.
--
-- Prima di essere cancellate, le righe vengono copiate in tabelle di archivio
-- con lo stesso nome e il suffisso _aaaammgg (notifiche, notifiche_app_io,
-- promemoria, operazioni, stampe, allegati, rpt, singoli_versamenti,
-- versamenti), create se non esistono, senza vincoli ne' indici, e nella
-- stessa transazione delle cancellazioni: l'archivio contiene esattamente le
-- righe cancellate. svecchiamento-db.sh sostituisce aaaammgg con la data
-- dell'esecuzione; eseguito da solo, lo script archivia nelle tabelle
-- _aaaammgg. Gli eventi non vengono archiviati. Salvare ed eliminare le
-- tabelle di archivio e' compito del DBA.
--
-- Uso: sqlcmd -b -S <host> -U <utente> -d <database> -i pendenze_scadute_non_pagate.sql
--
-- Il valore qui sotto e' il default. svecchiamento-db.sh lo sostituisce quando
-- gli si passa --retention-pendenze_scadute_non_pagate.
-- =============================================================================

DECLARE @retention_pendenze INT = 365;

DECLARE @end_pendenze DATE = DATEADD(DAY, -@retention_pendenze, GETDATE());

PRINT '--- Svecchiamento PENDENZE SCADUTE NON PAGATE ---';
PRINT 'Retention: ' + CAST(@retention_pendenze AS VARCHAR) + ' giorni';

IF OBJECT_ID('tempdb..#svecchiamento_pendenze') IS NOT NULL DROP TABLE #svecchiamento_pendenze;
CREATE TABLE #svecchiamento_pendenze (id BIGINT NOT NULL PRIMARY KEY);

PRINT 'Creazione delle tabelle di archivio...';
-- SELECT INTO copierebbe la proprieta' IDENTITY degli id, e l'archivio non
-- accetterebbe gli id originali: con UNION ALL la tabella nasce senza.
IF OBJECT_ID('notifiche_aaaammgg', 'U') IS NULL
    SELECT * INTO notifiche_aaaammgg FROM notifiche WHERE 1 = 0 UNION ALL SELECT * FROM notifiche WHERE 1 = 0;
IF OBJECT_ID('notifiche_app_io_aaaammgg', 'U') IS NULL
    SELECT * INTO notifiche_app_io_aaaammgg FROM notifiche_app_io WHERE 1 = 0 UNION ALL SELECT * FROM notifiche_app_io WHERE 1 = 0;
IF OBJECT_ID('promemoria_aaaammgg', 'U') IS NULL
    SELECT * INTO promemoria_aaaammgg FROM promemoria WHERE 1 = 0 UNION ALL SELECT * FROM promemoria WHERE 1 = 0;
IF OBJECT_ID('operazioni_aaaammgg', 'U') IS NULL
    SELECT * INTO operazioni_aaaammgg FROM operazioni WHERE 1 = 0 UNION ALL SELECT * FROM operazioni WHERE 1 = 0;
IF OBJECT_ID('stampe_aaaammgg', 'U') IS NULL
    SELECT * INTO stampe_aaaammgg FROM stampe WHERE 1 = 0 UNION ALL SELECT * FROM stampe WHERE 1 = 0;
IF OBJECT_ID('allegati_aaaammgg', 'U') IS NULL
    SELECT * INTO allegati_aaaammgg FROM allegati WHERE 1 = 0 UNION ALL SELECT * FROM allegati WHERE 1 = 0;
IF OBJECT_ID('rpt_aaaammgg', 'U') IS NULL
    SELECT * INTO rpt_aaaammgg FROM rpt WHERE 1 = 0 UNION ALL SELECT * FROM rpt WHERE 1 = 0;
IF OBJECT_ID('singoli_versamenti_aaaammgg', 'U') IS NULL
    SELECT * INTO singoli_versamenti_aaaammgg FROM singoli_versamenti WHERE 1 = 0 UNION ALL SELECT * FROM singoli_versamenti WHERE 1 = 0;
IF OBJECT_ID('versamenti_aaaammgg', 'U') IS NULL
    SELECT * INTO versamenti_aaaammgg FROM versamenti WHERE 1 = 0 UNION ALL SELECT * FROM versamenti WHERE 1 = 0;

BEGIN TRANSACTION;

PRINT 'Selezione delle pendenze da cancellare...';
INSERT INTO #svecchiamento_pendenze (id)
SELECT v.id FROM versamenti v
WHERE v.stato_versamento = 'NON_ESEGUITO'
  AND v.data_scadenza < @end_pendenze
  AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN rendicontazioni r ON r.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
  AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN pagamenti p ON p.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
  AND NOT EXISTS (SELECT 1 FROM rpt JOIN pagamenti p ON p.id_rpt = rpt.id WHERE rpt.id_versamento = v.id);

PRINT 'Pendenze da cancellare: ' + CAST(@@ROWCOUNT AS VARCHAR);

PRINT 'Cancellazione notifiche...';
INSERT INTO notifiche_aaaammgg SELECT * FROM notifiche WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento IN (SELECT id FROM #svecchiamento_pendenze));
DELETE FROM notifiche WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento IN (SELECT id FROM #svecchiamento_pendenze));

PRINT 'Cancellazione notifiche App IO...';
INSERT INTO notifiche_app_io_aaaammgg SELECT * FROM notifiche_app_io WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento IN (SELECT id FROM #svecchiamento_pendenze));
DELETE FROM notifiche_app_io WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento IN (SELECT id FROM #svecchiamento_pendenze));
INSERT INTO notifiche_app_io_aaaammgg SELECT * FROM notifiche_app_io WHERE id_versamento IN (SELECT id FROM #svecchiamento_pendenze);
DELETE FROM notifiche_app_io WHERE id_versamento IN (SELECT id FROM #svecchiamento_pendenze);

PRINT 'Cancellazione promemoria...';
INSERT INTO promemoria_aaaammgg SELECT * FROM promemoria WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento IN (SELECT id FROM #svecchiamento_pendenze));
DELETE FROM promemoria WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento IN (SELECT id FROM #svecchiamento_pendenze));
INSERT INTO promemoria_aaaammgg SELECT * FROM promemoria WHERE id_versamento IN (SELECT id FROM #svecchiamento_pendenze);
DELETE FROM promemoria WHERE id_versamento IN (SELECT id FROM #svecchiamento_pendenze);

PRINT 'Cancellazione operazioni...';
INSERT INTO operazioni_aaaammgg SELECT * FROM operazioni WHERE id_stampa IN (SELECT id FROM stampe WHERE id_versamento IN (SELECT id FROM #svecchiamento_pendenze));
DELETE FROM operazioni WHERE id_stampa IN (SELECT id FROM stampe WHERE id_versamento IN (SELECT id FROM #svecchiamento_pendenze));
INSERT INTO operazioni_aaaammgg SELECT * FROM operazioni WHERE id_versamento IN (SELECT id FROM #svecchiamento_pendenze);
DELETE FROM operazioni WHERE id_versamento IN (SELECT id FROM #svecchiamento_pendenze);

PRINT 'Cancellazione stampe...';
INSERT INTO stampe_aaaammgg SELECT * FROM stampe WHERE id_versamento IN (SELECT id FROM #svecchiamento_pendenze);
DELETE FROM stampe WHERE id_versamento IN (SELECT id FROM #svecchiamento_pendenze);

PRINT 'Cancellazione allegati...';
INSERT INTO allegati_aaaammgg SELECT * FROM allegati WHERE id_versamento IN (SELECT id FROM #svecchiamento_pendenze);
DELETE FROM allegati WHERE id_versamento IN (SELECT id FROM #svecchiamento_pendenze);

PRINT 'Cancellazione RPT...';
INSERT INTO rpt_aaaammgg SELECT * FROM rpt WHERE id_versamento IN (SELECT id FROM #svecchiamento_pendenze);
DELETE FROM rpt WHERE id_versamento IN (SELECT id FROM #svecchiamento_pendenze);

PRINT 'Cancellazione voci delle pendenze...';
INSERT INTO singoli_versamenti_aaaammgg SELECT * FROM singoli_versamenti WHERE id_versamento IN (SELECT id FROM #svecchiamento_pendenze);
DELETE FROM singoli_versamenti WHERE id_versamento IN (SELECT id FROM #svecchiamento_pendenze);

PRINT 'Cancellazione pendenze...';
INSERT INTO versamenti_aaaammgg SELECT * FROM versamenti WHERE id IN (SELECT id FROM #svecchiamento_pendenze);
DELETE FROM versamenti WHERE id IN (SELECT id FROM #svecchiamento_pendenze);

COMMIT TRANSACTION;

DROP TABLE #svecchiamento_pendenze;
