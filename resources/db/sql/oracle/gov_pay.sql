CREATE SEQUENCE seq_psp MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE psp
(
	cod_psp VARCHAR2(35 CHAR) NOT NULL,
	ragione_sociale VARCHAR2(70 CHAR) NOT NULL,
	url_info VARCHAR2(255 CHAR),
	abilitato NUMBER NOT NULL,
	storno NUMBER NOT NULL,
	marca_bollo NUMBER NOT NULL,
	-- fk/pk columns
	id NUMBER NOT NULL,
	-- unique constraints
	CONSTRAINT unique_psp_1 UNIQUE (cod_psp),
	-- fk/pk keys constraints
	CONSTRAINT pk_psp PRIMARY KEY (id)
);

CREATE TRIGGER trg_psp
BEFORE
insert on psp
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_psp.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;
/



CREATE SEQUENCE seq_canali MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE canali
(
	cod_canale VARCHAR2(35 CHAR) NOT NULL,
	cod_intermediario VARCHAR2(35 CHAR) NOT NULL,
	tipo_versamento VARCHAR2(4 CHAR) NOT NULL,
	modello_pagamento NUMBER NOT NULL,
	disponibilita CLOB,
	descrizione CLOB,
	condizioni VARCHAR2(35 CHAR),
	url_info VARCHAR2(255 CHAR),
	abilitato NUMBER NOT NULL,
	-- fk/pk columns
	id NUMBER NOT NULL,
	id_psp NUMBER NOT NULL,
	-- unique constraints
	CONSTRAINT unique_canali_1 UNIQUE (id_psp,cod_canale,tipo_versamento),
	-- fk/pk keys constraints
	CONSTRAINT fk_can_id_psp FOREIGN KEY (id_psp) REFERENCES psp(id),
	CONSTRAINT pk_canali PRIMARY KEY (id)
);

CREATE TRIGGER trg_canali
BEFORE
insert on canali
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_canali.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;
/



