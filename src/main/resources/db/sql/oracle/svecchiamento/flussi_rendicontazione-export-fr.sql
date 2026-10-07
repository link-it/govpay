-- Export dei flussi di rendicontazione da cancellare - Oracle
--
-- Non e' una sezione: svecchiamento-db.sh lo inserisce nella sezione
-- flussi_rendicontazione, al posto della riga export-fr, quando gli si passa
-- --export-fr. Gira dentro la transazione della sezione e prima delle DELETE,
-- quindi i flussi esportati sono esattamente quelli cancellati.
--
-- Scrive righe "FR <cod_dominio>_<cod_flusso>_<data_ora_flusso> <xml in
-- esadecimale>", con la data nel formato AAAAMMGGhhmmss, nel file indicato con
-- SPOOL, che il client scrive sulla macchina del client. svecchiamento-db.sh ricava
-- poi dal file un .xml per flusso. I flussi senza XML, come quelli acquisiti dal
-- batch FdR, non hanno nulla da esportare: vengono solo contati.
--
-- Ogni flusso e' spezzato in pezzi da 1000 byte, in ordine: in SQL un VARCHAR2
-- non supera i 4000 caratteri. I flussi sono quelli della sezione, letti nella
-- stessa transazione SERIALIZABLE. Richiede Oracle 12c o successivo, per la
-- LATERAL che genera i pezzi.

PROMPT Export dei flussi su file...
SET TERMOUT OFF
SET FEEDBACK OFF
SET HEADING OFF
SET PAGESIZE 0
SET LINESIZE 4000
SET TRIMSPOOL ON
SPOOL '@file_export_fr@'
SELECT 'FR ' || x.cod_dominio || '_' || x.cod_flusso || '_' || TO_CHAR(x.data_ora_flusso, 'YYYYMMDDHH24MISS') || ' '
       || RAWTOHEX(DBMS_LOB.SUBSTR(x.xml, 1000, (n.k - 1) * 1000 + 1))
FROM fr x,
     LATERAL (SELECT LEVEL AS k FROM dual
              CONNECT BY LEVEL <= CEIL(DBMS_LOB.GETLENGTH(x.xml) / 1000)) n
WHERE x.data_ora_flusso < ADD_MONTHS(CURRENT_DATE, -&retention_fr)
  AND DBMS_LOB.GETLENGTH(x.xml) > 0
ORDER BY x.id, n.k;
SPOOL OFF
SET TERMOUT ON
SET FEEDBACK ON
SET HEADING ON
SET PAGESIZE 14
SET LINESIZE 80

PROMPT Flussi esportati e senza XML:
SELECT SUM(CASE WHEN DBMS_LOB.GETLENGTH(xml) > 0 THEN 1 ELSE 0 END) AS flussi_esportati,
       SUM(CASE WHEN DBMS_LOB.GETLENGTH(xml) > 0 THEN 0 ELSE 1 END) AS flussi_senza_xml
FROM fr WHERE data_ora_flusso < ADD_MONTHS(CURRENT_DATE, -&retention_fr);
