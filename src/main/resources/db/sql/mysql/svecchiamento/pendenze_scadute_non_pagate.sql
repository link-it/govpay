-- =============================================================================
-- Svecchiamento PENDENZE SCADUTE NON PAGATE - MySQL/MariaDB
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
-- temporanea. Su MySQL e' anche necessario: una DELETE non puo' leggere in
-- sottoquery la tabella da cui cancella. E poiche' una tabella temporanea non
-- puo' comparire due volte nella stessa istruzione, le cancellazioni per RPT e
-- per pendenza sono DELETE separate.
--
-- Prima di essere cancellate, le righe vengono copiate in tabelle di archivio
-- con lo stesso nome e il suffisso _aaaammgg (notifiche, notifiche_app_io,
-- promemoria, operazioni, stampe, allegati, rpt, singoli_versamenti,
-- versamenti), create se non esistono con CREATE TABLE ... LIKE, che copia
-- indici e chiave primaria ma non le chiavi esterne (CREATE TABLE ... SELECT
-- non e' ammessa con GTID attivo). Le righe sono copiate nella stessa
-- transazione delle cancellazioni: l'archivio contiene esattamente le righe
-- cancellate. svecchiamento-db.sh sostituisce aaaammgg con la data
-- dell'esecuzione; eseguito da solo, lo script archivia nelle tabelle
-- _aaaammgg. Gli eventi non vengono archiviati. Salvare ed eliminare le
-- tabelle di archivio e' compito del DBA.
--
-- Uso: mysql -h <host> -u <utente> -D <database> < pendenze_scadute_non_pagate.sql
--
-- Il valore qui sotto e' il default. svecchiamento-db.sh lo sostituisce quando
-- gli si passa --retention-pendenze_scadute_non_pagate.
-- =============================================================================

SET @retention_pendenze = 365;

SET @end_pendenze = CURRENT_DATE - INTERVAL @retention_pendenze DAY;

SELECT '--- Svecchiamento PENDENZE SCADUTE NON PAGATE ---' AS stato;
SELECT CONCAT('Retention: ', @retention_pendenze, ' giorni') AS stato;

DROP TEMPORARY TABLE IF EXISTS svecchiamento_pendenze;
CREATE TEMPORARY TABLE svecchiamento_pendenze (id BIGINT NOT NULL PRIMARY KEY);

SELECT 'Creazione delle tabelle di archivio...' AS stato;
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
INSERT INTO svecchiamento_pendenze (id)
SELECT v.id FROM versamenti v
WHERE v.stato_versamento = 'NON_ESEGUITO'
  AND v.data_scadenza < @end_pendenze
  AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN rendicontazioni r ON r.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
  AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN pagamenti p ON p.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
  AND NOT EXISTS (SELECT 1 FROM rpt JOIN pagamenti p ON p.id_rpt = rpt.id WHERE rpt.id_versamento = v.id);

SELECT COUNT(*) AS pendenze_da_cancellare FROM svecchiamento_pendenze;

SELECT 'Cancellazione notifiche...' AS stato;
INSERT INTO notifiche_aaaammgg SELECT * FROM notifiche WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento IN (SELECT id FROM svecchiamento_pendenze));
DELETE FROM notifiche WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento IN (SELECT id FROM svecchiamento_pendenze));

SELECT 'Cancellazione notifiche App IO...' AS stato;
INSERT INTO notifiche_app_io_aaaammgg SELECT * FROM notifiche_app_io WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento IN (SELECT id FROM svecchiamento_pendenze));
DELETE FROM notifiche_app_io WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento IN (SELECT id FROM svecchiamento_pendenze));
INSERT INTO notifiche_app_io_aaaammgg SELECT * FROM notifiche_app_io WHERE id_versamento IN (SELECT id FROM svecchiamento_pendenze);
DELETE FROM notifiche_app_io WHERE id_versamento IN (SELECT id FROM svecchiamento_pendenze);

SELECT 'Cancellazione promemoria...' AS stato;
INSERT INTO promemoria_aaaammgg SELECT * FROM promemoria WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento IN (SELECT id FROM svecchiamento_pendenze));
DELETE FROM promemoria WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento IN (SELECT id FROM svecchiamento_pendenze));
INSERT INTO promemoria_aaaammgg SELECT * FROM promemoria WHERE id_versamento IN (SELECT id FROM svecchiamento_pendenze);
DELETE FROM promemoria WHERE id_versamento IN (SELECT id FROM svecchiamento_pendenze);

SELECT 'Cancellazione operazioni...' AS stato;
INSERT INTO operazioni_aaaammgg SELECT * FROM operazioni WHERE id_stampa IN (SELECT id FROM stampe WHERE id_versamento IN (SELECT id FROM svecchiamento_pendenze));
DELETE FROM operazioni WHERE id_stampa IN (SELECT id FROM stampe WHERE id_versamento IN (SELECT id FROM svecchiamento_pendenze));
INSERT INTO operazioni_aaaammgg SELECT * FROM operazioni WHERE id_versamento IN (SELECT id FROM svecchiamento_pendenze);
DELETE FROM operazioni WHERE id_versamento IN (SELECT id FROM svecchiamento_pendenze);

SELECT 'Cancellazione stampe...' AS stato;
INSERT INTO stampe_aaaammgg SELECT * FROM stampe WHERE id_versamento IN (SELECT id FROM svecchiamento_pendenze);
DELETE FROM stampe WHERE id_versamento IN (SELECT id FROM svecchiamento_pendenze);

SELECT 'Cancellazione allegati...' AS stato;
INSERT INTO allegati_aaaammgg SELECT * FROM allegati WHERE id_versamento IN (SELECT id FROM svecchiamento_pendenze);
DELETE FROM allegati WHERE id_versamento IN (SELECT id FROM svecchiamento_pendenze);

SELECT 'Cancellazione RPT...' AS stato;
INSERT INTO rpt_aaaammgg SELECT * FROM rpt WHERE id_versamento IN (SELECT id FROM svecchiamento_pendenze);
DELETE FROM rpt WHERE id_versamento IN (SELECT id FROM svecchiamento_pendenze);

SELECT 'Cancellazione voci delle pendenze...' AS stato;
INSERT INTO singoli_versamenti_aaaammgg SELECT * FROM singoli_versamenti WHERE id_versamento IN (SELECT id FROM svecchiamento_pendenze);
DELETE FROM singoli_versamenti WHERE id_versamento IN (SELECT id FROM svecchiamento_pendenze);

SELECT 'Cancellazione pendenze...' AS stato;
INSERT INTO versamenti_aaaammgg SELECT * FROM versamenti WHERE id IN (SELECT id FROM svecchiamento_pendenze);
DELETE FROM versamenti WHERE id IN (SELECT id FROM svecchiamento_pendenze);

COMMIT;

DROP TEMPORARY TABLE IF EXISTS svecchiamento_pendenze;
