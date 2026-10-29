-- 3.11.0

-- Issue #901: modello v3 delle posizioni debitorie di govpay-common-pendenze.
-- Nuove colonne su documenti e versamenti, nuove tabelle opzioni_pagamento e
-- soggetti_debitori. Le colonne NOT NULL mantengono il DEFAULT anche dopo la
-- patch, come in gov_pay.sql: il core v2 continua a inserire righe in documenti
-- e versamenti senza conoscerle.
-- Va eseguita con WHENEVER SQLERROR EXIT FAILURE, per fermarsi al primo errore.
-- Oracle non supporta IF NOT EXISTS: ogni statement e' in un blocco PL/SQL che
-- ignora il solo errore dell'oggetto gia' presente, come nella 3.10.0:
-- ORA-01430 colonna, ORA-00955 nome gia' in uso, ORA-02275 e ORA-02264 vincolo.

-- documenti.descrizione da 255 a 1024 caratteri, come causale_versamento:
-- govpay-common-pendenze vi riporta la causale intera dei versamenti migrati
-- senza documento.
-- Ripetere il MODIFY su una colonna gia' a 1024 non da' errore. v_versamenti,
-- che legge la colonna, viene ricompilata in coda alla patch.
ALTER TABLE documenti MODIFY (descrizione VARCHAR2(1024 CHAR));

-- Colonne della posizione debitoria. id_unita_operativa non ha FK verso uo,
-- come nella libreria.
DECLARE
    TYPE t_colonne IS TABLE OF VARCHAR2(200);
    colonne t_colonne := t_colonne(
        'id_unita_operativa NUMBER',
        'data_pubblicazione DATE',
        'notifica_send NUMBER DEFAULT 0 NOT NULL',
        'nav_notifica VARCHAR2(18 CHAR)',
        'data_ultima_modifica_aca TIMESTAMP',
        'data_ultima_comunicazione_aca TIMESTAMP',
        'data_creazione TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL',
        'data_ultimo_aggiornamento TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL',
        'versione NUMBER DEFAULT 0 NOT NULL');
BEGIN
    FOR i IN 1 .. colonne.COUNT LOOP
        BEGIN
            EXECUTE IMMEDIATE 'ALTER TABLE documenti ADD ' || colonne(i);
        EXCEPTION
            WHEN OTHERS THEN
                IF SQLCODE != -1430 THEN
                    RAISE;
                END IF;
        END;
    END LOOP;
END;
/

BEGIN
    EXECUTE IMMEDIATE 'CREATE INDEX idx_doc_app_data_creaz ON documenti (id_applicazione,data_creazione,id)';
EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE != -955 THEN
            RAISE;
        END IF;
END;
/

-- Opzioni di pagamento della posizione debitoria.
BEGIN
    EXECUTE IMMEDIATE 'CREATE SEQUENCE seq_opzioni_pagamento MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE';
EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE != -955 THEN
            RAISE;
        END IF;
END;
/

BEGIN
    EXECUTE IMMEDIATE '
CREATE TABLE opzioni_pagamento
(
	id_opzione_pagamento RAW(16) NOT NULL,
	tipologia VARCHAR2(35 CHAR) NOT NULL,
	giorni NUMBER,
	stato VARCHAR2(35 CHAR) NOT NULL,
	data_inizio_validita DATE,
	data_scadenza DATE,
	data_creazione TIMESTAMP NOT NULL,
	data_ultimo_aggiornamento TIMESTAMP NOT NULL,
	versione NUMBER NOT NULL,
	id NUMBER NOT NULL,
	id_documento NUMBER NOT NULL,
	CONSTRAINT unique_opzioni_pagamento_1 UNIQUE (id_opzione_pagamento),
	CONSTRAINT fk_opz_id_documento FOREIGN KEY (id_documento) REFERENCES documenti(id),
	CONSTRAINT pk_opzioni_pagamento PRIMARY KEY (id)
)';
EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE != -955 THEN
            RAISE;
        END IF;
END;
/

