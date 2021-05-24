-- 27/04/2021 Aggiunta colonna contabilita alla tabella singoli versamenti
ALTER TABLE singoli_versamenti ADD COLUMN contabilita LONGTEXT;


-- 11/05/2021 Aggiunto identificativo tracciato notifica pagamenti
ALTER TABLE trac_notif_pag ADD COLUMN identificativo VARCHAR(255);
UPDATE trac_notif_pag SET identificativo = nome_file;
ALTER TABLE trac_notif_pag MODIFY COLUMN identificativo VARCHAR(255) NOT NULL;


-- 17/05/2021 Indice sulla colonna iuv della tabella eventi
CREATE INDEX idx_evt_iuv ON eventi (iuv);


-- 20/05/2021 Aggiunta colonna connettore hyper_sic_apk alla tabella domini
ALTER TABLE domini ADD COLUMN cod_connettore_hyper_sic_apk VARCHAR(255);


-- 24/05/2021 Aggiornata vista v_riscossioni
DROP VIEW IF EXISTS v_riscossioni;
DROP VIEW IF EXISTS v_riscossioni_con_rpt;
DROP VIEW IF EXISTS v_riscossioni_senza_rpt;

CREATE VIEW v_riscossioni_con_rpt AS
SELECT pagamenti.cod_dominio AS cod_dominio,
    pagamenti.iuv AS iuv,
    pagamenti.iur AS iur,
    fr.cod_flusso AS cod_flusso,
    fr.iur AS fr_iur,
    fr.data_regolamento AS data_regolamento,
    fr.importo_totale_pagamenti AS importo_totale_pagamenti,
    fr.numero_pagamenti AS numero_pagamenti,
    pagamenti.importo_pagato AS importo_pagato,
    pagamenti.data_pagamento AS data_pagamento,
    singoli_versamenti.cod_singolo_versamento_ente AS cod_singolo_versamento_ente,
    singoli_versamenti.indice_dati AS indice_dati,
    versamenti.cod_versamento_ente AS cod_versamento_ente,
    versamenti.id_applicazione AS id_applicazione,
    versamenti.debitore_identificativo AS debitore_identificativo,
    versamenti.id_tipo_versamento AS id_tipo_versamento,
    versamenti.cod_anno_tributario AS cod_anno_tributario,
    singoli_versamenti.id_tributo AS id_tributo,
    versamenti.debitore_anagrafica AS debitore_anagrafica,
    fr.cod_psp AS cod_psp,
    fr.ragione_sociale_psp AS ragione_sociale_psp,
    versamenti.cod_rata AS cod_rata,
    versamenti.id_documento AS id_documento,
    versamenti.causale_versamento AS causale_versamento,
    versamenti.importo_totale AS importo_versamento,
    versamenti.numero_avviso AS numero_avviso,
    versamenti.iuv_pagamento AS iuv_pagamento,
    versamenti.data_scadenza AS data_scadenza,
    singoli_versamenti.contabilita AS contabilita
   FROM pagamenti
     LEFT JOIN rendicontazioni ON rendicontazioni.id_pagamento = pagamenti.id
     LEFT JOIN fr ON rendicontazioni.id_fr = fr.id
     JOIN singoli_versamenti ON pagamenti.id_singolo_versamento = singoli_versamenti.id
     JOIN versamenti ON singoli_versamenti.id_versamento = versamenti.id; 

