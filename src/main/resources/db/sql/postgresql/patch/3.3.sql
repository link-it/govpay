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
  versamenti.id_dominio as id_dominio, 
  versamenti.id_uo as id_uo, 
  versamenti.id_tipo_versamento as id_tipo_versamento
FROM pagamenti_portale 
JOIN pag_port_versamenti ON pagamenti_portale.id = pag_port_versamenti.id_pagamento_portale 
JOIN versamenti ON versamenti.id=pag_port_versamenti.id_versamento;

-- 03/03/2020 Modifiche alla tabelle TipiVersamento e TipiVersamentoDominio
-- 1) Aggiunte Colonne per la configurazione dell'avvisatura con AppIO

ALTER TABLE tipi_versamento ADD COLUMN avv_app_io_prom_avv_abilitato BOOLEAN NOT NULL DEFAULT false;
ALTER TABLE tipi_versamento ADD COLUMN avv_app_io_prom_avv_tipo VARCHAR(35);
ALTER TABLE tipi_versamento ADD COLUMN avv_app_io_prom_avv_oggetto TEXT;
ALTER TABLE tipi_versamento ADD COLUMN avv_app_io_prom_avv_messaggio TEXT;
ALTER TABLE tipi_versamento ADD COLUMN avv_app_io_prom_ric_abilitato BOOLEAN NOT NULL DEFAULT false;
ALTER TABLE tipi_versamento ADD COLUMN avv_app_io_prom_ric_tipo VARCHAR(35);
ALTER TABLE tipi_versamento ADD COLUMN avv_app_io_prom_ric_oggetto TEXT;
ALTER TABLE tipi_versamento ADD COLUMN avv_app_io_prom_ric_messaggio TEXT;
ALTER TABLE tipi_versamento ADD COLUMN avv_app_io_prom_ric_eseguiti BOOLEAN;
ALTER TABLE tipi_versamento ADD COLUMN avv_app_io_prom_scad_abilitato BOOLEAN NOT NULL DEFAULT false;
ALTER TABLE tipi_versamento ADD COLUMN avv_app_io_prom_scad_preavviso INT;
ALTER TABLE tipi_versamento ADD COLUMN avv_app_io_prom_scad_tipo VARCHAR(35);
ALTER TABLE tipi_versamento ADD COLUMN avv_app_io_prom_scad_oggetto TEXT;
ALTER TABLE tipi_versamento ADD COLUMN avv_app_io_prom_scad_messaggio TEXT;

ALTER TABLE tipi_vers_domini ADD COLUMN avv_app_io_prom_avv_abilitato BOOLEAN;
ALTER TABLE tipi_vers_domini ADD COLUMN avv_app_io_prom_avv_tipo VARCHAR(35);
ALTER TABLE tipi_vers_domini ADD COLUMN avv_app_io_prom_avv_oggetto TEXT;
ALTER TABLE tipi_vers_domini ADD COLUMN avv_app_io_prom_avv_messaggio TEXT;
ALTER TABLE tipi_vers_domini ADD COLUMN avv_app_io_prom_ric_abilitato BOOLEAN;
ALTER TABLE tipi_vers_domini ADD COLUMN avv_app_io_prom_ric_tipo VARCHAR(35);
ALTER TABLE tipi_vers_domini ADD COLUMN avv_app_io_prom_ric_oggetto TEXT;
ALTER TABLE tipi_vers_domini ADD COLUMN avv_app_io_prom_ric_messaggio TEXT;
ALTER TABLE tipi_vers_domini ADD COLUMN avv_app_io_prom_ric_eseguiti BOOLEAN;
ALTER TABLE tipi_vers_domini ADD COLUMN avv_app_io_prom_scad_abilitato BOOLEAN;
ALTER TABLE tipi_vers_domini ADD COLUMN avv_app_io_prom_scad_preavviso INT;
ALTER TABLE tipi_vers_domini ADD COLUMN avv_app_io_prom_scad_tipo VARCHAR(35);
ALTER TABLE tipi_vers_domini ADD COLUMN avv_app_io_prom_scad_oggetto TEXT;
ALTER TABLE tipi_vers_domini ADD COLUMN avv_app_io_prom_scad_messaggio TEXT;

