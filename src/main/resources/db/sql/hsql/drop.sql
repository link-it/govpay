-- Gli indici vengono eliminati automaticamente una volta eliminata la tabella
-- DROP INDEX index_stampe_1;
-- DROP INDEX index_batch_1;
-- DROP INDEX index_fr_1;
-- DROP INDEX index_incassi_1;
-- DROP INDEX index_iuv_1;
-- DROP INDEX index_rr_1;
-- DROP INDEX index_tipi_vers_domini_1;
-- DROP INDEX index_tipi_versamento_1;
-- DROP INDEX index_connettori_1;
-- DROP INDEX index_operatori_1;
-- DROP INDEX index_uo_1;
-- DROP INDEX index_tributi_1;
-- DROP INDEX index_tipi_tributo_1;
-- DROP INDEX index_iban_accredito_1;
-- DROP INDEX index_domini_1;
-- DROP INDEX index_applicazioni_2;
-- DROP INDEX index_applicazioni_1;
-- DROP INDEX index_utenze_1;
-- DROP INDEX index_stazioni_1;
-- DROP INDEX index_intermediari_1;
-- DROP INDEX index_configurazione_1;
-- DROP INDEX idx_evt_iuv;
-- DROP INDEX idx_evt_id_sessione;
-- DROP INDEX idx_evt_fk_vrs;
-- DROP INDEX idx_evt_data;
-- DROP INDEX idx_pag_fk_sng;
-- DROP INDEX idx_pag_fk_rpt;
-- DROP INDEX idx_iuv_rifversamento;
-- DROP INDEX idx_nai_da_spedire;
-- DROP INDEX idx_ntf_da_spedire;
-- DROP INDEX idx_rpt_fk_prt;
-- DROP INDEX idx_rpt_fk_vrs;
-- DROP INDEX idx_rpt_stato;
-- DROP INDEX idx_rpt_cod_msg_richiesta;
-- DROP INDEX idx_ppv_fk_vrs;
-- DROP INDEX idx_ppv_fk_prt;
-- DROP INDEX idx_prt_versante_identif;
-- DROP INDEX idx_prt_id_sessione_psp;
-- DROP INDEX idx_prt_id_sessione;
-- DROP INDEX idx_prt_stato;
-- DROP INDEX idx_vrs_avv_io_prom_scad;
-- DROP INDEX idx_vrs_avv_mail_prom_scad;
-- DROP INDEX idx_vrs_prom_avviso;
-- DROP INDEX idx_vrs_auth;
-- DROP INDEX idx_vrs_iuv;
-- DROP INDEX idx_vrs_deb_identificativo;
-- DROP INDEX idx_vrs_stato_vrs;
-- DROP INDEX idx_vrs_data_creaz;
-- DROP INDEX idx_vrs_id_pendenza;
DROP VIEW versamenti_incassi;
DROP VIEW v_eventi_vers;
DROP VIEW v_eventi_vers_pagamenti;       
DROP VIEW v_eventi_vers_rendicontazioni; 
DROP VIEW v_eventi_vers_riconciliazioni;
DROP VIEW v_eventi_vers_tracciati;
DROP VIEW v_pagamenti_portale;      
DROP VIEW v_riscossioni;
DROP VIEW v_riscossioni_con_rpt;
DROP VIEW v_riscossioni_senza_rpt;
DROP VIEW v_rendicontazioni_ext;
DROP VIEW v_rpt_versamenti;
DROP VIEW v_pagamenti;
DROP VIEW v_versamenti;
DROP TABLE sonde;
DROP TABLE allegati_init_seq;
DROP TABLE allegati;
DROP TABLE ID_MESSAGGIO_RELATIVO;
DROP TABLE gp_audit_init_seq;
DROP TABLE gp_audit;
DROP TABLE operazioni_init_seq;
DROP TABLE operazioni;
DROP TABLE stampe_init_seq;
DROP TABLE stampe;
DROP TABLE batch_init_seq;
DROP TABLE batch;
DROP TABLE eventi_init_seq;
DROP TABLE eventi;
DROP TABLE rendicontazioni_init_seq;
DROP TABLE rendicontazioni;
DROP TABLE pagamenti_init_seq;
DROP TABLE pagamenti;
DROP TABLE fr_init_seq;
DROP TABLE fr;
DROP TABLE incassi_init_seq;
DROP TABLE incassi;
DROP TABLE iuv_init_seq;
DROP TABLE iuv;
DROP TABLE promemoria_init_seq;
DROP TABLE promemoria;
DROP TABLE notifiche_app_io_init_seq;
DROP TABLE notifiche_app_io;
DROP TABLE notifiche_init_seq;
DROP TABLE notifiche;
DROP TABLE rr_init_seq;
DROP TABLE rr;
DROP TABLE rpt_init_seq;
DROP TABLE rpt;
DROP TABLE trac_notif_pag_init_seq;
DROP TABLE trac_notif_pag;
DROP TABLE pag_port_versamenti_init_seq;
DROP TABLE pag_port_versamenti;
DROP TABLE pagamenti_portale_init_seq;
DROP TABLE pagamenti_portale;
DROP TABLE singoli_versamenti_init_seq;
DROP TABLE singoli_versamenti;
DROP TABLE versamenti_init_seq;
DROP TABLE versamenti;
DROP TABLE documenti_init_seq;
DROP TABLE documenti;
DROP TABLE utenze_tipo_vers_init_seq;
DROP TABLE utenze_tipo_vers;
DROP TABLE tipi_vers_domini_init_seq;
DROP TABLE tipi_vers_domini;
DROP TABLE tipi_versamento_init_seq;
DROP TABLE tipi_versamento;
DROP TABLE tracciati_init_seq;
DROP TABLE tracciati;
DROP TABLE acl_init_seq;
DROP TABLE acl;
DROP TABLE connettori_init_seq;
DROP TABLE connettori;
DROP TABLE operatori_init_seq;
DROP TABLE operatori;
DROP TABLE utenze_domini_init_seq;
DROP TABLE utenze_domini;
DROP TABLE uo_init_seq;
DROP TABLE uo;
DROP TABLE tributi_init_seq;
DROP TABLE tributi;
DROP TABLE tipi_tributo_init_seq;
DROP TABLE tipi_tributo;
DROP TABLE iban_accredito_init_seq;
DROP TABLE iban_accredito;
DROP TABLE domini_init_seq;
DROP TABLE domini;
DROP TABLE applicazioni_init_seq;
DROP TABLE applicazioni;
DROP TABLE utenze_init_seq;
DROP TABLE utenze;
DROP TABLE stazioni_init_seq;
DROP TABLE stazioni;
DROP TABLE intermediari_init_seq;
DROP TABLE intermediari;
DROP TABLE configurazione_init_seq;
DROP TABLE configurazione;
DROP SEQUENCE seq_allegati;
DROP SEQUENCE seq_gp_audit;
DROP SEQUENCE seq_operazioni;
DROP SEQUENCE seq_stampe;
DROP SEQUENCE seq_batch;
DROP SEQUENCE seq_eventi;
DROP SEQUENCE seq_rendicontazioni;
DROP SEQUENCE seq_pagamenti;
DROP SEQUENCE seq_fr;
DROP SEQUENCE seq_incassi;
DROP SEQUENCE seq_iuv;
DROP SEQUENCE seq_promemoria;
DROP SEQUENCE seq_notifiche_app_io;
DROP SEQUENCE seq_notifiche;
DROP SEQUENCE seq_rr;
DROP SEQUENCE seq_rpt;
DROP SEQUENCE seq_trac_notif_pag;
DROP SEQUENCE seq_pag_port_versamenti;
DROP SEQUENCE seq_pagamenti_portale;
DROP SEQUENCE seq_singoli_versamenti;
DROP SEQUENCE seq_versamenti;
DROP SEQUENCE seq_documenti;
DROP SEQUENCE seq_utenze_tipo_vers;
DROP SEQUENCE seq_tipi_vers_domini;
DROP SEQUENCE seq_tipi_versamento;
DROP SEQUENCE seq_tracciati;
DROP SEQUENCE seq_acl;
DROP SEQUENCE seq_connettori;
DROP SEQUENCE seq_operatori;
DROP SEQUENCE seq_utenze_domini;
DROP SEQUENCE seq_uo;
DROP SEQUENCE seq_tributi;
DROP SEQUENCE seq_tipi_tributo;
DROP SEQUENCE seq_iban_accredito;
DROP SEQUENCE seq_domini;
DROP SEQUENCE seq_applicazioni;
DROP SEQUENCE seq_utenze;
DROP SEQUENCE seq_stazioni;
DROP SEQUENCE seq_intermediari;
DROP SEQUENCE seq_configurazione;