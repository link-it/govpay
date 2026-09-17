-- =============================================================================
-- Script di svecchiamento dati GovPay - PostgreSQL
--
-- Uso: psql -h <host> -U <user> -d <database> -f svecchiamento.sql
--
-- Parametri (impostare prima dell'esecuzione):
-- =============================================================================

-- Retention tracciati
\set retention_tracciati '\'7 days\''

-- Retention eventi del giornale
\set retention_eventi '\'90 days\''

-- =============================================================================

\echo '=== Avvio svecchiamento GovPay ==='
SELECT now() AS inizio_svecchiamento;

\echo ''
\echo '--- Parametri ---'
\echo 'Retention tracciati: ' :retention_tracciati
\echo 'Retention eventi:    ' :retention_eventi

BEGIN;

-- =====================
-- TRACCIATI
-- =====================
\echo ''
\echo '--- Svecchiamento TRACCIATI ---'

\set end_tracciati 'CURRENT_DATE - interval :retention_tracciati '

\echo 'Cancellazione eventi collegati ai tracciati...'
DELETE FROM eventi WHERE id_tracciato IN (SELECT id FROM tracciati WHERE data_completamento < :end_tracciati);

\echo 'Cancellazione operazioni collegate ai tracciati...'
DELETE FROM operazioni WHERE id_tracciato IN (SELECT id FROM tracciati WHERE data_completamento < :end_tracciati);

\echo 'Rimozione large objects (zip_stampe) dai tracciati...'
SELECT lo_unlink(zip_stampe) FROM tracciati WHERE data_completamento < :end_tracciati AND zip_stampe IS NOT NULL;

\echo 'Cancellazione tracciati...'
DELETE FROM tracciati WHERE data_completamento < :end_tracciati;

-- =====================
-- EVENTI
-- =====================
-- Gli eventi collegati ai tracciati sono gia' stati eliminati sopra, insieme ai
-- tracciati che li referenziavano. Qui si svecchia il resto del giornale per
-- eta', che e' la parte che cresce di piu'. La condizione e' sulla colonna data,
-- coperta dall'indice idx_evt_data. Nessuna tabella referenzia eventi, quindi la
-- cancellazione non e' vincolata da chiavi esterne.
\echo ''
\echo '--- Svecchiamento EVENTI ---'

\set end_eventi 'CURRENT_DATE - interval :retention_eventi '

\echo 'Cancellazione eventi...'
DELETE FROM eventi WHERE data < :end_eventi;

COMMIT;

\echo ''
\echo '=== Svecchiamento completato ==='
SELECT now() AS fine_svecchiamento;
