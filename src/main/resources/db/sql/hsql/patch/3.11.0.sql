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

-- Casi non migrati dell'ultima esecuzione. Non e' in gov_pay.sql: si elimina a
-- bonifica conclusa, con DROP TABLE migrazione_v3_scarti.
CREATE TABLE IF NOT EXISTS migrazione_v3_scarti
(
	id_documento BIGINT,
	id_versamento BIGINT,
	motivo VARCHAR(35) NOT NULL,
	dettaglio VARCHAR(255),
	data TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

DELETE FROM migrazione_v3_scarti;

DROP TABLE IF EXISTS gp_mig_causali;
DROP TABLE IF EXISTS gp_mig_versamenti;
DROP TABLE IF EXISTS gp_mig_opzioni;

-- 1. Versamenti senza documento. In v3 ogni pendenza appartiene a una posizione
-- debitoria: per ciascuno si crea un documento 'MIGR-' || id del versamento, con
-- la causale come descrizione. La causale v2 e' '01 ' || base64(UTF-8), come la
-- scrive Versamento.CausaleSemplice: la validita' del base64 si controlla prima,
-- perche' la decodifica di un testo non valido interromperebbe la patch.
-- HSQLDB decodifica il base64 ma non converte byte UTF-8 in testo: lo fa la
-- query ricorsiva qui sotto, un carattere per passo, sulla rappresentazione
-- esadecimale dei byte. Un byte che non puo' aprire un carattere diventa U+FFFD.
-- E' una query e non una funzione perche' il corpo BEGIN ... END di una routine
-- non sopravvive a un esecutore che divide lo script a ogni ';'.
CREATE TABLE gp_mig_causali
(
	id BIGINT NOT NULL,
	descrizione VARCHAR(1024)
);

INSERT INTO gp_mig_causali (id, descrizione)
WITH RECURSIVE decodifica (id, h, pos, testo) AS (
    SELECT v.id, LOWER(RAWTOHEX(FROM_BASE64(SUBSTRING(v.causale_versamento, 4)))), 1, CAST('' AS VARCHAR(1024))
      FROM versamenti v
     WHERE v.id_documento IS NULL
       AND SUBSTRING(v.causale_versamento, 1, 3) = '01 '
       AND REGEXP_MATCHES(SUBSTRING(v.causale_versamento, 4), '[A-Za-z0-9+/]+={0,2}')
       AND MOD(CHAR_LENGTH(SUBSTRING(v.causale_versamento, 4)), 4) = 0
    UNION ALL
    SELECT id, h,
           pos + 2 * CASE WHEN ((POSITION(SUBSTRING(h, pos, 1) IN '0123456789abcdef') - 1) * 16 + POSITION(SUBSTRING(h, pos + 1, 1) IN '0123456789abcdef') - 1) < 192 THEN 1
                          WHEN ((POSITION(SUBSTRING(h, pos, 1) IN '0123456789abcdef') - 1) * 16 + POSITION(SUBSTRING(h, pos + 1, 1) IN '0123456789abcdef') - 1) >= 240 THEN 4
                          WHEN ((POSITION(SUBSTRING(h, pos, 1) IN '0123456789abcdef') - 1) * 16 + POSITION(SUBSTRING(h, pos + 1, 1) IN '0123456789abcdef') - 1) >= 224 THEN 3
                          ELSE 2 END,
           testo || CHAR(CASE WHEN ((POSITION(SUBSTRING(h, pos, 1) IN '0123456789abcdef') - 1) * 16 + POSITION(SUBSTRING(h, pos + 1, 1) IN '0123456789abcdef') - 1) < 128 THEN ((POSITION(SUBSTRING(h, pos, 1) IN '0123456789abcdef') - 1) * 16 + POSITION(SUBSTRING(h, pos + 1, 1) IN '0123456789abcdef') - 1)
                              WHEN ((POSITION(SUBSTRING(h, pos, 1) IN '0123456789abcdef') - 1) * 16 + POSITION(SUBSTRING(h, pos + 1, 1) IN '0123456789abcdef') - 1) < 192 THEN 65533
                              WHEN ((POSITION(SUBSTRING(h, pos, 1) IN '0123456789abcdef') - 1) * 16 + POSITION(SUBSTRING(h, pos + 1, 1) IN '0123456789abcdef') - 1) >= 240 THEN (((POSITION(SUBSTRING(h, pos, 1) IN '0123456789abcdef') - 1) * 16 + POSITION(SUBSTRING(h, pos + 1, 1) IN '0123456789abcdef') - 1) - 240) * 262144 + (((POSITION(SUBSTRING(h, pos + 2, 1) IN '0123456789abcdef') - 1) * 16 + POSITION(SUBSTRING(h, pos + 2 + 1, 1) IN '0123456789abcdef') - 1) - 128) * 4096
                                                    + (((POSITION(SUBSTRING(h, pos + 4, 1) IN '0123456789abcdef') - 1) * 16 + POSITION(SUBSTRING(h, pos + 4 + 1, 1) IN '0123456789abcdef') - 1) - 128) * 64 + (((POSITION(SUBSTRING(h, pos + 6, 1) IN '0123456789abcdef') - 1) * 16 + POSITION(SUBSTRING(h, pos + 6 + 1, 1) IN '0123456789abcdef') - 1) - 128)
                              WHEN ((POSITION(SUBSTRING(h, pos, 1) IN '0123456789abcdef') - 1) * 16 + POSITION(SUBSTRING(h, pos + 1, 1) IN '0123456789abcdef') - 1) >= 224 THEN (((POSITION(SUBSTRING(h, pos, 1) IN '0123456789abcdef') - 1) * 16 + POSITION(SUBSTRING(h, pos + 1, 1) IN '0123456789abcdef') - 1) - 224) * 4096 + (((POSITION(SUBSTRING(h, pos + 2, 1) IN '0123456789abcdef') - 1) * 16 + POSITION(SUBSTRING(h, pos + 2 + 1, 1) IN '0123456789abcdef') - 1) - 128) * 64
                                                    + (((POSITION(SUBSTRING(h, pos + 4, 1) IN '0123456789abcdef') - 1) * 16 + POSITION(SUBSTRING(h, pos + 4 + 1, 1) IN '0123456789abcdef') - 1) - 128)
                              ELSE (((POSITION(SUBSTRING(h, pos, 1) IN '0123456789abcdef') - 1) * 16 + POSITION(SUBSTRING(h, pos + 1, 1) IN '0123456789abcdef') - 1) - 192) * 64 + (((POSITION(SUBSTRING(h, pos + 2, 1) IN '0123456789abcdef') - 1) * 16 + POSITION(SUBSTRING(h, pos + 2 + 1, 1) IN '0123456789abcdef') - 1) - 128) END)
      FROM decodifica
     WHERE pos < CHAR_LENGTH(h)
)
SELECT id, testo FROM decodifica WHERE pos >= CHAR_LENGTH(h);

INSERT INTO gp_mig_causali (id, descrizione)
SELECT v.id, ''
  FROM versamenti v
 WHERE v.id_documento IS NULL
   AND v.causale_versamento IS NULL;

-- Si scartano le causali che non si decodificano (i formati '02' e '03' non sono
-- piu' prodotti da nessun percorso di scrittura) e i versamenti il cui codice e'
-- gia' usato da un documento della stessa applicazione e dello stesso dominio.
INSERT INTO migrazione_v3_scarti (id_versamento, motivo, dettaglio)
SELECT v.id, 'CAUSALE_NON_DECODIFICABILE', SUBSTRING(v.causale_versamento, 1, 255)
  FROM versamenti v
 WHERE v.id_documento IS NULL
   AND NOT EXISTS (SELECT 1 FROM gp_mig_causali c WHERE c.id = v.id);

INSERT INTO migrazione_v3_scarti (id_versamento, motivo, dettaglio)
SELECT v.id, 'COLLISIONE_COD_DOCUMENTO', 'MIGR-' || CAST(v.id AS VARCHAR(20))
  FROM versamenti v
 WHERE v.id_documento IS NULL
   AND EXISTS (SELECT 1 FROM documenti d
                WHERE d.cod_documento = 'MIGR-' || CAST(v.id AS VARCHAR(20))
                  AND d.id_applicazione = v.id_applicazione
                  AND d.id_dominio = v.id_dominio);

INSERT INTO documenti (cod_documento, descrizione, id_dominio, id_applicazione)
SELECT 'MIGR-' || CAST(v.id AS VARCHAR(20)), c.descrizione, v.id_dominio, v.id_applicazione
  FROM versamenti v
  JOIN gp_mig_causali c ON c.id = v.id
 WHERE v.id_documento IS NULL
   AND NOT EXISTS (SELECT 1 FROM migrazione_v3_scarti s WHERE s.id_versamento = v.id);

UPDATE versamenti v
   SET v.id_documento = (SELECT d.id FROM documenti d
                          WHERE d.cod_documento = 'MIGR-' || CAST(v.id AS VARCHAR(20))
                            AND d.id_applicazione = v.id_applicazione
                            AND d.id_dominio = v.id_dominio)
 WHERE v.id_documento IS NULL
   AND NOT EXISTS (SELECT 1 FROM migrazione_v3_scarti s WHERE s.id_versamento = v.id);

-- 2. Colonne nuove di documenti, dai versamenti: data_creazione la minima,
-- data_ultimo_aggiornamento e le date ACA le massime, id_unita_operativa solo se
-- e' la stessa per tutti i versamenti. Al posto dei DEFAULT impostati
-- dall'aggiunta delle colonne.
MERGE INTO documenti d
USING (SELECT v.id_documento,
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
   ON (d.id = x.id_documento)
 WHEN MATCHED THEN UPDATE
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
   AND COUNT(DISTINCT COALESCE(v.debitore_tipo,
           CASE WHEN REGEXP_MATCHES(v.debitore_identificativo, '[0-9]{11}') THEN 'G' ELSE 'F' END)
           || '|' || v.debitore_identificativo) > 1;

INSERT INTO soggetti_debitori (ordine, tipo, identificativo, anagrafica, indirizzo, civico, cap,
        localita, provincia, nazione, email, cellulare, id_documento)
SELECT 0,
       COALESCE(v.debitore_tipo,
           CASE WHEN REGEXP_MATCHES(v.debitore_identificativo, '[0-9]{11}') THEN 'G' ELSE 'F' END),
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
-- Per HSQLDB un CASE tra letterali di lunghezza diversa e' un CHAR della
-- lunghezza massima, riempito di spazi, che neanche un CAST a VARCHAR toglie:
-- senza TRIM tipologia e stato verrebbero salvati con spazi in coda, e la
-- libreria non li riconoscerebbe.
CREATE TABLE gp_mig_versamenti AS (
SELECT v.id, v.id_documento, v.cod_rata, v.stato_versamento,
       CASE WHEN REGEXP_MATCHES(v.cod_rata, '[1-9][0-9]*') THEN 1 ELSE 0 END AS numerico,
       CASE WHEN REGEXP_MATCHES(v.cod_rata, 'ENTRO[0-9]+|OLTRE[0-9]+|RIDOTTO|SCONTATO') THEN 1 ELSE 0 END AS soglia
  FROM versamenti v
 WHERE v.id_documento IS NOT NULL
   AND v.id_opzione_pagamento IS NULL
   AND NOT EXISTS (SELECT 1 FROM migrazione_v3_scarti s
                    WHERE s.id_documento = v.id_documento AND s.motivo = 'DEBITORI_INCOERENTI')
) WITH DATA;

CREATE TABLE gp_mig_opzioni
(
	chiave BINARY(16) NOT NULL,
	id_documento BIGINT NOT NULL,
	id_versamento BIGINT,
	tipologia VARCHAR(35) NOT NULL,
	giorni INT,
	stato VARCHAR(35) NOT NULL
);

INSERT INTO gp_mig_opzioni (chiave, id_documento, tipologia, stato)
SELECT UUID(), c.id_documento,
       TRIM(CASE WHEN c.n_null = c.totale THEN 'SOLUZIONE_UNICA' ELSE 'PIANO_RATEALE' END),
       TRIM(CASE WHEN c.n_annullato = c.totale THEN 'ANNULLATA'
                 WHEN c.n_altro > 0 THEN 'ATTIVATA'
                 ELSE 'DISPONIBILE'
            END)
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
SELECT UUID(), m.id_documento, m.id,
       TRIM(CASE WHEN m.cod_rata LIKE 'ENTRO%' THEN 'SOLUZIONE_UNICA_ENTRO'
                 WHEN m.cod_rata LIKE 'OLTRE%' THEN 'SOLUZIONE_UNICA_OLTRE'
                 WHEN m.cod_rata = 'SCONTATO' THEN 'SOLUZIONE_UNICA_SCONTATO'
                 ELSE 'SOLUZIONE_UNICA_RIDOTTO'
            END),
       CASE WHEN m.cod_rata LIKE 'ENTRO%' OR m.cod_rata LIKE 'OLTRE%'
            THEN CAST(SUBSTRING(m.cod_rata, 6) AS INT)
       END,
       TRIM(CASE WHEN m.stato_versamento = 'NON_ESEGUITO' THEN 'DISPONIBILE'
                 WHEN m.stato_versamento = 'ANNULLATO' THEN 'ANNULLATA'
                 ELSE 'ATTIVATA'
            END)
  FROM gp_mig_versamenti m
 WHERE m.id_documento IN (SELECT id_documento
                            FROM gp_mig_versamenti
                           GROUP BY id_documento
                          HAVING SUM(soglia) = COUNT(*) AND COUNT(DISTINCT cod_rata) = COUNT(*));

INSERT INTO opzioni_pagamento (id_opzione_pagamento, id_documento, tipologia, giorni, stato, versione,
        data_creazione, data_ultimo_aggiornamento)
SELECT chiave, id_documento, tipologia, giorni, stato, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
  FROM gp_mig_opzioni;

MERGE INTO versamenti v
USING (SELECT m.id_versamento AS id, o.id AS id_opzione
         FROM gp_mig_opzioni m
         JOIN opzioni_pagamento o ON o.id_opzione_pagamento = m.chiave
        WHERE m.id_versamento IS NOT NULL) x
   ON (v.id = x.id)
 WHEN MATCHED THEN UPDATE
  SET v.id_opzione_pagamento = x.id_opzione,
      v.numero_rata = 1;

MERGE INTO versamenti v
USING (SELECT c.id, o.id AS id_opzione,
              CASE WHEN c.numerico = 1 THEN CAST(c.cod_rata AS INT)
                   ELSE (SELECT COUNT(*) FROM gp_mig_versamenti p
                          WHERE p.id_documento = c.id_documento AND p.id <= c.id)
              END AS numero_rata
         FROM gp_mig_versamenti c
         JOIN gp_mig_opzioni m ON m.id_documento = c.id_documento AND m.id_versamento IS NULL
         JOIN opzioni_pagamento o ON o.id_opzione_pagamento = m.chiave) x
   ON (v.id = x.id)
 WHEN MATCHED THEN UPDATE
  SET v.id_opzione_pagamento = x.id_opzione,
      v.numero_rata = x.numero_rata;

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
-- opzioni da annullare non sarebbero piu' riconoscibili.
UPDATE versamenti v
   SET v.stato_versamento = 'ANNULLATO',
       v.data_ora_ultimo_aggiornamento = CURRENT_TIMESTAMP
 WHERE v.stato_versamento = 'NON_ESEGUITO'
   AND v.id_opzione_pagamento IN (
       SELECT op.id FROM opzioni_pagamento op
        WHERE op.stato = 'DISPONIBILE'
          AND EXISTS (SELECT 1 FROM opzioni_pagamento altra
                       WHERE altra.id_documento = op.id_documento
                         AND altra.stato = 'ATTIVATA'));

UPDATE opzioni_pagamento op
   SET op.stato = 'ANNULLATA',
       op.data_ultimo_aggiornamento = CURRENT_TIMESTAMP
 WHERE op.stato = 'DISPONIBILE'
   AND EXISTS (SELECT 1 FROM opzioni_pagamento altra
                WHERE altra.id_documento = op.id_documento
                  AND altra.stato = 'ATTIVATA');

DROP TABLE gp_mig_causali;
DROP TABLE gp_mig_versamenti;
DROP TABLE gp_mig_opzioni;

-- SqlTool lavora senza autocommit: senza questo COMMIT le modifiche ai dati
-- andrebbero perse alla chiusura.
COMMIT;
