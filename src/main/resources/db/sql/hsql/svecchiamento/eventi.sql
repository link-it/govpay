-- =============================================================================
-- Svecchiamento EVENTI - HSQLDB
--
-- Svecchia il giornale degli eventi per eta'. E' la tabella che cresce di piu'.
-- La condizione e' sulla colonna data, coperta dall'indice idx_evt_data; nessuna
-- tabella referenzia eventi, quindi la cancellazione non e' vincolata da chiavi
-- esterne.
--
-- Se si esegue anche lo svecchiamento dei tracciati, farlo prima: elimina gli
-- eventi collegati ai tracciati scaduti a prescindere dalla loro eta'.
--
-- Uso: SqlTool, oppure da applicazione Java
--
-- Il valore qui sotto e' il default. svecchiamento-db.sh lo sostituisce quando
-- gli si passa --retention-eventi.
-- =============================================================================

-- HSQLDB non ha variabili negli script: la retention e' il letterale
-- nelle DELETE qui sotto, 90 giorni.

DELETE FROM eventi WHERE data < CURRENT_DATE - 90 DAY;

COMMIT;
