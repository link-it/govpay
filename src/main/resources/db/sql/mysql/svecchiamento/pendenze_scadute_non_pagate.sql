-- =============================================================================
-- Svecchiamento PENDENZE SCADUTE NON PAGATE - MySQL/MariaDB
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
-- temporanea. Su MySQL e' anche necessario: una DELETE non puo' leggere in
-- sottoquery la tabella da cui cancella. E poiche' una tabella temporanea non
-- puo' comparire due volte nella stessa istruzione, le cancellazioni per RPT e
-- per pendenza sono DELETE separate.
--
-- Uso: mysql -h <host> -u <utente> -D <database> < pendenze_scadute_non_pagate.sql
--
-- Il valore qui sotto e' il default. svecchiamento-db.sh lo sostituisce quando
-- gli si passa --retention-pendenze_scadute_non_pagate.
-- =============================================================================

SET @retention_pendenze = 12;

SET @end_pendenze = CURRENT_DATE - INTERVAL @retention_pendenze MONTH;

SELECT '--- Svecchiamento PENDENZE SCADUTE NON PAGATE ---' AS stato;
SELECT CONCAT('Retention: ', @retention_pendenze, ' mesi') AS stato;

DROP TEMPORARY TABLE IF EXISTS svecchiamento_pendenze;
CREATE TEMPORARY TABLE svecchiamento_pendenze (id BIGINT NOT NULL PRIMARY KEY);

START TRANSACTION;

SELECT 'Selezione delle pendenze da cancellare...' AS stato;
INSERT INTO svecchiamento_pendenze (id)
SELECT v.id FROM versamenti v
WHERE v.stato_versamento = 'NON_ESEGUITO'
  AND v.data_scadenza < @end_pendenze
  AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN rendicontazioni r ON r.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
  AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN pagamenti p ON p.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
  AND NOT EXISTS (SELECT 1 FROM rpt JOIN pagamenti p ON p.id_rpt = rpt.id WHERE rpt.id_versamento = v.id);

SELECT COUNT(*) AS pendenze_da_cancellare FROM svecchiamento_pendenze;

SELECT 'Cancellazione notifiche...' AS stato;
DELETE FROM notifiche WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento IN (SELECT id FROM svecchiamento_pendenze));

SELECT 'Cancellazione notifiche App IO...' AS stato;
DELETE FROM notifiche_app_io WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento IN (SELECT id FROM svecchiamento_pendenze));
DELETE FROM notifiche_app_io WHERE id_versamento IN (SELECT id FROM svecchiamento_pendenze);

SELECT 'Cancellazione promemoria...' AS stato;
DELETE FROM promemoria WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento IN (SELECT id FROM svecchiamento_pendenze));
DELETE FROM promemoria WHERE id_versamento IN (SELECT id FROM svecchiamento_pendenze);

SELECT 'Cancellazione operazioni...' AS stato;
DELETE FROM operazioni WHERE id_stampa IN (SELECT id FROM stampe WHERE id_versamento IN (SELECT id FROM svecchiamento_pendenze));
DELETE FROM operazioni WHERE id_versamento IN (SELECT id FROM svecchiamento_pendenze);

SELECT 'Cancellazione stampe...' AS stato;
DELETE FROM stampe WHERE id_versamento IN (SELECT id FROM svecchiamento_pendenze);

SELECT 'Cancellazione allegati...' AS stato;
DELETE FROM allegati WHERE id_versamento IN (SELECT id FROM svecchiamento_pendenze);

SELECT 'Cancellazione RPT...' AS stato;
DELETE FROM rpt WHERE id_versamento IN (SELECT id FROM svecchiamento_pendenze);

SELECT 'Cancellazione voci delle pendenze...' AS stato;
DELETE FROM singoli_versamenti WHERE id_versamento IN (SELECT id FROM svecchiamento_pendenze);

SELECT 'Cancellazione pendenze...' AS stato;
DELETE FROM versamenti WHERE id IN (SELECT id FROM svecchiamento_pendenze);

COMMIT;

DROP TEMPORARY TABLE IF EXISTS svecchiamento_pendenze;
