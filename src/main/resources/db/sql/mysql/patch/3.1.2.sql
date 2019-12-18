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

