drop table canali;
drop sequence seq_canali;
drop trigger trg_canali;
drop table psp;
drop sequence seq_psp;
drop trigger trg_psp;
ALTER TABLE tributi ADD id_iban_accredito_postale NUMBER;
ALTER TABLE tributi ADD CONSTRAINT fk_trb_id_iban_accredito_postale FOREIGN KEY (id_iban_accredito_postale) REFERENCES iban_accredito(id);

update tributi set id_iban_accredito_postale = id_iban_accredito;
update tributi set id_iban_accredito=null where id_iban_accredito in (select id from iban_accredito where postale = true);
update tributi set id_iban_accredito_postale=null where id_iban_accredito in (select id from iban_accredito where postale = false);
