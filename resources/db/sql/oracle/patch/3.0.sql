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
alter table incassi add id_operatore NUMBER;
alter table incassi add CONSTRAINT fk_inc_id_operatore FOREIGN KEY (id_operatore) REFERENCES operatori(id);
CREATE SEQUENCE seq_pagamenti_portale MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE pagamenti_portale
(
       cod_portale VARCHAR2(35 CHAR) NOT NULL,
       cod_canale VARCHAR2(35 CHAR),
       nome VARCHAR2(255 CHAR) NOT NULL,
       importo BINARY_DOUBLE NOT NULL,
       versante_identificativo VARCHAR2(35 CHAR),
       id_sessione VARCHAR2(35 CHAR) NOT NULL,
       id_sessione_portale VARCHAR2(35 CHAR),
       id_sessione_psp VARCHAR2(35 CHAR),
       stato VARCHAR2(35 CHAR) NOT NULL,
       codice_stato VARCHAR2(35 CHAR) NOT NULL,
       descrizione_stato VARCHAR2(1024 CHAR),
       psp_redirect_url VARCHAR2(1024 CHAR),
       psp_esito VARCHAR2(255 CHAR),
       json_request CLOB,
       wisp_id_dominio VARCHAR2(255 CHAR),
       wisp_key_pa VARCHAR2(255 CHAR),
       wisp_key_wisp VARCHAR2(255 CHAR),
       wisp_html CLOB,
       data_richiesta TIMESTAMP,
       url_ritorno VARCHAR2(1024 CHAR) NOT NULL,
       cod_psp VARCHAR2(35 CHAR),
       tipo_versamento VARCHAR2(4 CHAR),
       multi_beneficiario VARCHAR2(35 CHAR),
       -- fk/pk columns
       id NUMBER NOT NULL,
       -- unique constraints
       CONSTRAINT unique_pagamenti_portale_1 UNIQUE (id_sessione),
       -- fk/pk keys constraints
       CONSTRAINT pk_pagamenti_portale PRIMARY KEY (id)
);

CREATE TRIGGER trg_pagamenti_portale
BEFORE
insert on pagamenti_portale
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_pagamenti_portale.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;
/



CREATE SEQUENCE seq_pag_port_versamenti MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE pag_port_versamenti
(
       -- fk/pk columns
       id NUMBER NOT NULL,
       id_pagamento_portale NUMBER NOT NULL,
       id_versamento NUMBER NOT NULL,
       -- fk/pk keys constraints
       CONSTRAINT fk_ppv_id_pagamento_portale FOREIGN KEY (id_pagamento_portale) REFERENCES pagamenti_portale(id),
       CONSTRAINT fk_ppv_id_versamento FOREIGN KEY (id_versamento) REFERENCES versamenti(id),
       CONSTRAINT pk_pag_port_versamenti PRIMARY KEY (id)
);

CREATE TRIGGER trg_pag_port_versamenti
BEFORE
insert on pag_port_versamenti
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_pag_port_versamenti.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;
/




ALTER TABLE rpt ADD id_pagamento_portale NUMBER;
ALTER TABLE rpt ADD CONSTRAINT fk_rpt_id_pagamento_portale FOREIGN KEY (id_pagamento_portale) REFERENCES pagamenti_portale(id);

ALTER TABLE versamenti ADD data_validita TIMESTAMP(3);
ALTER TABLE versamenti ADD nome VARCHAR(35);
--TODO not null
ALTER TABLE versamenti ADD tassonomia_avviso VARCHAR(35);
--TODO not null
ALTER TABLE versamenti ADD tassonomia VARCHAR(35);
ALTER TABLE versamenti ADD id_dominio BIGINT;
update versamenti set id_dominio = (select id_dominio from uo where id = versamenti.id_uo);
ALTER TABLE versamenti MODIFY (id_dominio NOT NULL);
ALTER TABLE versamenti MODIFY (id_uo NULL);
ALTER TABLE versamenti ADD CONSTRAINT fk_vrs_id_dominio FOREIGN KEY (id_dominio) REFERENCES domini(id);
ALTER TABLE versamenti ADD debitore_tipo VARCHAR2(1 CHAR);

