-- =============================================================================
-- Svecchiamento RENDICONTAZIONI - Oracle
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
-- Due filtri restringono la selezione, e di default sono disattivi (0):
--   solo_obsoleti      = 1  solo i flussi obsoleti, cioe' le revisioni
--                           precedenti di un flusso ripubblicato
--   solo_non_incassati = 1  solo i flussi non riconciliati con un incasso
-- Attivi entrambi, valgono insieme.
--
-- Il criterio di selezione e' ripetuto in ogni DELETE invece di essere
-- raccolto in una tabella temporanea, che su Oracle richiederebbe DDL e quindi
-- un commit implicito. E' corretto perche' il criterio dipende solo da colonne
-- di fr, e i flussi si cancellano per ultimi.
--
-- Uso: sqlplus utente/password@host:porta/servizio @rendicontazioni.sql
--
-- I valori qui sotto sono i default. svecchiamento-db.sh li sostituisce quando
-- gli si passa --retention-rendicontazioni, --solo-obsoleti o
-- --solo-non-incassati.
-- =============================================================================

DEFINE retention_rendicontazioni = 24;
DEFINE solo_obsoleti = 0;
DEFINE solo_non_incassati = 0;

PROMPT
PROMPT --- Svecchiamento RENDICONTAZIONI ---
PROMPT Retention: &retention_rendicontazioni mesi
PROMPT Solo obsoleti: &solo_obsoleti, solo non incassati: &solo_non_incassati

PROMPT Flussi da cancellare:
SELECT COUNT(*) AS flussi_da_cancellare FROM fr
WHERE fr.data_acquisizione < ADD_MONTHS(CURRENT_DATE, -&retention_rendicontazioni)
  AND (&solo_obsoleti = 0 OR fr.obsoleto = 1)
  AND (&solo_non_incassati = 0 OR fr.id_incasso IS NULL);

PROMPT Cancellazione eventi dei flussi...
DELETE FROM eventi WHERE id_fr IN (
    SELECT fr.id FROM fr
    WHERE fr.data_acquisizione < ADD_MONTHS(CURRENT_DATE, -&retention_rendicontazioni)
      AND (&solo_obsoleti = 0 OR fr.obsoleto = 1)
      AND (&solo_non_incassati = 0 OR fr.id_incasso IS NULL)
);

PROMPT Cancellazione rendicontazioni...
DELETE FROM rendicontazioni WHERE id_fr IN (
    SELECT fr.id FROM fr
    WHERE fr.data_acquisizione < ADD_MONTHS(CURRENT_DATE, -&retention_rendicontazioni)
      AND (&solo_obsoleti = 0 OR fr.obsoleto = 1)
      AND (&solo_non_incassati = 0 OR fr.id_incasso IS NULL)
);

PROMPT Cancellazione flussi...
DELETE FROM fr
WHERE fr.data_acquisizione < ADD_MONTHS(CURRENT_DATE, -&retention_rendicontazioni)
  AND (&solo_obsoleti = 0 OR fr.obsoleto = 1)
  AND (&solo_non_incassati = 0 OR fr.id_incasso IS NULL);

COMMIT;
