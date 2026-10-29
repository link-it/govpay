-- 3.11.0

-- Issue #901: modello v3 delle posizioni debitorie di govpay-common-pendenze.
-- Nuove colonne su documenti e versamenti, nuove tabelle opzioni_pagamento e
-- soggetti_debitori. Le colonne NOT NULL mantengono il DEFAULT anche dopo la
-- patch, come in gov_pay.sql: il core v2 continua a inserire righe in documenti
-- e versamenti senza conoscerle.
-- Le guardie sono quelle della 3.10.0: condizione su information_schema e
-- statement preparato, con 'DO 0' come ramo a vuoto.

-- documenti.descrizione da 255 a 1024 caratteri, come causale_versamento:
-- govpay-common-pendenze vi riporta la causale intera dei versamenti migrati
-- senza documento.
-- La guardia evita di rieseguire il MODIFY, che oltre i 255 byte riscrive la
-- tabella.
SET @gp_sql := IF(
    (SELECT COUNT(*) FROM information_schema.columns
      WHERE table_schema = DATABASE()
        AND table_name = 'documenti'
        AND column_name = 'descrizione'
        AND character_maximum_length < 1024) > 0,
    'ALTER TABLE documenti MODIFY COLUMN descrizione VARCHAR(1024) NOT NULL COMMENT ''Descrizione del documento''',
    'DO 0');
PREPARE gp_stmt FROM @gp_sql;
EXECUTE gp_stmt;
DEALLOCATE PREPARE gp_stmt;

-- Colonne della posizione debitoria. id_unita_operativa non ha FK verso uo,
-- come nella libreria.
SET @gp_sql := IF(
    (SELECT COUNT(*) FROM information_schema.columns
      WHERE table_schema = DATABASE()
        AND table_name = 'documenti'
        AND column_name = 'id_unita_operativa') = 0,
    'ALTER TABLE documenti ADD COLUMN id_unita_operativa BIGINT COMMENT ''Identificativo dell\\''unita operativa della posizione debitoria''',
    'DO 0');
PREPARE gp_stmt FROM @gp_sql;
EXECUTE gp_stmt;
DEALLOCATE PREPARE gp_stmt;

SET @gp_sql := IF(
    (SELECT COUNT(*) FROM information_schema.columns
      WHERE table_schema = DATABASE()
        AND table_name = 'documenti'
        AND column_name = 'data_pubblicazione') = 0,
    'ALTER TABLE documenti ADD COLUMN data_pubblicazione DATE COMMENT ''Data di pubblicazione della posizione debitoria''',
    'DO 0');
PREPARE gp_stmt FROM @gp_sql;
EXECUTE gp_stmt;
DEALLOCATE PREPARE gp_stmt;

SET @gp_sql := IF(
    (SELECT COUNT(*) FROM information_schema.columns
      WHERE table_schema = DATABASE()
        AND table_name = 'documenti'
        AND column_name = 'notifica_send') = 0,
    'ALTER TABLE documenti ADD COLUMN notifica_send BOOLEAN NOT NULL DEFAULT false COMMENT ''Indica se la posizione debitoria va notificata tramite SEND''',
    'DO 0');
PREPARE gp_stmt FROM @gp_sql;
EXECUTE gp_stmt;
DEALLOCATE PREPARE gp_stmt;

SET @gp_sql := IF(
    (SELECT COUNT(*) FROM information_schema.columns
      WHERE table_schema = DATABASE()
        AND table_name = 'documenti'
        AND column_name = 'nav_notifica') = 0,
    'ALTER TABLE documenti ADD COLUMN nav_notifica VARCHAR(18) COMMENT ''Numero avviso della notifica SEND''',
    'DO 0');
PREPARE gp_stmt FROM @gp_sql;
EXECUTE gp_stmt;
DEALLOCATE PREPARE gp_stmt;

SET @gp_sql := IF(
    (SELECT COUNT(*) FROM information_schema.columns
      WHERE table_schema = DATABASE()
        AND table_name = 'documenti'
        AND column_name = 'data_ultima_modifica_aca') = 0,
    'ALTER TABLE documenti ADD COLUMN data_ultima_modifica_aca DATETIME(3) COMMENT ''Data ultima modifica rilevante per l\\''ACA''',
    'DO 0');
PREPARE gp_stmt FROM @gp_sql;
EXECUTE gp_stmt;
DEALLOCATE PREPARE gp_stmt;

SET @gp_sql := IF(
    (SELECT COUNT(*) FROM information_schema.columns
      WHERE table_schema = DATABASE()
        AND table_name = 'documenti'
        AND column_name = 'data_ultima_comunicazione_aca') = 0,
    'ALTER TABLE documenti ADD COLUMN data_ultima_comunicazione_aca DATETIME(3) COMMENT ''Data ultima comunicazione all\\''ACA''',
    'DO 0');
