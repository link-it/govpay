-- =============================================================================
-- Svecchiamento PENDENZE ANNULLATE - HSQLDB
--
-- Elimina le pendenze in stato ANNULLATO non piu' aggiornate da piu' di
-- retention_annullate giorni, con tutto cio' che le referenzia, nell'ordine
-- imposto dalle chiavi esterne.
--
-- La data di riferimento e' data_ora_ultimo_aggiornamento: GovPay non registra
-- la data dell'annullamento, e l'annullamento da API o da tracciato non aggiorna
-- questa colonna. Una pendenza vecchia annullata di recente puo' quindi essere
-- svecchiata al primo giro; resta comunque nell'archivio.
--
-- Sono escluse le pendenze che hanno anche una sola voce rendicontata o un
-- pagamento, sulle voci o sulle RPT: non sono pendenze abbandonate, e le loro
-- righe sono referenziate da flussi e incassi che qui non si toccano.
--
-- I documenti a cui le pendenze appartengono non vengono cancellati: un
-- documento raggruppa piu' pendenze, e non e' detto che siano tutte annullate.
-- Quelli rimasti senza pendenze li cancella la sezione documenti_orfani.
--
-- Gli id da cancellare vengono raccolti una volta sola in una tabella
-- temporanea di sessione, che DECLARE LOCAL TEMPORARY TABLE crea senza chiudere
-- la transazione: CURRENT_DATE non e' fissato per tutta la transazione, e
-- ripetere il criterio in ogni istruzione, a cavallo della mezzanotte,
-- selezionerebbe pendenze diverse.
--
-- Prima di essere cancellate, le righe vengono copiate in tabelle di archivio
-- con lo stesso nome e il suffisso _aaaammgg (notifiche, notifiche_app_io,
-- promemoria, operazioni, stampe, allegati, rpt, singoli_versamenti,
-- versamenti), create se non esistono, senza vincoli ne' indici, e nella
-- stessa transazione delle cancellazioni: l'archivio contiene esattamente le
-- righe cancellate. svecchiamento-db.sh sostituisce aaaammgg con la data
-- dell'esecuzione; eseguito da solo, lo script archivia nelle tabelle
-- _aaaammgg. Gli eventi non vengono archiviati. Salvare ed eliminare le
-- tabelle di archivio e' compito del DBA.
--
-- Uso: SqlTool, oppure da applicazione Java
--
-- Il valore qui sotto e' il default. svecchiamento-db.sh lo sostituisce quando
-- gli si passa --retention-pendenze_annullate.
-- =============================================================================

-- HSQLDB non ha variabili negli script: la retention e' il letterale
-- nelle DELETE qui sotto, 365 giorni.

CREATE TABLE IF NOT EXISTS notifiche_aaaammgg AS (SELECT * FROM notifiche) WITH NO DATA;
CREATE TABLE IF NOT EXISTS notifiche_app_io_aaaammgg AS (SELECT * FROM notifiche_app_io) WITH NO DATA;
CREATE TABLE IF NOT EXISTS promemoria_aaaammgg AS (SELECT * FROM promemoria) WITH NO DATA;
CREATE TABLE IF NOT EXISTS operazioni_aaaammgg AS (SELECT * FROM operazioni) WITH NO DATA;
CREATE TABLE IF NOT EXISTS stampe_aaaammgg AS (SELECT * FROM stampe) WITH NO DATA;
CREATE TABLE IF NOT EXISTS allegati_aaaammgg AS (SELECT * FROM allegati) WITH NO DATA;
CREATE TABLE IF NOT EXISTS rpt_aaaammgg AS (SELECT * FROM rpt) WITH NO DATA;
CREATE TABLE IF NOT EXISTS singoli_versamenti_aaaammgg AS (SELECT * FROM singoli_versamenti) WITH NO DATA;
CREATE TABLE IF NOT EXISTS versamenti_aaaammgg AS (SELECT * FROM versamenti) WITH NO DATA;

DECLARE LOCAL TEMPORARY TABLE svecchiamento_annullate (id BIGINT NOT NULL PRIMARY KEY) ON COMMIT PRESERVE ROWS;
DELETE FROM SESSION.svecchiamento_annullate;

