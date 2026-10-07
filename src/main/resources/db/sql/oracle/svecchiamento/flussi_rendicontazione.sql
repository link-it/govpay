-- =============================================================================
-- Svecchiamento FLUSSI DI RENDICONTAZIONE - Oracle
--
-- Elimina i flussi di rendicontazione con data_ora_flusso piu' vecchia di
-- retention_fr giorni, con le loro rendicontazioni e i loro eventi, le pendenze
-- che rendicontano e gli incassi che li riconciliano.
--
-- Le pendenze vengono cancellate qualunque sia il loro stato: una pendenza
-- rendicontata puo' risultare non pagata perche' non ancora riconciliata, o
-- essere stata pagata senza RT. Ne viene cancellata tutta la struttura, come in
-- pendenze_pagate: pagamenti, RPT, voci, notifiche, promemoria, stampe,
-- operazioni e allegati. Una pendenza resta se:
--   - una sua voce o un suo pagamento e' rendicontato anche in un flusso piu'
--     recente della soglia: una revoca, un'altra revisione del flusso, un
--     pagamento multibeneficiario rendicontato da piu' enti;
--   - ha un pagamento piu' recente della soglia.
-- In quel caso perde solo le rendicontazioni dei flussi vecchi, e i pagamenti
-- che esse riferivano risultano non rendicontati (vista v_vrs_non_rnd) finche'
-- anche gli altri flussi non superano la soglia: succede solo alle pendenze a
-- cavallo della soglia.
--
-- I documenti delle pendenze cancellate restano: quelli rimasti senza pendenze
-- li cancella la sezione documenti_orfani.
--
-- Con il flusso va via anche l'incasso che lo riconcilia, con i suoi eventi: la
-- riconciliazione collega un incasso a un solo flusso. I pagamenti rimasti che
-- riferiscono l'incasso, quelli delle pendenze che restano, vengono scollegati
-- (id_incasso a NULL) e conservano lo stato INCASSATO.
--
-- Gli id da cancellare vengono raccolti all'inizio, perche' le DELETE cambiano
-- le rendicontazioni su cui si basa la selezione. Una tabella temporanea
-- richiederebbe DDL, quindi un commit implicito: gli id stanno in collection
-- PL/SQL, e le DELETE le scorrono con FORALL.
--
-- La soglia viene calcolata una volta sola, all'inizio, e usata come costante:
-- in Oracle CURRENT_DATE e' rivalutato a ogni istruzione, e a cavallo della
-- mezzanotte istruzioni diverse selezionerebbero righe diverse.
--
-- Prima di essere cancellate, le righe vengono copiate in tabelle di archivio
-- con lo stesso nome e il suffisso _aaaammgg (rendicontazioni, pagamenti,
-- notifiche, notifiche_app_io, promemoria, operazioni, stampe, allegati, rpt,
-- singoli_versamenti, versamenti, fr, incassi), create se non esistono, senza
-- vincoli ne' indici, e nella stessa transazione delle cancellazioni:
-- l'archivio contiene esattamente le righe cancellate. svecchiamento-db.sh
-- sostituisce aaaammgg con la data dell'esecuzione; eseguito da solo, lo
-- script archivia nelle tabelle _aaaammgg. Gli eventi non vengono archiviati.
-- Salvare ed eliminare le tabelle di archivio e' compito del DBA.
--
-- Uso: sqlplus utente/password@host:porta/servizio @flussi_rendicontazione.sql
--
-- Il valore qui sotto e' il default. svecchiamento-db.sh lo sostituisce quando
-- gli si passa --retention-flussi_rendicontazione.
-- =============================================================================

DEFINE retention_fr = 730;

PROMPT
PROMPT --- Svecchiamento FLUSSI DI RENDICONTAZIONE ---
PROMPT Retention: &retention_fr giorni

COLUMN soglia_fr NEW_VALUE soglia_fr NOPRINT
SELECT TO_CHAR((CURRENT_DATE - &retention_fr), 'YYYYMMDDHH24MISS') AS soglia_fr FROM dual;

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
    crea('rendicontazioni_aaaammgg', 'rendicontazioni');
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
    crea('fr_aaaammgg', 'fr');
    crea('incassi_aaaammgg', 'incassi');
END;
/

SET SERVEROUTPUT ON

