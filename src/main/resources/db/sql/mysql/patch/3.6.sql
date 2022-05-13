-- 30/08/2021 Aggiunta colonna connettore maggioli jppa alla tabella domini
ALTER TABLE domini ADD COLUMN cod_connettore_maggioli_jppa VARCHAR(255);


-- 08/09/2021 Estesa dimensione del campo tipo_evento della tabella eventi
DROP VIEW IF EXISTS v_eventi_vers;
DROP VIEW IF EXISTS v_eventi_vers_pagamenti;
DROP VIEW IF EXISTS v_eventi_vers_rendicontazioni;
DROP VIEW IF EXISTS v_eventi_vers_riconciliazioni;
DROP VIEW IF EXISTS v_eventi_vers_tracciati;

ALTER TABLE eventi MODIFY COLUMN tipo_evento VARCHAR(255);


CREATE VIEW v_eventi_vers_rendicontazioni AS (
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
               versamenti.cod_versamento_ente as cod_versamento_ente,
               applicazioni.cod_applicazione as cod_applicazione,
               eventi.iuv,
               eventi.cod_dominio,
               eventi.ccp,
               eventi.id_sessione,
               eventi.severita,
               eventi.id
        FROM eventi 
        JOIN rendicontazioni ON rendicontazioni.id_fr = eventi.id_fr
        JOIN pagamenti ON pagamenti.id = rendicontazioni.id_pagamento
        JOIN singoli_versamenti ON pagamenti.id_singolo_versamento=singoli_versamenti.id
        JOIN versamenti ON singoli_versamenti.id_versamento=versamenti.id
        JOIN applicazioni ON versamenti.id_applicazione = applicazioni.id
);

CREATE VIEW v_eventi_vers_pagamenti AS (
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
    versamenti.cod_versamento_ente,
    applicazioni.cod_applicazione,
    eventi.iuv,
    eventi.cod_dominio,
    eventi.ccp,
    eventi.id_sessione,
    eventi.severita,
    eventi.id
   FROM versamenti
     JOIN applicazioni ON versamenti.id_applicazione = applicazioni.id
     JOIN pag_port_versamenti ON versamenti.id = pag_port_versamenti.id_versamento
     JOIN pagamenti_portale ON pag_port_versamenti.id_pagamento_portale = pagamenti_portale.id
     JOIN eventi ON eventi.id_sessione = pagamenti_portale.id_sessione);

CREATE VIEW v_eventi_vers_riconciliazioni AS (
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
               versamenti.cod_versamento_ente as cod_versamento_ente,
               applicazioni.cod_applicazione as cod_applicazione,
               eventi.iuv,
               eventi.cod_dominio,
               eventi.ccp,
               eventi.id_sessione,
	       eventi.severita,
               eventi.id
        FROM eventi
        JOIN pagamenti ON pagamenti.id_incasso = eventi.id_incasso
        JOIN singoli_versamenti ON pagamenti.id_singolo_versamento=singoli_versamenti.id
        JOIN versamenti ON singoli_versamenti.id_versamento=versamenti.id
        JOIN applicazioni ON versamenti.id_applicazione = applicazioni.id
);

CREATE VIEW v_eventi_vers_tracciati AS (
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
               versamenti.cod_versamento_ente as cod_versamento_ente,
               applicazioni.cod_applicazione as cod_applicazione,
               eventi.iuv,
               eventi.cod_dominio,
               eventi.ccp,
               eventi.id_sessione,
	       eventi.severita,
               eventi.id
        FROM eventi
        JOIN operazioni ON operazioni.id_tracciato = eventi.id_tracciato
        JOIN versamenti ON operazioni.id_applicazione = versamenti.id_applicazione AND operazioni.cod_versamento_ente = versamenti.cod_versamento_ente
        JOIN applicazioni ON versamenti.id_applicazione = applicazioni.id
);

