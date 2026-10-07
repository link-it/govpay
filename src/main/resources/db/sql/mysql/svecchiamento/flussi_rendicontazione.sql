-- =============================================================================
-- Svecchiamento FLUSSI DI RENDICONTAZIONE - MySQL/MariaDB
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
-- Gli id da cancellare vengono raccolti all'inizio in tabelle temporanee: le
-- DELETE cambiano le rendicontazioni su cui si basa la selezione, e ripeterla
-- dopo darebbe un altro risultato. Su MySQL una tabella temporanea non puo'
-- comparire due volte nella stessa istruzione: per questo la selezione delle
-- pendenze confronta le date dei flussi invece di leggere svecchiamento_fr.
--
-- Prima di essere cancellate, le righe vengono copiate in tabelle di archivio
-- con lo stesso nome e il suffisso _aaaammgg (rendicontazioni, pagamenti,
-- notifiche, notifiche_app_io, promemoria, operazioni, stampe, allegati, rpt,
-- singoli_versamenti, versamenti, fr, incassi), create se non esistono con
-- CREATE TABLE ... LIKE, che copia indici e chiave primaria ma non le chiavi
-- esterne (CREATE TABLE ... SELECT non e' ammessa con GTID attivo). Le righe
-- sono copiate nella stessa transazione delle cancellazioni: l'archivio
-- contiene esattamente le righe cancellate. svecchiamento-db.sh sostituisce
-- aaaammgg con la data dell'esecuzione; eseguito da solo, lo script archivia
-- nelle tabelle _aaaammgg. Gli eventi non vengono archiviati. Salvare ed
-- eliminare le tabelle di archivio e' compito del DBA.
--
-- Uso: mysql -h <host> -u <utente> -D <database> < flussi_rendicontazione.sql
--
-- Il valore qui sotto e' il default. svecchiamento-db.sh lo sostituisce quando
-- gli si passa --retention-flussi_rendicontazione.
-- =============================================================================

SET @retention_fr = 730;

SET @end_fr = CURRENT_DATE - INTERVAL @retention_fr DAY;

SELECT '--- Svecchiamento FLUSSI DI RENDICONTAZIONE ---' AS stato;
SELECT CONCAT('Retention: ', @retention_fr, ' giorni') AS stato;

DROP TEMPORARY TABLE IF EXISTS svecchiamento_fr;
CREATE TEMPORARY TABLE svecchiamento_fr (id BIGINT NOT NULL PRIMARY KEY);
DROP TEMPORARY TABLE IF EXISTS svecchiamento_fr_pendenze;
CREATE TEMPORARY TABLE svecchiamento_fr_pendenze (id BIGINT NOT NULL PRIMARY KEY);
DROP TEMPORARY TABLE IF EXISTS svecchiamento_fr_incassi;
CREATE TEMPORARY TABLE svecchiamento_fr_incassi (id BIGINT NOT NULL PRIMARY KEY);

SELECT 'Creazione delle tabelle di archivio...' AS stato;
CREATE TABLE IF NOT EXISTS rendicontazioni_aaaammgg LIKE rendicontazioni;
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
CREATE TABLE IF NOT EXISTS fr_aaaammgg LIKE fr;
CREATE TABLE IF NOT EXISTS incassi_aaaammgg LIKE incassi;

START TRANSACTION;

SELECT 'Selezione dei flussi da cancellare...' AS stato;
INSERT INTO svecchiamento_fr (id) SELECT id FROM fr WHERE data_ora_flusso < @end_fr;

SELECT 'Selezione delle pendenze rendicontate da cancellare...' AS stato;
-- Una rendicontazione riferisce la pendenza sulla voce, o sul pagamento, che a
-- sua volta e' legato alla voce o alla RPT. Le tre strade valgono sia per
-- trovare le pendenze dei flussi vecchi sia per escludere quelle rendicontate
-- anche in flussi recenti.
INSERT INTO svecchiamento_fr_pendenze (id)
SELECT v.id FROM versamenti v
WHERE (EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN rendicontazioni r ON r.id_singolo_versamento = sv.id JOIN fr f ON f.id = r.id_fr WHERE sv.id_versamento = v.id AND f.data_ora_flusso < @end_fr)
    OR EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN pagamenti p ON p.id_singolo_versamento = sv.id JOIN rendicontazioni r ON r.id_pagamento = p.id JOIN fr f ON f.id = r.id_fr WHERE sv.id_versamento = v.id AND f.data_ora_flusso < @end_fr)
    OR EXISTS (SELECT 1 FROM rpt JOIN pagamenti p ON p.id_rpt = rpt.id JOIN rendicontazioni r ON r.id_pagamento = p.id JOIN fr f ON f.id = r.id_fr WHERE rpt.id_versamento = v.id AND f.data_ora_flusso < @end_fr))
  AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN rendicontazioni r ON r.id_singolo_versamento = sv.id JOIN fr f ON f.id = r.id_fr WHERE sv.id_versamento = v.id AND f.data_ora_flusso >= @end_fr)
  AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN pagamenti p ON p.id_singolo_versamento = sv.id JOIN rendicontazioni r ON r.id_pagamento = p.id JOIN fr f ON f.id = r.id_fr WHERE sv.id_versamento = v.id AND f.data_ora_flusso >= @end_fr)
  AND NOT EXISTS (SELECT 1 FROM rpt JOIN pagamenti p ON p.id_rpt = rpt.id JOIN rendicontazioni r ON r.id_pagamento = p.id JOIN fr f ON f.id = r.id_fr WHERE rpt.id_versamento = v.id AND f.data_ora_flusso >= @end_fr)
  AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN pagamenti p ON p.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id AND p.data_pagamento >= @end_fr)
  AND NOT EXISTS (SELECT 1 FROM rpt JOIN pagamenti p ON p.id_rpt = rpt.id WHERE rpt.id_versamento = v.id AND p.data_pagamento >= @end_fr);

