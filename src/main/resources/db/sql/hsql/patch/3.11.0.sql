-- 3.11.0

-- Issue #901: modello v3 delle posizioni debitorie di govpay-common-pendenze.
-- Nuove colonne su documenti e versamenti, nuove tabelle opzioni_pagamento e
-- soggetti_debitori. Le colonne NOT NULL mantengono il DEFAULT anche dopo la
-- patch, come in gov_pay.sql: il core v2 continua a inserire righe in documenti
-- e versamenti senza conoscerle.
-- A differenza della 3.10.0 questa patch e' riapplicabile: HSQLDB non ha
-- costrutti condizionali negli script, ma accetta IF NOT EXISTS su CREATE
-- SEQUENCE, CREATE TABLE, CREATE INDEX, ADD COLUMN e ADD CONSTRAINT, e tutti gli
-- statement qui sotto lo usano, o sono idempotenti di per se'. Verificato su
-- HSQLDB 2.7.2 e 2.7.3.

-- documenti.descrizione da 255 a 1024 caratteri, come causale_versamento:
-- govpay-common-pendenze vi riporta la causale intera dei versamenti migrati
-- senza documento.
-- HSQLDB cambia il tipo anche sotto una vista, ma la vista resta con la
-- lunghezza di prima: v_versamenti va ricreata, con la stessa definizione di
-- gov_pay.sql.
DROP VIEW IF EXISTS v_versamenti;
ALTER TABLE documenti ALTER COLUMN descrizione SET DATA TYPE VARCHAR(1024);
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
    versamenti.data_ultima_modifica_aca,
    versamenti.data_ultima_comunicazione_aca,
    documenti.cod_documento,
    documenti.descrizione AS doc_descrizione
    FROM versamenti LEFT JOIN documenti ON versamenti.id_documento = documenti.id;

-- Colonne della posizione debitoria. id_unita_operativa non ha FK verso uo,
-- come nella libreria. HSQLDB vuole DEFAULT prima di NOT NULL.
ALTER TABLE documenti ADD COLUMN IF NOT EXISTS id_unita_operativa BIGINT;
ALTER TABLE documenti ADD COLUMN IF NOT EXISTS data_pubblicazione DATE;
ALTER TABLE documenti ADD COLUMN IF NOT EXISTS notifica_send BOOLEAN DEFAULT false NOT NULL;
ALTER TABLE documenti ADD COLUMN IF NOT EXISTS nav_notifica VARCHAR(18);
ALTER TABLE documenti ADD COLUMN IF NOT EXISTS data_ultima_modifica_aca TIMESTAMP;
ALTER TABLE documenti ADD COLUMN IF NOT EXISTS data_ultima_comunicazione_aca TIMESTAMP;
ALTER TABLE documenti ADD COLUMN IF NOT EXISTS data_creazione TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL;
ALTER TABLE documenti ADD COLUMN IF NOT EXISTS data_ultimo_aggiornamento TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL;
ALTER TABLE documenti ADD COLUMN IF NOT EXISTS versione BIGINT DEFAULT 0 NOT NULL;

CREATE INDEX IF NOT EXISTS idx_doc_app_data_creaz ON documenti (id_applicazione,data_creazione,id);

-- Opzioni di pagamento della posizione debitoria.
-- La tabella _init_seq e' quella che gov_pay.sql crea per ogni sequence; la
-- INSERT consuma il primo valore solo se la tabella e' vuota, come
-- all'installazione da zero.
CREATE SEQUENCE IF NOT EXISTS seq_opzioni_pagamento AS BIGINT START WITH 1 INCREMENT BY 1 ;

CREATE TABLE IF NOT EXISTS opzioni_pagamento
(
	id_opzione_pagamento BINARY(16) NOT NULL,
	tipologia VARCHAR(35) NOT NULL,
	giorni INT,
	stato VARCHAR(35) NOT NULL,
	data_inizio_validita DATE,
	data_scadenza DATE,
	data_creazione TIMESTAMP NOT NULL,
	data_ultimo_aggiornamento TIMESTAMP NOT NULL,
	versione BIGINT NOT NULL,
	-- fk/pk columns
	id BIGINT GENERATED BY DEFAULT AS SEQUENCE seq_opzioni_pagamento,
	id_documento BIGINT NOT NULL,
	-- unique constraints
	CONSTRAINT unique_opzioni_pagamento_1 UNIQUE (id_opzione_pagamento),
	-- fk/pk keys constraints
	CONSTRAINT fk_opz_id_documento FOREIGN KEY (id_documento) REFERENCES documenti(id),
	CONSTRAINT pk_opzioni_pagamento PRIMARY KEY (id)
);

CREATE INDEX IF NOT EXISTS idx_opz_id_documento ON opzioni_pagamento (id_documento);
CREATE TABLE IF NOT EXISTS opzioni_pagamento_init_seq (id BIGINT);
INSERT INTO opzioni_pagamento_init_seq
    SELECT NEXT VALUE FOR seq_opzioni_pagamento FROM (VALUES(0))
     WHERE NOT EXISTS (SELECT 1 FROM opzioni_pagamento_init_seq);

-- Soggetti debitori della posizione debitoria, compreso il primo (ordine 0).
CREATE SEQUENCE IF NOT EXISTS seq_soggetti_debitori AS BIGINT START WITH 1 INCREMENT BY 1 ;

CREATE TABLE IF NOT EXISTS soggetti_debitori
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
	id BIGINT GENERATED BY DEFAULT AS SEQUENCE seq_soggetti_debitori,
	id_documento BIGINT NOT NULL,
	-- unique constraints
	CONSTRAINT unique_soggetti_debitori_1 UNIQUE (id_documento,ordine),
	-- fk/pk keys constraints
	CONSTRAINT fk_sgd_id_documento FOREIGN KEY (id_documento) REFERENCES documenti(id),
	CONSTRAINT pk_soggetti_debitori PRIMARY KEY (id)
);

CREATE INDEX IF NOT EXISTS idx_sgd_identificativo ON soggetti_debitori (identificativo);
CREATE TABLE IF NOT EXISTS soggetti_debitori_init_seq (id BIGINT);
INSERT INTO soggetti_debitori_init_seq
    SELECT NEXT VALUE FOR seq_soggetti_debitori FROM (VALUES(0))
     WHERE NOT EXISTS (SELECT 1 FROM soggetti_debitori_init_seq);

-- Collegamento della pendenza all'opzione di pagamento. Le righe create dalle
-- API v2 restano senza opzione, con numero_rata al valore di DEFAULT.
ALTER TABLE versamenti ADD COLUMN IF NOT EXISTS numero_rata INT DEFAULT -1 NOT NULL;
ALTER TABLE versamenti ADD COLUMN IF NOT EXISTS id_opzione_pagamento BIGINT;
ALTER TABLE versamenti ADD CONSTRAINT IF NOT EXISTS fk_vrs_id_opzione_pagamento
    FOREIGN KEY (id_opzione_pagamento) REFERENCES opzioni_pagamento(id);

CREATE INDEX IF NOT EXISTS idx_vrs_id_opz_pagamento ON versamenti (id_opzione_pagamento);
