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

-- 18/06/2019 Configurazione avanzata dei tipi pendenza
ALTER TABLE tipi_versamento DROP COLUMN json_schema;
ALTER TABLE tipi_versamento DROP COLUMN dati_allegati;
ALTER TABLE tipi_versamento ADD COLUMN form_tipo VARCHAR2(35 CHAR);
ALTER TABLE tipi_versamento ADD COLUMN form_definizione CLOB;
ALTER TABLE tipi_versamento ADD COLUMN validazione_definizione CLOB;
ALTER TABLE tipi_versamento ADD COLUMN trasformazione_tipo VARCHAR2(35 CHAR);
ALTER TABLE tipi_versamento ADD COLUMN trasformazione_definizione CLOB;
ALTER TABLE tipi_versamento ADD COLUMN cod_applicazione VARCHAR2(35 CHAR);
ALTER TABLE tipi_versamento ADD COLUMN promemoria_avviso_tipo VARCHAR2(35 CHAR);
ALTER TABLE tipi_versamento ADD COLUMN promemoria_avviso_pdf NUMBER NOT NULL;
ALTER TABLE tipi_versamento ADD COLUMN promemoria_avviso_oggetto CLOB;
ALTER TABLE tipi_versamento ADD COLUMN promemoria_avviso_messaggio CLOB;
ALTER TABLE tipi_versamento ADD COLUMN promemoria_ricevuta_tipo VARCHAR2(35 CHAR);
ALTER TABLE tipi_versamento ADD COLUMN promemoria_ricevuta_pdf NUMBER NOT NULL;
ALTER TABLE tipi_versamento ADD COLUMN promemoria_ricevuta_oggetto CLOB;
ALTER TABLE tipi_versamento ADD COLUMN promemoria_ricevuta_messaggio CLOB;
ALTER TABLE tipi_versamento MODIFY promemoria_avviso_pdf DEFAULT 0;
ALTER TABLE tipi_versamento MODIFY promemoria_ricevuta_pdf DEFAULT 0;

ALTER TABLE tipi_vers_domini DROP COLUMN json_schema;
ALTER TABLE tipi_vers_domini DROP COLUMN dati_allegati;
ALTER TABLE tipi_vers_domini ADD COLUMN form_tipo VARCHAR2(35 CHAR);
ALTER TABLE tipi_vers_domini ADD COLUMN form_definizione CLOB;
ALTER TABLE tipi_vers_domini ADD COLUMN validazione_definizione CLOB;
ALTER TABLE tipi_vers_domini ADD COLUMN trasformazione_tipo VARCHAR2(35 CHAR);
ALTER TABLE tipi_vers_domini ADD COLUMN trasformazione_definizione CLOB;
ALTER TABLE tipi_vers_domini ADD COLUMN cod_applicazione VARCHAR2(35 CHAR);
ALTER TABLE tipi_vers_domini ADD COLUMN promemoria_avviso_tipo VARCHAR2(35 CHAR);
ALTER TABLE tipi_vers_domini ADD COLUMN promemoria_avviso_pdf NUMBER;
ALTER TABLE tipi_vers_domini ADD COLUMN promemoria_avviso_oggetto CLOB;
ALTER TABLE tipi_vers_domini ADD COLUMN promemoria_avviso_messaggio CLOB;
ALTER TABLE tipi_vers_domini ADD COLUMN promemoria_ricevuta_tipo VARCHAR2(35 CHAR);
ALTER TABLE tipi_vers_domini ADD COLUMN promemoria_ricevuta_pdf NUMBER;
ALTER TABLE tipi_vers_domini ADD COLUMN promemoria_ricevuta_oggetto CLOB;
ALTER TABLE tipi_vers_domini ADD COLUMN promemoria_ricevuta_messaggio CLOB;


