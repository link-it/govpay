-- GP-677 (3.1.x_blocco_portale)
ALTER TABLE rpt ADD COLUMN bloccante BOOLEAN NOT NULL DEFAULT TRUE;

-- Autorizzazione tutti i domini e tutte le entrate per utenza
ALTER TABLE utenze ADD COLUMN autorizzazione_domini_star BOOLEAN NOT NULL DEFAULT FALSE;

ALTER TABLE utenze ADD COLUMN autorizzazione_tributi_star BOOLEAN NOT NULL DEFAULT FALSE;
