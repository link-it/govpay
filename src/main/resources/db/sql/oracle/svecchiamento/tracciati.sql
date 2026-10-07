-- =============================================================================
-- Svecchiamento TRACCIATI - Oracle
--
-- Elimina i tracciati completati da piu' di retention_tracciati giorni, con le
-- operazioni e gli eventi che li referenziano, nell'ordine imposto dalle chiavi
-- esterne.
--
-- La soglia viene calcolata una volta sola, all'inizio, e usata come costante:
-- in Oracle CURRENT_DATE e' rivalutato a ogni istruzione, e a cavallo della
-- mezzanotte istruzioni diverse selezionerebbero righe diverse.
--
-- Prima di essere cancellate, le righe vengono copiate in tabelle di archivio
-- con lo stesso nome e il suffisso _aaaammgg (operazioni, tracciati), create
-- se non esistono, senza vincoli ne' indici, e nella stessa transazione delle
-- cancellazioni: l'archivio contiene esattamente le righe cancellate.
-- svecchiamento-db.sh sostituisce aaaammgg con la data dell'esecuzione;
-- eseguito da solo, lo script archivia nelle tabelle _aaaammgg. Gli eventi non
-- vengono archiviati. Salvare ed eliminare le tabelle di archivio e' compito
-- del DBA.
--
-- Uso: sqlplus utente/password@host:porta/servizio @tracciati.sql
--
-- Il valore qui sotto e' il default. svecchiamento-db.sh lo sostituisce quando
-- gli si passa --retention-tracciati.
-- =============================================================================

DEFINE retention_tracciati = 30;

PROMPT
PROMPT --- Svecchiamento TRACCIATI ---
PROMPT Retention: &retention_tracciati giorni

COLUMN soglia_tracciati NEW_VALUE soglia_tracciati NOPRINT
SELECT TO_CHAR((CURRENT_DATE - &retention_tracciati), 'YYYYMMDDHH24MISS') AS soglia_tracciati FROM dual;

PROMPT Creazione delle tabelle di archivio...
DECLARE
    -- ORA-00955: la tabella esiste gia', creata da un'esecuzione dello stesso giorno.
    PROCEDURE crea(p_archivio VARCHAR2, p_tabella VARCHAR2) IS
    BEGIN
        EXECUTE IMMEDIATE 'CREATE TABLE ' || p_archivio || ' AS SELECT * FROM ' || p_tabella || ' WHERE 1 = 0';
    EXCEPTION WHEN OTHERS THEN
        IF SQLCODE != -955 THEN RAISE; END IF;
    END;
BEGIN
    crea('operazioni_aaaammgg', 'operazioni');
    crea('tracciati_aaaammgg', 'tracciati');
END;
/

PROMPT Cancellazione eventi collegati ai tracciati...
DELETE FROM eventi WHERE id_tracciato IN (SELECT id FROM tracciati WHERE data_completamento < TO_DATE('&soglia_tracciati', 'YYYYMMDDHH24MISS'));

PROMPT Cancellazione operazioni collegate ai tracciati...
INSERT INTO operazioni_aaaammgg SELECT * FROM operazioni WHERE id_tracciato IN (SELECT id FROM tracciati WHERE data_completamento < TO_DATE('&soglia_tracciati', 'YYYYMMDDHH24MISS'));
DELETE FROM operazioni WHERE id_tracciato IN (SELECT id FROM tracciati WHERE data_completamento < TO_DATE('&soglia_tracciati', 'YYYYMMDDHH24MISS'));

PROMPT Cancellazione tracciati...
INSERT INTO tracciati_aaaammgg SELECT * FROM tracciati WHERE data_completamento < TO_DATE('&soglia_tracciati', 'YYYYMMDDHH24MISS');
DELETE FROM tracciati WHERE data_completamento < TO_DATE('&soglia_tracciati', 'YYYYMMDDHH24MISS');

COMMIT;
