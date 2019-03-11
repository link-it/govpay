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



-- 11/03/2019 (Correzione bug autorizzazione utenze_tributi)
ALTER TABLE utenze_tributi ADD COLUMN id_tipo_tributo NUMBER;
UPDATE ut SET ut.id_tipo_tributo = t.id_tipo_tributo FROM utenze_tributi ut, tributi t WHERE ut.id_tributo = t.id;
ALTER TABLE utenze_tributi MODIFY (id_tipo_tributo NOT NULL);
ALTER TABLE utenze_tributi DROP CONSTRAINT fk_nzt_id_tributo;
ALTER TABLE utenze_tributi DROP COLUMN id_tributo;
ALTER TABLE utenze_tributi ADD CONSTRAINT fk_nzt_id_tipo_tributo FOREIGN KEY (id_tipo_tributo) REFERENCES tipi_tributo(id);
