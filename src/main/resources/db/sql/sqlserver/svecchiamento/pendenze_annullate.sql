-- =============================================================================
-- Svecchiamento PENDENZE ANNULLATE - SQL Server
--
-- Elimina le pendenze in stato ANNULLATO non piu' aggiornate da piu' di
-- retention_annullate giorni, con tutto cio' che le referenzia, nell'ordine
-- imposto dalle chiavi esterne.
--
-- La data di riferimento e' data_ora_ultimo_aggiornamento: GovPay non registra
-- la data dell'annullamento, e l'annullamento da API o da tracciato non aggiorna
-- questa colonna. Una pendenza vecchia annullata di recente puo' quindi essere
-- svecchiata al primo giro; resta comunque nell'archivio.
--
-- Sono escluse le pendenze che hanno anche una sola voce rendicontata o un
-- pagamento, sulle voci o sulle RPT: non sono pendenze abbandonate, e le loro
-- righe sono referenziate da flussi e incassi che qui non si toccano.
--
-- I documenti a cui le pendenze appartengono non vengono cancellati: un
-- documento raggruppa piu' pendenze, e non e' detto che siano tutte annullate.
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
-- Uso: sqlcmd -b -S <host> -U <utente> -d <database> -i pendenze_annullate.sql
--
-- Il valore qui sotto e' il default. svecchiamento-db.sh lo sostituisce quando
-- gli si passa --retention-pendenze_annullate.
-- =============================================================================

DECLARE @retention_annullate INT = 365;

DECLARE @end_annullate DATE = DATEADD(DAY, -@retention_annullate, GETDATE());

PRINT '--- Svecchiamento PENDENZE ANNULLATE ---';
PRINT 'Retention: ' + CAST(@retention_annullate AS VARCHAR) + ' giorni';

IF OBJECT_ID('tempdb..#svecchiamento_annullate') IS NOT NULL DROP TABLE #svecchiamento_annullate;
CREATE TABLE #svecchiamento_annullate (id BIGINT NOT NULL PRIMARY KEY);

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
INSERT INTO #svecchiamento_annullate (id)
SELECT v.id FROM versamenti v
WHERE v.stato_versamento = 'ANNULLATO'
  AND v.data_ora_ultimo_aggiornamento < @end_annullate
  AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN rendicontazioni r ON r.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
  AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN pagamenti p ON p.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
  AND NOT EXISTS (SELECT 1 FROM rpt JOIN pagamenti p ON p.id_rpt = rpt.id WHERE rpt.id_versamento = v.id);

PRINT 'Pendenze da cancellare: ' + CAST(@@ROWCOUNT AS VARCHAR);

PRINT 'Cancellazione notifiche...';
INSERT INTO notifiche_aaaammgg SELECT * FROM notifiche WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento IN (SELECT id FROM #svecchiamento_annullate));
DELETE FROM notifiche WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento IN (SELECT id FROM #svecchiamento_annullate));

PRINT 'Cancellazione notifiche App IO...';
INSERT INTO notifiche_app_io_aaaammgg SELECT * FROM notifiche_app_io WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento IN (SELECT id FROM #svecchiamento_annullate));
DELETE FROM notifiche_app_io WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento IN (SELECT id FROM #svecchiamento_annullate));
INSERT INTO notifiche_app_io_aaaammgg SELECT * FROM notifiche_app_io WHERE id_versamento IN (SELECT id FROM #svecchiamento_annullate);
DELETE FROM notifiche_app_io WHERE id_versamento IN (SELECT id FROM #svecchiamento_annullate);

PRINT 'Cancellazione promemoria...';
INSERT INTO promemoria_aaaammgg SELECT * FROM promemoria WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento IN (SELECT id FROM #svecchiamento_annullate));
DELETE FROM promemoria WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento IN (SELECT id FROM #svecchiamento_annullate));
INSERT INTO promemoria_aaaammgg SELECT * FROM promemoria WHERE id_versamento IN (SELECT id FROM #svecchiamento_annullate);
DELETE FROM promemoria WHERE id_versamento IN (SELECT id FROM #svecchiamento_annullate);

PRINT 'Cancellazione operazioni...';
INSERT INTO operazioni_aaaammgg SELECT * FROM operazioni WHERE id_stampa IN (SELECT id FROM stampe WHERE id_versamento IN (SELECT id FROM #svecchiamento_annullate));
DELETE FROM operazioni WHERE id_stampa IN (SELECT id FROM stampe WHERE id_versamento IN (SELECT id FROM #svecchiamento_annullate));
INSERT INTO operazioni_aaaammgg SELECT * FROM operazioni WHERE id_versamento IN (SELECT id FROM #svecchiamento_annullate);
DELETE FROM operazioni WHERE id_versamento IN (SELECT id FROM #svecchiamento_annullate);

PRINT 'Cancellazione stampe...';
INSERT INTO stampe_aaaammgg SELECT * FROM stampe WHERE id_versamento IN (SELECT id FROM #svecchiamento_annullate);
DELETE FROM stampe WHERE id_versamento IN (SELECT id FROM #svecchiamento_annullate);

PRINT 'Cancellazione allegati...';
INSERT INTO allegati_aaaammgg SELECT * FROM allegati WHERE id_versamento IN (SELECT id FROM #svecchiamento_annullate);
DELETE FROM allegati WHERE id_versamento IN (SELECT id FROM #svecchiamento_annullate);

PRINT 'Cancellazione RPT...';
INSERT INTO rpt_aaaammgg SELECT * FROM rpt WHERE id_versamento IN (SELECT id FROM #svecchiamento_annullate);
DELETE FROM rpt WHERE id_versamento IN (SELECT id FROM #svecchiamento_annullate);

PRINT 'Cancellazione voci delle pendenze...';
INSERT INTO singoli_versamenti_aaaammgg SELECT * FROM singoli_versamenti WHERE id_versamento IN (SELECT id FROM #svecchiamento_annullate);
DELETE FROM singoli_versamenti WHERE id_versamento IN (SELECT id FROM #svecchiamento_annullate);

PRINT 'Cancellazione pendenze...';
INSERT INTO versamenti_aaaammgg SELECT * FROM versamenti WHERE id IN (SELECT id FROM #svecchiamento_annullate);
DELETE FROM versamenti WHERE id IN (SELECT id FROM #svecchiamento_annullate);

COMMIT TRANSACTION;

DROP TABLE #svecchiamento_annullate;
