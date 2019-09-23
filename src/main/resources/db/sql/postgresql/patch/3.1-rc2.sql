-- 22/05/2019 Configurazione Giornale Eventi
UPDATE configurazione set giornale_eventi = '{"apiEnte":{"letture":{"log":"MAI","dump":"MAI"},"scritture":{"log":"SOLO_ERRORE","dump":"SOLO_ERRORE"}},"apiPagamento":{"letture":{"log":"MAI","dump":"MAI"},"scritture":{"log":"SOLO_ERRORE","dump":"SOLO_ERRORE"}},"apiRagioneria":{"letture":{"log":"MAI","dump":"MAI"},"scritture":{"log":"SOLO_ERRORE","dump":"SOLO_ERRORE"}},"apiBackoffice":{"letture":{"log":"MAI","dump":"MAI"},"scritture":{"log":"SOLO_ERRORE","dump":"SOLO_ERRORE"}},"apiPagoPA":{"letture":{"log":"SEMPRE","dump":"SOLO_ERRORE"},"scritture":{"log":"SEMPRE","dump":"SOLO_ERRORE"}},"apiPendenze":{"letture":{"log":"MAI","dump":"MAI"},"scritture":{"log":"SOLO_ERRORE","dump":"SOLO_ERRORE"}}}' ;

-- 24/05/2019 nuova tabella eventi
DROP TABLE eventi;


CREATE TABLE eventi
(
	componente VARCHAR(35),
	ruolo VARCHAR(1),
	categoria_evento VARCHAR(1),
	tipo_evento VARCHAR(70),
	sottotipo_evento VARCHAR(35),
	data TIMESTAMP,
	intervallo BIGINT,
	esito VARCHAR(4),
	sottotipo_esito VARCHAR(35),
	dettaglio_esito TEXT,
	parametri_richiesta BYTEA,
	parametri_risposta BYTEA,
	dati_pago_pa TEXT,
	cod_versamento_ente VARCHAR(35),
	cod_applicazione VARCHAR(35),
	iuv VARCHAR(35),
	ccp VARCHAR(35),
	cod_dominio VARCHAR(35),
	id_sessione VARCHAR(35),
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_eventi') NOT NULL,
	-- fk/pk keys constraints
	CONSTRAINT pk_eventi PRIMARY KEY (id)
);


-- 18/06/2019 Configurazione avanzata dei tipi pendenza
ALTER TABLE tipi_versamento DROP COLUMN json_schema;
ALTER TABLE tipi_versamento DROP COLUMN dati_allegati;
ALTER TABLE tipi_versamento ADD COLUMN form_tipo VARCHAR(35);
ALTER TABLE tipi_versamento ADD COLUMN form_definizione TEXT;
ALTER TABLE tipi_versamento ADD COLUMN validazione_definizione TEXT;
ALTER TABLE tipi_versamento ADD COLUMN trasformazione_tipo VARCHAR(35);
ALTER TABLE tipi_versamento ADD COLUMN trasformazione_definizione TEXT;
ALTER TABLE tipi_versamento ADD COLUMN cod_applicazione VARCHAR(35);
ALTER TABLE tipi_versamento ADD COLUMN promemoria_avviso_tipo VARCHAR(35);
ALTER TABLE tipi_versamento ADD COLUMN promemoria_avviso_pdf BOOLEAN NOT NULL DEFAULT false;
ALTER TABLE tipi_versamento ADD COLUMN promemoria_avviso_oggetto TEXT;
ALTER TABLE tipi_versamento ADD COLUMN promemoria_avviso_messaggio TEXT;
ALTER TABLE tipi_versamento ADD COLUMN promemoria_ricevuta_tipo VARCHAR(35);
ALTER TABLE tipi_versamento ADD COLUMN promemoria_ricevuta_pdf BOOLEAN NOT NULL DEFAULT false;
ALTER TABLE tipi_versamento ADD COLUMN promemoria_ricevuta_oggetto TEXT;
ALTER TABLE tipi_versamento ADD COLUMN promemoria_ricevuta_messaggio TEXT;

