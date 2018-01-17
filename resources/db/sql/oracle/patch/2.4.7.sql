-- GP-588
ALTER TABLE tributi ADD id_iban_accredito_alternativo NUMBER;
ALTER TABLE tributi ADD CONSTRAINT fk_trb_id_iban_accredito_alternativo FOREIGN KEY (id_iban_accredito_alternativo) REFERENCES iban_accredito(id);
