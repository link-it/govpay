CREATE SEQUENCE seq_tracciatixml MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE tracciatixml
(
	tipo_tracciato VARCHAR(255) NOT NULL,
	cod_messaggio VARCHAR(255) NOT NULL,
	data_ora_creazione TIMESTAMP NOT NULL,
	xml BLOB NOT NULL,
	-- fk/pk columns
	id NUMBER NOT NULL,
	-- fk/pk keys constraints
	CONSTRAINT pk_tracciatixml PRIMARY KEY (id)
);

CREATE TRIGGER trg_tracciatixml
BEFORE
insert on tracciatixml
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_tracciatixml.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;
/



CREATE SEQUENCE seq_psp MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE psp
(
	cod_psp VARCHAR(255) NOT NULL,
	ragione_sociale VARCHAR(255) NOT NULL,
	cod_flusso VARCHAR(255) NOT NULL,
	url_info VARCHAR(255),
	abilitato NUMBER NOT NULL,
	storno NUMBER NOT NULL,
	marca_bollo NUMBER NOT NULL,
	-- fk/pk columns
	id NUMBER NOT NULL,
	id_tracciato_xml NUMBER NOT NULL,
	-- unique constraints
	CONSTRAINT unique_psp_1 UNIQUE (cod_psp),
	-- fk/pk keys constraints
	CONSTRAINT fk_psp_1 FOREIGN KEY (id_tracciato_xml) REFERENCES tracciatixml(id) ON DELETE CASCADE,
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
	cod_canale VARCHAR(255) NOT NULL,
	cod_intermediario VARCHAR(255) NOT NULL,
	tipo_versamento VARCHAR(255) NOT NULL,
	modello_pagamento NUMBER NOT NULL,
	disponibilita CLOB,
	descrizione CLOB,
	condizioni VARCHAR(255),
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



CREATE SEQUENCE seq_anagrafiche MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE anagrafiche
(
	ragione_sociale VARCHAR(255) NOT NULL,
	cod_univoco VARCHAR(255) NOT NULL,
	indirizzo VARCHAR(255),
	civico VARCHAR(255),
	cap VARCHAR(255),
	localita VARCHAR(255),
	provincia VARCHAR(255),
	nazione VARCHAR(255),
	email VARCHAR(255),
	telefono VARCHAR(255),
	cellulare VARCHAR(255),
	fax VARCHAR(255),
	-- fk/pk columns
	id NUMBER NOT NULL,
	-- fk/pk keys constraints
	CONSTRAINT pk_anagrafiche PRIMARY KEY (id)
);

CREATE TRIGGER trg_anagrafiche
BEFORE
insert on anagrafiche
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_anagrafiche.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;
/



CREATE SEQUENCE seq_intermediari MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE intermediari
(
	cod_intermediario VARCHAR(255) NOT NULL,
	cod_connettore_pdd VARCHAR(255) NOT NULL,
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
	cod_stazione VARCHAR(255) NOT NULL,
	password VARCHAR(255) NOT NULL,
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



CREATE SEQUENCE seq_domini MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE domini
(
	cod_dominio VARCHAR(255) NOT NULL,
	ragione_sociale VARCHAR(255) NOT NULL,
	gln VARCHAR(255) NOT NULL,
	plugin_class VARCHAR(512),
	abilitato NUMBER NOT NULL,
	-- fk/pk columns
	id NUMBER NOT NULL,
	id_stazione NUMBER NOT NULL,
	-- unique constraints
	CONSTRAINT unique_domini_1 UNIQUE (cod_dominio),
	-- fk/pk keys constraints
	CONSTRAINT fk_domini_1 FOREIGN KEY (id_stazione) REFERENCES stazioni(id) ON DELETE CASCADE,
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



CREATE SEQUENCE seq_disponibilita MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE disponibilita
(
	tipo_periodo VARCHAR(255) NOT NULL,
	giorno VARCHAR(255) NOT NULL,
	fasce_orarie CLOB NOT NULL,
	tipo_disponibilita VARCHAR(255) NOT NULL,
	-- fk/pk columns
	id NUMBER NOT NULL,
	id_dominio NUMBER,
	-- fk/pk keys constraints
	CONSTRAINT fk_disponibilita_1 FOREIGN KEY (id_dominio) REFERENCES domini(id) ON DELETE CASCADE,
	CONSTRAINT pk_disponibilita PRIMARY KEY (id)
);

CREATE TRIGGER trg_disponibilita
BEFORE
insert on disponibilita
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_disponibilita.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;
/



CREATE SEQUENCE seq_mail_template MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE mail_template
(
	mittente VARCHAR(255) NOT NULL,
	template_oggetto CLOB NOT NULL,
	template_messaggio CLOB NOT NULL,
	allegati VARCHAR(255),
	-- fk/pk columns
	id NUMBER NOT NULL,
	-- fk/pk keys constraints
	CONSTRAINT pk_mail_template PRIMARY KEY (id)
);

CREATE TRIGGER trg_mail_template
BEFORE
insert on mail_template
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_mail_template.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;
/



CREATE SEQUENCE seq_enti MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE enti
(
	cod_ente VARCHAR(255) NOT NULL,
	abilitato NUMBER NOT NULL,
	invio_mail_rptabilitato NUMBER NOT NULL,
	invio_mail_rtabilitato NUMBER NOT NULL,
	-- fk/pk columns
	id NUMBER NOT NULL,
	id_anagrafica_ente NUMBER NOT NULL,
	id_dominio NUMBER NOT NULL,
	id_template_rpt NUMBER,
	id_template_rt NUMBER,
	-- unique constraints
	CONSTRAINT unique_enti_1 UNIQUE (cod_ente),
	-- fk/pk keys constraints
	CONSTRAINT fk_enti_1 FOREIGN KEY (id_anagrafica_ente) REFERENCES anagrafiche(id) ON DELETE CASCADE,
	CONSTRAINT fk_enti_2 FOREIGN KEY (id_dominio) REFERENCES domini(id) ON DELETE CASCADE,
	CONSTRAINT fk_enti_3 FOREIGN KEY (id_template_rpt) REFERENCES mail_template(id) ON DELETE CASCADE,
	CONSTRAINT fk_enti_4 FOREIGN KEY (id_template_rt) REFERENCES mail_template(id) ON DELETE CASCADE,
	CONSTRAINT pk_enti PRIMARY KEY (id)
);

CREATE TRIGGER trg_enti
BEFORE
insert on enti
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_enti.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;
/



CREATE SEQUENCE seq_applicazioni MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE applicazioni
(
	cod_applicazione VARCHAR(255) NOT NULL,
	principal VARCHAR(255) NOT NULL,
	abilitato NUMBER NOT NULL,
	versione VARCHAR(255) NOT NULL,
	policy_rispedizione VARCHAR(255),
	cod_connettore_esito VARCHAR(255),
	cod_connettore_verifica VARCHAR(255),
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



CREATE SEQUENCE seq_operatori MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE operatori
(
	principal VARCHAR(255) NOT NULL,
	nome VARCHAR(255) NOT NULL,
	profilo VARCHAR(255) NOT NULL,
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



CREATE SEQUENCE seq_operatori_enti MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE operatori_enti
(
	-- fk/pk columns
	id NUMBER NOT NULL,
	id_operatore NUMBER NOT NULL,
	id_ente NUMBER NOT NULL,
	-- fk/pk keys constraints
	CONSTRAINT fk_operatori_enti_1 FOREIGN KEY (id_operatore) REFERENCES operatori(id) ON DELETE CASCADE,
	CONSTRAINT fk_operatori_enti_2 FOREIGN KEY (id_ente) REFERENCES enti(id) ON DELETE CASCADE,
	CONSTRAINT pk_operatori_enti PRIMARY KEY (id)
);

CREATE TRIGGER trg_operatori_enti
BEFORE
insert on operatori_enti
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_operatori_enti.nextval INTO :new.id
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
	cod_portale VARCHAR(255) NOT NULL,
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
	CONSTRAINT unique_iban_accredito_1 UNIQUE (cod_iban),
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
	cod_tributo VARCHAR(255) NOT NULL,
	abilitato NUMBER NOT NULL,
	descrizione VARCHAR(255),
	tipo_contabilita VARCHAR(255) NOT NULL,
	codice_contabilita VARCHAR(255) NOT NULL,
	-- fk/pk columns
	id NUMBER NOT NULL,
	id_ente NUMBER NOT NULL,
	id_iban_accredito NUMBER,
	-- unique constraints
	CONSTRAINT unique_tributi_1 UNIQUE (id_ente,cod_tributo),
	-- fk/pk keys constraints
	CONSTRAINT fk_tributi_1 FOREIGN KEY (id_ente) REFERENCES enti(id) ON DELETE CASCADE,
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



CREATE SEQUENCE seq_versamenti MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE versamenti
(
	cod_versamento_ente VARCHAR(255) NOT NULL,
	cod_dominio VARCHAR(255) NOT NULL,
	iuv VARCHAR(255) NOT NULL,
	importo_totale BINARY_DOUBLE NOT NULL,
	stato_versamento VARCHAR(255) NOT NULL,
	descrizione_stato VARCHAR(255),
	stato_rendicontazione VARCHAR(255),
	importo_pagato BINARY_DOUBLE,
	data_scadenza DATE NOT NULL,
	data_ora_ultimo_aggiornamento TIMESTAMP NOT NULL,
	-- fk/pk columns
	id NUMBER NOT NULL,
	id_ente NUMBER NOT NULL,
	id_applicazione NUMBER NOT NULL,
	id_anagrafica_debitore NUMBER NOT NULL,
	-- unique constraints
	CONSTRAINT unique_versamenti_1 UNIQUE (cod_versamento_ente,cod_dominio),
	CONSTRAINT unique_versamenti_2 UNIQUE (iuv,cod_dominio),
	-- fk/pk keys constraints
	CONSTRAINT fk_versamenti_1 FOREIGN KEY (id_ente) REFERENCES enti(id) ON DELETE CASCADE,
	CONSTRAINT fk_versamenti_2 FOREIGN KEY (id_applicazione) REFERENCES applicazioni(id) ON DELETE CASCADE,
	CONSTRAINT fk_versamenti_3 FOREIGN KEY (id_anagrafica_debitore) REFERENCES anagrafiche(id) ON DELETE CASCADE,
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
	indice NUMBER NOT NULL,
	cod_singolo_versamento_ente VARCHAR(255),
	anno_riferimento NUMBER,
	iban_accredito VARCHAR(255),
	importo_singolo_versamento BINARY_DOUBLE NOT NULL,
	importo_commissioni_pa BINARY_DOUBLE,
	singolo_importo_pagato BINARY_DOUBLE,
	causale_versamento VARCHAR(255),
	dati_specifici_riscossione VARCHAR(255),
	stato_singolo_versamento VARCHAR(255),
	esito_singolo_pagamento VARCHAR(255),
	data_esito_singolo_pagamento DATE,
	iur VARCHAR(255),
	-- fk/pk columns
	id NUMBER NOT NULL,
	id_versamento NUMBER NOT NULL,
	id_tributo NUMBER NOT NULL,
	-- unique constraints
	CONSTRAINT unique_singoli_versamenti_1 UNIQUE (id_versamento,indice),
	-- fk/pk keys constraints
	CONSTRAINT fk_singoli_versamenti_1 FOREIGN KEY (id_versamento) REFERENCES versamenti(id) ON DELETE CASCADE,
	CONSTRAINT fk_singoli_versamenti_2 FOREIGN KEY (id_tributo) REFERENCES tributi(id) ON DELETE CASCADE,
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
	cod_carrello VARCHAR(255),
	iuv VARCHAR(255) NOT NULL,
	ccp VARCHAR(255) NOT NULL,
	cod_dominio VARCHAR(255) NOT NULL,
	tipo_versamento VARCHAR(255),
	data_ora_msg_richiesta TIMESTAMP,
	data_ora_creazione TIMESTAMP NOT NULL,
	cod_msg_richiesta VARCHAR(255) NOT NULL,
	iban_addebito VARCHAR(255),
	autenticazione_soggetto VARCHAR(255),
	firma_rt VARCHAR(255) NOT NULL,
	stato VARCHAR(255) NOT NULL,
	descrizione_stato CLOB,
	cod_fault VARCHAR(255),
	callback_url CLOB,
	cod_sessione VARCHAR(255),
	psp_redirect_url VARCHAR(512),
	-- fk/pk columns
	id NUMBER NOT NULL,
	id_versamento NUMBER NOT NULL,
	id_psp NUMBER,
	id_canale NUMBER,
	id_portale NUMBER,
	id_tracciato_xml NUMBER,
	id_stazione NUMBER,
	id_anagrafica_versante NUMBER,
	-- unique constraints
	CONSTRAINT unique_rpt_1 UNIQUE (cod_msg_richiesta),
	CONSTRAINT unique_rpt_2 UNIQUE (iuv,ccp,cod_dominio),
	-- fk/pk keys constraints
	CONSTRAINT fk_rpt_1 FOREIGN KEY (id_versamento) REFERENCES versamenti(id) ON DELETE CASCADE,
	CONSTRAINT fk_rpt_2 FOREIGN KEY (id_psp) REFERENCES psp(id) ON DELETE CASCADE,
	CONSTRAINT fk_rpt_3 FOREIGN KEY (id_canale) REFERENCES canali(id) ON DELETE CASCADE,
	CONSTRAINT fk_rpt_4 FOREIGN KEY (id_portale) REFERENCES portali(id) ON DELETE CASCADE,
	CONSTRAINT fk_rpt_5 FOREIGN KEY (id_tracciato_xml) REFERENCES tracciatixml(id) ON DELETE CASCADE,
	CONSTRAINT fk_rpt_6 FOREIGN KEY (id_stazione) REFERENCES stazioni(id) ON DELETE CASCADE,
	CONSTRAINT fk_rpt_7 FOREIGN KEY (id_anagrafica_versante) REFERENCES anagrafiche(id) ON DELETE CASCADE,
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



CREATE SEQUENCE seq_carrelli MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE carrelli
(
	cod_carrello VARCHAR(255) NOT NULL,
	-- fk/pk columns
	id NUMBER NOT NULL,
	id_rpt NUMBER NOT NULL,
	-- unique constraints
	CONSTRAINT unique_carrelli_1 UNIQUE (id_rpt,cod_carrello),
	-- fk/pk keys constraints
	CONSTRAINT fk_carrelli_1 FOREIGN KEY (id_rpt) REFERENCES rpt(id) ON DELETE CASCADE,
	CONSTRAINT pk_carrelli PRIMARY KEY (id)
);

CREATE TRIGGER trg_carrelli
BEFORE
insert on carrelli
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_carrelli.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;
/



CREATE SEQUENCE seq_esiti MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE esiti
(
	cod_dominio VARCHAR(255) NOT NULL,
	iuv VARCHAR(255) NOT NULL,
	stato_spedizione VARCHAR(255) NOT NULL,
	dettaglio_spedizione VARCHAR(255),
	tentativi_spedizione NUMBER,
	data_ora_creazione TIMESTAMP NOT NULL,
	data_ora_ultima_spedizione TIMESTAMP,
	data_ora_prossima_spedizione TIMESTAMP,
	xml BLOB NOT NULL,
	-- fk/pk columns
	id NUMBER NOT NULL,
	id_applicazione NUMBER NOT NULL,
	-- fk/pk keys constraints
	CONSTRAINT fk_esiti_1 FOREIGN KEY (id_applicazione) REFERENCES applicazioni(id) ON DELETE CASCADE,
	CONSTRAINT pk_esiti PRIMARY KEY (id)
);

CREATE TRIGGER trg_esiti
BEFORE
insert on esiti
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_esiti.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;
/



CREATE SEQUENCE seq_rt MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE rt
(
	cod_msg_ricevuta VARCHAR(255) NOT NULL,
	data_ora_msg_ricevuta TIMESTAMP NOT NULL,
	cod_esito_pagamento NUMBER NOT NULL,
	importo_totale_pagato BINARY_DOUBLE NOT NULL,
	stato VARCHAR(255) NOT NULL,
	descrizione_stato VARCHAR(255) NOT NULL,
	-- fk/pk columns
	id NUMBER NOT NULL,
	id_rpt NUMBER NOT NULL,
	id_tracciato_xml NUMBER NOT NULL,
	id_anagrafica_attestante NUMBER NOT NULL,
	-- unique constraints
	CONSTRAINT unique_rt_1 UNIQUE (cod_msg_ricevuta),
	-- fk/pk keys constraints
	CONSTRAINT fk_rt_1 FOREIGN KEY (id_rpt) REFERENCES rpt(id) ON DELETE CASCADE,
	CONSTRAINT fk_rt_2 FOREIGN KEY (id_tracciato_xml) REFERENCES tracciatixml(id) ON DELETE CASCADE,
	CONSTRAINT fk_rt_3 FOREIGN KEY (id_anagrafica_attestante) REFERENCES anagrafiche(id) ON DELETE CASCADE,
	CONSTRAINT pk_rt PRIMARY KEY (id)
);

CREATE TRIGGER trg_rt
BEFORE
insert on rt
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_rt.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;
/



CREATE SEQUENCE seq_iuv MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE iuv
(
	cod_dominio VARCHAR(255) NOT NULL,
	prg NUMBER NOT NULL,
	iuv VARCHAR(255) NOT NULL,
	data_generazione DATE NOT NULL,
	application_code NUMBER NOT NULL,
	aux_digit NUMBER NOT NULL,
	-- fk/pk columns
	id NUMBER NOT NULL,
	id_applicazione NUMBER NOT NULL,
	-- unique constraints
	CONSTRAINT unique_iuv_1 UNIQUE (cod_dominio,iuv),
	-- fk/pk keys constraints
	CONSTRAINT fk_iuv_1 FOREIGN KEY (id_applicazione) REFERENCES applicazioni(id) ON DELETE CASCADE,
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



CREATE SEQUENCE seq_eventi MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE eventi
(
	data_ora_evento TIMESTAMP NOT NULL,
	cod_dominio VARCHAR(255),
	iuv VARCHAR(255),
	id_applicazione NUMBER,
	ccp VARCHAR(255),
	cod_psp VARCHAR(255),
	tipo_versamento VARCHAR(255),
	componente VARCHAR(255),
	categoria_evento VARCHAR(255),
	tipo_evento VARCHAR(255),
	sottotipo_evento VARCHAR(255),
	cod_fruitore VARCHAR(255),
	cod_erogatore VARCHAR(255),
	cod_stazione VARCHAR(255),
	canale_pagamento VARCHAR(255),
	altri_parametri VARCHAR(255),
	esito VARCHAR(255),
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



CREATE SEQUENCE seq_sla MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE sla
(
	descrizione VARCHAR(255) NOT NULL,
	tipo_evento_iniziale VARCHAR(255) NOT NULL,
	sottotipo_evento_iniziale VARCHAR(255) NOT NULL,
	tipo_evento_finale VARCHAR(255) NOT NULL,
	sottotipo_evento_finale VARCHAR(255) NOT NULL,
	tempo_a NUMBER NOT NULL,
	tempo_b NUMBER NOT NULL,
	tolleranza_a BINARY_DOUBLE NOT NULL,
	tolleranza_b BINARY_DOUBLE NOT NULL,
	-- fk/pk columns
	id NUMBER NOT NULL,
	-- fk/pk keys constraints
	CONSTRAINT pk_sla PRIMARY KEY (id)
);

CREATE TRIGGER trg_sla
BEFORE
insert on sla
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_sla.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;
/



CREATE SEQUENCE seq_rilevamenti MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE rilevamenti
(
	id_applicazione NUMBER,
	data_rilevamento DATE NOT NULL,
	durata NUMBER NOT NULL,
	-- fk/pk columns
	id NUMBER NOT NULL,
	id_sla NUMBER NOT NULL,
	id_evento_iniziale NUMBER NOT NULL,
	id_evento_finale NUMBER NOT NULL,
	-- fk/pk keys constraints
	CONSTRAINT fk_rilevamenti_1 FOREIGN KEY (id_sla) REFERENCES sla(id) ON DELETE CASCADE,
	CONSTRAINT fk_rilevamenti_2 FOREIGN KEY (id_evento_iniziale) REFERENCES eventi(id) ON DELETE CASCADE,
	CONSTRAINT fk_rilevamenti_3 FOREIGN KEY (id_evento_finale) REFERENCES eventi(id) ON DELETE CASCADE,
	CONSTRAINT pk_rilevamenti PRIMARY KEY (id)
);

CREATE TRIGGER trg_rilevamenti
BEFORE
insert on rilevamenti
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_rilevamenti.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;
/



CREATE SEQUENCE seq_medie_rilevamenti MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE medie_rilevamenti
(
	id_applicazione NUMBER,
	data_osservazione DATE NOT NULL,
	num_rilevamenti_a NUMBER NOT NULL,
	percentuale_a BINARY_DOUBLE NOT NULL,
	num_rilevamenti_b NUMBER NOT NULL,
	percentuale_b BINARY_DOUBLE NOT NULL,
	num_rilevamenti_over NUMBER NOT NULL,
	-- fk/pk columns
	id NUMBER NOT NULL,
	id_sla NUMBER NOT NULL,
	-- unique constraints
	CONSTRAINT unique_medie_rilevamenti_1 UNIQUE (id_sla,data_osservazione),
	-- fk/pk keys constraints
	CONSTRAINT fk_medie_rilevamenti_1 FOREIGN KEY (id_sla) REFERENCES sla(id) ON DELETE CASCADE,
	CONSTRAINT pk_medie_rilevamenti PRIMARY KEY (id)
);

CREATE TRIGGER trg_medie_rilevamenti
BEFORE
insert on medie_rilevamenti
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_medie_rilevamenti.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;
/



CREATE SEQUENCE seq_rr MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE rr
(
	cod_msg_revoca VARCHAR(255) NOT NULL,
	data_ora_msg_revoca TIMESTAMP NOT NULL,
	importo_totale_revocato BINARY_DOUBLE NOT NULL,
	stato VARCHAR(255) NOT NULL,
	descrizione_stato VARCHAR(255),
	data_ora_creazione TIMESTAMP NOT NULL,
	-- fk/pk columns
	id NUMBER NOT NULL,
	id_rt NUMBER NOT NULL,
	id_tracciato_xml NUMBER NOT NULL,
	-- unique constraints
	CONSTRAINT unique_rr_1 UNIQUE (cod_msg_revoca),
	-- fk/pk keys constraints
	CONSTRAINT fk_rr_1 FOREIGN KEY (id_rt) REFERENCES rt(id) ON DELETE CASCADE,
	CONSTRAINT fk_rr_2 FOREIGN KEY (id_tracciato_xml) REFERENCES tracciatixml(id) ON DELETE CASCADE,
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



CREATE SEQUENCE seq_er MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE er
(
	cod_msg_esito VARCHAR(255) NOT NULL,
	data_ora_msg_esito TIMESTAMP NOT NULL,
	importo_totale_revocato BINARY_DOUBLE NOT NULL,
	stato VARCHAR(255) NOT NULL,
	descrizione_stato VARCHAR(255),
	data_ora_creazione TIMESTAMP NOT NULL,
	-- fk/pk columns
	id NUMBER NOT NULL,
	id_rr NUMBER NOT NULL,
	id_tracciato_xml NUMBER NOT NULL,
	-- unique constraints
	CONSTRAINT unique_er_1 UNIQUE (cod_msg_esito),
	-- fk/pk keys constraints
	CONSTRAINT fk_er_1 FOREIGN KEY (id_rr) REFERENCES rr(id) ON DELETE CASCADE,
	CONSTRAINT fk_er_2 FOREIGN KEY (id_tracciato_xml) REFERENCES tracciatixml(id) ON DELETE CASCADE,
	CONSTRAINT pk_er PRIMARY KEY (id)
);

CREATE TRIGGER trg_er
BEFORE
insert on er
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_er.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;
/



CREATE SEQUENCE seq_singole_revoche MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE singole_revoche
(
	id_er NUMBER,
	causale_revoca VARCHAR(255) NOT NULL,
	dati_aggiuntivi_revoca VARCHAR(255) NOT NULL,
	singolo_importo BINARY_DOUBLE NOT NULL,
	singolo_importo_revocato BINARY_DOUBLE,
	causale_esito VARCHAR(255),
	dati_aggiuntivi_esito VARCHAR(255),
	stato VARCHAR(255) NOT NULL,
	descrizione_stato VARCHAR(255),
	-- fk/pk columns
	id NUMBER NOT NULL,
	id_rr NUMBER NOT NULL,
	id_singolo_versamento NUMBER NOT NULL,
	-- fk/pk keys constraints
	CONSTRAINT fk_singole_revoche_1 FOREIGN KEY (id_rr) REFERENCES rr(id) ON DELETE CASCADE,
	CONSTRAINT fk_singole_revoche_2 FOREIGN KEY (id_singolo_versamento) REFERENCES singoli_versamenti(id) ON DELETE CASCADE,
	CONSTRAINT pk_singole_revoche PRIMARY KEY (id)
);

CREATE TRIGGER trg_singole_revoche
BEFORE
insert on singole_revoche
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_singole_revoche.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;
/



CREATE SEQUENCE seq_fr MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE fr
(
	cod_flusso VARCHAR(255) NOT NULL,
	anno_riferimento NUMBER NOT NULL,
	data_ora_flusso TIMESTAMP NOT NULL,
	iur VARCHAR(255) NOT NULL,
	data_regolamento DATE NOT NULL,
	numero_pagamenti NUMBER NOT NULL,
	importo_totale_pagamenti BINARY_DOUBLE NOT NULL,
	stato VARCHAR(255) NOT NULL,
	descrizione_stato CLOB,
	-- fk/pk columns
	id NUMBER NOT NULL,
	id_tracciato_xml NUMBER NOT NULL,
	id_psp NUMBER NOT NULL,
	id_dominio NUMBER NOT NULL,
	-- unique constraints
	CONSTRAINT unique_fr_1 UNIQUE (cod_flusso,anno_riferimento),
	-- fk/pk keys constraints
	CONSTRAINT fk_fr_1 FOREIGN KEY (id_tracciato_xml) REFERENCES tracciatixml(id) ON DELETE CASCADE,
	CONSTRAINT fk_fr_2 FOREIGN KEY (id_psp) REFERENCES psp(id) ON DELETE CASCADE,
	CONSTRAINT fk_fr_3 FOREIGN KEY (id_dominio) REFERENCES domini(id) ON DELETE CASCADE,
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



CREATE SEQUENCE seq_singole_rendicontazioni MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE singole_rendicontazioni
(
	iuv VARCHAR(255) NOT NULL,
	iur VARCHAR(255) NOT NULL,
	singolo_importo BINARY_DOUBLE NOT NULL,
	codice_esito NUMBER NOT NULL,
	data_esito DATE NOT NULL,
	-- fk/pk columns
	id NUMBER NOT NULL,
	id_fr NUMBER NOT NULL,
	id_singolo_versamento NUMBER,
	-- fk/pk keys constraints
	CONSTRAINT fk_singole_rendicontazioni_1 FOREIGN KEY (id_fr) REFERENCES fr(id) ON DELETE CASCADE,
	CONSTRAINT fk_singole_rendicontazioni_2 FOREIGN KEY (id_singolo_versamento) REFERENCES singoli_versamenti(id) ON DELETE CASCADE,
	CONSTRAINT pk_singole_rendicontazioni PRIMARY KEY (id)
);

CREATE TRIGGER trg_singole_rendicontazioni
BEFORE
insert on singole_rendicontazioni
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_singole_rendicontazioni.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;
/



CREATE SEQUENCE seq_mail MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE mail
(
	tipo_mail VARCHAR(255) NOT NULL,
	bundle_key NUMBER NOT NULL,
	id_versamento NUMBER,
	mittente VARCHAR(255) NOT NULL,
	destinatario VARCHAR(255) NOT NULL,
	cc CLOB,
	oggetto CLOB NOT NULL,
	messaggio CLOB NOT NULL,
	stato_spedizione VARCHAR(255) NOT NULL,
	dettaglio_errore_spedizione VARCHAR(255),
	data_ora_ultima_spedizione TIMESTAMP,
	tentativi_rispedizione NUMBER,
	-- fk/pk columns
	id NUMBER NOT NULL,
	id_tracciato_rpt NUMBER,
	id_tracciato_rt NUMBER,
	-- fk/pk keys constraints
	CONSTRAINT fk_mail_1 FOREIGN KEY (id_tracciato_rpt) REFERENCES tracciatixml(id) ON DELETE CASCADE,
	CONSTRAINT fk_mail_2 FOREIGN KEY (id_tracciato_rt) REFERENCES tracciatixml(id) ON DELETE CASCADE,
	CONSTRAINT pk_mail PRIMARY KEY (id)
);

CREATE TRIGGER trg_mail
BEFORE
insert on mail
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_mail.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;
/



CREATE SEQUENCE seq_tabella_controparti MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE tabella_controparti
(
	id_flusso VARCHAR(255) NOT NULL,
	data_ora_pubblicazione TIMESTAMP NOT NULL,
	data_ora_inizio_validita TIMESTAMP NOT NULL,
	xml BLOB NOT NULL,
	-- fk/pk columns
	id NUMBER NOT NULL,
	id_dominio NUMBER NOT NULL,
	-- unique constraints
	CONSTRAINT unique_tabella_controparti_1 UNIQUE (id_flusso,id_dominio),
	-- fk/pk keys constraints
	CONSTRAINT fk_tabella_controparti_1 FOREIGN KEY (id_dominio) REFERENCES domini(id) ON DELETE CASCADE,
	CONSTRAINT pk_tabella_controparti PRIMARY KEY (id)
);

CREATE TRIGGER trg_tabella_controparti
BEFORE
insert on tabella_controparti
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_tabella_controparti.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;
/



CREATE SEQUENCE seq_conti_accredito MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE conti_accredito
(
	id_flusso VARCHAR(255) NOT NULL,
	data_ora_pubblicazione TIMESTAMP NOT NULL,
	data_ora_inizio_validita TIMESTAMP NOT NULL,
	xml BLOB NOT NULL,
	-- fk/pk columns
	id NUMBER NOT NULL,
	id_dominio NUMBER NOT NULL,
	-- unique constraints
	CONSTRAINT unique_conti_accredito_1 UNIQUE (id_flusso,id_dominio),
	-- fk/pk keys constraints
	CONSTRAINT fk_conti_accredito_1 FOREIGN KEY (id_dominio) REFERENCES domini(id) ON DELETE CASCADE,
	CONSTRAINT pk_conti_accredito PRIMARY KEY (id)
);

CREATE TRIGGER trg_conti_accredito
BEFORE
insert on conti_accredito
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_conti_accredito.nextval INTO :new.id
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

