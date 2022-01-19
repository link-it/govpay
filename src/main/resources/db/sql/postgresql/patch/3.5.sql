-- 27/04/2021 Aggiunta colonna contabilita alla tabella singoli versamenti
ALTER TABLE singoli_versamenti ADD COLUMN contabilita TEXT;


-- 11/05/2021 Aggiunto identificativo tracciato notifica pagamenti
ALTER TABLE trac_notif_pag ADD COLUMN identificativo VARCHAR(255);
UPDATE trac_notif_pag SET identificativo = nome_file;
ALTER TABLE trac_notif_pag ALTER COLUMN identificativo SET NOT NULL;


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

     
-- 07/06/2021 Aggiunta colonna con la versione dell'RPT
DROP VIEW IF EXISTS v_rpt_versamenti;

ALTER TABLE rpt ADD COLUMN versione VARCHAR(35);
UPDATE rpt SET versione = 'SANP_230';
ALTER TABLE rpt ALTER COLUMN versione SET NOT NULL;

CREATE VIEW v_rpt_versamenti AS
 SELECT 
rpt.cod_carrello as cod_carrello,                   
rpt.iuv as iuv,                            
rpt.ccp as ccp,                            
rpt.cod_dominio as cod_dominio,                    
rpt.cod_msg_richiesta as cod_msg_richiesta,              
rpt.data_msg_richiesta as data_msg_richiesta,             
rpt.stato as stato,                          
rpt.descrizione_stato as descrizione_stato,              
rpt.cod_sessione as cod_sessione,                   
rpt.cod_sessione_portale as cod_sessione_portale,           
rpt.psp_redirect_url as psp_redirect_url,               
rpt.xml_rpt as xml_rpt,                        
rpt.data_aggiornamento_stato as data_aggiornamento_stato,       
rpt.callback_url as callback_url,                   
rpt.modello_pagamento as modello_pagamento,              
rpt.cod_msg_ricevuta as cod_msg_ricevuta,               
rpt.data_msg_ricevuta as data_msg_ricevuta,              
rpt.cod_esito_pagamento as cod_esito_pagamento,            
rpt.importo_totale_pagato as importo_totale_pagato,          
rpt.xml_rt as xml_rt,                         
rpt.cod_canale as cod_canale,                     
rpt.cod_psp as cod_psp,                        
rpt.cod_intermediario_psp as cod_intermediario_psp,          
rpt.tipo_versamento as tipo_versamento,                
rpt.tipo_identificativo_attestante as tipo_identificativo_attestante, 
rpt.identificativo_attestante as identificativo_attestante,      
rpt.denominazione_attestante as denominazione_attestante,       
rpt.cod_stazione as cod_stazione,                   
rpt.cod_transazione_rpt as cod_transazione_rpt,            
rpt.cod_transazione_rt as cod_transazione_rt,             
rpt.stato_conservazione as stato_conservazione,            
rpt.descrizione_stato_cons as descrizione_stato_cons,         
rpt.data_conservazione as data_conservazione,             
rpt.bloccante as bloccante,                      
rpt.versione as versione, 
rpt.id as id,                             
rpt.id_pagamento_portale as id_pagamento_portale, 
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
    versamenti.src_debitore_identificativo as vrs_src_debitore_identificativ,
    versamenti.cod_rata as vrs_cod_rata,
    versamenti.id_documento as vrs_id_documento,
    versamenti.tipo as vrs_tipo,
    versamenti.proprieta as vrs_proprieta
FROM rpt JOIN versamenti ON versamenti.id = rpt.id_versamento;


-- 07/06/2021 Aggiunta colonna intermediato alla tabella domini
ALTER TABLE domini ADD COLUMN intermediato BOOLEAN;
UPDATE domini SET intermediato = true;
ALTER TABLE domini ALTER COLUMN intermediato SET NOT NULL;

-- 08/06/2021 Stazione di un dominio opzionale
ALTER TABLE domini ALTER COLUMN id_stazione DROP NOT NULL;


     

-- 22/06/2021 API-Rendicontazione V3, nuovi campi tabella incassi

