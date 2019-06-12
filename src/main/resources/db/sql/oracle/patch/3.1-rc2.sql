-- 22/05/2019 Configurazione Giornale Eventi
UPDATE configurazione  SET giornale_eventi = '{"apiEnte":{"letture":{"log":"MAI","dump":"MAI"},"scritture":{"log":"SOLO_ERRORE","dump":"SOLO_ERRORE"}},"apiPagamento":{"letture":{"log":"MAI","dump":"MAI"},"scritture":{"log":"SOLO_ERRORE","dump":"SOLO_ERRORE"}},"apiRagioneria":{"letture":{"log":"MAI","dump":"MAI"},"scritture":{"log":"SOLO_ERRORE","dump":"SOLO_ERRORE"}},"apiBackoffice":{"letture":{"log":"MAI","dump":"MAI"},"scritture":{"log":"SOLO_ERRORE","dump":"SOLO_ERRORE"}},"apiPagoPA":{"letture":{"log":"SEMPRE","dump":"SOLO_ERRORE"},"scritture":{"log":"SEMPRE","dump":"SOLO_ERRORE"}},"apiPendenze":{"letture":{"log":"MAI","dump":"MAI"},"scritture":{"log":"SOLO_ERRORE","dump":"SOLO_ERRORE"}}}';

-- 24/05/2019 nuova tabella eventi
DROP TABLE eventi;

CREATE TABLE eventi
(
	componente VARCHAR2(35 CHAR),
	ruolo VARCHAR2(1 CHAR),
	categoria_evento VARCHAR2(1 CHAR),
	tipo_evento VARCHAR2(70 CHAR),
	sottotipo_evento VARCHAR2(35 CHAR),
	data TIMESTAMP,
	intervallo NUMBER,
	esito VARCHAR2(4 CHAR),
	sottotipo_esito VARCHAR2(35 CHAR),
	dettaglio_esito CLOB,
	parametri_richiesta BLOB,
	parametri_risposta BLOB,
	dati_pago_pa CLOB,
	cod_versamento_ente VARCHAR2(35 CHAR),
	cod_applicazione VARCHAR2(35 CHAR),
	iuv VARCHAR2(35 CHAR),
	ccp VARCHAR2(35 CHAR),
	cod_dominio VARCHAR2(35 CHAR),
	id_sessione VARCHAR2(35 CHAR),
	-- fk/pk columns
	id NUMBER NOT NULL,
	-- fk/pk keys constraints
	CONSTRAINT pk_eventi PRIMARY KEY (id)
);

CREATE TRIGGER trg_eventi
BEFORE
insert on eventi
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_eventi.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;
/



