-- Export dei flussi di rendicontazione da cancellare - SQL Server
--
-- Non e' una sezione: svecchiamento-db.sh lo inserisce nella sezione
-- flussi_rendicontazione, al posto della riga export-fr, quando gli si passa
-- --export-fr. Gira dentro la transazione della sezione e prima delle DELETE,
-- quindi i flussi esportati sono esattamente quelli cancellati.
--
-- Scrive righe "FR <cod_dominio>_<cod_flusso>_<data_ora_flusso> <xml in
-- esadecimale>", con la data nel formato AAAAMMGGhhmmss, nel file indicato con
-- :Out, che il client scrive sulla macchina del client. svecchiamento-db.sh ricava
-- poi dal file un .xml per flusso. I flussi senza XML, come quelli acquisiti dal
-- batch FdR, non hanno nulla da esportare: vengono solo contati.
--
-- Ogni flusso e' spezzato in pezzi da 1000 byte, in ordine: sqlcmd tronca a
-- video le colonne di tipo MAX. I GO servono a :Out: sqlcmd esegue i propri
-- comandi quando li legge, e il batch solo al GO.

PRINT 'Export dei flussi su file...';
GO
:Out "@file_export_fr@"
SET NOCOUNT ON;
SELECT CAST('FR ' + f.cod_dominio + '_' + f.cod_flusso + '_'
            + CONVERT(VARCHAR(8), f.data_ora_flusso, 112) + REPLACE(CONVERT(VARCHAR(8), f.data_ora_flusso, 108), ':', '') + ' '
            + CONVERT(VARCHAR(2000), SUBSTRING(f.xml, (n.k - 1) * 1000 + 1, 1000), 2) AS VARCHAR(2200)) AS fr
FROM fr f
CROSS APPLY (
    SELECT TOP (CAST(CEILING(DATALENGTH(f.xml) / 1000.0) AS INT))
           ROW_NUMBER() OVER (ORDER BY (SELECT NULL)) AS k
    FROM sys.all_objects a CROSS JOIN sys.all_objects b
) n
WHERE f.id IN (SELECT id FROM #svecchiamento_fr) AND DATALENGTH(f.xml) > 0
ORDER BY f.id, n.k;
GO
:Out stdout
SET NOCOUNT OFF;

SELECT SUM(CASE WHEN DATALENGTH(xml) > 0 THEN 1 ELSE 0 END) AS flussi_esportati,
       SUM(CASE WHEN DATALENGTH(xml) > 0 THEN 0 ELSE 1 END) AS flussi_senza_xml
FROM fr WHERE id IN (SELECT id FROM #svecchiamento_fr);
