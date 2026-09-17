-- 3.10.0

-- Rimozione connettore FTP dall'intermediario: eliminazione dei connettori
-- FTP censiti nella tabella connettori e drop della colonna cod_connettore_ftp
-- dalla tabella intermediari.
-- La DELETE va eseguita solo se la colonna c'e' ancora: riapplicando la patch a
-- un'installazione gia' alla 3.10 la colonna e' stata eliminata poco sotto, e la
-- sottoquery fallirebbe. Lo statement e' dinamico perche' altrimenti il
-- riferimento alla colonna assente sarebbe un errore di compilazione, che la
-- guardia non eviterebbe.
SET @gp_sql := IF(
    (SELECT COUNT(*) FROM information_schema.columns
      WHERE table_schema = DATABASE()
        AND table_name = 'intermediari'
        AND column_name = 'cod_connettore_ftp') > 0,
    'DELETE FROM connettori WHERE cod_connettore IN (SELECT cod_connettore_ftp FROM (SELECT cod_connettore_ftp FROM intermediari WHERE cod_connettore_ftp IS NOT NULL) t)',
    'DO 0');
PREPARE gp_stmt FROM @gp_sql;
EXECUTE gp_stmt;
DEALLOCATE PREPARE gp_stmt;

-- DROP COLUMN IF EXISTS e' un'estensione MariaDB: su MySQL e' un errore di
-- sintassi, quindi qui la patch si fermava senza nemmeno arrivare al resto.
-- Stessa guardia delle altre: condizione su information_schema e statement
-- preparato.
SET @gp_sql := IF(
    (SELECT COUNT(*) FROM information_schema.columns
      WHERE table_schema = DATABASE()
        AND table_name = 'intermediari'
        AND column_name = 'cod_connettore_ftp') > 0,
    'ALTER TABLE intermediari DROP COLUMN cod_connettore_ftp',
    'DO 0');
PREPARE gp_stmt FROM @gp_sql;
EXECUTE gp_stmt;
DEALLOCATE PREPARE gp_stmt;

-- Tracciamento dell'IP del richiedente sull'audit trail.
-- MySQL non supporta IF NOT EXISTS sulle colonne ne' un costrutto condizionale
-- negli script: la guardia passa da SET con IF e da uno statement preparato,
-- come per la DELETE qui sopra. 'DO 0' e' il ramo a vuoto.
SET @gp_sql := IF(
    (SELECT COUNT(*) FROM information_schema.columns
      WHERE table_schema = DATABASE()
        AND table_name = 'gp_audit'
        AND column_name = 'ip_richiedente') = 0,
    'ALTER TABLE gp_audit ADD COLUMN ip_richiedente VARCHAR(45) COMMENT ''Indirizzo IP del richiedente della modifica''',
    'DO 0');
PREPARE gp_stmt FROM @gp_sql;
EXECUTE gp_stmt;
DEALLOCATE PREPARE gp_stmt;

-- Integrazione a SEND: attualizzazione dell'importo della pendenza
-- con le spese di notifica sostenute tramite SEND.
SET @gp_sql := IF(
    (SELECT COUNT(*) FROM information_schema.columns
      WHERE table_schema = DATABASE()
        AND table_name = 'versamenti'
        AND column_name = 'send_abilitato') = 0,
    'ALTER TABLE versamenti ADD COLUMN send_abilitato BOOLEAN NOT NULL DEFAULT false',
    'DO 0');
PREPARE gp_stmt FROM @gp_sql;
EXECUTE gp_stmt;
DEALLOCATE PREPARE gp_stmt;

SET @gp_sql := IF(
    (SELECT COUNT(*) FROM information_schema.columns
      WHERE table_schema = DATABASE()
        AND table_name = 'versamenti'
        AND column_name = 'send_importo_totale') = 0,
    'ALTER TABLE versamenti ADD COLUMN send_importo_totale DOUBLE',
    'DO 0');
PREPARE gp_stmt FROM @gp_sql;
EXECUTE gp_stmt;
DEALLOCATE PREPARE gp_stmt;

SET @gp_sql := IF(
    (SELECT COUNT(*) FROM information_schema.columns
      WHERE table_schema = DATABASE()
        AND table_name = 'versamenti'
        AND column_name = 'send_data_aggiornamento') = 0,
    'ALTER TABLE versamenti ADD COLUMN send_data_aggiornamento DATETIME',
    'DO 0');
PREPARE gp_stmt FROM @gp_sql;
EXECUTE gp_stmt;
DEALLOCATE PREPARE gp_stmt;

SET @gp_sql := IF(
    (SELECT COUNT(*) FROM information_schema.columns
      WHERE table_schema = DATABASE()
        AND table_name = 'domini'
        AND column_name = 'cod_connettore_send') = 0,
    'ALTER TABLE domini ADD COLUMN cod_connettore_send VARCHAR(255)',
    'DO 0');
PREPARE gp_stmt FROM @gp_sql;
EXECUTE gp_stmt;
DEALLOCATE PREPARE gp_stmt;

-- Indici a supporto della cursor pagination delle console-api su GET /pendenze
-- e GET /ricevute: sort fisso (data DESC, id DESC) della query keyset. Senza
-- questi indici la paginazione degrada a scan sequenziale su tabelle grandi.
-- Dichiarati qui e non in govpay-console-api: impattano tabelle del core,
-- quindi per la regola di proprieta' delle patch stanno nel core e nel govpay.xsd.
-- MySQL non supporta IF NOT EXISTS su CREATE INDEX: stessa guardia, su
-- information_schema.statistics.
SET @gp_sql := IF(
    (SELECT COUNT(*) FROM information_schema.statistics
      WHERE table_schema = DATABASE()
        AND table_name = 'versamenti'
        AND index_name = 'idx_versamenti_data_ult_agg_id') = 0,
    'CREATE INDEX idx_versamenti_data_ult_agg_id ON versamenti (data_ora_ultimo_aggiornamento DESC, id DESC)',
    'DO 0');
PREPARE gp_stmt FROM @gp_sql;
EXECUTE gp_stmt;
DEALLOCATE PREPARE gp_stmt;

SET @gp_sql := IF(
    (SELECT COUNT(*) FROM information_schema.statistics
      WHERE table_schema = DATABASE()
        AND table_name = 'rpt'
        AND index_name = 'idx_rpt_data_msg_ricevuta_id') = 0,
    'CREATE INDEX idx_rpt_data_msg_ricevuta_id ON rpt (data_msg_ricevuta DESC, id DESC)',
    'DO 0');
PREPARE gp_stmt FROM @gp_sql;
EXECUTE gp_stmt;
DEALLOCATE PREPARE gp_stmt;

-- Issue #881 del cruscotto (govpay-console-api#80): preferenze d'uso dell'operatore,
-- JSON opaco che interpreta il solo frontend. Dichiarata qui e non in
-- govpay-console-api perche' operatori e' una tabella del core.
-- Il tipo e' quello che il generatore usa per un xsd:string senza facet, che
-- cambia da dialetto a dialetto: TEXT non esiste su oracle e sqlserver.
SET @gp_sql := IF(
    (SELECT COUNT(*) FROM information_schema.columns
      WHERE table_schema = DATABASE()
        AND table_name = 'operatori'
        AND column_name = 'preferenze') = 0,
    'ALTER TABLE operatori ADD COLUMN preferenze LONGTEXT COMMENT ''Preferenze di uso del cruscotto, JSON opaco interpretato dal solo frontend''',
    'DO 0');
PREPARE gp_stmt FROM @gp_sql;
EXECUTE gp_stmt;
DEALLOCATE PREPARE gp_stmt;
