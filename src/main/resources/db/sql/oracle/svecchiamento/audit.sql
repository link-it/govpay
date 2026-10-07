-- =============================================================================
-- Svecchiamento AUDIT - Oracle
--
-- Elimina le registrazioni dell'audit delle operazioni degli operatori
-- (gp_audit) piu' vecchie di retention_audit giorni. Nessun'altra tabella le
-- referenzia.
--
-- Prima di essere cancellate, le righe vengono copiate nella tabella di
-- archivio gp_audit_aaaammgg, creata se non esiste e riempita nella stessa
-- transazione della cancellazione: l'archivio contiene esattamente le righe
-- cancellate. svecchiamento-db.sh sostituisce aaaammgg con la data
-- dell'esecuzione; eseguito da solo, lo script archivia nella tabella
-- gp_audit_aaaammgg. Salvare ed eliminare la tabella di archivio e' compito
-- del DBA.
--
-- Uso: sqlplus utente/password@host:porta/servizio @audit.sql
--
-- Il valore qui sotto e' il default. svecchiamento-db.sh lo sostituisce quando
-- gli si passa --retention-audit.
-- =============================================================================

DEFINE retention_audit = 365;

PROMPT
PROMPT --- Svecchiamento AUDIT ---
PROMPT Retention: &retention_audit giorni

-- La soglia viene calcolata una volta sola: in Oracle CURRENT_DATE e'
-- rivalutato a ogni istruzione, e a cavallo della mezzanotte l'archiviazione e
-- la cancellazione selezionerebbero righe diverse.
COLUMN soglia_audit NEW_VALUE soglia_audit NOPRINT
SELECT TO_CHAR((CURRENT_DATE - &retention_audit), 'YYYYMMDDHH24MISS') AS soglia_audit FROM dual;

PROMPT Creazione della tabella di archivio...
BEGIN
    EXECUTE IMMEDIATE 'CREATE TABLE gp_audit_aaaammgg AS SELECT * FROM gp_audit WHERE 1 = 0';
EXCEPTION WHEN OTHERS THEN
    -- ORA-00955: la tabella esiste gia', creata da un'esecuzione dello stesso giorno.
    IF SQLCODE != -955 THEN RAISE; END IF;
END;
/

PROMPT Archiviazione...
INSERT INTO gp_audit_aaaammgg SELECT * FROM gp_audit WHERE data < TO_DATE('&soglia_audit', 'YYYYMMDDHH24MISS');

PROMPT Cancellazione...
DELETE FROM gp_audit WHERE data < TO_DATE('&soglia_audit', 'YYYYMMDDHH24MISS');

COMMIT;
