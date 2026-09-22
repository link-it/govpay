-- =============================================================================
-- Svecchiamento TRACCIATI - PostgreSQL
--
-- Elimina i tracciati completati da piu' di retention_tracciati giorni, con le
-- operazioni e gli eventi che li referenziano, nell'ordine imposto dalle chiavi
-- esterne. I large object degli zip di stampa vengono rimossi prima dei
-- tracciati che li referenziano, altrimenti resterebbero orfani nel catalogo.
--
-- Uso: psql -v ON_ERROR_STOP=1 -h <host> -U <utente> -d <database> -f tracciati.sql
--
-- Il valore qui sotto e' il default. svecchiamento-db.sh lo sostituisce quando
-- gli si passa --retention-tracciati.
-- =============================================================================

\set retention_tracciati '\'7 days\''

\set end_tracciati 'CURRENT_DATE - interval :retention_tracciati '

\echo ''
\echo '--- Svecchiamento TRACCIATI ---'
\echo 'Retention: ' :retention_tracciati

BEGIN;

\echo 'Cancellazione eventi collegati ai tracciati...'
DELETE FROM eventi WHERE id_tracciato IN (SELECT id FROM tracciati WHERE data_completamento < :end_tracciati);

\echo 'Cancellazione operazioni collegate ai tracciati...'
DELETE FROM operazioni WHERE id_tracciato IN (SELECT id FROM tracciati WHERE data_completamento < :end_tracciati);

\echo 'Rimozione large objects (zip_stampe) dai tracciati...'
SELECT lo_unlink(zip_stampe) FROM tracciati WHERE data_completamento < :end_tracciati AND zip_stampe IS NOT NULL;

\echo 'Cancellazione tracciati...'
DELETE FROM tracciati WHERE data_completamento < :end_tracciati;

COMMIT;
