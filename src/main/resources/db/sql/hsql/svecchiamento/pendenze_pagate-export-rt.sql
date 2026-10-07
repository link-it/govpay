-- Export delle RT delle pendenze pagate da cancellare - HSQLDB
--
-- Non e' una sezione: svecchiamento-db.sh lo inserisce in pendenze_pagate.sql,
-- al posto della riga export-rt, quando gli si passa --export-rt. Gira dentro
-- la transazione della sezione e prima delle DELETE, quindi le RT esportate
-- sono esattamente quelle delle RPT cancellate.
--
-- Scrive una riga per RT, "RT <cod_dominio>_<iuv>_<ccp> <xml_rt in esadecimale>",
-- nel file indicato con \o, che SqlTool scrive sulla macchina del client. \o
-- copia l'output anche a video: svecchiamento-db.sh lo filtra.
-- svecchiamento-db.sh ricava poi dal file un .xml per RT.

\o @file_export_rt@
SELECT 'RT ' || cod_dominio || '_' || iuv || '_' || ccp || ' ' || RAWTOHEX(xml_rt) AS rt
FROM rpt
WHERE id_versamento IN (SELECT id FROM SESSION.svecchiamento_pagate) AND OCTET_LENGTH(xml_rt) > 0
ORDER BY id;
\o

SELECT 'rt esportate: ' || COUNT(*) FROM rpt
WHERE id_versamento IN (SELECT id FROM SESSION.svecchiamento_pagate) AND OCTET_LENGTH(xml_rt) > 0;
