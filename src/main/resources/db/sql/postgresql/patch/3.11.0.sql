-- 3.11.0

-- Issue #901: modello v3 delle posizioni debitorie di govpay-common-pendenze.
-- Nuove colonne su documenti e versamenti, nuove tabelle opzioni_pagamento e
-- soggetti_debitori. Le colonne NOT NULL mantengono il DEFAULT anche dopo la
-- patch, come in gov_pay.sql: il core v2 continua a inserire righe in documenti
-- e versamenti senza conoscerle.
-- Va eseguita con psql -v ON_ERROR_STOP=1: il controllo qui sotto serve a
-- fermarla prima di qualsiasi modifica.

-- Un documento e' identificato dal codice nell'ambito dell'applicazione, non piu'
-- anche del dominio. Il vincolo e' il primo statement della patch: se ci sono
-- duplicati la patch si ferma prima di aver modificato qualcosa. I duplicati
-- vanno risolti a mano e la patch rieseguita; per elencarli:
--   SELECT cod_documento, id_applicazione, COUNT(*) FROM documenti
--    GROUP BY cod_documento, id_applicazione HAVING COUNT(*) > 1;
DO $$
DECLARE
    n BIGINT;
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_constraint
                    WHERE conname = 'unique_documenti_2'
                      AND conrelid = 'documenti'::regclass) THEN
        SELECT COUNT(*) INTO n FROM (
            SELECT 1 FROM documenti GROUP BY cod_documento, id_applicazione HAVING COUNT(*) > 1
        ) d;
        IF n > 0 THEN
            RAISE EXCEPTION 'Patch 3.11.0 interrotta prima di qualsiasi modifica: % coppie (cod_documento, id_applicazione) duplicate in documenti. Per elencarle: SELECT cod_documento, id_applicazione, COUNT(*) FROM documenti GROUP BY cod_documento, id_applicazione HAVING COUNT(*) > 1', n;
        END IF;
        ALTER TABLE documenti ADD CONSTRAINT unique_documenti_2 UNIQUE (cod_documento, id_applicazione);
    END IF;
END $$;

-- Colonne della posizione debitoria. id_unita_operativa non ha FK verso uo,
-- come nella libreria.
ALTER TABLE documenti ADD COLUMN IF NOT EXISTS id_unita_operativa BIGINT;
ALTER TABLE documenti ADD COLUMN IF NOT EXISTS data_pubblicazione DATE;
ALTER TABLE documenti ADD COLUMN IF NOT EXISTS notifica_send BOOLEAN NOT NULL DEFAULT false;
ALTER TABLE documenti ADD COLUMN IF NOT EXISTS nav_notifica VARCHAR(18);
ALTER TABLE documenti ADD COLUMN IF NOT EXISTS data_ultima_modifica_aca TIMESTAMP;
ALTER TABLE documenti ADD COLUMN IF NOT EXISTS data_ultima_comunicazione_aca TIMESTAMP;
ALTER TABLE documenti ADD COLUMN IF NOT EXISTS data_creazione TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE documenti ADD COLUMN IF NOT EXISTS data_ultimo_aggiornamento TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE documenti ADD COLUMN IF NOT EXISTS versione BIGINT NOT NULL DEFAULT 0;

CREATE INDEX IF NOT EXISTS idx_doc_app_data_creaz ON documenti (id_applicazione,data_creazione,id);

-- Opzioni di pagamento della posizione debitoria.
CREATE SEQUENCE IF NOT EXISTS seq_opzioni_pagamento start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE IF NOT EXISTS opzioni_pagamento
(
	id_opzione_pagamento UUID NOT NULL,
	tipologia VARCHAR(35) NOT NULL,
	giorni INT,
	stato VARCHAR(35) NOT NULL,
	data_inizio_validita DATE,
	data_scadenza DATE,
	data_creazione TIMESTAMP NOT NULL,
	data_ultimo_aggiornamento TIMESTAMP NOT NULL,
	versione BIGINT NOT NULL,
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_opzioni_pagamento') NOT NULL,
	id_documento BIGINT NOT NULL,
	-- unique constraints
	CONSTRAINT unique_opzioni_pagamento_1 UNIQUE (id_opzione_pagamento),
	-- fk/pk keys constraints
	CONSTRAINT fk_opz_id_documento FOREIGN KEY (id_documento) REFERENCES documenti(id),
	CONSTRAINT pk_opzioni_pagamento PRIMARY KEY (id)
);

CREATE INDEX IF NOT EXISTS idx_opz_id_documento ON opzioni_pagamento (id_documento);

-- Soggetti debitori della posizione debitoria, compreso il primo (ordine 0).
CREATE SEQUENCE IF NOT EXISTS seq_soggetti_debitori start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

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
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_soggetti_debitori') NOT NULL,
	id_documento BIGINT NOT NULL,
	-- unique constraints
	CONSTRAINT unique_soggetti_debitori_1 UNIQUE (id_documento,ordine),
	-- fk/pk keys constraints
	CONSTRAINT fk_sgd_id_documento FOREIGN KEY (id_documento) REFERENCES documenti(id),
	CONSTRAINT pk_soggetti_debitori PRIMARY KEY (id)
);

CREATE INDEX IF NOT EXISTS idx_sgd_identificativo ON soggetti_debitori (identificativo);

-- Collegamento della pendenza all'opzione di pagamento. Le righe create dalle
-- API v2 restano senza opzione, con numero_rata al valore di DEFAULT.
ALTER TABLE versamenti ADD COLUMN IF NOT EXISTS numero_rata INT NOT NULL DEFAULT -1;
ALTER TABLE versamenti ADD COLUMN IF NOT EXISTS id_opzione_pagamento BIGINT;

DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_constraint
                    WHERE conname = 'fk_vrs_id_opzione_pagamento'
                      AND conrelid = 'versamenti'::regclass) THEN
        ALTER TABLE versamenti ADD CONSTRAINT fk_vrs_id_opzione_pagamento
            FOREIGN KEY (id_opzione_pagamento) REFERENCES opzioni_pagamento(id);
    END IF;
END $$;

CREATE INDEX IF NOT EXISTS idx_vrs_id_opz_pagamento ON versamenti (id_opzione_pagamento);
