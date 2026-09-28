-- =============================================================================
-- Svecchiamento RENDICONTAZIONI - HSQLDB
--
-- Elimina i flussi di rendicontazione acquisiti da piu' di
-- retention_rendicontazioni mesi, con le loro rendicontazioni e gli eventi
-- collegati al flusso, nell'ordine imposto dalle chiavi esterne.
--
-- Gli eventi del flusso vengono cancellati qui, e per primi, anche quando la
-- sezione eventi e' attiva: non e' detto che lo sia, e comunque le due
-- cancellazioni convivono, perche' qui si tolgono solo gli eventi che
-- impedirebbero di cancellare il flusso.
--
-- Pagamenti, pendenze e incassi non vengono toccati: sono loro a essere
-- referenziati dalle rendicontazioni e dai flussi, non il contrario.
--
-- Due filtri restringono la selezione, e di default sono disattivi:
--   solo_obsoleti       solo i flussi obsoleti, cioe' le revisioni
--                       precedenti di un flusso ripubblicato
--   solo_non_incassati  solo i flussi non riconciliati con un incasso
-- Attivi entrambi, valgono insieme.
--
-- Il criterio di selezione e' ripetuto in ogni DELETE invece di essere
-- raccolto in una tabella temporanea. E' corretto perche' il criterio dipende
-- solo da colonne di fr, e i flussi si cancellano per ultimi.
--
-- Uso: SqlTool, oppure da applicazione Java
--
-- I valori qui sotto sono i default. svecchiamento-db.sh li sostituisce quando
-- gli si passa --retention-rendicontazioni, --solo-obsoleti o
-- --solo-non-incassati.
-- =============================================================================

-- HSQLDB non ha variabili negli script: la retention e' il letterale
-- nelle DELETE qui sotto, 24 mesi. I filtri sono il primo termine delle
-- condizioni su obsoleto e id_incasso: 0 = 0 disattivo, 1 = 0 attivo.

DELETE FROM eventi WHERE id_fr IN (
    SELECT fr.id FROM fr
    WHERE fr.data_acquisizione < CURRENT_DATE - 24 MONTH
      AND (0 = 0 OR fr.obsoleto = TRUE)
      AND (0 = 0 OR fr.id_incasso IS NULL)
);

DELETE FROM rendicontazioni WHERE id_fr IN (
    SELECT fr.id FROM fr
    WHERE fr.data_acquisizione < CURRENT_DATE - 24 MONTH
      AND (0 = 0 OR fr.obsoleto = TRUE)
      AND (0 = 0 OR fr.id_incasso IS NULL)
);

DELETE FROM fr
WHERE fr.data_acquisizione < CURRENT_DATE - 24 MONTH
  AND (0 = 0 OR fr.obsoleto = TRUE)
  AND (0 = 0 OR fr.id_incasso IS NULL);

COMMIT;
