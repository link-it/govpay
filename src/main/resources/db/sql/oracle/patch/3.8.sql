-- 24/07/2023 Aggiunto indice mancante sulla tabella RPT
CREATE INDEX idx_rpt_ric_pend_scad ON rpt (cod_dominio,versione,data_msg_richiesta);


-- 12/05/2023 Aggiunti indici mancanti alla tabella eventi
CREATE INDEX idx_evt_fk_inc ON eventi (id_incasso);
CREATE INDEX idx_evt_fk_trac ON eventi (id_tracciato);


-- 01/02/2024 Aggiunti indici mancanti alle tabelle versamenti e rendicontazioni
create index idx_vrs_iuv_dominio on versamenti(iuv_versamento, id_dominio);
create index idx_rnd_fk_singoli_versamenti on rendicontazioni(id_singolo_versamento);
create index idx_rnd_fk_pagamenti on rendicontazioni(id_pagamento);


