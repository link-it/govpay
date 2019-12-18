-- 18/12/2019 Eliminate colonne dati WISP dalla tabella PagamentiPortale.

DROP VIEW v_pagamenti_portale_ext;

ALTER TABLE pagamenti_portale DROP COLUMN wisp_id_dominio;
ALTER TABLE pagamenti_portale DROP COLUMN wisp_key_pa;
ALTER TABLE pagamenti_portale DROP COLUMN wisp_key_wisp;
ALTER TABLE pagamenti_portale DROP COLUMN wisp_html;
