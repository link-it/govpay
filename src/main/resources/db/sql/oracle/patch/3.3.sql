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

ALTER TABLE tipi_versamento ADD avv_app_io_prom_avv_abilitato NUMBER DEFAULT 0;
UPDATE tipi_versamento SET avv_app_io_prom_avv_abilitato = 0;
ALTER TABLE tipi_versamento MODIFY (avv_app_io_prom_avv_abilitato NOT NULL);
ALTER TABLE tipi_versamento ADD avv_app_io_prom_avv_tipo VARCHAR2(35 CHAR);
ALTER TABLE tipi_versamento ADD avv_app_io_prom_avv_oggetto CLOB;
ALTER TABLE tipi_versamento ADD avv_app_io_prom_avv_messaggio CLOB;
ALTER TABLE tipi_versamento ADD avv_app_io_prom_ric_abilitato NUMBER DEFAULT 0;
UPDATE tipi_versamento SET avv_app_io_prom_ric_abilitato = 0;
ALTER TABLE tipi_versamento MODIFY (avv_app_io_prom_ric_abilitato NOT NULL);
ALTER TABLE tipi_versamento ADD avv_app_io_prom_ric_tipo VARCHAR2(35 CHAR);
ALTER TABLE tipi_versamento ADD avv_app_io_prom_ric_oggetto CLOB;
ALTER TABLE tipi_versamento ADD avv_app_io_prom_ric_messaggio CLOB;
ALTER TABLE tipi_versamento ADD avv_app_io_prom_ric_eseguiti NUMBER;
ALTER TABLE tipi_versamento ADD avv_app_io_prom_scad_abilitato NUMBER DEFAULT 0;
UPDATE tipi_versamento SET avv_app_io_prom_scad_abilitato = 0;
ALTER TABLE tipi_versamento MODIFY (avv_app_io_prom_scad_abilitato NOT NULL);
ALTER TABLE tipi_versamento ADD avv_app_io_prom_scad_preavviso NUMBER;
ALTER TABLE tipi_versamento ADD avv_app_io_prom_scad_tipo VARCHAR2(35 CHAR);
ALTER TABLE tipi_versamento ADD avv_app_io_prom_scad_oggetto CLOB;
ALTER TABLE tipi_versamento ADD avv_app_io_prom_scad_messaggio CLOB;

ALTER TABLE tipi_vers_domini ADD avv_app_io_prom_avv_abilitato NUMBER;
ALTER TABLE tipi_vers_domini ADD avv_app_io_prom_avv_tipo VARCHAR2(35 CHAR);
ALTER TABLE tipi_vers_domini ADD avv_app_io_prom_avv_oggetto CLOB;
ALTER TABLE tipi_vers_domini ADD avv_app_io_prom_avv_messaggio CLOB;
ALTER TABLE tipi_vers_domini ADD avv_app_io_prom_ric_abilitato NUMBER;
ALTER TABLE tipi_vers_domini ADD avv_app_io_prom_ric_tipo VARCHAR2(35 CHAR);
ALTER TABLE tipi_vers_domini ADD avv_app_io_prom_ric_oggetto CLOB;
ALTER TABLE tipi_vers_domini ADD avv_app_io_prom_ric_messaggio CLOB;
ALTER TABLE tipi_vers_domini ADD avv_app_io_prom_ric_eseguiti NUMBER;
ALTER TABLE tipi_vers_domini ADD avv_app_io_prom_scad_abilitato NUMBER;
ALTER TABLE tipi_vers_domini ADD avv_app_io_prom_scad_preavviso NUMBER;
ALTER TABLE tipi_vers_domini ADD avv_app_io_prom_scad_tipo VARCHAR2(35 CHAR);
ALTER TABLE tipi_vers_domini ADD avv_app_io_prom_scad_oggetto CLOB;
ALTER TABLE tipi_vers_domini ADD avv_app_io_prom_scad_messaggio CLOB;