PROMPT Flussi e pendenze da cancellare:
SELECT (SELECT COUNT(*) FROM fr WHERE data_ora_flusso < TO_DATE('&soglia_fr', 'YYYYMMDDHH24MISS')) AS flussi_da_cancellare,
       (SELECT COUNT(*) FROM (
            SELECT v.id FROM versamenti v
            WHERE (EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN rendicontazioni r ON r.id_singolo_versamento = sv.id JOIN fr f ON f.id = r.id_fr WHERE sv.id_versamento = v.id AND f.data_ora_flusso < TO_DATE('&soglia_fr', 'YYYYMMDDHH24MISS'))
                OR EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN pagamenti p ON p.id_singolo_versamento = sv.id JOIN rendicontazioni r ON r.id_pagamento = p.id JOIN fr f ON f.id = r.id_fr WHERE sv.id_versamento = v.id AND f.data_ora_flusso < TO_DATE('&soglia_fr', 'YYYYMMDDHH24MISS'))
                OR EXISTS (SELECT 1 FROM rpt JOIN pagamenti p ON p.id_rpt = rpt.id JOIN rendicontazioni r ON r.id_pagamento = p.id JOIN fr f ON f.id = r.id_fr WHERE rpt.id_versamento = v.id AND f.data_ora_flusso < TO_DATE('&soglia_fr', 'YYYYMMDDHH24MISS')))
              AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN rendicontazioni r ON r.id_singolo_versamento = sv.id JOIN fr f ON f.id = r.id_fr WHERE sv.id_versamento = v.id AND f.data_ora_flusso >= TO_DATE('&soglia_fr', 'YYYYMMDDHH24MISS'))
              AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN pagamenti p ON p.id_singolo_versamento = sv.id JOIN rendicontazioni r ON r.id_pagamento = p.id JOIN fr f ON f.id = r.id_fr WHERE sv.id_versamento = v.id AND f.data_ora_flusso >= TO_DATE('&soglia_fr', 'YYYYMMDDHH24MISS'))
              AND NOT EXISTS (SELECT 1 FROM rpt JOIN pagamenti p ON p.id_rpt = rpt.id JOIN rendicontazioni r ON r.id_pagamento = p.id JOIN fr f ON f.id = r.id_fr WHERE rpt.id_versamento = v.id AND f.data_ora_flusso >= TO_DATE('&soglia_fr', 'YYYYMMDDHH24MISS'))
              AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN pagamenti p ON p.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id AND p.data_pagamento >= TO_DATE('&soglia_fr', 'YYYYMMDDHH24MISS'))
              AND NOT EXISTS (SELECT 1 FROM rpt JOIN pagamenti p ON p.id_rpt = rpt.id WHERE rpt.id_versamento = v.id AND p.data_pagamento >= TO_DATE('&soglia_fr', 'YYYYMMDDHH24MISS'))
       )) AS pendenze_da_cancellare
FROM dual;

DECLARE
    TYPE t_id IS TABLE OF NUMBER;
    ids_fr t_id;
    ids_pendenze t_id;
    ids_incassi t_id;

    PROCEDURE esito(p_cosa VARCHAR2, p_righe NUMBER) IS
    BEGIN
        DBMS_OUTPUT.PUT_LINE(p_cosa || ': ' || p_righe || ' righe');
    END;
