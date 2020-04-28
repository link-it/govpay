-- Funzione per calcolare il numero di millisecondi dal 1/1/1970
create OR REPLACE FUNCTION date_to_unix_for_smart_order (p_date  date,in_src_tz in varchar2 default 'Europe/Kiev') return number is
begin
    return round((cast((FROM_TZ(CAST(p_date as timestamp), in_src_tz) at time zone 'GMT') as date)-TO_DATE('01.01.1970','dd.mm.yyyy'))*(24*60*60));
end;

CREATE SEQUENCE seq_configurazione MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE configurazione
(
	nome VARCHAR2(255 CHAR) NOT NULL,
	valore CLOB,
	-- fk/pk columns
	id NUMBER NOT NULL,
	-- unique constraints
	CONSTRAINT unique_configurazione_1 UNIQUE (nome),
	-- fk/pk keys constraints
	CONSTRAINT pk_configurazione PRIMARY KEY (id)
);

CREATE TRIGGER trg_configurazione
BEFORE
insert on configurazione
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_configurazione.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;
/



CREATE SEQUENCE seq_intermediari MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE intermediari
(
	cod_intermediario VARCHAR2(35 CHAR) NOT NULL,
	cod_connettore_pdd VARCHAR2(35 CHAR) NOT NULL,
	cod_connettore_ftp VARCHAR2(35 CHAR),
	denominazione VARCHAR2(255 CHAR) NOT NULL,
	principal VARCHAR2(4000 CHAR) NOT NULL,
	principal_originale VARCHAR2(4000 CHAR) NOT NULL,
	abilitato NUMBER NOT NULL,
	-- fk/pk columns
	id NUMBER NOT NULL,
	-- unique constraints
	CONSTRAINT unique_intermediari_1 UNIQUE (cod_intermediario),
	-- fk/pk keys constraints
	CONSTRAINT pk_intermediari PRIMARY KEY (id)
);

CREATE TRIGGER trg_intermediari
BEFORE
insert on intermediari
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_intermediari.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;
/



CREATE SEQUENCE seq_stazioni MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE stazioni
(
	cod_stazione VARCHAR2(35 CHAR) NOT NULL,
	password VARCHAR2(35 CHAR) NOT NULL,
	abilitato NUMBER NOT NULL,
	application_code NUMBER NOT NULL,
	ndp_stato NUMBER,
	ndp_operazione VARCHAR2(256 CHAR),
	ndp_descrizione VARCHAR2(1024 CHAR),
	ndp_data TIMESTAMP,
	-- fk/pk columns
	id NUMBER NOT NULL,
	id_intermediario NUMBER NOT NULL,
	-- unique constraints
	CONSTRAINT unique_stazioni_1 UNIQUE (cod_stazione),
	-- fk/pk keys constraints
	CONSTRAINT fk_stz_id_intermediario FOREIGN KEY (id_intermediario) REFERENCES intermediari(id),
	CONSTRAINT pk_stazioni PRIMARY KEY (id)
);

CREATE TRIGGER trg_stazioni
BEFORE
insert on stazioni
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_stazioni.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;
/



CREATE SEQUENCE seq_utenze MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE utenze
(
	principal VARCHAR2(4000 CHAR) NOT NULL,
	principal_originale VARCHAR2(4000 CHAR) NOT NULL,
	abilitato NUMBER NOT NULL,
	autorizzazione_domini_star NUMBER NOT NULL,
	autorizzazione_tipi_vers_star NUMBER NOT NULL,
	ruoli VARCHAR2(512 CHAR),
	password VARCHAR2(255 CHAR),
	-- fk/pk columns
	id NUMBER NOT NULL,
	-- unique constraints
	CONSTRAINT unique_utenze_1 UNIQUE (principal),
	-- fk/pk keys constraints
	CONSTRAINT pk_utenze PRIMARY KEY (id)
);


ALTER TABLE utenze MODIFY abilitato DEFAULT 1;
ALTER TABLE utenze MODIFY autorizzazione_domini_star DEFAULT 0;
ALTER TABLE utenze MODIFY autorizzazione_tipi_vers_star DEFAULT 0;

CREATE TRIGGER trg_utenze
BEFORE
insert on utenze
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_utenze.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;
/



CREATE SEQUENCE seq_applicazioni MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE applicazioni
(
	cod_applicazione VARCHAR2(35 CHAR) NOT NULL,
	auto_iuv NUMBER NOT NULL,
	firma_ricevuta VARCHAR2(1 CHAR) NOT NULL,
	cod_connettore_integrazione VARCHAR2(255 CHAR),
	trusted NUMBER NOT NULL,
	cod_applicazione_iuv VARCHAR2(3 CHAR),
	reg_exp VARCHAR2(1024 CHAR),
	-- fk/pk columns
	id NUMBER NOT NULL,
	id_utenza NUMBER NOT NULL,
	-- unique constraints
	CONSTRAINT unique_applicazioni_1 UNIQUE (cod_applicazione),
	CONSTRAINT unique_applicazioni_2 UNIQUE (id_utenza),
	-- fk/pk keys constraints
	CONSTRAINT fk_app_id_utenza FOREIGN KEY (id_utenza) REFERENCES utenze(id),
	CONSTRAINT pk_applicazioni PRIMARY KEY (id)
);

CREATE TRIGGER trg_applicazioni
BEFORE
insert on applicazioni
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_applicazioni.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;
/



CREATE SEQUENCE seq_domini MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE domini
(
	cod_dominio VARCHAR2(35 CHAR) NOT NULL,
	gln VARCHAR2(35 CHAR),
	abilitato NUMBER NOT NULL,
	ragione_sociale VARCHAR2(70 CHAR) NOT NULL,
	aux_digit NUMBER NOT NULL,
	iuv_prefix VARCHAR2(255 CHAR),
	segregation_code NUMBER,
	ndp_stato NUMBER,
	ndp_operazione VARCHAR2(256 CHAR),
	ndp_descrizione VARCHAR2(1024 CHAR),
	ndp_data TIMESTAMP,
	logo BLOB,
	cbill VARCHAR(255),
	aut_stampa_poste VARCHAR2(255 CHAR),
	-- fk/pk columns
	id NUMBER NOT NULL,
	id_stazione NUMBER NOT NULL,
	id_applicazione_default NUMBER,
	-- unique constraints
	CONSTRAINT unique_domini_1 UNIQUE (cod_dominio),
	-- fk/pk keys constraints
	CONSTRAINT fk_dom_id_stazione FOREIGN KEY (id_stazione) REFERENCES stazioni(id),
	CONSTRAINT fk_dom_id_applicazione_default FOREIGN KEY (id_applicazione_default) REFERENCES applicazioni(id),
	CONSTRAINT pk_domini PRIMARY KEY (id)
);


ALTER TABLE domini MODIFY aux_digit DEFAULT 0;

CREATE TRIGGER trg_domini
BEFORE
insert on domini
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_domini.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;
/



CREATE SEQUENCE seq_iban_accredito MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE iban_accredito
(
	cod_iban VARCHAR2(255 CHAR) NOT NULL,
	bic_accredito VARCHAR2(255 CHAR),
	postale NUMBER NOT NULL,
	attivato NUMBER NOT NULL,
	abilitato NUMBER NOT NULL,
	-- fk/pk columns
	id NUMBER NOT NULL,
	id_dominio NUMBER NOT NULL,
	-- unique constraints
	CONSTRAINT unique_iban_accredito_1 UNIQUE (cod_iban,id_dominio),
	-- fk/pk keys constraints
	CONSTRAINT fk_iba_id_dominio FOREIGN KEY (id_dominio) REFERENCES domini(id),
	CONSTRAINT pk_iban_accredito PRIMARY KEY (id)
);

CREATE TRIGGER trg_iban_accredito
BEFORE
insert on iban_accredito
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_iban_accredito.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;
/



CREATE SEQUENCE seq_tipi_tributo MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE tipi_tributo
(
	cod_tributo VARCHAR2(255 CHAR) NOT NULL,
	descrizione VARCHAR2(255 CHAR),
	tipo_contabilita VARCHAR2(1 CHAR),
	cod_contabilita VARCHAR2(255 CHAR),
	-- fk/pk columns
	id NUMBER NOT NULL,
	-- unique constraints
	CONSTRAINT unique_tipi_tributo_1 UNIQUE (cod_tributo),
	-- fk/pk keys constraints
	CONSTRAINT pk_tipi_tributo PRIMARY KEY (id)
);

CREATE TRIGGER trg_tipi_tributo
BEFORE
insert on tipi_tributo
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_tipi_tributo.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;
/



