-- =============================================================================
-- Svecchiamento PENDENZE SCADUTE NON PAGATE - SQL Server
--
-- Elimina le pendenze in stato NON_ESEGUITO scadute da piu' di
-- retention_pendenze mesi, con tutto cio' che le referenzia, nell'ordine
-- imposto dalle chiavi esterne.
--
-- Sono escluse le pendenze che hanno anche una sola voce rendicontata o un
-- pagamento, sulle voci o sulle RPT: non sono pendenze abbandonate, e le loro
-- righe sono referenziate da flussi e incassi che qui non si toccano.
--
-- I documenti a cui le pendenze appartengono non vengono cancellati: un
-- documento raggruppa piu' pendenze, e non e' detto che siano tutte scadute.
--
-- Gli id da cancellare vengono raccolti una volta sola in una tabella
-- temporanea locale alla sessione.
--
-- Uso: sqlcmd -b -S <host> -U <utente> -d <database> -i pendenze_scadute_non_pagate.sql
--
-- Il valore qui sotto e' il default. svecchiamento-db.sh lo sostituisce quando
-- gli si passa --retention-pendenze_scadute_non_pagate.
-- =============================================================================

DECLARE @retention_pendenze INT = 12;

DECLARE @end_pendenze DATE = DATEADD(MONTH, -@retention_pendenze, GETDATE());

PRINT '--- Svecchiamento PENDENZE SCADUTE NON PAGATE ---';
PRINT 'Retention: ' + CAST(@retention_pendenze AS VARCHAR) + ' mesi';

IF OBJECT_ID('tempdb..#svecchiamento_pendenze') IS NOT NULL DROP TABLE #svecchiamento_pendenze;
CREATE TABLE #svecchiamento_pendenze (id BIGINT NOT NULL PRIMARY KEY);

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
DELETE FROM notifiche WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento IN (SELECT id FROM #svecchiamento_pendenze));

PRINT 'Cancellazione notifiche App IO...';
DELETE FROM notifiche_app_io WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento IN (SELECT id FROM #svecchiamento_pendenze));
DELETE FROM notifiche_app_io WHERE id_versamento IN (SELECT id FROM #svecchiamento_pendenze);

PRINT 'Cancellazione promemoria...';
DELETE FROM promemoria WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento IN (SELECT id FROM #svecchiamento_pendenze));
DELETE FROM promemoria WHERE id_versamento IN (SELECT id FROM #svecchiamento_pendenze);

PRINT 'Cancellazione operazioni...';
DELETE FROM operazioni WHERE id_stampa IN (SELECT id FROM stampe WHERE id_versamento IN (SELECT id FROM #svecchiamento_pendenze));
DELETE FROM operazioni WHERE id_versamento IN (SELECT id FROM #svecchiamento_pendenze);

PRINT 'Cancellazione stampe...';
DELETE FROM stampe WHERE id_versamento IN (SELECT id FROM #svecchiamento_pendenze);

PRINT 'Cancellazione allegati...';
DELETE FROM allegati WHERE id_versamento IN (SELECT id FROM #svecchiamento_pendenze);

PRINT 'Cancellazione RPT...';
DELETE FROM rpt WHERE id_versamento IN (SELECT id FROM #svecchiamento_pendenze);

PRINT 'Cancellazione voci delle pendenze...';
DELETE FROM singoli_versamenti WHERE id_versamento IN (SELECT id FROM #svecchiamento_pendenze);

PRINT 'Cancellazione pendenze...';
DELETE FROM versamenti WHERE id IN (SELECT id FROM #svecchiamento_pendenze);

COMMIT TRANSACTION;

DROP TABLE #svecchiamento_pendenze;
