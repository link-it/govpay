-- =============================================================================
-- Svecchiamento TRACCIATI - Oracle
--
-- Elimina i tracciati completati da piu' di retention_tracciati giorni, con le
-- operazioni e gli eventi che li referenziano, nell'ordine imposto dalle chiavi
-- esterne.
--
-- Uso: sqlplus utente/password@host:porta/servizio @tracciati.sql
--
-- Il valore qui sotto e' il default. svecchiamento-db.sh lo sostituisce quando
-- gli si passa --retention-tracciati.
-- =============================================================================

DEFINE retention_tracciati = 7;

PROMPT
PROMPT --- Svecchiamento TRACCIATI ---
PROMPT Retention: &retention_tracciati giorni

PROMPT Cancellazione eventi collegati ai tracciati...
DELETE FROM eventi WHERE id_tracciato IN (SELECT id FROM tracciati WHERE data_completamento < CURRENT_DATE - &retention_tracciati);

PROMPT Cancellazione operazioni collegate ai tracciati...
DELETE FROM operazioni WHERE id_tracciato IN (SELECT id FROM tracciati WHERE data_completamento < CURRENT_DATE - &retention_tracciati);

PROMPT Cancellazione tracciati...
DELETE FROM tracciati WHERE data_completamento < CURRENT_DATE - &retention_tracciati;

COMMIT;
