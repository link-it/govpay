--GP-557
CREATE SEQUENCE seq_avvisi start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE avvisi
(
       cod_dominio VARCHAR(35) NOT NULL,
       iuv VARCHAR(35) NOT NULL,
       data_creazione TIMESTAMP NOT NULL,
       stato VARCHAR(255) NOT NULL,
       pdf BYTEA,
       -- fk/pk columns
       id BIGINT DEFAULT nextval('seq_avvisi') NOT NULL,
       -- fk/pk keys constraints
       CONSTRAINT pk_avvisi PRIMARY KEY (id)
);

-- index
CREATE INDEX index_avvisi_1 ON avvisi (cod_dominio,iuv);

insert into sonde(nome, classe, soglia_warn, soglia_error) values ('generazione-avvisi', 'org.openspcoop2.utils.sonde.impl.SondaBatch', 3600000, 21600000);

ALTER TABLE domini ADD COLUMN cbill VARCHAR(255);
ALTER TABLE uo ADD COLUMN uo_area VARCHAR(255);
ALTER TABLE uo ADD COLUMN uo_url_sito_web VARCHAR(255);
ALTER TABLE uo ADD COLUMN uo_email VARCHAR(255);
ALTER TABLE uo ADD COLUMN uo_pec VARCHAR(255);

alter table tracciati DROP CONSTRAINT chk_tracciati_1;

alter table tracciati add COLUMN tipo_tracciato VARCHAR(255);
update tracciati set tipo_tracciato = 'VERSAMENTI';
alter table tracciati ALTER COLUMN tipo_tracciato SET NOT NULL;

alter table operazioni add COLUMN cod_dominio VARCHAR(35);
alter table operazioni add COLUMN iuv VARCHAR(35);
alter table operazioni add COLUMN trn VARCHAR(35);


alter table operazioni DROP CONSTRAINT chk_operazioni_1;

alter table incassi add COLUMN id_operatore BIGINT;
alter table incassi add CONSTRAINT fk_inc_id_operatore FOREIGN KEY (id_operatore) REFERENCES operatori(id);

-- abilitazione al caricamento incassi per il ruolo amministratore
UPDATE acl set amministratore = TRUE where cod_servizio = 'G_RND' and id_ruolo in (select id from ruoli where cod_ruolo ='Amministratore');
-- GP-588
ALTER TABLE tributi ADD COLUMN id_iban_accredito_postale BIGINT;
ALTER TABLE tributi ADD CONSTRAINT fk_trb_id_iban_accredito_postale FOREIGN KEY (id_iban_accredito_postale) REFERENCES iban_accredito(id);

update tributi set id_iban_accredito_postale = id_iban_accredito;
update tributi set id_iban_accredito=null where id_iban_accredito in (select id from iban_accredito where postale = true);
update tributi set id_iban_accredito_postale=null where id_iban_accredito in (select id from iban_accredito where postale = false);