CREATE SEQUENCE seq_tributi MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE tributi
(
	abilitato NUMBER NOT NULL,
	tipo_contabilita VARCHAR2(1 CHAR),
	codice_contabilita VARCHAR2(255 CHAR),
	-- fk/pk columns
	id NUMBER NOT NULL,
	id_dominio NUMBER NOT NULL,
	id_iban_accredito NUMBER,
	id_iban_appoggio NUMBER,
	id_tipo_tributo NUMBER NOT NULL,
	-- unique constraints
	CONSTRAINT unique_tributi_1 UNIQUE (id_dominio,id_tipo_tributo),
	-- fk/pk keys constraints
	CONSTRAINT fk_trb_id_dominio FOREIGN KEY (id_dominio) REFERENCES domini(id),
	CONSTRAINT fk_trb_id_iban_accredito FOREIGN KEY (id_iban_accredito) REFERENCES iban_accredito(id),
	CONSTRAINT fk_trb_id_iban_appoggio FOREIGN KEY (id_iban_appoggio) REFERENCES iban_accredito(id),
	CONSTRAINT fk_trb_id_tipo_tributo FOREIGN KEY (id_tipo_tributo) REFERENCES tipi_tributo(id),
	CONSTRAINT pk_tributi PRIMARY KEY (id)
);

CREATE TRIGGER trg_tributi
BEFORE
insert on tributi
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_tributi.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;
/



CREATE SEQUENCE seq_uo MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE uo
(
	cod_uo VARCHAR2(35 CHAR) NOT NULL,
	abilitato NUMBER NOT NULL,
	uo_codice_identificativo VARCHAR2(35 CHAR),
	uo_denominazione VARCHAR2(70 CHAR),
	uo_indirizzo VARCHAR2(70 CHAR),
	uo_civico VARCHAR2(16 CHAR),
	uo_cap VARCHAR2(16 CHAR),
	uo_localita VARCHAR2(35 CHAR),
	uo_provincia VARCHAR2(35 CHAR),
	uo_nazione VARCHAR2(2 CHAR),
	uo_area VARCHAR2(255 CHAR),
	uo_url_sito_web VARCHAR2(255 CHAR),
	uo_email VARCHAR2(255 CHAR),
	uo_pec VARCHAR2(255 CHAR),
	uo_tel VARCHAR2(255 CHAR),
	uo_fax VARCHAR2(255 CHAR),
	-- fk/pk columns
	id NUMBER NOT NULL,
	id_dominio NUMBER NOT NULL,
	-- unique constraints
	CONSTRAINT unique_uo_1 UNIQUE (cod_uo,id_dominio),
	-- fk/pk keys constraints
	CONSTRAINT fk_uo_id_dominio FOREIGN KEY (id_dominio) REFERENCES domini(id),
	CONSTRAINT pk_uo PRIMARY KEY (id)
);

CREATE TRIGGER trg_uo
BEFORE
insert on uo
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_uo.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;
/



CREATE SEQUENCE seq_utenze_domini MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE utenze_domini
(
	-- fk/pk columns
	id NUMBER NOT NULL,
	id_utenza NUMBER NOT NULL,
	id_dominio NUMBER,
	id_uo NUMBER,
	-- fk/pk keys constraints
	CONSTRAINT fk_nzd_id_utenza FOREIGN KEY (id_utenza) REFERENCES utenze(id),
	CONSTRAINT fk_nzd_id_dominio FOREIGN KEY (id_dominio) REFERENCES domini(id),
	CONSTRAINT fk_nzd_id_uo FOREIGN KEY (id_uo) REFERENCES uo(id),
	CONSTRAINT pk_utenze_domini PRIMARY KEY (id)
);

CREATE TRIGGER trg_utenze_domini
BEFORE
insert on utenze_domini
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_utenze_domini.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;
/



CREATE SEQUENCE seq_operatori MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE operatori
(
	nome VARCHAR2(35 CHAR) NOT NULL,
	-- fk/pk columns
	id NUMBER NOT NULL,
	id_utenza NUMBER NOT NULL,
	-- unique constraints
	CONSTRAINT unique_operatori_1 UNIQUE (id_utenza),
	-- fk/pk keys constraints
	CONSTRAINT fk_opr_id_utenza FOREIGN KEY (id_utenza) REFERENCES utenze(id),
	CONSTRAINT pk_operatori PRIMARY KEY (id)
);

CREATE TRIGGER trg_operatori
BEFORE
insert on operatori
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_operatori.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;
/



CREATE SEQUENCE seq_connettori MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE connettori
(
	cod_connettore VARCHAR2(255 CHAR) NOT NULL,
	cod_proprieta VARCHAR2(255 CHAR) NOT NULL,
	valore VARCHAR2(255 CHAR) NOT NULL,
	-- fk/pk columns
	id NUMBER NOT NULL,
	-- unique constraints
	CONSTRAINT unique_connettori_1 UNIQUE (cod_connettore,cod_proprieta),
	-- fk/pk keys constraints
	CONSTRAINT pk_connettori PRIMARY KEY (id)
);

CREATE TRIGGER trg_connettori
BEFORE
insert on connettori
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_connettori.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;
/



CREATE SEQUENCE seq_acl MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE acl
(
	ruolo VARCHAR2(255 CHAR),
	servizio VARCHAR2(255 CHAR) NOT NULL,
	diritti VARCHAR2(255 CHAR) NOT NULL,
	-- fk/pk columns
	id NUMBER NOT NULL,
	id_utenza NUMBER,
	-- fk/pk keys constraints
	CONSTRAINT fk_acl_id_utenza FOREIGN KEY (id_utenza) REFERENCES utenze(id),
	CONSTRAINT pk_acl PRIMARY KEY (id)
);

CREATE TRIGGER trg_acl
BEFORE
insert on acl
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_acl.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;
/



CREATE SEQUENCE seq_tracciati MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE tracciati
(
	cod_dominio VARCHAR2(35 CHAR) NOT NULL
	cod_tipo_versamento VARCHAR2(35 CHAR),
	formato VARCHAR2(10 CHAR) NOT NULL,
	tipo VARCHAR2(10 CHAR) NOT NULL,
	stato VARCHAR2(12 CHAR) NOT NULL,
	descrizione_stato VARCHAR2(256 CHAR),
	data_caricamento TIMESTAMP NOT NULL,
	data_completamento TIMESTAMP,
	bean_dati CLOB,
	file_name_richiesta VARCHAR2(256 CHAR),
	raw_richiesta BLOB,
	file_name_esito VARCHAR2(256 CHAR),
	raw_esito BLOB,
	-- fk/pk columns
	id NUMBER NOT NULL,
	id_operatore NUMBER,
	-- fk/pk keys constraints
	CONSTRAINT fk_trc_id_operatore FOREIGN KEY (id_operatore) REFERENCES operatori(id),
	CONSTRAINT pk_tracciati PRIMARY KEY (id)
);

CREATE TRIGGER trg_tracciati
BEFORE
insert on tracciati
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_tracciati.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;
/



CREATE SEQUENCE seq_tipi_versamento MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE tipi_versamento
(
	cod_tipo_versamento VARCHAR2(35 CHAR) NOT NULL,
	descrizione VARCHAR2(255 CHAR) NOT NULL,
	codifica_iuv VARCHAR2(4 CHAR),
	tipo VARCHAR(35) NOT NULL,
	paga_terzi NUMBER NOT NULL,
	abilitato NUMBER NOT NULL,
	bo_form_tipo VARCHAR2(35 CHAR),
	bo_form_definizione CLOB,
	bo_validazione_def CLOB,
	bo_trasformazione_tipo VARCHAR2(35 CHAR),
	bo_trasformazione_def CLOB,
	bo_cod_applicazione VARCHAR2(35 CHAR),
	bo_abilitato NUMBER NOT NULL,
	pag_form_tipo VARCHAR2(35 CHAR),
	pag_form_definizione CLOB,
	pag_form_impaginazione CLOB,
	pag_validazione_def CLOB,
	pag_trasformazione_tipo VARCHAR2(35 CHAR),
	pag_trasformazione_def CLOB,
	pag_cod_applicazione VARCHAR2(35 CHAR),
	pag_abilitato NUMBER NOT NULL,
	avv_mail_prom_avv_abilitato NUMBER NOT NULL,
	avv_mail_prom_avv_pdf NUMBER,
	avv_mail_prom_avv_tipo VARCHAR2(35 CHAR),
	avv_mail_prom_avv_oggetto CLOB,
	avv_mail_prom_avv_messaggio CLOB,
	avv_mail_prom_ric_abilitato NUMBER NOT NULL,
	avv_mail_prom_ric_pdf NUMBER,
	avv_mail_prom_ric_tipo VARCHAR2(35 CHAR),
	avv_mail_prom_ric_oggetto CLOB,
	avv_mail_prom_ric_messaggio CLOB,
	avv_mail_prom_ric_eseguiti NUMBER,
	avv_mail_prom_scad_abilitato NUMBER NOT NULL,
	avv_mail_prom_scad_preavviso NUMBER,
	avv_mail_prom_scad_tipo VARCHAR2(35 CHAR),
	avv_mail_prom_scad_oggetto CLOB,
	avv_mail_prom_scad_messaggio CLOB,
	visualizzazione_definizione CLOB,
	trac_csv_tipo VARCHAR2(35 CHAR),
	trac_csv_header_risposta CLOB,
	trac_csv_template_richiesta CLOB,
	trac_csv_template_risposta CLOB,
	avv_app_io_prom_avv_abilitato NUMBER NOT NULL,
	avv_app_io_prom_avv_tipo VARCHAR2(35 CHAR),
	avv_app_io_prom_avv_oggetto CLOB,
	avv_app_io_prom_avv_messaggio CLOB,
	avv_app_io_prom_ric_abilitato NUMBER NOT NULL,
	avv_app_io_prom_ric_tipo VARCHAR2(35 CHAR),
	avv_app_io_prom_ric_oggetto CLOB,
	avv_app_io_prom_ric_messaggio CLOB,
	avv_app_io_prom_ric_eseguiti NUMBER,
	avv_app_io_prom_scad_abilitato NUMBER NOT NULL,
	avv_app_io_prom_scad_preavviso NUMBER,
	avv_app_io_prom_scad_tipo VARCHAR2(35 CHAR),
	avv_app_io_prom_scad_oggetto CLOB,
	avv_app_io_prom_scad_messaggio CLOB,
	-- fk/pk columns
	id NUMBER NOT NULL,
	-- unique constraints
	CONSTRAINT unique_tipi_versamento_1 UNIQUE (cod_tipo_versamento),
	-- fk/pk keys constraints
	CONSTRAINT pk_tipi_versamento PRIMARY KEY (id)
);

