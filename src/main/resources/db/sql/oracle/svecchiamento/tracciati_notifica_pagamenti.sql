-- =============================================================================
-- Svecchiamento TRACCIATI NOTIFICA PAGAMENTI - Oracle
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
-- Uso: sqlplus utente/password@host:porta/servizio @tracciati_notifica_pagamenti.sql
--
-- Il valore qui sotto e' il default. svecchiamento-db.sh lo sostituisce quando
-- gli si passa --retention-tracciati_notifica_pagamenti.
-- =============================================================================

DEFINE retention_tnp = 30;

PROMPT
PROMPT --- Svecchiamento TRACCIATI NOTIFICA PAGAMENTI ---
PROMPT Retention: &retention_tnp giorni

-- La soglia viene calcolata una volta sola: in Oracle CURRENT_DATE e'
-- rivalutato a ogni istruzione, e a cavallo della mezzanotte l'archiviazione e
-- la cancellazione selezionerebbero righe diverse.
COLUMN soglia_tnp NEW_VALUE soglia_tnp NOPRINT
SELECT TO_CHAR((CURRENT_DATE - &retention_tnp), 'YYYYMMDDHH24MISS') AS soglia_tnp FROM dual;

PROMPT Creazione della tabella di archivio...
BEGIN
    EXECUTE IMMEDIATE 'CREATE TABLE trac_notif_pag_aaaammgg AS SELECT * FROM trac_notif_pag WHERE 1 = 0';
EXCEPTION WHEN OTHERS THEN
    -- ORA-00955: la tabella esiste gia', creata da un'esecuzione dello stesso giorno.
    IF SQLCODE != -955 THEN RAISE; END IF;
END;
/

PROMPT Archiviazione...
INSERT INTO trac_notif_pag_aaaammgg SELECT * FROM trac_notif_pag WHERE data_creazione < TO_DATE('&soglia_tnp', 'YYYYMMDDHH24MISS');

PROMPT Cancellazione...
DELETE FROM trac_notif_pag WHERE data_creazione < TO_DATE('&soglia_tnp', 'YYYYMMDDHH24MISS');

COMMIT;
