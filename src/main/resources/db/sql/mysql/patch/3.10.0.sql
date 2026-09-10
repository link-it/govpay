-- 3.10.0

-- Rimozione connettore FTP dall'intermediario: eliminazione dei connettori
-- FTP censiti nella tabella connettori e drop della colonna cod_connettore_ftp
-- dalla tabella intermediari.
DELETE FROM connettori WHERE cod_connettore IN (
    SELECT cod_connettore_ftp FROM (
        SELECT cod_connettore_ftp FROM intermediari WHERE cod_connettore_ftp IS NOT NULL
    ) t
);

ALTER TABLE intermediari DROP COLUMN IF EXISTS cod_connettore_ftp;

-- Tracciamento dell'IP del richiedente sull'audit trail.
ALTER TABLE gp_audit ADD COLUMN ip_richiedente VARCHAR(45) COMMENT 'Indirizzo IP del richiedente della modifica';

-- Integrazione a SEND: attualizzazione dell'importo della pendenza
-- con le spese di notifica sostenute tramite SEND.
ALTER TABLE versamenti ADD COLUMN send_abilitato BOOLEAN NOT NULL DEFAULT false;
ALTER TABLE versamenti ADD COLUMN send_importo_totale DOUBLE;
ALTER TABLE versamenti ADD COLUMN send_data_aggiornamento DATETIME;

ALTER TABLE domini ADD COLUMN cod_connettore_send VARCHAR(255);

-- Indici a supporto della cursor pagination delle console-api su GET /pendenze
-- e GET /ricevute: sort fisso (data DESC, id DESC) della query keyset. Senza
-- questi indici la paginazione degrada a scan sequenziale su tabelle grandi.
-- Dichiarati qui e non in govpay-console-api: impattano tabelle del core,
-- quindi per la regola di proprieta' delle patch stanno nel core e nel govpay.xsd.
-- MySQL non supporta IF NOT EXISTS su CREATE INDEX: l'istruzione fallisce se
-- l'indice esiste gia'.
CREATE INDEX idx_versamenti_data_ult_agg_id
    ON versamenti (data_ora_ultimo_aggiornamento DESC, id DESC);

CREATE INDEX idx_rpt_data_msg_ricevuta_id
    ON rpt (data_msg_ricevuta DESC, id DESC);
