-- GP-677 (3.1.x_blocco_portale)
ALTER TABLE rpt ADD COLUMN bloccante BOOLEAN NOT NULL DEFAULT TRUE;

-- Autorizzazione tutti i domini e tutte le entrate per utenza
ALTER TABLE utenze ADD COLUMN autorizzazione_domini_star BOOLEAN NOT NULL DEFAULT FALSE;

ALTER TABLE utenze ADD COLUMN autorizzazione_tributi_star BOOLEAN NOT NULL DEFAULT FALSE;


-- 11/03/2019 (Correzione bug autorizzazione utenze_tributi)

ALTER TABLE utenze_tributi ADD COLUMN id_tipo_tributo BIGINT;
UPDATE utenze_tributi SET id_tipo_tributo = (SELECT id_tipo_tributo FROM tributi WHERE utenze_tributi.id_tributo = tributi.id);
ALTER TABLE utenze_tributi ALTER COLUMN id_tipo_tributo SET NOT NULL;
ALTER TABLE utenze_tributi DROP CONSTRAINT fk_nzt_id_tributo;
ALTER TABLE utenze_tributi DROP COLUMN id_tributo;
ALTER TABLE utenze_tributi ADD CONSTRAINT fk_nzt_id_tipo_tributo FOREIGN KEY (id_tipo_tributo) REFERENCES tipi_tributo(id);
	
