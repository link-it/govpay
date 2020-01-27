ALTER TABLE batch MODIFY nodo VARCHAR(255);

DROP VIEW v_riscossioni;

CREATE VIEW v_riscossioni AS (
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
     LEFT JOIN tipi_versamento ON a.id_tipo_versamento = tipi_versamento.id
     LEFT JOIN tributi ON a.id_tributo = tributi.id
     LEFT JOIN tipi_tributo ON tributi.id_tipo_tributo = tipi_tributo.id);


-- 18/12/2019 Aggiornamento della vista pagamenti portale

DROP VIEW v_pagamenti_portale_ext;

CREATE VIEW v_pag_portale_base AS
 SELECT DISTINCT
  pagamenti_portale.id,
  versamenti.debitore_identificativo as debitore_identificativo,
  versamenti.id_dominio as id_dominio,
  versamenti.id_uo as id_uo,
  versamenti.id_tipo_versamento as id_tipo_versamento
FROM pagamenti_portale
JOIN pag_port_versamenti ON pagamenti_portale.id = pag_port_versamenti.id_pagamento_portale
JOIN versamenti ON versamenti.id=pag_port_versamenti.id_versamento;

CREATE VIEW v_pagamenti_portale_ext AS
 SELECT
  pagamenti_portale.cod_canale,
  pagamenti_portale.nome,
  pagamenti_portale.importo,
  pagamenti_portale.versante_identificativo,
  pagamenti_portale.id_sessione,
  pagamenti_portale.id_sessione_portale,
  pagamenti_portale.id_sessione_psp,
  pagamenti_portale.stato,
  pagamenti_portale.codice_stato,
  pagamenti_portale.descrizione_stato,
  pagamenti_portale.psp_redirect_url,
  pagamenti_portale.psp_esito,
  pagamenti_portale.json_request,
  pagamenti_portale.wisp_id_dominio,
  pagamenti_portale.wisp_key_pa,
  pagamenti_portale.wisp_key_wisp,
  pagamenti_portale.wisp_html,
  pagamenti_portale.data_richiesta,
  pagamenti_portale.url_ritorno,
  pagamenti_portale.cod_psp,
  pagamenti_portale.tipo_versamento,
  pagamenti_portale.multi_beneficiario,
  pagamenti_portale.ack,
  pagamenti_portale.tipo,
  pagamenti_portale.principal,
  pagamenti_portale.tipo_utenza,
  pagamenti_portale.id,
  pagamenti_portale.id_applicazione,
  v_pag_portale_base.debitore_identificativo,
  v_pag_portale_base.id_dominio,
  v_pag_portale_base.id_uo,
  v_pag_portale_base.id_tipo_versamento
FROM v_pag_portale_base JOIN pagamenti_portale ON v_pag_portale_base.id = pagamenti_portale.id;

-- 19/12/2019 Miglioramento performance accesso alla lista pendenze

DROP VIEW versamenti_incassi;

ALTER TABLE versamenti ADD COLUMN data_pagamento TIMESTAMP(3);
UPDATE versamenti SET data_pagamento = (SELECT MAX(pagamenti.data_pagamento) FROM pagamenti JOIN singoli_versamenti ON pagamenti.id_singolo_versamento = singoli_versamenti.id WHERE singoli_versamenti.id_versamento = versamenti.id);

ALTER TABLE versamenti ADD COLUMN importo_pagato DOUBLE;
UPDATE versamenti SET importo_pagato = 0;
UPDATE versamenti SET importo_pagato = (SELECT SUM(CASE WHEN pagamenti.importo_pagato IS NOT NULL THEN pagamenti.importo_pagato ELSE 0 END) FROM pagamenti JOIN singoli_versamenti ON pagamenti.id_singolo_versamento = singoli_versamenti.id WHERE singoli_versamenti.id_versamento = versamenti.id) WHERE versamenti.stato_versamento = 'ESEGUITO';
ALTER TABLE versamenti MODIFY COLUMN importo_pagato DOUBLE NOT NULL;