-- 2) Aggiunte Colonne per la configurazione dell'interfaccia caricamento pendenze nei portali pagamento

ALTER TABLE tipi_versamento ADD pag_form_tipo VARCHAR2(35 CHAR);
ALTER TABLE tipi_versamento ADD pag_form_definizione CLOB;
ALTER TABLE tipi_versamento ADD pag_validazione_def CLOB;
ALTER TABLE tipi_versamento ADD pag_trasformazione_tipo VARCHAR2(35 CHAR);
ALTER TABLE tipi_versamento ADD pag_trasformazione_def CLOB;
ALTER TABLE tipi_versamento ADD pag_cod_applicazione VARCHAR2(35 CHAR);
ALTER TABLE tipi_versamento ADD pag_abilitato NUMBER NOT NULL;
ALTER TABLE tipi_versamento MODIFY pag_abilitato DEFAULT 0;

ALTER TABLE tipi_vers_domini ADD pag_form_tipo VARCHAR2(35 CHAR);
ALTER TABLE tipi_vers_domini ADD pag_form_definizione CLOB;
ALTER TABLE tipi_vers_domini ADD pag_validazione_def CLOB;
ALTER TABLE tipi_vers_domini ADD pag_trasformazione_tipo VARCHAR2(35 CHAR);
ALTER TABLE tipi_vers_domini ADD pag_trasformazione_def CLOB;
ALTER TABLE tipi_vers_domini ADD pag_cod_applicazione VARCHAR2(35 CHAR);
ALTER TABLE tipi_vers_domini ADD pag_abilitato NUMBER;

-- 3) Rinominate Colonne per la configurazione dell'interfaccia caricamento pendenze nei portali backoffice

ALTER TABLE tipi_versamento RENAME COLUMN form_tipo TO bo_form_tipo;
ALTER TABLE tipi_versamento RENAME COLUMN form_definizione TO bo_form_definizione;
ALTER TABLE tipi_versamento RENAME COLUMN validazione_definizione TO bo_validazione_def;
ALTER TABLE tipi_versamento RENAME COLUMN trasformazione_tipo TO bo_trasformazione_tipo;
ALTER TABLE tipi_versamento RENAME COLUMN trasformazione_definizione TO bo_trasformazione_def;
ALTER TABLE tipi_versamento RENAME COLUMN cod_applicazione TO bo_cod_applicazione;
ALTER TABLE tipi_versamento ADD bo_abilitato NUMBER DEFAULT 0;
UPDATE tipi_versamento SET bo_abilitato = 0;
UPDATE tipi_versamento SET bo_abilitato = 1 WHERE (bo_form_tipo IS NOT NULL OR bo_validazione_def IS NOT NULL OR bo_trasformazione_tipo IS NOT NULL OR bo_cod_applicazione IS NOT NULL);
ALTER TABLE tipi_versamento MODIFY (bo_abilitato NOT NULL);

