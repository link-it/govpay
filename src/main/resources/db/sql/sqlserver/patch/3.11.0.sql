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
-- Le tabelle gp_mig_* sono di lavoro e vengono eliminate in coda; all'inizio si
-- eliminano quelle rimaste da un'esecuzione interrotta.
-- SQL Server non ha espressioni regolari: i formati si controllano con LIKE e la
-- collation binaria, che come le regex degli altri dialetti distingue maiuscole e
-- minuscole. Richiede SQL Server 2017 o successivo (STRING_AGG).
-- I metodi del tipo XML, usati per decodificare la causale, vogliono
-- QUOTED_IDENTIFIER ON, che sqlcmd non imposta da solo.
SET QUOTED_IDENTIFIER ON;
GO

-- Casi non migrati dell'ultima esecuzione. Non e' in gov_pay.sql: si elimina a
-- bonifica conclusa, con DROP TABLE migrazione_v3_scarti.
IF OBJECT_ID('migrazione_v3_scarti', 'U') IS NULL
CREATE TABLE migrazione_v3_scarti
(
	id_documento BIGINT,
	id_versamento BIGINT,
	motivo VARCHAR(35) NOT NULL,
	dettaglio VARCHAR(255),
	data DATETIME2 NOT NULL DEFAULT CURRENT_TIMESTAMP
);
GO

DELETE FROM migrazione_v3_scarti;

IF OBJECT_ID('gp_mig_causali', 'U') IS NOT NULL DROP TABLE gp_mig_causali;
IF OBJECT_ID('gp_mig_versamenti', 'U') IS NOT NULL DROP TABLE gp_mig_versamenti;
IF OBJECT_ID('gp_mig_opzioni', 'U') IS NOT NULL DROP TABLE gp_mig_opzioni;
GO

-- 1. Versamenti senza documento. In v3 ogni pendenza appartiene a una posizione
-- debitoria: per ciascuno si crea un documento 'MIGR-' || id del versamento, con
-- la causale come descrizione. La causale v2 e' '01 ' || base64(UTF-8), come la
-- scrive Versamento.CausaleSemplice. Il base64 si controlla prima, perche' la sua
-- decodifica di un testo non valido interromperebbe la patch. I byte decodificati
-- passano da un documento XML che dichiara la codifica UTF-8: convertirli
-- direttamente in VARCHAR li leggerebbe nella code page del database, e le
-- lettere accentate risulterebbero alterate. TRY_CONVERT restituisce NULL se i
-- byte non sono UTF-8 valido.
CREATE TABLE gp_mig_causali
(
	id BIGINT NOT NULL,
	descrizione VARCHAR(1024)
);
GO

INSERT INTO gp_mig_causali (id, descrizione)
SELECT v.id,
       CASE WHEN v.causale_versamento IS NULL THEN ''
            ELSE CAST(TRY_CONVERT(XML,
                     CONVERT(VARBINARY(MAX), '<?xml version="1.0" encoding="UTF-8"?><x><![CDATA[')
                     + CAST(N'' AS XML).value('xs:base64Binary(sql:column("b.b64"))', 'VARBINARY(MAX)')
                     + CONVERT(VARBINARY(MAX), ']]></x>')
                 ).value('(/x)[1]', 'NVARCHAR(1024)') AS VARCHAR(1024))
       END
  FROM versamenti v
 CROSS APPLY (SELECT SUBSTRING(v.causale_versamento, 4, 8000) AS b64) b
 WHERE v.id_documento IS NULL
   AND (v.causale_versamento IS NULL
        OR (SUBSTRING(v.causale_versamento, 1, 3) = '01 '
            AND LEN(b.b64) > 0
            AND LEN(b.b64) % 4 = 0
            AND b.b64 COLLATE Latin1_General_BIN NOT LIKE '%[^A-Za-z0-9+/=]%'
            AND SUBSTRING(b.b64, 1, LEN(b.b64) - 2) NOT LIKE '%=%'));

