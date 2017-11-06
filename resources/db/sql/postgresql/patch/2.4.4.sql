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
       -- check constraints
       CONSTRAINT chk_avvisi_1 CHECK (stato IN ('DA_STAMPARE','STAMPATO')),
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
alter table tracciati add CONSTRAINT chk_tracciati_1 CHECK (stato IN ('ANNULLATO','NUOVO','IN_CARICAMENTO','CARICAMENTO_OK','CARICAMENTO_KO','STAMPATO'));

alter table tracciati add COLUMN tipo_tracciato VARCHAR(255);
alter table tracciati add CONSTRAINT chk_tracciati_2 CHECK (tipo_tracciato IN ('VERSAMENTI','INCASSI'));
update tracciati set tipo_tracciato = 'VERSAMENTI';
alter table tracciati ALTER COLUMN tipo_tracciato SET NOT NULL;

alter table operazioni add COLUMN cod_dominio VARCHAR(35);
alter table operazioni add COLUMN iuv VARCHAR(35);
alter table operazioni add COLUMN trn VARCHAR(35);


alter table operazioni DROP CONSTRAINT chk_operazioni_1;
alter table operazioni add CONSTRAINT chk_operazioni_1 CHECK (tipo_operazione IN ('ADD','DEL','INC','N_V'));
alter table operazioni add CONSTRAINT chk_operazioni_2 CHECK (stato IN ('NON_VALIDO','ESEGUITO_OK','ESEGUITO_KO'));
