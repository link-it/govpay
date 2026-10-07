-- Export dei flussi di rendicontazione da cancellare - MySQL/MariaDB
--
-- Non e' una sezione: svecchiamento-db.sh lo inserisce nella sezione
-- flussi_rendicontazione, al posto della riga export-fr, quando gli si passa
-- --export-fr. Gira dentro la transazione della sezione e prima delle DELETE,
-- quindi i flussi esportati sono esattamente quelli cancellati.
--
-- Scrive righe "FR <cod_dominio>_<cod_flusso>_<data_ora_flusso> <xml in
-- esadecimale>", con la data nel formato AAAAMMGGhhmmss, nel file indicato con
-- tee, che il client scrive sulla macchina del client. svecchiamento-db.sh ricava
-- poi dal file un .xml per flusso. I flussi senza XML, come quelli acquisiti dal
-- batch FdR, non hanno nulla da esportare: vengono solo contati.
-- tee copia l'output anche a video: svecchiamento-db.sh lo filtra.

SELECT 'Export dei flussi su file...' AS stato;
tee @file_export_fr@
SELECT CONCAT('FR ', cod_dominio, '_', cod_flusso, '_', DATE_FORMAT(data_ora_flusso, '%Y%m%d%H%i%s'), ' ', HEX(xml)) AS fr
FROM fr
WHERE id IN (SELECT id FROM svecchiamento_fr) AND LENGTH(xml) > 0
ORDER BY id;
notee

SELECT SUM(CASE WHEN LENGTH(xml) > 0 THEN 1 ELSE 0 END) AS flussi_esportati,
       SUM(CASE WHEN LENGTH(xml) > 0 THEN 0 ELSE 1 END) AS flussi_senza_xml
FROM fr WHERE id IN (SELECT id FROM svecchiamento_fr);
