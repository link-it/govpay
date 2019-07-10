-- 22/05/2019 Configurazione Giornale Eventi
UPDATE configurazione SET giornale_eventi = '{"apiEnte":{"letture":{"log":"MAI","dump":"MAI"},"scritture":{"log":"SOLO_ERRORE","dump":"SOLO_ERRORE"}},"apiPagamento":{"letture":{"log":"MAI","dump":"MAI"},"scritture":{"log":"SOLO_ERRORE","dump":"SOLO_ERRORE"}},"apiRagioneria":{"letture":{"log":"MAI","dump":"MAI"},"scritture":{"log":"SOLO_ERRORE","dump":"SOLO_ERRORE"}},"apiBackoffice":{"letture":{"log":"MAI","dump":"MAI"},"scritture":{"log":"SOLO_ERRORE","dump":"SOLO_ERRORE"}},"apiPagoPA":{"letture":{"log":"SEMPRE","dump":"SOLO_ERRORE"},"scritture":{"log":"SEMPRE","dump":"SOLO_ERRORE"}},"apiPendenze":{"letture":{"log":"MAI","dump":"MAI"},"scritture":{"log":"SOLO_ERRORE","dump":"SOLO_ERRORE"}}}';

-- 24/05/2019 nuova tabella eventi
DROP TABLE eventi;

