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
ALTER TABLE gp_audit ADD ip_richiedente VARCHAR(45);
GO

-- Integrazione a SEND: attualizzazione dell'importo della pendenza
-- con le spese di notifica sostenute tramite SEND.
ALTER TABLE versamenti ADD send_abilitato BIT NOT NULL DEFAULT 0;
ALTER TABLE versamenti ADD send_importo_totale DECIMAL(15,2);
ALTER TABLE versamenti ADD send_data_aggiornamento DATETIME2;

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
