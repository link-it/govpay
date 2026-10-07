-- =============================================================================
-- Svecchiamento PENDENZE ANNULLATE - Oracle
--
-- Elimina le pendenze in stato ANNULLATO non piu' aggiornate da piu' di
-- retention_annullate giorni, con tutto cio' che le referenzia, nell'ordine
-- imposto dalle chiavi esterne.
--
-- La data di riferimento e' data_ora_ultimo_aggiornamento: GovPay non registra
-- la data dell'annullamento, e l'annullamento da API o da tracciato non aggiorna
-- questa colonna. Una pendenza vecchia annullata di recente puo' quindi essere
-- svecchiata al primo giro; resta comunque nell'archivio.
--
-- Sono escluse le pendenze che hanno anche una sola voce rendicontata o un
-- pagamento, sulle voci o sulle RPT: non sono pendenze abbandonate, e le loro
-- righe sono referenziate da flussi e incassi che qui non si toccano.
--
-- I documenti a cui le pendenze appartengono non vengono cancellati: un
-- documento raggruppa piu' pendenze, e non e' detto che siano tutte annullate.
-- Quelli rimasti senza pendenze li cancella la sezione documenti_orfani.
--
-- Il criterio di selezione e' ripetuto in ogni DELETE invece di essere
-- raccolto in una tabella temporanea, che su Oracle richiederebbe DDL e quindi
-- un commit implicito. E' corretto perche' il criterio e' stabile durante la
-- cancellazione: dipende solo da stato e data di ultimo aggiornamento della pendenza e
-- dall'assenza di rendicontazioni e pagamenti, che qui non si cancellano.
--
-- La soglia viene calcolata una volta sola, all'inizio, e usata come costante:
-- in Oracle CURRENT_DATE e' rivalutato a ogni istruzione, e a cavallo della
-- mezzanotte istruzioni diverse selezionerebbero righe diverse.
--
-- Prima di essere cancellate, le righe vengono copiate in tabelle di archivio
-- con lo stesso nome e il suffisso _aaaammgg (notifiche, notifiche_app_io,
-- promemoria, operazioni, stampe, allegati, rpt, singoli_versamenti,
-- versamenti), create se non esistono, senza vincoli ne' indici, e nella
-- stessa transazione delle cancellazioni: l'archivio contiene esattamente le
-- righe cancellate. svecchiamento-db.sh sostituisce aaaammgg con la data
-- dell'esecuzione; eseguito da solo, lo script archivia nelle tabelle
-- _aaaammgg. Gli eventi non vengono archiviati. Salvare ed eliminare le
-- tabelle di archivio e' compito del DBA.
--
-- Uso: sqlplus utente/password@host:porta/servizio @pendenze_annullate.sql
--
-- Il valore qui sotto e' il default. svecchiamento-db.sh lo sostituisce quando
-- gli si passa --retention-pendenze_annullate.
-- =============================================================================

DEFINE retention_annullate = 365;

PROMPT
PROMPT --- Svecchiamento PENDENZE ANNULLATE ---
PROMPT Retention: &retention_annullate giorni

COLUMN soglia_annullate NEW_VALUE soglia_annullate NOPRINT
SELECT TO_CHAR((CURRENT_DATE - &retention_annullate), 'YYYYMMDDHH24MISS') AS soglia_annullate FROM dual;

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

PROMPT Pendenze da cancellare:
SELECT COUNT(*) AS pendenze_da_cancellare FROM (
    SELECT v.id FROM versamenti v
    WHERE v.stato_versamento = 'ANNULLATO'
      AND v.data_ora_ultimo_aggiornamento < TO_DATE('&soglia_annullate', 'YYYYMMDDHH24MISS')
      AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN rendicontazioni r ON r.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
      AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN pagamenti p ON p.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
      AND NOT EXISTS (SELECT 1 FROM rpt JOIN pagamenti p ON p.id_rpt = rpt.id WHERE rpt.id_versamento = v.id)
);

PROMPT Cancellazione notifiche...
INSERT INTO notifiche_aaaammgg SELECT * FROM notifiche WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento IN (
    SELECT v.id FROM versamenti v
    WHERE v.stato_versamento = 'ANNULLATO'
      AND v.data_ora_ultimo_aggiornamento < TO_DATE('&soglia_annullate', 'YYYYMMDDHH24MISS')
      AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN rendicontazioni r ON r.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
      AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN pagamenti p ON p.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
      AND NOT EXISTS (SELECT 1 FROM rpt JOIN pagamenti p ON p.id_rpt = rpt.id WHERE rpt.id_versamento = v.id)
));
DELETE FROM notifiche WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento IN (
    SELECT v.id FROM versamenti v
    WHERE v.stato_versamento = 'ANNULLATO'
      AND v.data_ora_ultimo_aggiornamento < TO_DATE('&soglia_annullate', 'YYYYMMDDHH24MISS')
      AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN rendicontazioni r ON r.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
      AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN pagamenti p ON p.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
      AND NOT EXISTS (SELECT 1 FROM rpt JOIN pagamenti p ON p.id_rpt = rpt.id WHERE rpt.id_versamento = v.id)
));

