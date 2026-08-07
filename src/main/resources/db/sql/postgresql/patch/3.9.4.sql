-- 3.9.4

-- 06/08/2026 Integrazione a SEND: attualizzazione dell'importo della pendenza
-- con le spese di notifica sostenute tramite SEND.

ALTER TABLE versamenti ADD COLUMN send_abilitato BOOLEAN NOT NULL DEFAULT false;
ALTER TABLE versamenti ADD COLUMN send_importo_totale DOUBLE PRECISION;
ALTER TABLE versamenti ADD COLUMN send_data_aggiornamento TIMESTAMP;

ALTER TABLE domini ADD COLUMN cod_connettore_send VARCHAR(255);
