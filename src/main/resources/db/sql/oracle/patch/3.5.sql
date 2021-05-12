-- 11/05/2021 Aggiunto identificativo tracciato notifica pagamenti
ALTER TABLE trac_notif_pag ADD identificativo VARCHAR2(255 CHAR);
UPDATE trac_notif_pag SET identificativo = nome_file;
ALTER TABLE trac_notif_pag MODIFY (identificativo NOT NULL);
