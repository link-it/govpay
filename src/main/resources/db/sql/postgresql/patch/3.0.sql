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

alter table incassi add COLUMN id_operatore BIGINT;
alter table incassi add CONSTRAINT fk_inc_id_operatore FOREIGN KEY (id_operatore) REFERENCES operatori(id);





CREATE SEQUENCE seq_pagamenti_portale start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE pagamenti_portale
(
       cod_applicazione VARCHAR(35) NOT NULL,
       cod_canale VARCHAR(35),
       nome VARCHAR(255) NOT NULL,
       importo DOUBLE PRECISION NOT NULL,
       versante_identificativo VARCHAR(35),
       id_sessione VARCHAR(35) NOT NULL,
       id_sessione_portale VARCHAR(35),
       id_sessione_psp VARCHAR(35),
       stato VARCHAR(35) NOT NULL,
       codice_stato VARCHAR(35) NOT NULL,
       descrizione_stato VARCHAR(1024),
       psp_redirect_url VARCHAR(1024),
       psp_esito VARCHAR(255),
       json_request TEXT,
       wisp_id_dominio VARCHAR(255),
       wisp_key_pa VARCHAR(255),
       wisp_key_wisp VARCHAR(255),
       wisp_html TEXT,
       data_richiesta TIMESTAMP,
       url_ritorno VARCHAR(1024) NOT NULL,
       cod_psp VARCHAR(35),
       tipo_versamento VARCHAR(4),
       multi_beneficiario VARCHAR(35),
       -- fk/pk columns
       id BIGINT DEFAULT nextval('seq_pagamenti_portale') NOT NULL,
       -- unique constraints
       CONSTRAINT unique_pagamenti_portale_1 UNIQUE (id_sessione),
       -- fk/pk keys constraints
       CONSTRAINT pk_pagamenti_portale PRIMARY KEY (id)
);




CREATE SEQUENCE seq_pag_port_versamenti start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE pag_port_versamenti
(
       -- fk/pk columns
       id BIGINT DEFAULT nextval('seq_pag_port_versamenti') NOT NULL,
       id_pagamento_portale BIGINT NOT NULL,
       id_versamento BIGINT NOT NULL,
       -- fk/pk keys constraints
       CONSTRAINT fk_ppv_id_pagamento_portale FOREIGN KEY (id_pagamento_portale) REFERENCES pagamenti_portale(id),
       CONSTRAINT fk_ppv_id_versamento FOREIGN KEY (id_versamento) REFERENCES versamenti(id),
       CONSTRAINT pk_pag_port_versamenti PRIMARY KEY (id)
);

ALTER TABLE rpt ADD COLUMN id_pagamento_portale BIGINT;
ALTER TABLE rpt ADD CONSTRAINT fk_rpt_id_pagamento_portale FOREIGN KEY (id_pagamento_portale) REFERENCES pagamenti_portale(id);

ALTER TABLE versamenti ADD COLUMN data_validita TIMESTAMP(3);
ALTER TABLE versamenti ADD COLUMN nome VARCHAR(35);
--TODO not null
ALTER TABLE versamenti ADD COLUMN tassonomia_avviso VARCHAR(35);
--TODO not null
ALTER TABLE versamenti ADD COLUMN tassonomia VARCHAR(35);
ALTER TABLE versamenti ADD COLUMN id_dominio BIGINT;
update versamenti set id_dominio = (select id_dominio from uo where id = versamenti.id_uo);
ALTER TABLE versamenti ALTER COLUMN id_dominio SET NOT NULL;
ALTER TABLE versamenti ALTER COLUMN id_uo DROP NOT NULL;
ALTER TABLE versamenti ADD CONSTRAINT fk_vrs_id_dominio FOREIGN KEY (id_dominio) REFERENCES domini(id);
ALTER TABLE versamenti ADD COLUMN debitore_tipo VARCHAR(1);


ALTER TABLE acl DROP CONSTRAINT fk_acl_id_portale;
ALTER TABLE acl DROP COLUMN id_portale;

