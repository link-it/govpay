-- =============================================================================
-- Svecchiamento INCASSI ORFANI - MySQL/MariaDB
--
-- Elimina gli incassi piu' vecchi di retention_incassi giorni non piu'
-- referenziati da nessun flusso di rendicontazione ne' da nessun pagamento,
-- con i loro eventi.
--
-- Un incasso resta orfano quando il suo flusso o i suoi pagamenti sono stati
-- cancellati senza di lui: da pendenze_pagate, che cancella i pagamenti ma non
-- gli incassi, o da una rimozione manuale delle rendicontazioni o dei flussi.
-- Gli incassi dei flussi svecchiati li cancella gia' flussi_rendicontazione,
-- insieme al flusso.
--
-- Il riferimento e' data_ora_incasso. Gli eventi dell'incasso vengono
-- cancellati e non archiviati, come in tutte le sezioni.
--
-- Prima di essere cancellate, le righe vengono copiate nella tabella di
-- archivio incassi_aaaammgg, creata se non esiste e riempita nella stessa
-- transazione della cancellazione: l'archivio contiene esattamente le righe
-- cancellate. svecchiamento-db.sh sostituisce aaaammgg con la data
-- dell'esecuzione; eseguito da solo, lo script archivia nella tabella
-- incassi_aaaammgg. Salvare ed eliminare la tabella di archivio e' compito del
-- DBA.
--
-- Uso: mysql -h <host> -u <utente> -D <database> < incassi_orfani.sql
--
-- Il valore qui sotto e' il default. svecchiamento-db.sh lo sostituisce quando
-- gli si passa --retention-incassi_orfani.
-- =============================================================================

SET @retention_incassi = 730;

SET @end_incassi = CURRENT_DATE - INTERVAL @retention_incassi DAY;

SELECT '--- Svecchiamento INCASSI ORFANI ---' AS stato;
SELECT CONCAT('Retention: ', @retention_incassi, ' giorni') AS stato;

SELECT 'Creazione della tabella di archivio...' AS stato;
CREATE TABLE IF NOT EXISTS incassi_aaaammgg LIKE incassi;

START TRANSACTION;

SELECT 'Cancellazione eventi degli incassi...' AS stato;
DELETE FROM eventi WHERE id_incasso IN (SELECT id FROM incassi WHERE data_ora_incasso < @end_incassi
  AND NOT EXISTS (SELECT 1 FROM fr WHERE fr.id_incasso = incassi.id)
  AND NOT EXISTS (SELECT 1 FROM pagamenti p WHERE p.id_incasso = incassi.id));

SELECT 'Archiviazione...' AS stato;
INSERT INTO incassi_aaaammgg SELECT * FROM incassi WHERE data_ora_incasso < @end_incassi
  AND NOT EXISTS (SELECT 1 FROM fr WHERE fr.id_incasso = incassi.id)
  AND NOT EXISTS (SELECT 1 FROM pagamenti p WHERE p.id_incasso = incassi.id);

SELECT 'Cancellazione...' AS stato;
DELETE FROM incassi WHERE data_ora_incasso < @end_incassi
  AND NOT EXISTS (SELECT 1 FROM fr WHERE fr.id_incasso = incassi.id)
  AND NOT EXISTS (SELECT 1 FROM pagamenti p WHERE p.id_incasso = incassi.id);

COMMIT;
