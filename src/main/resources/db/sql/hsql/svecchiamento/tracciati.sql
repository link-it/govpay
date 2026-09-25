-- =============================================================================
-- Svecchiamento TRACCIATI - HSQLDB
--
-- Elimina i tracciati completati da piu' di retention_tracciati mesi, con le
-- operazioni e gli eventi che li referenziano, nell'ordine imposto dalle chiavi
-- esterne.
--
-- Uso: SqlTool, oppure da applicazione Java
--
-- Il valore qui sotto e' il default. svecchiamento-db.sh lo sostituisce quando
-- gli si passa --retention-tracciati.
-- =============================================================================

-- HSQLDB non ha variabili negli script: la retention e' il letterale
-- nelle DELETE qui sotto, 1 mesi.

DELETE FROM eventi WHERE id_tracciato IN (SELECT id FROM tracciati WHERE data_completamento < CURRENT_DATE - 1 MONTH);

DELETE FROM operazioni WHERE id_tracciato IN (SELECT id FROM tracciati WHERE data_completamento < CURRENT_DATE - 1 MONTH);

DELETE FROM tracciati WHERE data_completamento < CURRENT_DATE - 1 MONTH;

COMMIT;
