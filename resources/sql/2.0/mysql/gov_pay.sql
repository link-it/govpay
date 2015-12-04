CREATE TABLE tracciatixml
(
	tipo_tracciato VARCHAR(255) NOT NULL,
	cod_messaggio VARCHAR(255) NOT NULL,
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
	data_ora_creazione TIMESTAMP(3) NOT NULL DEFAULT 0,
	xml MEDIUMBLOB NOT NULL,
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT,
	-- fk/pk keys constraints
	CONSTRAINT pk_tracciatixml PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;




CREATE TABLE psp
(
	cod_psp VARCHAR(255) NOT NULL,
	ragione_sociale VARCHAR(255) NOT NULL,
	cod_flusso VARCHAR(255) NOT NULL,
	url_info VARCHAR(255),
	abilitato BOOLEAN NOT NULL,
	storno BOOLEAN NOT NULL,
	marca_bollo BOOLEAN NOT NULL,
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT,
	id_tracciato_xml BIGINT NOT NULL,
	-- unique constraints
	CONSTRAINT unique_psp_1 UNIQUE (cod_psp),
	-- fk/pk keys constraints
	CONSTRAINT fk_psp_1 FOREIGN KEY (id_tracciato_xml) REFERENCES tracciatixml(id) ON DELETE CASCADE,
	CONSTRAINT pk_psp PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;

-- index
CREATE INDEX index_psp_1 ON psp (cod_psp);



CREATE TABLE canali
(
	cod_canale VARCHAR(255) NOT NULL,
	cod_intermediario VARCHAR(255) NOT NULL,
	tipo_versamento VARCHAR(255) NOT NULL,
	modello_pagamento INT NOT NULL,
	disponibilita LONGTEXT,
	descrizione LONGTEXT,
	condizioni VARCHAR(255),
	url_info VARCHAR(255),
	abilitato BOOLEAN NOT NULL,
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT,
	id_psp BIGINT NOT NULL,
	-- unique constraints
	CONSTRAINT unique_canali_1 UNIQUE (id_psp,cod_canale,tipo_versamento),
	-- fk/pk keys constraints
	CONSTRAINT fk_canali_1 FOREIGN KEY (id_psp) REFERENCES psp(id) ON DELETE CASCADE,
	CONSTRAINT pk_canali PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;

-- index
CREATE INDEX index_canali_1 ON canali (id_psp,cod_canale,tipo_versamento);



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
	id BIGINT AUTO_INCREMENT,
	-- fk/pk keys constraints
	CONSTRAINT pk_anagrafiche PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;




CREATE TABLE intermediari
(
	cod_intermediario VARCHAR(255) NOT NULL,
	cod_connettore_pdd VARCHAR(255) NOT NULL,
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
	cod_stazione VARCHAR(255) NOT NULL,
	password VARCHAR(255) NOT NULL,
	abilitato BOOLEAN NOT NULL,
	application_code INT NOT NULL,
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT,
	id_intermediario BIGINT NOT NULL,
	-- unique constraints
	CONSTRAINT unique_stazioni_1 UNIQUE (cod_stazione),
	-- fk/pk keys constraints
	CONSTRAINT fk_stazioni_1 FOREIGN KEY (id_intermediario) REFERENCES intermediari(id) ON DELETE CASCADE,
	CONSTRAINT pk_stazioni PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;

-- index
CREATE INDEX index_stazioni_1 ON stazioni (cod_stazione);



CREATE TABLE domini
(
	cod_dominio VARCHAR(255) NOT NULL,
	ragione_sociale VARCHAR(255) NOT NULL,
	gln VARCHAR(255) NOT NULL,
	plugin_class VARCHAR(512),
	abilitato BOOLEAN NOT NULL,
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT,
	id_stazione BIGINT NOT NULL,
	-- unique constraints
	CONSTRAINT unique_domini_1 UNIQUE (cod_dominio),
	-- fk/pk keys constraints
	CONSTRAINT fk_domini_1 FOREIGN KEY (id_stazione) REFERENCES stazioni(id) ON DELETE CASCADE,
	CONSTRAINT pk_domini PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;

-- index
CREATE INDEX index_domini_1 ON domini (cod_dominio);



CREATE TABLE disponibilita
(
	tipo_periodo VARCHAR(255) NOT NULL,
	giorno VARCHAR(255) NOT NULL,
	fasce_orarie LONGTEXT NOT NULL,
	tipo_disponibilita VARCHAR(255) NOT NULL,
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT,
	id_dominio BIGINT,
	-- fk/pk keys constraints
	CONSTRAINT fk_disponibilita_1 FOREIGN KEY (id_dominio) REFERENCES domini(id) ON DELETE CASCADE,
	CONSTRAINT pk_disponibilita PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;




CREATE TABLE mail_template
(
	mittente VARCHAR(255) NOT NULL,
	template_oggetto LONGTEXT NOT NULL,
	template_messaggio LONGTEXT NOT NULL,
	allegati VARCHAR(255),
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT,
	-- fk/pk keys constraints
	CONSTRAINT pk_mail_template PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;




CREATE TABLE enti
(
	cod_ente VARCHAR(255) NOT NULL,
	abilitato BOOLEAN NOT NULL,
	invio_mail_rptabilitato BOOLEAN NOT NULL,
	invio_mail_rtabilitato BOOLEAN NOT NULL,
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT,
	id_anagrafica_ente BIGINT NOT NULL,
	id_dominio BIGINT NOT NULL,
	id_template_rpt BIGINT,
	id_template_rt BIGINT,
	-- unique constraints
	CONSTRAINT unique_enti_1 UNIQUE (cod_ente),
	-- fk/pk keys constraints
	CONSTRAINT fk_enti_1 FOREIGN KEY (id_anagrafica_ente) REFERENCES anagrafiche(id) ON DELETE CASCADE,
	CONSTRAINT fk_enti_2 FOREIGN KEY (id_dominio) REFERENCES domini(id) ON DELETE CASCADE,
	CONSTRAINT fk_enti_3 FOREIGN KEY (id_template_rpt) REFERENCES mail_template(id) ON DELETE CASCADE,
	CONSTRAINT fk_enti_4 FOREIGN KEY (id_template_rt) REFERENCES mail_template(id) ON DELETE CASCADE,
	CONSTRAINT pk_enti PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;

-- index
CREATE INDEX index_enti_1 ON enti (cod_ente);



CREATE TABLE applicazioni
(
	cod_applicazione VARCHAR(255) NOT NULL,
	principal VARCHAR(255) NOT NULL,
	abilitato BOOLEAN NOT NULL,
	versione VARCHAR(255) NOT NULL,
	policy_rispedizione VARCHAR(255),
	cod_connettore_esito VARCHAR(255),
	cod_connettore_verifica VARCHAR(255),
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT,
	-- unique constraints
	CONSTRAINT unique_applicazioni_1 UNIQUE (cod_applicazione),
	CONSTRAINT unique_applicazioni_2 UNIQUE (principal),
	-- fk/pk keys constraints
	CONSTRAINT pk_applicazioni PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;

-- index
CREATE INDEX index_applicazioni_1 ON applicazioni (cod_applicazione);
CREATE INDEX index_applicazioni_2 ON applicazioni (principal);



CREATE TABLE operatori
(
	principal VARCHAR(255) NOT NULL,
	profilo VARCHAR(255) NOT NULL,
	abilitato BOOLEAN NOT NULL DEFAULT true,
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT,
	id_anagrafica BIGINT NOT NULL,
	-- unique constraints
	CONSTRAINT unique_operatori_1 UNIQUE (principal),
	-- fk/pk keys constraints
	CONSTRAINT fk_operatori_1 FOREIGN KEY (id_anagrafica) REFERENCES anagrafiche(id) ON DELETE CASCADE,
	CONSTRAINT pk_operatori PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;

-- index
CREATE INDEX index_operatori_1 ON operatori (principal);



CREATE TABLE operatori_enti
(
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT,
	id_operatore BIGINT NOT NULL,
	id_ente BIGINT NOT NULL,
	-- fk/pk keys constraints
	CONSTRAINT fk_operatori_enti_1 FOREIGN KEY (id_operatore) REFERENCES operatori(id) ON DELETE CASCADE,
	CONSTRAINT fk_operatori_enti_2 FOREIGN KEY (id_ente) REFERENCES enti(id) ON DELETE CASCADE,
	CONSTRAINT pk_operatori_enti PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;




CREATE TABLE operatori_applicazioni
(
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT,
	id_operatore BIGINT NOT NULL,
	id_applicazione BIGINT NOT NULL,
	-- fk/pk keys constraints
	CONSTRAINT fk_operatori_applicazioni_1 FOREIGN KEY (id_operatore) REFERENCES operatori(id) ON DELETE CASCADE,
	CONSTRAINT fk_operatori_applicazioni_2 FOREIGN KEY (id_applicazione) REFERENCES applicazioni(id) ON DELETE CASCADE,
	CONSTRAINT pk_operatori_applicazioni PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;




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



CREATE TABLE portali
(
	cod_portale VARCHAR(255) NOT NULL,
	default_callback_url VARCHAR(512) NOT NULL,
	principal VARCHAR(255) NOT NULL,
	abilitato BOOLEAN NOT NULL,
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT,
	id_stazione BIGINT NOT NULL,
	-- unique constraints
	CONSTRAINT unique_portali_1 UNIQUE (cod_portale),
	CONSTRAINT unique_portali_2 UNIQUE (principal),
	-- fk/pk keys constraints
	CONSTRAINT fk_portali_1 FOREIGN KEY (id_stazione) REFERENCES stazioni(id) ON DELETE CASCADE,
	CONSTRAINT pk_portali PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;

-- index
CREATE INDEX index_portali_1 ON portali (cod_portale);
CREATE INDEX index_portali_2 ON portali (principal);



CREATE TABLE portali_applicazioni
(
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT,
	id_portale BIGINT NOT NULL,
	id_applicazione BIGINT NOT NULL,
	-- fk/pk keys constraints
	CONSTRAINT fk_portali_applicazioni_1 FOREIGN KEY (id_portale) REFERENCES portali(id) ON DELETE CASCADE,
	CONSTRAINT fk_portali_applicazioni_2 FOREIGN KEY (id_applicazione) REFERENCES applicazioni(id) ON DELETE CASCADE,
	CONSTRAINT pk_portali_applicazioni PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;




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
	id BIGINT AUTO_INCREMENT,
	id_dominio BIGINT NOT NULL,
	-- unique constraints
	CONSTRAINT unique_iban_accredito_1 UNIQUE (cod_iban),
	-- fk/pk keys constraints
	CONSTRAINT fk_iban_accredito_1 FOREIGN KEY (id_dominio) REFERENCES domini(id) ON DELETE CASCADE,
	CONSTRAINT pk_iban_accredito PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;

-- index
CREATE INDEX index_iban_accredito_1 ON iban_accredito (cod_iban);



CREATE TABLE tributi
(
	cod_tributo VARCHAR(255) NOT NULL,
	abilitato BOOLEAN NOT NULL,
	descrizione VARCHAR(255),
	tipo_contabilita VARCHAR(255) NOT NULL,
	codice_contabilita VARCHAR(255) NOT NULL,
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT,
	id_ente BIGINT NOT NULL,
	id_iban_accredito BIGINT,
	-- unique constraints
	CONSTRAINT unique_tributi_1 UNIQUE (id_ente,cod_tributo),
	-- fk/pk keys constraints
	CONSTRAINT fk_tributi_1 FOREIGN KEY (id_ente) REFERENCES enti(id) ON DELETE CASCADE,
	CONSTRAINT fk_tributi_2 FOREIGN KEY (id_iban_accredito) REFERENCES iban_accredito(id) ON DELETE CASCADE,
	CONSTRAINT pk_tributi PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;

-- index
CREATE INDEX index_tributi_1 ON tributi (id_ente,cod_tributo);



CREATE TABLE applicazioni_tributi
(
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT,
	id_applicazione BIGINT,
	id_tributo BIGINT NOT NULL,
	-- fk/pk keys constraints
	CONSTRAINT fk_applicazioni_tributi_1 FOREIGN KEY (id_applicazione) REFERENCES applicazioni(id) ON DELETE CASCADE,
	CONSTRAINT fk_applicazioni_tributi_2 FOREIGN KEY (id_tributo) REFERENCES tributi(id) ON DELETE CASCADE,
	CONSTRAINT pk_applicazioni_tributi PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;




CREATE TABLE versamenti
(
	cod_versamento_ente VARCHAR(255) NOT NULL,
	cod_dominio VARCHAR(255) NOT NULL,
	iuv VARCHAR(255) NOT NULL,
	importo_totale DOUBLE NOT NULL,
	stato_versamento VARCHAR(255) NOT NULL,
	descrizione_stato VARCHAR(255),
	stato_rendicontazione VARCHAR(255),
	importo_pagato DOUBLE,
	data_scadenza TIMESTAMP NOT NULL DEFAULT 0,
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
	data_ora_ultimo_aggiornamento TIMESTAMP(3) NOT NULL DEFAULT 0,
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT,
	id_ente BIGINT NOT NULL,
	id_applicazione BIGINT NOT NULL,
	id_anagrafica_debitore BIGINT NOT NULL,
	-- unique constraints
	CONSTRAINT unique_versamenti_1 UNIQUE (cod_versamento_ente,cod_dominio),
	CONSTRAINT unique_versamenti_2 UNIQUE (iuv,cod_dominio),
	-- fk/pk keys constraints
	CONSTRAINT fk_versamenti_1 FOREIGN KEY (id_ente) REFERENCES enti(id) ON DELETE CASCADE,
	CONSTRAINT fk_versamenti_2 FOREIGN KEY (id_applicazione) REFERENCES applicazioni(id) ON DELETE CASCADE,
	CONSTRAINT fk_versamenti_3 FOREIGN KEY (id_anagrafica_debitore) REFERENCES anagrafiche(id) ON DELETE CASCADE,
	CONSTRAINT pk_versamenti PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;

-- index
CREATE INDEX index_versamenti_1 ON versamenti (cod_versamento_ente,cod_dominio);
CREATE INDEX index_versamenti_2 ON versamenti (iuv,cod_dominio);



CREATE TABLE singoli_versamenti
(
	indice INT NOT NULL,
	cod_singolo_versamento_ente VARCHAR(255),
	anno_riferimento INT,
	iban_accredito VARCHAR(255),
	importo_singolo_versamento DOUBLE NOT NULL,
	importo_commissioni_pa DOUBLE,
	singolo_importo_pagato DOUBLE,
	causale_versamento VARCHAR(255),
	dati_specifici_riscossione VARCHAR(255),
	stato_singolo_versamento VARCHAR(255),
	esito_singolo_pagamento VARCHAR(255),
	data_esito_singolo_pagamento TIMESTAMP DEFAULT 0,
	iur VARCHAR(255),
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT,
	id_versamento BIGINT NOT NULL,
	id_tributo BIGINT NOT NULL,
	-- unique constraints
	CONSTRAINT unique_singoli_versamenti_1 UNIQUE (id_versamento,indice),
	-- fk/pk keys constraints
	CONSTRAINT fk_singoli_versamenti_1 FOREIGN KEY (id_versamento) REFERENCES versamenti(id) ON DELETE CASCADE,
	CONSTRAINT fk_singoli_versamenti_2 FOREIGN KEY (id_tributo) REFERENCES tributi(id) ON DELETE CASCADE,
	CONSTRAINT pk_singoli_versamenti PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;

-- index
CREATE INDEX index_singoli_versamenti_1 ON singoli_versamenti (id_versamento,indice);



CREATE TABLE rpt
(
	cod_carrello VARCHAR(255),
	iuv VARCHAR(255) NOT NULL,
	ccp VARCHAR(255) NOT NULL,
	cod_dominio VARCHAR(255) NOT NULL,
	tipo_versamento VARCHAR(255),
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
	data_ora_msg_richiesta TIMESTAMP(3) DEFAULT 0,
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
	data_ora_creazione TIMESTAMP(3) NOT NULL DEFAULT 0,
	cod_msg_richiesta VARCHAR(255) NOT NULL,
	iban_addebito VARCHAR(255),
	autenticazione_soggetto VARCHAR(255),
	firma_rt VARCHAR(255) NOT NULL,
	stato VARCHAR(255) NOT NULL,
	descrizione_stato LONGTEXT,
	cod_fault VARCHAR(255),
	callback_url LONGTEXT,
	cod_sessione VARCHAR(255),
	psp_redirect_url VARCHAR(512),
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT,
	id_versamento BIGINT NOT NULL,
	id_psp BIGINT,
	id_canale BIGINT,
	id_portale BIGINT,
	id_tracciato_xml BIGINT,
	id_stazione BIGINT,
	id_anagrafica_versante BIGINT,
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
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;

-- index
CREATE INDEX index_rpt_1 ON rpt (cod_msg_richiesta);
CREATE INDEX index_rpt_2 ON rpt (iuv,ccp,cod_dominio);



CREATE TABLE carrelli
(
	cod_carrello VARCHAR(255) NOT NULL,
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT,
	id_rpt BIGINT NOT NULL,
	-- unique constraints
	CONSTRAINT unique_carrelli_1 UNIQUE (id_rpt,cod_carrello),
	-- fk/pk keys constraints
	CONSTRAINT fk_carrelli_1 FOREIGN KEY (id_rpt) REFERENCES rpt(id) ON DELETE CASCADE,
	CONSTRAINT pk_carrelli PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;

-- index
CREATE INDEX index_carrelli_1 ON carrelli (id_rpt,cod_carrello);



CREATE TABLE esiti
(
	cod_dominio VARCHAR(255) NOT NULL,
	iuv VARCHAR(255) NOT NULL,
	stato_spedizione VARCHAR(255) NOT NULL,
	dettaglio_spedizione VARCHAR(255),
	tentativi_spedizione BIGINT,
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
	data_ora_creazione TIMESTAMP(3) NOT NULL DEFAULT 0,
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
	data_ora_ultima_spedizione TIMESTAMP(3) DEFAULT 0,
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
	data_ora_prossima_spedizione TIMESTAMP(3) DEFAULT 0,
	xml MEDIUMBLOB NOT NULL,
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT,
	id_applicazione BIGINT NOT NULL,
	-- fk/pk keys constraints
	CONSTRAINT fk_esiti_1 FOREIGN KEY (id_applicazione) REFERENCES applicazioni(id) ON DELETE CASCADE,
	CONSTRAINT pk_esiti PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;




CREATE TABLE rt
(
	cod_msg_ricevuta VARCHAR(255) NOT NULL,
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
	data_ora_msg_ricevuta TIMESTAMP(3) NOT NULL DEFAULT 0,
	cod_esito_pagamento INT NOT NULL,
	importo_totale_pagato DOUBLE NOT NULL,
	stato VARCHAR(255) NOT NULL,
	descrizione_stato VARCHAR(255) NOT NULL,
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT,
	id_rpt BIGINT NOT NULL,
	id_tracciato_xml BIGINT NOT NULL,
	id_anagrafica_attestante BIGINT NOT NULL,
	-- unique constraints
	CONSTRAINT unique_rt_1 UNIQUE (cod_msg_ricevuta),
	-- fk/pk keys constraints
	CONSTRAINT fk_rt_1 FOREIGN KEY (id_rpt) REFERENCES rpt(id) ON DELETE CASCADE,
	CONSTRAINT fk_rt_2 FOREIGN KEY (id_tracciato_xml) REFERENCES tracciatixml(id) ON DELETE CASCADE,
	CONSTRAINT fk_rt_3 FOREIGN KEY (id_anagrafica_attestante) REFERENCES anagrafiche(id) ON DELETE CASCADE,
	CONSTRAINT pk_rt PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;

-- index
CREATE INDEX index_rt_1 ON rt (cod_msg_ricevuta);



CREATE TABLE iuv
(
	cod_dominio VARCHAR(255) NOT NULL,
	prg BIGINT NOT NULL,
	iuv VARCHAR(255) NOT NULL,
	data_generazione TIMESTAMP NOT NULL DEFAULT 0,
	application_code INT NOT NULL,
	aux_digit INT NOT NULL,
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT,
	id_applicazione BIGINT NOT NULL,
	-- unique constraints
	CONSTRAINT unique_iuv_1 UNIQUE (cod_dominio,iuv),
	-- fk/pk keys constraints
	CONSTRAINT fk_iuv_1 FOREIGN KEY (id_applicazione) REFERENCES applicazioni(id) ON DELETE CASCADE,
	CONSTRAINT pk_iuv PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;

-- index
CREATE INDEX index_iuv_1 ON iuv (cod_dominio,iuv);



CREATE TABLE eventi
(
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
	data_ora_evento TIMESTAMP(3) NOT NULL DEFAULT 0,
	cod_dominio VARCHAR(255),
	iuv VARCHAR(255),
	id_applicazione BIGINT,
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
	id BIGINT AUTO_INCREMENT,
	-- fk/pk keys constraints
	CONSTRAINT pk_eventi PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;




CREATE TABLE sla
(
	descrizione VARCHAR(255) NOT NULL,
	tipo_evento_iniziale VARCHAR(255) NOT NULL,
	sottotipo_evento_iniziale VARCHAR(255) NOT NULL,
	tipo_evento_finale VARCHAR(255) NOT NULL,
	sottotipo_evento_finale VARCHAR(255) NOT NULL,
	tempo_a BIGINT NOT NULL,
	tempo_b BIGINT NOT NULL,
	tolleranza_a DOUBLE NOT NULL,
	tolleranza_b DOUBLE NOT NULL,
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT,
	-- fk/pk keys constraints
	CONSTRAINT pk_sla PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;




CREATE TABLE rilevamenti
(
	id_applicazione BIGINT,
	data_rilevamento TIMESTAMP NOT NULL DEFAULT 0,
	durata BIGINT NOT NULL,
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT,
	id_sla BIGINT NOT NULL,
	id_evento_iniziale BIGINT NOT NULL,
	id_evento_finale BIGINT NOT NULL,
	-- fk/pk keys constraints
	CONSTRAINT fk_rilevamenti_1 FOREIGN KEY (id_sla) REFERENCES sla(id) ON DELETE CASCADE,
	CONSTRAINT fk_rilevamenti_2 FOREIGN KEY (id_evento_iniziale) REFERENCES eventi(id) ON DELETE CASCADE,
	CONSTRAINT fk_rilevamenti_3 FOREIGN KEY (id_evento_finale) REFERENCES eventi(id) ON DELETE CASCADE,
	CONSTRAINT pk_rilevamenti PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;




CREATE TABLE medie_rilevamenti
(
	id_applicazione BIGINT,
	data_osservazione TIMESTAMP NOT NULL DEFAULT 0,
	num_rilevamenti_a BIGINT NOT NULL,
	percentuale_a DOUBLE NOT NULL,
	num_rilevamenti_b BIGINT NOT NULL,
	percentuale_b DOUBLE NOT NULL,
	num_rilevamenti_over BIGINT NOT NULL,
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT,
	id_sla BIGINT NOT NULL,
	-- unique constraints
	CONSTRAINT unique_medie_rilevamenti_1 UNIQUE (id_sla,data_osservazione),
	-- fk/pk keys constraints
	CONSTRAINT fk_medie_rilevamenti_1 FOREIGN KEY (id_sla) REFERENCES sla(id) ON DELETE CASCADE,
	CONSTRAINT pk_medie_rilevamenti PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;

-- index
CREATE INDEX index_medie_rilevamenti_1 ON medie_rilevamenti (id_sla,data_osservazione);



CREATE TABLE rr
(
	cod_msg_revoca VARCHAR(255) NOT NULL,
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
	data_ora_msg_revoca TIMESTAMP(3) NOT NULL DEFAULT 0,
	importo_totale_revocato DOUBLE NOT NULL,
	stato VARCHAR(255) NOT NULL,
	descrizione_stato VARCHAR(255),
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
	data_ora_creazione TIMESTAMP(3) NOT NULL DEFAULT 0,
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT,
	id_rt BIGINT NOT NULL,
	id_tracciato_xml BIGINT NOT NULL,
	-- unique constraints
	CONSTRAINT unique_rr_1 UNIQUE (cod_msg_revoca),
	-- fk/pk keys constraints
	CONSTRAINT fk_rr_1 FOREIGN KEY (id_rt) REFERENCES rt(id) ON DELETE CASCADE,
	CONSTRAINT fk_rr_2 FOREIGN KEY (id_tracciato_xml) REFERENCES tracciatixml(id) ON DELETE CASCADE,
	CONSTRAINT pk_rr PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;

-- index
CREATE INDEX index_rr_1 ON rr (cod_msg_revoca);



CREATE TABLE er
(
	cod_msg_esito VARCHAR(255) NOT NULL,
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
	data_ora_msg_esito TIMESTAMP(3) NOT NULL DEFAULT 0,
	importo_totale_revocato DOUBLE NOT NULL,
	stato VARCHAR(255) NOT NULL,
	descrizione_stato VARCHAR(255),
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
	data_ora_creazione TIMESTAMP(3) NOT NULL DEFAULT 0,
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT,
	id_rr BIGINT NOT NULL,
	id_tracciato_xml BIGINT NOT NULL,
	-- unique constraints
	CONSTRAINT unique_er_1 UNIQUE (cod_msg_esito),
	-- fk/pk keys constraints
	CONSTRAINT fk_er_1 FOREIGN KEY (id_rr) REFERENCES rr(id) ON DELETE CASCADE,
	CONSTRAINT fk_er_2 FOREIGN KEY (id_tracciato_xml) REFERENCES tracciatixml(id) ON DELETE CASCADE,
	CONSTRAINT pk_er PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;

-- index
CREATE INDEX index_er_1 ON er (cod_msg_esito);



CREATE TABLE singole_revoche
(
	id_er BIGINT,
	causale_revoca VARCHAR(255) NOT NULL,
	dati_aggiuntivi_revoca VARCHAR(255) NOT NULL,
	singolo_importo DOUBLE NOT NULL,
	singolo_importo_revocato DOUBLE,
	causale_esito VARCHAR(255),
	dati_aggiuntivi_esito VARCHAR(255),
	stato VARCHAR(255) NOT NULL,
	descrizione_stato VARCHAR(255),
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT,
	id_rr BIGINT NOT NULL,
	id_singolo_versamento BIGINT NOT NULL,
	-- fk/pk keys constraints
	CONSTRAINT fk_singole_revoche_1 FOREIGN KEY (id_rr) REFERENCES rr(id) ON DELETE CASCADE,
	CONSTRAINT fk_singole_revoche_2 FOREIGN KEY (id_singolo_versamento) REFERENCES singoli_versamenti(id) ON DELETE CASCADE,
	CONSTRAINT pk_singole_revoche PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;




CREATE TABLE fr
(
	cod_flusso VARCHAR(255) NOT NULL,
	anno_riferimento INT NOT NULL,
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
	data_ora_flusso TIMESTAMP(3) NOT NULL DEFAULT 0,
	iur VARCHAR(255) NOT NULL,
	data_regolamento TIMESTAMP NOT NULL DEFAULT 0,
	numero_pagamenti BIGINT NOT NULL,
	importo_totale_pagamenti DOUBLE NOT NULL,
	stato VARCHAR(255) NOT NULL,
	descrizione_stato LONGTEXT,
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT,
	id_tracciato_xml BIGINT NOT NULL,
	id_psp BIGINT NOT NULL,
	-- unique constraints
	CONSTRAINT unique_fr_1 UNIQUE (cod_flusso,anno_riferimento),
	-- fk/pk keys constraints
	CONSTRAINT fk_fr_1 FOREIGN KEY (id_tracciato_xml) REFERENCES tracciatixml(id) ON DELETE CASCADE,
	CONSTRAINT fk_fr_2 FOREIGN KEY (id_psp) REFERENCES psp(id) ON DELETE CASCADE,
	CONSTRAINT pk_fr PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;

-- index
CREATE INDEX index_fr_1 ON fr (cod_flusso,anno_riferimento);



CREATE TABLE singole_rendicontazioni
(
	iuv VARCHAR(255) NOT NULL,
	iur VARCHAR(255) NOT NULL,
	singolo_importo DOUBLE NOT NULL,
	codice_esito INT NOT NULL,
	data_esito TIMESTAMP NOT NULL DEFAULT 0,
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT,
	id_fr BIGINT NOT NULL,
	id_singolo_versamento BIGINT,
	-- fk/pk keys constraints
	CONSTRAINT fk_singole_rendicontazioni_1 FOREIGN KEY (id_fr) REFERENCES fr(id) ON DELETE CASCADE,
	CONSTRAINT fk_singole_rendicontazioni_2 FOREIGN KEY (id_singolo_versamento) REFERENCES singoli_versamenti(id) ON DELETE CASCADE,
	CONSTRAINT pk_singole_rendicontazioni PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;




CREATE TABLE mail
(
	tipo_mail VARCHAR(255) NOT NULL,
	bundle_key BIGINT NOT NULL,
	id_versamento BIGINT,
	mittente VARCHAR(255) NOT NULL,
	destinatario VARCHAR(255) NOT NULL,
	cc LONGTEXT,
	oggetto LONGTEXT NOT NULL,
	messaggio LONGTEXT NOT NULL,
	stato_spedizione VARCHAR(255) NOT NULL,
	dettaglio_errore_spedizione VARCHAR(255),
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
	data_ora_ultima_spedizione TIMESTAMP(3) DEFAULT 0,
	tentativi_rispedizione BIGINT,
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT,
	id_tracciato_rpt BIGINT,
	id_tracciato_rt BIGINT,
	-- fk/pk keys constraints
	CONSTRAINT fk_mail_1 FOREIGN KEY (id_tracciato_rpt) REFERENCES tracciatixml(id) ON DELETE CASCADE,
	CONSTRAINT fk_mail_2 FOREIGN KEY (id_tracciato_rt) REFERENCES tracciatixml(id) ON DELETE CASCADE,
	CONSTRAINT pk_mail PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;




CREATE TABLE tabella_controparti
(
	id_flusso VARCHAR(255) NOT NULL,
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
	data_ora_pubblicazione TIMESTAMP(3) NOT NULL DEFAULT 0,
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
	data_ora_inizio_validita TIMESTAMP(3) NOT NULL DEFAULT 0,
	xml MEDIUMBLOB NOT NULL,
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT,
	id_dominio BIGINT NOT NULL,
	-- unique constraints
	CONSTRAINT unique_tabella_controparti_1 UNIQUE (id_flusso,id_dominio),
	-- fk/pk keys constraints
	CONSTRAINT fk_tabella_controparti_1 FOREIGN KEY (id_dominio) REFERENCES domini(id) ON DELETE CASCADE,
	CONSTRAINT pk_tabella_controparti PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;

-- index
CREATE INDEX index_tabella_controparti_1 ON tabella_controparti (id_flusso,id_dominio);



CREATE TABLE conti_accredito
(
	id_flusso VARCHAR(255) NOT NULL,
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
	data_ora_pubblicazione TIMESTAMP(3) NOT NULL DEFAULT 0,
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
	data_ora_inizio_validita TIMESTAMP(3) NOT NULL DEFAULT 0,
	xml MEDIUMBLOB NOT NULL,
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT,
	id_dominio BIGINT NOT NULL,
	-- unique constraints
	CONSTRAINT unique_conti_accredito_1 UNIQUE (id_flusso,id_dominio),
	-- fk/pk keys constraints
	CONSTRAINT fk_conti_accredito_1 FOREIGN KEY (id_dominio) REFERENCES domini(id) ON DELETE CASCADE,
	CONSTRAINT pk_conti_accredito PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;

-- index
CREATE INDEX index_conti_accredito_1 ON conti_accredito (id_flusso,id_dominio);



CREATE TABLE id_messaggio_relativo
(
	COUNTER BIGINT NOT NULL,
	PROTOCOLLO VARCHAR(255) NOT NULL,
	INFO_ASSOCIATA VARCHAR(255) NOT NULL,
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
	ora_registrazione TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP(3),
	-- fk/pk columns
	-- fk/pk keys constraints
	CONSTRAINT pk_id_messaggio_relativo PRIMARY KEY (COUNTER,PROTOCOLLO,INFO_ASSOCIATA)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;