PREPARE gp_stmt FROM @gp_sql;
EXECUTE gp_stmt;
DEALLOCATE PREPARE gp_stmt;

SET @gp_sql := IF(
    (SELECT COUNT(*) FROM information_schema.columns
      WHERE table_schema = DATABASE()
        AND table_name = 'documenti'
        AND column_name = 'data_creazione') = 0,
    'ALTER TABLE documenti ADD COLUMN data_creazione DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT ''Data di creazione del documento''',
    'DO 0');
PREPARE gp_stmt FROM @gp_sql;
EXECUTE gp_stmt;
DEALLOCATE PREPARE gp_stmt;

SET @gp_sql := IF(
    (SELECT COUNT(*) FROM information_schema.columns
      WHERE table_schema = DATABASE()
        AND table_name = 'documenti'
        AND column_name = 'data_ultimo_aggiornamento') = 0,
    'ALTER TABLE documenti ADD COLUMN data_ultimo_aggiornamento DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT ''Data di ultimo aggiornamento del documento''',
    'DO 0');
PREPARE gp_stmt FROM @gp_sql;
EXECUTE gp_stmt;
DEALLOCATE PREPARE gp_stmt;

SET @gp_sql := IF(
    (SELECT COUNT(*) FROM information_schema.columns
      WHERE table_schema = DATABASE()
        AND table_name = 'documenti'
        AND column_name = 'versione') = 0,
    'ALTER TABLE documenti ADD COLUMN versione BIGINT NOT NULL DEFAULT 0 COMMENT ''Versione per il lock ottimistico di govpay-common-pendenze''',
    'DO 0');
PREPARE gp_stmt FROM @gp_sql;
EXECUTE gp_stmt;
DEALLOCATE PREPARE gp_stmt;

SET @gp_sql := IF(
    (SELECT COUNT(*) FROM information_schema.statistics
      WHERE table_schema = DATABASE()
        AND table_name = 'documenti'
        AND index_name = 'idx_doc_app_data_creaz') = 0,
    'CREATE INDEX idx_doc_app_data_creaz ON documenti (id_applicazione,data_creazione,id)',
    'DO 0');
PREPARE gp_stmt FROM @gp_sql;
EXECUTE gp_stmt;
DEALLOCATE PREPARE gp_stmt;

