drop table canali;
drop table psp;

-- GP-588
ALTER TABLE tributi ADD COLUMN id_iban_accredito_postale BIGINT;
ALTER TABLE tributi ADD CONSTRAINT fk_trb_id_iban_accredito_postale FOREIGN KEY (id_iban_accredito_postale) REFERENCES iban_accredito(id);
