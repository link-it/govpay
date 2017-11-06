CREATE SEQUENCE seq_psp start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE psp
(
	cod_psp VARCHAR(35) NOT NULL,
	ragione_sociale VARCHAR(70) NOT NULL,
	url_info VARCHAR(255),
	abilitato BOOLEAN NOT NULL,
	storno BOOLEAN NOT NULL,
	marca_bollo BOOLEAN NOT NULL,
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_psp') NOT NULL,
	-- unique constraints
	CONSTRAINT unique_psp_1 UNIQUE (cod_psp),
	-- fk/pk keys constraints
	CONSTRAINT pk_psp PRIMARY KEY (id)
);




CREATE SEQUENCE seq_canali start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE canali
(
	cod_canale VARCHAR(35) NOT NULL,
	cod_intermediario VARCHAR(35) NOT NULL,
	tipo_versamento VARCHAR(4) NOT NULL,
	modello_pagamento INT NOT NULL,
	disponibilita TEXT,
	descrizione TEXT,
	condizioni VARCHAR(35),
	url_info VARCHAR(255),
	abilitato BOOLEAN NOT NULL,
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_canali') NOT NULL,
	id_psp BIGINT NOT NULL,
	-- unique constraints
	CONSTRAINT unique_canali_1 UNIQUE (id_psp,cod_canale,tipo_versamento),
	-- fk/pk keys constraints
	CONSTRAINT fk_can_id_psp FOREIGN KEY (id_psp) REFERENCES psp(id),
	CONSTRAINT pk_canali PRIMARY KEY (id)
);




