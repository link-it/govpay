-- 04/08/2022 Aggiunta colonna versione alla tabella stazioni
ALTER TABLE stazioni ADD COLUMN versione VARCHAR(35);
UPDATE stazioni SET versione = 'V1' WHERE versione IS NULL;
ALTER TABLE stazioni ALTER COLUMN versione SET NOT NULL;


-- 31/08/2022 Correzione vista eventi riconciliazione
DROP VIEW IF EXISTS v_eventi_vers;
DROP VIEW IF EXISTS v_eventi_vers_pagamenti;
DROP VIEW IF EXISTS v_eventi_vers_rendicontazioni;
DROP VIEW IF EXISTS v_eventi_vers_riconciliazioni;
DROP VIEW IF EXISTS v_eventi_vers_tracciati;

CREATE VIEW v_eventi_vers_rendicontazioni AS (
        SELECT DISTINCT eventi.componente,
               eventi.ruolo,
               eventi.categoria_evento,
               eventi.tipo_evento,
               eventi.sottotipo_evento,
               eventi.data,
               eventi.intervallo,
               eventi.esito,
               eventi.sottotipo_esito,
               eventi.dettaglio_esito,
               eventi.parametri_richiesta,
               eventi.parametri_risposta,
               eventi.dati_pago_pa,
               versamenti.cod_versamento_ente as cod_versamento_ente,
               applicazioni.cod_applicazione as cod_applicazione,
               eventi.iuv,
               eventi.cod_dominio,
               eventi.ccp,
               eventi.id_sessione,
	       eventi.severita,
               eventi.id
        FROM eventi 
        JOIN rendicontazioni ON rendicontazioni.id_fr = eventi.id_fr
        JOIN singoli_versamenti ON rendicontazioni.id_singolo_versamento=singoli_versamenti.id
        JOIN versamenti ON singoli_versamenti.id_versamento=versamenti.id
        JOIN applicazioni ON versamenti.id_applicazione = applicazioni.id
);

CREATE VIEW v_eventi_vers_pagamenti AS (
 SELECT DISTINCT eventi.componente,
    eventi.ruolo,
    eventi.categoria_evento,
    eventi.tipo_evento,
    eventi.sottotipo_evento,
    eventi.data,
    eventi.intervallo,
    eventi.esito,
    eventi.sottotipo_esito,
    eventi.dettaglio_esito,
    eventi.parametri_richiesta,
    eventi.parametri_risposta,
    eventi.dati_pago_pa,
    versamenti.cod_versamento_ente,
    applicazioni.cod_applicazione,
    eventi.iuv,
    eventi.cod_dominio,
    eventi.ccp,
    eventi.id_sessione,
    eventi.severita,
    eventi.id
   FROM versamenti
     JOIN applicazioni ON versamenti.id_applicazione = applicazioni.id
     JOIN pag_port_versamenti ON versamenti.id = pag_port_versamenti.id_versamento
     JOIN pagamenti_portale ON pag_port_versamenti.id_pagamento_portale = pagamenti_portale.id
     JOIN eventi ON eventi.id_sessione::text = pagamenti_portale.id_sessione::text);

CREATE VIEW v_eventi_vers_riconciliazioni AS (
        SELECT DISTINCT eventi.componente,
               eventi.ruolo,
               eventi.categoria_evento,
               eventi.tipo_evento,
               eventi.sottotipo_evento,
               eventi.data,
               eventi.intervallo,
               eventi.esito,
               eventi.sottotipo_esito,
               eventi.dettaglio_esito,
               eventi.parametri_richiesta,
               eventi.parametri_risposta,
               eventi.dati_pago_pa,
               versamenti.cod_versamento_ente as cod_versamento_ente,
               applicazioni.cod_applicazione as cod_applicazione,
               eventi.iuv,
               eventi.cod_dominio,
               eventi.ccp,
               eventi.id_sessione,
	       eventi.severita,
               eventi.id
        FROM eventi
        JOIN pagamenti ON pagamenti.id_incasso = eventi.id_incasso
        JOIN singoli_versamenti ON pagamenti.id_singolo_versamento=singoli_versamenti.id
        JOIN versamenti ON singoli_versamenti.id_versamento=versamenti.id
        JOIN applicazioni ON versamenti.id_applicazione = applicazioni.id
);

