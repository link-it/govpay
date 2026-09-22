-- =============================================================================
-- Svecchiamento EVENTI - Oracle
--
-- Svecchia il giornale degli eventi per eta'. E' la tabella che cresce di piu'.
-- La condizione e' sulla colonna data, coperta dall'indice idx_evt_data; nessuna
-- tabella referenzia eventi, quindi la cancellazione non e' vincolata da chiavi
-- esterne.
--
-- Se si esegue anche lo svecchiamento dei tracciati, farlo prima: elimina gli
-- eventi collegati ai tracciati scaduti a prescindere dalla loro eta'.
--
-- Uso: sqlplus utente/password@host:porta/servizio @eventi.sql
--
-- Il valore qui sotto e' il default. svecchiamento-db.sh lo sostituisce quando
-- gli si passa --retention-eventi.
-- =============================================================================

DEFINE retention_eventi = 90;

PROMPT
PROMPT --- Svecchiamento EVENTI ---
PROMPT Retention: &retention_eventi giorni

PROMPT Cancellazione eventi...
DELETE FROM eventi WHERE data < CURRENT_DATE - &retention_eventi;

COMMIT;
