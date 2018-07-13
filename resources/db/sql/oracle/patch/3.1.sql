ALTER TABLE versamenti MODIFY (tassonomia_avviso NULL);

ALTER TABLE pagamenti_portale ADD ack NUMBER DEFAULT 0;
ALTER TABLE pagamenti_portale MODIFY (ack NOT NULL);

ALTER TABLE pagamenti_portale ADD note CLOB;
ALTER TABLE pagamenti_portale ADD tipo NUMBER;
update pagamenti_portale set tipo = 1;

ALTER TABLE pagamenti_portale MODIFY (tipo NOT NULL);

ALTER TABLE pagamenti_portale MODIFY (url_ritorno NULL);



ALTER TABLE intermediari ADD cod_connettore_ftp VARCHAR2(35 CHAR);

CREATE SEQUENCE seq_tracciati MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE tracciati
(
	cod_dominio VARCHAR2(35 CHAR) NOT NULL,
        tipo VARCHAR2(10 CHAR) NOT NULL,
        stato VARCHAR2(12 CHAR) NOT NULL,
        descrizione_stato VARCHAR2(256 CHAR),
        data_caricamento TIMESTAMP NOT NULL,
        data_completamento TIMESTAMP NOT NULL,
        bean_dati CLOB,
        file_name_richiesta VARCHAR2(256 CHAR),
        raw_richiesta BLOB NOT NULL,
        file_name_esito VARCHAR2(256 CHAR),
        raw_esito BLOB NOT NULL,
        -- fk/pk columns
        id NUMBER NOT NULL,
        -- fk/pk keys constraints
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


ALTER TABLE versamenti ADD da_avvisare NUMBER NOT NULL DEFAULT 0;
ALTER TABLE versamenti ADD cod_avvisatura VARCHAR2(20 CHAR);
ALTER TABLE versamenti ADD id_tracciato NUMBER;
ALTER TABLE versamenti ADD CONSTRAINT fk_vrs_id_tracciato FOREIGN KEY (id_tracciato) REFERENCES tracciati(id);


CREATE SEQUENCE seq_esiti_avvisatura MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE esiti_avvisatura
(
        cod_dominio VARCHAR2(35 CHAR) NOT NULL,
        identificativo_avvisatura VARCHAR2(20 CHAR) NOT NULL,
        tipo_canale NUMBER NOT NULL,
        cod_canale VARCHAR2(35 CHAR),
        data TIMESTAMP NOT NULL,
        cod_esito NUMBER NOT NULL,
        descrizione_esito VARCHAR2(140 CHAR) NOT NULL,
        -- fk/pk columns
        id NUMBER NOT NULL,
        id_tracciato NUMBER NOT NULL,
        -- fk/pk keys constraints
        CONSTRAINT fk_sta_id_tracciato FOREIGN KEY (id_tracciato) REFERENCES tracciati(id),
        CONSTRAINT pk_esiti_avvisatura PRIMARY KEY (id)
);

CREATE TRIGGER trg_esiti_avvisatura
BEFORE
insert on esiti_avvisatura
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_esiti_avvisatura.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;
/

insert into sonde(nome, classe, soglia_warn, soglia_error) values ('avvisatura-digitale', 'org.openspcoop2.utils.sonde.impl.SondaBatch', 3600000, 21600000);
insert into sonde(nome, classe, soglia_warn, soglia_error) values ('esito-avvisatura-digitale', 'org.openspcoop2.utils.sonde.impl.SondaBatch', 3600000, 21600000);

