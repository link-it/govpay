-- =============================================================================
-- Svecchiamento PENDENZE PAGATE - SQL Server
--
-- Elimina le pendenze pagate il cui ultimo pagamento e' piu' vecchio di
-- retention_pendenze_pagate giorni, con le RPT, i pagamenti e tutto cio' che le
-- referenzia, nell'ordine imposto dalle chiavi esterne.
--
-- Una pendenza rientra se:
--   - e' in uno stato di pagamento eseguito: ESEGUITO, PARZIALMENTE_ESEGUITO,
--     ESEGUITO_ALTRO_CANALE, ESEGUITO_SENZA_RPT, INCASSATO;
--   - e' stata pagata piu' di retention_pendenze_pagate giorni fa. Con ricevuta,
--     la data e' data_pagamento, valorizzata all'elaborazione della RT. Senza
--     ricevuta data_pagamento e' vuota, e basta che la pendenza abbia dei
--     pagamenti: e' il caso dei pagamenti creati dalla riconciliazione, rimasti
--     senza rendicontazione dove le rendicontazioni vecchie sono state rimosse;
--   - nessuno dei suoi pagamenti, sulle voci o sulle RPT, e' piu' recente della
--     soglia: senza ricevuta e' questa la condizione sulla data, e con ricevuta
--     copre una RT recuperata in ritardo, piu' vecchia di un altro pagamento;
--   - nessuna sua voce e nessun suo pagamento e' rendicontato. Le pendenze
--     rendicontate sono svecchiate insieme ai loro flussi, dalla sezione
--     flussi_rendicontazione, e le pendenze pagate senza ricevuta con esse.
--
-- I documenti a cui le pendenze appartengono non vengono cancellati, come per
-- le pendenze scadute: quelli rimasti senza pendenze li cancella la sezione
-- documenti_orfani. Gli incassi neppure: sono i pagamenti a referenziarli.
--
-- Gli id da cancellare vengono raccolti una volta sola in una tabella
-- temporanea locale alla sessione.
--
-- Prima di essere cancellate, le righe vengono copiate in tabelle di archivio
-- con lo stesso nome e il suffisso _aaaammgg (pagamenti, notifiche,
-- notifiche_app_io, promemoria, operazioni, stampe, allegati, rpt,
-- singoli_versamenti, versamenti), create se non esistono, senza vincoli ne'
-- indici, e nella stessa transazione delle cancellazioni: l'archivio contiene
-- esattamente le righe cancellate. svecchiamento-db.sh sostituisce aaaammgg
-- con la data dell'esecuzione; eseguito da solo, lo script archivia nelle
-- tabelle _aaaammgg. Gli eventi non vengono archiviati. Salvare ed eliminare
-- le tabelle di archivio e' compito del DBA.
--
-- Uso: sqlcmd -b -S <host> -U <utente> -d <database> -i pendenze_pagate.sql
--
-- Il valore qui sotto e' il default. svecchiamento-db.sh lo sostituisce quando
-- gli si passa --retention-pendenze_pagate.
-- =============================================================================

DECLARE @retention_pendenze_pagate INT = 730;

DECLARE @end_pendenze_pagate DATE = DATEADD(DAY, -@retention_pendenze_pagate, GETDATE());

PRINT '--- Svecchiamento PENDENZE PAGATE ---';
PRINT 'Retention: ' + CAST(@retention_pendenze_pagate AS VARCHAR) + ' giorni';

IF OBJECT_ID('tempdb..#svecchiamento_pagate') IS NOT NULL DROP TABLE #svecchiamento_pagate;
CREATE TABLE #svecchiamento_pagate (id BIGINT NOT NULL PRIMARY KEY);

PRINT 'Creazione delle tabelle di archivio...';
-- SELECT INTO copierebbe la proprieta' IDENTITY degli id, e l'archivio non
-- accetterebbe gli id originali: con UNION ALL la tabella nasce senza.
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

BEGIN TRANSACTION;

PRINT 'Selezione delle pendenze da cancellare...';
INSERT INTO #svecchiamento_pagate (id)
SELECT v.id FROM versamenti v
WHERE v.stato_versamento IN ('ESEGUITO', 'PARZIALMENTE_ESEGUITO', 'ESEGUITO_ALTRO_CANALE', 'ESEGUITO_SENZA_RPT', 'INCASSATO')
  AND (v.data_pagamento < @end_pendenze_pagate
    OR (v.data_pagamento IS NULL
        AND (EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN pagamenti p ON p.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
          OR EXISTS (SELECT 1 FROM rpt JOIN pagamenti p ON p.id_rpt = rpt.id WHERE rpt.id_versamento = v.id))))
  AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN pagamenti p ON p.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id AND p.data_pagamento >= @end_pendenze_pagate)
  AND NOT EXISTS (SELECT 1 FROM rpt JOIN pagamenti p ON p.id_rpt = rpt.id WHERE rpt.id_versamento = v.id AND p.data_pagamento >= @end_pendenze_pagate)
  AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN rendicontazioni r ON r.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
  AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN pagamenti p ON p.id_singolo_versamento = sv.id JOIN rendicontazioni r ON r.id_pagamento = p.id WHERE sv.id_versamento = v.id)
  AND NOT EXISTS (SELECT 1 FROM rpt JOIN pagamenti p ON p.id_rpt = rpt.id JOIN rendicontazioni r ON r.id_pagamento = p.id WHERE rpt.id_versamento = v.id);