CREATE VIEW v_eventi_vers_tracciati AS (
        SELECT DISTINCT eventi.componente,
               eventi.ruolo,
               eventi.categoria_evento,
               eventi.tipo_evento,
               eventi.sottotipo_evento,
               eventi.data,
               eventi.intervallo,
               eventi.esito,
               eventi.sottotipo_esito,
               eventi.dettaglio_esito,
               eventi.parametri_richiesta,
               eventi.parametri_risposta,
               eventi.dati_pago_pa,
               versamenti.cod_versamento_ente as cod_versamento_ente,
               applicazioni.cod_applicazione as cod_applicazione,
               eventi.iuv,
               eventi.cod_dominio,
               eventi.ccp,
               eventi.id_sessione,
	       eventi.severita,
               eventi.id
        FROM eventi
        JOIN operazioni ON operazioni.id_tracciato = eventi.id_tracciato
        JOIN versamenti ON operazioni.id_applicazione = versamenti.id_applicazione AND operazioni.cod_versamento_ente = versamenti.cod_versamento_ente
        JOIN applicazioni ON versamenti.id_applicazione = applicazioni.id
);

CREATE VIEW v_eventi_vers AS (
        SELECT eventi.componente,
               eventi.ruolo,
               eventi.categoria_evento,
               eventi.tipo_evento,
               eventi.sottotipo_evento,
               eventi.data,
               eventi.intervallo,
               eventi.esito,
               eventi.sottotipo_esito,
               eventi.dettaglio_esito,
               eventi.parametri_richiesta,
               eventi.parametri_risposta,
               eventi.dati_pago_pa,
               eventi.cod_versamento_ente,
               eventi.cod_applicazione,
               eventi.iuv,
               eventi.cod_dominio,
               eventi.ccp,
               eventi.id_sessione,
	       eventi.severita,
               eventi.id FROM eventi 
        UNION SELECT * FROM v_eventi_vers_pagamenti 
        UNION SELECT * FROM v_eventi_vers_rendicontazioni
        UNION SELECT * FROM v_eventi_vers_riconciliazioni
	UNION SELECT * FROM v_eventi_vers_tracciati
);


-- 30/09/2022 indice sulla colonna id_fr della tabella eventi
CREATE INDEX idx_evt_fk_fr ON eventi (id_fr);


-- 30/09/2022 indice sulla colonna cod_flusso della tabella fr
CREATE INDEX idx_fr_cod_flusso ON fr (cod_flusso);


-- 26/10/2022 indice sulla colonna iuv della tabella rendicontazioni
CREATE INDEX idx_rnd_iuv ON rendicontazioni (iuv);


-- 26/10/2022 indice sulla colonna data_msg_richiesta della tabella rpt
CREATE INDEX idx_rpt_data_msg_richiesta ON rpt (data_msg_richiesta);


-- 15/11/2022 nuove colonne cluster_id e transaction_id nella tabella eventi
DROP VIEW IF EXISTS v_eventi_vers;
DROP VIEW IF EXISTS v_eventi_vers_pagamenti;
DROP VIEW IF EXISTS v_eventi_vers_rendicontazioni;
DROP VIEW IF EXISTS v_eventi_vers_riconciliazioni;
DROP VIEW IF EXISTS v_eventi_vers_tracciati;

ALTER TABLE eventi ADD COLUMN cluster_id VARCHAR(255);
ALTER TABLE eventi ADD COLUMN transaction_id VARCHAR(255);

