-- GP-677 (3.1.x_blocco_portale)
ALTER TABLE rpt ADD COLUMN bloccante BOOLEAN DEFAULT true;
update rpt set bloccante = true;
ALTER TABLE rpt MODIFY COLUMN bloccante BOOLEAN NOT NULL;

-- Autorizzazione tutti i domini e tutte le entrate per utenza
ALTER TABLE utenze ADD COLUMN autorizzazione_domini_star BOOLEAN DEFAULT false;
update utenze set autorizzazione_domini_star = false;
ALTER TABLE utenze MODIFY COLUMN autorizzazione_domini_star BOOLEAN NOT NULL;

ALTER TABLE utenze ADD COLUMN autorizzazione_tributi_star BOOLEAN DEFAULT false;
update utenze set autorizzazione_tributi_star = false;
ALTER TABLE utenze MODIFY COLUMN autorizzazione_tributi_star BOOLEAN NOT NULL;
