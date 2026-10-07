-- =============================================================================
-- Svecchiamento TRACCIATI - HSQLDB
--
-- Elimina i tracciati completati da piu' di retention_tracciati giorni, con le
-- operazioni e gli eventi che li referenziano, nell'ordine imposto dalle chiavi
-- esterne.
--
-- Gli id dei tracciati vengono raccolti una volta sola in una tabella
-- temporanea di sessione, che DECLARE LOCAL TEMPORARY TABLE crea senza chiudere
-- la transazione: CURRENT_DATE non e' fissato per tutta la transazione, e
-- ripetere il criterio in ogni istruzione, a cavallo della mezzanotte,
-- selezionerebbe tracciati diversi.
--
-- Prima di essere cancellate, le righe vengono copiate in tabelle di archivio
-- con lo stesso nome e il suffisso _aaaammgg (operazioni, tracciati), create
-- se non esistono, senza vincoli ne' indici, e nella stessa transazione delle
-- cancellazioni: l'archivio contiene esattamente le righe cancellate.
-- svecchiamento-db.sh sostituisce aaaammgg con la data dell'esecuzione;
-- eseguito da solo, lo script archivia nelle tabelle _aaaammgg. Gli eventi non
-- vengono archiviati. Salvare ed eliminare le tabelle di archivio e' compito
-- del DBA.
--
-- Uso: SqlTool, oppure da applicazione Java
--
-- Il valore qui sotto e' il default. svecchiamento-db.sh lo sostituisce quando
-- gli si passa --retention-tracciati.
-- =============================================================================

-- HSQLDB non ha variabili negli script: la retention e' il letterale
-- nelle DELETE qui sotto, 30 giorni.

CREATE TABLE IF NOT EXISTS operazioni_aaaammgg AS (SELECT * FROM operazioni) WITH NO DATA;
CREATE TABLE IF NOT EXISTS tracciati_aaaammgg AS (SELECT * FROM tracciati) WITH NO DATA;

DECLARE LOCAL TEMPORARY TABLE svecchiamento_tracciati (id BIGINT NOT NULL PRIMARY KEY) ON COMMIT PRESERVE ROWS;
DELETE FROM SESSION.svecchiamento_tracciati;

INSERT INTO SESSION.svecchiamento_tracciati (id)
SELECT id FROM tracciati WHERE data_completamento < CURRENT_DATE - 30 DAY;

DELETE FROM eventi WHERE id_tracciato IN (SELECT id FROM SESSION.svecchiamento_tracciati);

INSERT INTO operazioni_aaaammgg SELECT * FROM operazioni WHERE id_tracciato IN (SELECT id FROM SESSION.svecchiamento_tracciati);

DELETE FROM operazioni WHERE id_tracciato IN (SELECT id FROM SESSION.svecchiamento_tracciati);

INSERT INTO tracciati_aaaammgg SELECT * FROM tracciati WHERE id IN (SELECT id FROM SESSION.svecchiamento_tracciati);

DELETE FROM tracciati WHERE id IN (SELECT id FROM SESSION.svecchiamento_tracciati);

COMMIT;