SELECT 'Selezione degli incassi dei flussi da cancellare...' AS stato;
INSERT INTO svecchiamento_fr_incassi (id) SELECT DISTINCT id_incasso FROM fr WHERE id IN (SELECT id FROM svecchiamento_fr) AND id_incasso IS NOT NULL;

SELECT COUNT(*) AS flussi_da_cancellare FROM svecchiamento_fr;
SELECT COUNT(*) AS pendenze_da_cancellare FROM svecchiamento_fr_pendenze;

SELECT 'Cancellazione eventi dei flussi...' AS stato;
DELETE FROM eventi WHERE id_fr IN (SELECT id FROM svecchiamento_fr);

SELECT 'Cancellazione rendicontazioni dei flussi...' AS stato;
INSERT INTO rendicontazioni_aaaammgg SELECT * FROM rendicontazioni WHERE id_fr IN (SELECT id FROM svecchiamento_fr);
DELETE FROM rendicontazioni WHERE id_fr IN (SELECT id FROM svecchiamento_fr);

SELECT 'Cancellazione pagamenti...' AS stato;
INSERT INTO pagamenti_aaaammgg SELECT * FROM pagamenti WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento IN (SELECT id FROM svecchiamento_fr_pendenze));
DELETE FROM pagamenti WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento IN (SELECT id FROM svecchiamento_fr_pendenze));
INSERT INTO pagamenti_aaaammgg SELECT * FROM pagamenti WHERE id_singolo_versamento IN (SELECT id FROM singoli_versamenti WHERE id_versamento IN (SELECT id FROM svecchiamento_fr_pendenze));
DELETE FROM pagamenti WHERE id_singolo_versamento IN (SELECT id FROM singoli_versamenti WHERE id_versamento IN (SELECT id FROM svecchiamento_fr_pendenze));

SELECT 'Cancellazione notifiche...' AS stato;
INSERT INTO notifiche_aaaammgg SELECT * FROM notifiche WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento IN (SELECT id FROM svecchiamento_fr_pendenze));
DELETE FROM notifiche WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento IN (SELECT id FROM svecchiamento_fr_pendenze));

SELECT 'Cancellazione notifiche App IO...' AS stato;
INSERT INTO notifiche_app_io_aaaammgg SELECT * FROM notifiche_app_io WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento IN (SELECT id FROM svecchiamento_fr_pendenze));
DELETE FROM notifiche_app_io WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento IN (SELECT id FROM svecchiamento_fr_pendenze));
INSERT INTO notifiche_app_io_aaaammgg SELECT * FROM notifiche_app_io WHERE id_versamento IN (SELECT id FROM svecchiamento_fr_pendenze);
DELETE FROM notifiche_app_io WHERE id_versamento IN (SELECT id FROM svecchiamento_fr_pendenze);

