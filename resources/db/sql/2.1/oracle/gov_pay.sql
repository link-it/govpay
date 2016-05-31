CREATE SEQUENCE seq_psp MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE psp
(
	cod_psp VARCHAR(35) NOT NULL,
	ragione_sociale VARCHAR(70) NOT NULL,
	url_info VARCHAR(255),
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
	cod_canale VARCHAR(35) NOT NULL,
	cod_intermediario VARCHAR(35) NOT NULL,
	tipo_versamento VARCHAR(4) NOT NULL,
	modello_pagamento NUMBER NOT NULL,
	disponibilita CLOB,
	descrizione CLOB,
	condizioni VARCHAR(35),
	url_info VARCHAR(255),
	abilitato NUMBER NOT NULL,
	-- fk/pk columns
	id NUMBER NOT NULL,
	id_psp NUMBER NOT NULL,
	-- unique constraints
	CONSTRAINT unique_canali_1 UNIQUE (id_psp,cod_canale,tipo_versamento),
	-- fk/pk keys constraints
	CONSTRAINT fk_canali_1 FOREIGN KEY (id_psp) REFERENCES psp(id) ON DELETE CASCADE,
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
	cod_intermediario VARCHAR(35) NOT NULL,
	cod_connettore_pdd VARCHAR(35) NOT NULL,
	denominazione VARCHAR(255) NOT NULL,
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
	cod_stazione VARCHAR(35) NOT NULL,
	password VARCHAR(35) NOT NULL,
	abilitato NUMBER NOT NULL,
	application_code NUMBER NOT NULL,
	-- fk/pk columns
	id NUMBER NOT NULL,
	id_intermediario NUMBER NOT NULL,
	-- unique constraints
	CONSTRAINT unique_stazioni_1 UNIQUE (cod_stazione),
	-- fk/pk keys constraints
	CONSTRAINT fk_stazioni_1 FOREIGN KEY (id_intermediario) REFERENCES intermediari(id) ON DELETE CASCADE,
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
	cod_applicazione VARCHAR(35) NOT NULL,
	abilitato NUMBER NOT NULL,
	principal VARCHAR(255) NOT NULL,
	firma_ricevuta VARCHAR(1) NOT NULL,
	cod_connettore_esito VARCHAR(255),
	cod_connettore_verifica VARCHAR(255),
	trusted NUMBER NOT NULL,
	-- fk/pk columns
	id NUMBER NOT NULL,
	-- unique constraints
	CONSTRAINT unique_applicazioni_1 UNIQUE (cod_applicazione),
	CONSTRAINT unique_applicazioni_2 UNIQUE (principal),
	-- fk/pk keys constraints
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
	cod_dominio VARCHAR(35) NOT NULL,
	gln VARCHAR(35) NOT NULL,
	abilitato NUMBER NOT NULL,
	ragione_sociale VARCHAR(70) NOT NULL,
	xml_conti_accredito BLOB NOT NULL,
	xml_tabella_controparti BLOB NOT NULL,
	riuso_iuv NUMBER NOT NULL,
	custom_iuv NUMBER NOT NULL,
	-- fk/pk columns
	id NUMBER NOT NULL,
	id_stazione NUMBER NOT NULL,
	id_applicazione_default NUMBER,
	-- unique constraints
	CONSTRAINT unique_domini_1 UNIQUE (cod_dominio),
	-- fk/pk keys constraints
	CONSTRAINT fk_domini_1 FOREIGN KEY (id_stazione) REFERENCES stazioni(id) ON DELETE CASCADE,
	CONSTRAINT fk_domini_2 FOREIGN KEY (id_applicazione_default) REFERENCES applicazioni(id) ON DELETE CASCADE,
	CONSTRAINT pk_domini PRIMARY KEY (id)
);

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
	cod_uo VARCHAR(35) NOT NULL,
	abilitato NUMBER NOT NULL,
	uo_codice_identificativo VARCHAR(35),
	uo_denominazione VARCHAR(70),
	uo_indirizzo VARCHAR(70),
	uo_civico VARCHAR(16),
	uo_cap VARCHAR(16),
	uo_localita VARCHAR(35),
	uo_provincia VARCHAR(35),
	uo_nazione VARCHAR(2),
	-- fk/pk columns
	id NUMBER NOT NULL,
	id_dominio NUMBER NOT NULL,
	-- unique constraints
	CONSTRAINT unique_uo_1 UNIQUE (cod_uo,id_dominio),
	-- fk/pk keys constraints
	CONSTRAINT fk_uo_1 FOREIGN KEY (id_dominio) REFERENCES domini(id) ON DELETE CASCADE,
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
	principal VARCHAR(255) NOT NULL,
	nome VARCHAR(35) NOT NULL,
	profilo VARCHAR(16) NOT NULL,
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



