-- 3.9.4

-- 06/08/2026 Integrazione a SEND: attualizzazione dell'importo della pendenza
-- con le spese di notifica sostenute tramite SEND.

ALTER TABLE versamenti ADD send_abilitato NUMBER DEFAULT 0 NOT NULL;
ALTER TABLE versamenti ADD send_importo_totale BINARY_DOUBLE;
ALTER TABLE versamenti ADD send_data_aggiornamento TIMESTAMP;

ALTER TABLE domini ADD cod_connettore_send VARCHAR2(255 CHAR);
