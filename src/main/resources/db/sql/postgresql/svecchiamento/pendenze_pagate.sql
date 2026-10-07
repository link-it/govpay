-- =============================================================================
-- Svecchiamento PENDENZE PAGATE - PostgreSQL
--
-- Elimina le pendenze pagate il cui ultimo pagamento e' piu' vecchio di
-- retention_pendenze_pagate mesi, con le RPT, i pagamenti e tutto cio' che le
-- referenzia, nell'ordine imposto dalle chiavi esterne.
--
-- Una pendenza rientra se:
--   - e' in uno stato di pagamento eseguito: ESEGUITO, PARZIALMENTE_ESEGUITO,
--     ESEGUITO_ALTRO_CANALE, ESEGUITO_SENZA_RPT, INCASSATO;
--   - e' stata pagata con ricevuta piu' di retention_pendenze_pagate mesi fa:
--     data_pagamento e' valorizzata, all'elaborazione della RT, solo per le
--     pendenze pagate con ricevuta;
--   - nessuno dei suoi pagamenti, sulle voci o sulle RPT, e' piu' recente della
--     soglia: data_pagamento e' quella dell'ultima RT elaborata, e una RT
--     recuperata in ritardo potrebbe essere piu' vecchia di un altro pagamento;
--   - nessuna sua voce e nessun suo pagamento e' rendicontato. Le pendenze
--     rendicontate sono svecchiate insieme ai loro flussi, dalla sezione
--     flussi_rendicontazione, e le pendenze pagate senza ricevuta con esse.
--
-- I documenti a cui le pendenze appartengono non vengono cancellati, come per
-- le pendenze scadute. Gli incassi neppure: sono i pagamenti a referenziarli.
--
-- Le RT possono essere salvate su file prima della cancellazione: la riga
-- marcata export-rt qui sotto viene sostituita da svecchiamento-db.sh con il
-- contenuto di pendenze_pagate-export-rt.sql quando gli si passa --export-rt.
-- Eseguito da solo, lo script non salva le RT.
--
-- Uso: psql -v ON_ERROR_STOP=1 -h <host> -U <utente> -d <database> -f pendenze_pagate.sql
--
-- Il valore qui sotto e' il default. svecchiamento-db.sh lo sostituisce quando
-- gli si passa --retention-pendenze_pagate.
-- =============================================================================

\set retention_pendenze_pagate '\'24 months\''

\set end_pendenze_pagate 'CURRENT_DATE - interval :retention_pendenze_pagate '

\echo ''
\echo '--- Svecchiamento PENDENZE PAGATE ---'
\echo 'Retention: ' :retention_pendenze_pagate

BEGIN;

\echo 'Selezione delle pendenze da cancellare...'
CREATE TEMP TABLE svecchiamento_pagate ON COMMIT DROP AS
SELECT v.id FROM versamenti v
WHERE v.stato_versamento IN ('ESEGUITO', 'PARZIALMENTE_ESEGUITO', 'ESEGUITO_ALTRO_CANALE', 'ESEGUITO_SENZA_RPT', 'INCASSATO')
  AND v.data_pagamento < :end_pendenze_pagate
  AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN pagamenti p ON p.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id AND p.data_pagamento >= :end_pendenze_pagate)
  AND NOT EXISTS (SELECT 1 FROM rpt JOIN pagamenti p ON p.id_rpt = rpt.id WHERE rpt.id_versamento = v.id AND p.data_pagamento >= :end_pendenze_pagate)
  AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN rendicontazioni r ON r.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
  AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN pagamenti p ON p.id_singolo_versamento = sv.id JOIN rendicontazioni r ON r.id_pagamento = p.id WHERE sv.id_versamento = v.id)
  AND NOT EXISTS (SELECT 1 FROM rpt JOIN pagamenti p ON p.id_rpt = rpt.id JOIN rendicontazioni r ON r.id_pagamento = p.id WHERE rpt.id_versamento = v.id);

ALTER TABLE svecchiamento_pagate ADD PRIMARY KEY (id);
ANALYZE svecchiamento_pagate;

SELECT count(*) AS pendenze_da_cancellare FROM svecchiamento_pagate;

-- export-rt

\echo 'Cancellazione pagamenti...'
DELETE FROM pagamenti WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento IN (SELECT id FROM svecchiamento_pagate));
DELETE FROM pagamenti WHERE id_singolo_versamento IN (SELECT id FROM singoli_versamenti WHERE id_versamento IN (SELECT id FROM svecchiamento_pagate));

\echo 'Cancellazione notifiche...'
DELETE FROM notifiche WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento IN (SELECT id FROM svecchiamento_pagate));

\echo 'Cancellazione notifiche App IO...'
DELETE FROM notifiche_app_io WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento IN (SELECT id FROM svecchiamento_pagate));
DELETE FROM notifiche_app_io WHERE id_versamento IN (SELECT id FROM svecchiamento_pagate);

\echo 'Cancellazione promemoria...'
DELETE FROM promemoria WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento IN (SELECT id FROM svecchiamento_pagate));
DELETE FROM promemoria WHERE id_versamento IN (SELECT id FROM svecchiamento_pagate);

\echo 'Cancellazione operazioni...'
DELETE FROM operazioni WHERE id_stampa IN (SELECT id FROM stampe WHERE id_versamento IN (SELECT id FROM svecchiamento_pagate));
DELETE FROM operazioni WHERE id_versamento IN (SELECT id FROM svecchiamento_pagate);

\echo 'Cancellazione stampe...'
DELETE FROM stampe WHERE id_versamento IN (SELECT id FROM svecchiamento_pagate);

\echo 'Rimozione large objects (raw_contenuto) dagli allegati...'
SELECT lo_unlink(raw_contenuto) FROM allegati WHERE id_versamento IN (SELECT id FROM svecchiamento_pagate) AND raw_contenuto IS NOT NULL;

\echo 'Cancellazione allegati...'
DELETE FROM allegati WHERE id_versamento IN (SELECT id FROM svecchiamento_pagate);

\echo 'Cancellazione RPT...'
DELETE FROM rpt WHERE id_versamento IN (SELECT id FROM svecchiamento_pagate);

\echo 'Cancellazione voci delle pendenze...'
DELETE FROM singoli_versamenti WHERE id_versamento IN (SELECT id FROM svecchiamento_pagate);

\echo 'Cancellazione pendenze...'
DELETE FROM versamenti WHERE id IN (SELECT id FROM svecchiamento_pagate);

COMMIT;

-- VACUUM non puo' stare in una transazione: va dopo il COMMIT.
\echo 'VACUUM ANALYZE delle tabelle svecchiate...'
VACUUM ANALYZE pagamenti;
VACUUM ANALYZE singoli_versamenti;
VACUUM ANALYZE rpt;
VACUUM ANALYZE versamenti;
VACUUM ANALYZE notifiche;
VACUUM ANALYZE promemoria;
VACUUM ANALYZE stampe;
