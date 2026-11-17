-- =============================================================================
-- Svecchiamento TRACCIATI - Oracle
--
-- Elimina i tracciati completati da piu' di retention_tracciati giorni, con le
-- operazioni e gli eventi che li referenziano, nell'ordine imposto dalle chiavi
-- esterne.
--
-- La soglia viene calcolata una volta sola, all'inizio, e usata come costante:
-- in Oracle CURRENT_DATE e' rivalutato a ogni istruzione, e a cavallo della
-- mezzanotte istruzioni diverse selezionerebbero righe diverse.
--
-- Nessun archivio: tracciati e operazioni non servono alla riconciliazione, e
-- vengono cancellati senza copia.
--
-- Uso: sqlplus utente/password@host:porta/servizio @tracciati.sql
--
-- Il valore qui sotto e' il default. svecchiamento-db.sh lo sostituisce quando
-- gli si passa --retention-tracciati.
-- =============================================================================

DEFINE retention_tracciati = 30;

PROMPT
PROMPT --- Svecchiamento TRACCIATI ---
PROMPT Retention: &retention_tracciati giorni

COLUMN soglia_tracciati NEW_VALUE soglia_tracciati NOPRINT
SELECT TO_CHAR((CURRENT_DATE - &retention_tracciati), 'YYYYMMDDHH24MISS') AS soglia_tracciati FROM dual;

PROMPT Cancellazione eventi collegati ai tracciati...
DELETE FROM eventi WHERE id_tracciato IN (SELECT id FROM tracciati WHERE data_completamento < TO_DATE('&soglia_tracciati', 'YYYYMMDDHH24MISS'));

PROMPT Cancellazione operazioni collegate ai tracciati...
DELETE FROM operazioni WHERE id_tracciato IN (SELECT id FROM tracciati WHERE data_completamento < TO_DATE('&soglia_tracciati', 'YYYYMMDDHH24MISS'));

PROMPT Cancellazione tracciati...
DELETE FROM tracciati WHERE data_completamento < TO_DATE('&soglia_tracciati', 'YYYYMMDDHH24MISS');

COMMIT;
