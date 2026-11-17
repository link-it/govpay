-- =============================================================================
-- Svecchiamento TRACCIATI - PostgreSQL
--
-- Elimina i tracciati di caricamento completati da piu' di retention_tracciati
-- giorni, con le operazioni e gli eventi che li referenziano, nell'ordine
-- imposto dalle chiavi esterne. I large object degli zip di stampa vengono
-- rimossi prima dei tracciati che li referenziano, altrimenti resterebbero
-- orfani nel catalogo.
--
-- Nessun archivio: tracciati, operazioni e zip di stampa non servono alla
-- riconciliazione, e vengono cancellati senza copia.
--
-- Chiude la sezione la rimozione dei large object orfani: quelli che nessuna
-- colonna di GovPay referenzia piu' (tracciati.zip_stampe,
-- allegati.raw_contenuto, trac_notif_pag.raw_contenuto), rimasti da
-- cancellazioni fatte senza lo_unlink. Non hanno una data, e vengono rimossi
-- tutti a ogni esecuzione. GovPay crea il large object e la riga che lo
-- referenzia nella stessa transazione, quindi un large object in corso di
-- scrittura non e' visibile qui e non puo' essere scambiato per un orfano.
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

BEGIN;

\echo 'Cancellazione eventi collegati ai tracciati...'
DELETE FROM eventi WHERE id_tracciato IN (SELECT id FROM tracciati WHERE data_completamento < :end_tracciati);

\echo 'Cancellazione operazioni collegate ai tracciati...'
DELETE FROM operazioni WHERE id_tracciato IN (SELECT id FROM tracciati WHERE data_completamento < :end_tracciati);

\echo 'Rimozione large objects (zip_stampe) dai tracciati...'
SELECT count(lo_unlink(zip_stampe)) AS zip_stampe_rimossi FROM tracciati WHERE data_completamento < :end_tracciati AND zip_stampe IS NOT NULL;

\echo 'Cancellazione tracciati...'
DELETE FROM tracciati WHERE data_completamento < :end_tracciati;

\echo 'Rimozione large objects orfani...'
SELECT count(lo_unlink(m.oid)) AS large_object_orfani_rimossi
FROM pg_largeobject_metadata m
WHERE NOT EXISTS (SELECT 1 FROM tracciati WHERE zip_stampe = m.oid)
  AND NOT EXISTS (SELECT 1 FROM allegati WHERE raw_contenuto = m.oid)
  AND NOT EXISTS (SELECT 1 FROM trac_notif_pag WHERE raw_contenuto = m.oid);

COMMIT;
