-- 18/12/2019 Eliminate colonne dati WISP dalla tabella PagamentiPortale.
DROP VIEW IF EXISTS v_pagamenti_portale_ext;
DROP VIEW IF EXISTS v_pag_portale_base;
DROP VIEW IF EXISTS v_pagamenti_portale;

ALTER TABLE pagamenti_portale DROP COLUMN wisp_id_dominio;
ALTER TABLE pagamenti_portale DROP COLUMN wisp_key_pa;
ALTER TABLE pagamenti_portale DROP COLUMN wisp_key_wisp;
ALTER TABLE pagamenti_portale DROP COLUMN wisp_html;

CREATE VIEW v_pagamenti_portale AS
 SELECT 
  pagamenti_portale.cod_canale,
  pagamenti_portale.nome,
  pagamenti_portale.importo,
  pagamenti_portale.versante_identificativo,
  pagamenti_portale.src_versante_identificativo,
  pagamenti_portale.id_sessione,
  pagamenti_portale.id_sessione_portale,
  pagamenti_portale.id_sessione_psp,
  pagamenti_portale.stato,
  pagamenti_portale.codice_stato,
  pagamenti_portale.descrizione_stato,
  pagamenti_portale.psp_redirect_url,
  pagamenti_portale.psp_esito,
  pagamenti_portale.data_richiesta,
  pagamenti_portale.url_ritorno,
  pagamenti_portale.cod_psp,
  pagamenti_portale.tipo_versamento,
  pagamenti_portale.multi_beneficiario,
  pagamenti_portale.ack,
  pagamenti_portale.tipo,
  pagamenti_portale.principal,
  pagamenti_portale.tipo_utenza,
  pagamenti_portale.id,
  pagamenti_portale.id_applicazione,
  versamenti.debitore_identificativo as debitore_identificativo,
  versamenti.src_debitore_identificativo as src_debitore_identificativo,
  versamenti.id_dominio as id_dominio, 
  versamenti.id_uo as id_uo, 
  versamenti.id_tipo_versamento as id_tipo_versamento
FROM pagamenti_portale 
JOIN pag_port_versamenti ON pagamenti_portale.id = pag_port_versamenti.id_pagamento_portale 
JOIN versamenti ON versamenti.id=pag_port_versamenti.id_versamento;

-- 31/03/2020 Colonne di ricerca upper case per identificativo_debitore nella vista RPT.

DROP VIEW IF EXISTS v_rpt_versamenti;
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
    versamenti.src_debitore_identificativo as vrs_src_debitore_identificativ
FROM rpt JOIN versamenti ON versamenti.id = rpt.id_versamento;

-- 03/03/2020 Modifiche alla tabelle TipiVersamento e TipiVersamentoDominio
-- 1) Aggiunte Colonne per la configurazione dell'avvisatura con AppIO

ALTER TABLE tipi_versamento ADD COLUMN avv_app_io_prom_avv_abilitato BOOLEAN NOT NULL DEFAULT false;
ALTER TABLE tipi_versamento ADD COLUMN avv_app_io_prom_avv_tipo VARCHAR(35);
ALTER TABLE tipi_versamento ADD COLUMN avv_app_io_prom_avv_oggetto LONGTEXT;
ALTER TABLE tipi_versamento ADD COLUMN avv_app_io_prom_avv_messaggio LONGTEXT;
ALTER TABLE tipi_versamento ADD COLUMN avv_app_io_prom_ric_abilitato BOOLEAN NOT NULL DEFAULT false;
ALTER TABLE tipi_versamento ADD COLUMN avv_app_io_prom_ric_tipo VARCHAR(35);
ALTER TABLE tipi_versamento ADD COLUMN avv_app_io_prom_ric_oggetto LONGTEXT;
ALTER TABLE tipi_versamento ADD COLUMN avv_app_io_prom_ric_messaggio LONGTEXT;
ALTER TABLE tipi_versamento ADD COLUMN avv_app_io_prom_ric_eseguiti BOOLEAN;
ALTER TABLE tipi_versamento ADD COLUMN avv_app_io_prom_scad_abilitato BOOLEAN NOT NULL DEFAULT false;
ALTER TABLE tipi_versamento ADD COLUMN avv_app_io_prom_scad_preavviso INT;
ALTER TABLE tipi_versamento ADD COLUMN avv_app_io_prom_scad_tipo VARCHAR(35);
ALTER TABLE tipi_versamento ADD COLUMN avv_app_io_prom_scad_oggetto LONGTEXT;
ALTER TABLE tipi_versamento ADD COLUMN avv_app_io_prom_scad_messaggio LONGTEXT;

