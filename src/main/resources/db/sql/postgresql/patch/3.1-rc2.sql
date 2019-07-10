-- 22/05/2019 Configurazione Giornale Eventi
UPDATE configurazione set giornale_eventi = '{"apiEnte":{"letture":{"log":"MAI","dump":"MAI"},"scritture":{"log":"SOLO_ERRORE","dump":"SOLO_ERRORE"}},"apiPagamento":{"letture":{"log":"MAI","dump":"MAI"},"scritture":{"log":"SOLO_ERRORE","dump":"SOLO_ERRORE"}},"apiRagioneria":{"letture":{"log":"MAI","dump":"MAI"},"scritture":{"log":"SOLO_ERRORE","dump":"SOLO_ERRORE"}},"apiBackoffice":{"letture":{"log":"MAI","dump":"MAI"},"scritture":{"log":"SOLO_ERRORE","dump":"SOLO_ERRORE"}},"apiPagoPA":{"letture":{"log":"SEMPRE","dump":"SOLO_ERRORE"},"scritture":{"log":"SEMPRE","dump":"SOLO_ERRORE"}},"apiPendenze":{"letture":{"log":"MAI","dump":"MAI"},"scritture":{"log":"SOLO_ERRORE","dump":"SOLO_ERRORE"}}}' ;

-- 24/05/2019 nuova tabella eventi
DROP TABLE eventi;


CREATE TABLE eventi
(
	componente VARCHAR(35),
	ruolo VARCHAR(1),
	categoria_evento VARCHAR(1),
	tipo_evento VARCHAR(70),
	sottotipo_evento VARCHAR(35),
	data TIMESTAMP,
	intervallo BIGINT,
	esito VARCHAR(4),
	sottotipo_esito VARCHAR(35),
	dettaglio_esito TEXT,
	parametri_richiesta BYTEA,
	parametri_risposta BYTEA,
	dati_pago_pa TEXT,
	cod_versamento_ente VARCHAR(35),
	cod_applicazione VARCHAR(35),
	iuv VARCHAR(35),
	ccp VARCHAR(35),
	cod_dominio VARCHAR(35),
	id_sessione VARCHAR(35),
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_eventi') NOT NULL,
	-- fk/pk keys constraints
	CONSTRAINT pk_eventi PRIMARY KEY (id)
);


-- 18/06/2019 Configurazione avanzata dei tipi pendenza
ALTER TABLE tipi_versamento DROP COLUMN json_schema;
ALTER TABLE tipi_versamento DROP COLUMN dati_allegati;
ALTER TABLE tipi_versamento ADD COLUMN form_tipo VARCHAR(35);
ALTER TABLE tipi_versamento ADD COLUMN form_definizione TEXT;
ALTER TABLE tipi_versamento ADD COLUMN validazione_definizione TEXT;
ALTER TABLE tipi_versamento ADD COLUMN trasformazione_tipo VARCHAR(35);
ALTER TABLE tipi_versamento ADD COLUMN trasformazione_definizione TEXT;
ALTER TABLE tipi_versamento ADD COLUMN cod_applicazione VARCHAR(35);
ALTER TABLE tipi_versamento ADD COLUMN promemoria_avviso_tipo VARCHAR(35);
ALTER TABLE tipi_versamento ADD COLUMN promemoria_avviso_pdf BOOLEAN NOT NULL DEFAULT false;
ALTER TABLE tipi_versamento ADD COLUMN promemoria_avviso_oggetto TEXT;
ALTER TABLE tipi_versamento ADD COLUMN promemoria_avviso_messaggio TEXT;
ALTER TABLE tipi_versamento ADD COLUMN promemoria_ricevuta_tipo VARCHAR(35);
ALTER TABLE tipi_versamento ADD COLUMN promemoria_ricevuta_pdf BOOLEAN NOT NULL DEFAULT false;
ALTER TABLE tipi_versamento ADD COLUMN promemoria_ricevuta_oggetto TEXT;
ALTER TABLE tipi_versamento ADD COLUMN promemoria_ricevuta_messaggio TEXT;

