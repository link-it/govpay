-- Export dei flussi di rendicontazione da cancellare - PostgreSQL
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

\echo 'Export dei flussi su file...'
\pset format unaligned
\pset tuples_only on
\pset fieldsep ' '
\o '@file_export_fr@'
SELECT 'FR', cod_dominio || '_' || cod_flusso || '_' || to_char(data_ora_flusso, 'YYYYMMDDHH24MISS'), encode(xml, 'hex')
FROM fr
WHERE id IN (SELECT id FROM svecchiamento_fr) AND length(xml) > 0
ORDER BY id;
\o
\pset format aligned
\pset tuples_only off
\pset fieldsep '|'

SELECT count(*) FILTER (WHERE length(xml) > 0) AS flussi_esportati,
       count(*) FILTER (WHERE xml IS NULL OR length(xml) = 0) AS flussi_senza_xml
FROM fr WHERE id IN (SELECT id FROM svecchiamento_fr);
