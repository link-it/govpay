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

ALTER TABLE tipi_vers_domini ADD COLUMN app_io_api_key VARCHAR(255);
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

ALTER TABLE tipi_versamento RENAME COLUMN form_tipo TO bo_form_tipo;
ALTER TABLE tipi_versamento RENAME COLUMN form_definizione TO bo_form_definizione;
ALTER TABLE tipi_versamento RENAME COLUMN validazione_definizione TO bo_validazione_def;
ALTER TABLE tipi_versamento RENAME COLUMN trasformazione_tipo TO bo_trasformazione_tipo;
ALTER TABLE tipi_versamento RENAME COLUMN trasformazione_definizione TO bo_trasformazione_def;
ALTER TABLE tipi_versamento RENAME COLUMN cod_applicazione TO bo_cod_applicazione;
ALTER TABLE tipi_versamento ADD COLUMN bo_abilitato BOOLEAN DEFAULT false;
UPDATE tipi_versamento SET bo_abilitato = true WHERE (bo_form_tipo IS NOT NULL OR bo_validazione_def IS NOT NULL OR bo_trasformazione_tipo IS NOT NULL OR bo_cod_applicazione IS NOT NULL);
ALTER TABLE tipi_versamento MODIFY COLUMN bo_abilitato BOOLEAN DEFAULT false NOT NULL;

ALTER TABLE tipi_vers_domini RENAME COLUMN form_tipo TO bo_form_tipo;
ALTER TABLE tipi_vers_domini RENAME COLUMN form_definizione TO bo_form_definizione;
ALTER TABLE tipi_vers_domini RENAME COLUMN validazione_definizione TO bo_validazione_def;
ALTER TABLE tipi_vers_domini RENAME COLUMN trasformazione_tipo TO bo_trasformazione_tipo;
ALTER TABLE tipi_vers_domini RENAME COLUMN trasformazione_definizione TO bo_trasformazione_def;
ALTER TABLE tipi_vers_domini RENAME COLUMN cod_applicazione TO bo_cod_applicazione;
ALTER TABLE tipi_vers_domini ADD COLUMN bo_abilitato BOOLEAN;
UPDATE tipi_vers_domini SET bo_abilitato = true WHERE (bo_form_tipo IS NOT NULL OR bo_validazione_def IS NOT NULL OR bo_trasformazione_tipo IS NOT NULL OR bo_cod_applicazione IS NOT NULL);

-- 4) Rinominate Colonne per la configurazione dell'avvisatura via mail 

ALTER TABLE tipi_versamento RENAME COLUMN promemoria_avviso_abilitato TO avv_mail_prom_avv_abilitato;
ALTER TABLE tipi_versamento RENAME COLUMN promemoria_avviso_tipo TO avv_mail_prom_avv_tipo;
ALTER TABLE tipi_versamento RENAME COLUMN promemoria_avviso_pdf TO avv_mail_prom_avv_pdf;
ALTER TABLE tipi_versamento RENAME COLUMN promemoria_avviso_oggetto TO avv_mail_prom_avv_oggetto;
ALTER TABLE tipi_versamento RENAME COLUMN promemoria_avviso_messaggio TO avv_mail_prom_avv_messaggio;
ALTER TABLE tipi_versamento RENAME COLUMN promemoria_ricevuta_abilitato TO avv_mail_prom_ric_abilitato;
ALTER TABLE tipi_versamento RENAME COLUMN promemoria_ricevuta_tipo TO avv_mail_prom_ric_tipo;
ALTER TABLE tipi_versamento RENAME COLUMN promemoria_ricevuta_pdf TO avv_mail_prom_ric_pdf;
ALTER TABLE tipi_versamento RENAME COLUMN promemoria_ricevuta_oggetto TO avv_mail_prom_ric_oggetto;
ALTER TABLE tipi_versamento RENAME COLUMN promemoria_ricevuta_messaggio TO avv_mail_prom_ric_messaggio;
ALTER TABLE tipi_versamento ADD COLUMN avv_mail_prom_ric_eseguiti BOOLEAN;
ALTER TABLE tipi_versamento ADD COLUMN avv_mail_prom_scad_abilitato BOOLEAN NOT NULL DEFAULT false;
ALTER TABLE tipi_versamento ADD COLUMN avv_mail_prom_scad_preavviso INT;
ALTER TABLE tipi_versamento ADD COLUMN avv_mail_prom_scad_tipo VARCHAR(35);
ALTER TABLE tipi_versamento ADD COLUMN avv_mail_prom_scad_oggetto LONGTEXT;
ALTER TABLE tipi_versamento ADD COLUMN avv_mail_prom_scad_messaggio LONGTEXT;