-- index
CREATE INDEX idx_tipi_versamento_tipo ON tipi_versamento (tipo);

ALTER TABLE tipi_versamento MODIFY paga_terzi DEFAULT 0;
ALTER TABLE tipi_versamento MODIFY bo_abilitato DEFAULT 0;
ALTER TABLE tipi_versamento MODIFY pag_abilitato DEFAULT 0;
ALTER TABLE tipi_versamento MODIFY avv_mail_prom_avv_abilitato DEFAULT 0;
ALTER TABLE tipi_versamento MODIFY avv_mail_prom_ric_abilitato DEFAULT 0;
ALTER TABLE tipi_versamento MODIFY avv_mail_prom_scad_abilitato DEFAULT 0;
ALTER TABLE tipi_versamento MODIFY avv_app_io_prom_avv_abilitato DEFAULT 0;
ALTER TABLE tipi_versamento MODIFY avv_app_io_prom_ric_abilitato DEFAULT 0;
ALTER TABLE tipi_versamento MODIFY avv_app_io_prom_scad_abilitato DEFAULT 0;

CREATE TRIGGER trg_tipi_versamento
BEFORE
insert on tipi_versamento
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_tipi_versamento.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;
/



CREATE SEQUENCE seq_tipi_vers_domini MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE tipi_vers_domini
(
	codifica_iuv VARCHAR2(4 CHAR),
	tipo VARCHAR2(35 CHAR),
	paga_terzi NUMBER,
	abilitato NUMBER,
	bo_form_tipo VARCHAR2(35 CHAR),
	bo_form_definizione CLOB,
	bo_validazione_def CLOB,
	bo_trasformazione_tipo VARCHAR2(35 CHAR),
	bo_trasformazione_def CLOB,
	bo_cod_applicazione VARCHAR2(35 CHAR),
	bo_abilitato NUMBER,
	pag_form_tipo VARCHAR2(35 CHAR),
	pag_form_definizione CLOB,
	pag_form_impaginazione CLOB,
	pag_validazione_def CLOB,
	pag_trasformazione_tipo VARCHAR2(35 CHAR),
	pag_trasformazione_def CLOB,
	pag_cod_applicazione VARCHAR2(35 CHAR),
	pag_abilitato NUMBER,
	avv_mail_prom_avv_abilitato NUMBER,
	avv_mail_prom_avv_pdf NUMBER,
	avv_mail_prom_avv_tipo VARCHAR2(35 CHAR),
	avv_mail_prom_avv_oggetto CLOB,
	avv_mail_prom_avv_messaggio CLOB,
	avv_mail_prom_ric_abilitato NUMBER,
	avv_mail_prom_ric_pdf NUMBER,
	avv_mail_prom_ric_tipo VARCHAR2(35 CHAR),
	avv_mail_prom_ric_oggetto CLOB,
	avv_mail_prom_ric_messaggio CLOB,
	avv_mail_prom_ric_eseguiti NUMBER,
	avv_mail_prom_scad_abilitato NUMBER,
	avv_mail_prom_scad_preavviso NUMBER,
	avv_mail_prom_scad_tipo VARCHAR2(35 CHAR),
	avv_mail_prom_scad_oggetto CLOB,
	avv_mail_prom_scad_messaggio CLOB,
	visualizzazione_definizione CLOB,
	trac_csv_tipo VARCHAR2(35 CHAR),
	trac_csv_header_risposta CLOB,
	trac_csv_template_richiesta CLOB,
	trac_csv_template_risposta CLOB,
	app_io_api_key VARCHAR2(255 CHAR),
	avv_app_io_prom_avv_abilitato NUMBER,
	avv_app_io_prom_avv_tipo VARCHAR2(35 CHAR),
	avv_app_io_prom_avv_oggetto CLOB,
	avv_app_io_prom_avv_messaggio CLOB,
	avv_app_io_prom_ric_abilitato NUMBER,
	avv_app_io_prom_ric_tipo VARCHAR2(35 CHAR),
	avv_app_io_prom_ric_oggetto CLOB,
	avv_app_io_prom_ric_messaggio CLOB,
	avv_app_io_prom_ric_eseguiti NUMBER,
	avv_app_io_prom_scad_abilitato NUMBER,
	avv_app_io_prom_scad_preavviso NUMBER,
	avv_app_io_prom_scad_tipo VARCHAR2(35 CHAR),
	avv_app_io_prom_scad_oggetto CLOB,
	avv_app_io_prom_scad_messaggio CLOB,
	-- fk/pk columns
	id NUMBER NOT NULL,
	id_tipo_versamento NUMBER NOT NULL,
	id_dominio NUMBER NOT NULL,
	-- unique constraints
	CONSTRAINT unique_tipi_vers_domini_1 UNIQUE (id_dominio,id_tipo_versamento),
	-- fk/pk keys constraints
	CONSTRAINT fk_tvd_id_tipo_versamento FOREIGN KEY (id_tipo_versamento) REFERENCES tipi_versamento(id),
	CONSTRAINT fk_tvd_id_dominio FOREIGN KEY (id_dominio) REFERENCES domini(id),
	CONSTRAINT pk_tipi_vers_domini PRIMARY KEY (id)
);

CREATE TRIGGER trg_tipi_vers_domini
BEFORE
insert on tipi_vers_domini
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_tipi_vers_domini.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;
/



CREATE SEQUENCE seq_utenze_tipo_vers MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE utenze_tipo_vers
(
	-- fk/pk columns
	id NUMBER NOT NULL,
	id_utenza NUMBER NOT NULL,
	id_tipo_versamento NUMBER NOT NULL,
	-- fk/pk keys constraints
	CONSTRAINT fk_utv_id_utenza FOREIGN KEY (id_utenza) REFERENCES utenze(id),
	CONSTRAINT fk_utv_id_tipo_versamento FOREIGN KEY (id_tipo_versamento) REFERENCES tipi_versamento(id),
	CONSTRAINT pk_utenze_tipo_vers PRIMARY KEY (id)
);

CREATE TRIGGER trg_utenze_tipo_vers
BEFORE
insert on utenze_tipo_vers
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_utenze_tipo_vers.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;
/



CREATE SEQUENCE seq_documenti MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE documenti
(
	cod_documento VARCHAR2(35 CHAR) NOT NULL,
	descrizione VARCHAR2(255 CHAR) NOT NULL,
	-- fk/pk columns
	id NUMBER NOT NULL,
	id_dominio NUMBER NOT NULL,
	id_applicazione NUMBER NOT NULL,
	-- unique constraints
	CONSTRAINT unique_documenti_1 UNIQUE (cod_documento,id_applicazione),
	-- fk/pk keys constraints
	CONSTRAINT fk_doc_id_dominio FOREIGN KEY (id_dominio) REFERENCES domini(id),
	CONSTRAINT fk_doc_id_applicazione FOREIGN KEY (id_applicazione) REFERENCES applicazioni(id),
	CONSTRAINT pk_documenti PRIMARY KEY (id)
);

