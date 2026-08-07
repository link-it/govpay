-- 3.9.4

-- 06/08/2026 Integrazione a SEND: attualizzazione dell'importo della pendenza
-- con le spese di notifica sostenute tramite SEND.

ALTER TABLE versamenti ADD send_abilitato BIT NOT NULL DEFAULT 0;
ALTER TABLE versamenti ADD send_importo_totale DECIMAL(15,2);
ALTER TABLE versamenti ADD send_data_aggiornamento DATETIME2;

ALTER TABLE domini ADD cod_connettore_send VARCHAR(255);