ALTER TABLE rpt DROP CONSTRAINT fk_rpt_id_portale;
ALTER TABLE rpt DROP COLUMN id_portale;

ALTER TABLE rpt ADD COLUMN id_applicazione BIGINT;
ALTER TABLE rpt ADD CONSTRAINT fk_rpt_id_applicazione FOREIGN KEY (id_applicazione) REFERENCES applicazioni(id);

DROP TABLE portali;
DROP SEQUENCE portali_seq;

ALTER TABLE applicazioni ADD COLUMN reg_exp VARCHAR(1024);

ALTER TABLE domini DROP COLUMN xml_conti_accredito;
ALTER TABLE domini DROP COLUMN xml_tabella_controparti;

DROP TABLE acl;
CREATE TABLE acl
(
	ruolo VARCHAR(255),
	principal VARCHAR(255),
	servizio VARCHAR(255) NOT NULL,
	diritti VARCHAR(255) NOT NULL,
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_acl') NOT NULL,
	-- fk/pk keys constraints
	CONSTRAINT pk_acl PRIMARY KEY (id)
);

DROP TABLE ruoli;
DROP SEQUENCE seq_ruoli;

CREATE SEQUENCE seq_utenze start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE utenze
(
	principal VARCHAR(255) NOT NULL,
	abilitato BOOLEAN NOT NULL DEFAULT true,
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_utenze') NOT NULL,
	-- unique constraints
	CONSTRAINT unique_utenze_1 UNIQUE (principal),
	-- fk/pk keys constraints
	CONSTRAINT pk_utenze PRIMARY KEY (id)
);


CREATE SEQUENCE seq_utenze_domini start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE utenze_domini
(
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_utenze_domini') NOT NULL,
	id_utenza BIGINT NOT NULL,
	id_dominio BIGINT NOT NULL,
	-- fk/pk keys constraints
	CONSTRAINT fk_nzd_id_utenza FOREIGN KEY (id_utenza) REFERENCES utenze(id),
	CONSTRAINT fk_nzd_id_dominio FOREIGN KEY (id_dominio) REFERENCES domini(id),
	CONSTRAINT pk_utenze_domini PRIMARY KEY (id)
);




CREATE SEQUENCE seq_utenze_tributi start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE utenze_tributi
(
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_utenze_tributi') NOT NULL,
	id_utenza BIGINT NOT NULL,
	id_tributo BIGINT NOT NULL,
	-- fk/pk keys constraints
	CONSTRAINT fk_nzt_id_utenza FOREIGN KEY (id_utenza) REFERENCES utenze(id),
	CONSTRAINT fk_nzt_id_tributo FOREIGN KEY (id_tributo) REFERENCES tributi(id),
	CONSTRAINT pk_utenze_tributi PRIMARY KEY (id)
);


insert into utenze (principal) select distinct principal from ((select principal from operatori) union (select principal from applicazioni) )as s;


ALTER TABLE applicazioni ADD COLUMN id_utenza BIGINT;
UPDATE applicazioni set id_utenza = (select id from utenze where principal = applicazioni.principal);
ALTER TABLE applicazioni ALTER COLUMN id_utenza SET NOT NULL;
ALTER TABLE applicazioni DROP COLUMN principal;
ALTER TABLE applicazioni DROP COLUMN abilitato;


ALTER TABLE operatori ADD id_utenza BIGINT;
UPDATE operatori set id_utenza = (select id from utenze where principal = operatori.principal);
ALTER TABLE operatori ALTER COLUMN id_utenza SET NOT NULL;
ALTER TABLE operatori DROP COLUMN principal;
ALTER TABLE operatori DROP COLUMN profilo;
ALTER TABLE operatori DROP COLUMN abilitato;

ALTER TABLE applicazioni ADD COLUMN auto_iuv BOOLEAN;
UPDATE applicazioni SET auto_iuv = true;
ALTER TABLE applicazioni ALTER COLUMN auto_iuv SET NOT NULL;

