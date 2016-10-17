--GP-341
ALTER TABLE pagamenti ADD iban_accredito VARCHAR2(255 CHAR);
--GP-351
ALTER TABLE rpt ADD cod_sessione_portale VARCHAR2(255 CHAR);
--GP-169
ALTER TABLE versamenti DROP CONSTRAINT fk_versamenti_1;
ALTER TABLE versamenti DROP CONSTRAINT fk_versamenti_2;
ALTER TABLE singoli_versamenti DROP CONSTRAINT fk_singoli_versamenti_1;
ALTER TABLE singoli_versamenti DROP CONSTRAINT fk_singoli_versamenti_2;
ALTER TABLE singoli_versamenti DROP CONSTRAINT fk_singoli_versamenti_3;
ALTER TABLE rpt DROP CONSTRAINT fk_rpt_1;
ALTER TABLE rpt DROP CONSTRAINT fk_rpt_2;
ALTER TABLE rpt DROP CONSTRAINT fk_rpt_3;
ALTER TABLE notifiche DROP CONSTRAINT fk_notifiche_1;
ALTER TABLE notifiche DROP CONSTRAINT fk_notifiche_2;
ALTER TABLE notifiche DROP CONSTRAINT fk_notifiche_3;
ALTER TABLE iuv DROP CONSTRAINT fk_iuv_1;
ALTER TABLE iuv DROP CONSTRAINT fk_iuv_2;

CREATE INDEX index_iuv_1 ON iuv (cod_versamento_ente,tipo_iuv,id_applicazione);

-- FIX VARCHAR AS BYTE

ALTER TABLE psp MODIFY cod_psp VARCHAR2(35 CHAR);
ALTER TABLE psp MODIFY ragione_sociale VARCHAR2(70 CHAR);
ALTER TABLE psp MODIFY url_info VARCHAR2(255 CHAR);

ALTER TABLE canali MODIFY cod_canale VARCHAR2(35 CHAR);
ALTER TABLE canali MODIFY cod_intermediario VARCHAR2(35 CHAR);
ALTER TABLE canali MODIFY tipo_versamento VARCHAR2(4 CHAR);
ALTER TABLE canali MODIFY condizioni VARCHAR2(35 CHAR);
ALTER TABLE canali MODIFY url_info VARCHAR2(255 CHAR);

ALTER TABLE intermediari MODIFY cod_intermediario VARCHAR2(35 CHAR);
ALTER TABLE intermediari MODIFY cod_connettore_pdd VARCHAR2(35 CHAR);
ALTER TABLE intermediari MODIFY denominazione VARCHAR2(255 CHAR);

ALTER TABLE stazioni MODIFY cod_stazione VARCHAR2(35 CHAR);
ALTER TABLE stazioni MODIFY password VARCHAR2(35 CHAR);

ALTER TABLE applicazioni MODIFY cod_applicazione VARCHAR2(35 CHAR);
ALTER TABLE applicazioni MODIFY principal VARCHAR2(35 CHAR);
ALTER TABLE applicazioni MODIFY firma_ricevuta VARCHAR2(1 CHAR);
ALTER TABLE applicazioni MODIFY cod_connettore_esito VARCHAR2(255 CHAR);
ALTER TABLE applicazioni MODIFY cod_connettore_verifica VARCHAR2(255 CHAR);
ALTER TABLE applicazioni MODIFY versione VARCHAR2(10 CHAR);

ALTER TABLE domini MODIFY cod_dominio VARCHAR2(35 CHAR);
ALTER TABLE domini MODIFY gln VARCHAR2(35 CHAR);
ALTER TABLE domini MODIFY ragione_sociale VARCHAR2(70 CHAR);

ALTER TABLE uo MODIFY cod_uo VARCHAR2(35 CHAR);
ALTER TABLE uo MODIFY uo_codice_identificativo VARCHAR2(35 CHAR);
ALTER TABLE uo MODIFY uo_denominazione VARCHAR2(70 CHAR);
ALTER TABLE uo MODIFY uo_indirizzo VARCHAR2(70 CHAR);
ALTER TABLE uo MODIFY uo_civico VARCHAR2(16 CHAR);
ALTER TABLE uo MODIFY uo_cap VARCHAR2(16 CHAR);
ALTER TABLE uo MODIFY uo_localita VARCHAR2(35 CHAR);
ALTER TABLE uo MODIFY uo_provincia VARCHAR2(35 CHAR);
ALTER TABLE uo MODIFY uo_nazione VARCHAR2(2 CHAR);

ALTER TABLE operatori MODIFY principal VARCHAR2(255 CHAR);
ALTER TABLE operatori MODIFY nome VARCHAR2(35 CHAR);
ALTER TABLE operatori MODIFY profilo VARCHAR2(16 CHAR);

ALTER TABLE connettori MODIFY cod_connettore VARCHAR2(255 CHAR);
ALTER TABLE connettori MODIFY cod_proprieta VARCHAR2(255 CHAR);
ALTER TABLE connettori MODIFY valore VARCHAR2(255 CHAR);