-- Si scartano le causali che non si decodificano (i formati '02' e '03' non sono
-- piu' prodotti da nessun percorso di scrittura) e i versamenti il cui codice e'
-- gia' usato da un documento della stessa applicazione e dello stesso dominio.
INSERT INTO migrazione_v3_scarti (id_versamento, motivo, dettaglio)
SELECT v.id, 'CAUSALE_NON_DECODIFICABILE', SUBSTRING(v.causale_versamento, 1, 255)
  FROM versamenti v
 WHERE v.id_documento IS NULL
   AND NOT EXISTS (SELECT 1 FROM gp_mig_causali c WHERE c.id = v.id AND c.descrizione IS NOT NULL);

INSERT INTO migrazione_v3_scarti (id_versamento, motivo, dettaglio)
SELECT v.id, 'COLLISIONE_COD_DOCUMENTO', 'MIGR-' + CAST(v.id AS VARCHAR(20))
  FROM versamenti v
 WHERE v.id_documento IS NULL
   AND EXISTS (SELECT 1 FROM documenti d
                WHERE d.cod_documento = 'MIGR-' + CAST(v.id AS VARCHAR(20))
                  AND d.id_applicazione = v.id_applicazione
                  AND d.id_dominio = v.id_dominio);

INSERT INTO documenti (cod_documento, descrizione, id_dominio, id_applicazione)
SELECT 'MIGR-' + CAST(v.id AS VARCHAR(20)), c.descrizione, v.id_dominio, v.id_applicazione
  FROM versamenti v
  JOIN gp_mig_causali c ON c.id = v.id
 WHERE v.id_documento IS NULL
   AND NOT EXISTS (SELECT 1 FROM migrazione_v3_scarti s WHERE s.id_versamento = v.id);

UPDATE v
   SET v.id_documento = d.id
  FROM versamenti v
  JOIN documenti d
    ON d.cod_documento = 'MIGR-' + CAST(v.id AS VARCHAR(20))
   AND d.id_applicazione = v.id_applicazione
   AND d.id_dominio = v.id_dominio
 WHERE v.id_documento IS NULL
   AND NOT EXISTS (SELECT 1 FROM migrazione_v3_scarti s WHERE s.id_versamento = v.id);
GO

-- 2. Colonne nuove di documenti, dai versamenti: data_creazione la minima,
-- data_ultimo_aggiornamento e le date ACA le massime, id_unita_operativa solo se
-- e' la stessa per tutti i versamenti. Al posto dei DEFAULT impostati
-- dall'aggiunta delle colonne.
UPDATE d
   SET d.data_creazione = x.data_creazione,
       d.data_ultimo_aggiornamento = x.data_ultimo_aggiornamento,
       d.data_ultima_modifica_aca = x.data_ultima_modifica_aca,
       d.data_ultima_comunicazione_aca = x.data_ultima_comunicazione_aca,
       d.id_unita_operativa = x.id_unita_operativa
  FROM documenti d
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
    ON x.id_documento = d.id;
GO

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
   AND COUNT(DISTINCT COALESCE(v.debitore_tipo,
           CASE WHEN LEN(v.debitore_identificativo) = 11
                 AND v.debitore_identificativo NOT LIKE '%[^0-9]%' THEN 'G' ELSE 'F' END)
           + '|' + v.debitore_identificativo) > 1;

INSERT INTO soggetti_debitori (ordine, tipo, identificativo, anagrafica, indirizzo, civico, cap,
        localita, provincia, nazione, email, cellulare, id_documento)
SELECT 0,
       COALESCE(v.debitore_tipo,
           CASE WHEN LEN(v.debitore_identificativo) = 11
                 AND v.debitore_identificativo NOT LIKE '%[^0-9]%' THEN 'G' ELSE 'F' END),
       v.debitore_identificativo, v.debitore_anagrafica, v.debitore_indirizzo,
       v.debitore_civico, v.debitore_cap, v.debitore_localita, v.debitore_provincia,
       v.debitore_nazione, v.debitore_email, v.debitore_cellulare, v.id_documento
  FROM versamenti v
 WHERE v.id IN (SELECT MIN(p.id) FROM versamenti p
                 WHERE p.id_documento IS NOT NULL
                 GROUP BY p.id_documento
                HAVING COUNT(p.id_opzione_pagamento) = 0)
   AND NOT EXISTS (SELECT 1 FROM soggetti_debitori s WHERE s.id_documento = v.id_documento)
   AND NOT EXISTS (SELECT 1 FROM migrazione_v3_scarti s
                    WHERE s.id_documento = v.id_documento AND s.motivo = 'DEBITORI_INCOERENTI');
GO

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
SELECT v.id, v.id_documento, v.cod_rata, v.stato_versamento,
       CASE WHEN v.cod_rata COLLATE Latin1_General_BIN NOT LIKE '%[^0-9]%'
                 AND v.cod_rata COLLATE Latin1_General_BIN LIKE '[1-9]%' THEN 1 ELSE 0 END AS numerico,
       CASE WHEN v.cod_rata COLLATE Latin1_General_BIN IN ('RIDOTTO', 'SCONTATO')
                 OR ((v.cod_rata COLLATE Latin1_General_BIN LIKE 'ENTRO[0-9]%'
                      OR v.cod_rata COLLATE Latin1_General_BIN LIKE 'OLTRE[0-9]%')
                     AND SUBSTRING(v.cod_rata, 6, 30) NOT LIKE '%[^0-9]%') THEN 1 ELSE 0 END AS soglia
  INTO gp_mig_versamenti
  FROM versamenti v
 WHERE v.id_documento IS NOT NULL
   AND v.id_opzione_pagamento IS NULL
   AND NOT EXISTS (SELECT 1 FROM migrazione_v3_scarti s
                    WHERE s.id_documento = v.id_documento AND s.motivo = 'DEBITORI_INCOERENTI');

CREATE TABLE gp_mig_opzioni
(
	chiave UNIQUEIDENTIFIER NOT NULL,
	id_documento BIGINT NOT NULL,
	id_versamento BIGINT,
	tipologia VARCHAR(35) NOT NULL,
	giorni INT,
	stato VARCHAR(35) NOT NULL
);
GO

INSERT INTO gp_mig_opzioni (chiave, id_documento, tipologia, stato)
SELECT NEWID(), c.id_documento,
       CASE WHEN c.n_null = c.totale THEN 'SOLUZIONE_UNICA' ELSE 'PIANO_RATEALE' END,
       CASE WHEN c.n_annullato = c.totale THEN 'ANNULLATA'
            WHEN c.n_altro > 0 THEN 'ATTIVATA'
            ELSE 'DISPONIBILE'
       END
  FROM (SELECT id_documento,
               COUNT(*) AS totale,
               SUM(CASE WHEN cod_rata IS NULL THEN 1 ELSE 0 END) AS n_null,
               SUM(numerico) AS n_numerico,
               COUNT(DISTINCT CASE WHEN numerico = 1 THEN CAST(cod_rata AS BIGINT) END) AS n_numerico_distinti,
               SUM(CASE WHEN stato_versamento = 'ANNULLATO' THEN 1 ELSE 0 END) AS n_annullato,
               SUM(CASE WHEN stato_versamento NOT IN ('NON_ESEGUITO', 'ANNULLATO') THEN 1 ELSE 0 END) AS n_altro
          FROM gp_mig_versamenti
         GROUP BY id_documento) c
 WHERE c.n_null = c.totale
    OR (c.n_numerico = c.totale AND c.n_numerico_distinti = c.totale);

INSERT INTO gp_mig_opzioni (chiave, id_documento, id_versamento, tipologia, giorni, stato)
SELECT NEWID(), m.id_documento, m.id,
       CASE WHEN m.cod_rata LIKE 'ENTRO%' THEN 'SOLUZIONE_UNICA_ENTRO'
            WHEN m.cod_rata LIKE 'OLTRE%' THEN 'SOLUZIONE_UNICA_OLTRE'
            WHEN m.cod_rata = 'SCONTATO' THEN 'SOLUZIONE_UNICA_SCONTATO'
            ELSE 'SOLUZIONE_UNICA_RIDOTTO'
       END,
       CASE WHEN m.cod_rata LIKE 'ENTRO%' OR m.cod_rata LIKE 'OLTRE%'
            THEN CAST(SUBSTRING(m.cod_rata, 6, 30) AS INT)
       END,
       CASE WHEN m.stato_versamento = 'NON_ESEGUITO' THEN 'DISPONIBILE'
            WHEN m.stato_versamento = 'ANNULLATO' THEN 'ANNULLATA'
            ELSE 'ATTIVATA'
       END
  FROM gp_mig_versamenti m
 WHERE m.id_documento IN (SELECT id_documento
                            FROM gp_mig_versamenti
                           GROUP BY id_documento
                          HAVING SUM(soglia) = COUNT(*) AND COUNT(DISTINCT cod_rata) = COUNT(*));

INSERT INTO opzioni_pagamento (id_opzione_pagamento, id_documento, tipologia, giorni, stato, versione,
        data_creazione, data_ultimo_aggiornamento)
SELECT chiave, id_documento, tipologia, giorni, stato, 0, SYSDATETIME(), SYSDATETIME()
  FROM gp_mig_opzioni;

UPDATE v
   SET v.id_opzione_pagamento = o.id,
       v.numero_rata = 1
  FROM versamenti v
  JOIN gp_mig_opzioni m ON m.id_versamento = v.id
  JOIN opzioni_pagamento o ON o.id_opzione_pagamento = m.chiave;

UPDATE v
   SET v.id_opzione_pagamento = o.id,
       v.numero_rata = CASE WHEN c.numerico = 1 THEN CAST(c.cod_rata AS INT)
                            ELSE (SELECT COUNT(*) FROM gp_mig_versamenti p
                                   WHERE p.id_documento = c.id_documento AND p.id <= c.id)
                       END
  FROM versamenti v
  JOIN gp_mig_versamenti c ON c.id = v.id
  JOIN gp_mig_opzioni m ON m.id_documento = c.id_documento AND m.id_versamento IS NULL
  JOIN opzioni_pagamento o ON o.id_opzione_pagamento = m.chiave;

INSERT INTO migrazione_v3_scarti (id_documento, motivo, dettaglio)
SELECT v.id_documento, 'COD_RATA_ANOMALO',
       SUBSTRING(STRING_AGG(CAST(COALESCE(v.cod_rata, 'NULL') AS VARCHAR(MAX)), ',') WITHIN GROUP (ORDER BY v.id), 1, 255)
  FROM versamenti v
 WHERE v.id_documento IS NOT NULL
   AND v.id_opzione_pagamento IS NULL
   AND NOT EXISTS (SELECT 1 FROM migrazione_v3_scarti s
                    WHERE s.id_documento = v.id_documento AND s.motivo = 'DEBITORI_INCOERENTI')
 GROUP BY v.id_documento;
GO

-- 5. Mutua esclusione: le opzioni DISPONIBILI di un documento che ne ha una
-- ATTIVATA non sono piu' praticabili e diventano ANNULLATA, insieme ai loro
-- versamenti, come fa PosizioneDebitoriaService.attiva() della libreria. Vale su
-- tutta la tabella, non solo sulle righe appena migrate. Due opzioni ATTIVATA
-- sullo stesso documento si lasciano come sono. Prima i versamenti: dopo, le
-- opzioni da annullare non sarebbero piu' riconoscibili.
UPDATE versamenti
   SET stato_versamento = 'ANNULLATO',
       data_ora_ultimo_aggiornamento = SYSDATETIME()
 WHERE stato_versamento = 'NON_ESEGUITO'
   AND id_opzione_pagamento IN (
       SELECT op.id FROM opzioni_pagamento op
        WHERE op.stato = 'DISPONIBILE'
          AND EXISTS (SELECT 1 FROM opzioni_pagamento altra
                       WHERE altra.id_documento = op.id_documento
                         AND altra.stato = 'ATTIVATA'));

UPDATE op
   SET op.stato = 'ANNULLATA',
       op.data_ultimo_aggiornamento = SYSDATETIME()
  FROM opzioni_pagamento op
 WHERE op.stato = 'DISPONIBILE'
   AND EXISTS (SELECT 1 FROM opzioni_pagamento altra
                WHERE altra.id_documento = op.id_documento
                  AND altra.stato = 'ATTIVATA');
GO

DROP TABLE gp_mig_causali;
DROP TABLE gp_mig_versamenti;
DROP TABLE gp_mig_opzioni;
GO
