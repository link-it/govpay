-- =============================================================================
-- Svecchiamento INCASSI ORFANI - HSQLDB
--
-- Elimina gli incassi piu' vecchi di retention_incassi giorni non piu'
-- referenziati da nessun flusso di rendicontazione ne' da nessun pagamento,
-- con i loro eventi.
--
-- Un incasso resta orfano quando il suo flusso o i suoi pagamenti sono stati
-- cancellati senza di lui: da pendenze_pagate, che cancella i pagamenti ma non
-- gli incassi, o da una rimozione manuale delle rendicontazioni o dei flussi.
-- Gli incassi dei flussi svecchiati li cancella gia' flussi_rendicontazione,
-- insieme al flusso.
--
-- Il riferimento e' data_ora_incasso. Gli eventi dell'incasso vengono
-- cancellati e non archiviati, come in tutte le sezioni.
--
-- Prima di essere cancellate, le righe vengono copiate nella tabella di
-- archivio incassi_aaaammgg, creata se non esiste e riempita nella stessa
-- transazione della cancellazione: l'archivio contiene esattamente le righe
-- cancellate. svecchiamento-db.sh sostituisce aaaammgg con la data
-- dell'esecuzione; eseguito da solo, lo script archivia nella tabella
-- incassi_aaaammgg. Salvare ed eliminare la tabella di archivio e' compito del
-- DBA.
--
-- Uso: SqlTool, oppure da applicazione Java
--
-- Il valore qui sotto e' il default. svecchiamento-db.sh lo sostituisce quando
-- gli si passa --retention-incassi_orfani.
-- =============================================================================

-- HSQLDB non ha variabili negli script: la retention e' il letterale
-- nelle DELETE qui sotto, 730 giorni. Gli id vengono raccolti una volta sola
-- in una tabella temporanea di sessione: CURRENT_DATE non e' fissato per tutta
-- la transazione, e a cavallo della mezzanotte l'archiviazione e la
-- cancellazione selezionerebbero righe diverse.

CREATE TABLE IF NOT EXISTS incassi_aaaammgg AS (SELECT * FROM incassi) WITH NO DATA;

DECLARE LOCAL TEMPORARY TABLE svecchiamento_incassi (id BIGINT NOT NULL PRIMARY KEY) ON COMMIT PRESERVE ROWS;
DELETE FROM SESSION.svecchiamento_incassi;

INSERT INTO SESSION.svecchiamento_incassi (id) SELECT id FROM incassi
WHERE data_ora_incasso < CURRENT_DATE - 730 DAY
  AND NOT EXISTS (SELECT 1 FROM fr WHERE fr.id_incasso = incassi.id)
  AND NOT EXISTS (SELECT 1 FROM pagamenti p WHERE p.id_incasso = incassi.id);

DELETE FROM eventi WHERE id_incasso IN (SELECT id FROM SESSION.svecchiamento_incassi);

INSERT INTO incassi_aaaammgg SELECT * FROM incassi WHERE id IN (SELECT id FROM SESSION.svecchiamento_incassi);

DELETE FROM incassi WHERE id IN (SELECT id FROM SESSION.svecchiamento_incassi);

COMMIT;
