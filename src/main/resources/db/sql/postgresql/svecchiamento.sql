-- =============================================================================
-- Script di svecchiamento dati GovPay - PostgreSQL
--
-- Uso: psql -h <host> -U <user> -d <database> -f svecchiamento.sql
--
-- Parametri (impostare prima dell'esecuzione):
-- =============================================================================

-- Retention tracciati
\set retention_tracciati '\'7 days\''

-- =============================================================================

\echo '=== Avvio svecchiamento GovPay ==='
SELECT now() AS inizio_svecchiamento;

\echo ''
\echo '--- Parametri ---'
\echo 'Retention tracciati: ' :retention_tracciati

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

COMMIT;

\echo ''
\echo '=== Svecchiamento completato ==='
SELECT now() AS fine_svecchiamento;
