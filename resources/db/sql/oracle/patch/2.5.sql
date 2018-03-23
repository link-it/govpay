--GP-603
ALTER TABLE rpt MODIFY (modello_pagamento NULL);
ALTER TABLE rpt ADD cod_canale VARCHAR2(35 CHAR);
ALTER TABLE rpt ADD cod_psp VARCHAR2(35 CHAR);
ALTER TABLE rpt ADD cod_intermediario_psp VARCHAR2(35 CHAR);
ALTER TABLE rpt ADD tipo_versamento VARCHAR2(4 CHAR);
ALTER TABLE rpt ADD tipo_identificativo_attestante VARCHAR2(1 CHAR);
ALTER TABLE rpt ADD identificativo_attestante VARCHAR2(35 CHAR);
ALTER TABLE rpt ADD denominazione_attestante VARCHAR2(70 CHAR);

UPDATE
 rpt
SET
 cod_canale = (select cod_canale from canali where canali.id = rpt.id_canale),
 tipo_versamento = (select tipo_versamento from canali where canali.id = rpt.id_canale),
 cod_intermediario_psp = (select cod_intermediario from canali where canali.id = rpt.id_canale),
 cod_psp = (select cod_psp from canali join psp on canali.id_psp=psp.id where canali.id = rpt.id_canale);

ALTER TABLE rpt DROP COLUMN id_canale;
ALTER TABLE rpt DROP COLUMN firma_ricevuta;

