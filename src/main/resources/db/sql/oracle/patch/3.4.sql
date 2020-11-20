-- 17/11/2020 Storicizzazione dei flussi di rendicontazione

DROP VIEW IF EXISTS v_rendicontazioni_ext;

ALTER TABLE fr ADD obsoleto NUMBER;
UPDATE fr SET obsoleto = 0;
ALTER TABLE fr MODIFY (obsoleto NOT NULL);
UPDATE fr SET data_ora_flusso = data_acquisizione WHERE data_ora_flusso IS NULL;
ALTER TABLE fr MODIFY (data_ora_flusso NOT NULL);

ALTER TABLE fr DROP CONSTRAINT unique_fr_1;
ALTER TABLE fr ADD CONSTRAINT unique_fr_1 UNIQUE (cod_flusso,data_ora_flusso);

-- Vista Rendicontazioni

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
    versamenti.tipo as vrs_tipo
   FROM fr
     JOIN rendicontazioni ON rendicontazioni.id_fr = fr.id
     JOIN singoli_versamenti ON rendicontazioni.id_singolo_versamento = singoli_versamenti.id
     JOIN versamenti ON versamenti.id = singoli_versamenti.id_versamento WHERE fr.obsoleto = 0; 
