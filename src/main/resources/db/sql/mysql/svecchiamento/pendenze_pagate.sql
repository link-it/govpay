-- =============================================================================
-- Svecchiamento PENDENZE PAGATE - MySQL/MariaDB
--
-- Elimina le pendenze pagate il cui ultimo pagamento e' piu' vecchio di
-- retention_pendenze_pagate mesi, con le RPT, i pagamenti e tutto cio' che le
-- referenzia, nell'ordine imposto dalle chiavi esterne.
--
-- Una pendenza rientra se:
--   - e' in uno stato di pagamento eseguito: ESEGUITO, PARZIALMENTE_ESEGUITO,
--     ESEGUITO_ALTRO_CANALE, ESEGUITO_SENZA_RPT, INCASSATO;
--   - e' stata pagata con ricevuta piu' di retention_pendenze_pagate mesi fa:
--     data_pagamento e' valorizzata, all'elaborazione della RT, solo per le
--     pendenze pagate con ricevuta;
--   - nessuno dei suoi pagamenti, sulle voci o sulle RPT, e' piu' recente della
--     soglia: data_pagamento e' quella dell'ultima RT elaborata, e una RT
--     recuperata in ritardo potrebbe essere piu' vecchia di un altro pagamento;
--   - nessuna sua voce e nessun suo pagamento e' rendicontato. Le pendenze
--     rendicontate sono svecchiate insieme ai loro flussi, dalla sezione
--     flussi_rendicontazione, e le pendenze pagate senza ricevuta con esse.
--
-- I documenti a cui le pendenze appartengono non vengono cancellati, come per
-- le pendenze scadute. Gli incassi neppure: sono i pagamenti a referenziarli.
--
-- Gli id da cancellare vengono raccolti una volta sola in una tabella
-- temporanea. Su MySQL e' anche necessario: una DELETE non puo' leggere in
-- sottoquery la tabella da cui cancella. E poiche' una tabella temporanea non
-- puo' comparire due volte nella stessa istruzione, le cancellazioni per RPT e
-- per pendenza sono DELETE separate.
--
-- Le RT possono essere salvate su file prima della cancellazione: la riga
-- marcata export-rt qui sotto viene sostituita da svecchiamento-db.sh con il
-- contenuto di pendenze_pagate-export-rt.sql quando gli si passa --export-rt.
-- Eseguito da solo, lo script non salva le RT.
--
-- Uso: mysql -h <host> -u <utente> -D <database> < pendenze_pagate.sql
--
-- Il valore qui sotto e' il default. svecchiamento-db.sh lo sostituisce quando
-- gli si passa --retention-pendenze_pagate.
-- =============================================================================

SET @retention_pendenze_pagate = 24;

SET @end_pendenze_pagate = CURRENT_DATE - INTERVAL @retention_pendenze_pagate MONTH;

SELECT '--- Svecchiamento PENDENZE PAGATE ---' AS stato;
SELECT CONCAT('Retention: ', @retention_pendenze_pagate, ' mesi') AS stato;

DROP TEMPORARY TABLE IF EXISTS svecchiamento_pagate;
CREATE TEMPORARY TABLE svecchiamento_pagate (id BIGINT NOT NULL PRIMARY KEY);

START TRANSACTION;

SELECT 'Selezione delle pendenze da cancellare...' AS stato;
INSERT INTO svecchiamento_pagate (id)
SELECT v.id FROM versamenti v
WHERE v.stato_versamento IN ('ESEGUITO', 'PARZIALMENTE_ESEGUITO', 'ESEGUITO_ALTRO_CANALE', 'ESEGUITO_SENZA_RPT', 'INCASSATO')
  AND v.data_pagamento < @end_pendenze_pagate
  AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN pagamenti p ON p.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id AND p.data_pagamento >= @end_pendenze_pagate)
  AND NOT EXISTS (SELECT 1 FROM rpt JOIN pagamenti p ON p.id_rpt = rpt.id WHERE rpt.id_versamento = v.id AND p.data_pagamento >= @end_pendenze_pagate)
  AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN rendicontazioni r ON r.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
  AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN pagamenti p ON p.id_singolo_versamento = sv.id JOIN rendicontazioni r ON r.id_pagamento = p.id WHERE sv.id_versamento = v.id)
  AND NOT EXISTS (SELECT 1 FROM rpt JOIN pagamenti p ON p.id_rpt = rpt.id JOIN rendicontazioni r ON r.id_pagamento = p.id WHERE rpt.id_versamento = v.id);

SELECT COUNT(*) AS pendenze_da_cancellare FROM svecchiamento_pagate;

-- export-rt

SELECT 'Cancellazione pagamenti...' AS stato;
DELETE FROM pagamenti WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento IN (SELECT id FROM svecchiamento_pagate));
DELETE FROM pagamenti WHERE id_singolo_versamento IN (SELECT id FROM singoli_versamenti WHERE id_versamento IN (SELECT id FROM svecchiamento_pagate));

SELECT 'Cancellazione notifiche...' AS stato;
DELETE FROM notifiche WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento IN (SELECT id FROM svecchiamento_pagate));

SELECT 'Cancellazione notifiche App IO...' AS stato;
DELETE FROM notifiche_app_io WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento IN (SELECT id FROM svecchiamento_pagate));
DELETE FROM notifiche_app_io WHERE id_versamento IN (SELECT id FROM svecchiamento_pagate);

SELECT 'Cancellazione promemoria...' AS stato;
DELETE FROM promemoria WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento IN (SELECT id FROM svecchiamento_pagate));
DELETE FROM promemoria WHERE id_versamento IN (SELECT id FROM svecchiamento_pagate);

SELECT 'Cancellazione operazioni...' AS stato;
DELETE FROM operazioni WHERE id_stampa IN (SELECT id FROM stampe WHERE id_versamento IN (SELECT id FROM svecchiamento_pagate));
DELETE FROM operazioni WHERE id_versamento IN (SELECT id FROM svecchiamento_pagate);

SELECT 'Cancellazione stampe...' AS stato;
DELETE FROM stampe WHERE id_versamento IN (SELECT id FROM svecchiamento_pagate);

SELECT 'Cancellazione allegati...' AS stato;
DELETE FROM allegati WHERE id_versamento IN (SELECT id FROM svecchiamento_pagate);

SELECT 'Cancellazione RPT...' AS stato;
DELETE FROM rpt WHERE id_versamento IN (SELECT id FROM svecchiamento_pagate);

SELECT 'Cancellazione voci delle pendenze...' AS stato;
DELETE FROM singoli_versamenti WHERE id_versamento IN (SELECT id FROM svecchiamento_pagate);

SELECT 'Cancellazione pendenze...' AS stato;
DELETE FROM versamenti WHERE id IN (SELECT id FROM svecchiamento_pagate);

COMMIT;

DROP TEMPORARY TABLE IF EXISTS svecchiamento_pagate;
