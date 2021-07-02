-- 27/04/2021 Aggiunta colonna contabilita alla tabella singoli versamenti
ALTER TABLE singoli_versamenti ADD contabilita CLOB;


-- 11/05/2021 Aggiunto identificativo tracciato notifica pagamenti
ALTER TABLE trac_notif_pag ADD identificativo VARCHAR2(255 CHAR);
UPDATE trac_notif_pag SET identificativo = nome_file;
ALTER TABLE trac_notif_pag MODIFY (identificativo NOT NULL);


-- 17/05/2021 Indice sulla colonna iuv della tabella eventi
CREATE INDEX idx_evt_iuv ON eventi (iuv);


-- 20/05/2021 Aggiunta colonna connettore hyper_sic_apk alla tabella domini
ALTER TABLE domini ADD cod_connettore_hyper_sic_apk VARCHAR2(255 CHAR);


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
    versamenti.data_creazione AS data_creazione,
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
    versamenti.data_creazione AS data_creazione,
    singoli_versamenti.contabilita AS contabilita
   FROM fr
     JOIN rendicontazioni ON rendicontazioni.id_fr = fr.id
     JOIN versamenti ON versamenti.iuv_versamento = rendicontazioni.iuv
     JOIN domini ON versamenti.id_dominio = domini.id AND domini.cod_dominio = fr.cod_dominio
     JOIN singoli_versamenti ON singoli_versamenti.id_versamento = versamenti.id
  WHERE rendicontazioni.esito = 9;


CREATE VIEW v_riscossioni AS
 SELECT a.cod_dominio,
    a.iuv,
    a.iur,
    a.cod_flusso,
    a.fr_iur,
    a.data_regolamento,
    a.importo_totale_pagamenti,
    a.numero_pagamenti,
    a.importo_pagato,
    a.data_pagamento,
    a.cod_singolo_versamento_ente,
    a.indice_dati,
    a.cod_versamento_ente,
    applicazioni.cod_applicazione,
    a.debitore_identificativo AS identificativo_debitore,
    a.cod_anno_tributario AS anno,
    tipi_versamento.cod_tipo_versamento,
    tipi_tributo.cod_tributo AS cod_entrata,
    tipi_versamento.descrizione AS descr_tipo_versamento,
    a.debitore_anagrafica,
    a.cod_psp,
    a.ragione_sociale_psp,
    a.cod_rata,
    a.id_documento,
    a.causale_versamento,
    a.importo_versamento,
    a.numero_avviso,
    a.iuv_pagamento,
    a.data_scadenza,
    a.data_creazione,
    a.contabilita
   FROM ( SELECT v_riscossioni_senza_rpt.cod_dominio,
            v_riscossioni_senza_rpt.iuv,
            v_riscossioni_senza_rpt.iur,
            v_riscossioni_senza_rpt.cod_flusso,
            v_riscossioni_senza_rpt.fr_iur,
            v_riscossioni_senza_rpt.data_regolamento,
            v_riscossioni_senza_rpt.importo_totale_pagamenti,
            v_riscossioni_senza_rpt.numero_pagamenti,
            v_riscossioni_senza_rpt.importo_pagato,
            v_riscossioni_senza_rpt.data_pagamento,
            v_riscossioni_senza_rpt.cod_singolo_versamento_ente,
            v_riscossioni_senza_rpt.indice_dati,
            v_riscossioni_senza_rpt.cod_versamento_ente,
            v_riscossioni_senza_rpt.id_applicazione,
            v_riscossioni_senza_rpt.debitore_identificativo,
            v_riscossioni_senza_rpt.id_tipo_versamento,
            v_riscossioni_senza_rpt.cod_anno_tributario,
            v_riscossioni_senza_rpt.id_tributo,
		    v_riscossioni_senza_rpt.debitore_anagrafica,
		    v_riscossioni_senza_rpt.cod_psp,
		    v_riscossioni_senza_rpt.ragione_sociale_psp,
		    v_riscossioni_senza_rpt.cod_rata,
		    v_riscossioni_senza_rpt.id_documento,
		    v_riscossioni_senza_rpt.causale_versamento,
		    v_riscossioni_senza_rpt.importo_versamento,
		    v_riscossioni_senza_rpt.numero_avviso,
		    v_riscossioni_senza_rpt.iuv_pagamento,
		    v_riscossioni_senza_rpt.data_scadenza,
		    v_riscossioni_senza_rpt.data_creazione,
		    v_riscossioni_senza_rpt.contabilita
           FROM v_riscossioni_senza_rpt
        UNION
         SELECT v_riscossioni_con_rpt.cod_dominio,
            v_riscossioni_con_rpt.iuv,
            v_riscossioni_con_rpt.iur,
            v_riscossioni_con_rpt.cod_flusso,
            v_riscossioni_con_rpt.fr_iur,
            v_riscossioni_con_rpt.data_regolamento,
            v_riscossioni_con_rpt.importo_totale_pagamenti,
            v_riscossioni_con_rpt.numero_pagamenti,
            v_riscossioni_con_rpt.importo_pagato,
            v_riscossioni_con_rpt.data_pagamento,
            v_riscossioni_con_rpt.cod_singolo_versamento_ente,
            v_riscossioni_con_rpt.indice_dati,
            v_riscossioni_con_rpt.cod_versamento_ente,
            v_riscossioni_con_rpt.id_applicazione,
            v_riscossioni_con_rpt.debitore_identificativo,
            v_riscossioni_con_rpt.id_tipo_versamento,
            v_riscossioni_con_rpt.cod_anno_tributario,
            v_riscossioni_con_rpt.id_tributo,
		    v_riscossioni_con_rpt.debitore_anagrafica,
		    v_riscossioni_con_rpt.cod_psp,
		    v_riscossioni_con_rpt.ragione_sociale_psp,
		    v_riscossioni_con_rpt.cod_rata,
		    v_riscossioni_con_rpt.id_documento,
		    v_riscossioni_con_rpt.causale_versamento,
		    v_riscossioni_con_rpt.importo_versamento,
		    v_riscossioni_con_rpt.numero_avviso,
		    v_riscossioni_con_rpt.iuv_pagamento,
		    v_riscossioni_con_rpt.data_scadenza,
		    v_riscossioni_con_rpt.data_creazione,
		    v_riscossioni_con_rpt.contabilita
           FROM v_riscossioni_con_rpt) a
     JOIN applicazioni ON a.id_applicazione = applicazioni.id 
     LEFT JOIN tipi_versamento ON a.id_tipo_versamento = tipi_versamento.id 
     LEFT JOIN tributi ON a.id_tributo = tributi.id 
     LEFT JOIN tipi_tributo ON tributi.id_tipo_tributo = tipi_tributo.id;