CREATE TRIGGER trg_documenti
BEFORE
insert on documenti
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_documenti.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;
/



CREATE SEQUENCE seq_versamenti MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE versamenti
(
	cod_versamento_ente VARCHAR2(35 CHAR) NOT NULL,
	nome VARCHAR2(35 CHAR),
	importo_totale BINARY_DOUBLE NOT NULL,
	stato_versamento VARCHAR2(35 CHAR) NOT NULL,
	descrizione_stato VARCHAR2(255 CHAR),
	-- Indica se, decorsa la dataScadenza, deve essere aggiornato da remoto o essere considerato scaduto
	aggiornabile NUMBER NOT NULL,
	data_creazione TIMESTAMP NOT NULL,
	data_validita TIMESTAMP,
	data_scadenza TIMESTAMP,
	data_ora_ultimo_aggiornamento TIMESTAMP NOT NULL,
	causale_versamento VARCHAR2(1024 CHAR),
	debitore_tipo VARCHAR2(1 CHAR),
	debitore_identificativo VARCHAR2(35 CHAR) NOT NULL,
	debitore_anagrafica VARCHAR2(70 CHAR) NOT NULL,
	debitore_indirizzo VARCHAR2(70 CHAR),
	debitore_civico VARCHAR2(16 CHAR),
	debitore_cap VARCHAR2(16 CHAR),
	debitore_localita VARCHAR2(35 CHAR),
	debitore_provincia VARCHAR2(35 CHAR),
	debitore_nazione VARCHAR2(2 CHAR),
	debitore_email VARCHAR2(256 CHAR),
	debitore_telefono VARCHAR2(35 CHAR),
	debitore_cellulare VARCHAR2(35 CHAR),
	debitore_fax VARCHAR2(35 CHAR),
	tassonomia_avviso VARCHAR2(35 CHAR),
	tassonomia VARCHAR2(35 CHAR),
	cod_lotto VARCHAR2(35 CHAR),
	cod_versamento_lotto VARCHAR2(35 CHAR),
	cod_anno_tributario VARCHAR2(35 CHAR),
	cod_bundlekey VARCHAR2(256 CHAR),
	dati_allegati CLOB,
	incasso VARCHAR2(1 CHAR),
	anomalie CLOB,
	iuv_versamento VARCHAR2(35 CHAR),
	numero_avviso VARCHAR2(35 CHAR),
	avvisatura_abilitata NUMBER NOT NULL,
	avvisatura_da_inviare NUMBER NOT NULL,
	avvisatura_operazione VARCHAR2(1 CHAR),
	avvisatura_modalita VARCHAR2(1 CHAR),
	avvisatura_tipo_pagamento NUMBER,
	avvisatura_cod_avvisatura VARCHAR(20),
	ack NUMBER NOT NULL,
	anomalo NUMBER NOT NULL,
	divisione VARCHAR2(35 CHAR),
	direzione VARCHAR2(35 CHAR),
	id_sessione VARCHAR2(35 CHAR),
	data_pagamento TIMESTAMP,
	importo_pagato BINARY_DOUBLE NOT NULL,
	importo_incassato BINARY_DOUBLE NOT NULL,
	stato_pagamento VARCHAR2(35 CHAR) NOT NULL,
	iuv_pagamento VARCHAR2(35 CHAR),
	src_iuv VARCHAR2(35 CHAR),
	src_debitore_identificativo VARCHAR2(35 CHAR) NOT NULL,
	cod_rata NUMBER,
	-- fk/pk columns
	id NUMBER NOT NULL,
	id_tipo_versamento_dominio NUMBER NOT NULL,
	id_tipo_versamento NUMBER NOT NULL,
	id_dominio NUMBER NOT NULL,
	id_uo NUMBER,
	id_applicazione NUMBER NOT NULL,
	id_tracciato NUMBER,
	id_documento NUMBER,
	-- unique constraints
	CONSTRAINT unique_versamenti_1 UNIQUE (cod_versamento_ente,id_applicazione),
	-- fk/pk keys constraints
	CONSTRAINT fk_vrs_id_tipo_versamento_dominio FOREIGN KEY (id_tipo_versamento_dominio) REFERENCES tipi_vers_domini(id),
	CONSTRAINT fk_vrs_id_tipo_versamento FOREIGN KEY (id_tipo_versamento) REFERENCES tipi_versamento(id),
	CONSTRAINT fk_vrs_id_dominio FOREIGN KEY (id_dominio) REFERENCES domini(id),
	CONSTRAINT fk_vrs_id_uo FOREIGN KEY (id_uo) REFERENCES uo(id),
	CONSTRAINT fk_vrs_id_applicazione FOREIGN KEY (id_applicazione) REFERENCES applicazioni(id),
	CONSTRAINT fk_vrs_id_tracciato FOREIGN KEY (id_tracciato) REFERENCES tracciati(id),
	CONSTRAINT fk_vrs_id_documento FOREIGN KEY (id_documento) REFERENCES documenti(id),
	CONSTRAINT pk_versamenti PRIMARY KEY (id)
);

-- index
CREATE INDEX idx_vrs_id_pendenza ON versamenti (cod_versamento_ente,id_applicazione);
CREATE INDEX idx_vrs_data_creaz ON versamenti (data_creazione DESC);
CREATE INDEX idx_vrs_stato_vrs ON versamenti (stato_versamento);
CREATE INDEX idx_vrs_deb_identificativo ON versamenti (src_debitore_identificativo);
CREATE INDEX idx_vrs_iuv ON versamenti (src_iuv);
CREATE INDEX idx_vrs_auth ON versamenti (id_dominio,id_tipo_versamento,id_uo);
CREATE TRIGGER trg_versamenti
BEFORE
insert on versamenti
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_versamenti.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;
/



CREATE SEQUENCE seq_singoli_versamenti MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE singoli_versamenti
(
	cod_singolo_versamento_ente VARCHAR2(70 CHAR) NOT NULL,
	stato_singolo_versamento VARCHAR2(35 CHAR) NOT NULL,
	importo_singolo_versamento BINARY_DOUBLE NOT NULL,
	-- MARCA BOLLO Valori possibili:\n01: Imposta di bollo
	tipo_bollo VARCHAR2(2 CHAR),
	-- MARCA BOLLO: Digest in Base64 del documento da bollare
	hash_documento VARCHAR2(70 CHAR),
	-- MARCA BOLLO: Sigla automobilistica della provincia di residenza
	provincia_residenza VARCHAR2(2 CHAR),
	tipo_contabilita VARCHAR2(1 CHAR),
	codice_contabilita VARCHAR2(255 CHAR),
	descrizione VARCHAR2(256 CHAR),
	dati_allegati CLOB,
	indice_dati NUMBER NOT NULL,
	descrizione_causale_rpt VARCHAR2(140 CHAR),
	-- fk/pk columns
	id NUMBER NOT NULL,
	id_versamento NUMBER NOT NULL,
	id_tributo NUMBER,
	id_iban_accredito NUMBER,
	id_iban_appoggio NUMBER,
	-- fk/pk keys constraints
	CONSTRAINT fk_sng_id_versamento FOREIGN KEY (id_versamento) REFERENCES versamenti(id),
	CONSTRAINT fk_sng_id_tributo FOREIGN KEY (id_tributo) REFERENCES tributi(id),
	CONSTRAINT fk_sng_id_iban_accredito FOREIGN KEY (id_iban_accredito) REFERENCES iban_accredito(id),
	CONSTRAINT fk_sng_id_iban_appoggio FOREIGN KEY (id_iban_appoggio) REFERENCES iban_accredito(id),
	CONSTRAINT pk_singoli_versamenti PRIMARY KEY (id)
);

-- index
CREATE UNIQUE INDEX idx_sng_id_voce ON singoli_versamenti (id_versamento, indice_dati);
ALTER TABLE singoli_versamenti ADD CONSTRAINT unique_sng_id_voce UNIQUE USING INDEX idx_sng_id_voce;
CREATE TRIGGER trg_singoli_versamenti
BEFORE
insert on singoli_versamenti
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_singoli_versamenti.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;
/



