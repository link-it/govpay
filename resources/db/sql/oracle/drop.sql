-- Gli indici vengono eliminati automaticamente una volta eliminata la tabella
-- DROP INDEX index_iuv_1 CASCADE;
DROP TRIGGER trg_gp_audit;
DROP TRIGGER trg_operazioni;
DROP TRIGGER trg_tracciati;
DROP TRIGGER trg_batch;
DROP TRIGGER trg_rendicontazioni CASCADE;
DROP TRIGGER trg_eventi CASCADE;
DROP TRIGGER trg_pagamenti CASCADE;
DROP TRIGGER trg_fr CASCADE;
DROP TRIGGER trg_iuv CASCADE;
DROP TRIGGER trg_notifiche CASCADE;
DROP TRIGGER trg_rr CASCADE;
DROP TRIGGER trg_rpt CASCADE;
DROP TRIGGER trg_singoli_versamenti CASCADE;
DROP TRIGGER trg_versamenti CASCADE;
DROP TRIGGER trg_acl CASCADE;
DROP TRIGGER trg_tributi CASCADE;
DROP TRIGGER trg_tipi_tributo CASCADE;
DROP TRIGGER trg_iban_accredito CASCADE;
DROP TRIGGER trg_portali CASCADE;
DROP TRIGGER trg_connettori CASCADE;
DROP TRIGGER trg_operatori CASCADE;
DROP TRIGGER trg_uo CASCADE;
DROP TRIGGER trg_domini CASCADE;
DROP TRIGGER trg_applicazioni CASCADE;
DROP TRIGGER trg_stazioni CASCADE;
DROP TRIGGER trg_intermediari CASCADE;
DROP TRIGGER trg_canali CASCADE;
DROP TRIGGER trg_psp CASCADE;
DROP TABLE ID_MESSAGGIO_RELATIVO CASCADE;
DROP TABLE gp_audit CASCADE;
DROP TABLE operazioni CASCADE;
DROP TABLE tracciati CASCADE;
DROP TABLE batch CASCADE;
DROP TABLE rendicontazioni CASCADE;
DROP TABLE eventi CASCADE;
DROP TABLE pagamenti CASCADE;
DROP TABLE incassi CASCADE;
DROP TABLE fr CASCADE;
DROP TABLE iuv CASCADE;
DROP TABLE notifiche CASCADE;
DROP TABLE rr CASCADE;
DROP TABLE rpt CASCADE;
DROP TABLE singoli_versamenti CASCADE;
DROP TABLE versamenti CASCADE;
DROP TABLE acl CASCADE;
DROP TABLE tributi CASCADE;
DROP TABLE tipi_tributo CASCADE;
DROP TABLE iban_accredito CASCADE;
DROP TABLE portali CASCADE;
DROP TABLE connettori CASCADE;
DROP TABLE operatori CASCADE;
DROP TABLE uo CASCADE;
DROP TABLE domini CASCADE;
DROP TABLE applicazioni CASCADE;
DROP TABLE stazioni CASCADE;
DROP TABLE intermediari CASCADE;
DROP TABLE canali CASCADE;
DROP TABLE psp CASCADE;
DROP SEQUENCE seq_rendicontazioni_senza_rpt CASCADE;
DROP SEQUENCE seq_eventi CASCADE;
DROP SEQUENCE seq_pagamenti CASCADE;
DROP SEQUENCE seq_fr_applicazioni CASCADE;
DROP SEQUENCE seq_fr CASCADE;
DROP SEQUENCE seq_iuv CASCADE;
DROP SEQUENCE seq_notifiche CASCADE;
DROP SEQUENCE seq_rr CASCADE;
DROP SEQUENCE seq_rpt CASCADE;
DROP SEQUENCE seq_singoli_versamenti CASCADE;
DROP SEQUENCE seq_versamenti CASCADE;
DROP SEQUENCE seq_acl CASCADE;
DROP SEQUENCE seq_tributi CASCADE;
DROP SEQUENCE seq_tipi_tributo CASCADE;
DROP SEQUENCE seq_iban_accredito CASCADE;
DROP SEQUENCE seq_portali CASCADE;
DROP SEQUENCE seq_connettori CASCADE;
DROP SEQUENCE seq_operatori CASCADE;
DROP SEQUENCE seq_uo CASCADE;
DROP SEQUENCE seq_domini CASCADE;
DROP SEQUENCE seq_applicazioni CASCADE;
DROP SEQUENCE seq_stazioni CASCADE;
DROP SEQUENCE seq_intermediari CASCADE;
DROP SEQUENCE seq_canali CASCADE;
DROP SEQUENCE seq_psp CASCADE;