ALTER TABLE tipi_vers_domini ADD COLUMN avv_app_io_prom_avv_abilitato BOOLEAN;
ALTER TABLE tipi_vers_domini ADD COLUMN avv_app_io_prom_avv_tipo VARCHAR(35);
ALTER TABLE tipi_vers_domini ADD COLUMN avv_app_io_prom_avv_oggetto LONGTEXT;
ALTER TABLE tipi_vers_domini ADD COLUMN avv_app_io_prom_avv_messaggio LONGTEXT;
ALTER TABLE tipi_vers_domini ADD COLUMN avv_app_io_prom_ric_abilitato BOOLEAN;
ALTER TABLE tipi_vers_domini ADD COLUMN avv_app_io_prom_ric_tipo VARCHAR(35);
ALTER TABLE tipi_vers_domini ADD COLUMN avv_app_io_prom_ric_oggetto LONGTEXT;
ALTER TABLE tipi_vers_domini ADD COLUMN avv_app_io_prom_ric_messaggio LONGTEXT;
ALTER TABLE tipi_vers_domini ADD COLUMN avv_app_io_prom_ric_eseguiti BOOLEAN;
ALTER TABLE tipi_vers_domini ADD COLUMN avv_app_io_prom_scad_abilitato BOOLEAN;
ALTER TABLE tipi_vers_domini ADD COLUMN avv_app_io_prom_scad_preavviso INT;
ALTER TABLE tipi_vers_domini ADD COLUMN avv_app_io_prom_scad_tipo VARCHAR(35);
ALTER TABLE tipi_vers_domini ADD COLUMN avv_app_io_prom_scad_oggetto LONGTEXT;
ALTER TABLE tipi_vers_domini ADD COLUMN avv_app_io_prom_scad_messaggio LONGTEXT;

-- 2) Aggiunte Colonne per la configurazione dell'interfaccia caricamento pendenze nei portali pagamento

ALTER TABLE tipi_versamento ADD COLUMN pag_form_tipo VARCHAR(35);
ALTER TABLE tipi_versamento ADD COLUMN pag_form_definizione LONGTEXT;
ALTER TABLE tipi_versamento ADD COLUMN pag_form_impaginazione LONGTEXT;
ALTER TABLE tipi_versamento ADD COLUMN pag_validazione_def LONGTEXT;
ALTER TABLE tipi_versamento ADD COLUMN pag_trasformazione_tipo VARCHAR(35);
ALTER TABLE tipi_versamento ADD COLUMN pag_trasformazione_def LONGTEXT;
ALTER TABLE tipi_versamento ADD COLUMN pag_cod_applicazione VARCHAR(35);
ALTER TABLE tipi_versamento ADD COLUMN pag_abilitato BOOLEAN NOT NULL DEFAULT false;

ALTER TABLE tipi_vers_domini ADD COLUMN pag_form_tipo VARCHAR(35);
ALTER TABLE tipi_vers_domini ADD COLUMN pag_form_definizione LONGTEXT;
ALTER TABLE tipi_vers_domini ADD COLUMN pag_form_impaginazione LONGTEXT;
ALTER TABLE tipi_vers_domini ADD COLUMN pag_validazione_def LONGTEXT;
ALTER TABLE tipi_vers_domini ADD COLUMN pag_trasformazione_tipo VARCHAR(35);
ALTER TABLE tipi_vers_domini ADD COLUMN pag_trasformazione_def LONGTEXT;
ALTER TABLE tipi_vers_domini ADD COLUMN pag_cod_applicazione VARCHAR(35);
ALTER TABLE tipi_vers_domini ADD COLUMN pag_abilitato BOOLEAN;

-- 3) Rinominate Colonne per la configurazione dell'interfaccia caricamento pendenze nei portali backoffice

