-- 3.10.0

-- Rimozione connettore FTP dall'intermediario: eliminazione dei connettori
-- FTP censiti nella tabella connettori e drop della colonna cod_connettore_ftp
-- dalla tabella intermediari.
DELETE FROM connettori WHERE cod_connettore IN (
    SELECT cod_connettore_ftp FROM intermediari WHERE cod_connettore_ftp IS NOT NULL
);
GO

-- SQL Server non supporta IF EXISTS su ALTER TABLE ... DROP COLUMN prima di 2016:
-- guard con COL_LENGTH per idempotenza.
IF COL_LENGTH('intermediari', 'cod_connettore_ftp') IS NOT NULL
    ALTER TABLE intermediari DROP COLUMN cod_connettore_ftp;
GO

-- Tracciamento dell'IP del richiedente sull'audit trail.
ALTER TABLE gp_audit ADD ip_richiedente VARCHAR(45);
GO
