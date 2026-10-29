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


-- ===========================================================================
-- Migrazione dei dati al modello v3
-- ===========================================================================
-- Porta i dati v2 nel modello delle posizioni debitorie di
-- govpay-common-pendenze, con le stesse regole dei suoi script
-- migrazione/05_documenti_orfani.sql e 06_opzioni_pagamento_documenti_esistenti.sql
-- (versione PostgreSQL), piu' i soggetti debitori e le colonne nuove di
-- documenti, che la libreria non valorizza.
-- Gira a ogni aggiornamento ed e' rieseguibile: ogni passo considera solo i
-- documenti i cui versamenti non hanno ancora un'opzione di pagamento. I casi che
-- non si possono migrare restano come sono e finiscono in migrazione_v3_scarti,
-- svuotata a ogni esecuzione: dopo averli corretti si riesegue la patch.
-- Le tabelle gp_mig_* sono di lavoro e vengono eliminate in coda. Sono tabelle
-- normali e non TEMPORARY perche' MySQL non permette di leggere due volte una
-- tabella temporanea nella stessa istruzione.

-- Casi non migrati dell'ultima esecuzione. Non e' in gov_pay.sql: si elimina a
-- bonifica conclusa, con DROP TABLE migrazione_v3_scarti.
CREATE TABLE IF NOT EXISTS migrazione_v3_scarti
(
	id_documento BIGINT COMMENT 'Documento non migrato',
	id_versamento BIGINT COMMENT 'Versamento non migrato',
	motivo VARCHAR(35) NOT NULL COMMENT 'Motivo dello scarto',
	dettaglio VARCHAR(255) COMMENT 'Dato che ha causato lo scarto',
	data DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT 'Data della migrazione'
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs COMMENT 'Casi non migrati al modello v3 (Issue #901)';

DELETE FROM migrazione_v3_scarti;

DROP TABLE IF EXISTS gp_mig_versamenti;
DROP TABLE IF EXISTS gp_mig_opzioni;

-- 1. Versamenti senza documento. In v3 ogni pendenza appartiene a una posizione
-- debitoria: per ciascuno si crea un documento 'MIGR-' || id del versamento, con
-- la causale come descrizione. La causale v2 e' '01 ' || base64(UTF-8), come la
-- scrive Versamento.CausaleSemplice: la validita' del base64 si controlla prima,
-- perche' su MariaDB FROM_BASE64 di un testo non valido e' un errore, non NULL.
-- Il testo decodificato si converte in latin1, il set di caratteri delle tabelle:
-- i caratteri che latin1 non ha diventano '?'.
-- Si scartano le causali che non si decodificano (i formati '02' e '03' non sono
-- piu' prodotti da nessun percorso di scrittura) e i versamenti il cui codice e'
-- gia' usato da un documento della stessa applicazione e dello stesso dominio.
INSERT INTO migrazione_v3_scarti (id_versamento, motivo, dettaglio)
SELECT v.id, 'CAUSALE_NON_DECODIFICABILE', SUBSTRING(v.causale_versamento, 1, 255)
  FROM versamenti v
 WHERE v.id_documento IS NULL
   AND v.causale_versamento IS NOT NULL
   AND (SUBSTRING(v.causale_versamento, 1, 3) <> '01 '
        OR SUBSTRING(v.causale_versamento, 4) NOT REGEXP '^[A-Za-z0-9+/]+={0,2}$'
        OR MOD(LENGTH(SUBSTRING(v.causale_versamento, 4)), 4) <> 0);

INSERT INTO migrazione_v3_scarti (id_versamento, motivo, dettaglio)
SELECT v.id, 'COLLISIONE_COD_DOCUMENTO', CONCAT('MIGR-', v.id)
  FROM versamenti v
 WHERE v.id_documento IS NULL
   AND EXISTS (SELECT 1 FROM documenti d
                WHERE d.cod_documento = CONCAT('MIGR-', v.id)
                  AND d.id_applicazione = v.id_applicazione
                  AND d.id_dominio = v.id_dominio);

INSERT INTO documenti (cod_documento, descrizione, id_dominio, id_applicazione)
SELECT CONCAT('MIGR-', v.id),
       COALESCE(CONVERT(CONVERT(FROM_BASE64(SUBSTRING(v.causale_versamento, 4)) USING utf8mb4) USING latin1), ''),
       v.id_dominio,
       v.id_applicazione
  FROM versamenti v
 WHERE v.id_documento IS NULL
   AND NOT EXISTS (SELECT 1 FROM migrazione_v3_scarti s WHERE s.id_versamento = v.id);

UPDATE versamenti v
  JOIN documenti d
    ON d.cod_documento = CONCAT('MIGR-', v.id)
   AND d.id_applicazione = v.id_applicazione
   AND d.id_dominio = v.id_dominio
   SET v.id_documento = d.id
 WHERE v.id_documento IS NULL
   AND NOT EXISTS (SELECT 1 FROM migrazione_v3_scarti s WHERE s.id_versamento = v.id);

-- 2. Colonne nuove di documenti, dai versamenti: data_creazione la minima,
-- data_ultimo_aggiornamento e le date ACA le massime, id_unita_operativa solo se
-- e' la stessa per tutti i versamenti. Al posto dei DEFAULT impostati
-- dall'aggiunta delle colonne.
UPDATE documenti d
  JOIN (SELECT v.id_documento,
               MIN(v.data_creazione) AS data_creazione,
               MAX(v.data_ora_ultimo_aggiornamento) AS data_ultimo_aggiornamento,
               MAX(v.data_ultima_modifica_aca) AS data_ultima_modifica_aca,
               MAX(v.data_ultima_comunicazione_aca) AS data_ultima_comunicazione_aca,
               CASE WHEN COUNT(v.id_uo) = COUNT(*) AND COUNT(DISTINCT v.id_uo) = 1
                    THEN MAX(v.id_uo) END AS id_unita_operativa
          FROM versamenti v
         WHERE v.id_documento IS NOT NULL
         GROUP BY v.id_documento
        HAVING COUNT(v.id_opzione_pagamento) = 0) x
    ON x.id_documento = d.id
   SET d.data_creazione = x.data_creazione,
       d.data_ultimo_aggiornamento = x.data_ultimo_aggiornamento,
       d.data_ultima_modifica_aca = x.data_ultima_modifica_aca,
       d.data_ultima_comunicazione_aca = x.data_ultima_comunicazione_aca,
       d.id_unita_operativa = x.id_unita_operativa;

-- 3. Soggetti debitori. Un soggetto con ordine 0 per documento, dai campi
-- debitore_* del suo primo versamento. Telefono e fax non hanno una colonna di
-- destinazione e restano su versamenti. debitore_tipo e' facoltativo in v2 ma
-- obbligatorio in v3: se manca, G per una partita IVA (11 cifre), F altrimenti.
-- I documenti con debitori diversi tra i versamenti si scartano.
INSERT INTO migrazione_v3_scarti (id_documento, motivo)
SELECT v.id_documento, 'DEBITORI_INCOERENTI'
  FROM versamenti v
 WHERE v.id_documento IS NOT NULL
   AND NOT EXISTS (SELECT 1 FROM soggetti_debitori s WHERE s.id_documento = v.id_documento)
 GROUP BY v.id_documento
HAVING COUNT(v.id_opzione_pagamento) = 0
   AND COUNT(DISTINCT CONCAT(COALESCE(v.debitore_tipo,
           CASE WHEN v.debitore_identificativo REGEXP '^[0-9]{11}$' THEN 'G' ELSE 'F' END),
           '|', v.debitore_identificativo)) > 1;

INSERT INTO soggetti_debitori (ordine, tipo, identificativo, anagrafica, indirizzo, civico, cap,
        localita, provincia, nazione, email, cellulare, id_documento)
SELECT 0,
       COALESCE(v.debitore_tipo,
           CASE WHEN v.debitore_identificativo REGEXP '^[0-9]{11}$' THEN 'G' ELSE 'F' END),
       v.debitore_identificativo, v.debitore_anagrafica, v.debitore_indirizzo,
       v.debitore_civico, v.debitore_cap, v.debitore_localita, v.debitore_provincia,
       v.debitore_nazione, v.debitore_email, v.debitore_cellulare, v.id_documento
  FROM versamenti v
  JOIN (SELECT MIN(p.id) AS id FROM versamenti p
         WHERE p.id_documento IS NOT NULL
         GROUP BY p.id_documento
        HAVING COUNT(p.id_opzione_pagamento) = 0) primo ON primo.id = v.id
 WHERE NOT EXISTS (SELECT 1 FROM soggetti_debitori s WHERE s.id_documento = v.id_documento)
   AND NOT EXISTS (SELECT 1 FROM migrazione_v3_scarti s
                    WHERE s.id_documento = v.id_documento AND s.motivo = 'DEBITORI_INCOERENTI');

-- 4. Opzioni di pagamento, dai valori di cod_rata dei versamenti del documento:
--  A) tutti NULL: una SOLUZIONE_UNICA con tutti i versamenti, numero_rata 1..n
--     in ordine di id;
--  B) tutti numerici, senza ripetizioni: un PIANO_RATEALE, numero_rata = cod_rata;
--  C) tutti soglie (ENTRO<gg>, OLTRE<gg>, RIDOTTO, SCONTATO), senza ripetizioni:
--     un'opzione per versamento, SOLUZIONE_UNICA_ENTRO/_OLTRE con giorni dal
--     codice, SOLUZIONE_UNICA_RIDOTTO/_SCONTATO senza giorni; numero_rata 1.
-- Stato: per A e B ANNULLATA se tutti i versamenti sono ANNULLATO, ATTIVATA se
-- almeno uno e' in uno stato diverso da NON_ESEGUITO e ANNULLATO, DISPONIBILE
-- altrimenti; per C dal suo versamento. Gli altri documenti restano senza opzione,
-- come quelli con debitori incoerenti: con un'opzione, una nuova esecuzione dopo
-- la correzione non creerebbe piu' il loro soggetto.
-- Le opzioni si preparano in gp_mig_opzioni con il loro UUID, che dopo
-- l'inserimento serve a ritrovarne l'id per collegarle ai versamenti.
CREATE TABLE gp_mig_versamenti ENGINE INNODB AS
SELECT v.id, v.id_documento, v.cod_rata, v.stato_versamento,
       CASE WHEN v.cod_rata REGEXP '^[1-9][0-9]*$' THEN 1 ELSE 0 END AS numerico,
       CASE WHEN v.cod_rata REGEXP '^(ENTRO[0-9]+|OLTRE[0-9]+|RIDOTTO|SCONTATO)$' THEN 1 ELSE 0 END AS soglia
  FROM versamenti v
 WHERE v.id_documento IS NOT NULL
   AND v.id_opzione_pagamento IS NULL
   AND NOT EXISTS (SELECT 1 FROM migrazione_v3_scarti s
                    WHERE s.id_documento = v.id_documento AND s.motivo = 'DEBITORI_INCOERENTI');

