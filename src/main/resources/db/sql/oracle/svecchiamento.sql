-- =============================================================================
-- Script di svecchiamento dati GovPay - Oracle
--
-- Uso: sqlplus user/password@host:port/service @svecchiamento.sql
--
-- Parametri (impostare prima dell'esecuzione):
-- =============================================================================

-- Retention tracciati (in giorni)
DEFINE retention_tracciati = 7;

-- =============================================================================

SET SERVEROUTPUT ON;

PROMPT === Avvio svecchiamento GovPay ===
SELECT SYSDATE AS inizio_svecchiamento FROM DUAL;

PROMPT --- Parametri ---
PROMPT Retention tracciati: &retention_tracciati giorni

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

COMMIT;

PROMPT
PROMPT === Svecchiamento completato ===
SELECT SYSDATE AS fine_svecchiamento FROM DUAL;
