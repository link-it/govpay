CREATE TABLE pagamenti_portale
(
       cod_portale VARCHAR(35) NOT NULL,
       cod_canale VARCHAR(35),
       nome VARCHAR(255) NOT NULL,
       importo DOUBLE NOT NULL,
       versante_identificativo VARCHAR(35),
       id_sessione VARCHAR(35) NOT NULL,
       id_sessione_portale VARCHAR(35),
       id_sessione_psp VARCHAR(35),
       stato VARCHAR(35) NOT NULL,
       codice_stato VARCHAR(35) NOT NULL,
       descrizione_stato VARCHAR(1024),
       psp_redirect_url VARCHAR(1024),
       psp_esito VARCHAR(255),
       json_request LONGTEXT,
       wisp_id_dominio VARCHAR(255),
       wisp_key_pa VARCHAR(255),
       wisp_key_wisp VARCHAR(255),
       wisp_html LONGTEXT,
       -- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
       data_richiesta TIMESTAMP(3) DEFAULT 0,
       url_ritorno VARCHAR(1024) NOT NULL,
       cod_psp VARCHAR(35),
       tipo_versamento VARCHAR(4),
       -- fk/pk columns
       id BIGINT AUTO_INCREMENT,
       -- unique constraints
       CONSTRAINT unique_pagamenti_portale_1 UNIQUE (id_sessione),
       -- fk/pk keys constraints
       CONSTRAINT pk_pagamenti_portale PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;

-- index
CREATE UNIQUE INDEX index_pagamenti_portale_1 ON pagamenti_portale (id_sessione);



CREATE TABLE pag_port_versamenti
(
       -- fk/pk columns
       id BIGINT AUTO_INCREMENT,
       id_pagamento_portale BIGINT NOT NULL,
       id_versamento BIGINT NOT NULL,
       -- fk/pk keys constraints
       CONSTRAINT fk_ppv_id_pagamento_portale FOREIGN KEY (id_pagamento_portale) REFERENCES pagamenti_portale(id),
       CONSTRAINT fk_ppv_id_versamento FOREIGN KEY (id_versamento) REFERENCES versamenti(id),
       CONSTRAINT pk_pag_port_versamenti PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;

ALTER TABLE rpt ADD COLUMN id_pagamento_portale BIGINT;
ALTER TABLE rpt ADD CONSTRAINT fk_rpt_id_pagamento_portale FOREIGN KEY (id_pagamento_portale) REFERENCES pagamenti_portale(id);

ALTER TABLE versamenti ADD COLUMN data_validita TIMESTAMP(3);
ALTER TABLE versamenti ADD COLUMN nome VARCHAR(35);
--TODO not null
ALTER TABLE versamenti ADD COLUMN tassonomia_avviso VARCHAR(35);
--TODO not null
ALTER TABLE versamenti ADD COLUMN tassonomia VARCHAR(35);
ALTER TABLE versamenti ADD COLUMN id_dominio BIGINT;
update versamenti set id_dominio = (select id_dominio from uo where id = versamenti.id_uo);
ALTER TABLE versamenti MODIFY COLUMN id_dominio BIGINT NOT NULL;
ALTER TABLE versamenti ADD COLUMN debitore_tipo VARCHAR(1);


ALTER TABLE acl DROP COLUMN id_portale;

ALTER TABLE rpt DROP COLUMN id_portale;

ALTER TABLE rpt ADD COLUMN id_applicazione;
ALTER TABLE rpt ADD fk_rpt_id_applicazione FOREIGN KEY (id_applicazione) REFERENCES applicazioni(id);

DROP TABLE portali;
DROP SEQUENCE portali_seq;