ALTER TABLE domini DROP COLUMN custom_iuv;
ALTER TABLE domini DROP COLUMN iuv_prefix_strict;
ALTER TABLE domini DROP COLUMN riuso_iuv;

ALTER TABLE iban_accredito DROP COLUMN id_seller_bank;
ALTER TABLE iban_accredito DROP COLUMN id_negozio;
ALTER TABLE applicazioni DROP COLUMN versione;

ALTER TABLE versamenti add COLUMN dati_allegati TEXT;
ALTER TABLE singoli_versamenti add COLUMN dati_allegati TEXT;
ALTER TABLE singoli_versamenti add COLUMN descrizione VARCHAR(256);
ALTER TABLE singoli_versamenti drop column note;

ALTER TABLE iban_accredito DROP COLUMN iban_appoggio;
ALTER TABLE iban_accredito DROP COLUMN bic_appoggio;

ALTER TABLE tributi RENAME COLUMN id_iban_accredito_postale TO id_iban_appoggio;

ALTER TABLE singoli_versamenti add column id_iban_appoggio BIGINT;
ALTER TABLE singoli_versamenti add CONSTRAINT fk_sng_id_iban_appoggio FOREIGN KEY (id_iban_appoggio) REFERENCES iban_accredito(id);

ALTER TABLE incassi add column iban_accredito VARCHAR(35);
ALTER TABLE incassi ADD CONSTRAINT unique_incassi_1 UNIQUE (cod_dominio,trn);

ALTER TABLE pagamenti ADD COLUMN tipo VARCHAR(35);
update pagamenti set tipo = 'ENTRATA' where iban_accredito is not null;
update pagamenti set tipo = 'MBT' where iban_accredito is null;
ALTER TABLE pagamenti DROP COLUMN iban_accredito;
ALTER TABLE pagamenti ALTER COLUMN tipo SET NOT NULL;

ALTER TABLE versamenti ADD COLUMN incasso VARCHAR(1);
ALTER TABLE versamenti ADD COLUMN anomalie TEXT;

ALTER TABLE versamenti ADD COLUMN iuv_versamento VARCHAR(35);
ALTER TABLE versamenti ADD COLUMN numero_avviso VARCHAR(35);
ALTER TABLE versamenti ADD COLUMN avvisatura VARCHAR(1);
ALTER TABLE versamenti ADD COLUMN tipo_pagamento INT;

ALTER TABLE utenze ALTER COLUMN principal type VARCHAR(4000);
ALTER TABLE utenze ADD COLUMN principal_originale VARCHAR(4000);
update utenze set principal_originale = principal;
ALTER TABLE utenze ALTER COLUMN principal_originale SET NOT NULL;

-- patch dati pagamenti_portale malformati non gestiti dal cruscotto

delete from pag_port_versamenti where id_pagamento_portale in (select pagamenti_portale.id from  pagamenti_portale left join rpt on rpt.id_pagamento_portale = pagamenti_portale.id where rpt.id is null);
delete from pagamenti_portale where id in (select pagamenti_portale.id from  pagamenti_portale left join rpt on rpt.id_pagamento_portale = pagamenti_portale.id where rpt.id is null);

-- fine patch dati


ALTER TABLE versamenti ALTER COLUMN tassonomia_avviso DROP NOT NULL;

ALTER TABLE pagamenti_portale ADD COLUMN ack BOOLEAN NOT NULL DEFAULT FALSE;
ALTER TABLE pagamenti_portale ADD COLUMN note TEXT;

ALTER TABLE pagamenti_portale ADD COLUMN tipo INT;
UPDATE pagamenti_portale set tipo=1;
ALTER TABLE pagamenti_portale ALTER COLUMN tipo SET NOT NULL;

ALTER TABLE pagamenti_portale ALTER COLUMN url_ritorno DROP NOT NULL;