CREATE TABLE eventi
(
	componente VARCHAR(35),
	ruolo VARCHAR(1),
	categoria_evento VARCHAR(1),
	tipo_evento VARCHAR(70),
	sottotipo_evento VARCHAR(35),
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
	data TIMESTAMP(3) DEFAULT 0,
	intervallo BIGINT,
	esito VARCHAR(4),
	sottotipo_esito VARCHAR(35),
	dettaglio_esito LONGTEXT,
	parametri_richiesta MEDIUMBLOB,
	parametri_risposta MEDIUMBLOB,
	dati_pago_pa LONGTEXT,
	cod_versamento_ente VARCHAR(35),
	cod_applicazione VARCHAR(35),
	iuv VARCHAR(35),
	ccp VARCHAR(35),
	cod_dominio VARCHAR(35),
	id_sessione VARCHAR(35),
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT,
	-- fk/pk keys constraints
	CONSTRAINT pk_eventi PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;


-- 18/06/2019 Configurazione avanzata dei tipi pendenza
ALTER TABLE tipi_versamento DROP COLUMN json_schema;
ALTER TABLE tipi_versamento DROP COLUMN dati_allegati;
ALTER TABLE tipi_versamento ADD COLUMN form_tipo VARCHAR(35);
ALTER TABLE tipi_versamento ADD COLUMN form_definizione LONGTEXT;
ALTER TABLE tipi_versamento ADD COLUMN validazione_definizione LONGTEXT;
ALTER TABLE tipi_versamento ADD COLUMN trasformazione_tipo VARCHAR(35);
ALTER TABLE tipi_versamento ADD COLUMN trasformazione_definizione LONGTEXT;
ALTER TABLE tipi_versamento ADD COLUMN cod_applicazione VARCHAR(35);
ALTER TABLE tipi_versamento ADD COLUMN promemoria_avviso_tipo VARCHAR(35);
ALTER TABLE tipi_versamento ADD COLUMN promemoria_avviso_pdf BOOLEAN NOT NULL DEFAULT false;
ALTER TABLE tipi_versamento ADD COLUMN promemoria_avviso_oggetto LONGTEXT;
ALTER TABLE tipi_versamento ADD COLUMN promemoria_avviso_messaggio LONGTEXT;
ALTER TABLE tipi_versamento ADD COLUMN promemoria_ricevuta_tipo VARCHAR(35);
ALTER TABLE tipi_versamento ADD COLUMN promemoria_ricevuta_pdf BOOLEAN NOT NULL DEFAULT false;
ALTER TABLE tipi_versamento ADD COLUMN promemoria_ricevuta_oggetto LONGTEXT;
ALTER TABLE tipi_versamento ADD COLUMN promemoria_ricevuta_messaggio LONGTEXT;

ALTER TABLE tipi_vers_domini DROP COLUMN json_schema;
ALTER TABLE tipi_vers_domini DROP COLUMN dati_allegati;
ALTER TABLE tipi_vers_domini ADD COLUMN form_tipo VARCHAR(35);
ALTER TABLE tipi_vers_domini ADD COLUMN form_definizione LONGTEXT;
ALTER TABLE tipi_vers_domini ADD COLUMN validazione_definizione LONGTEXT;
ALTER TABLE tipi_vers_domini ADD COLUMN trasformazione_tipo VARCHAR(35);
ALTER TABLE tipi_vers_domini ADD COLUMN trasformazione_definizione LONGTEXT;
ALTER TABLE tipi_vers_domini ADD COLUMN cod_applicazione VARCHAR(35);
ALTER TABLE tipi_vers_domini ADD COLUMN promemoria_avviso_tipo VARCHAR(35);
ALTER TABLE tipi_vers_domini ADD COLUMN promemoria_avviso_pdf BOOLEAN;
ALTER TABLE tipi_vers_domini ADD COLUMN promemoria_avviso_oggetto LONGTEXT;
ALTER TABLE tipi_vers_domini ADD COLUMN promemoria_avviso_messaggio LONGTEXT;
ALTER TABLE tipi_vers_domini ADD COLUMN promemoria_ricevuta_tipo VARCHAR(35);
ALTER TABLE tipi_vers_domini ADD COLUMN promemoria_ricevuta_pdf BOOLEAN;
ALTER TABLE tipi_vers_domini ADD COLUMN promemoria_ricevuta_oggetto LONGTEXT;
ALTER TABLE tipi_vers_domini ADD COLUMN promemoria_ricevuta_messaggio LONGTEXT;


-- 24/06/2019 Tabella per la spedizione dei promemoria via mail
CREATE TABLE promemoria
(
	tipo VARCHAR(16) NOT NULL,
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
	data_creazione TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
	stato VARCHAR(16) NOT NULL,
	descrizione_stato VARCHAR(255),
	destinatario_to VARCHAR(256) NOT NULL,
	destinatario_cc VARCHAR(256),
	messaggio_content_type VARCHAR(256),
	oggetto VARCHAR(512),
	messaggio LONGTEXT,
	allega_pdf BOOLEAN NOT NULL DEFAULT false,
	-- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
	data_aggiornamento_stato TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
	-- DATETIME invece che TIMESTAMP(3) per supportare la data di default 31-12-9999
	data_prossima_spedizione TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
	tentativi_spedizione BIGINT,
	-- fk/pk columns
	id BIGINT AUTO_INCREMENT,
	id_versamento BIGINT NOT NULL,
	id_rpt BIGINT,
	-- fk/pk keys constraints
	CONSTRAINT fk_ntf_id_versamento FOREIGN KEY (id_versamento) REFERENCES versamenti(id),
	CONSTRAINT fk_ntf_id_rpt FOREIGN KEY (id_rpt) REFERENCES rpt(id),
	CONSTRAINT pk_promemoria PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;

insert into sonde(nome, classe, soglia_warn, soglia_error) values ('spedizione-promemoria', 'org.openspcoop2.utils.sonde.impl.SondaBatch', 86400000, 172800000);

-- 05/07/2019 Aggiunti ruoli utenza
ALTER TABLE utenze ADD COLUMN ruoli VARCHAR(512);

-- 05/07/2019 Aggiunte informazioni direzione e divisione alla tabella versamenti;

ALTER TABLE versamenti ADD COLUMN divisione VARCHAR(35);
ALTER TABLE versamenti ADD COLUMN direzione VARCHAR(35);

DROP view versamenti_incassi;

CREATE VIEW versamenti_incassi AS SELECT
versamenti.id as id,
MAX(versamenti.cod_versamento_ente) as cod_versamento_ente,          
MAX(versamenti.nome) as nome,                         
MAX(versamenti.importo_totale) as importo_totale,               
versamenti.stato_versamento as stato_versamento,             
MAX(versamenti.descrizione_stato) as descrizione_stato,           
MAX(CASE WHEN versamenti.aggiornabile = TRUE THEN 'TRUE' ELSE 'FALSE' END) AS aggiornabile,
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
MAX(versamenti.dati_allegati) as dati_allegati,                
MAX(versamenti.incasso) as incasso,                      
MAX(versamenti.anomalie) as anomalie,                     
MAX(versamenti.iuv_versamento) as iuv_versamento,               
MAX(versamenti.numero_avviso) as numero_avviso,  
MAX(versamenti.id_tipo_versamento) as id_tipo_versamento,
MAX(versamenti.id_tipo_versamento_dominio) AS id_tipo_versamento_dominio,
MAX(versamenti.id_dominio) as id_dominio,                   
MAX(versamenti.id_uo) as id_uo,                        
MAX(versamenti.id_applicazione) as id_applicazione,             
MAX(CASE WHEN versamenti.avvisatura_abilitata = TRUE THEN 'TRUE' ELSE 'FALSE' END) AS avvisatura_abilitata,
MAX(CASE WHEN versamenti.avvisatura_da_inviare = TRUE THEN 'TRUE' ELSE 'FALSE' END) AS avvisatura_da_inviare,
MAX(versamenti.avvisatura_operazione) as avvisatura_operazione,               
MAX(versamenti.avvisatura_modalita) as avvisatura_modalita,
MAX(versamenti.avvisatura_tipo_pagamento) as avvisatura_tipo_pagamento,                   
MAX(versamenti.avvisatura_cod_avvisatura) as avvisatura_cod_avvisatura,      
MAX(versamenti.divisione) as divisione, 
MAX(versamenti.direzione) as direzione, 
MAX(versamenti.id_tracciato) as id_tracciato,
MAX(CASE WHEN versamenti.ack = TRUE THEN 'TRUE' ELSE 'FALSE' END) AS ack,
MAX(CASE WHEN versamenti.anomalo = TRUE THEN 'TRUE' ELSE 'FALSE' END) AS anomalo,
MAX(pagamenti.data_pagamento) as data_pagamento,            
SUM(CASE WHEN pagamenti.importo_pagato IS NOT NULL THEN pagamenti.importo_pagato ELSE 0 END) AS importo_pagato,
SUM(CASE WHEN pagamenti.stato = 'INCASSATO' THEN pagamenti.importo_pagato ELSE 0 END) AS importo_incassato,
MAX(CASE WHEN pagamenti.stato IS NULL THEN 'NON_PAGATO' WHEN pagamenti.stato = 'INCASSATO' THEN 'INCASSATO' ELSE 'PAGATO' END) AS stato_pagamento,
MAX(pagamenti.iuv) AS iuv_pagamento,
MAX(CASE WHEN versamenti.stato_versamento = 'NON_ESEGUITO' AND versamenti.data_validita > now() THEN 0 ELSE 1 END) AS smart_order_rank,
MIN(ABS((UNIX_TIMESTAMP(now()) *1000) - (UNIX_TIMESTAMP(COALESCE(pagamenti.data_pagamento, versamenti.data_validita, versamenti.data_creazione)) * 1000))) AS smart_order_date
FROM versamenti LEFT JOIN singoli_versamenti ON versamenti.id = singoli_versamenti.id_versamento LEFT join pagamenti on singoli_versamenti.id = pagamenti.id_singolo_versamento 
JOIN tipi_versamento ON tipi_versamento.id = versamenti.id_tipo_versamento 
GROUP BY versamenti.id, versamenti.debitore_identificativo, versamenti.stato_versamento;