PROMPT Cancellazione notifiche App IO...
INSERT INTO notifiche_app_io_aaaammgg SELECT * FROM notifiche_app_io WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento IN (
    SELECT v.id FROM versamenti v
    WHERE v.stato_versamento = 'ANNULLATO'
      AND v.data_ora_ultimo_aggiornamento < TO_DATE('&soglia_annullate', 'YYYYMMDDHH24MISS')
      AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN rendicontazioni r ON r.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
      AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN pagamenti p ON p.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
      AND NOT EXISTS (SELECT 1 FROM rpt JOIN pagamenti p ON p.id_rpt = rpt.id WHERE rpt.id_versamento = v.id)
));
DELETE FROM notifiche_app_io WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento IN (
    SELECT v.id FROM versamenti v
    WHERE v.stato_versamento = 'ANNULLATO'
      AND v.data_ora_ultimo_aggiornamento < TO_DATE('&soglia_annullate', 'YYYYMMDDHH24MISS')
      AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN rendicontazioni r ON r.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
      AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN pagamenti p ON p.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
      AND NOT EXISTS (SELECT 1 FROM rpt JOIN pagamenti p ON p.id_rpt = rpt.id WHERE rpt.id_versamento = v.id)
));
INSERT INTO notifiche_app_io_aaaammgg SELECT * FROM notifiche_app_io WHERE id_versamento IN (
    SELECT v.id FROM versamenti v
    WHERE v.stato_versamento = 'ANNULLATO'
      AND v.data_ora_ultimo_aggiornamento < TO_DATE('&soglia_annullate', 'YYYYMMDDHH24MISS')
      AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN rendicontazioni r ON r.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
      AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN pagamenti p ON p.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
      AND NOT EXISTS (SELECT 1 FROM rpt JOIN pagamenti p ON p.id_rpt = rpt.id WHERE rpt.id_versamento = v.id)
);
DELETE FROM notifiche_app_io WHERE id_versamento IN (
    SELECT v.id FROM versamenti v
    WHERE v.stato_versamento = 'ANNULLATO'
      AND v.data_ora_ultimo_aggiornamento < TO_DATE('&soglia_annullate', 'YYYYMMDDHH24MISS')
      AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN rendicontazioni r ON r.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
      AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN pagamenti p ON p.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
      AND NOT EXISTS (SELECT 1 FROM rpt JOIN pagamenti p ON p.id_rpt = rpt.id WHERE rpt.id_versamento = v.id)
);

PROMPT Cancellazione promemoria...
INSERT INTO promemoria_aaaammgg SELECT * FROM promemoria WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento IN (
    SELECT v.id FROM versamenti v
    WHERE v.stato_versamento = 'ANNULLATO'
      AND v.data_ora_ultimo_aggiornamento < TO_DATE('&soglia_annullate', 'YYYYMMDDHH24MISS')
      AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN rendicontazioni r ON r.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
      AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN pagamenti p ON p.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
      AND NOT EXISTS (SELECT 1 FROM rpt JOIN pagamenti p ON p.id_rpt = rpt.id WHERE rpt.id_versamento = v.id)
));
DELETE FROM promemoria WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento IN (
    SELECT v.id FROM versamenti v
    WHERE v.stato_versamento = 'ANNULLATO'
      AND v.data_ora_ultimo_aggiornamento < TO_DATE('&soglia_annullate', 'YYYYMMDDHH24MISS')
      AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN rendicontazioni r ON r.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
      AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN pagamenti p ON p.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
      AND NOT EXISTS (SELECT 1 FROM rpt JOIN pagamenti p ON p.id_rpt = rpt.id WHERE rpt.id_versamento = v.id)
));
INSERT INTO promemoria_aaaammgg SELECT * FROM promemoria WHERE id_versamento IN (
    SELECT v.id FROM versamenti v
    WHERE v.stato_versamento = 'ANNULLATO'
      AND v.data_ora_ultimo_aggiornamento < TO_DATE('&soglia_annullate', 'YYYYMMDDHH24MISS')
      AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN rendicontazioni r ON r.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
      AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN pagamenti p ON p.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
      AND NOT EXISTS (SELECT 1 FROM rpt JOIN pagamenti p ON p.id_rpt = rpt.id WHERE rpt.id_versamento = v.id)
);
DELETE FROM promemoria WHERE id_versamento IN (
    SELECT v.id FROM versamenti v
    WHERE v.stato_versamento = 'ANNULLATO'
      AND v.data_ora_ultimo_aggiornamento < TO_DATE('&soglia_annullate', 'YYYYMMDDHH24MISS')
      AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN rendicontazioni r ON r.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
      AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN pagamenti p ON p.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
      AND NOT EXISTS (SELECT 1 FROM rpt JOIN pagamenti p ON p.id_rpt = rpt.id WHERE rpt.id_versamento = v.id)
);