ALTER TABLE acl DROP CONSTRAINT fk_acl_id_portale;
ALTER TABLE acl DROP COLUMN id_portale;

ALTER TABLE rpt DROP CONSTRAINT fk_rpt_id_portale;
ALTER TABLE rpt DROP COLUMN id_portale;

ALTER TABLE rpt ADD id_applicazione BIGINT;
ALTER TABLE rpt ADD CONSTRAINT fk_rpt_id_applicazione FOREIGN KEY (id_applicazione) REFERENCES applicazioni(id);

DROP TABLE portali;
DROP SEQUENCE portali_seq;

ALTER TABLE applicazioni ADD reg_exp VARCHAR2(1024 CHAR);

ALTER TABLE domini DROP COLUMN xml_conti_accredito;
ALTER TABLE domini DROP COLUMN xml_tabella_controparti;


DROP TABLE acl;
CREATE TABLE acl
(
	ruolo VARCHAR2(255 CHAR),
	principal VARCHAR2(255 CHAR),
	servizio VARCHAR2(255 CHAR) NOT NULL,
	diritti VARCHAR2(255 CHAR) NOT NULL,
	-- fk/pk columns
	id NUMBER NOT NULL,
	-- fk/pk keys constraints
	CONSTRAINT pk_acl PRIMARY KEY (id)
);

DROP TABLE ruoli;
DROP SEQUENCE seq_ruoli;
DROP TRIGGER trg_ruoli;

CREATE SEQUENCE seq_utenze MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE utenze
(
	principal VARCHAR2(255 CHAR) NOT NULL,
	abilitato NUMBER NOT NULL,
	-- fk/pk columns
	id NUMBER NOT NULL,
	-- unique constraints
	CONSTRAINT unique_utenze_1 UNIQUE (principal),
	-- fk/pk keys constraints
	CONSTRAINT pk_utenze PRIMARY KEY (id)
);

ALTER TABLE utenze MODIFY abilitato DEFAULT 1;

CREATE TRIGGER trg_utenze
BEFORE
insert on utenze
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_utenze.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;
/



CREATE SEQUENCE seq_utenze_domini MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE utenze_domini
(
	-- fk/pk columns
	id NUMBER NOT NULL,
	id_utenza NUMBER NOT NULL,
	id_dominio NUMBER NOT NULL,
	-- fk/pk keys constraints
	CONSTRAINT fk_nzd_id_utenza FOREIGN KEY (id_utenza) REFERENCES utenze(id),
	CONSTRAINT fk_nzd_id_dominio FOREIGN KEY (id_dominio) REFERENCES domini(id),
	CONSTRAINT pk_utenze_domini PRIMARY KEY (id)
);

CREATE TRIGGER trg_utenze_domini
BEFORE
insert on utenze_domini
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_utenze_domini.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;
/



CREATE SEQUENCE seq_utenze_tributi MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE utenze_tributi
(
	-- fk/pk columns
	id NUMBER NOT NULL,
	id_utenza NUMBER NOT NULL,
	id_tributo NUMBER NOT NULL,
	-- fk/pk keys constraints
	CONSTRAINT fk_nzt_id_utenza FOREIGN KEY (id_utenza) REFERENCES utenze(id),
	CONSTRAINT fk_nzt_id_tributo FOREIGN KEY (id_tributo) REFERENCES tributi(id),
	CONSTRAINT pk_utenze_tributi PRIMARY KEY (id)
);

CREATE TRIGGER trg_utenze_tributi
BEFORE
insert on utenze_tributi
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_utenze_tributi.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;
/



insert into utenze (principal) select distinct principal from ((select principal from operatori) union (select principal from applicazioni) )as s;


ALTER TABLE applicazioni ADD id_utenza NUMBER;
UPDATE applicazioni set id_utenza = (select id from utenze where principal = applicazioni.principal);
ALTER TABLE applicazioni MODIFY (id_utenza NOT NULL);
ALTER TABLE applicazioni DROP COLUMN principal;
ALTER TABLE applicazioni DROP COLUMN abilitato;