ALTER TABLE tipi_vers_domini DROP COLUMN json_schema;
ALTER TABLE tipi_vers_domini DROP COLUMN dati_allegati;
ALTER TABLE tipi_vers_domini ADD COLUMN form_tipo VARCHAR(35);
ALTER TABLE tipi_vers_domini ADD COLUMN form_definizione TEXT;
ALTER TABLE tipi_vers_domini ADD COLUMN validazione_definizione TEXT;
ALTER TABLE tipi_vers_domini ADD COLUMN trasformazione_tipo VARCHAR(35);
ALTER TABLE tipi_vers_domini ADD COLUMN trasformazione_definizione TEXT;
ALTER TABLE tipi_vers_domini ADD COLUMN cod_applicazione VARCHAR(35);
ALTER TABLE tipi_vers_domini ADD COLUMN promemoria_avviso_tipo VARCHAR(35);
ALTER TABLE tipi_vers_domini ADD COLUMN promemoria_avviso_pdf BOOLEAN;
ALTER TABLE tipi_vers_domini ADD COLUMN promemoria_avviso_oggetto TEXT;
ALTER TABLE tipi_vers_domini ADD COLUMN promemoria_avviso_messaggio TEXT;
ALTER TABLE tipi_vers_domini ADD COLUMN promemoria_ricevuta_tipo VARCHAR(35);
ALTER TABLE tipi_vers_domini ADD COLUMN promemoria_ricevuta_pdf BOOLEAN;
ALTER TABLE tipi_vers_domini ADD COLUMN promemoria_ricevuta_oggetto TEXT;
ALTER TABLE tipi_vers_domini ADD COLUMN promemoria_ricevuta_messaggio TEXT;


-- 24/06/2019 Tabella per la spedizione dei promemoria via mail

CREATE SEQUENCE seq_promemoria start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE promemoria
(
	tipo VARCHAR(16) NOT NULL,
	data_creazione TIMESTAMP NOT NULL,
	stato VARCHAR(16) NOT NULL,
	descrizione_stato VARCHAR(255),
	destinatario_to VARCHAR(256) NOT NULL,
	destinatario_cc VARCHAR(256),
	messaggio_content_type VARCHAR(256),
	oggetto VARCHAR(512),
	messaggio TEXT,
	allega_pdf BOOLEAN NOT NULL DEFAULT false,
	data_aggiornamento_stato TIMESTAMP NOT NULL,
	data_prossima_spedizione TIMESTAMP NOT NULL,
	tentativi_spedizione BIGINT,
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_promemoria') NOT NULL,
	id_versamento BIGINT NOT NULL,
	id_rpt BIGINT,
	-- fk/pk keys constraints
	CONSTRAINT fk_prm_id_versamento FOREIGN KEY (id_versamento) REFERENCES versamenti(id),
	CONSTRAINT fk_prm_id_rpt FOREIGN KEY (id_rpt) REFERENCES rpt(id),
	CONSTRAINT pk_promemoria PRIMARY KEY (id)
);

insert into sonde(nome, classe, soglia_warn, soglia_error) values ('spedizione-promemoria', 'org.openspcoop2.utils.sonde.impl.SondaBatch', 86400000, 172800000);

-- 05/07/2019 Aggiunti ruoli utenza
ALTER TABLE utenze ADD COLUMN ruoli VARCHAR(512);

-- 05/07/2019 Aggiunte informazioni direzione e divisione alla tabella versamenti;

ALTER TABLE versamenti ADD COLUMN divisione VARCHAR(35);
ALTER TABLE versamenti ADD COLUMN direzione VARCHAR(35);

DROP view versamenti_incassi;

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
    MAX(versamenti.divisione) as divisione,
    MAX(versamenti.direzione) as direzione,	
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

-- 16/07/2019 Dimensione della colonna descrizione stato della tabella promemoria;
ALTER TABLE promemoria ALTER COLUMN descrizione_stato TYPE VARCHAR(1024);