PROMPT Cancellazione operazioni...
INSERT INTO operazioni_aaaammgg SELECT * FROM operazioni WHERE id_stampa IN (SELECT id FROM stampe WHERE id_versamento IN (
    SELECT v.id FROM versamenti v
    WHERE v.stato_versamento = 'ANNULLATO'
      AND v.data_ora_ultimo_aggiornamento < TO_DATE('&soglia_annullate', 'YYYYMMDDHH24MISS')
      AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN rendicontazioni r ON r.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
      AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN pagamenti p ON p.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
      AND NOT EXISTS (SELECT 1 FROM rpt JOIN pagamenti p ON p.id_rpt = rpt.id WHERE rpt.id_versamento = v.id)
));
DELETE FROM operazioni WHERE id_stampa IN (SELECT id FROM stampe WHERE id_versamento IN (
    SELECT v.id FROM versamenti v
    WHERE v.stato_versamento = 'ANNULLATO'
      AND v.data_ora_ultimo_aggiornamento < TO_DATE('&soglia_annullate', 'YYYYMMDDHH24MISS')
      AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN rendicontazioni r ON r.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
      AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN pagamenti p ON p.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
      AND NOT EXISTS (SELECT 1 FROM rpt JOIN pagamenti p ON p.id_rpt = rpt.id WHERE rpt.id_versamento = v.id)
));
INSERT INTO operazioni_aaaammgg SELECT * FROM operazioni WHERE id_versamento IN (
    SELECT v.id FROM versamenti v
    WHERE v.stato_versamento = 'ANNULLATO'
      AND v.data_ora_ultimo_aggiornamento < TO_DATE('&soglia_annullate', 'YYYYMMDDHH24MISS')
      AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN rendicontazioni r ON r.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
      AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN pagamenti p ON p.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
      AND NOT EXISTS (SELECT 1 FROM rpt JOIN pagamenti p ON p.id_rpt = rpt.id WHERE rpt.id_versamento = v.id)
);
DELETE FROM operazioni WHERE id_versamento IN (
    SELECT v.id FROM versamenti v
    WHERE v.stato_versamento = 'ANNULLATO'
      AND v.data_ora_ultimo_aggiornamento < TO_DATE('&soglia_annullate', 'YYYYMMDDHH24MISS')
      AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN rendicontazioni r ON r.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
      AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN pagamenti p ON p.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
      AND NOT EXISTS (SELECT 1 FROM rpt JOIN pagamenti p ON p.id_rpt = rpt.id WHERE rpt.id_versamento = v.id)
);

PROMPT Cancellazione stampe...
INSERT INTO stampe_aaaammgg SELECT * FROM stampe WHERE id_versamento IN (
    SELECT v.id FROM versamenti v
    WHERE v.stato_versamento = 'ANNULLATO'
      AND v.data_ora_ultimo_aggiornamento < TO_DATE('&soglia_annullate', 'YYYYMMDDHH24MISS')
      AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN rendicontazioni r ON r.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
      AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN pagamenti p ON p.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
      AND NOT EXISTS (SELECT 1 FROM rpt JOIN pagamenti p ON p.id_rpt = rpt.id WHERE rpt.id_versamento = v.id)
);
DELETE FROM stampe WHERE id_versamento IN (
    SELECT v.id FROM versamenti v
    WHERE v.stato_versamento = 'ANNULLATO'
      AND v.data_ora_ultimo_aggiornamento < TO_DATE('&soglia_annullate', 'YYYYMMDDHH24MISS')
      AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN rendicontazioni r ON r.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
      AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN pagamenti p ON p.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
      AND NOT EXISTS (SELECT 1 FROM rpt JOIN pagamenti p ON p.id_rpt = rpt.id WHERE rpt.id_versamento = v.id)
);

