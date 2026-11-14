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
-- NAV notificato su SEND, da usare per l'attualizzazione al posto del numero avviso
-- della pendenza quando l'avviso notificato e' stato sostituito (Issue #912).
ALTER TABLE versamenti ADD COLUMN send_nav VARCHAR(18);

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

-- govpay-gde-api#43: un evento del Giornale degli Eventi puo' riferirsi alla
-- posizione debitoria complessiva, identificata da cod_documento insieme a
-- cod_applicazione, oltre che alla singola pendenza (cod_versamento_ente).
-- Dichiarata qui e non in govpay-gde-api perche' eventi e' una tabella del core.
ALTER TABLE eventi ADD COLUMN cod_documento VARCHAR(35);

-- Indice per le ricerche degli eventi di una posizione debitoria (filtro
-- codDocumento di govpay-gde-api), come idx_evt_fk_vrs per la singola pendenza.
CREATE INDEX idx_evt_documento ON eventi (cod_applicazione, cod_documento);

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

-- Data di pubblicazione della pendenza: fino a quella data l'avviso non esiste per pagoPA
-- e non viene inviato all'ACA (Issue #914). Esposta anche nella vista v_versamenti.
ALTER TABLE versamenti ADD COLUMN data_pubblicazione TIMESTAMP;

DROP VIEW v_versamenti IF EXISTS;
CREATE VIEW v_versamenti AS
SELECT versamenti.id,
    versamenti.cod_versamento_ente,
    versamenti.nome,
    versamenti.importo_totale,
    versamenti.stato_versamento,
    versamenti.descrizione_stato,
    versamenti.aggiornabile,
    versamenti.tassonomia,
    versamenti.tassonomia_avviso,
    versamenti.data_creazione,
    versamenti.data_validita,
    versamenti.data_scadenza,
    versamenti.data_ora_ultimo_aggiornamento,
    versamenti.causale_versamento,
    versamenti.debitore_identificativo,
    versamenti.debitore_tipo,
    versamenti.debitore_anagrafica,
    versamenti.debitore_indirizzo,
    versamenti.debitore_civico,
    versamenti.debitore_cap,
    versamenti.debitore_localita,
    versamenti.debitore_provincia,
    versamenti.debitore_nazione,
    versamenti.debitore_email,
    versamenti.debitore_telefono,
    versamenti.debitore_cellulare,
    versamenti.debitore_fax,
    versamenti.cod_lotto,
    versamenti.cod_versamento_lotto,
    versamenti.cod_anno_tributario,
    versamenti.cod_bundlekey,
    versamenti.dati_allegati,
    versamenti.incasso,
    versamenti.anomalie,
    versamenti.iuv_versamento,
    versamenti.numero_avviso,
    versamenti.ack,
    versamenti.anomalo,
    versamenti.direzione,
    versamenti.divisione,
    versamenti.id_sessione,
    versamenti.importo_pagato,
    versamenti.data_pagamento,
    versamenti.importo_incassato,
    versamenti.stato_pagamento,
    versamenti.iuv_pagamento,
    versamenti.src_debitore_identificativo,
    versamenti.src_iuv,
    versamenti.cod_rata,
    versamenti.tipo,
    versamenti.data_notifica_avviso,
    versamenti.avviso_notificato,
    versamenti.avv_mail_data_prom_scadenza,
    versamenti.avv_mail_prom_scad_notificato,
    versamenti.avv_app_io_data_prom_scadenza,
    versamenti.avv_app_io_prom_scad_notificat,
    versamenti.id_applicazione,
    versamenti.id_dominio,
    versamenti.id_uo,
    versamenti.id_tipo_versamento,
    versamenti.id_tipo_versamento_dominio,
    versamenti.id_documento,
    versamenti.proprieta,
    versamenti.data_ultima_modifica_aca,
    versamenti.data_ultima_comunicazione_aca,
    documenti.cod_documento,
    documenti.descrizione AS doc_descrizione,
    versamenti.data_pubblicazione
    FROM versamenti LEFT JOIN documenti ON versamenti.id_documento = documenti.id;
