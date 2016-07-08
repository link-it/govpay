ALTER TABLE versamenti ADD COLUMN cod_lotto VARCHAR(35);
ALTER TABLE versamenti ADD COLUMN cod_versamento_lotto VARCHAR(35);
ALTER TABLE versamenti ADD COLUMN cod_anno_tributario VARCHAR(35);
ALTER TABLE pagamenti ADD COLUMN data_acquisizione TIMESTAMP NOT NULL DEFAULT current_timestamp;
ALTER TABLE pagamenti ADD COLUMN data_acquisizione_revoca TIMESTAMP;
--GP-288 Aggiungere colonna Note e BundleKey
ALTER TABLE versamenti ADD COLUMN cod_bundlekey VARCHAR(256);
ALTER TABLE singoli_versamenti ADD COLUMN note VARCHAR(512);
