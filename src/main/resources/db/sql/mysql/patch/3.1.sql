-- GP-677 (3.1.x_blocco_portale)
ALTER TABLE rpt ADD COLUMN bloccante BOOLEAN DEFAULT true;
update rpt set bloccante = true;
ALTER TABLE rpt MODIFY COLUMN bloccante BOOLEAN NOT NULL;

-- Autorizzazione tutti i domini e tutte le entrate per utenza
ALTER TABLE utenze ADD COLUMN autorizzazione_domini_star BOOLEAN DEFAULT false;
update utenze set autorizzazione_domini_star = false;
ALTER TABLE utenze MODIFY COLUMN autorizzazione_domini_star BOOLEAN NOT NULL;

ALTER TABLE utenze ADD COLUMN autorizzazione_tributi_star BOOLEAN DEFAULT false;
update utenze set autorizzazione_tributi_star = false;
ALTER TABLE utenze MODIFY COLUMN autorizzazione_tributi_star BOOLEAN NOT NULL;


-- 11/03/2019 (Correzione bug autorizzazione utenze_tributi)
ALTER TABLE utenze_tributi ADD COLUMN id_tipo_tributo BIGINT;
UPDATE utenze_tributi ut, tributi t SET ut.id_tipo_tributo = t.id_tipo_tributo WHERE ut.id_tributo = t.id;
ALTER TABLE utenze_tributi MODIFY COLUMN id_tipo_tributo BIGINT NOT NULL;
ALTER TABLE utenze_tributi DROP FOREIGN KEY fk_nzt_id_tributo;
ALTER TABLE utenze_tributi DROP COLUMN id_tributo;
ALTER TABLE utenze_tributi ADD CONSTRAINT fk_nzt_id_tipo_tributo FOREIGN KEY (id_tipo_tributo) REFERENCES tipi_tributo(id);

