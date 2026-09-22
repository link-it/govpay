-- =============================================================================
-- Svecchiamento EVENTI - MySQL/MariaDB
--
-- Svecchia il giornale degli eventi per eta'. E' la tabella che cresce di piu'.
-- La condizione e' sulla colonna data, coperta dall'indice idx_evt_data; nessuna
-- tabella referenzia eventi, quindi la cancellazione non e' vincolata da chiavi
-- esterne.
--
-- Se si esegue anche lo svecchiamento dei tracciati, farlo prima: elimina gli
-- eventi collegati ai tracciati scaduti a prescindere dalla loro eta'.
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
