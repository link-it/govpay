-- 3.10.0

-- Rimozione connettore FTP dall'intermediario: eliminazione dei connettori
-- FTP censiti nella tabella connettori e drop della colonna cod_connettore_ftp
-- dalla tabella intermediari.
DELETE FROM connettori WHERE cod_connettore IN (
    SELECT cod_connettore_ftp FROM intermediari WHERE cod_connettore_ftp IS NOT NULL
);

ALTER TABLE intermediari DROP COLUMN IF EXISTS cod_connettore_ftp;

-- Tracciamento dell'IP del richiedente sull'audit trail.
ALTER TABLE gp_audit ADD COLUMN ip_richiedente VARCHAR(45);

-- Integrazione a SEND: attualizzazione dell'importo della pendenza
-- con le spese di notifica sostenute tramite SEND.
ALTER TABLE versamenti ADD COLUMN send_abilitato BOOLEAN NOT NULL DEFAULT false;
ALTER TABLE versamenti ADD COLUMN send_importo_totale DOUBLE PRECISION;
ALTER TABLE versamenti ADD COLUMN send_data_aggiornamento TIMESTAMP;

ALTER TABLE domini ADD COLUMN cod_connettore_send VARCHAR(255);
