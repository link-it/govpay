-- =============================================================================
-- Svecchiamento PENDENZE SCADUTE NON PAGATE - HSQLDB
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
-- raccolto in una tabella temporanea. E' corretto perche' il criterio e'
-- stabile durante la cancellazione: dipende solo da stato e scadenza della
-- pendenza e dall'assenza di rendicontazioni e pagamenti, che qui non si
-- cancellano.
--
-- Uso: SqlTool, oppure da applicazione Java
--
-- Il valore qui sotto e' il default. svecchiamento-db.sh lo sostituisce quando
-- gli si passa --retention-pendenze_scadute_non_pagate.
-- =============================================================================

-- HSQLDB non ha variabili negli script: la retention e' il letterale
-- nelle DELETE qui sotto, 12 mesi.

DELETE FROM notifiche WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento IN (
    SELECT v.id FROM versamenti v
    WHERE v.stato_versamento = 'NON_ESEGUITO'
      AND v.data_scadenza < CURRENT_DATE - 12 MONTH
      AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN rendicontazioni r ON r.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
      AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN pagamenti p ON p.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
      AND NOT EXISTS (SELECT 1 FROM rpt JOIN pagamenti p ON p.id_rpt = rpt.id WHERE rpt.id_versamento = v.id)
));

DELETE FROM notifiche_app_io WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento IN (
    SELECT v.id FROM versamenti v
    WHERE v.stato_versamento = 'NON_ESEGUITO'
      AND v.data_scadenza < CURRENT_DATE - 12 MONTH
      AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN rendicontazioni r ON r.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
      AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN pagamenti p ON p.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
      AND NOT EXISTS (SELECT 1 FROM rpt JOIN pagamenti p ON p.id_rpt = rpt.id WHERE rpt.id_versamento = v.id)
));

DELETE FROM notifiche_app_io WHERE id_versamento IN (
    SELECT v.id FROM versamenti v
    WHERE v.stato_versamento = 'NON_ESEGUITO'
      AND v.data_scadenza < CURRENT_DATE - 12 MONTH
      AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN rendicontazioni r ON r.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
      AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN pagamenti p ON p.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
      AND NOT EXISTS (SELECT 1 FROM rpt JOIN pagamenti p ON p.id_rpt = rpt.id WHERE rpt.id_versamento = v.id)
);

DELETE FROM promemoria WHERE id_rpt IN (SELECT id FROM rpt WHERE id_versamento IN (
    SELECT v.id FROM versamenti v
    WHERE v.stato_versamento = 'NON_ESEGUITO'
      AND v.data_scadenza < CURRENT_DATE - 12 MONTH
      AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN rendicontazioni r ON r.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
      AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN pagamenti p ON p.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
      AND NOT EXISTS (SELECT 1 FROM rpt JOIN pagamenti p ON p.id_rpt = rpt.id WHERE rpt.id_versamento = v.id)
));

DELETE FROM promemoria WHERE id_versamento IN (
    SELECT v.id FROM versamenti v
    WHERE v.stato_versamento = 'NON_ESEGUITO'
      AND v.data_scadenza < CURRENT_DATE - 12 MONTH
      AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN rendicontazioni r ON r.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
      AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN pagamenti p ON p.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
      AND NOT EXISTS (SELECT 1 FROM rpt JOIN pagamenti p ON p.id_rpt = rpt.id WHERE rpt.id_versamento = v.id)
);

DELETE FROM operazioni WHERE id_stampa IN (SELECT id FROM stampe WHERE id_versamento IN (
    SELECT v.id FROM versamenti v
    WHERE v.stato_versamento = 'NON_ESEGUITO'
      AND v.data_scadenza < CURRENT_DATE - 12 MONTH
      AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN rendicontazioni r ON r.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
      AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN pagamenti p ON p.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
      AND NOT EXISTS (SELECT 1 FROM rpt JOIN pagamenti p ON p.id_rpt = rpt.id WHERE rpt.id_versamento = v.id)
));

DELETE FROM operazioni WHERE id_versamento IN (
    SELECT v.id FROM versamenti v
    WHERE v.stato_versamento = 'NON_ESEGUITO'
      AND v.data_scadenza < CURRENT_DATE - 12 MONTH
      AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN rendicontazioni r ON r.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
      AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN pagamenti p ON p.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
      AND NOT EXISTS (SELECT 1 FROM rpt JOIN pagamenti p ON p.id_rpt = rpt.id WHERE rpt.id_versamento = v.id)
);

DELETE FROM stampe WHERE id_versamento IN (
    SELECT v.id FROM versamenti v
    WHERE v.stato_versamento = 'NON_ESEGUITO'
      AND v.data_scadenza < CURRENT_DATE - 12 MONTH
      AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN rendicontazioni r ON r.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
      AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN pagamenti p ON p.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
      AND NOT EXISTS (SELECT 1 FROM rpt JOIN pagamenti p ON p.id_rpt = rpt.id WHERE rpt.id_versamento = v.id)
);

DELETE FROM allegati WHERE id_versamento IN (
    SELECT v.id FROM versamenti v
    WHERE v.stato_versamento = 'NON_ESEGUITO'
      AND v.data_scadenza < CURRENT_DATE - 12 MONTH
      AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN rendicontazioni r ON r.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
      AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN pagamenti p ON p.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
      AND NOT EXISTS (SELECT 1 FROM rpt JOIN pagamenti p ON p.id_rpt = rpt.id WHERE rpt.id_versamento = v.id)
);

DELETE FROM rpt WHERE id_versamento IN (
    SELECT v.id FROM versamenti v
    WHERE v.stato_versamento = 'NON_ESEGUITO'
      AND v.data_scadenza < CURRENT_DATE - 12 MONTH
      AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN rendicontazioni r ON r.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
      AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN pagamenti p ON p.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
      AND NOT EXISTS (SELECT 1 FROM rpt JOIN pagamenti p ON p.id_rpt = rpt.id WHERE rpt.id_versamento = v.id)
);

DELETE FROM singoli_versamenti WHERE id_versamento IN (
    SELECT v.id FROM versamenti v
    WHERE v.stato_versamento = 'NON_ESEGUITO'
      AND v.data_scadenza < CURRENT_DATE - 12 MONTH
      AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN rendicontazioni r ON r.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
      AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN pagamenti p ON p.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
      AND NOT EXISTS (SELECT 1 FROM rpt JOIN pagamenti p ON p.id_rpt = rpt.id WHERE rpt.id_versamento = v.id)
);

DELETE FROM versamenti WHERE id IN (
    SELECT v.id FROM versamenti v
    WHERE v.stato_versamento = 'NON_ESEGUITO'
      AND v.data_scadenza < CURRENT_DATE - 12 MONTH
      AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN rendicontazioni r ON r.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
      AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN pagamenti p ON p.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id)
      AND NOT EXISTS (SELECT 1 FROM rpt JOIN pagamenti p ON p.id_rpt = rpt.id WHERE rpt.id_versamento = v.id)
);

COMMIT;
