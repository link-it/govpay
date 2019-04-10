-- GP-677 (3.1.x_blocco_portale)
ALTER TABLE rpt ADD COLUMN bloccante BOOLEAN NOT NULL DEFAULT TRUE;

-- Autorizzazione tutti i domini e tutte le entrate per utenza
ALTER TABLE utenze ADD COLUMN autorizzazione_domini_star BOOLEAN NOT NULL DEFAULT FALSE;

ALTER TABLE utenze ADD COLUMN autorizzazione_tributi_star BOOLEAN NOT NULL DEFAULT FALSE;


-- 11/03/2019 (Correzione bug autorizzazione utenze_tributi)

ALTER TABLE utenze_tributi ADD COLUMN id_tipo_tributo BIGINT;
UPDATE utenze_tributi SET id_tipo_tributo = (SELECT id_tipo_tributo FROM tributi WHERE utenze_tributi.id_tributo = tributi.id);
ALTER TABLE utenze_tributi ALTER COLUMN id_tipo_tributo SET NOT NULL;
ALTER TABLE utenze_tributi DROP CONSTRAINT fk_nzt_id_tributo;
ALTER TABLE utenze_tributi DROP COLUMN id_tributo;
ALTER TABLE utenze_tributi ADD CONSTRAINT fk_nzt_id_tipo_tributo FOREIGN KEY (id_tipo_tributo) REFERENCES tipi_tributo(id);
	
