--GP-603
ALTER TABLE rpt ALTER COLUMN modello_pagamento DROP NOT NULL;
ALTER TABLE rpt ADD COLUMN cod_canale VARCHAR(35);
ALTER TABLE rpt ADD COLUMN cod_psp VARCHAR(35);
ALTER TABLE rpt ADD COLUMN cod_intermediario_psp VARCHAR(35);
ALTER TABLE rpt ADD COLUMN tipo_versamento VARCHAR(4);
ALTER TABLE rpt ADD COLUMN tipo_identificativo_attestante VARCHAR(1);
ALTER TABLE rpt ADD COLUMN identificativo_attestante VARCHAR(35);
ALTER TABLE rpt ADD COLUMN denominazione_attestante VARCHAR(70);

UPDATE 
 rpt 
SET 
 cod_canale = (select cod_canale from canali where canali.id = rpt.id_canale), 
 tipo_versamento = (select tipo_versamento from canali where canali.id = rpt.id_canale), 
 cod_intermediario_psp = (select cod_intermediario from canali where canali.id = rpt.id_canale), 
 cod_psp = (select cod_psp from canali join psp on canali.id_psp=psp.id where canali.id = rpt.id_canale);

ALTER TABLE rpt DROP COLUMN id_canale;
ALTER TABLE rpt DROP COLUMN firma_ricevuta;

