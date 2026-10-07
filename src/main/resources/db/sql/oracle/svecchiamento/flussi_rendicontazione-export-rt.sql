-- Export delle RT delle pendenze rendicontate da cancellare - Oracle
--
-- Non e' una sezione: svecchiamento-db.sh lo inserisce nella sezione
-- flussi_rendicontazione, al posto della riga export-rt, quando gli si passa
-- --export-rt. Gira dentro la transazione SERIALIZABLE della sezione e prima
-- delle DELETE, con lo stesso criterio: le RT esportate sono esattamente quelle
-- delle RPT cancellate.
--
-- Scrive righe "RT <cod_dominio>_<iuv>_<ccp> <pezzo di xml_rt in esadecimale>"
-- nel file indicato con SPOOL, che sqlplus scrive sulla macchina del client.
-- Ogni RT e' spezzata in pezzi da 1000 byte, in ordine: in SQL un VARCHAR2 non
-- supera i 4000 caratteri, e una RT intera non ci sta. svecchiamento-db.sh
-- ricompone i pezzi e ricava un .xml per RT.
--
-- Richiede Oracle 12c o successivo, per la LATERAL che genera i pezzi.

PROMPT Export delle RT su file...
SET TERMOUT OFF
SET FEEDBACK OFF
SET HEADING OFF
SET PAGESIZE 0
SET LINESIZE 4000
SET TRIMSPOOL ON
SPOOL '@file_export_rt@'
SELECT 'RT ' || x.cod_dominio || '_' || x.iuv || '_' || x.ccp || ' '
       || RAWTOHEX(DBMS_LOB.SUBSTR(x.xml_rt, 1000, (n.k - 1) * 1000 + 1))
FROM rpt x,
     LATERAL (SELECT LEVEL AS k FROM dual
              CONNECT BY LEVEL <= CEIL(DBMS_LOB.GETLENGTH(x.xml_rt) / 1000)) n
WHERE DBMS_LOB.GETLENGTH(x.xml_rt) > 0
  AND x.id_versamento IN (
        SELECT v.id FROM versamenti v
        WHERE (EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN rendicontazioni r ON r.id_singolo_versamento = sv.id JOIN fr f ON f.id = r.id_fr WHERE sv.id_versamento = v.id AND f.data_ora_flusso < ADD_MONTHS(CURRENT_DATE, -&retention_fr))
            OR EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN pagamenti p ON p.id_singolo_versamento = sv.id JOIN rendicontazioni r ON r.id_pagamento = p.id JOIN fr f ON f.id = r.id_fr WHERE sv.id_versamento = v.id AND f.data_ora_flusso < ADD_MONTHS(CURRENT_DATE, -&retention_fr))
            OR EXISTS (SELECT 1 FROM rpt JOIN pagamenti p ON p.id_rpt = rpt.id JOIN rendicontazioni r ON r.id_pagamento = p.id JOIN fr f ON f.id = r.id_fr WHERE rpt.id_versamento = v.id AND f.data_ora_flusso < ADD_MONTHS(CURRENT_DATE, -&retention_fr)))
          AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN rendicontazioni r ON r.id_singolo_versamento = sv.id JOIN fr f ON f.id = r.id_fr WHERE sv.id_versamento = v.id AND f.data_ora_flusso >= ADD_MONTHS(CURRENT_DATE, -&retention_fr))
          AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN pagamenti p ON p.id_singolo_versamento = sv.id JOIN rendicontazioni r ON r.id_pagamento = p.id JOIN fr f ON f.id = r.id_fr WHERE sv.id_versamento = v.id AND f.data_ora_flusso >= ADD_MONTHS(CURRENT_DATE, -&retention_fr))
          AND NOT EXISTS (SELECT 1 FROM rpt JOIN pagamenti p ON p.id_rpt = rpt.id JOIN rendicontazioni r ON r.id_pagamento = p.id JOIN fr f ON f.id = r.id_fr WHERE rpt.id_versamento = v.id AND f.data_ora_flusso >= ADD_MONTHS(CURRENT_DATE, -&retention_fr))
          AND NOT EXISTS (SELECT 1 FROM singoli_versamenti sv JOIN pagamenti p ON p.id_singolo_versamento = sv.id WHERE sv.id_versamento = v.id AND p.data_pagamento >= ADD_MONTHS(CURRENT_DATE, -&retention_fr))
          AND NOT EXISTS (SELECT 1 FROM rpt JOIN pagamenti p ON p.id_rpt = rpt.id WHERE rpt.id_versamento = v.id AND p.data_pagamento >= ADD_MONTHS(CURRENT_DATE, -&retention_fr))
  )
ORDER BY x.id, n.k;
SPOOL OFF
SET TERMOUT ON
SET FEEDBACK ON
SET HEADING ON
SET PAGESIZE 14
SET LINESIZE 80