ALTER TABLE tipi_vers_domini RENAME COLUMN promemoria_avviso_abilitato TO avv_mail_prom_avv_abilitato;
ALTER TABLE tipi_vers_domini RENAME COLUMN promemoria_avviso_tipo TO avv_mail_prom_avv_tipo;
ALTER TABLE tipi_vers_domini RENAME COLUMN promemoria_avviso_pdf TO avv_mail_prom_avv_pdf;
ALTER TABLE tipi_vers_domini RENAME COLUMN promemoria_avviso_oggetto TO avv_mail_prom_avv_oggetto;
ALTER TABLE tipi_vers_domini RENAME COLUMN promemoria_avviso_messaggio TO avv_mail_prom_avv_messaggio;
ALTER TABLE tipi_vers_domini RENAME COLUMN promemoria_ricevuta_abilitato TO avv_mail_prom_ric_abilitato;
ALTER TABLE tipi_vers_domini RENAME COLUMN promemoria_ricevuta_tipo TO avv_mail_prom_ric_tipo;
ALTER TABLE tipi_vers_domini RENAME COLUMN promemoria_ricevuta_pdf TO avv_mail_prom_ric_pdf;
ALTER TABLE tipi_vers_domini RENAME COLUMN promemoria_ricevuta_oggetto TO avv_mail_prom_ric_oggetto;
ALTER TABLE tipi_vers_domini RENAME COLUMN promemoria_ricevuta_messaggio TO avv_mail_prom_ric_messaggio;
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