CREATE VIEW v_eventi_vers AS
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
               eventi.cod_versamento_ente,
               eventi.cod_applicazione,
               eventi.iuv,
               eventi.cod_dominio,
               eventi.ccp,
               eventi.id_sessione,
	       eventi.severita,
               eventi.id FROM eventi 
        UNION SELECT componente,
               ruolo,
               categoria_evento,
               tipo_evento,
               sottotipo_evento,
               data,
               intervallo,
               esito,
               sottotipo_esito,
               dettaglio_esito,
               parametri_richiesta,
               parametri_risposta,
               dati_pago_pa,
               cod_versamento_ente,
               cod_applicazione,
               iuv,
               cod_dominio,
               ccp,
               id_sessione,
	       severita,
               id FROM v_eventi_vers_pagamenti 
        UNION SELECT componente,
               ruolo,
               categoria_evento,
               tipo_evento,
               sottotipo_evento,
               data,
               intervallo,
               esito,
               sottotipo_esito,
               dettaglio_esito,
               parametri_richiesta,
               parametri_risposta,
               dati_pago_pa,
               cod_versamento_ente,
               cod_applicazione,
               iuv,
               cod_dominio,
               ccp,
               id_sessione,
	       severita,
               id FROM v_eventi_vers_rendicontazioni
        UNION SELECT componente,
               ruolo,
               categoria_evento,
               tipo_evento,
               sottotipo_evento,
               data,
               intervallo,
               esito,
               sottotipo_esito,
               dettaglio_esito,
               parametri_richiesta,
               parametri_risposta,
               dati_pago_pa,
               cod_versamento_ente,
               cod_applicazione,
               iuv,
               cod_dominio,
               ccp,
               id_sessione,
	       severita,
               id FROM v_eventi_vers_riconciliazioni
	    UNION SELECT componente,
               ruolo,
               categoria_evento,
               tipo_evento,
               sottotipo_evento,
               data,
               intervallo,
               esito,
               sottotipo_esito,
               dettaglio_esito,
               parametri_richiesta,
               parametri_risposta,
               dati_pago_pa,
               cod_versamento_ente,
               cod_applicazione,
               iuv,
               cod_dominio,
               ccp,
               id_sessione,
	       severita,
               id FROM v_eventi_vers_tracciati;

-- 07/04/2022 Allegati di una pendenza

