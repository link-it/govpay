--GP-393
ALTER TABLE applicazioni ADD COLUMN cod_applicazione_iuv VARCHAR(3);
ALTER TABLE domini ADD COLUMN aux_digit INT NOT NULL DEFAULT 0;
ALTER TABLE domini ADD COLUMN iuv_prefix VARCHAR(255);
ALTER TABLE domini ADD COLUMN iuv_prefix_strict BOOLEAN NOT NULL DEFAULT false;
ALTER TABLE tipi_tributo ADD COLUMN tipo_contabilita VARCHAR(1);
ALTER TABLE tipi_tributo ADD COLUMN cod_contabilita VARCHAR(255);
ALTER TABLE tipi_tributo ADD COLUMN cod_tributo_iuv VARCHAR(4);
ALTER TABLE tributi ADD COLUMN cod_tributo_iuv VARCHAR(4);
ALTER TABLE tributi MODIFY tipo_contabilita varchar(1);
ALTER TABLE tributi MODIFY codice_contabilita varchar(255);

--GP-406
ALTER TABLE versamenti ADD COLUMN debitore_email VARCHAR(256);
ALTER TABLE versamenti ADD COLUMN debitore_telefono VARCHAR(35);
ALTER TABLE versamenti ADD COLUMN debitore_cellulare VARCHAR(35);
ALTER TABLE versamenti ADD COLUMN debitore_fax VARCHAR(35);


--GP-439
ALTER TABLE domini ADD COLUMN segregation_code INT;

ALTER TABLE fr ADD COLUMN cod_psp VARCHAR(35);
ALTER TABLE fr ADD COLUMN cod_dominio VARCHAR(35);

UPDATE fr SET cod_psp = (SELECT cod_psp FROM psp WHERE id = id_psp);
UPDATE fr SET cod_dominio = (SELECT cod_dominio FROM domini WHERE id = id_dominio);

ALTER TABLE fr DROP COLUMN id_psp;
ALTER TABLE fr DROP COLUMN id_dominio;

ALTER TABLE pagamenti ADD COLUMN cod_dominio VARCHAR(35);
ALTER TABLE pagamenti ADD COLUMN iuv VARCHAR(35);
UPDATE pagamenti set cod_dominio = (SELECT cod_dominio from rpt where rpt.id=pagamenti.id_rpt);
UPDATE pagamenti set iuv = (SELECT iuv from rpt where rpt.id=pagamenti.id_rpt);
ALTER TABLE pagamenti MODIFY iuv VARCHAR(35) NOT NULL ;
ALTER TABLE pagamenti MODIFY cod_dominio VARCHAR(35) NOT NULL ;

ALTER TABLE pagamenti DROP COLUMN cod_singolo_versamento_ente;
INSERT INTO pagamenti (iuv, cod_dominio, importo_pagato, data_acquisizione, iur, data_pagamento, rendicontazione_data, rendicontazione_esito, id_singolo_versamento, iban_accredito, id_fr_applicazione) 
SELECT iuv, cod_dominio, importo_pagato, rendicontazione_data, iur, rendicontazione_data, rendicontazione_data, 9, id_singolo_versamento, null, id_fr_applicazione from rendicontazioni_senza_rpt, iuv, domini where rendicontazioni_senza_rpt.id_iuv = iuv.id AND iuv.id_dominio = domini.id;


CREATE TABLE rendicontazioni
(
        iuv VARCHAR(35) NOT NULL,
        iur VARCHAR(35) NOT NULL,
        importo_pagato DOUBLE,
        esito INT,
        -- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
        data TIMESTAMP(3) DEFAULT 0,
        stato VARCHAR(35) NOT NULL,
        anomalie LONGTEXT,
        -- fk/pk columns
        id BIGINT AUTO_INCREMENT,
        id_fr BIGINT NOT NULL,
        id_pagamento BIGINT,
        -- fk/pk keys constraints
        CONSTRAINT fk_rendicontazioni_1 FOREIGN KEY (id_fr) REFERENCES fr(id) ON DELETE CASCADE,
        CONSTRAINT fk_rendicontazioni_2 FOREIGN KEY (id_pagamento) REFERENCES pagamenti(id) ON DELETE CASCADE,
        CONSTRAINT pk_rendicontazioni PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;


INSERT INTO RENDICONTAZIONI (iuv, iur, importo_pagato, esito, data, stato, anomalie, id_fr, id_pagamento)
SELECT iuv, pagamenti.iur, importo_pagato, rendicontazione_esito, rendicontazione_data, 'OK', null, fr_applicazioni.id_fr, pagamenti.id from pagamenti, fr_applicazioni where pagamenti.id_fr_applicazione=fr_applicazioni.id; 

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

ALTER TABLE pagamenti MODIFY COLUMN id_singolo_versamento BIGINT NULL;

DROP TABLE rendicontazioni_senza_rpt;
DROP TABLE fr_applicazioni;

ALTER TABLE fr DROP COLUMN anno_riferimento;
ALTER TABLE fr ADD CONSTRAINT unique_fr_1 UNIQUE (cod_flusso);
ALTER TABLE iuv ADD COLUMN aux_digit INT NOT NULL DEFAULT 0;

-- GP-479
ALTER TABLE domini ADD COLUMN ndp_stato INT;
ALTER TABLE domini ADD COLUMN ndp_operazione VARCHAR(256);
ALTER TABLE domini ADD COLUMN ndp_descrizione VARCHAR(1024);

ALTER TABLE stazioni ADD COLUMN ndp_stato INT;
ALTER TABLE stazioni ADD COLUMN ndp_operazione VARCHAR(256);
ALTER TABLE stazioni ADD COLUMN ndp_descrizione VARCHAR(1024);


--GP-484
ALTER TABLE domini ADD COLUMN ndp_data TIMESTAMP(3);
ALTER TABLE stazioni ADD COLUMN ndp_data TIMESTAMP(3);