-- 24/06/2019 Tabella per la spedizione dei promemoria via mail
CREATE SEQUENCE seq_promemoria MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE promemoria
(
	tipo VARCHAR2(16 CHAR) NOT NULL,
	data_creazione TIMESTAMP NOT NULL,
	stato VARCHAR2(16 CHAR) NOT NULL,
	descrizione_stato VARCHAR2(255 CHAR),
	destinatario_to VARCHAR2(256 CHAR) NOT NULL,
	destinatario_cc VARCHAR2(256 CHAR),
	messaggio_content_type VARCHAR2(256 CHAR),
	oggetto VARCHAR2(512 CHAR),
	messaggio CLOB,
	allega_pdf NUMBER NOT NULL,
	data_aggiornamento_stato TIMESTAMP NOT NULL,
	data_prossima_spedizione TIMESTAMP NOT NULL,
	tentativi_spedizione NUMBER,
	-- fk/pk columns
	id NUMBER NOT NULL,
	id_versamento NUMBER NOT NULL,
	id_rpt NUMBER,
	-- fk/pk keys constraints
	CONSTRAINT fk_prm_id_versamento FOREIGN KEY (id_versamento) REFERENCES versamenti(id),
	CONSTRAINT fk_prm_id_rpt FOREIGN KEY (id_rpt) REFERENCES rpt(id),
	CONSTRAINT pk_promemoria PRIMARY KEY (id)
);


ALTER TABLE promemoria MODIFY allega_pdf DEFAULT 0;

CREATE TRIGGER trg_promemoria
BEFORE
insert on promemoria
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_promemoria.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;
/

insert into sonde(nome, classe, soglia_warn, soglia_error) values ('spedizione-promemoria', 'org.openspcoop2.utils.sonde.impl.SondaBatch', 86400000, 172800000);

-- 05/07/2019 Aggiunti ruoli utenza
ALTER TABLE utenze ADD COLUMN ruoli VARCHAR2(512 CHAR);

-- 05/07/2019 Aggiunte informazioni direzione e divisione alla tabella versamenti;

ALTER TABLE versamenti ADD COLUMN divisione VARCHAR2(35 CHAR);
ALTER TABLE versamenti ADD COLUMN direzione VARCHAR2(35 CHAR);

DROP view versamenti_incassi;