ALTER TABLE tipi_vers_domini DROP COLUMN json_schema;
ALTER TABLE tipi_vers_domini DROP COLUMN dati_allegati;
ALTER TABLE tipi_vers_domini ADD COLUMN form_tipo VARCHAR(35);
ALTER TABLE tipi_vers_domini ADD COLUMN form_definizione TEXT;
ALTER TABLE tipi_vers_domini ADD COLUMN validazione_definizione TEXT;
ALTER TABLE tipi_vers_domini ADD COLUMN trasformazione_tipo VARCHAR(35);
ALTER TABLE tipi_vers_domini ADD COLUMN trasformazione_definizione TEXT;
ALTER TABLE tipi_vers_domini ADD COLUMN cod_applicazione VARCHAR(35);
ALTER TABLE tipi_vers_domini ADD COLUMN promemoria_avviso_tipo VARCHAR(35);
ALTER TABLE tipi_vers_domini ADD COLUMN promemoria_avviso_pdf BOOLEAN;
ALTER TABLE tipi_vers_domini ADD COLUMN promemoria_avviso_oggetto TEXT;
ALTER TABLE tipi_vers_domini ADD COLUMN promemoria_avviso_messaggio TEXT;
ALTER TABLE tipi_vers_domini ADD COLUMN promemoria_ricevuta_tipo VARCHAR(35);
ALTER TABLE tipi_vers_domini ADD COLUMN promemoria_ricevuta_pdf BOOLEAN;
ALTER TABLE tipi_vers_domini ADD COLUMN promemoria_ricevuta_oggetto TEXT;
ALTER TABLE tipi_vers_domini ADD COLUMN promemoria_ricevuta_messaggio TEXT;


-- 24/06/2019 Tabella per la spedizione dei promemoria via mail

CREATE SEQUENCE seq_promemoria start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE promemoria
(
	tipo VARCHAR(16) NOT NULL,
	data_creazione TIMESTAMP NOT NULL,
	stato VARCHAR(16) NOT NULL,
	descrizione_stato VARCHAR(255),
	destinatario_to VARCHAR(256) NOT NULL,
	destinatario_cc VARCHAR(256),
	messaggio_content_type VARCHAR(256),
	oggetto VARCHAR(512),
	messaggio TEXT,
	allega_pdf BOOLEAN NOT NULL DEFAULT false,
	data_aggiornamento_stato TIMESTAMP NOT NULL,
	data_prossima_spedizione TIMESTAMP NOT NULL,
	tentativi_spedizione BIGINT,
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_promemoria') NOT NULL,
	id_versamento BIGINT NOT NULL,
	id_rpt BIGINT,
	-- fk/pk keys constraints
	CONSTRAINT fk_ntf_id_versamento FOREIGN KEY (id_versamento) REFERENCES versamenti(id),
	CONSTRAINT fk_ntf_id_rpt FOREIGN KEY (id_rpt) REFERENCES rpt(id),
	CONSTRAINT pk_promemoria PRIMARY KEY (id)
);

insert into sonde(nome, classe, soglia_warn, soglia_error) values ('spedizione-promemoria', 'org.openspcoop2.utils.sonde.impl.SondaBatch', 86400000, 172800000);

-- 05/07/2019 Aggiunti ruoli utenza
ALTER TABLE utenze ADD COLUMN ruoli VARCHAR(512);

-- 05/07/2019 Aggiunte informazioni direzione e divisione alla tabella versamenti;

ALTER TABLE versamenti ADD COLUMN divisione VARCHAR(35);
ALTER TABLE versamenti ADD COLUMN direzione VARCHAR(35);

DROP view versamenti_incassi;

