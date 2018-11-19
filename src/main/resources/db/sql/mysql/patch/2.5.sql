
ALTER TABLE rpt MODIFY COLUMN modello_pagamento VARCHAR(16) NOT NULL;
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
 cod_psp = (select cod_psp from canali join psp on canali.id_psp=psp.id where canali.id = rpt.id_canale),
 tipo_identificativo_attestante = CASE WHEN (select cod_psp from canali join psp on canali.id_psp=psp.id where canali.id = rpt.id_canale) LIKE 'ABI%' THEN 'A' ELSE 'B' END,
 identificativo_attestante = (select cod_psp from canali join psp on canali.id_psp=psp.id where canali.id = rpt.id_canale),
 denominazione_attestante = (select psp.ragione_sociale from canali join psp on canali.id_psp=psp.id where canali.id = rpt.id_canale);

ALTER TABLE rpt DROP COLUMN id_canale;
ALTER TABLE rpt DROP COLUMN firma_ricevuta;