CREATE VIEW v_riscossioni_senza_rpt AS
SELECT fr.cod_dominio AS cod_dominio,
    rendicontazioni.iuv AS iuv,
    rendicontazioni.iur AS iur,
    fr.cod_flusso AS cod_flusso,
    fr.iur AS fr_iur,
    fr.data_regolamento AS data_regolamento,
    fr.importo_totale_pagamenti AS importo_totale_pagamenti,
    fr.numero_pagamenti AS numero_pagamenti,
    rendicontazioni.importo_pagato AS importo_pagato,
    rendicontazioni.data AS data_pagamento,
    singoli_versamenti.cod_singolo_versamento_ente AS cod_singolo_versamento_ente,
    rendicontazioni.indice_dati AS indice_dati,
    versamenti.cod_versamento_ente AS cod_versamento_ente,
    versamenti.id_applicazione AS id_applicazione,
    versamenti.debitore_identificativo AS debitore_identificativo,
    versamenti.id_tipo_versamento AS id_tipo_versamento,
    versamenti.cod_anno_tributario AS cod_anno_tributario,
    singoli_versamenti.id_tributo AS id_tributo,
    versamenti.debitore_anagrafica AS debitore_anagrafica,
    fr.cod_psp AS cod_psp,
    fr.ragione_sociale_psp AS ragione_sociale_psp,
    versamenti.cod_rata AS cod_rata,
    versamenti.id_documento AS id_documento,
    versamenti.causale_versamento AS causale_versamento,
    versamenti.importo_totale AS importo_versamento,
    versamenti.numero_avviso AS numero_avviso,
    versamenti.iuv_pagamento AS iuv_pagamento,
    versamenti.data_scadenza AS data_scadenza,
    singoli_versamenti.contabilita AS contabilita
   FROM fr
     JOIN rendicontazioni ON rendicontazioni.id_fr = fr.id
     JOIN versamenti ON versamenti.iuv_versamento = rendicontazioni.iuv
     JOIN domini ON versamenti.id_dominio = domini.id AND domini.cod_dominio = fr.cod_dominio
     JOIN singoli_versamenti ON singoli_versamenti.id_versamento = versamenti.id
  WHERE rendicontazioni.esito = 9;


CREATE VIEW v_riscossioni AS
        SELECT cod_dominio, iuv, iur, cod_flusso, fr_iur,  data_regolamento, importo_totale_pagamenti, numero_pagamenti, importo_pagato, data_pagamento, cod_singolo_versamento_ente, indice_dati, cod_versamento_ente, applicazioni.cod_applicazione, debitore_identificativo AS identificativo_debitore, cod_anno_tributario AS anno, cod_tipo_versamento, cod_tributo AS cod_entrata, tipi_versamento.descrizione AS descr_tipo_versamento, debitore_anagrafica, cod_psp, ragione_sociale_psp, cod_rata, id_documento, causale_versamento, importo_versamento, numero_avviso, iuv_pagamento, data_scadenza, contabilita FROM v_riscossioni_con_rpt JOIN applicazioni ON v_riscossioni_con_rpt.id_applicazione = applicazioni.id JOIN tipi_versamento ON v_riscossioni_con_rpt.id_tipo_versamento = tipi_versamento.id LEFT JOIN tributi ON v_riscossioni_con_rpt.id_tributo = tributi.id JOIN tipi_tributo ON tributi.id_tipo_tributo = tipi_tributo.id           
        UNION
        SELECT cod_dominio, iuv, iur, cod_flusso, fr_iur,  data_regolamento, importo_totale_pagamenti, numero_pagamenti, importo_pagato, data_pagamento, cod_singolo_versamento_ente, indice_dati, cod_versamento_ente, applicazioni.cod_applicazione, debitore_identificativo AS identificativo_debitore, cod_anno_tributario AS anno, cod_tipo_versamento, cod_tributo AS cod_entrata, tipi_versamento.descrizione AS descr_tipo_versamento, debitore_anagrafica, cod_psp, ragione_sociale_psp, cod_rata, id_documento, causale_versamento, importo_versamento, numero_avviso, iuv_pagamento, data_scadenza, contabilita FROM v_riscossioni_senza_rpt join applicazioni ON v_riscossioni_senza_rpt.id_applicazione = applicazioni.id LEFT JOIN tipi_versamento ON v_riscossioni_senza_rpt.id_tipo_versamento = tipi_versamento.id LEFT JOIN tributi ON v_riscossioni_senza_rpt.id_tributo = tributi.id LEFT JOIN tipi_tributo ON tributi.id_tipo_tributo = tipi_tributo.id;



