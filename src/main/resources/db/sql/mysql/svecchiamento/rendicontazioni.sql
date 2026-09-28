-- =============================================================================
-- Svecchiamento RENDICONTAZIONI - MySQL/MariaDB
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
-- Gli id da cancellare vengono raccolti una volta sola in una tabella
-- temporanea. Su MySQL e' anche necessario: una DELETE non puo' leggere in
-- sottoquery la tabella da cui cancella.
--
-- Uso: mysql -h <host> -u <utente> -D <database> < rendicontazioni.sql
--
-- I valori qui sotto sono i default. svecchiamento-db.sh li sostituisce quando
-- gli si passa --retention-rendicontazioni, --solo-obsoleti o
-- --solo-non-incassati.
-- =============================================================================

SET @retention_rendicontazioni = 24;
SET @solo_obsoleti = 0;
SET @solo_non_incassati = 0;

SET @end_rendicontazioni = CURRENT_DATE - INTERVAL @retention_rendicontazioni MONTH;

SELECT '--- Svecchiamento RENDICONTAZIONI ---' AS stato;
SELECT CONCAT('Retention: ', @retention_rendicontazioni, ' mesi') AS stato;
SELECT CONCAT('Solo obsoleti: ', @solo_obsoleti, ', solo non incassati: ', @solo_non_incassati) AS stato;

DROP TEMPORARY TABLE IF EXISTS svecchiamento_fr;
CREATE TEMPORARY TABLE svecchiamento_fr (id BIGINT NOT NULL PRIMARY KEY);

START TRANSACTION;

SELECT 'Selezione dei flussi da cancellare...' AS stato;
INSERT INTO svecchiamento_fr (id)
SELECT fr.id FROM fr
WHERE fr.data_acquisizione < @end_rendicontazioni
  AND (@solo_obsoleti = 0 OR fr.obsoleto = TRUE)
  AND (@solo_non_incassati = 0 OR fr.id_incasso IS NULL);

SELECT COUNT(*) AS flussi_da_cancellare FROM svecchiamento_fr;

SELECT 'Cancellazione eventi dei flussi...' AS stato;
DELETE FROM eventi WHERE id_fr IN (SELECT id FROM svecchiamento_fr);

SELECT 'Cancellazione rendicontazioni...' AS stato;
DELETE FROM rendicontazioni WHERE id_fr IN (SELECT id FROM svecchiamento_fr);

SELECT 'Cancellazione flussi...' AS stato;
DELETE FROM fr WHERE id IN (SELECT id FROM svecchiamento_fr);

COMMIT;

DROP TEMPORARY TABLE IF EXISTS svecchiamento_fr;
