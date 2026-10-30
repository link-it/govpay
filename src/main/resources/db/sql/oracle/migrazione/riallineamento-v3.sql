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
-- Va eseguito con WHENEVER SQLERROR EXIT FAILURE, per fermarsi al primo errore.

-- R1. Versamenti aggiunti da v2 a una posizione gia' migrata. Si collegano
-- all'unica opzione della posizione: una rata numerica a un PIANO_RATEALE, se
-- quel numero non c'e' ancora, con numero_rata = cod_rata; un versamento senza
-- rata a una SOLUZIONE_UNICA, in coda alle pendenze che ha gia'. Gli altri casi
-- non hanno un'opzione certa: restano senza e la sezione di migrazione li mette
-- negli scarti come COD_RATA_ANOMALO.
BEGIN
    FOR t IN (SELECT table_name FROM user_tables WHERE table_name = 'GP_MIG_AGGANCIO') LOOP
        EXECUTE IMMEDIATE 'DROP TABLE ' || t.table_name;
    END LOOP;
END;
/

CREATE TABLE gp_mig_aggancio AS
SELECT v.id AS id_versamento, o.id AS id_opzione,
       CASE WHEN REGEXP_LIKE(v.cod_rata, '^[1-9][0-9]*$') THEN TO_NUMBER(v.cod_rata) END AS numero_rata
  FROM versamenti v
  JOIN opzioni_pagamento o ON o.id_documento = v.id_documento
 WHERE v.id_opzione_pagamento IS NULL
   AND o.tipologia = 'PIANO_RATEALE'
   AND REGEXP_LIKE(v.cod_rata, '^[1-9][0-9]*$')
   AND (SELECT COUNT(*) FROM opzioni_pagamento a WHERE a.id_documento = v.id_documento) = 1
   AND NOT EXISTS (SELECT 1 FROM versamenti w
                    WHERE w.id_opzione_pagamento = o.id AND TO_CHAR(w.numero_rata) = v.cod_rata)
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

MERGE INTO versamenti v
USING gp_mig_aggancio g
   ON (v.id = g.id_versamento)
 WHEN MATCHED THEN UPDATE
  SET v.id_opzione_pagamento = g.id_opzione,
      v.numero_rata = g.numero_rata;

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
       data_ultimo_aggiornamento = SYSTIMESTAMP
 WHERE stato <> 'ANNULLATA'
   AND EXISTS (SELECT 1 FROM versamenti v WHERE v.id_opzione_pagamento = opzioni_pagamento.id)
   AND NOT EXISTS (SELECT 1 FROM versamenti v
                    WHERE v.id_opzione_pagamento = opzioni_pagamento.id
                      AND v.stato_versamento <> 'ANNULLATO');

UPDATE opzioni_pagamento
   SET stato = 'ATTIVATA',
       versione = versione + 1,
       data_ultimo_aggiornamento = SYSTIMESTAMP
 WHERE stato = 'DISPONIBILE'
   AND EXISTS (SELECT 1 FROM versamenti v
                WHERE v.id_opzione_pagamento = opzioni_pagamento.id
                  AND v.stato_versamento NOT IN ('NON_ESEGUITO', 'ANNULLATO'));

-- R3. Date delle posizioni migrate, solo in avanti: la piu' recente tra quella
-- del documento e quelle dei suoi versamenti, che v2 ha continuato ad aggiornare.
MERGE INTO documenti d
USING (SELECT v.id_documento, MAX(v.data_ora_ultimo_aggiornamento) AS massima
          FROM versamenti v
         WHERE v.id_documento IS NOT NULL
         GROUP BY v.id_documento) x
   ON (d.id = x.id_documento)
 WHEN MATCHED THEN UPDATE
  SET d.data_ultimo_aggiornamento = CASE WHEN x.massima > d.data_ultimo_aggiornamento THEN x.massima ELSE d.data_ultimo_aggiornamento END;