CREATE SEQUENCE seq_pagamenti_portale MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE pagamenti_portale
(
	cod_canale VARCHAR2(35 CHAR),
	nome VARCHAR2(255 CHAR) NOT NULL,
	importo BINARY_DOUBLE NOT NULL,
	versante_identificativo VARCHAR2(35 CHAR),
	id_sessione VARCHAR2(35 CHAR) NOT NULL,
	id_sessione_portale VARCHAR2(255 CHAR),
	id_sessione_psp VARCHAR2(255 CHAR),
	stato VARCHAR2(35 CHAR) NOT NULL,
	codice_stato VARCHAR2(35 CHAR) NOT NULL,
	descrizione_stato VARCHAR2(1024 CHAR),
	psp_redirect_url VARCHAR2(1024 CHAR),
	psp_esito VARCHAR2(255 CHAR),
	json_request CLOB,
	data_richiesta TIMESTAMP,
	url_ritorno VARCHAR2(1024 CHAR),
	cod_psp VARCHAR2(35 CHAR),
	tipo_versamento VARCHAR2(4 CHAR),
	multi_beneficiario VARCHAR2(35 CHAR),
	ack NUMBER NOT NULL,
	tipo NUMBER NOT NULL,
	principal VARCHAR2(4000 CHAR) NOT NULL,
	tipo_utenza VARCHAR2(35 CHAR) NOT NULL,
	src_versante_identificativo VARCHAR2(35 CHAR),
	-- fk/pk columns
	id NUMBER NOT NULL,
	id_applicazione NUMBER,
	-- fk/pk keys constraints
	CONSTRAINT fk_ppt_id_applicazione FOREIGN KEY (id_applicazione) REFERENCES applicazioni(id),
	CONSTRAINT pk_pagamenti_portale PRIMARY KEY (id)
);

-- index
CREATE INDEX idx_prt_stato ON pagamenti_portale (stato);
CREATE INDEX idx_prt_id_sessione ON pagamenti_portale (id_sessione);
CREATE INDEX idx_prt_id_sessione_psp ON pagamenti_portale (id_sessione_psp);
CREATE INDEX idx_prt_versante_identif ON pagamenti_portale (src_versante_identificativo);
CREATE TRIGGER trg_pagamenti_portale
BEFORE
insert on pagamenti_portale
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_pagamenti_portale.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;
/



CREATE SEQUENCE seq_pag_port_versamenti MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE pag_port_versamenti
(
	-- fk/pk columns
	id NUMBER NOT NULL,
	id_pagamento_portale NUMBER NOT NULL,
	id_versamento NUMBER NOT NULL,
	-- fk/pk keys constraints
	CONSTRAINT fk_ppv_id_pagamento_portale FOREIGN KEY (id_pagamento_portale) REFERENCES pagamenti_portale(id),
	CONSTRAINT fk_ppv_id_versamento FOREIGN KEY (id_versamento) REFERENCES versamenti(id),
	CONSTRAINT pk_pag_port_versamenti PRIMARY KEY (id)
);

-- index
CREATE INDEX idx_ppv_fk_prt ON pag_port_versamenti (id_pagamento_portale);
CREATE INDEX idx_ppv_fk_vrs ON pag_port_versamenti (id_versamento);
CREATE TRIGGER trg_pag_port_versamenti
BEFORE
insert on pag_port_versamenti
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_pag_port_versamenti.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;
/



CREATE SEQUENCE seq_rpt MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE rpt
(
	cod_carrello VARCHAR2(35 CHAR),
	iuv VARCHAR2(35 CHAR) NOT NULL,
	ccp VARCHAR2(35 CHAR) NOT NULL,
	cod_dominio VARCHAR2(35 CHAR) NOT NULL,
	-- Identificativo dell'RPT utilizzato come riferimento nell'RT
	cod_msg_richiesta VARCHAR2(35 CHAR) NOT NULL,
	-- Data di creazione dell'RPT
	data_msg_richiesta TIMESTAMP NOT NULL,
	-- Stato RPT secondo la codifica AgID
	stato VARCHAR2(35 CHAR) NOT NULL,
	descrizione_stato CLOB,
	cod_sessione VARCHAR2(255 CHAR),
	cod_sessione_portale VARCHAR2(255 CHAR),
	-- Indirizzo del portale psp a cui redirigere il cittadino per eseguire il pagamento
	psp_redirect_url VARCHAR2(512 CHAR),
	xml_rpt BLOB NOT NULL,
	data_aggiornamento_stato TIMESTAMP NOT NULL,
	-- Indirizzo di ritorno al portale dell'ente al termine del pagamento
	callback_url CLOB,
	modello_pagamento VARCHAR2(16 CHAR),
	cod_msg_ricevuta VARCHAR2(35 CHAR),
	data_msg_ricevuta TIMESTAMP,
	-- Esito del pagamento:\n0: Eseguito\n1: Non eseguito\n2: Parzialmente eseguito\n3: Decorrenza\n4: Decorrenza Parziale
	cod_esito_pagamento NUMBER,
	importo_totale_pagato BINARY_DOUBLE,
	xml_rt BLOB,
	cod_canale VARCHAR2(35 CHAR),
	cod_psp VARCHAR2(35 CHAR),
	cod_intermediario_psp VARCHAR2(35 CHAR),
	tipo_versamento VARCHAR2(4 CHAR),
	tipo_identificativo_attestante VARCHAR2(1 CHAR),
	identificativo_attestante VARCHAR2(35 CHAR),
	denominazione_attestante VARCHAR2(70 CHAR),
	cod_stazione VARCHAR2(35 CHAR) NOT NULL,
	cod_transazione_rpt VARCHAR2(36 CHAR),
	cod_transazione_rt VARCHAR2(36 CHAR),
	stato_conservazione VARCHAR2(35 CHAR),
	descrizione_stato_cons VARCHAR2(512 CHAR),
	data_conservazione TIMESTAMP,
	bloccante NUMBER NOT NULL,
	-- fk/pk columns
	id NUMBER NOT NULL,
	id_versamento NUMBER NOT NULL,
	id_pagamento_portale NUMBER,
	-- fk/pk keys constraints
	CONSTRAINT fk_rpt_id_versamento FOREIGN KEY (id_versamento) REFERENCES versamenti(id),
	CONSTRAINT fk_rpt_id_pagamento_portale FOREIGN KEY (id_pagamento_portale) REFERENCES pagamenti_portale(id),
	CONSTRAINT pk_rpt PRIMARY KEY (id)
);

-- index
CREATE INDEX idx_rpt_cod_msg_richiesta ON rpt (cod_msg_richiesta);
CREATE INDEX idx_rpt_stato ON rpt (stato);
CREATE INDEX idx_rpt_fk_vrs ON rpt (id_versamento);
CREATE INDEX idx_rpt_fk_prt ON rpt (id_pagamento_portale);
CREATE UNIQUE INDEX idx_rpt_id_transazione ON rpt (iuv, ccp, cod_dominio);
ALTER TABLE rpt ADD CONSTRAINT unique_rpt_id_transazione UNIQUE USING INDEX idx_rpt_id_transazione;

ALTER TABLE rpt MODIFY bloccante DEFAULT 1;

CREATE TRIGGER trg_rpt
BEFORE
insert on rpt
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_rpt.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;
/



CREATE SEQUENCE seq_rr MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE rr
(
	cod_dominio VARCHAR2(35 CHAR) NOT NULL,
	iuv VARCHAR2(35 CHAR) NOT NULL,
	ccp VARCHAR2(35 CHAR) NOT NULL,
	cod_msg_revoca VARCHAR2(35 CHAR) NOT NULL,
	data_msg_revoca TIMESTAMP NOT NULL,
	data_msg_esito TIMESTAMP,
	stato VARCHAR2(35 CHAR) NOT NULL,
	descrizione_stato VARCHAR2(512 CHAR),
	importo_totale_richiesto BINARY_DOUBLE NOT NULL,
	cod_msg_esito VARCHAR2(35 CHAR),
	importo_totale_revocato BINARY_DOUBLE,
	xml_rr BLOB NOT NULL,
	xml_er BLOB,
	cod_transazione_rr VARCHAR2(36 CHAR),
	cod_transazione_er VARCHAR2(36 CHAR),
	-- fk/pk columns
	id NUMBER NOT NULL,
	id_rpt NUMBER NOT NULL,
	-- unique constraints
	CONSTRAINT unique_rr_1 UNIQUE (cod_msg_revoca),
	-- fk/pk keys constraints
	CONSTRAINT fk_rr_id_rpt FOREIGN KEY (id_rpt) REFERENCES rpt(id),
	CONSTRAINT pk_rr PRIMARY KEY (id)
);

CREATE TRIGGER trg_rr
BEFORE
insert on rr
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_rr.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;
/



CREATE SEQUENCE seq_notifiche MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE notifiche
(
	tipo_esito VARCHAR2(16 CHAR) NOT NULL,
	data_creazione TIMESTAMP NOT NULL,
	stato VARCHAR2(16 CHAR) NOT NULL,
	descrizione_stato VARCHAR2(255 CHAR),
	data_aggiornamento_stato TIMESTAMP NOT NULL,
	data_prossima_spedizione TIMESTAMP NOT NULL,
	tentativi_spedizione NUMBER,
	-- fk/pk columns
	id NUMBER NOT NULL,
	id_applicazione NUMBER NOT NULL,
	id_rpt NUMBER,
	id_rr NUMBER,
	-- fk/pk keys constraints
	CONSTRAINT fk_ntf_id_applicazione FOREIGN KEY (id_applicazione) REFERENCES applicazioni(id),
	CONSTRAINT fk_ntf_id_rpt FOREIGN KEY (id_rpt) REFERENCES rpt(id),
	CONSTRAINT fk_ntf_id_rr FOREIGN KEY (id_rr) REFERENCES rr(id),
	CONSTRAINT pk_notifiche PRIMARY KEY (id)
);

