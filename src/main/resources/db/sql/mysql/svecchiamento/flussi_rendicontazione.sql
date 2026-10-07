-- =============================================================================
-- Svecchiamento FLUSSI DI RENDICONTAZIONE - MySQL/MariaDB
--
-- Elimina i flussi di rendicontazione con data_ora_flusso piu' vecchia di
-- retention_fr mesi, con le loro rendicontazioni e i loro eventi, le pendenze
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
-- Le RT delle pendenze cancellate possono essere salvate su file prima della
-- cancellazione: la riga marcata export-rt qui sotto viene sostituita da
-- svecchiamento-db.sh con il contenuto di flussi_rendicontazione-export-rt.sql
-- quando gli si passa --export-rt. Allo stesso modo, con --export-fr, la riga
-- marcata export-fr diventa il contenuto di flussi_rendicontazione-export-fr.sql
-- e salva l'XML dei flussi. Eseguito da solo, lo script non salva nulla.
--
-- Uso: mysql -h <host> -u <utente> -D <database> < flussi_rendicontazione.sql
--
-- Il valore qui sotto e' il default. svecchiamento-db.sh lo sostituisce quando
-- gli si passa --retention-flussi_rendicontazione.
-- =============================================================================

SET @retention_fr = 24;

SET @end_fr = CURRENT_DATE - INTERVAL @retention_fr MONTH;

SELECT '--- Svecchiamento FLUSSI DI RENDICONTAZIONE ---' AS stato;
SELECT CONCAT('Retention: ', @retention_fr, ' mesi') AS stato;

DROP TEMPORARY TABLE IF EXISTS svecchiamento_fr;
CREATE TEMPORARY TABLE svecchiamento_fr (id BIGINT NOT NULL PRIMARY KEY);
DROP TEMPORARY TABLE IF EXISTS svecchiamento_fr_pendenze;
CREATE TEMPORARY TABLE svecchiamento_fr_pendenze (id BIGINT NOT NULL PRIMARY KEY);
DROP TEMPORARY TABLE IF EXISTS svecchiamento_fr_incassi;
CREATE TEMPORARY TABLE svecchiamento_fr_incassi (id BIGINT NOT NULL PRIMARY KEY);

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

-- export-rt

-- export-fr

SELECT 'Cancellazione eventi dei flussi...' AS stato;
DELETE FROM eventi WHERE id_fr IN (SELECT id FROM svecchiamento_fr);

SELECT 'Cancellazione rendicontazioni dei flussi...' AS stato;
DELETE FROM rendicontazioni WHERE id_fr IN (SELECT id FROM svecchiamento_fr);

SELECT 'Cancellazione pagamenti...' AS stato;
DELETE FROM pagamenti WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento IN (SELECT id FROM svecchiamento_fr_pendenze));
DELETE FROM pagamenti WHERE id_singolo_versamento IN (SELECT id FROM singoli_versamenti WHERE id_versamento IN (SELECT id FROM svecchiamento_fr_pendenze));

SELECT 'Cancellazione notifiche...' AS stato;
DELETE FROM notifiche WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento IN (SELECT id FROM svecchiamento_fr_pendenze));

SELECT 'Cancellazione notifiche App IO...' AS stato;
DELETE FROM notifiche_app_io WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento IN (SELECT id FROM svecchiamento_fr_pendenze));
DELETE FROM notifiche_app_io WHERE id_versamento IN (SELECT id FROM svecchiamento_fr_pendenze);

SELECT 'Cancellazione promemoria...' AS stato;
DELETE FROM promemoria WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento IN (SELECT id FROM svecchiamento_fr_pendenze));
DELETE FROM promemoria WHERE id_versamento IN (SELECT id FROM svecchiamento_fr_pendenze);

SELECT 'Cancellazione operazioni...' AS stato;
DELETE FROM operazioni WHERE id_stampa IN (SELECT id FROM stampe WHERE id_versamento IN (SELECT id FROM svecchiamento_fr_pendenze));
DELETE FROM operazioni WHERE id_versamento IN (SELECT id FROM svecchiamento_fr_pendenze);

SELECT 'Cancellazione stampe...' AS stato;
DELETE FROM stampe WHERE id_versamento IN (SELECT id FROM svecchiamento_fr_pendenze);

SELECT 'Cancellazione allegati...' AS stato;
DELETE FROM allegati WHERE id_versamento IN (SELECT id FROM svecchiamento_fr_pendenze);

SELECT 'Cancellazione RPT...' AS stato;
DELETE FROM rpt WHERE id_versamento IN (SELECT id FROM svecchiamento_fr_pendenze);

SELECT 'Cancellazione voci delle pendenze...' AS stato;
DELETE FROM singoli_versamenti WHERE id_versamento IN (SELECT id FROM svecchiamento_fr_pendenze);

SELECT 'Cancellazione pendenze...' AS stato;
DELETE FROM versamenti WHERE id IN (SELECT id FROM svecchiamento_fr_pendenze);

SELECT 'Cancellazione flussi...' AS stato;
DELETE FROM fr WHERE id IN (SELECT id FROM svecchiamento_fr);

SELECT 'Cancellazione incassi dei flussi...' AS stato;
UPDATE pagamenti SET id_incasso = NULL WHERE id_incasso IN (SELECT id FROM svecchiamento_fr_incassi);
DELETE FROM eventi WHERE id_incasso IN (SELECT id FROM svecchiamento_fr_incassi);
DELETE FROM incassi WHERE id IN (SELECT id FROM svecchiamento_fr_incassi);

COMMIT;

DROP TEMPORARY TABLE IF EXISTS svecchiamento_fr;
DROP TEMPORARY TABLE IF EXISTS svecchiamento_fr_pendenze;
DROP TEMPORARY TABLE IF EXISTS svecchiamento_fr_incassi;
