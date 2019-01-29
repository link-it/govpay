-- GP-677 (3.1.x_blocco_portale)
ALTER TABLE rpt ADD COLUMN bloccante NUMBER;
UPDATE rpt SET bloccante = 1;
alter table rpt MODIFY (bloccante NOT NULL);

-- Autorizzazione tutti i domini e tutte le entrate per utenza
ALTER TABLE utenze ADD COLUMN autorizzazione_domini_star NUMBER;
update utenze set autorizzazione_domini_star = 0;
ALTER TABLE utenze MODIFY (autorizzazione_domini_star NOT NULL);

ALTER TABLE utenze ADD COLUMN autorizzazione_tributi_star NUMBER;
update utenze set autorizzazione_tributi_star = 0;
ALTER TABLE utenze MODIFY (autorizzazione_tributi_star NOT NULL);