ALTER TABLE incassi ADD COLUMN identificativo VARCHAR(35);
UPDATE incassi SET identificativo = trn;
ALTER TABLE incassi ALTER COLUMN identificativo SET NOT NULL;

ALTER TABLE incassi ADD COLUMN stato VARCHAR(35);
UPDATE incassi SET stato = 'ACQUISITO';
ALTER TABLE incassi ALTER COLUMN stato SET NOT NULL;

ALTER TABLE incassi ADD COLUMN iuv VARCHAR(35);
ALTER TABLE incassi ADD COLUMN cod_flusso_rendicontazione VARCHAR(35);
ALTER TABLE incassi ADD COLUMN descrizione_stato VARCHAR(255);

ALTER TABLE incassi DROP CONSTRAINT unique_incassi_1;
ALTER TABLE incassi ADD CONSTRAINT unique_incassi_1 UNIQUE (cod_dominio,identificativo);

ALTER TABLE incassi ALTER COLUMN causale DROP NOT NULL;

insert into sonde(nome, classe, soglia_warn, soglia_error) values ('riconciliazioni', 'org.openspcoop2.utils.sonde.impl.SondaBatch', 86400000, 172800000);
insert into sonde(nome, classe, soglia_warn, soglia_error) values ('check-riconciliazioni', 'org.openspcoop2.utils.sonde.impl.SondaCoda', 10, 100);


-- 14/07/2021 Batch per la chiusura delle RPT scadute
insert into sonde(nome, classe, soglia_warn, soglia_error) values ('rpt-scadute', 'org.openspcoop2.utils.sonde.impl.SondaBatch', 86400000, 172800000);
insert into sonde(nome, classe, soglia_warn, soglia_error) values ('check-rpt-scadute', 'org.openspcoop2.utils.sonde.impl.SondaCoda', 10, 100);


-- 20/07/2021 Fix anomalie per rendicontazione senza RT

update rendicontazioni set stato='OK', anomalie=null where anomalie = '007101#Il pagamento riferito dalla rendicontazione non risulta presente in base dati.' and esito=9;
update fr set stato='ACCETTATA', descrizione_stato = null where stato='ANOMALA' and id not in (select fr.id from fr join rendicontazioni on rendicontazioni.id_fr=fr.id where fr.stato='ANOMALA' and rendicontazioni.stato='ANOMALA');


-- 21/07/2021 Identificativo dominio nel singolo versamento per gestire le pendenze multibeneficiario
ALTER TABLE singoli_versamenti ADD COLUMN id_dominio BIGINT;
ALTER TABLE singoli_versamenti ADD CONSTRAINT fk_sng_id_dominio FOREIGN KEY (id_dominio) REFERENCES domini(id);


-- 15/10/2021 Identificativo RPT nella tabella notificheAppIo per gestire l'invio delle ricevute di pagamento
ALTER TABLE notifiche_app_io ADD COLUMN id_rpt BIGINT;
ALTER TABLE notifiche_app_io ADD CONSTRAINT fk_nai_id_rpt FOREIGN KEY (id_rpt) REFERENCES rpt(id);


-- 15/12/2021 Vista per le pendenze non ancora rendicontate
CREATE VIEW v_vrs_non_rnd AS 
 SELECT singoli_versamenti.cod_singolo_versamento_ente AS sng_cod_sing_vers_ente,
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
    versamenti.cod_rata AS vrs_cod_rata,
    versamenti.id_documento AS vrs_id_documento,
    versamenti.tipo AS vrs_tipo,
    versamenti.proprieta AS vrs_proprieta,
    pagamenti.id AS id,
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
    pagamenti.id_incasso AS pag_id_incasso,
    rpt.iuv AS rpt_iuv,
    rpt.ccp AS rpt_ccp,
    incassi.trn AS rnc_trn
   FROM pagamenti
     LEFT JOIN rendicontazioni ON rendicontazioni.id_pagamento = pagamenti.id
     LEFT JOIN singoli_versamenti ON pagamenti.id_singolo_versamento = singoli_versamenti.id
     LEFT JOIN versamenti ON versamenti.id = singoli_versamenti.id_versamento
     LEFT JOIN rpt ON pagamenti.id_rpt = rpt.id
     LEFT JOIN incassi ON pagamenti.id_incasso = incassi.id
  WHERE rendicontazioni.id IS NULL;
  
  