CREATE SEQUENCE seq_operatori_uo MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE operatori_uo
(
	-- fk/pk columns
	id NUMBER NOT NULL,
	id_operatore NUMBER NOT NULL,
	id_uo NUMBER NOT NULL,
	-- fk/pk keys constraints
	CONSTRAINT fk_operatori_uo_1 FOREIGN KEY (id_operatore) REFERENCES operatori(id) ON DELETE CASCADE,
	CONSTRAINT fk_operatori_uo_2 FOREIGN KEY (id_uo) REFERENCES uo(id) ON DELETE CASCADE,
	CONSTRAINT pk_operatori_uo PRIMARY KEY (id)
);

CREATE TRIGGER trg_operatori_uo
BEFORE
insert on operatori_uo
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_operatori_uo.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;
/



CREATE SEQUENCE seq_operatori_applicazioni MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE operatori_applicazioni
(
	-- fk/pk columns
	id NUMBER NOT NULL,
	id_operatore NUMBER NOT NULL,
	id_applicazione NUMBER NOT NULL,
	-- fk/pk keys constraints
	CONSTRAINT fk_operatori_applicazioni_1 FOREIGN KEY (id_operatore) REFERENCES operatori(id) ON DELETE CASCADE,
	CONSTRAINT fk_operatori_applicazioni_2 FOREIGN KEY (id_applicazione) REFERENCES applicazioni(id) ON DELETE CASCADE,
	CONSTRAINT pk_operatori_applicazioni PRIMARY KEY (id)
);

CREATE TRIGGER trg_operatori_applicazioni
BEFORE
insert on operatori_applicazioni
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_operatori_applicazioni.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;
/



CREATE SEQUENCE seq_connettori MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE connettori
(
	cod_connettore VARCHAR(255) NOT NULL,
	cod_proprieta VARCHAR(255) NOT NULL,
	valore VARCHAR(255) NOT NULL,
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
	cod_portale VARCHAR(35) NOT NULL,
	default_callback_url VARCHAR(512) NOT NULL,
	principal VARCHAR(255) NOT NULL,
	abilitato NUMBER NOT NULL,
	-- fk/pk columns
	id NUMBER NOT NULL,
	-- unique constraints
	CONSTRAINT unique_portali_1 UNIQUE (cod_portale),
	CONSTRAINT unique_portali_2 UNIQUE (principal),
	-- fk/pk keys constraints
	CONSTRAINT pk_portali PRIMARY KEY (id)
);

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



CREATE SEQUENCE seq_operatori_portali MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE operatori_portali
(
	-- fk/pk columns
	id NUMBER NOT NULL,
	id_operatore NUMBER NOT NULL,
	id_portale NUMBER NOT NULL,
	-- fk/pk keys constraints
	CONSTRAINT fk_operatori_portali_1 FOREIGN KEY (id_operatore) REFERENCES operatori(id) ON DELETE CASCADE,
	CONSTRAINT fk_operatori_portali_2 FOREIGN KEY (id_portale) REFERENCES portali(id) ON DELETE CASCADE,
	CONSTRAINT pk_operatori_portali PRIMARY KEY (id)
);

CREATE TRIGGER trg_operatori_portali
BEFORE
insert on operatori_portali
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_operatori_portali.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;
/



