-- 3.11.0

-- Issue #901: modello v3 delle posizioni debitorie di govpay-common-pendenze.
-- Nuove colonne su documenti e versamenti, nuove tabelle opzioni_pagamento e
-- soggetti_debitori. Le colonne NOT NULL mantengono il DEFAULT anche dopo la
-- patch, come in gov_pay.sql: il core v2 continua a inserire righe in documenti
-- e versamenti senza conoscerle.
-- Va eseguita con sqlcmd -b, per fermarsi al primo errore: senza -b sqlcmd
-- passerebbe al batch successivo.

-- documenti.descrizione da 255 a 1024 caratteri, come causale_versamento:
-- govpay-common-pendenze vi riporta la causale intera dei versamenti migrati
-- senza documento.
-- Ripetere l'ALTER su una colonna gia' a 1024 non ha effetto. v_versamenti, che
-- legge la colonna, va riallineata con sp_refreshview: le viste di SQL Server
-- tengono i metadati delle colonne fissati alla creazione.
ALTER TABLE documenti ALTER COLUMN descrizione VARCHAR(1024) NOT NULL;
GO

IF OBJECT_ID('v_versamenti', 'V') IS NOT NULL
    EXEC sp_refreshview 'v_versamenti';
GO

-- Colonne della posizione debitoria. id_unita_operativa non ha FK verso uo,
-- come nella libreria.
IF COL_LENGTH('documenti', 'id_unita_operativa') IS NULL
    ALTER TABLE documenti ADD id_unita_operativa BIGINT;
IF COL_LENGTH('documenti', 'data_pubblicazione') IS NULL
    ALTER TABLE documenti ADD data_pubblicazione DATE;
IF COL_LENGTH('documenti', 'notifica_send') IS NULL
    ALTER TABLE documenti ADD notifica_send BIT NOT NULL DEFAULT 0;
IF COL_LENGTH('documenti', 'nav_notifica') IS NULL
    ALTER TABLE documenti ADD nav_notifica VARCHAR(18);
IF COL_LENGTH('documenti', 'data_ultima_modifica_aca') IS NULL
    ALTER TABLE documenti ADD data_ultima_modifica_aca DATETIME2;
IF COL_LENGTH('documenti', 'data_ultima_comunicazione_aca') IS NULL
    ALTER TABLE documenti ADD data_ultima_comunicazione_aca DATETIME2;
IF COL_LENGTH('documenti', 'data_creazione') IS NULL
    ALTER TABLE documenti ADD data_creazione DATETIME2 NOT NULL DEFAULT CURRENT_TIMESTAMP;
IF COL_LENGTH('documenti', 'data_ultimo_aggiornamento') IS NULL
    ALTER TABLE documenti ADD data_ultimo_aggiornamento DATETIME2 NOT NULL DEFAULT CURRENT_TIMESTAMP;
IF COL_LENGTH('documenti', 'versione') IS NULL
    ALTER TABLE documenti ADD versione BIGINT NOT NULL DEFAULT 0;
GO

IF NOT EXISTS (
    SELECT 1 FROM sys.indexes
     WHERE name = 'idx_doc_app_data_creaz'
       AND object_id = OBJECT_ID('dbo.documenti')
)
    CREATE INDEX idx_doc_app_data_creaz ON documenti (id_applicazione,data_creazione,id);
GO

-- Opzioni di pagamento della posizione debitoria.
IF OBJECT_ID('opzioni_pagamento', 'U') IS NULL
CREATE TABLE opzioni_pagamento
(
	id_opzione_pagamento UNIQUEIDENTIFIER NOT NULL,
	tipologia VARCHAR(35) NOT NULL,
	giorni INT,
	stato VARCHAR(35) NOT NULL,
	data_inizio_validita DATE,
	data_scadenza DATE,
	data_creazione DATETIME2 NOT NULL,
	data_ultimo_aggiornamento DATETIME2 NOT NULL,
	versione BIGINT NOT NULL,
	-- fk/pk columns
	id BIGINT IDENTITY,
	id_documento BIGINT NOT NULL,
	-- unique constraints
	CONSTRAINT unique_opzioni_pagamento_1 UNIQUE (id_opzione_pagamento),
	-- fk/pk keys constraints
	CONSTRAINT fk_opz_id_documento FOREIGN KEY (id_documento) REFERENCES documenti(id),
	CONSTRAINT pk_opzioni_pagamento PRIMARY KEY (id)
);
GO

IF NOT EXISTS (
    SELECT 1 FROM sys.indexes
     WHERE name = 'idx_opz_id_documento'
       AND object_id = OBJECT_ID('dbo.opzioni_pagamento')
)
    CREATE INDEX idx_opz_id_documento ON opzioni_pagamento (id_documento);
GO

-- Soggetti debitori della posizione debitoria, compreso il primo (ordine 0).
IF OBJECT_ID('soggetti_debitori', 'U') IS NULL
CREATE TABLE soggetti_debitori
(
	ordine INT NOT NULL,
	tipo VARCHAR(1) NOT NULL,
	identificativo VARCHAR(35) NOT NULL,
	anagrafica VARCHAR(70),
	indirizzo VARCHAR(70),
	civico VARCHAR(16),
	cap VARCHAR(16),
	localita VARCHAR(35),
	provincia VARCHAR(35),
	nazione VARCHAR(2),
	email VARCHAR(256),
	cellulare VARCHAR(35),
	-- fk/pk columns
	id BIGINT IDENTITY,
	id_documento BIGINT NOT NULL,
	-- unique constraints
	CONSTRAINT unique_soggetti_debitori_1 UNIQUE (id_documento,ordine),
	-- fk/pk keys constraints
	CONSTRAINT fk_sgd_id_documento FOREIGN KEY (id_documento) REFERENCES documenti(id),
	CONSTRAINT pk_soggetti_debitori PRIMARY KEY (id)
);
GO

IF NOT EXISTS (
    SELECT 1 FROM sys.indexes
     WHERE name = 'idx_sgd_identificativo'
       AND object_id = OBJECT_ID('dbo.soggetti_debitori')
)
    CREATE INDEX idx_sgd_identificativo ON soggetti_debitori (identificativo);
GO

-- Collegamento della pendenza all'opzione di pagamento. Le righe create dalle
-- API v2 restano senza opzione, con numero_rata al valore di DEFAULT.
IF COL_LENGTH('versamenti', 'numero_rata') IS NULL
    ALTER TABLE versamenti ADD numero_rata INT NOT NULL DEFAULT -1;
IF COL_LENGTH('versamenti', 'id_opzione_pagamento') IS NULL
    ALTER TABLE versamenti ADD id_opzione_pagamento BIGINT;
GO

IF OBJECT_ID('fk_vrs_id_opzione_pagamento', 'F') IS NULL
    ALTER TABLE versamenti ADD CONSTRAINT fk_vrs_id_opzione_pagamento
        FOREIGN KEY (id_opzione_pagamento) REFERENCES opzioni_pagamento(id);
GO

IF NOT EXISTS (
    SELECT 1 FROM sys.indexes
     WHERE name = 'idx_vrs_id_opz_pagamento'
       AND object_id = OBJECT_ID('dbo.versamenti')
)
    CREATE INDEX idx_vrs_id_opz_pagamento ON versamenti (id_opzione_pagamento);
GO
