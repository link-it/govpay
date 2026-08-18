-- 3.10.0

-- Rimozione connettore FTP dall'intermediario: eliminazione dei connettori
-- FTP censiti nella tabella connettori e drop della colonna cod_connettore_ftp
-- dalla tabella intermediari.
DELETE FROM connettori WHERE cod_connettore IN (
    SELECT cod_connettore_ftp FROM intermediari WHERE cod_connettore_ftp IS NOT NULL
);

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
ALTER TABLE gp_audit ADD (ip_richiedente VARCHAR2(45 CHAR));

-- Integrazione a SEND: attualizzazione dell'importo della pendenza
-- con le spese di notifica sostenute tramite SEND.
ALTER TABLE versamenti ADD send_abilitato NUMBER DEFAULT 0 NOT NULL;
ALTER TABLE versamenti ADD send_importo_totale BINARY_DOUBLE;
ALTER TABLE versamenti ADD send_data_aggiornamento TIMESTAMP;

ALTER TABLE domini ADD cod_connettore_send VARCHAR2(255 CHAR);

COMMIT;
