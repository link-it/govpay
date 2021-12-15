-- Per versioni successive alla 5.7, rimuovere dalla sql_mode NO_ZERO_DATE
SET @@SESSION.sql_mode=(SELECT REPLACE(@@SESSION.sql_mode,'NO_ZERO_DATE',''));


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
	password VARCHAR(255) COMMENT 'Password utenza',
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
	logo MEDIUMBLOB COMMENT 'Logo dell\'ente creditore codificato in base64',
	cbill VARCHAR(255) COMMENT 'Codice cbill assegnato da pagopa in caso di adesione su modello 3',
	aut_stampa_poste VARCHAR(255) COMMENT 'Autorizzazione alla stampa in poprio di poste italiane',
	cod_connettore_my_pivot VARCHAR(255) COMMENT 'Identificativo connettore mypivot',
	cod_connettore_secim VARCHAR(255) COMMENT 'Identificativo connettore secim',
	cod_connettore_gov_pay VARCHAR(255) COMMENT 'Identificativo connettore govpay',
	cod_connettore_hyper_sic_apk VARCHAR(255) COMMENT 'Identificativo connettore hypersic_apk',
	intermediato BOOLEAN NOT NULL COMMENT 'Indica se l\'ente e\' intermediato',
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT COMMENT 'Identificativo fisico',
	id_stazione BIGINT COMMENT 'Riferimento alla stazione',
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
	abilitato BOOLEAN NOT NULL COMMENT 'Indicazione se e\' abilitato ad operare',
	descrizione VARCHAR(255) COMMENT 'Descrizione estesa Iban',
	intestatario VARCHAR(255) COMMENT 'Intestatario del conto di accredito',
	aut_stampa_poste VARCHAR(255) COMMENT 'Autorizzazione alla stampa in poprio di poste italiane',
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
	zip_stampe MEDIUMBLOB COMMENT 'Zip contenente le stampe degli avvisi del tracciato',
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
	paga_terzi BOOLEAN NOT NULL DEFAULT false COMMENT 'Indica se il tipo pendenza e\' pagabile da soggetti terzi',
	abilitato BOOLEAN NOT NULL COMMENT 'Indicazione se e\' abilitato ad operare',
	-- Configurazione caricamento pendenze da portali Backoffice
	bo_form_tipo VARCHAR(35) COMMENT 'Tipo di linguaggio per il disegno della form',
	bo_form_definizione LONGTEXT COMMENT 'Definizione della form nel linguaggio indicato',
	bo_validazione_def LONGTEXT COMMENT 'Definizione dello schema per la validazione della pendenza',
	bo_trasformazione_tipo VARCHAR(35) COMMENT 'Tipo di trasformazione richiesta',
	bo_trasformazione_def LONGTEXT COMMENT 'Template di trasformazione',
	bo_cod_applicazione VARCHAR(35) COMMENT 'Identificativo dell\'applicazione a cui inoltrare la pendenza',
	bo_abilitato BOOLEAN NOT NULL DEFAULT false COMMENT 'abilitazione della funzionalita\'',
	-- Configurazione caricamento pendenze da portali per il Cittadino
	pag_form_tipo VARCHAR(35) COMMENT 'Tipo di linguaggio per il disegno della form',
	pag_form_definizione LONGTEXT COMMENT 'Definizione della form nel linguaggio indicato',
	pag_form_impaginazione LONGTEXT COMMENT 'Definizione impaginazione della form nel linguaggio indicato',
	pag_validazione_def LONGTEXT COMMENT 'Definizione dello schema per la validazione della pendenza',
	pag_trasformazione_tipo VARCHAR(35) COMMENT 'Tipo di trasformazione richiesta',
	pag_trasformazione_def LONGTEXT COMMENT 'Template di trasformazione',
	pag_cod_applicazione VARCHAR(35) COMMENT 'Identificativo dell\'applicazione a cui inoltrare la pendenza',
	pag_abilitato BOOLEAN NOT NULL DEFAULT false COMMENT 'abilitazione della funzionalita\'',
	-- Configurazione Avvisatura via mail 	
	avv_mail_prom_avv_abilitato BOOLEAN NOT NULL DEFAULT false COMMENT 'abilitazione della funzionalita\'',
	avv_mail_prom_avv_pdf BOOLEAN COMMENT 'Indica se inserire l\'avviso di pagamento come allegato alla mail',
	avv_mail_prom_avv_tipo VARCHAR(35) COMMENT 'Tipo template',
	avv_mail_prom_avv_oggetto LONGTEXT COMMENT 'Template oggetto della mail',
	avv_mail_prom_avv_messaggio LONGTEXT COMMENT 'Template messaggio della mail',
	avv_mail_prom_ric_abilitato BOOLEAN NOT NULL DEFAULT false COMMENT 'abilitazione della funzionalita\'',
	avv_mail_prom_ric_pdf BOOLEAN COMMENT 'Indica se inserire la ricevuta di pagamento come allegato alla mail',
	avv_mail_prom_ric_tipo VARCHAR(35) COMMENT 'Tipo template',
	avv_mail_prom_ric_oggetto LONGTEXT COMMENT 'Template oggetto della mail',
	avv_mail_prom_ric_messaggio LONGTEXT COMMENT 'Template messaggio della mail',
	avv_mail_prom_ric_eseguiti BOOLEAN COMMENT 'Indica se limitare gli invii ai soli eseguiti.',
	avv_mail_prom_scad_abilitato BOOLEAN NOT NULL DEFAULT false COMMENT 'abilitazione della funzionalita\'',
	avv_mail_prom_scad_preavviso INT COMMENT 'Numero di giorni di preavviso alla scadenza.',
	avv_mail_prom_scad_tipo VARCHAR(35) COMMENT 'Tipo template',
	avv_mail_prom_scad_oggetto LONGTEXT COMMENT 'Template oggetto della mail',
	avv_mail_prom_scad_messaggio LONGTEXT COMMENT 'Template messaggio della mail',
	-- Configurazione visualizzazione custom del dettaglio pendenza
	visualizzazione_definizione LONGTEXT COMMENT 'Definisce la visualizzazione custom della tipologia',
	-- Configurazione della trasformazione dei tracciati csv
	trac_csv_tipo VARCHAR(35) COMMENT 'Indica il tipo di template di conversione',
	trac_csv_header_risposta LONGTEXT COMMENT 'Header del file Csv di risposta del tracciato',
	trac_csv_template_richiesta LONGTEXT COMMENT 'Template di conversione della pendenza da CSV a JSON',
	trac_csv_template_risposta LONGTEXT COMMENT 'Template di conversione della pendenza da JSON a CSV',
	-- Configurazione avvisatura via AppIO
	avv_app_io_prom_avv_abilitato BOOLEAN NOT NULL DEFAULT false COMMENT 'abilitazione della funzionalita\'',
	avv_app_io_prom_avv_tipo VARCHAR(35) COMMENT 'Tipo template',
	avv_app_io_prom_avv_oggetto LONGTEXT COMMENT 'Template oggetto della comunicazione',
	avv_app_io_prom_avv_messaggio LONGTEXT COMMENT 'Template messaggio della comunicazione',
	avv_app_io_prom_ric_abilitato BOOLEAN NOT NULL DEFAULT false COMMENT 'abilitazione della funzionalita\'',
	avv_app_io_prom_ric_tipo VARCHAR(35) COMMENT 'Tipo template',
	avv_app_io_prom_ric_oggetto LONGTEXT COMMENT 'Template oggetto della comunicazione',
	avv_app_io_prom_ric_messaggio LONGTEXT COMMENT 'Template messaggio della comunicazione',
	avv_app_io_prom_ric_eseguiti BOOLEAN COMMENT 'Indica se limitare gli invii ai soli eseguiti.',
	avv_app_io_prom_scad_abilitato BOOLEAN NOT NULL DEFAULT false COMMENT 'abilitazione della funzionalita\'',
	avv_app_io_prom_scad_preavviso INT COMMENT 'Numero di giorni di preavviso alla scadenza.',
	avv_app_io_prom_scad_tipo VARCHAR(35) COMMENT 'Tipo template',
	avv_app_io_prom_scad_oggetto LONGTEXT COMMENT 'Template oggetto della comunicazione',
	avv_app_io_prom_scad_messaggio LONGTEXT COMMENT 'Template messaggio della comunicazione',
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
	paga_terzi BOOLEAN  COMMENT 'Indica se il tipo pendenza e\' pagabile da soggetti terzi per il dominio',
	abilitato BOOLEAN COMMENT 'Indicazione se e\' abilitato ad operare',
	-- Configurazione caricamento pendenze da portali Backoffice
	bo_form_tipo VARCHAR(35) COMMENT 'Tipo di linguaggio per il disegno della form',
	bo_form_definizione LONGTEXT COMMENT 'Definizione della form nel linguaggio indicato',
	bo_validazione_def LONGTEXT COMMENT 'Definizione dello schema per la validazione della pendenza',
	bo_trasformazione_tipo VARCHAR(35) COMMENT 'Tipo di trasformazione richiesta',
	bo_trasformazione_def LONGTEXT COMMENT 'Template di trasformazione',
	bo_cod_applicazione VARCHAR(35) COMMENT 'Identificativo dell\'applicazione a cui inoltrare la pendenza',
	bo_abilitato BOOLEAN COMMENT 'abilitazione della funzionalita\'',
	-- Configurazione caricamento pendenze da portali per il Cittadino
	pag_form_tipo VARCHAR(35) COMMENT 'Tipo di linguaggio per il disegno della form',
	pag_form_definizione LONGTEXT COMMENT 'Definizione della form nel linguaggio indicato',
	pag_form_impaginazione LONGTEXT COMMENT 'Definizione impaginazione della form nel linguaggio indicato',
	pag_validazione_def LONGTEXT COMMENT 'Definizione dello schema per la validazione della pendenza',
	pag_trasformazione_tipo VARCHAR(35) COMMENT 'Tipo di trasformazione richiesta',
	pag_trasformazione_def LONGTEXT COMMENT 'Template di trasformazione',
	pag_cod_applicazione VARCHAR(35) COMMENT 'Identificativo dell\'applicazione a cui inoltrare la pendenza',
	pag_abilitato BOOLEAN COMMENT 'abilitazione della funzionalita\'',
	-- Configurazione Avvisatura via mail 	
	avv_mail_prom_avv_abilitato BOOLEAN COMMENT 'abilitazione della funzionalita\'',
	avv_mail_prom_avv_pdf BOOLEAN COMMENT 'Indica se inserire l\'avviso di pagamento come allegato alla mail',
	avv_mail_prom_avv_tipo VARCHAR(35) COMMENT 'Tipo template',
	avv_mail_prom_avv_oggetto LONGTEXT COMMENT 'Template oggetto della mail',
	avv_mail_prom_avv_messaggio LONGTEXT COMMENT 'Template messaggio della mail',
	avv_mail_prom_ric_abilitato BOOLEAN COMMENT 'abilitazione della funzionalita\'',
	avv_mail_prom_ric_pdf BOOLEAN COMMENT 'Indica se inserire la ricevuta di pagamento come allegato alla mail',
	avv_mail_prom_ric_tipo VARCHAR(35) COMMENT 'Tipo template',
	avv_mail_prom_ric_oggetto LONGTEXT COMMENT 'Template oggetto della mail',
	avv_mail_prom_ric_messaggio LONGTEXT COMMENT 'Template messaggio della mail',
	avv_mail_prom_ric_eseguiti BOOLEAN COMMENT 'Indica se limitare gli invii ai soli eseguiti.',
	avv_mail_prom_scad_abilitato BOOLEAN COMMENT 'abilitazione della funzionalita\'',
	avv_mail_prom_scad_preavviso INT COMMENT 'Numero di giorni di preavviso alla scadenza.',
	avv_mail_prom_scad_tipo VARCHAR(35) COMMENT 'Tipo template',
	avv_mail_prom_scad_oggetto LONGTEXT COMMENT 'Template oggetto della mail',
	avv_mail_prom_scad_messaggio LONGTEXT COMMENT 'Template messaggio della mail',	
	-- Configurazione visualizzazione custom del dettaglio pendenza	
	visualizzazione_definizione LONGTEXT COMMENT 'Definisce la visualizzazione custom della tipologia',
	-- Configurazione della trasformazione dei tracciati csv
	trac_csv_tipo VARCHAR(35) COMMENT 'Indica il tipo del template di conversione',
	trac_csv_header_risposta LONGTEXT COMMENT 'Header del file Csv di risposta del tracciato',
	trac_csv_template_richiesta LONGTEXT COMMENT 'Template di conversione della pendenza da CSV a JSON',
	trac_csv_template_risposta LONGTEXT COMMENT 'Template di conversione della pendenza da JSON a CSV',
	-- Configurazione avvisatura via AppIO	
	app_io_api_key VARCHAR(255) COMMENT 'Api Key AppIO',
	avv_app_io_prom_avv_abilitato BOOLEAN COMMENT 'abilitazione della funzionalita\'',
	avv_app_io_prom_avv_tipo VARCHAR(35) COMMENT 'Tipo template',
	avv_app_io_prom_avv_oggetto LONGTEXT COMMENT 'Template oggetto della comunicazione',
	avv_app_io_prom_avv_messaggio LONGTEXT COMMENT 'Template messaggio della comunicazione',
	avv_app_io_prom_ric_abilitato BOOLEAN COMMENT 'abilitazione della funzionalita\'',
	avv_app_io_prom_ric_tipo VARCHAR(35) COMMENT 'Tipo template',
	avv_app_io_prom_ric_oggetto LONGTEXT COMMENT 'Template oggetto della comunicazione',
	avv_app_io_prom_ric_messaggio LONGTEXT COMMENT 'Template messaggio della comunicazione',
	avv_app_io_prom_ric_eseguiti BOOLEAN COMMENT 'Indica se limitare gli invii ai soli eseguiti.',
	avv_app_io_prom_scad_abilitato BOOLEAN COMMENT 'abilitazione della funzionalita\'',
	avv_app_io_prom_scad_preavviso INT COMMENT 'Numero di giorni di preavviso alla scadenza.',
	avv_app_io_prom_scad_tipo VARCHAR(35) COMMENT 'Tipo template',
	avv_app_io_prom_scad_oggetto LONGTEXT COMMENT 'Template oggetto della comunicazione',
	avv_app_io_prom_scad_messaggio LONGTEXT COMMENT 'Template messaggio della comunicazione',
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