-- 28/05/2021
DROP VIEW IF EXISTS v_rendicontazioni_ext;


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
    singoli_versamenti.contabilita AS sng_contabilita,
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
     
     
-- 22/06/2021 API-Rendicontazione V3, nuovi campi tabella incassi

ALTER TABLE incassi ADD identificativo VARCHAR2(35 CHAR);
UPDATE incassi SET identificativo = trn;
ALTER TABLE incassi MODIFY (identificativo NOT NULL);

ALTER TABLE incassi ADD stato VARCHAR2(35 CHAR);
UPDATE incassi SET stato = 'ACQUISITO';
ALTER TABLE incassi MODIFY (stato NOT NULL);

ALTER TABLE incassi ADD iuv VARCHAR2(35 CHAR);
ALTER TABLE incassi ADD cod_flusso_rendicontazione VARCHAR2(35 CHAR);
ALTER TABLE incassi ADD descrizione_stato VARCHAR2(255 CHAR);

ALTER TABLE incassi DROP CONSTRAINT unique_incassi_1;
ALTER TABLE incassi ADD CONSTRAINT unique_incassi_1 UNIQUE (cod_dominio,identificativo);

ALTER TABLE incassi MODIFY (causale NULL);

insert into sonde(nome, classe, soglia_warn, soglia_error) values ('riconciliazioni', 'org.openspcoop2.utils.sonde.impl.SondaBatch', 86400000, 172800000);
insert into sonde(nome, classe, soglia_warn, soglia_error) values ('check-riconciliazioni', 'org.openspcoop2.utils.sonde.impl.SondaCoda', 10, 100);

