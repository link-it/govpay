-- Export dei flussi di rendicontazione da cancellare - HSQLDB
--
-- Non e' una sezione: svecchiamento-db.sh lo inserisce nella sezione
-- flussi_rendicontazione, al posto della riga export-fr, quando gli si passa
-- --export-fr. Gira dentro la transazione della sezione e prima delle DELETE,
-- quindi i flussi esportati sono esattamente quelli cancellati.
--
-- Scrive righe "FR <cod_dominio>_<cod_flusso>_<data_ora_flusso> <xml in
-- esadecimale>", con la data nel formato AAAAMMGGhhmmss, nel file indicato con
-- \o, che il client scrive sulla macchina del client. svecchiamento-db.sh ricava
-- poi dal file un .xml per flusso. I flussi senza XML, come quelli acquisiti dal
-- batch FdR, non hanno nulla da esportare: vengono solo contati.
-- \o copia l'output anche a video: svecchiamento-db.sh lo filtra.

\o @file_export_fr@
SELECT 'FR ' || cod_dominio || '_' || cod_flusso || '_' || TO_CHAR(data_ora_flusso, 'YYYYMMDDHH24MISS') || ' ' || RAWTOHEX(xml) AS fr
FROM fr
WHERE id IN (SELECT id FROM SESSION.svecchiamento_fr) AND OCTET_LENGTH(xml) > 0
ORDER BY id;
\o

SELECT 'flussi esportati: ' || SUM(CASE WHEN OCTET_LENGTH(xml) > 0 THEN 1 ELSE 0 END)
       || ', senza XML: ' || SUM(CASE WHEN OCTET_LENGTH(xml) > 0 THEN 0 ELSE 1 END)
FROM fr WHERE id IN (SELECT id FROM SESSION.svecchiamento_fr);
