-- =============================================================================
-- Svecchiamento FLUSSI DI RENDICONTAZIONE - PostgreSQL
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
-- dopo darebbe un altro risultato.
--
-- Le RT delle pendenze cancellate possono essere salvate su file prima della
-- cancellazione: la riga marcata export-rt qui sotto viene sostituita da
-- svecchiamento-db.sh con il contenuto di flussi_rendicontazione-export-rt.sql
-- quando gli si passa --export-rt. Allo stesso modo, con --export-fr, la riga
-- marcata export-fr diventa il contenuto di flussi_rendicontazione-export-fr.sql
-- e salva l'XML dei flussi. Eseguito da solo, lo script non salva nulla.
--
-- Uso: psql -v ON_ERROR_STOP=1 -h <host> -U <utente> -d <database> -f flussi_rendicontazione.sql
--
-- Il valore qui sotto e' il default. svecchiamento-db.sh lo sostituisce quando
-- gli si passa --retention-flussi_rendicontazione.
-- =============================================================================

\set retention_fr '\'24 months\''

\set end_fr 'CURRENT_DATE - interval :retention_fr '

\echo ''
\echo '--- Svecchiamento FLUSSI DI RENDICONTAZIONE ---'
\echo 'Retention: ' :retention_fr

BEGIN;

\echo 'Selezione dei flussi da cancellare...'
CREATE TEMP TABLE svecchiamento_fr ON COMMIT DROP AS
SELECT id FROM fr WHERE data_ora_flusso < :end_fr;
ALTER TABLE svecchiamento_fr ADD PRIMARY KEY (id);

\echo 'Selezione delle pendenze rendicontate da cancellare...'
-- Una rendicontazione riferisce la pendenza sulla voce, o sul pagamento, che a
-- sua volta e' legato alla voce o alla RPT. Le tre strade valgono sia per
-- trovare le pendenze dei flussi vecchi sia per escludere quelle rendicontate
-- anche in flussi recenti.
CREATE TEMP TABLE svecchiamento_fr_pendenze ON COMMIT DROP AS
SELECT v.id FROM versamenti v
WHERE (EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN rendicontazioni r ON r.id_singolo_versamento = sv.id JOIN fr f ON f.id = r.id_fr WHERE sv.id_versamento = v.id AND f.data_ora_flusso < :end_fr)
    OR EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN pagamenti p ON p.id_singolo_versamento = sv.id JOIN rendicontazioni r ON r.id_pagamento = p.id JOIN fr f ON f.id = r.id_fr WHERE sv.id_versamento = v.id AND f.data_ora_flusso < :end_fr)
    OR EXISTS (SELECT 1 FROM rpt JOIN pagamenti p ON p.id_rpt = rpt.id JOIN rendicontazioni r ON r.id_pagamento = p.id JOIN fr f ON f.id = r.id_fr WHERE rpt.id_versamento = v.id AND f.data_ora_flusso < :end_fr))
  AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN rendicontazioni r ON r.id_singolo_versamento = sv.id JOIN fr f ON f.id = r.id_fr WHERE sv.id_versamento = v.id AND f.data_ora_flusso >= :end_fr)
  AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN pagamenti p ON p.id_singolo_versamento = sv.id JOIN rendicontazioni r ON r.id_pagamento = p.id JOIN fr f ON f.id = r.id_fr WHERE sv.id_versamento = v.id AND f.data_ora_flusso >= :end_fr)
  AND NOT EXISTS (SELECT 1 FROM rpt JOIN pagamenti p ON p.id_rpt = rpt.id JOIN rendicontazioni r ON r.id_pagamento = p.id JOIN fr f ON f.id = r.id_fr WHERE rpt.id_versamento = v.id AND f.data_ora_flusso >= :end_fr)
  AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN pagamenti p ON p.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id AND p.data_pagamento >= :end_fr)
  AND NOT EXISTS (SELECT 1 FROM rpt JOIN pagamenti p ON p.id_rpt = rpt.id WHERE rpt.id_versamento = v.id AND p.data_pagamento >= :end_fr);
ALTER TABLE svecchiamento_fr_pendenze ADD PRIMARY KEY (id);
ANALYZE svecchiamento_fr_pendenze;

\echo 'Selezione degli incassi dei flussi da cancellare...'
CREATE TEMP TABLE svecchiamento_fr_incassi ON COMMIT DROP AS
SELECT DISTINCT id_incasso AS id FROM fr WHERE id IN (SELECT id FROM svecchiamento_fr) AND id_incasso IS NOT NULL;
ALTER TABLE svecchiamento_fr_incassi ADD PRIMARY KEY (id);

