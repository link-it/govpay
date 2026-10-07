-- =============================================================================
-- Svecchiamento PENDENZE PAGATE - Oracle
--
-- Elimina le pendenze pagate il cui ultimo pagamento e' piu' vecchio di
-- retention_pendenze_pagate giorni, con le RPT, i pagamenti e tutto cio' che le
-- referenzia, nell'ordine imposto dalle chiavi esterne.
--
-- Una pendenza rientra se:
--   - e' in uno stato di pagamento eseguito: ESEGUITO, PARZIALMENTE_ESEGUITO,
--     ESEGUITO_ALTRO_CANALE, ESEGUITO_SENZA_RPT, INCASSATO;
--   - e' stata pagata con ricevuta piu' di retention_pendenze_pagate giorni fa:
--     data_pagamento e' valorizzata, all'elaborazione della RT, solo per le
--     pendenze pagate con ricevuta;
--   - nessuno dei suoi pagamenti, sulle voci o sulle RPT, e' piu' recente della
--     soglia: data_pagamento e' quella dell'ultima RT elaborata, e una RT
--     recuperata in ritardo potrebbe essere piu' vecchia di un altro pagamento;
--   - nessuna sua voce e nessun suo pagamento e' rendicontato. Le pendenze
--     rendicontate sono svecchiate insieme ai loro flussi, dalla sezione
--     flussi_rendicontazione, e le pendenze pagate senza ricevuta con esse.
--
-- I documenti a cui le pendenze appartengono non vengono cancellati, come per
-- le pendenze scadute: quelli rimasti senza pendenze li cancella la sezione
-- documenti_orfani. Gli incassi neppure: sono i pagamenti a referenziarli.
--
-- Gli id vengono raccolti una volta sola, e il criterio, con le sue sottoquery
-- su pagamenti e rendicontazioni, viene valutato una volta invece che in ogni
-- DELETE. Una tabella temporanea richiederebbe DDL, quindi un commit implicito:
-- gli id stanno in una collection PL/SQL, e le DELETE la scorrono con FORALL.
--
-- La soglia viene calcolata una volta sola, all'inizio, e usata come costante:
-- in Oracle CURRENT_DATE e' rivalutato a ogni istruzione, e a cavallo della
-- mezzanotte istruzioni diverse selezionerebbero righe diverse.
--
-- Prima di essere cancellate, le righe vengono copiate in tabelle di archivio
-- con lo stesso nome e il suffisso _aaaammgg (pagamenti, notifiche,
-- notifiche_app_io, promemoria, operazioni, stampe, allegati, rpt,
-- singoli_versamenti, versamenti), create se non esistono, senza vincoli ne'
-- indici, e nella stessa transazione delle cancellazioni: l'archivio contiene
-- esattamente le righe cancellate. svecchiamento-db.sh sostituisce aaaammgg
-- con la data dell'esecuzione; eseguito da solo, lo script archivia nelle
-- tabelle _aaaammgg. Gli eventi non vengono archiviati. Salvare ed eliminare
-- le tabelle di archivio e' compito del DBA.
--
-- Uso: sqlplus utente/password@host:porta/servizio @pendenze_pagate.sql
--
-- Il valore qui sotto e' il default. svecchiamento-db.sh lo sostituisce quando
-- gli si passa --retention-pendenze_pagate.
-- =============================================================================

DEFINE retention_pendenze_pagate = 730;

PROMPT
PROMPT --- Svecchiamento PENDENZE PAGATE ---
PROMPT Retention: &retention_pendenze_pagate giorni

COLUMN soglia_pendenze_pagate NEW_VALUE soglia_pendenze_pagate NOPRINT
SELECT TO_CHAR((CURRENT_DATE - &retention_pendenze_pagate), 'YYYYMMDDHH24MISS') AS soglia_pendenze_pagate FROM dual;

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
    crea('pagamenti_aaaammgg', 'pagamenti');
    crea('notifiche_aaaammgg', 'notifiche');
    crea('notifiche_app_io_aaaammgg', 'notifiche_app_io');
    crea('promemoria_aaaammgg', 'promemoria');
    crea('operazioni_aaaammgg', 'operazioni');
    crea('stampe_aaaammgg', 'stampe');
    crea('allegati_aaaammgg', 'allegati');
    crea('rpt_aaaammgg', 'rpt');
    crea('singoli_versamenti_aaaammgg', 'singoli_versamenti');
    crea('versamenti_aaaammgg', 'versamenti');
