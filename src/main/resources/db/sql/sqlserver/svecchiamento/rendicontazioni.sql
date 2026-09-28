-- =============================================================================
-- Svecchiamento RENDICONTAZIONI - SQL Server
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
-- temporanea locale alla sessione.
--
-- Uso: sqlcmd -b -S <host> -U <utente> -d <database> -i rendicontazioni.sql
--
-- I valori qui sotto sono i default. svecchiamento-db.sh li sostituisce quando
-- gli si passa --retention-rendicontazioni, --solo-obsoleti o
-- --solo-non-incassati.
-- =============================================================================

DECLARE @retention_rendicontazioni INT = 24;
DECLARE @solo_obsoleti INT = 0;
DECLARE @solo_non_incassati INT = 0;

DECLARE @end_rendicontazioni DATE = DATEADD(MONTH, -@retention_rendicontazioni, GETDATE());

PRINT '--- Svecchiamento RENDICONTAZIONI ---';
PRINT 'Retention: ' + CAST(@retention_rendicontazioni AS VARCHAR) + ' mesi';
PRINT 'Solo obsoleti: ' + CAST(@solo_obsoleti AS VARCHAR) + ', solo non incassati: ' + CAST(@solo_non_incassati AS VARCHAR);

IF OBJECT_ID('tempdb..#svecchiamento_fr') IS NOT NULL DROP TABLE #svecchiamento_fr;
CREATE TABLE #svecchiamento_fr (id BIGINT NOT NULL PRIMARY KEY);

BEGIN TRANSACTION;

PRINT 'Selezione dei flussi da cancellare...';
INSERT INTO #svecchiamento_fr (id)
SELECT fr.id FROM fr
WHERE fr.data_acquisizione < @end_rendicontazioni
  AND (@solo_obsoleti = 0 OR fr.obsoleto = 1)
  AND (@solo_non_incassati = 0 OR fr.id_incasso IS NULL);

PRINT 'Flussi da cancellare: ' + CAST(@@ROWCOUNT AS VARCHAR);

PRINT 'Cancellazione eventi dei flussi...';
DELETE FROM eventi WHERE id_fr IN (SELECT id FROM #svecchiamento_fr);

PRINT 'Cancellazione rendicontazioni...';
DELETE FROM rendicontazioni WHERE id_fr IN (SELECT id FROM #svecchiamento_fr);

PRINT 'Cancellazione flussi...';
DELETE FROM fr WHERE id IN (SELECT id FROM #svecchiamento_fr);

COMMIT TRANSACTION;

DROP TABLE #svecchiamento_fr;