BEGIN
    EXECUTE IMMEDIATE 'CREATE INDEX idx_opz_id_documento ON opzioni_pagamento (id_documento)';
EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE != -955 THEN
            RAISE;
        END IF;
END;
/

CREATE OR REPLACE TRIGGER trg_opzioni_pagamento
BEFORE
insert on opzioni_pagamento
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_opzioni_pagamento.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;
/

-- Soggetti debitori della posizione debitoria, compreso il primo (ordine 0).
BEGIN
    EXECUTE IMMEDIATE 'CREATE SEQUENCE seq_soggetti_debitori MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE';
EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE != -955 THEN
            RAISE;
        END IF;
END;
/

BEGIN
    EXECUTE IMMEDIATE '
CREATE TABLE soggetti_debitori
(
	ordine NUMBER NOT NULL,
	tipo VARCHAR2(1 CHAR) NOT NULL,
	identificativo VARCHAR2(35 CHAR) NOT NULL,
	anagrafica VARCHAR2(70 CHAR),
	indirizzo VARCHAR2(70 CHAR),
	civico VARCHAR2(16 CHAR),
	cap VARCHAR2(16 CHAR),
	localita VARCHAR2(35 CHAR),
	provincia VARCHAR2(35 CHAR),
	nazione VARCHAR2(2 CHAR),
	email VARCHAR2(256 CHAR),
	cellulare VARCHAR2(35 CHAR),
	id NUMBER NOT NULL,
	id_documento NUMBER NOT NULL,
	CONSTRAINT unique_soggetti_debitori_1 UNIQUE (id_documento,ordine),
	CONSTRAINT fk_sgd_id_documento FOREIGN KEY (id_documento) REFERENCES documenti(id),
	CONSTRAINT pk_soggetti_debitori PRIMARY KEY (id)
)';
EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE != -955 THEN
            RAISE;
        END IF;
END;
/

BEGIN
    EXECUTE IMMEDIATE 'CREATE INDEX idx_sgd_identificativo ON soggetti_debitori (identificativo)';
EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE != -955 THEN
            RAISE;
        END IF;
END;
/

CREATE OR REPLACE TRIGGER trg_soggetti_debitori
BEFORE
insert on soggetti_debitori
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_soggetti_debitori.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;
/

-- Collegamento della pendenza all'opzione di pagamento. Le righe create dalle
-- API v2 restano senza opzione, con numero_rata al valore di DEFAULT.
BEGIN
    EXECUTE IMMEDIATE 'ALTER TABLE versamenti ADD numero_rata NUMBER DEFAULT -1 NOT NULL';
EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE != -1430 THEN
            RAISE;
        END IF;
END;
/

BEGIN
    EXECUTE IMMEDIATE 'ALTER TABLE versamenti ADD id_opzione_pagamento NUMBER';
EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE != -1430 THEN
            RAISE;
        END IF;
END;
/

BEGIN
    EXECUTE IMMEDIATE 'ALTER TABLE versamenti ADD CONSTRAINT fk_vrs_id_opzione_pagamento FOREIGN KEY (id_opzione_pagamento) REFERENCES opzioni_pagamento(id)';
EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE NOT IN (-2275, -2264) THEN
            RAISE;
        END IF;
END;
/

BEGIN
    EXECUTE IMMEDIATE 'CREATE INDEX idx_vrs_id_opz_pagamento ON versamenti (id_opzione_pagamento)';
EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE != -955 THEN
            RAISE;
        END IF;
END;
/

-- Gli ALTER TABLE su documenti e versamenti invalidano le viste che le leggono.
-- Oracle le ricompilerebbe al primo accesso; ricompilarle qui lascia lo schema
-- aggiornato tutto VALID, come uno installato da zero, e fa emergere subito un
-- eventuale errore di compilazione. compile_all FALSE: solo gli oggetti invalidi.
BEGIN
    DBMS_UTILITY.compile_schema(schema => USER, compile_all => FALSE);
END;
/

COMMIT;