END;
/

SET SERVEROUTPUT ON

PROMPT Pendenze da cancellare:
SELECT COUNT(*) AS pendenze_da_cancellare FROM (
    SELECT v.id FROM versamenti v
    WHERE v.stato_versamento IN ('ESEGUITO', 'PARZIALMENTE_ESEGUITO', 'ESEGUITO_ALTRO_CANALE', 'ESEGUITO_SENZA_RPT', 'INCASSATO')
      AND v.data_pagamento < TO_DATE('&soglia_pendenze_pagate', 'YYYYMMDDHH24MISS')
      AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN pagamenti p ON p.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id AND p.data_pagamento >= TO_DATE('&soglia_pendenze_pagate', 'YYYYMMDDHH24MISS'))
      AND NOT EXISTS (SELECT 1 FROM rpt JOIN pagamenti p ON p.id_rpt = rpt.id WHERE rpt.id_versamento = v.id AND p.data_pagamento >= TO_DATE('&soglia_pendenze_pagate', 'YYYYMMDDHH24MISS'))
      AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN rendicontazioni r ON r.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
      AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN pagamenti p ON p.id_singolo_versamento = sv.id JOIN rendicontazioni r ON r.id_pagamento = p.id WHERE sv.id_versamento = v.id)
      AND NOT EXISTS (SELECT 1 FROM rpt JOIN pagamenti p ON p.id_rpt = rpt.id JOIN rendicontazioni r ON r.id_pagamento = p.id WHERE rpt.id_versamento = v.id)
);

DECLARE
    TYPE t_id IS TABLE OF NUMBER;
    ids t_id;

    PROCEDURE esito(p_cosa VARCHAR2, p_righe NUMBER) IS
    BEGIN
        DBMS_OUTPUT.PUT_LINE(p_cosa || ': ' || p_righe || ' righe');
    END;
