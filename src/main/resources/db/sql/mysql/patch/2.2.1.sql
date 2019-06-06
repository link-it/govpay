--GP-341
ALTER TABLE pagamenti ADD COLUMN iban_accredito VARCHAR(255);
--GP-351
ALTER TABLE rpt ADD COLUMN cod_sessione_portale VARCHAR(255);
--GP-169
ALTER TABLE versamenti DROP FOREIGN KEY fk_versamenti_1;
ALTER TABLE versamenti DROP FOREIGN KEY fk_versamenti_2;
ALTER TABLE singoli_versamenti DROP FOREIGN KEY fk_singoli_versamenti_1;
ALTER TABLE singoli_versamenti DROP FOREIGN KEY fk_singoli_versamenti_2;
ALTER TABLE singoli_versamenti DROP FOREIGN KEY fk_singoli_versamenti_3;
ALTER TABLE rpt DROP FOREIGN KEY fk_rpt_1;
ALTER TABLE rpt DROP FOREIGN KEY fk_rpt_2;
ALTER TABLE rpt DROP FOREIGN KEY fk_rpt_3;
ALTER TABLE notifiche DROP FOREIGN KEY fk_notifiche_1;
ALTER TABLE notifiche DROP FOREIGN KEY fk_notifiche_2;
ALTER TABLE notifiche DROP FOREIGN KEY fk_notifiche_3;
ALTER TABLE iuv DROP FOREIGN KEY fk_iuv_1;
ALTER TABLE iuv DROP FOREIGN KEY fk_iuv_2;

CREATE INDEX index_iuv_2 ON iuv (cod_versamento_ente,tipo_iuv,id_applicazione);