DROP TABLE operazioni;
DROP SEQUENCE seq_operazioni;
DROP TABLE tracciati;  
DROP SEQUENCE seq_tracciati;
CREATE SEQUENCE seq_tracciati start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE tracciati
(
	cod_dominio VARCHAR(35) NOT NULL,
	tipo VARCHAR(10) NOT NULL,
	stato VARCHAR(12) NOT NULL,
	descrizione_stato VARCHAR(256),
	data_caricamento TIMESTAMP NOT NULL,
	data_completamento TIMESTAMP NOT NULL,
	bean_dati TEXT,
	file_name_richiesta VARCHAR(256),
	raw_richiesta BYTEA,
	file_name_esito VARCHAR(256),
	raw_esito BYTEA,
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_tracciati') NOT NULL,
	id_operatore BIGINT,
	-- fk/pk keys constraints
	CONSTRAINT fk_trc_id_operatore FOREIGN KEY (id_operatore) REFERENCES operatori(id),
	CONSTRAINT pk_tracciati PRIMARY KEY (id)
);

ALTER TABLE intermediari ADD COLUMN cod_connettore_ftp VARCHAR(35);
ALTER TABLE versamenti ADD COLUMN da_avvisare BOOLEAN NOT NULL DEFAULT false;
ALTER TABLE versamenti ADD COLUMN cod_avvisatura VARCHAR(20);
ALTER TABLE versamenti ADD COLUMN id_tracciato BIGINT;
ALTER TABLE versamenti ADD CONSTRAINT fk_vrs_id_tracciato FOREIGN KEY (id_tracciato) REFERENCES tracciati(id);

CREATE SEQUENCE seq_esiti_avvisatura start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;
CREATE TABLE esiti_avvisatura
(
	cod_dominio VARCHAR(35) NOT NULL,
	identificativo_avvisatura VARCHAR(20) NOT NULL,
	tipo_canale INT NOT NULL,
	cod_canale VARCHAR(35),
	data TIMESTAMP NOT NULL,
	cod_esito INT NOT NULL,
	descrizione_esito VARCHAR(140) NOT NULL,
	id BIGINT DEFAULT nextval('seq_esiti_avvisatura') NOT NULL,
	id_tracciato BIGINT NOT NULL,
	CONSTRAINT fk_sta_id_tracciato FOREIGN KEY (id_tracciato) REFERENCES tracciati(id),
	CONSTRAINT pk_esiti_avvisatura PRIMARY KEY (id)
);

insert into sonde(nome, classe, soglia_warn, soglia_error) values ('avvisatura-digitale', 'org.openspcoop2.utils.sonde.impl.SondaBatch', 3600000, 21600000);
insert into sonde(nome, classe, soglia_warn, soglia_error) values ('esito-avvisatura-digitale', 'org.openspcoop2.utils.sonde.impl.SondaBatch', 3600000, 21600000);

CREATE SEQUENCE seq_operazioni start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE operazioni
(
        tipo_operazione VARCHAR(16) NOT NULL,
        linea_elaborazione BIGINT NOT NULL,
        stato VARCHAR(16) NOT NULL,
        dati_richiesta BYTEA NOT NULL,
        dati_risposta BYTEA,
        dettaglio_esito VARCHAR(255),
        cod_versamento_ente VARCHAR(255),
        cod_dominio VARCHAR(35),
        iuv VARCHAR(35),
        trn VARCHAR(35),
        -- fk/pk columns
        id BIGINT DEFAULT nextval('seq_operazioni') NOT NULL,
        id_tracciato BIGINT NOT NULL,
        id_applicazione BIGINT,
        -- fk/pk keys constraints
        CONSTRAINT fk_ope_id_tracciato FOREIGN KEY (id_tracciato) REFERENCES tracciati(id),
        CONSTRAINT fk_ope_id_applicazione FOREIGN KEY (id_applicazione) REFERENCES applicazioni(id),
        CONSTRAINT pk_operazioni PRIMARY KEY (id)
);

