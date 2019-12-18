CREATE SEQUENCE seq_configurazione start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE configurazione
(
	nome VARCHAR(255) NOT NULL,
	valore TEXT,
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_configurazione') NOT NULL,
	-- unique constraints
	CONSTRAINT unique_configurazione_1 UNIQUE (nome),
	-- fk/pk keys constraints
	CONSTRAINT pk_configurazione PRIMARY KEY (id)
);




CREATE SEQUENCE seq_intermediari start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE intermediari
(
	cod_intermediario VARCHAR(35) NOT NULL,
	cod_connettore_pdd VARCHAR(35) NOT NULL,
	cod_connettore_ftp VARCHAR(35),
	denominazione VARCHAR(255) NOT NULL,
	principal VARCHAR(4000) NOT NULL,
	principal_originale VARCHAR(4000) NOT NULL,
	abilitato BOOLEAN NOT NULL,
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_intermediari') NOT NULL,
	-- unique constraints
	CONSTRAINT unique_intermediari_1 UNIQUE (cod_intermediario),
	-- fk/pk keys constraints
	CONSTRAINT pk_intermediari PRIMARY KEY (id)
);




CREATE SEQUENCE seq_stazioni start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE stazioni
(
	cod_stazione VARCHAR(35) NOT NULL,
	password VARCHAR(35) NOT NULL,
	abilitato BOOLEAN NOT NULL,
	application_code INT NOT NULL,
	ndp_stato INT,
	ndp_operazione VARCHAR(256),
	ndp_descrizione VARCHAR(1024),
	ndp_data TIMESTAMP,
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_stazioni') NOT NULL,
	id_intermediario BIGINT NOT NULL,
	-- unique constraints
	CONSTRAINT unique_stazioni_1 UNIQUE (cod_stazione),
	-- fk/pk keys constraints
	CONSTRAINT fk_stz_id_intermediario FOREIGN KEY (id_intermediario) REFERENCES intermediari(id),
	CONSTRAINT pk_stazioni PRIMARY KEY (id)
);




CREATE SEQUENCE seq_utenze start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE utenze
(
	principal VARCHAR(4000) NOT NULL,
	principal_originale VARCHAR(4000) NOT NULL,
	abilitato BOOLEAN NOT NULL DEFAULT true,
	autorizzazione_domini_star BOOLEAN NOT NULL DEFAULT false,
	autorizzazione_tipi_vers_star BOOLEAN NOT NULL DEFAULT false,
	ruoli VARCHAR(512),
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_utenze') NOT NULL,
	-- unique constraints
	CONSTRAINT unique_utenze_1 UNIQUE (principal),
	-- fk/pk keys constraints
	CONSTRAINT pk_utenze PRIMARY KEY (id)
);




CREATE SEQUENCE seq_applicazioni start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE applicazioni
(
	cod_applicazione VARCHAR(35) NOT NULL,
	auto_iuv BOOLEAN NOT NULL,
	firma_ricevuta VARCHAR(1) NOT NULL,
	cod_connettore_integrazione VARCHAR(255),
	trusted BOOLEAN NOT NULL,
	cod_applicazione_iuv VARCHAR(3),
	reg_exp VARCHAR(1024),
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_applicazioni') NOT NULL,
	id_utenza BIGINT NOT NULL,
	-- unique constraints
	CONSTRAINT unique_applicazioni_1 UNIQUE (cod_applicazione),
	CONSTRAINT unique_applicazioni_2 UNIQUE (id_utenza),
	-- fk/pk keys constraints
	CONSTRAINT fk_app_id_utenza FOREIGN KEY (id_utenza) REFERENCES utenze(id),
	CONSTRAINT pk_applicazioni PRIMARY KEY (id)
);




CREATE SEQUENCE seq_domini start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE domini
(
	cod_dominio VARCHAR(35) NOT NULL,
	gln VARCHAR(35),
	abilitato BOOLEAN NOT NULL,
	ragione_sociale VARCHAR(70) NOT NULL,
	aux_digit INT NOT NULL DEFAULT 0,
	iuv_prefix VARCHAR(255),
	segregation_code INT,
	ndp_stato INT,
	ndp_operazione VARCHAR(256),
	ndp_descrizione VARCHAR(1024),
	ndp_data TIMESTAMP,
	logo BYTEA,
	cbill VARCHAR(255),
	aut_stampa_poste VARCHAR(255),
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_domini') NOT NULL,
	id_stazione BIGINT NOT NULL,
	id_applicazione_default BIGINT,
	-- unique constraints
	CONSTRAINT unique_domini_1 UNIQUE (cod_dominio),
	-- fk/pk keys constraints
	CONSTRAINT fk_dom_id_stazione FOREIGN KEY (id_stazione) REFERENCES stazioni(id),
	CONSTRAINT fk_dom_id_applicazione_default FOREIGN KEY (id_applicazione_default) REFERENCES applicazioni(id),
	CONSTRAINT pk_domini PRIMARY KEY (id)
);




CREATE SEQUENCE seq_iban_accredito start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE iban_accredito
(
	cod_iban VARCHAR(255) NOT NULL,
	bic_accredito VARCHAR(255),
	postale BOOLEAN NOT NULL,
	attivato BOOLEAN NOT NULL,
	abilitato BOOLEAN NOT NULL,
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_iban_accredito') NOT NULL,
	id_dominio BIGINT NOT NULL,
	-- unique constraints
	CONSTRAINT unique_iban_accredito_1 UNIQUE (cod_iban,id_dominio),
	-- fk/pk keys constraints
	CONSTRAINT fk_iba_id_dominio FOREIGN KEY (id_dominio) REFERENCES domini(id),
	CONSTRAINT pk_iban_accredito PRIMARY KEY (id)
);




CREATE SEQUENCE seq_tipi_tributo start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE tipi_tributo
(
	cod_tributo VARCHAR(255) NOT NULL,
	descrizione VARCHAR(255),
	tipo_contabilita VARCHAR(1),
	cod_contabilita VARCHAR(255),
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_tipi_tributo') NOT NULL,
	-- unique constraints
	CONSTRAINT unique_tipi_tributo_1 UNIQUE (cod_tributo),
	-- fk/pk keys constraints
	CONSTRAINT pk_tipi_tributo PRIMARY KEY (id)
);




