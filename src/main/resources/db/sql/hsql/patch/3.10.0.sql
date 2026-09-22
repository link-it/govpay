-- 3.10.0

-- Rimozione connettore FTP dall'intermediario: eliminazione dei connettori
-- FTP censiti nella tabella connettori e drop della colonna cod_connettore_ftp
-- dalla tabella intermediari.
-- NB: questa patch NON e' riapplicabile su un'installazione gia' alla 3.10, ed
-- e' l'unico dei cinque dialetti in cui non lo e'. Sugli altri quattro ogni
-- statement ha una guardia; qui non e' possibile, perche' HSQLDB non offre negli
-- script alcuna forma condizionale: IF ... THEN e BEGIN ATOMIC valgono solo
-- dentro una routine, e il corpo di una routine viene compilato al momento della
-- CREATE, quindi fallisce anch'esso sul riferimento a una colonna assente.
-- Provate tutte e tre le forme su HSQLDB 2.7.2 prima di rinunciare.
--
-- In concreto, rieseguendola: la DELETE qui sotto fallisce perche' la colonna
-- cod_connettore_ftp e' stata eliminata poco piu' avanti, e le ALTER TABLE ADD
-- COLUMN e le CREATE INDEX fallirebbero perche' gli oggetti esistono gia'. Se
-- una patch si interrompe a meta' su questo dialetto, il ripristino non e'
-- rieseguirla ma verificare a mano quali statement sono passati.
DELETE FROM connettori WHERE cod_connettore IN (
    SELECT cod_connettore_ftp FROM intermediari WHERE cod_connettore_ftp IS NOT NULL
);

-- IF EXISTS su DROP COLUMN non esiste in HSQLDB, che lo rifiuta con "user lacks
-- privilege or object not found: IF": la patch si fermava anche qui. Resta il
-- DROP nudo, coerente con la nota sopra sulla non riapplicabilita' di questo
-- dialetto; su HSQLDB non c'e' una forma condizionale con cui proteggerlo.
ALTER TABLE intermediari DROP COLUMN cod_connettore_ftp;

-- Tracciamento dell'IP del richiedente sull'audit trail.
ALTER TABLE gp_audit ADD COLUMN ip_richiedente VARCHAR(45);

-- Integrazione a SEND: attualizzazione dell'importo della pendenza
-- con le spese di notifica sostenute tramite SEND.
-- HSQLDB vuole DEFAULT prima di NOT NULL: nell'ordine inverso rifiuta con
-- "unexpected token: DEFAULT" e la patch si fermava qui.
ALTER TABLE versamenti ADD COLUMN send_abilitato BOOLEAN DEFAULT FALSE NOT NULL;
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

-- Allineamento dei default dei tre booleani fra installazione nuova e aggiornata.
-- Le patch che introducono le colonne non lasciano il default che gov_pay.sql
-- dichiara: esegui_recupero_rt e notifica_inviata sono aggiunte dalla 3.9 senza
-- default, mentre la baseline lo imposta con due ALTER dopo la CREATE TABLE, e
-- send_abilitato e' aggiunta qui sopra con DEFAULT FALSE, che serve solo perche'
-- e' NOT NULL su una tabella popolata e che la baseline non ha. La nullability
-- invece converge gia', perche' la 3.9 la imposta dopo l'UPDATE di
-- valorizzazione.
-- I tre statement sono nella forma usata da gov_pay.sql e sono idempotenti di
-- per se', che su hsql e' l'unica riapplicabilita' disponibile, non avendo il
-- dialetto alcun costrutto condizionale.
ALTER TABLE rendicontazioni ALTER COLUMN esegui_recupero_rt SET DEFAULT true;
ALTER TABLE rendicontazioni ALTER COLUMN notifica_inviata SET DEFAULT false;
ALTER TABLE versamenti ALTER COLUMN send_abilitato DROP DEFAULT;

-- La sonda update-rnd sorvegliava il batch interno di acquisizione dei flussi di
-- rendicontazione, eliminato con la procedura SOAP: nessuno la aggiorna piu', e
-- su un'installazione nuova non viene piu' creata. Senza questa DELETE
-- un'installazione aggiornata se la porterebbe dietro, ferma all'ultimo
-- aggiornamento e segnalata in errore dal cruscotto.
DELETE FROM sonde WHERE nome = 'update-rnd';