insert into sonde(nome, classe, soglia_warn, soglia_error) values ('caricamento-tracciati', 'org.openspcoop2.utils.sonde.impl.SondaBatch', 3600000, 21600000);
insert into sonde(nome, classe, soglia_warn, soglia_error) values ('check-tracciati', 'org.openspcoop2.utils.sonde.impl.SondaCoda', 1, 1);

alter table versamenti add column ack BOOLEAN;
update versamenti set ack = FALSE;
alter table versamenti alter column ack set NOT NULL;
alter table versamenti add column note TEXT; 
alter table versamenti add column anomalo BOOLEAN;
update versamenti set anomalo = FALSE;
alter table versamenti alter column anomalo set NOT NULL;

alter table singoli_versamenti add column indice_dati INT;
update singoli_versamenti sv set indice_dati = (select sb1.indice_dati from (select sv1.id as id , sv1.id_versamento as id_versamento, row_number() over (partition by sv1.id_versamento) as indice_dati from singoli_versamenti sv1) as sb1 where sb1.id = sv.id);
alter table singoli_versamenti alter column indice_dati set NOT NULL;

alter table singoli_versamenti drop constraint unique_singoli_versamenti_1;
alter table singoli_versamenti add CONSTRAINT unique_singoli_versamenti_1 UNIQUE (id_versamento,cod_singolo_versamento_ente,indice_dati);

alter table rendicontazioni add column id_singolo_versamento BIGINT;
alter table rendicontazioni add CONSTRAINT fk_rnd_id_singolo_versamento FOREIGN KEY (id_singolo_versamento) REFERENCES singoli_versamenti(id);
-- aggiorno entries prendendo l'id singolo_versamento dal pagamento
update rendicontazioni set id_singolo_versamento = (select p.id_singolo_versamento from (select r1.id as id, pagamenti.id_singolo_versamento as id_singolo_versamento from pagamenti , rendicontazioni r1 where r1.id_pagamento = pagamenti.id) as p where p.id = rendicontazioni.id) ;