ALTER TABLE versamenti ADD COLUMN importo_incassato DOUBLE;
UPDATE versamenti SET importo_incassato = 0;
UPDATE versamenti SET importo_incassato = (SELECT SUM(CASE WHEN pagamenti.stato = 'INCASSATO' THEN pagamenti.importo_pagato ELSE 0 END) FROM pagamenti JOIN singoli_versamenti ON pagamenti.id_singolo_versamento = singoli_versamenti.id WHERE singoli_versamenti.id_versamento = versamenti.id) WHERE versamenti.stato_versamento = 'ESEGUITO';
ALTER TABLE versamenti MODIFY COLUMN importo_incassato DOUBLE NOT NULL;

ALTER TABLE versamenti ADD COLUMN stato_pagamento VARCHAR(35);
UPDATE versamenti SET stato_pagamento = 'NON_PAGATO';
UPDATE versamenti SET stato_pagamento= (SELECT MAX(CASE  WHEN pagamenti.stato IS NULL THEN 'NON_PAGATO' WHEN pagamenti.stato = 'INCASSATO' THEN 'INCASSATO' ELSE 'PAGATO' END) FROM pagamenti JOIN singoli_versamenti ON pagamenti.id_singolo_versamento = singoli_versamenti.id WHERE singoli_versamenti.id_versamento = versamenti.id) WHERE versamenti.stato_versamento = 'ESEGUITO';
ALTER TABLE versamenti MODIFY COLUMN stato_pagamento VARCHAR(35) NOT NULL;

ALTER TABLE versamenti ADD COLUMN iuv_pagamento VARCHAR(35);
UPDATE versamenti SET iuv_pagamento = (SELECT MAX(pagamenti.iuv) FROM pagamenti JOIN singoli_versamenti ON pagamenti.id_singolo_versamento = singoli_versamenti.id WHERE singoli_versamenti.id_versamento = versamenti.id);

CREATE VIEW versamenti_incassi AS SELECT
    versamenti.id,
    versamenti.cod_versamento_ente,
    versamenti.nome,
    versamenti.importo_totale,
    versamenti.stato_versamento,
    versamenti.descrizione_stato,
    versamenti.aggiornabile,
    versamenti.data_creazione,
    versamenti.data_validita,
    versamenti.data_scadenza,
    versamenti.data_ora_ultimo_aggiornamento,
    versamenti.causale_versamento,
    versamenti.debitore_tipo,
    versamenti.debitore_identificativo,
    versamenti.debitore_anagrafica,
    versamenti.debitore_indirizzo,
    versamenti.debitore_civico,
    versamenti.debitore_cap,
    versamenti.debitore_localita,
    versamenti.debitore_provincia,
    versamenti.debitore_nazione,
    versamenti.debitore_email,
    versamenti.debitore_telefono,
    versamenti.debitore_cellulare,
    versamenti.debitore_fax,
    versamenti.tassonomia_avviso,
    versamenti.tassonomia,
    versamenti.cod_lotto,
    versamenti.cod_versamento_lotto,
    versamenti.cod_anno_tributario,
    versamenti.cod_bundlekey,
    versamenti.dati_allegati,
    versamenti.incasso,
    versamenti.anomalie,
    versamenti.iuv_versamento,
    versamenti.numero_avviso,
    versamenti.id_dominio,
    versamenti.id_tipo_versamento,
    versamenti.id_tipo_versamento_dominio,
    versamenti.id_uo,
    versamenti.id_applicazione,
    versamenti.avvisatura_abilitata,
    versamenti.avvisatura_da_inviare,
    versamenti.avvisatura_operazione,
    versamenti.avvisatura_modalita,
    versamenti.avvisatura_tipo_pagamento,
    versamenti.avvisatura_cod_avvisatura,
    versamenti.divisione,
    versamenti.direzione,	
    versamenti.id_tracciato,
    versamenti.ack,
    versamenti.anomalo,
    versamenti.data_pagamento,
    versamenti.importo_pagato,
    versamenti.importo_incassato,
    versamenti.stato_pagamento,
    versamenti.iuv_pagamento,
    (CASE WHEN versamenti.stato_versamento = 'NON_ESEGUITO' AND versamenti.data_validita > now() THEN 0 ELSE 1 END) AS smart_order_rank,
    (ABS((UNIX_TIMESTAMP(now()) *1000) - (UNIX_TIMESTAMP(COALESCE(pagamenti.data_pagamento, versamenti.data_validita, versamenti.data_creazione)) * 1000))) AS smart_order_date
    FROM versamenti JOIN tipi_versamento ON tipi_versamento.id = versamenti.id_tipo_versamento;