CREATE SEQUENCE seq_tributi start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE tributi
(
	abilitato BOOLEAN NOT NULL,
	tipo_contabilita VARCHAR(1),
	codice_contabilita VARCHAR(255),
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_tributi') NOT NULL,
	id_dominio BIGINT NOT NULL,
	id_iban_accredito BIGINT,
	id_iban_appoggio BIGINT,
	id_tipo_tributo BIGINT NOT NULL,
	-- unique constraints
	CONSTRAINT unique_tributi_1 UNIQUE (id_dominio,id_tipo_tributo),
	-- fk/pk keys constraints
	CONSTRAINT fk_trb_id_dominio FOREIGN KEY (id_dominio) REFERENCES domini(id),
	CONSTRAINT fk_trb_id_iban_accredito FOREIGN KEY (id_iban_accredito) REFERENCES iban_accredito(id),
	CONSTRAINT fk_trb_id_iban_appoggio FOREIGN KEY (id_iban_appoggio) REFERENCES iban_accredito(id),
	CONSTRAINT fk_trb_id_tipo_tributo FOREIGN KEY (id_tipo_tributo) REFERENCES tipi_tributo(id),
	CONSTRAINT pk_tributi PRIMARY KEY (id)
);




CREATE SEQUENCE seq_uo start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE uo
(
	cod_uo VARCHAR(35) NOT NULL,
	abilitato BOOLEAN NOT NULL,
	uo_codice_identificativo VARCHAR(35),
	uo_denominazione VARCHAR(70),
	uo_indirizzo VARCHAR(70),
	uo_civico VARCHAR(16),
	uo_cap VARCHAR(16),
	uo_localita VARCHAR(35),
	uo_provincia VARCHAR(35),
	uo_nazione VARCHAR(2),
	uo_area VARCHAR(255),
	uo_url_sito_web VARCHAR(255),
	uo_email VARCHAR(255),
	uo_pec VARCHAR(255),
	uo_tel VARCHAR(255),
	uo_fax VARCHAR(255),
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_uo') NOT NULL,
	id_dominio BIGINT NOT NULL,
	-- unique constraints
	CONSTRAINT unique_uo_1 UNIQUE (cod_uo,id_dominio),
	-- fk/pk keys constraints
	CONSTRAINT fk_uo_id_dominio FOREIGN KEY (id_dominio) REFERENCES domini(id),
	CONSTRAINT pk_uo PRIMARY KEY (id)
);




CREATE SEQUENCE seq_utenze_domini start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE utenze_domini
(
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_utenze_domini') NOT NULL,
	id_utenza BIGINT NOT NULL,
	id_dominio BIGINT,
	id_uo BIGINT,
	-- fk/pk keys constraints
	CONSTRAINT fk_nzd_id_utenza FOREIGN KEY (id_utenza) REFERENCES utenze(id),
	CONSTRAINT fk_nzd_id_dominio FOREIGN KEY (id_dominio) REFERENCES domini(id),
	CONSTRAINT fk_nzd_id_uo FOREIGN KEY (id_uo) REFERENCES uo(id),
	CONSTRAINT pk_utenze_domini PRIMARY KEY (id)
);




CREATE SEQUENCE seq_operatori start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE operatori
(
	nome VARCHAR(35) NOT NULL,
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_operatori') NOT NULL,
	id_utenza BIGINT NOT NULL,
	-- unique constraints
	CONSTRAINT unique_operatori_1 UNIQUE (id_utenza),
	-- fk/pk keys constraints
	CONSTRAINT fk_opr_id_utenza FOREIGN KEY (id_utenza) REFERENCES utenze(id),
	CONSTRAINT pk_operatori PRIMARY KEY (id)
);




CREATE SEQUENCE seq_connettori start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE connettori
(
	cod_connettore VARCHAR(255) NOT NULL,
	cod_proprieta VARCHAR(255) NOT NULL,
	valore VARCHAR(255) NOT NULL,
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_connettori') NOT NULL,
	-- unique constraints
	CONSTRAINT unique_connettori_1 UNIQUE (cod_connettore,cod_proprieta),
	-- fk/pk keys constraints
	CONSTRAINT pk_connettori PRIMARY KEY (id)
);




CREATE SEQUENCE seq_acl start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE acl
(
	ruolo VARCHAR(255),
	servizio VARCHAR(255) NOT NULL,
	diritti VARCHAR(255) NOT NULL,
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_acl') NOT NULL,
	id_utenza BIGINT,
	-- fk/pk keys constraints
	CONSTRAINT fk_acl_id_utenza FOREIGN KEY (id_utenza) REFERENCES utenze(id),
	CONSTRAINT pk_acl PRIMARY KEY (id)
);




CREATE SEQUENCE seq_tracciati start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE tracciati
(
	cod_dominio VARCHAR(35) NOT NULL,
	cod_tipo_versamento VARCHAR(35),
	formato VARCHAR(10) NOT NULL,
	tipo VARCHAR(10) NOT NULL,
	stato VARCHAR(12) NOT NULL,
	descrizione_stato VARCHAR(256),
	data_caricamento TIMESTAMP NOT NULL,
	data_completamento TIMESTAMP,
	bean_dati TEXT,
	file_name_richiesta VARCHAR(256),
	raw_richiesta BYTEA,
	file_name_esito VARCHAR(256),
	raw_esito BYTEA,
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_tracciati') NOT NULL,
	id_operatore BIGINT,
	-- fk/pk keys constraints
	CONSTRAINT fk_trc_id_operatore FOREIGN KEY (id_operatore) REFERENCES operatori(id),
	CONSTRAINT pk_tracciati PRIMARY KEY (id)
);




