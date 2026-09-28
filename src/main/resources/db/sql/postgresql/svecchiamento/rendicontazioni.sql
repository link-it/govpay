-- =============================================================================
-- Svecchiamento RENDICONTAZIONI - PostgreSQL
--
-- Elimina i flussi di rendicontazione acquisiti da piu' di
-- retention_rendicontazioni mesi, con le loro rendicontazioni e gli eventi
-- collegati al flusso, nell'ordine imposto dalle chiavi esterne.
--
-- Gli eventi del flusso vengono cancellati qui, e per primi, anche quando la
-- sezione eventi e' attiva: non e' detto che lo sia, e comunque le due
-- cancellazioni convivono, perche' qui si tolgono solo gli eventi che
-- impedirebbero di cancellare il flusso.
--
-- Pagamenti, pendenze e incassi non vengono toccati: sono loro a essere
-- referenziati dalle rendicontazioni e dai flussi, non il contrario.
--
-- Due filtri restringono la selezione, e di default sono disattivi (0):
--   solo_obsoleti      = 1  solo i flussi obsoleti, cioe' le revisioni
--                           precedenti di un flusso ripubblicato
--   solo_non_incassati = 1  solo i flussi non riconciliati con un incasso
-- Attivi entrambi, valgono insieme.
--
-- Gli id da cancellare vengono raccolti una volta sola in una tabella
-- temporanea, eliminata al COMMIT.
--
-- Uso: psql -v ON_ERROR_STOP=1 -h <host> -U <utente> -d <database> -f rendicontazioni.sql
--
-- I valori qui sotto sono i default. svecchiamento-db.sh li sostituisce quando
-- gli si passa --retention-rendicontazioni, --solo-obsoleti o
-- --solo-non-incassati.
-- =============================================================================

\set retention_rendicontazioni '\'24 months\''
\set solo_obsoleti 0
\set solo_non_incassati 0

\set end_rendicontazioni 'CURRENT_DATE - interval :retention_rendicontazioni '

\echo ''
\echo '--- Svecchiamento RENDICONTAZIONI ---'
\echo 'Retention: ' :retention_rendicontazioni
\echo 'Solo obsoleti: ' :solo_obsoleti ', solo non incassati: ' :solo_non_incassati

BEGIN;

\echo 'Selezione dei flussi da cancellare...'
CREATE TEMP TABLE svecchiamento_fr ON COMMIT DROP AS
SELECT fr.id FROM fr
WHERE fr.data_acquisizione < :end_rendicontazioni
  AND (:solo_obsoleti = 0 OR fr.obsoleto = true)
  AND (:solo_non_incassati = 0 OR fr.id_incasso IS NULL);

ALTER TABLE svecchiamento_fr ADD PRIMARY KEY (id);
ANALYZE svecchiamento_fr;

SELECT count(*) AS flussi_da_cancellare FROM svecchiamento_fr;

\echo 'Cancellazione eventi dei flussi...'
DELETE FROM eventi WHERE id_fr IN (SELECT id FROM svecchiamento_fr);

\echo 'Cancellazione rendicontazioni...'
DELETE FROM rendicontazioni WHERE id_fr IN (SELECT id FROM svecchiamento_fr);

\echo 'Cancellazione flussi...'
DELETE FROM fr WHERE id IN (SELECT id FROM svecchiamento_fr);

COMMIT;

-- VACUUM non puo' stare in una transazione: va dopo il COMMIT.
\echo 'VACUUM ANALYZE delle tabelle svecchiate...'
VACUUM ANALYZE eventi;
VACUUM ANALYZE rendicontazioni;
VACUUM ANALYZE fr;
