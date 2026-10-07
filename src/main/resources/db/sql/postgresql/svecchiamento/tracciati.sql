-- =============================================================================
-- Svecchiamento TRACCIATI - PostgreSQL
--
-- Elimina i tracciati completati da piu' di retention_tracciati giorni, con le
-- operazioni e gli eventi che li referenziano, nell'ordine imposto dalle chiavi
-- esterne. I large object degli zip di stampa vengono rimossi prima dei
-- tracciati che li referenziano, altrimenti resterebbero orfani nel catalogo.
--
-- Prima di essere cancellate, le righe vengono copiate in tabelle di archivio
-- con lo stesso nome e il suffisso _aaaammgg (operazioni, tracciati), create
-- se non esistono, senza vincoli ne' indici, e nella stessa transazione delle
-- cancellazioni: l'archivio contiene esattamente le righe cancellate.
-- svecchiamento-db.sh sostituisce aaaammgg con la data dell'esecuzione;
-- eseguito da solo, lo script archivia nelle tabelle _aaaammgg. Gli eventi non
-- vengono archiviati. Salvare ed eliminare le tabelle di archivio e' compito
-- del DBA. Il contenuto dei large object (tracciati.zip_stampe), che lo_unlink
-- cancella, e' copiato nella colonna aggiuntiva <colonna>_dati.
--
-- Uso: psql -v ON_ERROR_STOP=1 -h <host> -U <utente> -d <database> -f tracciati.sql
--
-- Il valore qui sotto e' il default. svecchiamento-db.sh lo sostituisce quando
-- gli si passa --retention-tracciati.
-- =============================================================================

\set retention_tracciati '\'30 days\''

\set end_tracciati 'CURRENT_DATE - interval :retention_tracciati '

\echo ''
\echo '--- Svecchiamento TRACCIATI ---'
\echo 'Retention: ' :retention_tracciati

\echo 'Creazione delle tabelle di archivio...'
CREATE TABLE IF NOT EXISTS operazioni_aaaammgg AS SELECT * FROM operazioni WITH NO DATA;
CREATE TABLE IF NOT EXISTS tracciati_aaaammgg AS SELECT tracciati.*, lo_get(tracciati.zip_stampe) AS zip_stampe_dati FROM tracciati WITH NO DATA;

BEGIN;

\echo 'Cancellazione eventi collegati ai tracciati...'
DELETE FROM eventi WHERE id_tracciato IN (SELECT id FROM tracciati WHERE data_completamento < :end_tracciati);

\echo 'Cancellazione operazioni collegate ai tracciati...'
INSERT INTO operazioni_aaaammgg SELECT * FROM operazioni WHERE id_tracciato IN (SELECT id FROM tracciati WHERE data_completamento < :end_tracciati);
DELETE FROM operazioni WHERE id_tracciato IN (SELECT id FROM tracciati WHERE data_completamento < :end_tracciati);

INSERT INTO tracciati_aaaammgg SELECT tracciati.*, lo_get(tracciati.zip_stampe) FROM tracciati WHERE data_completamento < :end_tracciati;
\echo 'Rimozione large objects (zip_stampe) dai tracciati...'
SELECT lo_unlink(zip_stampe) FROM tracciati WHERE data_completamento < :end_tracciati AND zip_stampe IS NOT NULL;

\echo 'Cancellazione tracciati...'
DELETE FROM tracciati WHERE data_completamento < :end_tracciati;

COMMIT;
