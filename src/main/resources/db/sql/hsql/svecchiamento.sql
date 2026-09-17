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

DELETE FROM eventi WHERE id_tracciato IN (SELECT id FROM tracciati WHERE data_completamento < CURRENT_DATE - 7 DAY);

DELETE FROM operazioni WHERE id_tracciato IN (SELECT id FROM tracciati WHERE data_completamento < CURRENT_DATE - 7 DAY);

DELETE FROM tracciati WHERE data_completamento < CURRENT_DATE - 7 DAY;

-- =====================
-- EVENTI (retention: 90 giorni)
-- =====================
-- Gli eventi collegati ai tracciati sono gia' stati eliminati sopra, insieme ai
-- tracciati che li referenziavano. Qui si svecchia il resto del giornale per
-- eta', che e' la parte che cresce di piu'. La condizione e' sulla colonna data,
-- coperta dall'indice idx_evt_data. Nessuna tabella referenzia eventi, quindi la
-- cancellazione non e' vincolata da chiavi esterne.

DELETE FROM eventi WHERE data < CURRENT_DATE - 90 DAY;

COMMIT;