CREATE SEQUENCE seq_intermediari MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE intermediari
(
	cod_intermediario VARCHAR2(35 CHAR) NOT NULL,
	cod_connettore_pdd VARCHAR2(35 CHAR) NOT NULL,
	denominazione VARCHAR2(255 CHAR) NOT NULL,
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
	ndp_operazione VARCHAR(256),
	ndp_descrizione VARCHAR(1024),
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



CREATE SEQUENCE seq_applicazioni MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE applicazioni
(
	cod_applicazione VARCHAR2(35 CHAR) NOT NULL,
	abilitato NUMBER NOT NULL,
	principal VARCHAR2(255 CHAR) NOT NULL,
	firma_ricevuta VARCHAR2(1 CHAR) NOT NULL,
	cod_connettore_esito VARCHAR2(255 CHAR),
	cod_connettore_verifica VARCHAR2(255 CHAR),
	versione VARCHAR2(10 CHAR) NOT NULL,
	trusted NUMBER NOT NULL,
	cod_applicazione_iuv VARCHAR2(3 CHAR),
	-- fk/pk columns
	id NUMBER NOT NULL,
	-- unique constraints
	CONSTRAINT unique_applicazioni_1 UNIQUE (cod_applicazione),
	CONSTRAINT unique_applicazioni_2 UNIQUE (principal),
	-- fk/pk keys constraints
	CONSTRAINT pk_applicazioni PRIMARY KEY (id)
);


ALTER TABLE applicazioni MODIFY versione DEFAULT '2.1';

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
	gln VARCHAR2(35 CHAR) NOT NULL,
	abilitato NUMBER NOT NULL,
	ragione_sociale VARCHAR2(70 CHAR) NOT NULL,
	xml_conti_accredito BLOB NOT NULL,
	xml_tabella_controparti BLOB NOT NULL,
	riuso_iuv NUMBER NOT NULL,
	custom_iuv NUMBER NOT NULL,
	aux_digit NUMBER NOT NULL,
	iuv_prefix VARCHAR2(255 CHAR),
	iuv_prefix_strict NUMBER NOT NULL,
	segregation_code NUMBER,
	ndp_stato NUMBER,
	ndp_operazione VARCHAR(256),
	ndp_descrizione VARCHAR(1024),
	ndp_data TIMESTAMP,
	logo BLOB,
	cbill VARCHAR2(255 CHAR),
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
ALTER TABLE domini MODIFY iuv_prefix_strict DEFAULT 0;

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



CREATE SEQUENCE seq_operatori MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE operatori
(
	principal VARCHAR2(255 CHAR) NOT NULL,
	nome VARCHAR2(35 CHAR) NOT NULL,
	profilo VARCHAR2(1024 CHAR) NOT NULL,
	abilitato NUMBER NOT NULL,
	-- fk/pk columns
	id NUMBER NOT NULL,
	-- unique constraints
	CONSTRAINT unique_operatori_1 UNIQUE (principal),
	-- fk/pk keys constraints
	CONSTRAINT pk_operatori PRIMARY KEY (id)
);


ALTER TABLE operatori MODIFY abilitato DEFAULT 1;

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



CREATE SEQUENCE seq_portali MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE portali
(
	cod_portale VARCHAR2(35 CHAR) NOT NULL,
	default_callback_url VARCHAR2(512 CHAR) NOT NULL,
	principal VARCHAR2(255 CHAR) NOT NULL,
	versione VARCHAR2(10 CHAR) NOT NULL,
	trusted NUMBER NOT NULL,
	abilitato NUMBER NOT NULL,
	-- fk/pk columns
	id NUMBER NOT NULL,
	-- unique constraints
	CONSTRAINT unique_portali_1 UNIQUE (cod_portale),
	CONSTRAINT unique_portali_2 UNIQUE (principal),
	-- fk/pk keys constraints
	CONSTRAINT pk_portali PRIMARY KEY (id)
);


ALTER TABLE portali MODIFY versione DEFAULT '2.1';

CREATE TRIGGER trg_portali
BEFORE
insert on portali
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_portali.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;
/



CREATE SEQUENCE seq_iban_accredito MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE iban_accredito
(
	cod_iban VARCHAR2(255 CHAR) NOT NULL,
	id_seller_bank VARCHAR2(255 CHAR),
	id_negozio VARCHAR2(255 CHAR),
	bic_accredito VARCHAR2(255 CHAR),
	iban_appoggio VARCHAR2(255 CHAR),
	bic_appoggio VARCHAR2(255 CHAR),
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
	cod_tributo_iuv VARCHAR2(4 CHAR),
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
	cod_tributo_iuv VARCHAR2(4 CHAR),
	-- fk/pk columns
	id NUMBER NOT NULL,
	id_dominio NUMBER NOT NULL,
	id_iban_accredito NUMBER,
	id_tipo_tributo NUMBER NOT NULL,
	-- unique constraints
	CONSTRAINT unique_tributi_1 UNIQUE (id_dominio,id_tipo_tributo),
	-- fk/pk keys constraints
	CONSTRAINT fk_trb_id_dominio FOREIGN KEY (id_dominio) REFERENCES domini(id),
	CONSTRAINT fk_trb_id_iban_accredito FOREIGN KEY (id_iban_accredito) REFERENCES iban_accredito(id),
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



CREATE SEQUENCE seq_ruoli MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE ruoli
(
	cod_ruolo VARCHAR(35) NOT NULL,
	descrizione VARCHAR(255) NOT NULL,
	-- fk/pk columns
	id NUMBER NOT NULL,
	-- unique constraints
	CONSTRAINT unique_ruoli_1 UNIQUE (cod_ruolo),
	-- fk/pk keys constraints
	CONSTRAINT pk_ruoli PRIMARY KEY (id)
);

CREATE TRIGGER trg_ruoli
BEFORE
insert on ruoli
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_ruoli.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;
/



CREATE SEQUENCE seq_acl MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE acl
(
	cod_tipo VARCHAR2(1 CHAR) NOT NULL,
	diritti NUMBER,
	cod_servizio VARCHAR2(35 CHAR) NOT NULL,
	amministratore NUMBER NOT NULL,
	-- fk/pk columns
	id NUMBER NOT NULL,
	id_applicazione NUMBER,
	id_portale NUMBER,
	id_operatore NUMBER,
	id_ruolo NUMBER,
	id_dominio NUMBER,
	id_tipo_tributo NUMBER,
	-- fk/pk keys constraints
	CONSTRAINT fk_acl_id_applicazione FOREIGN KEY (id_applicazione) REFERENCES applicazioni(id),
	CONSTRAINT fk_acl_id_portale FOREIGN KEY (id_portale) REFERENCES portali(id),
	CONSTRAINT fk_acl_id_operatore FOREIGN KEY (id_operatore) REFERENCES operatori(id),
	CONSTRAINT fk_acl_id_ruolo FOREIGN KEY (id_ruolo) REFERENCES ruoli(id),
	CONSTRAINT fk_acl_id_dominio FOREIGN KEY (id_dominio) REFERENCES domini(id),
	CONSTRAINT fk_acl_id_tipo_tributo FOREIGN KEY (id_tipo_tributo) REFERENCES tipi_tributo(id),
	CONSTRAINT pk_acl PRIMARY KEY (id)
);


ALTER TABLE acl MODIFY amministratore DEFAULT 0;

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



CREATE SEQUENCE seq_versamenti MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE versamenti
(
	cod_versamento_ente VARCHAR2(35 CHAR) NOT NULL,
	importo_totale BINARY_DOUBLE NOT NULL,
	stato_versamento VARCHAR2(35 CHAR) NOT NULL,
	descrizione_stato VARCHAR2(255 CHAR),
	-- Indica se, decorsa la dataScadenza, deve essere aggiornato da remoto o essere considerato scaduto
	aggiornabile NUMBER NOT NULL,
	data_creazione TIMESTAMP NOT NULL,
	data_scadenza TIMESTAMP,
	data_ora_ultimo_aggiornamento TIMESTAMP NOT NULL,
	causale_versamento VARCHAR2(1024 CHAR),
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
	cod_lotto VARCHAR2(35 CHAR),
	cod_versamento_lotto VARCHAR2(35 CHAR),
	cod_anno_tributario VARCHAR2(35 CHAR),
	cod_bundlekey VARCHAR2(256 CHAR),
	-- fk/pk columns
	id NUMBER NOT NULL,
	id_uo NUMBER NOT NULL,
	id_applicazione NUMBER NOT NULL,
	-- unique constraints
	CONSTRAINT unique_versamenti_1 UNIQUE (cod_versamento_ente,id_applicazione),
	-- fk/pk keys constraints
	CONSTRAINT fk_vrs_id_uo FOREIGN KEY (id_uo) REFERENCES uo(id),
	CONSTRAINT fk_vrs_id_applicazione FOREIGN KEY (id_applicazione) REFERENCES applicazioni(id),
	CONSTRAINT pk_versamenti PRIMARY KEY (id)
);

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
	anno_riferimento NUMBER,
	-- MARCA BOLLO Valori possibili:\n01: Imposta di bollo
	tipo_bollo VARCHAR2(2 CHAR),
	-- MARCA BOLLO: Digest in Base64 del documento da bollare
	hash_documento VARCHAR2(70 CHAR),
	-- MARCA BOLLO: Sigla automobilistica della provincia di residenza
	provincia_residenza VARCHAR2(2 CHAR),
	tipo_contabilita VARCHAR2(1 CHAR),
	codice_contabilita VARCHAR2(255 CHAR),
	note VARCHAR2(512 CHAR),
	-- fk/pk columns
	id NUMBER NOT NULL,
	id_versamento NUMBER NOT NULL,
	id_tributo NUMBER,
	id_iban_accredito NUMBER,
	-- unique constraints
	CONSTRAINT unique_singoli_versamenti_1 UNIQUE (id_versamento,cod_singolo_versamento_ente),
	-- fk/pk keys constraints
	CONSTRAINT fk_sng_id_versamento FOREIGN KEY (id_versamento) REFERENCES versamenti(id),
	CONSTRAINT fk_sng_id_tributo FOREIGN KEY (id_tributo) REFERENCES tributi(id),
	CONSTRAINT fk_sng_id_iban_accredito FOREIGN KEY (id_iban_accredito) REFERENCES iban_accredito(id),
	CONSTRAINT pk_singoli_versamenti PRIMARY KEY (id)
);

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
	modello_pagamento VARCHAR2(16 CHAR) NOT NULL,
	cod_msg_ricevuta VARCHAR2(35 CHAR),
	data_msg_ricevuta TIMESTAMP,
	firma_ricevuta VARCHAR2(1 CHAR) NOT NULL,
	-- Esito del pagamento:\n0: Eseguito\n1: Non eseguito\n2: Parzialmente eseguito\n3: Decorrenza\n4: Decorrenza Parziale
	cod_esito_pagamento NUMBER,
	importo_totale_pagato BINARY_DOUBLE,
	xml_rt BLOB,
	cod_stazione VARCHAR2(35 CHAR) NOT NULL,
	cod_transazione_rpt VARCHAR2(36 CHAR),
	cod_transazione_rt VARCHAR2(36 CHAR),
	stato_conservazione VARCHAR2(35 CHAR),
	descrizione_stato_cons VARCHAR2(512 CHAR),
	data_conservazione TIMESTAMP,
	-- fk/pk columns
	id NUMBER NOT NULL,
	id_versamento NUMBER NOT NULL,
	id_canale NUMBER NOT NULL,
	id_portale NUMBER,
	-- unique constraints
	CONSTRAINT unique_rpt_1 UNIQUE (cod_msg_richiesta),
	CONSTRAINT unique_rpt_2 UNIQUE (iuv,ccp,cod_dominio),
	-- fk/pk keys constraints
	CONSTRAINT fk_rpt_id_versamento FOREIGN KEY (id_versamento) REFERENCES versamenti(id),
	CONSTRAINT fk_rpt_id_canale FOREIGN KEY (id_canale) REFERENCES canali(id),
	CONSTRAINT fk_rpt_id_portale FOREIGN KEY (id_portale) REFERENCES portali(id),
	CONSTRAINT pk_rpt PRIMARY KEY (id)
);

