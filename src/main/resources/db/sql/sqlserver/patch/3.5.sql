-- 27/04/2021 Aggiunta colonna contabilita alla tabella singoli versamenti
ALTER TABLE singoli_versamenti ADD contabilita VARCHAR(max);


-- 11/05/2021 Aggiunto identificativo tracciato notifica pagamenti
ALTER TABLE trac_notif_pag ADD identificativo VARCHAR(255);
UPDATE trac_notif_pag SET identificativo = nome_file;
ALTER TABLE trac_notif_pag ALTER COLUMN identificativo VARCHAR(255) NOT NULL;


-- 17/05/2021 Indice sulla colonna iuv della tabella eventi
CREATE INDEX idx_evt_iuv ON eventi (iuv);


-- 20/05/2021 Aggiunta colonna connettore hyper_sic_apk alla tabella domini
ALTER TABLE domini ADD cod_connettore_hyper_sic_apk VARCHAR(255);


