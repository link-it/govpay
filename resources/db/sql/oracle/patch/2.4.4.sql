--GP-557

CREATE SEQUENCE seq_avvisi MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE avvisi
(
       cod_dominio VARCHAR2(35 CHAR) NOT NULL,
       iuv VARCHAR(35) NOT NULL,
       data_creazione TIMESTAMP NOT NULL,
       stato VARCHAR2(255 CHAR) NOT NULL,
       pdf BLOB,
       -- fk/pk columns
       id NUMBER NOT NULL,
       -- fk/pk keys constraints
       CONSTRAINT pk_avvisi PRIMARY KEY (id)
);

-- index
CREATE INDEX index_avvisi_1 ON avvisi (cod_dominio,iuv);
CREATE TRIGGER trg_avvisi
BEFORE
insert on avvisi
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_avvisi.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;
/

insert into sonde(nome, classe, soglia_warn, soglia_error) values ('generazione-avvisi', 'org.openspcoop2.utils.sonde.impl.SondaBatch', 3600000, 21600000);

ALTER TABLE domini ADD cbill VARCHAR2(255 CHAR);
ALTER TABLE uo ADD uo_area VARCHAR2(255 CHAR);
ALTER TABLE uo ADD uo_url_sito_web VARCHAR2(255 CHAR);
ALTER TABLE uo ADD uo_email VARCHAR2(255 CHAR);
ALTER TABLE uo ADD uo_pec VARCHAR2(255 CHAR);

alter table tracciati DROP CONSTRAINT chk_tracciati_1;

alter table tracciati add tipo_tracciato VARCHAR(255);
update tracciati set tipo_tracciato = 'VERSAMENTI';
alter table tracciati MODIFY (tipo_tracciato NOT NULL);

alter table operazioni add cod_dominio VARCHAR(35);
alter table operazioni add iuv VARCHAR(35);
alter table operazioni add trn VARCHAR(35);


alter table operazioni DROP CONSTRAINT chk_operazioni_1;

alter table incassi add id_operatore NUMBER;
alter table incassi add CONSTRAINT fk_inc_id_operatore FOREIGN KEY (id_operatore) REFERENCES operatori(id);
