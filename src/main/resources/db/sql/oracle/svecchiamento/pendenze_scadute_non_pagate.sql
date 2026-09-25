-- =============================================================================
-- Svecchiamento PENDENZE SCADUTE NON PAGATE - Oracle
--
-- Elimina le pendenze in stato NON_ESEGUITO scadute da piu' di
-- retention_pendenze mesi, con tutto cio' che le referenzia, nell'ordine
-- imposto dalle chiavi esterne.
--
-- Sono escluse le pendenze che hanno anche una sola voce rendicontata o un
-- pagamento, sulle voci o sulle RPT: non sono pendenze abbandonate, e le loro
-- righe sono referenziate da flussi e incassi che qui non si toccano.
--
-- I documenti a cui le pendenze appartengono non vengono cancellati: un
-- documento raggruppa piu' pendenze, e non e' detto che siano tutte scadute.
--
-- Il criterio di selezione e' ripetuto in ogni DELETE invece di essere
-- raccolto in una tabella temporanea, che su Oracle richiederebbe DDL e quindi
-- un commit implicito. E' corretto perche' il criterio e' stabile durante la
-- cancellazione: dipende solo da stato e scadenza della pendenza e
-- dall'assenza di rendicontazioni e pagamenti, che qui non si cancellano.
--
-- Uso: sqlplus utente/password@host:porta/servizio @pendenze_scadute_non_pagate.sql
--
-- Il valore qui sotto e' il default. svecchiamento-db.sh lo sostituisce quando
-- gli si passa --retention-pendenze_scadute_non_pagate.
-- =============================================================================

DEFINE retention_pendenze = 12;

PROMPT
PROMPT --- Svecchiamento PENDENZE SCADUTE NON PAGATE ---
PROMPT Retention: &retention_pendenze mesi

PROMPT Pendenze da cancellare:
SELECT COUNT(*) AS pendenze_da_cancellare FROM (
    SELECT v.id FROM versamenti v
    WHERE v.stato_versamento = 'NON_ESEGUITO'
      AND v.data_scadenza < ADD_MONTHS(CURRENT_DATE, -&retention_pendenze)
      AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN rendicontazioni r ON r.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
      AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN pagamenti p ON p.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
      AND NOT EXISTS (SELECT 1 FROM rpt JOIN pagamenti p ON p.id_rpt = rpt.id WHERE rpt.id_versamento = v.id)
);

PROMPT Cancellazione notifiche...
DELETE FROM notifiche WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento IN (
    SELECT v.id FROM versamenti v
    WHERE v.stato_versamento = 'NON_ESEGUITO'
      AND v.data_scadenza < ADD_MONTHS(CURRENT_DATE, -&retention_pendenze)
      AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN rendicontazioni r ON r.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
      AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN pagamenti p ON p.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
      AND NOT EXISTS (SELECT 1 FROM rpt JOIN pagamenti p ON p.id_rpt = rpt.id WHERE rpt.id_versamento = v.id)
));

PROMPT Cancellazione notifiche App IO...
DELETE FROM notifiche_app_io WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento IN (
    SELECT v.id FROM versamenti v
    WHERE v.stato_versamento = 'NON_ESEGUITO'
      AND v.data_scadenza < ADD_MONTHS(CURRENT_DATE, -&retention_pendenze)
      AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN rendicontazioni r ON r.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
      AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN pagamenti p ON p.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
      AND NOT EXISTS (SELECT 1 FROM rpt JOIN pagamenti p ON p.id_rpt = rpt.id WHERE rpt.id_versamento = v.id)
));
DELETE FROM notifiche_app_io WHERE id_versamento IN (
    SELECT v.id FROM versamenti v
    WHERE v.stato_versamento = 'NON_ESEGUITO'
      AND v.data_scadenza < ADD_MONTHS(CURRENT_DATE, -&retention_pendenze)
      AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN rendicontazioni r ON r.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
      AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN pagamenti p ON p.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
      AND NOT EXISTS (SELECT 1 FROM rpt JOIN pagamenti p ON p.id_rpt = rpt.id WHERE rpt.id_versamento = v.id)
);

PROMPT Cancellazione promemoria...
DELETE FROM promemoria WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento IN (
    SELECT v.id FROM versamenti v
    WHERE v.stato_versamento = 'NON_ESEGUITO'
      AND v.data_scadenza < ADD_MONTHS(CURRENT_DATE, -&retention_pendenze)
      AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN rendicontazioni r ON r.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
      AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN pagamenti p ON p.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
      AND NOT EXISTS (SELECT 1 FROM rpt JOIN pagamenti p ON p.id_rpt = rpt.id WHERE rpt.id_versamento = v.id)
));
DELETE FROM promemoria WHERE id_versamento IN (
    SELECT v.id FROM versamenti v
    WHERE v.stato_versamento = 'NON_ESEGUITO'
      AND v.data_scadenza < ADD_MONTHS(CURRENT_DATE, -&retention_pendenze)
      AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN rendicontazioni r ON r.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
      AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN pagamenti p ON p.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
      AND NOT EXISTS (SELECT 1 FROM rpt JOIN pagamenti p ON p.id_rpt = rpt.id WHERE rpt.id_versamento = v.id)
);