CREATE SEQUENCE seq_intermediari start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE intermediari
(
	cod_intermediario VARCHAR(35) NOT NULL,
	cod_connettore_pdd VARCHAR(35) NOT NULL,
	denominazione VARCHAR(255) NOT NULL,
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




CREATE SEQUENCE seq_applicazioni start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE applicazioni
(
	cod_applicazione VARCHAR(35) NOT NULL,
	abilitato BOOLEAN NOT NULL,
	principal VARCHAR(255) NOT NULL,
	firma_ricevuta VARCHAR(1) NOT NULL,
	cod_connettore_esito VARCHAR(255),
	cod_connettore_verifica VARCHAR(255),
	versione VARCHAR(10) NOT NULL DEFAULT '2.1',
	trusted BOOLEAN NOT NULL,
	cod_applicazione_iuv VARCHAR(3),
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_applicazioni') NOT NULL,
	-- unique constraints
	CONSTRAINT unique_applicazioni_1 UNIQUE (cod_applicazione),
	CONSTRAINT unique_applicazioni_2 UNIQUE (principal),
	-- fk/pk keys constraints
	CONSTRAINT pk_applicazioni PRIMARY KEY (id)
);




CREATE SEQUENCE seq_domini start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE domini
(
	cod_dominio VARCHAR(35) NOT NULL,
	gln VARCHAR(35) NOT NULL,
	abilitato BOOLEAN NOT NULL,
	ragione_sociale VARCHAR(70) NOT NULL,
	xml_conti_accredito BYTEA NOT NULL,
	xml_tabella_controparti BYTEA NOT NULL,
	riuso_iuv BOOLEAN NOT NULL,
	custom_iuv BOOLEAN NOT NULL,
	aux_digit INT NOT NULL DEFAULT 0,
	iuv_prefix VARCHAR(255),
	iuv_prefix_strict BOOLEAN NOT NULL DEFAULT false,
	segregation_code INT,
	ndp_stato INT,
	ndp_operazione VARCHAR(256),
	ndp_descrizione VARCHAR(1024),
	ndp_data TIMESTAMP,
	logo BYTEA,
	cbill VARCHAR(255),
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
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_uo') NOT NULL,
	id_dominio BIGINT NOT NULL,
	-- unique constraints
	CONSTRAINT unique_uo_1 UNIQUE (cod_uo,id_dominio),
	-- fk/pk keys constraints
	CONSTRAINT fk_uo_id_dominio FOREIGN KEY (id_dominio) REFERENCES domini(id),
	CONSTRAINT pk_uo PRIMARY KEY (id)
);




CREATE SEQUENCE seq_operatori start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE operatori
(
	principal VARCHAR(255) NOT NULL,
	nome VARCHAR(35) NOT NULL,
	profilo VARCHAR(1024) NOT NULL,
	abilitato BOOLEAN NOT NULL DEFAULT true,
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_operatori') NOT NULL,
	-- unique constraints
	CONSTRAINT unique_operatori_1 UNIQUE (principal),
	-- fk/pk keys constraints
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




CREATE SEQUENCE seq_portali start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE portali
(
	cod_portale VARCHAR(35) NOT NULL,
	default_callback_url VARCHAR(512) NOT NULL,
	principal VARCHAR(255) NOT NULL,
	versione VARCHAR(10) NOT NULL DEFAULT '2.1',
	trusted BOOLEAN NOT NULL,
	abilitato BOOLEAN NOT NULL,
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_portali') NOT NULL,
	-- unique constraints
	CONSTRAINT unique_portali_1 UNIQUE (cod_portale),
	CONSTRAINT unique_portali_2 UNIQUE (principal),
	-- fk/pk keys constraints
	CONSTRAINT pk_portali PRIMARY KEY (id)
);




CREATE SEQUENCE seq_iban_accredito start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE iban_accredito
(
	cod_iban VARCHAR(255) NOT NULL,
	id_seller_bank VARCHAR(255),
	id_negozio VARCHAR(255),
	bic_accredito VARCHAR(255),
	iban_appoggio VARCHAR(255),
	bic_appoggio VARCHAR(255),
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
	cod_tributo_iuv VARCHAR(4),
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
	cod_tributo_iuv VARCHAR(4),
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_tributi') NOT NULL,
	id_dominio BIGINT NOT NULL,
	id_iban_accredito BIGINT,
	id_tipo_tributo BIGINT NOT NULL,
	-- unique constraints
	CONSTRAINT unique_tributi_1 UNIQUE (id_dominio,id_tipo_tributo),
	-- fk/pk keys constraints
	CONSTRAINT fk_trb_id_dominio FOREIGN KEY (id_dominio) REFERENCES domini(id),
	CONSTRAINT fk_trb_id_iban_accredito FOREIGN KEY (id_iban_accredito) REFERENCES iban_accredito(id),
	CONSTRAINT fk_trb_id_tipo_tributo FOREIGN KEY (id_tipo_tributo) REFERENCES tipi_tributo(id),
	CONSTRAINT pk_tributi PRIMARY KEY (id)
);




CREATE SEQUENCE seq_ruoli start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE ruoli
(
	cod_ruolo VARCHAR(35) NOT NULL,
	descrizione VARCHAR(255) NOT NULL,
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_ruoli') NOT NULL,
	-- unique constraints
	CONSTRAINT unique_ruoli_1 UNIQUE (cod_ruolo),
	-- fk/pk keys constraints
	CONSTRAINT pk_ruoli PRIMARY KEY (id)
);




CREATE SEQUENCE seq_acl start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE acl
(
	cod_tipo VARCHAR(1) NOT NULL,
	diritti INT,
	cod_servizio VARCHAR(35) NOT NULL,
	amministratore BOOLEAN NOT NULL DEFAULT false,
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_acl') NOT NULL,
	id_applicazione BIGINT,
	id_portale BIGINT,
	id_operatore BIGINT,
	id_ruolo BIGINT,
	id_dominio BIGINT,
	id_tipo_tributo BIGINT,
	-- fk/pk keys constraints
	CONSTRAINT fk_acl_id_applicazione FOREIGN KEY (id_applicazione) REFERENCES applicazioni(id),
	CONSTRAINT fk_acl_id_portale FOREIGN KEY (id_portale) REFERENCES portali(id),
	CONSTRAINT fk_acl_id_operatore FOREIGN KEY (id_operatore) REFERENCES operatori(id),
	CONSTRAINT fk_acl_id_ruolo FOREIGN KEY (id_ruolo) REFERENCES ruoli(id),
	CONSTRAINT fk_acl_id_dominio FOREIGN KEY (id_dominio) REFERENCES domini(id),
	CONSTRAINT fk_acl_id_tipo_tributo FOREIGN KEY (id_tipo_tributo) REFERENCES tipi_tributo(id),
	CONSTRAINT pk_acl PRIMARY KEY (id)
);




CREATE SEQUENCE seq_versamenti start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE versamenti
(
	cod_versamento_ente VARCHAR(35) NOT NULL,
	importo_totale DOUBLE PRECISION NOT NULL,
	stato_versamento VARCHAR(35) NOT NULL,
	descrizione_stato VARCHAR(255),
	-- Indica se, decorsa la dataScadenza, deve essere aggiornato da remoto o essere considerato scaduto
	aggiornabile BOOLEAN NOT NULL,
	data_creazione TIMESTAMP NOT NULL,
	data_scadenza TIMESTAMP,
	data_ora_ultimo_aggiornamento TIMESTAMP NOT NULL,
	causale_versamento VARCHAR(1024),
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
	cod_lotto VARCHAR(35),
	cod_versamento_lotto VARCHAR(35),
	cod_anno_tributario VARCHAR(35),
	cod_bundlekey VARCHAR(256),
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_versamenti') NOT NULL,
	id_uo BIGINT NOT NULL,
	id_applicazione BIGINT NOT NULL,
	-- unique constraints
	CONSTRAINT unique_versamenti_1 UNIQUE (cod_versamento_ente,id_applicazione),
	-- fk/pk keys constraints
	CONSTRAINT fk_vrs_id_uo FOREIGN KEY (id_uo) REFERENCES uo(id),
	CONSTRAINT fk_vrs_id_applicazione FOREIGN KEY (id_applicazione) REFERENCES applicazioni(id),
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
	note VARCHAR(512),
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_singoli_versamenti') NOT NULL,
	id_versamento BIGINT NOT NULL,
	id_tributo BIGINT,
	id_iban_accredito BIGINT,
	-- unique constraints
	CONSTRAINT unique_singoli_versamenti_1 UNIQUE (id_versamento,cod_singolo_versamento_ente),
	-- fk/pk keys constraints
	CONSTRAINT fk_sng_id_versamento FOREIGN KEY (id_versamento) REFERENCES versamenti(id),
	CONSTRAINT fk_sng_id_tributo FOREIGN KEY (id_tributo) REFERENCES tributi(id),
	CONSTRAINT fk_sng_id_iban_accredito FOREIGN KEY (id_iban_accredito) REFERENCES iban_accredito(id),
	CONSTRAINT pk_singoli_versamenti PRIMARY KEY (id)
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
	modello_pagamento VARCHAR(16) NOT NULL,
	cod_msg_ricevuta VARCHAR(35),
	data_msg_ricevuta TIMESTAMP,
	firma_ricevuta VARCHAR(1) NOT NULL,
	-- Esito del pagamento:\n0: Eseguito\n1: Non eseguito\n2: Parzialmente eseguito\n3: Decorrenza\n4: Decorrenza Parziale
	cod_esito_pagamento INT,
	importo_totale_pagato DOUBLE PRECISION,
	xml_rt BYTEA,
	cod_stazione VARCHAR(35) NOT NULL,
	cod_transazione_rpt VARCHAR(36),
	cod_transazione_rt VARCHAR(36),
	stato_conservazione VARCHAR(35),
	descrizione_stato_cons VARCHAR(512),
	data_conservazione TIMESTAMP,
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_rpt') NOT NULL,
	id_versamento BIGINT NOT NULL,
	id_canale BIGINT NOT NULL,
	id_portale BIGINT,
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
	-- unique constraints
	CONSTRAINT unique_fr_1 UNIQUE (cod_flusso),
	-- fk/pk keys constraints
	CONSTRAINT pk_fr PRIMARY KEY (id)
);




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
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_incassi') NOT NULL,
	id_applicazione BIGINT,
	-- fk/pk keys constraints
	CONSTRAINT fk_inc_id_applicazione FOREIGN KEY (id_applicazione) REFERENCES applicazioni(id),
	CONSTRAINT pk_incassi PRIMARY KEY (id)
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
	iban_accredito VARCHAR(255),
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
	-- fk/pk keys constraints
	CONSTRAINT fk_rnd_id_fr FOREIGN KEY (id_fr) REFERENCES fr(id),
	CONSTRAINT fk_rnd_id_pagamento FOREIGN KEY (id_pagamento) REFERENCES pagamenti(id),
	CONSTRAINT pk_rendicontazioni PRIMARY KEY (id)
);




CREATE SEQUENCE seq_eventi start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

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
	id BIGINT DEFAULT nextval('seq_eventi') NOT NULL,
	-- fk/pk keys constraints
	CONSTRAINT pk_eventi PRIMARY KEY (id)
);




