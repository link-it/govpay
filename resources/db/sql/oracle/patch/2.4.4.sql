--GP-557

CREATE SEQUENCE seq_avvisi MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE avvisi
(
       cod_dominio VARCHAR(35) NOT NULL,
       iuv VARCHAR(35) NOT NULL,
       data_creazione TIMESTAMP NOT NULL,
       stato VARCHAR(255) NOT NULL,
       pdf BLOB NOT NULL,
       -- fk/pk columns
       id NUMBER NOT NULL,
       -- check constraints
       CONSTRAINT chk_avvisi_1 CHECK (stato IN ('DA_STAMPARE','STAMPATO')),
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

