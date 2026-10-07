-- =============================================================================
-- Svecchiamento DOCUMENTI ORFANI - Oracle
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
-- Uso: sqlplus utente/password@host:porta/servizio @documenti_orfani.sql
--
-- La sezione non ha retention: un documento orfano non ha una data, e non
-- serve a nulla da subito.
-- =============================================================================

PROMPT
PROMPT --- Svecchiamento DOCUMENTI ORFANI ---

PROMPT Creazione della tabella di archivio...
BEGIN
    EXECUTE IMMEDIATE 'CREATE TABLE documenti_aaaammgg AS SELECT * FROM documenti WHERE 1 = 0';
EXCEPTION WHEN OTHERS THEN
    -- ORA-00955: la tabella esiste gia', creata da un'esecuzione dello stesso giorno.
    IF SQLCODE != -955 THEN RAISE; END IF;
END;
/

PROMPT Archiviazione...
INSERT INTO documenti_aaaammgg SELECT * FROM documenti
WHERE NOT EXISTS (SELECT 1 FROM versamenti v WHERE v.id_documento = documenti.id)
  AND NOT EXISTS (SELECT 1 FROM promemoria p WHERE p.id_documento = documenti.id)
  AND NOT EXISTS (SELECT 1 FROM stampe s WHERE s.id_documento = documenti.id);

PROMPT Cancellazione...
DELETE FROM documenti
WHERE NOT EXISTS (SELECT 1 FROM versamenti v WHERE v.id_documento = documenti.id)
  AND NOT EXISTS (SELECT 1 FROM promemoria p WHERE p.id_documento = documenti.id)
  AND NOT EXISTS (SELECT 1 FROM stampe s WHERE s.id_documento = documenti.id);

COMMIT;