CREATE TABLE gp_mig_opzioni
(
	chiave BINARY(16) NOT NULL,
	id_documento BIGINT NOT NULL,
	id_versamento BIGINT,
	tipologia VARCHAR(35) NOT NULL,
	giorni INT,
	stato VARCHAR(35) NOT NULL
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;

INSERT INTO gp_mig_opzioni (chiave, id_documento, tipologia, stato)
SELECT UNHEX(REPLACE(UUID(), '-', '')), c.id_documento,
       CASE WHEN c.n_null = c.totale THEN 'SOLUZIONE_UNICA' ELSE 'PIANO_RATEALE' END,
       CASE WHEN c.n_annullato = c.totale THEN 'ANNULLATA'
            WHEN c.n_altro > 0 THEN 'ATTIVATA'
            ELSE 'DISPONIBILE'
       END
  FROM (SELECT id_documento,
               COUNT(*) AS totale,
               SUM(CASE WHEN cod_rata IS NULL THEN 1 ELSE 0 END) AS n_null,
               SUM(numerico) AS n_numerico,
               COUNT(DISTINCT CASE WHEN numerico = 1 THEN CAST(cod_rata AS UNSIGNED) END) AS n_numerico_distinti,
               SUM(CASE WHEN stato_versamento = 'ANNULLATO' THEN 1 ELSE 0 END) AS n_annullato,
               SUM(CASE WHEN stato_versamento NOT IN ('NON_ESEGUITO', 'ANNULLATO') THEN 1 ELSE 0 END) AS n_altro
          FROM gp_mig_versamenti
         GROUP BY id_documento) c
 WHERE c.n_null = c.totale
    OR (c.n_numerico = c.totale AND c.n_numerico_distinti = c.totale);

INSERT INTO gp_mig_opzioni (chiave, id_documento, id_versamento, tipologia, giorni, stato)
SELECT UNHEX(REPLACE(UUID(), '-', '')), m.id_documento, m.id,
       CASE WHEN m.cod_rata LIKE 'ENTRO%' THEN 'SOLUZIONE_UNICA_ENTRO'
            WHEN m.cod_rata LIKE 'OLTRE%' THEN 'SOLUZIONE_UNICA_OLTRE'
            WHEN m.cod_rata = 'SCONTATO' THEN 'SOLUZIONE_UNICA_SCONTATO'
            ELSE 'SOLUZIONE_UNICA_RIDOTTO'
       END,
       CASE WHEN m.cod_rata LIKE 'ENTRO%' OR m.cod_rata LIKE 'OLTRE%'
            THEN CAST(SUBSTRING(m.cod_rata, 6) AS UNSIGNED)
       END,
       CASE WHEN m.stato_versamento = 'NON_ESEGUITO' THEN 'DISPONIBILE'
            WHEN m.stato_versamento = 'ANNULLATO' THEN 'ANNULLATA'
            ELSE 'ATTIVATA'
       END
  FROM gp_mig_versamenti m
  JOIN (SELECT id_documento
          FROM gp_mig_versamenti
         GROUP BY id_documento
        HAVING SUM(soglia) = COUNT(*) AND COUNT(DISTINCT cod_rata) = COUNT(*)) ds
    ON ds.id_documento = m.id_documento;

INSERT INTO opzioni_pagamento (id_opzione_pagamento, id_documento, tipologia, giorni, stato, versione,
        data_creazione, data_ultimo_aggiornamento)
SELECT chiave, id_documento, tipologia, giorni, stato, 0, NOW(3), NOW(3)
  FROM gp_mig_opzioni;

UPDATE versamenti v
  JOIN gp_mig_opzioni m ON m.id_versamento = v.id
  JOIN opzioni_pagamento o ON o.id_opzione_pagamento = m.chiave
   SET v.id_opzione_pagamento = o.id,
       v.numero_rata = 1;

UPDATE versamenti v
  JOIN gp_mig_versamenti c ON c.id = v.id
  JOIN gp_mig_opzioni m ON m.id_documento = c.id_documento AND m.id_versamento IS NULL
  JOIN opzioni_pagamento o ON o.id_opzione_pagamento = m.chiave
   SET v.id_opzione_pagamento = o.id,
       v.numero_rata = CASE WHEN c.numerico = 1 THEN CAST(c.cod_rata AS UNSIGNED)
                            ELSE (SELECT COUNT(*) FROM gp_mig_versamenti p
                                   WHERE p.id_documento = c.id_documento AND p.id <= c.id)
                       END;

INSERT INTO migrazione_v3_scarti (id_documento, motivo, dettaglio)
SELECT v.id_documento, 'COD_RATA_ANOMALO',
       SUBSTRING(GROUP_CONCAT(COALESCE(v.cod_rata, 'NULL') ORDER BY v.id SEPARATOR ','), 1, 255)
  FROM versamenti v
 WHERE v.id_documento IS NOT NULL
   AND v.id_opzione_pagamento IS NULL
   AND NOT EXISTS (SELECT 1 FROM migrazione_v3_scarti s
                    WHERE s.id_documento = v.id_documento AND s.motivo = 'DEBITORI_INCOERENTI')
 GROUP BY v.id_documento;

-- 5. Mutua esclusione: le opzioni DISPONIBILI di un documento che ne ha una
-- ATTIVATA non sono piu' praticabili e diventano ANNULLATA, insieme ai loro
-- versamenti, come fa PosizioneDebitoriaService.attiva() della libreria. Vale su
-- tutta la tabella, non solo sulle righe appena migrate. Due opzioni ATTIVATA
-- sullo stesso documento si lasciano come sono. Prima i versamenti: dopo, le
-- opzioni da annullare non sarebbero piu' riconoscibili. La tabella derivata
-- serve perche' MySQL non aggiorna una tabella letta nella sottoquery.
UPDATE versamenti v
   SET v.stato_versamento = 'ANNULLATO',
       v.data_ora_ultimo_aggiornamento = NOW(3)
 WHERE v.stato_versamento = 'NON_ESEGUITO'
   AND v.id_opzione_pagamento IN (
       SELECT op.id FROM opzioni_pagamento op
        WHERE op.stato = 'DISPONIBILE'
          AND EXISTS (SELECT 1 FROM opzioni_pagamento altra
                       WHERE altra.id_documento = op.id_documento
                         AND altra.stato = 'ATTIVATA'));

UPDATE opzioni_pagamento
   SET stato = 'ANNULLATA',
       data_ultimo_aggiornamento = NOW(3)
 WHERE id IN (SELECT id FROM (
       SELECT op.id FROM opzioni_pagamento op
        WHERE op.stato = 'DISPONIBILE'
          AND EXISTS (SELECT 1 FROM opzioni_pagamento altra
                       WHERE altra.id_documento = op.id_documento
                         AND altra.stato = 'ATTIVATA')) da_annullare);

DROP TABLE gp_mig_versamenti;
DROP TABLE gp_mig_opzioni;
