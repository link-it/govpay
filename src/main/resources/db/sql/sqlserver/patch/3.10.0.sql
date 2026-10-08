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

-- Indici sulle colonne delle chiavi esterne che non ne avevano uno. Senza, ogni
-- riga cancellata da una tabella padre (rpt, versamenti, incassi, documenti,
-- tracciati, stampe) costa al database una scansione completa della tabella
-- figlia per verificare la chiave esterna: lo svecchiamento ne cancella migliaia
-- a ogni esecuzione. Sette di questi indici servono anche a GovPay in esercizio:
-- allegati di una pendenza, operazioni di un tracciato, pagamenti di un incasso,
-- flussi di un incasso, pendenze e stampe di un documento.
IF NOT EXISTS (
    SELECT 1 FROM sys.indexes
     WHERE name = 'idx_ntf_fk_rpt'
       AND object_id = OBJECT_ID('dbo.notifiche')
)
    CREATE INDEX idx_ntf_fk_rpt ON notifiche (id_rpt);
GO

IF NOT EXISTS (
    SELECT 1 FROM sys.indexes
     WHERE name = 'idx_nai_fk_rpt'
       AND object_id = OBJECT_ID('dbo.notifiche_app_io')
)
    CREATE INDEX idx_nai_fk_rpt ON notifiche_app_io (id_rpt);
GO

IF NOT EXISTS (
    SELECT 1 FROM sys.indexes
     WHERE name = 'idx_nai_fk_vrs'
       AND object_id = OBJECT_ID('dbo.notifiche_app_io')
)
    CREATE INDEX idx_nai_fk_vrs ON notifiche_app_io (id_versamento);
GO

IF NOT EXISTS (
    SELECT 1 FROM sys.indexes
     WHERE name = 'idx_prm_fk_rpt'
       AND object_id = OBJECT_ID('dbo.promemoria')
)
    CREATE INDEX idx_prm_fk_rpt ON promemoria (id_rpt);
GO

IF NOT EXISTS (
    SELECT 1 FROM sys.indexes
     WHERE name = 'idx_prm_fk_vrs'
       AND object_id = OBJECT_ID('dbo.promemoria')
)
    CREATE INDEX idx_prm_fk_vrs ON promemoria (id_versamento);
GO

IF NOT EXISTS (
    SELECT 1 FROM sys.indexes
     WHERE name = 'idx_prm_fk_doc'
       AND object_id = OBJECT_ID('dbo.promemoria')
)
    CREATE INDEX idx_prm_fk_doc ON promemoria (id_documento);
GO

IF NOT EXISTS (
    SELECT 1 FROM sys.indexes
     WHERE name = 'idx_ope_fk_trac'
       AND object_id = OBJECT_ID('dbo.operazioni')
)
    CREATE INDEX idx_ope_fk_trac ON operazioni (id_tracciato);
GO

IF NOT EXISTS (
    SELECT 1 FROM sys.indexes
     WHERE name = 'idx_ope_fk_vrs'
       AND object_id = OBJECT_ID('dbo.operazioni')
)
    CREATE INDEX idx_ope_fk_vrs ON operazioni (id_versamento);
GO

IF NOT EXISTS (
    SELECT 1 FROM sys.indexes
     WHERE name = 'idx_ope_fk_stm'
       AND object_id = OBJECT_ID('dbo.operazioni')
)
    CREATE INDEX idx_ope_fk_stm ON operazioni (id_stampa);
GO

IF NOT EXISTS (
    SELECT 1 FROM sys.indexes
     WHERE name = 'idx_all_fk_vrs'
       AND object_id = OBJECT_ID('dbo.allegati')
)
    CREATE INDEX idx_all_fk_vrs ON allegati (id_versamento);
GO

IF NOT EXISTS (
    SELECT 1 FROM sys.indexes
     WHERE name = 'idx_pag_fk_inc'
       AND object_id = OBJECT_ID('dbo.pagamenti')
)
    CREATE INDEX idx_pag_fk_inc ON pagamenti (id_incasso);
GO

IF NOT EXISTS (
    SELECT 1 FROM sys.indexes
     WHERE name = 'idx_fr_fk_inc'
       AND object_id = OBJECT_ID('dbo.fr')
)
    CREATE INDEX idx_fr_fk_inc ON fr (id_incasso);
GO

IF NOT EXISTS (
    SELECT 1 FROM sys.indexes
     WHERE name = 'idx_vrs_fk_doc'
       AND object_id = OBJECT_ID('dbo.versamenti')
)
    CREATE INDEX idx_vrs_fk_doc ON versamenti (id_documento);
GO

IF NOT EXISTS (
    SELECT 1 FROM sys.indexes
     WHERE name = 'idx_stm_fk_doc'
       AND object_id = OBJECT_ID('dbo.stampe')
)
    CREATE INDEX idx_stm_fk_doc ON stampe (id_documento);
GO
