-- =============================================================================
-- Svecchiamento EVENTI - HSQLDB
--
-- Svecchia il giornale degli eventi per eta'. E' la tabella che cresce di piu'.
-- La condizione e' sulla colonna data, coperta dall'indice idx_evt_data; nessuna
-- tabella referenzia eventi, quindi la cancellazione non e' vincolata da chiavi
-- esterne.
--
-- Questa sezione va per prima: eventi e' la tabella piu' grande, e sfoltirla
-- rende meno costose le DELETE della sezione tracciati, che su eventi passano da
-- una sottoquery. Gli eventi collegati a un tracciato scaduto ma piu' recenti
-- della retention non vengono toccati qui: li elimina quella sezione, insieme al
-- tracciato che li referenzia.
--
-- Uso: SqlTool, oppure da applicazione Java
--
-- Il valore qui sotto e' il default. svecchiamento-db.sh lo sostituisce quando
-- gli si passa --retention-eventi.
-- =============================================================================

-- HSQLDB non ha variabili negli script: la retention e' il letterale
-- nelle DELETE qui sotto, 3 mesi.

DELETE FROM eventi WHERE data < CURRENT_DATE - 3 MONTH;

COMMIT;
