-- 27/04/2021 Aggiunta colonna contabilita alla tabella singoli versamenti
ALTER TABLE singoli_versamenti ADD COLUMN contabilita TEXT;


-- 11/05/2021 Aggiunto identificativo tracciato notifica pagamenti
ALTER TABLE trac_notif_pag ADD COLUMN identificativo VARCHAR(255);
UPDATE trac_notif_pag SET identificativo = nome_file;
ALTER TABLE trac_notif_pag ALTER COLUMN identificativo SET NOT NULL;


-- 17/05/2021 Indice sulla colonna iuv della tabella eventi
CREATE INDEX idx_evt_iuv ON eventi (iuv);