INSERT INTO SESSION.svecchiamento_annullate (id)
SELECT v.id FROM versamenti v
WHERE v.stato_versamento = 'ANNULLATO'
  AND v.data_ora_ultimo_aggiornamento < CURRENT_DATE - 365 DAY
  AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN rendicontazioni r ON r.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
  AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN pagamenti p ON p.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
  AND NOT EXISTS (SELECT 1 FROM rpt JOIN pagamenti p ON p.id_rpt = rpt.id WHERE rpt.id_versamento = v.id);

INSERT INTO notifiche_aaaammgg SELECT * FROM notifiche WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento IN (SELECT id FROM SESSION.svecchiamento_annullate));

DELETE FROM notifiche WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento IN (SELECT id FROM SESSION.svecchiamento_annullate));

INSERT INTO notifiche_app_io_aaaammgg SELECT * FROM notifiche_app_io WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento IN (SELECT id FROM SESSION.svecchiamento_annullate));

DELETE FROM notifiche_app_io WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento IN (SELECT id FROM SESSION.svecchiamento_annullate));

INSERT INTO notifiche_app_io_aaaammgg SELECT * FROM notifiche_app_io WHERE id_versamento IN (SELECT id FROM SESSION.svecchiamento_annullate);

DELETE FROM notifiche_app_io WHERE id_versamento IN (SELECT id FROM SESSION.svecchiamento_annullate);

INSERT INTO promemoria_aaaammgg SELECT * FROM promemoria WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento IN (SELECT id FROM SESSION.svecchiamento_annullate));

DELETE FROM promemoria WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento IN (SELECT id FROM SESSION.svecchiamento_annullate));

INSERT INTO promemoria_aaaammgg SELECT * FROM promemoria WHERE id_versamento IN (SELECT id FROM SESSION.svecchiamento_annullate);

DELETE FROM promemoria WHERE id_versamento IN (SELECT id FROM SESSION.svecchiamento_annullate);

INSERT INTO operazioni_aaaammgg SELECT * FROM operazioni WHERE id_stampa IN (SELECT id FROM stampe WHERE id_versamento IN (SELECT id FROM SESSION.svecchiamento_annullate));

DELETE FROM operazioni WHERE id_stampa IN (SELECT id FROM stampe WHERE id_versamento IN (SELECT id FROM SESSION.svecchiamento_annullate));

INSERT INTO operazioni_aaaammgg SELECT * FROM operazioni WHERE id_versamento IN (SELECT id FROM SESSION.svecchiamento_annullate);

DELETE FROM operazioni WHERE id_versamento IN (SELECT id FROM SESSION.svecchiamento_annullate);

INSERT INTO stampe_aaaammgg SELECT * FROM stampe WHERE id_versamento IN (SELECT id FROM SESSION.svecchiamento_annullate);

DELETE FROM stampe WHERE id_versamento IN (SELECT id FROM SESSION.svecchiamento_annullate);

INSERT INTO allegati_aaaammgg SELECT * FROM allegati WHERE id_versamento IN (SELECT id FROM SESSION.svecchiamento_annullate);

DELETE FROM allegati WHERE id_versamento IN (SELECT id FROM SESSION.svecchiamento_annullate);

INSERT INTO rpt_aaaammgg SELECT * FROM rpt WHERE id_versamento IN (SELECT id FROM SESSION.svecchiamento_annullate);

DELETE FROM rpt WHERE id_versamento IN (SELECT id FROM SESSION.svecchiamento_annullate);

INSERT INTO singoli_versamenti_aaaammgg SELECT * FROM singoli_versamenti WHERE id_versamento IN (SELECT id FROM SESSION.svecchiamento_annullate);

DELETE FROM singoli_versamenti WHERE id_versamento IN (SELECT id FROM SESSION.svecchiamento_annullate);

INSERT INTO versamenti_aaaammgg SELECT * FROM versamenti WHERE id IN (SELECT id FROM SESSION.svecchiamento_annullate);

DELETE FROM versamenti WHERE id IN (SELECT id FROM SESSION.svecchiamento_annullate);

COMMIT;