CREATE VIEW versamenti_incassi AS
SELECT
versamenti.id as id,
MAX(versamenti.cod_versamento_ente) as cod_versamento_ente,
MAX(versamenti.nome) as nome,
MAX(versamenti.importo_totale) as importo_totale,
versamenti.stato_versamento as stato_versamento,
MAX(versamenti.descrizione_stato) as descrizione_stato,
MAX(CASE WHEN versamenti.aggiornabile = 1 THEN 'TRUE' ELSE 'FALSE' END) AS aggiornabile,
MAX(versamenti.data_creazione) as data_creazione,
MAX(versamenti.data_validita) as data_validita,
MAX(versamenti.data_scadenza) as data_scadenza,
MAX(versamenti.data_ora_ultimo_aggiornamento) as data_ora_ultimo_aggiornamento,
MAX(versamenti.causale_versamento) as causale_versamento,
MAX(versamenti.debitore_tipo) as debitore_tipo,
versamenti.debitore_identificativo as debitore_identificativo,
MAX(versamenti.debitore_anagrafica) as debitore_anagrafica,
MAX(versamenti.debitore_indirizzo) as debitore_indirizzo,
MAX(versamenti.debitore_civico) as debitore_civico,
MAX(versamenti.debitore_cap) as debitore_cap,
MAX(versamenti.debitore_localita) as debitore_localita,
MAX(versamenti.debitore_provincia) as debitore_provincia,
MAX(versamenti.debitore_nazione) as debitore_nazione,
MAX(versamenti.debitore_email) as debitore_email,
MAX(versamenti.debitore_telefono) as debitore_telefono,
MAX(versamenti.debitore_cellulare) as debitore_cellulare,
MAX(versamenti.debitore_fax) as debitore_fax,
MAX(versamenti.tassonomia_avviso) as tassonomia_avviso,
MAX(versamenti.tassonomia) as tassonomia,
MAX(versamenti.cod_lotto) as cod_lotto,
MAX(versamenti.cod_versamento_lotto) as cod_versamento_lotto,
MAX(versamenti.cod_anno_tributario) as cod_anno_tributario,
MAX(versamenti.cod_bundlekey) as cod_bundlekey,
MAX(dbms_lob.substr(versamenti.dati_allegati)) as dati_allegati,
MAX(versamenti.incasso) as incasso,
MAX(dbms_lob.substr(versamenti.anomalie)) as anomalie,
MAX(versamenti.iuv_versamento) as iuv_versamento,
MAX(versamenti.numero_avviso) as numero_avviso,
MAX(versamenti.id_dominio) as id_dominio,
MAX(versamenti.id_tipo_versamento) AS id_tipo_versamento,
MAX(versamenti.id_tipo_versamento_dominio) AS id_tipo_versamento_dominio,
MAX(versamenti.id_uo) as id_uo,
MAX(versamenti.id_applicazione) as id_applicazione,
MAX(CASE WHEN versamenti.avvisatura_abilitata = 1 THEN 'TRUE' ELSE 'FALSE' END) AS avvisatura_abilitata,
MAX(CASE WHEN versamenti.avvisatura_da_inviare = 1 THEN 'TRUE' ELSE 'FALSE' END) AS avvisatura_da_inviare,
MAX(versamenti.avvisatura_operazione) as avvisatura_operazione,               
MAX(versamenti.avvisatura_modalita) as avvisatura_modalita,
MAX(versamenti.avvisatura_tipo_pagamento) as avvisatura_tipo_pagamento,                   
MAX(versamenti.avvisatura_cod_avvisatura) as avvisatura_cod_avvisatura,  
MAX(versamenti.divisione) as divisione,  
MAX(versamenti.direzione) as direzione,     
MAX(versamenti.id_tracciato) as id_tracciato,
MAX(CASE WHEN versamenti.ack = 1 THEN 'TRUE' ELSE 'FALSE' END) AS ack,
MAX(CASE WHEN versamenti.anomalo = 1 THEN 'TRUE' ELSE 'FALSE' END) AS anomalo,
MAX(pagamenti.data_pagamento) as data_pagamento,
SUM(CASE WHEN pagamenti.importo_pagato IS NOT NULL THEN pagamenti.importo_pagato ELSE 0 END) AS importo_pagato,
SUM(CASE WHEN pagamenti.stato = 'INCASSATO' THEN pagamenti.importo_pagato ELSE 0 END) AS importo_incassato,
MAX(CASE WHEN pagamenti.stato IS NULL THEN 'NON_PAGATO' WHEN pagamenti.stato = 'INCASSATO' THEN 'INCASSATO' ELSE 'PAGATO' END) AS stato_pagamento,
MAX(pagamenti.iuv) AS iuv_pagamento,
MAX(CASE WHEN versamenti.stato_versamento = 'NON_ESEGUITO' AND versamenti.data_validita > CURRENT_DATE THEN 0 ELSE 1 END) AS smart_order_rank,
MIN(ABS((date_to_unix_for_smart_order(CURRENT_DATE) * 1000) - (date_to_unix_for_smart_order(COALESCE(pagamenti.data_pagamento, versamenti.data_validita, versamenti.data_creazione))) *1000)) AS smart_order_date
FROM versamenti LEFT JOIN singoli_versamenti ON versamenti.id = singoli_versamenti.id_versamento LEFT join pagamenti on singoli_versamenti.id = pagamenti.id_singolo_versamento 
JOIN tipi_versamento ON tipi_versamento.id = versamenti.id_tipo_versamento 
GROUP BY versamenti.id, versamenti.debitore_identificativo, versamenti.stato_versamento;

