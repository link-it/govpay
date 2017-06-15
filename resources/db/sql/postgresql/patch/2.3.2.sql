CREATE SEQUENCE seq_incassi start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE incassi
(
       trn VARCHAR(35) NOT NULL,
       cod_dominio VARCHAR(35) NOT NULL,
       causale VARCHAR(512) NOT NULL,
       importo DOUBLE PRECISION NOT NULL,
       data_valuta DATE,
       data_contabile DATE,
       data_ora_incasso TIMESTAMP NOT NULL,
       nome_dispositivo VARCHAR(512),
       -- fk/pk columns
       id BIGINT DEFAULT nextval('seq_incassi') NOT NULL,
       id_applicazione BIGINT,
       -- fk/pk keys constraints
       CONSTRAINT fk_incassi_1 FOREIGN KEY (id_applicazione) REFERENCES applicazioni(id),
       CONSTRAINT pk_incassi PRIMARY KEY (id)
);

ALTER TABLE pagamenti ADD COLUMN stato VARCHAR(35);
UPDATE pagamenti set stato = 'PAGATO';
ALTER TABLE pagamenti ADD COLUMN id_incasso BIGINT;
ALTER TABLE pagamenti ADD CONSTRAINT fk_pagamenti_4 FOREIGN KEY (id_incasso) REFERENCES incassi(id); 