-- 18/07/2019 Aggiunti nuovi campi alle viste riscossioni
DROP VIEW v_riscossioni;
DROP VIEW v_riscossioni_con_rpt;
DROP VIEW v_riscossioni_senza_rpt;

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
    rendicontazioni.data AS data_pagamento,
    singoli_versamenti.cod_singolo_versamento_ente AS cod_singolo_versamento_ente,
    rendicontazioni.indice_dati AS indice_dati,
    versamenti.cod_versamento_ente AS cod_versamento_ente,
    versamenti.id_applicazione AS id_applicazione,
    versamenti.debitore_identificativo AS debitore_identificativo,
    versamenti.id_tipo_versamento AS id_tipo_versamento,
    versamenti.cod_anno_tributario AS cod_anno_tributario,
    singoli_versamenti.id_tributo AS id_tributo
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
    pagamenti.data_pagamento AS data_pagamento,
    singoli_versamenti.cod_singolo_versamento_ente AS cod_singolo_versamento_ente,
    singoli_versamenti.indice_dati AS indice_dati,
    versamenti.cod_versamento_ente AS cod_versamento_ente,
    versamenti.id_applicazione AS id_applicazione,
    versamenti.debitore_identificativo AS debitore_identificativo,
    versamenti.id_tipo_versamento AS id_tipo_versamento,
    versamenti.cod_anno_tributario AS cod_anno_tributario,
    singoli_versamenti.id_tributo AS id_tributo
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
    a.data_pagamento,
    a.cod_singolo_versamento_ente,
    a.indice_dati,
    a.cod_versamento_ente,
    applicazioni.cod_applicazione,
    a.debitore_identificativo AS identificativo_debitore,
    a.cod_anno_tributario AS anno,
    tipi_versamento.cod_tipo_versamento,
    tipi_tributo.cod_tributo AS cod_entrata
   FROM ( SELECT v_riscossioni_senza_rpt.cod_dominio,
            v_riscossioni_senza_rpt.iuv,
            v_riscossioni_senza_rpt.iur,
            v_riscossioni_senza_rpt.cod_flusso,
            v_riscossioni_senza_rpt.fr_iur,
            v_riscossioni_senza_rpt.data_regolamento,
            v_riscossioni_senza_rpt.importo_totale_pagamenti,
            v_riscossioni_senza_rpt.numero_pagamenti,
            v_riscossioni_senza_rpt.importo_pagato,
            v_riscossioni_senza_rpt.data_pagamento,
            v_riscossioni_senza_rpt.cod_singolo_versamento_ente,
            v_riscossioni_senza_rpt.indice_dati,
            v_riscossioni_senza_rpt.cod_versamento_ente,
            v_riscossioni_senza_rpt.id_applicazione,
            v_riscossioni_senza_rpt.debitore_identificativo,
            v_riscossioni_senza_rpt.id_tipo_versamento,
            v_riscossioni_senza_rpt.cod_anno_tributario,
            v_riscossioni_senza_rpt.id_tributo
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
            v_riscossioni_con_rpt.data_pagamento,
            v_riscossioni_con_rpt.cod_singolo_versamento_ente,
            v_riscossioni_con_rpt.indice_dati,
            v_riscossioni_con_rpt.cod_versamento_ente,
            v_riscossioni_con_rpt.id_applicazione,
            v_riscossioni_con_rpt.debitore_identificativo,
            v_riscossioni_con_rpt.id_tipo_versamento,
            v_riscossioni_con_rpt.cod_anno_tributario,
            v_riscossioni_con_rpt.id_tributo
           FROM v_riscossioni_con_rpt) a
     JOIN applicazioni ON a.id_applicazione = applicazioni.id 
     JOIN tipi_versamento ON a.id_tipo_versamento = tipi_versamento.id 
     LEFT JOIN tributi ON a.id_tributo = tributi.id JOIN tipi_tributo 
     ON tributi.id_tipo_tributo = tipi_tributo.id;


-- 26/07/2019 Aggiunti campi per definire la visualizzazione di un tipo pendenza
ALTER TABLE tipi_versamento ADD COLUMN visualizzazione_definizione TEXT;
ALTER TABLE tipi_vers_domini ADD COLUMN visualizzazione_definizione TEXT;


