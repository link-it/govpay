-- =============================================================================
-- Svecchiamento EVENTI - MySQL/MariaDB
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
-- Uso: mysql -h <host> -u <utente> -D <database> < eventi.sql
--
-- Il valore qui sotto e' il default. svecchiamento-db.sh lo sostituisce quando
-- gli si passa --retention-eventi.
-- =============================================================================

SET @retention_eventi = 90;

SET @end_eventi = CURRENT_DATE - INTERVAL @retention_eventi DAY;

SELECT '--- Svecchiamento EVENTI ---' AS stato;
SELECT CONCAT('Retention: ', @retention_eventi, ' giorni') AS stato;

START TRANSACTION;

SELECT 'Cancellazione eventi...' AS stato;
DELETE FROM eventi WHERE data < @end_eventi;

COMMIT;
