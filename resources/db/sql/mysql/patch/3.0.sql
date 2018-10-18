--GP-557
CREATE TABLE avvisi
(
       cod_dominio VARCHAR(35) NOT NULL,
       iuv VARCHAR(35) NOT NULL,
       -- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
       data_creazione TIMESTAMP(3) NOT NULL DEFAULT 0,
       stato VARCHAR(255) NOT NULL,
       pdf MEDIUMBLOB,
       -- fk/pk columns
       id BIGINT AUTO_INCREMENT,
       -- check constraints
       -- fk/pk keys constraints
       CONSTRAINT pk_avvisi PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;

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


CREATE TABLE pagamenti_portale
(
       cod_portale VARCHAR(35) NOT NULL,
       cod_canale VARCHAR(35),
       nome VARCHAR(255) NOT NULL,
       importo DOUBLE NOT NULL,
       versante_identificativo VARCHAR(35),
       id_sessione VARCHAR(35) NOT NULL,
       id_sessione_portale VARCHAR(35),
       id_sessione_psp VARCHAR(35),
       stato VARCHAR(35) NOT NULL,
       codice_stato VARCHAR(35) NOT NULL,
       descrizione_stato VARCHAR(1024),
       psp_redirect_url VARCHAR(1024),
       psp_esito VARCHAR(255),
       json_request LONGTEXT,
       wisp_id_dominio VARCHAR(255),
       wisp_key_pa VARCHAR(255),
       wisp_key_wisp VARCHAR(255),
       wisp_html LONGTEXT,
       -- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
       data_richiesta TIMESTAMP(3) DEFAULT 0,
       url_ritorno VARCHAR(1024) NOT NULL,
       cod_psp VARCHAR(35),
       tipo_versamento VARCHAR(4),
       multi_beneficiario VARCHAR(35),
       -- fk/pk columns
       id BIGINT AUTO_INCREMENT,
       -- unique constraints
       CONSTRAINT unique_pagamenti_portale_1 UNIQUE (id_sessione),
       -- fk/pk keys constraints
       CONSTRAINT pk_pagamenti_portale PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;

-- index
CREATE UNIQUE INDEX index_pagamenti_portale_1 ON pagamenti_portale (id_sessione);



CREATE TABLE pag_port_versamenti
(
       -- fk/pk columns
       id BIGINT AUTO_INCREMENT,
       id_pagamento_portale BIGINT NOT NULL,
       id_versamento BIGINT NOT NULL,
       -- fk/pk keys constraints
       CONSTRAINT fk_ppv_id_pagamento_portale FOREIGN KEY (id_pagamento_portale) REFERENCES pagamenti_portale(id),
       CONSTRAINT fk_ppv_id_versamento FOREIGN KEY (id_versamento) REFERENCES versamenti(id),
       CONSTRAINT pk_pag_port_versamenti PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;

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
ALTER TABLE versamenti MODIFY COLUMN id_dominio BIGINT NOT NULL;
ALTER TABLE versamenti ADD COLUMN debitore_tipo VARCHAR(1);


ALTER TABLE acl DROP COLUMN id_portale;

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
	VARCHAR(255) NOT NULL,
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT,
	-- fk/pk keys constraints
	CONSTRAINT pk_acl PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;


DROP TABLE ruoli;


CREATE TABLE utenze
(
	principal VARCHAR(255) NOT NULL,
        abilitato BOOLEAN NOT NULL DEFAULT true,
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT,
	-- unique constraints
	CONSTRAINT unique_utenze_1 UNIQUE (principal),
	-- fk/pk keys constraints
	CONSTRAINT pk_utenze PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;

-- index
CREATE INDEX index_utenze_1 ON utenze (principal);


CREATE TABLE utenze_domini
(
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT,
	id_utenza BIGINT NOT NULL,
	id_dominio BIGINT NOT NULL,
	-- fk/pk keys constraints
	CONSTRAINT fk_nzd_id_utenza FOREIGN KEY (id_utenza) REFERENCES utenze(id),
	CONSTRAINT fk_nzd_id_dominio FOREIGN KEY (id_dominio) REFERENCES domini(id),
	CONSTRAINT pk_utenze_domini PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;




CREATE TABLE utenze_tributi
(
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT,
	id_utenza BIGINT NOT NULL,
	id_tributo BIGINT NOT NULL,
	-- fk/pk keys constraints
	CONSTRAINT fk_nzt_id_utenza FOREIGN KEY (id_utenza) REFERENCES utenze(id),
	CONSTRAINT fk_nzt_id_tributo FOREIGN KEY (id_tributo) REFERENCES tributi(id),
	CONSTRAINT pk_utenze_tributi PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;



insert into utenze (principal) select distinct principal from ((select principal from operatori) union (select principal from applicazioni) )as s;


ALTER TABLE applicazioni ADD COLUMN id_utenza BIGINT;
UPDATE applicazioni set id_utenza = (select id from utenze where principal = applicazioni.principal);
ALTER TABLE applicazioni MODIFY COLUMN id_utenza BIGINT NOT NULL;
ALTER TABLE applicazioni DROP COLUMN principal;
ALTER TABLE applicazioni DROP COLUMN abilitato;


ALTER TABLE operatori ADD COLUMN id_utenza BIGINT;
UPDATE operatori set id_utenza = (select id from utenze where principal = operatori.principal);
ALTER TABLE operatori MODIFY COLUMN id_utenza BIGINT NOT NULL;
ALTER TABLE operatori DROP COLUMN principal;
ALTER TABLE operatori DROP COLUMN profilo;
ALTER TABLE operatori DROP COLUMN abilitato;

ALTER TABLE applicazioni ADD COLUMN auto_iuv BOOLEAN;
UPDATE applicazioni SET auto_iuv = true;
ALTER TABLE applicazioni MODIFY COLUMN auto_iuv BOOLEAN NOT NULL;

ALTER TABLE domini DROP COLUMN custom_iuv;
ALTER TABLE domini DROP COLUMN iuv_prefix_strict;
ALTER TABLE domini DROP COLUMN riuso_iuv;

ALTER TABLE iban_accredito DROP COLUMN id_seller_bank;
ALTER TABLE iban_accredito DROP COLUMN id_negozio;
ALTER TABLE applicazioni DROP COLUMN versione;

ALTER TABLE versamenti add column dati_allegati LONGTEXT;
ALTER TABLE singoli_versamenti add column dati_allegati LONGTEXT;
ALTER TABLE singoli_versamenti add column descrizione VARCHAR(256);
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
ALTER TABLE pagamenti MODIFY COLUMN tipo VARCHAR(35) NOT NULL;

ALTER TABLE versamenti ADD COLUMN incasso VARCHAR(1);
ALTER TABLE versamenti ADD COLUMN anomalie LONGTEXT;

ALTER TABLE versamenti ADD COLUMN iuv_versamento VARCHAR(35);
ALTER TABLE versamenti ADD COLUMN numero_avviso VARCHAR(35);
ALTER TABLE versamenti ADD COLUMN avvisatura VARCHAR(1);
ALTER TABLE versamenti ADD COLUMN tipo_pagamento INT;

ALTER TABLE utenze MODIFY COLUMN principal VARCHAR(4000);
ALTER TABLE utenze ADD COLUMN principal_originale VARCHAR(4000);
update utenze set principal_originale = principal;
ALTER TABLE utenze MODIFY COLUMN principal_originale VARCHAR(4000) NOT NULL;

-- patch dati pagamenti_portale malformati non gestiti dal cruscotto

delete from pag_port_versamenti where id_pagamento_portale in (select pagamenti_portale.id from  pagamenti_portale left join rpt on rpt.id_pagamento_portale = pagamenti_portale.id where rpt.id is null);
delete from pagamenti_portale where id in (select pagamenti_portale.id from  pagamenti_portale left join rpt on rpt.id_pagamento_portale = pagamenti_portale.id where rpt.id is null);

-- fine patch dati
ALTER TABLE versamenti MODIFY COLUMN tassonomia_avviso VARCHAR(35) NULL;

ALTER TABLE pagamenti_portale ADD COLUMN ack BOOLEAN NOT NULL DEFAULT FALSE;
ALTER TABLE pagamenti_portale ADD COLUMN note LONGTEXT;

ALTER TABLE pagamenti_portale ADD COLUMN tipo INT;
UPDATE pagamenti_portale set tipo=1;
ALTER TABLE pagamenti_portale MODIFY COLUMN tipo INT NOT NULL;

ALTER TABLE pagamenti_portale MODIFY COLUMN url_ritorno VARCHAR(1024) NULL;

ALTER TABLE intermediari ADD COLUMN cod_connettore_ftp VARCHAR(35);
CREATE TABLE tracciati
(
	cod_dominio VARCHAR(35) NOT NULL,
	tipo VARCHAR(10) NOT NULL,
	stato VARCHAR(122) NOT NULL,
	descrizione_stato VARCHAR(256),
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
	data_caricamento TIMESTAMP(3) NOT NULL DEFAULT 0,
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
	data_completamento TIMESTAMP(3) NOT NULL DEFAULT 0,
	bean_dati LONGTEXT,
	file_name_richiesta VARCHAR(256),
	raw_richiesta MEDIUMBLOB NOT NULL,
	file_name_esito VARCHAR(256),
	raw_esito MEDIUMBLOB NOT NULL,
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT,
	id_operatore BIGINT,
	-- fk/pk keys constraints
	CONSTRAINT fk_trc_id_operatore FOREIGN KEY (id_operatore) REFERENCES operatori(id),
	CONSTRAINT pk_tracciati PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;




ALTER TABLE intermediari ADD COLUMN da_avvisare BOOLEAN NOT NULL DEFAULT false;
ALTER TABLE intermediari ADD COLUMN cod_avvisatura VARCHAR(20);
ALTER TABLE intermediari ADD COLUMN id_tracciato BIGINT;
ALTER TABLE intermediari ADD CONSTRAINT fk_vrs_id_tracciato FOREIGN KEY (id_tracciato) REFERENCES tracciati(id);

CREATE TABLE esiti_avvisatura
(
	cod_dominio VARCHAR(35) NOT NULL,
	identificativo_avvisatura VARCHAR(20) NOT NULL,
	tipo_canale INT NOT NULL,
	cod_canale VARCHAR(35),
	data TIMESTAMP(3) NOT NULL DEFAULT 0,
	cod_esito INT NOT NULL,
	descrizione_esito VARCHAR(140) NOT NULL,
	id_tracciato BIGINT NOT NULL,
	CONSTRAINT fk_sta_id_tracciato FOREIGN KEY (id_tracciato) REFERENCES tracciati(id),
	CONSTRAINT pk_esiti_avvisatura PRIMARY KEY (id)
);

insert into sonde(nome, classe, soglia_warn, soglia_error) values ('avvisatura-digitale', 'org.openspcoop2.utils.sonde.impl.SondaBatch', 3600000, 21600000);
insert into sonde(nome, classe, soglia_warn, soglia_error) values ('esito-avvisatura-digitale', 'org.openspcoop2.utils.sonde.impl.SondaBatch', 3600000, 21600000);

CREATE TABLE operazioni
(
        tipo_operazione VARCHAR(255) NOT NULL,
        linea_elaborazione BIGINT NOT NULL,
        stato VARCHAR(255) NOT NULL,
        dati_richiesta MEDIUMBLOB NOT NULL,
        dati_risposta MEDIUMBLOB,
        dettaglio_esito VARCHAR(255),
        cod_versamento_ente VARCHAR(255),
        cod_dominio VARCHAR(35),
        iuv VARCHAR(35),
        trn VARCHAR(35),
        -- fk/pk columns
        id BIGINT AUTO_INCREMENT,
        id_tracciato BIGINT NOT NULL,
        id_applicazione BIGINT,
        -- fk/pk keys constraints
        CONSTRAINT fk_ope_id_tracciato FOREIGN KEY (id_tracciato) REFERENCES tracciati(id),
        CONSTRAINT fk_ope_id_applicazione FOREIGN KEY (id_applicazione) REFERENCES applicazioni(id),
        CONSTRAINT pk_operazioni PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;

insert into sonde(nome, classe, soglia_warn, soglia_error) values ('caricamento-tracciati', 'org.openspcoop2.utils.sonde.impl.SondaBatch', 3600000, 21600000);
insert into sonde(nome, classe, soglia_warn, soglia_error) values ('check-tracciati', 'org.openspcoop2.utils.sonde.impl.SondaCoda', 1, 1);

ALTER TABLE versamenti ADD COLUMN ack BOOLEAN NOT NULL DEFAULT FALSE;
ALTER TABLE versamenti ADD COLUMN note LONGTEXT;
ALTER TABLE versamenti ADD COLUMN anomalo BOOLEAN NOT NULL DEFAULT FALSE;

alter table singoli_versamenti add column indice_dati INT;
update singoli_versamenti sv set indice_dati = (select sb1.indice_dati from (select sv1.id as id , sv1.id_versamento as id_versamento, row_number() over (partition by sv1.id_versamento) as indice_dati from singoli_versamenti sv1) as sb1 where sb1.id = sv.id);
alter table singoli_versamenti MODIFY column indice_dati NOT NULL;

alter table singoli_versamenti drop index unique_singoli_versamenti_1;
alter table singoli_versamenti add CONSTRAINT unique_singoli_versamenti_1 UNIQUE (id_versamento,cod_singolo_versamento_ente,indice_dati);
alter table singoli_versamenti drop index index_singoli_versamenti_1;
alter table singoli_versamenti add index index_singoli_versamenti_1 (id_versamento,cod_singolo_versamento_ente,indice_dati);

alter table rendicontazioni add column id_singolo_versamento BIGINT;
alter table rendicontazioni add CONSTRAINT fk_rnd_id_singolo_versamento FOREIGN KEY (id_singolo_versamento) REFERENCES singoli_versamenti(id);
-- aggiorno entries prendendo l'id singolo_versamento dal pagamento
update rendicontazioni set id_singolo_versamento = (select p.id_singolo_versamento from (select r1.id as id, pagamenti.id_singolo_versamento as id_singolo_versamento from pagamenti , rendicontazioni r1 where r1.id_pagamento = pagamenti.id) as p where p.id = rendicontazioni.id) ;

-- Sezione Viste

CREATE VIEW versamenti_incassi AS SELECT
versamenti.id as id,
MAX(versamenti.cod_versamento_ente) as cod_versamento_ente,          
MAX(versamenti.nome) as nome,                         
MAX(versamenti.importo_totale) as importo_totale,               
versamenti.stato_versamento as stato_versamento,             
MAX(versamenti.descrizione_stato) as descrizione_stato,           
MAX(CASE WHEN versamenti.aggiornabile = TRUE THEN 'TRUE' ELSE 'FALSE' END) AS aggiornabile,
MAX(versamenti.data_creazione) as data_creazione,               
MAX(versamenti.data_validita) as data_validita,                
MAX(versamenti.data_scadenza) as data_scadenza,                
MAX(versamenti.data_ora_ultimo_aggiornamento) as data_ora_ultimo_aggiornamento,
MAX(versamenti.causale_versamento) as causale_versamento,           
MAX(versamenti.debitore_tipo) as debitore_tipo,                
versamenti.debitore_identificativo as debitore_identificativo,      
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
MAX(CASE WHEN versamenti.da_avvisare = TRUE THEN 'TRUE' ELSE 'FALSE' END) AS da_avvisare,
MAX(versamenti.cod_avvisatura) as cod_avvisatura,               
MAX(versamenti.id_tracciato) as id_tracciato,      
MAX(CASE WHEN versamenti.ack = TRUE THEN 'TRUE' ELSE 'FALSE' END) AS ack,
MAX(versamenti.note) as note,
MAX(CASE WHEN versamenti.anomalo = TRUE THEN 'TRUE' ELSE 'FALSE' END) AS anomalo,
MAX(pagamenti.data_pagamento) as data_pagamento,            
SUM(CASE WHEN pagamenti.importo_pagato IS NOT NULL THEN pagamenti.importo_pagato ELSE 0 END) AS importo_pagato,
SUM(CASE WHEN pagamenti.stato = 'INCASSATO' THEN pagamenti.importo_pagato ELSE 0 END) AS importo_incassato,
MAX(CASE WHEN pagamenti.stato IS NULL THEN 'NON_PAGATO' WHEN pagamenti.stato = 'INCASSATO' THEN 'INCASSATO' ELSE 'PAGATO' END) AS stato_pagamento,
MAX(pagamenti.iuv) AS iuv_pagamento,
MAX(0) AS smart_order_rank,
MIN(0) AS smart_order_date
FROM versamenti LEFT JOIN singoli_versamenti ON versamenti.id = singoli_versamenti.id_versamento LEFT join pagamenti on singoli_versamenti.id = pagamenti.id_singolo_versamento
WHERE versamenti.numero_avviso IS NOT NULL OR pagamenti.importo_pagato > 0
GROUP BY versamenti.id, versamenti.debitore_identificativo, versamenti.stato_versamento;

-- FIX bug che non valorizzava il tipo debitore
update versamenti set debitore_tipo = 'F';

alter table domini add column aut_stampa_poste VARCHAR(255);