-- 05/08/2019 modifica alla struttura della tabella delle configurazioni
ALTER TABLE configurazione ADD COLUMN nome VARCHAR(255);
ALTER TABLE configurazione RENAME giornale_eventi TO valore;
UPDATE configurazione SET nome = 'giornale_eventi';
ALTER TABLE configurazione ALTER COLUMN nome SET NOT NULL;
ALTER TABLE configurazione ADD CONSTRAINT unique_configurazione_1 UNIQUE (nome);

-- 05/08/2019 aggiunti nuovi campi alla tabella tracciati per gestire il formato csv
ALTER TABLE tracciati ADD COLUMN cod_tipo_versamento VARCHAR(35);
ALTER TABLE tracciati ADD COLUMN formato VARCHAR(10);
UPDATE tracciati SET formato = 'JSON';
ALTER TABLE tracciati ALTER COLUMN formato SET NOT NULL;


-- 06/08/2019 aggiunte colonne per la trasformazione del tracciato csv alla tabella tipo versamento
ALTER TABLE tipi_versamento ADD COLUMN trac_csv_header_risposta TEXT;
ALTER TABLE tipi_versamento ADD COLUMN trac_csv_template_richiesta TEXT;
ALTER TABLE tipi_versamento ADD COLUMN trac_csv_template_risposta TEXT;

ALTER TABLE tipi_vers_domini ADD COLUMN trac_csv_header_risposta TEXT;
ALTER TABLE tipi_vers_domini ADD COLUMN trac_csv_template_richiesta TEXT;
ALTER TABLE tipi_vers_domini ADD COLUMN trac_csv_template_risposta TEXT;

-- 27/08/2019 Vista Eventi per Versamenti
CREATE OR REPLACE VIEW v_eventi_vers AS (
        SELECT DISTINCT eventi.componente,
               eventi.ruolo,
               eventi.categoria_evento,
               eventi.tipo_evento,
               eventi.sottotipo_evento,
               eventi.data,
               eventi.intervallo,
               eventi.esito,
               eventi.sottotipo_esito,
               eventi.dettaglio_esito,
               eventi.parametri_richiesta,
               eventi.parametri_risposta,
               eventi.dati_pago_pa,
               coalesce(eventi.cod_versamento_ente, versamenti.cod_versamento_ente) as cod_versamento_ente,
               coalesce (eventi.cod_applicazione, applicazioni.cod_applicazione) as cod_applicazione,
               eventi.iuv,
               eventi.cod_dominio,
               eventi.ccp,
               eventi.id_sessione,
               eventi.id
               FROM eventi LEFT JOIN pagamenti_portale ON eventi.id_sessione = pagamenti_portale.id_sessione
               LEFT JOIN pag_port_versamenti ON pagamenti_portale.id = pag_port_versamenti.id_pagamento_portale
               LEFT JOIN versamenti ON versamenti.id = pag_port_versamenti.id_versamento
               LEFT JOIN applicazioni ON versamenti.id_applicazione = applicazioni.id
         );


-- 19/09/2019 Perfezionamento configurazione standard
UPDATE configurazione set valore = '{"apiEnte":{"letture":{"log":"MAI","dump":"MAI"},"scritture":{"log":"SEMPRE","dump":"SOLO_ERRORE"}},"apiPagamento":{"letture":{"log":"MAI","dump":"MAI"},"scritture":{"log":"SEMPRE","dump":"SOLO_ERRORE"}},"apiRagioneria":{"letture":{"log":"MAI","dump":"MAI"},"scritture":{"log":"SEMPRE","dump":"SOLO_ERRORE"}},"apiBackoffice":{"letture":{"log":"MAI","dump":"MAI"},"scritture":{"log":"SEMPRE","dump":"SEMPRE"}},"apiPagoPA":{"letture":{"log":"SEMRPE","dump":"SEMPRE"},"scritture":{"log":"SEMPRE","dump":"SEMPRE"}},"apiPendenze":{"letture":{"log":"MAI","dump":"MAI"},"scritture":{"log":"SEMPRE","dump":"SOLO_ERRORE"}}}' ;