CREATE SEQUENCE seq_portali_applicazioni MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE portali_applicazioni
(
	-- fk/pk columns
	id NUMBER NOT NULL,
	id_portale NUMBER NOT NULL,
	id_applicazione NUMBER NOT NULL,
	-- fk/pk keys constraints
	CONSTRAINT fk_portali_applicazioni_1 FOREIGN KEY (id_portale) REFERENCES portali(id) ON DELETE CASCADE,
	CONSTRAINT fk_portali_applicazioni_2 FOREIGN KEY (id_applicazione) REFERENCES applicazioni(id) ON DELETE CASCADE,
	CONSTRAINT pk_portali_applicazioni PRIMARY KEY (id)
);

CREATE TRIGGER trg_portali_applicazioni
BEFORE
insert on portali_applicazioni
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_portali_applicazioni.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;
/



CREATE SEQUENCE seq_iban_accredito MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE iban_accredito
(
	cod_iban VARCHAR(255) NOT NULL,
	id_seller_bank VARCHAR(255),
	id_negozio VARCHAR(255),
	bic_accredito VARCHAR(255),
	iban_appoggio VARCHAR(255),
	bic_appoggio VARCHAR(255),
	postale NUMBER NOT NULL,
	attivato NUMBER NOT NULL,
	abilitato NUMBER NOT NULL,
	-- fk/pk columns
	id NUMBER NOT NULL,
	id_dominio NUMBER NOT NULL,
	-- unique constraints
	CONSTRAINT unique_iban_accredito_1 UNIQUE (cod_iban,id_dominio),
	-- fk/pk keys constraints
	CONSTRAINT fk_iban_accredito_1 FOREIGN KEY (id_dominio) REFERENCES domini(id) ON DELETE CASCADE,
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



CREATE SEQUENCE seq_tributi MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE tributi
(
	cod_tributo VARCHAR(35) NOT NULL,
	abilitato NUMBER NOT NULL,
	descrizione VARCHAR(255),
	tipo_contabilita VARCHAR(1) NOT NULL,
	codice_contabilita VARCHAR(255) NOT NULL,
	-- fk/pk columns
	id NUMBER NOT NULL,
	id_dominio NUMBER NOT NULL,
	id_iban_accredito NUMBER,
	-- unique constraints
	CONSTRAINT unique_tributi_1 UNIQUE (id_dominio,cod_tributo),
	-- fk/pk keys constraints
	CONSTRAINT fk_tributi_1 FOREIGN KEY (id_dominio) REFERENCES domini(id) ON DELETE CASCADE,
	CONSTRAINT fk_tributi_2 FOREIGN KEY (id_iban_accredito) REFERENCES iban_accredito(id) ON DELETE CASCADE,
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



CREATE SEQUENCE seq_applicazioni_tributi MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE applicazioni_tributi
(
	-- fk/pk columns
	id NUMBER NOT NULL,
	id_applicazione NUMBER,
	id_tributo NUMBER NOT NULL,
	-- fk/pk keys constraints
	CONSTRAINT fk_applicazioni_tributi_1 FOREIGN KEY (id_applicazione) REFERENCES applicazioni(id) ON DELETE CASCADE,
	CONSTRAINT fk_applicazioni_tributi_2 FOREIGN KEY (id_tributo) REFERENCES tributi(id) ON DELETE CASCADE,
	CONSTRAINT pk_applicazioni_tributi PRIMARY KEY (id)
);

CREATE TRIGGER trg_applicazioni_tributi
BEFORE
insert on applicazioni_tributi
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_applicazioni_tributi.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;
/



CREATE SEQUENCE seq_applicazioni_domini MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE applicazioni_domini
(
	-- fk/pk columns
	id NUMBER NOT NULL,
	id_applicazione NUMBER,
	id_dominio NUMBER NOT NULL,
	-- fk/pk keys constraints
	CONSTRAINT fk_applicazioni_domini_1 FOREIGN KEY (id_applicazione) REFERENCES applicazioni(id) ON DELETE CASCADE,
	CONSTRAINT fk_applicazioni_domini_2 FOREIGN KEY (id_dominio) REFERENCES domini(id) ON DELETE CASCADE,
	CONSTRAINT pk_applicazioni_domini PRIMARY KEY (id)
);

CREATE TRIGGER trg_applicazioni_domini
BEFORE
insert on applicazioni_domini
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_applicazioni_domini.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;
/



CREATE SEQUENCE seq_versamenti MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE versamenti
(
	cod_versamento_ente VARCHAR(35) NOT NULL,
	importo_totale BINARY_DOUBLE NOT NULL,
	stato_versamento VARCHAR(35) NOT NULL,
	descrizione_stato VARCHAR(255),
	-- Indica se, decorsa la dataScadenza, deve essere aggiornato da remoto o essere considerato scaduto
	aggiornabile NUMBER NOT NULL,
	data_creazione TIMESTAMP NOT NULL,
	data_scadenza TIMESTAMP,
	data_ora_ultimo_aggiornamento TIMESTAMP NOT NULL,
	causale_versamento VARCHAR(511),
	debitore_identificativo VARCHAR(35) NOT NULL,
	debitore_anagrafica VARCHAR(70) NOT NULL,
	debitore_indirizzo VARCHAR(70),
	debitore_civico VARCHAR(16),
	debitore_cap VARCHAR(16),
	debitore_localita VARCHAR(35),
	debitore_provincia VARCHAR(35),
	debitore_nazione VARCHAR(2),
	-- fk/pk columns
	id NUMBER NOT NULL,
	id_uo NUMBER NOT NULL,
	id_applicazione NUMBER NOT NULL,
	-- unique constraints
	CONSTRAINT unique_versamenti_1 UNIQUE (cod_versamento_ente,id_applicazione),
	-- fk/pk keys constraints
	CONSTRAINT fk_versamenti_1 FOREIGN KEY (id_uo) REFERENCES uo(id) ON DELETE CASCADE,
	CONSTRAINT fk_versamenti_2 FOREIGN KEY (id_applicazione) REFERENCES applicazioni(id) ON DELETE CASCADE,
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
	cod_singolo_versamento_ente VARCHAR(70) NOT NULL,
	stato_singolo_versamento VARCHAR(35) NOT NULL,
	importo_singolo_versamento BINARY_DOUBLE NOT NULL,
	anno_riferimento NUMBER,
	-- MARCA BOLLO Valori possibili:\n01: Imposta di bollo
	tipo_bollo VARCHAR(2),
	-- MARCA BOLLO: Digest in Base64 del documento da bollare
	hash_documento VARCHAR(70),
	-- MARCA BOLLO: Sigla automobilistica della provincia di residenza
	provincia_residenza VARCHAR(2),
	tipo_contabilita VARCHAR(1),
	codice_contabilita VARCHAR(255),
	-- fk/pk columns
	id NUMBER NOT NULL,
	id_versamento NUMBER NOT NULL,
	id_tributo NUMBER,
	id_iban_accredito NUMBER,
	-- unique constraints
	CONSTRAINT unique_singoli_versamenti_1 UNIQUE (id_versamento,cod_singolo_versamento_ente),
	-- fk/pk keys constraints
	CONSTRAINT fk_singoli_versamenti_1 FOREIGN KEY (id_versamento) REFERENCES versamenti(id) ON DELETE CASCADE,
	CONSTRAINT fk_singoli_versamenti_2 FOREIGN KEY (id_tributo) REFERENCES tributi(id) ON DELETE CASCADE,
	CONSTRAINT fk_singoli_versamenti_3 FOREIGN KEY (id_iban_accredito) REFERENCES iban_accredito(id) ON DELETE CASCADE,
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
	cod_carrello VARCHAR(35),
	iuv VARCHAR(35) NOT NULL,
	ccp VARCHAR(35) NOT NULL,
	cod_dominio VARCHAR(35) NOT NULL,
	-- Identificativo dell'RPT utilizzato come riferimento nell'RT
	cod_msg_richiesta VARCHAR(35) NOT NULL,
	-- Data di creazione dell'RPT
	data_msg_richiesta TIMESTAMP NOT NULL,
	-- Stato RPT secondo la codifica AgID
	stato VARCHAR(35) NOT NULL,
	descrizione_stato CLOB,
	cod_sessione VARCHAR(255),
	-- Indirizzo del portale psp a cui redirigere il cittadino per eseguire il pagamento
	psp_redirect_url VARCHAR(512),
	xml_rpt BLOB NOT NULL,
	data_aggiornamento_stato TIMESTAMP NOT NULL,
	-- Indirizzo di ritorno al portale dell'ente al termine del pagamento
	callback_url CLOB,
	modello_pagamento VARCHAR(16) NOT NULL,
	cod_msg_ricevuta VARCHAR(35),
	data_msg_ricevuta TIMESTAMP,
	firma_ricevuta VARCHAR(1) NOT NULL,
	-- Esito del pagamento:\n0: Eseguito\n1: Non eseguito\n2: Parzialmente eseguito\n3: Decorrenza\n4: Decorrenza Parziale
	cod_esito_pagamento NUMBER,
	importo_totale_pagato BINARY_DOUBLE,
	xml_rt BLOB,
	cod_stazione VARCHAR(35) NOT NULL,
	-- fk/pk columns
	id NUMBER NOT NULL,
	id_versamento NUMBER NOT NULL,
	id_canale NUMBER NOT NULL,
	id_portale NUMBER,
	-- unique constraints
	CONSTRAINT unique_rpt_1 UNIQUE (cod_msg_richiesta),
	CONSTRAINT unique_rpt_2 UNIQUE (iuv,ccp,cod_dominio),
	-- fk/pk keys constraints
	CONSTRAINT fk_rpt_1 FOREIGN KEY (id_versamento) REFERENCES versamenti(id) ON DELETE CASCADE,
	CONSTRAINT fk_rpt_2 FOREIGN KEY (id_canale) REFERENCES canali(id) ON DELETE CASCADE,
	CONSTRAINT fk_rpt_3 FOREIGN KEY (id_portale) REFERENCES portali(id) ON DELETE CASCADE,
	CONSTRAINT pk_rpt PRIMARY KEY (id)
);

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
	cod_dominio VARCHAR(35) NOT NULL,
	iuv VARCHAR(35) NOT NULL,
	ccp VARCHAR(35) NOT NULL,
	cod_msg_revoca VARCHAR(35) NOT NULL,
	data_msg_revoca TIMESTAMP NOT NULL,
	data_msg_esito TIMESTAMP,
	stato VARCHAR(35) NOT NULL,
	descrizione_stato VARCHAR(512),
	importo_totale_richiesto BINARY_DOUBLE NOT NULL,
	cod_msg_esito VARCHAR(35),
	importo_totale_revocato BINARY_DOUBLE,
	xml_rr BLOB NOT NULL,
	xml_er BLOB,
	-- fk/pk columns
	id NUMBER NOT NULL,
	id_rpt NUMBER NOT NULL,
	-- unique constraints
	CONSTRAINT unique_rr_1 UNIQUE (cod_msg_revoca),
	-- fk/pk keys constraints
	CONSTRAINT fk_rr_1 FOREIGN KEY (id_rpt) REFERENCES rpt(id) ON DELETE CASCADE,
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
	tipo_esito VARCHAR(16) NOT NULL,
	data_creazione TIMESTAMP NOT NULL,
	stato VARCHAR(16) NOT NULL,
	descrizione_stato VARCHAR(255),
	data_aggiornamento_stato TIMESTAMP NOT NULL,
	data_prossima_spedizione TIMESTAMP NOT NULL,
	tentativi_spedizione NUMBER,
	-- fk/pk columns
	id NUMBER NOT NULL,
	id_applicazione NUMBER NOT NULL,
	id_rpt NUMBER,
	id_rr NUMBER,
	-- fk/pk keys constraints
	CONSTRAINT fk_notifiche_1 FOREIGN KEY (id_applicazione) REFERENCES applicazioni(id) ON DELETE CASCADE,
	CONSTRAINT fk_notifiche_2 FOREIGN KEY (id_rpt) REFERENCES rpt(id) ON DELETE CASCADE,
	CONSTRAINT fk_notifiche_3 FOREIGN KEY (id_rr) REFERENCES rr(id) ON DELETE CASCADE,
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
	iuv VARCHAR(35) NOT NULL,
	application_code NUMBER NOT NULL,
	data_generazione DATE NOT NULL,
	tipo_iuv VARCHAR(1) NOT NULL,
	cod_versamento_ente VARCHAR(35),
	-- fk/pk columns
	id NUMBER NOT NULL,
	id_applicazione NUMBER NOT NULL,
	id_dominio NUMBER NOT NULL,
	-- unique constraints
	CONSTRAINT unique_iuv_1 UNIQUE (id_dominio,iuv),
	-- fk/pk keys constraints
	CONSTRAINT fk_iuv_1 FOREIGN KEY (id_applicazione) REFERENCES applicazioni(id) ON DELETE CASCADE,
	CONSTRAINT fk_iuv_2 FOREIGN KEY (id_dominio) REFERENCES domini(id) ON DELETE CASCADE,
	CONSTRAINT pk_iuv PRIMARY KEY (id)
);

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
	cod_flusso VARCHAR(35) NOT NULL,
	stato VARCHAR(35) NOT NULL,
	descrizione_stato CLOB,
	iur VARCHAR(35) NOT NULL,
	anno_riferimento NUMBER NOT NULL,
	data_ora_flusso TIMESTAMP,
	data_regolamento TIMESTAMP,
	numero_pagamenti NUMBER,
	importo_totale_pagamenti BINARY_DOUBLE,
	cod_bic_riversamento VARCHAR(35),
	xml BLOB NOT NULL,
	-- fk/pk columns
	id NUMBER NOT NULL,
	id_psp NUMBER NOT NULL,
	id_dominio NUMBER NOT NULL,
	-- unique constraints
	CONSTRAINT unique_fr_1 UNIQUE (cod_flusso,anno_riferimento),
	-- fk/pk keys constraints
	CONSTRAINT fk_fr_1 FOREIGN KEY (id_psp) REFERENCES psp(id) ON DELETE CASCADE,
	CONSTRAINT fk_fr_2 FOREIGN KEY (id_dominio) REFERENCES domini(id) ON DELETE CASCADE,
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



CREATE SEQUENCE seq_fr_applicazioni MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE fr_applicazioni
(
	numero_pagamenti NUMBER NOT NULL,
	importo_totale_pagamenti BINARY_DOUBLE NOT NULL,
	-- fk/pk columns
	id NUMBER NOT NULL,
	id_applicazione NUMBER NOT NULL,
	id_fr NUMBER NOT NULL,
	-- fk/pk keys constraints
	CONSTRAINT fk_fr_applicazioni_1 FOREIGN KEY (id_applicazione) REFERENCES applicazioni(id) ON DELETE CASCADE,
	CONSTRAINT fk_fr_applicazioni_2 FOREIGN KEY (id_fr) REFERENCES fr(id) ON DELETE CASCADE,
	CONSTRAINT pk_fr_applicazioni PRIMARY KEY (id)
);

CREATE TRIGGER trg_fr_applicazioni
BEFORE
insert on fr_applicazioni
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_fr_applicazioni.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;
/



CREATE SEQUENCE seq_pagamenti MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE pagamenti
(
	cod_singolo_versamento_ente VARCHAR(35) NOT NULL,
	importo_pagato BINARY_DOUBLE NOT NULL,
	iur VARCHAR(35) NOT NULL,
	data_pagamento TIMESTAMP NOT NULL,
	commissioni_psp BINARY_DOUBLE,
	-- Valori possibili:\nES: Esito originario\nBD: Marca da Bollo
	tipo_allegato VARCHAR(2),
	allegato BLOB,
	rendicontazione_esito NUMBER,
	rendicontazione_data TIMESTAMP,
	codflusso_rendicontazione VARCHAR(35),
	anno_riferimento NUMBER,
	indice_singolo_pagamento NUMBER,
	causale_revoca VARCHAR(140),
	dati_revoca VARCHAR(140),
	importo_revocato BINARY_DOUBLE,
	esito_revoca VARCHAR(140),
	dati_esito_revoca VARCHAR(140),
	rendicontazione_esito_revoca NUMBER,
	rendicontazione_data_revoca TIMESTAMP,
	cod_flusso_rendicontaz_revoca VARCHAR(35),
	anno_riferimento_revoca NUMBER,
	ind_singolo_pagamento_revoca NUMBER,
	-- fk/pk columns
	id NUMBER NOT NULL,
	id_rpt NUMBER NOT NULL,
	id_singolo_versamento NUMBER NOT NULL,
	id_fr_applicazione NUMBER,
	id_rr NUMBER,
	id_fr_applicazione_revoca NUMBER,
	-- fk/pk keys constraints
	CONSTRAINT fk_pagamenti_1 FOREIGN KEY (id_rpt) REFERENCES rpt(id) ON DELETE CASCADE,
	CONSTRAINT fk_pagamenti_2 FOREIGN KEY (id_singolo_versamento) REFERENCES singoli_versamenti(id) ON DELETE CASCADE,
	CONSTRAINT fk_pagamenti_3 FOREIGN KEY (id_fr_applicazione) REFERENCES fr_applicazioni(id) ON DELETE CASCADE,
	CONSTRAINT fk_pagamenti_4 FOREIGN KEY (id_rr) REFERENCES rr(id) ON DELETE CASCADE,
	CONSTRAINT fk_pagamenti_5 FOREIGN KEY (id_fr_applicazione_revoca) REFERENCES fr_applicazioni(id) ON DELETE CASCADE,
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



CREATE SEQUENCE seq_eventi MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE eventi
(
	cod_dominio VARCHAR(35),
	iuv VARCHAR(35),
	ccp VARCHAR(35),
	cod_psp VARCHAR(35),
	tipo_versamento VARCHAR(10),
	componente VARCHAR(4),
	categoria_evento VARCHAR(1),
	tipo_evento VARCHAR(35),
	sottotipo_evento VARCHAR(35),
	erogatore VARCHAR(35),
	fruitore VARCHAR(35),
	cod_stazione VARCHAR(35),
	cod_canale VARCHAR(35),
	parametri_1 VARCHAR(512),
	parametri_2 VARCHAR(512),
	esito VARCHAR(35),
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



CREATE SEQUENCE seq_rendicontazioni_senza_rpt MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE rendicontazioni_senza_rpt
(
	importo_pagato BINARY_DOUBLE NOT NULL,
	iur VARCHAR(35) NOT NULL,
	rendicontazione_data DATE NOT NULL,
	-- fk/pk columns
	id NUMBER NOT NULL,
	id_fr_applicazione NUMBER NOT NULL,
	id_iuv NUMBER NOT NULL,
	id_singolo_versamento NUMBER,
	-- fk/pk keys constraints
	CONSTRAINT fk_rendicontazioni_senza_rpt_1 FOREIGN KEY (id_fr_applicazione) REFERENCES fr_applicazioni(id) ON DELETE CASCADE,
	CONSTRAINT fk_rendicontazioni_senza_rpt_2 FOREIGN KEY (id_iuv) REFERENCES iuv(id) ON DELETE CASCADE,
	CONSTRAINT fk_rendicontazioni_senza_rpt_3 FOREIGN KEY (id_singolo_versamento) REFERENCES singoli_versamenti(id) ON DELETE CASCADE,
	CONSTRAINT pk_rendicontazioni_senza_rpt PRIMARY KEY (id)
);

CREATE TRIGGER trg_rendicontazioni_senza_rpt
BEFORE
insert on rendicontazioni_senza_rpt
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_rendicontazioni_senza_rpt.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;
/



CREATE TABLE ID_MESSAGGIO_RELATIVO
(
	COUNTER NUMBER NOT NULL,
	PROTOCOLLO VARCHAR(255) NOT NULL,
	INFO_ASSOCIATA VARCHAR(255) NOT NULL,
	ora_registrazione TIMESTAMP,
	-- fk/pk columns
	-- fk/pk keys constraints
	CONSTRAINT pk_ID_MESSAGGIO_RELATIVO PRIMARY KEY (PROTOCOLLO,INFO_ASSOCIATA)
);


ALTER TABLE ID_MESSAGGIO_RELATIVO MODIFY ora_registrazione DEFAULT CURRENT_TIMESTAMP;

