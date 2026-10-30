-- Riallineamento al modello v3 (Issue #901)
--
-- Da eseguire una volta, al passaggio all'API v3, su un database gia' aggiornato
-- alla 3.11.0. Tra l'aggiornamento e il passaggio il core v2 ha continuato a
-- scrivere: ha creato versamenti nuovi, ne ha aggiunti a posizioni gia' migrate e
-- ha aggiornato lo stato di quelli migrati, senza toccare le opzioni di pagamento.
-- Lo script:
--  R1. collega i versamenti aggiunti a una posizione gia' migrata alla sua unica
--      opzione, quando e' chiaro come: una rata nuova di un PIANO_RATEALE, o un
--      versamento senza rata di una SOLUZIONE_UNICA; gli altri restano negli
--      scarti;
--  R2. porta lo stato delle opzioni migrate allo stato attuale dei versamenti,
--      solo in avanti: DISPONIBILE diventa ATTIVATA se un versamento risulta
--      pagato, e un'opzione diventa ANNULLATA se tutti i suoi versamenti lo sono;
--  R3. porta in avanti le date delle posizioni migrate;
-- poi ripete la migrazione della patch 3.11.0 per le righe ancora da migrare.
--
-- La sezione "Migrazione dei dati al modello v3" e' la stessa di
-- patch/3.11.0.sql: si modifica li' e si ricopia qui, e
-- src/main/resources/db/check-riallineamento-v3.sh verifica che coincidano.
-- Come la patch e' rieseguibile.
-- Va eseguito con psql -v ON_ERROR_STOP=1, per fermarsi al primo errore.

-- R1. Versamenti aggiunti da v2 a una posizione gia' migrata. Si collegano
-- all'unica opzione della posizione: una rata numerica a un PIANO_RATEALE, se
-- quel numero non c'e' ancora, con numero_rata = cod_rata; un versamento senza
-- rata a una SOLUZIONE_UNICA, in coda alle pendenze che ha gia'. Gli altri casi
-- non hanno un'opzione certa: restano senza e la sezione di migrazione li mette
-- negli scarti come COD_RATA_ANOMALO.
DROP TABLE IF EXISTS gp_mig_aggancio;

CREATE TABLE gp_mig_aggancio AS
SELECT v.id AS id_versamento, o.id AS id_opzione,
       CASE WHEN v.cod_rata ~ '^[1-9][0-9]*$' THEN CAST(v.cod_rata AS INT) END AS numero_rata
  FROM versamenti v
  JOIN opzioni_pagamento o ON o.id_documento = v.id_documento
 WHERE v.id_opzione_pagamento IS NULL
   AND o.tipologia = 'PIANO_RATEALE'
   AND v.cod_rata ~ '^[1-9][0-9]*$'
   AND (SELECT COUNT(*) FROM opzioni_pagamento a WHERE a.id_documento = v.id_documento) = 1
   AND NOT EXISTS (SELECT 1 FROM versamenti w
                    WHERE w.id_opzione_pagamento = o.id AND CAST(w.numero_rata AS VARCHAR(20)) = v.cod_rata)
   AND (SELECT COUNT(*) FROM versamenti n
         WHERE n.id_documento = v.id_documento AND n.id_opzione_pagamento IS NULL
           AND n.cod_rata = v.cod_rata) = 1
UNION ALL
SELECT v.id, o.id,
       (SELECT MAX(w.numero_rata) FROM versamenti w WHERE w.id_opzione_pagamento = o.id)
       + (SELECT COUNT(*) FROM versamenti n
           WHERE n.id_documento = v.id_documento AND n.id_opzione_pagamento IS NULL
             AND n.cod_rata IS NULL AND n.id <= v.id)
  FROM versamenti v
  JOIN opzioni_pagamento o ON o.id_documento = v.id_documento
 WHERE v.id_opzione_pagamento IS NULL
   AND o.tipologia = 'SOLUZIONE_UNICA'
   AND v.cod_rata IS NULL
   AND (SELECT COUNT(*) FROM opzioni_pagamento a WHERE a.id_documento = v.id_documento) = 1;

UPDATE versamenti v
   SET id_opzione_pagamento = g.id_opzione,
       numero_rata = g.numero_rata
  FROM gp_mig_aggancio g
 WHERE v.id = g.id_versamento;

DROP TABLE gp_mig_aggancio;

-- R2. Stato delle opzioni, solo in avanti. v2 aggiorna lo stato dei versamenti
-- ma non quello delle opzioni: un'opzione DISPONIBILE con un versamento pagato
-- diventa ATTIVATA, e un'opzione con tutti i versamenti ANNULLATO diventa
-- ANNULLATA. Uno stato non torna mai indietro: un'opzione ATTIVATA resta tale
-- anche se i suoi versamenti risultano ancora da pagare. Le alternative
-- DISPONIBILI di un'opzione ATTIVATA le annulla il passo 5 della migrazione.
-- versione cresce come a ogni modifica fatta dalla libreria.
UPDATE opzioni_pagamento
   SET stato = 'ANNULLATA',
       versione = versione + 1,
       data_ultimo_aggiornamento = now()
 WHERE stato <> 'ANNULLATA'
   AND EXISTS (SELECT 1 FROM versamenti v WHERE v.id_opzione_pagamento = opzioni_pagamento.id)
   AND NOT EXISTS (SELECT 1 FROM versamenti v
                    WHERE v.id_opzione_pagamento = opzioni_pagamento.id
                      AND v.stato_versamento <> 'ANNULLATO');

UPDATE opzioni_pagamento
   SET stato = 'ATTIVATA',
       versione = versione + 1,
       data_ultimo_aggiornamento = now()
 WHERE stato = 'DISPONIBILE'
   AND EXISTS (SELECT 1 FROM versamenti v
                WHERE v.id_opzione_pagamento = opzioni_pagamento.id
                  AND v.stato_versamento NOT IN ('NON_ESEGUITO', 'ANNULLATO'));

-- R3. Date delle posizioni migrate, solo in avanti: la piu' recente tra quella
-- del documento e quelle dei suoi versamenti, che v2 ha continuato ad aggiornare.
UPDATE documenti d
   SET data_ultimo_aggiornamento = x.massima
  FROM (SELECT v.id_documento, MAX(v.data_ora_ultimo_aggiornamento) AS massima
          FROM versamenti v
         WHERE v.id_documento IS NOT NULL
         GROUP BY v.id_documento) x
 WHERE d.id = x.id_documento
   AND x.massima > d.data_ultimo_aggiornamento;

UPDATE documenti d
   SET data_ultima_modifica_aca = x.massima
  FROM (SELECT v.id_documento, MAX(v.data_ultima_modifica_aca) AS massima
          FROM versamenti v
         WHERE v.id_documento IS NOT NULL
         GROUP BY v.id_documento) x
 WHERE d.id = x.id_documento
   AND x.massima IS NOT NULL AND (d.data_ultima_modifica_aca IS NULL OR x.massima > d.data_ultima_modifica_aca);

UPDATE documenti d
   SET data_ultima_comunicazione_aca = x.massima
  FROM (SELECT v.id_documento, MAX(v.data_ultima_comunicazione_aca) AS massima
          FROM versamenti v
         WHERE v.id_documento IS NOT NULL
         GROUP BY v.id_documento) x
 WHERE d.id = x.id_documento
   AND x.massima IS NOT NULL AND (d.data_ultima_comunicazione_aca IS NULL OR x.massima > d.data_ultima_comunicazione_aca);



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