-- Sezione Viste
CREATE VIEW versamenti_incassi AS
 SELECT versamenti.id,
    max(versamenti.cod_versamento_ente::text) AS cod_versamento_ente,
    max(versamenti.nome::text) AS nome,
    max(versamenti.importo_totale) AS importo_totale,
    versamenti.stato_versamento::text AS stato_versamento,
    max(versamenti.descrizione_stato::text) AS descrizione_stato,
    max(
        CASE
            WHEN versamenti.aggiornabile = true THEN 'TRUE'::text
            ELSE 'FALSE'::text
        END) AS aggiornabile,
    max(versamenti.data_creazione) AS data_creazione,
    max(versamenti.data_validita) AS data_validita,
    max(versamenti.data_scadenza) AS data_scadenza,
    max(versamenti.data_ora_ultimo_aggiornamento) AS data_ora_ultimo_aggiornamento,
    max(versamenti.causale_versamento::text) AS causale_versamento,
    max(versamenti.debitore_tipo::text) AS debitore_tipo,
    versamenti.debitore_identificativo,
    max(versamenti.debitore_anagrafica::text) AS debitore_anagrafica,
    max(versamenti.debitore_indirizzo::text) AS debitore_indirizzo,
    max(versamenti.debitore_civico::text) AS debitore_civico,
    max(versamenti.debitore_cap::text) AS debitore_cap,
    max(versamenti.debitore_localita::text) AS debitore_localita,
    max(versamenti.debitore_provincia::text) AS debitore_provincia,
    max(versamenti.debitore_nazione::text) AS debitore_nazione,
    max(versamenti.debitore_email::text) AS debitore_email,
    max(versamenti.debitore_telefono::text) AS debitore_telefono,
    max(versamenti.debitore_cellulare::text) AS debitore_cellulare,
    max(versamenti.debitore_fax::text) AS debitore_fax,
    max(versamenti.tassonomia_avviso::text) AS tassonomia_avviso,
    max(versamenti.tassonomia::text) AS tassonomia,
    max(versamenti.cod_lotto::text) AS cod_lotto,
    max(versamenti.cod_versamento_lotto::text) AS cod_versamento_lotto,
    max(versamenti.cod_anno_tributario::text) AS cod_anno_tributario,
    max(versamenti.cod_bundlekey::text) AS cod_bundlekey,
    max(versamenti.dati_allegati) AS dati_allegati,
    max(versamenti.incasso::text) AS incasso,
    max(versamenti.anomalie) AS anomalie,
    max(versamenti.iuv_versamento::text) AS iuv_versamento,
    max(versamenti.numero_avviso::text) AS numero_avviso,
    max(versamenti.avvisatura::text) AS avvisatura,
    max(versamenti.tipo_pagamento) AS tipo_pagamento,
    max(versamenti.id_dominio) AS id_dominio,
    max(versamenti.id_uo) AS id_uo,
    max(versamenti.id_applicazione) AS id_applicazione,
    max(
        CASE
            WHEN versamenti.da_avvisare = true THEN 'TRUE'::text
            ELSE 'FALSE'::text
        END) AS da_avvisare,
    max(versamenti.cod_avvisatura::text) AS cod_avvisatura,
    max(versamenti.id_tracciato) AS id_tracciato,
    max(
        CASE
            WHEN versamenti.ack = true THEN 'TRUE'::text
            ELSE 'FALSE'::text
        END) AS ack,
    max(versamenti.note) AS note,
    max(
        CASE
            WHEN versamenti.anomalo = true THEN 'TRUE'::text
            ELSE 'FALSE'::text
        END) AS anomalo,
    max(pagamenti.data_pagamento) AS data_pagamento,
    sum(
        CASE
            WHEN pagamenti.importo_pagato IS NOT NULL THEN pagamenti.importo_pagato
            ELSE 0::double precision
        END) AS importo_pagato,
    sum(
        CASE
            WHEN pagamenti.stato::text = 'INCASSATO'::text THEN pagamenti.importo_pagato
            ELSE 0::double precision
        END) AS importo_incassato,
    max(
        CASE
            WHEN pagamenti.stato IS NULL THEN 'NON_PAGATO'::text
            WHEN pagamenti.stato::text = 'INCASSATO'::text THEN 'INCASSATO'::text
            ELSE 'PAGATO'::text
        END) AS stato_pagamento,
    max(pagamenti.iuv::text) AS iuv_pagamento,
    max(
        CASE
            WHEN versamenti.stato_versamento::text = 'NON_ESEGUITO'::text AND versamenti.data_validita > now() THEN 0
            ELSE 1
        END) AS smart_order_rank,
    min(@ (date_part('epoch'::text, now()) * 1000::bigint - date_part('epoch'::text, COALESCE(pagamenti.data_pagamento, versamenti.data_validita, versamenti.data_creazione)) * 1000::bigint))::bigint AS smart_order_date
   FROM versamenti
     LEFT JOIN singoli_versamenti ON versamenti.id = singoli_versamenti.id_versamento
     LEFT JOIN pagamenti ON singoli_versamenti.id = pagamenti.id_singolo_versamento
  WHERE versamenti.numero_avviso IS NOT NULL OR pagamenti.importo_pagato > 0::double precision
  GROUP BY versamenti.id, versamenti.debitore_identificativo, versamenti.stato_versamento;



-- INDICI

-- eventi
create index idx_eventi_1 on eventi (data_1);
create index idx_eventi_2 on eventi (iuv);
create index idx_eventi_3 on eventi (cod_dominio);

-- versamenti
create index idx_versamenti_1 on versamenti (numero_avviso);
create index idx_versamenti_2 on versamenti (id_dominio);
create index idx_versamenti_3 on versamenti (stato_versamento);

-- pagamenti
create index idx_pagamenti_1 on pagamenti (id_singolo_versamento);