SELECT 'Cancellazione promemoria...' AS stato;
INSERT INTO promemoria_aaaammgg SELECT * FROM promemoria WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento IN (SELECT id FROM svecchiamento_fr_pendenze));
DELETE FROM promemoria WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento IN (SELECT id FROM svecchiamento_fr_pendenze));
INSERT INTO promemoria_aaaammgg SELECT * FROM promemoria WHERE id_versamento IN (SELECT id FROM svecchiamento_fr_pendenze);
DELETE FROM promemoria WHERE id_versamento IN (SELECT id FROM svecchiamento_fr_pendenze);

SELECT 'Cancellazione operazioni...' AS stato;
INSERT INTO operazioni_aaaammgg SELECT * FROM operazioni WHERE id_stampa IN (SELECT id FROM stampe WHERE id_versamento IN (SELECT id FROM svecchiamento_fr_pendenze));
DELETE FROM operazioni WHERE id_stampa IN (SELECT id FROM stampe WHERE id_versamento IN (SELECT id FROM svecchiamento_fr_pendenze));
INSERT INTO operazioni_aaaammgg SELECT * FROM operazioni WHERE id_versamento IN (SELECT id FROM svecchiamento_fr_pendenze);
DELETE FROM operazioni WHERE id_versamento IN (SELECT id FROM svecchiamento_fr_pendenze);

SELECT 'Cancellazione stampe...' AS stato;
INSERT INTO stampe_aaaammgg SELECT * FROM stampe WHERE id_versamento IN (SELECT id FROM svecchiamento_fr_pendenze);
DELETE FROM stampe WHERE id_versamento IN (SELECT id FROM svecchiamento_fr_pendenze);

SELECT 'Cancellazione allegati...' AS stato;
INSERT INTO allegati_aaaammgg SELECT * FROM allegati WHERE id_versamento IN (SELECT id FROM svecchiamento_fr_pendenze);
DELETE FROM allegati WHERE id_versamento IN (SELECT id FROM svecchiamento_fr_pendenze);

SELECT 'Cancellazione RPT...' AS stato;
INSERT INTO rpt_aaaammgg SELECT * FROM rpt WHERE id_versamento IN (SELECT id FROM svecchiamento_fr_pendenze);
DELETE FROM rpt WHERE id_versamento IN (SELECT id FROM svecchiamento_fr_pendenze);

SELECT 'Cancellazione voci delle pendenze...' AS stato;
INSERT INTO singoli_versamenti_aaaammgg SELECT * FROM singoli_versamenti WHERE id_versamento IN (SELECT id FROM svecchiamento_fr_pendenze);
DELETE FROM singoli_versamenti WHERE id_versamento IN (SELECT id FROM svecchiamento_fr_pendenze);

SELECT 'Cancellazione pendenze...' AS stato;
INSERT INTO versamenti_aaaammgg SELECT * FROM versamenti WHERE id IN (SELECT id FROM svecchiamento_fr_pendenze);
DELETE FROM versamenti WHERE id IN (SELECT id FROM svecchiamento_fr_pendenze);

SELECT 'Cancellazione flussi...' AS stato;
INSERT INTO fr_aaaammgg SELECT * FROM fr WHERE id IN (SELECT id FROM svecchiamento_fr);
DELETE FROM fr WHERE id IN (SELECT id FROM svecchiamento_fr);

SELECT 'Cancellazione incassi dei flussi...' AS stato;
UPDATE pagamenti SET id_incasso = NULL WHERE id_incasso IN (SELECT id FROM svecchiamento_fr_incassi);
DELETE FROM eventi WHERE id_incasso IN (SELECT id FROM svecchiamento_fr_incassi);
INSERT INTO incassi_aaaammgg SELECT * FROM incassi WHERE id IN (SELECT id FROM svecchiamento_fr_incassi);
DELETE FROM incassi WHERE id IN (SELECT id FROM svecchiamento_fr_incassi);

COMMIT;

DROP TEMPORARY TABLE IF EXISTS svecchiamento_fr;
DROP TEMPORARY TABLE IF EXISTS svecchiamento_fr_pendenze;
DROP TEMPORARY TABLE IF EXISTS svecchiamento_fr_incassi;