CREATE VIEW v_eventi_vers_rendicontazioni AS (
        SELECT DISTINCT eventi.componente,
               eventi.ruolo,
               eventi.categoria_evento,
               eventi.tipo_evento,
               eventi.sottotipo_evento,
               eventi.data,
               eventi.intervallo,
               eventi.esito,
               eventi.sottotipo_esito,
               eventi.dettaglio_esito,
               eventi.parametri_richiesta,
               eventi.parametri_risposta,
               eventi.dati_pago_pa,
               versamenti.cod_versamento_ente as cod_versamento_ente,
               applicazioni.cod_applicazione as cod_applicazione,
               eventi.iuv,
               eventi.cod_dominio,
               eventi.ccp,
               eventi.id_sessione,
	       eventi.severita,
	       eventi.cluster_id,
	       eventi.transaction_id,
               eventi.id
        FROM eventi 
        JOIN rendicontazioni ON rendicontazioni.id_fr = eventi.id_fr
        JOIN singoli_versamenti ON rendicontazioni.id_singolo_versamento=singoli_versamenti.id
        JOIN versamenti ON singoli_versamenti.id_versamento=versamenti.id
        JOIN applicazioni ON versamenti.id_applicazione = applicazioni.id
);

CREATE VIEW v_eventi_vers_pagamenti AS (
 SELECT DISTINCT eventi.componente,
    eventi.ruolo,
    eventi.categoria_evento,
    eventi.tipo_evento,
    eventi.sottotipo_evento,
    eventi.data,
    eventi.intervallo,
    eventi.esito,
    eventi.sottotipo_esito,
    eventi.dettaglio_esito,
    eventi.parametri_richiesta,
    eventi.parametri_risposta,
    eventi.dati_pago_pa,
    versamenti.cod_versamento_ente,
    applicazioni.cod_applicazione,
    eventi.iuv,
    eventi.cod_dominio,
    eventi.ccp,
    eventi.id_sessione,
    eventi.severita,
    eventi.cluster_id,
    eventi.transaction_id,
    eventi.id
   FROM versamenti
     JOIN applicazioni ON versamenti.id_applicazione = applicazioni.id
     JOIN pag_port_versamenti ON versamenti.id = pag_port_versamenti.id_versamento
     JOIN pagamenti_portale ON pag_port_versamenti.id_pagamento_portale = pagamenti_portale.id
     JOIN eventi ON eventi.id_sessione::text = pagamenti_portale.id_sessione::text);

CREATE VIEW v_eventi_vers_riconciliazioni AS (
        SELECT DISTINCT eventi.componente,
               eventi.ruolo,
               eventi.categoria_evento,
               eventi.tipo_evento,
               eventi.sottotipo_evento,
               eventi.data,
               eventi.intervallo,
               eventi.esito,
               eventi.sottotipo_esito,
               eventi.dettaglio_esito,
               eventi.parametri_richiesta,
               eventi.parametri_risposta,
               eventi.dati_pago_pa,
               versamenti.cod_versamento_ente as cod_versamento_ente,
               applicazioni.cod_applicazione as cod_applicazione,
               eventi.iuv,
               eventi.cod_dominio,
               eventi.ccp,
               eventi.id_sessione,
	       eventi.severita,
	       eventi.cluster_id,
	       eventi.transaction_id,
               eventi.id
        FROM eventi
        JOIN pagamenti ON pagamenti.id_incasso = eventi.id_incasso
        JOIN singoli_versamenti ON pagamenti.id_singolo_versamento=singoli_versamenti.id
        JOIN versamenti ON singoli_versamenti.id_versamento=versamenti.id
        JOIN applicazioni ON versamenti.id_applicazione = applicazioni.id
);

