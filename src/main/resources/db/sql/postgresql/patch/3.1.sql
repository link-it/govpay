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
	
-- 13/03/2019 (Eliminazione colonna principal dalla tabella Acl e sostituzione con la foreign key verso l'utenza)
ALTER TABLE acl ADD COLUMN id_utenza BIGINT;
UPDATE acl SET id_utenza = (SELECT id FROM utenze WHERE acl.principal is not null and acl.principal = utenze.principal_originale);
ALTER TABLE acl DROP COLUMN principal;
ALTER TABLE acl ADD CONSTRAINT fk_acl_id_utenza FOREIGN KEY (id_utenza) REFERENCES utenze(id);

-- 15/03/2019 campo GLN della tabella Domini non piu' obbligatorio
ALTER TABLE domini ALTER COLUMN gln DROP NOT NULL; 