-- 14/01/2020 Indici sulla tabella dei versamenti
CREATE INDEX idx_tipi_versamento_tipo ON tipi_versamento (tipo);

CREATE INDEX idx_vrs_data_creaz ON versamenti (data_creazione DESC);
CREATE INDEX idx_vrs_stato_vrs ON versamenti (stato_versamento);
CREATE INDEX idx_vrs_deb_identificativo ON versamenti (debitore_identificativo);

-- 15/01/2020 Indici sulla tabella delle notifiche
CREATE INDEX idx_ntf_da_spedire ON notifiche (stato,data_prossima_spedizione DESC);

-- 22/01/2020 Aggiornamento indice tabella notifiche;
DROP INDEX idx_ntf_da_spedire;
CREATE INDEX idx_ntf_da_spedire ON notifiche (id_applicazione,stato,data_prossima_spedizione);

-- 22/01/2020 Nuovi indici tabelle monitoraggio
CREATE INDEX idx_vrs_numero_avviso ON versamenti (numero_avviso);
CREATE INDEX idx_vrs_auth ON versamenti (id_dominio,id_tipo_versamento,id_uo);

CREATE INDEX idx_prt_stato ON pagamenti_portale (stato);
DROP INDEX index_pagamenti_portale_1;
ALTER TABLE pagamenti_portale DROP CONSTRAINT unique_pagamenti_portale_1;
CREATE INDEX idx_prt_id_sessione ON pagamenti_portale (id_sessione);

CREATE INDEX idx_ppv_fk_prt ON pag_port_versamenti (id_pagamento_portale);
CREATE INDEX idx_ppv_fk_vrs ON pag_port_versamenti (id_versamento);

CREATE INDEX idx_rpt_fk_prt ON rpt (id_pagamento_portale);

DROP INDEX index_iuv_2;
CREATE INDEX idx_iuv_rifversamento ON iuv (cod_versamento_ente,id_applicazione,tipo_iuv);

CREATE INDEX idx_pag_fk_rpt ON pagamenti (id_rpt);
CREATE INDEX idx_pag_fk_sng ON pagamenti (id_singolo_versamento);

CREATE INDEX idx_evt_data ON eventi (data);
CREATE INDEX idx_evt_fk_vrs ON eventi (cod_applicazione,cod_versamento_ente);
CREATE INDEX idx_evt_id_sessione ON eventi (id_sessione);

-- 23/01/2020 Correzione della vista pagamenti portale per incrementare le performance di lettura.
DROP VIEW v_pagamenti_portale_ext;
DROP VIEW v_pag_portale_base;

CREATE VIEW v_pagamenti_portale AS
 SELECT 
  pagamenti_portale.cod_canale,
  pagamenti_portale.nome,
  pagamenti_portale.importo,
  pagamenti_portale.versante_identificativo,
  pagamenti_portale.id_sessione,
  pagamenti_portale.id_sessione_portale,
  pagamenti_portale.id_sessione_psp,
  pagamenti_portale.stato,
  pagamenti_portale.codice_stato,
  pagamenti_portale.descrizione_stato,
  pagamenti_portale.psp_redirect_url,
  pagamenti_portale.psp_esito,
  pagamenti_portale.data_richiesta,
  pagamenti_portale.url_ritorno,
  pagamenti_portale.cod_psp,
  pagamenti_portale.tipo_versamento,
  pagamenti_portale.multi_beneficiario,
  pagamenti_portale.ack,
  pagamenti_portale.tipo,
  pagamenti_portale.principal,
  pagamenti_portale.tipo_utenza,
  pagamenti_portale.id,
  pagamenti_portale.id_applicazione,
  versamenti.debitore_identificativo as debitore_identificativo,
  versamenti.id_dominio as id_dominio, 
  versamenti.id_uo as id_uo, 
  versamenti.id_tipo_versamento as id_tipo_versamento