PROMPT Cancellazione operazioni...
DELETE FROM operazioni WHERE id_stampa IN (SELECT id FROM stampe WHERE id_versamento IN (
    SELECT v.id FROM versamenti v
    WHERE v.stato_versamento = 'NON_ESEGUITO'
      AND v.data_scadenza < ADD_MONTHS(CURRENT_DATE, -&retention_pendenze)
      AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN rendicontazioni r ON r.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
      AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN pagamenti p ON p.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
      AND NOT EXISTS (SELECT 1 FROM rpt JOIN pagamenti p ON p.id_rpt = rpt.id WHERE rpt.id_versamento = v.id)
));
DELETE FROM operazioni WHERE id_versamento IN (
    SELECT v.id FROM versamenti v
    WHERE v.stato_versamento = 'NON_ESEGUITO'
      AND v.data_scadenza < ADD_MONTHS(CURRENT_DATE, -&retention_pendenze)
      AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN rendicontazioni r ON r.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
      AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN pagamenti p ON p.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
      AND NOT EXISTS (SELECT 1 FROM rpt JOIN pagamenti p ON p.id_rpt = rpt.id WHERE rpt.id_versamento = v.id)
);

PROMPT Cancellazione stampe...
DELETE FROM stampe WHERE id_versamento IN (
    SELECT v.id FROM versamenti v
    WHERE v.stato_versamento = 'NON_ESEGUITO'
      AND v.data_scadenza < ADD_MONTHS(CURRENT_DATE, -&retention_pendenze)
      AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN rendicontazioni r ON r.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
      AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN pagamenti p ON p.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
      AND NOT EXISTS (SELECT 1 FROM rpt JOIN pagamenti p ON p.id_rpt = rpt.id WHERE rpt.id_versamento = v.id)
);

PROMPT Cancellazione allegati...
DELETE FROM allegati WHERE id_versamento IN (
    SELECT v.id FROM versamenti v
    WHERE v.stato_versamento = 'NON_ESEGUITO'
      AND v.data_scadenza < ADD_MONTHS(CURRENT_DATE, -&retention_pendenze)
      AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN rendicontazioni r ON r.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
      AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN pagamenti p ON p.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
      AND NOT EXISTS (SELECT 1 FROM rpt JOIN pagamenti p ON p.id_rpt = rpt.id WHERE rpt.id_versamento = v.id)
);

PROMPT Cancellazione RPT...
DELETE FROM rpt WHERE id_versamento IN (
    SELECT v.id FROM versamenti v
    WHERE v.stato_versamento = 'NON_ESEGUITO'
      AND v.data_scadenza < ADD_MONTHS(CURRENT_DATE, -&retention_pendenze)
      AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN rendicontazioni r ON r.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
      AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN pagamenti p ON p.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
      AND NOT EXISTS (SELECT 1 FROM rpt JOIN pagamenti p ON p.id_rpt = rpt.id WHERE rpt.id_versamento = v.id)
);

PROMPT Cancellazione voci delle pendenze...
DELETE FROM singoli_versamenti WHERE id_versamento IN (
    SELECT v.id FROM versamenti v
    WHERE v.stato_versamento = 'NON_ESEGUITO'
      AND v.data_scadenza < ADD_MONTHS(CURRENT_DATE, -&retention_pendenze)
      AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN rendicontazioni r ON r.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
      AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN pagamenti p ON p.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
      AND NOT EXISTS (SELECT 1 FROM rpt JOIN pagamenti p ON p.id_rpt = rpt.id WHERE rpt.id_versamento = v.id)
);

PROMPT Cancellazione pendenze...
DELETE FROM versamenti WHERE id IN (
    SELECT v.id FROM versamenti v
    WHERE v.stato_versamento = 'NON_ESEGUITO'
      AND v.data_scadenza < ADD_MONTHS(CURRENT_DATE, -&retention_pendenze)
      AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN rendicontazioni r ON r.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
      AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN pagamenti p ON p.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
      AND NOT EXISTS (SELECT 1 FROM rpt JOIN pagamenti p ON p.id_rpt = rpt.id WHERE rpt.id_versamento = v.id)
);

COMMIT;
