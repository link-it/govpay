-- 3.10.0

-- Rimozione connettore FTP dall'intermediario: eliminazione dei connettori
-- FTP censiti nella tabella connettori e drop della colonna cod_connettore_ftp
-- dalla tabella intermediari.
-- La DELETE va eseguita solo se la colonna c'e' ancora: riapplicando la patch a
-- un'installazione gia' alla 3.10 la colonna e' stata eliminata poco sotto, e la
-- sottoquery fallirebbe. Lo statement e' dinamico perche' altrimenti il
-- riferimento alla colonna assente sarebbe un errore di compilazione, che la
-- guardia non eviterebbe.
IF COL_LENGTH('intermediari', 'cod_connettore_ftp') IS NOT NULL
    EXEC('DELETE FROM connettori WHERE cod_connettore IN (SELECT cod_connettore_ftp FROM intermediari WHERE cod_connettore_ftp IS NOT NULL)');
GO

-- SQL Server non supporta IF EXISTS su ALTER TABLE ... DROP COLUMN prima di 2016:
-- guard con COL_LENGTH per idempotenza.
IF COL_LENGTH('intermediari', 'cod_connettore_ftp') IS NOT NULL
    ALTER TABLE intermediari DROP COLUMN cod_connettore_ftp;
GO

-- Tracciamento dell'IP del richiedente sull'audit trail.
IF COL_LENGTH('gp_audit', 'ip_richiedente') IS NULL
    ALTER TABLE gp_audit ADD ip_richiedente VARCHAR(45);
GO

-- Integrazione a SEND: attualizzazione dell'importo della pendenza
-- con le spese di notifica sostenute tramite SEND.
IF COL_LENGTH('versamenti', 'send_abilitato') IS NULL
    ALTER TABLE versamenti ADD send_abilitato BIT NOT NULL DEFAULT 0;
IF COL_LENGTH('versamenti', 'send_importo_totale') IS NULL
    ALTER TABLE versamenti ADD send_importo_totale DECIMAL(15,2);
IF COL_LENGTH('versamenti', 'send_data_aggiornamento') IS NULL
    ALTER TABLE versamenti ADD send_data_aggiornamento DATETIME2;
