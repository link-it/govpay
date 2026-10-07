-- =============================================================================
-- Svecchiamento DOCUMENTI ORFANI - PostgreSQL
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
-- Uso: psql -v ON_ERROR_STOP=1 -h <host> -U <utente> -d <database> -f documenti_orfani.sql
--
-- La sezione non ha retention: un documento orfano non ha una data, e non
-- serve a nulla da subito.
-- =============================================================================

\echo '--- Svecchiamento DOCUMENTI ORFANI ---'

\echo 'Creazione della tabella di archivio...'
CREATE TABLE IF NOT EXISTS documenti_aaaammgg AS SELECT * FROM documenti WITH NO DATA;

BEGIN;

\echo 'Archiviazione...'
INSERT INTO documenti_aaaammgg SELECT * FROM documenti
WHERE NOT EXISTS (SELECT 1 FROM versamenti v WHERE v.id_documento = documenti.id)
  AND NOT EXISTS (SELECT 1 FROM promemoria p WHERE p.id_documento = documenti.id)
  AND NOT EXISTS (SELECT 1 FROM stampe s WHERE s.id_documento = documenti.id);

\echo 'Cancellazione...'
DELETE FROM documenti
WHERE NOT EXISTS (SELECT 1 FROM versamenti v WHERE v.id_documento = documenti.id)
  AND NOT EXISTS (SELECT 1 FROM promemoria p WHERE p.id_documento = documenti.id)
  AND NOT EXISTS (SELECT 1 FROM stampe s WHERE s.id_documento = documenti.id);

COMMIT;

-- VACUUM non puo' stare in una transazione: va dopo il COMMIT.
\echo 'VACUUM ANALYZE della tabella svecchiata...'
VACUUM ANALYZE documenti;