ALTER TABLE operatori ADD id_utenza NUMBER;
UPDATE operatori set id_utenza = (select id from utenze where principal = operatori.principal);
ALTER TABLE operatori MODIFY (id_utenza NOT NULL);
ALTER TABLE operatori DROP COLUMN principal;
ALTER TABLE operatori DROP COLUMN profilo;
ALTER TABLE operatori DROP COLUMN abilitato;

ALTER TABLE applicazioni ADD auto_iuv BOOLEAN;
UPDATE applicazioni SET auto_iuv = true;
ALTER TABLE applicazioni MODIFY (auto_iuv NOT NULL);

ALTER TABLE domini DROP COLUMN custom_iuv;
ALTER TABLE domini DROP COLUMN iuv_prefix_strict;
ALTER TABLE domini DROP COLUMN riuso_iuv;

ALTER TABLE iban_accredito DROP COLUMN id_seller_bank;
ALTER TABLE iban_accredito DROP COLUMN id_negozio;
ALTER TABLE applicazioni DROP COLUMN versione;

ALTER TABLE versamenti add dati_allegati CLOB;
ALTER TABLE singoli_versamenti add dati_allegati CLOB;
ALTER TABLE singoli_versamenti add descrizione VARCHAR2(256 CHAR);
ALTER TABLE singoli_versamenti drop column note;

ALTER TABLE iban_accredito DROP COLUMN iban_appoggio;
ALTER TABLE iban_accredito DROP COLUMN bic_appoggio;

ALTER TABLE tributi RENAME COLUMN id_iban_accredito_postale TO id_iban_appoggio;

ALTER TABLE singoli_versamenti add id_iban_appoggio NUMBER;
ALTER TABLE singoli_versamenti add CONSTRAINT fk_sng_id_iban_appoggio FOREIGN KEY (id_iban_appoggio) REFERENCES iban_accredito(id);

ALTER TABLE incassi add iban_accredito VARCHAR2(35 CHAR);
ALTER TABLE incassi ADD CONSTRAINT unique_incassi_1 UNIQUE (cod_dominio,trn);

ALTER TABLE pagamenti ADD tipo VARCHAR2(35 CHAR);
update pagamenti set tipo = 'ENTRATA' where iban_accredito is not null;
update pagamenti set tipo = 'MBT' where iban_accredito is null;
ALTER TABLE pagamenti DROP COLUMN iban_accredito;
ALTER TABLE pagamenti MODIFY (tipo NOT NULL);

ALTER TABLE versamenti ADD incasso VARCHAR2(1 CHAR);
ALTER TABLE versamenti ADD anomalie CLOB;
ALTER TABLE versamenti ADD iuv_versamento VARCHAR2(35 CHAR);
ALTER TABLE versamenti ADD numero_avviso VARCHAR2(35 CHAR);
ALTER TABLE versamenti ADD avvisatura VARCHAR2(1 CHAR);
ALTER TABLE versamenti ADD tipo_pagamento NUMBER;

ALTER TABLE utenze MODIFY principal VARCHAR2(4000 CHAR);
ALTER TABLE utenze ADD principal_originale VARCHAR2(4000 CHAR);
update utenze set principal_originale = principal;
ALTER TABLE utenze MODIFY (principal_originale NOT NULL);

-- patch dati pagamenti_portale malformati non gestiti dal cruscotto

delete from pag_port_versamenti where id_pagamento_portale in (select pagamenti_portale.id from  pagamenti_portale left join rpt on rpt.id_pagamento_portale = pagamenti_portale.id where rpt.id is null);
delete from pagamenti_portale where id in (select pagamenti_portale.id from  pagamenti_portale left join rpt on rpt.id_pagamento_portale = pagamenti_portale.id where rpt.id is null);

