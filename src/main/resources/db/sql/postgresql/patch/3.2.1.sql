-- 30/03/2020 Colonne di ricerca upper case per versamenti e pagamenti portale

ALTER TABLE versamenti ADD column src_iuv VARCHAR(35);
UPDATE versamenti SET src_iuv = UPPER(COALESCE(iuv_pagamento, iuv_versamento));

ALTER TABLE versamenti ADD column src_debitore_identificativo VARCHAR(35);
UPDATE versamenti SET src_debitore_identificativo = UPPER(debitore_identificativo);
ALTER TABLE versamenti ALTER COLUMN src_debitore_identificativo SET NOT NULL;

DROP INDEX idx_vrs_deb_identificativo;
DROP INDEX idx_vrs_numero_avviso;
CREATE INDEX idx_vrs_deb_identificativo ON versamenti (src_debitore_identificativo);
CREATE INDEX idx_vrs_iuv ON versamenti (src_iuv);

ALTER TABLE pagamenti_portale ADD column src_versante_identificativo VARCHAR(35);
UPDATE pagamenti_portale SET src_versante_identificativo = UPPER(versante_identificativo);

CREATE INDEX idx_prt_id_sessione_psp ON pagamenti_portale (id_sessione_psp);
CREATE INDEX idx_prt_versante_identif ON pagamenti_portale (src_versante_identificativo);

-- Aggiornamento Viste

DROP VIEW versamenti_incassi;
CREATE VIEW versamenti_incassi AS 
SELECT versamenti.id,
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
    versamenti.id_sessione,
    versamenti.ack,
    versamenti.anomalo,
    versamenti.data_pagamento,
    versamenti.importo_pagato,
    versamenti.importo_incassato,
    versamenti.stato_pagamento,
    versamenti.iuv_pagamento,
    versamenti.src_iuv,
    versamenti.src_debitore_identificativo,
    (CASE WHEN versamenti.stato_versamento = 'NON_ESEGUITO' AND versamenti.data_validita > now() THEN 0 ELSE 1 END) AS smart_order_rank,
    (@ (date_part('epoch'::text, now()) * 1000::bigint - date_part('epoch'::text, COALESCE(versamenti.data_pagamento, versamenti.data_validita, versamenti.data_creazione)) * 1000::bigint))::bigint AS smart_order_date
   FROM versamenti JOIN tipi_versamento ON tipi_versamento.id = versamenti.id_tipo_versamento;

DROP VIEW v_pagamenti_portale;
CREATE VIEW v_pagamenti_portale AS
 SELECT 
  pagamenti_portale.cod_canale,
  pagamenti_portale.nome,
  pagamenti_portale.importo,
  pagamenti_portale.versante_identificativo,
  pagamenti_portale.src_versante_identificativo,
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
  versamenti.src_debitore_identificativo as src_debitore_identificativo,
  versamenti.id_dominio as id_dominio, 
  versamenti.id_uo as id_uo, 
  versamenti.id_tipo_versamento as id_tipo_versamento
FROM pagamenti_portale 
JOIN pag_port_versamenti ON pagamenti_portale.id = pag_port_versamenti.id_pagamento_portale 
JOIN versamenti ON versamenti.id=pag_port_versamenti.id_versamento;