-- 08/04/2020 Nuovi valori configurazione generale
INSERT INTO configurazione (NOME,VALORE) values ('app_io_batch', '{"abilitato": false, "url": null, "timeToLive": 3600 }');
INSERT INTO configurazione (NOME,VALORE) values ('avvisatura_mail', '{"promemoriaAvviso": { "tipo": "freemarker", "oggetto": "\\"UHJvbWVtb3JpYSBwYWdhbWVudG86ICR7dmVyc2FtZW50by5nZXRDYXVzYWxlVmVyc2FtZW50bygpLmdldFNpbXBsZSgpfQ==\\"", "messaggio": "\\"R2VudGlsZSAke3ZlcnNhbWVudG8uZ2V0QW5hZ3JhZmljYURlYml0b3JlKCkuZ2V0UmFnaW9uZVNvY2lhbGUoKX0sCgpsZSBub3RpZmljaGlhbW8gY2hlIMOoIHN0YXRhIGVtZXNzYSB1bmEgcmljaGllc3RhIGRpIHBhZ2FtZW50byBhIHN1byBjYXJpY286ICR7dmVyc2FtZW50by5nZXRDYXVzYWxlVmVyc2FtZW50bygpLmdldFNpbXBsZSgpfQoKPCNpZiB2ZXJzYW1lbnRvLmdldE51bWVyb0F2dmlzbygpP2hhc19jb250ZW50PgpQdcOyIGVmZmV0dHVhcmUgaWwgcGFnYW1lbnRvIHRyYW1pdGUgbCdhcHAgbW9iaWxlIElPIG9wcHVyZSBwcmVzc28gdW5vIGRlaSBwcmVzdGF0b3JpIGRpIHNlcnZpemkgZGkgcGFnYW1lbnRvIGFkZXJlbnRpIGFsIGNpcmN1aXRvIHBhZ29QQSB1dGlsaXp6YW5kbyBsJ2F2dmlzbyBkaSBwYWdhbWVudG8gYWxsZWdhdG8uCjwjZWxzZT4KUHVvJyBlZmZldHR1YXJlIGlsIHBhZ2FtZW50byBvbi1saW5lIHByZXNzbyBpbCBwb3J0YWxlIGRlbGwnZW50ZSBjcmVkaXRvcmU6ICR7ZG9taW5pby5nZXRSYWdpb25lU29jaWFsZSgpfSAKPC8jaWY+CgpEaXN0aW50aSBzYWx1dGku\\"", "allegaPdf": true }, "promemoriaRicevuta": { "tipo": "freemarker", "oggetto": "\\"PCNpZiBycHQuZ2V0RXNpdG9QYWdhbWVudG8oKS5nZXRDb2RpZmljYSgpID0gMD4KTm90aWZpY2EgcGFnYW1lbnRvIGVzZWd1aXRvOiAke3JwdC5nZXRDb2REb21pbmlvKCl9LyR7cnB0LmdldEl1digpfS8ke3JwdC5nZXRDY3AoKX0KPCNlbHNlaWYgcnB0LmdldEVzaXRvUGFnYW1lbnRvKCkuZ2V0Q29kaWZpY2EoKSA9IDE+Ck5vdGlmaWNhIHBhZ2FtZW50byBub24gZXNlZ3VpdG86ICR7cnB0LmdldENvZERvbWluaW8oKX0vJHtycHQuZ2V0SXV2KCl9LyR7cnB0LmdldENjcCgpfQo8I2Vsc2VpZiBycHQuZ2V0RXNpdG9QYWdhbWVudG8oKS5nZXRDb2RpZmljYSgpID0gMj4KTm90aWZpY2EgcGFnYW1lbnRvIGVzZWd1aXRvIHBhcnppYWxtZW50ZTogJHtycHQuZ2V0Q29kRG9taW5pbygpfS8ke3JwdC5nZXRJdXYoKX0vJHtycHQuZ2V0Q2NwKCl9CjwjZWxzZWlmIHJwdC5nZXRFc2l0b1BhZ2FtZW50bygpLmdldENvZGlmaWNhKCkgPSAzPgpOb3RpZmljYSBkZWNvcnJlbnphIHRlcm1pbmkgcGFnYW1lbnRvOiAke3JwdC5nZXRDb2REb21pbmlvKCl9LyR7cnB0LmdldEl1digpfS8ke3JwdC5nZXRDY3AoKX0KPCNlbHNlaWYgcnB0LmdldEVzaXRvUGFnYW1lbnRvKCkuZ2V0Q29kaWZpY2EoKSA9IDQ+Ck5vdGlmaWNhIGRlY29ycmVuemEgdGVybWluaSBwYWdhbWVudG86ICR7cnB0LmdldENvZERvbWluaW8oKX0vJHtycHQuZ2V0SXV2KCl9LyR7cnB0LmdldENjcCgpfQo8LyNpZj4=\\"", "messaggio": "\\"PCNhc3NpZ24gZGF0YVJpY2hpZXN0YSA9IHJwdC5nZXREYXRhTXNnUmljaGllc3RhKCk/c3RyaW5nKCJ5eXl5LU1NLWRkIEhIOm1tOnNzIik+CklsIHBhZ2FtZW50byBkaSAiJHt2ZXJzYW1lbnRvLmdldENhdXNhbGVWZXJzYW1lbnRvKCkuZ2V0U2ltcGxlKCl9IiBlZmZldHR1YXRvIGlsICR7ZGF0YVJpY2hpZXN0YX0gcmlzdWx0YSBjb25jbHVzbyBjb24gZXNpdG8gJHtycHQuZ2V0RXNpdG9QYWdhbWVudG8oKS5uYW1lKCl9OgoKRW50ZSBjcmVkaXRvcmU6ICR7ZG9taW5pby5nZXRSYWdpb25lU29jaWFsZSgpfSAoJHtkb21pbmlvLmdldENvZERvbWluaW8oKX0pCklzdGl0dXRvIGF0dGVzdGFudGU6ICR7cnB0LmdldERlbm9taW5hemlvbmVBdHRlc3RhbnRlKCl9ICgke3JwdC5nZXRJZGVudGlmaWNhdGl2b0F0dGVzdGFudGUoKX0pCklkZW50aWZpY2F0aXZvIHVuaXZvY28gdmVyc2FtZW50byAoSVVWKTogJHtycHQuZ2V0SXV2KCl9CkNvZGljZSBjb250ZXN0byBwYWdhbWVudG8gKENDUCk6ICR7cnB0LmdldENjcCgpfQpJbXBvcnRvIHBhZ2F0bzogJHtycHQuZ2V0SW1wb3J0b1RvdGFsZVBhZ2F0bygpfQoKRGlzdGludGkgc2FsdXRpLg==\\"", "allegaPdf": true , "soloEseguiti": true }, "promemoriaScadenza": { "tipo": "freemarker", "oggetto": "\\"UHJvbWVtb3JpYSBwYWdhbWVudG86ICR7dmVyc2FtZW50by5nZXRDYXVzYWxlVmVyc2FtZW50bygpLmdldFNpbXBsZSgpfQ==\\"", "messaggio": "\\"R2VudGlsZSAke3ZlcnNhbWVudG8uZ2V0QW5hZ3JhZmljYURlYml0b3JlKCkuZ2V0UmFnaW9uZVNvY2lhbGUoKX0sCgpsZSBub3RpZmljaGlhbW8gY2hlIMOoIHN0YXRhIGVtZXNzYSB1bmEgcmljaGllc3RhIGRpIHBhZ2FtZW50byBhIHN1byBjYXJpY286ICR7dmVyc2FtZW50by5nZXRDYXVzYWxlVmVyc2FtZW50bygpLmdldFNpbXBsZSgpfQoKPCNpZiB2ZXJzYW1lbnRvLmdldE51bWVyb0F2dmlzbygpP2hhc19jb250ZW50PgpQdcOyIGVmZmV0dHVhcmUgaWwgcGFnYW1lbnRvIHRyYW1pdGUgbCdhcHAgbW9iaWxlIElPIG9wcHVyZSBwcmVzc28gdW5vIGRlaSBwcmVzdGF0b3JpIGRpIHNlcnZpemkgZGkgcGFnYW1lbnRvIGFkZXJlbnRpIGFsIGNpcmN1aXRvIHBhZ29QQSB1dGlsaXp6YW5kbyBsJ2F2dmlzbyBkaSBwYWdhbWVudG8gYWxsZWdhdG8uCjwjZWxzZT4KUHVvJyBlZmZldHR1YXJlIGlsIHBhZ2FtZW50byBvbi1saW5lIHByZXNzbyBpbCBwb3J0YWxlIGRlbGwnZW50ZSBjcmVkaXRvcmU6ICR7ZG9taW5pby5nZXRSYWdpb25lU29jaWFsZSgpfSAKPC8jaWY+CgpEaXN0aW50aSBzYWx1dGku\\"", "preavviso": 10 } }' );
INSERT INTO configurazione (NOME,VALORE) values ('avvisatura_app_io', '{"promemoriaAvviso": { "tipo": "freemarker", "oggetto": "\\"UHJvbWVtb3JpYSBwYWdhbWVudG86ICR7dmVyc2FtZW50by5nZXRDYXVzYWxlVmVyc2FtZW50bygpLmdldFNpbXBsZSgpfQ==\\"", "messaggio": "\\"R2VudGlsZSAke3ZlcnNhbWVudG8uZ2V0QW5hZ3JhZmljYURlYml0b3JlKCkuZ2V0UmFnaW9uZVNvY2lhbGUoKX0sCgpsZSBub3RpZmljaGlhbW8gY2hlIMOoIHN0YXRhIGVtZXNzYSB1bmEgcmljaGllc3RhIGRpIHBhZ2FtZW50byBhIHN1byBjYXJpY286ICR7dmVyc2FtZW50by5nZXRDYXVzYWxlVmVyc2FtZW50bygpLmdldFNpbXBsZSgpfQoKPCNpZiB2ZXJzYW1lbnRvLmdldE51bWVyb0F2dmlzbygpP2hhc19jb250ZW50PgpQdcOyIGVmZmV0dHVhcmUgaWwgcGFnYW1lbnRvIHRyYW1pdGUgbCdhcHAgbW9iaWxlIElPIG9wcHVyZSBwcmVzc28gdW5vIGRlaSBwcmVzdGF0b3JpIGRpIHNlcnZpemkgZGkgcGFnYW1lbnRvIGFkZXJlbnRpIGFsIGNpcmN1aXRvIHBhZ29QQSB1dGlsaXp6YW5kbyBsJ2F2dmlzbyBkaSBwYWdhbWVudG8gYWxsZWdhdG8uCjwjZWxzZT4KUHVvJyBlZmZldHR1YXJlIGlsIHBhZ2FtZW50byBvbi1saW5lIHByZXNzbyBpbCBwb3J0YWxlIGRlbGwnZW50ZSBjcmVkaXRvcmU6ICR7ZG9taW5pby5nZXRSYWdpb25lU29jaWFsZSgpfSAKPC8jaWY+CgpEaXN0aW50aSBzYWx1dGku\\"" }, "promemoriaRicevuta": { "tipo": "freemarker", "oggetto": "\\"PCNpZiBycHQuZ2V0RXNpdG9QYWdhbWVudG8oKS5nZXRDb2RpZmljYSgpID0gMD4KTm90aWZpY2EgcGFnYW1lbnRvIGVzZWd1aXRvOiAke3JwdC5nZXRDb2REb21pbmlvKCl9LyR7cnB0LmdldEl1digpfS8ke3JwdC5nZXRDY3AoKX0KPCNlbHNlaWYgcnB0LmdldEVzaXRvUGFnYW1lbnRvKCkuZ2V0Q29kaWZpY2EoKSA9IDE+Ck5vdGlmaWNhIHBhZ2FtZW50byBub24gZXNlZ3VpdG86ICR7cnB0LmdldENvZERvbWluaW8oKX0vJHtycHQuZ2V0SXV2KCl9LyR7cnB0LmdldENjcCgpfQo8I2Vsc2VpZiBycHQuZ2V0RXNpdG9QYWdhbWVudG8oKS5nZXRDb2RpZmljYSgpID0gMj4KTm90aWZpY2EgcGFnYW1lbnRvIGVzZWd1aXRvIHBhcnppYWxtZW50ZTogJHtycHQuZ2V0Q29kRG9taW5pbygpfS8ke3JwdC5nZXRJdXYoKX0vJHtycHQuZ2V0Q2NwKCl9CjwjZWxzZWlmIHJwdC5nZXRFc2l0b1BhZ2FtZW50bygpLmdldENvZGlmaWNhKCkgPSAzPgpOb3RpZmljYSBkZWNvcnJlbnphIHRlcm1pbmkgcGFnYW1lbnRvOiAke3JwdC5nZXRDb2REb21pbmlvKCl9LyR7cnB0LmdldEl1digpfS8ke3JwdC5nZXRDY3AoKX0KPCNlbHNlaWYgcnB0LmdldEVzaXRvUGFnYW1lbnRvKCkuZ2V0Q29kaWZpY2EoKSA9IDQ+Ck5vdGlmaWNhIGRlY29ycmVuemEgdGVybWluaSBwYWdhbWVudG86ICR7cnB0LmdldENvZERvbWluaW8oKX0vJHtycHQuZ2V0SXV2KCl9LyR7cnB0LmdldENjcCgpfQo8LyNpZj4=\\"", "messaggio": "\\"PCNhc3NpZ24gZGF0YVJpY2hpZXN0YSA9IHJwdC5nZXREYXRhTXNnUmljaGllc3RhKCk/c3RyaW5nKCJ5eXl5LU1NLWRkIEhIOm1tOnNzIik+CklsIHBhZ2FtZW50byBkaSAiJHt2ZXJzYW1lbnRvLmdldENhdXNhbGVWZXJzYW1lbnRvKCkuZ2V0U2ltcGxlKCl9IiBlZmZldHR1YXRvIGlsICR7ZGF0YVJpY2hpZXN0YX0gcmlzdWx0YSBjb25jbHVzbyBjb24gZXNpdG8gJHtycHQuZ2V0RXNpdG9QYWdhbWVudG8oKS5uYW1lKCl9OgoKRW50ZSBjcmVkaXRvcmU6ICR7ZG9taW5pby5nZXRSYWdpb25lU29jaWFsZSgpfSAoJHtkb21pbmlvLmdldENvZERvbWluaW8oKX0pCklzdGl0dXRvIGF0dGVzdGFudGU6ICR7cnB0LmdldERlbm9taW5hemlvbmVBdHRlc3RhbnRlKCl9ICgke3JwdC5nZXRJZGVudGlmaWNhdGl2b0F0dGVzdGFudGUoKX0pCklkZW50aWZpY2F0aXZvIHVuaXZvY28gdmVyc2FtZW50byAoSVVWKTogJHtycHQuZ2V0SXV2KCl9CkNvZGljZSBjb250ZXN0byBwYWdhbWVudG8gKENDUCk6ICR7cnB0LmdldENjcCgpfQpJbXBvcnRvIHBhZ2F0bzogJHtycHQuZ2V0SW1wb3J0b1RvdGFsZVBhZ2F0bygpfQoKRGlzdGludGkgc2FsdXRpLg==\\"", "soloEseguiti": true }, "promemoriaScadenza": { "tipo": "freemarker", "oggetto": "\\"UHJvbWVtb3JpYSBwYWdhbWVudG86ICR7dmVyc2FtZW50by5nZXRDYXVzYWxlVmVyc2FtZW50bygpLmdldFNpbXBsZSgpfQ==\\"", "messaggio": "\\"R2VudGlsZSAke3ZlcnNhbWVudG8uZ2V0QW5hZ3JhZmljYURlYml0b3JlKCkuZ2V0UmFnaW9uZVNvY2lhbGUoKX0sCgpsZSBub3RpZmljaGlhbW8gY2hlIMOoIHN0YXRhIGVtZXNzYSB1bmEgcmljaGllc3RhIGRpIHBhZ2FtZW50byBhIHN1byBjYXJpY286ICR7dmVyc2FtZW50by5nZXRDYXVzYWxlVmVyc2FtZW50bygpLmdldFNpbXBsZSgpfQoKPCNpZiB2ZXJzYW1lbnRvLmdldE51bWVyb0F2dmlzbygpP2hhc19jb250ZW50PgpQdcOyIGVmZmV0dHVhcmUgaWwgcGFnYW1lbnRvIHRyYW1pdGUgbCdhcHAgbW9iaWxlIElPIG9wcHVyZSBwcmVzc28gdW5vIGRlaSBwcmVzdGF0b3JpIGRpIHNlcnZpemkgZGkgcGFnYW1lbnRvIGFkZXJlbnRpIGFsIGNpcmN1aXRvIHBhZ29QQSB1dGlsaXp6YW5kbyBsJ2F2dmlzbyBkaSBwYWdhbWVudG8gYWxsZWdhdG8uCjwjZWxzZT4KUHVvJyBlZmZldHR1YXJlIGlsIHBhZ2FtZW50byBvbi1saW5lIHByZXNzbyBpbCBwb3J0YWxlIGRlbGwnZW50ZSBjcmVkaXRvcmU6ICR7ZG9taW5pby5nZXRSYWdpb25lU29jaWFsZSgpfSAKPC8jaWY+CgpEaXN0aW50aSBzYWx1dGku\\"", "preavviso": 10 } }' );