MERGE INTO documenti d
USING (SELECT v.id_documento, MAX(v.data_ultima_modifica_aca) AS massima
          FROM versamenti v
         WHERE v.id_documento IS NOT NULL
         GROUP BY v.id_documento) x
   ON (d.id = x.id_documento)
 WHEN MATCHED THEN UPDATE
  SET d.data_ultima_modifica_aca = CASE WHEN x.massima IS NOT NULL AND (d.data_ultima_modifica_aca IS NULL OR x.massima > d.data_ultima_modifica_aca) THEN x.massima ELSE d.data_ultima_modifica_aca END;

MERGE INTO documenti d
USING (SELECT v.id_documento, MAX(v.data_ultima_comunicazione_aca) AS massima
          FROM versamenti v
         WHERE v.id_documento IS NOT NULL
         GROUP BY v.id_documento) x
   ON (d.id = x.id_documento)
 WHEN MATCHED THEN UPDATE
  SET d.data_ultima_comunicazione_aca = CASE WHEN x.massima IS NOT NULL AND (d.data_ultima_comunicazione_aca IS NULL OR x.massima > d.data_ultima_comunicazione_aca) THEN x.massima ELSE d.data_ultima_comunicazione_aca END;

COMMIT;


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
-- eliminano quelle rimaste da un'esecuzione interrotta (ORA-00942: non esiste).
-- Richiede Oracle 12.2 o successivo (LISTAGG ... ON OVERFLOW TRUNCATE).

-- Casi non migrati dell'ultima esecuzione. Non e' in gov_pay.sql: si elimina a
-- bonifica conclusa, con DROP TABLE migrazione_v3_scarti.
BEGIN
    EXECUTE IMMEDIATE '
CREATE TABLE migrazione_v3_scarti
(
	id_documento NUMBER,
	id_versamento NUMBER,
	motivo VARCHAR2(35 CHAR) NOT NULL,
	dettaglio VARCHAR2(255 CHAR),
	data TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
)';
EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE != -955 THEN
            RAISE;
        END IF;
END;
/

DELETE FROM migrazione_v3_scarti;

BEGIN
    FOR t IN (SELECT table_name FROM user_tables
               WHERE table_name IN ('GP_MIG_VERSAMENTI', 'GP_MIG_OPZIONI')) LOOP
        EXECUTE IMMEDIATE 'DROP TABLE ' || t.table_name;
    END LOOP;
END;
/

