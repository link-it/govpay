-- =============================================================================
-- Svecchiamento DOCUMENTI ORFANI - MySQL/MariaDB
--
-- Elimina i documenti non piu' referenziati da nessuna pendenza, promemoria o
-- stampa: quelli rimasti senza pendenze dopo le sezioni che svecchiano le
-- pendenze, che non li cancellano perche' un documento raggruppa piu'
-- pendenze. Va eseguita dopo di esse.
--
-- Un documento e' creato nella stessa transazione della sua prima pendenza
-- (VersamentiBD.insertVersamento), quindi nessun'altra sessione lo vede mai
-- orfano mentre nasce.
--
-- Prima di essere cancellate, le righe vengono copiate nella tabella di
-- archivio documenti_aaaammgg, creata se non esiste e riempita nella stessa
-- transazione della cancellazione: l'archivio contiene esattamente le righe
-- cancellate. svecchiamento-db.sh sostituisce aaaammgg con la data
-- dell'esecuzione; eseguito da solo, lo script archivia nella tabella
-- documenti_aaaammgg. Salvare ed eliminare la tabella di archivio e' compito
-- del DBA.
--
-- Uso: mysql -h <host> -u <utente> -D <database> < documenti_orfani.sql
--
-- La sezione non ha retention: un documento orfano non ha una data, e non
-- serve a nulla da subito.
-- =============================================================================

SELECT '--- Svecchiamento DOCUMENTI ORFANI ---' AS stato;

SELECT 'Creazione della tabella di archivio...' AS stato;
CREATE TABLE IF NOT EXISTS documenti_aaaammgg LIKE documenti;

START TRANSACTION;

SELECT 'Archiviazione...' AS stato;
INSERT INTO documenti_aaaammgg SELECT * FROM documenti
WHERE NOT EXISTS (SELECT 1 FROM versamenti v WHERE v.id_documento = documenti.id)
  AND NOT EXISTS (SELECT 1 FROM promemoria p WHERE p.id_documento = documenti.id)
  AND NOT EXISTS (SELECT 1 FROM stampe s WHERE s.id_documento = documenti.id);

SELECT 'Cancellazione...' AS stato;
DELETE FROM documenti
WHERE NOT EXISTS (SELECT 1 FROM versamenti v WHERE v.id_documento = documenti.id)
  AND NOT EXISTS (SELECT 1 FROM promemoria p WHERE p.id_documento = documenti.id)
  AND NOT EXISTS (SELECT 1 FROM stampe s WHERE s.id_documento = documenti.id);

COMMIT;
