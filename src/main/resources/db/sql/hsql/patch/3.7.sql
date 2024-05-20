-- 04/08/2022 Aggiunta colonna versione alla tabella stazioni
ALTER TABLE stazioni ADD versione VARCHAR(35);
UPDATE stazioni SET versione = 'V1' WHERE versione IS NULL;
ALTER TABLE stazioni ALTER COLUMN versione NOT NULL;


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
     JOIN eventi ON eventi.id_sessione = pagamenti_portale.id_sessione);

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

CREATE VIEW v_eventi_vers AS
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
        UNION SELECT componente,
               ruolo,
               categoria_evento,
               tipo_evento,
               sottotipo_evento,
               data,
               intervallo,
               esito,
               sottotipo_esito,
               dettaglio_esito,
               parametri_richiesta,
               parametri_risposta,
               dati_pago_pa,
               cod_versamento_ente,
               cod_applicazione,
               iuv,
               cod_dominio,
               ccp,
               id_sessione,
	       severita,
               id FROM v_eventi_vers_pagamenti 
        UNION SELECT componente,
               ruolo,
               categoria_evento,
               tipo_evento,
               sottotipo_evento,
               data,
               intervallo,
               esito,
               sottotipo_esito,
               dettaglio_esito,
               parametri_richiesta,
               parametri_risposta,
               dati_pago_pa,
               cod_versamento_ente,
               cod_applicazione,
               iuv,
               cod_dominio,
               ccp,
               id_sessione,
	       severita,
               id FROM v_eventi_vers_rendicontazioni
        UNION SELECT componente,
               ruolo,
               categoria_evento,
               tipo_evento,
               sottotipo_evento,
               data,
               intervallo,
               esito,
               sottotipo_esito,
               dettaglio_esito,
               parametri_richiesta,
               parametri_risposta,
               dati_pago_pa,
               cod_versamento_ente,
               cod_applicazione,
               iuv,
               cod_dominio,
               ccp,
               id_sessione,
	       severita,
               id FROM v_eventi_vers_riconciliazioni
	    UNION SELECT componente,
               ruolo,
               categoria_evento,
               tipo_evento,
               sottotipo_evento,
               data,
               intervallo,
               esito,
               sottotipo_esito,
               dettaglio_esito,
               parametri_richiesta,
               parametri_risposta,
               dati_pago_pa,
               cod_versamento_ente,
               cod_applicazione,
               iuv,
               cod_dominio,
               ccp,
               id_sessione,
	       severita,
               id FROM v_eventi_vers_tracciati;


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
     JOIN eventi ON eventi.id_sessione = pagamenti_portale.id_sessione);

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

