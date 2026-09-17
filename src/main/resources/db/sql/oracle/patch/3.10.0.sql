-- 3.10.0

-- Rimozione connettore FTP dall'intermediario: eliminazione dei connettori
-- FTP censiti nella tabella connettori e drop della colonna cod_connettore_ftp
-- dalla tabella intermediari.
-- La DELETE va eseguita solo se la colonna c'e' ancora: riapplicando la patch a
-- un'installazione gia' alla 3.10 la colonna e' stata eliminata poco sotto, e la
-- sottoquery fallirebbe. Lo statement e' dinamico perche' altrimenti il
-- riferimento alla colonna assente sarebbe un errore di compilazione, che la
-- guardia non eviterebbe.
DECLARE
    n NUMBER;
BEGIN
    SELECT COUNT(*) INTO n FROM user_tab_columns
     WHERE table_name = 'INTERMEDIARI' AND column_name = 'COD_CONNETTORE_FTP';
    IF n > 0 THEN
        EXECUTE IMMEDIATE 'DELETE FROM connettori WHERE cod_connettore IN (SELECT cod_connettore_ftp FROM intermediari WHERE cod_connettore_ftp IS NOT NULL)';
    END IF;
END;
/

-- Oracle non supporta IF EXISTS su ALTER TABLE ... DROP COLUMN:
-- blocco PL/SQL idempotente che ignora ORA-00904 (colonna gia' assente).
BEGIN
    EXECUTE IMMEDIATE 'ALTER TABLE intermediari DROP COLUMN cod_connettore_ftp';
EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE != -904 THEN
            RAISE;
        END IF;
END;
/

-- Tracciamento dell'IP del richiedente sull'audit trail.
-- Oracle non supporta IF NOT EXISTS su ADD: blocchi PL/SQL idempotenti che
-- ignorano ORA-01430 (colonna gia' presente), come per operatori.preferenze
-- piu' sotto.
BEGIN
    EXECUTE IMMEDIATE 'ALTER TABLE gp_audit ADD (ip_richiedente VARCHAR2(45 CHAR))';
EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE != -1430 THEN
            RAISE;
        END IF;
END;
/

-- Integrazione a SEND: attualizzazione dell'importo della pendenza
-- con le spese di notifica sostenute tramite SEND.
BEGIN
    EXECUTE IMMEDIATE 'ALTER TABLE versamenti ADD send_abilitato NUMBER DEFAULT 0 NOT NULL';
EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE != -1430 THEN
            RAISE;
        END IF;
END;
/

BEGIN
    EXECUTE IMMEDIATE 'ALTER TABLE versamenti ADD send_importo_totale BINARY_DOUBLE';
EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE != -1430 THEN
            RAISE;
        END IF;
END;
/

BEGIN
    EXECUTE IMMEDIATE 'ALTER TABLE versamenti ADD send_data_aggiornamento TIMESTAMP';
EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE != -1430 THEN
            RAISE;
        END IF;
END;
/

BEGIN
    EXECUTE IMMEDIATE 'ALTER TABLE domini ADD cod_connettore_send VARCHAR2(255 CHAR)';
EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE != -1430 THEN
            RAISE;
        END IF;
END;
/

-- Indici a supporto della cursor pagination delle console-api su GET /pendenze
-- e GET /ricevute: sort fisso (data DESC, id DESC) della query keyset. Senza
-- questi indici la paginazione degrada a scan sequenziale su tabelle grandi.
-- Dichiarati qui e non in govpay-console-api: impattano tabelle del core,
-- quindi per la regola di proprieta' delle patch stanno nel core e nel govpay.xsd.
-- Oracle non supporta IF NOT EXISTS su CREATE INDEX: blocco PL/SQL idempotente
-- che ignora ORA-00955 (nome gia' in uso).
BEGIN
    EXECUTE IMMEDIATE 'CREATE INDEX idx_versamenti_data_ult_agg_id ON versamenti (data_ora_ultimo_aggiornamento DESC, id DESC)';
EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE != -955 THEN
            RAISE;
        END IF;
END;
/

BEGIN
    EXECUTE IMMEDIATE 'CREATE INDEX idx_rpt_data_msg_ricevuta_id ON rpt (data_msg_ricevuta DESC, id DESC)';
EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE != -955 THEN
            RAISE;
        END IF;
END;
/

-- Issue #881 del cruscotto (govpay-console-api#80): preferenze d'uso dell'operatore,
-- JSON opaco che interpreta il solo frontend. Dichiarata qui e non in
-- govpay-console-api perche' operatori e' una tabella del core.
-- Il tipo e' quello che il generatore usa per un xsd:string senza facet, che
-- cambia da dialetto a dialetto: TEXT non esiste su oracle e sqlserver.
-- Oracle non supporta IF NOT EXISTS su ADD: blocco PL/SQL idempotente che
-- ignora ORA-01430 (colonna gia' presente).
BEGIN
    EXECUTE IMMEDIATE 'ALTER TABLE operatori ADD preferenze CLOB';
EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE != -1430 THEN
            RAISE;
        END IF;
END;
/


-- Sequence rimaste orfane dalla rimozione dei pagamenti portale: la patch 3.9.2
-- elimina le tabelle pagamenti_portale e pag_port_versamenti ma non le loro
-- sequence, mentre il gov_pay.sql rigenerato non le dichiara piu'. Un'installazione
-- aggiornata se le portava quindi dietro, divergendo da una installata da zero.
-- I drop sono idempotenti perche' su un'installazione nuova quegli oggetti non
-- esistono e la patch deve restare applicabile in entrambi i casi.
-- ORA-02289 e' "sequence does not exist".
BEGIN
    EXECUTE IMMEDIATE 'DROP SEQUENCE seq_pagamenti_portale';
EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE != -2289 THEN
            RAISE;
        END IF;
END;
/

BEGIN
    EXECUTE IMMEDIATE 'DROP SEQUENCE seq_pag_port_versamenti';
EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE != -2289 THEN
            RAISE;
        END IF;
END;
/

COMMIT;
