-- 29/08/2019 Autorizzazione utenze per UO
ALTER TABLE utenze_domini ADD COLUMN id_uo BIGINT;
ALTER TABLE utenze_domini ADD CONSTRAINT fk_nzd_id_uo FOREIGN KEY (id_uo) REFERENCES uo(id);
ALTER TABLE utenze_domini ALTER COLUMN id_dominio DROP NOT NULL;

-- 26/09/2019 Identificativo univoco di creazione del versamento
ALTER TABLE versamenti ADD COLUMN id_sessione VARCHAR(35);

DROP VIEW versamenti_incassi;

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
    MAX(versamenti.id_sessione) as id_sessione,
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


-- 01/10/2019 Configurazione del controllo Captcha
INSERT INTO configurazione (NOME,VALORE) values ('it.govpay.autenticazione.utenzaAnonima.captcha.enabled','true');
INSERT INTO configurazione (NOME,VALORE) values ('it.govpay.autenticazione.utenzaAnonima.captcha.serverURL','https://www.google.com/recaptcha/api/siteverify');
INSERT INTO configurazione (NOME,VALORE) values ('it.govpay.autenticazione.utenzaAnonima.captcha.siteKey','CHANGE_ME');
INSERT INTO configurazione (NOME,VALORE) values ('it.govpay.autenticazione.utenzaAnonima.captcha.secretKey','CHANGE_ME');
INSERT INTO configurazione (NOME,VALORE) values ('it.govpay.autenticazione.utenzaAnonima.captcha.soglia','1.0');
INSERT INTO configurazione (NOME,VALORE) values ('it.govpay.autenticazione.utenzaAnonima.captcha.responseParameter','gRecaptchaResponse');