-- 16/07/2019 Dimensione della colonna descrizione stato della tabella promemoria;
ALTER TABLE promemoria MODIFY descrizione_stato VARCHAR2(1024 CHAR);

-- 18/07/2019 Aggiunti nuovi campi alle viste riscossioni
DROP VIEW v_riscossioni;
DROP VIEW v_riscossioni_con_rpt;
DROP VIEW v_riscossioni_senza_rpt;

CREATE VIEW v_riscossioni_senza_rpt AS
SELECT fr.cod_dominio AS cod_dominio,
    rendicontazioni.iuv AS iuv,
    rendicontazioni.iur AS iur,
    fr.cod_flusso AS cod_flusso,
    fr.iur AS fr_iur,
    fr.data_regolamento AS data_regolamento,
    fr.importo_totale_pagamenti AS importo_totale_pagamenti,
    fr.numero_pagamenti AS numero_pagamenti,
    rendicontazioni.importo_pagato AS importo_pagato,
    rendicontazioni.data AS data_pagamento,
    singoli_versamenti.cod_singolo_versamento_ente AS cod_singolo_versamento_ente,
    rendicontazioni.indice_dati AS indice_dati,
    versamenti.cod_versamento_ente AS cod_versamento_ente,
    versamenti.id_applicazione AS id_applicazione,
    versamenti.debitore_identificativo AS debitore_identificativo,
    versamenti.id_tipo_versamento AS id_tipo_versamento,
    versamenti.cod_anno_tributario AS cod_anno_tributario,
    singoli_versamenti.id_tributo AS id_tributo
   FROM fr
     JOIN rendicontazioni ON rendicontazioni.id_fr = fr.id
     JOIN versamenti ON versamenti.iuv_versamento = rendicontazioni.iuv
     JOIN domini ON versamenti.id_dominio = domini.id
     JOIN singoli_versamenti ON singoli_versamenti.id_versamento = versamenti.id
  WHERE rendicontazioni.esito = 9;

CREATE VIEW v_riscossioni_con_rpt AS
SELECT pagamenti.cod_dominio AS cod_dominio,
    pagamenti.iuv AS iuv,
    pagamenti.iur AS iur,
    fr.cod_flusso AS cod_flusso,
    fr.iur AS fr_iur,
    fr.data_regolamento AS data_regolamento,
    fr.importo_totale_pagamenti AS importo_totale_pagamenti,
    fr.numero_pagamenti AS numero_pagamenti,
    pagamenti.importo_pagato AS importo_pagato,
    pagamenti.data_pagamento AS data_pagamento,
    singoli_versamenti.cod_singolo_versamento_ente AS cod_singolo_versamento_ente,
    singoli_versamenti.indice_dati AS indice_dati,
    versamenti.cod_versamento_ente AS cod_versamento_ente,
    versamenti.id_applicazione AS id_applicazione,
    versamenti.debitore_identificativo AS debitore_identificativo,
    versamenti.id_tipo_versamento AS id_tipo_versamento,
    versamenti.cod_anno_tributario AS cod_anno_tributario,
    singoli_versamenti.id_tributo AS id_tributo
   FROM pagamenti
     LEFT JOIN rendicontazioni ON rendicontazioni.id_pagamento = pagamenti.id
     LEFT JOIN fr ON rendicontazioni.id_fr = fr.id
     JOIN singoli_versamenti ON pagamenti.id_singolo_versamento = singoli_versamenti.id
     JOIN versamenti ON singoli_versamenti.id_versamento = versamenti.id; 

