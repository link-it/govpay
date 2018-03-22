--GP-603
ALTER TABLE rpt DROP COLUMN id_canale;
ALTER TABLE rpt DROP COLUMN firma_ricevuta;

ALTER TABLE rpt MODIFY COLUMN modello_pagamento VARCHAR(16) NOT NULL;

ALTER TABLE rpt ADD COLUMN cod_canale VARCHAR(35);
ALTER TABLE rpt ADD COLUMN cod_psp VARCHAR(35);
ALTER TABLE rpt ADD COLUMN cod_intermediario_psp VARCHAR(35);
ALTER TABLE rpt ADD COLUMN tipo_versamento VARCHAR(4);
ALTER TABLE rpt ADD COLUMN tipo_identificativo_attestante VARCHAR(1);
ALTER TABLE rpt ADD COLUMN identificativo_attestante VARCHAR(35);
ALTER TABLE rpt ADD COLUMN denominazione_attestante VARCHAR(70);

