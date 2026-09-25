-- =============================================================================
-- Svecchiamento PENDENZE SCADUTE NON PAGATE - PostgreSQL
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
-- temporanea, eliminata al COMMIT. I large object degli allegati vengono rimossi
-- prima delle righe che li referenziano, altrimenti resterebbero orfani nel
-- catalogo.
--
-- Uso: psql -v ON_ERROR_STOP=1 -h <host> -U <utente> -d <database> -f pendenze_scadute_non_pagate.sql
--
-- Il valore qui sotto e' il default. svecchiamento-db.sh lo sostituisce quando
-- gli si passa --retention-pendenze_scadute_non_pagate.
-- =============================================================================

\set retention_pendenze '\'12 months\''

\set end_pendenze 'CURRENT_DATE - interval :retention_pendenze '

\echo ''
\echo '--- Svecchiamento PENDENZE SCADUTE NON PAGATE ---'
\echo 'Retention: ' :retention_pendenze

BEGIN;

\echo 'Selezione delle pendenze da cancellare...'
CREATE TEMP TABLE svecchiamento_pendenze ON COMMIT DROP AS
SELECT v.id FROM versamenti v
WHERE v.stato_versamento = 'NON_ESEGUITO'
  AND v.data_scadenza < :end_pendenze
  AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN rendicontazioni r ON r.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
  AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN pagamenti p ON p.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
  AND NOT EXISTS (SELECT 1 FROM rpt JOIN pagamenti p ON p.id_rpt = rpt.id WHERE rpt.id_versamento = v.id);

ALTER TABLE svecchiamento_pendenze ADD PRIMARY KEY (id);
ANALYZE svecchiamento_pendenze;

SELECT count(*) AS pendenze_da_cancellare FROM svecchiamento_pendenze;

\echo 'Cancellazione notifiche...'
DELETE FROM notifiche WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento IN (SELECT id FROM svecchiamento_pendenze));

\echo 'Cancellazione notifiche App IO...'
DELETE FROM notifiche_app_io WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento IN (SELECT id FROM svecchiamento_pendenze));
DELETE FROM notifiche_app_io WHERE id_versamento IN (SELECT id FROM svecchiamento_pendenze);

\echo 'Cancellazione promemoria...'
DELETE FROM promemoria WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento IN (SELECT id FROM svecchiamento_pendenze));
DELETE FROM promemoria WHERE id_versamento IN (SELECT id FROM svecchiamento_pendenze);

\echo 'Cancellazione operazioni...'
DELETE FROM operazioni WHERE id_stampa IN (SELECT id FROM stampe WHERE id_versamento IN (SELECT id FROM svecchiamento_pendenze));
DELETE FROM operazioni WHERE id_versamento IN (SELECT id FROM svecchiamento_pendenze);

\echo 'Cancellazione stampe...'
DELETE FROM stampe WHERE id_versamento IN (SELECT id FROM svecchiamento_pendenze);

\echo 'Rimozione large objects (raw_contenuto) dagli allegati...'
SELECT lo_unlink(raw_contenuto) FROM allegati WHERE id_versamento IN (SELECT id FROM svecchiamento_pendenze) AND raw_contenuto IS NOT NULL;

\echo 'Cancellazione allegati...'
DELETE FROM allegati WHERE id_versamento IN (SELECT id FROM svecchiamento_pendenze);

\echo 'Cancellazione RPT...'
DELETE FROM rpt WHERE id_versamento IN (SELECT id FROM svecchiamento_pendenze);

\echo 'Cancellazione voci delle pendenze...'
DELETE FROM singoli_versamenti WHERE id_versamento IN (SELECT id FROM svecchiamento_pendenze);

\echo 'Cancellazione pendenze...'
DELETE FROM versamenti WHERE id IN (SELECT id FROM svecchiamento_pendenze);

COMMIT;

-- VACUUM non puo' stare in una transazione: va dopo il COMMIT.
\echo 'VACUUM ANALYZE delle tabelle svecchiate...'
VACUUM ANALYZE singoli_versamenti;
VACUUM ANALYZE rpt;
VACUUM ANALYZE versamenti;
VACUUM ANALYZE notifiche;
VACUUM ANALYZE promemoria;
VACUUM ANALYZE stampe;