-- index
CREATE INDEX idx_ntf_da_spedire ON notifiche (id_applicazione,stato,data_prossima_spedizione);
CREATE TRIGGER trg_notifiche
BEFORE
insert on notifiche
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_notifiche.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;
/



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



CREATE SEQUENCE seq_promemoria MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE promemoria
(
	tipo VARCHAR2(16 CHAR) NOT NULL,
	data_creazione TIMESTAMP NOT NULL,
	stato VARCHAR2(16 CHAR) NOT NULL,
	descrizione_stato VARCHAR2(1024 CHAR),
	destinatario_to VARCHAR2(256 CHAR) NOT NULL,
	destinatario_cc VARCHAR2(256 CHAR),
	messaggio_content_type VARCHAR2(256 CHAR),
	oggetto VARCHAR2(512 CHAR),
	messaggio CLOB,
	allega_pdf NUMBER NOT NULL,
	data_aggiornamento_stato TIMESTAMP NOT NULL,
	data_prossima_spedizione TIMESTAMP NOT NULL,
	tentativi_spedizione NUMBER,
	-- fk/pk columns
	id NUMBER NOT NULL,
	id_versamento NUMBER,
	id_rpt NUMBER,
	id_documento NUMBER,
	-- fk/pk keys constraints
	CONSTRAINT fk_prm_id_versamento FOREIGN KEY (id_versamento) REFERENCES versamenti(id),
	CONSTRAINT fk_prm_id_rpt FOREIGN KEY (id_rpt) REFERENCES rpt(id),
	CONSTRAINT fk_prm_id_documento FOREIGN KEY (id_documento) REFERENCES documenti(id),
	CONSTRAINT pk_promemoria PRIMARY KEY (id)
);


ALTER TABLE promemoria MODIFY allega_pdf DEFAULT 0;

CREATE TRIGGER trg_promemoria
BEFORE
insert on promemoria
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_promemoria.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;
/



CREATE SEQUENCE seq_iuv MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE iuv
(
	prg NUMBER NOT NULL,
	iuv VARCHAR2(35 CHAR) NOT NULL,
	application_code NUMBER NOT NULL,
	data_generazione DATE NOT NULL,
	tipo_iuv VARCHAR2(1 CHAR) NOT NULL,
	cod_versamento_ente VARCHAR2(35 CHAR),
	aux_digit NUMBER NOT NULL,
	-- fk/pk columns
	id NUMBER NOT NULL,
	id_applicazione NUMBER NOT NULL,
	id_dominio NUMBER NOT NULL,
	-- unique constraints
	CONSTRAINT unique_iuv_1 UNIQUE (id_dominio,iuv),
	-- fk/pk keys constraints
	CONSTRAINT fk_iuv_id_applicazione FOREIGN KEY (id_applicazione) REFERENCES applicazioni(id),
	CONSTRAINT fk_iuv_id_dominio FOREIGN KEY (id_dominio) REFERENCES domini(id),
	CONSTRAINT pk_iuv PRIMARY KEY (id)
);

-- index
CREATE INDEX idx_iuv_rifversamento ON iuv (cod_versamento_ente,id_applicazione,tipo_iuv);

ALTER TABLE iuv MODIFY aux_digit DEFAULT 0;

CREATE TRIGGER trg_iuv
BEFORE
insert on iuv
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_iuv.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;
/



CREATE SEQUENCE seq_incassi MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE incassi
(
	trn VARCHAR2(35 CHAR) NOT NULL,
	cod_dominio VARCHAR2(35 CHAR) NOT NULL,
	causale VARCHAR2(512 CHAR) NOT NULL,
	importo BINARY_DOUBLE NOT NULL,
	data_valuta DATE,
	data_contabile DATE,
	data_ora_incasso TIMESTAMP NOT NULL,
	nome_dispositivo VARCHAR2(512 CHAR),
	iban_accredito VARCHAR2(35 CHAR),
        sct VARCHAR2(35 CHAR),
	-- fk/pk columns
	id NUMBER NOT NULL,
	id_applicazione NUMBER,
	id_operatore NUMBER,
	-- unique constraints
	CONSTRAINT unique_incassi_1 UNIQUE (cod_dominio,trn),
	-- fk/pk keys constraints
	CONSTRAINT fk_inc_id_applicazione FOREIGN KEY (id_applicazione) REFERENCES applicazioni(id),
	CONSTRAINT fk_inc_id_operatore FOREIGN KEY (id_operatore) REFERENCES operatori(id),
	CONSTRAINT pk_incassi PRIMARY KEY (id)
);

CREATE TRIGGER trg_incassi
BEFORE
insert on incassi
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_incassi.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;
/



CREATE SEQUENCE seq_fr MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE fr
(
	cod_psp VARCHAR2(35 CHAR) NOT NULL,
	cod_dominio VARCHAR2(35 CHAR) NOT NULL,
	cod_flusso VARCHAR2(35 CHAR) NOT NULL,
	stato VARCHAR2(35 CHAR) NOT NULL,
	descrizione_stato CLOB,
	iur VARCHAR2(35 CHAR) NOT NULL,
	data_ora_flusso TIMESTAMP,
	data_regolamento TIMESTAMP,
	data_acquisizione TIMESTAMP NOT NULL,
	numero_pagamenti NUMBER,
	importo_totale_pagamenti BINARY_DOUBLE,
	cod_bic_riversamento VARCHAR2(35 CHAR),
	xml BLOB NOT NULL,
	ragione_sociale_psp VARCHAR2(70 CHAR),
	ragione_sociale_dominio VARCHAR2(70 CHAR),
	-- fk/pk columns
	id NUMBER NOT NULL,
	id_incasso NUMBER,
	-- unique constraints
	CONSTRAINT unique_fr_1 UNIQUE (cod_flusso),
	-- fk/pk keys constraints
	CONSTRAINT fk_fr_id_incasso FOREIGN KEY (id_incasso) REFERENCES incassi(id),
	CONSTRAINT pk_fr PRIMARY KEY (id)
);

CREATE TRIGGER trg_fr
BEFORE
insert on fr
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_fr.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;
/



CREATE SEQUENCE seq_pagamenti MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE pagamenti
(
	cod_dominio VARCHAR2(35 CHAR) NOT NULL,
	iuv VARCHAR2(35 CHAR) NOT NULL,
	indice_dati NUMBER NOT NULL,
	importo_pagato BINARY_DOUBLE NOT NULL,
	data_acquisizione TIMESTAMP NOT NULL,
	iur VARCHAR2(35 CHAR) NOT NULL,
	data_pagamento TIMESTAMP NOT NULL,
	commissioni_psp BINARY_DOUBLE,
	-- Valori possibili:\nES: Esito originario\nBD: Marca da Bollo
	tipo_allegato VARCHAR2(2 CHAR),
	allegato BLOB,
	data_acquisizione_revoca TIMESTAMP,
	causale_revoca VARCHAR2(140 CHAR),
	dati_revoca VARCHAR2(140 CHAR),
	importo_revocato BINARY_DOUBLE,
	esito_revoca VARCHAR2(140 CHAR),
	dati_esito_revoca VARCHAR2(140 CHAR),
	stato VARCHAR2(35 CHAR),
	tipo VARCHAR2(35 CHAR) NOT NULL,
	-- fk/pk columns
	id NUMBER NOT NULL,
	id_rpt NUMBER,
	id_singolo_versamento NUMBER NOT NULL,
	id_rr NUMBER,
	id_incasso NUMBER,
	-- fk/pk keys constraints
	CONSTRAINT fk_pag_id_rpt FOREIGN KEY (id_rpt) REFERENCES rpt(id),
	CONSTRAINT fk_pag_id_singolo_versamento FOREIGN KEY (id_singolo_versamento) REFERENCES singoli_versamenti(id),
	CONSTRAINT fk_pag_id_rr FOREIGN KEY (id_rr) REFERENCES rr(id),
	CONSTRAINT fk_pag_id_incasso FOREIGN KEY (id_incasso) REFERENCES incassi(id),
	CONSTRAINT pk_pagamenti PRIMARY KEY (id)
);

