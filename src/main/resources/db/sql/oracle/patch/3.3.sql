-- 18/12/2019 Eliminate colonne dati WISP dalla tabella PagamentiPortale.
DROP VIEW IF EXISTS v_pagamenti_portale_ext;
DROP VIEW IF EXISTS v_pag_portale_base;
DROP VIEW IF EXISTS v_pagamenti_portale;

ALTER TABLE pagamenti_portale DROP COLUMN wisp_id_dominio;
ALTER TABLE pagamenti_portale DROP COLUMN wisp_key_pa;
ALTER TABLE pagamenti_portale DROP COLUMN wisp_key_wisp;
ALTER TABLE pagamenti_portale DROP COLUMN wisp_html;

CREATE VIEW v_pagamenti_portale AS
 SELECT 
  pagamenti_portale.cod_canale,
  pagamenti_portale.nome,
  pagamenti_portale.importo,
  pagamenti_portale.versante_identificativo,
  pagamenti_portale.id_sessione,
  pagamenti_portale.id_sessione_portale,
  pagamenti_portale.id_sessione_psp,
  pagamenti_portale.stato,
  pagamenti_portale.codice_stato,
  pagamenti_portale.descrizione_stato,
  pagamenti_portale.psp_redirect_url,
  pagamenti_portale.psp_esito,
  pagamenti_portale.data_richiesta,
  pagamenti_portale.url_ritorno,
  pagamenti_portale.cod_psp,
  pagamenti_portale.tipo_versamento,
  pagamenti_portale.multi_beneficiario,
  pagamenti_portale.ack,
  pagamenti_portale.tipo,
  pagamenti_portale.principal,
  pagamenti_portale.tipo_utenza,
  pagamenti_portale.id,
  pagamenti_portale.id_applicazione,
  versamenti.debitore_identificativo as debitore_identificativo,
  versamenti.id_dominio as id_dominio, 
  versamenti.id_uo as id_uo, 
  versamenti.id_tipo_versamento as id_tipo_versamento
FROM pagamenti_portale 
JOIN pag_port_versamenti ON pagamenti_portale.id = pag_port_versamenti.id_pagamento_portale 
JOIN versamenti ON versamenti.id=pag_port_versamenti.id_versamento;


-- 08/01/2020 Aggiunte Colonne per la configurazione delle notifiche con AppIO nei TipiVersamento e TipiVersamentoDominio

ALTER TABLE tipi_versamento ADD app_io_tipo VARCHAR2(35 CHAR);
ALTER TABLE tipi_versamento ADD app_io_template_messaggio CLOB;
ALTER TABLE tipi_versamento ADD app_io_template_oggetto CLOB;

ALTER TABLE tipi_vers_domini ADD app_io_abilitato NUMBER;
ALTER TABLE tipi_vers_domini SET app_io_abilitato = 0;
ALTER TABLE tipi_vers_domini MODIFY (app_io_abilitato DEFAULT 0);
ALTER TABLE tipi_vers_domini MODIFY (app_io_abilitato NOT NULL);

ALTER TABLE tipi_vers_domini ADD app_io_api_key VARCHAR2(255 CHAR);
ALTER TABLE tipi_vers_domini ADD app_io_tipo VARCHAR2(35 CHAR);
ALTER TABLE tipi_vers_domini ADD app_io_template_messaggio CLOB;
ALTER TABLE tipi_vers_domini ADD app_io_template_oggetto CLOB;

-- 12/02/2020 Tabella Notifiche AppIO
CREATE SEQUENCE seq_notifiche_app_io MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE notifiche_app_io
(
	debitore_identificativo VARCHAR2(35 CHAR) NOT NULL,
	cod_versamento_ente VARCHAR2(35 CHAR) NOT NULL,
	cod_applicazione VARCHAR2(35 CHAR) NOT NULL,
	cod_dominio VARCHAR2(35 CHAR) NOT NULL,
	iuv VARCHAR2(35 CHAR) NOT NULL,
	tipo_esito VARCHAR2(16 CHAR) NOT NULL,
	data_creazione TIMESTAMP NOT NULL,
	stato VARCHAR2(16 CHAR) NOT NULL,
	descrizione_stato VARCHAR2(255 CHAR),
	data_aggiornamento_stato TIMESTAMP NOT NULL,
	data_prossima_spedizione TIMESTAMP NOT NULL,
	tentativi_spedizione NUMBER,
	id_messaggio VARCHAR2(255 CHAR),
	stato_messaggio VARCHAR2(16 CHAR),
	-- fk/pk columns
	id NUMBER NOT NULL,
	id_versamento NUMBER NOT NULL,
	id_tipo_versamento_dominio NUMBER NOT NULL,
	-- fk/pk keys constraints
	CONSTRAINT fk_nai_id_versamento FOREIGN KEY (id_versamento) REFERENCES versamenti(id),
	CONSTRAINT fk_nai_id_tipo_versamento_dominio FOREIGN KEY (id_tipo_versamento_dominio) REFERENCES tipi_vers_domini(id),
	CONSTRAINT pk_notifiche_app_io PRIMARY KEY (id)
);

-- index
CREATE INDEX idx_nai_da_spedire ON notifiche_app_io (stato,data_prossima_spedizione);
CREATE TRIGGER trg_notifiche_app_io
BEFORE
insert on notifiche_app_io
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_notifiche_app_io.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;
/

insert into sonde(nome, classe, soglia_warn, soglia_error) values ('update-ntfy-appio', 'org.openspcoop2.utils.sonde.impl.SondaBatch', 86400000, 172800000);
insert into sonde(nome, classe, soglia_warn, soglia_error) values ('check-ntfy-appio', 'org.openspcoop2.utils.sonde.impl.SondaCoda', 10, 100);