ALTER TABLE portali MODIFY cod_portale VARCHAR2(35 CHAR);
ALTER TABLE portali MODIFY default_callback_url VARCHAR2(512 CHAR);
ALTER TABLE portali MODIFY principal VARCHAR2(255 CHAR);
ALTER TABLE portali MODIFY versione VARCHAR2(10 CHAR);

ALTER TABLE iban_accredito MODIFY cod_iban VARCHAR2(255 CHAR);
ALTER TABLE iban_accredito MODIFY id_seller_bank VARCHAR2(255 CHAR);
ALTER TABLE iban_accredito MODIFY id_negozio VARCHAR2(255 CHAR);
ALTER TABLE iban_accredito MODIFY bic_accredito VARCHAR2(255 CHAR);
ALTER TABLE iban_accredito MODIFY iban_appoggio VARCHAR2(255 CHAR);
ALTER TABLE iban_accredito MODIFY bic_appoggio VARCHAR2(255 CHAR);

ALTER TABLE tipi_tributo MODIFY cod_tributo VARCHAR2(255 CHAR);
ALTER TABLE tipi_tributo MODIFY descrizione VARCHAR2(255 CHAR);

ALTER TABLE tributi MODIFY tipo_contabilita VARCHAR2(1 CHAR);
ALTER TABLE tributi MODIFY codice_contabilita VARCHAR2(255 CHAR);

ALTER TABLE acl MODIFY cod_tipo VARCHAR2(1 CHAR);
ALTER TABLE acl MODIFY cod_servizio VARCHAR2(1 CHAR);

ALTER TABLE versamenti MODIFY cod_versamento_ente VARCHAR2(35 CHAR);
ALTER TABLE versamenti MODIFY stato_versamento VARCHAR2(35 CHAR);
ALTER TABLE versamenti MODIFY descrizione_stato VARCHAR2(255 CHAR);
ALTER TABLE versamenti MODIFY causale_versamento VARCHAR2(511 CHAR);
ALTER TABLE versamenti MODIFY debitore_identificativo VARCHAR2(35 CHAR);
ALTER TABLE versamenti MODIFY debitore_anagrafica VARCHAR2(70 CHAR);
ALTER TABLE versamenti MODIFY debitore_indirizzo VARCHAR2(70 CHAR);
ALTER TABLE versamenti MODIFY debitore_civico VARCHAR2(16 CHAR);
ALTER TABLE versamenti MODIFY debitore_cap VARCHAR2(16 CHAR);
ALTER TABLE versamenti MODIFY debitore_localita VARCHAR2(35 CHAR);
ALTER TABLE versamenti MODIFY debitore_provincia VARCHAR2(35 CHAR);
ALTER TABLE versamenti MODIFY debitore_nazione VARCHAR2(2 CHAR);
ALTER TABLE versamenti MODIFY cod_lotto VARCHAR2(35 CHAR);
ALTER TABLE versamenti MODIFY cod_versamento_lotto VARCHAR2(35 CHAR);
ALTER TABLE versamenti MODIFY cod_anno_tributario VARCHAR2(35 CHAR);
ALTER TABLE versamenti MODIFY cod_bundlekey VARCHAR2(256 CHAR);

ALTER TABLE singoli_versamenti MODIFY cod_singolo_versamento_ente VARCHAR2(70 CHAR);
ALTER TABLE singoli_versamenti MODIFY stato_singolo_versamento VARCHAR2(35 CHAR);
ALTER TABLE singoli_versamenti MODIFY tipo_bollo VARCHAR2(2 CHAR);
ALTER TABLE singoli_versamenti MODIFY hash_documento VARCHAR2(70 CHAR);
ALTER TABLE singoli_versamenti MODIFY provincia_residenza VARCHAR2(2 CHAR);
ALTER TABLE singoli_versamenti MODIFY tipo_contabilita VARCHAR2(1 CHAR);
ALTER TABLE singoli_versamenti MODIFY codice_contabilita VARCHAR2(255 CHAR);
ALTER TABLE singoli_versamenti MODIFY note VARCHAR2(512 CHAR);

ALTER TABLE rpt MODIFY cod_carrello VARCHAR2(35 CHAR);
ALTER TABLE rpt MODIFY iuv VARCHAR2(35 CHAR);
ALTER TABLE rpt MODIFY ccp VARCHAR2(35 CHAR);
ALTER TABLE rpt MODIFY cod_dominio VARCHAR2(35 CHAR);
ALTER TABLE rpt MODIFY cod_msg_richiesta VARCHAR2(35 CHAR);
ALTER TABLE rpt MODIFY stato VARCHAR2(35 CHAR);
ALTER TABLE rpt MODIFY cod_sessione VARCHAR2(255 CHAR);
ALTER TABLE rpt MODIFY cod_sessione_portale VARCHAR2(255 CHAR);
ALTER TABLE rpt MODIFY psp_redirect_url VARCHAR2(512 CHAR);
ALTER TABLE rpt MODIFY modello_pagamento VARCHAR2(16 CHAR);
ALTER TABLE rpt MODIFY cod_msg_ricevuta VARCHAR2(35 CHAR);
ALTER TABLE rpt MODIFY firma_ricevuta VARCHAR2(1 CHAR);
ALTER TABLE rpt MODIFY cod_stazione VARCHAR2(35 CHAR);
ALTER TABLE rpt MODIFY cod_transazione_rpt VARCHAR2(36 CHAR);
ALTER TABLE rpt MODIFY cod_transazione_rt VARCHAR2(36 CHAR);