-- FIX bug che non valorizzava il tipo debitore
update versamenti set debitore_tipo = 'F';

alter table domini add column aut_stampa_poste VARCHAR(255);

ALTER TABLE uo ADD COLUMN uo_tel VARCHAR(255);
ALTER TABLE uo ADD COLUMN uo_fax VARCHAR(255);

-- 3.0.0-RC3

ALTER TABLE tipi_tributo ADD COLUMN on_line BOOLEAN NOT NULL DEFAULT false;
ALTER TABLE tipi_tributo ADD COLUMN paga_terzi BOOLEAN NOT NULL DEFAULT false;

ALTER TABLE tributi ADD COLUMN on_line BOOLEAN;
ALTER TABLE tributi ADD COLUMN paga_terzi BOOLEAN;

alter table pagamenti_portale add column principal VARCHAR(4000);
update pagamenti_portale pp set principal = (select u.principal from utenze u, applicazioni a where u.id = a.id_utenza and a.cod_applicazione = pp.cod_applicazione);
alter table pagamenti_portale alter column principal set not null;

alter table pagamenti_portale add column tipo_utenza VARCHAR(35);
update pagamenti_portale set tipo_utenza = 'APPLICAZIONE';
alter table pagamenti_portale alter column tipo_utenza set not null;

alter table pagamenti_portale drop column cod_applicazione;


-- VISTE REPORTISTICA

CREATE VIEW v_riscossioni_senza_rpt AS
SELECT fr.cod_dominio AS cod_dominio,
    rendicontazioni.iuv AS iuv,
    rendicontazioni.iur AS iur,
    fr.cod_flusso AS cod_flusso,
    fr.iur AS fr_iur,
    fr.data_regolamento AS data_regolamento,
    fr.importo_totale_pagamenti AS importo_totale_pagamenti,
    fr.numero_pagamenti AS numero_pagamenti,
    rendicontazioni.importo_pagato AS importo_pagato,
    rendicontazioni.data AS data,
    singoli_versamenti.cod_singolo_versamento_ente AS cod_singolo_versamento_ente,
    rendicontazioni.indice_dati AS indice_dati,
    versamenti.cod_versamento_ente AS cod_versamento_ente,
    versamenti.id_applicazione AS id_applicazione
   FROM fr
     JOIN rendicontazioni ON rendicontazioni.id_fr = fr.id
     JOIN versamenti ON versamenti.iuv_versamento = rendicontazioni.iuv
     JOIN domini ON versamenti.id_dominio = domini.id
     JOIN singoli_versamenti ON singoli_versamenti.id_versamento = versamenti.id
  WHERE rendicontazioni.esito = 9;

CREATE VIEW v_riscossioni_con_rpt AS
SELECT pagamenti.cod_dominio AS cod_dominio,
    pagamenti.iuv AS iuv,
    pagamenti.iur AS iur,
    fr.cod_flusso AS cod_flusso,
    fr.iur AS fr_iur,
    fr.data_regolamento AS data_regolamento,
    fr.importo_totale_pagamenti AS importo_totale_pagamenti,
    fr.numero_pagamenti AS numero_pagamenti,
    pagamenti.importo_pagato AS importo_pagato,
    pagamenti.data_pagamento AS data,
    singoli_versamenti.cod_singolo_versamento_ente AS cod_singolo_versamento_ente,
    singoli_versamenti.indice_dati AS indice_dati,
    versamenti.cod_versamento_ente AS cod_versamento_ente,
    versamenti.id_applicazione AS id_applicazione
   FROM pagamenti
     LEFT JOIN rendicontazioni ON rendicontazioni.id_pagamento = pagamenti.id
     LEFT JOIN fr ON rendicontazioni.id_fr = fr.id
     JOIN singoli_versamenti ON pagamenti.id_singolo_versamento = singoli_versamenti.id
     JOIN versamenti ON singoli_versamenti.id_versamento = versamenti.id; 

