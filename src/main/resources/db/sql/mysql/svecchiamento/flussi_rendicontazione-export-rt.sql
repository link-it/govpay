-- Export delle RT delle pendenze rendicontate da cancellare - MySQL/MariaDB
--
-- Non e' una sezione: svecchiamento-db.sh lo inserisce nella sezione
-- flussi_rendicontazione, al posto della riga export-rt, quando gli si passa
-- --export-rt. Gira dentro la transazione della sezione e prima delle DELETE,
-- quindi le RT esportate sono esattamente quelle delle RPT cancellate.
--
-- Scrive una riga per RT, "RT <cod_dominio>_<iuv>_<ccp> <xml_rt in esadecimale>",
-- nel file indicato con tee, che il client mysql scrive sulla macchina del
-- client. tee copia l'output anche a video: svecchiamento-db.sh lo filtra.
-- svecchiamento-db.sh ricava poi dal file un .xml per RT.

SELECT 'Export delle RT su file...' AS stato;
tee @file_export_rt@
SELECT CONCAT('RT ', cod_dominio, '_', iuv, '_', ccp, ' ', HEX(xml_rt)) AS rt
FROM rpt
WHERE id_versamento IN (SELECT id FROM svecchiamento_fr_pendenze) AND LENGTH(xml_rt) > 0
ORDER BY id;
notee

SELECT COUNT(*) AS rt_esportate FROM rpt
WHERE id_versamento IN (SELECT id FROM svecchiamento_fr_pendenze) AND LENGTH(xml_rt) > 0;
