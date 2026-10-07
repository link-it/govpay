-- =============================================================================
-- Svecchiamento FLUSSI DI RENDICONTAZIONE - SQL Server
--
-- Elimina i flussi di rendicontazione con data_ora_flusso piu' vecchia di
-- retention_fr giorni, con le loro rendicontazioni e i loro eventi, le pendenze
-- che rendicontano e gli incassi che li riconciliano.
--
-- Le pendenze vengono cancellate qualunque sia il loro stato: una pendenza
-- rendicontata puo' risultare non pagata perche' non ancora riconciliata, o
-- essere stata pagata senza RT. Ne viene cancellata tutta la struttura, come in
-- pendenze_pagate: pagamenti, RPT, voci, notifiche, promemoria, stampe,
-- operazioni e allegati. Una pendenza resta se:
--   - una sua voce o un suo pagamento e' rendicontato anche in un flusso piu'
--     recente della soglia: una revoca, un'altra revisione del flusso, un
--     pagamento multibeneficiario rendicontato da piu' enti;
--   - ha un pagamento piu' recente della soglia.
-- In quel caso perde solo le rendicontazioni dei flussi vecchi, e i pagamenti
-- che esse riferivano risultano non rendicontati (vista v_vrs_non_rnd) finche'
-- anche gli altri flussi non superano la soglia: succede solo alle pendenze a
-- cavallo della soglia.
--
-- I documenti delle pendenze cancellate restano: quelli rimasti senza pendenze
-- li cancella la sezione documenti_orfani.
--
-- Con il flusso va via anche l'incasso che lo riconcilia, con i suoi eventi: la
-- riconciliazione collega un incasso a un solo flusso. I pagamenti rimasti che
-- riferiscono l'incasso, quelli delle pendenze che restano, vengono scollegati
-- (id_incasso a NULL) e conservano lo stato INCASSATO.
--
-- Gli id da cancellare vengono raccolti all'inizio in tabelle temporanee locali
-- alla sessione: le DELETE cambiano le rendicontazioni su cui si basa la
-- selezione, e ripeterla dopo darebbe un altro risultato.
--
-- Prima di essere cancellate, le righe vengono copiate in tabelle di archivio
-- con lo stesso nome e il suffisso _aaaammgg (rendicontazioni, pagamenti,
-- notifiche, notifiche_app_io, promemoria, operazioni, stampe, allegati, rpt,
-- singoli_versamenti, versamenti, fr, incassi), create se non esistono, senza
-- vincoli ne' indici, e nella stessa transazione delle cancellazioni:
-- l'archivio contiene esattamente le righe cancellate. svecchiamento-db.sh
-- sostituisce aaaammgg con la data dell'esecuzione; eseguito da solo, lo
-- script archivia nelle tabelle _aaaammgg. Gli eventi non vengono archiviati.
-- Salvare ed eliminare le tabelle di archivio e' compito del DBA.
--
-- Uso: sqlcmd -b -S <host> -U <utente> -d <database> -i flussi_rendicontazione.sql
--
-- Il valore qui sotto e' il default. svecchiamento-db.sh lo sostituisce quando
-- gli si passa --retention-flussi_rendicontazione.
-- =============================================================================

DECLARE @retention_fr INT = 730;

DECLARE @end_fr DATE = DATEADD(DAY, -@retention_fr, GETDATE());

PRINT '--- Svecchiamento FLUSSI DI RENDICONTAZIONE ---';
PRINT 'Retention: ' + CAST(@retention_fr AS VARCHAR) + ' giorni';

IF OBJECT_ID('tempdb..#svecchiamento_fr') IS NOT NULL DROP TABLE #svecchiamento_fr;
CREATE TABLE #svecchiamento_fr (id BIGINT NOT NULL PRIMARY KEY);
IF OBJECT_ID('tempdb..#svecchiamento_fr_pendenze') IS NOT NULL DROP TABLE #svecchiamento_fr_pendenze;
CREATE TABLE #svecchiamento_fr_pendenze (id BIGINT NOT NULL PRIMARY KEY);
IF OBJECT_ID('tempdb..#svecchiamento_fr_incassi') IS NOT NULL DROP TABLE #svecchiamento_fr_incassi;
CREATE TABLE #svecchiamento_fr_incassi (id BIGINT NOT NULL PRIMARY KEY);

PRINT 'Creazione delle tabelle di archivio...';
-- SELECT INTO copierebbe la proprieta' IDENTITY degli id, e l'archivio non
-- accetterebbe gli id originali: con UNION ALL la tabella nasce senza.
IF OBJECT_ID('rendicontazioni_aaaammgg', 'U') IS NULL
    SELECT * INTO rendicontazioni_aaaammgg FROM rendicontazioni WHERE 1 = 0 UNION ALL SELECT * FROM rendicontazioni WHERE 1 = 0;
