-- GP-677 (3.1.x_blocco_portale)
ALTER TABLE rpt ADD COLUMN bloccante NUMBER;
UPDATE rpt SET bloccante = 1;
alter table rpt MODIFY (bloccante NOT NULL);

-- Autorizzazione tutti i domini e tutte le entrate per utenza
ALTER TABLE utenze ADD COLUMN autorizzazione_domini_star NUMBER;
update utenze set autorizzazione_domini_star = 0;
ALTER TABLE utenze MODIFY (autorizzazione_domini_star NOT NULL);

ALTER TABLE utenze ADD COLUMN autorizzazione_tributi_star NUMBER;
update utenze set autorizzazione_tributi_star = 0;
ALTER TABLE utenze MODIFY (autorizzazione_tributi_star NOT NULL);



-- 11/03/2019 (Correzione bug autorizzazione utenze_tributi)
ALTER TABLE utenze_tributi ADD COLUMN id_tipo_tributo NUMBER;
UPDATE ut SET ut.id_tipo_tributo = t.id_tipo_tributo FROM utenze_tributi ut, tributi t WHERE ut.id_tributo = t.id;
ALTER TABLE utenze_tributi MODIFY (id_tipo_tributo NOT NULL);
ALTER TABLE utenze_tributi DROP CONSTRAINT fk_nzt_id_tributo;
ALTER TABLE utenze_tributi DROP COLUMN id_tributo;
ALTER TABLE utenze_tributi ADD CONSTRAINT fk_nzt_id_tipo_tributo FOREIGN KEY (id_tipo_tributo) REFERENCES tipi_tributo(id);