-- 21/04/2020 Gestione rateizzazione pagamenti

CREATE TABLE documenti
(
	cod_documento VARCHAR(35) NOT NULL,
	descrizione VARCHAR(255) NOT NULL,
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT,
	id_dominio BIGINT NOT NULL,
	id_applicazione BIGINT NOT NULL,
	-- unique constraints
	CONSTRAINT unique_documenti_1 UNIQUE (cod_documento,id_applicazione),
	-- fk/pk keys constraints
	CONSTRAINT fk_doc_id_dominio FOREIGN KEY (id_dominio) REFERENCES domini(id),
	CONSTRAINT fk_doc_id_applicazione FOREIGN KEY (id_applicazione) REFERENCES applicazioni(id),
	CONSTRAINT pk_documenti PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;

ALTER TABLE versamenti ADD COLUMN cod_rata INT;
ALTER TABLE versamenti ADD COLUMN id_documento BIGINT;
-- N.B. -> nello script generale questa constraint viene eliminata
-- ALTER TABLE versamenti ADD CONSTRAINT fk_vrs_id_documento FOREIGN KEY (id_documento) REFERENCES documenti(id);

-- 21/04/2020 Gestione spedizione promemoria per i documenti

ALTER TABLE promemoria MODIFY COLUMN id_versamento BIGINT NULL;
ALTER TABLE promemoria ADD COLUMN id_documento BIGINT;
ALTER TABLE promemoria ADD CONSTRAINT fk_prm_id_documento FOREIGN KEY (id_documento) REFERENCES documenti(id);

-- 21/04/2020 Gestione spedizione stampe per i documenti

ALTER TABLE stampe DROP FOREIGN KEY fk_stm_id_versamento;
ALTER TABLE stampe DROP INDEX index_stampe_1;
ALTER TABLE stampe DROP CONSTRAINT unique_stampe_1;
ALTER TABLE stampe MODIFY COLUMN id_versamento BIGINT NULL;
ALTER TABLE stampe ADD COLUMN id_documento BIGINT;
ALTER TABLE stampe ADD CONSTRAINT fk_stm_id_documento FOREIGN KEY (id_documento) REFERENCES documenti(id);
ALTER TABLE stampe ADD CONSTRAINT fk_stm_id_versamento FOREIGN KEY (id_versamento) REFERENCES versamenti(id);
ALTER TABLE stampe ADD CONSTRAINT unique_stampe_1 UNIQUE (id_versamento,id_documento,tipo);
CREATE UNIQUE INDEX index_stampe_1 ON stampe (id_versamento,id_documento,tipo);

-- 21/04/2020 allineo viste per utilizzare i nuovi campi

DROP VIEW IF EXISTS versamenti_incassi;

CREATE VIEW versamenti_incassi AS SELECT
    versamenti.id,
    versamenti.cod_versamento_ente,
    versamenti.nome,
    versamenti.importo_totale,
    versamenti.stato_versamento,
    versamenti.descrizione_stato,
    versamenti.aggiornabile,
    versamenti.data_creazione,
    versamenti.data_validita,
    versamenti.data_scadenza,
    versamenti.data_ora_ultimo_aggiornamento,
    versamenti.causale_versamento,
    versamenti.debitore_tipo,
    versamenti.debitore_identificativo,
    versamenti.debitore_anagrafica,
    versamenti.debitore_indirizzo,
    versamenti.debitore_civico,
    versamenti.debitore_cap,
    versamenti.debitore_localita,
    versamenti.debitore_provincia,
    versamenti.debitore_nazione,
    versamenti.debitore_email,
    versamenti.debitore_telefono,
    versamenti.debitore_cellulare,
    versamenti.debitore_fax,
    versamenti.tassonomia_avviso,
    versamenti.tassonomia,
    versamenti.cod_lotto,
    versamenti.cod_versamento_lotto,
    versamenti.cod_anno_tributario,
    versamenti.cod_bundlekey,
    versamenti.dati_allegati,
    versamenti.incasso,
    versamenti.anomalie,
    versamenti.iuv_versamento,
    versamenti.numero_avviso,
    versamenti.id_dominio,
    versamenti.id_tipo_versamento,
    versamenti.id_tipo_versamento_dominio,
    versamenti.id_uo,
    versamenti.id_applicazione,
    versamenti.avvisatura_abilitata,
    versamenti.avvisatura_da_inviare,
    versamenti.avvisatura_operazione,
    versamenti.avvisatura_modalita,
    versamenti.avvisatura_tipo_pagamento,
    versamenti.avvisatura_cod_avvisatura,
    versamenti.divisione,
    versamenti.direzione,
    versamenti.id_tracciato,
    versamenti.id_sessione,
    versamenti.ack,
    versamenti.anomalo,
    versamenti.data_pagamento,
    versamenti.importo_pagato,
    versamenti.importo_incassato,
    versamenti.stato_pagamento,
    versamenti.iuv_pagamento,
    versamenti.src_iuv,
    versamenti.src_debitore_identificativo,
    versamenti.cod_rata,
    documenti.cod_documento,
    (CASE WHEN versamenti.stato_versamento = 'NON_ESEGUITO' AND versamenti.data_validita > now() THEN 0 ELSE 1 END) AS smart_order_rank,
    (ABS((UNIX_TIMESTAMP(now()) *1000) - (UNIX_TIMESTAMP(COALESCE(versamenti.data_pagamento, versamenti.data_validita, versamenti.data_creazione)) * 1000))) AS smart_order_date
    FROM versamenti JOIN tipi_versamento ON tipi_versamento.id = versamenti.id_tipo_versamento
    LEFT JOIN documenti ON versamenti.id_documento = documenti.id;


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
    versamenti.src_debitore_identificativo as vrs_src_debitore_identificativ,
    versamenti.cod_rata as vrs_cod_rata,
    versamenti.id_documento as vrs_id_documento
FROM rpt JOIN versamenti ON versamenti.id = rpt.id_versamento;


-- 28/04/2020 Aggiunte colonne RagioneSocialeDominio e RagioneSocialePsp alla tabella FR

ALTER TABLE fr ADD COLUMN ragione_sociale_psp VARCHAR(70);
ALTER TABLE fr ADD COLUMN ragione_sociale_dominio VARCHAR(70);

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
    versamenti.iuv_pagamento AS vrs_iuv_pagamento
   FROM fr
     JOIN rendicontazioni ON rendicontazioni.id_fr = fr.id
     JOIN singoli_versamenti ON rendicontazioni.id_singolo_versamento = singoli_versamenti.id
     JOIN versamenti ON versamenti.id = singoli_versamenti.id_versamento;


-- Gestione del tipo versamento all'interno della tabella versamenti
ALTER TABLE versamenti ADD COLUMN tipo VARCHAR(35);
UPDATE versamenti SET tipo = 'DOVUTO';
UPDATE versamenti SET tipo = 'SPONTANEO' FROM tipi_versamento WHERE versamenti.id_tipo_versamento = tipi_versamento.id AND tipi_versamento.tipo = 'SPONTANEO';
ALTER TABLE versamenti MODIFY COLUMN tipo VARCHAR(35) NOT NULL;

ALTER TABLE tipi_vers_domini DROP COLUMN tipo;

DROP INDEX idx_tipi_versamento_tipo ON tipi_versamento;
ALTER TABLE tipi_versamento DROP COLUMN tipo;


DROP VIEW IF EXISTS versamenti_incassi;

CREATE VIEW versamenti_incassi AS SELECT
    versamenti.id,
    versamenti.cod_versamento_ente,
    versamenti.nome,
    versamenti.importo_totale,
    versamenti.stato_versamento,
    versamenti.descrizione_stato,
    versamenti.aggiornabile,
    versamenti.data_creazione,
    versamenti.data_validita,
    versamenti.data_scadenza,
    versamenti.data_ora_ultimo_aggiornamento,
    versamenti.causale_versamento,
    versamenti.debitore_tipo,
    versamenti.debitore_identificativo,
    versamenti.debitore_anagrafica,
    versamenti.debitore_indirizzo,
    versamenti.debitore_civico,
    versamenti.debitore_cap,
    versamenti.debitore_localita,
    versamenti.debitore_provincia,
    versamenti.debitore_nazione,
    versamenti.debitore_email,
    versamenti.debitore_telefono,
    versamenti.debitore_cellulare,
    versamenti.debitore_fax,
    versamenti.tassonomia_avviso,
    versamenti.tassonomia,
    versamenti.cod_lotto,
    versamenti.cod_versamento_lotto,
    versamenti.cod_anno_tributario,
    versamenti.cod_bundlekey,
    versamenti.dati_allegati,
    versamenti.incasso,
    versamenti.anomalie,
    versamenti.iuv_versamento,
    versamenti.numero_avviso,
    versamenti.id_dominio,
    versamenti.id_tipo_versamento,
    versamenti.id_tipo_versamento_dominio,
    versamenti.id_uo,
    versamenti.id_applicazione,
    versamenti.avvisatura_abilitata,
    versamenti.avvisatura_da_inviare,
    versamenti.avvisatura_operazione,
    versamenti.avvisatura_modalita,
    versamenti.avvisatura_tipo_pagamento,
    versamenti.avvisatura_cod_avvisatura,
    versamenti.divisione,
    versamenti.direzione,
    versamenti.id_tracciato,
    versamenti.id_sessione,
    versamenti.ack,
    versamenti.anomalo,
    versamenti.data_pagamento,
    versamenti.importo_pagato,
    versamenti.importo_incassato,
    versamenti.stato_pagamento,
    versamenti.iuv_pagamento,
    versamenti.src_iuv,
    versamenti.src_debitore_identificativo,
    versamenti.cod_rata,
    documenti.cod_documento,
    versamenti.tipo,
    (CASE WHEN versamenti.stato_versamento = 'NON_ESEGUITO' AND versamenti.data_validita > now() THEN 0 ELSE 1 END) AS smart_order_rank,
    (ABS((UNIX_TIMESTAMP(now()) *1000) - (UNIX_TIMESTAMP(COALESCE(versamenti.data_pagamento, versamenti.data_validita, versamenti.data_creazione)) * 1000))) AS smart_order_date
    FROM versamenti LEFT JOIN documenti ON versamenti.id_documento = documenti.id;

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
    versamenti.src_debitore_identificativo as vrs_src_debitore_identificativ,
    versamenti.cod_rata as vrs_cod_rata,
    versamenti.id_documento as vrs_id_documento,
    versamenti.tipo as vrs_tipo
FROM rpt JOIN versamenti ON versamenti.id = rpt.id_versamento;


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
     JOIN versamenti ON versamenti.id = singoli_versamenti.id_versamento;

-- 15/05/2020 Eliminazione colonne deprecate stato domini e stazioni
ALTER TABLE stazioni DROP COLUMN ndp_stato;
ALTER TABLE stazioni DROP COLUMN ndp_operazione;
ALTER TABLE stazioni DROP COLUMN ndp_descrizione;
ALTER TABLE stazioni DROP COLUMN ndp_data;

ALTER TABLE domini DROP COLUMN ndp_stato;
ALTER TABLE domini DROP COLUMN ndp_operazione;
ALTER TABLE domini DROP COLUMN ndp_descrizione;
ALTER TABLE domini DROP COLUMN ndp_data;

-- 19/05/2020 Aggiunta gestione della descrizione degli IBAN
ALTER TABLE iban_accredito ADD COLUMN descrizione VARCHAR(255);
ALTER TABLE iban_accredito DROP COLUMN attivato;

-- 02/06/2020 Batch check promemoria
insert into sonde(nome, classe, soglia_warn, soglia_error) values ('check-promemoria', 'org.openspcoop2.utils.sonde.impl.SondaCoda', 10, 100);