BEGIN
    SELECT v.id BULK COLLECT INTO ids FROM versamenti v
        WHERE v.stato_versamento IN ('ESEGUITO', 'PARZIALMENTE_ESEGUITO', 'ESEGUITO_ALTRO_CANALE', 'ESEGUITO_SENZA_RPT', 'INCASSATO')
          AND v.data_pagamento < TO_DATE('&soglia_pendenze_pagate', 'YYYYMMDDHH24MISS')
          AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN pagamenti p ON p.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id AND p.data_pagamento >= TO_DATE('&soglia_pendenze_pagate', 'YYYYMMDDHH24MISS'))
          AND NOT EXISTS (SELECT 1 FROM rpt JOIN pagamenti p ON p.id_rpt = rpt.id WHERE rpt.id_versamento = v.id AND p.data_pagamento >= TO_DATE('&soglia_pendenze_pagate', 'YYYYMMDDHH24MISS'))
          AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN rendicontazioni r ON r.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
          AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN pagamenti p ON p.id_singolo_versamento = sv.id JOIN rendicontazioni r ON r.id_pagamento = p.id WHERE sv.id_versamento = v.id)
          AND NOT EXISTS (SELECT 1 FROM rpt JOIN pagamenti p ON p.id_rpt = rpt.id JOIN rendicontazioni r ON r.id_pagamento = p.id WHERE rpt.id_versamento = v.id);

    FORALL i IN 1 .. ids.COUNT
        INSERT INTO pagamenti_aaaammgg SELECT * FROM pagamenti WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento = ids(i));
    FORALL i IN 1 .. ids.COUNT
        DELETE FROM pagamenti WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento = ids(i));
    esito('Cancellazione pagamenti sulle RPT', SQL%ROWCOUNT);
    FORALL i IN 1 .. ids.COUNT
        INSERT INTO pagamenti_aaaammgg SELECT * FROM pagamenti WHERE id_singolo_versamento IN (SELECT id FROM singoli_versamenti WHERE id_versamento = ids(i));
    FORALL i IN 1 .. ids.COUNT
        DELETE FROM pagamenti WHERE id_singolo_versamento IN (SELECT id FROM singoli_versamenti WHERE id_versamento = ids(i));
    esito('Cancellazione pagamenti sulle voci', SQL%ROWCOUNT);

    FORALL i IN 1 .. ids.COUNT
        INSERT INTO notifiche_aaaammgg SELECT * FROM notifiche WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento = ids(i));
    FORALL i IN 1 .. ids.COUNT
        DELETE FROM notifiche WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento = ids(i));
    esito('Cancellazione notifiche', SQL%ROWCOUNT);

    FORALL i IN 1 .. ids.COUNT
        INSERT INTO notifiche_app_io_aaaammgg SELECT * FROM notifiche_app_io WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento = ids(i));
    FORALL i IN 1 .. ids.COUNT
        DELETE FROM notifiche_app_io WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento = ids(i));
    esito('Cancellazione notifiche App IO sulle RPT', SQL%ROWCOUNT);
    FORALL i IN 1 .. ids.COUNT
        INSERT INTO notifiche_app_io_aaaammgg SELECT * FROM notifiche_app_io WHERE id_versamento = ids(i);
    FORALL i IN 1 .. ids.COUNT
        DELETE FROM notifiche_app_io WHERE id_versamento = ids(i);
    esito('Cancellazione notifiche App IO sulle pendenze', SQL%ROWCOUNT);

    FORALL i IN 1 .. ids.COUNT
        INSERT INTO promemoria_aaaammgg SELECT * FROM promemoria WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento = ids(i));
    FORALL i IN 1 .. ids.COUNT
        DELETE FROM promemoria WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento = ids(i));
    esito('Cancellazione promemoria sulle RPT', SQL%ROWCOUNT);
    FORALL i IN 1 .. ids.COUNT
        INSERT INTO promemoria_aaaammgg SELECT * FROM promemoria WHERE id_versamento = ids(i);
    FORALL i IN 1 .. ids.COUNT
        DELETE FROM promemoria WHERE id_versamento = ids(i);
    esito('Cancellazione promemoria sulle pendenze', SQL%ROWCOUNT);

    FORALL i IN 1 .. ids.COUNT
        INSERT INTO operazioni_aaaammgg SELECT * FROM operazioni WHERE id_stampa IN (SELECT id FROM stampe WHERE id_versamento = ids(i));
    FORALL i IN 1 .. ids.COUNT
        DELETE FROM operazioni WHERE id_stampa IN (SELECT id FROM stampe WHERE id_versamento = ids(i));
    esito('Cancellazione operazioni sulle stampe', SQL%ROWCOUNT);
    FORALL i IN 1 .. ids.COUNT
        INSERT INTO operazioni_aaaammgg SELECT * FROM operazioni WHERE id_versamento = ids(i);
    FORALL i IN 1 .. ids.COUNT
        DELETE FROM operazioni WHERE id_versamento = ids(i);
    esito('Cancellazione operazioni sulle pendenze', SQL%ROWCOUNT);

    FORALL i IN 1 .. ids.COUNT
        INSERT INTO stampe_aaaammgg SELECT * FROM stampe WHERE id_versamento = ids(i);
    FORALL i IN 1 .. ids.COUNT
        DELETE FROM stampe WHERE id_versamento = ids(i);
    esito('Cancellazione stampe', SQL%ROWCOUNT);

    FORALL i IN 1 .. ids.COUNT
        INSERT INTO allegati_aaaammgg SELECT * FROM allegati WHERE id_versamento = ids(i);
    FORALL i IN 1 .. ids.COUNT
        DELETE FROM allegati WHERE id_versamento = ids(i);
    esito('Cancellazione allegati', SQL%ROWCOUNT);

    FORALL i IN 1 .. ids.COUNT
        INSERT INTO rpt_aaaammgg SELECT * FROM rpt WHERE id_versamento = ids(i);
    FORALL i IN 1 .. ids.COUNT
        DELETE FROM rpt WHERE id_versamento = ids(i);
    esito('Cancellazione RPT', SQL%ROWCOUNT);

    FORALL i IN 1 .. ids.COUNT
        INSERT INTO singoli_versamenti_aaaammgg SELECT * FROM singoli_versamenti WHERE id_versamento = ids(i);
    FORALL i IN 1 .. ids.COUNT
        DELETE FROM singoli_versamenti WHERE id_versamento = ids(i);
    esito('Cancellazione voci delle pendenze', SQL%ROWCOUNT);

    FORALL i IN 1 .. ids.COUNT
        INSERT INTO versamenti_aaaammgg SELECT * FROM versamenti WHERE id = ids(i);
    FORALL i IN 1 .. ids.COUNT
        DELETE FROM versamenti WHERE id = ids(i);
    esito('Cancellazione pendenze', SQL%ROWCOUNT);
END;
/

COMMIT;
