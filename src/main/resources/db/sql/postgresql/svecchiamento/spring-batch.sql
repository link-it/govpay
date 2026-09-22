-- =============================================================================
-- Svecchiamento METADATI SPRING BATCH - PostgreSQL
--
-- Elimina le righe delle esecuzioni dei batch anteriori a retention_batch
-- giorni, nell'ordine imposto dalle chiavi esterne.
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
-- passata dall'esterno, qui e' una retention in giorni come per le altre
-- sezioni. Se cambia la struttura delle tabelle BATCH_*, vanno allineati.
--
-- Uso: psql -v ON_ERROR_STOP=1 -h <host> -U <utente> -d <database> -f spring-batch.sql
--
-- Il valore qui sotto e' il default. svecchiamento-db.sh lo sostituisce quando
-- gli si passa --retention-batch.
-- =============================================================================

\set retention_batch '\'90 days\''

\set end_batch 'CURRENT_TIMESTAMP - interval :retention_batch '

\echo ''
\echo '--- Svecchiamento METADATI SPRING BATCH ---'
\echo 'Retention: ' :retention_batch

BEGIN;

\echo '1/6 contesti degli step...'
DELETE FROM BATCH_STEP_EXECUTION_CONTEXT
WHERE STEP_EXECUTION_ID IN (
    SELECT se.STEP_EXECUTION_ID
    FROM BATCH_STEP_EXECUTION se
    JOIN BATCH_JOB_EXECUTION je ON se.JOB_EXECUTION_ID = je.JOB_EXECUTION_ID
    WHERE COALESCE(je.END_TIME, je.START_TIME, je.CREATE_TIME) < :end_batch
);

\echo '2/6 step...'
DELETE FROM BATCH_STEP_EXECUTION
WHERE JOB_EXECUTION_ID IN (
    SELECT JOB_EXECUTION_ID
    FROM BATCH_JOB_EXECUTION
    WHERE COALESCE(END_TIME, START_TIME, CREATE_TIME) < :end_batch
);

\echo '3/6 contesti delle esecuzioni...'
DELETE FROM BATCH_JOB_EXECUTION_CONTEXT
WHERE JOB_EXECUTION_ID IN (
    SELECT JOB_EXECUTION_ID
    FROM BATCH_JOB_EXECUTION
    WHERE COALESCE(END_TIME, START_TIME, CREATE_TIME) < :end_batch
);

\echo '4/6 parametri delle esecuzioni...'
DELETE FROM BATCH_JOB_EXECUTION_PARAMS
WHERE JOB_EXECUTION_ID IN (
    SELECT JOB_EXECUTION_ID
    FROM BATCH_JOB_EXECUTION
    WHERE COALESCE(END_TIME, START_TIME, CREATE_TIME) < :end_batch
);

\echo '5/6 esecuzioni...'
DELETE FROM BATCH_JOB_EXECUTION
WHERE COALESCE(END_TIME, START_TIME, CREATE_TIME) < :end_batch;

\echo '6/6 istanze rimaste senza alcuna esecuzione...'
DELETE FROM BATCH_JOB_INSTANCE
WHERE JOB_INSTANCE_ID NOT IN (
    SELECT JOB_INSTANCE_ID
    FROM BATCH_JOB_EXECUTION
);

COMMIT;