-- 13/03/2019 (Eliminazione colonna principal dalla tabella Acl e sostituzione con la foreign key verso l'utenza)
ALTER TABLE acl ADD COLUMN id_utenza BIGINT;
UPDATE acl SET id_utenza = (SELECT id FROM utenze WHERE acl.principal is not null and acl.principal = utenze.principal_originale);
ALTER TABLE acl DROP COLUMN principal;
ALTER TABLE acl ADD CONSTRAINT fk_acl_id_utenza FOREIGN KEY (id_utenza) REFERENCES utenze(id);

-- 15/03/2019 campo GLN della tabella Domini non piu' obbligatorio
ALTER TABLE domini ALTER COLUMN gln DROP NOT NULL; 

-- 20/03/2019 (Autorizzazione basata sui tipi versamento)
ALTER TABLE utenze RENAME autorizzazione_tributi_star TO autorizzazione_tipi_vers_star;

CREATE SEQUENCE seq_tipi_versamento start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE tipi_versamento
(
	cod_tipo_versamento VARCHAR(35) NOT NULL,
	descrizione VARCHAR(255) NOT NULL,
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_tipi_versamento') NOT NULL,
	-- unique constraints
	CONSTRAINT unique_tipi_versamento_1 UNIQUE (cod_tipo_versamento),
	-- fk/pk keys constraints
	CONSTRAINT pk_tipi_versamento PRIMARY KEY (id)
);

INSERT INTO tipi_versamento (cod_tipo_versamento, descrizione) VALUES ('LIBERO', 'Pendenza libera');
INSERT INTO tipi_versamento (cod_tipo_versamento, descrizione) SELECT cod_tributo, descrizione FROM tipi_tributo;

CREATE SEQUENCE seq_utenze_tipo_vers start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE utenze_tipo_vers
(
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_utenze_tipo_vers') NOT NULL,
	id_utenza BIGINT NOT NULL,
	id_tipo_versamento BIGINT NOT NULL,
	-- fk/pk keys constraints
	CONSTRAINT fk_utv_id_utenza FOREIGN KEY (id_utenza) REFERENCES utenze(id),
	CONSTRAINT fk_utv_id_tipo_versamento FOREIGN KEY (id_tipo_versamento) REFERENCES tipi_versamento(id),
	CONSTRAINT pk_utenze_tipo_vers PRIMARY KEY (id)
);

INSERT INTO utenze_tipo_vers (id_utenza, id_tipo_versamento) SELECT utenze_tributi.id_utenza,
 tipi_versamento.id FROM utenze_tributi JOIN tipi_tributo ON utenze_tributi.id_tipo_tributo=tipi_tributo.id JOIN tipi_versamento ON tipi_tributo.cod_tributo = tipi_versamento.cod_tipo_versamento;

DROP VIEW versamenti_incassi;

ALTER TABLE versamenti ADD COLUMN id_tipo_versamento BIGINT;

UPDATE versamenti SET id_tipo_versamento = a.id_tipo_versamento FROM (SELECT versamenti.id as id_versamento, 
 tipi_versamento.id as id_tipo_versamento FROM versamenti JOIN singoli_versamenti ON versamenti.id = singoli_versamenti.id_versamento 
 AND singoli_versamenti.indice_dati=1 JOIN tributi ON singoli_versamenti.id_tributo = tributi.id JOIN tipi_tributo ON tributi.id_tipo_tributo = tipi_tributo.id 
 JOIN tipi_versamento ON tipi_versamento.cod_tipo_versamento = tipi_tributo.cod_tributo) a WHERE a.id_versamento = versamenti.id;

UPDATE versamenti SET id_tipo_versamento = (SELECT id FROM tipi_versamento WHERE cod_tipo_versamento = 'LIBERO') WHERE id_tipo_versamento IS NULL;

ALTER TABLE versamenti ALTER COLUMN id_tipo_versamento SET NOT NULL;
ALTER TABLE versamenti ADD CONSTRAINT fk_vrs_id_tipo_versamento FOREIGN KEY (id_tipo_versamento) REFERENCES tipi_versamento(id);

CREATE VIEW versamenti_incassi AS SELECT versamenti.id,
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
    max(versamenti.id_dominio) AS id_dominio,
    max(versamenti.id_tipo_versamento) AS id_tipo_versamento,
    max(versamenti.id_uo) AS id_uo,
    max(versamenti.id_applicazione) AS id_applicazione,
    MAX(CASE WHEN versamenti.avvisatura_abilitata = TRUE THEN 'TRUE' ELSE 'FALSE' END) AS avvisatura_abilitata,
    MAX(CASE WHEN versamenti.avvisatura_da_inviare = TRUE THEN 'TRUE' ELSE 'FALSE' END) AS avvisatura_da_inviare,
    MAX(versamenti.avvisatura_operazione) as avvisatura_operazione,               
    MAX(versamenti.avvisatura_modalita) as avvisatura_modalita,
    MAX(versamenti.avvisatura_tipo_pagamento) as avvisatura_tipo_pagamento,                   
    MAX(versamenti.avvisatura_cod_avvisatura) as avvisatura_cod_avvisatura,      
    MAX(versamenti.id_tracciato) as id_tracciato,
    max(
        CASE
            WHEN versamenti.ack = true THEN 'TRUE'::text
            ELSE 'FALSE'::text
        END) AS ack,
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

DROP TABLE utenze_tributi;
DROP SEQUENCE seq_utenze_tributi;

-- 22/03/2019 Tabella TipiVersamentoDomini
CREATE SEQUENCE seq_tipi_vers_domini start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE tipi_vers_domini
(
	codifica_iuv VARCHAR(4),
	tipo VARCHAR(35),
	paga_terzi BOOLEAN,
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_tipi_vers_domini') NOT NULL,
	id_tipo_versamento BIGINT NOT NULL,
	id_dominio BIGINT NOT NULL,
	-- unique constraints
	CONSTRAINT unique_tipi_vers_domini_1 UNIQUE (id_dominio,id_tipo_versamento),
	-- fk/pk keys constraints
	CONSTRAINT fk_tvd_id_tipo_versamento FOREIGN KEY (id_tipo_versamento) REFERENCES tipi_versamento(id),
	CONSTRAINT fk_tvd_id_dominio FOREIGN KEY (id_dominio) REFERENCES domini(id),
	CONSTRAINT pk_tipi_vers_domini PRIMARY KEY (id)
);

ALTER TABLE tipi_versamento ADD COLUMN codifica_iuv VARCHAR(4);
UPDATE tipi_versamento SET codifica_iuv = (SELECT cod_tributo_iuv FROM tipi_tributo WHERE cod_tipo_versamento = cod_tributo);

ALTER TABLE tipi_versamento ADD COLUMN tipo VARCHAR(35);
update tipi_versamento set tipo = 'DOVUTO' where (select tt.on_line from tipi_tributo tt where tt.cod_tributo = tipi_versamento.cod_tipo_versamento) = false;
update tipi_versamento set tipo = 'SPONTANEO' where (select tt.on_line from tipi_tributo tt where tt.cod_tributo = tipi_versamento.cod_tipo_versamento) = true;

ALTER TABLE tipi_versamento ADD COLUMN paga_terzi BOOLEAN DEFAULT false;
UPDATE tipi_versamento SET paga_terzi = (SELECT paga_terzi FROM tipi_tributo WHERE cod_tipo_versamento = cod_tributo);

UPDATE tipi_versamento SET tipo = 'DOVUTO', codifica_iuv = '', paga_terzi = false WHERE cod_tipo_versamento = 'LIBERO';
ALTER TABLE tipi_versamento ALTER COLUMN tipo SET NOT NULL;
ALTER TABLE tipi_versamento ALTER COLUMN paga_terzi SET NOT NULL;

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

ALTER TABLE versamenti ALTER COLUMN id_tipo_versamento_dominio SET NOT NULL;
ALTER TABLE versamenti ADD CONSTRAINT fk_vrs_id_tipo_versamento_dominio FOREIGN KEY (id_tipo_versamento_dominio) REFERENCES tipi_vers_domini(id);

CREATE VIEW versamenti_incassi AS SELECT versamenti.id,
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
    max(versamenti.id_dominio) AS id_dominio,
    max(versamenti.id_tipo_versamento) AS id_tipo_versamento,
    max(versamenti.id_tipo_versamento_dominio) AS id_tipo_versamento_dominio,
    max(versamenti.id_uo) AS id_uo,
    max(versamenti.id_applicazione) AS id_applicazione,
    MAX(CASE WHEN versamenti.avvisatura_abilitata = TRUE THEN 'TRUE' ELSE 'FALSE' END) AS avvisatura_abilitata,
    MAX(CASE WHEN versamenti.avvisatura_da_inviare = TRUE THEN 'TRUE' ELSE 'FALSE' END) AS avvisatura_da_inviare,
    MAX(versamenti.avvisatura_operazione) as avvisatura_operazione,               
    MAX(versamenti.avvisatura_modalita) as avvisatura_modalita,
    MAX(versamenti.avvisatura_tipo_pagamento) as avvisatura_tipo_pagamento,                   
    MAX(versamenti.avvisatura_cod_avvisatura) as avvisatura_cod_avvisatura,      
    MAX(versamenti.id_tracciato) as id_tracciato,
    max(
        CASE
            WHEN versamenti.ack = true THEN 'TRUE'::text
            ELSE 'FALSE'::text
        END) AS ack,
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
     JOIN tipi_versamento ON tipi_versamento.id = versamenti.id_tipo_versamento 
     JOIN tipi_vers_domini ON tipi_vers_domini.id = versamenti.id_tipo_versamento_dominio 
  WHERE COALESCE(tipi_vers_domini.tipo,tipi_versamento.tipo) = 'DOVUTO' OR pagamenti.importo_pagato > 0::double precision
  GROUP BY versamenti.id, versamenti.debitore_identificativo, versamenti.stato_versamento;

-- 27/03/2019 Tipo Pendenza Abilitato

ALTER TABLE tipi_versamento ADD COLUMN abilitato BOOLEAN;
UPDATE tipi_versamento SET abilitato = true;
ALTER TABLE tipi_versamento ALTER COLUMN abilitato SET NOT NULL;

ALTER TABLE tipi_vers_domini ADD COLUMN abilitato BOOLEAN;
UPDATE tipi_vers_domini SET abilitato = tributi.abilitato FROM tributi, tipi_tributo, tipi_versamento WHERE tributi.id_tipo_tributo = tipi_tributo.id AND tipi_tributo.cod_tributo = tipi_versamento.cod_tipo_versamento AND tipi_versamento.id = tipi_vers_domini.id_tipo_versamento;
UPDATE tipi_vers_domini SET abilitato = true FROM tipi_versamento WHERE tipi_versamento.id = tipi_vers_domini.id_tipo_versamento AND tipi_versamento.cod_tipo_versamento = 'LIBERO';
ALTER TABLE tipi_vers_domini ALTER COLUMN abilitato SET NOT NULL;

-- 02/04/2019 Aggiunto riferimento all'applicazione nella tabella pagamenti portale

ALTER TABLE pagamenti_portale ADD COLUMN id_applicazione BIGINT;
ALTER TABLE pagamenti_portale ADD CONSTRAINT fk_ppt_id_applicazione FOREIGN KEY (id_applicazione) REFERENCES applicazioni(id);
	
-- 02/04/2019 Divisione dei diritti sul servizio 'Pagamenti e Pendenze' in 'Pagamenti' e 'Pendenze'
insert into acl (ruolo,servizio,diritti,id_utenza) select acl.ruolo as ruolo, 'Pagamenti' as servizio, acl.diritti as diritti, acl.id_utenza as id_utenza from acl where acl.servizio = 'Pagamenti e Pendenze';
insert into acl (ruolo,servizio,diritti,id_utenza) select acl.ruolo as ruolo, 'Pendenze' as servizio, acl.diritti as diritti, acl.id_utenza as id_utenza from acl where acl.servizio = 'Pagamenti e Pendenze';
delete from acl where servizio = 'Pagamenti e Pendenze';
delete from acl where servizio = 'Statistiche';

-- 04/04/2019 Eliminazione del filtro (tipo_versamento = Dovuto o versamento pagato) preimpostato sulla vista incassi.
DROP VIEW versamenti_incassi;

CREATE VIEW versamenti_incassi AS SELECT versamenti.id,
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
    versamenti.debitore_identificativo AS debitore_identificativo,
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
    max(versamenti.id_dominio) AS id_dominio,
    max(versamenti.id_tipo_versamento) AS id_tipo_versamento,
    max(versamenti.id_tipo_versamento_dominio) AS id_tipo_versamento_dominio,
    max(versamenti.id_uo) AS id_uo,
    max(versamenti.id_applicazione) AS id_applicazione,
    MAX(CASE WHEN versamenti.avvisatura_abilitata = TRUE THEN 'TRUE' ELSE 'FALSE' END) AS avvisatura_abilitata,
    MAX(CASE WHEN versamenti.avvisatura_da_inviare = TRUE THEN 'TRUE' ELSE 'FALSE' END) AS avvisatura_da_inviare,
    MAX(versamenti.avvisatura_operazione) as avvisatura_operazione,
    MAX(versamenti.avvisatura_modalita) as avvisatura_modalita,
    MAX(versamenti.avvisatura_tipo_pagamento) as avvisatura_tipo_pagamento,
    MAX(versamenti.avvisatura_cod_avvisatura) as avvisatura_cod_avvisatura,
    MAX(versamenti.id_tracciato) as id_tracciato,
    max(
        CASE
            WHEN versamenti.ack = true THEN 'TRUE'::text
            ELSE 'FALSE'::text
        END) AS ack,
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
     JOIN tipi_versamento ON tipi_versamento.id = versamenti.id_tipo_versamento
  GROUP BY versamenti.id, versamenti.debitore_identificativo, versamenti.stato_versamento;

-- 09/04/2019 Unificazione del connettore di verifica e notifica

UPDATE applicazioni SET cod_connettore_esito = SUBSTRING(cod_connettore_esito, 1, POSITION ('_' in cod_connettore_esito)) || 'INTEGRAZIONE';
ALTER TABLE applicazioni RENAME cod_connettore_esito TO cod_connettore_integrazione;

UPDATE connettori SET cod_connettore = SUBSTRING(cod_connettore, 1, POSITION ('_' in cod_connettore)) || 'INTEGRAZIONE' WHERE cod_connettore LIKE '%_ESITO';
DELETE FROM connettori WHERE cod_connettore LIKE '%_VERIFICA';

ALTER TABLE applicazioni DROP COLUMN cod_connettore_verifica;

-- 09/04/2019 ACL sulle API
insert into acl (servizio,diritti,id_utenza) select 'API Pagamenti' as Servizio , 'RW' as diritti, acl.id_utenza from acl join applicazioni on acl.id_utenza = applicazioni.id_utenza where (acl.ruolo is null and acl.id_utenza is not null and acl.servizio  = 'Pagamenti');
insert into acl (servizio,diritti,id_utenza) select 'API Pendenze' as Servizio , 'RW' as diritti, acl.id_utenza from acl join applicazioni on acl.id_utenza = applicazioni.id_utenza where (acl.ruolo is null and acl.id_utenza is not null and acl.servizio  = 'Pendenze');
insert into acl (servizio,diritti,id_utenza) select 'API Ragioneria' as Servizio , 'RW' as diritti, acl.id_utenza from acl join applicazioni on acl.id_utenza = applicazioni.id_utenza where (acl.ruolo is null and acl.id_utenza is not null and acl.servizio  = 'Rendicontazioni e Incassi');



