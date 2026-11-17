-- =============================================================================
-- Svecchiamento TRACCIATI NOTIFICA PAGAMENTI - MySQL/MariaDB
--
-- Elimina i tracciati di notifica dei pagamenti (trac_notif_pag) creati da
-- piu' di retention_tnp giorni. Nessun'altra tabella li referenzia.
--
-- Il riferimento e' data_creazione, valorizzata per ogni tracciato: anche
-- quelli mai completati, dopo la retention, non servono piu'.
--
-- Nessun archivio: i tracciati di notifica sono copie di quanto gia' inviato
-- all'ente, e vengono cancellati senza copia.
--
-- Uso: mysql -h <host> -u <utente> -D <database> < tracciati_notifica_pagamenti.sql
--
-- Il valore qui sotto e' il default. svecchiamento-db.sh lo sostituisce quando
-- gli si passa --retention-tracciati_notifica_pagamenti.
-- =============================================================================

SET @retention_tnp = 30;

SET @end_tnp = CURRENT_DATE - INTERVAL @retention_tnp DAY;

SELECT '--- Svecchiamento TRACCIATI NOTIFICA PAGAMENTI ---' AS stato;
SELECT CONCAT('Retention: ', @retention_tnp, ' giorni') AS stato;

START TRANSACTION;

SELECT 'Cancellazione...' AS stato;
DELETE FROM trac_notif_pag WHERE data_creazione < @end_tnp;

COMMIT;