CREATE VIEW v_eventi_vers_tracciati AS (
        SELECT DISTINCT eventi.componente,
               eventi.ruolo,
               eventi.categoria_evento,
               eventi.tipo_evento,
               eventi.sottotipo_evento,
               eventi.data,
               eventi.intervallo,
               eventi.esito,
               eventi.sottotipo_esito,
               eventi.dettaglio_esito,
               eventi.parametri_richiesta,
               eventi.parametri_risposta,
               eventi.dati_pago_pa,
               versamenti.cod_versamento_ente as cod_versamento_ente,
               applicazioni.cod_applicazione as cod_applicazione,
               eventi.iuv,
               eventi.cod_dominio,
               eventi.ccp,
               eventi.id_sessione,
	       eventi.severita,
	       eventi.cluster_id,
	       eventi.transaction_id,
               eventi.id
        FROM eventi
        JOIN operazioni ON operazioni.id_tracciato = eventi.id_tracciato
        JOIN versamenti ON operazioni.id_applicazione = versamenti.id_applicazione AND operazioni.cod_versamento_ente = versamenti.cod_versamento_ente
        JOIN applicazioni ON versamenti.id_applicazione = applicazioni.id
);

CREATE VIEW v_eventi_vers AS (
        SELECT eventi.componente,
               eventi.ruolo,
               eventi.categoria_evento,
               eventi.tipo_evento,
               eventi.sottotipo_evento,
               eventi.data,
               eventi.intervallo,
               eventi.esito,
               eventi.sottotipo_esito,
               eventi.dettaglio_esito,
               eventi.parametri_richiesta,
               eventi.parametri_risposta,
               eventi.dati_pago_pa,
               eventi.cod_versamento_ente,
               eventi.cod_applicazione,
               eventi.iuv,
               eventi.cod_dominio,
               eventi.ccp,
               eventi.id_sessione,
	       eventi.severita,
	       eventi.cluster_id,
	       eventi.transaction_id,
               eventi.id FROM eventi 
        UNION SELECT * FROM v_eventi_vers_pagamenti 
        UNION SELECT * FROM v_eventi_vers_rendicontazioni
        UNION SELECT * FROM v_eventi_vers_riconciliazioni
	UNION SELECT * FROM v_eventi_vers_tracciati
);


-- 18/01/2023 Ottimizzazione della vista v_riscossioni
DROP VIEW IF EXISTS v_riscossioni;
DROP VIEW IF EXISTS v_riscossioni_senza_rpt;
DROP VIEW IF EXISTS v_riscossioni_con_rpt;

CREATE VIEW v_riscossioni AS (
 SELECT 
    fr.cod_dominio AS cod_dominio,
    rendicontazioni.iuv AS iuv,
    rendicontazioni.iur AS iur,
    fr.cod_flusso AS cod_flusso, 
    fr.iur AS fr_iur,
    fr.data_regolamento AS data_regolamento,
    fr.importo_totale_pagamenti AS importo_totale_pagamenti,
    fr.numero_pagamenti AS numero_pagamenti,
    rendicontazioni.importo_pagato AS importo_pagato,
    rendicontazioni.data AS data_pagamento,
    singoli_versamenti.cod_singolo_versamento_ente as cod_singolo_versamento_ente, 
    rendicontazioni.indice_dati as indice_dati, 
    versamenti.cod_versamento_ente AS cod_versamento_ente,
    applicazioni.cod_applicazione AS cod_applicazione,
    versamenti.debitore_identificativo AS identificativo_debitore,
    versamenti.cod_anno_tributario AS anno,
    tipi_versamento.cod_tipo_versamento AS cod_tipo_versamento,
    tipi_tributo.cod_tributo AS cod_entrata, 
    tipi_versamento.descrizione AS descr_tipo_versamento,
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
    versamenti.data_creazione AS data_creazione,
    singoli_versamenti.contabilita AS contabilita
   FROM fr
   JOIN rendicontazioni ON rendicontazioni.id_fr = fr.id
   LEFT JOIN singoli_versamenti ON rendicontazioni.id_singolo_versamento = singoli_versamenti.id
   LEFT JOIN versamenti ON versamenti.id = singoli_versamenti.id_versamento
   LEFT JOIN applicazioni ON versamenti.id_applicazione=applicazioni.id
   LEFT JOIN tipi_versamento ON versamenti.id_tipo_versamento=tipi_versamento.id
   LEFT JOIN tributi ON singoli_versamenti.id_tributo = tributi.id 
   LEFT JOIN tipi_tributo ON tributi.id_tipo_tributo = tipi_tributo.id);

