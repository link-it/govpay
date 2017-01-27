--GP-393
ALTER TABLE applicazioni ADD cod_applicazione_iuv VARCHAR2(3 CHAR);
ALTER TABLE domini ADD aux_digit NUMBER NOT NULL;
ALTER TABLE domini ADD iuv_prefix VARCHAR2(255 CHAR);
ALTER TABLE domini ADD iuv_prefix_strict NUMBER NOT NULL;
ALTER TABLE domini MODIFY aux_digit DEFAULT 0;
ALTER TABLE domini MODIFY iuv_prefix_strict DEFAULT 0;
ALTER TABLE tipi_tributo ADD tipo_contabilita VARCHAR2(1 CHAR);
ALTER TABLE tipi_tributo ADD cod_contabilita VARCHAR2(255 CHAR);
ALTER TABLE tipi_tributo ADD cod_tributo_iuv VARCHAR2(4 CHAR);
ALTER TABLE tributi ADD cod_tributo_iuv VARCHAR2(4 CHAR);
ALTER TABLE tributi MODIFY (tipo_contabilita NULL);
ALTER TABLE tributi MODIFY (codice_contabilita NULL);

--GP-406
ALTER TABLE versamenti ADD debitore_email VARCHAR2(256 CHAR);
ALTER TABLE versamenti ADD debitore_telefono VARCHAR2(35 CHAR);
ALTER TABLE versamenti ADD debitore_cellulare VARCHAR2(35 CHAR);
ALTER TABLE versamenti ADD debitore_fax VARCHAR2(35 CHAR);

--GP-439
ALTER TABLE domini ADD segregation_code NUMBER;

ALTER TABLE fr ADD cod_psp VARCHAR(35 CHAR);
ALTER TABLE fr ADD cod_dominio VARCHAR(35 CHAR);

UPDATE fr SET cod_psp = (SELECT cod_psp FROM psp WHERE id = id_psp);
UPDATE fr SET cod_dominio = (SELECT cod_dominio FROM domini WHERE id = id_dominio);

ALTER TABLE fr DROP COLUMN id_psp;
ALTER TABLE fr DROP COLUMN id_dominio;

ALTER TABLE pagamenti ADD cod_dominio VARCHAR(35 CHAR);
ALTER TABLE pagamenti ADD iuv VARCHAR(35 CHAR);
UPDATE pagamenti set cod_dominio = (SELECT cod_dominio from rpt where rpt.id=pagamenti.id_rpt);
UPDATE pagamenti set iuv = (SELECT iuv from rpt where rpt.id=pagamenti.id_rpt);
ALTER TABLE pagamenti MODIFY (iuv NOT NULL);
ALTER TABLE pagamenti MODIFY (cod_dominio NOT NULL);

ALTER TABLE pagamenti DROP COLUMN cod_singolo_versamento_ente;
INSERT INTO pagamenti (iuv, cod_dominio, importo_pagato, data_acquisizione, iur, data_pagamento, rendicontazione_data, rendicontazione_esito, id_singolo_versamento, iban_accredito, id_fr_applicazione) 
SELECT iuv, cod_dominio, importo_pagato, rendicontazione_data, iur, rendicontazione_data, rendicontazione_data, 9, id_singolo_versamento, null, id_fr_applicazione from rendicontazioni_senza_rpt, iuv, domini where rendicontazioni_senza_rpt.id_iuv = iuv.id AND iuv.id_dominio = domini.id;

CREATE SEQUENCE seq_rendicontazioni MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE rendicontazioni
(
	iuv VARCHAR2(35 CHAR) NOT NULL,
	iur VARCHAR2(35 CHAR) NOT NULL,
	importo_pagato BINARY_DOUBLE,
	esito NUMBER,
	data TIMESTAMP,
	stato VARCHAR2(35 CHAR) NOT NULL,
	anomalie CLOB,
	-- fk/pk columns
	id NUMBER NOT NULL,
	id_fr NUMBER NOT NULL,
	id_pagamento NUMBER,
	-- fk/pk keys constraints
	CONSTRAINT fk_rendicontazioni_1 FOREIGN KEY (id_fr) REFERENCES fr(id) ON DELETE CASCADE,
	CONSTRAINT fk_rendicontazioni_2 FOREIGN KEY (id_pagamento) REFERENCES pagamenti(id) ON DELETE CASCADE,
	CONSTRAINT pk_rendicontazioni PRIMARY KEY (id)
);

CREATE TRIGGER trg_rendicontazioni
BEFORE
insert on rendicontazioni
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_rendicontazioni.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;
/


INSERT INTO RENDICONTAZIONI (iuv, iur, importo_pagato, esito, data, stato, anomalie, id_fr, id_pagamento)
SELECT iuv, pagamenti.iur, importo_pagato, rendicontazione_esito, rendicontazione_data, 'REGOLARE', null, fr_applicazioni.id_fr, pagamenti.id from pagamenti, fr_applicazioni where pagamenti.id_fr_applicazione=fr_applicazioni.id; 

ALTER TABLE pagamenti DROP COLUMN rendicontazione_esito;
ALTER TABLE pagamenti DROP COLUMN rendicontazione_data;
ALTER TABLE pagamenti DROP COLUMN codflusso_rendicontazione;
ALTER TABLE pagamenti DROP COLUMN anno_riferimento;
ALTER TABLE pagamenti DROP COLUMN indice_singolo_pagamento;
ALTER TABLE pagamenti DROP COLUMN rendicontazione_esito_revoca;
ALTER TABLE pagamenti DROP COLUMN rendicontazione_data_revoca;
ALTER TABLE pagamenti DROP COLUMN cod_flusso_rendicontaz_revoca;
ALTER TABLE pagamenti DROP COLUMN anno_riferimento_revoca;
ALTER TABLE pagamenti DROP COLUMN ind_singolo_pagamento_revoca;
ALTER TABLE pagamenti DROP COLUMN ind_singolo_pagamento_revoca;
ALTER TABLE pagamenti DROP COLUMN id_fr_applicazione;
ALTER TABLE pagamenti DROP COLUMN id_fr_applicazione_revoca;

ALTER TABLE pagamenti MODIFY (id_singolo_versamento NULL);

DROP SEQUENCE sequence_rendicontazioni_senza_rpt;
DROP TRIGGER trigger_rendicontazioni_senza_rpt;
DROP TABLE rendicontazioni_senza_rpt;
DROP SEQUENCE sequence_fr_applicazioni;
DROP TRIGGER trigger_fr_applicazioni;
DROP TABLE fr_applicazioni;

ALTER TABLE iuv ADD aux_digit NUMBER NOT NULL;
ALTER TABLE iuv MODIFY aux_digit DEFAULT 0;
