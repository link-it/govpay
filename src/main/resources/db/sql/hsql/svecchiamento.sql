-- =============================================================================
-- Script di svecchiamento dati GovPay - HSQLDB
--
-- Uso: Da SqlTool o applicazione Java
--
-- Parametri (impostare prima dell'esecuzione):
-- =============================================================================

-- Retention tracciati (in giorni)
-- HSQLDB non supporta variabili utente, modificare il valore direttamente nelle query

-- =============================================================================

-- =====================
-- TRACCIATI (retention: 7 giorni)
-- =====================

DELETE FROM eventi WHERE id_tracciato IN (SELECT id FROM tracciati WHERE data_completamento < CURRENT_DATE - 7);

DELETE FROM operazioni WHERE id_tracciato IN (SELECT id FROM tracciati WHERE data_completamento < CURRENT_DATE - 7);

DELETE FROM tracciati WHERE data_completamento < CURRENT_DATE - 7;

COMMIT;
