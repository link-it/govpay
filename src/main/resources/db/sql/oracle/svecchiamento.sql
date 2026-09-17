-- =============================================================================
-- Script di svecchiamento dati GovPay - Oracle
--
-- Uso: sqlplus user/password@host:port/service @svecchiamento.sql
--
-- Parametri (impostare prima dell'esecuzione):
-- =============================================================================

-- Retention tracciati (in giorni)
DEFINE retention_tracciati = 7;

-- Retention eventi del giornale (in giorni)
DEFINE retention_eventi = 90;

-- =============================================================================

SET SERVEROUTPUT ON;

PROMPT === Avvio svecchiamento GovPay ===
SELECT SYSDATE AS inizio_svecchiamento FROM DUAL;

PROMPT --- Parametri ---
PROMPT Retention tracciati: &retention_tracciati giorni
PROMPT Retention eventi: &retention_eventi giorni

-- =====================
-- TRACCIATI
-- =====================
PROMPT
PROMPT --- Svecchiamento TRACCIATI ---

PROMPT Cancellazione eventi collegati ai tracciati...
DELETE FROM eventi WHERE id_tracciato IN (SELECT id FROM tracciati WHERE data_completamento < CURRENT_DATE - &retention_tracciati);

PROMPT Cancellazione operazioni collegate ai tracciati...
DELETE FROM operazioni WHERE id_tracciato IN (SELECT id FROM tracciati WHERE data_completamento < CURRENT_DATE - &retention_tracciati);

PROMPT Cancellazione tracciati...
DELETE FROM tracciati WHERE data_completamento < CURRENT_DATE - &retention_tracciati;

-- =====================
-- EVENTI
-- =====================
-- Gli eventi collegati ai tracciati sono gia' stati eliminati sopra, insieme ai
-- tracciati che li referenziavano. Qui si svecchia il resto del giornale per
-- eta', che e' la parte che cresce di piu'. La condizione e' sulla colonna data,
-- coperta dall'indice idx_evt_data. Nessuna tabella referenzia eventi, quindi la
-- cancellazione non e' vincolata da chiavi esterne.
PROMPT
PROMPT --- Svecchiamento EVENTI ---

PROMPT Cancellazione eventi...
DELETE FROM eventi WHERE data < CURRENT_DATE - &retention_eventi;

COMMIT;

PROMPT
PROMPT === Svecchiamento completato ===
SELECT SYSDATE AS fine_svecchiamento FROM DUAL;
