-- Per versioni successive alla 5.7, rimuovere dalla sql_mode NO_ZERO_DATE
-- SET @@SESSION.sql_mode=(SELECT REPLACE(@@SESSION.sql_mode,'NO_ZERO_DATE',''));


CREATE TABLE configurazione
(
	nome VARCHAR(255) NOT NULL,
	valore LONGTEXT,
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT,
	-- unique constraints
	CONSTRAINT unique_configurazione_1 UNIQUE (nome),
	-- fk/pk keys constraints
	CONSTRAINT pk_configurazione PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;

-- index
CREATE UNIQUE INDEX index_configurazione_1 ON configurazione (nome);



CREATE TABLE intermediari
(
	cod_intermediario VARCHAR(35) NOT NULL COMMENT 'Identificativo intermediario su pagopa',
	cod_connettore_pdd VARCHAR(35) NOT NULL COMMENT 'Riferimento alle properties in tabella connettori di configurazione dele connettore http verso pagopa',
	cod_connettore_ftp VARCHAR(35) COMMENT 'Riferimento alle properties in tabella connettori di configurazione dele connettore ftp verso pagopa',
	denominazione VARCHAR(255) NOT NULL COMMENT 'Nome dell\'intermediario',
	principal VARCHAR(756) NOT NULL COMMENT 'Principal in forma canonica con cui si autentica l\'intermediario a govpay',
	principal_originale VARCHAR(756) NOT NULL COMMENT 'Principal con cui si autentica l\'intermediario a govpay',
	abilitato BOOLEAN NOT NULL COMMENT 'Indicazione se e\' abilitato ad operare',
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT COMMENT 'Identificativo fisico',
	-- unique constraints
	CONSTRAINT unique_intermediari_1 UNIQUE (cod_intermediario),
	-- fk/pk keys constraints
	CONSTRAINT pk_intermediari PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs COMMENT 'Intermediari pagoPA';

-- index
CREATE UNIQUE INDEX index_intermediari_1 ON intermediari (cod_intermediario);



CREATE TABLE stazioni
(
	cod_stazione VARCHAR(35) NOT NULL COMMENT 'Identificativo della stazione su pagopa',
	password VARCHAR(35) NOT NULL COMMENT 'Password assegnata da pagopa',
	abilitato BOOLEAN NOT NULL COMMENT 'Indicazione se e\' abilitato ad operare',
	application_code INT NOT NULL COMMENT 'Application code estratto della stazione',
	ndp_stato INT COMMENT 'Stato dell\'ultima interazione con pagopa',
	ndp_operazione VARCHAR(256) COMMENT 'Ultima operazione richiesta a pagopa',
	ndp_descrizione VARCHAR(1024) COMMENT 'Descrizione dell\'esito dell\'ultima interazione con pagopa',
	ndp_data TIMESTAMP(3) COMMENT 'Data dell\'ultima interazione con pagopa',
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT COMMENT 'Identificativo fisico',
	id_intermediario BIGINT NOT NULL COMMENT 'Riferimento all\'intermediario',
	-- unique constraints
	CONSTRAINT unique_stazioni_1 UNIQUE (cod_stazione),
	-- fk/pk keys constraints
	CONSTRAINT fk_stz_id_intermediario FOREIGN KEY (id_intermediario) REFERENCES intermediari(id),
	CONSTRAINT pk_stazioni PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs COMMENT 'Stazioni pagoPA';

-- index
CREATE UNIQUE INDEX index_stazioni_1 ON stazioni (cod_stazione);



CREATE TABLE utenze
(
	principal VARCHAR(756) NOT NULL COMMENT 'Principal di autenticazione in forma canonica',
	principal_originale VARCHAR(756) NOT NULL COMMENT 'Principal di autenticazione in forma originale',
	abilitato BOOLEAN NOT NULL DEFAULT true COMMENT 'Indicazione se e\' abilitato ad operare',
	autorizzazione_domini_star BOOLEAN NOT NULL DEFAULT false COMMENT 'Indicazione se l\'utenza e\' autorizzata ad operare su tutti i domini',
	autorizzazione_tipi_vers_star BOOLEAN NOT NULL DEFAULT false COMMENT 'Indicazione se l\'utenza e\' autorizzata ad operare su tutti i tipi pendenza',
	ruoli VARCHAR(512) COMMENT 'Ruoli associati all\'utenza',
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT COMMENT 'Identificativo fisico',
	-- unique constraints
	CONSTRAINT unique_utenze_1 UNIQUE (principal),
	-- fk/pk keys constraints
	CONSTRAINT pk_utenze PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs COMMENT 'Utenze di sistema';

-- index
CREATE UNIQUE INDEX index_utenze_1 ON utenze (principal);



CREATE TABLE applicazioni
(
	cod_applicazione VARCHAR(35) NOT NULL COMMENT 'Codice identificativo del verticale',
	auto_iuv BOOLEAN NOT NULL COMMENT 'Indicazione se il verticale genera in autonomia gli IUV dei pagamenti',
	firma_ricevuta VARCHAR(1) NOT NULL COMMENT 'Indicazione se le RT ricevute da pagoPA devono essere richieste firmate',
	cod_connettore_integrazione VARCHAR(255) COMMENT 'Riferimento alle properties di configurazione del connettore per il servizio di integrazione',
	trusted BOOLEAN NOT NULL COMMENT 'Indicazione se il verticale e\' autorizzato alla definizione delle entrate',
	cod_applicazione_iuv VARCHAR(3) COMMENT 'Codifica del verticale negli IUV',
	reg_exp VARCHAR(1024) COMMENT 'Espressione regolare per l\'identificazione degli IUV afferenti al verticale',
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT COMMENT 'Identificativo fisico',
	id_utenza BIGINT NOT NULL COMMENT 'Riferimento all\'utenza che identifica il verticale',
	-- unique constraints
	CONSTRAINT unique_applicazioni_1 UNIQUE (cod_applicazione),
	CONSTRAINT unique_applicazioni_2 UNIQUE (id_utenza),
	-- fk/pk keys constraints
	CONSTRAINT fk_app_id_utenza FOREIGN KEY (id_utenza) REFERENCES utenze(id),
	CONSTRAINT pk_applicazioni PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs COMMENT 'Verticali abilitati';

-- index
CREATE UNIQUE INDEX index_applicazioni_1 ON applicazioni (cod_applicazione);
CREATE UNIQUE INDEX index_applicazioni_2 ON applicazioni (id_utenza);



CREATE TABLE domini
(
	cod_dominio VARCHAR(35) NOT NULL COMMENT 'Identificativo del\'ente creditore su pagopa',
	gln VARCHAR(35) COMMENT 'Global location number assegnato da pagopa',
	abilitato BOOLEAN NOT NULL COMMENT 'Indicazione se e\' abilitato ad operare',
	ragione_sociale VARCHAR(70) NOT NULL COMMENT 'Ragione sociale dell\'ente creditore',
	aux_digit INT NOT NULL DEFAULT 0 COMMENT 'Aux digit di generazione dei numeri avviso',
	iuv_prefix VARCHAR(255) COMMENT 'Sintassi del prefisso degli iuv generati per il dominio',
	segregation_code INT COMMENT 'Codice di segregazione in caso di aux digit multi-intermediato',
	ndp_stato INT COMMENT 'Stato dell\'ultima interazione con pagopa',
	ndp_operazione VARCHAR(256) COMMENT 'Ultima operazione richiesta a pagopa',
	ndp_descrizione VARCHAR(1024) COMMENT 'Descrizione dell\'esito dell\'ultima interazione con pagopa',
	ndp_data TIMESTAMP(3) COMMENT 'Data dell\'ultima interazione con pagopa',
	logo MEDIUMBLOB COMMENT 'Logo dell\'ente creditore codificato in base64',
	cbill VARCHAR(255) COMMENT 'Codice cbill assegnato da pagopa in caso di adesione su modello 3',
	aut_stampa_poste VARCHAR(255) COMMENT 'Autorizzazione alla stampa in poprio di poste italiane',
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT COMMENT 'Identificativo fisico',
	id_stazione BIGINT NOT NULL COMMENT 'Riferimento alla stazione',
	id_applicazione_default BIGINT COMMENT 'Rferimento all\'appplicazione di default in caso di non risoluzione dello iuv',
	-- unique constraints
	CONSTRAINT unique_domini_1 UNIQUE (cod_dominio),
	-- fk/pk keys constraints
	CONSTRAINT fk_dom_id_stazione FOREIGN KEY (id_stazione) REFERENCES stazioni(id),
	CONSTRAINT fk_dom_id_applicazione_default FOREIGN KEY (id_applicazione_default) REFERENCES applicazioni(id),
	CONSTRAINT pk_domini PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs COMMENT 'Enti creditori';

-- index
CREATE UNIQUE INDEX index_domini_1 ON domini (cod_dominio);



CREATE TABLE iban_accredito
(
	cod_iban VARCHAR(255) NOT NULL COMMENT 'Iban del conto di accredito',
	bic_accredito VARCHAR(255) COMMENT 'Bic del conto di accredito',
	postale BOOLEAN NOT NULL COMMENT 'Indicazione se il conto di accredito e\' postale',
	attivato BOOLEAN NOT NULL COMMENT 'Indicazione se il conto e\' stato attivato su pagopa',
	abilitato BOOLEAN NOT NULL COMMENT 'Indicazione se e\' abilitato ad operare',
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT COMMENT 'Identificativo fisico',
	id_dominio BIGINT NOT NULL COMMENT 'Riferimento al Ente proprietario del conto',
	-- unique constraints
	CONSTRAINT unique_iban_accredito_1 UNIQUE (cod_iban,id_dominio),
	-- fk/pk keys constraints
	CONSTRAINT fk_iba_id_dominio FOREIGN KEY (id_dominio) REFERENCES domini(id),
	CONSTRAINT pk_iban_accredito PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs COMMENT 'Conti di accredito';

-- index
CREATE UNIQUE INDEX index_iban_accredito_1 ON iban_accredito (cod_iban,id_dominio);



CREATE TABLE tipi_tributo
(
	cod_tributo VARCHAR(255) NOT NULL COMMENT 'Codice identificativo dell\'entrata',
	descrizione VARCHAR(255) COMMENT 'Descrizione dell\'entrata',
	tipo_contabilita VARCHAR(1) COMMENT 'Tipo di codifica della contabilita',
	cod_contabilita VARCHAR(255) COMMENT 'Codifica della contabilita',
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT COMMENT 'Identificativo fisico',
	-- unique constraints
	CONSTRAINT unique_tipi_tributo_1 UNIQUE (cod_tributo),
	-- fk/pk keys constraints
	CONSTRAINT pk_tipi_tributo PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs COMMENT 'Tipologie entrate';

-- index
CREATE UNIQUE INDEX index_tipi_tributo_1 ON tipi_tributo (cod_tributo);



CREATE TABLE tributi
(
	abilitato BOOLEAN NOT NULL COMMENT 'Indicazione se e\' abilitato ad operare',
	tipo_contabilita VARCHAR(1) COMMENT 'Tipo di codifica della contabilita',
	codice_contabilita VARCHAR(255) COMMENT 'Codifica della contabilita',
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT COMMENT 'Identificativo fisico',
	id_dominio BIGINT NOT NULL COMMENT 'Riferimento al dominio',
	id_iban_accredito BIGINT COMMENT 'Conto di accredito dell\'entrata',
	id_iban_appoggio BIGINT COMMENT 'Conto di appoggio dell\'entrata',
	id_tipo_tributo BIGINT NOT NULL COMMENT 'Riferimento alla tipologia di entrata',
	-- unique constraints
	CONSTRAINT unique_tributi_1 UNIQUE (id_dominio,id_tipo_tributo),
	-- fk/pk keys constraints
	CONSTRAINT fk_trb_id_dominio FOREIGN KEY (id_dominio) REFERENCES domini(id),
	CONSTRAINT fk_trb_id_iban_accredito FOREIGN KEY (id_iban_accredito) REFERENCES iban_accredito(id),
	CONSTRAINT fk_trb_id_iban_appoggio FOREIGN KEY (id_iban_appoggio) REFERENCES iban_accredito(id),
	CONSTRAINT fk_trb_id_tipo_tributo FOREIGN KEY (id_tipo_tributo) REFERENCES tipi_tributo(id),
	CONSTRAINT pk_tributi PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs COMMENT 'Istanze di entrate';

-- index
CREATE UNIQUE INDEX index_tributi_1 ON tributi (id_dominio,id_tipo_tributo);



CREATE TABLE uo
(
	cod_uo VARCHAR(35) NOT NULL COMMENT 'Codice identificativo dell\'unita operativa',
	abilitato BOOLEAN NOT NULL COMMENT 'Indicazione se e\' abilitato ad operare',
	uo_codice_identificativo VARCHAR(35) COMMENT 'Codice fiscale dell\'unita operativa',
	uo_denominazione VARCHAR(70) COMMENT 'Denominazione dell\'unita operativa',
	uo_indirizzo VARCHAR(70) COMMENT 'Indirizzo dell\'unita operativa',
	uo_civico VARCHAR(16) COMMENT 'Numero civico dell\'unita operativa',
	uo_cap VARCHAR(16) COMMENT 'Cap dell\'unita operativa',
	uo_localita VARCHAR(35) COMMENT 'Localita dell\'unita operativa',
	uo_provincia VARCHAR(35) COMMENT 'Provincia dell\'unita operativa',
	uo_nazione VARCHAR(2) COMMENT 'Sigla della nazione dell\'unita operativa',
	uo_area VARCHAR(255) COMMENT 'Descrizione dell\'area di competenza dell\'unita operativa',
	uo_url_sito_web VARCHAR(255) COMMENT 'URL del sito di riferimento dell\'unita operativa',
	uo_email VARCHAR(255) COMMENT 'Indirizzo email ordinario dell\'unita operativa',
	uo_pec VARCHAR(255) COMMENT 'Indirizzo email certificato dell\'unita operativa',
	uo_tel VARCHAR(255) COMMENT 'Numero di telefono dell\'unita operativa',
	uo_fax VARCHAR(255) COMMENT 'Numero di fax dell\'unita operativa',
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT COMMENT 'Identificativo fisico',
	id_dominio BIGINT NOT NULL COMMENT 'Riferimento al dominio',
	-- unique constraints
	CONSTRAINT unique_uo_1 UNIQUE (cod_uo,id_dominio),
	-- fk/pk keys constraints
	CONSTRAINT fk_uo_id_dominio FOREIGN KEY (id_dominio) REFERENCES domini(id),
	CONSTRAINT pk_uo PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs COMMENT 'Unita operative';

-- index
CREATE UNIQUE INDEX index_uo_1 ON uo (cod_uo,id_dominio);



CREATE TABLE utenze_domini
(
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT COMMENT 'Identificativo fisico',
	id_utenza BIGINT NOT NULL COMMENT 'Riferimento all\'utenza',
	id_dominio BIGINT COMMENT 'Riferimento al dominio',
	id_uo BIGINT COMMENT 'Riferimento alla uo',
	-- fk/pk keys constraints
	CONSTRAINT fk_nzd_id_utenza FOREIGN KEY (id_utenza) REFERENCES utenze(id),
	CONSTRAINT fk_nzd_id_dominio FOREIGN KEY (id_dominio) REFERENCES domini(id),
	CONSTRAINT fk_nzd_id_uo FOREIGN KEY (id_uo) REFERENCES uo(id),
	CONSTRAINT pk_utenze_domini PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs COMMENT 'Autorizzazioni sui domini';




CREATE TABLE operatori
(
	nome VARCHAR(35) NOT NULL COMMENT 'Nome dell\'operatore',
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT COMMENT 'Identificativo fisico',
	id_utenza BIGINT NOT NULL COMMENT 'Riferimento all\'utenza dell\'operatore',
	-- unique constraints
	CONSTRAINT unique_operatori_1 UNIQUE (id_utenza),
	-- fk/pk keys constraints
	CONSTRAINT fk_opr_id_utenza FOREIGN KEY (id_utenza) REFERENCES utenze(id),
	CONSTRAINT pk_operatori PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs COMMENT 'Operatori di backoffice';

-- index
CREATE UNIQUE INDEX index_operatori_1 ON operatori (id_utenza);



CREATE TABLE connettori
(
	cod_connettore VARCHAR(255) NOT NULL COMMENT 'Codice identificativo del connettore',
	cod_proprieta VARCHAR(255) NOT NULL COMMENT 'Codice identificativo della proprieta',
	valore VARCHAR(255) NOT NULL COMMENT 'Valore della proprieta',
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT COMMENT 'Identificativo fisico',
	-- unique constraints
	CONSTRAINT unique_connettori_1 UNIQUE (cod_connettore,cod_proprieta),
	-- fk/pk keys constraints
	CONSTRAINT pk_connettori PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;

-- index
CREATE UNIQUE INDEX index_connettori_1 ON connettori (cod_connettore,cod_proprieta) COMMENT 'Configurazione dei connettori';



CREATE TABLE acl
(
	ruolo VARCHAR(255) COMMENT 'Ruolo a cui si riferisce la ACL',
	servizio VARCHAR(255) NOT NULL COMMENT 'Servizio su cui si riferisce l\'ACL',
	diritti VARCHAR(255) NOT NULL COMMENT 'Autorizzazione dell\'ACL',
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT COMMENT 'Identificativo fisico',
	id_utenza BIGINT COMMENT 'Id fisico dell\'utenza a cui si riferisce l\'ACL',
	-- fk/pk keys constraints
	CONSTRAINT fk_acl_id_utenza FOREIGN KEY (id_utenza) REFERENCES utenze(id),
	CONSTRAINT pk_acl PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs COMMENT 'Autorizzazioni di accesso';




CREATE TABLE tracciati
(
	cod_dominio VARCHAR(35) NOT NULL COMMENT 'Identificativo del dominio a cui si riferisce il tracciato',
	cod_tipo_versamento VARCHAR(35) COMMENT 'Identificativo del tipo versamento',
	formato VARCHAR(10) NOT NULL COMMENT 'tipo formato del tracciato CSV/JSON',
	tipo VARCHAR(10) NOT NULL COMMENT 'Tipologia di tracciato',
	stato VARCHAR(12) NOT NULL COMMENT 'Stato di lavorazione del tracciato',
	descrizione_stato VARCHAR(256) COMMENT 'Descrizione dello stato di lavorazione del tracciato',
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
	data_caricamento TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT 'Data di caricamento del tracciato',
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
	data_completamento TIMESTAMP(3) COMMENT 'Data di fine lavorazione del tracciato',
	bean_dati LONGTEXT COMMENT 'Dataset custom del tracciato',
	file_name_richiesta VARCHAR(256) COMMENT 'Filename del tracciato',
	raw_richiesta MEDIUMBLOB COMMENT 'Tracciato nel formato originale',
	file_name_esito VARCHAR(256) COMMENT 'Filename del tracciato di esito',
	raw_esito MEDIUMBLOB COMMENT 'Tracciato di esito nel formato originale',
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT COMMENT 'Identificativo fisico',
	id_operatore BIGINT COMMENT 'Riferimento all\'operatore di backoffice che ha caricato il tracciato',
	-- fk/pk keys constraints
	CONSTRAINT fk_trc_id_operatore FOREIGN KEY (id_operatore) REFERENCES operatori(id),
	CONSTRAINT pk_tracciati PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;




CREATE TABLE tipi_versamento
(
	cod_tipo_versamento VARCHAR(35) NOT NULL COMMENT 'Identificativo della tipologia pendenza',
	descrizione VARCHAR(255) NOT NULL COMMENT 'descrizione della tipologia pendenza',
	codifica_iuv VARCHAR(4) COMMENT 'Codifica del tipo pendenza nello iuv',
	tipo VARCHAR(35) NOT NULL COMMENT 'Indica se il tipo pendenza e\' pagabile spontaneamente',
	paga_terzi BOOLEAN NOT NULL DEFAULT false COMMENT 'Indica se il tipo pendenza e\' pagabile da soggetti terzi',
	abilitato BOOLEAN NOT NULL COMMENT 'Indicazione se e\' abilitato ad operare',
	form_tipo VARCHAR(35) COMMENT 'Tipo di linguaggio per il disegno della form',
	form_definizione LONGTEXT COMMENT 'Definizione della form nel linguaggio indicato',
	validazione_definizione LONGTEXT COMMENT 'Definizione dello schema per la validazione della pendenza',
	trasformazione_tipo VARCHAR(35) COMMENT 'Tipo di trasformazione richiesta',
	trasformazione_definizione LONGTEXT COMMENT 'Template di trasformazione',
	cod_applicazione VARCHAR(35) COMMENT 'Identificativo dell\'applicazione a cui inoltrare la pendenza',
	promemoria_avviso_abilitato BOOLEAN NOT NULL DEFAULT false COMMENT 'Indica se spedire l\'avviso',
	promemoria_avviso_tipo VARCHAR(35) COMMENT 'Tipo template promemoria avviso',
	promemoria_avviso_pdf BOOLEAN COMMENT 'Indica se inserire l\'avviso di pagamento come allegato alla mail',
	promemoria_avviso_oggetto LONGTEXT COMMENT 'Template della mail del promemoria avviso',
	promemoria_avviso_messaggio LONGTEXT COMMENT 'Messaggio della mail del promemoria avviso',
	promemoria_ricevuta_abilitato BOOLEAN NOT NULL DEFAULT false COMMENT 'Indica se spedire la ricevuta',
	promemoria_ricevuta_tipo VARCHAR(35)  COMMENT 'Tipo template promemoria ricevuta',
	promemoria_ricevuta_pdf BOOLEAN COMMENT 'Indica se inserire la ricevuta di pagamento come allegato alla mail',
	promemoria_ricevuta_oggetto LONGTEXT COMMENT 'Template della mail del promemoria ricevuta',
	promemoria_ricevuta_messaggio LONGTEXT COMMENT 'Messaggio della mail del promemoria ricevuta',
	visualizzazione_definizione LONGTEXT COMMENT 'Definisce la visualizzazione custom della tipologia',
	trac_csv_tipo VARCHAR(35) COMMENT 'Indica il tipo di template di conversione',
	trac_csv_header_risposta LONGTEXT COMMENT 'Header del file Csv di risposta del tracciato',
	trac_csv_template_richiesta LONGTEXT COMMENT 'Template di conversione della pendenza da CSV a JSON',
	trac_csv_template_risposta LONGTEXT COMMENT 'Template di conversione della pendenza da JSON a CSV',
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT COMMENT 'Identificativo fisico',
	-- unique constraints
	CONSTRAINT unique_tipi_versamento_1 UNIQUE (cod_tipo_versamento),
	-- fk/pk keys constraints
	CONSTRAINT pk_tipi_versamento PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;

-- index
CREATE UNIQUE INDEX index_tipi_versamento_1 ON tipi_versamento (cod_tipo_versamento);



CREATE TABLE tipi_vers_domini
(
	codifica_iuv VARCHAR(4) COMMENT 'Codifica del tipo pendenza nello iuv specifica per dominio',
	tipo VARCHAR(35) COMMENT 'Indica se il tipo pendenza e\' pagabile spontaneamente per il dominio',
	paga_terzi BOOLEAN  COMMENT 'Indica se il tipo pendenza e\' pagabile da soggetti terzi per il dominio',
	abilitato BOOLEAN COMMENT 'Indicazione se e\' abilitato ad operare',
	form_tipo VARCHAR(35) COMMENT 'Tipo di linguaggio per il disegno della form',
	form_definizione LONGTEXT COMMENT 'Definizione della form nel linguaggio indicato',
	validazione_definizione LONGTEXT COMMENT 'Definizione dello schema per la validazione della pendenza',
	trasformazione_tipo VARCHAR(35) COMMENT 'Tipo di trasformazione richiesta',
	trasformazione_definizione LONGTEXT COMMENT 'Template di trasformazione',
	cod_applicazione VARCHAR(35) COMMENT 'Identificativo dell\'applicazione a cui inoltrare la pendenza',
	promemoria_avviso_abilitato BOOLEAN COMMENT 'Indica se inviare l\'avviso di pagamento',
	promemoria_avviso_tipo VARCHAR(35) COMMENT 'Tipo template promemoria avviso',
	promemoria_avviso_pdf BOOLEAN COMMENT 'Indica se inserire l\'avviso di pagamento come allegato alla mail',
	promemoria_avviso_oggetto LONGTEXT COMMENT 'Template della mail del promemoria avviso',
	promemoria_avviso_messaggio LONGTEXT COMMENT 'Messaggio della mail del promemoria avviso',
	promemoria_ricevuta_abilitato BOOLEAN COMMENT 'Indica se inviare la ricevuta',
	promemoria_ricevuta_tipo VARCHAR(35)  COMMENT 'Tipo template promemoria ricevuta',
	promemoria_ricevuta_pdf BOOLEAN COMMENT 'Indica se inserire la ricevuta di pagamento come allegato alla mail',
	promemoria_ricevuta_oggetto LONGTEXT COMMENT 'Template della mail del promemoria ricevuta',
	promemoria_ricevuta_messaggio LONGTEXT COMMENT 'Messaggio della mail del promemoria ricevuta',
	visualizzazione_definizione LONGTEXT COMMENT 'Definisce la visualizzazione custom della tipologia',
	trac_csv_tipo VARCHAR(35) COMMENT 'Indica il tipo del template di conversione',
	trac_csv_header_risposta LONGTEXT COMMENT 'Header del file Csv di risposta del tracciato',
	trac_csv_template_richiesta LONGTEXT COMMENT 'Template di conversione della pendenza da CSV a JSON',
	trac_csv_template_risposta LONGTEXT COMMENT 'Template di conversione della pendenza da JSON a CSV',
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT COMMENT 'Identificativo fisico',
	id_tipo_versamento BIGINT NOT NULL COMMENT 'Riferimento al tipo pendenza afferente',
	id_dominio BIGINT NOT NULL COMMENT 'Riferimento al dominio afferente',
	-- unique constraints
	CONSTRAINT unique_tipi_vers_domini_1 UNIQUE (id_dominio,id_tipo_versamento),
	-- fk/pk keys constraints
	CONSTRAINT fk_tvd_id_tipo_versamento FOREIGN KEY (id_tipo_versamento) REFERENCES tipi_versamento(id),
	CONSTRAINT fk_tvd_id_dominio FOREIGN KEY (id_dominio) REFERENCES domini(id),
	CONSTRAINT pk_tipi_vers_domini PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;

-- index
CREATE UNIQUE INDEX index_tipi_vers_domini_1 ON tipi_vers_domini (id_dominio,id_tipo_versamento);



CREATE TABLE utenze_tipo_vers
(
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT COMMENT 'Identificativo fisico',
	id_utenza BIGINT NOT NULL COMMENT 'Riferimento all\'utenza',
	id_tipo_versamento BIGINT NOT NULL COMMENT 'Riferimento al tipo pendenza',
	-- fk/pk keys constraints
	CONSTRAINT fk_utv_id_utenza FOREIGN KEY (id_utenza) REFERENCES utenze(id),
	CONSTRAINT fk_utv_id_tipo_versamento FOREIGN KEY (id_tipo_versamento) REFERENCES tipi_versamento(id),
	CONSTRAINT pk_utenze_tipo_vers PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs COMMENT 'Autorizzazioni sui tipi pendenza';




CREATE TABLE versamenti
(
	cod_versamento_ente VARCHAR(35) NOT NULL COMMENT 'Identificativo della pendenza nel verticale di competenza',
	nome VARCHAR(35) COMMENT 'Nome della pendenza',
	importo_totale DOUBLE NOT NULL COMMENT 'Importo della pendenza',
	stato_versamento VARCHAR(35) NOT NULL COMMENT 'Stato di pagamento della pendenza',
	descrizione_stato VARCHAR(255) COMMENT 'Descrizione dello stato di pagamento della pendenza',
	-- Indica se, decorsa la dataScadenza, deve essere aggiornato da remoto o essere considerato scaduto'
	aggiornabile BOOLEAN NOT NULL COMMENT 'Indica se, decorsa la dataScadenza, deve essere aggiornato da remoto o essere considerato scaduto',
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
	data_creazione TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT 'Data di inserimento della pendenza',
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
	data_validita TIMESTAMP(3) COMMENT 'Data entro la quale i dati della pendenza risultano validi e non richiedono aggiornamenti',
    	-- Per versioni successive alla 5.7, rimuovere dalla sql_mode NO_ZERO_DATE 
	data_scadenza TIMESTAMP(3) COMMENT 'Data oltre la quale la pendenza non e\' pagabile', 
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
	data_ora_ultimo_aggiornamento TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT 'Data dell\'ultima variazione dei dati della pendenza',
	causale_versamento VARCHAR(1024) COMMENT 'Descrizione dell\'avviso di pagamento associato alla pendenza',
	debitore_tipo VARCHAR(1) COMMENT 'Tipo dell\'identificativo del debitore',
	debitore_identificativo VARCHAR(35) NOT NULL COMMENT 'Codice fiscale o partita iva del soggetto debitore',
	debitore_anagrafica VARCHAR(70) NOT NULL COMMENT 'Nome e cognome o ragione sociale del soggetto debitore',
	debitore_indirizzo VARCHAR(70) COMMENT 'Indirizzo del debitore',
	debitore_civico VARCHAR(16) COMMENT 'Numero civico del debitore',
	debitore_cap VARCHAR(16) COMMENT 'Cap del debitore',
	debitore_localita VARCHAR(35) COMMENT 'Localita del debitore',
	debitore_provincia VARCHAR(35) COMMENT 'Provincia del debitore',
	debitore_nazione VARCHAR(2) COMMENT 'Nazione del debitore',
	debitore_email VARCHAR(256) COMMENT 'Email del debitore',
	debitore_telefono VARCHAR(35) COMMENT 'Telefono del debitore',
	debitore_cellulare VARCHAR(35) COMMENT 'Cellulare del debitore',
	debitore_fax VARCHAR(35) COMMENT 'Numero di fax del debitore',
	tassonomia_avviso VARCHAR(35) COMMENT 'Tassonomia dell\'avviso di pagamento secondo la codifica agid',
	tassonomia VARCHAR(35) COMMENT 'Tassonomia della pendenza secondo la codifica dell\'ente',
	cod_lotto VARCHAR(35) COMMENT 'Codice identificativo della cartella di pagamento',
	cod_versamento_lotto VARCHAR(35) COMMENT 'Codice identificativo del versamento all\'interno del lotto',
	cod_anno_tributario VARCHAR(35) COMMENT 'Annualita della pendenza',
	cod_bundlekey VARCHAR(256) COMMENT 'Codice identificativo della pendenza per costruzione di dati noti all\'utente',
	dati_allegati LONGTEXT COMMENT 'Campo dati a disposizione dell\'ente',
	incasso VARCHAR(1) COMMENT 'Indicazione sullo stato di riconciliazione',
	anomalie LONGTEXT COMMENT 'Anomalie riscontrate nel pagamento della pendenza',
	iuv_versamento VARCHAR(35) COMMENT 'IUV di pagamento',
	numero_avviso VARCHAR(35) COMMENT 'Numero avviso associato alla pendenza',
	avvisatura_abilitata BOOLEAN NOT NULL COMMENT 'Indicazione se la pendenza viene avvisata digitalmente',
	avvisatura_da_inviare BOOLEAN NOT NULL COMMENT 'Indicazione sullo stato di avvisatura della pendenza',
	avvisatura_operazione VARCHAR(1) COMMENT 'Indicazione sull\'operazione di avvisatura da comunicare',
	avvisatura_modalita VARCHAR(1) COMMENT 'Indicazione sulla modalita di avvisatura della pendenza',
	avvisatura_tipo_pagamento INT COMMENT 'Tipologia di pagamento nella codifica agid di avvisatura telematica',
	avvisatura_cod_avvisatura VARCHAR(20) COMMENT 'Identificativo di avvisatura',
	ack BOOLEAN NOT NULL COMMENT 'Indicazione sullo stato di ack dell\'avviso telematico',
	anomalo BOOLEAN NOT NULL COMMENT 'Indicazione sullo stato della pendenza',
	divisione VARCHAR(35) COMMENT 'Dati Divisione',
	direzione VARCHAR(35) COMMENT 'Dati Direzione',
	id_sessione VARCHAR(35) COMMENT 'Identificativo univoco assegnato al momento della creazione',
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT COMMENT 'Identificativo fisico',
	id_tipo_versamento_dominio BIGINT NOT NULL COMMENT 'Riferimento al tipo pendenza dominio afferente',
	id_tipo_versamento BIGINT NOT NULL COMMENT 'Riferimento al tipo pendenza afferente',
	id_dominio BIGINT NOT NULL COMMENT 'Riferimento al dominio afferente',
	id_uo BIGINT COMMENT 'Riferimento all\'unita operativa afferente',
	id_applicazione BIGINT NOT NULL COMMENT 'Riferimento al verticale afferente',
	id_tracciato BIGINT COMMENT 'Riferimento al tracciato che ha caricato la pendenza',
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
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs COMMENT 'Arichivio dei pagamenti in attesa';

-- index
CREATE INDEX index_versamenti_1 ON versamenti (cod_versamento_ente,id_applicazione);



CREATE TABLE singoli_versamenti
(
	cod_singolo_versamento_ente VARCHAR(70) NOT NULL COMMENT 'Identificativo della voce di pendenza',
	stato_singolo_versamento VARCHAR(35) NOT NULL COMMENT 'Stato di pagamento della voce di pendenza',
	importo_singolo_versamento DOUBLE NOT NULL COMMENT 'Importo della voce di pendenza',
	anno_riferimento INT COMMENT 'Anno di riferimento',
	-- MARCA BOLLO Valori possibili:\n01: Imposta di bollo
	tipo_bollo VARCHAR(2) COMMENT 'Tipologia della marca da bollo telematica',
	-- MARCA BOLLO: Digest in Base64 del documento da bollare
	hash_documento VARCHAR(70) COMMENT 'Digest in Base64 del documento da bollare',
	-- MARCA BOLLO: Sigla automobilistica della provincia di residenza
	provincia_residenza VARCHAR(2) COMMENT 'Sigla automobilistica della provincia di residenza',
	tipo_contabilita VARCHAR(1) COMMENT 'Tipologia di contabilita nelle codifiche agid',
	codice_contabilita VARCHAR(255) COMMENT 'Codice di contabilita nella tipologia scelta',
	descrizione VARCHAR(256) COMMENT 'Descrizione della voce di pendenza',
	dati_allegati LONGTEXT COMMENT 'Campo dati a disposizione dell\'ente',
	indice_dati INT NOT NULL COMMENT 'Numero progressivo della voce di pendenza',
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT COMMENT 'Identificativo fisico',
	id_versamento BIGINT NOT NULL COMMENT 'Riferimento alla pendenza',
	id_tributo BIGINT COMMENT 'Riferimento alla tipologia di tribuito',
	id_iban_accredito BIGINT COMMENT 'Riferimento al conto di accredito',
	id_iban_appoggio BIGINT COMMENT 'Riferimento al conto di appoggio',
	-- unique constraints
	CONSTRAINT unique_singoli_versamenti_1 UNIQUE (id_versamento,cod_singolo_versamento_ente,indice_dati),
	-- fk/pk keys constraints
	CONSTRAINT fk_sng_id_versamento FOREIGN KEY (id_versamento) REFERENCES versamenti(id),
	CONSTRAINT fk_sng_id_tributo FOREIGN KEY (id_tributo) REFERENCES tributi(id),
	CONSTRAINT fk_sng_id_iban_accredito FOREIGN KEY (id_iban_accredito) REFERENCES iban_accredito(id),
	CONSTRAINT fk_sng_id_iban_appoggio FOREIGN KEY (id_iban_appoggio) REFERENCES iban_accredito(id),
	CONSTRAINT pk_singoli_versamenti PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs COMMENT 'Voci di pendenza';

-- index
CREATE INDEX index_singoli_versamenti_1 ON singoli_versamenti (id_versamento,cod_singolo_versamento_ente,indice_dati);



CREATE TABLE pagamenti_portale
(
	cod_canale VARCHAR(35) COMMENT 'Codice identificativo del canale scelto per il pagamento (WISP 1.3)',
	nome VARCHAR(255) NOT NULL COMMENT 'Nome del pagamento',
	importo DOUBLE NOT NULL COMMENT 'Importo del pagamento',
	versante_identificativo VARCHAR(35) COMMENT 'Codice fiscale o partita iva del versante, se diverso dal pagatore',
	id_sessione VARCHAR(35) NOT NULL COMMENT 'Identificativo della sessione di pagamento assegnata da GovPay',
	id_sessione_portale VARCHAR(255) COMMENT 'Identificativo della sessione di pagamento assegnata dal portale richiedente',
	id_sessione_psp VARCHAR(255) COMMENT 'Identificativo della sessione di pagamento assegnata da pagoPA',
	stato VARCHAR(35) NOT NULL COMMENT 'Stato del pagamento',
	codice_stato VARCHAR(35) NOT NULL COMMENT 'Codice dello stato del pagamento',
	descrizione_stato VARCHAR(1024) COMMENT 'Descrizione dello stato di pagamento',
	psp_redirect_url VARCHAR(1024) COMMENT 'Url di redirezione per pagamenti ad iniziativa ente',
	psp_esito VARCHAR(255) COMMENT 'Esito del pagamento fornito dal PSP',
	json_request LONGTEXT COMMENT 'Richiesta di pagamento originale inviata dal portale',
	wisp_id_dominio VARCHAR(255) COMMENT 'Identificativo dominio nella richiesta (WISP 1.3)',
	wisp_key_pa VARCHAR(255) COMMENT 'Identificativo di scelta WISP assegnato dalla PA (WISP 1.3)',
	wisp_key_wisp VARCHAR(255) COMMENT 'Identificativo di scelta WISP assegnato da pagopa (WISP 1.3)',
	wisp_html LONGTEXT COMMENT 'Codice http per la pagina di redirezione al WISP (WISP 1.3)',
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
	data_richiesta TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP(3) COMMENT 'Data della richiesta di pagamento',
	url_ritorno VARCHAR(1024) COMMENT 'Url di redirezione al portale di pagamento',
	cod_psp VARCHAR(35) COMMENT 'Codice identificativo del psp scelto per il pagamento (WISP 1.3)',
	tipo_versamento VARCHAR(4) COMMENT 'Tipologia di pagamento scelto (WISP 1.3)',
	multi_beneficiario VARCHAR(35) COMMENT 'Indicazione se il pagmento e\' per un carrello multibeneficiario (WISP 1.3)',
	ack BOOLEAN NOT NULL COMMENT '',
	tipo INT NOT NULL COMMENT 'Tipologia di pagamento (Modello 1, 2 o 3)',
	principal VARCHAR(4000) NOT NULL COMMENT 'Principal del richiedente',
	tipo_utenza VARCHAR(35) NOT NULL COMMENT 'Tipologia dell\'utenza richiedente',
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT COMMENT 'Identificativo fisico',
	id_applicazione BIGINT COMMENT 'Riferimento all\'applicazione',
	-- unique constraints
	CONSTRAINT unique_pagamenti_portale_1 UNIQUE (id_sessione),
	-- fk/pk keys constraints
	CONSTRAINT fk_ppt_id_applicazione FOREIGN KEY (id_applicazione) REFERENCES applicazioni(id),
	CONSTRAINT pk_pagamenti_portale PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs COMMENT 'Richieste di pagamento';

-- index
CREATE UNIQUE INDEX index_pagamenti_portale_1 ON pagamenti_portale (id_sessione);



CREATE TABLE pag_port_versamenti
(
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT COMMENT 'Identificativo fisico',
	id_pagamento_portale BIGINT NOT NULL COMMENT 'Riferimento alla richeista di pagamento',
	id_versamento BIGINT NOT NULL COMMENT 'Riferimento alla pendenza oggetto di pagamento',
	-- fk/pk keys constraints
	CONSTRAINT fk_ppv_id_pagamento_portale FOREIGN KEY (id_pagamento_portale) REFERENCES pagamenti_portale(id),
	CONSTRAINT fk_ppv_id_versamento FOREIGN KEY (id_versamento) REFERENCES versamenti(id),
	CONSTRAINT pk_pag_port_versamenti PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs COMMENT 'Pendenze oggetto di pagamento';




CREATE TABLE rpt
(
	cod_carrello VARCHAR(35) COMMENT 'Codice identificativo assegnato al carrello di RPT',
	iuv VARCHAR(35) NOT NULL COMMENT 'Identificativo Univoco di Versamento della RPT',
	ccp VARCHAR(35) NOT NULL COMMENT 'Codice Contesto di Pagamento della RPT',
	cod_dominio VARCHAR(35) NOT NULL COMMENT 'Identificativo dell\'ente creditore',
	-- Identificativo de RPT utilizzato come riferimento nell'RT
	cod_msg_richiesta VARCHAR(35) NOT NULL COMMENT 'Identificativo dell\'RPT',
	-- Data di creazione dell'RPT
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
	data_msg_richiesta TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT 'Data di creazione della RPT',
	-- Stato RPT secondo la codifica AgID
	stato VARCHAR(35) NOT NULL COMMENT 'Stato della RPT',
	descrizione_stato LONGTEXT COMMENT 'Descrizione dello stato della RPT',
	cod_sessione VARCHAR(255) COMMENT 'Codice della sessione di pagamento assegnata da GovPay',
	cod_sessione_portale VARCHAR(255) COMMENT 'Codice della sessione di pagamento assegnato dal Portale',
	-- Indirizzo del portale psp a cui redirigere il cittadino per eseguire il pagamento
	psp_redirect_url VARCHAR(512) COMMENT 'Url di redirezione per procedere al pagamento',
	xml_rpt MEDIUMBLOB NOT NULL COMMENT 'XML della RPT codificato in base64',
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
	data_aggiornamento_stato TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT 'Data dell\'ultimo aggiornamento di stato della RPT',
	-- Indirizzo di ritorno al portale dell'ente al termine del pagamento
	callback_url LONGTEXT COMMENT 'Url di ritorno al portale di pagamento',
	modello_pagamento VARCHAR(16) NOT NULL COMMENT 'Indicazione sul modello di pagamento della RPT',
	cod_msg_ricevuta VARCHAR(35) COMMENT 'Codice identificativo della RT',
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
	-- Per versioni successive alla 5.7, rimuovere dalla sql_mode NO_ZERO_DATE 
	data_msg_ricevuta TIMESTAMP(3) COMMENT 'Data di produzione della RT',
	-- Esito del pagamento:\n0: Eseguito\n1: Non eseguito\n2: Parzialmente eseguito\n3: Decorrenza\n4: Decorrenza Parziale
	cod_esito_pagamento INT COMMENT 'Codice di esito del pagamento estratto dalla RT',
	importo_totale_pagato DOUBLE COMMENT 'Importo pagato estratto dalla RT',
	xml_rt MEDIUMBLOB COMMENT 'XML della RT codificato in base64',
	cod_canale VARCHAR(35) COMMENT 'Identificativo del canale con cui e\' stato eseguito il pagamento',
	cod_psp VARCHAR(35) COMMENT 'Identificativo del psp con cui e\' stato eseguito il pagamento',
	cod_intermediario_psp VARCHAR(35) COMMENT 'Identificativo dell\'intermediario psp con cui e\' stato eseguito il pagamento',
	tipo_versamento VARCHAR(4) COMMENT 'Tipo di versamento scelto per il pagamento',
	tipo_identificativo_attestante VARCHAR(1) COMMENT 'Identificativo del canale con cui e\' stato eseguito il pagamento',
	identificativo_attestante VARCHAR(35) COMMENT 'Identificativo del\'attestante',
	denominazione_attestante VARCHAR(70) COMMENT 'Ragione sociale dell\'attestante',
	cod_stazione VARCHAR(35) NOT NULL COMMENT 'Identificativo della stazione intermediaria',
	cod_transazione_rpt VARCHAR(36) COMMENT 'Identificativo della comunicazione RPT',
	cod_transazione_rt VARCHAR(36) COMMENT 'Identificativo della comunicazione RT',
	stato_conservazione VARCHAR(35) COMMENT 'Indicazione sullo stato di conservazione a norma della RT',
	descrizione_stato_cons VARCHAR(512) COMMENT 'Descrizione dello stato di conservazione a norma della RT',
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
	data_conservazione TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP(3) COMMENT 'Data di gestione del processo di conservazione a norma della RT',
	bloccante BOOLEAN NOT NULL DEFAULT true COMMENT 'Indicazione se la RPT risulta bloccante per ulteriori transazioni di pagamento',
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT COMMENT 'Identificativo fisico',
	id_versamento BIGINT NOT NULL COMMENT 'Riferimento alla pendenza oggetto della richeista di pagmaento',
	id_pagamento_portale BIGINT COMMENT 'Identificativo della richiesta di pagamento assegnato dal portale chiamante',
	-- unique constraints
	CONSTRAINT unique_rpt_1 UNIQUE (cod_msg_richiesta),
	CONSTRAINT unique_rpt_2 UNIQUE (iuv,ccp,cod_dominio),
	-- fk/pk keys constraints
	CONSTRAINT fk_rpt_id_versamento FOREIGN KEY (id_versamento) REFERENCES versamenti(id),
	CONSTRAINT fk_rpt_id_pagamento_portale FOREIGN KEY (id_pagamento_portale) REFERENCES pagamenti_portale(id),
	CONSTRAINT pk_rpt PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs COMMENT 'Transazioni di pagmaento';

-- index
CREATE INDEX index_rpt_1 ON rpt (cod_msg_richiesta);
CREATE INDEX index_rpt_2 ON rpt (iuv,ccp,cod_dominio);
CREATE INDEX index_rpt_3 ON rpt (stato);
CREATE INDEX index_rpt_4 ON rpt (id_versamento);



CREATE TABLE rr
(
	cod_dominio VARCHAR(35) NOT NULL COMMENT 'Identificativo del creditore',
	iuv VARCHAR(35) NOT NULL COMMENT 'Identificativo univoco di versamento',
	ccp VARCHAR(35) NOT NULL COMMENT 'Codice contesto di pagamento',
	cod_msg_revoca VARCHAR(35) NOT NULL COMMENT 'Identificativo della richiesta di revoca',
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
	data_msg_revoca TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT 'Data di emissione della revoca',
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
    -- Per versioni successive alla 5.7, rimuovere dalla sql_mode NO_ZERO_DATE 
	data_msg_esito TIMESTAMP(3) COMMENT 'Data del messaggio di esito',
	stato VARCHAR(35) NOT NULL COMMENT 'Stato della revoca',
	descrizione_stato VARCHAR(512) COMMENT 'Descrizione dello stato della revoca',
	importo_totale_richiesto DOUBLE NOT NULL COMMENT 'Importo della richiesta',
	cod_msg_esito VARCHAR(35) COMMENT 'Codice di esito della revoca',
	importo_totale_revocato DOUBLE COMMENT 'Importo revocato',
	xml_rr MEDIUMBLOB NOT NULL COMMENT 'XML della richiesta di revoca in base64',
	xml_er MEDIUMBLOB COMMENT 'XML dell\'esito della revoca di revoca in base64',
	cod_transazione_rr VARCHAR(36) COMMENT 'Identificativo della comunicazione della richiesta di revoca',
	cod_transazione_er VARCHAR(36) COMMENT 'Identificativo della comunicazione dell\'esito di revoca',
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT COMMENT 'Identificativo fisico',
	id_rpt BIGINT NOT NULL COMMENT 'Riferimento alla richiesta di pagamento oggetto della revoca',
	-- unique constraints
	CONSTRAINT unique_rr_1 UNIQUE (cod_msg_revoca),
	-- fk/pk keys constraints
	CONSTRAINT fk_rr_id_rpt FOREIGN KEY (id_rpt) REFERENCES rpt(id),
	CONSTRAINT pk_rr PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs COMMENT 'Richieste di revoca';

-- index
CREATE INDEX index_rr_1 ON rr (cod_msg_revoca);



CREATE TABLE notifiche
(
	tipo_esito VARCHAR(16) NOT NULL COMMENT 'Tipologia della notifica',
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
	data_creazione TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT 'Data di creazione della notifica',
	stato VARCHAR(16) NOT NULL COMMENT 'Stato di comunicazione della notifica',
	descrizione_stato VARCHAR(255) COMMENT 'Descrizione dello stato di comunicazione della notifica',
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
	data_aggiornamento_stato TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT 'Data dell\'ultimo aggiornamento',
	-- DATETIME invece che TIMESTAMP(3) per supportare la data di default 31-12-9999
	data_prossima_spedizione DATETIME NOT NULL COMMENT 'Data di prossima spedizione della notiifca',
	tentativi_spedizione BIGINT COMMENT 'Numero di tentativi di consegna della notifica',
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT COMMENT 'Identificativo fisico',
	id_applicazione BIGINT NOT NULL COMMENT 'Riferimento al verticale destinatario della notifica',
	id_rpt BIGINT COMMENT 'Riferimento alla transazione di pagamento oggetto della notifica',
	id_rr BIGINT COMMENT 'Riferimento alla transazione di revoca oggetto della notifica',
	-- fk/pk keys constraints
	CONSTRAINT fk_ntf_id_applicazione FOREIGN KEY (id_applicazione) REFERENCES applicazioni(id),
	CONSTRAINT fk_ntf_id_rpt FOREIGN KEY (id_rpt) REFERENCES rpt(id),
	CONSTRAINT fk_ntf_id_rr FOREIGN KEY (id_rr) REFERENCES rr(id),
	CONSTRAINT pk_notifiche PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs COMMENT 'Notifiche';




CREATE TABLE promemoria
(
	tipo VARCHAR(16) NOT NULL,
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
	data_creazione TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT 'Data di creazione del promemoria',
	stato VARCHAR(16) NOT NULL COMMENT 'Stato di comunicazione del promemoria',
	descrizione_stato VARCHAR(1024) COMMENT 'Descrizione dello stato di comunicazione del promemoria',
	destinatario_to VARCHAR(256) NOT NULL COMMENT 'Indirizzo email al quale spedire il promemoria',
	destinatario_cc VARCHAR(256) COMMENT 'Indirizzo email CC al quale spedire il promemoria',
	messaggio_content_type VARCHAR(256) COMMENT 'Content/Type messaggio da spedire',
	oggetto VARCHAR(512) COMMENT 'Oggetto email promemoria',
	messaggio LONGTEXT COMMENT 'Messaggio email promemoria',
	allega_pdf BOOLEAN NOT NULL DEFAULT false COMMENT 'Indica se allegare l\'avviso di pagamento alla email promemoria',
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
	data_aggiornamento_stato TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT 'Data dell\'ultimo aggiornamento',
	-- DATETIME invece che TIMESTAMP(3) per supportare la data di default 31-12-9999
	data_prossima_spedizione TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3)  COMMENT 'Data di prossima spedizione del promemoria',
	tentativi_spedizione BIGINT COMMENT 'Numero di tentativi di consegna del promemoria',
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT COMMENT 'Identificativo fisico',
	id_versamento BIGINT NOT NULL COMMENT 'Riferimento alla pendenza oggetto del promemoria',
	id_rpt BIGINT COMMENT 'Riferimento alla richiesta di pagamento oggetto del promemoria',
	-- fk/pk keys constraints
	CONSTRAINT fk_prm_id_versamento FOREIGN KEY (id_versamento) REFERENCES versamenti(id),
	CONSTRAINT fk_prm_id_rpt FOREIGN KEY (id_rpt) REFERENCES rpt(id),
	CONSTRAINT pk_promemoria PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;




CREATE TABLE iuv
(
	prg BIGINT NOT NULL COMMENT 'Seme di generazione dello IUV',
	iuv VARCHAR(35) NOT NULL COMMENT 'IUV generato',
	application_code INT NOT NULL COMMENT 'Application code codificato nello IUV',
	data_generazione TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT 'Data di generazione dello IUV',
	tipo_iuv VARCHAR(1) NOT NULL COMMENT 'Tipologia di IUV generato',
	cod_versamento_ente VARCHAR(35) COMMENT 'Identificativo della pendenza associata allo IUV',
	aux_digit INT NOT NULL DEFAULT 0 COMMENT 'Aux digit codificato nello IUV',
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT COMMENT 'Identificativo fisico',
	id_applicazione BIGINT NOT NULL COMMENT 'Identificativo dell\'applicazione che ha richiesto lo IUV',
	id_dominio BIGINT NOT NULL COMMENT 'Identificativo del creditore proprietario dello IUV',
	-- unique constraints
	CONSTRAINT unique_iuv_1 UNIQUE (id_dominio,iuv),
	-- fk/pk keys constraints
	CONSTRAINT fk_iuv_id_applicazione FOREIGN KEY (id_applicazione) REFERENCES applicazioni(id),
	CONSTRAINT fk_iuv_id_dominio FOREIGN KEY (id_dominio) REFERENCES domini(id),
	CONSTRAINT pk_iuv PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs COMMENT 'IUV emessi';

-- index
CREATE INDEX index_iuv_1 ON iuv (id_dominio,iuv);
CREATE INDEX index_iuv_2 ON iuv (cod_versamento_ente,tipo_iuv,id_applicazione);



CREATE TABLE incassi
(
	trn VARCHAR(35) NOT NULL COMMENT 'Identificativo del movimento bancario riconciliato',
	cod_dominio VARCHAR(35) NOT NULL COMMENT 'Identificaitvo dell\'ente',
	causale VARCHAR(512) NOT NULL COMMENT 'Causale del bonifico',
	importo DOUBLE NOT NULL COMMENT 'Importo riconciliato',
	data_valuta TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP(3) COMMENT 'Data valuta del bonifico',
	data_contabile TIMESTAMP(3) DEFAULT  CURRENT_TIMESTAMP(3) COMMENT 'Data contabile del bonifico',
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
	data_ora_incasso TIMESTAMP(3) NOT NULL DEFAULT  CURRENT_TIMESTAMP(3) COMMENT 'Data della riconciliazione',
	nome_dispositivo VARCHAR(512) COMMENT 'Riferimento al giornale di cassa',
	iban_accredito VARCHAR(35) COMMENT 'Conto di accredito',
        sct VARCHAR(35) COMMENT 'Identificativo SEPA credit transfert',
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT COMMENT 'Identificativo fisico',
	id_applicazione BIGINT COMMENT 'Riferimento all\'applicativo che ha registrato l\'inccasso',
	id_operatore BIGINT COMMENT 'Riferimento all\'operatore che ha registrato l\'inccasso',
	-- unique constraints
	CONSTRAINT unique_incassi_1 UNIQUE (cod_dominio,trn),
	-- fk/pk keys constraints
	CONSTRAINT fk_inc_id_applicazione FOREIGN KEY (id_applicazione) REFERENCES applicazioni(id),
	CONSTRAINT fk_inc_id_operatore FOREIGN KEY (id_operatore) REFERENCES operatori(id),
	CONSTRAINT pk_incassi PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs COMMENT 'Riconciliazioni';

-- index
CREATE INDEX index_incassi_1 ON incassi (cod_dominio,trn);



CREATE TABLE fr
(
	cod_psp VARCHAR(35) NOT NULL COMMENT 'Identificativo del PSP che ha emesso il flusso ',
	cod_dominio VARCHAR(35) NOT NULL COMMENT 'Identificativo dell\'Ente creditore',
	cod_flusso VARCHAR(35) NOT NULL COMMENT 'Identificativo del flusso',
	stato VARCHAR(35) NOT NULL COMMENT 'Stato di acquisizione del flusso',
	descrizione_stato LONGTEXT COMMENT 'Descrizione dello stato di acquisizione del flusso',
	iur VARCHAR(35) NOT NULL COMMENT 'Identificativo Univoco di Riversamento',
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
    -- Per versioni successive alla 5.7, rimuovere dalla sql_mode NO_ZERO_DATE 
	data_ora_flusso TIMESTAMP(3) COMMENT 'Data di emissione del flusso',
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
    -- Per versioni successive alla 5.7, rimuovere dalla sql_mode NO_ZERO_DATE 
	data_regolamento TIMESTAMP(3) COMMENT 'Data dell\'operazione di regolamento bancario',
	data_acquisizione TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT 'Data di acquisizione del flusso',
	numero_pagamenti BIGINT COMMENT 'Numero di pagamenti rendicontati',
	importo_totale_pagamenti DOUBLE COMMENT 'Importo totale rendicontato',
	cod_bic_riversamento VARCHAR(35) COMMENT 'Bic del conto di riversamento',
	xml MEDIUMBLOB NOT NULL COMMENT 'XML del flusso codfificato in base64',
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT COMMENT 'Identificativo fisico',
	id_incasso BIGINT COMMENT 'Riferimento all\'incasso',
	-- unique constraints
	CONSTRAINT unique_fr_1 UNIQUE (cod_flusso),
	-- fk/pk keys constraints
	CONSTRAINT fk_fr_id_incasso FOREIGN KEY (id_incasso) REFERENCES incassi(id),
	CONSTRAINT pk_fr PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs COMMENT 'Flussi di rendicontazione';

-- index
CREATE INDEX index_fr_1 ON fr (cod_flusso);



CREATE TABLE pagamenti
(
	cod_dominio VARCHAR(35) NOT NULL COMMENT 'Identificativo del creditore',
	iuv VARCHAR(35) NOT NULL COMMENT 'Identificativo univoco di versamento',
	indice_dati INT NOT NULL COMMENT 'Indice progressivo della voce di pendenza',
	importo_pagato DOUBLE NOT NULL COMMENT'Importo del pagamento',
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
	data_acquisizione TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT 'Data acquisizione del pagamento',
	iur VARCHAR(35) NOT NULL COMMENT 'Identificativo di riscossione',
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
	data_pagamento TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT 'Data del pagamento',
	commissioni_psp DOUBLE COMMENT 'Importo delle commissioni applicate dal psp',
	-- Valori possibili:\nES: Esito originario\nBD: Marca da Bollo
	tipo_allegato VARCHAR(2) COMMENT 'Tipo di allegato al pagamento',
	allegato MEDIUMBLOB COMMENT 'Allegato al pagamento',
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
	data_acquisizione_revoca TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP(3) COMMENT 'Data di revoca del pagamento',
	causale_revoca VARCHAR(140) COMMENT 'Causale della revoca del pagamento',
	dati_revoca VARCHAR(140) COMMENT 'Dati allegati alla revoca',
	importo_revocato DOUBLE COMMENT 'Importo revocato',
	esito_revoca VARCHAR(140) COMMENT 'Esito della revoca',
	dati_esito_revoca VARCHAR(140) COMMENT 'Dati allegati all\'esito della revoca',
	stato VARCHAR(35) COMMENT 'Stato del pagamento',
	tipo VARCHAR(35) NOT NULL COMMENT 'Tipo di pagamento',
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT COMMENT 'Identificativo fisico',
	id_rpt BIGINT COMMENT 'Riferimento alla transazione di pagmaento',
	id_singolo_versamento BIGINT COMMENT 'Riferimento alla voce della pendenza pagata',
	id_rr BIGINT COMMENT 'Riferimento alla transazione di revoca',
	id_incasso BIGINT COMMENT 'Riferimento all\'operazione di riconciliazione',
	-- unique constraints
	CONSTRAINT unique_pagamenti_1 UNIQUE (cod_dominio,iuv,iur,indice_dati),
	-- fk/pk keys constraints
	CONSTRAINT fk_pag_id_rpt FOREIGN KEY (id_rpt) REFERENCES rpt(id),
	CONSTRAINT fk_pag_id_singolo_versamento FOREIGN KEY (id_singolo_versamento) REFERENCES singoli_versamenti(id),
	CONSTRAINT fk_pag_id_rr FOREIGN KEY (id_rr) REFERENCES rr(id),
	CONSTRAINT fk_pag_id_incasso FOREIGN KEY (id_incasso) REFERENCES incassi(id),
	CONSTRAINT pk_pagamenti PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs COMMENT 'Pagamenti';

-- index
CREATE UNIQUE INDEX index_pagamenti_1 ON pagamenti (cod_dominio,iuv,iur,indice_dati);



CREATE TABLE rendicontazioni
(
	iuv VARCHAR(35) NOT NULL COMMENT 'Identificativo Univoco di Versamento',
	iur VARCHAR(35) NOT NULL COMMENT 'Identificativo Univoco di Riscossione',
	indice_dati INT COMMENT 'Progressivo di voce di penendza',
	importo_pagato DOUBLE COMMENT 'Importo rendicontato',
	esito INT COMMENT 'Codice di esito della rendicontazione',
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
	data TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP(3) COMMENT 'Data di rendicontazione',
	stato VARCHAR(35) NOT NULL COMMENT 'Stato della rendicontazione',
	anomalie LONGTEXT COMMENT 'Anomalie riscontrate nella rendicontazione',
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT COMMENT 'Identificativo fisico',
	id_fr BIGINT NOT NULL COMMENT 'Riferimento al flusso di rendicontazione',
	id_pagamento BIGINT COMMENT 'Riferimento al pagamento rendicotnato',
	id_singolo_versamento BIGINT COMMENT 'Riferimento alla voce di pendenza rendicontata',
	-- fk/pk keys constraints
	CONSTRAINT fk_rnd_id_fr FOREIGN KEY (id_fr) REFERENCES fr(id),
	CONSTRAINT fk_rnd_id_pagamento FOREIGN KEY (id_pagamento) REFERENCES pagamenti(id),
	CONSTRAINT fk_rnd_id_singolo_versamento FOREIGN KEY (id_singolo_versamento) REFERENCES singoli_versamenti(id),
	CONSTRAINT pk_rendicontazioni PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs COMMENT 'Rendicontazioni';




CREATE TABLE eventi
(
	componente VARCHAR(35) COMMENT 'Componente che ha generato l\'evento',
	ruolo VARCHAR(1) COMMENT 'Ruolo (Client/Server)',
	categoria_evento VARCHAR(1) COMMENT 'Categoria dell\'evento',
	tipo_evento VARCHAR(70) COMMENT 'Tipologia dell\'evento',
	sottotipo_evento VARCHAR(35) COMMENT 'Sotto tipologia dell\'evento',
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
	-- Per versioni successive alla 5.7, rimuovere dalla sql_mode NO_ZERO_DATE 
	data TIMESTAMP(3) DEFAULT 0 COMMENT 'Data di emissione dell\'evento',
	intervallo BIGINT COMMENT 'Intervallo tra l\'evento di richiesta e risposta',
	esito VARCHAR(4) COMMENT 'Esito operazione registrata',
	sottotipo_esito VARCHAR(35) COMMENT 'Descrizione esito operazione',
	dettaglio_esito LONGTEXT COMMENT 'Dettaglio esito in forma estesa',
	parametri_richiesta MEDIUMBLOB COMMENT 'Dettagli della richiesta',
	parametri_risposta MEDIUMBLOB COMMENT 'Dettagli della risposta',
	dati_pago_pa LONGTEXT COMMENT 'Dati relativi alle transazioni pagopa',
	cod_versamento_ente VARCHAR(35) COMMENT 'Identificativo della pendenza nel verticale di competenza',
	cod_applicazione VARCHAR(35) COMMENT 'Codice identificativo del verticale',
	iuv VARCHAR(35) COMMENT 'Identificativo univoco di versamento',
	ccp VARCHAR(35) COMMENT 'Codice contesto di pagamento',
	cod_dominio VARCHAR(35) COMMENT 'Identificativo dell\'Ente creditore',
	id_sessione VARCHAR(35) COMMENT 'Identificativo del pagamento portale',
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT COMMENT 'Identificativo fisico',
	id_fr BIGINT COMMENT 'Riferimento al flusso di rendicontazione',
	id_incasso BIGINT COMMENT 'Riferimento all\'incasso',
	id_tracciato BIGINT COMMENT 'Riferimento al tracciato',
	-- fk/pk keys constraints
	CONSTRAINT fk_evt_id_fr FOREIGN KEY (id_fr) REFERENCES fr(id),
	CONSTRAINT fk_evt_id_incasso FOREIGN KEY (id_incasso) REFERENCES incassi(id),
	CONSTRAINT fk_evt_id_tracciato FOREIGN KEY (id_tracciato) REFERENCES tracciati(id),
	CONSTRAINT pk_eventi PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs COMMENT 'Giornale degli eventi';




CREATE TABLE batch
(
	cod_batch VARCHAR(255) NOT NULL COMMENT 'Identificativo del batch',
	nodo VARCHAR(255) COMMENT 'Nodo del cluster che possiede il token',
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
	inizio TIMESTAMP(3) COMMENT 'Data di cattura del token',
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
	aggiornamento TIMESTAMP(3) COMMENT 'Data dell\'ultimo aggiornamento ricevuto',
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT COMMENT 'Identificativo fisico',
	-- unique constraints
	CONSTRAINT unique_batch_1 UNIQUE (cod_batch),
	-- fk/pk keys constraints
	CONSTRAINT pk_batch PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs COMMENT 'Gestione concorrenza batch';

-- index
CREATE INDEX index_batch_1 ON batch (cod_batch);



CREATE TABLE esiti_avvisatura
(
	cod_dominio VARCHAR(35) NOT NULL COMMENT 'Identificativo del creditore',
	identificativo_avvisatura VARCHAR(20) NOT NULL COMMENT 'Identificativo avvisatura',
	tipo_canale INT NOT NULL COMMENT 'Tipo di canale di avvisatura',
	cod_canale VARCHAR(35) COMMENT 'Identificativo del canale di avvisatura',
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
	data TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT 'Data avvisatura',
	cod_esito INT NOT NULL COMMENT 'Codice esito avvisatura',
	descrizione_esito VARCHAR(140) NOT NULL COMMENT 'Descrizione esito avvisatura',
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT COMMENT 'Identificativo fisico',
	id_tracciato BIGINT NOT NULL COMMENT 'Riferimento al tracciato di avvisatura',
	-- fk/pk keys constraints
	CONSTRAINT fk_sta_id_tracciato FOREIGN KEY (id_tracciato) REFERENCES tracciati(id),
	CONSTRAINT pk_esiti_avvisatura PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs COMMENT 'Esiti delle avvisature digitali';




CREATE TABLE operazioni
(
	tipo_operazione VARCHAR(16) NOT NULL COMMENT 'Tipo di operazione',
	linea_elaborazione BIGINT NOT NULL COMMENT 'Numero linea nel tracciato di origine',
	stato VARCHAR(16) NOT NULL COMMENT 'Stato di elaborazione',
	dati_richiesta MEDIUMBLOB NOT NULL COMMENT 'Dati raw di richiesta',
	dati_risposta MEDIUMBLOB COMMENT 'Dati raw di risposta',
	dettaglio_esito VARCHAR(255) COMMENT 'Descrizione dell\'esito',
	cod_versamento_ente VARCHAR(255) COMMENT 'Identificativo pendenza',
	cod_dominio VARCHAR(35) COMMENT 'Identificativo ente creditore',
	iuv VARCHAR(35) COMMENT 'Identificativo unico di versamento',
	trn VARCHAR(35) COMMENT 'Identificativo operazione contabile',
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT COMMENT 'Identificativo fisico',
	id_tracciato BIGINT NOT NULL COMMENT 'Riferimento al tracciato di origine',
	id_applicazione BIGINT COMMENT 'Riferiemnto all\'applicazione chiamante',
	-- fk/pk keys constraints
	CONSTRAINT fk_ope_id_tracciato FOREIGN KEY (id_tracciato) REFERENCES tracciati(id),
	CONSTRAINT fk_ope_id_applicazione FOREIGN KEY (id_applicazione) REFERENCES applicazioni(id),
	CONSTRAINT pk_operazioni PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs COMMENT 'Operazioni richieste da tracciato';




CREATE TABLE gp_audit
(
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
	data TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT 'Data di emissione dell\'audit',
	id_oggetto BIGINT NOT NULL COMMENT 'Identificativo dell\'elemento modificato',
	tipo_oggetto VARCHAR(255) NOT NULL COMMENT 'Tipo dell\'elemento modificato',
	oggetto LONGTEXT NOT NULL COMMENT 'Serializzazione dell\'elemento modificato',
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT COMMENT 'Identificativo fisico',
	id_operatore BIGINT NOT NULL COMMENT 'Riferimento all\utente che ha richiesto la modifica',
	-- fk/pk keys constraints
	CONSTRAINT fk_aud_id_operatore FOREIGN KEY (id_operatore) REFERENCES operatori(id),
	CONSTRAINT pk_gp_audit PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;




CREATE TABLE stampe
(
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
	data_creazione TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT 'Data di creazione della stampa',
	tipo VARCHAR(16) NOT NULL COMMENT 'Tipologia di stampa',
	pdf MEDIUMBLOB COMMENT 'Byte della Stampa',
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT COMMENT 'Identificativo fisico',
        id_versamento BIGINT NOT NULL COMMENT 'Riferimento alla pendenza',
        -- unique constraints
	CONSTRAINT unique_stampe_1 UNIQUE (id_versamento,tipo),
	-- fk/pk keys constraints
	CONSTRAINT fk_stm_id_versamento FOREIGN KEY (id_versamento) REFERENCES versamenti(id),
	CONSTRAINT pk_stampe PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs COMMENT 'Stampe relative alla pendenza';

-- index
CREATE UNIQUE INDEX index_stampe_1 ON stampe (id_versamento,tipo);



CREATE TABLE ID_MESSAGGIO_RELATIVO
(
	COUNTER BIGINT NOT NULL COMMENT 'Contatore progressivo',
	PROTOCOLLO VARCHAR(255) NOT NULL COMMENT 'Tipologia di progressivo',
	INFO_ASSOCIATA VARCHAR(255) NOT NULL COMMENT 'Namespace di generazione',
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
	ora_registrazione TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP(3) COMMENT 'Ora di registrazione',
	-- fk/pk columns
	-- fk/pk keys constraints
	CONSTRAINT pk_ID_MESSAGGIO_RELATIVO PRIMARY KEY (PROTOCOLLO,INFO_ASSOCIATA)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs COMMENT 'Generatore di progressivi';


CREATE TABLE sonde
(
	nome VARCHAR(35) NOT NULL COMMENT 'Nome della sonda',
	classe VARCHAR(255) NOT NULL COMMENT 'Classe che implementa il check',
	soglia_warn BIGINT NOT NULL COMMENT 'Soglia di warning',
	soglia_error BIGINT NOT NULL COMMENT 'Soglia di errore',
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
	data_ok TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP(3) COMMENT 'Data di ultima esecuzione del check con esito ok',
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
	data_warn TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP(3) COMMENT 'Data di ultima esecuzione del check con esito warning',
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
	data_error TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP(3) COMMENT 'Data di ultima esecuzione del check con esito errore',
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
	data_ultimo_check TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP(3) COMMENT 'Data di ultima esecuzione del check',
	dati_check LONGTEXT COMMENT 'Data di configurazione del check',
	stato_ultimo_check INT COMMENT 'Esito dell\'ultima esecuzione del check',
	-- fk/pk columns
	-- fk/pk keys constraints
	CONSTRAINT pk_sonde PRIMARY KEY (nome)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs COMMENT 'Sonde di controllo della piattaforma';

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
MAX(versamenti.id_tipo_versamento) as id_tipo_versamento,
MAX(versamenti.id_tipo_versamento_dominio) AS id_tipo_versamento_dominio,
MAX(versamenti.id_dominio) as id_dominio,                   
MAX(versamenti.id_uo) as id_uo,                        
MAX(versamenti.id_applicazione) as id_applicazione,             
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
MAX(CASE WHEN versamenti.ack = TRUE THEN 'TRUE' ELSE 'FALSE' END) AS ack,
MAX(CASE WHEN versamenti.anomalo = TRUE THEN 'TRUE' ELSE 'FALSE' END) AS anomalo,
MAX(pagamenti.data_pagamento) as data_pagamento,            
SUM(CASE WHEN pagamenti.importo_pagato IS NOT NULL THEN pagamenti.importo_pagato ELSE 0 END) AS importo_pagato,
SUM(CASE WHEN pagamenti.stato = 'INCASSATO' THEN pagamenti.importo_pagato ELSE 0 END) AS importo_incassato,
MAX(CASE WHEN pagamenti.stato IS NULL THEN 'NON_PAGATO' WHEN pagamenti.stato = 'INCASSATO' THEN 'INCASSATO' ELSE 'PAGATO' END) AS stato_pagamento,
MAX(pagamenti.iuv) AS iuv_pagamento,
MAX(CASE WHEN versamenti.stato_versamento = 'NON_ESEGUITO' AND versamenti.data_validita > now() THEN 0 ELSE 1 END) AS smart_order_rank,
MIN(ABS((UNIX_TIMESTAMP(now()) *1000) - (UNIX_TIMESTAMP(COALESCE(pagamenti.data_pagamento, versamenti.data_validita, versamenti.data_creazione)) * 1000))) AS smart_order_date
FROM versamenti LEFT JOIN singoli_versamenti ON versamenti.id = singoli_versamenti.id_versamento LEFT join pagamenti on singoli_versamenti.id = pagamenti.id_singolo_versamento 
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
        SELECT cod_dominio, iuv, iur, cod_flusso, fr_iur,  data_regolamento, importo_totale_pagamenti, numero_pagamenti, importo_pagato, data_pagamento, cod_singolo_versamento_ente, indice_dati, cod_versamento_ente, applicazioni.cod_applicazione, debitore_identificativo AS identificativo_debitore, cod_anno_tributario AS anno, cod_tipo_versamento, cod_tributo AS cod_entrata FROM v_riscossioni_con_rpt JOIN applicazioni ON v_riscossioni_con_rpt.id_applicazione = applicazioni.id JOIN tipi_versamento ON v_riscossioni_con_rpt.id_tipo_versamento = tipi_versamento.id LEFT JOIN tributi ON v_riscossioni_con_rpt.id_tributo = tributi.id JOIN tipi_tributo ON tributi.id_tipo_tributo = tipi_tributo.id           
        UNION
        SELECT cod_dominio, iuv, iur, cod_flusso, fr_iur,  data_regolamento, importo_totale_pagamenti, numero_pagamenti, importo_pagato, data_pagamento, cod_singolo_versamento_ente, indice_dati, cod_versamento_ente, applicazioni.cod_applicazione, debitore_identificativo AS identificativo_debitore, cod_anno_tributario AS anno, cod_tipo_versamento, cod_tributo AS cod_entrata FROM v_riscossioni_senza_rpt join applicazioni ON v_riscossioni_senza_rpt.id_applicazione = applicazioni.id LEFT JOIN tipi_versamento ON v_riscossioni_senza_rpt.id_tipo_versamento = tipi_versamento.id LEFT JOIN tributi ON v_riscossioni_senza_rpt.id_tributo = tributi.id LEFT JOIN tipi_tributo ON tributi.id_tipo_tributo = tipi_tributo.id;

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

CREATE VIEW v_eventi_vers AS
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
        UNION SELECT componente,
               ruolo,
               categoria_evento,
               tipo_evento,
               sottotipo_evento,
               data,
               intervallo,
               esito,
               sottotipo_esito,
               dettaglio_esito,
               parametri_richiesta,
               parametri_risposta,
               dati_pago_pa,
               cod_versamento_ente,
               cod_applicazione,
               iuv,
               cod_dominio,
               ccp,
               id_sessione,
               id FROM v_eventi_vers_pagamenti 
        UNION SELECT componente,
               ruolo,
               categoria_evento,
               tipo_evento,
               sottotipo_evento,
               data,
               intervallo,
               esito,
               sottotipo_esito,
               dettaglio_esito,
               parametri_richiesta,
               parametri_risposta,
               dati_pago_pa,
               cod_versamento_ente,
               cod_applicazione,
               iuv,
               cod_dominio,
               ccp,
               id_sessione,
               id FROM v_eventi_vers_rendicontazioni
        UNION SELECT componente,
               ruolo,
               categoria_evento,
               tipo_evento,
               sottotipo_evento,
               data,
               intervallo,
               esito,
               sottotipo_esito,
               dettaglio_esito,
               parametri_richiesta,
               parametri_risposta,
               dati_pago_pa,
               cod_versamento_ente,
               cod_applicazione,
               iuv,
               cod_dominio,
               ccp,
               id_sessione,
               id FROM v_eventi_vers_riconciliazioni
	    UNION SELECT componente,
               ruolo,
               categoria_evento,
               tipo_evento,
               sottotipo_evento,
               data,
               intervallo,
               esito,
               sottotipo_esito,
               dettaglio_esito,
               parametri_richiesta,
               parametri_risposta,
               dati_pago_pa,
               cod_versamento_ente,
               cod_applicazione,
               iuv,
               cod_dominio,
               ccp,
               id_sessione,
               id FROM v_eventi_vers_tracciati;