-- 2) Aggiunte Colonne per la configurazione dell'interfaccia caricamento pendenze nei portali pagamento

ALTER TABLE tipi_versamento ADD COLUMN pag_form_tipo VARCHAR(35);
ALTER TABLE tipi_versamento ADD COLUMN pag_form_definizione TEXT;
ALTER TABLE tipi_versamento ADD COLUMN pag_validazione_def TEXT;
ALTER TABLE tipi_versamento ADD COLUMN pag_trasformazione_tipo VARCHAR(35);
ALTER TABLE tipi_versamento ADD COLUMN pag_trasformazione_def TEXT;
ALTER TABLE tipi_versamento ADD COLUMN pag_cod_applicazione VARCHAR(35);
ALTER TABLE tipi_versamento ADD COLUMN pag_abilitato BOOLEAN NOT NULL DEFAULT false;

ALTER TABLE tipi_vers_domini ADD COLUMN pag_form_tipo VARCHAR(35);
ALTER TABLE tipi_vers_domini ADD COLUMN pag_form_definizione TEXT;
ALTER TABLE tipi_vers_domini ADD COLUMN pag_validazione_def TEXT;
ALTER TABLE tipi_vers_domini ADD COLUMN pag_trasformazione_tipo VARCHAR(35);
ALTER TABLE tipi_vers_domini ADD COLUMN pag_trasformazione_def TEXT;
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
ALTER TABLE tipi_versamento ADD COLUMN avv_mail_prom_scad_oggetto TEXT;
ALTER TABLE tipi_versamento ADD COLUMN avv_mail_prom_scad_messaggio TEXT;

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
ALTER TABLE tipi_vers_domini ADD COLUMN avv_mail_prom_scad_oggetto TEXT;
ALTER TABLE tipi_vers_domini ADD COLUMN avv_mail_prom_scad_messaggio TEXT;

-- 12/02/2020 Tabella Notifiche AppIO
CREATE SEQUENCE seq_notifiche_app_io start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE notifiche_app_io
(
	debitore_identificativo VARCHAR(35) NOT NULL,
	cod_versamento_ente VARCHAR(35) NOT NULL,
	cod_applicazione VARCHAR(35) NOT NULL,
	cod_dominio VARCHAR(35) NOT NULL,
	iuv VARCHAR(35) NOT NULL,
	tipo_esito VARCHAR(16) NOT NULL,
	data_creazione TIMESTAMP NOT NULL,
	stato VARCHAR(16) NOT NULL,
	descrizione_stato VARCHAR(255),
	data_aggiornamento_stato TIMESTAMP NOT NULL,
	data_prossima_spedizione TIMESTAMP NOT NULL,
	tentativi_spedizione BIGINT,
	id_messaggio VARCHAR(255),
	stato_messaggio VARCHAR(16),
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_notifiche_app_io') NOT NULL,
	id_versamento BIGINT NOT NULL,
	id_tipo_versamento_dominio BIGINT NOT NULL,
	-- fk/pk keys constraints
	CONSTRAINT fk_nai_id_versamento FOREIGN KEY (id_versamento) REFERENCES versamenti(id),
	CONSTRAINT fk_nai_id_tipo_versamento_dominio FOREIGN KEY (id_tipo_versamento_dominio) REFERENCES tipi_vers_domini(id),
	CONSTRAINT pk_notifiche_app_io PRIMARY KEY (id)
);

-- index
CREATE INDEX idx_nai_da_spedire ON notifiche_app_io (stato,data_prossima_spedizione);

insert into sonde(nome, classe, soglia_warn, soglia_error) values ('update-ntfy-appio', 'org.openspcoop2.utils.sonde.impl.SondaBatch', 86400000, 172800000);
insert into sonde(nome, classe, soglia_warn, soglia_error) values ('check-ntfy-appio', 'org.openspcoop2.utils.sonde.impl.SondaCoda', 10, 100);




