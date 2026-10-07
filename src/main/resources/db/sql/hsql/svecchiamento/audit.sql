-- =============================================================================
-- Svecchiamento AUDIT - HSQLDB
--
-- Elimina le registrazioni dell'audit delle operazioni degli operatori
-- (gp_audit) piu' vecchie di retention_audit giorni. Nessun'altra tabella le
-- referenzia.
--
-- Prima di essere cancellate, le righe vengono copiate nella tabella di
-- archivio gp_audit_aaaammgg, creata se non esiste e riempita nella stessa
-- transazione della cancellazione: l'archivio contiene esattamente le righe
-- cancellate. svecchiamento-db.sh sostituisce aaaammgg con la data
-- dell'esecuzione; eseguito da solo, lo script archivia nella tabella
-- gp_audit_aaaammgg. Salvare ed eliminare la tabella di archivio e' compito
-- del DBA.
--
-- Uso: SqlTool, oppure da applicazione Java
--
-- Il valore qui sotto e' il default. svecchiamento-db.sh lo sostituisce quando
-- gli si passa --retention-audit.
-- =============================================================================

-- HSQLDB non ha variabili negli script: la retention e' il letterale
-- nelle DELETE qui sotto, 365 giorni. Gli id vengono raccolti una volta sola
-- in una tabella temporanea di sessione: CURRENT_DATE non e' fissato per tutta
-- la transazione, e a cavallo della mezzanotte l'archiviazione e la
-- cancellazione selezionerebbero righe diverse.

CREATE TABLE IF NOT EXISTS gp_audit_aaaammgg AS (SELECT * FROM gp_audit) WITH NO DATA;

DECLARE LOCAL TEMPORARY TABLE svecchiamento_audit (id BIGINT NOT NULL PRIMARY KEY) ON COMMIT PRESERVE ROWS;
DELETE FROM SESSION.svecchiamento_audit;

INSERT INTO SESSION.svecchiamento_audit (id) SELECT id FROM gp_audit WHERE data < CURRENT_DATE - 365 DAY;

INSERT INTO gp_audit_aaaammgg SELECT * FROM gp_audit WHERE id IN (SELECT id FROM SESSION.svecchiamento_audit);

DELETE FROM gp_audit WHERE id IN (SELECT id FROM SESSION.svecchiamento_audit);

COMMIT;