CREATE VIEW v_riscossioni AS
 SELECT a.cod_dominio,
    a.iuv,
    a.iur,
    a.cod_flusso,
    a.fr_iur,
    a.data_regolamento,
    a.importo_totale_pagamenti,
    a.numero_pagamenti,
    a.importo_pagato,
    a.data_pagamento,
    a.cod_singolo_versamento_ente,
    a.indice_dati,
    a.cod_versamento_ente,
    applicazioni.cod_applicazione,
    a.debitore_identificativo AS identificativo_debitore,
    a.cod_anno_tributario AS anno,
    tipi_versamento.cod_tipo_versamento,
    tipi_tributo.cod_tributo AS cod_entrata
   FROM ( SELECT v_riscossioni_senza_rpt.cod_dominio,
            v_riscossioni_senza_rpt.iuv,
            v_riscossioni_senza_rpt.iur,
            v_riscossioni_senza_rpt.cod_flusso,
            v_riscossioni_senza_rpt.fr_iur,
            v_riscossioni_senza_rpt.data_regolamento,
            v_riscossioni_senza_rpt.importo_totale_pagamenti,
            v_riscossioni_senza_rpt.numero_pagamenti,
            v_riscossioni_senza_rpt.importo_pagato,
            v_riscossioni_senza_rpt.data_pagamento,
            v_riscossioni_senza_rpt.cod_singolo_versamento_ente,
            v_riscossioni_senza_rpt.indice_dati,
            v_riscossioni_senza_rpt.cod_versamento_ente,
            v_riscossioni_senza_rpt.id_applicazione,
            v_riscossioni_senza_rpt.debitore_identificativo,
            v_riscossioni_senza_rpt.id_tipo_versamento,
            v_riscossioni_senza_rpt.cod_anno_tributario,
            v_riscossioni_senza_rpt.id_tributo
           FROM v_riscossioni_senza_rpt
        UNION
         SELECT v_riscossioni_con_rpt.cod_dominio,
            v_riscossioni_con_rpt.iuv,
            v_riscossioni_con_rpt.iur,
            v_riscossioni_con_rpt.cod_flusso,
            v_riscossioni_con_rpt.fr_iur,
            v_riscossioni_con_rpt.data_regolamento,
            v_riscossioni_con_rpt.importo_totale_pagamenti,
            v_riscossioni_con_rpt.numero_pagamenti,
            v_riscossioni_con_rpt.importo_pagato,
            v_riscossioni_con_rpt.data_pagamento,
            v_riscossioni_con_rpt.cod_singolo_versamento_ente,
            v_riscossioni_con_rpt.indice_dati,
            v_riscossioni_con_rpt.cod_versamento_ente,
            v_riscossioni_con_rpt.id_applicazione,
            v_riscossioni_con_rpt.debitore_identificativo,
            v_riscossioni_con_rpt.id_tipo_versamento,
            v_riscossioni_con_rpt.cod_anno_tributario,
            v_riscossioni_con_rpt.id_tributo
           FROM v_riscossioni_con_rpt) a
     JOIN applicazioni ON a.id_applicazione = applicazioni.id 
     JOIN tipi_versamento ON a.id_tipo_versamento = tipi_versamento.id 
     LEFT JOIN tributi ON a.id_tributo = tributi.id JOIN tipi_tributo  
     ON tributi.id_tipo_tributo = tipi_tributo.id;


-- 24/07/2019 Vista Eventi per Versamenti
CREATE VIEW v_eventi_vers AS (
	SELECT eventi.componente, 
	       eventi.ruolo,
               eventi.categoria_evento, 
               eventi.tipo_evento, 
               eventi.sottotipo_evento, 
               eventi.data, 
               eventi.intervallo, 
               eventi.esito, 
               eventi.sottotipo_esito, 
               eventi.dettaglio_esito, 
               eventi.parametri_richiesta, 
               eventi.parametri_risposta, 
               eventi.dati_pago_pa, 
               coalesce(eventi.cod_versamento_ente, versamenti.cod_versamento_ente) as cod_versamento_ente, 
               coalesce (eventi.cod_applicazione, applicazioni.cod_applicazione) as cod_applicazione, 
               eventi.iuv, 
               eventi.cod_dominio, 
               eventi.ccp, 
               eventi.id_sessione, 
               eventi.id 
               FROM eventi LEFT JOIN pagamenti_portale ON eventi.id_sessione = pagamenti_portale.id_sessione 
               LEFT JOIN pag_port_versamenti ON pagamenti_portale.id = pag_port_versamenti.id_pagamento_portale 
               LEFT JOIN versamenti ON versamenti.id = pag_port_versamenti.id_versamento 
               LEFT JOIN applicazioni ON versamenti.id_applicazione = applicazioni.id
         );


