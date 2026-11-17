-- =============================================================================
-- Svecchiamento TRACCIATI NOTIFICA PAGAMENTI - PostgreSQL
--
-- Elimina i tracciati di notifica dei pagamenti (trac_notif_pag) creati da
-- piu' di retention_tnp giorni. Nessun'altra tabella li referenzia.
--
-- Il riferimento e' data_creazione, valorizzata per ogni tracciato: anche
-- quelli mai completati, dopo la retention, non servono piu'.
--
-- Il contenuto del tracciato e' un large object, che lo_unlink cancella prima
-- della riga che lo referenzia.
--
-- Nessun archivio: i tracciati di notifica sono copie di quanto gia' inviato
-- all'ente, e vengono cancellati senza copia.
--
-- Uso: psql -v ON_ERROR_STOP=1 -h <host> -U <utente> -d <database> -f tracciati_notifica_pagamenti.sql
--
-- Il valore qui sotto e' il default. svecchiamento-db.sh lo sostituisce quando
-- gli si passa --retention-tracciati_notifica_pagamenti.
-- =============================================================================

\set retention_tnp '\'30 days\''

\set end_tnp 'CURRENT_DATE - interval :retention_tnp '

\echo ''
\echo '--- Svecchiamento TRACCIATI NOTIFICA PAGAMENTI ---'
\echo 'Retention: ' :retention_tnp

BEGIN;

\echo 'Rimozione large objects (raw_contenuto)...'
SELECT count(lo_unlink(raw_contenuto)) AS raw_contenuto_rimossi FROM trac_notif_pag WHERE data_creazione < :end_tnp AND raw_contenuto IS NOT NULL;

\echo 'Cancellazione...'
DELETE FROM trac_notif_pag WHERE data_creazione < :end_tnp;

COMMIT;

-- VACUUM non puo' stare in una transazione: va dopo il COMMIT.
\echo 'VACUUM ANALYZE della tabella svecchiata...'
VACUUM ANALYZE trac_notif_pag;