CREATE SEQUENCE seq_tipi_versamento start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE tipi_versamento
(
	cod_tipo_versamento VARCHAR(35) NOT NULL,
	descrizione VARCHAR(255) NOT NULL,
	codifica_iuv VARCHAR(4),
	tipo VARCHAR(35) NOT NULL,
	paga_terzi BOOLEAN NOT NULL DEFAULT false,
	abilitato BOOLEAN NOT NULL,
	form_tipo VARCHAR(35),
	form_definizione TEXT,
	validazione_definizione TEXT,
	trasformazione_tipo VARCHAR(35),
	trasformazione_definizione TEXT,
	cod_applicazione VARCHAR(35),
	promemoria_avviso_abilitato BOOLEAN NOT NULL DEFAULT false,
	promemoria_avviso_pdf BOOLEAN,
	promemoria_avviso_tipo VARCHAR(35),
	promemoria_avviso_oggetto TEXT,
	promemoria_avviso_messaggio TEXT,
	promemoria_ricevuta_abilitato BOOLEAN NOT NULL DEFAULT false,
	promemoria_ricevuta_tipo VARCHAR(35),
	promemoria_ricevuta_pdf BOOLEAN,
	promemoria_ricevuta_oggetto TEXT,
	promemoria_ricevuta_messaggio TEXT,
	visualizzazione_definizione TEXT,
	trac_csv_tipo VARCHAR(35),
	trac_csv_header_risposta TEXT,
	trac_csv_template_richiesta TEXT,
	trac_csv_template_risposta TEXT,
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_tipi_versamento') NOT NULL,
	-- unique constraints
	CONSTRAINT unique_tipi_versamento_1 UNIQUE (cod_tipo_versamento),
	-- fk/pk keys constraints
	CONSTRAINT pk_tipi_versamento PRIMARY KEY (id)
);




CREATE SEQUENCE seq_tipi_vers_domini start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE tipi_vers_domini
(
	codifica_iuv VARCHAR(4),
	tipo VARCHAR(35),
	paga_terzi BOOLEAN,
	abilitato BOOLEAN,
	form_tipo VARCHAR(35),
	form_definizione TEXT,
	validazione_definizione TEXT,
	trasformazione_tipo VARCHAR(35),
	trasformazione_definizione TEXT,
	cod_applicazione VARCHAR(35),
	promemoria_avviso_abilitato BOOLEAN,
	promemoria_avviso_tipo VARCHAR(35),
	promemoria_avviso_pdf BOOLEAN,
	promemoria_avviso_oggetto TEXT,
	promemoria_avviso_messaggio TEXT,
	promemoria_ricevuta_abilitato BOOLEAN,
	promemoria_ricevuta_tipo VARCHAR(35),
	promemoria_ricevuta_pdf BOOLEAN,
	promemoria_ricevuta_oggetto TEXT,
	promemoria_ricevuta_messaggio TEXT,
	visualizzazione_definizione TEXT,
	trac_csv_tipo VARCHAR(35),
	trac_csv_header_risposta TEXT,
	trac_csv_template_richiesta TEXT,
	trac_csv_template_risposta TEXT,
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_tipi_vers_domini') NOT NULL,
	id_tipo_versamento BIGINT NOT NULL,
	id_dominio BIGINT NOT NULL,
	-- unique constraints
	CONSTRAINT unique_tipi_vers_domini_1 UNIQUE (id_dominio,id_tipo_versamento),
	-- fk/pk keys constraints
	CONSTRAINT fk_tvd_id_tipo_versamento FOREIGN KEY (id_tipo_versamento) REFERENCES tipi_versamento(id),
	CONSTRAINT fk_tvd_id_dominio FOREIGN KEY (id_dominio) REFERENCES domini(id),
	CONSTRAINT pk_tipi_vers_domini PRIMARY KEY (id)
);




CREATE SEQUENCE seq_utenze_tipo_vers start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE utenze_tipo_vers
(
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_utenze_tipo_vers') NOT NULL,
	id_utenza BIGINT NOT NULL,
	id_tipo_versamento BIGINT NOT NULL,
	-- fk/pk keys constraints
	CONSTRAINT fk_utv_id_utenza FOREIGN KEY (id_utenza) REFERENCES utenze(id),
	CONSTRAINT fk_utv_id_tipo_versamento FOREIGN KEY (id_tipo_versamento) REFERENCES tipi_versamento(id),
	CONSTRAINT pk_utenze_tipo_vers PRIMARY KEY (id)
);




CREATE SEQUENCE seq_versamenti start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE versamenti
(
	cod_versamento_ente VARCHAR(35) NOT NULL,
	nome VARCHAR(35),
	importo_totale DOUBLE PRECISION NOT NULL,
	stato_versamento VARCHAR(35) NOT NULL,
	descrizione_stato VARCHAR(255),
	-- Indica se, decorsa la dataScadenza, deve essere aggiornato da remoto o essere considerato scaduto
	aggiornabile BOOLEAN NOT NULL,
	data_creazione TIMESTAMP NOT NULL,
	data_validita TIMESTAMP,
	data_scadenza TIMESTAMP,
	data_ora_ultimo_aggiornamento TIMESTAMP NOT NULL,
	causale_versamento VARCHAR(1024),
	debitore_tipo VARCHAR(1),
	debitore_identificativo VARCHAR(35) NOT NULL,
	debitore_anagrafica VARCHAR(70) NOT NULL,
	debitore_indirizzo VARCHAR(70),
	debitore_civico VARCHAR(16),
	debitore_cap VARCHAR(16),
	debitore_localita VARCHAR(35),
	debitore_provincia VARCHAR(35),
	debitore_nazione VARCHAR(2),
	debitore_email VARCHAR(256),
	debitore_telefono VARCHAR(35),
	debitore_cellulare VARCHAR(35),
	debitore_fax VARCHAR(35),
	tassonomia_avviso VARCHAR(35),
	tassonomia VARCHAR(35),
	cod_lotto VARCHAR(35),
	cod_versamento_lotto VARCHAR(35),
	cod_anno_tributario VARCHAR(35),
	cod_bundlekey VARCHAR(256),
	dati_allegati TEXT,
	incasso VARCHAR(1),
	anomalie TEXT,
	iuv_versamento VARCHAR(35),
	numero_avviso VARCHAR(35),
	avvisatura_abilitata BOOLEAN NOT NULL,
	avvisatura_da_inviare BOOLEAN NOT NULL,
	avvisatura_operazione VARCHAR(1),
	avvisatura_modalita VARCHAR(1),
	avvisatura_tipo_pagamento INT,
	avvisatura_cod_avvisatura VARCHAR(20),
	ack BOOLEAN NOT NULL,
	anomalo BOOLEAN NOT NULL,
	divisione VARCHAR(35),
	direzione VARCHAR(35),
	id_sessione VARCHAR(35),
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_versamenti') NOT NULL,
	id_tipo_versamento_dominio BIGINT NOT NULL,
	id_tipo_versamento BIGINT NOT NULL,
	id_dominio BIGINT NOT NULL,
	id_uo BIGINT,
	id_applicazione BIGINT NOT NULL,
	id_tracciato BIGINT,
	-- unique constraints
	CONSTRAINT unique_versamenti_1 UNIQUE (cod_versamento_ente,id_applicazione),
	-- fk/pk keys constraints
	CONSTRAINT fk_vrs_id_tipo_versamento_dominio FOREIGN KEY (id_tipo_versamento_dominio) REFERENCES tipi_vers_domini(id),
	CONSTRAINT fk_vrs_id_tipo_versamento FOREIGN KEY (id_tipo_versamento) REFERENCES tipi_versamento(id),
	CONSTRAINT fk_vrs_id_dominio FOREIGN KEY (id_dominio) REFERENCES domini(id),
	CONSTRAINT fk_vrs_id_uo FOREIGN KEY (id_uo) REFERENCES uo(id),
	CONSTRAINT fk_vrs_id_applicazione FOREIGN KEY (id_applicazione) REFERENCES applicazioni(id),
	CONSTRAINT fk_vrs_id_tracciato FOREIGN KEY (id_tracciato) REFERENCES tracciati(id),
	CONSTRAINT pk_versamenti PRIMARY KEY (id)
);