-- 13/03/2019 (Eliminazione colonna principal dalla tabella Acl e sostituzione con la foreign key verso l'utenza)
ALTER TABLE acl ADD COLUMN id_utenza NUMBER;
UPDATE a SET a.id_utenza = u.id FROM acl a, utenze u WHERE a.principal is not null and a.principal = u.principal_originale;
ALTER TABLE acl DROP COLUMN principal;
ALTER TABLE acl ADD CONSTRAINT fk_acl_id_utenza FOREIGN KEY (id_utenza) REFERENCES utenze(id);

-- 15/03/2019 campo GLN della tabella Domini non piu' obbligatorio
ALTER TABLE domini MODIFY (gln NULL);

-- 20/03/2019 (Autorizzazione basata sui tipi versamento)
ALTER TABLE utenze RENAME COLUMN autorizzazione_tributi_star TO autorizzazione_tipi_vers_star;

CREATE SEQUENCE seq_tipi_versamento MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE tipi_versamento
(
	cod_tipo_versamento VARCHAR2(35 CHAR) NOT NULL,
	descrizione VARCHAR2(255 CHAR) NOT NULL,
	-- fk/pk columns
	id NUMBER NOT NULL,
	-- unique constraints
	CONSTRAINT unique_tipi_versamento_1 UNIQUE (cod_tipo_versamento),
	-- fk/pk keys constraints
	CONSTRAINT pk_tipi_versamento PRIMARY KEY (id)
);

CREATE TRIGGER trg_tipi_versamento
BEFORE
insert on tipi_versamento
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_tipi_versamento.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;
/

INSERT INTO tipi_versamento (cod_tipo_versamento, descrizione) VALUES ('LIBERO', 'Pendenza libera');
INSERT INTO tipi_versamento (cod_tipo_versamento, descrizione) SELECT cod_tributo, descrizione FROM tipi_tributo;

CREATE SEQUENCE seq_utenze_tipo_vers MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE utenze_tipo_vers
(
	-- fk/pk columns
	id NUMBER NOT NULL,
	id_utenza NUMBER NOT NULL,
	id_tipo_versamento NUMBER NOT NULL,
	-- fk/pk keys constraints
	CONSTRAINT fk_utv_id_utenza FOREIGN KEY (id_utenza) REFERENCES utenze(id),
	CONSTRAINT fk_utv_id_tipo_versamento FOREIGN KEY (id_tipo_versamento) REFERENCES tipi_versamento(id),
	CONSTRAINT pk_utenze_tipo_vers PRIMARY KEY (id)
);

CREATE TRIGGER trg_utenze_tipo_vers
BEFORE
insert on utenze_tipo_vers
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_utenze_tipo_vers.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;
/

INSERT INTO utenze_tipo_vers (id_utenza, id_tipo_versamento) SELECT utenze_tributi.id_utenza,
 tipi_versamento.id FROM utenze_tributi JOIN tipi_tributo ON utenze_tributi.id_tipo_tributo=tipi_tributo.id JOIN tipi_versamento ON tipi_tributo.cod_tributo = tipi_versamento.cod_tipo_versamento;

DROP VIEW versamenti_incassi;

ALTER TABLE versamenti ADD COLUMN id_tipo_versamento NUMBER;

UPDATE v SET v.id_tipo_versamento = tv.id FROM versamenti v JOIN singoli_versamenti sv ON v.id = sv.id_versamento 
 AND sv.indice_dati=1 JOIN tributi t ON sv.id_tributo = t.id JOIN tipi_tributo tt ON t.id_tipo_tributo = tt.id JOIN tipi_versamento tv ON tv.cod_tipo_versamento = tt.cod_tributo
 WHERE sv.id_versamento = v.id;

UPDATE v SET v.id_tipo_versamento = tv.id FROM versamenti v, tipi_versamento tv WHERE tv.cod_tipo_versamento = 'LIBERO' and v.id_tipo_versamento IS NULL

ALTER TABLE versamenti MODIFY (id_tipo_versamento NOT NULL);
ALTER TABLE versamenti ADD CONSTRAINT fk_vrs_id_tipo_versamento FOREIGN KEY (id_tipo_versamento) REFERENCES tipi_versamento(id);

CREATE VIEW versamenti_incassi AS
SELECT
versamenti.id as id,
MAX(versamenti.cod_versamento_ente) as cod_versamento_ente,
MAX(versamenti.nome) as nome,
MAX(versamenti.importo_totale) as importo_totale,
versamenti.stato_versamento as stato_versamento,
MAX(versamenti.descrizione_stato) as descrizione_stato,
MAX(CASE WHEN versamenti.aggiornabile = 1 THEN 'TRUE' ELSE 'FALSE' END) AS aggiornabile,
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
MAX(dbms_lob.substr(versamenti.dati_allegati)) as dati_allegati,
MAX(versamenti.incasso) as incasso,
MAX(dbms_lob.substr(versamenti.anomalie)) as anomalie,
MAX(versamenti.iuv_versamento) as iuv_versamento,
MAX(versamenti.numero_avviso) as numero_avviso,
MAX(versamenti.id_dominio) as id_dominio,
MAX(versamenti.id_tipo_versamento) AS id_tipo_versamento,
MAX(versamenti.id_uo) as id_uo,
MAX(versamenti.id_applicazione) as id_applicazione,
MAX(CASE WHEN versamenti.avvisatura_abilitata = 1 THEN 'TRUE' ELSE 'FALSE' END) AS avvisatura_abilitata,
MAX(CASE WHEN versamenti.avvisatura_da_inviare = 1 THEN 'TRUE' ELSE 'FALSE' END) AS avvisatura_da_inviare,
MAX(versamenti.avvisatura_operazione) as avvisatura_operazione,               
MAX(versamenti.avvisatura_modalita) as avvisatura_modalita,
MAX(versamenti.avvisatura_tipo_pagamento) as avvisatura_tipo_pagamento,                   
MAX(versamenti.avvisatura_cod_avvisatura) as avvisatura_cod_avvisatura,      
MAX(versamenti.id_tracciato) as id_tracciato,
MAX(CASE WHEN versamenti.ack = 1 THEN 'TRUE' ELSE 'FALSE' END) AS ack,
MAX(CASE WHEN versamenti.anomalo = 1 THEN 'TRUE' ELSE 'FALSE' END) AS anomalo,
MAX(pagamenti.data_pagamento) as data_pagamento,
SUM(CASE WHEN pagamenti.importo_pagato IS NOT NULL THEN pagamenti.importo_pagato ELSE 0 END) AS importo_pagato,
SUM(CASE WHEN pagamenti.stato = 'INCASSATO' THEN pagamenti.importo_pagato ELSE 0 END) AS importo_incassato,
MAX(CASE WHEN pagamenti.stato IS NULL THEN 'NON_PAGATO' WHEN pagamenti.stato = 'INCASSATO' THEN 'INCASSATO' ELSE 'PAGATO' END) AS stato_pagamento,
MAX(pagamenti.iuv) AS iuv_pagamento,
MAX(CASE WHEN versamenti.stato_versamento = 'NON_ESEGUITO' AND versamenti.data_validita > CURRENT_DATE THEN 0 ELSE 1 END) AS smart_order_rank,
MIN(ABS((date_to_unix_for_smart_order(CURRENT_DATE) * 1000) - (date_to_unix_for_smart_order(COALESCE(pagamenti.data_pagamento, versamenti.data_validita, versamenti.data_creazione))) *1000)) AS smart_order_date
FROM versamenti LEFT JOIN singoli_versamenti ON versamenti.id = singoli_versamenti.id_versamento LEFT join pagamenti on singoli_versamenti.id = pagamenti.id_singolo_versamento
WHERE versamenti.numero_avviso IS NOT NULL OR pagamenti.importo_pagato > 0
GROUP BY versamenti.id, versamenti.debitore_identificativo, versamenti.stato_versamento;

DROP TRIGGER trg_utenze_tributi;
DROP TABLE utenze_tributi;
DROP SEQUENCE seq_utenze_tributi;

-- 22/03/2019 Tabella TipiVersamentoDomini
CREATE SEQUENCE seq_tipi_vers_domini MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE tipi_vers_domini
(
	codifica_iuv VARCHAR2(4 CHAR),
	tipo VARCHAR2(35 CHAR),
	paga_terzi NUMBER,
	-- fk/pk columns
	id NUMBER NOT NULL,
	id_tipo_versamento NUMBER NOT NULL,
	id_dominio NUMBER NOT NULL,
	-- unique constraints
	CONSTRAINT unique_tipi_vers_domini_1 UNIQUE (id_dominio,id_tipo_versamento),
	-- fk/pk keys constraints
	CONSTRAINT fk_tvd_id_tipo_versamento FOREIGN KEY (id_tipo_versamento) REFERENCES tipi_versamento(id),
	CONSTRAINT fk_tvd_id_dominio FOREIGN KEY (id_dominio) REFERENCES domini(id),
	CONSTRAINT pk_tipi_vers_domini PRIMARY KEY (id)
);

CREATE TRIGGER trg_tipi_vers_domini
BEFORE
insert on tipi_vers_domini
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_tipi_vers_domini.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;
/

ALTER TABLE tipi_versamento ADD COLUMN codifica_iuv VARCHAR2(4 CHAR);
UPDATE tipi_versamento SET codifica_iuv = (SELECT cod_tributo_iuv FROM tipi_tributo WHERE cod_tipo_versamento = cod_tributo);

ALTER TABLE tipi_versamento ADD COLUMN tipo VARCHAR2(35 CHAR);
update tipi_versamento set tipo = 'DOVUTO' where (select tt.on_line from tipi_tributo tt where tt.cod_tributo = tipi_versamento.cod_tipo_versamento) = 0;
update tipi_versamento set tipo = 'SPONTANEO' where (select tt.on_line from tipi_tributo tt where tt.cod_tributo = tipi_versamento.cod_tipo_versamento) = 1;

ALTER TABLE tipi_versamento ADD COLUMN paga_terzi NUMBER;
UPDATE tipi_versamento SET paga_terzi = (SELECT paga_terzi FROM tipi_tributo WHERE cod_tipo_versamento = cod_tributo);

UPDATE tipi_versamento SET tipo = 'DOVUTO', codifica_iuv = '', paga_terzi = 0 WHERE cod_tipo_versamento = 'LIBERO';
ALTER TABLE tipi_versamento MODIFY (tipo NOT NULL);
ALTER TABLE tipi_versamento MODIFY (paga_terzi DEFAULT 0);
ALTER TABLE tipi_versamento MODIFY (paga_terzi NOT NULL);


-- copia dei dati della tabella tributi
insert into tipi_vers_domini (id_tipo_versamento,codifica_iuv,tipo,paga_terzi,id_dominio) select tv.id as id_tipo_versamento, t.cod_tributo_iuv as codifica_iuv, 'DOVUTO' as tipo, t.paga_terzi as paga_terzi , t.id_dominio as id_dominio from tributi t, tipi_tributo tt, tipi_versamento tv where t.id_tipo_tributo = tt.id and tt.cod_tributo = tv.cod_tipo_versamento and t.on_line = 0;
insert into tipi_vers_domini (id_tipo_versamento,codifica_iuv,tipo,paga_terzi,id_dominio) select tv.id as id_tipo_versamento, t.cod_tributo_iuv as codifica_iuv, 'SPONTANEO' as tipo, t.paga_terzi as paga_terzi , t.id_dominio as id_dominio from tributi t, tipi_tributo tt, tipi_versamento tv where t.id_tipo_tributo = tt.id and tt.cod_tributo = tv.cod_tipo_versamento and t.on_line = 1;
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

ALTER TABLE versamenti ADD COLUMN id_tipo_versamento_dominio NUMBER;
update versamenti set id_tipo_versamento_dominio = (select tipi_vers_domini.id from tipi_vers_domini where versamenti.id_dominio = tipi_vers_domini.id_dominio and versamenti.id_tipo_versamento = tipi_vers_domini.id_tipo_versamento );

ALTER TABLE versamenti MODIFY (id_tipo_versamento_dominio NOT NULL);
ALTER TABLE versamenti ADD CONSTRAINT fk_vrs_id_tipo_versamento_dominio FOREIGN KEY (id_tipo_versamento_dominio) REFERENCES tipi_vers_domini(id);

CREATE VIEW versamenti_incassi AS
SELECT
versamenti.id as id,
MAX(versamenti.cod_versamento_ente) as cod_versamento_ente,
MAX(versamenti.nome) as nome,
MAX(versamenti.importo_totale) as importo_totale,
versamenti.stato_versamento as stato_versamento,
MAX(versamenti.descrizione_stato) as descrizione_stato,
MAX(CASE WHEN versamenti.aggiornabile = 1 THEN 'TRUE' ELSE 'FALSE' END) AS aggiornabile,
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
MAX(dbms_lob.substr(versamenti.dati_allegati)) as dati_allegati,
MAX(versamenti.incasso) as incasso,
MAX(dbms_lob.substr(versamenti.anomalie)) as anomalie,
MAX(versamenti.iuv_versamento) as iuv_versamento,
MAX(versamenti.numero_avviso) as numero_avviso,
MAX(versamenti.id_dominio) as id_dominio,
MAX(versamenti.id_tipo_versamento) AS id_tipo_versamento,
MAX(versamenti.id_tipo_versamento_dominio) AS id_tipo_versamento_dominio,
MAX(versamenti.id_uo) as id_uo,
MAX(versamenti.id_applicazione) as id_applicazione,
MAX(CASE WHEN versamenti.avvisatura_abilitata = 1 THEN 'TRUE' ELSE 'FALSE' END) AS avvisatura_abilitata,
MAX(CASE WHEN versamenti.avvisatura_da_inviare = 1 THEN 'TRUE' ELSE 'FALSE' END) AS avvisatura_da_inviare,
MAX(versamenti.avvisatura_operazione) as avvisatura_operazione,               
MAX(versamenti.avvisatura_modalita) as avvisatura_modalita,
MAX(versamenti.avvisatura_tipo_pagamento) as avvisatura_tipo_pagamento,                   
MAX(versamenti.avvisatura_cod_avvisatura) as avvisatura_cod_avvisatura,      
MAX(versamenti.id_tracciato) as id_tracciato,
MAX(CASE WHEN versamenti.ack = 1 THEN 'TRUE' ELSE 'FALSE' END) AS ack,
MAX(CASE WHEN versamenti.anomalo = 1 THEN 'TRUE' ELSE 'FALSE' END) AS anomalo,
MAX(pagamenti.data_pagamento) as data_pagamento,
SUM(CASE WHEN pagamenti.importo_pagato IS NOT NULL THEN pagamenti.importo_pagato ELSE 0 END) AS importo_pagato,
SUM(CASE WHEN pagamenti.stato = 'INCASSATO' THEN pagamenti.importo_pagato ELSE 0 END) AS importo_incassato,
MAX(CASE WHEN pagamenti.stato IS NULL THEN 'NON_PAGATO' WHEN pagamenti.stato = 'INCASSATO' THEN 'INCASSATO' ELSE 'PAGATO' END) AS stato_pagamento,
MAX(pagamenti.iuv) AS iuv_pagamento,
MAX(CASE WHEN versamenti.stato_versamento = 'NON_ESEGUITO' AND versamenti.data_validita > CURRENT_DATE THEN 0 ELSE 1 END) AS smart_order_rank,
MIN(ABS((date_to_unix_for_smart_order(CURRENT_DATE) * 1000) - (date_to_unix_for_smart_order(COALESCE(pagamenti.data_pagamento, versamenti.data_validita, versamenti.data_creazione))) *1000)) AS smart_order_date
FROM versamenti LEFT JOIN singoli_versamenti ON versamenti.id = singoli_versamenti.id_versamento LEFT join pagamenti on singoli_versamenti.id = pagamenti.id_singolo_versamento 
JOIN tipi_versamento ON tipi_versamento.id = versamenti.id_tipo_versamento JOIN tipi_vers_domini ON tipi_vers_domini.id = versamenti.id_tipo_versamento_dominio  
WHERE COALESCE(tipi_vers_domini.tipo,tipi_versamento.tipo) = 'DOVUTO' OR pagamenti.importo_pagato > 0
GROUP BY versamenti.id, versamenti.debitore_identificativo, versamenti.stato_versamento;

-- 27/03/2019 Tipo Pendenza Abilitato

ALTER TABLE tipi_versamento ADD COLUMN abilitato NUMBER;
UPDATE tipi_versamento SET abilitato = 1;
ALTER TABLE tipi_versamento MODIFY (abilitato NOT NULL);

ALTER TABLE tipi_vers_domini ADD COLUMN abilitato NUMBER;
UPDATE tipi_vers_domini SET abilitato = tributi.abilitato FROM tributi, tipi_tributo, tipi_versamento WHERE tributi.id_tipo_tributo = tipi_tributo.id AND tipi_tributo.cod_tributo = tipi_versamento.cod_tipo_versamento AND tipi_versamento.id = tipi_vers_domini.id_tipo_versamento;
UPDATE tipi_vers_domini SET abilitato = 1 FROM tipi_versamento WHERE tipi_versamento.id = tipi_vers_domini.id_tipo_versamento AND tipi_versamento.cod_tipo_versamento = 'LIBERO';
ALTER TABLE tipi_vers_domini MODIFY (abilitato NOT NULL);


-- 02/04/2019 Aggiunto riferimento all'applicazione nella tabella pagamenti portale

ALTER TABLE pagamenti_portale ADD COLUMN id_applicazione NUMBER;
ALTER TABLE pagamenti_portale ADD CONSTRAINT fk_ppt_id_applicazione FOREIGN KEY (id_applicazione) REFERENCES applicazioni(id);

-- 02/04/2019 Divisione dei diritti sul servizio 'Pagamenti e Pendenze' in 'Pagamenti' e 'Pendenze'
insert into acl (ruolo,servizio,diritti,id_utenza) select acl.ruolo as ruolo, 'Pagamenti' as servizio, acl.diritti as diritti, acl.id_utenza as id_utenza from acl where acl.servizio = 'Pagamenti e Pendenze';
insert into acl (ruolo,servizio,diritti,id_utenza) select acl.ruolo as ruolo, 'Pendenze' as servizio, acl.diritti as diritti, acl.id_utenza as id_utenza from acl where acl.servizio = 'Pagamenti e Pendenze';
delete from acl where servizio = 'Pagamenti e Pendenze';
delete from acl where servizio = 'Statistiche';


-- 04/04/2019 Eliminazione del filtro (tipo_versamento = Dovuto o versamento pagato) preimpostato sulla vista incassi.
DROP VIEW versamenti_incassi;

CREATE VIEW versamenti_incassi AS
SELECT
versamenti.id as id,
MAX(versamenti.cod_versamento_ente) as cod_versamento_ente,
MAX(versamenti.nome) as nome,
MAX(versamenti.importo_totale) as importo_totale,
versamenti.stato_versamento as stato_versamento,
MAX(versamenti.descrizione_stato) as descrizione_stato,
MAX(CASE WHEN versamenti.aggiornabile = 1 THEN 'TRUE' ELSE 'FALSE' END) AS aggiornabile,
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
MAX(dbms_lob.substr(versamenti.dati_allegati)) as dati_allegati,
MAX(versamenti.incasso) as incasso,
MAX(dbms_lob.substr(versamenti.anomalie)) as anomalie,
MAX(versamenti.iuv_versamento) as iuv_versamento,
MAX(versamenti.numero_avviso) as numero_avviso,
MAX(versamenti.id_dominio) as id_dominio,
MAX(versamenti.id_tipo_versamento) AS id_tipo_versamento,
MAX(versamenti.id_tipo_versamento_dominio) AS id_tipo_versamento_dominio,
MAX(versamenti.id_uo) as id_uo,
MAX(versamenti.id_applicazione) as id_applicazione,
MAX(CASE WHEN versamenti.avvisatura_abilitata = 1 THEN 'TRUE' ELSE 'FALSE' END) AS avvisatura_abilitata,
MAX(CASE WHEN versamenti.avvisatura_da_inviare = 1 THEN 'TRUE' ELSE 'FALSE' END) AS avvisatura_da_inviare,
MAX(versamenti.avvisatura_operazione) as avvisatura_operazione,               
MAX(versamenti.avvisatura_modalita) as avvisatura_modalita,
MAX(versamenti.avvisatura_tipo_pagamento) as avvisatura_tipo_pagamento,                   
MAX(versamenti.avvisatura_cod_avvisatura) as avvisatura_cod_avvisatura,      
MAX(versamenti.id_tracciato) as id_tracciato,
MAX(CASE WHEN versamenti.ack = 1 THEN 'TRUE' ELSE 'FALSE' END) AS ack,
MAX(CASE WHEN versamenti.anomalo = 1 THEN 'TRUE' ELSE 'FALSE' END) AS anomalo,
MAX(pagamenti.data_pagamento) as data_pagamento,
SUM(CASE WHEN pagamenti.importo_pagato IS NOT NULL THEN pagamenti.importo_pagato ELSE 0 END) AS importo_pagato,
SUM(CASE WHEN pagamenti.stato = 'INCASSATO' THEN pagamenti.importo_pagato ELSE 0 END) AS importo_incassato,
MAX(CASE WHEN pagamenti.stato IS NULL THEN 'NON_PAGATO' WHEN pagamenti.stato = 'INCASSATO' THEN 'INCASSATO' ELSE 'PAGATO' END) AS stato_pagamento,
MAX(pagamenti.iuv) AS iuv_pagamento,
MAX(CASE WHEN versamenti.stato_versamento = 'NON_ESEGUITO' AND versamenti.data_validita > CURRENT_DATE THEN 0 ELSE 1 END) AS smart_order_rank,
MIN(ABS((date_to_unix_for_smart_order(CURRENT_DATE) * 1000) - (date_to_unix_for_smart_order(COALESCE(pagamenti.data_pagamento, versamenti.data_validita, versamenti.data_creazione))) *1000)) AS smart_order_date
FROM versamenti LEFT JOIN singoli_versamenti ON versamenti.id = singoli_versamenti.id_versamento LEFT join pagamenti on singoli_versamenti.id = pagamenti.id_singolo_versamento 
JOIN tipi_versamento ON tipi_versamento.id = versamenti.id_tipo_versamento 
GROUP BY versamenti.id, versamenti.debitore_identificativo, versamenti.stato_versamento;


-- 09/04/2019 Unificazione del connettore di verifica e notifica

UPDATE applicazioni SET cod_connettore_esito = CONCAT(SUBSTRING_INDEX(cod_connettore_esito, '_', 1),'_INTEGRAZIONE');
ALTER TABLE applicazioni RENAME COLUMN cod_connettore_esito TO cod_connettore_integrazione;

UPDATE connettori SET cod_connettore = CONCAT(SUBSTRING_INDEX(cod_connettore, '_', 1),'_INTEGRAZIONE') WHERE cod_connettore LIKE '%_ESITO';
DELETE FROM connettori WHERE cod_connettore LIKE '%_VERIFICA';

ALTER TABLE applicazioni DROP COLUMN cod_connettore_verifica;

-- 09/04/2019 ACL sulle API
insert into acl (servizio,diritti,id_utenza) select 'API Pagamenti' as Servizio , 'RW' as diritti, acl.id_utenza from acl join applicazioni on acl.id_utenza = applicazioni.id_utenza where (acl.ruolo is null and acl.id_utenza is not null and acl.servizio  = 'Pagamenti');
insert into acl (servizio,diritti,id_utenza) select 'API Pendenze' as Servizio , 'RW' as diritti, acl.id_utenza from acl join applicazioni on acl.id_utenza = applicazioni.id_utenza where (acl.ruolo is null and acl.id_utenza is not null and acl.servizio  = 'Pendenze');
insert into acl (servizio,diritti,id_utenza) select 'API Ragioneria' as Servizio , 'RW' as diritti, acl.id_utenza from acl join applicazioni on acl.id_utenza = applicazioni.id_utenza where (acl.ruolo is null and acl.id_utenza is not null and acl.servizio  = 'Rendicontazioni e Incassi');

-- 12/04/2019 Informazioni su Json Schema e Dati Allegati per i Tipi Versamento

ALTER TABLE tipi_versamento ADD COLUMN json_schema CLOB;
ALTER TABLE tipi_versamento ADD COLUMN dati_allegati CLOB;

ALTER TABLE tipi_vers_domini ADD COLUMN json_schema CLOB;
ALTER TABLE tipi_vers_domini ADD COLUMN dati_allegati CLOB;

ALTER TABLE tipi_vers_domini MODIFY (abilitato NULL);


-- 30/04/2019 eliminazione foreign key id_applicazione dalla tabella RPT

ALTER TABLE rpt DROP CONSTRAINT fk_rpt_id_applicazione;
ALTER TABLE rpt DROP COLUMN id_applicazione;

-- 08/05/2019 aggiunto idincasso ai flussi di rendicontazione
ALTER TABLE fr ADD COLUMN id_incasso NUMBER;
ALTER TABLE fr ADD CONSTRAINT fk_fr_id_incasso FOREIGN KEY (id_incasso) REFERENCES incassi(id);

-- 13/05/2019 aggiunto sct alla tabella incassi

ALTER TABLE incassi ADD COLUMN sct VARCHAR2(35 CHAR);

-- 13/05/2019 nuova tabella gestione delle stampe

DROP TABLE avvisi;
DROP SEQUENCE seq_avvisi;

CREATE SEQUENCE seq_stampe MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE stampe
(
	data_creazione TIMESTAMP NOT NULL,
	tipo VARCHAR2(16 CHAR) NOT NULL,
	pdf BLOB,
	-- fk/pk columns
	id NUMBER NOT NULL,
	id_versamento NUMBER NOT NULL,
	-- unique constraints
	CONSTRAINT unique_stampe_1 UNIQUE (id_versamento,tipo),
	-- fk/pk keys constraints
	CONSTRAINT fk_stm_id_versamento FOREIGN KEY (id_versamento) REFERENCES versamenti(id),
	CONSTRAINT pk_stampe PRIMARY KEY (id)
);

CREATE TRIGGER trg_stampe
BEFORE
insert on stampe
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_stampe.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;
/


-- 21/05/2019 Aggiunta tabella per memorizzare la configurazione

CREATE SEQUENCE seq_configurazione MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE configurazione
(
	giornale_eventi CLOB,
	-- fk/pk columns
	id NUMBER NOT NULL,
	-- fk/pk keys constraints
	CONSTRAINT pk_configurazione PRIMARY KEY (id)
);

CREATE TRIGGER trg_configurazione
BEFORE
insert on configurazione
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_configurazione.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;
/

-- 22/05/2019 Configurazione Giornale Eventi
INSERT INTO configurazione (giornale_eventi) values ('{"apiEnte":{"letture":{"log":"MAI","dump":"MAI"},"scritture":{"log":"SOLO_ERRORE","dump":"SOLO_ERRORE"}},"apiPagamento":{"letture":{"log":"MAI","dump":"MAI"},"scritture":{"log":"SOLO_ERRORE","dump":"SOLO_ERRORE"}},"apiRagioneria":{"letture":{"log":"MAI","dump":"MAI"},"scritture":{"log":"SOLO_ERRORE","dump":"SOLO_ERRORE"}},"apiBackoffice":{"letture":{"log":"MAI","dump":"MAI"},"scritture":{"log":"SOLO_ERRORE","dump":"SOLO_ERRORE"}},"apiPagoPA":{"letture":{"log":"SEMPRE","dump":"SOLO_ERRORE"},"scritture":{"log":"SEMPRE","dump":"SOLO_ERRORE"}},"apiPendenze":{"letture":{"log":"MAI","dump":"MAI"},"scritture":{"log":"SOLO_ERRORE","dump":"SOLO_ERRORE"}}}');



