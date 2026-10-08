-- =============================================================================
-- Svecchiamento PENDENZE PAGATE - MySQL/MariaDB
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
-- temporanea. Su MySQL e' anche necessario: una DELETE non puo' leggere in
-- sottoquery la tabella da cui cancella. E poiche' una tabella temporanea non
-- puo' comparire due volte nella stessa istruzione, le cancellazioni per RPT e
-- per pendenza sono DELETE separate.
--
-- Prima di essere cancellate, le righe vengono copiate in tabelle di archivio
-- con lo stesso nome e il suffisso _aaaammgg (pagamenti, notifiche,
-- notifiche_app_io, promemoria, operazioni, stampe, allegati, rpt,
-- singoli_versamenti, versamenti), create se non esistono con CREATE TABLE ...
-- LIKE, che copia indici e chiave primaria ma non le chiavi esterne (CREATE
-- TABLE ... SELECT non e' ammessa con GTID attivo). Le righe sono copiate
-- nella stessa transazione delle cancellazioni: l'archivio contiene
-- esattamente le righe cancellate. svecchiamento-db.sh sostituisce aaaammgg
-- con la data dell'esecuzione; eseguito da solo, lo script archivia nelle
-- tabelle _aaaammgg. Gli eventi non vengono archiviati. Salvare ed eliminare
-- le tabelle di archivio e' compito del DBA.
--
-- Uso: mysql -h <host> -u <utente> -D <database> < pendenze_pagate.sql
--
-- Il valore qui sotto e' il default. svecchiamento-db.sh lo sostituisce quando
-- gli si passa --retention-pendenze_pagate.
-- =============================================================================

SET @retention_pendenze_pagate = 730;

SET @end_pendenze_pagate = CURRENT_DATE - INTERVAL @retention_pendenze_pagate DAY;

SELECT '--- Svecchiamento PENDENZE PAGATE ---' AS stato;
SELECT CONCAT('Retention: ', @retention_pendenze_pagate, ' giorni') AS stato;

DROP TEMPORARY TABLE IF EXISTS svecchiamento_pagate;
CREATE TEMPORARY TABLE svecchiamento_pagate (id BIGINT NOT NULL PRIMARY KEY);

SELECT 'Creazione delle tabelle di archivio...' AS stato;
CREATE TABLE IF NOT EXISTS pagamenti_aaaammgg LIKE pagamenti;
CREATE TABLE IF NOT EXISTS notifiche_aaaammgg LIKE notifiche;
CREATE TABLE IF NOT EXISTS notifiche_app_io_aaaammgg LIKE notifiche_app_io;
CREATE TABLE IF NOT EXISTS promemoria_aaaammgg LIKE promemoria;
CREATE TABLE IF NOT EXISTS operazioni_aaaammgg LIKE operazioni;
CREATE TABLE IF NOT EXISTS stampe_aaaammgg LIKE stampe;
CREATE TABLE IF NOT EXISTS allegati_aaaammgg LIKE allegati;
CREATE TABLE IF NOT EXISTS rpt_aaaammgg LIKE rpt;
CREATE TABLE IF NOT EXISTS singoli_versamenti_aaaammgg LIKE singoli_versamenti;
CREATE TABLE IF NOT EXISTS versamenti_aaaammgg LIKE versamenti;

START TRANSACTION;

SELECT 'Selezione delle pendenze da cancellare...' AS stato;
INSERT INTO svecchiamento_pagate (id)
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

SELECT COUNT(*) AS pendenze_da_cancellare FROM svecchiamento_pagate;

SELECT 'Cancellazione pagamenti...' AS stato;
INSERT INTO pagamenti_aaaammgg SELECT * FROM pagamenti WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento IN (SELECT id FROM svecchiamento_pagate));
DELETE FROM pagamenti WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento IN (SELECT id FROM svecchiamento_pagate));
INSERT INTO pagamenti_aaaammgg SELECT * FROM pagamenti WHERE id_singolo_versamento IN (SELECT id FROM singoli_versamenti WHERE id_versamento IN (SELECT id FROM svecchiamento_pagate));
DELETE FROM pagamenti WHERE id_singolo_versamento IN (SELECT id FROM singoli_versamenti WHERE id_versamento IN (SELECT id FROM svecchiamento_pagate));

