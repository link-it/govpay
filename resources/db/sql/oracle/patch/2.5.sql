--GP-603
ALTER TABLE rpt DROP COLUMN id_canale;
ALTER TABLE rpt DROP COLUMN firma_ricevuta;

ALTER TABLE rpt MODIFY (modello_pagamento NULL);

ALTER TABLE rpt ADD cod_canale VARCHAR2(35 CHAR);
ALTER TABLE rpt ADD cod_psp VARCHAR2(35 CHAR);
ALTER TABLE rpt ADD cod_intermediario_psp VARCHAR2(35 CHAR);
ALTER TABLE rpt ADD tipo_versamento VARCHAR2(4 CHAR);
ALTER TABLE rpt ADD tipo_identificativo_attestante VARCHAR2(1 CHAR);
ALTER TABLE rpt ADD identificativo_attestante VARCHAR2(35 CHAR);
ALTER TABLE rpt ADD denominazione_attestante VARCHAR2(70 CHAR);
