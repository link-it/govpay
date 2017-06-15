--GP-523

CREATE SEQUENCE seq_tracciati MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE tracciati
(
       data_caricamento TIMESTAMP NOT NULL,
       data_ultimo_aggiornamento TIMESTAMP NOT NULL,
       stato VARCHAR2(255 CHAR) NOT NULL,
       linea_elaborazione NUMBER NOT NULL,
       descrizione_stato VARCHAR2(1024) CHAR,
       num_linee_totali NUMBER NOT NULL,
       num_operazioni_ok NUMBER NOT NULL,
       num_operazioni_ko NUMBER NOT NULL,
       nome_file VARCHAR2(255 CHAR) NOT NULL,
       raw_data_richiesta BLOB NOT NULL,
       raw_data_risposta BLOB,
       -- fk/pk columns
       id NUMBER NOT NULL,
       id_operatore NUMBER NOT NULL,
       -- check constraints
       CONSTRAINT chk_tracciati_1 CHECK (stato IN ('ANNULLATO','NUOVO','IN_CARICAMENTO','CARICAMENTO_OK','CARICAMENTO_KO')),
       -- fk/pk keys constraints
       CONSTRAINT fk_tracciati_1 FOREIGN KEY (id_operatore) REFERENCES operatori(id),
       CONSTRAINT pk_tracciati PRIMARY KEY (id)
);

CREATE TRIGGER trg_tracciati
BEFORE
insert on tracciati
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_tracciati.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;
/



CREATE SEQUENCE seq_operazioni MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE operazioni
(
       tipo_operazione VARCHAR2(255 CHAR) NOT NULL,
       linea_elaborazione NUMBER NOT NULL,
       stato VARCHAR2(255 CHAR) NOT NULL,
       dati_richiesta BLOB NOT NULL,
       dati_risposta BLOB,
       dettaglio_esito VARCHAR2(255 CHAR),
       cod_versamento_ente VARCHAR2(255 CHAR),
       -- fk/pk columns
       id NUMBER NOT NULL,
       id_tracciato NUMBER NOT NULL,
       id_applicazione NUMBER,
       -- check constraints
       CONSTRAINT chk_operazioni_1 CHECK (stato IN ('NON_VALIDO','ESEGUITO_OK','ESEGUITO_KO')),
       -- fk/pk keys constraints
       CONSTRAINT fk_operazioni_1 FOREIGN KEY (id_tracciato) REFERENCES tracciati(id),
       CONSTRAINT fk_operazioni_2 FOREIGN KEY (id_applicazione) REFERENCES applicazioni(id),
       CONSTRAINT pk_operazioni PRIMARY KEY (id)
);

CREATE TRIGGER trg_operazioni
BEFORE
insert on operazioni
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_operazioni.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;
/


--GP-524
CREATE SEQUENCE seq_audit MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE audit
(
       data TIMESTAMP NOT NULL,
       id_oggetto NUMBER NOT NULL,
       tipo_oggetto VARCHAR2(255 CHAR) NOT NULL,
       oggetto CLOB NOT NULL,
       -- fk/pk columns
       id NUMBER NOT NULL,
       id_operatore NUMBER NOT NULL,
       -- fk/pk keys constraints
       CONSTRAINT fk_audit_1 FOREIGN KEY (id_operatore) REFERENCES operatori(id),
       CONSTRAINT pk_audit PRIMARY KEY (id)
);

CREATE TRIGGER trg_audit
BEFORE
insert on audit
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_audit.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;
/

--GP-526

CREATE SEQUENCE seq_ruoli MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE ruoli
(
       cod_ruolo VARCHAR2(35 CHAR) NOT NULL,
       descrizione VARCHAR2(255 CHAR) NOT NULL,
       -- fk/pk columns
       id NUMBER NOT NULL,
       -- unique constraints
       CONSTRAINT unique_ruoli_1 UNIQUE (cod_ruolo),
       -- fk/pk keys constraints
       CONSTRAINT pk_ruoli PRIMARY KEY (id)
);

CREATE TRIGGER trg_ruoli
BEFORE
insert on ruoli
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_ruoli.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;
/

ALTER TABLE acl ADD diritti NUMBER;
ALTER TABLE acl MODIFY cod_servizio VARCHAR2(35 CHAR) NOT NULL;
