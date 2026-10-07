-- =============================================================================
-- Svecchiamento TRACCIATI NOTIFICA PAGAMENTI - PostgreSQL
--
-- Elimina i tracciati di notifica dei pagamenti (trac_notif_pag) creati da
-- piu' di retention_tnp giorni. Nessun'altra tabella li referenzia.
--
-- Il riferimento e' data_creazione, valorizzata per ogni tracciato: anche
-- quelli mai completati, dopo la retention, non servono piu'.
--
-- Il contenuto del tracciato e' un large object, che lo_unlink cancella:
-- nell'archivio e' copiato nella colonna aggiuntiva raw_contenuto_dati.
--
-- Prima di essere cancellate, le righe vengono copiate nella tabella di
-- archivio trac_notif_pag_aaaammgg, creata se non esiste e riempita nella
-- stessa transazione della cancellazione: l'archivio contiene esattamente le
-- righe cancellate. svecchiamento-db.sh sostituisce aaaammgg con la data
-- dell'esecuzione; eseguito da solo, lo script archivia nella tabella
-- trac_notif_pag_aaaammgg. Salvare ed eliminare la tabella di archivio e'
-- compito del DBA.
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

\echo 'Creazione della tabella di archivio...'
CREATE TABLE IF NOT EXISTS trac_notif_pag_aaaammgg AS SELECT trac_notif_pag.*, lo_get(trac_notif_pag.raw_contenuto) AS raw_contenuto_dati FROM trac_notif_pag WITH NO DATA;

BEGIN;

\echo 'Archiviazione...'
INSERT INTO trac_notif_pag_aaaammgg SELECT trac_notif_pag.*, lo_get(trac_notif_pag.raw_contenuto) FROM trac_notif_pag WHERE data_creazione < :end_tnp;

\echo 'Rimozione large objects (raw_contenuto)...'
SELECT lo_unlink(raw_contenuto) FROM trac_notif_pag WHERE data_creazione < :end_tnp AND raw_contenuto IS NOT NULL;

\echo 'Cancellazione...'
DELETE FROM trac_notif_pag WHERE data_creazione < :end_tnp;

COMMIT;

-- VACUUM non puo' stare in una transazione: va dopo il COMMIT.
\echo 'VACUUM ANALYZE della tabella svecchiata...'
VACUUM ANALYZE trac_notif_pag;