-- 26/07/2019 Aggiunti campi per definire la visualizzazione di un tipo pendenza
ALTER TABLE tipi_versamento ADD COLUMN visualizzazione_definizione CLOB;
ALTER TABLE tipi_vers_domini ADD COLUMN visualizzazione_definizione CLOB;

-- 05/08/2019 modifica alla struttura della tabella delle configurazioni
ALTER TABLE configurazione ADD COLUMN nome VARCHAR2(255 CHAR);
ALTER TABLE configurazione RENAME COLUMN giornale_eventi TO valore;
UPDATE configurazione SET nome = 'giornale_eventi';
ALTER TABLE configurazione MODIFY (nome NOT NULL);
ALTER TABLE configurazione ADD CONSTRAINT unique_configurazione_1 UNIQUE (nome);

-- 05/08/2019 aggiunti nuovi campi alla tabella tracciati per gestire il formato csv
ALTER TABLE tracciati ADD COLUMN cod_tipo_versamento VARCHAR2(35 CHAR);
ALTER TABLE tracciati ADD COLUMN formato VARCHAR2(10 CHAR);
UPDATE tracciati SET formato = 'JSON';
ALTER TABLE tracciati MODIFY (formato NOT NULL);


-- 06/08/2019 aggiunte colonne per la trasformazione del tracciato csv alla tabella tipo versamento
ALTER TABLE tipi_versamento ADD COLUMN trac_csv_header_risposta CLOB;
ALTER TABLE tipi_versamento ADD COLUMN trac_csv_template_richiesta CLOB;
ALTER TABLE tipi_versamento ADD COLUMN trac_csv_template_risposta CLOB;

ALTER TABLE tipi_vers_domini ADD COLUMN trac_csv_header_risposta CLOB;
ALTER TABLE tipi_vers_domini ADD COLUMN trac_csv_template_richiesta CLOB;
ALTER TABLE tipi_vers_domini ADD COLUMN trac_csv_template_risposta CLOB;

-- 27/08/2019 Vista Eventi per Versamenti
DROP VIEW v_eventi_vers;
CREATE VIEW v_eventi_vers AS (
        SELECT DISTINCT eventi.componente,
               eventi.ruolo,
               eventi.categoria_evento,
               eventi.tipo_evento,
               eventi.sottotipo_evento,
               eventi.data,
               eventi.intervallo,
               eventi.esito,
               eventi.sottotipo_esito,
               eventi.dettaglio_esito,
               eventi.parametri_richiesta,
               eventi.parametri_risposta,
               eventi.dati_pago_pa,
               coalesce(eventi.cod_versamento_ente, versamenti.cod_versamento_ente) as cod_versamento_ente,
               coalesce (eventi.cod_applicazione, applicazioni.cod_applicazione) as cod_applicazione,
               eventi.iuv,
               eventi.cod_dominio,
               eventi.ccp,
               eventi.id_sessione,
               eventi.id
               FROM eventi LEFT JOIN pagamenti_portale ON eventi.id_sessione = pagamenti_portale.id_sessione
               LEFT JOIN pag_port_versamenti ON pagamenti_portale.id = pag_port_versamenti.id_pagamento_portale
               LEFT JOIN versamenti ON versamenti.id = pag_port_versamenti.id_versamento
               LEFT JOIN applicazioni ON versamenti.id_applicazione = applicazioni.id
         );

