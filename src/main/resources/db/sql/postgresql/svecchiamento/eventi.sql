-- =============================================================================
-- Svecchiamento EVENTI - PostgreSQL
--
-- Svecchia il giornale degli eventi per eta'. E' la tabella che cresce di piu'.
-- La condizione e' sulla colonna data, coperta dall'indice idx_evt_data; nessuna
-- tabella referenzia eventi, quindi la cancellazione non e' vincolata da chiavi
-- esterne.
--
-- Se si esegue anche lo svecchiamento dei tracciati, farlo prima: elimina gli
-- eventi collegati ai tracciati scaduti a prescindere dalla loro eta'.
--
-- Uso: psql -v ON_ERROR_STOP=1 -h <host> -U <utente> -d <database> -f eventi.sql
--
-- Il valore qui sotto e' il default. svecchiamento-db.sh lo sostituisce quando
-- gli si passa --retention-eventi.
-- =============================================================================

\set retention_eventi '\'90 days\''

\set end_eventi 'CURRENT_DATE - interval :retention_eventi '

\echo ''
\echo '--- Svecchiamento EVENTI ---'
\echo 'Retention: ' :retention_eventi

BEGIN;

\echo 'Cancellazione eventi...'
DELETE FROM eventi WHERE data < :end_eventi;

COMMIT;