CREATE SEQUENCE seq_singoli_versamenti start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE singoli_versamenti
(
	cod_singolo_versamento_ente VARCHAR(70) NOT NULL,
	stato_singolo_versamento VARCHAR(35) NOT NULL,
	importo_singolo_versamento DOUBLE PRECISION NOT NULL,
	anno_riferimento INT,
	-- MARCA BOLLO Valori possibili:\n01: Imposta di bollo
	tipo_bollo VARCHAR(2),
	-- MARCA BOLLO: Digest in Base64 del documento da bollare
	hash_documento VARCHAR(70),
	-- MARCA BOLLO: Sigla automobilistica della provincia di residenza
	provincia_residenza VARCHAR(2),
	tipo_contabilita VARCHAR(1),
	codice_contabilita VARCHAR(255),
	descrizione VARCHAR(256),
	dati_allegati TEXT,
	indice_dati INT NOT NULL,
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_singoli_versamenti') NOT NULL,
	id_versamento BIGINT NOT NULL,
	id_tributo BIGINT,
	id_iban_accredito BIGINT,
	id_iban_appoggio BIGINT,
	-- unique constraints
	CONSTRAINT unique_singoli_versamenti_1 UNIQUE (id_versamento,cod_singolo_versamento_ente,indice_dati),
	-- fk/pk keys constraints
	CONSTRAINT fk_sng_id_versamento FOREIGN KEY (id_versamento) REFERENCES versamenti(id),
	CONSTRAINT fk_sng_id_tributo FOREIGN KEY (id_tributo) REFERENCES tributi(id),
	CONSTRAINT fk_sng_id_iban_accredito FOREIGN KEY (id_iban_accredito) REFERENCES iban_accredito(id),
	CONSTRAINT fk_sng_id_iban_appoggio FOREIGN KEY (id_iban_appoggio) REFERENCES iban_accredito(id),
	CONSTRAINT pk_singoli_versamenti PRIMARY KEY (id)
);




CREATE SEQUENCE seq_pagamenti_portale start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE pagamenti_portale
(
	cod_canale VARCHAR(35),
	nome VARCHAR(255) NOT NULL,
	importo DOUBLE PRECISION NOT NULL,
	versante_identificativo VARCHAR(35),
	id_sessione VARCHAR(35) NOT NULL,
	id_sessione_portale VARCHAR(255),
	id_sessione_psp VARCHAR(255),
	stato VARCHAR(35) NOT NULL,
	codice_stato VARCHAR(35) NOT NULL,
	descrizione_stato VARCHAR(1024),
	psp_redirect_url VARCHAR(1024),
	psp_esito VARCHAR(255),
	json_request TEXT,
	wisp_id_dominio VARCHAR(255),
	wisp_key_pa VARCHAR(255),
	wisp_key_wisp VARCHAR(255),
	wisp_html TEXT,
	data_richiesta TIMESTAMP,
	url_ritorno VARCHAR(1024),
	cod_psp VARCHAR(35),
	tipo_versamento VARCHAR(4),
	multi_beneficiario VARCHAR(35),
	ack BOOLEAN NOT NULL,
	tipo INT NOT NULL,
	principal VARCHAR(4000) NOT NULL,
	tipo_utenza VARCHAR(35) NOT NULL,
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_pagamenti_portale') NOT NULL,
	id_applicazione BIGINT,
	-- unique constraints
	CONSTRAINT unique_pagamenti_portale_1 UNIQUE (id_sessione),
	-- fk/pk keys constraints
	CONSTRAINT fk_ppt_id_applicazione FOREIGN KEY (id_applicazione) REFERENCES applicazioni(id),
	CONSTRAINT pk_pagamenti_portale PRIMARY KEY (id)
);




CREATE SEQUENCE seq_pag_port_versamenti start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE pag_port_versamenti
(
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_pag_port_versamenti') NOT NULL,
	id_pagamento_portale BIGINT NOT NULL,
	id_versamento BIGINT NOT NULL,
	-- fk/pk keys constraints
	CONSTRAINT fk_ppv_id_pagamento_portale FOREIGN KEY (id_pagamento_portale) REFERENCES pagamenti_portale(id),
	CONSTRAINT fk_ppv_id_versamento FOREIGN KEY (id_versamento) REFERENCES versamenti(id),
	CONSTRAINT pk_pag_port_versamenti PRIMARY KEY (id)
);