IF OBJECT_ID('pagamenti_aaaammgg', 'U') IS NULL
    SELECT * INTO pagamenti_aaaammgg FROM pagamenti WHERE 1 = 0 UNION ALL SELECT * FROM pagamenti WHERE 1 = 0;
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
IF OBJECT_ID('fr_aaaammgg', 'U') IS NULL
    SELECT * INTO fr_aaaammgg FROM fr WHERE 1 = 0 UNION ALL SELECT * FROM fr WHERE 1 = 0;
IF OBJECT_ID('incassi_aaaammgg', 'U') IS NULL
    SELECT * INTO incassi_aaaammgg FROM incassi WHERE 1 = 0 UNION ALL SELECT * FROM incassi WHERE 1 = 0;

BEGIN TRANSACTION;

PRINT 'Selezione dei flussi da cancellare...';
INSERT INTO #svecchiamento_fr (id) SELECT id FROM fr WHERE data_ora_flusso < @end_fr;

PRINT 'Selezione delle pendenze rendicontate da cancellare...';
-- Una rendicontazione riferisce la pendenza sulla voce, o sul pagamento, che a
-- sua volta e' legato alla voce o alla RPT. Le tre strade valgono sia per
-- trovare le pendenze dei flussi vecchi sia per escludere quelle rendicontate
-- anche in flussi recenti.
INSERT INTO #svecchiamento_fr_pendenze (id)
SELECT v.id FROM versamenti v
WHERE (EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN rendicontazioni r ON r.id_singolo_versamento = sv.id JOIN fr f ON f.id = r.id_fr WHERE sv.id_versamento = v.id AND f.data_ora_flusso < @end_fr)
    OR EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN pagamenti p ON p.id_singolo_versamento = sv.id JOIN rendicontazioni r ON r.id_pagamento = p.id JOIN fr f ON f.id = r.id_fr WHERE sv.id_versamento = v.id AND f.data_ora_flusso < @end_fr)
    OR EXISTS (SELECT 1 FROM rpt JOIN pagamenti p ON p.id_rpt = rpt.id JOIN rendicontazioni r ON r.id_pagamento = p.id JOIN fr f ON f.id = r.id_fr WHERE rpt.id_versamento = v.id AND f.data_ora_flusso < @end_fr))
  AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN rendicontazioni r ON r.id_singolo_versamento = sv.id JOIN fr f ON f.id = r.id_fr WHERE sv.id_versamento = v.id AND f.data_ora_flusso >= @end_fr)
  AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN pagamenti p ON p.id_singolo_versamento = sv.id JOIN rendicontazioni r ON r.id_pagamento = p.id JOIN fr f ON f.id = r.id_fr WHERE sv.id_versamento = v.id AND f.data_ora_flusso >= @end_fr)
  AND NOT EXISTS (SELECT 1 FROM rpt JOIN pagamenti p ON p.id_rpt = rpt.id JOIN rendicontazioni r ON r.id_pagamento = p.id JOIN fr f ON f.id = r.id_fr WHERE rpt.id_versamento = v.id AND f.data_ora_flusso >= @end_fr)
  AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN pagamenti p ON p.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id AND p.data_pagamento >= @end_fr)
  AND NOT EXISTS (SELECT 1 FROM rpt JOIN pagamenti p ON p.id_rpt = rpt.id WHERE rpt.id_versamento = v.id AND p.data_pagamento >= @end_fr);

PRINT 'Selezione degli incassi dei flussi da cancellare...';
INSERT INTO #svecchiamento_fr_incassi (id) SELECT DISTINCT id_incasso FROM fr WHERE id IN (SELECT id FROM #svecchiamento_fr) AND id_incasso IS NOT NULL;