-- Opzioni di pagamento della posizione debitoria.
CREATE TABLE IF NOT EXISTS opzioni_pagamento
(
	id_opzione_pagamento BINARY(16) NOT NULL COMMENT 'Identificativo UUID dell\'opzione di pagamento',
	tipologia VARCHAR(35) NOT NULL COMMENT 'Tipologia dell\'opzione di pagamento',
	giorni INT COMMENT 'Giorni per le tipologie SOLUZIONE_UNICA_ENTRO e SOLUZIONE_UNICA_OLTRE',
	stato VARCHAR(35) NOT NULL COMMENT 'Stato dell\'opzione di pagamento',
	data_inizio_validita DATE COMMENT 'Data di inizio validita\'',
	data_scadenza DATE COMMENT 'Data di scadenza',
	data_creazione DATETIME(3) NOT NULL COMMENT 'Data di creazione',
	data_ultimo_aggiornamento DATETIME(3) NOT NULL COMMENT 'Data di ultimo aggiornamento',
	versione BIGINT NOT NULL COMMENT 'Versione per il lock ottimistico di govpay-common-pendenze',
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT COMMENT 'Identificativo fisico',
	id_documento BIGINT NOT NULL COMMENT 'Riferimento alla posizione debitoria',
	-- unique constraints
	CONSTRAINT unique_opzioni_pagamento_1 UNIQUE (id_opzione_pagamento),
	-- fk/pk keys constraints
	CONSTRAINT fk_opz_id_documento FOREIGN KEY (id_documento) REFERENCES documenti(id),
	CONSTRAINT pk_opzioni_pagamento PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs COMMENT 'Opzioni di pagamento delle posizioni debitorie';

SET @gp_sql := IF(
    (SELECT COUNT(*) FROM information_schema.statistics
      WHERE table_schema = DATABASE()
        AND table_name = 'opzioni_pagamento'
        AND index_name = 'idx_opz_id_documento') = 0,
    'CREATE INDEX idx_opz_id_documento ON opzioni_pagamento (id_documento)',
    'DO 0');
PREPARE gp_stmt FROM @gp_sql;
EXECUTE gp_stmt;
DEALLOCATE PREPARE gp_stmt;

-- Soggetti debitori della posizione debitoria, compreso il primo (ordine 0).
CREATE TABLE IF NOT EXISTS soggetti_debitori
(
	ordine INT NOT NULL COMMENT 'Posizione del soggetto nella posizione debitoria',
	tipo VARCHAR(1) NOT NULL COMMENT 'Tipo soggetto, persona fisica (F) o giuridica (G)',
	identificativo VARCHAR(35) NOT NULL COMMENT 'Codice fiscale o partita iva del soggetto',
	anagrafica VARCHAR(70) COMMENT 'Nome e cognome o ragione sociale',
	indirizzo VARCHAR(70) COMMENT 'Indirizzo',
	civico VARCHAR(16) COMMENT 'Numero civico',
	cap VARCHAR(16) COMMENT 'CAP',
	localita VARCHAR(35) COMMENT 'Localita\'',
	provincia VARCHAR(35) COMMENT 'Provincia',
	nazione VARCHAR(2) COMMENT 'Nazione',
	email VARCHAR(256) COMMENT 'Indirizzo email',
	cellulare VARCHAR(35) COMMENT 'Numero di cellulare',
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT COMMENT 'Identificativo fisico',
	id_documento BIGINT NOT NULL COMMENT 'Riferimento alla posizione debitoria',
	-- unique constraints
	CONSTRAINT unique_soggetti_debitori_1 UNIQUE (id_documento,ordine),
	-- fk/pk keys constraints
	CONSTRAINT fk_sgd_id_documento FOREIGN KEY (id_documento) REFERENCES documenti(id),
	CONSTRAINT pk_soggetti_debitori PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs COMMENT 'Soggetti debitori delle posizioni debitorie';

SET @gp_sql := IF(
    (SELECT COUNT(*) FROM information_schema.statistics
      WHERE table_schema = DATABASE()
        AND table_name = 'soggetti_debitori'
        AND index_name = 'idx_sgd_identificativo') = 0,
    'CREATE INDEX idx_sgd_identificativo ON soggetti_debitori (identificativo)',
    'DO 0');
PREPARE gp_stmt FROM @gp_sql;
EXECUTE gp_stmt;
DEALLOCATE PREPARE gp_stmt;

-- Collegamento della pendenza all'opzione di pagamento. Le righe create dalle
-- API v2 restano senza opzione, con numero_rata al valore di DEFAULT.
SET @gp_sql := IF(
    (SELECT COUNT(*) FROM information_schema.columns
      WHERE table_schema = DATABASE()
        AND table_name = 'versamenti'
        AND column_name = 'numero_rata') = 0,
    'ALTER TABLE versamenti ADD COLUMN numero_rata INT NOT NULL DEFAULT -1 COMMENT ''Numero della rata nell\\''opzione di pagamento del modello v3''',
    'DO 0');
PREPARE gp_stmt FROM @gp_sql;
EXECUTE gp_stmt;
DEALLOCATE PREPARE gp_stmt;

SET @gp_sql := IF(
    (SELECT COUNT(*) FROM information_schema.columns
      WHERE table_schema = DATABASE()
        AND table_name = 'versamenti'
        AND column_name = 'id_opzione_pagamento') = 0,
    'ALTER TABLE versamenti ADD COLUMN id_opzione_pagamento BIGINT COMMENT ''Riferimento all\\''opzione di pagamento del modello v3''',
    'DO 0');
PREPARE gp_stmt FROM @gp_sql;
EXECUTE gp_stmt;
DEALLOCATE PREPARE gp_stmt;

SET @gp_sql := IF(
    (SELECT COUNT(*) FROM information_schema.table_constraints
      WHERE table_schema = DATABASE()
        AND table_name = 'versamenti'
        AND constraint_name = 'fk_vrs_id_opzione_pagamento') = 0,
    'ALTER TABLE versamenti ADD CONSTRAINT fk_vrs_id_opzione_pagamento FOREIGN KEY (id_opzione_pagamento) REFERENCES opzioni_pagamento(id)',
    'DO 0');
PREPARE gp_stmt FROM @gp_sql;
EXECUTE gp_stmt;
DEALLOCATE PREPARE gp_stmt;

SET @gp_sql := IF(
    (SELECT COUNT(*) FROM information_schema.statistics
      WHERE table_schema = DATABASE()
        AND table_name = 'versamenti'
        AND index_name = 'idx_vrs_id_opz_pagamento') = 0,
    'CREATE INDEX idx_vrs_id_opz_pagamento ON versamenti (id_opzione_pagamento)',
    'DO 0');
PREPARE gp_stmt FROM @gp_sql;
EXECUTE gp_stmt;
DEALLOCATE PREPARE gp_stmt;