CREATE SEQUENCE seq_rpt start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

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
	descrizione_stato TEXT,
	cod_sessione VARCHAR(255),
	cod_sessione_portale VARCHAR(255),
	-- Indirizzo del portale psp a cui redirigere il cittadino per eseguire il pagamento
	psp_redirect_url VARCHAR(512),
	xml_rpt BYTEA NOT NULL,
	data_aggiornamento_stato TIMESTAMP NOT NULL,
	-- Indirizzo di ritorno al portale dell'ente al termine del pagamento
	callback_url TEXT,
	modello_pagamento VARCHAR(16),
	cod_msg_ricevuta VARCHAR(35),
	data_msg_ricevuta TIMESTAMP,
	-- Esito del pagamento:\n0: Eseguito\n1: Non eseguito\n2: Parzialmente eseguito\n3: Decorrenza\n4: Decorrenza Parziale
	cod_esito_pagamento INT,
	importo_totale_pagato DOUBLE PRECISION,
	xml_rt BYTEA,
	cod_canale VARCHAR(35),
	cod_psp VARCHAR(35),
	cod_intermediario_psp VARCHAR(35),
	tipo_versamento VARCHAR(4),
	tipo_identificativo_attestante VARCHAR(1),
	identificativo_attestante VARCHAR(35),
	denominazione_attestante VARCHAR(70),
	cod_stazione VARCHAR(35) NOT NULL,
	cod_transazione_rpt VARCHAR(36),
	cod_transazione_rt VARCHAR(36),
	stato_conservazione VARCHAR(35),
	descrizione_stato_cons VARCHAR(512),
	data_conservazione TIMESTAMP,
	bloccante BOOLEAN NOT NULL DEFAULT true,
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_rpt') NOT NULL,
	id_versamento BIGINT NOT NULL,
	id_pagamento_portale BIGINT,
	-- unique constraints
	CONSTRAINT unique_rpt_1 UNIQUE (cod_msg_richiesta),
	CONSTRAINT unique_rpt_2 UNIQUE (iuv,ccp,cod_dominio),
	-- fk/pk keys constraints
	CONSTRAINT fk_rpt_id_versamento FOREIGN KEY (id_versamento) REFERENCES versamenti(id),
	CONSTRAINT fk_rpt_id_pagamento_portale FOREIGN KEY (id_pagamento_portale) REFERENCES pagamenti_portale(id),
	CONSTRAINT pk_rpt PRIMARY KEY (id)
);

-- index
CREATE INDEX index_rpt_1 ON rpt (stato);
CREATE INDEX index_rpt_2 ON rpt (id_versamento);



CREATE SEQUENCE seq_rr start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

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
	importo_totale_richiesto DOUBLE PRECISION NOT NULL,
	cod_msg_esito VARCHAR(35),
	importo_totale_revocato DOUBLE PRECISION,
	xml_rr BYTEA NOT NULL,
	xml_er BYTEA,
	cod_transazione_rr VARCHAR(36),
	cod_transazione_er VARCHAR(36),
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_rr') NOT NULL,
	id_rpt BIGINT NOT NULL,
	-- unique constraints
	CONSTRAINT unique_rr_1 UNIQUE (cod_msg_revoca),
	-- fk/pk keys constraints
	CONSTRAINT fk_rr_id_rpt FOREIGN KEY (id_rpt) REFERENCES rpt(id),
	CONSTRAINT pk_rr PRIMARY KEY (id)
);




CREATE SEQUENCE seq_notifiche start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE notifiche
(
	tipo_esito VARCHAR(16) NOT NULL,
	data_creazione TIMESTAMP NOT NULL,
	stato VARCHAR(16) NOT NULL,
	descrizione_stato VARCHAR(255),
	data_aggiornamento_stato TIMESTAMP NOT NULL,
	data_prossima_spedizione TIMESTAMP NOT NULL,
	tentativi_spedizione BIGINT,
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_notifiche') NOT NULL,
	id_applicazione BIGINT NOT NULL,
	id_rpt BIGINT,
	id_rr BIGINT,
	-- fk/pk keys constraints
	CONSTRAINT fk_ntf_id_applicazione FOREIGN KEY (id_applicazione) REFERENCES applicazioni(id),
	CONSTRAINT fk_ntf_id_rpt FOREIGN KEY (id_rpt) REFERENCES rpt(id),
	CONSTRAINT fk_ntf_id_rr FOREIGN KEY (id_rr) REFERENCES rr(id),
	CONSTRAINT pk_notifiche PRIMARY KEY (id)
);




CREATE SEQUENCE seq_promemoria start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE promemoria
(
	tipo VARCHAR(16) NOT NULL,
	data_creazione TIMESTAMP NOT NULL,
	stato VARCHAR(16) NOT NULL,
	descrizione_stato VARCHAR(1024),
	destinatario_to VARCHAR(256) NOT NULL,
	destinatario_cc VARCHAR(256),
	messaggio_content_type VARCHAR(256),
	oggetto VARCHAR(512),
	messaggio TEXT,
	allega_pdf BOOLEAN NOT NULL DEFAULT false,
	data_aggiornamento_stato TIMESTAMP NOT NULL,
	data_prossima_spedizione TIMESTAMP NOT NULL,
	tentativi_spedizione BIGINT,
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_promemoria') NOT NULL,
	id_versamento BIGINT NOT NULL,
	id_rpt BIGINT,
	-- fk/pk keys constraints
	CONSTRAINT fk_prm_id_versamento FOREIGN KEY (id_versamento) REFERENCES versamenti(id),
	CONSTRAINT fk_prm_id_rpt FOREIGN KEY (id_rpt) REFERENCES rpt(id),
	CONSTRAINT pk_promemoria PRIMARY KEY (id)
);




CREATE SEQUENCE seq_iuv start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE iuv
(
	prg BIGINT NOT NULL,
	iuv VARCHAR(35) NOT NULL,
	application_code INT NOT NULL,
	data_generazione DATE NOT NULL,
	tipo_iuv VARCHAR(1) NOT NULL,
	cod_versamento_ente VARCHAR(35),
	aux_digit INT NOT NULL DEFAULT 0,
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_iuv') NOT NULL,
	id_applicazione BIGINT NOT NULL,
	id_dominio BIGINT NOT NULL,
	-- unique constraints
	CONSTRAINT unique_iuv_1 UNIQUE (id_dominio,iuv),
	-- fk/pk keys constraints
	CONSTRAINT fk_iuv_id_applicazione FOREIGN KEY (id_applicazione) REFERENCES applicazioni(id),
	CONSTRAINT fk_iuv_id_dominio FOREIGN KEY (id_dominio) REFERENCES domini(id),
	CONSTRAINT pk_iuv PRIMARY KEY (id)
);

-- index
CREATE INDEX index_iuv_1 ON iuv (cod_versamento_ente,tipo_iuv,id_applicazione);