-- index
CREATE INDEX idx_pag_fk_rpt ON pagamenti (id_rpt);
CREATE INDEX idx_pag_fk_sng ON pagamenti (id_singolo_versamento);
ALTER TABLE pagamenti MODIFY indice_dati DEFAULT 1;
CREATE UNIQUE INDEX idx_pag_id_riscossione ON pagamenti (cod_dominio, iuv, iur, indice_dati);
ALTER TABLE pagamenti ADD CONSTRAINT unique_pag_id_riscossione UNIQUE USING INDEX idx_pag_id_riscossione;

CREATE TRIGGER trg_pagamenti
BEFORE
insert on pagamenti
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_pagamenti.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;
/



CREATE SEQUENCE seq_rendicontazioni MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE rendicontazioni
(
	iuv VARCHAR2(35 CHAR) NOT NULL,
	iur VARCHAR2(35 CHAR) NOT NULL,
	indice_dati NUMBER,
	importo_pagato BINARY_DOUBLE,
	esito NUMBER,
	data TIMESTAMP,
	stato VARCHAR2(35 CHAR) NOT NULL,
	anomalie CLOB,
	-- fk/pk columns
	id NUMBER NOT NULL,
	id_fr NUMBER NOT NULL,
	id_pagamento NUMBER,
	id_singolo_versamento NUMBER,
	-- fk/pk keys constraints
	CONSTRAINT fk_rnd_id_fr FOREIGN KEY (id_fr) REFERENCES fr(id),
	CONSTRAINT fk_rnd_id_pagamento FOREIGN KEY (id_pagamento) REFERENCES pagamenti(id),
	CONSTRAINT fk_rnd_id_singolo_versamento FOREIGN KEY (id_singolo_versamento) REFERENCES singoli_versamenti(id),
	CONSTRAINT pk_rendicontazioni PRIMARY KEY (id)
);

CREATE TRIGGER trg_rendicontazioni
BEFORE
insert on rendicontazioni
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_rendicontazioni.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;
/



CREATE SEQUENCE seq_eventi MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE eventi
(
	componente VARCHAR2(35 CHAR),
	ruolo VARCHAR2(1 CHAR),
	categoria_evento VARCHAR2(1 CHAR),
	tipo_evento VARCHAR2(70 CHAR),
	sottotipo_evento VARCHAR2(35 CHAR),
	data TIMESTAMP,
	intervallo NUMBER,
	esito VARCHAR2(4 CHAR),
	sottotipo_esito VARCHAR2(35 CHAR),
	dettaglio_esito CLOB,
	parametri_richiesta BLOB,
	parametri_risposta BLOB,
	dati_pago_pa CLOB,
	cod_versamento_ente VARCHAR2(35 CHAR),
	cod_applicazione VARCHAR2(35 CHAR),
	iuv VARCHAR2(35 CHAR),
	ccp VARCHAR2(35 CHAR),
	cod_dominio VARCHAR2(35 CHAR),
	id_sessione VARCHAR2(35 CHAR),
	-- fk/pk columns
	id NUMBER NOT NULL,
	id_fr NUMBER,
	id_incasso NUMBER,
	id_tracciato NUMBER,
	-- fk/pk keys constraints
	CONSTRAINT fk_evt_id_fr FOREIGN KEY (id_fr) REFERENCES fr(id),
	CONSTRAINT fk_evt_id_incasso FOREIGN KEY (id_incasso) REFERENCES incassi(id),
	CONSTRAINT fk_evt_id_tracciato FOREIGN KEY (id_tracciato) REFERENCES tracciati(id),
	CONSTRAINT pk_eventi PRIMARY KEY (id)
);

-- index
CREATE INDEX idx_evt_data ON eventi (data);
CREATE INDEX idx_evt_fk_vrs ON eventi (cod_applicazione,cod_versamento_ente);
CREATE INDEX idx_evt_id_sessione ON eventi (id_sessione);
CREATE TRIGGER trg_eventi
BEFORE
insert on eventi
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_eventi.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;
/



CREATE SEQUENCE seq_batch MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE batch
(
	cod_batch VARCHAR2(255 CHAR) NOT NULL,
	nodo VARCHAR2(255 CHAR),
	inizio TIMESTAMP,
	aggiornamento TIMESTAMP,
	-- fk/pk columns
	id NUMBER NOT NULL,
	-- unique constraints
	CONSTRAINT unique_batch_1 UNIQUE (cod_batch),
	-- fk/pk keys constraints
	CONSTRAINT pk_batch PRIMARY KEY (id)
);

CREATE TRIGGER trg_batch
BEFORE
insert on batch
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_batch.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;
/



CREATE SEQUENCE seq_esiti_avvisatura MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE esiti_avvisatura
(
	cod_dominio VARCHAR2(35 CHAR) NOT NULL,
	identificativo_avvisatura VARCHAR2(20 CHAR) NOT NULL,
	tipo_canale NUMBER NOT NULL,
	cod_canale VARCHAR2(35 CHAR),
	data TIMESTAMP NOT NULL,
	cod_esito NUMBER NOT NULL,
	descrizione_esito VARCHAR2(140 CHAR) NOT NULL,
	-- fk/pk columns
	id NUMBER NOT NULL,
	id_tracciato NUMBER NOT NULL,
	-- fk/pk keys constraints
	CONSTRAINT fk_sta_id_tracciato FOREIGN KEY (id_tracciato) REFERENCES tracciati(id),
	CONSTRAINT pk_esiti_avvisatura PRIMARY KEY (id)
);

CREATE TRIGGER trg_esiti_avvisatura
BEFORE
insert on esiti_avvisatura
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_esiti_avvisatura.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;
/



CREATE SEQUENCE seq_operazioni MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE operazioni
(
	tipo_operazione VARCHAR2(16 CHAR) NOT NULL,
	linea_elaborazione NUMBER NOT NULL,
	stato VARCHAR2(16 CHAR) NOT NULL,
	dati_richiesta BLOB,
	dati_risposta BLOB,
	dettaglio_esito VARCHAR2(255 CHAR),
	cod_versamento_ente VARCHAR2(255 CHAR),
	cod_dominio VARCHAR2(35 CHAR),
	iuv VARCHAR2(35 CHAR),
	trn VARCHAR2(35 CHAR),
	-- fk/pk columns
	id NUMBER NOT NULL,
	id_tracciato NUMBER NOT NULL,
	id_applicazione NUMBER,
	-- fk/pk keys constraints
	CONSTRAINT fk_ope_id_tracciato FOREIGN KEY (id_tracciato) REFERENCES tracciati(id),
	CONSTRAINT fk_ope_id_applicazione FOREIGN KEY (id_applicazione) REFERENCES applicazioni(id),
	CONSTRAINT pk_operazioni PRIMARY KEY (id)
);

CREATE TRIGGER trg_operazioni
BEFORE
insert on operazioni
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_operazioni.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;
/



CREATE SEQUENCE seq_gp_audit MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE gp_audit
(
	data TIMESTAMP NOT NULL,
	id_oggetto NUMBER NOT NULL,
	tipo_oggetto VARCHAR2(255 CHAR) NOT NULL,
	oggetto CLOB NOT NULL,
	-- fk/pk columns
	id NUMBER NOT NULL,
	id_operatore NUMBER NOT NULL,
	-- fk/pk keys constraints
	CONSTRAINT fk_aud_id_operatore FOREIGN KEY (id_operatore) REFERENCES operatori(id),
	CONSTRAINT pk_gp_audit PRIMARY KEY (id)
);

CREATE TRIGGER trg_gp_audit
BEFORE
insert on gp_audit
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_gp_audit.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;
/



CREATE SEQUENCE seq_stampe MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE stampe
(
	data_creazione TIMESTAMP NOT NULL,
	tipo VARCHAR2(16 CHAR) NOT NULL,
	pdf BLOB,
	-- fk/pk columns
	id NUMBER NOT NULL,
	id_versamento NUMBER,
	id_documento NUMBER,
	-- unique constraints
	CONSTRAINT unique_stampe_1 UNIQUE (id_versamento,id_documento,tipo),
	-- fk/pk keys constraints
	CONSTRAINT fk_stm_id_versamento FOREIGN KEY (id_versamento) REFERENCES versamenti(id),
	CONSTRAINT fk_stm_id_documento FOREIGN KEY (id_documento) REFERENCES documenti(id),
	CONSTRAINT pk_stampe PRIMARY KEY (id)
);

CREATE TRIGGER trg_stampe
BEFORE
insert on stampe
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_stampe.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;
/



CREATE TABLE ID_MESSAGGIO_RELATIVO
(
	COUNTER NUMBER NOT NULL,
	PROTOCOLLO VARCHAR2(255 CHAR) NOT NULL,
	INFO_ASSOCIATA VARCHAR2(255 CHAR) NOT NULL,
	ora_registrazione TIMESTAMP,
	-- fk/pk columns
	-- fk/pk keys constraints
	CONSTRAINT pk_ID_MESSAGGIO_RELATIVO PRIMARY KEY (PROTOCOLLO,INFO_ASSOCIATA)
);


