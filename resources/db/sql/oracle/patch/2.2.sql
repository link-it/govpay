ALTER TABLE versamenti ADD cod_lotto VARCHAR(35);
ALTER TABLE versamenti ADD cod_versamento_lotto VARCHAR(35);
ALTER TABLE versamenti ADD cod_anno_tributario VARCHAR(35);
ALTER TABLE pagamenti ADD data_acquisizione TIMESTAMP NOT NULL default CURRENT_TIMESTAMP;
ALTER TABLE pagamenti ADD data_acquisizione_revoca TIMESTAMP;
--GP-288 Aggiungere colonna Note e BundleKey
ALTER TABLE versamenti ADD cod_bundlekey VARCHAR(256);
ALTER TABLE singoli_versamenti ADD note VARCHAR(512);
