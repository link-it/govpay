-- =============================================================================
-- Svecchiamento TRACCIATI NOTIFICA PAGAMENTI - HSQLDB
--
-- Elimina i tracciati di notifica dei pagamenti (trac_notif_pag) creati da
-- piu' di retention_tnp giorni. Nessun'altra tabella li referenzia.
--
-- Il riferimento e' data_creazione, valorizzata per ogni tracciato: anche
-- quelli mai completati, dopo la retention, non servono piu'.
--
-- Nessun archivio: i tracciati di notifica sono copie di quanto gia' inviato
-- all'ente, e vengono cancellati senza copia.
--
-- Uso: SqlTool, oppure da applicazione Java
--
-- Il valore qui sotto e' il default. svecchiamento-db.sh lo sostituisce quando
-- gli si passa --retention-tracciati_notifica_pagamenti.
-- =============================================================================

-- HSQLDB non ha variabili negli script: la retention e' il letterale
-- nelle DELETE qui sotto, 30 giorni. Gli id vengono raccolti una volta sola
-- in una tabella temporanea di sessione: CURRENT_DATE non e' fissato per tutta
-- la transazione: con un criterio unico la cancellazione resta coerente anche
-- se l'esecuzione passa la mezzanotte.

DECLARE LOCAL TEMPORARY TABLE svecchiamento_tnp (id BIGINT NOT NULL PRIMARY KEY) ON COMMIT PRESERVE ROWS;
DELETE FROM SESSION.svecchiamento_tnp;

INSERT INTO SESSION.svecchiamento_tnp (id) SELECT id FROM trac_notif_pag WHERE data_creazione < CURRENT_DATE - 30 DAY;

DELETE FROM trac_notif_pag WHERE id IN (SELECT id FROM SESSION.svecchiamento_tnp);

COMMIT;
