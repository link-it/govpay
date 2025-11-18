-- 29/10/2025 Aggiunto indice sulla data alla tabella rendicontazioni
CREATE INDEX idx_rnd_data ON rendicontazioni (data);


-- 31/10/2025 Aggiunta colonna scarica_fr alla tabella domini
ALTER TABLE domini ADD scarica_fr BIT;
UPDATE domini SET scarica_fr = 1;
ALTER TABLE domini ALTER COLUMN scarica_fr BIT NOT NULL;


-- 01/11/2025 Issue #821 Aggiunta colonna data_ora_pubblicazione alla tabella fr
ALTER TABLE fr ADD data_ora_pubblicazione DATETIME;


-- 03/11/2025 Reso opzionale il campo xml della tabella fr
ALTER TABLE fr ALTER COLUMN xml VARBINARY(MAX) NULL;


-- 03/11/2025 Aggiunti campi data_ora_aggiornamento e revisione alla tabella fr
ALTER TABLE fr ADD data_ora_aggiornamento DATETIME;
ALTER TABLE fr ADD revisione BIGINT;


-- 04/11/2025 Aggiornamento della vista rendicontazioni
DROP VIEW v_rendicontazioni_ext;

CREATE VIEW v_rendicontazioni_ext AS
 SELECT fr.cod_psp AS fr_cod_psp,
    fr.cod_dominio AS fr_cod_dominio,
    fr.cod_flusso AS fr_cod_flusso,
    fr.stato AS fr_stato,
    fr.descrizione_stato AS fr_descrizione_stato,
    fr.iur AS fr_iur,
    fr.data_ora_flusso AS fr_data_ora_flusso,
    fr.data_regolamento AS fr_data_regolamento,
    fr.data_acquisizione AS fr_data_acquisizione,
    fr.numero_pagamenti AS fr_numero_pagamenti,
    fr.importo_totale_pagamenti AS fr_importo_totale_pagamenti,
    fr.cod_bic_riversamento AS fr_cod_bic_riversamento,
    fr.id AS fr_id,
    fr.id_incasso AS fr_id_incasso,
    fr.ragione_sociale_psp AS fr_ragione_sociale_psp,
    fr.ragione_sociale_dominio AS fr_ragione_sociale_dominio,
    fr.obsoleto AS fr_obsoleto,
    fr.data_ora_pubblicazione AS fr_data_ora_pubblicazione,
    fr.data_ora_aggiornamento AS fr_data_ora_aggiornamento,
    fr.revisione AS fr_revisione,
    rendicontazioni.iuv AS rnd_iuv,
    rendicontazioni.iur AS rnd_iur,
    rendicontazioni.indice_dati AS rnd_indice_dati,
    rendicontazioni.importo_pagato AS rnd_importo_pagato,
    rendicontazioni.esito AS rnd_esito,
    rendicontazioni.data AS rnd_data,
    rendicontazioni.stato AS rnd_stato,
    rendicontazioni.anomalie AS rnd_anomalie,
    rendicontazioni.id,
    rendicontazioni.id_pagamento AS rnd_id_pagamento,
    singoli_versamenti.cod_singolo_versamento_ente AS sng_cod_sing_vers_ente,
    singoli_versamenti.importo_singolo_versamento AS sng_importo_singolo_versamento,
    singoli_versamenti.descrizione AS sng_descrizione,
    singoli_versamenti.dati_allegati AS sng_dati_allegati,
    singoli_versamenti.stato_singolo_versamento AS sng_stato_singolo_versamento,
    singoli_versamenti.indice_dati AS sng_indice_dati,
    singoli_versamenti.descrizione_causale_rpt AS sng_descrizione_causale_rpt,
    singoli_versamenti.contabilita AS sng_contabilita,
    singoli_versamenti.metadata AS sng_metadata,
    singoli_versamenti.id_tributo AS sng_id_tributo,
    versamenti.cod_versamento_ente AS vrs_cod_versamento_ente,
    versamenti.importo_totale AS vrs_importo_totale,
    versamenti.debitore_identificativo AS vrs_debitore_identificativo,
    versamenti.debitore_anagrafica AS vrs_debitore_anagrafica,
    versamenti.tassonomia AS vrs_tassonomia,
    versamenti.divisione AS vrs_divisione,
    versamenti.direzione AS vrs_direzione,
    versamenti.id_tipo_versamento AS vrs_id_tipo_versamento,
    versamenti.id_tipo_versamento_dominio AS vrs_id_tipo_versamento_dominio,
    versamenti.id_dominio AS vrs_id_dominio,
    versamenti.id_uo AS vrs_id_uo,
    versamenti.id_applicazione AS vrs_id_applicazione,
    versamenti.id AS vrs_id,
    versamenti.nome AS vrs_nome,
    versamenti.stato_versamento AS vrs_stato_versamento,
    versamenti.descrizione_stato AS vrs_descrizione_stato,
    versamenti.aggiornabile AS vrs_aggiornabile,
    versamenti.data_creazione AS vrs_data_creazione,
    versamenti.data_validita AS vrs_data_validita,
    versamenti.data_scadenza AS vrs_data_scadenza,
    versamenti.data_ora_ultimo_aggiornamento AS vrs_data_ora_ultimo_agg,
    versamenti.causale_versamento AS vrs_causale_versamento,
    versamenti.debitore_tipo AS vrs_debitore_tipo,
    versamenti.debitore_indirizzo AS vrs_debitore_indirizzo,
    versamenti.debitore_civico AS vrs_debitore_civico,
    versamenti.debitore_cap AS vrs_debitore_cap,
    versamenti.debitore_localita AS vrs_debitore_localita,
    versamenti.debitore_provincia AS vrs_debitore_provincia,
    versamenti.debitore_nazione AS vrs_debitore_nazione,
    versamenti.debitore_email AS vrs_debitore_email,
    versamenti.debitore_telefono AS vrs_debitore_telefono,
    versamenti.debitore_cellulare AS vrs_debitore_cellulare,
    versamenti.debitore_fax AS vrs_debitore_fax,
    versamenti.tassonomia_avviso AS vrs_tassonomia_avviso,
    versamenti.cod_lotto AS vrs_cod_lotto,
    versamenti.cod_versamento_lotto AS vrs_cod_versamento_lotto,
    versamenti.cod_anno_tributario AS vrs_cod_anno_tributario,
    versamenti.cod_bundlekey AS vrs_cod_bundlekey,
    versamenti.dati_allegati AS vrs_dati_allegati,
    versamenti.incasso AS vrs_incasso,
    versamenti.anomalie AS vrs_anomalie,
    versamenti.iuv_versamento AS vrs_iuv_versamento,
    versamenti.numero_avviso AS vrs_numero_avviso,
    versamenti.ack AS vrs_ack,
    versamenti.anomalo AS vrs_anomalo,
    versamenti.id_sessione AS vrs_id_sessione,
    versamenti.data_pagamento AS vrs_data_pagamento,
    versamenti.importo_pagato AS vrs_importo_pagato,
    versamenti.importo_incassato AS vrs_importo_incassato,
    versamenti.stato_pagamento AS vrs_stato_pagamento,
    versamenti.iuv_pagamento AS vrs_iuv_pagamento,
    versamenti.cod_rata as vrs_cod_rata,
    versamenti.id_documento as vrs_id_documento,
    versamenti.tipo as vrs_tipo,
    versamenti.proprieta as vrs_proprieta,
    pagamenti.cod_dominio AS pag_cod_dominio,             
	pagamenti.iuv AS pag_iuv,                     
	pagamenti.indice_dati AS pag_indice_dati,             
	pagamenti.importo_pagato AS pag_importo_pagato,          
	pagamenti.data_acquisizione AS pag_data_acquisizione,       
	pagamenti.iur AS pag_iur,                     
	pagamenti.data_pagamento AS pag_data_pagamento,          
	pagamenti.commissioni_psp AS pag_commissioni_psp,         
	pagamenti.tipo_allegato AS pag_tipo_allegato,           
	pagamenti.allegato AS pag_allegato,                
	pagamenti.data_acquisizione_revoca AS pag_data_acquisizione_revoca,
	pagamenti.causale_revoca AS pag_causale_revoca,          
	pagamenti.dati_revoca AS pag_dati_revoca,             
	pagamenti.importo_revocato AS pag_importo_revocato,        
	pagamenti.esito_revoca AS pag_esito_revoca,            
	pagamenti.dati_esito_revoca AS pag_dati_esito_revoca,       
	pagamenti.stato AS pag_stato,                  
	pagamenti.tipo AS pag_tipo,                  
	rpt.iuv AS rpt_iuv,
	rpt.ccp AS rpt_ccp,
	incassi.trn AS rnc_trn
   FROM fr
     JOIN rendicontazioni ON rendicontazioni.id_fr = fr.id
     LEFT JOIN singoli_versamenti ON rendicontazioni.id_singolo_versamento = singoli_versamenti.id
     LEFT JOIN versamenti ON versamenti.id = singoli_versamenti.id_versamento 
     LEFT JOIN pagamenti on rendicontazioni.id_pagamento = pagamenti.id 
     LEFT JOIN rpt on pagamenti.id_rpt = rpt.id
     LEFT JOIN incassi on pagamenti.id_incasso = incassi.id;