CREATE SEQUENCE seq_incassi start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE incassi
(
	trn VARCHAR(35) NOT NULL,
	cod_dominio VARCHAR(35) NOT NULL,
	causale VARCHAR(512) NOT NULL,
	importo DOUBLE PRECISION NOT NULL,
	data_valuta DATE,
	data_contabile DATE,
	data_ora_incasso TIMESTAMP NOT NULL,
	nome_dispositivo VARCHAR(512),
	iban_accredito VARCHAR(35),
	sct VARCHAR(35),
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_incassi') NOT NULL,
	id_applicazione BIGINT,
	id_operatore BIGINT,
	-- unique constraints
	CONSTRAINT unique_incassi_1 UNIQUE (cod_dominio,trn),
	-- fk/pk keys constraints
	CONSTRAINT fk_inc_id_applicazione FOREIGN KEY (id_applicazione) REFERENCES applicazioni(id),
	CONSTRAINT fk_inc_id_operatore FOREIGN KEY (id_operatore) REFERENCES operatori(id),
	CONSTRAINT pk_incassi PRIMARY KEY (id)
);




CREATE SEQUENCE seq_fr start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE fr
(
	cod_psp VARCHAR(35) NOT NULL,
	cod_dominio VARCHAR(35) NOT NULL,
	cod_flusso VARCHAR(35) NOT NULL,
	stato VARCHAR(35) NOT NULL,
	descrizione_stato TEXT,
	iur VARCHAR(35) NOT NULL,
	data_ora_flusso TIMESTAMP,
	data_regolamento TIMESTAMP,
	data_acquisizione TIMESTAMP NOT NULL,
	numero_pagamenti BIGINT,
	importo_totale_pagamenti DOUBLE PRECISION,
	cod_bic_riversamento VARCHAR(35),
	xml BYTEA NOT NULL,
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_fr') NOT NULL,
	id_incasso BIGINT,
	-- unique constraints
	CONSTRAINT unique_fr_1 UNIQUE (cod_flusso),
	-- fk/pk keys constraints
	CONSTRAINT fk_fr_id_incasso FOREIGN KEY (id_incasso) REFERENCES incassi(id),
	CONSTRAINT pk_fr PRIMARY KEY (id)
);




CREATE SEQUENCE seq_pagamenti start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE pagamenti
(
	cod_dominio VARCHAR(35) NOT NULL,
	iuv VARCHAR(35) NOT NULL,
	indice_dati INT NOT NULL DEFAULT 1,
	importo_pagato DOUBLE PRECISION NOT NULL,
	data_acquisizione TIMESTAMP NOT NULL,
	iur VARCHAR(35) NOT NULL,
	data_pagamento TIMESTAMP NOT NULL,
	commissioni_psp DOUBLE PRECISION,
	-- Valori possibili:\nES: Esito originario\nBD: Marca da Bollo
	tipo_allegato VARCHAR(2),
	allegato BYTEA,
	data_acquisizione_revoca TIMESTAMP,
	causale_revoca VARCHAR(140),
	dati_revoca VARCHAR(140),
	importo_revocato DOUBLE PRECISION,
	esito_revoca VARCHAR(140),
	dati_esito_revoca VARCHAR(140),
	stato VARCHAR(35),
	tipo VARCHAR(35) NOT NULL,
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_pagamenti') NOT NULL,
	id_rpt BIGINT,
	id_singolo_versamento BIGINT,
	id_rr BIGINT,
	id_incasso BIGINT,
	-- unique constraints
	CONSTRAINT unique_pagamenti_1 UNIQUE (cod_dominio,iuv,iur,indice_dati),
	-- fk/pk keys constraints
	CONSTRAINT fk_pag_id_rpt FOREIGN KEY (id_rpt) REFERENCES rpt(id),
	CONSTRAINT fk_pag_id_singolo_versamento FOREIGN KEY (id_singolo_versamento) REFERENCES singoli_versamenti(id),
	CONSTRAINT fk_pag_id_rr FOREIGN KEY (id_rr) REFERENCES rr(id),
	CONSTRAINT fk_pag_id_incasso FOREIGN KEY (id_incasso) REFERENCES incassi(id),
	CONSTRAINT pk_pagamenti PRIMARY KEY (id)
);




CREATE SEQUENCE seq_rendicontazioni start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE rendicontazioni
(
	iuv VARCHAR(35) NOT NULL,
	iur VARCHAR(35) NOT NULL,
	indice_dati INT,
	importo_pagato DOUBLE PRECISION,
	esito INT,
	data TIMESTAMP,
	stato VARCHAR(35) NOT NULL,
	anomalie TEXT,
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_rendicontazioni') NOT NULL,
	id_fr BIGINT NOT NULL,
	id_pagamento BIGINT,
	id_singolo_versamento BIGINT,
	-- fk/pk keys constraints
	CONSTRAINT fk_rnd_id_fr FOREIGN KEY (id_fr) REFERENCES fr(id),
	CONSTRAINT fk_rnd_id_pagamento FOREIGN KEY (id_pagamento) REFERENCES pagamenti(id),
	CONSTRAINT fk_rnd_id_singolo_versamento FOREIGN KEY (id_singolo_versamento) REFERENCES singoli_versamenti(id),
	CONSTRAINT pk_rendicontazioni PRIMARY KEY (id)
);




CREATE SEQUENCE seq_eventi start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE eventi
(
	componente VARCHAR(35),
	ruolo VARCHAR(1),
	categoria_evento VARCHAR(1),
	tipo_evento VARCHAR(70),
	sottotipo_evento VARCHAR(35),
	data TIMESTAMP,
	intervallo BIGINT,
	esito VARCHAR(4),
	sottotipo_esito VARCHAR(35),
	dettaglio_esito TEXT,
	parametri_richiesta BYTEA,
	parametri_risposta BYTEA,
	dati_pago_pa TEXT,
	cod_versamento_ente VARCHAR(35),
	cod_applicazione VARCHAR(35),
	iuv VARCHAR(35),
	ccp VARCHAR(35),
	cod_dominio VARCHAR(35),
	id_sessione VARCHAR(35),
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_eventi') NOT NULL,
	id_fr BIGINT,
	id_incasso BIGINT,
	id_tracciato BIGINT,
	-- fk/pk keys constraints
	CONSTRAINT fk_evt_id_fr FOREIGN KEY (id_fr) REFERENCES fr(id),
	CONSTRAINT fk_evt_id_incasso FOREIGN KEY (id_incasso) REFERENCES incassi(id),
	CONSTRAINT fk_evt_id_tracciato FOREIGN KEY (id_tracciato) REFERENCES tracciati(id),
	CONSTRAINT pk_eventi PRIMARY KEY (id)
);




