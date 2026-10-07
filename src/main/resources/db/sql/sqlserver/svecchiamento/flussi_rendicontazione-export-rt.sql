-- Export delle RT delle pendenze rendicontate da cancellare - SQL Server
--
-- Non e' una sezione: svecchiamento-db.sh lo inserisce nella sezione
-- flussi_rendicontazione, al posto della riga export-rt, quando gli si passa
-- --export-rt. Gira dentro la transazione della sezione e prima delle DELETE,
-- quindi le RT esportate sono esattamente quelle delle RPT cancellate.
--
-- Scrive righe "RT <cod_dominio>_<iuv>_<ccp> <pezzo di xml_rt in esadecimale>"
-- nel file indicato con :Out, che sqlcmd scrive sulla macchina del client.
-- Ogni RT e' spezzata in pezzi da 1000 byte, in ordine: sqlcmd tronca a video
-- le colonne di tipo MAX, e una RT intera in una sola colonna arriverebbe
-- tagliata senza errore. svecchiamento-db.sh ricompone i pezzi e ricava un
-- .xml per RT.
--
-- I GO servono a :Out: sqlcmd esegue i propri comandi quando li legge, e il
-- batch solo al GO. Senza, entrambi i :Out scatterebbero prima della SELECT e
-- il file resterebbe vuoto. Spezzare il batch e' innocuo: la transazione e la
-- tabella temporanea appartengono alla sessione, e le variabili della sezione
-- non servono piu' dopo la selezione.

PRINT 'Export delle RT su file...';
GO
:Out "@file_export_rt@"
SET NOCOUNT ON;
SELECT CAST('RT ' + r.cod_dominio + '_' + r.iuv + '_' + r.ccp + ' '
            + CONVERT(VARCHAR(2000), SUBSTRING(r.xml_rt, (n.k - 1) * 1000 + 1, 1000), 2) AS VARCHAR(2200)) AS rt
FROM rpt r
CROSS APPLY (
    SELECT TOP (CAST(CEILING(DATALENGTH(r.xml_rt) / 1000.0) AS INT))
           ROW_NUMBER() OVER (ORDER BY (SELECT NULL)) AS k
    FROM sys.all_objects a CROSS JOIN sys.all_objects b
) n
WHERE r.id_versamento IN (SELECT id FROM #svecchiamento_fr_pendenze) AND DATALENGTH(r.xml_rt) > 0
ORDER BY r.id, n.k;
GO
:Out stdout
SET NOCOUNT OFF;

SELECT COUNT(*) AS rt_esportate FROM rpt
WHERE id_versamento IN (SELECT id FROM #svecchiamento_fr_pendenze) AND DATALENGTH(xml_rt) > 0;
