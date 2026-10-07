-- =============================================================================
-- Svecchiamento TRACCIATI NOTIFICA PAGAMENTI - HSQLDB
--
-- Elimina i tracciati di notifica dei pagamenti (trac_notif_pag) creati da
-- piu' di retention_tnp giorni. Nessun'altra tabella li referenzia.
--
-- Il riferimento e' data_creazione, valorizzata per ogni tracciato: anche
-- quelli mai completati, dopo la retention, non servono piu'.
--
-- Prima di essere cancellate, le righe vengono copiate nella tabella di
-- archivio trac_notif_pag_aaaammgg, creata se non esiste e riempita nella
-- stessa transazione della cancellazione: l'archivio contiene esattamente le
-- righe cancellate. svecchiamento-db.sh sostituisce aaaammgg con la data
-- dell'esecuzione; eseguito da solo, lo script archivia nella tabella
-- trac_notif_pag_aaaammgg. Salvare ed eliminare la tabella di archivio e'
-- compito del DBA.
--
-- Uso: SqlTool, oppure da applicazione Java
--
-- Il valore qui sotto e' il default. svecchiamento-db.sh lo sostituisce quando
-- gli si passa --retention-tracciati_notifica_pagamenti.
-- =============================================================================

-- HSQLDB non ha variabili negli script: la retention e' il letterale
-- nelle DELETE qui sotto, 30 giorni. Gli id vengono raccolti una volta sola
-- in una tabella temporanea di sessione: CURRENT_DATE non e' fissato per tutta
-- la transazione, e a cavallo della mezzanotte l'archiviazione e la
-- cancellazione selezionerebbero righe diverse.

CREATE TABLE IF NOT EXISTS trac_notif_pag_aaaammgg AS (SELECT * FROM trac_notif_pag) WITH NO DATA;

DECLARE LOCAL TEMPORARY TABLE svecchiamento_tnp (id BIGINT NOT NULL PRIMARY KEY) ON COMMIT PRESERVE ROWS;
DELETE FROM SESSION.svecchiamento_tnp;

INSERT INTO SESSION.svecchiamento_tnp (id) SELECT id FROM trac_notif_pag WHERE data_creazione < CURRENT_DATE - 30 DAY;

INSERT INTO trac_notif_pag_aaaammgg SELECT * FROM trac_notif_pag WHERE id IN (SELECT id FROM SESSION.svecchiamento_tnp);

DELETE FROM trac_notif_pag WHERE id IN (SELECT id FROM SESSION.svecchiamento_tnp);

COMMIT;