SELECT (SELECT COUNT(*) FROM #svecchiamento_fr) AS flussi_da_cancellare, (SELECT COUNT(*) FROM #svecchiamento_fr_pendenze) AS pendenze_da_cancellare;

PRINT 'Cancellazione eventi dei flussi...';
DELETE FROM eventi WHERE id_fr IN (SELECT id FROM #svecchiamento_fr);

PRINT 'Cancellazione rendicontazioni dei flussi...';
INSERT INTO rendicontazioni_aaaammgg SELECT * FROM rendicontazioni WHERE id_fr IN (SELECT id FROM #svecchiamento_fr);
DELETE FROM rendicontazioni WHERE id_fr IN (SELECT id FROM #svecchiamento_fr);

PRINT 'Cancellazione pagamenti...';
INSERT INTO pagamenti_aaaammgg SELECT * FROM pagamenti WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento IN (SELECT id FROM #svecchiamento_fr_pendenze));
DELETE FROM pagamenti WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento IN (SELECT id FROM #svecchiamento_fr_pendenze));
INSERT INTO pagamenti_aaaammgg SELECT * FROM pagamenti WHERE id_singolo_versamento IN (SELECT id FROM singoli_versamenti WHERE id_versamento IN (SELECT id FROM #svecchiamento_fr_pendenze));
DELETE FROM pagamenti WHERE id_singolo_versamento IN (SELECT id FROM singoli_versamenti WHERE id_versamento IN (SELECT id FROM #svecchiamento_fr_pendenze));

PRINT 'Cancellazione notifiche...';
INSERT INTO notifiche_aaaammgg SELECT * FROM notifiche WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento IN (SELECT id FROM #svecchiamento_fr_pendenze));
DELETE FROM notifiche WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento IN (SELECT id FROM #svecchiamento_fr_pendenze));

PRINT 'Cancellazione notifiche App IO...';
INSERT INTO notifiche_app_io_aaaammgg SELECT * FROM notifiche_app_io WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento IN (SELECT id FROM #svecchiamento_fr_pendenze));
DELETE FROM notifiche_app_io WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento IN (SELECT id FROM #svecchiamento_fr_pendenze));
INSERT INTO notifiche_app_io_aaaammgg SELECT * FROM notifiche_app_io WHERE id_versamento IN (SELECT id FROM #svecchiamento_fr_pendenze);
DELETE FROM notifiche_app_io WHERE id_versamento IN (SELECT id FROM #svecchiamento_fr_pendenze);

PRINT 'Cancellazione promemoria...';
INSERT INTO promemoria_aaaammgg SELECT * FROM promemoria WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento IN (SELECT id FROM #svecchiamento_fr_pendenze));
DELETE FROM promemoria WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento IN (SELECT id FROM #svecchiamento_fr_pendenze));
INSERT INTO promemoria_aaaammgg SELECT * FROM promemoria WHERE id_versamento IN (SELECT id FROM #svecchiamento_fr_pendenze);
DELETE FROM promemoria WHERE id_versamento IN (SELECT id FROM #svecchiamento_fr_pendenze);

PRINT 'Cancellazione operazioni...';
INSERT INTO operazioni_aaaammgg SELECT * FROM operazioni WHERE id_stampa IN (SELECT id FROM stampe WHERE id_versamento IN (SELECT id FROM #svecchiamento_fr_pendenze));
DELETE FROM operazioni WHERE id_stampa IN (SELECT id FROM stampe WHERE id_versamento IN (SELECT id FROM #svecchiamento_fr_pendenze));
INSERT INTO operazioni_aaaammgg SELECT * FROM operazioni WHERE id_versamento IN (SELECT id FROM #svecchiamento_fr_pendenze);
DELETE FROM operazioni WHERE id_versamento IN (SELECT id FROM #svecchiamento_fr_pendenze);

PRINT 'Cancellazione stampe...';
INSERT INTO stampe_aaaammgg SELECT * FROM stampe WHERE id_versamento IN (SELECT id FROM #svecchiamento_fr_pendenze);
DELETE FROM stampe WHERE id_versamento IN (SELECT id FROM #svecchiamento_fr_pendenze);

PRINT 'Cancellazione allegati...';
INSERT INTO allegati_aaaammgg SELECT * FROM allegati WHERE id_versamento IN (SELECT id FROM #svecchiamento_fr_pendenze);
DELETE FROM allegati WHERE id_versamento IN (SELECT id FROM #svecchiamento_fr_pendenze);

PRINT 'Cancellazione RPT...';
INSERT INTO rpt_aaaammgg SELECT * FROM rpt WHERE id_versamento IN (SELECT id FROM #svecchiamento_fr_pendenze);
DELETE FROM rpt WHERE id_versamento IN (SELECT id FROM #svecchiamento_fr_pendenze);

PRINT 'Cancellazione voci delle pendenze...';
INSERT INTO singoli_versamenti_aaaammgg SELECT * FROM singoli_versamenti WHERE id_versamento IN (SELECT id FROM #svecchiamento_fr_pendenze);
DELETE FROM singoli_versamenti WHERE id_versamento IN (SELECT id FROM #svecchiamento_fr_pendenze);

PRINT 'Cancellazione pendenze...';
INSERT INTO versamenti_aaaammgg SELECT * FROM versamenti WHERE id IN (SELECT id FROM #svecchiamento_fr_pendenze);
DELETE FROM versamenti WHERE id IN (SELECT id FROM #svecchiamento_fr_pendenze);

PRINT 'Cancellazione flussi...';
INSERT INTO fr_aaaammgg SELECT * FROM fr WHERE id IN (SELECT id FROM #svecchiamento_fr);
DELETE FROM fr WHERE id IN (SELECT id FROM #svecchiamento_fr);

PRINT 'Cancellazione incassi dei flussi...';
UPDATE pagamenti SET id_incasso = NULL WHERE id_incasso IN (SELECT id FROM #svecchiamento_fr_incassi);
DELETE FROM eventi WHERE id_incasso IN (SELECT id FROM #svecchiamento_fr_incassi);
INSERT INTO incassi_aaaammgg SELECT * FROM incassi WHERE id IN (SELECT id FROM #svecchiamento_fr_incassi);
DELETE FROM incassi WHERE id IN (SELECT id FROM #svecchiamento_fr_incassi);

COMMIT TRANSACTION;

DROP TABLE #svecchiamento_fr;
DROP TABLE #svecchiamento_fr_pendenze;
DROP TABLE #svecchiamento_fr_incassi;
