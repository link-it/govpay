-- Export delle RT delle pendenze rendicontate da cancellare - PostgreSQL
--
-- Non e' una sezione: svecchiamento-db.sh lo inserisce nella sezione
-- flussi_rendicontazione, al posto della riga export-rt, quando gli si passa
-- --export-rt. Gira dentro la transazione della sezione e prima delle DELETE,
-- quindi le RT esportate sono esattamente quelle delle RPT cancellate.
--
-- Scrive una riga per RT, "RT <cod_dominio>_<iuv>_<ccp> <xml_rt in esadecimale>",
-- nel file indicato con \o, che psql scrive sulla macchina del client.
-- svecchiamento-db.sh ricava poi dal file un .xml per RT.

\echo 'Export delle RT su file...'
\pset format unaligned
\pset tuples_only on
\pset fieldsep ' '
\o '@file_export_rt@'
SELECT 'RT', cod_dominio || '_' || iuv || '_' || ccp, encode(xml_rt, 'hex')
FROM rpt
WHERE id_versamento IN (SELECT id FROM svecchiamento_fr_pendenze) AND length(xml_rt) > 0
ORDER BY id;
\o
\pset format aligned
\pset tuples_only off
\pset fieldsep '|'

SELECT count(*) AS rt_esportate FROM rpt
WHERE id_versamento IN (SELECT id FROM svecchiamento_fr_pendenze) AND length(xml_rt) > 0;
