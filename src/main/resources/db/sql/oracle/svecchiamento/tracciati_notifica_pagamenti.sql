-- =============================================================================
-- Svecchiamento TRACCIATI NOTIFICA PAGAMENTI - Oracle
--
-- Elimina i tracciati di notifica dei pagamenti (trac_notif_pag) creati da
-- piu' di retention_tnp giorni. Nessun'altra tabella li referenzia.
--
-- Il riferimento e' data_creazione, valorizzata per ogni tracciato: anche
-- quelli mai completati, dopo la retention, non servono piu'.
--
-- Nessun archivio: i tracciati di notifica sono copie di quanto gia' inviato
-- all'ente, e vengono cancellati senza copia.
--
-- Uso: sqlplus utente/password@host:porta/servizio @tracciati_notifica_pagamenti.sql
--
-- Il valore qui sotto e' il default. svecchiamento-db.sh lo sostituisce quando
-- gli si passa --retention-tracciati_notifica_pagamenti.
-- =============================================================================

DEFINE retention_tnp = 30;

PROMPT
PROMPT --- Svecchiamento TRACCIATI NOTIFICA PAGAMENTI ---
PROMPT Retention: &retention_tnp giorni

-- La soglia viene calcolata una volta sola: in Oracle CURRENT_DATE e'
-- rivalutato a ogni istruzione: con un criterio unico la cancellazione resta
-- coerente anche se l'esecuzione passa la mezzanotte.
COLUMN soglia_tnp NEW_VALUE soglia_tnp NOPRINT
SELECT TO_CHAR((CURRENT_DATE - &retention_tnp), 'YYYYMMDDHH24MISS') AS soglia_tnp FROM dual;

PROMPT Cancellazione...
DELETE FROM trac_notif_pag WHERE data_creazione < TO_DATE('&soglia_tnp', 'YYYYMMDDHH24MISS');

COMMIT;
