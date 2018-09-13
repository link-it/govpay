drop table canali;
drop table psp;

ALTER TABLE singoli_versamenti ADD COLUMN id_iban_appoggio BIGINT;
ALTER TABLE singoli_versamenti ADD CONSTRAINT fk_sng_id_iban_appoggio FOREIGN KEY (id_iban_appoggio) REFERENCES iban_accredito(id);