ALTER TABLE tipi_vers_domini RENAME COLUMN form_tipo TO bo_form_tipo;
ALTER TABLE tipi_vers_domini RENAME COLUMN form_definizione TO bo_form_definizione;
ALTER TABLE tipi_vers_domini RENAME COLUMN validazione_definizione TO bo_validazione_def;
ALTER TABLE tipi_vers_domini RENAME COLUMN trasformazione_tipo TO bo_trasformazione_tipo;
ALTER TABLE tipi_vers_domini RENAME COLUMN trasformazione_definizione TO bo_trasformazione_def;
ALTER TABLE tipi_vers_domini RENAME COLUMN cod_applicazione TO bo_cod_applicazione;
ALTER TABLE tipi_vers_domini ADD bo_abilitato NUMBER;
UPDATE tipi_vers_domini SET bo_abilitato = 1 WHERE (bo_form_tipo IS NOT NULL OR bo_validazione_def IS NOT NULL OR bo_trasformazione_tipo IS NOT NULL OR bo_cod_applicazione IS NOT NULL);

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
ALTER TABLE tipi_versamento ADD avv_mail_prom_ric_eseguiti NUMBER;
ALTER TABLE tipi_versamento ADD avv_mail_prom_scad_abilitato NUMBER DEFAULT 0;
UPDATE tipi_versamento SET avv_mail_prom_scad_abilitato = 0;
ALTER TABLE tipi_versamento MODIFY (avv_mail_prom_scad_abilitato NOT NULL);
ALTER TABLE tipi_versamento ADD avv_mail_prom_scad_preavviso NUMBER;
ALTER TABLE tipi_versamento ADD avv_mail_prom_scad_tipo VARCHAR2(35 CHAR);
ALTER TABLE tipi_versamento ADD avv_mail_prom_scad_oggetto CLOB;
ALTER TABLE tipi_versamento ADD avv_mail_prom_scad_messaggio CLOB;

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
ALTER TABLE tipi_vers_domini ADD avv_mail_prom_ric_eseguiti NUMBER;
ALTER TABLE tipi_vers_domini ADD avv_mail_prom_scad_abilitato BOOLEAN;
ALTER TABLE tipi_vers_domini ADD avv_mail_prom_scad_preavviso NUMBER;
ALTER TABLE tipi_vers_domini ADD avv_mail_prom_scad_tipo VARCHAR2(35 CHAR);
ALTER TABLE tipi_vers_domini ADD avv_mail_prom_scad_oggetto CLOB;
ALTER TABLE tipi_vers_domini ADD avv_mail_prom_scad_messaggio CLOB;


-- 12/02/2020 Tabella Notifiche AppIO
CREATE SEQUENCE seq_notifiche_app_io MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE notifiche_app_io
(
	debitore_identificativo VARCHAR2(35 CHAR) NOT NULL,
	cod_versamento_ente VARCHAR2(35 CHAR) NOT NULL,
	cod_applicazione VARCHAR2(35 CHAR) NOT NULL,
	cod_dominio VARCHAR2(35 CHAR) NOT NULL,
	iuv VARCHAR2(35 CHAR) NOT NULL,
	tipo_esito VARCHAR2(16 CHAR) NOT NULL,
	data_creazione TIMESTAMP NOT NULL,
	stato VARCHAR2(16 CHAR) NOT NULL,
	descrizione_stato VARCHAR2(255 CHAR),
	data_aggiornamento_stato TIMESTAMP NOT NULL,
	data_prossima_spedizione TIMESTAMP NOT NULL,
	tentativi_spedizione NUMBER,
	id_messaggio VARCHAR2(255 CHAR),
	stato_messaggio VARCHAR2(16 CHAR),
	-- fk/pk columns
	id NUMBER NOT NULL,
	id_versamento NUMBER NOT NULL,
	id_tipo_versamento_dominio NUMBER NOT NULL,
	-- fk/pk keys constraints
	CONSTRAINT fk_nai_id_versamento FOREIGN KEY (id_versamento) REFERENCES versamenti(id),
	CONSTRAINT fk_nai_id_tipo_versamento_dominio FOREIGN KEY (id_tipo_versamento_dominio) REFERENCES tipi_vers_domini(id),
	CONSTRAINT pk_notifiche_app_io PRIMARY KEY (id)
);

-- index
CREATE INDEX idx_nai_da_spedire ON notifiche_app_io (stato,data_prossima_spedizione);
CREATE TRIGGER trg_notifiche_app_io
BEFORE
insert on notifiche_app_io
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_notifiche_app_io.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;
/

insert into sonde(nome, classe, soglia_warn, soglia_error) values ('update-ntfy-appio', 'org.openspcoop2.utils.sonde.impl.SondaBatch', 86400000, 172800000);
insert into sonde(nome, classe, soglia_warn, soglia_error) values ('check-ntfy-appio', 'org.openspcoop2.utils.sonde.impl.SondaCoda', 10, 100);