-- 10/11/2025 Aggiunti nuovi connettori PagoPA all'intermediario (ACA, GPD, FR)
ALTER TABLE intermediari ADD cod_connettore_aca VARCHAR(35);
ALTER TABLE intermediari ADD cod_connettore_gpd VARCHAR(35);
ALTER TABLE intermediari ADD cod_connettore_fr VARCHAR(35);


-- 17/11/2025 Connettore per il Microservizio GDE
INSERT INTO connettori(cod_connettore,cod_proprieta,valore) values ('govpay_gde_api', 'ABILITATO', 0);


-- 18/11/2025 Aggiunta FK id_dominio alla tabella fr
ALTER TABLE fr ADD id_dominio BIGINT;
GO
UPDATE fr SET fr.id_dominio = domini.id FROM fr INNER JOIN domini ON fr.cod_dominio = domini.cod_dominio;
GO
ALTER TABLE fr ALTER COLUMN id_dominio BIGINT NOT NULL;
GO
ALTER TABLE fr ADD CONSTRAINT fk_fr_id_dominio FOREIGN KEY (id_dominio) REFERENCES domini(id);
GO
CREATE INDEX idx_fr_id_dominio ON fr (id_dominio);
GO


-- 18/11/2025 Aggiornamento vista v_rendicontazioni_ext per includere fr.id_dominio
DROP VIEW v_rendicontazioni_ext;
GO