CREATE VIEW v_eventi_vers AS
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
        UNION SELECT componente,
               ruolo,
               categoria_evento,
               tipo_evento,
               sottotipo_evento,
               data,
               intervallo,
               esito,
               sottotipo_esito,
               dettaglio_esito,
               parametri_richiesta,
               parametri_risposta,
               dati_pago_pa,
               cod_versamento_ente,
               cod_applicazione,
               iuv,
               cod_dominio,
               ccp,
               id_sessione,
	       severita,
               cluster_id,
               transaction_id,
               id FROM v_eventi_vers_pagamenti 
        UNION SELECT componente,
               ruolo,
               categoria_evento,
               tipo_evento,
               sottotipo_evento,
               data,
               intervallo,
               esito,
               sottotipo_esito,
               dettaglio_esito,
               parametri_richiesta,
               parametri_risposta,
               dati_pago_pa,
               cod_versamento_ente,
               cod_applicazione,
               iuv,
               cod_dominio,
               ccp,
               id_sessione,
	       severita,
               cluster_id,
               transaction_id,
               id FROM v_eventi_vers_rendicontazioni
        UNION SELECT componente,
               ruolo,
               categoria_evento,
               tipo_evento,
               sottotipo_evento,
               data,
               intervallo,
               esito,
               sottotipo_esito,
               dettaglio_esito,
               parametri_richiesta,
               parametri_risposta,
               dati_pago_pa,
               cod_versamento_ente,
               cod_applicazione,
               iuv,
               cod_dominio,
               ccp,
               id_sessione,
	       severita,
               cluster_id,
               transaction_id,
               id FROM v_eventi_vers_riconciliazioni
	    UNION SELECT componente,
               ruolo,
               categoria_evento,
               tipo_evento,
               sottotipo_evento,
               data,
               intervallo,
               esito,
               sottotipo_esito,
               dettaglio_esito,
               parametri_richiesta,
               parametri_risposta,
               dati_pago_pa,
               cod_versamento_ente,
               cod_applicazione,
               iuv,
               cod_dominio,
               ccp,
               id_sessione,
	       severita,
               cluster_id,
               transaction_id,
               id FROM v_eventi_vers_tracciati;


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
   
   
-- 01/02/2023 Corretto template di trasformazione tracciato csv.
UPDATE configurazione SET valore = '{"tipo":"freemarker","intestazione":"idA2A,idPendenza,idDominio,tipoPendenza,numeroAvviso,pdfAvviso,tipoSoggettoPagatore,identificativoPagatore,anagraficaPagatore,indirizzoPagatore,civicoPagatore,capPagatore,localitaPagatore,provinciaPagatore,nazionePagatore,emailPagatore,cellularePagatore,errore","richiesta":"\"PCNhc3NpZ24gY3N2VXRpbHMgPSBjbGFzc1siaXQuZ292cGF5LmNvcmUudXRpbHMuQ1NWVXRpbHMiXS5nZXRJbnN0YW5jZSgpPgoKPCNhc3NpZ24gY3N2UmVjb3JkID0gY3N2VXRpbHMuZ2V0Q1NWUmVjb3JkKGxpbmVhQ3N2UmljaGllc3RhKT4KPCNpZiBjc3ZVdGlscy5pc09wZXJhemlvbmVBbm51bGxhbWVudG8oY3N2UmVjb3JkLCAxMik+Cgk8I2Fzc2lnbiB0bXA9Y29udGV4dD9hcGkucHV0KCJ0aXBvT3BlcmF6aW9uZSIsICJERUwiKSEvPgp7CgkiaWRBMkEiOiAke2NzdlV0aWxzLnRvSnNvblZhbHVlKGNzdlJlY29yZCwgMCl9LAoJImlkUGVuZGVuemEiOiAke2NzdlV0aWxzLnRvSnNvblZhbHVlKGNzdlJlY29yZCwgMSl9Cn0KPCNlbHNlPgo8I2Fzc2lnbiB0bXA9Y29udGV4dD9hcGkucHV0KCJ0aXBvT3BlcmF6aW9uZSIsICJBREQiKSEvPgo8I2lmICFjc3ZVdGlscy5pc0VtcHR5KGNzdlJlY29yZCwgODIpPgogIDwjYXNzaWduIGRhdGFBdnZpc29TdHJpbmcgPSBjc3ZSZWNvcmQuZ2V0KDgyKT4KICA8I2lmIGRhdGFBdnZpc29TdHJpbmcuZXF1YWxzKCJNQUkiKT4KICAJPCNhc3NpZ24gdG1wPWNvbnRleHQ/YXBpLnB1dCgiYXZ2aXNhdHVyYSIsIGZhbHNlKSEvPgogIDwjZWxzZT4KICAJPCNhc3NpZ24gc2RmVXRpbHMgPSBjbGFzc1siaXQuZ292cGF5LmNvcmUudXRpbHMuU2ltcGxlRGF0ZUZvcm1hdFV0aWxzIl0uZ2V0SW5zdGFuY2UoKT4KICAJPCNhc3NpZ24gZGF0YUF2dmlzYXR1cmFEYXRlID0gc2RmVXRpbHMuZ2V0RGF0YUF2dmlzYXR1cmEoZGF0YUF2dmlzb1N0cmluZywgImRhdGFBdnZpc2F0dXJhIikgPgogIAk8I2Fzc2lnbiBjb250ZXh0VE1QID0gY29udGV4dCA+CiAgCTwjYXNzaWduIHRtcD1jb250ZXh0P2FwaS5wdXQoImRhdGFBdnZpc2F0dXJhIiwgZGF0YUF2dmlzYXR1cmFEYXRlKSEvPgogIDwvI2lmPgo8LyNpZj4KewoJImlkQTJBIjogJHtjc3ZVdGlscy50b0pzb25WYWx1ZShjc3ZSZWNvcmQsIDApfSwKCSJpZFBlbmRlbnphIjogJHtjc3ZVdGlscy50b0pzb25WYWx1ZShjc3ZSZWNvcmQsIDEpfSwKCSJpZERvbWluaW8iOiAke2NzdlV0aWxzLnRvSnNvblZhbHVlKGNzdlJlY29yZCwgMil9LAoJIm51bWVyb0F2dmlzbyI6ICR7Y3N2VXRpbHMudG9Kc29uVmFsdWUoY3N2UmVjb3JkLCAzKX0sCQoJImlkVGlwb1BlbmRlbnphIjogJHtjc3ZVdGlscy50b0pzb25WYWx1ZShjc3ZSZWNvcmQsIDQpfSwKCSJpZFVuaXRhT3BlcmF0aXZhIjogJHtjc3ZVdGlscy50b0pzb25WYWx1ZShjc3ZSZWNvcmQsIDUpfSwKIAkiY2F1c2FsZSI6ICR7Y3N2VXRpbHMudG9Kc29uVmFsdWUoY3N2UmVjb3JkLCA2KX0sCiAJImFubm9SaWZlcmltZW50byI6ICR7Y3N2VXRpbHMudG9Kc29uVmFsdWUoY3N2UmVjb3JkLCA3KX0sCiAJImNhcnRlbGxhUGFnYW1lbnRvIjogJHtjc3ZVdGlscy50b0pzb25WYWx1ZShjc3ZSZWNvcmQsIDgpfSwKIAk8I2lmICFjc3ZVdGlscy5pc0VtcHR5KGNzdlJlY29yZCwgOSk+ImRhdGlBbGxlZ2F0aSI6ICR7Y3N2UmVjb3JkLmdldCg5KX0sPC8jaWY+CiAJImRpcmV6aW9uZSI6ICR7Y3N2VXRpbHMudG9Kc29uVmFsdWUoY3N2UmVjb3JkLCAxMCl9LAogCSJkaXZpc2lvbmUiOiAke2NzdlV0aWxzLnRvSnNvblZhbHVlKGNzdlJlY29yZCwgMTEpfSwKIAkiaW1wb3J0byI6ICR7Y3N2VXRpbHMudG9Kc29uVmFsdWUoY3N2UmVjb3JkLCAxMil9LAoJImRhdGFWYWxpZGl0YSI6ICR7Y3N2VXRpbHMudG9Kc29uVmFsdWUoY3N2UmVjb3JkLCAxMyl9LAoJImRhdGFTY2FkZW56YSI6ICR7Y3N2VXRpbHMudG9Kc29uVmFsdWUoY3N2UmVjb3JkLCAxNCl9LAoJInRhc3Nvbm9taWFBdnZpc28iOiAke2NzdlV0aWxzLnRvSnNvblZhbHVlKGNzdlJlY29yZCwgMTUpfSwKCTwjaWYgIWNzdlV0aWxzLmlzRW1wdHkoY3N2UmVjb3JkLCA4Myk+CgkiZG9jdW1lbnRvIjogewoJCSJpZGVudGlmaWNhdGl2byI6ICR7Y3N2VXRpbHMudG9Kc29uVmFsdWUoY3N2UmVjb3JkLCA4Myl9LAoJCTwjaWYgIWNzdlV0aWxzLmlzRW1wdHkoY3N2UmVjb3JkLCA4NSk+CgkJCQoJCQk8I2Fzc2lnbiByYXRhU3RyaW5nID0gY3N2UmVjb3JkLmdldCg4NSk+CgkJCTwjaWYgcmF0YVN0cmluZz9tYXRjaGVzKCdbMC05XSonKT4KCQkJCSJyYXRhIjogJHtjc3ZVdGlscy50b0pzb25WYWx1ZShjc3ZSZWNvcmQsIDg1KX0sCQoJCQk8I2Vsc2U+CgkJCQkic29nbGlhIjogewoJCQkJCSJ0aXBvIjogIiR7cmF0YVN0cmluZ1swLi40XX0iLAoJCQkJCSJnaW9ybmkiOiAke3JhdGFTdHJpbmdbNS4uXX0gCgkJCQl9LAkgIAoJCQk8LyNpZj4KCQk8LyNpZj4KCQkiZGVzY3JpemlvbmUiOiAke2NzdlV0aWxzLnRvSnNvblZhbHVlKGNzdlJlY29yZCwgODQpfQoJfSwKCTwvI2lmPgoJPCNpZiAhY3N2VXRpbHMuaXNFbXB0eShjc3ZSZWNvcmQsIDg2KT4KCQk8I2Fzc2lnbiBsaW5ndWFTZWNvbmRhcmlhU3RyaW5nID0gY3N2UmVjb3JkLmdldCg4NikhLz4KCQk8I2Fzc2lnbiBtYXBwZWRBcnJheV9zdHJpbmcgPSBsaW5ndWFTZWNvbmRhcmlhU3RyaW5nP3NwbGl0KCJ8IikgIS8+CgkJCgkicHJvcHJpZXRhIjogewoJCSJsaW5ndWFTZWNvbmRhcmlhIiA6ICIke21hcHBlZEFycmF5X3N0cmluZ1swXX0iCgkJPCNpZiAobWFwcGVkQXJyYXlfc3RyaW5nP3NpemUgPiAxKT4KCQkJLCAibGluZ3VhU2Vjb25kYXJpYUNhdXNhbGUiIDogIiR7bWFwcGVkQXJyYXlfc3RyaW5nWzFdfSIKCQk8LyNpZj4KCX0sCgk8LyNpZj4KCSJzb2dnZXR0b1BhZ2F0b3JlIjogewoJCSJ0aXBvIjogJHtjc3ZVdGlscy50b0pzb25WYWx1ZShjc3ZSZWNvcmQsIDE2KX0sCgkJImlkZW50aWZpY2F0aXZvIjogJHtjc3ZVdGlscy50b0pzb25WYWx1ZShjc3ZSZWNvcmQsIDE3KX0sCgkJImFuYWdyYWZpY2EiOiAke2NzdlV0aWxzLnRvSnNvblZhbHVlKGNzdlJlY29yZCwgMTgpfSwKCQkiaW5kaXJpenpvIjogJHtjc3ZVdGlscy50b0pzb25WYWx1ZShjc3ZSZWNvcmQsIDE5KX0sCgkJImNpdmljbyI6ICR7Y3N2VXRpbHMudG9Kc29uVmFsdWUoY3N2UmVjb3JkLCAyMCl9LAoJCSJjYXAiOiAke2NzdlV0aWxzLnRvSnNvblZhbHVlKGNzdlJlY29yZCwgMjEpfSwKCQkibG9jYWxpdGEiOiAke2NzdlV0aWxzLnRvSnNvblZhbHVlKGNzdlJlY29yZCwgMjIpfSwKCQkicHJvdmluY2lhIjogJHtjc3ZVdGlscy50b0pzb25WYWx1ZShjc3ZSZWNvcmQsIDIzKX0sCgkJIm5hemlvbmUiOiAke2NzdlV0aWxzLnRvSnNvblZhbHVlKGNzdlJlY29yZCwgMjQpfSwKCQkiZW1haWwiOiAke2NzdlV0aWxzLnRvSnNvblZhbHVlKGNzdlJlY29yZCwgMjUpfSwKCQkiY2VsbHVsYXJlIjogJHtjc3ZVdGlscy50b0pzb25WYWx1ZShjc3ZSZWNvcmQsIDI2KX0KCX0sCgkidm9jaSI6IFsKCQl7CgkJCSJpZFZvY2VQZW5kZW56YSI6ICR7Y3N2VXRpbHMudG9Kc29uVmFsdWUoY3N2UmVjb3JkLCAyNyl9LAoJCQkiaW1wb3J0byI6ICR7Y3N2VXRpbHMudG9Kc29uVmFsdWUoY3N2UmVjb3JkLCAyOCl9LAoJCQkiZGVzY3JpemlvbmUiOiAke2NzdlV0aWxzLnRvSnNvblZhbHVlKGNzdlJlY29yZCwgMjkpfSwKCQkJPCNpZiAhY3N2VXRpbHMuaXNFbXB0eShjc3ZSZWNvcmQsIDM0KT4KCQkJImNvZEVudHJhdGEiOiAke2NzdlV0aWxzLnRvSnNvblZhbHVlKGNzdlJlY29yZCwgMzQpfQoJCQk8I2Vsc2VpZiAhY3N2VXRpbHMuaXNFbXB0eShjc3ZSZWNvcmQsIDM1KT4KCQkJInRpcG9Cb2xsbyI6ICR7Y3N2VXRpbHMudG9Kc29uVmFsdWUoY3N2UmVjb3JkLCAzNSl9LAogICAgICAgCQkiaGFzaERvY3VtZW50byI6ICR7Y3N2VXRpbHMudG9Kc29uVmFsdWUoY3N2UmVjb3JkLCAzNil9LAogICAgICAJCSJwcm92aW5jaWFSZXNpZGVuemEiOiAke2NzdlV0aWxzLnRvSnNvblZhbHVlKGNzdlJlY29yZCwgMzcpfQoJCQk8I2Vsc2U+CgkJCSJpZERvbWluaW8iOiAke2NzdlV0aWxzLmdldENvZERvbWluaW9Gcm9tSWJhbkFjY3JlZGl0byhjc3ZSZWNvcmQsIDMwKX0sCgkJCSJpYmFuQWNjcmVkaXRvIjogJHtjc3ZVdGlscy50b0pzb25WYWx1ZShjc3ZSZWNvcmQsIDMwKX0sCgkJCSJpYmFuQXBwb2dnaW8iOiAke2NzdlV0aWxzLnRvSnNvblZhbHVlKGNzdlJlY29yZCwgMzEpfSwKCQkJInRpcG9Db250YWJpbGl0YSI6ICR7Y3N2VXRpbHMudG9Kc29uVmFsdWUoY3N2UmVjb3JkLCAzMil9LAoJCQkiY29kaWNlQ29udGFiaWxpdGEiOiAke2NzdlV0aWxzLnRvSnNvblZhbHVlKGNzdlJlY29yZCwgMzMpfQoJCQk8LyNpZj4KCgkJfQoJCTwjaWYgIWNzdlV0aWxzLmlzRW1wdHkoY3N2UmVjb3JkLCAzOCk+CgkJLHsKCQkJImlkVm9jZVBlbmRlbnphIjogJHtjc3ZVdGlscy50b0pzb25WYWx1ZShjc3ZSZWNvcmQsIDM4KX0sCgkJCSJpbXBvcnRvIjogJHtjc3ZVdGlscy50b0pzb25WYWx1ZShjc3ZSZWNvcmQsIDM5KX0sCgkJCSJkZXNjcml6aW9uZSI6ICR7Y3N2VXRpbHMudG9Kc29uVmFsdWUoY3N2UmVjb3JkLCA0MCl9LAoJCQk8I2lmICFjc3ZVdGlscy5pc0VtcHR5KGNzdlJlY29yZCwgNDUpPgoJCQkiY29kRW50cmF0YSI6ICR7Y3N2VXRpbHMudG9Kc29uVmFsdWUoY3N2UmVjb3JkLCA0NSl9CgkJCTwjZWxzZWlmICFjc3ZVdGlscy5pc0VtcHR5KGNzdlJlY29yZCwgNDYpPgoJCQkidGlwb0JvbGxvIjogJHtjc3ZVdGlscy50b0pzb25WYWx1ZShjc3ZSZWNvcmQsIDQ2KX0sCiAgICAgICAJCSJoYXNoRG9jdW1lbnRvIjogJHtjc3ZVdGlscy50b0pzb25WYWx1ZShjc3ZSZWNvcmQsIDQ3KX0sCiAgICAgIAkJInByb3ZpbmNpYVJlc2lkZW56YSI6ICR7Y3N2VXRpbHMudG9Kc29uVmFsdWUoY3N2UmVjb3JkLCA0OCl9CgkJCTwjZWxzZT4KCQkJImlkRG9taW5pbyI6ICR7Y3N2VXRpbHMuZ2V0Q29kRG9taW5pb0Zyb21JYmFuQWNjcmVkaXRvKGNzdlJlY29yZCwgNDEpfSwKCQkJImliYW5BY2NyZWRpdG8iOiAke2NzdlV0aWxzLnRvSnNvblZhbHVlKGNzdlJlY29yZCwgNDEpfSwKCQkJImliYW5BcHBvZ2dpbyI6ICR7Y3N2VXRpbHMudG9Kc29uVmFsdWUoY3N2UmVjb3JkLCA0Mil9LAoJCQkidGlwb0NvbnRhYmlsaXRhIjogJHtjc3ZVdGlscy50b0pzb25WYWx1ZShjc3ZSZWNvcmQsIDQzKX0sCgkJCSJjb2RpY2VDb250YWJpbGl0YSI6ICR7Y3N2VXRpbHMudG9Kc29uVmFsdWUoY3N2UmVjb3JkLCA0NCl9CgkJCTwvI2lmPgoJCX0KCQk8LyNpZj4KCQk8I2lmICFjc3ZVdGlscy5pc0VtcHR5KGNzdlJlY29yZCwgNDkpPgoJCSx7CgkJCSJpZFZvY2VQZW5kZW56YSI6ICR7Y3N2VXRpbHMudG9Kc29uVmFsdWUoY3N2UmVjb3JkLCA0OSl9LAoJCQkiaW1wb3J0byI6ICR7Y3N2VXRpbHMudG9Kc29uVmFsdWUoY3N2UmVjb3JkLCA1MCl9LAoJCQkiZGVzY3JpemlvbmUiOiAke2NzdlV0aWxzLnRvSnNvblZhbHVlKGNzdlJlY29yZCwgNTEpfSwKCQkJPCNpZiAhY3N2VXRpbHMuaXNFbXB0eShjc3ZSZWNvcmQsIDU2KT4KCQkJImNvZEVudHJhdGEiOiAke2NzdlV0aWxzLnRvSnNvblZhbHVlKGNzdlJlY29yZCwgNTYpfQoJCQk8I2Vsc2VpZiAhY3N2VXRpbHMuaXNFbXB0eShjc3ZSZWNvcmQsIDU3KT4KCQkJInRpcG9Cb2xsbyI6ICR7Y3N2VXRpbHMudG9Kc29uVmFsdWUoY3N2UmVjb3JkLCA1Nyl9LAogICAgICAgCQkiaGFzaERvY3VtZW50byI6ICR7Y3N2VXRpbHMudG9Kc29uVmFsdWUoY3N2UmVjb3JkLCA1OCl9LAogICAgICAJCSJwcm92aW5jaWFSZXNpZGVuemEiOiAke2NzdlV0aWxzLnRvSnNvblZhbHVlKGNzdlJlY29yZCwgNTkpfQoJCQk8I2Vsc2U+CgkJCSJpZERvbWluaW8iOiAke2NzdlV0aWxzLmdldENvZERvbWluaW9Gcm9tSWJhbkFjY3JlZGl0byhjc3ZSZWNvcmQsIDUyKX0sCgkJCSJpYmFuQWNjcmVkaXRvIjogJHtjc3ZVdGlscy50b0pzb25WYWx1ZShjc3ZSZWNvcmQsIDUyKX0sCgkJCSJpYmFuQXBwb2dnaW8iOiAke2NzdlV0aWxzLnRvSnNvblZhbHVlKGNzdlJlY29yZCwgNTMpfSwKCQkJInRpcG9Db250YWJpbGl0YSI6ICR7Y3N2VXRpbHMudG9Kc29uVmFsdWUoY3N2UmVjb3JkLCA1NCl9LAoJCQkiY29kaWNlQ29udGFiaWxpdGEiOiAke2NzdlV0aWxzLnRvSnNvblZhbHVlKGNzdlJlY29yZCwgNTUpfQoJCQk8LyNpZj4KCQl9CgkJPC8jaWY+CgkJPCNpZiAhY3N2VXRpbHMuaXNFbXB0eShjc3ZSZWNvcmQsIDYwKT4KCQksewoJCQkiaWRWb2NlUGVuZGVuemEiOiAke2NzdlV0aWxzLnRvSnNvblZhbHVlKGNzdlJlY29yZCwgNjApfSwKCQkJImltcG9ydG8iOiAke2NzdlV0aWxzLnRvSnNvblZhbHVlKGNzdlJlY29yZCwgNjEpfSwKCQkJImRlc2NyaXppb25lIjogJHtjc3ZVdGlscy50b0pzb25WYWx1ZShjc3ZSZWNvcmQsIDYyKX0sCgkJCTwjaWYgIWNzdlV0aWxzLmlzRW1wdHkoY3N2UmVjb3JkLCA2Nyk+CgkJCSJjb2RFbnRyYXRhIjogJHtjc3ZVdGlscy50b0pzb25WYWx1ZShjc3ZSZWNvcmQsIDY3KX0KCQkJPCNlbHNlaWYgIWNzdlV0aWxzLmlzRW1wdHkoY3N2UmVjb3JkLCA2OCk+CgkJCSJ0aXBvQm9sbG8iOiAke2NzdlV0aWxzLnRvSnNvblZhbHVlKGNzdlJlY29yZCwgNjgpfSwKICAgICAgIAkJImhhc2hEb2N1bWVudG8iOiAke2NzdlV0aWxzLnRvSnNvblZhbHVlKGNzdlJlY29yZCwgNjkpfSwKICAgICAgCQkicHJvdmluY2lhUmVzaWRlbnphIjogJHtjc3ZVdGlscy50b0pzb25WYWx1ZShjc3ZSZWNvcmQsIDcwKX0KCQkJPCNlbHNlPgoJCQkiaWREb21pbmlvIjogJHtjc3ZVdGlscy5nZXRDb2REb21pbmlvRnJvbUliYW5BY2NyZWRpdG8oY3N2UmVjb3JkLCA2Myl9LAoJCQkiaWJhbkFjY3JlZGl0byI6ICR7Y3N2VXRpbHMudG9Kc29uVmFsdWUoY3N2UmVjb3JkLCA2Myl9LAoJCQkiaWJhbkFwcG9nZ2lvIjogJHtjc3ZVdGlscy50b0pzb25WYWx1ZShjc3ZSZWNvcmQsIDY0KX0sCgkJCSJ0aXBvQ29udGFiaWxpdGEiOiAke2NzdlV0aWxzLnRvSnNvblZhbHVlKGNzdlJlY29yZCwgNjUpfSwKCQkJImNvZGljZUNvbnRhYmlsaXRhIjogJHtjc3ZVdGlscy50b0pzb25WYWx1ZShjc3ZSZWNvcmQsIDY2KX0KCQkJPC8jaWY+CgkJfQoJCTwvI2lmPgoJCTwjaWYgIWNzdlV0aWxzLmlzRW1wdHkoY3N2UmVjb3JkLCA3MSk+CgkJLHsKCQkJImlkVm9jZVBlbmRlbnphIjogJHtjc3ZVdGlscy50b0pzb25WYWx1ZShjc3ZSZWNvcmQsIDcxKX0sCgkJCSJpbXBvcnRvIjogJHtjc3ZVdGlscy50b0pzb25WYWx1ZShjc3ZSZWNvcmQsIDcyKX0sCgkJCSJkZXNjcml6aW9uZSI6ICR7Y3N2VXRpbHMudG9Kc29uVmFsdWUoY3N2UmVjb3JkLCA3Myl9LAoJCQk8I2lmICFjc3ZVdGlscy5pc0VtcHR5KGNzdlJlY29yZCwgNzgpPgoJCQkiY29kRW50cmF0YSI6ICR7Y3N2VXRpbHMudG9Kc29uVmFsdWUoY3N2UmVjb3JkLCA3OCl9CgkJCTwjZWxzZWlmICFjc3ZVdGlscy5pc0VtcHR5KGNzdlJlY29yZCwgNzkpPgoJCQkidGlwb0JvbGxvIjogJHtjc3ZVdGlscy50b0pzb25WYWx1ZShjc3ZSZWNvcmQsIDc5KX0sCiAgICAgICAJCSJoYXNoRG9jdW1lbnRvIjogJHtjc3ZVdGlscy50b0pzb25WYWx1ZShjc3ZSZWNvcmQsIDgwKX0sCiAgICAgIAkJInByb3ZpbmNpYVJlc2lkZW56YSI6ICR7Y3N2VXRpbHMudG9Kc29uVmFsdWUoY3N2UmVjb3JkLCA4MSl9CgkJCTwjZWxzZT4KCQkJImlkRG9taW5pbyI6ICR7Y3N2VXRpbHMuZ2V0Q29kRG9taW5pb0Zyb21JYmFuQWNjcmVkaXRvKGNzdlJlY29yZCwgNzQpfSwKCQkJImliYW5BY2NyZWRpdG8iOiAke2NzdlV0aWxzLnRvSnNvblZhbHVlKGNzdlJlY29yZCwgNzQpfSwKCQkJImliYW5BcHBvZ2dpbyI6ICR7Y3N2VXRpbHMudG9Kc29uVmFsdWUoY3N2UmVjb3JkLCA3NSl9LAoJCQkidGlwb0NvbnRhYmlsaXRhIjogJHtjc3ZVdGlscy50b0pzb25WYWx1ZShjc3ZSZWNvcmQsIDc2KX0sCgkJCSJjb2RpY2VDb250YWJpbGl0YSI6ICR7Y3N2VXRpbHMudG9Kc29uVmFsdWUoY3N2UmVjb3JkLCA3Nyl9CgkJCTwvI2lmPgoJCX0KCQk8LyNpZj4KCgldCn0KPC8jaWY+\"","risposta":"\"PCNhc3NpZ24gY3N2VXRpbHMgPSBjbGFzc1siaXQuZ292cGF5LmNvcmUudXRpbHMuQ1NWVXRpbHMiXS5nZXRJbnN0YW5jZSgpIC8+CjwjaWYgZXNpdG9PcGVyYXppb25lID09ICJFU0VHVUlUT19PSyI+CjwjYXNzaWduIGlkQTJBID0gYXBwbGljYXppb25lLmdldENvZEFwcGxpY2F6aW9uZSgpISAvPgo8I2Fzc2lnbiBpZFBlbmRlbnphID0gdmVyc2FtZW50by5nZXRDb2RWZXJzYW1lbnRvRW50ZSgpISAvPgo8I2Fzc2lnbiBpZERvbWluaW8gPSBkb21pbmlvLmdldENvZERvbWluaW8oKSAvPgo8I2Fzc2lnbiB0aXBvUGVuZGVuemEgPSBpZFRpcG9WZXJzYW1lbnRvIC8+CjwjYXNzaWduIG51bWVyb0F2dmlzbyA9IHZlcnNhbWVudG8uZ2V0TnVtZXJvQXZ2aXNvKCkhIC8+CjwjaWYgbnVtZXJvQXZ2aXNvP2hhc19jb250ZW50PgoJPCNhc3NpZ24gcGRmQXZ2aXNvID0gaWREb21pbmlvICsgIl8iICsgbnVtZXJvQXZ2aXNvICsgIi5wZGYiIC8+CjwvI2lmPgo8I2Fzc2lnbiBpZERvY3VtZW50byA9IHZlcnNhbWVudG8uZ2V0SWREb2N1bWVudG8oKSEgLz4KPCNpZiBkb2N1bWVudG8/aGFzX2NvbnRlbnQ+CiAgICA8I2Fzc2lnbiBudW1lcm9Eb2N1bWVudG8gPSBkb2N1bWVudG8uZ2V0Q29kRG9jdW1lbnRvKCkgLz4KCTwjYXNzaWduIHBkZkF2dmlzbyA9IGlkRG9taW5pbyArICJfRE9DXyIgKyBudW1lcm9Eb2N1bWVudG8gKyAiLnBkZiIgLz4KPC8jaWY+CjwjYXNzaWduIHRpcG8gPSB2ZXJzYW1lbnRvLmdldEFuYWdyYWZpY2FEZWJpdG9yZSgpLmdldFRpcG8oKS50b1N0cmluZygpIC8+CjwjYXNzaWduIGlkZW50aWZpY2F0aXZvID0gdmVyc2FtZW50by5nZXRBbmFncmFmaWNhRGViaXRvcmUoKS5nZXRDb2RVbml2b2NvKCkhIC8+CjwjYXNzaWduIGFuYWdyYWZpY2EgPSB2ZXJzYW1lbnRvLmdldEFuYWdyYWZpY2FEZWJpdG9yZSgpLmdldFJhZ2lvbmVTb2NpYWxlKCkhIC8+CjwjYXNzaWduIGluZGlyaXp6byA9IHZlcnNhbWVudG8uZ2V0QW5hZ3JhZmljYURlYml0b3JlKCkuZ2V0SW5kaXJpenpvKCkhIC8+CjwjYXNzaWduIGNpdmljbyA9IHZlcnNhbWVudG8uZ2V0QW5hZ3JhZmljYURlYml0b3JlKCkuZ2V0Q2l2aWNvKCkhIC8+CjwjYXNzaWduIGNhcCA9IHZlcnNhbWVudG8uZ2V0QW5hZ3JhZmljYURlYml0b3JlKCkuZ2V0Q2FwKCkhIC8+CjwjYXNzaWduIGxvY2FsaXRhID0gdmVyc2FtZW50by5nZXRBbmFncmFmaWNhRGViaXRvcmUoKS5nZXRMb2NhbGl0YSgpISAvPgo8I2Fzc2lnbiBwcm92aW5jaWEgPSB2ZXJzYW1lbnRvLmdldEFuYWdyYWZpY2FEZWJpdG9yZSgpLmdldFByb3ZpbmNpYSgpISAvPgo8I2Fzc2lnbiBuYXppb25lID0gdmVyc2FtZW50by5nZXRBbmFncmFmaWNhRGViaXRvcmUoKS5nZXROYXppb25lKCkhIC8+CjwjYXNzaWduIGVtYWlsID0gdmVyc2FtZW50by5nZXRBbmFncmFmaWNhRGViaXRvcmUoKS5nZXRFbWFpbCgpISAvPgo8I2Fzc2lnbiBjZWxsdWxhcmUgPSB2ZXJzYW1lbnRvLmdldEFuYWdyYWZpY2FEZWJpdG9yZSgpLmdldENlbGx1bGFyZSgpISAvPgo8I2lmIHRpcG9PcGVyYXppb25lID09ICJBREQiPgoJPCNhc3NpZ24gY3N2UmVjb3JkID0gY3N2VXRpbHMudG9Dc3YoaWRBMkEsIGlkUGVuZGVuemEsIGlkRG9taW5pbywgdGlwb1BlbmRlbnphLCBudW1lcm9BdnZpc28sIHBkZkF2dmlzbywgdGlwbywgaWRlbnRpZmljYXRpdm8sIGFuYWdyYWZpY2EsIGluZGlyaXp6bywgY2l2aWNvLCBjYXAsIGxvY2FsaXRhLCBwcm92aW5jaWEsIG5hemlvbmUsIGVtYWlsLCBjZWxsdWxhcmUsICIiKSAvPgo8I2Vsc2U+Cgk8I2Fzc2lnbiBjc3ZSZWNvcmQgPSBjc3ZVdGlscy50b0NzdihpZEEyQSwgaWRQZW5kZW56YSwgaWREb21pbmlvLCB0aXBvUGVuZGVuemEsIG51bWVyb0F2dmlzbywgcGRmQXZ2aXNvLCB0aXBvLCBpZGVudGlmaWNhdGl2bywgYW5hZ3JhZmljYSwgaW5kaXJpenpvLCBjaXZpY28sIGNhcCwgbG9jYWxpdGEsIHByb3ZpbmNpYSwgbmF6aW9uZSwgZW1haWwsIGNlbGx1bGFyZSwgZGVzY3JpemlvbmVFc2l0b09wZXJhemlvbmUpIC8+CjwvI2lmPgo8I2Vsc2U+CjwjYXNzaWduIGNzdlJlY29yZCA9IGNzdlV0aWxzLnRvQ3N2KCIiLCAiIiwgIiIsICIiLCAiIiwgIiIsICIiLCAiIiwgIiIsICIiLCAiIiwgIiIsICIiLCAiIiwgIiIsICIiLCAiIiwgZGVzY3JpemlvbmVFc2l0b09wZXJhemlvbmUpIC8+CjwvI2lmPgoke2NzdlJlY29yZH0=\""}' WHERE nome = 'tracciato_csv';


-- 12/05/2023 Aggiunti indici mancanti
CREATE INDEX idx_fr_data_acq ON fr (data_acquisizione);
CREATE INDEX idx_prt_data_richiesta ON pagamenti_portale (data_richiesta);
CREATE INDEX idx_pag_data_acq ON pagamenti (data_acquisizione);


-- 05/09/2023 fix modifica connettori con tipo autenticazione subscription key
UPDATE connettori SET valore ='NONE' WHERE cod_proprieta = 'TIPOAUTENTICAZIONE' and valore = 'SUBSCRIPTION_KEY';