-- index
CREATE INDEX index_rpt_1 ON rpt (stato);
CREATE INDEX index_rpt_2 ON rpt (id_versamento);
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
CREATE INDEX index_iuv_1 ON iuv (cod_versamento_ente,tipo_iuv,id_applicazione);
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
	-- fk/pk columns
	id NUMBER NOT NULL,
	-- unique constraints
	CONSTRAINT unique_fr_1 UNIQUE (cod_flusso),
	-- fk/pk keys constraints
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
	-- fk/pk columns
	id NUMBER NOT NULL,
	id_applicazione NUMBER,
	-- fk/pk keys constraints
	CONSTRAINT fk_inc_id_applicazione FOREIGN KEY (id_applicazione) REFERENCES applicazioni(id),
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



CREATE SEQUENCE seq_pagamenti MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE pagamenti
(
	cod_dominio VARCHAR2(35 CHAR) NOT NULL,
	iuv VARCHAR2(35 CHAR) NOT NULL,
	indice_dati NUMBER NOT NULL DEFAULT 1,
	importo_pagato BINARY_DOUBLE NOT NULL,
	data_acquisizione TIMESTAMP NOT NULL,
	iur VARCHAR2(35 CHAR) NOT NULL,
	data_pagamento TIMESTAMP NOT NULL,
	iban_accredito VARCHAR2(255 CHAR),
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
	-- fk/pk columns
	id NUMBER NOT NULL,
	id_rpt NUMBER,
	id_singolo_versamento NUMBER NOT NULL,
	id_rr NUMBER,
	id_incasso NUMBER,
	-- unique constraints
	CONSTRAINT unique_pagamenti_1 UNIQUE (cod_dominio,iuv,iur,indice_dati),
	-- fk/pk keys constraints
	CONSTRAINT fk_pag_id_rpt FOREIGN KEY (id_rpt) REFERENCES rpt(id),
	CONSTRAINT fk_pag_id_singolo_versamento FOREIGN KEY (id_singolo_versamento) REFERENCES singoli_versamenti(id),
	CONSTRAINT fk_pag_id_rr FOREIGN KEY (id_rr) REFERENCES rr(id),
	CONSTRAINT fk_pag_id_incasso FOREIGN KEY (id_incasso) REFERENCES incassi(id),
	CONSTRAINT pk_pagamenti PRIMARY KEY (id)
);

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
	-- fk/pk keys constraints
	CONSTRAINT fk_rnd_id_fr FOREIGN KEY (id_fr) REFERENCES fr(id),
	CONSTRAINT fk_rnd_id_pagamento FOREIGN KEY (id_pagamento) REFERENCES pagamenti(id),
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
	cod_dominio VARCHAR2(35),
	iuv VARCHAR2(35),
	ccp VARCHAR2(35),
	cod_psp VARCHAR2(35),
	tipo_versamento VARCHAR2(10),
	componente VARCHAR2(4),
	categoria_evento VARCHAR2(1),
	tipo_evento VARCHAR2(35),
	sottotipo_evento VARCHAR2(35),
	erogatore VARCHAR2(35),
	fruitore VARCHAR2(35),
	cod_stazione VARCHAR2(35),
	cod_canale VARCHAR2(35),
	parametri_1 VARCHAR2(512),
	parametri_2 VARCHAR2(512),
	esito VARCHAR2(35),
	data_1 TIMESTAMP,
	data_2 TIMESTAMP,
	-- fk/pk columns
	id NUMBER NOT NULL,
	-- fk/pk keys constraints
	CONSTRAINT pk_eventi PRIMARY KEY (id)
);

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
	nodo NUMBER,
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