-- fine patch dati
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
	id_operatore NUMBER,
	-- fk/pk keys constraints
	CONSTRAINT fk_trc_id_operatore FOREIGN KEY (id_operatore) REFERENCES operatori(id),
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

CREATE SEQUENCE seq_operazioni MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE operazioni
(
        tipo_operazione VARCHAR2(16 CHAR) NOT NULL,
        linea_elaborazione NUMBER NOT NULL,
        stato VARCHAR2(16 CHAR) NOT NULL,
        dati_richiesta BLOB NOT NULL,
        dati_risposta BLOB,
        dettaglio_esito VARCHAR2(255 CHAR),
        cod_versamento_ente VARCHAR2(255 CHAR),
        cod_dominio VARCHAR2(35 CHAR),
        iuv VARCHAR2(35 CHAR),
        trn VARCHAR2(35 CHAR),
        -- fk/pk columns
        id NUMBER NOT NULL,
        id_tracciato NUMBER NOT NULL,
        id_applicazione NUMBER,
        -- fk/pk keys constraints
        CONSTRAINT fk_ope_id_tracciato FOREIGN KEY (id_tracciato) REFERENCES tracciati(id),
        CONSTRAINT fk_ope_id_applicazione FOREIGN KEY (id_applicazione) REFERENCES applicazioni(id),
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

insert into sonde(nome, classe, soglia_warn, soglia_error) values ('caricamento-tracciati', 'org.openspcoop2.utils.sonde.impl.SondaBatch', 3600000, 21600000);
insert into sonde(nome, classe, soglia_warn, soglia_error) values ('check-tracciati', 'org.openspcoop2.utils.sonde.impl.SondaCoda', 1, 1);

ALTER TABLE versamenti ADD ack NUMBER DEFAULT 0;
ALTER TABLE versamenti MODIFY (ack NOT NULL);
ALTER TABLE versamenti ADD note CLOB;
ALTER TABLE versamenti ADD anomalo NUMBER DEFAULT 0;
ALTER TABLE versamenti MODIFY (anomalo NOT NULL);

alter table singoli_versamenti add column indice_dati NUMBER;
update singoli_versamenti sv set indice_dati = (select sb1.indice_dati from (select sv1.id as id , sv1.id_versamento as id_versamento, row_number() over (partition by sv1.id_versamento) as indice_dati from singoli_versamenti sv1) as sb1 where sb1.id = sv.id);
alter table singoli_versamenti MODIFY (indice_dati NOT NULL);

alter table singoli_versamenti drop constraint unique_singoli_versamenti_1;
alter table singoli_versamenti add CONSTRAINT unique_singoli_versamenti_1 UNIQUE (id_versamento,cod_singolo_versamento_ente,indice_dati);

alter table rendicontazioni add column id_singolo_versamento NUMBER;
alter table rendicontazioni add CONSTRAINT fk_rnd_id_singolo_versamento FOREIGN KEY (id_singolo_versamento) REFERENCES singoli_versamenti(id);
-- aggiorno entries prendendo l'id singolo_versamento dal pagamento
update rendicontazioni set id_singolo_versamento = (select p.id_singolo_versamento from (select r1.id as id, pagamenti.id_singolo_versamento as id_singolo_versamento from pagamenti , rendicontazioni r1 where r1.id_pagamento = pagamenti.id) as p where p.id = rendicontazioni.id) ;

-- Sezione Viste

CREATE VIEW versamenti_incassi AS SELECT
versamenti.id as id,
MAX(versamenti.cod_versamento_ente) as cod_versamento_ente,          
MAX(versamenti.nome) as nome,                         
MAX(versamenti.importo_totale) as importo_totale,               
MAX(versamenti.stato_versamento) as stato_versamento,             
MAX(versamenti.descrizione_stato) as descrizione_stato,           
MAX(CASE WHEN versamenti.aggiornabile = 1 THEN 'TRUE' ELSE 'FALSE' END) AS aggiornabile,
MAX(versamenti.data_creazione) as data_creazione,               
MAX(versamenti.data_validita) as data_validita,                
MAX(versamenti.data_scadenza) as data_scadenza,                
MAX(versamenti.data_ora_ultimo_aggiornamento) as data_ora_ultimo_aggiornamento,
MAX(versamenti.causale_versamento) as causale_versamento,           
MAX(versamenti.debitore_tipo) as debitore_tipo,                
MAX(versamenti.debitore_identificativo) as debitore_identificativo,      
MAX(versamenti.debitore_anagrafica) as debitore_anagrafica,          
MAX(versamenti.debitore_indirizzo) as debitore_indirizzo,           
MAX(versamenti.debitore_civico) as debitore_civico,              
MAX(versamenti.debitore_cap) as debitore_cap,                 
MAX(versamenti.debitore_localita) as debitore_localita,            
MAX(versamenti.debitore_provincia) as debitore_provincia,           
MAX(versamenti.debitore_nazione) as debitore_nazione,             
MAX(versamenti.debitore_email) as debitore_email,               
MAX(versamenti.debitore_telefono) as debitore_telefono,            
MAX(versamenti.debitore_cellulare) as debitore_cellulare,           
MAX(versamenti.debitore_fax) as debitore_fax,                 
MAX(versamenti.tassonomia_avviso) as tassonomia_avviso,            
MAX(versamenti.tassonomia) as tassonomia,                   
MAX(versamenti.cod_lotto) as cod_lotto,                    
MAX(versamenti.cod_versamento_lotto) as cod_versamento_lotto,         
MAX(versamenti.cod_anno_tributario) as cod_anno_tributario,          
MAX(versamenti.cod_bundlekey) as cod_bundlekey,                
MAX(versamenti.dati_allegati) as dati_allegati,                
MAX(versamenti.incasso) as incasso,                      
MAX(versamenti.anomalie) as anomalie,                     
MAX(versamenti.iuv_versamento) as iuv_versamento,               
MAX(versamenti.numero_avviso) as numero_avviso,                
MAX(versamenti.avvisatura) as avvisatura,                   
MAX(versamenti.tipo_pagamento) as tipo_pagamento,               
MAX(versamenti.id_dominio) as id_dominio,                   
MAX(versamenti.id_uo) as id_uo,                        
MAX(versamenti.id_applicazione) as id_applicazione,             
MAX(CASE WHEN versamenti.da_avvisare = 1 THEN 'TRUE' ELSE 'FALSE' END) AS da_avvisare,
MAX(versamenti.cod_avvisatura) as cod_avvisatura,               
MAX(versamenti.id_tracciato) as id_tracciato,      
MAX(CASE WHEN versamenti.ack = 1 THEN 'TRUE' ELSE 'FALSE' END) AS ack,
MAX(versamenti.note) as note,
MAX(CASE WHEN versamenti.anomalo = 1 THEN 'TRUE' ELSE 'FALSE' END) AS anomalo,
MAX(pagamenti.data_pagamento) as data_pagamento,            
SUM(CASE WHEN pagamenti.importo_pagato IS NOT NULL THEN pagamenti.importo_pagato ELSE 0 END) AS importo_pagato,
SUM(CASE WHEN pagamenti.stato = 'INCASSATO' THEN pagamenti.importo_pagato ELSE 0 END) AS importo_incassato,
MAX(CASE WHEN pagamenti.stato IS NULL THEN 'NON_PAGATO' WHEN pagamenti.stato = 'INCASSATO' THEN 'INCASSATO' ELSE 'PAGATO' END) AS stato_pagamento,
MAX(pagamenti.iuv) AS iuv_pagamento,
MAX(0) AS smart_order_rank,
MIN(0) AS smart_order_date
FROM versamenti LEFT JOIN singoli_versamenti ON versamenti.id = singoli_versamenti.id_versamento LEFT join pagamenti on singoli_versamenti.id = pagamenti.id_singolo_versamento
WHERE versamenti.numero_avviso IS NOT NULL OR pagamenti.importo_pagato > 0
GROUP BY versamenti.id;

-- FIX bug che non valorizzava il tipo debitore
update versamenti set debitore_tipo = 'F';