SELECT 'Cancellazione notifiche...' AS stato;
INSERT INTO notifiche_aaaammgg SELECT * FROM notifiche WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento IN (SELECT id FROM svecchiamento_pagate));
DELETE FROM notifiche WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento IN (SELECT id FROM svecchiamento_pagate));

SELECT 'Cancellazione notifiche App IO...' AS stato;
INSERT INTO notifiche_app_io_aaaammgg SELECT * FROM notifiche_app_io WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento IN (SELECT id FROM svecchiamento_pagate));
DELETE FROM notifiche_app_io WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento IN (SELECT id FROM svecchiamento_pagate));
INSERT INTO notifiche_app_io_aaaammgg SELECT * FROM notifiche_app_io WHERE id_versamento IN (SELECT id FROM svecchiamento_pagate);
DELETE FROM notifiche_app_io WHERE id_versamento IN (SELECT id FROM svecchiamento_pagate);

SELECT 'Cancellazione promemoria...' AS stato;
INSERT INTO promemoria_aaaammgg SELECT * FROM promemoria WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento IN (SELECT id FROM svecchiamento_pagate));
DELETE FROM promemoria WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento IN (SELECT id FROM svecchiamento_pagate));
INSERT INTO promemoria_aaaammgg SELECT * FROM promemoria WHERE id_versamento IN (SELECT id FROM svecchiamento_pagate);
DELETE FROM promemoria WHERE id_versamento IN (SELECT id FROM svecchiamento_pagate);

SELECT 'Cancellazione operazioni...' AS stato;
INSERT INTO operazioni_aaaammgg SELECT * FROM operazioni WHERE id_stampa IN (SELECT id FROM stampe WHERE id_versamento IN (SELECT id FROM svecchiamento_pagate));
DELETE FROM operazioni WHERE id_stampa IN (SELECT id FROM stampe WHERE id_versamento IN (SELECT id FROM svecchiamento_pagate));
INSERT INTO operazioni_aaaammgg SELECT * FROM operazioni WHERE id_versamento IN (SELECT id FROM svecchiamento_pagate);
DELETE FROM operazioni WHERE id_versamento IN (SELECT id FROM svecchiamento_pagate);

SELECT 'Cancellazione stampe...' AS stato;
INSERT INTO stampe_aaaammgg SELECT * FROM stampe WHERE id_versamento IN (SELECT id FROM svecchiamento_pagate);
DELETE FROM stampe WHERE id_versamento IN (SELECT id FROM svecchiamento_pagate);

SELECT 'Cancellazione allegati...' AS stato;
INSERT INTO allegati_aaaammgg SELECT * FROM allegati WHERE id_versamento IN (SELECT id FROM svecchiamento_pagate);
DELETE FROM allegati WHERE id_versamento IN (SELECT id FROM svecchiamento_pagate);

SELECT 'Cancellazione RPT...' AS stato;
INSERT INTO rpt_aaaammgg SELECT * FROM rpt WHERE id_versamento IN (SELECT id FROM svecchiamento_pagate);
DELETE FROM rpt WHERE id_versamento IN (SELECT id FROM svecchiamento_pagate);

SELECT 'Cancellazione voci delle pendenze...' AS stato;
INSERT INTO singoli_versamenti_aaaammgg SELECT * FROM singoli_versamenti WHERE id_versamento IN (SELECT id FROM svecchiamento_pagate);
DELETE FROM singoli_versamenti WHERE id_versamento IN (SELECT id FROM svecchiamento_pagate);

SELECT 'Cancellazione pendenze...' AS stato;
INSERT INTO versamenti_aaaammgg SELECT * FROM versamenti WHERE id IN (SELECT id FROM svecchiamento_pagate);
DELETE FROM versamenti WHERE id IN (SELECT id FROM svecchiamento_pagate);

COMMIT;

DROP TEMPORARY TABLE IF EXISTS svecchiamento_pagate;