ALTER TABLE rr MODIFY cod_dominio VARCHAR2(35 CHAR);
ALTER TABLE rr MODIFY iuv VARCHAR2(35 CHAR);
ALTER TABLE rr MODIFY ccp VARCHAR2(35 CHAR);
ALTER TABLE rr MODIFY cod_msg_revoca VARCHAR2(35 CHAR);
ALTER TABLE rr MODIFY stato VARCHAR2(35 CHAR);
ALTER TABLE rr MODIFY descrizione_stato VARCHAR2(512 CHAR);
ALTER TABLE rr MODIFY cod_msg_esito VARCHAR2(35 CHAR);
ALTER TABLE rr MODIFY cod_transazione_rr VARCHAR2(36 CHAR);
ALTER TABLE rr MODIFY cod_transazione_er VARCHAR2(36 CHAR);

ALTER TABLE notifiche MODIFY tipo_esito VARCHAR2(16 CHAR);
ALTER TABLE notifiche MODIFY stato VARCHAR2(16 CHAR);
ALTER TABLE notifiche MODIFY descrizione_stato VARCHAR2(255 CHAR);

ALTER TABLE iuv MODIFY iuv VARCHAR2(35 CHAR);
ALTER TABLE iuv MODIFY tipo_iuv VARCHAR2(1 CHAR);
ALTER TABLE iuv MODIFY cod_versamento_ente VARCHAR2(35 CHAR);

ALTER TABLE fr MODIFY cod_flusso VARCHAR2(35 CHAR);
ALTER TABLE fr MODIFY stato VARCHAR2(35 CHAR);
ALTER TABLE fr MODIFY iur VARCHAR2(35 CHAR);
ALTER TABLE fr MODIFY cod_bic_riversamento VARCHAR2(35 CHAR);

ALTER TABLE pagamenti MODIFY cod_singolo_versamento_ente VARCHAR2(35 CHAR);
ALTER TABLE pagamenti MODIFY iur VARCHAR2(35 CHAR);
ALTER TABLE pagamenti MODIFY iban_accredito VARCHAR2(255 CHAR);
ALTER TABLE pagamenti MODIFY tipo_allegato VARCHAR2(2 CHAR);
ALTER TABLE pagamenti MODIFY codflusso_rendicontazione VARCHAR2(35 CHAR);
ALTER TABLE pagamenti MODIFY causale_revoca VARCHAR2(140 CHAR);
ALTER TABLE pagamenti MODIFY dati_revoca VARCHAR2(140 CHAR);
ALTER TABLE pagamenti MODIFY esito_revoca VARCHAR2(140 CHAR);
ALTER TABLE pagamenti MODIFY dati_esito_revoca VARCHAR2(140 CHAR);
ALTER TABLE pagamenti MODIFY cod_flusso_rendicontaz_revoca VARCHAR2(35 CHAR);

ALTER TABLE eventi MODIFY cod_dominio VARCHAR2(35 CHAR);
ALTER TABLE eventi MODIFY iuv VARCHAR2(35 CHAR);
ALTER TABLE eventi MODIFY ccp VARCHAR2(35 CHAR);
ALTER TABLE eventi MODIFY cod_psp VARCHAR2(35 CHAR);
ALTER TABLE eventi MODIFY tipo_versamento VARCHAR2(10 CHAR);
ALTER TABLE eventi MODIFY componente VARCHAR2(4 CHAR);
ALTER TABLE eventi MODIFY categoria_evento VARCHAR2(1 CHAR);
ALTER TABLE eventi MODIFY tipo_evento VARCHAR2(35 CHAR);
ALTER TABLE eventi MODIFY sottotipo_evento VARCHAR2(35 CHAR);
ALTER TABLE eventi MODIFY erogatore VARCHAR2(35 CHAR);
ALTER TABLE eventi MODIFY fruitore VARCHAR2(35 CHAR);
ALTER TABLE eventi MODIFY cod_stazione VARCHAR2(35 CHAR);
ALTER TABLE eventi MODIFY cod_canale VARCHAR2(35 CHAR);
ALTER TABLE eventi MODIFY parametri_1 VARCHAR2(512 CHAR);
ALTER TABLE eventi MODIFY parametri_2 VARCHAR2(512 CHAR);
ALTER TABLE eventi MODIFY esito VARCHAR2(35 CHAR);

ALTER TABLE rendicontazioni_senza_rpt MODIFY iur VARCHAR2(35 CHAR);

ALTER TABLE ID_MESSAGGIO_RELATIVO MODIFY PROTOCOLLO VARCHAR2(255 CHAR);
ALTER TABLE ID_MESSAGGIO_RELATIVO MODIFY INFO_ASSOCIATA VARCHAR2(255 CHAR);