BEGIN
    SELECT id BULK COLLECT INTO ids_fr FROM fr WHERE data_ora_flusso < TO_DATE('&soglia_fr', 'YYYYMMDDHH24MISS');

    -- Una rendicontazione riferisce la pendenza sulla voce, o sul pagamento,
    -- che a sua volta e' legato alla voce o alla RPT. Le tre strade valgono sia
    -- per trovare le pendenze dei flussi vecchi sia per escludere quelle
    -- rendicontate anche in flussi recenti.
    SELECT v.id BULK COLLECT INTO ids_pendenze FROM versamenti v
    WHERE (EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN rendicontazioni r ON r.id_singolo_versamento = sv.id JOIN fr f ON f.id = r.id_fr WHERE sv.id_versamento = v.id AND f.data_ora_flusso < TO_DATE('&soglia_fr', 'YYYYMMDDHH24MISS'))
        OR EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN pagamenti p ON p.id_singolo_versamento = sv.id JOIN rendicontazioni r ON r.id_pagamento = p.id JOIN fr f ON f.id = r.id_fr WHERE sv.id_versamento = v.id AND f.data_ora_flusso < TO_DATE('&soglia_fr', 'YYYYMMDDHH24MISS'))
        OR EXISTS (SELECT 1 FROM rpt JOIN pagamenti p ON p.id_rpt = rpt.id JOIN rendicontazioni r ON r.id_pagamento = p.id JOIN fr f ON f.id = r.id_fr WHERE rpt.id_versamento = v.id AND f.data_ora_flusso < TO_DATE('&soglia_fr', 'YYYYMMDDHH24MISS')))
      AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN rendicontazioni r ON r.id_singolo_versamento = sv.id JOIN fr f ON f.id = r.id_fr WHERE sv.id_versamento = v.id AND f.data_ora_flusso >= TO_DATE('&soglia_fr', 'YYYYMMDDHH24MISS'))
      AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN pagamenti p ON p.id_singolo_versamento = sv.id JOIN rendicontazioni r ON r.id_pagamento = p.id JOIN fr f ON f.id = r.id_fr WHERE sv.id_versamento = v.id AND f.data_ora_flusso >= TO_DATE('&soglia_fr', 'YYYYMMDDHH24MISS'))
      AND NOT EXISTS (SELECT 1 FROM rpt JOIN pagamenti p ON p.id_rpt = rpt.id JOIN rendicontazioni r ON r.id_pagamento = p.id JOIN fr f ON f.id = r.id_fr WHERE rpt.id_versamento = v.id AND f.data_ora_flusso >= TO_DATE('&soglia_fr', 'YYYYMMDDHH24MISS'))
      AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN pagamenti p ON p.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id AND p.data_pagamento >= TO_DATE('&soglia_fr', 'YYYYMMDDHH24MISS'))
      AND NOT EXISTS (SELECT 1 FROM rpt JOIN pagamenti p ON p.id_rpt = rpt.id WHERE rpt.id_versamento = v.id AND p.data_pagamento >= TO_DATE('&soglia_fr', 'YYYYMMDDHH24MISS'));

    SELECT DISTINCT id_incasso BULK COLLECT INTO ids_incassi FROM fr
    WHERE data_ora_flusso < TO_DATE('&soglia_fr', 'YYYYMMDDHH24MISS') AND id_incasso IS NOT NULL;

    FORALL i IN 1 .. ids_fr.COUNT
        DELETE FROM eventi WHERE id_fr = ids_fr(i);
    esito('Cancellazione eventi dei flussi', SQL%ROWCOUNT);
    FORALL i IN 1 .. ids_fr.COUNT
        INSERT INTO rendicontazioni_aaaammgg SELECT * FROM rendicontazioni WHERE id_fr = ids_fr(i);
    FORALL i IN 1 .. ids_fr.COUNT
        DELETE FROM rendicontazioni WHERE id_fr = ids_fr(i);
    esito('Cancellazione rendicontazioni dei flussi', SQL%ROWCOUNT);

    FORALL i IN 1 .. ids_pendenze.COUNT
        INSERT INTO pagamenti_aaaammgg SELECT * FROM pagamenti WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento = ids_pendenze(i));
    FORALL i IN 1 .. ids_pendenze.COUNT
        DELETE FROM pagamenti WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento = ids_pendenze(i));
    esito('Cancellazione pagamenti sulle RPT', SQL%ROWCOUNT);
    FORALL i IN 1 .. ids_pendenze.COUNT
        INSERT INTO pagamenti_aaaammgg SELECT * FROM pagamenti WHERE id_singolo_versamento IN (SELECT id FROM singoli_versamenti WHERE id_versamento = ids_pendenze(i));
    FORALL i IN 1 .. ids_pendenze.COUNT
        DELETE FROM pagamenti WHERE id_singolo_versamento IN (SELECT id FROM singoli_versamenti WHERE id_versamento = ids_pendenze(i));
    esito('Cancellazione pagamenti sulle voci', SQL%ROWCOUNT);

    FORALL i IN 1 .. ids_pendenze.COUNT
        INSERT INTO notifiche_aaaammgg SELECT * FROM notifiche WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento = ids_pendenze(i));
    FORALL i IN 1 .. ids_pendenze.COUNT
        DELETE FROM notifiche WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento = ids_pendenze(i));
    esito('Cancellazione notifiche', SQL%ROWCOUNT);

    FORALL i IN 1 .. ids_pendenze.COUNT
        INSERT INTO notifiche_app_io_aaaammgg SELECT * FROM notifiche_app_io WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento = ids_pendenze(i));
    FORALL i IN 1 .. ids_pendenze.COUNT
        DELETE FROM notifiche_app_io WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento = ids_pendenze(i));
    esito('Cancellazione notifiche App IO sulle RPT', SQL%ROWCOUNT);
    FORALL i IN 1 .. ids_pendenze.COUNT
        INSERT INTO notifiche_app_io_aaaammgg SELECT * FROM notifiche_app_io WHERE id_versamento = ids_pendenze(i);
    FORALL i IN 1 .. ids_pendenze.COUNT
        DELETE FROM notifiche_app_io WHERE id_versamento = ids_pendenze(i);
    esito('Cancellazione notifiche App IO sulle pendenze', SQL%ROWCOUNT);

    FORALL i IN 1 .. ids_pendenze.COUNT
        INSERT INTO promemoria_aaaammgg SELECT * FROM promemoria WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento = ids_pendenze(i));
    FORALL i IN 1 .. ids_pendenze.COUNT
        DELETE FROM promemoria WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento = ids_pendenze(i));
    esito('Cancellazione promemoria sulle RPT', SQL%ROWCOUNT);
    FORALL i IN 1 .. ids_pendenze.COUNT
        INSERT INTO promemoria_aaaammgg SELECT * FROM promemoria WHERE id_versamento = ids_pendenze(i);
    FORALL i IN 1 .. ids_pendenze.COUNT
        DELETE FROM promemoria WHERE id_versamento = ids_pendenze(i);
    esito('Cancellazione promemoria sulle pendenze', SQL%ROWCOUNT);

    FORALL i IN 1 .. ids_pendenze.COUNT
        INSERT INTO operazioni_aaaammgg SELECT * FROM operazioni WHERE id_stampa IN (SELECT id FROM stampe WHERE id_versamento = ids_pendenze(i));
    FORALL i IN 1 .. ids_pendenze.COUNT
        DELETE FROM operazioni WHERE id_stampa IN (SELECT id FROM stampe WHERE id_versamento = ids_pendenze(i));
    esito('Cancellazione operazioni sulle stampe', SQL%ROWCOUNT);
    FORALL i IN 1 .. ids_pendenze.COUNT
        INSERT INTO operazioni_aaaammgg SELECT * FROM operazioni WHERE id_versamento = ids_pendenze(i);
    FORALL i IN 1 .. ids_pendenze.COUNT
        DELETE FROM operazioni WHERE id_versamento = ids_pendenze(i);
    esito('Cancellazione operazioni sulle pendenze', SQL%ROWCOUNT);

    FORALL i IN 1 .. ids_pendenze.COUNT
        INSERT INTO stampe_aaaammgg SELECT * FROM stampe WHERE id_versamento = ids_pendenze(i);
    FORALL i IN 1 .. ids_pendenze.COUNT
        DELETE FROM stampe WHERE id_versamento = ids_pendenze(i);
    esito('Cancellazione stampe', SQL%ROWCOUNT);

    FORALL i IN 1 .. ids_pendenze.COUNT
        INSERT INTO allegati_aaaammgg SELECT * FROM allegati WHERE id_versamento = ids_pendenze(i);
    FORALL i IN 1 .. ids_pendenze.COUNT
        DELETE FROM allegati WHERE id_versamento = ids_pendenze(i);
    esito('Cancellazione allegati', SQL%ROWCOUNT);

    FORALL i IN 1 .. ids_pendenze.COUNT
        INSERT INTO rpt_aaaammgg SELECT * FROM rpt WHERE id_versamento = ids_pendenze(i);
    FORALL i IN 1 .. ids_pendenze.COUNT
        DELETE FROM rpt WHERE id_versamento = ids_pendenze(i);
    esito('Cancellazione RPT', SQL%ROWCOUNT);

    FORALL i IN 1 .. ids_pendenze.COUNT
        INSERT INTO singoli_versamenti_aaaammgg SELECT * FROM singoli_versamenti WHERE id_versamento = ids_pendenze(i);
    FORALL i IN 1 .. ids_pendenze.COUNT
        DELETE FROM singoli_versamenti WHERE id_versamento = ids_pendenze(i);
    esito('Cancellazione voci delle pendenze', SQL%ROWCOUNT);

    FORALL i IN 1 .. ids_pendenze.COUNT
        INSERT INTO versamenti_aaaammgg SELECT * FROM versamenti WHERE id = ids_pendenze(i);
    FORALL i IN 1 .. ids_pendenze.COUNT
        DELETE FROM versamenti WHERE id = ids_pendenze(i);
    esito('Cancellazione pendenze', SQL%ROWCOUNT);

    FORALL i IN 1 .. ids_fr.COUNT
        INSERT INTO fr_aaaammgg SELECT * FROM fr WHERE id = ids_fr(i);
    FORALL i IN 1 .. ids_fr.COUNT
        DELETE FROM fr WHERE id = ids_fr(i);
    esito('Cancellazione flussi', SQL%ROWCOUNT);

    FORALL i IN 1 .. ids_incassi.COUNT
        UPDATE pagamenti SET id_incasso = NULL WHERE id_incasso = ids_incassi(i);
    esito('Pagamenti rimasti scollegati dagli incassi', SQL%ROWCOUNT);
    FORALL i IN 1 .. ids_incassi.COUNT
        DELETE FROM eventi WHERE id_incasso = ids_incassi(i);
    esito('Cancellazione eventi degli incassi', SQL%ROWCOUNT);
    FORALL i IN 1 .. ids_incassi.COUNT
        INSERT INTO incassi_aaaammgg SELECT * FROM incassi WHERE id = ids_incassi(i);
    FORALL i IN 1 .. ids_incassi.COUNT
        DELETE FROM incassi WHERE id = ids_incassi(i);
    esito('Cancellazione incassi', SQL%ROWCOUNT);
END;
/

COMMIT;