CREATE SEQUENCE seq_batch start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE batch
(
	cod_batch VARCHAR(255) NOT NULL,
	nodo VARCHAR(255),
	inizio TIMESTAMP,
	aggiornamento TIMESTAMP,
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_batch') NOT NULL,
	-- unique constraints
	CONSTRAINT unique_batch_1 UNIQUE (cod_batch),
	-- fk/pk keys constraints
	CONSTRAINT pk_batch PRIMARY KEY (id)
);




CREATE SEQUENCE seq_esiti_avvisatura start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE esiti_avvisatura
(
	cod_dominio VARCHAR(35) NOT NULL,
	identificativo_avvisatura VARCHAR(20) NOT NULL,
	tipo_canale INT NOT NULL,
	cod_canale VARCHAR(35),
	data TIMESTAMP NOT NULL,
	cod_esito INT NOT NULL,
	descrizione_esito VARCHAR(140) NOT NULL,
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_esiti_avvisatura') NOT NULL,
	id_tracciato BIGINT NOT NULL,
	-- fk/pk keys constraints
	CONSTRAINT fk_sta_id_tracciato FOREIGN KEY (id_tracciato) REFERENCES tracciati(id),
	CONSTRAINT pk_esiti_avvisatura PRIMARY KEY (id)
);




CREATE SEQUENCE seq_operazioni start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE operazioni
(
	tipo_operazione VARCHAR(16) NOT NULL,
	linea_elaborazione BIGINT NOT NULL,
	stato VARCHAR(16) NOT NULL,
	dati_richiesta BYTEA NOT NULL,
	dati_risposta BYTEA,
	dettaglio_esito VARCHAR(255),
	cod_versamento_ente VARCHAR(255),
	cod_dominio VARCHAR(35),
	iuv VARCHAR(35),
	trn VARCHAR(35),
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_operazioni') NOT NULL,
	id_tracciato BIGINT NOT NULL,
	id_applicazione BIGINT,
	-- fk/pk keys constraints
	CONSTRAINT fk_ope_id_tracciato FOREIGN KEY (id_tracciato) REFERENCES tracciati(id),
	CONSTRAINT fk_ope_id_applicazione FOREIGN KEY (id_applicazione) REFERENCES applicazioni(id),
	CONSTRAINT pk_operazioni PRIMARY KEY (id)
);




CREATE SEQUENCE seq_gp_audit start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE gp_audit
(
	data TIMESTAMP NOT NULL,
	id_oggetto BIGINT NOT NULL,
	tipo_oggetto VARCHAR(255) NOT NULL,
	oggetto TEXT NOT NULL,
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_gp_audit') NOT NULL,
	id_operatore BIGINT NOT NULL,
	-- fk/pk keys constraints
	CONSTRAINT fk_aud_id_operatore FOREIGN KEY (id_operatore) REFERENCES operatori(id),
	CONSTRAINT pk_gp_audit PRIMARY KEY (id)
);




CREATE SEQUENCE seq_stampe start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE stampe
(
	data_creazione TIMESTAMP NOT NULL,
	tipo VARCHAR(16) NOT NULL,
	pdf BYTEA,
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_stampe') NOT NULL,
	id_versamento BIGINT NOT NULL,
	-- unique constraints
	CONSTRAINT unique_stampe_1 UNIQUE (id_versamento,tipo),
	-- fk/pk keys constraints
	CONSTRAINT fk_stm_id_versamento FOREIGN KEY (id_versamento) REFERENCES versamenti(id),
	CONSTRAINT pk_stampe PRIMARY KEY (id)
);




CREATE TABLE ID_MESSAGGIO_RELATIVO
(
	COUNTER BIGINT NOT NULL,
	PROTOCOLLO VARCHAR(255) NOT NULL,
	INFO_ASSOCIATA VARCHAR(255) NOT NULL,
	ora_registrazione TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	-- fk/pk columns
	-- fk/pk keys constraints
	CONSTRAINT pk_ID_MESSAGGIO_RELATIVO PRIMARY KEY (PROTOCOLLO,INFO_ASSOCIATA)
);

CREATE TABLE sonde
(
	nome VARCHAR(35) NOT NULL,
	classe VARCHAR(255) NOT NULL,
	soglia_warn BIGINT NOT NULL,
	soglia_error BIGINT NOT NULL,
	data_ok TIMESTAMP,
	data_warn TIMESTAMP,
	data_error TIMESTAMP,
	data_ultimo_check TIMESTAMP,
	dati_check TEXT,
	stato_ultimo_check INT,
	-- fk/pk columns
	-- fk/pk keys constraints
	CONSTRAINT pk_sonde PRIMARY KEY (nome)
);

-- Sezione Viste