ALTER TABLE tipi_versamento RENAME form_tipo TO bo_form_tipo;
ALTER TABLE tipi_versamento RENAME form_definizione TO bo_form_definizione;
ALTER TABLE tipi_versamento RENAME validazione_definizione TO bo_validazione_def;
ALTER TABLE tipi_versamento RENAME trasformazione_tipo TO bo_trasformazione_tipo;
ALTER TABLE tipi_versamento RENAME trasformazione_definizione TO bo_trasformazione_def;
ALTER TABLE tipi_versamento RENAME cod_applicazione TO bo_cod_applicazione;
ALTER TABLE tipi_versamento ADD COLUMN bo_abilitato BOOLEAN DEFAULT false;
UPDATE tipi_versamento SET bo_abilitato = true WHERE (bo_form_tipo IS NOT NULL OR bo_validazione_def IS NOT NULL OR bo_trasformazione_tipo IS NOT NULL OR bo_cod_applicazione IS NOT NULL);
ALTER TABLE tipi_versamento ALTER COLUMN bo_abilitato SET NOT NULL;

ALTER TABLE tipi_vers_domini RENAME form_tipo TO bo_form_tipo;
ALTER TABLE tipi_vers_domini RENAME form_definizione TO bo_form_definizione;
ALTER TABLE tipi_vers_domini RENAME validazione_definizione TO bo_validazione_def;
ALTER TABLE tipi_vers_domini RENAME trasformazione_tipo TO bo_trasformazione_tipo;
ALTER TABLE tipi_vers_domini RENAME trasformazione_definizione TO bo_trasformazione_def;
ALTER TABLE tipi_vers_domini RENAME cod_applicazione TO bo_cod_applicazione;
ALTER TABLE tipi_vers_domini ADD COLUMN bo_abilitato BOOLEAN;
UPDATE tipi_vers_domini SET bo_abilitato = true WHERE (bo_form_tipo IS NOT NULL OR bo_validazione_def IS NOT NULL OR bo_trasformazione_tipo IS NOT NULL OR bo_cod_applicazione IS NOT NULL);

-- 4) Rinominate Colonne per la configurazione dell'avvisatura via mail 

ALTER TABLE tipi_versamento RENAME promemoria_avviso_abilitato TO avv_mail_prom_avv_abilitato;
ALTER TABLE tipi_versamento RENAME promemoria_avviso_tipo TO avv_mail_prom_avv_tipo;
ALTER TABLE tipi_versamento RENAME promemoria_avviso_pdf TO avv_mail_prom_avv_pdf;
ALTER TABLE tipi_versamento RENAME promemoria_avviso_oggetto TO avv_mail_prom_avv_oggetto;
ALTER TABLE tipi_versamento RENAME promemoria_avviso_messaggio TO avv_mail_prom_avv_messaggio;
ALTER TABLE tipi_versamento RENAME promemoria_ricevuta_abilitato TO avv_mail_prom_ric_abilitato;
ALTER TABLE tipi_versamento RENAME promemoria_ricevuta_tipo TO avv_mail_prom_ric_tipo;
ALTER TABLE tipi_versamento RENAME promemoria_ricevuta_pdf TO avv_mail_prom_ric_pdf;
ALTER TABLE tipi_versamento RENAME promemoria_ricevuta_oggetto TO avv_mail_prom_ric_oggetto;
ALTER TABLE tipi_versamento RENAME promemoria_ricevuta_messaggio TO avv_mail_prom_ric_messaggio;
ALTER TABLE tipi_versamento ADD COLUMN avv_mail_prom_ric_eseguiti BOOLEAN;
ALTER TABLE tipi_versamento ADD COLUMN avv_mail_prom_scad_abilitato BOOLEAN NOT NULL DEFAULT false;
ALTER TABLE tipi_versamento ADD COLUMN avv_mail_prom_scad_preavviso INT;
ALTER TABLE tipi_versamento ADD COLUMN avv_mail_prom_scad_tipo VARCHAR(35);
ALTER TABLE tipi_versamento ADD COLUMN avv_mail_prom_scad_oggetto LONGTEXT;
ALTER TABLE tipi_versamento ADD COLUMN avv_mail_prom_scad_messaggio LONGTEXT;