ALTER TABLE ID_MESSAGGIO_RELATIVO MODIFY ora_registrazione DEFAULT CURRENT_TIMESTAMP;

CREATE TABLE sonde
(
	nome VARCHAR(35) NOT NULL,
	classe VARCHAR(255) NOT NULL,
	soglia_warn NUMBER NOT NULL,
	soglia_error NUMBER NOT NULL,
	data_ok TIMESTAMP,
	data_warn TIMESTAMP,
	data_error TIMESTAMP,
	data_ultimo_check TIMESTAMP,
	dati_check CLOB,
	stato_ultimo_check NUMBER,
	-- fk/pk columns
	-- fk/pk keys constraints
	CONSTRAINT pk_sonde PRIMARY KEY (nome)
);

-- Correzione SQL per performance DB
ALTER TABLE versamenti DROP CONSTRAINT fk_vrs_id_applicazione;
ALTER TABLE versamenti DROP CONSTRAINT fk_vrs_id_dominio;
ALTER TABLE versamenti DROP CONSTRAINT fk_vrs_id_tipo_versamento_dominio;
ALTER TABLE versamenti DROP CONSTRAINT fk_vrs_id_tipo_versamento;
ALTER TABLE versamenti DROP CONSTRAINT fk_vrs_id_tracciato;
ALTER TABLE versamenti DROP CONSTRAINT fk_vrs_id_uo;
ALTER TABLE versamenti DROP CONSTRAINT fk_vrs_id_documento;

ALTER TABLE singoli_versamenti DROP CONSTRAINT fk_sng_id_iban_accredito;
ALTER TABLE singoli_versamenti DROP CONSTRAINT fk_sng_id_iban_appoggio;
ALTER TABLE singoli_versamenti DROP CONSTRAINT fk_sng_id_tributo;
ALTER TABLE singoli_versamenti DROP CONSTRAINT fk_sng_id_versamento;

ALTER TABLE rpt DROP CONSTRAINT fk_rpt_id_pagamento_portale;
ALTER TABLE rpt DROP CONSTRAINT fk_rpt_id_versamento;

ALTER TABLE pagamenti DROP CONSTRAINT fk_pag_id_incasso;
ALTER TABLE pagamenti DROP CONSTRAINT fk_pag_id_rpt;
ALTER TABLE pagamenti DROP CONSTRAINT fk_pag_id_rr;
ALTER TABLE pagamenti DROP CONSTRAINT fk_pag_id_singolo_versamento;

ALTER TABLE pagamenti_portale DROP CONSTRAINT fk_ppt_id_applicazione;

ALTER TABLE pag_port_versamenti DROP CONSTRAINT fk_ppv_id_pagamento_portale;
ALTER TABLE pag_port_versamenti DROP CONSTRAINT fk_ppv_id_versamento;

-- Sezione Viste

CREATE VIEW versamenti_incassi AS
    SELECT
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
    (CASE WHEN versamenti.stato_versamento = 'NON_ESEGUITO' AND versamenti.data_validita > CURRENT_DATE THEN 0 ELSE 1 END) AS smart_order_rank,
    (ABS((date_to_unix_for_smart_order(CURRENT_DATE) * 1000) - (date_to_unix_for_smart_order(COALESCE(pagamenti.data_pagamento, versamenti.data_validita, versamenti.data_creazione))) *1000)) AS smart_order_date
    FROM versamenti JOIN tipi_versamento ON tipi_versamento.id = versamenti.id_tipo_versamento
    LEFT JOIN documenti ON versamenti.id_documento = documenti.id;

-- VISTE REPORTISTICA

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
    singoli_versamenti.id_tributo AS id_tributo
   FROM fr
     JOIN rendicontazioni ON rendicontazioni.id_fr = fr.id
     JOIN versamenti ON versamenti.iuv_versamento = rendicontazioni.iuv
     JOIN domini ON versamenti.id_dominio = domini.id
     JOIN singoli_versamenti ON singoli_versamenti.id_versamento = versamenti.id
  WHERE rendicontazioni.esito = 9;

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
    singoli_versamenti.id_tributo AS id_tributo
   FROM pagamenti
     LEFT JOIN rendicontazioni ON rendicontazioni.id_pagamento = pagamenti.id
     LEFT JOIN fr ON rendicontazioni.id_fr = fr.id
     JOIN singoli_versamenti ON pagamenti.id_singolo_versamento = singoli_versamenti.id
     JOIN versamenti ON singoli_versamenti.id_versamento = versamenti.id; 

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
    tipi_tributo.cod_tributo AS cod_entrata
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
            v_riscossioni_senza_rpt.id_tributo
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
            v_riscossioni_con_rpt.id_tributo
           FROM v_riscossioni_con_rpt) a
     JOIN applicazioni ON a.id_applicazione = applicazioni.id 
     LEFT JOIN tipi_versamento ON a.id_tipo_versamento = tipi_versamento.id 
     LEFT JOIN tributi ON a.id_tributo = tributi.id 
     LEFT JOIN tipi_tributo ON tributi.id_tipo_tributo = tipi_tributo.id;





-- Vista pagamenti_portale

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

-- Vista Eventi per Versamenti

CREATE VIEW v_eventi_vers_rendicontazioni AS (
        SELECT DISTINCT 
               versamenti.cod_versamento_ente as cod_versamento_ente,
               applicazioni.cod_applicazione as cod_applicazione,
               eventi.id
        FROM eventi 
        JOIN rendicontazioni ON rendicontazioni.id_fr = eventi.id_fr
        JOIN pagamenti ON pagamenti.id = rendicontazioni.id_pagamento
        JOIN singoli_versamenti ON pagamenti.id_singolo_versamento=singoli_versamenti.id
        JOIN versamenti ON singoli_versamenti.id_versamento=versamenti.id
        JOIN applicazioni ON versamenti.id_applicazione = applicazioni.id
);

CREATE VIEW v_eventi_vers_pagamenti AS (
	SELECT DISTINCT 
               versamenti.cod_versamento_ente as cod_versamento_ente,
               applicazioni.cod_applicazione as cod_applicazione,
               eventi.id
        FROM versamenti
        JOIN applicazioni ON versamenti.id_applicazione = applicazioni.id
        JOIN pag_port_versamenti ON versamenti.id = pag_port_versamenti.id_versamento
        JOIN pagamenti_portale ON pag_port_versamenti.id_pagamento_portale = pagamenti_portale.id
        JOIN eventi ON eventi.id_sessione::text = pagamenti_portale.id_sessione::text
);

CREATE VIEW v_eventi_vers_riconciliazioni AS (
        SELECT DISTINCT 
               versamenti.cod_versamento_ente as cod_versamento_ente,
               applicazioni.cod_applicazione as cod_applicazione,
               eventi.id
        FROM eventi
        JOIN pagamenti ON pagamenti.id_incasso = eventi.id_incasso
        JOIN singoli_versamenti ON pagamenti.id_singolo_versamento=singoli_versamenti.id
        JOIN versamenti ON singoli_versamenti.id_versamento=versamenti.id
        JOIN applicazioni ON versamenti.id_applicazione = applicazioni.id
);

CREATE VIEW v_eventi_vers_tracciati AS (
        SELECT DISTINCT 
               versamenti.cod_versamento_ente as cod_versamento_ente,
               applicazioni.cod_applicazione as cod_applicazione,
               eventi.id
        FROM eventi
        JOIN operazioni ON operazioni.id_tracciato = eventi.id_tracciato
        JOIN versamenti ON operazioni.id_applicazione = versamenti.id_applicazione AND operazioni.cod_versamento_ente = versamenti.cod_versamento_ente
        JOIN applicazioni ON versamenti.id_applicazione = applicazioni.id
);


CREATE VIEW v_eventi_vers_base AS (
        SELECT DISTINCT 
               cod_versamento_ente,
               cod_applicazione,
               id
        FROM eventi
        UNION SELECT * FROM v_eventi_vers_pagamenti
        UNION SELECT * FROM v_eventi_vers_rendicontazioni
        UNION SELECT * FROM v_eventi_vers_riconciliazioni
        UNION SELECT * FROM v_eventi_vers_tracciati
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
               v_eventi_vers_base.cod_versamento_ente,
               v_eventi_vers_base.cod_applicazione,
               eventi.iuv,
               eventi.cod_dominio,
               eventi.ccp,
               eventi.id_sessione,
               eventi.id
               FROM v_eventi_vers_base JOIN eventi ON v_eventi_vers_base.id = eventi.id
         );  

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

-- Vista Rpt Versamento
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
   