CREATE VIEW versamenti_incassi AS SELECT versamenti.id,
    max(versamenti.cod_versamento_ente::text) AS cod_versamento_ente,
    max(versamenti.nome::text) AS nome,
    max(versamenti.importo_totale) AS importo_totale,
    versamenti.stato_versamento::text AS stato_versamento,
    max(versamenti.descrizione_stato::text) AS descrizione_stato,
    max(
        CASE
            WHEN versamenti.aggiornabile = true THEN 'TRUE'::text
            ELSE 'FALSE'::text
        END) AS aggiornabile,
    max(versamenti.data_creazione) AS data_creazione,
    max(versamenti.data_validita) AS data_validita,
    max(versamenti.data_scadenza) AS data_scadenza,
    max(versamenti.data_ora_ultimo_aggiornamento) AS data_ora_ultimo_aggiornamento,
    max(versamenti.causale_versamento::text) AS causale_versamento,
    max(versamenti.debitore_tipo::text) AS debitore_tipo,
    versamenti.debitore_identificativo AS debitore_identificativo,
    max(versamenti.debitore_anagrafica::text) AS debitore_anagrafica,
    max(versamenti.debitore_indirizzo::text) AS debitore_indirizzo,
    max(versamenti.debitore_civico::text) AS debitore_civico,
    max(versamenti.debitore_cap::text) AS debitore_cap,
    max(versamenti.debitore_localita::text) AS debitore_localita,
    max(versamenti.debitore_provincia::text) AS debitore_provincia,
    max(versamenti.debitore_nazione::text) AS debitore_nazione,
    max(versamenti.debitore_email::text) AS debitore_email,
    max(versamenti.debitore_telefono::text) AS debitore_telefono,
    max(versamenti.debitore_cellulare::text) AS debitore_cellulare,
    max(versamenti.debitore_fax::text) AS debitore_fax,
    max(versamenti.tassonomia_avviso::text) AS tassonomia_avviso,
    max(versamenti.tassonomia::text) AS tassonomia,
    max(versamenti.cod_lotto::text) AS cod_lotto,
    max(versamenti.cod_versamento_lotto::text) AS cod_versamento_lotto,
    max(versamenti.cod_anno_tributario::text) AS cod_anno_tributario,
    max(versamenti.cod_bundlekey::text) AS cod_bundlekey,
    max(versamenti.dati_allegati) AS dati_allegati,
    max(versamenti.incasso::text) AS incasso,
    max(versamenti.anomalie) AS anomalie,
    max(versamenti.iuv_versamento::text) AS iuv_versamento,
    max(versamenti.numero_avviso::text) AS numero_avviso,
    max(versamenti.id_dominio) AS id_dominio,
    max(versamenti.id_tipo_versamento) AS id_tipo_versamento,
    max(versamenti.id_tipo_versamento_dominio) AS id_tipo_versamento_dominio,
    max(versamenti.id_uo) AS id_uo,
    max(versamenti.id_applicazione) AS id_applicazione,
    MAX(CASE WHEN versamenti.avvisatura_abilitata = TRUE THEN 'TRUE' ELSE 'FALSE' END) AS avvisatura_abilitata,
    MAX(CASE WHEN versamenti.avvisatura_da_inviare = TRUE THEN 'TRUE' ELSE 'FALSE' END) AS avvisatura_da_inviare,
    MAX(versamenti.avvisatura_operazione) as avvisatura_operazione,
    MAX(versamenti.avvisatura_modalita) as avvisatura_modalita,
    MAX(versamenti.avvisatura_tipo_pagamento) as avvisatura_tipo_pagamento,
    MAX(versamenti.avvisatura_cod_avvisatura) as avvisatura_cod_avvisatura,
    MAX(versamenti.divisione) as divisione,
    MAX(versamenti.direzione) as direzione,	
    MAX(versamenti.id_tracciato) as id_tracciato,
    MAX(versamenti.id_sessione) as id_sessione,
    max(
        CASE
            WHEN versamenti.ack = true THEN 'TRUE'::text
            ELSE 'FALSE'::text
        END) AS ack,
    max(
        CASE
            WHEN versamenti.anomalo = true THEN 'TRUE'::text
            ELSE 'FALSE'::text
        END) AS anomalo,
    max(pagamenti.data_pagamento) AS data_pagamento,
    sum(
        CASE
            WHEN pagamenti.importo_pagato IS NOT NULL THEN pagamenti.importo_pagato
            ELSE 0::double precision
        END) AS importo_pagato,
    sum(
        CASE
            WHEN pagamenti.stato::text = 'INCASSATO'::text THEN pagamenti.importo_pagato
            ELSE 0::double precision
        END) AS importo_incassato,
    max(
        CASE
            WHEN pagamenti.stato IS NULL THEN 'NON_PAGATO'::text
            WHEN pagamenti.stato::text = 'INCASSATO'::text THEN 'INCASSATO'::text
            ELSE 'PAGATO'::text
        END) AS stato_pagamento,
    max(pagamenti.iuv::text) AS iuv_pagamento,
    max(
        CASE
            WHEN versamenti.stato_versamento::text = 'NON_ESEGUITO'::text AND versamenti.data_validita > now() THEN 0
            ELSE 1
        END) AS smart_order_rank,
    min(@ (date_part('epoch'::text, now()) * 1000::bigint - date_part('epoch'::text, COALESCE(pagamenti.data_pagamento, versamenti.data_validita, versamenti.data_creazione)) * 1000::bigint))::bigint AS smart_order_date
   FROM versamenti
     LEFT JOIN singoli_versamenti ON versamenti.id = singoli_versamenti.id_versamento
     LEFT JOIN pagamenti ON singoli_versamenti.id = pagamenti.id_singolo_versamento
     JOIN tipi_versamento ON tipi_versamento.id = versamenti.id_tipo_versamento
  GROUP BY versamenti.id, versamenti.debitore_identificativo, versamenti.stato_versamento;

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

CREATE VIEW v_pag_portale_base AS
 SELECT DISTINCT
  pagamenti_portale.id,
  versamenti.debitore_identificativo as debitore_identificativo,
  versamenti.id_dominio as id_dominio, 
  versamenti.id_uo as id_uo, 
  versamenti.id_tipo_versamento as id_tipo_versamento
FROM pagamenti_portale 
JOIN pag_port_versamenti ON pagamenti_portale.id = pag_port_versamenti.id_pagamento_portale 
JOIN versamenti ON versamenti.id=pag_port_versamenti.id_versamento;

CREATE VIEW v_pagamenti_portale_ext AS
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
  pagamenti_portale.json_request,
  pagamenti_portale.wisp_id_dominio,
  pagamenti_portale.wisp_key_pa,
  pagamenti_portale.wisp_key_wisp,
  pagamenti_portale.wisp_html,
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
  v_pag_portale_base.debitore_identificativo,
  v_pag_portale_base.id_dominio, 
  v_pag_portale_base.id_uo, 
  v_pag_portale_base.id_tipo_versamento 
FROM v_pag_portale_base JOIN pagamenti_portale ON v_pag_portale_base.id = pagamenti_portale.id;

-- Vista Eventi per Versamenti

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
               versamenti.cod_versamento_ente as cod_versamento_ente,
               applicazioni.cod_applicazione as cod_applicazione,
               eventi.iuv,
               eventi.cod_dominio,
               eventi.ccp,
               eventi.id_sessione,
               eventi.id
        FROM eventi LEFT JOIN pagamenti_portale ON eventi.id_sessione = pagamenti_portale.id_sessione
        JOIN pag_port_versamenti ON pagamenti_portale.id = pag_port_versamenti.id_pagamento_portale
        JOIN versamenti ON versamenti.id = pag_port_versamenti.id_versamento
        JOIN applicazioni ON versamenti.id_applicazione = applicazioni.id
);

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
               eventi.id FROM eventi 
        UNION SELECT * FROM v_eventi_vers_pagamenti 
        UNION SELECT * FROM v_eventi_vers_rendicontazioni
        UNION SELECT * FROM v_eventi_vers_riconciliazioni
	UNION SELECT * FROM v_eventi_vers_tracciati
);