CREATE TABLE documenti
(
	cod_documento VARCHAR(35) NOT NULL COMMENT 'Identificativo del documento',
	descrizione VARCHAR(255) NOT NULL COMMENT 'Descrizione del documento',
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT COMMENT 'Identificativo fisico',
	id_dominio BIGINT NOT NULL COMMENT 'Riferimento al dominio afferente',
	id_applicazione BIGINT NOT NULL COMMENT 'Riferimento al verticale afferente',
	-- unique constraints
	CONSTRAINT unique_documenti_1 UNIQUE (cod_documento,id_applicazione,id_dominio),
	-- fk/pk keys constraints
	CONSTRAINT fk_doc_id_dominio FOREIGN KEY (id_dominio) REFERENCES domini(id),
	CONSTRAINT fk_doc_id_applicazione FOREIGN KEY (id_applicazione) REFERENCES applicazioni(id),
	CONSTRAINT pk_documenti PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs COMMENT 'Aggregazioni per pagamenti rateizzati';




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
	numero_avviso VARCHAR(35) COMMENT 'Numero avviso associato alla pendenza',ack BOOLEAN NOT NULL COMMENT 'Indicazione sullo stato di ack dell\'avviso telematico',
	anomalo BOOLEAN NOT NULL COMMENT 'Indicazione sullo stato della pendenza',
	divisione VARCHAR(35) COMMENT 'Dati Divisione',
	direzione VARCHAR(35) COMMENT 'Dati Direzione',
	id_sessione VARCHAR(35) COMMENT 'Identificativo univoco assegnato al momento della creazione',
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
	data_pagamento TIMESTAMP(3) COMMENT 'Data in cui e\' stato pagato il versamento',
	importo_pagato DOUBLE NOT NULL COMMENT 'Importo Pagato',
	importo_incassato DOUBLE NOT NULL COMMENT 'Importo Incassato',
	stato_pagamento VARCHAR(35) NOT NULL COMMENT 'Stato del pagamento',
	iuv_pagamento VARCHAR(35) COMMENT 'Iuv assegnato in fase di pagamento',
	src_iuv VARCHAR(35),
	src_debitore_identificativo VARCHAR(35) NOT NULL,
	cod_rata VARCHAR(35) COMMENT 'Progressivo della rata nel caso di pagamento rateizzato',
	tipo VARCHAR(35) NOT NULL COMMENT 'Tipologia del versamento',
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
	data_notifica_avviso TIMESTAMP(3),
	avviso_notificato BOOLEAN,
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
	avv_mail_data_prom_scadenza TIMESTAMP(3),
	avv_mail_prom_scad_notificato BOOLEAN,
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
	avv_app_io_data_prom_scadenza TIMESTAMP(3),
	avv_app_io_prom_scad_notificat BOOLEAN,
	proprieta LONGTEXT,
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT COMMENT 'Identificativo fisico',
	id_tipo_versamento_dominio BIGINT NOT NULL COMMENT 'Riferimento al tipo pendenza dominio afferente',
	id_tipo_versamento BIGINT NOT NULL COMMENT 'Riferimento al tipo pendenza afferente',
	id_dominio BIGINT NOT NULL COMMENT 'Riferimento al dominio afferente',
	id_uo BIGINT COMMENT 'Riferimento all\'unita operativa afferente',
	id_applicazione BIGINT NOT NULL COMMENT 'Riferimento al verticale afferente',
	id_documento BIGINT COMMENT 'Riferimento al documento per i pagamenti rateizzati',
	-- unique constraints
	CONSTRAINT unique_versamenti_1 UNIQUE (cod_versamento_ente,id_applicazione),
	-- fk/pk keys constraints
	CONSTRAINT fk_vrs_id_tipo_versamento_dominio FOREIGN KEY (id_tipo_versamento_dominio) REFERENCES tipi_vers_domini(id),
	CONSTRAINT fk_vrs_id_tipo_versamento FOREIGN KEY (id_tipo_versamento) REFERENCES tipi_versamento(id),
	CONSTRAINT fk_vrs_id_dominio FOREIGN KEY (id_dominio) REFERENCES domini(id),
	CONSTRAINT fk_vrs_id_uo FOREIGN KEY (id_uo) REFERENCES uo(id),
	CONSTRAINT fk_vrs_id_applicazione FOREIGN KEY (id_applicazione) REFERENCES applicazioni(id),
	CONSTRAINT fk_vrs_id_documento FOREIGN KEY (id_documento) REFERENCES documenti(id),
	CONSTRAINT pk_versamenti PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs COMMENT 'Archivio dei pagamenti in attesa';

-- index
CREATE INDEX idx_vrs_id_pendenza ON versamenti (cod_versamento_ente,id_applicazione);
CREATE INDEX idx_vrs_data_creaz ON versamenti (data_creazione DESC);
CREATE INDEX idx_vrs_stato_vrs ON versamenti (stato_versamento);
CREATE INDEX idx_vrs_deb_identificativo ON versamenti (src_debitore_identificativo);
CREATE INDEX idx_vrs_iuv ON versamenti (src_iuv);
CREATE INDEX idx_vrs_auth ON versamenti (id_dominio,id_tipo_versamento,id_uo);
CREATE INDEX idx_vrs_prom_avviso ON versamenti (avviso_notificato,data_notifica_avviso DESC);
CREATE INDEX idx_vrs_avv_mail_prom_scad ON versamenti (avv_mail_prom_scad_notificato,avv_mail_data_prom_scadenza DESC);
CREATE INDEX idx_vrs_avv_io_prom_scad ON versamenti (avv_app_io_prom_scad_notificat,avv_app_io_data_prom_scadenza DESC);



CREATE TABLE singoli_versamenti
(
	cod_singolo_versamento_ente VARCHAR(70) NOT NULL COMMENT 'Identificativo della voce di pendenza',
	stato_singolo_versamento VARCHAR(35) NOT NULL COMMENT 'Stato di pagamento della voce di pendenza',
	importo_singolo_versamento DOUBLE NOT NULL COMMENT 'Importo della voce di pendenza',
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
	descrizione_causale_rpt VARCHAR(140) COMMENT 'Descrizione da inserire nella RPT',
	contabilita LONGTEXT COMMENT 'Informazioni di contabilita\' associate alla voce pendenza',
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT COMMENT 'Identificativo fisico',
	id_versamento BIGINT NOT NULL COMMENT 'Riferimento alla pendenza',
	id_tributo BIGINT COMMENT 'Riferimento alla tipologia di tribuito',
	id_iban_accredito BIGINT COMMENT 'Riferimento al conto di accredito',
	id_iban_appoggio BIGINT COMMENT 'Riferimento al conto di appoggio',
	id_dominio BIGINT COMMENT 'Riferimento al dominio',
	-- fk/pk keys constraints
	CONSTRAINT fk_sng_id_versamento FOREIGN KEY (id_versamento) REFERENCES versamenti(id),
	CONSTRAINT fk_sng_id_tributo FOREIGN KEY (id_tributo) REFERENCES tributi(id),
	CONSTRAINT fk_sng_id_iban_accredito FOREIGN KEY (id_iban_accredito) REFERENCES iban_accredito(id),
	CONSTRAINT fk_sng_id_iban_appoggio FOREIGN KEY (id_iban_appoggio) REFERENCES iban_accredito(id),
	CONSTRAINT fk_sng_id_dominio FOREIGN KEY (id_dominio) REFERENCES domini(id),
	CONSTRAINT pk_singoli_versamenti PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs COMMENT 'Voci di pendenza';

-- index
-- CREATE UNIQUE INDEX idx_sng_id_voce ON singoli_versamenti (id_versamento, indice_dati);
-- ALTER TABLE singoli_versamenti ADD CONSTRAINT unique_sng_id_voce UNIQUE USING INDEX idx_sng_id_voce;
ALTER TABLE singoli_versamenti ADD CONSTRAINT unique_sng_id_voce UNIQUE INDEX idx_sng_id_voce (id_versamento, indice_dati);


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
	src_versante_identificativo VARCHAR(35),
	severita INT COMMENT 'Livello severita in caso di pagamento fallito',
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT COMMENT 'Identificativo fisico',
	id_applicazione BIGINT COMMENT 'Riferimento all\'applicazione',
	-- fk/pk keys constraints
	CONSTRAINT fk_ppt_id_applicazione FOREIGN KEY (id_applicazione) REFERENCES applicazioni(id),
	CONSTRAINT pk_pagamenti_portale PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs COMMENT 'Richieste di pagamento';

-- index
CREATE INDEX idx_prt_stato ON pagamenti_portale (stato);
CREATE INDEX idx_prt_id_sessione ON pagamenti_portale (id_sessione);
CREATE INDEX idx_prt_id_sessione_psp ON pagamenti_portale (id_sessione_psp);
CREATE INDEX idx_prt_versante_identif ON pagamenti_portale (src_versante_identificativo);



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

-- index
CREATE INDEX idx_ppv_fk_prt ON pag_port_versamenti (id_pagamento_portale);
CREATE INDEX idx_ppv_fk_vrs ON pag_port_versamenti (id_versamento);



CREATE TABLE trac_notif_pag
(
	nome_file VARCHAR(255) NOT NULL COMMENT 'nome file tracciato',
	tipo VARCHAR(20) NOT NULL COMMENT 'Tipo Tracciato',
	versione VARCHAR(20) NOT NULL COMMENT 'Versione tracciato',
	stato VARCHAR(20) NOT NULL COMMENT 'stato della notifica',
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
	data_creazione TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT 'data creazione della notifica',
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
	data_rt_da TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT 'data minima delle RT trasmesse',
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
	data_rt_a TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT 'data massima delle RT trasmesse',
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
	data_caricamento TIMESTAMP(3) COMMENT 'data caricamento',
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
	data_completamento TIMESTAMP(3) COMMENT 'data completamento spedizione notifica',
	raw_contenuto MEDIUMBLOB COMMENT 'Contenuto tracciato',
	bean_dati LONGTEXT COMMENT 'Gestione stato elaborazione',
	identificativo VARCHAR(255) NOT NULL COMMENT 'Identificativo univoco tracciato',
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT,
	id_dominio BIGINT NOT NULL,
	-- fk/pk keys constraints
	CONSTRAINT fk_tnp_id_dominio FOREIGN KEY (id_dominio) REFERENCES domini(id),
	CONSTRAINT pk_trac_notif_pag PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;




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
	versione VARCHAR(35) NOT NULL COMMENT 'Versione dell'api PagoPA utilizzata per la transazione.',
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT COMMENT 'Identificativo fisico',
	id_versamento BIGINT NOT NULL COMMENT 'Riferimento alla pendenza oggetto della richeista di pagmaento',
	id_pagamento_portale BIGINT COMMENT 'Identificativo della richiesta di pagamento assegnato dal portale chiamante',
	-- fk/pk keys constraints
	CONSTRAINT fk_rpt_id_versamento FOREIGN KEY (id_versamento) REFERENCES versamenti(id),
	CONSTRAINT fk_rpt_id_pagamento_portale FOREIGN KEY (id_pagamento_portale) REFERENCES pagamenti_portale(id),
	CONSTRAINT pk_rpt PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs COMMENT 'Transazioni di pagmaento';

-- index
CREATE INDEX idx_rpt_cod_msg_richiesta ON rpt (cod_msg_richiesta);
CREATE INDEX idx_rpt_stato ON rpt (stato);
CREATE INDEX idx_rpt_fk_vrs ON rpt (id_versamento);
CREATE INDEX idx_rpt_fk_prt ON rpt (id_pagamento_portale);
-- CREATE UNIQUE INDEX idx_rpt_id_transazione ON rpt (iuv, ccp, cod_dominio);
-- ALTER TABLE rpt ADD CONSTRAINT unique_rpt_id_transazione UNIQUE USING INDEX idx_rpt_id_transazione;
ALTER TABLE rpt ADD CONSTRAINT unique_rpt_id_transazione UNIQUE INDEX idx_rpt_id_transazione (iuv, ccp, cod_dominio);


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
CREATE UNIQUE INDEX index_rr_1 ON rr (cod_msg_revoca);



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

-- index
CREATE INDEX idx_ntf_da_spedire ON notifiche (id_applicazione,stato,data_prossima_spedizione);



CREATE TABLE notifiche_app_io
(
	debitore_identificativo VARCHAR(35) NOT NULL COMMENT 'Identificativo del debitore della pendenza',
	cod_versamento_ente VARCHAR(35) NOT NULL COMMENT 'Identificativo della pendenza',
	cod_applicazione VARCHAR(35) NOT NULL COMMENT 'Verticale della pendenza',
	cod_dominio VARCHAR(35) NOT NULL COMMENT 'Ente Creditore della pendenza',
	iuv VARCHAR(35) NOT NULL COMMENT 'Iuv della pendenza',
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
	id_messaggio VARCHAR(255) COMMENT 'Identificativo della notifica nel sistema App IO',
	stato_messaggio VARCHAR(16) COMMENT 'Stato della notifica nel sistema App IO',
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT COMMENT 'Identificativo fisico',
	id_versamento BIGINT NOT NULL COMMENT 'Riferimento alla pendenza',
	id_tipo_versamento_dominio BIGINT NOT NULL COMMENT 'Riferimento al tipo pendenza',
	id_rpt BIGINT COMMENT 'Riferimento alla transazione di pagamento',
	-- fk/pk keys constraints
	CONSTRAINT fk_nai_id_versamento FOREIGN KEY (id_versamento) REFERENCES versamenti(id),
	CONSTRAINT fk_nai_id_tipo_versamento_dominio FOREIGN KEY (id_tipo_versamento_dominio) REFERENCES tipi_vers_domini(id),
	CONSTRAINT fk_nai_id_rpt FOREIGN KEY (id_rpt) REFERENCES rpt(id),
	CONSTRAINT pk_notifiche_app_io PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs COMMENT 'Notifiche App IO';

-- index
CREATE INDEX idx_nai_da_spedire ON notifiche_app_io (stato,data_prossima_spedizione);



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
	id_versamento BIGINT COMMENT 'Riferimento alla pendenza oggetto del promemoria',
	id_rpt BIGINT COMMENT 'Riferimento alla richiesta di pagamento oggetto del promemoria',
	id_documento BIGINT COMMENT 'Riferimento al documento nel caso di pagamenti rateizzati',
	-- fk/pk keys constraints
	CONSTRAINT fk_prm_id_versamento FOREIGN KEY (id_versamento) REFERENCES versamenti(id),
	CONSTRAINT fk_prm_id_rpt FOREIGN KEY (id_rpt) REFERENCES rpt(id),
	CONSTRAINT fk_prm_id_documento FOREIGN KEY (id_documento) REFERENCES documenti(id),
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
CREATE UNIQUE INDEX index_iuv_1 ON iuv (id_dominio,iuv);
CREATE INDEX idx_iuv_rifversamento ON iuv (cod_versamento_ente,id_applicazione,tipo_iuv);



CREATE TABLE incassi
(
	trn VARCHAR(35) NOT NULL COMMENT 'Identificativo del movimento bancario riconciliato',
	cod_dominio VARCHAR(35) NOT NULL COMMENT 'Identificaitvo dell\'ente',
	causale VARCHAR(512) COMMENT 'Causale del bonifico',
	importo DOUBLE NOT NULL COMMENT 'Importo riconciliato',
	data_valuta TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP(3) COMMENT 'Data valuta del bonifico',
	data_contabile TIMESTAMP(3) DEFAULT  CURRENT_TIMESTAMP(3) COMMENT 'Data contabile del bonifico',
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
	data_ora_incasso TIMESTAMP(3) NOT NULL DEFAULT  CURRENT_TIMESTAMP(3) COMMENT 'Data della riconciliazione',
	nome_dispositivo VARCHAR(512) COMMENT 'Riferimento al giornale di cassa',
	iban_accredito VARCHAR(35) COMMENT 'Conto di accredito',
        sct VARCHAR(35) COMMENT 'Identificativo SEPA credit transfert',
	identificativo VARCHAR(35) NOT NULL COMMENT 'Identificativo univoco della riconciliazione',
	iuv VARCHAR(35) COMMENT 'Identificativo iuv riconciliato',
	cod_flusso_rendicontazione VARCHAR(35) COMMENT 'Identificativo flusso rendicontazione riconciliato',
	stato VARCHAR(35) NOT NULL COMMENT 'Stato della riconciliazione',
	descrizione_stato VARCHAR(255) COMMENT 'Decrizione dettaglio stato nei casi di errore',
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT COMMENT 'Identificativo fisico',
	id_applicazione BIGINT COMMENT 'Riferimento all\'applicativo che ha registrato l\'inccasso',
	id_operatore BIGINT COMMENT 'Riferimento all\'operatore che ha registrato l\'inccasso',
	-- unique constraints
	CONSTRAINT unique_incassi_1 UNIQUE (cod_dominio,identificativo),
	-- fk/pk keys constraints
	CONSTRAINT fk_inc_id_applicazione FOREIGN KEY (id_applicazione) REFERENCES applicazioni(id),
	CONSTRAINT fk_inc_id_operatore FOREIGN KEY (id_operatore) REFERENCES operatori(id),
	CONSTRAINT pk_incassi PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs COMMENT 'Riconciliazioni';

-- index
CREATE UNIQUE INDEX index_incassi_1 ON incassi (cod_dominio,identificativo);



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
	data_ora_flusso TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT 'Data di emissione del flusso',
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
    -- Per versioni successive alla 5.7, rimuovere dalla sql_mode NO_ZERO_DATE 
	data_regolamento TIMESTAMP(3) COMMENT 'Data dell\'operazione di regolamento bancario',
	data_acquisizione TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT 'Data di acquisizione del flusso',
	numero_pagamenti BIGINT COMMENT 'Numero di pagamenti rendicontati',
	importo_totale_pagamenti DOUBLE COMMENT 'Importo totale rendicontato',
	cod_bic_riversamento VARCHAR(35) COMMENT 'Bic del conto di riversamento',
	xml MEDIUMBLOB NOT NULL COMMENT 'XML del flusso codfificato in base64',
	ragione_sociale_psp VARCHAR(70) COMMENT 'Ragione sociale psp che ha emesso il flusso',
	ragione_sociale_dominio VARCHAR(70) COMMENT 'Ragione sociale ente creditore',
	obsoleto BOOLEAN NOT NULL COMMENT 'Indica se il flusso e\' l\'ultimo acquisito',
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
CREATE UNIQUE INDEX index_fr_1 ON fr (cod_flusso,data_ora_flusso);



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
	-- fk/pk keys constraints
	CONSTRAINT fk_pag_id_rpt FOREIGN KEY (id_rpt) REFERENCES rpt(id),
	CONSTRAINT fk_pag_id_singolo_versamento FOREIGN KEY (id_singolo_versamento) REFERENCES singoli_versamenti(id),
	CONSTRAINT fk_pag_id_rr FOREIGN KEY (id_rr) REFERENCES rr(id),
	CONSTRAINT fk_pag_id_incasso FOREIGN KEY (id_incasso) REFERENCES incassi(id),
	CONSTRAINT pk_pagamenti PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs COMMENT 'Pagamenti';

-- index
CREATE INDEX idx_pag_fk_rpt ON pagamenti (id_rpt);
CREATE INDEX idx_pag_fk_sng ON pagamenti (id_singolo_versamento);
-- CREATE UNIQUE INDEX idx_pag_id_riscossione ON pagamenti (cod_dominio, iuv, iur, indice_dati);
-- ALTER TABLE pagamenti ADD CONSTRAINT unique_pag_id_riscossione UNIQUE USING INDEX idx_pag_id_riscossione;
ALTER TABLE pagamenti ADD CONSTRAINT unique_pag_id_riscossione UNIQUE INDEX idx_pag_id_riscossione (cod_dominio, iuv, iur, indice_dati);


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
	severita INT COMMENT 'Livello severita errore',
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

-- index
CREATE INDEX idx_evt_data ON eventi (data);
CREATE INDEX idx_evt_fk_vrs ON eventi (cod_applicazione,cod_versamento_ente);
CREATE INDEX idx_evt_id_sessione ON eventi (id_sessione);
CREATE INDEX idx_evt_iuv ON eventi (iuv);



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
CREATE UNIQUE INDEX index_batch_1 ON batch (cod_batch);



CREATE TABLE stampe
(
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
	data_creazione TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT 'Data di creazione della stampa',
	tipo VARCHAR(16) NOT NULL COMMENT 'Tipologia di stampa',
	pdf MEDIUMBLOB COMMENT 'Byte della Stampa',
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT COMMENT 'Identificativo fisico',
	id_versamento BIGINT COMMENT 'Riferimento alla pendenza',
	id_documento BIGINT COMMENT 'Riferimento al documento',
	-- unique constraints
	CONSTRAINT unique_stampe_1 UNIQUE (id_versamento,id_documento,tipo),
	-- fk/pk keys constraints
	CONSTRAINT fk_stm_id_versamento FOREIGN KEY (id_versamento) REFERENCES versamenti(id),
	CONSTRAINT fk_stm_id_documento FOREIGN KEY (id_documento) REFERENCES documenti(id),
	CONSTRAINT pk_stampe PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs COMMENT 'Stampe relative alla pendenza';

-- index
CREATE UNIQUE INDEX index_stampe_1 ON stampe (id_versamento,id_documento,tipo);



CREATE TABLE operazioni
(
	tipo_operazione VARCHAR(16) NOT NULL COMMENT 'Tipo di operazione',
	linea_elaborazione BIGINT NOT NULL COMMENT 'Numero linea nel tracciato di origine',
	stato VARCHAR(16) NOT NULL COMMENT 'Stato di elaborazione',
	dati_richiesta MEDIUMBLOB COMMENT 'Dati raw di richiesta',
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
	id_stampa BIGINT COMMENT 'Riferimento alla stampa avviso pagamento se presente',
	id_versamento BIGINT COMMENT 'Riferimento alla pendenza',
	-- fk/pk keys constraints
	CONSTRAINT fk_ope_id_tracciato FOREIGN KEY (id_tracciato) REFERENCES tracciati(id),
	CONSTRAINT fk_ope_id_applicazione FOREIGN KEY (id_applicazione) REFERENCES applicazioni(id),
	CONSTRAINT fk_ope_id_stampa FOREIGN KEY (id_stampa) REFERENCES stampe(id),
	CONSTRAINT fk_ope_id_versamento FOREIGN KEY (id_versamento) REFERENCES versamenti(id),
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

-- Correzione SQL per performance DB
ALTER TABLE versamenti DROP FOREIGN KEY fk_vrs_id_applicazione;
ALTER TABLE versamenti DROP FOREIGN KEY fk_vrs_id_dominio;
ALTER TABLE versamenti DROP FOREIGN KEY fk_vrs_id_tipo_versamento_dominio;
ALTER TABLE versamenti DROP FOREIGN KEY fk_vrs_id_tipo_versamento;
ALTER TABLE versamenti DROP FOREIGN KEY fk_vrs_id_uo;
ALTER TABLE versamenti DROP FOREIGN KEY fk_vrs_id_documento;

ALTER TABLE singoli_versamenti DROP FOREIGN KEY fk_sng_id_iban_accredito;
ALTER TABLE singoli_versamenti DROP FOREIGN KEY fk_sng_id_iban_appoggio;
ALTER TABLE singoli_versamenti DROP FOREIGN KEY fk_sng_id_tributo;
ALTER TABLE singoli_versamenti DROP FOREIGN KEY fk_sng_id_versamento;

ALTER TABLE rpt DROP FOREIGN KEY fk_rpt_id_pagamento_portale;
ALTER TABLE rpt DROP FOREIGN KEY fk_rpt_id_versamento;

ALTER TABLE pagamenti DROP FOREIGN KEY fk_pag_id_incasso;
ALTER TABLE pagamenti DROP FOREIGN KEY fk_pag_id_rpt;
ALTER TABLE pagamenti DROP FOREIGN KEY fk_pag_id_rr;
ALTER TABLE pagamenti DROP FOREIGN KEY fk_pag_id_singolo_versamento;

ALTER TABLE pagamenti_portale DROP FOREIGN KEY fk_ppt_id_applicazione;

ALTER TABLE pag_port_versamenti DROP FOREIGN KEY fk_ppv_id_pagamento_portale;
ALTER TABLE pag_port_versamenti DROP FOREIGN KEY fk_ppv_id_versamento;

-- Sezione Viste

CREATE VIEW versamenti_incassi AS SELECT
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
    versamenti.divisione,
    versamenti.direzione,	
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
    versamenti.tipo,
    versamenti.proprieta,
    documenti.descrizione AS doc_descrizione,
    (CASE WHEN versamenti.stato_versamento = 'NON_ESEGUITO' AND versamenti.data_validita > now() THEN 0 ELSE 1 END) AS smart_order_rank,
    (ABS((UNIX_TIMESTAMP(now()) *1000) - (UNIX_TIMESTAMP(COALESCE(versamenti.data_pagamento, versamenti.data_validita, versamenti.data_creazione)) * 1000))) AS smart_order_date
    FROM versamenti LEFT JOIN documenti ON versamenti.id_documento = documenti.id;

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
    singoli_versamenti.id_tributo AS id_tributo,
    versamenti.debitore_anagrafica AS debitore_anagrafica,
    fr.cod_psp AS cod_psp,
    fr.ragione_sociale_psp AS ragione_sociale_psp,
    versamenti.cod_rata AS cod_rata,
    versamenti.id_documento AS id_documento,
    versamenti.causale_versamento AS causale_versamento,
    versamenti.importo_totale AS importo_versamento,
    versamenti.numero_avviso AS numero_avviso,
    versamenti.iuv_pagamento AS iuv_pagamento,
    versamenti.data_scadenza AS data_scadenza,
    versamenti.data_creazione AS data_creazione,
    singoli_versamenti.contabilita AS contabilita
   FROM fr
     JOIN rendicontazioni ON rendicontazioni.id_fr = fr.id
     JOIN versamenti ON versamenti.iuv_versamento = rendicontazioni.iuv
     JOIN domini ON versamenti.id_dominio = domini.id AND domini.cod_dominio = fr.cod_dominio
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
    singoli_versamenti.id_tributo AS id_tributo,
    versamenti.debitore_anagrafica AS debitore_anagrafica,
    fr.cod_psp AS cod_psp,
    fr.ragione_sociale_psp AS ragione_sociale_psp,
    versamenti.cod_rata AS cod_rata,
    versamenti.id_documento AS id_documento,
    versamenti.causale_versamento AS causale_versamento,
    versamenti.importo_totale AS importo_versamento,
    versamenti.numero_avviso AS numero_avviso,
    versamenti.iuv_pagamento AS iuv_pagamento,
    versamenti.data_scadenza AS data_scadenza,
    versamenti.data_creazione AS data_creazione,
    singoli_versamenti.contabilita AS contabilita
   FROM pagamenti
     LEFT JOIN rendicontazioni ON rendicontazioni.id_pagamento = pagamenti.id
     LEFT JOIN fr ON rendicontazioni.id_fr = fr.id
     JOIN singoli_versamenti ON pagamenti.id_singolo_versamento = singoli_versamenti.id
     JOIN versamenti ON singoli_versamenti.id_versamento = versamenti.id; 

CREATE VIEW v_riscossioni AS
        SELECT cod_dominio, iuv, iur, cod_flusso, fr_iur,  data_regolamento, importo_totale_pagamenti, numero_pagamenti, importo_pagato, data_pagamento, cod_singolo_versamento_ente, indice_dati, cod_versamento_ente, applicazioni.cod_applicazione, debitore_identificativo AS identificativo_debitore, cod_anno_tributario AS anno, cod_tipo_versamento, cod_tributo AS cod_entrata, tipi_versamento.descrizione AS descr_tipo_versamento, debitore_anagrafica, cod_psp, ragione_sociale_psp, cod_rata, id_documento, causale_versamento, importo_versamento, numero_avviso, iuv_pagamento, data_scadenza, data_creazione, contabilita FROM v_riscossioni_con_rpt JOIN applicazioni ON v_riscossioni_con_rpt.id_applicazione = applicazioni.id JOIN tipi_versamento ON v_riscossioni_con_rpt.id_tipo_versamento = tipi_versamento.id LEFT JOIN tributi ON v_riscossioni_con_rpt.id_tributo = tributi.id JOIN tipi_tributo ON tributi.id_tipo_tributo = tipi_tributo.id
        UNION
        SELECT cod_dominio, iuv, iur, cod_flusso, fr_iur,  data_regolamento, importo_totale_pagamenti, numero_pagamenti, importo_pagato, data_pagamento, cod_singolo_versamento_ente, indice_dati, cod_versamento_ente, applicazioni.cod_applicazione, debitore_identificativo AS identificativo_debitore, cod_anno_tributario AS anno, cod_tipo_versamento, cod_tributo AS cod_entrata, tipi_versamento.descrizione AS descr_tipo_versamento, debitore_anagrafica, cod_psp, ragione_sociale_psp, cod_rata, id_documento, causale_versamento, importo_versamento, numero_avviso, iuv_pagamento, data_scadenza, data_creazione, contabilita FROM v_riscossioni_senza_rpt join applicazioni ON v_riscossioni_senza_rpt.id_applicazione = applicazioni.id LEFT JOIN tipi_versamento ON v_riscossioni_senza_rpt.id_tipo_versamento = tipi_versamento.id LEFT JOIN tributi ON v_riscossioni_senza_rpt.id_tributo = tributi.id LEFT JOIN tipi_tributo ON tributi.id_tipo_tributo = tipi_tributo.id;

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
  pagamenti_portale.severita,
  versamenti.debitore_identificativo as debitore_identificativo,
  versamenti.src_debitore_identificativo as src_debitore_identificativo,
  versamenti.id_dominio as id_dominio, 
  versamenti.id_uo as id_uo, 
  versamenti.id_tipo_versamento as id_tipo_versamento,
  versamenti.cod_versamento_ente as cod_versamento_ente,
  versamenti.src_iuv as src_iuv
FROM pagamenti_portale 
JOIN pag_port_versamenti ON pagamenti_portale.id = pag_port_versamenti.id_pagamento_portale 
JOIN versamenti ON versamenti.id=pag_port_versamenti.id_versamento;

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
    versamenti.cod_versamento_ente,
    applicazioni.cod_applicazione,
    eventi.iuv,
    eventi.cod_dominio,
    eventi.ccp,
    eventi.id_sessione,
    eventi.severita,
    eventi.id
   FROM versamenti
     JOIN applicazioni ON versamenti.id_applicazione = applicazioni.id
     JOIN pag_port_versamenti ON versamenti.id = pag_port_versamenti.id_versamento
     JOIN pagamenti_portale ON pag_port_versamenti.id_pagamento_portale = pagamenti_portale.id
     JOIN eventi ON eventi.id_sessione = pagamenti_portale.id_sessione);

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
	       eventi.severita,
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
	       eventi.severita,
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
	       eventi.severita,
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
	       severita,
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
	       severita,
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
	       severita,
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
	       severita,
               id FROM v_eventi_vers_tracciati;

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
    fr.obsoleto AS fr_obsoleto,
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
    singoli_versamenti.contabilita AS sng_contabilita,
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
    versamenti.iuv_pagamento AS vrs_iuv_pagamento,
    versamenti.cod_rata as vrs_cod_rata,
    versamenti.id_documento as vrs_id_documento,
    versamenti.tipo as vrs_tipo,
    versamenti.proprieta as vrs_proprieta,
    pagamenti.cod_dominio AS pag_cod_dominio,             
	pagamenti.iuv AS pag_iuv,                     
	pagamenti.indice_dati AS pag_indice_dati,             
	pagamenti.importo_pagato AS pag_importo_pagato,          
	pagamenti.data_acquisizione AS pag_data_acquisizione,       
	pagamenti.iur AS pag_iur,                     
	pagamenti.data_pagamento AS pag_data_pagamento,          
	pagamenti.commissioni_psp AS pag_commissioni_psp,         
	pagamenti.tipo_allegato AS pag_tipo_allegato,           
	pagamenti.allegato AS pag_allegato,                
	pagamenti.data_acquisizione_revoca AS pag_data_acquisizione_revoca,
	pagamenti.causale_revoca AS pag_causale_revoca,          
	pagamenti.dati_revoca AS pag_dati_revoca,             
	pagamenti.importo_revocato AS pag_importo_revocato,        
	pagamenti.esito_revoca AS pag_esito_revoca,            
	pagamenti.dati_esito_revoca AS pag_dati_esito_revoca,       
	pagamenti.stato AS pag_stato,                  
	pagamenti.tipo AS pag_tipo,                  
	rpt.iuv AS rpt_iuv,
	rpt.ccp AS rpt_ccp,
	incassi.trn AS rnc_trn
   FROM fr
     JOIN rendicontazioni ON rendicontazioni.id_fr = fr.id
     LEFT JOIN singoli_versamenti ON rendicontazioni.id_singolo_versamento = singoli_versamenti.id
     LEFT JOIN versamenti ON versamenti.id = singoli_versamenti.id_versamento 
     LEFT JOIN pagamenti on rendicontazioni.id_pagamento = pagamenti.id 
     LEFT JOIN rpt on pagamenti.id_rpt = rpt.id
     LEFT JOIN incassi on pagamenti.id_incasso = incassi.id;

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
rpt.versione as versione,
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
    versamenti.id_documento as vrs_id_documento,
    versamenti.tipo as vrs_tipo,
    versamenti.proprieta as vrs_proprieta
FROM rpt JOIN versamenti ON versamenti.id = rpt.id_versamento;

-- Vista Pagamenti/Riscossioni

CREATE VIEW v_pagamenti AS
SELECT 
	pagamenti.id AS id,
	pagamenti.cod_dominio AS cod_dominio,             
	pagamenti.iuv AS iuv,                     
	pagamenti.indice_dati AS indice_dati,             
	pagamenti.importo_pagato AS importo_pagato,          
	pagamenti.data_acquisizione AS data_acquisizione,       
	pagamenti.iur AS iur,                     
	pagamenti.data_pagamento AS data_pagamento,          
	pagamenti.commissioni_psp AS commissioni_psp,         
	pagamenti.tipo_allegato AS tipo_allegato,           
	pagamenti.allegato AS allegato,                
	pagamenti.data_acquisizione_revoca AS data_acquisizione_revoca,
	pagamenti.causale_revoca AS causale_revoca,          
	pagamenti.dati_revoca AS dati_revoca,             
	pagamenti.importo_revocato AS importo_revocato,        
	pagamenti.esito_revoca AS esito_revoca,            
	pagamenti.dati_esito_revoca AS dati_esito_revoca,       
	pagamenti.stato AS stato,                  
	pagamenti.tipo AS tipo,                  
	pagamenti.id_rpt AS id_rpt,                  
	pagamenti.id_singolo_versamento AS id_singolo_versamento,                  
	pagamenti.id_rr AS id_rr,                  
	pagamenti.id_incasso AS id_incasso,       
	versamenti.cod_versamento_ente AS vrs_cod_versamento_ente,      
	versamenti.tassonomia AS vrs_tassonomia,
	versamenti.divisione AS vrs_divisione,
	versamenti.direzione AS vrs_direzione,
	versamenti.id_tipo_versamento AS vrs_id_tipo_versamento,
	versamenti.id_tipo_versamento_dominio AS vrs_id_tipo_versamento_dominio,
	versamenti.id_dominio AS vrs_id_dominio,
	versamenti.id_uo AS vrs_id_uo,
	versamenti.id_applicazione AS vrs_id_applicazione,
	versamenti.id AS vrs_id,  
	versamenti.id_documento as vrs_id_documento,  
	singoli_versamenti.cod_singolo_versamento_ente AS sng_cod_sing_vers_ente,
	rpt.iuv AS rpt_iuv,
	rpt.ccp AS rpt_ccp,
	incassi.trn AS rnc_trn
	FROM pagamenti JOIN singoli_versamenti ON pagamenti.id_singolo_versamento = singoli_versamenti.id
	     JOIN versamenti ON singoli_versamenti.id_versamento = versamenti.id JOIN rpt ON pagamenti.id_rpt = rpt.id LEFT JOIN incassi ON pagamenti.id_incasso = incassi.id;


-- Vista Versamenti Documenti
CREATE VIEW v_versamenti AS 
SELECT versamenti.id,
    versamenti.cod_versamento_ente,
    versamenti.nome,
    versamenti.importo_totale,
    versamenti.stato_versamento,
    versamenti.descrizione_stato,
    versamenti.aggiornabile,
    versamenti.tassonomia,
    versamenti.tassonomia_avviso,
    versamenti.data_creazione,
    versamenti.data_validita,
    versamenti.data_scadenza,
    versamenti.data_ora_ultimo_aggiornamento,
    versamenti.causale_versamento,
    versamenti.debitore_identificativo,
    versamenti.debitore_tipo,
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
    versamenti.cod_lotto,
    versamenti.cod_versamento_lotto,
    versamenti.cod_anno_tributario,
    versamenti.cod_bundlekey,
    versamenti.dati_allegati,
    versamenti.incasso,
    versamenti.anomalie,
    versamenti.iuv_versamento,
    versamenti.numero_avviso,
    versamenti.ack,
    versamenti.anomalo,
    versamenti.direzione,
    versamenti.divisione,
    versamenti.id_sessione,
    versamenti.importo_pagato,
    versamenti.data_pagamento,
    versamenti.importo_incassato,
    versamenti.stato_pagamento,
    versamenti.iuv_pagamento,
    versamenti.src_debitore_identificativo,
    versamenti.src_iuv,
    versamenti.cod_rata,
    versamenti.tipo,
    versamenti.data_notifica_avviso,
    versamenti.avviso_notificato,
    versamenti.avv_mail_data_prom_scadenza,
    versamenti.avv_mail_prom_scad_notificato,
    versamenti.avv_app_io_data_prom_scadenza,
    versamenti.avv_app_io_prom_scad_notificat,
    versamenti.id_applicazione,
    versamenti.id_dominio,
    versamenti.id_uo,
    versamenti.id_tipo_versamento,
    versamenti.id_tipo_versamento_dominio,
    versamenti.id_documento,
    versamenti.proprieta,
    documenti.cod_documento,
    documenti.descrizione AS doc_descrizione
    FROM versamenti LEFT JOIN documenti ON versamenti.id_documento = documenti.id;


-- Vista Versamenti non rendicontati	     
CREATE VIEW v_vrs_non_rnd AS 
 SELECT singoli_versamenti.cod_singolo_versamento_ente AS sng_cod_sing_vers_ente,
    singoli_versamenti.importo_singolo_versamento AS sng_importo_singolo_versamento,
    singoli_versamenti.descrizione AS sng_descrizione,
    singoli_versamenti.dati_allegati AS sng_dati_allegati,
    singoli_versamenti.stato_singolo_versamento AS sng_stato_singolo_versamento,
    singoli_versamenti.indice_dati AS sng_indice_dati,
    singoli_versamenti.descrizione_causale_rpt AS sng_descrizione_causale_rpt,
    singoli_versamenti.contabilita AS sng_contabilita,
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
    versamenti.iuv_pagamento AS vrs_iuv_pagamento,
    versamenti.cod_rata AS vrs_cod_rata,
    versamenti.id_documento AS vrs_id_documento,
    versamenti.tipo AS vrs_tipo,
    versamenti.proprieta AS vrs_proprieta,
    pagamenti.id AS id,
    pagamenti.cod_dominio AS pag_cod_dominio,
    pagamenti.iuv AS pag_iuv,
    pagamenti.indice_dati AS pag_indice_dati,
    pagamenti.importo_pagato AS pag_importo_pagato,
    pagamenti.data_acquisizione AS pag_data_acquisizione,
    pagamenti.iur AS pag_iur,
    pagamenti.data_pagamento AS pag_data_pagamento,
    pagamenti.commissioni_psp AS pag_commissioni_psp,
    pagamenti.tipo_allegato AS pag_tipo_allegato,
    pagamenti.allegato AS pag_allegato,
    pagamenti.data_acquisizione_revoca AS pag_data_acquisizione_revoca,
    pagamenti.causale_revoca AS pag_causale_revoca,
    pagamenti.dati_revoca AS pag_dati_revoca,
    pagamenti.importo_revocato AS pag_importo_revocato,
    pagamenti.esito_revoca AS pag_esito_revoca,
    pagamenti.dati_esito_revoca AS pag_dati_esito_revoca,
    pagamenti.stato AS pag_stato,
    pagamenti.tipo AS pag_tipo,
    pagamenti.id_incasso AS pag_id_incasso,
    rpt.iuv AS rpt_iuv,
    rpt.ccp AS rpt_ccp,
    incassi.trn AS rnc_trn
   FROM pagamenti
     LEFT JOIN rendicontazioni ON rendicontazioni.id_pagamento = pagamenti.id
     LEFT JOIN singoli_versamenti ON pagamenti.id_singolo_versamento = singoli_versamenti.id
     LEFT JOIN versamenti ON versamenti.id = singoli_versamenti.id_versamento
     LEFT JOIN rpt ON pagamenti.id_rpt = rpt.id
     LEFT JOIN incassi ON pagamenti.id_incasso = incassi.id
  WHERE rendicontazioni.id IS NULL;