SELECT (SELECT count(*) FROM svecchiamento_fr) AS flussi_da_cancellare,
       (SELECT count(*) FROM svecchiamento_fr_pendenze) AS pendenze_da_cancellare;

-- export-rt

-- export-fr

\echo 'Cancellazione eventi dei flussi...'
DELETE FROM eventi WHERE id_fr IN (SELECT id FROM svecchiamento_fr);

\echo 'Cancellazione rendicontazioni dei flussi...'
DELETE FROM rendicontazioni WHERE id_fr IN (SELECT id FROM svecchiamento_fr);

\echo 'Cancellazione pagamenti...'
DELETE FROM pagamenti WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento IN (SELECT id FROM svecchiamento_fr_pendenze));
DELETE FROM pagamenti WHERE id_singolo_versamento IN (SELECT id FROM singoli_versamenti WHERE id_versamento IN (SELECT id FROM svecchiamento_fr_pendenze));

\echo 'Cancellazione notifiche...'
DELETE FROM notifiche WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento IN (SELECT id FROM svecchiamento_fr_pendenze));

\echo 'Cancellazione notifiche App IO...'
DELETE FROM notifiche_app_io WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento IN (SELECT id FROM svecchiamento_fr_pendenze));
DELETE FROM notifiche_app_io WHERE id_versamento IN (SELECT id FROM svecchiamento_fr_pendenze);

\echo 'Cancellazione promemoria...'
DELETE FROM promemoria WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento IN (SELECT id FROM svecchiamento_fr_pendenze));
DELETE FROM promemoria WHERE id_versamento IN (SELECT id FROM svecchiamento_fr_pendenze);

\echo 'Cancellazione operazioni...'
DELETE FROM operazioni WHERE id_stampa IN (SELECT id FROM stampe WHERE id_versamento IN (SELECT id FROM svecchiamento_fr_pendenze));
DELETE FROM operazioni WHERE id_versamento IN (SELECT id FROM svecchiamento_fr_pendenze);

\echo 'Cancellazione stampe...'
DELETE FROM stampe WHERE id_versamento IN (SELECT id FROM svecchiamento_fr_pendenze);

\echo 'Rimozione large objects (raw_contenuto) dagli allegati...'
SELECT lo_unlink(raw_contenuto) FROM allegati WHERE id_versamento IN (SELECT id FROM svecchiamento_fr_pendenze) AND raw_contenuto IS NOT NULL;

\echo 'Cancellazione allegati...'
DELETE FROM allegati WHERE id_versamento IN (SELECT id FROM svecchiamento_fr_pendenze);

\echo 'Cancellazione RPT...'
DELETE FROM rpt WHERE id_versamento IN (SELECT id FROM svecchiamento_fr_pendenze);

\echo 'Cancellazione voci delle pendenze...'
DELETE FROM singoli_versamenti WHERE id_versamento IN (SELECT id FROM svecchiamento_fr_pendenze);

\echo 'Cancellazione pendenze...'
DELETE FROM versamenti WHERE id IN (SELECT id FROM svecchiamento_fr_pendenze);

\echo 'Cancellazione flussi...'
DELETE FROM fr WHERE id IN (SELECT id FROM svecchiamento_fr);

\echo 'Cancellazione incassi dei flussi...'
UPDATE pagamenti SET id_incasso = NULL WHERE id_incasso IN (SELECT id FROM svecchiamento_fr_incassi);
DELETE FROM eventi WHERE id_incasso IN (SELECT id FROM svecchiamento_fr_incassi);
DELETE FROM incassi WHERE id IN (SELECT id FROM svecchiamento_fr_incassi);

COMMIT;

-- VACUUM non puo' stare in una transazione: va dopo il COMMIT.
\echo 'VACUUM ANALYZE delle tabelle svecchiate...'
VACUUM ANALYZE rendicontazioni;
VACUUM ANALYZE fr;
VACUUM ANALYZE incassi;
VACUUM ANALYZE pagamenti;
VACUUM ANALYZE singoli_versamenti;
VACUUM ANALYZE rpt;
VACUUM ANALYZE versamenti;
VACUUM ANALYZE notifiche;
VACUUM ANALYZE promemoria;
VACUUM ANALYZE stampe;