-- 20/12/2021 Patch per gestione delle rendicontazioni che non venivano messe in stato anomala quando non viene trovato il versamento corrispondente.
UPDATE rendicontazioni SET stato='ANOMALA', anomalie='007111#Il versamento risulta sconosciuto' WHERE stato='OK' AND id_singolo_versamento IS null;


-- 21/12/2021 Patch per la gestione del riferimento al pagamento di una rendicontazione che arriva prima della ricevuta.
UPDATE rendicontazioni SET id_pagamento = pagamenti.id 
	FROM fr, pagamenti 
	WHERE fr.id=rendicontazioni.id_fr 
	AND pagamenti.cod_dominio=fr.cod_dominio 
	AND rendicontazioni.iuv=pagamenti.iuv 
	AND rendicontazioni.iur=pagamenti.iur 
	AND rendicontazioni.id_pagamento IS NULL;

-- 30/12/2021 Patch rendicontazioni con riferimenti assenti
-- Imposto il riferimento al versamento
update rendicontazioni set id_singolo_versamento=singoli_versamenti.id
        FROM fr, versamenti, domini, singoli_versamenti 
        WHERE fr.id=rendicontazioni.id_fr 
        AND fr.cod_dominio=domini.cod_dominio 
        AND domini.id=versamenti.id_dominio 
        AND rendicontazioni.iuv=versamenti.iuv_versamento
        AND singoli_versamenti.id_versamento=versamenti.id
        AND rendicontazioni.id_singolo_versamento is null;

update rendicontazioni set stato='ANOMALA', anomalie='007101#Il pagamento riferito dalla rendicontazione non risulta presente in base dati.' where id_pagamento is null and stato='OK' and esito=0;
UPDATE rendicontazioni SET stato='ANOMALA', anomalie='007111#Il versamento risulta sconosciuto' WHERE stato='OK' AND id_singolo_versamento IS null;
update rendicontazioni set stato='ALTRO_INTERMEDIARIO', anomalie=null where stato='ANOMALA' and char_length(iuv) not in (15,17) and id_pagamento is null and id_singolo_versamento is null ;
update rendicontazioni set stato='ALTRO_INTERMEDIARIO', anomalie=null where id in (select rendicontazioni.id from rendicontazioni join fr on fr.id=rendicontazioni.id_fr join domini on domini.cod_dominio=fr.cod_dominio and  rendicontazioni.stato = 'ANOMALA' and aux_digit='3' and length(iuv) <> 17 and (iuv not like ('0' || segregation_code || '%') and length(segregation_code::char) = 1 ) OR (iuv not like (segregation_code || '%') and length(segregation_code::char) = 2 ));

ALTER TABLE fr DROP CONSTRAINT unique_fr_1;
ALTER TABLE fr ADD CONSTRAINT unique_fr_1 UNIQUE (cod_dominio,cod_flusso,data_ora_flusso);
-- 30/08/2021 Aggiunta colonna connettore maggioli jppa alla tabella domini
ALTER TABLE domini ADD COLUMN cod_connettore_maggioli_jppa VARCHAR(255);


-- 08/09/2021 Estesa dimensione del campo tipo_evento della tabella eventi
DROP VIEW IF EXISTS v_eventi_vers;
DROP VIEW IF EXISTS v_eventi_vers_pagamenti;
DROP VIEW IF EXISTS v_eventi_vers_rendicontazioni;
DROP VIEW IF EXISTS v_eventi_vers_riconciliazioni;
DROP VIEW IF EXISTS v_eventi_vers_tracciati;

ALTER TABLE eventi ALTER COLUMN tipo_evento TYPE VARCHAR(255);

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
        JOIN pagamenti ON pagamenti.id = rendicontazioni.id_pagamento
        JOIN singoli_versamenti ON pagamenti.id_singolo_versamento=singoli_versamenti.id
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





