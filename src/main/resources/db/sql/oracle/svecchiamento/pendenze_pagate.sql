-- =============================================================================
-- Svecchiamento PENDENZE PAGATE - Oracle
--
-- Elimina le pendenze pagate il cui ultimo pagamento e' piu' vecchio di
-- retention_pendenze_pagate mesi, con le RPT, i pagamenti e tutto cio' che le
-- referenzia, nell'ordine imposto dalle chiavi esterne.
--
-- Una pendenza rientra se:
--   - e' in uno stato di pagamento eseguito: ESEGUITO, PARZIALMENTE_ESEGUITO,
--     ESEGUITO_ALTRO_CANALE, ESEGUITO_SENZA_RPT, INCASSATO;
--   - e' stata pagata con ricevuta piu' di retention_pendenze_pagate mesi fa:
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
-- le pendenze scadute. Gli incassi neppure: sono i pagamenti a referenziarli.
--
-- Gli id vengono raccolti una volta sola, e il criterio, con le sue sottoquery
-- su pagamenti e rendicontazioni, viene valutato una volta invece che in ogni
-- DELETE. Una tabella temporanea richiederebbe DDL, quindi un commit implicito:
-- gli id stanno in una collection PL/SQL, e le DELETE la scorrono con FORALL.
--
-- La transazione e' SERIALIZABLE: il conteggio, l'export delle RT e la raccolta
-- degli id vedono la stessa fotografia dei dati, e le RT esportate sono
-- esattamente quelle delle RPT cancellate. Se nel frattempo un'altra sessione
-- modifica una delle righe da cancellare, la DELETE fallisce con ORA-08177 e
-- la sezione viene annullata: basta rieseguirla.
--
-- Le RT possono essere salvate su file prima della cancellazione: la riga
-- marcata export-rt qui sotto viene sostituita da svecchiamento-db.sh con il
-- contenuto di pendenze_pagate-export-rt.sql quando gli si passa --export-rt.
-- Eseguito da solo, lo script non salva le RT.
--
-- Uso: sqlplus utente/password@host:porta/servizio @pendenze_pagate.sql
--
-- Il valore qui sotto e' il default. svecchiamento-db.sh lo sostituisce quando
-- gli si passa --retention-pendenze_pagate.
-- =============================================================================

DEFINE retention_pendenze_pagate = 24;

PROMPT
PROMPT --- Svecchiamento PENDENZE PAGATE ---
PROMPT Retention: &retention_pendenze_pagate mesi

SET SERVEROUTPUT ON

SET TRANSACTION ISOLATION LEVEL SERIALIZABLE;

PROMPT Pendenze da cancellare:
SELECT COUNT(*) AS pendenze_da_cancellare FROM (
    SELECT v.id FROM versamenti v
    WHERE v.stato_versamento IN ('ESEGUITO', 'PARZIALMENTE_ESEGUITO', 'ESEGUITO_ALTRO_CANALE', 'ESEGUITO_SENZA_RPT', 'INCASSATO')
      AND v.data_pagamento < ADD_MONTHS(CURRENT_DATE, -&retention_pendenze_pagate)
      AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN pagamenti p ON p.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id AND p.data_pagamento >= ADD_MONTHS(CURRENT_DATE, -&retention_pendenze_pagate))
      AND NOT EXISTS (SELECT 1 FROM rpt JOIN pagamenti p ON p.id_rpt = rpt.id WHERE rpt.id_versamento = v.id AND p.data_pagamento >= ADD_MONTHS(CURRENT_DATE, -&retention_pendenze_pagate))
      AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN rendicontazioni r ON r.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
      AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN pagamenti p ON p.id_singolo_versamento = sv.id JOIN rendicontazioni r ON r.id_pagamento = p.id WHERE sv.id_versamento = v.id)
      AND NOT EXISTS (SELECT 1 FROM rpt JOIN pagamenti p ON p.id_rpt = rpt.id JOIN rendicontazioni r ON r.id_pagamento = p.id WHERE rpt.id_versamento = v.id)
);