-- 1. Versamenti senza documento. In v3 ogni pendenza appartiene a una posizione
-- debitoria: per ciascuno si crea un documento 'MIGR-' || id del versamento, con
-- la causale come descrizione. La causale v2 e' '01 ' || base64(UTF-8), come la
-- scrive Versamento.CausaleSemplice: la validita' del base64 si controlla prima,
-- perche' la decodifica di un testo non valido interromperebbe la patch. Senza
-- causale la descrizione e' uno spazio: per Oracle la stringa vuota e' NULL, e la
-- colonna e' NOT NULL.
-- Si scartano le causali che non si decodificano (i formati '02' e '03' non sono
-- piu' prodotti da nessun percorso di scrittura) e i versamenti il cui codice e'
-- gia' usato da un documento della stessa applicazione e dello stesso dominio.
INSERT INTO migrazione_v3_scarti (id_versamento, motivo, dettaglio)
SELECT v.id, 'CAUSALE_NON_DECODIFICABILE', SUBSTR(v.causale_versamento, 1, 255)
  FROM versamenti v
 WHERE v.id_documento IS NULL
   AND v.causale_versamento IS NOT NULL
   AND (SUBSTR(v.causale_versamento, 1, 3) <> '01 '
        OR NOT REGEXP_LIKE(SUBSTR(v.causale_versamento, 4), '^[A-Za-z0-9+/]+={0,2}$')
        OR MOD(LENGTH(SUBSTR(v.causale_versamento, 4)), 4) <> 0);

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
       NVL(CASE WHEN v.causale_versamento IS NOT NULL THEN
               UTL_I18N.RAW_TO_CHAR(
                   UTL_ENCODE.BASE64_DECODE(UTL_RAW.CAST_TO_RAW(SUBSTR(v.causale_versamento, 4))),
                   'AL32UTF8')
           END, ' '),
       v.id_dominio,
       v.id_applicazione
  FROM versamenti v
 WHERE v.id_documento IS NULL
   AND NOT EXISTS (SELECT 1 FROM migrazione_v3_scarti s WHERE s.id_versamento = v.id);

UPDATE versamenti v
   SET v.id_documento = (SELECT d.id FROM documenti d
                          WHERE d.cod_documento = 'MIGR-' || v.id
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
   AND COUNT(DISTINCT NVL(v.debitore_tipo,
           CASE WHEN REGEXP_LIKE(v.debitore_identificativo, '^[0-9]{11}$') THEN 'G' ELSE 'F' END)
           || '|' || v.debitore_identificativo) > 1;

INSERT INTO soggetti_debitori (ordine, tipo, identificativo, anagrafica, indirizzo, civico, cap,
        localita, provincia, nazione, email, cellulare, id_documento)
SELECT 0,
       NVL(v.debitore_tipo,
           CASE WHEN REGEXP_LIKE(v.debitore_identificativo, '^[0-9]{11}$') THEN 'G' ELSE 'F' END),
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
-- Le opzioni si preparano in gp_mig_opzioni con il loro UUID, che dopo
-- l'inserimento serve a ritrovarne l'id per collegarle ai versamenti.
CREATE TABLE gp_mig_versamenti AS
SELECT v.id, v.id_documento, v.cod_rata, v.stato_versamento,
       CASE WHEN REGEXP_LIKE(v.cod_rata, '^[1-9][0-9]*$') THEN 1 ELSE 0 END AS numerico,
       CASE WHEN REGEXP_LIKE(v.cod_rata, '^(ENTRO[0-9]+|OLTRE[0-9]+|RIDOTTO|SCONTATO)$') THEN 1 ELSE 0 END AS soglia
  FROM versamenti v
 WHERE v.id_documento IS NOT NULL
   AND v.id_opzione_pagamento IS NULL
   AND NOT EXISTS (SELECT 1 FROM migrazione_v3_scarti s
                    WHERE s.id_documento = v.id_documento AND s.motivo = 'DEBITORI_INCOERENTI')
   AND NOT EXISTS (SELECT 1 FROM opzioni_pagamento o WHERE o.id_documento = v.id_documento);

CREATE TABLE gp_mig_opzioni
(
	chiave RAW(16) NOT NULL,
	id_documento NUMBER NOT NULL,
	id_versamento NUMBER,
	tipologia VARCHAR2(35 CHAR) NOT NULL,
	giorni NUMBER,
	stato VARCHAR2(35 CHAR) NOT NULL
);

INSERT INTO gp_mig_opzioni (chiave, id_documento, tipologia, stato)
SELECT SYS_GUID(), c.id_documento,
       CASE WHEN c.n_null = c.totale THEN 'SOLUZIONE_UNICA' ELSE 'PIANO_RATEALE' END,
       CASE WHEN c.n_annullato = c.totale THEN 'ANNULLATA'
            WHEN c.n_altro > 0 THEN 'ATTIVATA'
            ELSE 'DISPONIBILE'
       END
  FROM (SELECT id_documento,
               COUNT(*) AS totale,
               SUM(CASE WHEN cod_rata IS NULL THEN 1 ELSE 0 END) AS n_null,
               SUM(numerico) AS n_numerico,
               COUNT(DISTINCT CASE WHEN numerico = 1 THEN TO_NUMBER(cod_rata) END) AS n_numerico_distinti,
               SUM(CASE WHEN stato_versamento = 'ANNULLATO' THEN 1 ELSE 0 END) AS n_annullato,
               SUM(CASE WHEN stato_versamento NOT IN ('NON_ESEGUITO', 'ANNULLATO') THEN 1 ELSE 0 END) AS n_altro
          FROM gp_mig_versamenti
         GROUP BY id_documento) c
 WHERE c.n_null = c.totale
    OR (c.n_numerico = c.totale AND c.n_numerico_distinti = c.totale);

INSERT INTO gp_mig_opzioni (chiave, id_documento, id_versamento, tipologia, giorni, stato)
SELECT SYS_GUID(), m.id_documento, m.id,
       CASE WHEN m.cod_rata LIKE 'ENTRO%' THEN 'SOLUZIONE_UNICA_ENTRO'
            WHEN m.cod_rata LIKE 'OLTRE%' THEN 'SOLUZIONE_UNICA_OLTRE'
            WHEN m.cod_rata = 'SCONTATO' THEN 'SOLUZIONE_UNICA_SCONTATO'
            ELSE 'SOLUZIONE_UNICA_RIDOTTO'
       END,
       CASE WHEN m.cod_rata LIKE 'ENTRO%' OR m.cod_rata LIKE 'OLTRE%'
            THEN TO_NUMBER(SUBSTR(m.cod_rata, 6))
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
SELECT chiave, id_documento, tipologia, giorni, stato, 0, SYSTIMESTAMP, SYSTIMESTAMP
  FROM gp_mig_opzioni;

UPDATE versamenti v
   SET (v.id_opzione_pagamento, v.numero_rata) = (
       SELECT o.id, 1
         FROM gp_mig_opzioni m
         JOIN opzioni_pagamento o ON o.id_opzione_pagamento = m.chiave
        WHERE m.id_versamento = v.id)
 WHERE v.id IN (SELECT id_versamento FROM gp_mig_opzioni WHERE id_versamento IS NOT NULL);

UPDATE versamenti v
   SET (v.id_opzione_pagamento, v.numero_rata) = (
       SELECT o.id,
              CASE WHEN c.numerico = 1 THEN TO_NUMBER(c.cod_rata)
                   ELSE (SELECT COUNT(*) FROM gp_mig_versamenti p
                          WHERE p.id_documento = c.id_documento AND p.id <= c.id)
              END
         FROM gp_mig_versamenti c
         JOIN gp_mig_opzioni m ON m.id_documento = c.id_documento AND m.id_versamento IS NULL
         JOIN opzioni_pagamento o ON o.id_opzione_pagamento = m.chiave
        WHERE c.id = v.id)
 WHERE v.id IN (SELECT c.id
                  FROM gp_mig_versamenti c
                  JOIN gp_mig_opzioni m ON m.id_documento = c.id_documento AND m.id_versamento IS NULL);

INSERT INTO migrazione_v3_scarti (id_documento, motivo, dettaglio)
SELECT v.id_documento, 'COD_RATA_ANOMALO',
       SUBSTR(LISTAGG(NVL(v.cod_rata, 'NULL'), ',' ON OVERFLOW TRUNCATE) WITHIN GROUP (ORDER BY v.id), 1, 255)
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
       v.data_ora_ultimo_aggiornamento = SYSTIMESTAMP
 WHERE v.stato_versamento = 'NON_ESEGUITO'
   AND v.id_opzione_pagamento IN (
       SELECT op.id FROM opzioni_pagamento op
        WHERE op.stato = 'DISPONIBILE'
          AND EXISTS (SELECT 1 FROM opzioni_pagamento altra
                       WHERE altra.id_documento = op.id_documento
                         AND altra.stato = 'ATTIVATA'));

UPDATE opzioni_pagamento op
   SET op.stato = 'ANNULLATA',
       op.data_ultimo_aggiornamento = SYSTIMESTAMP
 WHERE op.stato = 'DISPONIBILE'
   AND EXISTS (SELECT 1 FROM opzioni_pagamento altra
                WHERE altra.id_documento = op.id_documento
                  AND altra.stato = 'ATTIVATA');

DROP TABLE gp_mig_versamenti;
DROP TABLE gp_mig_opzioni;

COMMIT;
