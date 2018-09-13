drop table canali;
drop sequence seq_canali;
drop trigger trg_canali;
drop table psp;
drop sequence seq_psp;
drop trigger trg_psp;

ALTER TABLE singoli_versamenti ADD id_iban_appoggio NUMBER;
ALTER TABLE singoli_versamenti ADD CONSTRAINT fk_sng_id_iban_appoggio FOREIGN KEY (id_iban_appoggio) REFERENCES iban_accredito(id);

