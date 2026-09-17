-- 3.10.0

-- Rimozione connettore FTP dall'intermediario: eliminazione dei connettori
-- FTP censiti nella tabella connettori e drop della colonna cod_connettore_ftp
-- dalla tabella intermediari.
-- NB: su questo dialetto la DELETE non e' protetta da una guardia, quindi la
-- patch non e' riapplicabile a un'installazione gia' alla 3.10: la colonna
-- viene eliminata poco sotto e la sottoquery fallirebbe. HSQLDB non offre negli
-- script alcuna forma condizionale: IF ... THEN e BEGIN ATOMIC sono ammessi solo
-- dentro una routine, e il corpo di una routine viene compilato al momento della
-- CREATE, quindi fallisce anch'esso sul riferimento alla colonna assente.
-- Verificato su HSQLDB 2.7.2. Sugli altri quattro dialetti la guardia c'e'.
DELETE FROM connettori WHERE cod_connettore IN (
    SELECT cod_connettore_ftp FROM intermediari WHERE cod_connettore_ftp IS NOT NULL
);

ALTER TABLE intermediari DROP COLUMN IF EXISTS cod_connettore_ftp;

-- Tracciamento dell'IP del richiedente sull'audit trail.
ALTER TABLE gp_audit ADD COLUMN ip_richiedente VARCHAR(45);

-- Integrazione a SEND: attualizzazione dell'importo della pendenza
-- con le spese di notifica sostenute tramite SEND.
ALTER TABLE versamenti ADD COLUMN send_abilitato BOOLEAN NOT NULL DEFAULT FALSE;
ALTER TABLE versamenti ADD COLUMN send_importo_totale DOUBLE;
ALTER TABLE versamenti ADD COLUMN send_data_aggiornamento TIMESTAMP;

ALTER TABLE domini ADD COLUMN cod_connettore_send VARCHAR(255);

-- Indici a supporto della cursor pagination delle console-api su GET /pendenze
-- e GET /ricevute: sort fisso (data DESC, id DESC) della query keyset. Senza
-- questi indici la paginazione degrada a scan sequenziale su tabelle grandi.
-- Dichiarati qui e non in govpay-console-api: impattano tabelle del core,
-- quindi per la regola di proprieta' delle patch stanno nel core e nel govpay.xsd.
CREATE INDEX idx_versamenti_data_ult_agg_id
    ON versamenti (data_ora_ultimo_aggiornamento DESC, id DESC);

CREATE INDEX idx_rpt_data_msg_ricevuta_id
    ON rpt (data_msg_ricevuta DESC, id DESC);

-- Issue #881 del cruscotto (govpay-console-api#80): preferenze d'uso dell'operatore,
-- JSON opaco che interpreta il solo frontend. Dichiarata qui e non in
-- govpay-console-api perche' operatori e' una tabella del core.
-- Il tipo e' quello che il generatore usa per un xsd:string senza facet, che
-- cambia da dialetto a dialetto: TEXT non esiste su oracle e sqlserver.
ALTER TABLE operatori ADD COLUMN preferenze LONGVARCHAR;

-- Sequence rimaste orfane dalla rimozione dei pagamenti portale: la patch 3.9.2
-- elimina le tabelle pagamenti_portale e pag_port_versamenti ma non le loro
-- sequence, mentre il gov_pay.sql rigenerato non le dichiara piu'. Un'installazione
-- aggiornata se le portava quindi dietro, divergendo da una installata da zero.
-- I drop sono idempotenti perche' su un'installazione nuova quegli oggetti non
-- esistono e la patch deve restare applicabile in entrambi i casi.
-- Su questo dialetto la baseline creava anche due tabelle di appoggio per
-- l'inizializzazione delle sequence, che la 3.9.2 non elimina: gli oggetti
-- orfani qui sono quattro, non due.
DROP SEQUENCE IF EXISTS seq_pagamenti_portale;
DROP SEQUENCE IF EXISTS seq_pag_port_versamenti;
DROP TABLE IF EXISTS pagamenti_portale_init_seq;
DROP TABLE IF EXISTS pag_port_versamenti_init_seq;