CREATE TABLE allegati
(
	nome VARCHAR(255) NOT NULL,
	tipo VARCHAR(255),
	descrizione VARCHAR(255),
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
	data_creazione TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
	raw_contenuto MEDIUMBLOB,
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT,
	id_versamento BIGINT NOT NULL,
	-- fk/pk keys constraints
	CONSTRAINT fk_all_id_versamento FOREIGN KEY (id_versamento) REFERENCES versamenti(id),
	CONSTRAINT pk_allegati PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;

ALTER TABLE allegati DROP FOREIGN KEY fk_all_id_versamento;


-- 14/04/2022 estesa dimensione campo sottotipo_evento e sottotipo_esito
DROP VIEW IF EXISTS v_eventi_vers;
DROP VIEW IF EXISTS v_eventi_vers_pagamenti;
DROP VIEW IF EXISTS v_eventi_vers_rendicontazioni;
DROP VIEW IF EXISTS v_eventi_vers_riconciliazioni;
DROP VIEW IF EXISTS v_eventi_vers_tracciati;

ALTER TABLE eventi MODIFY COLUMN sottotipo_evento VARCHAR(255);
ALTER TABLE eventi MODIFY COLUMN sottotipo_esito VARCHAR(255);


CREATE VIEW v_eventi_vers_rendicontazioni AS (
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
               versamenti.cod_versamento_ente as cod_versamento_ente,
               applicazioni.cod_applicazione as cod_applicazione,
               eventi.iuv,
               eventi.cod_dominio,
               eventi.ccp,
               eventi.id_sessione,
               eventi.severita,
               eventi.id
        FROM eventi 
        JOIN rendicontazioni ON rendicontazioni.id_fr = eventi.id_fr
        JOIN pagamenti ON pagamenti.id = rendicontazioni.id_pagamento
        JOIN singoli_versamenti ON pagamenti.id_singolo_versamento=singoli_versamenti.id
        JOIN versamenti ON singoli_versamenti.id_versamento=versamenti.id
        JOIN applicazioni ON versamenti.id_applicazione = applicazioni.id
);

CREATE VIEW v_eventi_vers_pagamenti AS (
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
    versamenti.cod_versamento_ente,
    applicazioni.cod_applicazione,
    eventi.iuv,
    eventi.cod_dominio,
    eventi.ccp,
    eventi.id_sessione,
    eventi.severita,
    eventi.id
   FROM versamenti
     JOIN applicazioni ON versamenti.id_applicazione = applicazioni.id
     JOIN pag_port_versamenti ON versamenti.id = pag_port_versamenti.id_versamento
     JOIN pagamenti_portale ON pag_port_versamenti.id_pagamento_portale = pagamenti_portale.id
     JOIN eventi ON eventi.id_sessione = pagamenti_portale.id_sessione);

CREATE VIEW v_eventi_vers_riconciliazioni AS (
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
               versamenti.cod_versamento_ente as cod_versamento_ente,
               applicazioni.cod_applicazione as cod_applicazione,
               eventi.iuv,
               eventi.cod_dominio,
               eventi.ccp,
               eventi.id_sessione,
	       eventi.severita,
               eventi.id
        FROM eventi
        JOIN pagamenti ON pagamenti.id_incasso = eventi.id_incasso
        JOIN singoli_versamenti ON pagamenti.id_singolo_versamento=singoli_versamenti.id
        JOIN versamenti ON singoli_versamenti.id_versamento=versamenti.id
        JOIN applicazioni ON versamenti.id_applicazione = applicazioni.id
);

CREATE VIEW v_eventi_vers_tracciati AS (
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
               versamenti.cod_versamento_ente as cod_versamento_ente,
               applicazioni.cod_applicazione as cod_applicazione,
               eventi.iuv,
               eventi.cod_dominio,
               eventi.ccp,
               eventi.id_sessione,
	       eventi.severita,
               eventi.id
        FROM eventi
        JOIN operazioni ON operazioni.id_tracciato = eventi.id_tracciato
        JOIN versamenti ON operazioni.id_applicazione = versamenti.id_applicazione AND operazioni.cod_versamento_ente = versamenti.cod_versamento_ente
        JOIN applicazioni ON versamenti.id_applicazione = applicazioni.id
);

CREATE VIEW v_eventi_vers AS
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
               eventi.cod_versamento_ente,
               eventi.cod_applicazione,
               eventi.iuv,
               eventi.cod_dominio,
               eventi.ccp,
               eventi.id_sessione,
	       eventi.severita,
               eventi.id FROM eventi 
        UNION SELECT componente,
               ruolo,
               categoria_evento,
               tipo_evento,
               sottotipo_evento,
               data,
               intervallo,
               esito,
               sottotipo_esito,
               dettaglio_esito,
               parametri_richiesta,
               parametri_risposta,
               dati_pago_pa,
               cod_versamento_ente,
               cod_applicazione,
               iuv,
               cod_dominio,
               ccp,
               id_sessione,
	       severita,
               id FROM v_eventi_vers_pagamenti 
        UNION SELECT componente,
               ruolo,
               categoria_evento,
               tipo_evento,
               sottotipo_evento,
               data,
               intervallo,
               esito,
               sottotipo_esito,
               dettaglio_esito,
               parametri_richiesta,
               parametri_risposta,
               dati_pago_pa,
               cod_versamento_ente,
               cod_applicazione,
               iuv,
               cod_dominio,
               ccp,
               id_sessione,
	       severita,
               id FROM v_eventi_vers_rendicontazioni
        UNION SELECT componente,
               ruolo,
               categoria_evento,
               tipo_evento,
               sottotipo_evento,
               data,
               intervallo,
               esito,
               sottotipo_esito,
               dettaglio_esito,
               parametri_richiesta,
               parametri_risposta,
               dati_pago_pa,
               cod_versamento_ente,
               cod_applicazione,
               iuv,
               cod_dominio,
               ccp,
               id_sessione,
	       severita,
               id FROM v_eventi_vers_riconciliazioni
	    UNION SELECT componente,
               ruolo,
               categoria_evento,
               tipo_evento,
               sottotipo_evento,
               data,
               intervallo,
               esito,
               sottotipo_esito,
               dettaglio_esito,
               parametri_richiesta,
               parametri_risposta,
               dati_pago_pa,
               cod_versamento_ente,
               cod_applicazione,
               iuv,
               cod_dominio,
               ccp,
               id_sessione,
	       severita,
               id FROM v_eventi_vers_tracciati;




