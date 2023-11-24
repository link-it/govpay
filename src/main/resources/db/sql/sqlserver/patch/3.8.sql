-- 24/07/2023 Aggiunto indice mancante sulla tabella RPT
CREATE INDEX idx_rpt_ric_pend_scad ON rpt (cod_dominio,versione,data_msg_richiesta);


-- 12/05/2023 Aggiunti indici mancanti alla tabella eventi
CREATE INDEX idx_evt_fk_inc ON eventi (id_incasso);
CREATE INDEX idx_evt_fk_trac ON eventi (id_tracciato);