FROM pagamenti_portale 
JOIN pag_port_versamenti ON pagamenti_portale.id = pag_port_versamenti.id_pagamento_portale 
JOIN versamenti ON versamenti.id=pag_port_versamenti.id_versamento;

-- 24/01/2020
DROP INDEX index_versamenti_1;
ALTER TABLE versamenti DROP CONSTRAINT unique_versamenti_1;
CREATE INDEX idx_vrs_id_pendenza ON versamenti (cod_versamento_ente,id_applicazione);

ALTER TABLE versamenti DROP CONSTRAINT fk_vrs_id_applicazione;
ALTER TABLE versamenti DROP CONSTRAINT fk_vrs_id_dominio;
ALTER TABLE versamenti DROP CONSTRAINT fk_vrs_id_tipo_versamento_dominio;
ALTER TABLE versamenti DROP CONSTRAINT fk_vrs_id_tipo_versamento;
ALTER TABLE versamenti DROP CONSTRAINT fk_vrs_id_tracciato;
ALTER TABLE versamenti DROP CONSTRAINT fk_vrs_id_uo;

DROP INDEX index_singoli_versamenti_1;
ALTER TABLE singoli_versamenti DROP CONSTRAINT unique_singoli_versamenti_1;
CREATE INDEX idx_sng_id_voce ON singoli_versamenti (id_versamento,cod_singolo_versamento_ente,indice_dati);

ALTER TABLE singoli_versamenti DROP CONSTRAINT fk_sng_id_iban_accredito;
ALTER TABLE singoli_versamenti DROP CONSTRAINT fk_sng_id_iban_accredito;
ALTER TABLE singoli_versamenti DROP CONSTRAINT fk_sng_id_iban_appoggio;
ALTER TABLE singoli_versamenti DROP CONSTRAINT fk_sng_id_tributo;
ALTER TABLE singoli_versamenti DROP CONSTRAINT fk_sng_id_versamento;

DROP INDEX index_rpt_1;
DROP INDEX index_rpt_2;
ALTER TABLE rpt DROP CONSTRAINT unique_rpt_1;
ALTER TABLE rpt DROP CONSTRAINT unique_rpt_2;
CREATE INDEX idx_rpt_cod_msg_richiesta ON rpt (cod_msg_richiesta);
CREATE INDEX idx_rpt_id_transazione ON rpt (iuv,ccp,cod_dominio);

ALTER TABLE rpt DROP CONSTRAINT fk_rpt_id_pagamento_portale;
ALTER TABLE rpt DROP CONSTRAINT fk_rpt_id_versamento;

DROP INDEX index_pagamenti_1;
ALTER TABLE pagamenti DROP CONSTRAINT unique_pagamenti_1;
CREATE INDEX idx_pag_id_riscossione ON pagamenti (cod_dominio,iuv,iur,indice_dati);

ALTER TABLE pagamenti DROP CONSTRAINT fk_pag_id_incasso;
ALTER TABLE pagamenti DROP CONSTRAINT fk_pag_id_rpt;
ALTER TABLE pagamenti DROP CONSTRAINT fk_pag_id_rr;
ALTER TABLE pagamenti DROP CONSTRAINT fk_pag_id_singolo_versamento;

ALTER TABLE pagamenti_portale DROP CONSTRAINT fk_ppt_id_applicazione;

ALTER TABLE pag_port_versamenti DROP CONSTRAINT fk_ppv_id_pagamento_portale;
ALTER TABLE pag_port_versamenti DROP CONSTRAINT fk_ppv_id_versamento;

-- 24/01/2020 Personalizzazione testo libero della causaleVersamento
ALTER TABLE singoli_versamenti ADD COLUMN descrizione_causale_rpt VARCHAR(140);