ALTER TABLE tipi_vers_domini RENAME promemoria_avviso_abilitato TO avv_mail_prom_avv_abilitato;
ALTER TABLE tipi_vers_domini RENAME promemoria_avviso_tipo TO avv_mail_prom_avv_tipo;
ALTER TABLE tipi_vers_domini RENAME promemoria_avviso_pdf TO avv_mail_prom_avv_pdf;
ALTER TABLE tipi_vers_domini RENAME promemoria_avviso_oggetto TO avv_mail_prom_avv_oggetto;
ALTER TABLE tipi_vers_domini RENAME promemoria_avviso_messaggio TO avv_mail_prom_avv_messaggio;
ALTER TABLE tipi_vers_domini RENAME promemoria_ricevuta_abilitato TO avv_mail_prom_ric_abilitato;
ALTER TABLE tipi_vers_domini RENAME promemoria_ricevuta_tipo TO avv_mail_prom_ric_tipo;
ALTER TABLE tipi_vers_domini RENAME promemoria_ricevuta_pdf TO avv_mail_prom_ric_pdf;
ALTER TABLE tipi_vers_domini RENAME promemoria_ricevuta_oggetto TO avv_mail_prom_ric_oggetto;
ALTER TABLE tipi_vers_domini RENAME promemoria_ricevuta_messaggio TO avv_mail_prom_ric_messaggio;
ALTER TABLE tipi_vers_domini ADD COLUMN avv_mail_prom_ric_eseguiti BOOLEAN;
ALTER TABLE tipi_vers_domini ADD COLUMN avv_mail_prom_scad_abilitato BOOLEAN;
ALTER TABLE tipi_vers_domini ADD COLUMN avv_mail_prom_scad_preavviso INT;
ALTER TABLE tipi_vers_domini ADD COLUMN avv_mail_prom_scad_tipo VARCHAR(35);
ALTER TABLE tipi_vers_domini ADD COLUMN avv_mail_prom_scad_oggetto LONGTEXT;
ALTER TABLE tipi_vers_domini ADD COLUMN avv_mail_prom_scad_messaggio LONGTEXT;


-- 12/02/2020 Tabella Notifiche AppIO
CREATE TABLE notifiche_app_io
(
	debitore_identificativo VARCHAR(35) NOT NULL,
	cod_versamento_ente VARCHAR(35) NOT NULL,
	cod_applicazione VARCHAR(35) NOT NULL,
	cod_dominio VARCHAR(35) NOT NULL,
	iuv VARCHAR(35) NOT NULL,
	tipo_esito VARCHAR(16) NOT NULL,
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
	data_creazione TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
	stato VARCHAR(16) NOT NULL,
	descrizione_stato VARCHAR(255),
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
	data_aggiornamento_stato TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
	-- DATETIME invece che TIMESTAMP(3) per supportare la data di default 31-12-9999
	data_prossima_spedizione DATETIME NOT NULL,
	tentativi_spedizione BIGINT,
	id_messaggio VARCHAR(255),
	stato_messaggio VARCHAR(16),
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT,
	id_versamento BIGINT NOT NULL,
	id_tipo_versamento_dominio BIGINT NOT NULL,
	-- fk/pk keys constraints
	CONSTRAINT fk_nai_id_versamento FOREIGN KEY (id_versamento) REFERENCES versamenti(id),
	CONSTRAINT fk_nai_id_tipo_versamento_dominio FOREIGN KEY (id_tipo_versamento_dominio) REFERENCES tipi_vers_domini(id),
	CONSTRAINT pk_notifiche_app_io PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;

-- index
CREATE INDEX idx_nai_da_spedire ON notifiche_app_io (stato,data_prossima_spedizione);

insert into sonde(nome, classe, soglia_warn, soglia_error) values ('update-ntfy-appio', 'org.openspcoop2.utils.sonde.impl.SondaBatch', 86400000, 172800000);
insert into sonde(nome, classe, soglia_warn, soglia_error) values ('check-ntfy-appio', 'org.openspcoop2.utils.sonde.impl.SondaCoda', 10, 100);


