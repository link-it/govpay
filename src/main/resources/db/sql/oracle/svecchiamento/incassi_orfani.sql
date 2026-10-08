-- =============================================================================
-- Svecchiamento INCASSI ORFANI - Oracle
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
-- Uso: sqlplus utente/password@host:porta/servizio @incassi_orfani.sql
--
-- Il valore qui sotto e' il default. svecchiamento-db.sh lo sostituisce quando
-- gli si passa --retention-incassi_orfani.
-- =============================================================================

DEFINE retention_incassi = 730;

PROMPT
PROMPT --- Svecchiamento INCASSI ORFANI ---
PROMPT Retention: &retention_incassi giorni

-- La soglia viene calcolata una volta sola: in Oracle CURRENT_DATE e'
-- rivalutato a ogni istruzione, e a cavallo della mezzanotte l'archiviazione e
-- la cancellazione selezionerebbero righe diverse.
COLUMN soglia_incassi NEW_VALUE soglia_incassi NOPRINT
SELECT TO_CHAR((CURRENT_DATE - &retention_incassi), 'YYYYMMDDHH24MISS') AS soglia_incassi FROM dual;

PROMPT Creazione della tabella di archivio...
BEGIN
    EXECUTE IMMEDIATE 'CREATE TABLE incassi_aaaammgg AS SELECT * FROM incassi WHERE 1 = 0';
EXCEPTION WHEN OTHERS THEN
    -- ORA-00955: la tabella esiste gia', creata da un'esecuzione dello stesso giorno.
    IF SQLCODE != -955 THEN RAISE; END IF;
END;
/

PROMPT Cancellazione eventi degli incassi...
DELETE FROM eventi WHERE id_incasso IN (SELECT id FROM incassi WHERE data_ora_incasso < TO_DATE('&soglia_incassi', 'YYYYMMDDHH24MISS')
  AND NOT EXISTS (SELECT 1 FROM fr WHERE fr.id_incasso = incassi.id)
  AND NOT EXISTS (SELECT 1 FROM pagamenti p WHERE p.id_incasso = incassi.id));

PROMPT Archiviazione...
INSERT INTO incassi_aaaammgg SELECT * FROM incassi WHERE data_ora_incasso < TO_DATE('&soglia_incassi', 'YYYYMMDDHH24MISS')
  AND NOT EXISTS (SELECT 1 FROM fr WHERE fr.id_incasso = incassi.id)
  AND NOT EXISTS (SELECT 1 FROM pagamenti p WHERE p.id_incasso = incassi.id);

PROMPT Cancellazione...
DELETE FROM incassi WHERE data_ora_incasso < TO_DATE('&soglia_incassi', 'YYYYMMDDHH24MISS')
  AND NOT EXISTS (SELECT 1 FROM fr WHERE fr.id_incasso = incassi.id)
  AND NOT EXISTS (SELECT 1 FROM pagamenti p WHERE p.id_incasso = incassi.id);

COMMIT;