CREATE VIEW versamenti_incassi AS SELECT versamenti.id,
    max(versamenti.cod_versamento_ente::text) AS cod_versamento_ente,
    max(versamenti.nome::text) AS nome,
    max(versamenti.importo_totale) AS importo_totale,
    versamenti.stato_versamento::text AS stato_versamento,
    max(versamenti.descrizione_stato::text) AS descrizione_stato,
    max(
        CASE
            WHEN versamenti.aggiornabile = true THEN 'TRUE'::text
            ELSE 'FALSE'::text
        END) AS aggiornabile,
    max(versamenti.data_creazione) AS data_creazione,
    max(versamenti.data_validita) AS data_validita,
    max(versamenti.data_scadenza) AS data_scadenza,
    max(versamenti.data_ora_ultimo_aggiornamento) AS data_ora_ultimo_aggiornamento,
    max(versamenti.causale_versamento::text) AS causale_versamento,
    max(versamenti.debitore_tipo::text) AS debitore_tipo,
    versamenti.debitore_identificativo AS debitore_identificativo,
    max(versamenti.debitore_anagrafica::text) AS debitore_anagrafica,
    max(versamenti.debitore_indirizzo::text) AS debitore_indirizzo,
    max(versamenti.debitore_civico::text) AS debitore_civico,
    max(versamenti.debitore_cap::text) AS debitore_cap,
    max(versamenti.debitore_localita::text) AS debitore_localita,
    max(versamenti.debitore_provincia::text) AS debitore_provincia,
    max(versamenti.debitore_nazione::text) AS debitore_nazione,
    max(versamenti.debitore_email::text) AS debitore_email,
    max(versamenti.debitore_telefono::text) AS debitore_telefono,
    max(versamenti.debitore_cellulare::text) AS debitore_cellulare,
    max(versamenti.debitore_fax::text) AS debitore_fax,
    max(versamenti.tassonomia_avviso::text) AS tassonomia_avviso,
    max(versamenti.tassonomia::text) AS tassonomia,
    max(versamenti.cod_lotto::text) AS cod_lotto,
    max(versamenti.cod_versamento_lotto::text) AS cod_versamento_lotto,
    max(versamenti.cod_anno_tributario::text) AS cod_anno_tributario,
    max(versamenti.cod_bundlekey::text) AS cod_bundlekey,
    max(versamenti.dati_allegati) AS dati_allegati,
    max(versamenti.incasso::text) AS incasso,
    max(versamenti.anomalie) AS anomalie,
    max(versamenti.iuv_versamento::text) AS iuv_versamento,
    max(versamenti.numero_avviso::text) AS numero_avviso,
    max(versamenti.id_dominio) AS id_dominio,
    max(versamenti.id_tipo_versamento) AS id_tipo_versamento,
    max(versamenti.id_tipo_versamento_dominio) AS id_tipo_versamento_dominio,
    max(versamenti.id_uo) AS id_uo,
    max(versamenti.id_applicazione) AS id_applicazione,
    MAX(CASE WHEN versamenti.avvisatura_abilitata = TRUE THEN 'TRUE' ELSE 'FALSE' END) AS avvisatura_abilitata,
    MAX(CASE WHEN versamenti.avvisatura_da_inviare = TRUE THEN 'TRUE' ELSE 'FALSE' END) AS avvisatura_da_inviare,
    MAX(versamenti.avvisatura_operazione) as avvisatura_operazione,
    MAX(versamenti.avvisatura_modalita) as avvisatura_modalita,
    MAX(versamenti.avvisatura_tipo_pagamento) as avvisatura_tipo_pagamento,
    MAX(versamenti.avvisatura_cod_avvisatura) as avvisatura_cod_avvisatura,
    MAX(versamenti.divisione) as divisione,
    MAX(versamenti.direzione) as direzione,	
    MAX(versamenti.id_tracciato) as id_tracciato,
    max(
        CASE
            WHEN versamenti.ack = true THEN 'TRUE'::text
            ELSE 'FALSE'::text
        END) AS ack,
    max(
        CASE
            WHEN versamenti.anomalo = true THEN 'TRUE'::text
            ELSE 'FALSE'::text
        END) AS anomalo,
    max(pagamenti.data_pagamento) AS data_pagamento,
    sum(
        CASE
            WHEN pagamenti.importo_pagato IS NOT NULL THEN pagamenti.importo_pagato
            ELSE 0::double precision
        END) AS importo_pagato,
    sum(
        CASE
            WHEN pagamenti.stato::text = 'INCASSATO'::text THEN pagamenti.importo_pagato
            ELSE 0::double precision
        END) AS importo_incassato,
    max(
        CASE
            WHEN pagamenti.stato IS NULL THEN 'NON_PAGATO'::text
            WHEN pagamenti.stato::text = 'INCASSATO'::text THEN 'INCASSATO'::text
            ELSE 'PAGATO'::text
        END) AS stato_pagamento,
    max(pagamenti.iuv::text) AS iuv_pagamento,
    max(
        CASE
            WHEN versamenti.stato_versamento::text = 'NON_ESEGUITO'::text AND versamenti.data_validita > now() THEN 0
            ELSE 1
        END) AS smart_order_rank,
    min(@ (date_part('epoch'::text, now()) * 1000::bigint - date_part('epoch'::text, COALESCE(pagamenti.data_pagamento, versamenti.data_validita, versamenti.data_creazione)) * 1000::bigint))::bigint AS smart_order_date
   FROM versamenti
     LEFT JOIN singoli_versamenti ON versamenti.id = singoli_versamenti.id_versamento
     LEFT JOIN pagamenti ON singoli_versamenti.id = pagamenti.id_singolo_versamento
     JOIN tipi_versamento ON tipi_versamento.id = versamenti.id_tipo_versamento
  GROUP BY versamenti.id, versamenti.debitore_identificativo, versamenti.stato_versamento;



