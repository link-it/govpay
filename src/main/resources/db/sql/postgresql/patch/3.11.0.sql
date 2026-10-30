-- 3.11.0

-- Issue #901: modello v3 delle posizioni debitorie di govpay-common-pendenze.
-- Nuove colonne su documenti e versamenti, nuove tabelle opzioni_pagamento e
-- soggetti_debitori. Le colonne NOT NULL mantengono il DEFAULT anche dopo la
-- patch, come in gov_pay.sql: il core v2 continua a inserire righe in documenti
-- e versamenti senza conoscerle.
-- Va eseguita con psql -v ON_ERROR_STOP=1, per fermarsi al primo errore.

-- documenti.descrizione da 255 a 1024 caratteri, come causale_versamento:
-- govpay-common-pendenze vi riporta la causale intera dei versamenti migrati
-- senza documento.
-- PostgreSQL non cambia il tipo di una colonna letta da una vista: v_versamenti
-- va eliminata e ricreata, con la stessa definizione di gov_pay.sql. Ripetere
-- l'ALTER su una colonna gia' a 1024 non ha effetto.
DROP VIEW IF EXISTS v_versamenti;
ALTER TABLE documenti ALTER COLUMN descrizione TYPE VARCHAR(1024);
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
    versamenti.proprieta,
    versamenti.data_ultima_modifica_aca,
    versamenti.data_ultima_comunicazione_aca,
    versamenti.id_documento,
    documenti.cod_documento,
    documenti.descrizione AS doc_descrizione
    FROM versamenti LEFT JOIN documenti ON versamenti.id_documento = documenti.id;

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
	cellulare VARCHAR(35),
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


-- ===========================================================================
-- Migrazione dei dati al modello v3
-- ===========================================================================
-- Porta i dati v2 nel modello delle posizioni debitorie di
-- govpay-common-pendenze, con le stesse regole dei suoi script
-- migrazione/05_documenti_orfani.sql e 06_opzioni_pagamento_documenti_esistenti.sql,
-- piu' i soggetti debitori e le colonne nuove di documenti, che la libreria non
-- valorizza.
-- Gira a ogni aggiornamento ed e' rieseguibile: ogni passo considera solo i
-- documenti i cui versamenti non hanno ancora un'opzione di pagamento. I casi che
-- non si possono migrare restano come sono e finiscono in migrazione_v3_scarti,
-- svuotata a ogni esecuzione: dopo averli corretti si riesegue la patch.