CREATE VIEW v_riscossioni AS
 SELECT a.cod_dominio,
    a.iuv,
    a.iur,
    a.cod_flusso,
    a.fr_iur,
    a.data_regolamento,
    a.importo_totale_pagamenti,
    a.numero_pagamenti,
    a.importo_pagato,
    a.data,
    a.cod_singolo_versamento_ente,
    a.indice_dati,
    a.cod_versamento_ente,
    applicazioni.cod_applicazione
   FROM ( SELECT v_riscossioni_senza_rpt.cod_dominio,
            v_riscossioni_senza_rpt.iuv,
            v_riscossioni_senza_rpt.iur,
            v_riscossioni_senza_rpt.cod_flusso,
            v_riscossioni_senza_rpt.fr_iur,
            v_riscossioni_senza_rpt.data_regolamento,
            v_riscossioni_senza_rpt.importo_totale_pagamenti,
            v_riscossioni_senza_rpt.numero_pagamenti,
            v_riscossioni_senza_rpt.importo_pagato,
            v_riscossioni_senza_rpt.data,
            v_riscossioni_senza_rpt.cod_singolo_versamento_ente,
            v_riscossioni_senza_rpt.indice_dati,
            v_riscossioni_senza_rpt.cod_versamento_ente,
            v_riscossioni_senza_rpt.id_applicazione
           FROM v_riscossioni_senza_rpt
        UNION
         SELECT v_riscossioni_con_rpt.cod_dominio,
            v_riscossioni_con_rpt.iuv,
            v_riscossioni_con_rpt.iur,
            v_riscossioni_con_rpt.cod_flusso,
            v_riscossioni_con_rpt.fr_iur,
            v_riscossioni_con_rpt.data_regolamento,
            v_riscossioni_con_rpt.importo_totale_pagamenti,
            v_riscossioni_con_rpt.numero_pagamenti,
            v_riscossioni_con_rpt.importo_pagato,
            v_riscossioni_con_rpt.data,
            v_riscossioni_con_rpt.cod_singolo_versamento_ente,
            v_riscossioni_con_rpt.indice_dati,
            v_riscossioni_con_rpt.cod_versamento_ente,
            v_riscossioni_con_rpt.id_applicazione
           FROM v_riscossioni_con_rpt) a
     JOIN applicazioni ON a.id_applicazione = applicazioni.id;



-- Principal Intermediario
alter table intermediari add column principal VARCHAR(4000);
update intermediari set principal = (select valore from connettori where connettori.cod_proprieta = 'PRINCIPAL' and connettori.cod_connettore = intermediari.cod_connettore_pdd);
alter table intermediari alter column principal set NOT NULL;

alter table intermediari add column principal_originale VARCHAR(4000);
update intermediari set principal_originale = (select valore from connettori where connettori.cod_proprieta = 'PRINCIPAL' and connettori.cod_connettore = intermediari.cod_connettore_pdd);
alter table intermediari alter column principal_originale set NOT NULL;

delete from connettori where cod_proprieta = 'PRINCIPAL';

-- Eventi
drop table eventi;

drop sequence seq_eventi;

CREATE SEQUENCE seq_eventi start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE eventi
(
	cod_dominio VARCHAR(35),
	iuv VARCHAR(35),
	ccp VARCHAR(35),
	categoria_evento VARCHAR(1),
	tipo_evento VARCHAR(35),
	sottotipo_evento VARCHAR(35),
	data TIMESTAMP,
	intervallo BIGINT,
	dettaglio TEXT,
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_eventi') NOT NULL,
	id_versamento BIGINT,
	id_pagamento_portale BIGINT,
	-- fk/pk keys constraints
	CONSTRAINT fk_evt_id_versamento FOREIGN KEY (id_versamento) REFERENCES versamenti(id),
	CONSTRAINT fk_evt_id_pagamento_portale FOREIGN KEY (id_pagamento_portale) REFERENCES pagamenti_portale(id),
	CONSTRAINT pk_eventi PRIMARY KEY (id)
);


