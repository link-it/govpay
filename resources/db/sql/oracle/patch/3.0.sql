CREATE SEQUENCE seq_pagamenti_portale MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE pagamenti_portale
(
       cod_portale VARCHAR2(35 CHAR) NOT NULL,
       cod_canale VARCHAR2(35 CHAR),
       nome VARCHAR2(255 CHAR) NOT NULL,
       importo BINARY_DOUBLE NOT NULL,
       versante_identificativo VARCHAR2(35 CHAR),
       id_sessione VARCHAR2(35 CHAR) NOT NULL,
       id_sessione_portale VARCHAR2(35 CHAR),
       id_sessione_psp VARCHAR2(35 CHAR),
       stato VARCHAR2(35 CHAR) NOT NULL,
       codice_stato VARCHAR2(35 CHAR) NOT NULL,
       descrizione_stato VARCHAR2(1024 CHAR),
       psp_redirect_url VARCHAR2(1024 CHAR),
       psp_esito VARCHAR2(255 CHAR),
       json_request CLOB,
       wisp_id_dominio VARCHAR2(255 CHAR),
       wisp_key_pa VARCHAR2(255 CHAR),
       wisp_key_wisp VARCHAR2(255 CHAR),
       wisp_html CLOB,
       data_richiesta TIMESTAMP,
       url_ritorno VARCHAR2(1024 CHAR) NOT NULL,
       cod_psp VARCHAR2(35 CHAR),
       tipo_versamento VARCHAR2(4 CHAR),
       -- fk/pk columns
       id NUMBER NOT NULL,
       -- unique constraints
       CONSTRAINT unique_pagamenti_portale_1 UNIQUE (id_sessione),
       -- fk/pk keys constraints
       CONSTRAINT pk_pagamenti_portale PRIMARY KEY (id)
);

CREATE TRIGGER trg_pagamenti_portale
BEFORE
insert on pagamenti_portale
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_pagamenti_portale.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;
/



CREATE SEQUENCE seq_pag_port_versamenti MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE pag_port_versamenti
(
       -- fk/pk columns
       id NUMBER NOT NULL,
       id_pagamento_portale NUMBER NOT NULL,
       id_versamento NUMBER NOT NULL,
       -- fk/pk keys constraints
       CONSTRAINT fk_ppv_id_pagamento_portale FOREIGN KEY (id_pagamento_portale) REFERENCES pagamenti_portale(id),
       CONSTRAINT fk_ppv_id_versamento FOREIGN KEY (id_versamento) REFERENCES versamenti(id),
       CONSTRAINT pk_pag_port_versamenti PRIMARY KEY (id)
);

CREATE TRIGGER trg_pag_port_versamenti
BEFORE
insert on pag_port_versamenti
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_pag_port_versamenti.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;
/




ALTER TABLE rpt ADD id_pagamento_portale NUMBER;
ALTER TABLE rpt ADD CONSTRAINT fk_rpt_id_pagamento_portale FOREIGN KEY (id_pagamento_portale) REFERENCES pagamenti_portale(id);

ALTER TABLE versamenti ADD data_validita TIMESTAMP(3);
ALTER TABLE versamenti ADD nome VARCHAR(35);
--TODO not null
ALTER TABLE versamenti ADD tassonomia_avviso VARCHAR(35);
--TODO not null
ALTER TABLE versamenti ADD tassonomia VARCHAR(35);
ALTER TABLE versamenti ADD id_dominio BIGINT;
update versamenti set id_dominio = (select id_dominio from uo where id = versamenti.id_uo);
ALTER TABLE versamenti MODIFY (id_dominio NOT NULL);
ALTER TABLE versamenti MODIFY (id_uo NULL);
ALTER TABLE versamenti ADD CONSTRAINT fk_vrs_id_dominio FOREIGN KEY (id_dominio) REFERENCES domini(id);
ALTER TABLE versamenti ADD debitore_tipo VARCHAR2(1 CHAR);