PROMPT Cancellazione allegati...
INSERT INTO allegati_aaaammgg SELECT * FROM allegati WHERE id_versamento IN (
    SELECT v.id FROM versamenti v
    WHERE v.stato_versamento = 'ANNULLATO'
      AND v.data_ora_ultimo_aggiornamento < TO_DATE('&soglia_annullate', 'YYYYMMDDHH24MISS')
      AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN rendicontazioni r ON r.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
      AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN pagamenti p ON p.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
      AND NOT EXISTS (SELECT 1 FROM rpt JOIN pagamenti p ON p.id_rpt = rpt.id WHERE rpt.id_versamento = v.id)
);
DELETE FROM allegati WHERE id_versamento IN (
    SELECT v.id FROM versamenti v
    WHERE v.stato_versamento = 'ANNULLATO'
      AND v.data_ora_ultimo_aggiornamento < TO_DATE('&soglia_annullate', 'YYYYMMDDHH24MISS')
      AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN rendicontazioni r ON r.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
      AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN pagamenti p ON p.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
      AND NOT EXISTS (SELECT 1 FROM rpt JOIN pagamenti p ON p.id_rpt = rpt.id WHERE rpt.id_versamento = v.id)
);

PROMPT Cancellazione RPT...
INSERT INTO rpt_aaaammgg SELECT * FROM rpt WHERE id_versamento IN (
    SELECT v.id FROM versamenti v
    WHERE v.stato_versamento = 'ANNULLATO'
      AND v.data_ora_ultimo_aggiornamento < TO_DATE('&soglia_annullate', 'YYYYMMDDHH24MISS')
      AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN rendicontazioni r ON r.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
      AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN pagamenti p ON p.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
      AND NOT EXISTS (SELECT 1 FROM rpt JOIN pagamenti p ON p.id_rpt = rpt.id WHERE rpt.id_versamento = v.id)
);
DELETE FROM rpt WHERE id_versamento IN (
    SELECT v.id FROM versamenti v
    WHERE v.stato_versamento = 'ANNULLATO'
      AND v.data_ora_ultimo_aggiornamento < TO_DATE('&soglia_annullate', 'YYYYMMDDHH24MISS')
      AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN rendicontazioni r ON r.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
      AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN pagamenti p ON p.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
      AND NOT EXISTS (SELECT 1 FROM rpt JOIN pagamenti p ON p.id_rpt = rpt.id WHERE rpt.id_versamento = v.id)
);

PROMPT Cancellazione voci delle pendenze...
INSERT INTO singoli_versamenti_aaaammgg SELECT * FROM singoli_versamenti WHERE id_versamento IN (
    SELECT v.id FROM versamenti v
    WHERE v.stato_versamento = 'ANNULLATO'
      AND v.data_ora_ultimo_aggiornamento < TO_DATE('&soglia_annullate', 'YYYYMMDDHH24MISS')
      AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN rendicontazioni r ON r.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
      AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN pagamenti p ON p.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
      AND NOT EXISTS (SELECT 1 FROM rpt JOIN pagamenti p ON p.id_rpt = rpt.id WHERE rpt.id_versamento = v.id)
);
DELETE FROM singoli_versamenti WHERE id_versamento IN (
    SELECT v.id FROM versamenti v
    WHERE v.stato_versamento = 'ANNULLATO'
      AND v.data_ora_ultimo_aggiornamento < TO_DATE('&soglia_annullate', 'YYYYMMDDHH24MISS')
      AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN rendicontazioni r ON r.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
      AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN pagamenti p ON p.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
      AND NOT EXISTS (SELECT 1 FROM rpt JOIN pagamenti p ON p.id_rpt = rpt.id WHERE rpt.id_versamento = v.id)
);

PROMPT Cancellazione pendenze...
INSERT INTO versamenti_aaaammgg SELECT * FROM versamenti WHERE id IN (
    SELECT v.id FROM versamenti v
    WHERE v.stato_versamento = 'ANNULLATO'
      AND v.data_ora_ultimo_aggiornamento < TO_DATE('&soglia_annullate', 'YYYYMMDDHH24MISS')
      AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN rendicontazioni r ON r.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
      AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN pagamenti p ON p.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
      AND NOT EXISTS (SELECT 1 FROM rpt JOIN pagamenti p ON p.id_rpt = rpt.id WHERE rpt.id_versamento = v.id)
);
DELETE FROM versamenti WHERE id IN (
    SELECT v.id FROM versamenti v
    WHERE v.stato_versamento = 'ANNULLATO'
      AND v.data_ora_ultimo_aggiornamento < TO_DATE('&soglia_annullate', 'YYYYMMDDHH24MISS')
      AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN rendicontazioni r ON r.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
      AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN pagamenti p ON p.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
      AND NOT EXISTS (SELECT 1 FROM rpt JOIN pagamenti p ON p.id_rpt = rpt.id WHERE rpt.id_versamento = v.id)
);

COMMIT;
