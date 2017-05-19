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