PRINT 'Pendenze da cancellare: ' + CAST(@@ROWCOUNT AS VARCHAR);

PRINT 'Cancellazione pagamenti...';
INSERT INTO pagamenti_aaaammgg SELECT * FROM pagamenti WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento IN (SELECT id FROM #svecchiamento_pagate));
DELETE FROM pagamenti WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento IN (SELECT id FROM #svecchiamento_pagate));
INSERT INTO pagamenti_aaaammgg SELECT * FROM pagamenti WHERE id_singolo_versamento IN (SELECT id FROM singoli_versamenti WHERE id_versamento IN (SELECT id FROM #svecchiamento_pagate));
DELETE FROM pagamenti WHERE id_singolo_versamento IN (SELECT id FROM singoli_versamenti WHERE id_versamento IN (SELECT id FROM #svecchiamento_pagate));

PRINT 'Cancellazione notifiche...';
INSERT INTO notifiche_aaaammgg SELECT * FROM notifiche WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento IN (SELECT id FROM #svecchiamento_pagate));
DELETE FROM notifiche WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento IN (SELECT id FROM #svecchiamento_pagate));

PRINT 'Cancellazione notifiche App IO...';
INSERT INTO notifiche_app_io_aaaammgg SELECT * FROM notifiche_app_io WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento IN (SELECT id FROM #svecchiamento_pagate));
DELETE FROM notifiche_app_io WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento IN (SELECT id FROM #svecchiamento_pagate));
INSERT INTO notifiche_app_io_aaaammgg SELECT * FROM notifiche_app_io WHERE id_versamento IN (SELECT id FROM #svecchiamento_pagate);
DELETE FROM notifiche_app_io WHERE id_versamento IN (SELECT id FROM #svecchiamento_pagate);

PRINT 'Cancellazione promemoria...';
INSERT INTO promemoria_aaaammgg SELECT * FROM promemoria WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento IN (SELECT id FROM #svecchiamento_pagate));
DELETE FROM promemoria WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento IN (SELECT id FROM #svecchiamento_pagate));
INSERT INTO promemoria_aaaammgg SELECT * FROM promemoria WHERE id_versamento IN (SELECT id FROM #svecchiamento_pagate);
DELETE FROM promemoria WHERE id_versamento IN (SELECT id FROM #svecchiamento_pagate);

PRINT 'Cancellazione operazioni...';
INSERT INTO operazioni_aaaammgg SELECT * FROM operazioni WHERE id_stampa IN (SELECT id FROM stampe WHERE id_versamento IN (SELECT id FROM #svecchiamento_pagate));
DELETE FROM operazioni WHERE id_stampa IN (SELECT id FROM stampe WHERE id_versamento IN (SELECT id FROM #svecchiamento_pagate));
INSERT INTO operazioni_aaaammgg SELECT * FROM operazioni WHERE id_versamento IN (SELECT id FROM #svecchiamento_pagate);
DELETE FROM operazioni WHERE id_versamento IN (SELECT id FROM #svecchiamento_pagate);

PRINT 'Cancellazione stampe...';
INSERT INTO stampe_aaaammgg SELECT * FROM stampe WHERE id_versamento IN (SELECT id FROM #svecchiamento_pagate);
DELETE FROM stampe WHERE id_versamento IN (SELECT id FROM #svecchiamento_pagate);

PRINT 'Cancellazione allegati...';
INSERT INTO allegati_aaaammgg SELECT * FROM allegati WHERE id_versamento IN (SELECT id FROM #svecchiamento_pagate);
DELETE FROM allegati WHERE id_versamento IN (SELECT id FROM #svecchiamento_pagate);

PRINT 'Cancellazione RPT...';
INSERT INTO rpt_aaaammgg SELECT * FROM rpt WHERE id_versamento IN (SELECT id FROM #svecchiamento_pagate);
DELETE FROM rpt WHERE id_versamento IN (SELECT id FROM #svecchiamento_pagate);

PRINT 'Cancellazione voci delle pendenze...';
INSERT INTO singoli_versamenti_aaaammgg SELECT * FROM singoli_versamenti WHERE id_versamento IN (SELECT id FROM #svecchiamento_pagate);
DELETE FROM singoli_versamenti WHERE id_versamento IN (SELECT id FROM #svecchiamento_pagate);

PRINT 'Cancellazione pendenze...';
INSERT INTO versamenti_aaaammgg SELECT * FROM versamenti WHERE id IN (SELECT id FROM #svecchiamento_pagate);
DELETE FROM versamenti WHERE id IN (SELECT id FROM #svecchiamento_pagate);

COMMIT TRANSACTION;

DROP TABLE #svecchiamento_pagate;
