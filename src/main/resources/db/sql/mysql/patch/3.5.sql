-- 11/05/2021 Aggiunto identificativo tracciato notifica pagamenti
ALTER TABLE trac_notif_pag ADD COLUMN identificativo VARCHAR(255);
UPDATE trac_notif_pag SET identificativo = nome_file;
ALTER TABLE trac_notif_pag MODIFY COLUMN identificativo VARCHAR(255) NOT NULL;
