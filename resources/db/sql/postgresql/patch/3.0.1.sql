
CREATE SEQUENCE seq_pagamenti_portale start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE pagamenti_portale
(
       cod_portale VARCHAR(35) NOT NULL,
       cod_canale VARCHAR(35) NOT NULL,
       id_sessione VARCHAR(35) NOT NULL,
       id_sessione_portale VARCHAR(35),
       id_sessione_psp VARCHAR(35),
       stato VARCHAR(35) NOT NULL,
       psp_redirect_url VARCHAR(1024),
       psp_esito VARCHAR(255),
       json_request TEXT,
       wisp_id_dominio VARCHAR(255),
       wisp_key_pa VARCHAR(255),
       wisp_key_wisp VARCHAR(255),
       wisp_html TEXT,
       data_richiesta TIMESTAMP,
       url_ritorno VARCHAR(1024) NOT NULL,
       cod_psp VARCHAR(35),
       tipo_versamento VARCHAR(4),
       -- fk/pk columns
       id BIGINT DEFAULT nextval('seq_pagamenti_portale') NOT NULL,
       -- unique constraints
       CONSTRAINT unique_pagamenti_portale_1 UNIQUE (id_sessione),
       -- fk/pk keys constraints
       CONSTRAINT pk_pagamenti_portale PRIMARY KEY (id)
);




CREATE SEQUENCE seq_pag_port_versamenti start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE pag_port_versamenti
(
       -- fk/pk columns
       id BIGINT DEFAULT nextval('seq_pag_port_versamenti') NOT NULL,
       id_pagamento_portale BIGINT NOT NULL,
       id_versamento BIGINT NOT NULL,
       -- fk/pk keys constraints
       CONSTRAINT fk_ppv_id_pagamento_portale FOREIGN KEY (id_pagamento_portale) REFERENCES pagamenti_portale(id),
       CONSTRAINT fk_ppv_id_versamento FOREIGN KEY (id_versamento) REFERENCES versamenti(id),
       CONSTRAINT pk_pag_port_versamenti PRIMARY KEY (id)
);


ALTER TABLE rpt ADD COLUMN id_pagamento_portale BIGINT;
ALTER TABLE rpt ADD CONSTRAINT fk_rpt_id_pagamento_portale FOREIGN KEY (id_pagamento_portale) REFERENCES pagamenti_portale(id);