CREATE SEQUENCE seq_batch start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE batch
(
	cod_batch VARCHAR(255) NOT NULL,
	nodo INT,
	inizio TIMESTAMP,
	aggiornamento TIMESTAMP,
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_batch') NOT NULL,
	-- unique constraints
	CONSTRAINT unique_batch_1 UNIQUE (cod_batch),
	-- fk/pk keys constraints
	CONSTRAINT pk_batch PRIMARY KEY (id)
);




CREATE SEQUENCE seq_tracciati start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE tracciati
(
	data_caricamento TIMESTAMP NOT NULL,
	data_ultimo_aggiornamento TIMESTAMP NOT NULL,
	stato VARCHAR(255) NOT NULL,
	linea_elaborazione BIGINT NOT NULL,
	descrizione_stato VARCHAR(1024),
	num_linee_totali BIGINT NOT NULL,
	num_operazioni_ok BIGINT NOT NULL,
	num_operazioni_ko BIGINT NOT NULL,
	nome_file VARCHAR(255) NOT NULL,
	raw_data_richiesta BYTEA NOT NULL,
	raw_data_risposta BYTEA,
	tipo_tracciato VARCHAR(255) NOT NULL,
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_tracciati') NOT NULL,
	id_operatore BIGINT,
	id_applicazione BIGINT,
	-- fk/pk keys constraints
	CONSTRAINT fk_trc_id_operatore FOREIGN KEY (id_operatore) REFERENCES operatori(id),
	CONSTRAINT fk_trc_id_applicazione FOREIGN KEY (id_applicazione) REFERENCES applicazioni(id),
	CONSTRAINT pk_tracciati PRIMARY KEY (id)
);




CREATE SEQUENCE seq_operazioni start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE operazioni
(
	tipo_operazione VARCHAR(255) NOT NULL,
	linea_elaborazione BIGINT NOT NULL,
	stato VARCHAR(255) NOT NULL,
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




CREATE SEQUENCE seq_avvisi start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE avvisi
(
	cod_dominio VARCHAR(35) NOT NULL,
	iuv VARCHAR(35) NOT NULL,
	data_creazione TIMESTAMP NOT NULL,
	stato VARCHAR(255) NOT NULL,
	pdf BYTEA,
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_avvisi') NOT NULL,
	-- fk/pk keys constraints
	CONSTRAINT pk_avvisi PRIMARY KEY (id)
);

-- index
CREATE INDEX index_avvisi_1 ON avvisi (cod_dominio,iuv);
CREATE INDEX index_avvisi_2 ON avvisi (stato);



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

