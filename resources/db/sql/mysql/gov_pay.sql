CREATE TABLE intermediari
(
	cod_intermediario VARCHAR(35) NOT NULL,
	cod_connettore_pdd VARCHAR(35) NOT NULL,
	cod_connettore_ftp VARCHAR(35),
	denominazione VARCHAR(255) NOT NULL,
	abilitato BOOLEAN NOT NULL,
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT,
	-- unique constraints
	CONSTRAINT unique_intermediari_1 UNIQUE (cod_intermediario),
	-- fk/pk keys constraints
	CONSTRAINT pk_intermediari PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;

-- index
CREATE INDEX index_intermediari_1 ON intermediari (cod_intermediario);



CREATE TABLE stazioni
(
	cod_stazione VARCHAR(35) NOT NULL,
	password VARCHAR(35) NOT NULL,
	abilitato BOOLEAN NOT NULL,
	application_code INT NOT NULL,
	ndp_stato INT,
	ndp_operazione VARCHAR(256),
	ndp_descrizione VARCHAR(1024),
	ndp_data TIMESTAMP(3),
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT,
	id_intermediario BIGINT NOT NULL,
	-- unique constraints
	CONSTRAINT unique_stazioni_1 UNIQUE (cod_stazione),
	-- fk/pk keys constraints
	CONSTRAINT fk_stz_id_intermediario FOREIGN KEY (id_intermediario) REFERENCES intermediari(id),
	CONSTRAINT pk_stazioni PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;

-- index
CREATE INDEX index_stazioni_1 ON stazioni (cod_stazione);



CREATE TABLE utenze
(
	principal VARCHAR(756) NOT NULL,
	principal_originale VARCHAR(756) NOT NULL,
	abilitato BOOLEAN NOT NULL DEFAULT true,
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT,
	-- unique constraints
	CONSTRAINT unique_utenze_1 UNIQUE (principal),
	-- fk/pk keys constraints
	CONSTRAINT pk_utenze PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;

-- index
CREATE INDEX index_utenze_1 ON utenze (principal);



CREATE TABLE applicazioni
(
	cod_applicazione VARCHAR(35) NOT NULL,
	auto_iuv BOOLEAN NOT NULL,
	firma_ricevuta VARCHAR(1) NOT NULL,
	cod_connettore_esito VARCHAR(255),
	cod_connettore_verifica VARCHAR(255),
	trusted BOOLEAN NOT NULL,
	cod_applicazione_iuv VARCHAR(3),
	reg_exp VARCHAR(1024),
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT,
	id_utenza BIGINT NOT NULL,
	-- unique constraints
	CONSTRAINT unique_applicazioni_1 UNIQUE (cod_applicazione),
	CONSTRAINT unique_applicazioni_2 UNIQUE (id_utenza),
	-- fk/pk keys constraints
	CONSTRAINT fk_app_id_utenza FOREIGN KEY (id_utenza) REFERENCES utenze(id),
	CONSTRAINT pk_applicazioni PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;

-- index
CREATE INDEX index_applicazioni_1 ON applicazioni (cod_applicazione);
CREATE INDEX index_applicazioni_2 ON applicazioni (id_utenza);



CREATE TABLE domini
(
	cod_dominio VARCHAR(35) NOT NULL,
	gln VARCHAR(35) NOT NULL,
	abilitato BOOLEAN NOT NULL,
	ragione_sociale VARCHAR(70) NOT NULL,
	aux_digit INT NOT NULL DEFAULT 0,
	iuv_prefix VARCHAR(255),
	segregation_code INT,
	ndp_stato INT,
	ndp_operazione VARCHAR(256),
	ndp_descrizione VARCHAR(1024),
	ndp_data TIMESTAMP(3),
	logo MEDIUMBLOB,
	cbill VARCHAR(255),
	aut_stampa_poste VARCHAR(255),
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT,
	id_stazione BIGINT NOT NULL,
	id_applicazione_default BIGINT,
	-- unique constraints
	CONSTRAINT unique_domini_1 UNIQUE (cod_dominio),
	-- fk/pk keys constraints
	CONSTRAINT fk_dom_id_stazione FOREIGN KEY (id_stazione) REFERENCES stazioni(id),
	CONSTRAINT fk_dom_id_applicazione_default FOREIGN KEY (id_applicazione_default) REFERENCES applicazioni(id),
	CONSTRAINT pk_domini PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;

-- index
CREATE INDEX index_domini_1 ON domini (cod_dominio);



CREATE TABLE iban_accredito
(
	cod_iban VARCHAR(255) NOT NULL,
	bic_accredito VARCHAR(255),
	postale BOOLEAN NOT NULL,
	attivato BOOLEAN NOT NULL,
	abilitato BOOLEAN NOT NULL,
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT,
	id_dominio BIGINT NOT NULL,
	-- unique constraints
	CONSTRAINT unique_iban_accredito_1 UNIQUE (cod_iban,id_dominio),
	-- fk/pk keys constraints
	CONSTRAINT fk_iba_id_dominio FOREIGN KEY (id_dominio) REFERENCES domini(id),
	CONSTRAINT pk_iban_accredito PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;

-- index
CREATE INDEX index_iban_accredito_1 ON iban_accredito (cod_iban,id_dominio);



CREATE TABLE tipi_tributo
(
	cod_tributo VARCHAR(255) NOT NULL,
	descrizione VARCHAR(255),
	tipo_contabilita VARCHAR(1),
	cod_contabilita VARCHAR(255),
	cod_tributo_iuv VARCHAR(4),
	on_line BOOLEAN NOT NULL DEFAULT false,
	paga_terzi BOOLEAN NOT NULL DEFAULT false,
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT,
	-- unique constraints
	CONSTRAINT unique_tipi_tributo_1 UNIQUE (cod_tributo),
	-- fk/pk keys constraints
	CONSTRAINT pk_tipi_tributo PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;

-- index
CREATE INDEX index_tipi_tributo_1 ON tipi_tributo (cod_tributo);



CREATE TABLE tributi
(
	abilitato BOOLEAN NOT NULL,
	tipo_contabilita VARCHAR(1),
	codice_contabilita VARCHAR(255),
	cod_tributo_iuv VARCHAR(4),
	on_line BOOLEAN,
	paga_terzi BOOLEAN,
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT,
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
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;

-- index
CREATE INDEX index_tributi_1 ON tributi (id_dominio,id_tipo_tributo);



CREATE TABLE utenze_domini
(
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT,
	id_utenza BIGINT NOT NULL,
	id_dominio BIGINT NOT NULL,
	-- fk/pk keys constraints
	CONSTRAINT fk_nzd_id_utenza FOREIGN KEY (id_utenza) REFERENCES utenze(id),
	CONSTRAINT fk_nzd_id_dominio FOREIGN KEY (id_dominio) REFERENCES domini(id),
	CONSTRAINT pk_utenze_domini PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;




CREATE TABLE utenze_tributi
(
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT,
	id_utenza BIGINT NOT NULL,
	id_tributo BIGINT NOT NULL,
	-- fk/pk keys constraints
	CONSTRAINT fk_nzt_id_utenza FOREIGN KEY (id_utenza) REFERENCES utenze(id),
	CONSTRAINT fk_nzt_id_tributo FOREIGN KEY (id_tributo) REFERENCES tributi(id),
	CONSTRAINT pk_utenze_tributi PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;




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
	id BIGINT AUTO_INCREMENT,
	id_dominio BIGINT NOT NULL,
	-- unique constraints
	CONSTRAINT unique_uo_1 UNIQUE (cod_uo,id_dominio),
	-- fk/pk keys constraints
	CONSTRAINT fk_uo_id_dominio FOREIGN KEY (id_dominio) REFERENCES domini(id),
	CONSTRAINT pk_uo PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;

-- index
CREATE INDEX index_uo_1 ON uo (cod_uo,id_dominio);



CREATE TABLE operatori
(
	nome VARCHAR(35) NOT NULL,
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT,
	id_utenza BIGINT NOT NULL,
	-- unique constraints
	CONSTRAINT unique_operatori_1 UNIQUE (id_utenza),
	-- fk/pk keys constraints
	CONSTRAINT fk_opr_id_utenza FOREIGN KEY (id_utenza) REFERENCES utenze(id),
	CONSTRAINT pk_operatori PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;

-- index
CREATE INDEX index_operatori_1 ON operatori (id_utenza);



CREATE TABLE connettori
(
	cod_connettore VARCHAR(255) NOT NULL,
	cod_proprieta VARCHAR(255) NOT NULL,
	valore VARCHAR(255) NOT NULL,
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT,
	-- unique constraints
	CONSTRAINT unique_connettori_1 UNIQUE (cod_connettore,cod_proprieta),
	-- fk/pk keys constraints
	CONSTRAINT pk_connettori PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;

-- index
CREATE INDEX index_connettori_1 ON connettori (cod_connettore,cod_proprieta);



CREATE TABLE acl
(
	ruolo VARCHAR(255),
	principal VARCHAR(255),
	servizio VARCHAR(255) NOT NULL,
	diritti VARCHAR(255) NOT NULL,
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT,
	-- fk/pk keys constraints
	CONSTRAINT pk_acl PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;




CREATE TABLE tracciati
(
	cod_dominio VARCHAR(35) NOT NULL,
	tipo VARCHAR(10) NOT NULL,
	stato VARCHAR(12) NOT NULL,
	descrizione_stato VARCHAR(256),
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
	data_caricamento TIMESTAMP(3) NOT NULL DEFAULT 0,
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
	data_completamento TIMESTAMP(3) DEFAULT 0,
	bean_dati LONGTEXT,
	file_name_richiesta VARCHAR(256),
	raw_richiesta MEDIUMBLOB,
	file_name_esito VARCHAR(256),
	raw_esito MEDIUMBLOB,
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT,
	id_operatore BIGINT,
	-- fk/pk keys constraints
	CONSTRAINT fk_trc_id_operatore FOREIGN KEY (id_operatore) REFERENCES operatori(id),
	CONSTRAINT pk_tracciati PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;




CREATE TABLE versamenti
(
	cod_versamento_ente VARCHAR(35) NOT NULL,
	nome VARCHAR(35),
	importo_totale DOUBLE NOT NULL,
	stato_versamento VARCHAR(35) NOT NULL,
	descrizione_stato VARCHAR(255),
	-- Indica se, decorsa la dataScadenza, deve essere aggiornato da remoto o essere considerato scaduto
	aggiornabile BOOLEAN NOT NULL,
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
	data_creazione TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
	data_validita TIMESTAMP(3),
    -- Per versioni successive alla 5.7, rimuovere dalla sql_mode NO_ZERO_DATE 
	data_scadenza TIMESTAMP(3), 
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
	data_ora_ultimo_aggiornamento TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
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
	dati_allegati LONGTEXT,
	incasso VARCHAR(1),
	anomalie LONGTEXT,
	iuv_versamento VARCHAR(35),
	numero_avviso VARCHAR(35),
	avvisatura VARCHAR(1),
	tipo_pagamento INT,
	da_avvisare BOOLEAN NOT NULL,
	cod_avvisatura VARCHAR(20),
	ack BOOLEAN NOT NULL,
	note LONGTEXT,
	anomalo BOOLEAN NOT NULL,
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT,
	id_dominio BIGINT NOT NULL,
	id_uo BIGINT,
	id_applicazione BIGINT NOT NULL,
	id_tracciato BIGINT,
	-- unique constraints
	CONSTRAINT unique_versamenti_1 UNIQUE (cod_versamento_ente,id_applicazione),
	-- fk/pk keys constraints
	CONSTRAINT fk_vrs_id_dominio FOREIGN KEY (id_dominio) REFERENCES domini(id),
	CONSTRAINT fk_vrs_id_uo FOREIGN KEY (id_uo) REFERENCES uo(id),
	CONSTRAINT fk_vrs_id_applicazione FOREIGN KEY (id_applicazione) REFERENCES applicazioni(id),
	CONSTRAINT fk_vrs_id_tracciato FOREIGN KEY (id_tracciato) REFERENCES tracciati(id),
	CONSTRAINT pk_versamenti PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;

-- index
CREATE INDEX index_versamenti_1 ON versamenti (cod_versamento_ente,id_applicazione);



CREATE TABLE singoli_versamenti
(
	cod_singolo_versamento_ente VARCHAR(70) NOT NULL,
	stato_singolo_versamento VARCHAR(35) NOT NULL,
	importo_singolo_versamento DOUBLE NOT NULL,
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
	dati_allegati LONGTEXT,
	indice_dati INT NOT NULL,
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT,
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
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;

-- index
CREATE INDEX index_singoli_versamenti_1 ON singoli_versamenti (id_versamento,cod_singolo_versamento_ente,indice_dati);



CREATE TABLE pagamenti_portale
(
	cod_canale VARCHAR(35),
	nome VARCHAR(255) NOT NULL,
	importo DOUBLE NOT NULL,
	versante_identificativo VARCHAR(35),
	id_sessione VARCHAR(35) NOT NULL,
	id_sessione_portale VARCHAR(35),
	id_sessione_psp VARCHAR(35),
	stato VARCHAR(35) NOT NULL,
	codice_stato VARCHAR(35) NOT NULL,
	descrizione_stato VARCHAR(1024),
	psp_redirect_url VARCHAR(1024),
	psp_esito VARCHAR(255),
	json_request LONGTEXT,
	wisp_id_dominio VARCHAR(255),
	wisp_key_pa VARCHAR(255),
	wisp_key_wisp VARCHAR(255),
	wisp_html LONGTEXT,
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
	data_richiesta TIMESTAMP(3) DEFAULT 0,
	url_ritorno VARCHAR(1024),
	cod_psp VARCHAR(35),
	tipo_versamento VARCHAR(4),
	multi_beneficiario VARCHAR(35),
	ack BOOLEAN NOT NULL,
	note LONGTEXT,
	tipo INT NOT NULL,
	principal VARCHAR(4000) NOT NULL,
	tipo_utenza VARCHAR(35) NOT NULL,
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT,
	-- unique constraints
	CONSTRAINT unique_pagamenti_portale_1 UNIQUE (id_sessione),
	-- fk/pk keys constraints
	CONSTRAINT pk_pagamenti_portale PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;

-- index
CREATE UNIQUE INDEX index_pagamenti_portale_1 ON pagamenti_portale (id_sessione);



CREATE TABLE pag_port_versamenti
(
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT,
	id_pagamento_portale BIGINT NOT NULL,
	id_versamento BIGINT NOT NULL,
	-- fk/pk keys constraints
	CONSTRAINT fk_ppv_id_pagamento_portale FOREIGN KEY (id_pagamento_portale) REFERENCES pagamenti_portale(id),
	CONSTRAINT fk_ppv_id_versamento FOREIGN KEY (id_versamento) REFERENCES versamenti(id),
	CONSTRAINT pk_pag_port_versamenti PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;




CREATE TABLE rpt
(
	cod_carrello VARCHAR(35),
	iuv VARCHAR(35) NOT NULL,
	ccp VARCHAR(35) NOT NULL,
	cod_dominio VARCHAR(35) NOT NULL,
	-- Identificativo dell'RPT utilizzato come riferimento nell'RT
	cod_msg_richiesta VARCHAR(35) NOT NULL,
	-- Data di creazione dell'RPT
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
	data_msg_richiesta TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
	-- Stato RPT secondo la codifica AgID
	stato VARCHAR(35) NOT NULL,
	descrizione_stato LONGTEXT,
	cod_sessione VARCHAR(255),
	cod_sessione_portale VARCHAR(255),
	-- Indirizzo del portale psp a cui redirigere il cittadino per eseguire il pagamento
	psp_redirect_url VARCHAR(512),
	xml_rpt MEDIUMBLOB NOT NULL,
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
	data_aggiornamento_stato TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
	-- Indirizzo di ritorno al portale dell'ente al termine del pagamento
	callback_url LONGTEXT,
	modello_pagamento VARCHAR(16) NOT NULL,
	cod_msg_ricevuta VARCHAR(35),
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
	-- Per versioni successive alla 5.7, rimuovere dalla sql_mode NO_ZERO_DATE 
	data_msg_ricevuta TIMESTAMP(3),
	-- Esito del pagamento:\n0: Eseguito\n1: Non eseguito\n2: Parzialmente eseguito\n3: Decorrenza\n4: Decorrenza Parziale
	cod_esito_pagamento INT,
	importo_totale_pagato DOUBLE,
	xml_rt MEDIUMBLOB,
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
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
	data_conservazione TIMESTAMP(3) DEFAULT 0,
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT,
	id_versamento BIGINT NOT NULL,
	id_pagamento_portale BIGINT,
	id_applicazione BIGINT,
	-- unique constraints
	CONSTRAINT unique_rpt_1 UNIQUE (cod_msg_richiesta),
	CONSTRAINT unique_rpt_2 UNIQUE (iuv,ccp,cod_dominio),
	-- fk/pk keys constraints
	CONSTRAINT fk_rpt_id_versamento FOREIGN KEY (id_versamento) REFERENCES versamenti(id),
	CONSTRAINT fk_rpt_id_pagamento_portale FOREIGN KEY (id_pagamento_portale) REFERENCES pagamenti_portale(id),
	CONSTRAINT fk_rpt_id_applicazione FOREIGN KEY (id_applicazione) REFERENCES applicazioni(id),
	CONSTRAINT pk_rpt PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;

-- index
CREATE INDEX index_rpt_1 ON rpt (cod_msg_richiesta);
CREATE INDEX index_rpt_2 ON rpt (iuv,ccp,cod_dominio);
CREATE INDEX index_rpt_3 ON rpt (stato);
CREATE INDEX index_rpt_4 ON rpt (id_versamento);



CREATE TABLE rr
(
	cod_dominio VARCHAR(35) NOT NULL,
	iuv VARCHAR(35) NOT NULL,
	ccp VARCHAR(35) NOT NULL,
	cod_msg_revoca VARCHAR(35) NOT NULL,
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
	data_msg_revoca TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
    -- Per versioni successive alla 5.7, rimuovere dalla sql_mode NO_ZERO_DATE 
	data_msg_esito TIMESTAMP(3),
	stato VARCHAR(35) NOT NULL,
	descrizione_stato VARCHAR(512),
	importo_totale_richiesto DOUBLE NOT NULL,
	cod_msg_esito VARCHAR(35),
	importo_totale_revocato DOUBLE,
	xml_rr MEDIUMBLOB NOT NULL,
	xml_er MEDIUMBLOB,
	cod_transazione_rr VARCHAR(36),
	cod_transazione_er VARCHAR(36),
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT,
	id_rpt BIGINT NOT NULL,
	-- unique constraints
	CONSTRAINT unique_rr_1 UNIQUE (cod_msg_revoca),
	-- fk/pk keys constraints
	CONSTRAINT fk_rr_id_rpt FOREIGN KEY (id_rpt) REFERENCES rpt(id),
	CONSTRAINT pk_rr PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;

-- index
CREATE INDEX index_rr_1 ON rr (cod_msg_revoca);



CREATE TABLE notifiche
(
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
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT,
	id_applicazione BIGINT NOT NULL,
	id_rpt BIGINT,
	id_rr BIGINT,
	-- fk/pk keys constraints
	CONSTRAINT fk_ntf_id_applicazione FOREIGN KEY (id_applicazione) REFERENCES applicazioni(id),
	CONSTRAINT fk_ntf_id_rpt FOREIGN KEY (id_rpt) REFERENCES rpt(id),
	CONSTRAINT fk_ntf_id_rr FOREIGN KEY (id_rr) REFERENCES rr(id),
	CONSTRAINT pk_notifiche PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;




CREATE TABLE iuv
(
	prg BIGINT NOT NULL,
	iuv VARCHAR(35) NOT NULL,
	application_code INT NOT NULL,
	data_generazione TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
	tipo_iuv VARCHAR(1) NOT NULL,
	cod_versamento_ente VARCHAR(35),
	aux_digit INT NOT NULL DEFAULT 0,
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT,
	id_applicazione BIGINT NOT NULL,
	id_dominio BIGINT NOT NULL,
	-- unique constraints
	CONSTRAINT unique_iuv_1 UNIQUE (id_dominio,iuv),
	-- fk/pk keys constraints
	CONSTRAINT fk_iuv_id_applicazione FOREIGN KEY (id_applicazione) REFERENCES applicazioni(id),
	CONSTRAINT fk_iuv_id_dominio FOREIGN KEY (id_dominio) REFERENCES domini(id),
	CONSTRAINT pk_iuv PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;

-- index
CREATE INDEX index_iuv_1 ON iuv (id_dominio,iuv);
CREATE INDEX index_iuv_2 ON iuv (cod_versamento_ente,tipo_iuv,id_applicazione);



CREATE TABLE fr
(
	cod_psp VARCHAR(35) NOT NULL,
	cod_dominio VARCHAR(35) NOT NULL,
	cod_flusso VARCHAR(35) NOT NULL,
	stato VARCHAR(35) NOT NULL,
	descrizione_stato LONGTEXT,
	iur VARCHAR(35) NOT NULL,
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
    -- Per versioni successive alla 5.7, rimuovere dalla sql_mode NO_ZERO_DATE 
	data_ora_flusso TIMESTAMP(3),
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
    -- Per versioni successive alla 5.7, rimuovere dalla sql_mode NO_ZERO_DATE 
	data_regolamento TIMESTAMP(3),
	data_acquisizione TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
	numero_pagamenti BIGINT,
	importo_totale_pagamenti DOUBLE,
	cod_bic_riversamento VARCHAR(35),
	xml MEDIUMBLOB NOT NULL,
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT,
	-- unique constraints
	CONSTRAINT unique_fr_1 UNIQUE (cod_flusso),
	-- fk/pk keys constraints
	CONSTRAINT pk_fr PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;

-- index
CREATE INDEX index_fr_1 ON fr (cod_flusso);



CREATE TABLE incassi
(
	trn VARCHAR(35) NOT NULL,
	cod_dominio VARCHAR(35) NOT NULL,
	causale VARCHAR(512) NOT NULL,
	importo DOUBLE NOT NULL,
	data_valuta TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP(3),
	data_contabile TIMESTAMP(3) DEFAULT  CURRENT_TIMESTAMP(3),
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
	data_ora_incasso TIMESTAMP(3) NOT NULL DEFAULT  CURRENT_TIMESTAMP(3),
	nome_dispositivo VARCHAR(512),
	iban_accredito VARCHAR(35),
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT,
	id_applicazione BIGINT,
	id_operatore BIGINT,
	-- unique constraints
	CONSTRAINT unique_incassi_1 UNIQUE (cod_dominio,trn),
	-- fk/pk keys constraints
	CONSTRAINT fk_inc_id_applicazione FOREIGN KEY (id_applicazione) REFERENCES applicazioni(id),
	CONSTRAINT fk_inc_id_operatore FOREIGN KEY (id_operatore) REFERENCES operatori(id),
	CONSTRAINT pk_incassi PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;

-- index
CREATE INDEX index_incassi_1 ON incassi (cod_dominio,trn);



CREATE TABLE pagamenti
(
	cod_dominio VARCHAR(35) NOT NULL,
	iuv VARCHAR(35) NOT NULL,
	indice_dati INT NOT NULL,
	importo_pagato DOUBLE NOT NULL DEFAULT 1,
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
	data_acquisizione TIMESTAMP(3) NOT NULL DEFAULT 0,
	iur VARCHAR(35) NOT NULL,
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
	data_pagamento TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
	commissioni_psp DOUBLE,
	-- Valori possibili:\nES: Esito originario\nBD: Marca da Bollo
	tipo_allegato VARCHAR(2),
	allegato MEDIUMBLOB,
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
	data_acquisizione_revoca TIMESTAMP(3) DEFAULT 0,
	causale_revoca VARCHAR(140),
	dati_revoca VARCHAR(140),
	importo_revocato DOUBLE,
	esito_revoca VARCHAR(140),
	dati_esito_revoca VARCHAR(140),
	stato VARCHAR(35),
	tipo VARCHAR(35) NOT NULL,
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT,
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
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;

-- index
CREATE UNIQUE INDEX index_pagamenti_1 ON pagamenti (cod_dominio,iuv,iur,indice_dati);



CREATE TABLE rendicontazioni
(
	iuv VARCHAR(35) NOT NULL,
	iur VARCHAR(35) NOT NULL,
	indice_dati INT,
	importo_pagato DOUBLE,
	esito INT,
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
	data TIMESTAMP(3) DEFAULT 0,
	stato VARCHAR(35) NOT NULL,
	anomalie LONGTEXT,
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT,
	id_fr BIGINT NOT NULL,
	id_pagamento BIGINT,
	id_singolo_versamento BIGINT,
	-- fk/pk keys constraints
	CONSTRAINT fk_rnd_id_fr FOREIGN KEY (id_fr) REFERENCES fr(id),
	CONSTRAINT fk_rnd_id_pagamento FOREIGN KEY (id_pagamento) REFERENCES pagamenti(id),
	CONSTRAINT fk_rnd_id_singolo_versamento FOREIGN KEY (id_singolo_versamento) REFERENCES singoli_versamenti(id),
	CONSTRAINT pk_rendicontazioni PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;




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
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
    -- Per versioni successive alla 5.7, rimuovere dalla sql_mode NO_ZERO_DATE 
	data_1 TIMESTAMP(3),
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
    -- Per versioni successive alla 5.7, rimuovere dalla sql_mode NO_ZERO_DATE 
	data_2 TIMESTAMP(3),
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT,
	-- fk/pk keys constraints
	CONSTRAINT pk_eventi PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;




CREATE TABLE batch
(
	cod_batch VARCHAR(255) NOT NULL,
	nodo INT,
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
	inizio TIMESTAMP(3),
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
	aggiornamento TIMESTAMP(3),
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT,
	-- unique constraints
	CONSTRAINT unique_batch_1 UNIQUE (cod_batch),
	-- fk/pk keys constraints
	CONSTRAINT pk_batch PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;

-- index
CREATE INDEX index_batch_1 ON batch (cod_batch);



CREATE TABLE esiti_avvisatura
(
	cod_dominio VARCHAR(35) NOT NULL,
	identificativo_avvisatura VARCHAR(20) NOT NULL,
	tipo_canale INT NOT NULL,
	cod_canale VARCHAR(35),
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
	data TIMESTAMP(3) NOT NULL DEFAULT 0,
	cod_esito INT NOT NULL,
	descrizione_esito VARCHAR(140) NOT NULL,
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT,
	id_tracciato BIGINT NOT NULL,
	-- fk/pk keys constraints
	CONSTRAINT fk_sta_id_tracciato FOREIGN KEY (id_tracciato) REFERENCES tracciati(id),
	CONSTRAINT pk_esiti_avvisatura PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;




CREATE TABLE operazioni
(
	tipo_operazione VARCHAR(255) NOT NULL,
	linea_elaborazione BIGINT NOT NULL,
	stato VARCHAR(255) NOT NULL,
	dati_richiesta MEDIUMBLOB NOT NULL,
	dati_risposta MEDIUMBLOB,
	dettaglio_esito VARCHAR(255),
	cod_versamento_ente VARCHAR(255),
	cod_dominio VARCHAR(35),
	iuv VARCHAR(35),
	trn VARCHAR(35),
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT,
	id_tracciato BIGINT NOT NULL,
	id_applicazione BIGINT,
	-- fk/pk keys constraints
	CONSTRAINT fk_ope_id_tracciato FOREIGN KEY (id_tracciato) REFERENCES tracciati(id),
	CONSTRAINT fk_ope_id_applicazione FOREIGN KEY (id_applicazione) REFERENCES applicazioni(id),
	CONSTRAINT pk_operazioni PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;




CREATE TABLE gp_audit
(
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
	data TIMESTAMP(3) NOT NULL DEFAULT 0,
	id_oggetto BIGINT NOT NULL,
	tipo_oggetto VARCHAR(255) NOT NULL,
	oggetto LONGTEXT NOT NULL,
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT,
	id_operatore BIGINT NOT NULL,
	-- fk/pk keys constraints
	CONSTRAINT fk_aud_id_operatore FOREIGN KEY (id_operatore) REFERENCES operatori(id),
	CONSTRAINT pk_gp_audit PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;




CREATE TABLE avvisi
(
	cod_dominio VARCHAR(35) NOT NULL,
	iuv VARCHAR(35) NOT NULL,
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
	data_creazione TIMESTAMP(3) NOT NULL DEFAULT 0,
	stato VARCHAR(255) NOT NULL,
	pdf MEDIUMBLOB,
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT,
	-- fk/pk keys constraints
	CONSTRAINT pk_avvisi PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;

-- index
CREATE INDEX index_avvisi_1 ON avvisi (cod_dominio,iuv);
CREATE INDEX index_avvisi_2 ON avvisi (stato);



CREATE TABLE ID_MESSAGGIO_RELATIVO
(
	COUNTER BIGINT NOT NULL,
	PROTOCOLLO VARCHAR(255) NOT NULL,
	INFO_ASSOCIATA VARCHAR(255) NOT NULL,
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
	ora_registrazione TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP(3),
	-- fk/pk columns
	-- fk/pk keys constraints
	CONSTRAINT pk_ID_MESSAGGIO_RELATIVO PRIMARY KEY (PROTOCOLLO,INFO_ASSOCIATA)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;


CREATE TABLE sonde
(
	nome VARCHAR(35) NOT NULL,
	classe VARCHAR(255) NOT NULL,
	soglia_warn BIGINT NOT NULL,
	soglia_error BIGINT NOT NULL,
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
	data_ok TIMESTAMP(3) DEFAULT 0,
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
	data_warn TIMESTAMP(3) DEFAULT 0,
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
	data_error TIMESTAMP(3) DEFAULT 0,
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
	data_ultimo_check TIMESTAMP(3) DEFAULT 0,
	dati_check LONGTEXT,
	stato_ultimo_check INT,
	-- fk/pk columns
	-- fk/pk keys constraints
	CONSTRAINT pk_sonde PRIMARY KEY (nome)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;

-- Sezione Viste

CREATE VIEW versamenti_incassi AS SELECT
versamenti.id as id,
MAX(versamenti.cod_versamento_ente) as cod_versamento_ente,          
MAX(versamenti.nome) as nome,                         
MAX(versamenti.importo_totale) as importo_totale,               
versamenti.stato_versamento as stato_versamento,             
MAX(versamenti.descrizione_stato) as descrizione_stato,           
MAX(CASE WHEN versamenti.aggiornabile = TRUE THEN 'TRUE' ELSE 'FALSE' END) AS aggiornabile,
MAX(versamenti.data_creazione) as data_creazione,               
MAX(versamenti.data_validita) as data_validita,                
MAX(versamenti.data_scadenza) as data_scadenza,                
MAX(versamenti.data_ora_ultimo_aggiornamento) as data_ora_ultimo_aggiornamento,
MAX(versamenti.causale_versamento) as causale_versamento,           
MAX(versamenti.debitore_tipo) as debitore_tipo,                
versamenti.debitore_identificativo as debitore_identificativo,      
MAX(versamenti.debitore_anagrafica) as debitore_anagrafica,          
MAX(versamenti.debitore_indirizzo) as debitore_indirizzo,           
MAX(versamenti.debitore_civico) as debitore_civico,              
MAX(versamenti.debitore_cap) as debitore_cap,                 
MAX(versamenti.debitore_localita) as debitore_localita,            
MAX(versamenti.debitore_provincia) as debitore_provincia,           
MAX(versamenti.debitore_nazione) as debitore_nazione,             
MAX(versamenti.debitore_email) as debitore_email,               
MAX(versamenti.debitore_telefono) as debitore_telefono,            
MAX(versamenti.debitore_cellulare) as debitore_cellulare,           
MAX(versamenti.debitore_fax) as debitore_fax,                 
MAX(versamenti.tassonomia_avviso) as tassonomia_avviso,            
MAX(versamenti.tassonomia) as tassonomia,                   
MAX(versamenti.cod_lotto) as cod_lotto,                    
MAX(versamenti.cod_versamento_lotto) as cod_versamento_lotto,         
MAX(versamenti.cod_anno_tributario) as cod_anno_tributario,          
MAX(versamenti.cod_bundlekey) as cod_bundlekey,                
MAX(versamenti.dati_allegati) as dati_allegati,                
MAX(versamenti.incasso) as incasso,                      
MAX(versamenti.anomalie) as anomalie,                     
MAX(versamenti.iuv_versamento) as iuv_versamento,               
MAX(versamenti.numero_avviso) as numero_avviso,                
MAX(versamenti.avvisatura) as avvisatura,                   
MAX(versamenti.tipo_pagamento) as tipo_pagamento,               
MAX(versamenti.id_dominio) as id_dominio,                   
MAX(versamenti.id_uo) as id_uo,                        
MAX(versamenti.id_applicazione) as id_applicazione,             
MAX(CASE WHEN versamenti.da_avvisare = TRUE THEN 'TRUE' ELSE 'FALSE' END) AS da_avvisare,
MAX(versamenti.cod_avvisatura) as cod_avvisatura,               
MAX(versamenti.id_tracciato) as id_tracciato,      
MAX(CASE WHEN versamenti.ack = TRUE THEN 'TRUE' ELSE 'FALSE' END) AS ack,
MAX(versamenti.note) as note,
MAX(CASE WHEN versamenti.anomalo = TRUE THEN 'TRUE' ELSE 'FALSE' END) AS anomalo,
MAX(pagamenti.data_pagamento) as data_pagamento,            
SUM(CASE WHEN pagamenti.importo_pagato IS NOT NULL THEN pagamenti.importo_pagato ELSE 0 END) AS importo_pagato,
SUM(CASE WHEN pagamenti.stato = 'INCASSATO' THEN pagamenti.importo_pagato ELSE 0 END) AS importo_incassato,
MAX(CASE WHEN pagamenti.stato IS NULL THEN 'NON_PAGATO' WHEN pagamenti.stato = 'INCASSATO' THEN 'INCASSATO' ELSE 'PAGATO' END) AS stato_pagamento,
MAX(pagamenti.iuv) AS iuv_pagamento,
MAX(CASE WHEN versamenti.stato_versamento = 'NON_ESEGUITO' AND versamenti.data_validita > now() THEN 0 ELSE 1 END) AS smart_order_rank,
MIN(ABS((UNIX_TIMESTAMP(now()) *1000) - (UNIX_TIMESTAMP(COALESCE(pagamenti.data_pagamento, versamenti.data_validita, versamenti.data_creazione)) * 1000))) AS smart_order_date
FROM versamenti LEFT JOIN singoli_versamenti ON versamenti.id = singoli_versamenti.id_versamento LEFT join pagamenti on singoli_versamenti.id = pagamenti.id_singolo_versamento
WHERE versamenti.numero_avviso IS NOT NULL OR pagamenti.importo_pagato > 0
GROUP BY versamenti.id, versamenti.debitore_identificativo, versamenti.stato_versamento;