-- NAV notificato su SEND, da usare per l'attualizzazione al posto del numero avviso
-- della pendenza quando l'avviso notificato e' stato sostituito (Issue #912).
IF COL_LENGTH('versamenti', 'send_nav') IS NULL
    ALTER TABLE versamenti ADD send_nav VARCHAR(18);

IF COL_LENGTH('domini', 'cod_connettore_send') IS NULL
    ALTER TABLE domini ADD cod_connettore_send VARCHAR(255);
GO

-- Indici a supporto della cursor pagination delle console-api su GET /pendenze
-- e GET /ricevute: sort fisso (data DESC, id DESC) della query keyset. Senza
-- questi indici la paginazione degrada a scan sequenziale su tabelle grandi.
-- Dichiarati qui e non in govpay-console-api: impattano tabelle del core,
-- quindi per la regola di proprieta' delle patch stanno nel core e nel govpay.xsd.
IF NOT EXISTS (
    SELECT 1 FROM sys.indexes
     WHERE name = 'idx_versamenti_data_ult_agg_id'
       AND object_id = OBJECT_ID('dbo.versamenti')
)
    CREATE INDEX idx_versamenti_data_ult_agg_id
        ON versamenti (data_ora_ultimo_aggiornamento DESC, id DESC);
GO

IF NOT EXISTS (
    SELECT 1 FROM sys.indexes
     WHERE name = 'idx_rpt_data_msg_ricevuta_id'
       AND object_id = OBJECT_ID('dbo.rpt')
)
    CREATE INDEX idx_rpt_data_msg_ricevuta_id
        ON rpt (data_msg_ricevuta DESC, id DESC);
GO

-- Issue #881 del cruscotto (govpay-console-api#80): preferenze d'uso dell'operatore,
-- JSON opaco che interpreta il solo frontend. Dichiarata qui e non in
-- govpay-console-api perche' operatori e' una tabella del core.
-- Il tipo e' quello che il generatore usa per un xsd:string senza facet, che
-- cambia da dialetto a dialetto: TEXT non esiste su oracle e sqlserver.
IF COL_LENGTH('operatori', 'preferenze') IS NULL
    ALTER TABLE operatori ADD preferenze VARCHAR(max);
GO

-- govpay-gde-api#43: un evento del Giornale degli Eventi puo' riferirsi alla
-- posizione debitoria complessiva, identificata da cod_documento insieme a
-- cod_applicazione, oltre che alla singola pendenza (cod_versamento_ente).
-- Dichiarata qui e non in govpay-gde-api perche' eventi e' una tabella del core.
IF COL_LENGTH('eventi', 'cod_documento') IS NULL
    ALTER TABLE eventi ADD cod_documento VARCHAR(35);
GO

-- Indice per le ricerche degli eventi di una posizione debitoria (filtro
-- codDocumento di govpay-gde-api), come idx_evt_fk_vrs per la singola pendenza.
IF NOT EXISTS (
    SELECT 1 FROM sys.indexes
     WHERE name = 'idx_evt_documento'
       AND object_id = OBJECT_ID('dbo.eventi')
)
    CREATE INDEX idx_evt_documento ON eventi (cod_applicazione, cod_documento);
GO

-- Allineamento dei default dei tre booleani fra installazione nuova e aggiornata.
-- Le patch che introducono le colonne non lasciano il default che gov_pay.sql
-- dichiara: esegui_recupero_rt e notifica_inviata sono aggiunte dalla 3.9 senza
-- default, e send_abilitato e' aggiunta qui sopra con DEFAULT 0, che serve solo
-- perche' la colonna e' NOT NULL su una tabella popolata e che la baseline non ha.
-- Su questo dialetto il default e' un constraint con nome generato dal sistema:
-- per aggiungerlo serve una guardia sull'esistenza, e per toglierlo serve
-- risalire al nome, che non e' noto a priori.
IF NOT EXISTS (SELECT 1 FROM sys.default_constraints
                WHERE parent_object_id = OBJECT_ID('rendicontazioni')
                  AND parent_column_id = COLUMNPROPERTY(OBJECT_ID('rendicontazioni'), 'esegui_recupero_rt', 'ColumnId'))
    ALTER TABLE rendicontazioni ADD DEFAULT 'true' FOR esegui_recupero_rt;
GO

IF NOT EXISTS (SELECT 1 FROM sys.default_constraints
                WHERE parent_object_id = OBJECT_ID('rendicontazioni')
                  AND parent_column_id = COLUMNPROPERTY(OBJECT_ID('rendicontazioni'), 'notifica_inviata', 'ColumnId'))
    ALTER TABLE rendicontazioni ADD DEFAULT 'false' FOR notifica_inviata;
GO

DECLARE @nome_default SYSNAME;
SELECT @nome_default = name FROM sys.default_constraints
 WHERE parent_object_id = OBJECT_ID('versamenti')
   AND parent_column_id = COLUMNPROPERTY(OBJECT_ID('versamenti'), 'send_abilitato', 'ColumnId');
IF @nome_default IS NOT NULL
    EXEC('ALTER TABLE versamenti DROP CONSTRAINT [' + @nome_default + ']');
GO

-- La sonda update-rnd sorvegliava il batch interno di acquisizione dei flussi di
-- rendicontazione, eliminato con la procedura SOAP: nessuno la aggiorna piu', e
-- su un'installazione nuova non viene piu' creata. Senza questa DELETE
-- un'installazione aggiornata se la porterebbe dietro, ferma all'ultimo
-- aggiornamento e segnalata in errore dal cruscotto.
DELETE FROM sonde WHERE nome = 'update-rnd';

-- Data di pubblicazione della pendenza: fino a quella data l'avviso non esiste per pagoPA
-- e non viene inviato all'ACA (Issue #914). Esposta anche nella vista v_versamenti, che espone
-- anche le colonne send_* per gli elenchi delle pendenze.
IF COL_LENGTH('versamenti', 'data_pubblicazione') IS NULL
    ALTER TABLE versamenti ADD data_pubblicazione DATETIME2;
GO

IF OBJECT_ID('v_versamenti', 'V') IS NOT NULL
    DROP VIEW v_versamenti;
GO

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
    versamenti.data_pubblicazione,
    versamenti.send_abilitato,
    versamenti.send_importo_totale,
    versamenti.send_data_aggiornamento,
    versamenti.send_nav
    FROM versamenti LEFT JOIN documenti ON versamenti.id_documento = documenti.id;
GO