-- 13/03/2019 (Eliminazione colonna principal dalla tabella Acl e sostituzione con la foreign key verso l'utenza)
ALTER TABLE acl ADD COLUMN id_utenza BIGINT;
UPDATE acl a, utenze u SET a.id_utenza = u.id WHERE a.principal is not null and a.principal = u.principal_originale;
ALTER TABLE acl DROP COLUMN principal;
ALTER TABLE acl ADD CONSTRAINT fk_acl_id_utenza FOREIGN KEY (id_utenza) REFERENCES utenze(id);

-- 15/03/2019 campo GLN della tabella Domini non piu' obbligatorio
ALTER TABLE domini MODIFY COLUMN gln VARCHAR(35) NULL;


-- 20/03/2019 (Autorizzazione basata sui tipi versamento)
ALTER TABLE utenze CHANGE COLUMN autorizzazione_tributi_star autorizzazione_tipi_vers_star BOOLEAN NOT NULL DEFAULT false;

CREATE TABLE tipi_versamento
(
	cod_tipo_versamento VARCHAR(35) NOT NULL,
	descrizione VARCHAR(255) NOT NULL,
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT,
	-- unique constraints
	CONSTRAINT unique_tipi_versamento_1 UNIQUE (cod_tipo_versamento),
	-- fk/pk keys constraints
	CONSTRAINT pk_tipi_versamento PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;

-- index
CREATE UNIQUE INDEX index_tipi_versamento_1 ON tipi_versamento (cod_tipo_versamento);

INSERT INTO tipi_versamento (cod_tipo_versamento, descrizione) VALUES ('LIBERO', 'Pendenza libera');
INSERT INTO tipi_versamento (cod_tipo_versamento, descrizione) SELECT cod_tributo, descrizione FROM tipi_tributo;

CREATE TABLE utenze_tipo_vers
(
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT COMMENT 'Identificativo fisico',
	id_utenza BIGINT NOT NULL COMMENT 'Riferimento all\'utenza',
	id_tipo_versamento BIGINT NOT NULL COMMENT 'Riferimento al tipo pendenza',
	-- fk/pk keys constraints
	CONSTRAINT fk_utv_id_utenza FOREIGN KEY (id_utenza) REFERENCES utenze(id),
	CONSTRAINT fk_utv_id_tipo_versamento FOREIGN KEY (id_tipo_versamento) REFERENCES tipi_versamento(id),
	CONSTRAINT pk_utenze_tipo_vers PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs COMMENT 'Autorizzazioni sui tipi pendenza';

INSERT INTO utenze_tipo_vers (id_utenza, id_tipo_versamento) SELECT utenze_tributi.id_utenza,
 tipi_versamento.id FROM utenze_tributi JOIN tipi_tributo ON utenze_tributi.id_tipo_tributo=tipi_tributo.id JOIN tipi_versamento ON tipi_tributo.cod_tributo = tipi_versamento.cod_tipo_versamento;

DROP VIEW versamenti_incassi;

ALTER TABLE versamenti ADD COLUMN id_tipo_versamento BIGINT;

UPDATE versamenti SET id_tipo_versamento = a.id_tipo_versamento FROM (SELECT versamenti.id as id_versamento, 
 tipi_versamento.id as id_tipo_versamento FROM versamenti JOIN singoli_versamenti ON versamenti.id = singoli_versamenti.id_versamento 
 AND singoli_versamenti.indice_dati=1 JOIN tributi ON singoli_versamenti.id_tributo = tributi.id JOIN tipi_tributo ON tributi.id_tipo_tributo = tipi_tributo.id 
 JOIN tipi_versamento ON tipi_versamento.cod_tipo_versamento = tipi_tributo.cod_tributo) a WHERE a.id_versamento = versamenti.id;

UPDATE versamenti SET id_tipo_versamento = (SELECT id FROM tipi_versamento WHERE cod_tipo_versamento = 'LIBERO') WHERE id_tipo_versamento IS NULL;

ALTER TABLE versamenti MODIFY COLUMN id_tipo_versamento BIGINT NOT NULL;
ALTER TABLE versamenti ADD CONSTRAINT fk_vrs_id_tipo_versamento FOREIGN KEY (id_tipo_versamento) REFERENCES tipi_versamento(id);

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
MAX(versamenti.id_tipo_versamento) as id_tipo_versamento,
MAX(versamenti.id_dominio) as id_dominio,                   
MAX(versamenti.id_uo) as id_uo,                        
MAX(versamenti.id_applicazione) as id_applicazione,             
MAX(CASE WHEN versamenti.avvisatura_abilitata = TRUE THEN 'TRUE' ELSE 'FALSE' END) AS avvisatura_abilitata,
MAX(CASE WHEN versamenti.avvisatura_da_inviare = TRUE THEN 'TRUE' ELSE 'FALSE' END) AS avvisatura_da_inviare,
MAX(versamenti.avvisatura_operazione) as avvisatura_operazione,               
MAX(versamenti.avvisatura_modalita) as avvisatura_modalita,
MAX(versamenti.avvisatura_tipo_pagamento) as avvisatura_tipo_pagamento,                   
MAX(versamenti.avvisatura_cod_avvisatura) as avvisatura_cod_avvisatura,      
MAX(versamenti.id_tracciato) as id_tracciato,
MAX(CASE WHEN versamenti.ack = TRUE THEN 'TRUE' ELSE 'FALSE' END) AS ack,
MAX(CASE WHEN versamenti.anomalo = TRUE THEN 'TRUE' ELSE 'FALSE' END) AS anomalo,
MAX(pagamenti.data_pagamento) as data_pagamento,            
SUM(CASE WHEN pagamenti.importo_pagato IS NOT NULL THEN pagamenti.importo_pagato ELSE 0 END) AS importo_pagato,
SUM(CASE WHEN pagamenti.stato = 'INCASSATO' THEN pagamenti.importo_pagato ELSE 0 END) AS importo_incassato,
MAX(CASE WHEN pagamenti.stato IS NULL THEN 'NON_PAGATO' WHEN pagamenti.stato = 'INCASSATO' THEN 'INCASSATO' ELSE 'PAGATO' END) AS stato_pagamento,
MAX(pagamenti.iuv) AS iuv_pagamento,
MAX(CASE WHEN versamenti.stato_versamento = 'NON_ESEGUITO' AND versamenti.data_validita > now() THEN 0 ELSE 1 END) AS smart_order_rank,
MIN(ABS((UNIX_TIMESTAMP(now()) *1000) - (UNIX_TIMESTAMP(COALESCE(pagamenti.data_pagamento, versamenti.data_validita, versamenti.data_creazione)) * 1000))) AS smart_order_date
FROM versamenti LEFT JOIN singoli_versamenti ON versamenti.id = singoli_versamenti.id_versamento LEFT join pagamenti on singoli_versamenti.id = pagamenti.id_singolo_versamento
WHERE versamenti.numero_avviso IS NOT NULL OR pagamenti.importo_pagato > 0
GROUP BY versamenti.id, versamenti.debitore_identificativo, versamenti.stato_versamento;

DROP TABLE utenze_tributi;

-- 22/03/2019 Tabella TipiVersamentoDomini
CREATE TABLE tipi_vers_domini
(
	codifica_iuv VARCHAR(4) COMMENT 'Codifica del tipo pendenza nello iuv specifica per dominio',
	tipo VARCHAR(35) COMMENT 'Indica se il tipo pendenza e\' pagabile spontaneamente per il dominio',
	paga_terzi BOOLEAN  COMMENT 'Indica se il tipo pendenza e\' pagabile da soggetti terzi per il dominio',
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT COMMENT 'Identificativo fisico',
	id_tipo_versamento BIGINT NOT NULL COMMENT 'Riferimento al tipo pendenza afferente',
	id_dominio BIGINT NOT NULL COMMENT 'Riferimento al dominio afferente',
	-- unique constraints
	CONSTRAINT unique_tipi_vers_domini_1 UNIQUE (id_dominio,id_tipo_versamento),
	-- fk/pk keys constraints
	CONSTRAINT fk_tvd_id_tipo_versamento FOREIGN KEY (id_tipo_versamento) REFERENCES tipi_versamento(id),
	CONSTRAINT fk_tvd_id_dominio FOREIGN KEY (id_dominio) REFERENCES domini(id),
	CONSTRAINT pk_tipi_vers_domini PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;

-- index
CREATE UNIQUE INDEX index_tipi_vers_domini_1 ON tipi_vers_domini (id_dominio,id_tipo_versamento);

ALTER TABLE tipi_versamento ADD COLUMN codifica_iuv VARCHAR(4);
UPDATE tipi_versamento SET codifica_iuv = (SELECT cod_tributo_iuv FROM tipi_tributo WHERE cod_tipo_versamento = cod_tributo);

ALTER TABLE tipi_versamento ADD COLUMN tipo VARCHAR(35);
update tipi_versamento set tipo = 'DOVUTO' where (select tt.on_line from tipi_tributo tt where tt.cod_tributo = tipi_versamento.cod_tipo_versamento) = false;
update tipi_versamento set tipo = 'SPONTANEO' where (select tt.on_line from tipi_tributo tt where tt.cod_tributo = tipi_versamento.cod_tipo_versamento) = true;

ALTER TABLE tipi_versamento ADD COLUMN paga_terzi BOOLEAN DEFAULT false;
UPDATE tipi_versamento SET paga_terzi = (SELECT paga_terzi FROM tipi_tributo WHERE cod_tipo_versamento = cod_tributo);

UPDATE tipi_versamento SET tipo = 'DOVUTO', codifica_iuv = '', paga_terzi = false WHERE cod_tipo_versamento = 'LIBERO';
ALTER TABLE tipi_versamento MODIFY COLUMN tipo VARCHAR(35) NOT NULL;
ALTER TABLE tipi_versamento MODIFY COLUMN paga_terzi BOOLEAN DEFAULT false NOT NULL;

-- copia dei dati della tabella tributi
insert into tipi_vers_domini (id_tipo_versamento,codifica_iuv,tipo,paga_terzi,id_dominio) select tv.id as id_tipo_versamento, t.cod_tributo_iuv as codifica_iuv, 'DOVUTO' as tipo, t.paga_terzi as paga_terzi , t.id_dominio as id_dominio from tributi t, tipi_tributo tt, tipi_versamento tv where t.id_tipo_tributo = tt.id and tt.cod_tributo = tv.cod_tipo_versamento and t.on_line = false;
insert into tipi_vers_domini (id_tipo_versamento,codifica_iuv,tipo,paga_terzi,id_dominio) select tv.id as id_tipo_versamento, t.cod_tributo_iuv as codifica_iuv, 'SPONTANEO' as tipo, t.paga_terzi as paga_terzi , t.id_dominio as id_dominio from tributi t, tipi_tributo tt, tipi_versamento tv where t.id_tipo_tributo = tt.id and tt.cod_tributo = tv.cod_tipo_versamento and t.on_line = true;
insert into tipi_vers_domini (id_tipo_versamento,codifica_iuv,tipo,paga_terzi,id_dominio) select tv.id as id_tipo_versamento, t.cod_tributo_iuv as codifica_iuv, null as tipo, t.paga_terzi as paga_terzi , t.id_dominio as id_dominio from tributi t, tipi_tributo tt, tipi_versamento tv where t.id_tipo_tributo = tt.id and tt.cod_tributo = tv.cod_tipo_versamento and t.on_line is null;

-- genero le entries per il tipo pendenza libero
insert into tipi_vers_domini (id_dominio, id_tipo_versamento) select id , (select id from tipi_versamento where cod_tipo_versamento = 'LIBERO') from domini;


-- eliminazione colonne non piu' significative
alter table tributi drop column paga_terzi;
alter table tributi drop column on_line;
alter table tributi drop column cod_tributo_iuv;

alter table tipi_tributo drop column paga_terzi;
alter table tipi_tributo drop column on_line;
alter table tipi_tributo drop column cod_tributo_iuv;

-- aggiunta id_tipo_versamento_dominio alla tabella versamenti
DROP VIEW versamenti_incassi;

ALTER TABLE versamenti ADD COLUMN id_tipo_versamento_dominio BIGINT;
update versamenti set id_tipo_versamento_dominio = (select tipi_vers_domini.id from tipi_vers_domini where versamenti.id_dominio = tipi_vers_domini.id_dominio and versamenti.id_tipo_versamento = tipi_vers_domini.id_tipo_versamento );

ALTER TABLE versamenti MODIFY COLUMN id_tipo_versamento_dominio BIGINT NOT NULL;
ALTER TABLE versamenti ADD CONSTRAINT fk_vrs_id_tipo_versamento_dominio FOREIGN KEY (id_tipo_versamento_dominio) REFERENCES tipi_vers_domini(id);


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
MAX(versamenti.id_tipo_versamento) as id_tipo_versamento,
MAX(versamenti.id_tipo_versamento_dominio) as id_tipo_versamento_dominio,
MAX(versamenti.id_dominio) as id_dominio,                   
MAX(versamenti.id_uo) as id_uo,                        
MAX(versamenti.id_applicazione) as id_applicazione,             
MAX(CASE WHEN versamenti.avvisatura_abilitata = TRUE THEN 'TRUE' ELSE 'FALSE' END) AS avvisatura_abilitata,
MAX(CASE WHEN versamenti.avvisatura_da_inviare = TRUE THEN 'TRUE' ELSE 'FALSE' END) AS avvisatura_da_inviare,
MAX(versamenti.avvisatura_operazione) as avvisatura_operazione,               
MAX(versamenti.avvisatura_modalita) as avvisatura_modalita,
MAX(versamenti.avvisatura_tipo_pagamento) as avvisatura_tipo_pagamento,                   
MAX(versamenti.avvisatura_cod_avvisatura) as avvisatura_cod_avvisatura,      
MAX(versamenti.id_tracciato) as id_tracciato,
MAX(CASE WHEN versamenti.ack = TRUE THEN 'TRUE' ELSE 'FALSE' END) AS ack,
MAX(CASE WHEN versamenti.anomalo = TRUE THEN 'TRUE' ELSE 'FALSE' END) AS anomalo,
MAX(pagamenti.data_pagamento) as data_pagamento,            
SUM(CASE WHEN pagamenti.importo_pagato IS NOT NULL THEN pagamenti.importo_pagato ELSE 0 END) AS importo_pagato,
SUM(CASE WHEN pagamenti.stato = 'INCASSATO' THEN pagamenti.importo_pagato ELSE 0 END) AS importo_incassato,
MAX(CASE WHEN pagamenti.stato IS NULL THEN 'NON_PAGATO' WHEN pagamenti.stato = 'INCASSATO' THEN 'INCASSATO' ELSE 'PAGATO' END) AS stato_pagamento,
MAX(pagamenti.iuv) AS iuv_pagamento,
MAX(CASE WHEN versamenti.stato_versamento = 'NON_ESEGUITO' AND versamenti.data_validita > now() THEN 0 ELSE 1 END) AS smart_order_rank,
MIN(ABS((UNIX_TIMESTAMP(now()) *1000) - (UNIX_TIMESTAMP(COALESCE(pagamenti.data_pagamento, versamenti.data_validita, versamenti.data_creazione)) * 1000))) AS smart_order_date
FROM versamenti LEFT JOIN singoli_versamenti ON versamenti.id = singoli_versamenti.id_versamento LEFT join pagamenti on singoli_versamenti.id = pagamenti.id_singolo_versamento 
JOIN tipi_versamento ON tipi_versamento.id = versamenti.id_tipo_versamento JOIN tipi_vers_domini ON tipi_vers_domini.id = versamenti.id_tipo_versamento_dominio 
WHERE COALESCE(tipi_vers_domini.tipo,tipi_versamento.tipo) = 'DOVUTO' OR pagamenti.importo_pagato > 0
GROUP BY versamenti.id, versamenti.debitore_identificativo, versamenti.stato_versamento;