CREATE SEQUENCE seq_tracciati MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE tracciati
(
	data_caricamento TIMESTAMP NOT NULL,
	data_ultimo_aggiornamento TIMESTAMP NOT NULL,
	stato VARCHAR2(255 CHAR) NOT NULL,
	linea_elaborazione NUMBER NOT NULL,
	descrizione_stato VARCHAR2(1024) CHAR,
	num_linee_totali NUMBER NOT NULL,
	num_operazioni_ok NUMBER NOT NULL,
	num_operazioni_ko NUMBER NOT NULL,
	nome_file VARCHAR2(255 CHAR) NOT NULL,
	raw_data_richiesta BLOB NOT NULL,
	raw_data_risposta BLOB,
	-- fk/pk columns
	id NUMBER NOT NULL,
	id_operatore NUMBER,
	id_applicazione NUMBER,
	-- check constraints
	CONSTRAINT chk_tracciati_1 CHECK (stato IN ('ANNULLATO','NUOVO','IN_CARICAMENTO','CARICAMENTO_OK','CARICAMENTO_KO')),
	-- fk/pk keys constraints
	CONSTRAINT fk_trc_id_operatore FOREIGN KEY (id_operatore) REFERENCES operatori(id),
	CONSTRAINT fk_trc_id_applicazione FOREIGN KEY (id_applicazione) REFERENCES applicazioni(id),
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



CREATE SEQUENCE seq_operazioni MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE operazioni
(
	tipo_operazione VARCHAR2(255 CHAR) NOT NULL,
	linea_elaborazione NUMBER NOT NULL,
	stato VARCHAR2(255 CHAR) NOT NULL,
	dati_richiesta BLOB NOT NULL,
	dati_risposta BLOB,
	dettaglio_esito VARCHAR2(255 CHAR),
	cod_versamento_ente VARCHAR2(255 CHAR),
	-- fk/pk columns
	id NUMBER NOT NULL,
	id_tracciato NUMBER NOT NULL,
	id_applicazione NUMBER,
	-- check constraints
	CONSTRAINT chk_operazioni_1 CHECK (stato IN ('NON_VALIDO','ESEGUITO_OK','ESEGUITO_KO')),
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



CREATE SEQUENCE seq_avvisi MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE avvisi
(
	cod_dominio VARCHAR(35) NOT NULL,
	iuv VARCHAR(35) NOT NULL,
	data_creazione TIMESTAMP NOT NULL,
	stato VARCHAR(255) NOT NULL,
	pdf BLOB NOT NULL,
	-- fk/pk columns
	id NUMBER NOT NULL,
	-- check constraints
	CONSTRAINT chk_avvisi_1 CHECK (stato IN ('DA_STAMPARE','STAMPATO')),
	-- fk/pk keys constraints
	CONSTRAINT pk_avvisi PRIMARY KEY (id)
);

-- index
CREATE INDEX index_avvisi_1 ON avvisi (cod_dominio,iuv);
CREATE TRIGGER trg_avvisi
BEFORE
insert on avvisi
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_avvisi.nextval INTO :new.id
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
