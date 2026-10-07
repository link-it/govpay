-- =============================================================================
-- Svecchiamento AUDIT - PostgreSQL
--
-- Elimina le registrazioni dell'audit delle operazioni degli operatori
-- (gp_audit) piu' vecchie di retention_audit giorni. Nessun'altra tabella le
-- referenzia.
--
-- Prima di essere cancellate, le righe vengono copiate nella tabella di
-- archivio gp_audit_aaaammgg, creata se non esiste e riempita nella stessa
-- transazione della cancellazione: l'archivio contiene esattamente le righe
-- cancellate. svecchiamento-db.sh sostituisce aaaammgg con la data
-- dell'esecuzione; eseguito da solo, lo script archivia nella tabella
-- gp_audit_aaaammgg. Salvare ed eliminare la tabella di archivio e' compito
-- del DBA.
--
-- Uso: psql -v ON_ERROR_STOP=1 -h <host> -U <utente> -d <database> -f audit.sql
--
-- Il valore qui sotto e' il default. svecchiamento-db.sh lo sostituisce quando
-- gli si passa --retention-audit.
-- =============================================================================

\set retention_audit '\'365 days\''

\set end_audit 'CURRENT_DATE - interval :retention_audit '

\echo ''
\echo '--- Svecchiamento AUDIT ---'
\echo 'Retention: ' :retention_audit

\echo 'Creazione della tabella di archivio...'
CREATE TABLE IF NOT EXISTS gp_audit_aaaammgg AS SELECT * FROM gp_audit WITH NO DATA;

BEGIN;

\echo 'Archiviazione...'
INSERT INTO gp_audit_aaaammgg SELECT * FROM gp_audit WHERE data < :end_audit;

\echo 'Cancellazione...'
DELETE FROM gp_audit WHERE data < :end_audit;

COMMIT;

-- VACUUM non puo' stare in una transazione: va dopo il COMMIT.
\echo 'VACUUM ANALYZE della tabella svecchiata...'
VACUUM ANALYZE gp_audit;
