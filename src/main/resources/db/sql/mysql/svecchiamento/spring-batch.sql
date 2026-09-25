-- =============================================================================
-- Svecchiamento METADATI SPRING BATCH - MySQL/MariaDB
--
-- Elimina le righe delle esecuzioni dei batch anteriori a retention_batch
-- mesi, nell'ordine imposto dalle chiavi esterne.
--
-- Riferimento temporale: COALESCE(END_TIME, START_TIME, CREATE_TIME). CREATE_TIME
-- e' NOT NULL nello schema di Spring Batch, quindi ogni esecuzione ha sempre una
-- data utile anche se non e' mai partita o non e' terminata.
--
-- Non c'e' filtro sullo stato: rientrano anche le esecuzioni non terminate, che
-- hanno END_TIME a NULL. E' una scelta esplicita, serve a poter bonificare le
-- esecuzioni rimaste appese.
--
-- ATTENZIONE: se un job e' in corso e la sua CREATE_TIME e' anteriore alla
-- soglia, le sue righe vengono cancellate e Spring Batch perde traccia
-- dell'esecuzione viva. Eseguire a batch fermi.
--
-- Il corpo replica gli script di svecchiamento dei metadati di govpay-common,
-- che restano la versione di riferimento: la' la soglia e' una data assoluta
-- passata dall'esterno, qui e' una retention in mesi come per le altre
-- sezioni. Se cambia la struttura delle tabelle BATCH_*, vanno allineati.
--
-- Uso: mysql -h <host> -u <utente> -D <database> < spring-batch.sql
--
-- Il valore qui sotto e' il default. svecchiamento-db.sh lo sostituisce quando
-- gli si passa --retention-spring-batch.
-- =============================================================================

SET @retention_batch = 3;

SET @end_batch = CURRENT_TIMESTAMP - INTERVAL @retention_batch MONTH;

SELECT '--- Svecchiamento METADATI SPRING BATCH ---' AS stato;
SELECT CONCAT('Retention: ', @retention_batch, ' mesi') AS stato;

START TRANSACTION;

SELECT '1/6 contesti degli step...' AS stato;
DELETE FROM BATCH_STEP_EXECUTION_CONTEXT
WHERE STEP_EXECUTION_ID IN (
    SELECT se.STEP_EXECUTION_ID
    FROM BATCH_STEP_EXECUTION se
    JOIN BATCH_JOB_EXECUTION je ON se.JOB_EXECUTION_ID = je.JOB_EXECUTION_ID
    WHERE COALESCE(je.END_TIME, je.START_TIME, je.CREATE_TIME) < @end_batch
);

SELECT '2/6 step...' AS stato;
DELETE FROM BATCH_STEP_EXECUTION
WHERE JOB_EXECUTION_ID IN (
    SELECT JOB_EXECUTION_ID
    FROM BATCH_JOB_EXECUTION
    WHERE COALESCE(END_TIME, START_TIME, CREATE_TIME) < @end_batch
);

SELECT '3/6 contesti delle esecuzioni...' AS stato;
DELETE FROM BATCH_JOB_EXECUTION_CONTEXT
WHERE JOB_EXECUTION_ID IN (
    SELECT JOB_EXECUTION_ID
    FROM BATCH_JOB_EXECUTION
    WHERE COALESCE(END_TIME, START_TIME, CREATE_TIME) < @end_batch
);

SELECT '4/6 parametri delle esecuzioni...' AS stato;
DELETE FROM BATCH_JOB_EXECUTION_PARAMS
WHERE JOB_EXECUTION_ID IN (
    SELECT JOB_EXECUTION_ID
    FROM BATCH_JOB_EXECUTION
    WHERE COALESCE(END_TIME, START_TIME, CREATE_TIME) < @end_batch
);

SELECT '5/6 esecuzioni...' AS stato;
DELETE FROM BATCH_JOB_EXECUTION
WHERE COALESCE(END_TIME, START_TIME, CREATE_TIME) < @end_batch;

SELECT '6/6 istanze rimaste senza alcuna esecuzione...' AS stato;
DELETE FROM BATCH_JOB_INSTANCE
WHERE JOB_INSTANCE_ID NOT IN (
    SELECT JOB_INSTANCE_ID
    FROM BATCH_JOB_EXECUTION
);

COMMIT;