CREATE VIEW v_rendicontazioni_ext AS
 SELECT fr.cod_psp AS fr_cod_psp,
    fr.cod_dominio AS fr_cod_dominio,
    fr.id_dominio AS fr_id_dominio,
    fr.cod_flusso AS fr_cod_flusso,
    fr.stato AS fr_stato,
    fr.descrizione_stato AS fr_descrizione_stato,
    fr.iur AS fr_iur,
    fr.data_ora_flusso AS fr_data_ora_flusso,
    fr.data_regolamento AS fr_data_regolamento,
    fr.data_acquisizione AS fr_data_acquisizione,
    fr.numero_pagamenti AS fr_numero_pagamenti,
    fr.importo_totale_pagamenti AS fr_importo_totale_pagamenti,
    fr.cod_bic_riversamento AS fr_cod_bic_riversamento,
    fr.id AS fr_id,
    fr.id_incasso AS fr_id_incasso,
    fr.ragione_sociale_psp AS fr_ragione_sociale_psp,
    fr.ragione_sociale_dominio AS fr_ragione_sociale_dominio,
    fr.obsoleto AS fr_obsoleto,
    fr.data_ora_pubblicazione AS fr_data_ora_pubblicazione,
    fr.data_ora_aggiornamento AS fr_data_ora_aggiornamento,
    fr.revisione AS fr_revisione,
    rendicontazioni.iuv AS rnd_iuv,
    rendicontazioni.iur AS rnd_iur,
    rendicontazioni.indice_dati AS rnd_indice_dati,
    rendicontazioni.importo_pagato AS rnd_importo_pagato,
    rendicontazioni.esito AS rnd_esito,
    rendicontazioni.data AS rnd_data,
    rendicontazioni.stato AS rnd_stato,
    rendicontazioni.anomalie AS rnd_anomalie,
    rendicontazioni.id,
    rendicontazioni.id_pagamento AS rnd_id_pagamento,
    singoli_versamenti.cod_singolo_versamento_ente AS sng_cod_sing_vers_ente,
    singoli_versamenti.importo_singolo_versamento AS sng_importo_singolo_versamento,
    singoli_versamenti.descrizione AS sng_descrizione,
    singoli_versamenti.dati_allegati AS sng_dati_allegati,
    singoli_versamenti.stato_singolo_versamento AS sng_stato_singolo_versamento,
    singoli_versamenti.indice_dati AS sng_indice_dati,
    singoli_versamenti.descrizione_causale_rpt AS sng_descrizione_causale_rpt,
    singoli_versamenti.contabilita AS sng_contabilita,
    singoli_versamenti.metadata AS sng_metadata,
    singoli_versamenti.id_tributo AS sng_id_tributo,
    versamenti.cod_versamento_ente AS vrs_cod_versamento_ente,
    versamenti.importo_totale AS vrs_importo_totale,
    versamenti.debitore_identificativo AS vrs_debitore_identificativo,
    versamenti.debitore_anagrafica AS vrs_debitore_anagrafica,
    versamenti.tassonomia AS vrs_tassonomia,
    versamenti.divisione AS vrs_divisione,
    versamenti.direzione AS vrs_direzione,
    versamenti.id_tipo_versamento AS vrs_id_tipo_versamento,
    versamenti.id_tipo_versamento_dominio AS vrs_id_tipo_versamento_dominio,
    versamenti.id_dominio AS vrs_id_dominio,
    versamenti.id_uo AS vrs_id_uo,
    versamenti.id_applicazione AS vrs_id_applicazione,
    versamenti.id AS vrs_id,
    versamenti.nome AS vrs_nome,
    versamenti.stato_versamento AS vrs_stato_versamento,
    versamenti.descrizione_stato AS vrs_descrizione_stato,
    versamenti.aggiornabile AS vrs_aggiornabile,
    versamenti.data_creazione AS vrs_data_creazione,
    versamenti.data_validita AS vrs_data_validita,
    versamenti.data_scadenza AS vrs_data_scadenza,
    versamenti.data_ora_ultimo_aggiornamento AS vrs_data_ora_ultimo_agg,
    versamenti.causale_versamento AS vrs_causale_versamento,
    versamenti.debitore_tipo AS vrs_debitore_tipo,
    versamenti.debitore_indirizzo AS vrs_debitore_indirizzo,
    versamenti.debitore_civico AS vrs_debitore_civico,
    versamenti.debitore_cap AS vrs_debitore_cap,
    versamenti.debitore_localita AS vrs_debitore_localita,
    versamenti.debitore_provincia AS vrs_debitore_provincia,
    versamenti.debitore_nazione AS vrs_debitore_nazione,
    versamenti.debitore_email AS vrs_debitore_email,
    versamenti.debitore_telefono AS vrs_debitore_telefono,
    versamenti.debitore_cellulare AS vrs_debitore_cellulare,
    versamenti.debitore_fax AS vrs_debitore_fax,
    versamenti.tassonomia_avviso AS vrs_tassonomia_avviso,
    versamenti.cod_lotto AS vrs_cod_lotto,
    versamenti.cod_versamento_lotto AS vrs_cod_versamento_lotto,
    versamenti.cod_anno_tributario AS vrs_cod_anno_tributario,
    versamenti.cod_bundlekey AS vrs_cod_bundlekey,
    versamenti.dati_allegati AS vrs_dati_allegati,
    versamenti.incasso AS vrs_incasso,
    versamenti.anomalie AS vrs_anomalie,
    versamenti.iuv_versamento AS vrs_iuv_versamento,
    versamenti.numero_avviso AS vrs_numero_avviso,
    versamenti.ack AS vrs_ack,
    versamenti.anomalo AS vrs_anomalo,
    versamenti.id_sessione AS vrs_id_sessione,
    versamenti.data_pagamento AS vrs_data_pagamento,
    versamenti.importo_pagato AS vrs_importo_pagato,
    versamenti.importo_incassato AS vrs_importo_incassato,
    versamenti.stato_pagamento AS vrs_stato_pagamento,
    versamenti.iuv_pagamento AS vrs_iuv_pagamento,
    versamenti.cod_rata as vrs_cod_rata,
    versamenti.id_documento as vrs_id_documento,
    versamenti.tipo as vrs_tipo,
    versamenti.proprieta as vrs_proprieta,
    pagamenti.cod_dominio AS pag_cod_dominio,
	pagamenti.iuv AS pag_iuv,
	pagamenti.indice_dati AS pag_indice_dati,
	pagamenti.importo_pagato AS pag_importo_pagato,
	pagamenti.data_acquisizione AS pag_data_acquisizione,
	pagamenti.iur AS pag_iur,
	pagamenti.data_pagamento AS pag_data_pagamento,
	pagamenti.commissioni_psp AS pag_commissioni_psp,
	pagamenti.tipo_allegato AS pag_tipo_allegato,
	pagamenti.allegato AS pag_allegato,
	pagamenti.data_acquisizione_revoca AS pag_data_acquisizione_revoca,
	pagamenti.causale_revoca AS pag_causale_revoca,
	pagamenti.dati_revoca AS pag_dati_revoca,
	pagamenti.importo_revocato AS pag_importo_revocato,
	pagamenti.esito_revoca AS pag_esito_revoca,
	pagamenti.dati_esito_revoca AS pag_dati_esito_revoca,
	pagamenti.stato AS pag_stato,
	pagamenti.tipo AS pag_tipo,
	rpt.iuv AS rpt_iuv,
	rpt.ccp AS rpt_ccp,
	incassi.trn AS rnc_trn
   FROM fr
     JOIN rendicontazioni ON rendicontazioni.id_fr = fr.id
     LEFT JOIN singoli_versamenti ON rendicontazioni.id_singolo_versamento = singoli_versamenti.id
     LEFT JOIN versamenti ON versamenti.id = singoli_versamenti.id_versamento
     LEFT JOIN pagamenti on rendicontazioni.id_pagamento = pagamenti.id
     LEFT JOIN rpt on pagamenti.id_rpt = rpt.id
     LEFT JOIN incassi on pagamenti.id_incasso = incassi.id;
GO