-- 14/04/2021 Aggiunta gestione campo ozionale lingua secondaria causale nel trasformatore dei tracciati csv.
UPDATE configurazione SET valore = '{"tipo":"freemarker","intestazione":"idA2A,idPendenza,idDominio,tipoPendenza,numeroAvviso,pdfAvviso,tipoSoggettoPagatore,identificativoPagatore,anagraficaPagatore,indirizzoPagatore,civicoPagatore,capPagatore,localitaPagatore,provinciaPagatore,nazionePagatore,emailPagatore,cellularePagatore,errore","richiesta":"\"PCNhc3NpZ24gY3N2VXRpbHMgPSBjbGFzc1siaXQuZ292cGF5LmNvcmUudXRpbHMuQ1NWVXRpbHMiXS5nZXRJbnN0YW5jZSgpPgoKPCNhc3NpZ24gY3N2UmVjb3JkID0gY3N2VXRpbHMuZ2V0Q1NWUmVjb3JkKGxpbmVhQ3N2UmljaGllc3RhKT4KPCNpZiBjc3ZVdGlscy5pc09wZXJhemlvbmVBbm51bGxhbWVudG8oY3N2UmVjb3JkLCAxMik+Cgk8I2Fzc2lnbiB0bXA9Y29udGV4dD9hcGkucHV0KCJ0aXBvT3BlcmF6aW9uZSIsICJERUwiKSEvPgp7CgkiaWRBMkEiOiAke2NzdlV0aWxzLnRvSnNvblZhbHVlKGNzdlJlY29yZCwgMCl9LAoJImlkUGVuZGVuemEiOiAke2NzdlV0aWxzLnRvSnNvblZhbHVlKGNzdlJlY29yZCwgMSl9Cn0KPCNlbHNlPgo8I2lmICFjc3ZVdGlscy5pc0VtcHR5KGNzdlJlY29yZCwgODIpPgogIDwjYXNzaWduIGRhdGFBdnZpc29TdHJpbmcgPSBjc3ZSZWNvcmQuZ2V0KDgyKT4KICA8I2lmIGRhdGFBdnZpc29TdHJpbmcuZXF1YWxzKCJNQUkiKT4KICAJPCNhc3NpZ24gdG1wPWNvbnRleHQ/YXBpLnB1dCgiYXZ2aXNhdHVyYSIsIGZhbHNlKSEvPgogIDwjZWxzZT4KICAJPCNhc3NpZ24gc2RmVXRpbHMgPSBjbGFzc1siaXQuZ292cGF5LmNvcmUudXRpbHMuU2ltcGxlRGF0ZUZvcm1hdFV0aWxzIl0uZ2V0SW5zdGFuY2UoKT4KICAJPCNhc3NpZ24gZGF0YUF2dmlzYXR1cmFEYXRlID0gc2RmVXRpbHMuZ2V0RGF0YUF2dmlzYXR1cmEoZGF0YUF2dmlzb1N0cmluZywgImRhdGFBdnZpc2F0dXJhIikgPgogIAk8I2Fzc2lnbiBjb250ZXh0VE1QID0gY29udGV4dCA+CiAgCTwjYXNzaWduIHRtcD1jb250ZXh0P2FwaS5wdXQoImRhdGFBdnZpc2F0dXJhIiwgZGF0YUF2dmlzYXR1cmFEYXRlKSEvPgogIDwvI2lmPgo8LyNpZj4KewoJImlkQTJBIjogJHtjc3ZVdGlscy50b0pzb25WYWx1ZShjc3ZSZWNvcmQsIDApfSwKCSJpZFBlbmRlbnphIjogJHtjc3ZVdGlscy50b0pzb25WYWx1ZShjc3ZSZWNvcmQsIDEpfSwKCSJpZERvbWluaW8iOiAke2NzdlV0aWxzLnRvSnNvblZhbHVlKGNzdlJlY29yZCwgMil9LAoJIm51bWVyb0F2dmlzbyI6ICR7Y3N2VXRpbHMudG9Kc29uVmFsdWUoY3N2UmVjb3JkLCAzKX0sCQoJImlkVGlwb1BlbmRlbnphIjogJHtjc3ZVdGlscy50b0pzb25WYWx1ZShjc3ZSZWNvcmQsIDQpfSwKCSJpZFVuaXRhT3BlcmF0aXZhIjogJHtjc3ZVdGlscy50b0pzb25WYWx1ZShjc3ZSZWNvcmQsIDUpfSwKIAkiY2F1c2FsZSI6ICR7Y3N2VXRpbHMudG9Kc29uVmFsdWUoY3N2UmVjb3JkLCA2KX0sCiAJImFubm9SaWZlcmltZW50byI6ICR7Y3N2VXRpbHMudG9Kc29uVmFsdWUoY3N2UmVjb3JkLCA3KX0sCiAJImNhcnRlbGxhUGFnYW1lbnRvIjogJHtjc3ZVdGlscy50b0pzb25WYWx1ZShjc3ZSZWNvcmQsIDgpfSwKIAk8I2lmICFjc3ZVdGlscy5pc0VtcHR5KGNzdlJlY29yZCwgOSk+ImRhdGlBbGxlZ2F0aSI6ICR7Y3N2UmVjb3JkLmdldCg5KX0sPC8jaWY+CiAJImRpcmV6aW9uZSI6ICR7Y3N2VXRpbHMudG9Kc29uVmFsdWUoY3N2UmVjb3JkLCAxMCl9LAogCSJkaXZpc2lvbmUiOiAke2NzdlV0aWxzLnRvSnNvblZhbHVlKGNzdlJlY29yZCwgMTEpfSwKIAkiaW1wb3J0byI6ICR7Y3N2VXRpbHMudG9Kc29uVmFsdWUoY3N2UmVjb3JkLCAxMil9LAoJImRhdGFWYWxpZGl0YSI6ICR7Y3N2VXRpbHMudG9Kc29uVmFsdWUoY3N2UmVjb3JkLCAxMyl9LAoJImRhdGFTY2FkZW56YSI6ICR7Y3N2VXRpbHMudG9Kc29uVmFsdWUoY3N2UmVjb3JkLCAxNCl9LAoJInRhc3Nvbm9taWFBdnZpc28iOiAke2NzdlV0aWxzLnRvSnNvblZhbHVlKGNzdlJlY29yZCwgMTUpfSwKCTwjaWYgIWNzdlV0aWxzLmlzRW1wdHkoY3N2UmVjb3JkLCA4Myk+CgkiZG9jdW1lbnRvIjogewoJCSJpZGVudGlmaWNhdGl2byI6ICR7Y3N2VXRpbHMudG9Kc29uVmFsdWUoY3N2UmVjb3JkLCA4Myl9LAoJCTwjaWYgIWNzdlV0aWxzLmlzRW1wdHkoY3N2UmVjb3JkLCA4NSk+CgkJCQoJCQk8I2Fzc2lnbiByYXRhU3RyaW5nID0gY3N2UmVjb3JkLmdldCg4NSk+CgkJCTwjaWYgcmF0YVN0cmluZz9tYXRjaGVzKCdbMC05XSonKT4KCQkJCSJyYXRhIjogJHtjc3ZVdGlscy50b0pzb25WYWx1ZShjc3ZSZWNvcmQsIDg1KX0sCQoJCQk8I2Vsc2U+CgkJCQkic29nbGlhIjogewoJCQkJCSJ0aXBvIjogIiR7cmF0YVN0cmluZ1swLi40XX0iLAoJCQkJCSJnaW9ybmkiOiAke3JhdGFTdHJpbmdbNS4uXX0gCgkJCQl9LAkgIAoJCQk8LyNpZj4KCQk8LyNpZj4KCQkiZGVzY3JpemlvbmUiOiAke2NzdlV0aWxzLnRvSnNvblZhbHVlKGNzdlJlY29yZCwgODQpfQoJfSwKCTwvI2lmPgoJPCNpZiAhY3N2VXRpbHMuaXNFbXB0eShjc3ZSZWNvcmQsIDg2KT4KCQk8I2Fzc2lnbiBsaW5ndWFTZWNvbmRhcmlhU3RyaW5nID0gY3N2UmVjb3JkLmdldCg4NikhLz4KCQk8I2Fzc2lnbiBtYXBwZWRBcnJheV9zdHJpbmcgPSBsaW5ndWFTZWNvbmRhcmlhU3RyaW5nP3NwbGl0KCJ8IikgIS8+CgkJCgkicHJvcHJpZXRhIjogewoJCSJsaW5ndWFTZWNvbmRhcmlhIiA6ICIke21hcHBlZEFycmF5X3N0cmluZ1swXX0iCgkJPCNpZiAobWFwcGVkQXJyYXlfc3RyaW5nP3NpemUgPiAxKT4KCQkJLCAibGluZ3VhU2Vjb25kYXJpYUNhdXNhbGUiIDogIiR7bWFwcGVkQXJyYXlfc3RyaW5nWzFdfSIKCQk8LyNpZj4KCX0sCgk8LyNpZj4KCSJzb2dnZXR0b1BhZ2F0b3JlIjogewoJCSJ0aXBvIjogJHtjc3ZVdGlscy50b0pzb25WYWx1ZShjc3ZSZWNvcmQsIDE2KX0sCgkJImlkZW50aWZpY2F0aXZvIjogJHtjc3ZVdGlscy50b0pzb25WYWx1ZShjc3ZSZWNvcmQsIDE3KX0sCgkJImFuYWdyYWZpY2EiOiAke2NzdlV0aWxzLnRvSnNvblZhbHVlKGNzdlJlY29yZCwgMTgpfSwKCQkiaW5kaXJpenpvIjogJHtjc3ZVdGlscy50b0pzb25WYWx1ZShjc3ZSZWNvcmQsIDE5KX0sCgkJImNpdmljbyI6ICR7Y3N2VXRpbHMudG9Kc29uVmFsdWUoY3N2UmVjb3JkLCAyMCl9LAoJCSJjYXAiOiAke2NzdlV0aWxzLnRvSnNvblZhbHVlKGNzdlJlY29yZCwgMjEpfSwKCQkibG9jYWxpdGEiOiAke2NzdlV0aWxzLnRvSnNvblZhbHVlKGNzdlJlY29yZCwgMjIpfSwKCQkicHJvdmluY2lhIjogJHtjc3ZVdGlscy50b0pzb25WYWx1ZShjc3ZSZWNvcmQsIDIzKX0sCgkJIm5hemlvbmUiOiAke2NzdlV0aWxzLnRvSnNvblZhbHVlKGNzdlJlY29yZCwgMjQpfSwKCQkiZW1haWwiOiAke2NzdlV0aWxzLnRvSnNvblZhbHVlKGNzdlJlY29yZCwgMjUpfSwKCQkiY2VsbHVsYXJlIjogJHtjc3ZVdGlscy50b0pzb25WYWx1ZShjc3ZSZWNvcmQsIDI2KX0KCX0sCgkidm9jaSI6IFsKCQl7CgkJCSJpZFZvY2VQZW5kZW56YSI6ICR7Y3N2VXRpbHMudG9Kc29uVmFsdWUoY3N2UmVjb3JkLCAyNyl9LAoJCQkiaW1wb3J0byI6ICR7Y3N2VXRpbHMudG9Kc29uVmFsdWUoY3N2UmVjb3JkLCAyOCl9LAoJCQkiZGVzY3JpemlvbmUiOiAke2NzdlV0aWxzLnRvSnNvblZhbHVlKGNzdlJlY29yZCwgMjkpfSwKCQkJPCNpZiAhY3N2VXRpbHMuaXNFbXB0eShjc3ZSZWNvcmQsIDM0KT4KCQkJImNvZEVudHJhdGEiOiAke2NzdlV0aWxzLnRvSnNvblZhbHVlKGNzdlJlY29yZCwgMzQpfQoJCQk8I2Vsc2VpZiAhY3N2VXRpbHMuaXNFbXB0eShjc3ZSZWNvcmQsIDM1KT4KCQkJInRpcG9Cb2xsbyI6ICR7Y3N2VXRpbHMudG9Kc29uVmFsdWUoY3N2UmVjb3JkLCAzNSl9LAogICAgICAgCQkiaGFzaERvY3VtZW50byI6ICR7Y3N2VXRpbHMudG9Kc29uVmFsdWUoY3N2UmVjb3JkLCAzNil9LAogICAgICAJCSJwcm92aW5jaWFSZXNpZGVuemEiOiAke2NzdlV0aWxzLnRvSnNvblZhbHVlKGNzdlJlY29yZCwgMzcpfQoJCQk8I2Vsc2U+CgkJCSJpYmFuQWNjcmVkaXRvIjogJHtjc3ZVdGlscy50b0pzb25WYWx1ZShjc3ZSZWNvcmQsIDMwKX0sCgkJCSJpYmFuQXBwb2dnaW8iOiAke2NzdlV0aWxzLnRvSnNvblZhbHVlKGNzdlJlY29yZCwgMzEpfSwKCQkJInRpcG9Db250YWJpbGl0YSI6ICR7Y3N2VXRpbHMudG9Kc29uVmFsdWUoY3N2UmVjb3JkLCAzMil9LAoJCQkiY29kaWNlQ29udGFiaWxpdGEiOiAke2NzdlV0aWxzLnRvSnNvblZhbHVlKGNzdlJlY29yZCwgMzMpfQoJCQk8LyNpZj4KCgkJfQoJCTwjaWYgIWNzdlV0aWxzLmlzRW1wdHkoY3N2UmVjb3JkLCAzOCk+CgkJLHsKCQkJImlkVm9jZVBlbmRlbnphIjogJHtjc3ZVdGlscy50b0pzb25WYWx1ZShjc3ZSZWNvcmQsIDM4KX0sCgkJCSJpbXBvcnRvIjogJHtjc3ZVdGlscy50b0pzb25WYWx1ZShjc3ZSZWNvcmQsIDM5KX0sCgkJCSJkZXNjcml6aW9uZSI6ICR7Y3N2VXRpbHMudG9Kc29uVmFsdWUoY3N2UmVjb3JkLCA0MCl9LAoJCQk8I2lmICFjc3ZVdGlscy5pc0VtcHR5KGNzdlJlY29yZCwgNDUpPgoJCQkiY29kRW50cmF0YSI6ICR7Y3N2VXRpbHMudG9Kc29uVmFsdWUoY3N2UmVjb3JkLCA0NSl9CgkJCTwjZWxzZWlmICFjc3ZVdGlscy5pc0VtcHR5KGNzdlJlY29yZCwgNDYpPgoJCQkidGlwb0JvbGxvIjogJHtjc3ZVdGlscy50b0pzb25WYWx1ZShjc3ZSZWNvcmQsIDQ2KX0sCiAgICAgICAJCSJoYXNoRG9jdW1lbnRvIjogJHtjc3ZVdGlscy50b0pzb25WYWx1ZShjc3ZSZWNvcmQsIDQ3KX0sCiAgICAgIAkJInByb3ZpbmNpYVJlc2lkZW56YSI6ICR7Y3N2VXRpbHMudG9Kc29uVmFsdWUoY3N2UmVjb3JkLCA0OCl9CgkJCTwjZWxzZT4KCQkJImliYW5BY2NyZWRpdG8iOiAke2NzdlV0aWxzLnRvSnNvblZhbHVlKGNzdlJlY29yZCwgNDEpfSwKCQkJImliYW5BcHBvZ2dpbyI6ICR7Y3N2VXRpbHMudG9Kc29uVmFsdWUoY3N2UmVjb3JkLCA0Mil9LAoJCQkidGlwb0NvbnRhYmlsaXRhIjogJHtjc3ZVdGlscy50b0pzb25WYWx1ZShjc3ZSZWNvcmQsIDQzKX0sCgkJCSJjb2RpY2VDb250YWJpbGl0YSI6ICR7Y3N2VXRpbHMudG9Kc29uVmFsdWUoY3N2UmVjb3JkLCA0NCl9CgkJCTwvI2lmPgoJCX0KCQk8LyNpZj4KCQk8I2lmICFjc3ZVdGlscy5pc0VtcHR5KGNzdlJlY29yZCwgNDkpPgoJCSx7CgkJCSJpZFZvY2VQZW5kZW56YSI6ICR7Y3N2VXRpbHMudG9Kc29uVmFsdWUoY3N2UmVjb3JkLCA0OSl9LAoJCQkiaW1wb3J0byI6ICR7Y3N2VXRpbHMudG9Kc29uVmFsdWUoY3N2UmVjb3JkLCA1MCl9LAoJCQkiZGVzY3JpemlvbmUiOiAke2NzdlV0aWxzLnRvSnNvblZhbHVlKGNzdlJlY29yZCwgNTEpfSwKCQkJPCNpZiAhY3N2VXRpbHMuaXNFbXB0eShjc3ZSZWNvcmQsIDU2KT4KCQkJImNvZEVudHJhdGEiOiAke2NzdlV0aWxzLnRvSnNvblZhbHVlKGNzdlJlY29yZCwgNTYpfQoJCQk8I2Vsc2VpZiAhY3N2VXRpbHMuaXNFbXB0eShjc3ZSZWNvcmQsIDU3KT4KCQkJInRpcG9Cb2xsbyI6ICR7Y3N2VXRpbHMudG9Kc29uVmFsdWUoY3N2UmVjb3JkLCA1Nyl9LAogICAgICAgCQkiaGFzaERvY3VtZW50byI6ICR7Y3N2VXRpbHMudG9Kc29uVmFsdWUoY3N2UmVjb3JkLCA1OCl9LAogICAgICAJCSJwcm92aW5jaWFSZXNpZGVuemEiOiAke2NzdlV0aWxzLnRvSnNvblZhbHVlKGNzdlJlY29yZCwgNTkpfQoJCQk8I2Vsc2U+CgkJCSJpYmFuQWNjcmVkaXRvIjogJHtjc3ZVdGlscy50b0pzb25WYWx1ZShjc3ZSZWNvcmQsIDUyKX0sCgkJCSJpYmFuQXBwb2dnaW8iOiAke2NzdlV0aWxzLnRvSnNvblZhbHVlKGNzdlJlY29yZCwgNTMpfSwKCQkJInRpcG9Db250YWJpbGl0YSI6ICR7Y3N2VXRpbHMudG9Kc29uVmFsdWUoY3N2UmVjb3JkLCA1NCl9LAoJCQkiY29kaWNlQ29udGFiaWxpdGEiOiAke2NzdlV0aWxzLnRvSnNvblZhbHVlKGNzdlJlY29yZCwgNTUpfQoJCQk8LyNpZj4KCQl9CgkJPC8jaWY+CgkJPCNpZiAhY3N2VXRpbHMuaXNFbXB0eShjc3ZSZWNvcmQsIDYwKT4KCQksewoJCQkiaWRWb2NlUGVuZGVuemEiOiAke2NzdlV0aWxzLnRvSnNvblZhbHVlKGNzdlJlY29yZCwgNjApfSwKCQkJImltcG9ydG8iOiAke2NzdlV0aWxzLnRvSnNvblZhbHVlKGNzdlJlY29yZCwgNjEpfSwKCQkJImRlc2NyaXppb25lIjogJHtjc3ZVdGlscy50b0pzb25WYWx1ZShjc3ZSZWNvcmQsIDYyKX0sCgkJCTwjaWYgIWNzdlV0aWxzLmlzRW1wdHkoY3N2UmVjb3JkLCA2Nyk+CgkJCSJjb2RFbnRyYXRhIjogJHtjc3ZVdGlscy50b0pzb25WYWx1ZShjc3ZSZWNvcmQsIDY3KX0KCQkJPCNlbHNlaWYgIWNzdlV0aWxzLmlzRW1wdHkoY3N2UmVjb3JkLCA2OCk+CgkJCSJ0aXBvQm9sbG8iOiAke2NzdlV0aWxzLnRvSnNvblZhbHVlKGNzdlJlY29yZCwgNjgpfSwKICAgICAgIAkJImhhc2hEb2N1bWVudG8iOiAke2NzdlV0aWxzLnRvSnNvblZhbHVlKGNzdlJlY29yZCwgNjkpfSwKICAgICAgCQkicHJvdmluY2lhUmVzaWRlbnphIjogJHtjc3ZVdGlscy50b0pzb25WYWx1ZShjc3ZSZWNvcmQsIDcwKX0KCQkJPCNlbHNlPgoJCQkiaWJhbkFjY3JlZGl0byI6ICR7Y3N2VXRpbHMudG9Kc29uVmFsdWUoY3N2UmVjb3JkLCA2Myl9LAoJCQkiaWJhbkFwcG9nZ2lvIjogJHtjc3ZVdGlscy50b0pzb25WYWx1ZShjc3ZSZWNvcmQsIDY0KX0sCgkJCSJ0aXBvQ29udGFiaWxpdGEiOiAke2NzdlV0aWxzLnRvSnNvblZhbHVlKGNzdlJlY29yZCwgNjUpfSwKCQkJImNvZGljZUNvbnRhYmlsaXRhIjogJHtjc3ZVdGlscy50b0pzb25WYWx1ZShjc3ZSZWNvcmQsIDY2KX0KCQkJPC8jaWY+CgkJfQoJCTwvI2lmPgoJCTwjaWYgIWNzdlV0aWxzLmlzRW1wdHkoY3N2UmVjb3JkLCA3MSk+CgkJLHsKCQkJImlkVm9jZVBlbmRlbnphIjogJHtjc3ZVdGlscy50b0pzb25WYWx1ZShjc3ZSZWNvcmQsIDcxKX0sCgkJCSJpbXBvcnRvIjogJHtjc3ZVdGlscy50b0pzb25WYWx1ZShjc3ZSZWNvcmQsIDcyKX0sCgkJCSJkZXNjcml6aW9uZSI6ICR7Y3N2VXRpbHMudG9Kc29uVmFsdWUoY3N2UmVjb3JkLCA3Myl9LAoJCQk8I2lmICFjc3ZVdGlscy5pc0VtcHR5KGNzdlJlY29yZCwgNzgpPgoJCQkiY29kRW50cmF0YSI6ICR7Y3N2VXRpbHMudG9Kc29uVmFsdWUoY3N2UmVjb3JkLCA3OCl9CgkJCTwjZWxzZWlmICFjc3ZVdGlscy5pc0VtcHR5KGNzdlJlY29yZCwgNzkpPgoJCQkidGlwb0JvbGxvIjogJHtjc3ZVdGlscy50b0pzb25WYWx1ZShjc3ZSZWNvcmQsIDc5KX0sCiAgICAgICAJCSJoYXNoRG9jdW1lbnRvIjogJHtjc3ZVdGlscy50b0pzb25WYWx1ZShjc3ZSZWNvcmQsIDgwKX0sCiAgICAgIAkJInByb3ZpbmNpYVJlc2lkZW56YSI6ICR7Y3N2VXRpbHMudG9Kc29uVmFsdWUoY3N2UmVjb3JkLCA4MSl9CgkJCTwjZWxzZT4KCQkJImliYW5BY2NyZWRpdG8iOiAke2NzdlV0aWxzLnRvSnNvblZhbHVlKGNzdlJlY29yZCwgNzQpfSwKCQkJImliYW5BcHBvZ2dpbyI6ICR7Y3N2VXRpbHMudG9Kc29uVmFsdWUoY3N2UmVjb3JkLCA3NSl9LAoJCQkidGlwb0NvbnRhYmlsaXRhIjogJHtjc3ZVdGlscy50b0pzb25WYWx1ZShjc3ZSZWNvcmQsIDc2KX0sCgkJCSJjb2RpY2VDb250YWJpbGl0YSI6ICR7Y3N2VXRpbHMudG9Kc29uVmFsdWUoY3N2UmVjb3JkLCA3Nyl9CgkJCTwvI2lmPgoJCX0KCQk8LyNpZj4KCgldCn0KPC8jaWY+\"","risposta":"\"PCNhc3NpZ24gY3N2VXRpbHMgPSBjbGFzc1siaXQuZ292cGF5LmNvcmUudXRpbHMuQ1NWVXRpbHMiXS5nZXRJbnN0YW5jZSgpIC8+CjwjaWYgZXNpdG9PcGVyYXppb25lID09ICJFU0VHVUlUT19PSyI+CjwjYXNzaWduIGlkQTJBID0gYXBwbGljYXppb25lLmdldENvZEFwcGxpY2F6aW9uZSgpISAvPgo8I2Fzc2lnbiBpZFBlbmRlbnphID0gdmVyc2FtZW50by5nZXRDb2RWZXJzYW1lbnRvRW50ZSgpISAvPgo8I2Fzc2lnbiBpZERvbWluaW8gPSBkb21pbmlvLmdldENvZERvbWluaW8oKSAvPgo8I2Fzc2lnbiB0aXBvUGVuZGVuemEgPSBpZFRpcG9WZXJzYW1lbnRvIC8+CjwjYXNzaWduIG51bWVyb0F2dmlzbyA9IHZlcnNhbWVudG8uZ2V0TnVtZXJvQXZ2aXNvKCkhIC8+CjwjaWYgbnVtZXJvQXZ2aXNvP2hhc19jb250ZW50PgoJPCNhc3NpZ24gcGRmQXZ2aXNvID0gaWREb21pbmlvICsgIl8iICsgbnVtZXJvQXZ2aXNvICsgIi5wZGYiIC8+CjwvI2lmPgo8I2Fzc2lnbiBpZERvY3VtZW50byA9IHZlcnNhbWVudG8uZ2V0SWREb2N1bWVudG8oKSEgLz4KPCNpZiBkb2N1bWVudG8/aGFzX2NvbnRlbnQ+CiAgICA8I2Fzc2lnbiBudW1lcm9Eb2N1bWVudG8gPSBkb2N1bWVudG8uZ2V0Q29kRG9jdW1lbnRvKCkgLz4KCTwjYXNzaWduIHBkZkF2dmlzbyA9IGlkRG9taW5pbyArICJfRE9DXyIgKyBudW1lcm9Eb2N1bWVudG8gKyAiLnBkZiIgLz4KPC8jaWY+CjwjYXNzaWduIHRpcG8gPSB2ZXJzYW1lbnRvLmdldEFuYWdyYWZpY2FEZWJpdG9yZSgpLmdldFRpcG8oKS50b1N0cmluZygpIC8+CjwjYXNzaWduIGlkZW50aWZpY2F0aXZvID0gdmVyc2FtZW50by5nZXRBbmFncmFmaWNhRGViaXRvcmUoKS5nZXRDb2RVbml2b2NvKCkhIC8+CjwjYXNzaWduIGFuYWdyYWZpY2EgPSB2ZXJzYW1lbnRvLmdldEFuYWdyYWZpY2FEZWJpdG9yZSgpLmdldFJhZ2lvbmVTb2NpYWxlKCkhIC8+CjwjYXNzaWduIGluZGlyaXp6byA9IHZlcnNhbWVudG8uZ2V0QW5hZ3JhZmljYURlYml0b3JlKCkuZ2V0SW5kaXJpenpvKCkhIC8+CjwjYXNzaWduIGNpdmljbyA9IHZlcnNhbWVudG8uZ2V0QW5hZ3JhZmljYURlYml0b3JlKCkuZ2V0Q2l2aWNvKCkhIC8+CjwjYXNzaWduIGNhcCA9IHZlcnNhbWVudG8uZ2V0QW5hZ3JhZmljYURlYml0b3JlKCkuZ2V0Q2FwKCkhIC8+CjwjYXNzaWduIGxvY2FsaXRhID0gdmVyc2FtZW50by5nZXRBbmFncmFmaWNhRGViaXRvcmUoKS5nZXRMb2NhbGl0YSgpISAvPgo8I2Fzc2lnbiBwcm92aW5jaWEgPSB2ZXJzYW1lbnRvLmdldEFuYWdyYWZpY2FEZWJpdG9yZSgpLmdldFByb3ZpbmNpYSgpISAvPgo8I2Fzc2lnbiBuYXppb25lID0gdmVyc2FtZW50by5nZXRBbmFncmFmaWNhRGViaXRvcmUoKS5nZXROYXppb25lKCkhIC8+CjwjYXNzaWduIGVtYWlsID0gdmVyc2FtZW50by5nZXRBbmFncmFmaWNhRGViaXRvcmUoKS5nZXRFbWFpbCgpISAvPgo8I2Fzc2lnbiBjZWxsdWxhcmUgPSB2ZXJzYW1lbnRvLmdldEFuYWdyYWZpY2FEZWJpdG9yZSgpLmdldENlbGx1bGFyZSgpISAvPgo8I2lmIHRpcG9PcGVyYXppb25lID09ICJBREQiPgoJPCNhc3NpZ24gY3N2UmVjb3JkID0gY3N2VXRpbHMudG9Dc3YoaWRBMkEsIGlkUGVuZGVuemEsIGlkRG9taW5pbywgdGlwb1BlbmRlbnphLCBudW1lcm9BdnZpc28sIHBkZkF2dmlzbywgdGlwbywgaWRlbnRpZmljYXRpdm8sIGFuYWdyYWZpY2EsIGluZGlyaXp6bywgY2l2aWNvLCBjYXAsIGxvY2FsaXRhLCBwcm92aW5jaWEsIG5hemlvbmUsIGVtYWlsLCBjZWxsdWxhcmUsICIiKSAvPgo8I2Vsc2U+Cgk8I2Fzc2lnbiBjc3ZSZWNvcmQgPSBjc3ZVdGlscy50b0NzdihpZEEyQSwgaWRQZW5kZW56YSwgaWREb21pbmlvLCB0aXBvUGVuZGVuemEsIG51bWVyb0F2dmlzbywgcGRmQXZ2aXNvLCB0aXBvLCBpZGVudGlmaWNhdGl2bywgYW5hZ3JhZmljYSwgaW5kaXJpenpvLCBjaXZpY28sIGNhcCwgbG9jYWxpdGEsIHByb3ZpbmNpYSwgbmF6aW9uZSwgZW1haWwsIGNlbGx1bGFyZSwgZGVzY3JpemlvbmVFc2l0b09wZXJhemlvbmUpIC8+CjwvI2lmPgo8I2Vsc2U+CjwjYXNzaWduIGNzdlJlY29yZCA9IGNzdlV0aWxzLnRvQ3N2KCIiLCAiIiwgIiIsICIiLCAiIiwgIiIsICIiLCAiIiwgIiIsICIiLCAiIiwgIiIsICIiLCAiIiwgIiIsICIiLCAiIiwgZGVzY3JpemlvbmVFc2l0b09wZXJhemlvbmUpIC8+CjwvI2lmPgoke2NzdlJlY29yZH0=\""}' WHERE nome = 'tracciato_csv';

