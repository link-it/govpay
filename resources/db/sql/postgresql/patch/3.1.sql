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
	raw_richiesta BYTEA NOT NULL,
	file_name_esito VARCHAR(256),
	raw_esito BYTEA NOT NULL,
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

CREATE VIEW versamenti_incassi AS SELECT
versamenti.id as id,
MAX(versamenti.cod_versamento_ente) as cod_versamento_ente,          
MAX(versamenti.nome) as nome,                         
MAX(versamenti.importo_totale) as importo_totale,               
MAX(versamenti.stato_versamento) as stato_versamento,             
MAX(versamenti.descrizione_stato) as descrizione_stato,           
MAX(CASE WHEN versamenti.aggiornabile = TRUE THEN 'TRUE' ELSE 'FALSE' END) AS aggiornabile,
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
MAX(CASE WHEN versamenti.da_avvisare = TRUE THEN 'TRUE' ELSE 'FALSE' END) AS da_avvisare,
MAX(versamenti.cod_avvisatura) as cod_avvisatura,               
MAX(versamenti.id_tracciato) as id_tracciato,      
MAX(CASE WHEN versamenti.ack = TRUE THEN 'TRUE' ELSE 'FALSE' END) AS ack,
MAX(versamenti.note) as note,
MAX(CASE WHEN versamenti.anomalo = TRUE THEN 'TRUE' ELSE 'FALSE' END) AS anomalo,
MAX(pagamenti.data_pagamento) as data_pagamento,            
SUM(CASE WHEN pagamenti.importo_pagato IS NOT NULL THEN pagamenti.importo_pagato ELSE 0 END) AS importo_pagato,
SUM(CASE WHEN pagamenti.stato = 'INCASSATO' THEN pagamenti.importo_pagato ELSE 0 END) AS importo_incassato,
MAX(CASE WHEN pagamenti.stato IS NULL THEN 'NON_PAGATO' WHEN pagamenti.stato = 'INCASSATO' THEN 'INCASSATO' ELSE 'PAGATO' END) AS stato_pagamento
FROM versamenti LEFT JOIN singoli_versamenti ON versamenti.id = singoli_versamenti.id_versamento LEFT join pagamenti on singoli_versamenti.id = pagamenti.id_singolo_versamento
GROUP BY versamenti.id;


