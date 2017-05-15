--GP-513
CREATE SEQUENCE seq_batch MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE batch
(
       cod_batch VARCHAR2(255 CHAR) NOT NULL,
       nodo NUMBER,
       inizio TIMESTAMP,
       aggiornamento TIMESTAMP,
       -- fk/pk columns
       id NUMBER NOT NULL,
       -- unique constraints
       CONSTRAINT unique_batch_1 UNIQUE (cod_batch),
       -- fk/pk keys constraints
       CONSTRAINT pk_batch PRIMARY KEY (id)
);

CREATE TRIGGER trg_batch
BEFORE
insert on batch
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_batch.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;

--DEV-238
CREATE SEQUENCE seq_incassi MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE incassi
(
       trn VARCHAR2(35 CHAR) NOT NULL,
       cod_dominio VARCHAR2(35 CHAR) NOT NULL,
       causale VARCHAR2(512 CHAR) NOT NULL,
       importo BINARY_DOUBLE NOT NULL,
       cod_applicazione VARCHAR2(35 CHAR) NOT NULL,
       data_valuta DATE,
       data_contabile DATE,
       data_ora_incasso TIMESTAMP NOT NULL,
       nome_dispositivo VARCHAR2(512 CHAR),
       -- fk/pk columns
       id NUMBER NOT NULL,
       -- fk/pk keys constraints
       CONSTRAINT pk_incassi PRIMARY KEY (id)
);

CREATE TRIGGER trg_incassi
BEFORE
insert on incassi
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_incassi.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;
/

ALTER TABLE pagamenti ADD stato VARCHAR2(35 CHAR);
ALTER TABLE pagamenti ADD id_incasso NUMBER;
ALTER TABLE pagamenti ADD CONSTRAINT fk_pagamenti_4 FOREIGN KEY (id_incasso) REFERENCES incassi(id) ON DELETE CASCADE;
