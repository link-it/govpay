-- =============================================================================
-- Svecchiamento TRACCIATI NOTIFICA PAGAMENTI - MySQL/MariaDB
--
-- Elimina i tracciati di notifica dei pagamenti (trac_notif_pag) creati da
-- piu' di retention_tnp giorni. Nessun'altra tabella li referenzia.
--
-- Il riferimento e' data_creazione, valorizzata per ogni tracciato: anche
-- quelli mai completati, dopo la retention, non servono piu'.
--
-- Prima di essere cancellate, le righe vengono copiate nella tabella di
-- archivio trac_notif_pag_aaaammgg, creata se non esiste e riempita nella
-- stessa transazione della cancellazione: l'archivio contiene esattamente le
-- righe cancellate. svecchiamento-db.sh sostituisce aaaammgg con la data
-- dell'esecuzione; eseguito da solo, lo script archivia nella tabella
-- trac_notif_pag_aaaammgg. Salvare ed eliminare la tabella di archivio e'
-- compito del DBA.
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

SELECT 'Creazione della tabella di archivio...' AS stato;
CREATE TABLE IF NOT EXISTS trac_notif_pag_aaaammgg LIKE trac_notif_pag;

START TRANSACTION;

SELECT 'Archiviazione...' AS stato;
INSERT INTO trac_notif_pag_aaaammgg SELECT * FROM trac_notif_pag WHERE data_creazione < @end_tnp;

SELECT 'Cancellazione...' AS stato;
DELETE FROM trac_notif_pag WHERE data_creazione < @end_tnp;

COMMIT;