-- Casi non migrati dell'ultima esecuzione. Non e' in gov_pay.sql: si elimina a
-- bonifica conclusa, con DROP TABLE migrazione_v3_scarti.
CREATE TABLE IF NOT EXISTS migrazione_v3_scarti
(
	id_documento BIGINT,
	id_versamento BIGINT,
	motivo VARCHAR(35) NOT NULL,
	dettaglio VARCHAR(255),
	data TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

DELETE FROM migrazione_v3_scarti;

-- Decodifica della causale v2 nel formato '01 ' || base64(UTF-8), che
-- Versamento.CausaleSemplice scrive. Restituisce NULL se il testo non e' un
-- base64 valido o non e' UTF-8: la conversione altrimenti interromperebbe la
-- patch. La funzione e' in pg_temp e sparisce a fine sessione.
CREATE OR REPLACE FUNCTION pg_temp.gp_decodifica_causale(causale TEXT) RETURNS TEXT AS $$
BEGIN
    RETURN convert_from(decode(substring(causale FROM 4), 'base64'), 'UTF8');
EXCEPTION
    WHEN OTHERS THEN
        RETURN NULL;
END;
$$ LANGUAGE plpgsql IMMUTABLE;

-- 1. Versamenti senza documento. In v3 ogni pendenza appartiene a una posizione
-- debitoria: per ciascuno si crea un documento 'MIGR-' || id del versamento, con
-- la causale come descrizione. Si scartano le causali che non si decodificano
-- (i formati '02' e '03' non sono piu' prodotti da nessun percorso di scrittura)
-- e i versamenti il cui codice e' gia' usato da un documento della stessa
-- applicazione e dello stesso dominio.
INSERT INTO migrazione_v3_scarti (id_versamento, motivo, dettaglio)
SELECT v.id, 'CAUSALE_NON_DECODIFICABILE', substring(v.causale_versamento FROM 1 FOR 255)
  FROM versamenti v
 WHERE v.id_documento IS NULL
   AND v.causale_versamento IS NOT NULL
   AND (substring(v.causale_versamento FROM 1 FOR 3) <> '01 '
        OR pg_temp.gp_decodifica_causale(v.causale_versamento) IS NULL);

INSERT INTO migrazione_v3_scarti (id_versamento, motivo, dettaglio)
SELECT v.id, 'COLLISIONE_COD_DOCUMENTO', 'MIGR-' || v.id
  FROM versamenti v
 WHERE v.id_documento IS NULL
   AND EXISTS (SELECT 1 FROM documenti d
                WHERE d.cod_documento = 'MIGR-' || v.id
                  AND d.id_applicazione = v.id_applicazione
                  AND d.id_dominio = v.id_dominio);

INSERT INTO documenti (cod_documento, descrizione, id_dominio, id_applicazione)
SELECT 'MIGR-' || v.id,
       COALESCE(pg_temp.gp_decodifica_causale(v.causale_versamento), ''),
       v.id_dominio,
       v.id_applicazione
  FROM versamenti v
 WHERE v.id_documento IS NULL
   AND NOT EXISTS (SELECT 1 FROM migrazione_v3_scarti s WHERE s.id_versamento = v.id);

UPDATE versamenti v
   SET id_documento = d.id
  FROM documenti d
 WHERE v.id_documento IS NULL
   AND d.cod_documento = 'MIGR-' || v.id
   AND d.id_applicazione = v.id_applicazione
   AND d.id_dominio = v.id_dominio
   AND NOT EXISTS (SELECT 1 FROM migrazione_v3_scarti s WHERE s.id_versamento = v.id);

-- 2. Colonne nuove di documenti, dai versamenti: data_creazione la minima,
-- data_ultimo_aggiornamento e le date ACA le massime, id_unita_operativa solo se
-- e' la stessa per tutti i versamenti. Al posto dei DEFAULT impostati
-- dall'aggiunta delle colonne.
UPDATE documenti d
   SET data_creazione = x.data_creazione,
       data_ultimo_aggiornamento = x.data_ultimo_aggiornamento,
       data_ultima_modifica_aca = x.data_ultima_modifica_aca,
       data_ultima_comunicazione_aca = x.data_ultima_comunicazione_aca,
       id_unita_operativa = x.id_unita_operativa
  FROM (SELECT v.id_documento,
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
 WHERE d.id = x.id_documento;

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
           CASE WHEN v.debitore_identificativo ~ '^[0-9]{11}$' THEN 'G' ELSE 'F' END)
           || '|' || v.debitore_identificativo) > 1;

INSERT INTO soggetti_debitori (ordine, tipo, identificativo, anagrafica, indirizzo, civico, cap,
        localita, provincia, nazione, email, cellulare, id_documento)
SELECT 0,
       COALESCE(v.debitore_tipo,
           CASE WHEN v.debitore_identificativo ~ '^[0-9]{11}$' THEN 'G' ELSE 'F' END),
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
-- la correzione non creerebbe piu' il loro soggetto. Non si toccano i documenti
-- che hanno gia' un'opzione: i versamenti che v2 vi aggiunge dopo l'aggiornamento
-- li collega migrazione/riallineamento-v3.sql, oppure restano negli scarti.
-- gen_random_uuid() richiede PostgreSQL 13 o successivo.
WITH versamenti_candidati AS (
    SELECT v.id, v.id_documento, v.cod_rata, v.stato_versamento
      FROM versamenti v
     WHERE v.id_documento IS NOT NULL
       AND v.id_opzione_pagamento IS NULL
       AND NOT EXISTS (SELECT 1 FROM migrazione_v3_scarti s
                        WHERE s.id_documento = v.id_documento AND s.motivo = 'DEBITORI_INCOERENTI')
       AND NOT EXISTS (SELECT 1 FROM opzioni_pagamento o WHERE o.id_documento = v.id_documento)
),
classificazione AS (
    SELECT id_documento,
           COUNT(*) AS totale,
           COUNT(*) FILTER (WHERE cod_rata IS NULL) AS n_null,
           COUNT(*) FILTER (WHERE cod_rata ~ '^[1-9][0-9]*$') AS n_numerico,
           COUNT(DISTINCT CASE WHEN cod_rata ~ '^[1-9][0-9]*$' THEN cod_rata::int END) AS n_numerico_distinti,
           COUNT(*) FILTER (WHERE stato_versamento = 'ANNULLATO') AS n_annullato,
           COUNT(*) FILTER (WHERE stato_versamento NOT IN ('NON_ESEGUITO', 'ANNULLATO')) AS n_altro
      FROM versamenti_candidati
     GROUP BY id_documento
),
documenti_eligibili AS (
    SELECT id_documento,
           CASE WHEN n_null = totale THEN 'SOLUZIONE_UNICA'
                WHEN n_numerico = totale AND n_numerico_distinti = totale THEN 'PIANO_RATEALE'
           END AS tipologia,
           CASE WHEN n_annullato = totale THEN 'ANNULLATA'
                WHEN n_altro > 0 THEN 'ATTIVATA'
                ELSE 'DISPONIBILE'
           END AS stato
      FROM classificazione
),
versamenti_numerati AS (
    SELECT v.id, v.id_documento,
           CASE WHEN v.cod_rata ~ '^[1-9][0-9]*$' THEN v.cod_rata::int
                ELSE ROW_NUMBER() OVER (PARTITION BY v.id_documento ORDER BY v.id)
           END AS numero_rata
      FROM versamenti_candidati v
),
nuove_opzioni AS (
    INSERT INTO opzioni_pagamento (id_opzione_pagamento, id_documento, tipologia, stato, versione,
            data_creazione, data_ultimo_aggiornamento)
    SELECT gen_random_uuid(), de.id_documento, de.tipologia, de.stato, 0, now(), now()
      FROM documenti_eligibili de
     WHERE de.tipologia IS NOT NULL
    RETURNING id, id_documento
)
UPDATE versamenti v
   SET id_opzione_pagamento = no.id,
       numero_rata = vn.numero_rata
  FROM nuove_opzioni no
  JOIN versamenti_numerati vn ON vn.id_documento = no.id_documento
 WHERE v.id = vn.id;

WITH versamenti_candidati AS (
    SELECT v.id, v.id_documento, v.cod_rata, v.stato_versamento
      FROM versamenti v
     WHERE v.id_documento IS NOT NULL
       AND v.id_opzione_pagamento IS NULL
       AND NOT EXISTS (SELECT 1 FROM migrazione_v3_scarti s
                        WHERE s.id_documento = v.id_documento AND s.motivo = 'DEBITORI_INCOERENTI')
       AND NOT EXISTS (SELECT 1 FROM opzioni_pagamento o WHERE o.id_documento = v.id_documento)
),
documenti_soglia AS (
    SELECT id_documento
      FROM versamenti_candidati
     GROUP BY id_documento
    HAVING COUNT(*) FILTER (WHERE cod_rata ~ '^(ENTRO[0-9]+|OLTRE[0-9]+|RIDOTTO|SCONTATO)$') = COUNT(*)
       AND COUNT(DISTINCT cod_rata) = COUNT(*)
),
versamenti_soglia AS (
    SELECT vc.id, vc.id_documento,
           CASE WHEN vc.cod_rata LIKE 'ENTRO%' THEN 'SOLUZIONE_UNICA_ENTRO'
                WHEN vc.cod_rata LIKE 'OLTRE%' THEN 'SOLUZIONE_UNICA_OLTRE'
                WHEN vc.cod_rata = 'SCONTATO' THEN 'SOLUZIONE_UNICA_SCONTATO'
                ELSE 'SOLUZIONE_UNICA_RIDOTTO'
           END AS tipologia,
           CASE WHEN vc.cod_rata ~ '^(ENTRO|OLTRE)[0-9]+$'
                THEN substring(vc.cod_rata FROM '[0-9]+$')::int
           END AS giorni,
           CASE WHEN vc.stato_versamento = 'NON_ESEGUITO' THEN 'DISPONIBILE'
                WHEN vc.stato_versamento = 'ANNULLATO' THEN 'ANNULLATA'
                ELSE 'ATTIVATA'
           END AS stato,
           nextval('seq_opzioni_pagamento') AS id_opzione
      FROM versamenti_candidati vc
      JOIN documenti_soglia ds ON ds.id_documento = vc.id_documento
),
nuove_opzioni AS (
    INSERT INTO opzioni_pagamento (id, id_opzione_pagamento, id_documento, tipologia, giorni, stato,
            versione, data_creazione, data_ultimo_aggiornamento)
    SELECT id_opzione, gen_random_uuid(), id_documento, tipologia, giorni, stato, 0, now(), now()
      FROM versamenti_soglia
    RETURNING id
)
-- Il join con nuove_opzioni serve: un CTE che modifica dati viene eseguito solo
-- se la query finale lo referenzia.
UPDATE versamenti v
   SET id_opzione_pagamento = vs.id_opzione,
       numero_rata = 1
  FROM versamenti_soglia vs
  JOIN nuove_opzioni no ON no.id = vs.id_opzione
 WHERE v.id = vs.id;

INSERT INTO migrazione_v3_scarti (id_documento, motivo, dettaglio)
SELECT v.id_documento, 'COD_RATA_ANOMALO',
       substring(string_agg(COALESCE(v.cod_rata, 'NULL'), ',' ORDER BY v.id) FROM 1 FOR 255)
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
-- sullo stesso documento si lasciano come sono.
WITH opzioni_annullate AS (
    UPDATE opzioni_pagamento op
       SET stato = 'ANNULLATA',
           data_ultimo_aggiornamento = now()
     WHERE op.stato = 'DISPONIBILE'
       AND EXISTS (SELECT 1 FROM opzioni_pagamento altra
                    WHERE altra.id_documento = op.id_documento
                      AND altra.stato = 'ATTIVATA')
    RETURNING op.id
)
UPDATE versamenti v
   SET stato_versamento = 'ANNULLATO',
       data_ora_ultimo_aggiornamento = now()
  FROM opzioni_annullate oa
 WHERE v.id_opzione_pagamento = oa.id
   AND v.stato_versamento = 'NON_ESEGUITO';

DROP FUNCTION IF EXISTS pg_temp.gp_decodifica_causale(TEXT);
