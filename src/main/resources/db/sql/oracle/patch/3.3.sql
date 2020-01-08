-- 18/12/2019 Eliminate colonne dati WISP dalla tabella PagamentiPortale.

DROP VIEW v_pagamenti_portale_ext;

ALTER TABLE pagamenti_portale DROP COLUMN wisp_id_dominio;
ALTER TABLE pagamenti_portale DROP COLUMN wisp_key_pa;
ALTER TABLE pagamenti_portale DROP COLUMN wisp_key_wisp;
ALTER TABLE pagamenti_portale DROP COLUMN wisp_html;

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

-- 08/01/2020 Aggiunte Colonne per la configurazione delle notifiche con AppIO nei TipiVersamento e TipiVersamentoDominio

ALTER TABLE tipi_versamento ADD app_io_tipo VARCHAR2(35 CHAR);
ALTER TABLE tipi_versamento ADD app_io_template_messaggio CLOB;
ALTER TABLE tipi_versamento ADD app_io_template_oggetto CLOB;

ALTER TABLE tipi_vers_domini ADD app_io_abilitato NUMBER;
ALTER TABLE tipi_vers_domini SET app_io_abilitato = 0;
ALTER TABLE tipi_vers_domini MODIFY (app_io_abilitato DEFAULT 0);
ALTER TABLE tipi_vers_domini MODIFY (app_io_abilitato NOT NULL);

ALTER TABLE tipi_vers_domini ADD app_io_api_key VARCHAR2(255 CHAR);
ALTER TABLE tipi_vers_domini ADD app_io_tipo VARCHAR2(35 CHAR);
ALTER TABLE tipi_vers_domini ADD app_io_template_messaggio CLOB;
ALTER TABLE tipi_vers_domini ADD app_io_template_oggetto CLOB;