-- export-rt

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
          AND v.data_pagamento < ADD_MONTHS(CURRENT_DATE, -&retention_pendenze_pagate)
          AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN pagamenti p ON p.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id AND p.data_pagamento >= ADD_MONTHS(CURRENT_DATE, -&retention_pendenze_pagate))
          AND NOT EXISTS (SELECT 1 FROM rpt JOIN pagamenti p ON p.id_rpt = rpt.id WHERE rpt.id_versamento = v.id AND p.data_pagamento >= ADD_MONTHS(CURRENT_DATE, -&retention_pendenze_pagate))
          AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN rendicontazioni r ON r.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
          AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN pagamenti p ON p.id_singolo_versamento = sv.id JOIN rendicontazioni r ON r.id_pagamento = p.id WHERE sv.id_versamento = v.id)
          AND NOT EXISTS (SELECT 1 FROM rpt JOIN pagamenti p ON p.id_rpt = rpt.id JOIN rendicontazioni r ON r.id_pagamento = p.id WHERE rpt.id_versamento = v.id);

    FORALL i IN 1 .. ids.COUNT
        DELETE FROM pagamenti WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento = ids(i));
    esito('Cancellazione pagamenti sulle RPT', SQL%ROWCOUNT);
    FORALL i IN 1 .. ids.COUNT
        DELETE FROM pagamenti WHERE id_singolo_versamento IN (SELECT id FROM singoli_versamenti WHERE id_versamento = ids(i));
    esito('Cancellazione pagamenti sulle voci', SQL%ROWCOUNT);

    FORALL i IN 1 .. ids.COUNT
        DELETE FROM notifiche WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento = ids(i));
    esito('Cancellazione notifiche', SQL%ROWCOUNT);

    FORALL i IN 1 .. ids.COUNT
        DELETE FROM notifiche_app_io WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento = ids(i));
    esito('Cancellazione notifiche App IO sulle RPT', SQL%ROWCOUNT);
    FORALL i IN 1 .. ids.COUNT
        DELETE FROM notifiche_app_io WHERE id_versamento = ids(i);
    esito('Cancellazione notifiche App IO sulle pendenze', SQL%ROWCOUNT);

    FORALL i IN 1 .. ids.COUNT
        DELETE FROM promemoria WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento = ids(i));
    esito('Cancellazione promemoria sulle RPT', SQL%ROWCOUNT);
    FORALL i IN 1 .. ids.COUNT
        DELETE FROM promemoria WHERE id_versamento = ids(i);
    esito('Cancellazione promemoria sulle pendenze', SQL%ROWCOUNT);

    FORALL i IN 1 .. ids.COUNT
        DELETE FROM operazioni WHERE id_stampa IN (SELECT id FROM stampe WHERE id_versamento = ids(i));
    esito('Cancellazione operazioni sulle stampe', SQL%ROWCOUNT);
    FORALL i IN 1 .. ids.COUNT
        DELETE FROM operazioni WHERE id_versamento = ids(i);
    esito('Cancellazione operazioni sulle pendenze', SQL%ROWCOUNT);

    FORALL i IN 1 .. ids.COUNT
        DELETE FROM stampe WHERE id_versamento = ids(i);
    esito('Cancellazione stampe', SQL%ROWCOUNT);

    FORALL i IN 1 .. ids.COUNT
        DELETE FROM allegati WHERE id_versamento = ids(i);
    esito('Cancellazione allegati', SQL%ROWCOUNT);

    FORALL i IN 1 .. ids.COUNT
        DELETE FROM rpt WHERE id_versamento = ids(i);
    esito('Cancellazione RPT', SQL%ROWCOUNT);

    FORALL i IN 1 .. ids.COUNT
        DELETE FROM singoli_versamenti WHERE id_versamento = ids(i);
    esito('Cancellazione voci delle pendenze', SQL%ROWCOUNT);

    FORALL i IN 1 .. ids.COUNT
        DELETE FROM versamenti WHERE id = ids(i);
    esito('Cancellazione pendenze', SQL%ROWCOUNT);
END;
/

COMMIT;
