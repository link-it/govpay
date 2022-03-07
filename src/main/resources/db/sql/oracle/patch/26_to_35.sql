--GP-557

CREATE SEQUENCE seq_avvisi MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE avvisi
(
       cod_dominio VARCHAR2(35 CHAR) NOT NULL,
       iuv VARCHAR(35) NOT NULL,
       data_creazione TIMESTAMP NOT NULL,
       stato VARCHAR2(255 CHAR) NOT NULL,
       pdf BLOB,
       -- fk/pk columns
       id NUMBER NOT NULL,
       -- fk/pk keys constraints
       CONSTRAINT pk_avvisi PRIMARY KEY (id)
);

-- index
CREATE INDEX index_avvisi_1 ON avvisi (cod_dominio,iuv);
CREATE TRIGGER trg_avvisi
BEFORE
insert on avvisi
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_avvisi.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;
/
insert into sonde(nome, classe, soglia_warn, soglia_error) values ('generazione-avvisi', 'org.openspcoop2.utils.sonde.impl.SondaBatch', 3600000, 21600000);

CREATE SEQUENCE seq_utenze MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE utenze
(
	principal VARCHAR2(255 CHAR) NOT NULL,
	abilitato NUMBER NOT NULL,
	-- fk/pk columns
	id NUMBER NOT NULL,
	-- unique constraints
	CONSTRAINT unique_utenze_1 UNIQUE (principal),
	-- fk/pk keys constraints
	CONSTRAINT pk_utenze PRIMARY KEY (id)
);

ALTER TABLE utenze MODIFY abilitato DEFAULT 1;

CREATE TRIGGER trg_utenze
BEFORE
insert on utenze
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_utenze.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;
/



CREATE SEQUENCE seq_utenze_domini MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE utenze_domini
(
	-- fk/pk columns
	id NUMBER NOT NULL,
	id_utenza NUMBER NOT NULL,
	id_dominio NUMBER NOT NULL,
	-- fk/pk keys constraints
	CONSTRAINT fk_nzd_id_utenza FOREIGN KEY (id_utenza) REFERENCES utenze(id),
	CONSTRAINT fk_nzd_id_dominio FOREIGN KEY (id_dominio) REFERENCES domini(id),
	CONSTRAINT pk_utenze_domini PRIMARY KEY (id)
);

CREATE TRIGGER trg_utenze_domini
BEFORE
insert on utenze_domini
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_utenze_domini.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;
/



CREATE SEQUENCE seq_utenze_tributi MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE utenze_tributi
(
	-- fk/pk columns
	id NUMBER NOT NULL,
	id_utenza NUMBER NOT NULL,
	id_tipo_tributo NUMBER NOT NULL,
	-- fk/pk keys constraints
	CONSTRAINT fk_nzt_id_utenza FOREIGN KEY (id_utenza) REFERENCES utenze(id),
	CONSTRAINT fk_nzt_id_tipo_tributo FOREIGN KEY (id_tipo_tributo) REFERENCES tipi_tributo(id),
	CONSTRAINT pk_utenze_tributi PRIMARY KEY (id)
);

CREATE TRIGGER trg_utenze_tributi
BEFORE
insert on utenze_tributi
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_utenze_tributi.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;
/

ALTER TABLE applicazioni ADD reg_exp VARCHAR2(1024 CHAR);

ALTER TABLE domini DROP COLUMN xml_conti_accredito;
ALTER TABLE domini DROP COLUMN xml_tabella_controparti;
ALTER TABLE domini ADD cbill VARCHAR2(255 CHAR);
ALTER TABLE uo ADD uo_area VARCHAR2(255 CHAR);
ALTER TABLE uo ADD uo_url_sito_web VARCHAR2(255 CHAR);
ALTER TABLE uo ADD uo_email VARCHAR2(255 CHAR);
ALTER TABLE uo ADD uo_pec VARCHAR2(255 CHAR);
alter table incassi add id_operatore NUMBER;
alter table incassi add CONSTRAINT fk_inc_id_operatore FOREIGN KEY (id_operatore) REFERENCES operatori(id);

-- Eliminazione tabella ruoli e salvataggio come ACL
ALTER TABLE acl ADD ruolo VARCHAR2(255 CHAR);
UPDATE acl SET ruolo = (SELECT cod_ruolo FROM ruoli WHERE ruoli.id = acl.id_ruolo AND acl.id_ruolo IS NOT NULL);
ALTER TABLE acl DROP COLUMN id_ruolo;
DROP TRIGGER trg_ruoli;
DROP TABLE ruoli;
DROP SEQUENCE seq_ruoli;


-- copio i principal di applicazioni portali e operatori nella tabella utenze
INSERT INTO utenze (principal) SELECT DISTINCT principal FROM ((SELECT principal FROM operatori) UNION (SELECT principal FROM applicazioni) UNION (SELECT principal FROM portali) );

ALTER TABLE applicazioni ADD id_utenza NUMBER;
UPDATE applicazioni SET id_utenza = (SELECT id FROM utenze WHERE principal = applicazioni.principal);
ALTER TABLE applicazioni MODIFY (id_utenza NOT NULL);

ALTER TABLE operatori ADD id_utenza NUMBER;
UPDATE operatori SET id_utenza = (SELECT id FROM utenze WHERE principal = operatori.principal);
ALTER TABLE operatori MODIFY (id_utenza NOT NULL);

ALTER TABLE portali ADD id_utenza NUMBER;
UPDATE portali set id_utenza = (SELECT id FROM utenze WHERE principal = portali.principal);

-- collego provvisoriamente portali applicazioni e operatori alle acl tramite l'id utenza
ALTER TABLE acl ADD id_utenza NUMBER;
ALTER TABLE acl ADD CONSTRAINT fk_acl_id_utenza FOREIGN KEY (id_utenza) REFERENCES utenze(id);

UPDATE acl SET id_utenza = (SELECT id_utenza FROM applicazioni WHERE acl.id_applicazione = applicazioni.id) WHERE acl.id_applicazione IS NOT NULL;
UPDATE acl SET id_utenza = (SELECT id_utenza FROM portali WHERE acl.id_portale = portali.id) WHERE acl.id_portale IS NOT NULL;
UPDATE acl SET id_utenza = (SELECT id_utenza FROM operatori WHERE acl.id_operatore = operatori.id) WHERE acl.id_operatore IS NOT NULL;

ALTER TABLE acl DROP CONSTRAINT fk_acl_id_applicazione;
ALTER TABLE acl DROP CONSTRAINT fk_acl_id_portale;
ALTER TABLE acl DROP CONSTRAINT fk_acl_id_operatore;

ALTER TABLE acl DROP COLUMN id_applicazione;
ALTER TABLE acl DROP COLUMN id_portale;
ALTER TABLE acl DROP COLUMN id_operatore;

-- trasformo la colonna diritti in stringa 
ALTER TABLE acl ADD diritti_tmp VARCHAR2(255 CHAR);

-- diritti 2 -> RW
UPDATE acl SET diritti_tmp = 'RW' WHERE diritti = 2;
-- diritti 1 -> R
UPDATE acl SET diritti_tmp = 'R' WHERE diritti = 1;
-- diritti 0 -> RW sono le acl che venivano controllate come presenti senza il check del livello
UPDATE acl SET diritti_tmp = 'RW' WHERE diritti = 0;
-- diritti null -> R
UPDATE acl SET diritti_tmp = 'R' WHERE diritti IS NULL;

-- rinomino colonna servizio
ALTER TABLE acl RENAME COLUMN cod_servizio TO servizio;

-- Conversione servizio per i ruoli

-- A_PPA -> Anagrafica PagoPA Domini, Intermediari, Stazioni, UO
UPDATE acl SET servizio = 'Anagrafica PagoPA' WHERE servizio = 'A_PPA';

-- A_CON -> Anagrafica Creditore Iban, TipiTributo, Tributi
UPDATE acl SET servizio = 'Anagrafica Creditore' WHERE servizio = 'A_CON';

-- A_APP -> Anagrafica Applicazioni  Applicazioni, Portali
UPDATE acl SET servizio = 'Anagrafica Applicazioni' WHERE servizio = 'A_APP';

-- A_USR -> Anagrafica Ruoli   Ruoli, Operatori
UPDATE acl SET servizio = 'Anagrafica Ruoli' WHERE servizio = 'A_USR';

-- GDE -> Giornale degli Eventi
UPDATE acl SET servizio = 'Giornale degli Eventi' WHERE servizio = 'GDE';

-- MAN -> Configurazione e manutenzione
UPDATE acl SET servizio = 'Configurazione e manutenzione' WHERE servizio = 'MAN';

-- R -> Rendicontazioni e Incassi
UPDATE acl SET servizio = 'Rendicontazioni e Incassi' WHERE servizio = 'R';

-- I -> Rendicontazioni e Incassi 
-- insert into acl (ruolo,servizio,diritti,id_utenza,id_dominio,id_tipo_tributo,cod_tipo) select acl.ruolo as ruolo, 'Rendicontazioni e Incassi' as servizio, acl.diritti as diritti, acl.id_utenza as id_utenza , acl.id_dominio as id_dominio, acl.id_tipo_tributo as id_tipo_tributo, acl.cod_tipo as cod_tipo from acl where acl.servizio = 'I' AND acl.ruolo IS NOT NULL AND NOT EXISTS (SELECT ruolo FROM acl WHERE ruolo IS NOT NULL AND servizio = 'Rendicontazioni e Incassi');
-- delete from acl where servizio = 'I';
UPDATE acl SET servizio = 'Rendicontazioni e Incassi' WHERE servizio = 'I';

-- STAT -> Rendicontazioni e Incassi
UPDATE acl SET servizio = 'Rendicontazioni e Incassi' WHERE servizio = 'STAT';

-- G_RND -> Ragioneria
UPDATE acl SET servizio = 'Rendicontazioni e Incassi' WHERE servizio = 'G_RND';

-- questi valori vengono impostati a 'Pagamenti e Pendenze' poi verranno corretti nella patch 3.1-rc1
-- G_PAG -> Pagamenti
UPDATE acl SET servizio = 'Pagamenti e Pendenze' WHERE servizio = 'G_PAG';

-- A -> Pagamenti
UPDATE acl SET servizio = 'Pagamenti e Pendenze' WHERE servizio = 'A';

-- O -> Pagamenti
UPDATE acl SET servizio = 'Pagamenti e Pendenze' WHERE servizio = 'O';

-- V -> Pendenze
UPDATE acl SET servizio = 'Pagamenti e Pendenze' WHERE servizio = 'V';

-- copio autorizzazzioni su id_dominio nella tabella utenze_domini
INSERT INTO utenze_domini (id_utenza, id_dominio) SELECT id_utenza,id_dominio FROM acl WHERE id_dominio IS NOT NULL and cod_tipo = 'D';
DELETE FROM acl WHERE id_dominio IS NOT NULL and cod_tipo = 'D';

-- copio autorizzazioni su id_tipo_tributo nella tabella utenze_tipi_tributo
INSERT INTO utenze_tributi (id_utenza, id_tipo_tributo) SELECT id_utenza,id_tipo_tributo FROM acl WHERE id_tipo_tributo IS NOT NULL and cod_tipo = 'T';
DELETE FROM acl WHERE id_tipo_tributo IS NOT NULL and cod_tipo = 'T';

-- ripristino nome colonna diritti
ALTER TABLE acl DROP COLUMN diritti;
ALTER TABLE acl RENAME COLUMN diritti_tmp TO diritti;
ALTER TABLE acl MODIFY (diritti VARCHAR2(255 CHAR) NOT NULL);

-- drop colonne non piu' usate
ALTER TABLE acl DROP COLUMN id_dominio;
ALTER TABLE acl DROP COLUMN id_tipo_tributo;
ALTER TABLE acl DROP COLUMN cod_tipo;
ALTER TABLE acl DROP COLUMN amministratore;

-- Eliminare eventuali duplicati per id_utenza
DELETE FROM acl WHERE ruolo IS NULL AND rowid NOT IN (SELECT MIN(rowid) FROM acl WHERE id_utenza IS NOT NULL GROUP BY servizio, diritti, id_utenza, ruolo);

-- Imposto colonna principal ed elimino riferimento id_utenza
ALTER TABLE acl ADD principal VARCHAR2(255 CHAR);
UPDATE acl SET principal = (SELECT principal FROM utenze WHERE acl.id_utenza = utenze.id);
ALTER TABLE acl DROP COLUMN id_utenza;

-- Copio i portali non censiti anche come applicazioni nella tabella applicazioni
-- questa applicazione ha il principal non coincidente con il cod_applicazione
UPDATE applicazioni SET principal = 'UNICA-PICA' WHERE cod_applicazione = 'UNICA-PICA';
UPDATE applicazioni SET id_utenza = (SELECT id FROM utenze WHERE principal = 'UNICA-PICA') WHERE cod_applicazione = 'UNICA-PICA';

INSERT INTO applicazioni (cod_applicazione, id_utenza, principal, trusted, abilitato, versione, cod_connettore_esito, cod_connettore_verifica, firma_ricevuta) SELECT portali.cod_portale, portali.id_utenza, portali.principal, portali.trusted, portali.abilitato, portali.versione, CONCAT(portali.cod_portale,'_ESITO'), CONCAT(portali.cod_portale,'_VERIFICA'), 0 FROM portali WHERE portali.id_utenza NOT IN (SELECT id_utenza FROM applicazioni);

CREATE SEQUENCE seq_pagamenti_portale MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE pagamenti_portale
(
       id_applicazione NUMBER,
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
       url_ritorno VARCHAR2(1024 CHAR),
       cod_psp VARCHAR2(35 CHAR),
       tipo_versamento VARCHAR2(4 CHAR),
       multi_beneficiario VARCHAR2(35 CHAR),
       tipo NUMBER,
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
ALTER TABLE versamenti ADD tassonomia_avviso VARCHAR(35);
ALTER TABLE versamenti ADD tassonomia VARCHAR(35);
ALTER TABLE versamenti ADD id_dominio NUMBER;
UPDATE versamenti SET id_dominio = (SELECT id_dominio FROM uo WHERE id = versamenti.id_uo);
ALTER TABLE versamenti MODIFY (id_dominio NOT NULL);
ALTER TABLE versamenti MODIFY (id_uo NULL);
ALTER TABLE versamenti ADD CONSTRAINT fk_vrs_id_dominio FOREIGN KEY (id_dominio) REFERENCES domini(id);
ALTER TABLE versamenti ADD debitore_tipo VARCHAR2(1 CHAR);

-- Collegare versamenti rpt da portali a applicazioni
ALTER TABLE rpt ADD id_applicazione NUMBER;
ALTER TABLE rpt ADD CONSTRAINT fk_rpt_id_applicazione FOREIGN KEY (id_applicazione) REFERENCES applicazioni(id);

-- Imposto id_applicazione in base al portale
UPDATE rpt SET id_applicazione = (SELECT applicazioni.id FROM applicazioni, portali WHERE applicazioni.id_utenza = portali.id_utenza AND rpt.id_portale = portali.id);

-- Le RPT sono associate tutte ad un'applicazione posso cancellare i portali
-- SEMBRA NON ESSERCI
ALTER TABLE rpt DROP CONSTRAINT fk_rpt_id_portale;

-- prima di fare questa delete controllare che id_applicazione sia stato valorizzato per tutte le rpt
ALTER TABLE rpt DROP COLUMN id_portale;

-- Collegare versamenti pagati e rpt a pagamenti portale
-- creo colonna temporanea per collegare rpt e pagamentiportale
ALTER TABLE pagamenti_portale ADD id_rpt_tmp NUMBER;

-- inserisco i pagamenti di tipo 3 tutti in stato non eseguito, aggiorno lo stato in seguito
INSERT INTO pagamenti_portale (id_rpt_tmp, id_applicazione,cod_canale,data_richiesta,cod_psp,multi_beneficiario, nome, importo, versante_identificativo, id_sessione, stato, codice_stato, tipo) 
	SELECT rpt.id, rpt.id_applicazione, rpt.cod_canale, rpt.data_msg_richiesta, rpt.cod_psp, rpt.cod_dominio, CONCAT('Pagamento Pendenza ', versamenti.cod_versamento_ente), versamenti.importo_totale, versamenti.debitore_identificativo,
	regexp_replace(rawtohex(sys_guid()), '([A-F0-9]{8})([A-F0-9]{4})([A-F0-9]{4})([A-F0-9]{4})([A-F0-9]{12})', '\1\2\3\4\5'), 'NON_ESEGUITO', 'PAGAMENTO_NON_ESEGUITO', 3 
	FROM rpt, versamenti WHERE rpt.id_versamento = versamenti.id AND rpt.cod_carrello IS NULL;

-- aggiorno riferimenti ai pagamenti portale nella tabella rpt
UPDATE rpt SET id_pagamento_portale = (SELECT pagamenti_portale.id FROM pagamenti_portale WHERE pagamenti_portale.id_rpt_tmp = rpt.id);

-- inserisco i pagamenti di tipo 1 tutti in stato non eseguito, aggiorno lo stato in seguito
INSERT INTO pagamenti_portale (id_rpt_tmp, id_applicazione,cod_canale,data_richiesta,cod_psp,multi_beneficiario, nome, importo, versante_identificativo, id_sessione, stato, codice_stato, tipo, id_sessione_portale) 
	SELECT rpt.id, rpt.id_applicazione, rpt.cod_canale, rpt.data_msg_richiesta, rpt.cod_psp, rpt.cod_dominio, CONCAT('Pagamento Pendenza ', versamenti.cod_versamento_ente), versamenti.importo_totale, versamenti.debitore_identificativo,
	rpt.cod_carrello, 'NON_ESEGUITO', 'PAGAMENTO_NON_ESEGUITO', 1, rpt.cod_sessione_portale 
	FROM rpt, versamenti WHERE rpt.id_versamento = versamenti.id AND rpt.cod_carrello IS NOT NULL AND rpt.cod_carrello NOT IN (SELECT id_sessione FROM pagamenti_portale);

-- aggiorno stati pagamento portale
UPDATE pagamenti_portale SET stato = 'ANNULLATO' WHERE pagamenti_portale.id IN (SELECT rpt.id_pagamento_portale FROM rpt WHERE rpt.id_pagamento_portale = pagamenti_portale.id AND rpt.stato = 'RPT_ANNULLATA');
UPDATE pagamenti_portale SET codice_stato = 'PAGAMENTO_IN_ATTESA_DI_ESITO' WHERE pagamenti_portale.id IN (SELECT rpt.id_pagamento_portale FROM rpt WHERE rpt.id_pagamento_portale = pagamenti_portale.id AND rpt.stato = 'RPT_ANNULLATA');

UPDATE pagamenti_portale SET stato = 'ESEGUITO' WHERE pagamenti_portale.id IN (SELECT rpt.id_pagamento_portale FROM rpt WHERE rpt.id_pagamento_portale = pagamenti_portale.id AND rpt.stato = 'RT_ACCETTATA_PA' AND rpt.cod_esito_pagamento = 0);
UPDATE pagamenti_portale SET codice_stato = 'PAGAMENTO_ESEGUITO' WHERE pagamenti_portale.id IN (SELECT rpt.id_pagamento_portale FROM rpt WHERE rpt.id_pagamento_portale = pagamenti_portale.id AND rpt.stato = 'RT_ACCETTATA_PA' AND rpt.cod_esito_pagamento = 0);

UPDATE pagamenti_portale SET stato = 'FALLITO' WHERE pagamenti_portale.id IN (SELECT rpt.id_pagamento_portale FROM rpt WHERE rpt.id_pagamento_portale = pagamenti_portale.id AND (rpt.stato = 'RPT_RIFIUTATA_NODO' OR rpt.stato = 'RPT_ERRORE_INVIO_A_NODO' OR rpt.stato = 'RPT_ERRORE_INVIO_A_PSP' OR rpt.stato = 'RPT_RIFIUTATA_PSP'));
UPDATE pagamenti_portale SET codice_stato = 'PAGAMENTO_FALLITO' WHERE pagamenti_portale.id IN (SELECT rpt.id_pagamento_portale FROM rpt WHERE rpt.id_pagamento_portale = pagamenti_portale.id AND (rpt.stato = 'RPT_RIFIUTATA_NODO' OR rpt.stato = 'RPT_ERRORE_INVIO_A_NODO' OR rpt.stato = 'RPT_ERRORE_INVIO_A_PSP' OR rpt.stato = 'RPT_RIFIUTATA_PSP'));
UPDATE pagamenti_portale SET descrizione_stato = 'Errore nella spedizione della richiesta di pagamento a pagoPA' WHERE pagamenti_portale.id IN (SELECT rpt.id_pagamento_portale FROM rpt WHERE rpt.id_pagamento_portale = pagamenti_portale.id AND (rpt.stato = 'RPT_ERRORE_INVIO_A_NODO'));

UPDATE pagamenti_portale SET stato = 'IN_CORSO' WHERE pagamenti_portale.id IN (SELECT rpt.id_pagamento_portale FROM rpt WHERE rpt.id_pagamento_portale = pagamenti_portale.id AND rpt.stato IN ('RPT_PARCHEGGIATA_NODO' , 'RPT_ATTIVATA', 'RPT_RICEVUTA_NODO', 'RPT_ACCETTATA_NODO', 'RPT_INVIATA_A_PSP', 'RPT_ACCETTATA_PSP', 'RT_RICEVUTA_NODO', 'RT_RIFIUTATA_NODO', 'RT_ACCETTATA_NODO', 'RT_RIFIUTATA_PA', 'RT_ESITO_SCONOSCIUTO_PA', 'RT_ERRORE_INVIO_A_PA', 'INTERNO_NODO'));
UPDATE pagamenti_portale SET codice_stato = 'PAGAMENTO_IN_ATTESA_DI_ESITO' WHERE pagamenti_portale.id IN (SELECT rpt.id_pagamento_portale FROM rpt WHERE rpt.id_pagamento_portale = pagamenti_portale.id AND rpt.stato IN ('RPT_PARCHEGGIATA_NODO' , 'RPT_ATTIVATA', 'RPT_RICEVUTA_NODO', 'RPT_ACCETTATA_NODO', 'RPT_INVIATA_A_PSP', 'RPT_ACCETTATA_PSP', 'RT_RICEVUTA_NODO', 'RT_RIFIUTATA_NODO', 'RT_ACCETTATA_NODO', 'RT_RIFIUTATA_PA', 'RT_ESITO_SCONOSCIUTO_PA', 'RT_ERRORE_INVIO_A_PA', 'INTERNO_NODO'));

-- aggiorno i riferimenti ai pagamenti portale per i carrelli
-- UPDATE rpt SET id_pagamento_portale = (SELECT pagamenti_portale.id FROM pagamenti_portale WHERE pagamenti_portale.id_sessione = rpt.cod_carrello AND rpt.cod_carrello IS NOT NULL);
-- sembra non ci siano carrelli con pendenze
UPDATE rpt SET id_pagamento_portale = (SELECT pagamenti_portale.id FROM pagamenti_portale WHERE pagamenti_portale.id_rpt_tmp = rpt.id);

-- elimino colonna id_rpt
ALTER TABLE pagamenti_portale DROP COLUMN id_rpt_tmp;

-- Inserimento entries nella tabella pag_port_versamenti leggendo dalla tabella rpt
INSERT INTO pag_port_versamenti (id_pagamento_portale,id_versamento) SELECT id_pagamento_portale,id_versamento FROM rpt;

-- elimino colonne inutili dalle tabelle applicazioni e operatori
ALTER TABLE applicazioni DROP COLUMN principal;
ALTER TABLE applicazioni DROP COLUMN abilitato;

-- elimino tabella portali
DROP TABLE portali;
DROP SEQUENCE seq_portali;

ALTER TABLE applicazioni ADD auto_iuv NUMBER;
UPDATE applicazioni SET auto_iuv = 1;
ALTER TABLE applicazioni MODIFY (auto_iuv NOT NULL);

ALTER TABLE domini DROP COLUMN custom_iuv;
ALTER TABLE domini DROP COLUMN iuv_prefix_strict;
ALTER TABLE domini DROP COLUMN riuso_iuv;

ALTER TABLE iban_accredito DROP COLUMN id_seller_bank;
ALTER TABLE iban_accredito DROP COLUMN id_negozio;
ALTER TABLE applicazioni DROP COLUMN versione;

ALTER TABLE versamenti add dati_allegati CLOB;
ALTER TABLE singoli_versamenti add dati_allegati CLOB;
ALTER TABLE singoli_versamenti add descrizione VARCHAR2(256 CHAR);
ALTER TABLE singoli_versamenti drop column note;

ALTER TABLE iban_accredito DROP COLUMN iban_appoggio;
ALTER TABLE iban_accredito DROP COLUMN bic_appoggio;

ALTER TABLE tributi RENAME COLUMN id_iban_accredito_postale TO id_iban_appoggio;

-- ALTER TABLE singoli_versamenti add id_iban_appoggio NUMBER;
-- ALTER TABLE singoli_versamenti add CONSTRAINT fk_sng_id_iban_appoggio FOREIGN KEY (id_iban_appoggio) REFERENCES iban_accredito(id);

ALTER TABLE incassi add iban_accredito VARCHAR2(35 CHAR);
ALTER TABLE incassi ADD CONSTRAINT unique_incassi_1 UNIQUE (cod_dominio,trn);

ALTER TABLE pagamenti ADD tipo VARCHAR2(35 CHAR);
UPDATE pagamenti SET tipo = 'ENTRATA' where iban_accredito is not null;
UPDATE pagamenti SET tipo = 'MBT' where iban_accredito is null;
ALTER TABLE pagamenti DROP COLUMN iban_accredito;
ALTER TABLE pagamenti MODIFY (tipo NOT NULL);

ALTER TABLE versamenti ADD incasso VARCHAR2(1 CHAR);
ALTER TABLE versamenti ADD anomalie CLOB;
ALTER TABLE versamenti ADD iuv_versamento VARCHAR2(35 CHAR);
ALTER TABLE versamenti ADD numero_avviso VARCHAR2(35 CHAR);
ALTER TABLE versamenti ADD avvisatura VARCHAR2(1 CHAR);
ALTER TABLE versamenti ADD tipo_pagamento NUMBER;

ALTER TABLE utenze MODIFY principal VARCHAR2(4000 CHAR);
ALTER TABLE utenze ADD principal_originale VARCHAR2(4000 CHAR);
update utenze set principal_originale = principal;
ALTER TABLE utenze MODIFY (principal_originale NOT NULL);

-- patch dati pagamenti_portale malformati non gestiti dal cruscotto

delete from pag_port_versamenti where id_pagamento_portale in (select pagamenti_portale.id from  pagamenti_portale left join rpt on rpt.id_pagamento_portale = pagamenti_portale.id where rpt.id is null);
delete from pagamenti_portale where id in (select pagamenti_portale.id from  pagamenti_portale left join rpt on rpt.id_pagamento_portale = pagamenti_portale.id where rpt.id is null);

-- fine patch dati
-- e' gia' nullable
-- ALTER TABLE versamenti MODIFY (tassonomia_avviso NULL);

ALTER TABLE pagamenti_portale ADD ack NUMBER DEFAULT 0;
ALTER TABLE pagamenti_portale MODIFY (ack NOT NULL);

ALTER TABLE pagamenti_portale ADD note CLOB;
-- ALTER TABLE pagamenti_portale ADD tipo NUMBER;
-- update pagamenti_portale set tipo = 1;

ALTER TABLE pagamenti_portale MODIFY (tipo NOT NULL);

-- e' gia' nullable
-- ALTER TABLE pagamenti_portale MODIFY (url_ritorno NULL);



ALTER TABLE intermediari ADD cod_connettore_ftp VARCHAR2(35 CHAR);

-- Eliminazione vecchi tracciati
DROP TABLE operazioni;
DROP SEQUENCE seq_operazioni;
DROP TABLE tracciati;
DROP SEQUENCE seq_tracciati;

CREATE SEQUENCE seq_tracciati MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE tracciati
(
	cod_dominio VARCHAR2(35 CHAR) NOT NULL,
        tipo VARCHAR2(10 CHAR) NOT NULL,
        stato VARCHAR2(12 CHAR) NOT NULL,
        descrizione_stato VARCHAR2(256 CHAR),
        data_caricamento TIMESTAMP NOT NULL,
        data_completamento TIMESTAMP NOT NULL,
        bean_dati CLOB,
        file_name_richiesta VARCHAR2(256 CHAR),
        raw_richiesta BLOB NOT NULL,
        file_name_esito VARCHAR2(256 CHAR),
        raw_esito BLOB NOT NULL,
        -- fk/pk columns
        id NUMBER NOT NULL,
	id_operatore NUMBER,
	-- fk/pk keys constraints
	CONSTRAINT fk_trc_id_operatore FOREIGN KEY (id_operatore) REFERENCES operatori(id),
        CONSTRAINT pk_tracciati PRIMARY KEY (id)
);

CREATE TRIGGER trg_tracciati
BEFORE
insert on tracciati
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_tracciati.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;
/


ALTER TABLE versamenti ADD da_avvisare NUMBER;
UPDATE versamenti set da_avvisare = 0;
ALTER TABLE versamenti MODIFY (da_avvisare NOT NULL);
ALTER TABLE versamenti MODIFY da_avvisare DEFAULT 0;
ALTER TABLE versamenti ADD cod_avvisatura VARCHAR2(20 CHAR);
ALTER TABLE versamenti ADD id_tracciato NUMBER;
ALTER TABLE versamenti ADD CONSTRAINT fk_vrs_id_tracciato FOREIGN KEY (id_tracciato) REFERENCES tracciati(id);


CREATE SEQUENCE seq_esiti_avvisatura MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE esiti_avvisatura
(
        cod_dominio VARCHAR2(35 CHAR) NOT NULL,
        identificativo_avvisatura VARCHAR2(20 CHAR) NOT NULL,
        tipo_canale NUMBER NOT NULL,
        cod_canale VARCHAR2(35 CHAR),
        data TIMESTAMP NOT NULL,
        cod_esito NUMBER NOT NULL,
        descrizione_esito VARCHAR2(140 CHAR) NOT NULL,
        -- fk/pk columns
        id NUMBER NOT NULL,
        id_tracciato NUMBER NOT NULL,
        -- fk/pk keys constraints
        CONSTRAINT fk_sta_id_tracciato FOREIGN KEY (id_tracciato) REFERENCES tracciati(id),
        CONSTRAINT pk_esiti_avvisatura PRIMARY KEY (id)
);

CREATE TRIGGER trg_esiti_avvisatura
BEFORE
insert on esiti_avvisatura
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_esiti_avvisatura.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;
/

insert into sonde(nome, classe, soglia_warn, soglia_error) values ('avvisatura-digitale', 'org.openspcoop2.utils.sonde.impl.SondaBatch', 3600000, 21600000);
insert into sonde(nome, classe, soglia_warn, soglia_error) values ('esito-avvisatura-digitale', 'org.openspcoop2.utils.sonde.impl.SondaBatch', 3600000, 21600000);

CREATE SEQUENCE seq_operazioni MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE operazioni
(
        tipo_operazione VARCHAR2(16 CHAR) NOT NULL,
        linea_elaborazione NUMBER NOT NULL,
        stato VARCHAR2(16 CHAR) NOT NULL,
        dati_richiesta BLOB NOT NULL,
        dati_risposta BLOB,
        dettaglio_esito VARCHAR2(255 CHAR),
        cod_versamento_ente VARCHAR2(255 CHAR),
        cod_dominio VARCHAR2(35 CHAR),
        iuv VARCHAR2(35 CHAR),
        trn VARCHAR2(35 CHAR),
        -- fk/pk columns
        id NUMBER NOT NULL,
        id_tracciato NUMBER NOT NULL,
        id_applicazione NUMBER,
        -- fk/pk keys constraints
        CONSTRAINT fk_ope_id_tracciato FOREIGN KEY (id_tracciato) REFERENCES tracciati(id),
        CONSTRAINT fk_ope_id_applicazione FOREIGN KEY (id_applicazione) REFERENCES applicazioni(id),
        CONSTRAINT pk_operazioni PRIMARY KEY (id)
);

CREATE TRIGGER trg_operazioni
BEFORE
insert on operazioni
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_operazioni.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;
/

-- controllare se sono presenti
-- insert into sonde(nome, classe, soglia_warn, soglia_error) values ('caricamento-tracciati', 'org.openspcoop2.utils.sonde.impl.SondaBatch', 3600000, 21600000);
-- insert into sonde(nome, classe, soglia_warn, soglia_error) values ('check-tracciati', 'org.openspcoop2.utils.sonde.impl.SondaCoda', 1, 1);

ALTER TABLE versamenti ADD ack NUMBER;
UPDATE versamenti set ack = 0;
ALTER TABLE versamenti MODIFY (ack NOT NULL);
ALTER TABLE versamenti MODIFY ack DEFAULT 0;
ALTER TABLE versamenti ADD note CLOB;
ALTER TABLE versamenti ADD anomalo NUMBER DEFAULT 0;
UPDATE versamenti set anomalo = 0;
ALTER TABLE versamenti MODIFY (anomalo NOT NULL);
ALTER TABLE versamenti MODIFY anomalo DEFAULT 0;

alter table singoli_versamenti add indice_dati NUMBER;
-- update singoli_versamenti sv set indice_dati = (select sb1.indice_dati from (select sv1.id as id , sv1.id_versamento as id_versamento, row_number() over (partition by sv1.id_versamento) as indice_dati from singoli_versamenti sv1) as sb1 where sb1.id = sv.id);
-- update singoli_versamenti sv set indice_dati = (select row_number() over (order by sv1.id_versamento) as indice_dati from singoli_versamenti sv1 where sv1.id = sv.id);
-- update singoli_versamenti sv set indice_dati = ( select sb1.indice_dati from ( select sv1.id as id , sv1.id_versamento as id_versamento, row_number() over (partition by sv1.id_versamento order by sv1.id) as indice_dati from singoli_versamenti sv1 ) sb1 where sb1.id = sv.id );
-- update singoli_versamenti sv set sv.indice_dati = ( with t1 as (select sv1.id as id, row_number() over (partition by sv1.id_versamento order by sv1.id) as indice_dati from singoli_versamenti sv1 ) select indice_dati from t1 where t1.id = sv.id);

CREATE TABLE sv_tmp (
	id NUMBER NOT NULL,
	indice_dati NUMBER NOT NULL
);
CREATE INDEX idx_sv_tmp_1 ON sv_tmp (id);
INSERT INTO sv_tmp (id, indice_dati) select sv1.id as id, row_number() over (partition by sv1.id_versamento order by sv1.id) as indice_dati from singoli_versamenti sv1;

UPDATE singoli_versamenti set indice_dati = (SELECT sv_tmp.indice_dati FROM sv_tmp WHERE sv_tmp.indice_dati > 1 AND singoli_versamenti.id = sv_tmp.id);

DROP INDEX idx_sv_tmp_1;
DROP TABLE sv_tmp;

alter table singoli_versamenti MODIFY (indice_dati NOT NULL);

alter table singoli_versamenti drop constraint unique_singoli_versamenti_1;
alter table singoli_versamenti add CONSTRAINT unique_singoli_versamenti_1 UNIQUE (id_versamento,cod_singolo_versamento_ente,indice_dati);

alter table rendicontazioni add id_singolo_versamento NUMBER;
alter table rendicontazioni add CONSTRAINT fk_rnd_id_singolo_versamento FOREIGN KEY (id_singolo_versamento) REFERENCES singoli_versamenti(id);
-- aggiorno entries prendendo l'id singolo_versamento dal pagamento
-- update rendicontazioni set id_singolo_versamento = (select p.id_singolo_versamento from (select r1.id as id, pagamenti.id_singolo_versamento as id_singolo_versamento from pagamenti , rendicontazioni r1 where r1.id_pagamento = pagamenti.id) as p where p.id = rendicontazioni.id) ;
update rendicontazioni set id_singolo_versamento = (select pagamenti.id_singolo_versamento from pagamenti WHERE rendicontazioni.id_pagamento = pagamenti.id);


-- Funzione per calcolare il numero di millisecondi dal 1/1/1970
CREATE OR REPLACE FUNCTION date_to_unix_for_smart_order (p_date date, in_src_tz in varchar2 default 'Europe/Rome') return number is
begin
    return round((cast((FROM_TZ(CAST(p_date as timestamp), in_src_tz) at time zone 'GMT') as date)-TO_DATE('01.01.1970','dd.mm.yyyy'))*(24*60*60));
end;
/

-- FIX bug che non valorizzava il tipo debitore
UPDATE versamenti SET debitore_tipo = 'F';

ALTER TABLE domini ADD aut_stampa_poste VARCHAR2(255 CHAR);

ALTER TABLE uo ADD uo_tel VARCHAR2(255 CHAR);
ALTER TABLE uo ADD uo_fax VARCHAR2(255 CHAR);

-- 3.0.0-RC3

ALTER TABLE tipi_tributo ADD on_line NUMBER;
UPDATE tipi_tributo SET on_line = 0;
ALTER TABLE tipi_tributo MODIFY (on_line NOT NULL);
ALTER TABLE tipi_tributo MODIFY on_line DEFAULT 0;
ALTER TABLE tipi_tributo ADD paga_terzi NUMBER;
UPDATE tipi_tributo SET paga_terzi = 0;
ALTER TABLE tipi_tributo MODIFY (paga_terzi NOT NULL);
ALTER TABLE tipi_tributo MODIFY paga_terzi DEFAULT 0;

ALTER TABLE tributi ADD on_line NUMBER;
ALTER TABLE tributi ADD paga_terzi NUMBER;

ALTER TABLE pagamenti_portale ADD principal VARCHAR2(4000 CHAR);
UPDATE pagamenti_portale pp SET principal = (SELECT u.principal FROM utenze u, applicazioni a WHERE u.id = a.id_utenza AND a.id = pp.id_applicazione);
ALTER TABLE pagamenti_portale MODIFY (principal NOT NULL);

ALTER TABLE pagamenti_portale add tipo_utenza VARCHAR2(35 CHAR);
UPDATE pagamenti_portale SET tipo_utenza = 'APPLICAZIONE';
ALTER TABLE pagamenti_portale MODIFY (tipo_utenza not null);

ALTER TABLE pagamenti_portale DROP COLUMN id_applicazione;

-- Principal Intermediario
ALTER TABLE intermediari ADD principal VARCHAR(4000);
UPDATE intermediari SET principal = (SELECT valore FROM connettori WHERE connettori.cod_proprieta = 'PRINCIPAL' AND connettori.cod_connettore = intermediari.cod_connettore_pdd);
ALTER TABLE intermediari MODIFY (principal NOT NULL);

ALTER TABLE intermediari ADD principal_originale VARCHAR(4000);
UPDATE intermediari SET principal_originale = (SELECT valore FROM connettori WHERE connettori.cod_proprieta = 'PRINCIPAL' AND connettori.cod_connettore = intermediari.cod_connettore_pdd);
ALTER TABLE intermediari MODIFY (principal_originale NOT NULL);

DELETE FROM connettori WHERE cod_proprieta = 'PRINCIPAL';

-- Eventi
DROP TABLE eventi;
DROP sequence seq_eventi;

CREATE SEQUENCE seq_eventi MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE eventi
(
        cod_dominio VARCHAR2(35 CHAR),
        iuv VARCHAR2(35 CHAR),
        ccp VARCHAR2(35 CHAR),
        categoria_evento VARCHAR2(1 CHAR),
        tipo_evento VARCHAR2(35 CHAR),
        sottotipo_evento VARCHAR2(35 CHAR),
        data TIMESTAMP,
        intervallo NUMBER,
        dettaglio CLOB,
        -- fk/pk columns
        id NUMBER NOT NULL,
        id_versamento NUMBER,
        id_pagamento_portale NUMBER,
        -- fk/pk keys constraints
        CONSTRAINT fk_evt_id_versamento FOREIGN KEY (id_versamento) REFERENCES versamenti(id),
        CONSTRAINT fk_evt_id_pagamento_portale FOREIGN KEY (id_pagamento_portale) REFERENCES pagamenti_portale(id),
        CONSTRAINT pk_eventi PRIMARY KEY (id)
);


-- 3.1-RC1

-- GP-677 (3.1.x_blocco_portale)
ALTER TABLE rpt ADD bloccante NUMBER;
UPDATE rpt SET bloccante = 1;
ALTER TABLE rpt MODIFY (bloccante NOT NULL);

-- Autorizzazione tutti i domini e tutte le entrate per utenza
ALTER TABLE utenze ADD autorizzazione_domini_star NUMBER;
UPDATE utenze SET autorizzazione_domini_star = 0;
ALTER TABLE utenze MODIFY (autorizzazione_domini_star NOT NULL);

ALTER TABLE utenze ADD autorizzazione_tributi_star NUMBER;
UPDATE utenze SET autorizzazione_tributi_star = 0;
ALTER TABLE utenze MODIFY (autorizzazione_tributi_star NOT NULL);



-- 11/03/2019 (Correzione bug autorizzazione utenze_tributi)
-- 23/02/2022 Gia' inserita nella crazione della tabella per migrare le ACL nella patch tra 2.6 e 3.0
-- ALTER TABLE utenze_tributi ADD id_tipo_tributo NUMBER;
-- UPDATE ut SET ut.id_tipo_tributo = t.id_tipo_tributo FROM utenze_tributi ut, tributi t WHERE ut.id_tributo = t.id;
-- ALTER TABLE utenze_tributi MODIFY (id_tipo_tributo NOT NULL);
-- ALTER TABLE utenze_tributi DROP CONSTRAINT fk_nzt_id_tributo;
-- ALTER TABLE utenze_tributi DROP COLUMN id_tributo;
-- ALTER TABLE utenze_tributi ADD CONSTRAINT fk_nzt_id_tipo_tributo FOREIGN KEY (id_tipo_tributo) REFERENCES tipi_tributo(id);

-- 13/03/2019 (Eliminazione colonna principal dalla tabella Acl e sostituzione con la foreign key verso l'utenza)
ALTER TABLE acl ADD id_utenza NUMBER;
UPDATE acl SET id_utenza = (SELECT id FROM utenze WHERE acl.principal IS NOT null AND acl.principal = utenze.principal_originale);
-- UPDATE acl SET a.id_utenza = (SELECT u.id FROM acl a, utenze u WHERE a.principal is not null and a.principal = u.principal_originale);
ALTER TABLE acl DROP COLUMN principal;
ALTER TABLE acl ADD CONSTRAINT fk_acl_id_utenza FOREIGN KEY (id_utenza) REFERENCES utenze(id);

-- 15/03/2019 campo GLN della tabella Domini non piu' obbligatorio
ALTER TABLE domini MODIFY (gln NULL);

-- 20/03/2019 (Autorizzazione basata sui tipi versamento)
ALTER TABLE utenze RENAME COLUMN autorizzazione_tributi_star TO autorizzazione_tipi_vers_star;

CREATE SEQUENCE seq_tipi_versamento MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE tipi_versamento
(
	cod_tipo_versamento VARCHAR2(35 CHAR) NOT NULL,
	descrizione VARCHAR2(255 CHAR) NOT NULL,
	-- fk/pk columns
	id NUMBER NOT NULL,
	-- unique constraints
	CONSTRAINT unique_tipi_versamento_1 UNIQUE (cod_tipo_versamento),
	-- fk/pk keys constraints
	CONSTRAINT pk_tipi_versamento PRIMARY KEY (id)
);

CREATE TRIGGER trg_tipi_versamento
BEFORE
insert on tipi_versamento
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_tipi_versamento.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;
/

INSERT INTO tipi_versamento (cod_tipo_versamento, descrizione) VALUES ('LIBERO', 'Pendenza libera');
INSERT INTO tipi_versamento (cod_tipo_versamento, descrizione) SELECT cod_tributo, descrizione FROM tipi_tributo;

CREATE SEQUENCE seq_utenze_tipo_vers MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE utenze_tipo_vers
(
	-- fk/pk columns
	id NUMBER NOT NULL,
	id_utenza NUMBER NOT NULL,
	id_tipo_versamento NUMBER NOT NULL,
	-- fk/pk keys constraints
	CONSTRAINT fk_utv_id_utenza FOREIGN KEY (id_utenza) REFERENCES utenze(id),
	CONSTRAINT fk_utv_id_tipo_versamento FOREIGN KEY (id_tipo_versamento) REFERENCES tipi_versamento(id),
	CONSTRAINT pk_utenze_tipo_vers PRIMARY KEY (id)
);

CREATE TRIGGER trg_utenze_tipo_vers
BEFORE
insert on utenze_tipo_vers
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_utenze_tipo_vers.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;
/

INSERT INTO utenze_tipo_vers (id_utenza, id_tipo_versamento) SELECT utenze_tributi.id_utenza,
 tipi_versamento.id FROM utenze_tributi JOIN tipi_tributo ON utenze_tributi.id_tipo_tributo=tipi_tributo.id JOIN tipi_versamento ON tipi_tributo.cod_tributo = tipi_versamento.cod_tipo_versamento;

ALTER TABLE versamenti ADD id_tipo_versamento NUMBER;

UPDATE versamenti SET id_tipo_versamento = (SELECT DISTINCT tipi_versamento.id FROM singoli_versamenti JOIN tributi ON singoli_versamenti.id_tributo = tributi.id JOIN tipi_tributo ON tributi.id_tipo_tributo = tipi_tributo.id JOIN tipi_versamento ON tipi_versamento.cod_tipo_versamento = tipi_tributo.cod_tributo WHERE singoli_versamenti.indice_dati=1 AND versamenti.id = singoli_versamenti.id_versamento);

UPDATE versamenti SET id_tipo_versamento = (SELECT id FROM tipi_versamento WHERE cod_tipo_versamento = 'LIBERO') WHERE id_tipo_versamento IS NULL;

ALTER TABLE versamenti MODIFY (id_tipo_versamento NOT NULL);
ALTER TABLE versamenti ADD CONSTRAINT fk_vrs_id_tipo_versamento FOREIGN KEY (id_tipo_versamento) REFERENCES tipi_versamento(id);

DROP TRIGGER trg_utenze_tributi;
DROP TABLE utenze_tributi;
DROP SEQUENCE seq_utenze_tributi;

-- 22/03/2019 Tabella TipiVersamentoDomini
CREATE SEQUENCE seq_tipi_vers_domini MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE tipi_vers_domini
(
	codifica_iuv VARCHAR2(4 CHAR),
	tipo VARCHAR2(35 CHAR),
	paga_terzi NUMBER,
	-- fk/pk columns
	id NUMBER NOT NULL,
	id_tipo_versamento NUMBER NOT NULL,
	id_dominio NUMBER NOT NULL,
	-- unique constraints
	CONSTRAINT unique_tipi_vers_domini_1 UNIQUE (id_dominio,id_tipo_versamento),
	-- fk/pk keys constraints
	CONSTRAINT fk_tvd_id_tipo_versamento FOREIGN KEY (id_tipo_versamento) REFERENCES tipi_versamento(id),
	CONSTRAINT fk_tvd_id_dominio FOREIGN KEY (id_dominio) REFERENCES domini(id),
	CONSTRAINT pk_tipi_vers_domini PRIMARY KEY (id)
);

CREATE TRIGGER trg_tipi_vers_domini
BEFORE
insert on tipi_vers_domini
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_tipi_vers_domini.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;
/

ALTER TABLE tipi_versamento ADD codifica_iuv VARCHAR2(4 CHAR);
UPDATE tipi_versamento SET codifica_iuv = (SELECT cod_tributo_iuv FROM tipi_tributo WHERE cod_tipo_versamento = cod_tributo);

ALTER TABLE tipi_versamento ADD tipo VARCHAR2(35 CHAR);
UPDATE tipi_versamento SET tipo = 'DOVUTO' WHERE (SELECT tt.on_line FROM tipi_tributo tt WHERE tt.cod_tributo = tipi_versamento.cod_tipo_versamento) = 0;
UPDATE tipi_versamento SET tipo = 'SPONTANEO' WHERE (SELECT tt.on_line FROM tipi_tributo tt WHERE tt.cod_tributo = tipi_versamento.cod_tipo_versamento) = 1;

ALTER TABLE tipi_versamento ADD paga_terzi NUMBER;
UPDATE tipi_versamento SET paga_terzi = (SELECT paga_terzi FROM tipi_tributo WHERE cod_tipo_versamento = cod_tributo);

UPDATE tipi_versamento SET tipo = 'DOVUTO', codifica_iuv = '', paga_terzi = 0 WHERE cod_tipo_versamento = 'LIBERO';
ALTER TABLE tipi_versamento MODIFY (tipo NOT NULL);
ALTER TABLE tipi_versamento MODIFY (paga_terzi DEFAULT 0);
ALTER TABLE tipi_versamento MODIFY (paga_terzi NOT NULL);


-- copia dei dati della tabella tributi
INSERT INTO tipi_vers_domini (id_tipo_versamento,codifica_iuv,tipo,paga_terzi,id_dominio) SELECT tv.id AS id_tipo_versamento, t.cod_tributo_iuv AS codifica_iuv, 'DOVUTO' AS tipo, t.paga_terzi AS paga_terzi , t.id_dominio AS id_dominio FROM tributi t, tipi_tributo tt, tipi_versamento tv WHERE t.id_tipo_tributo = tt.id AND tt.cod_tributo = tv.cod_tipo_versamento AND t.on_line = 0;
INSERT INTO tipi_vers_domini (id_tipo_versamento,codifica_iuv,tipo,paga_terzi,id_dominio) SELECT tv.id AS id_tipo_versamento, t.cod_tributo_iuv AS codifica_iuv, 'SPONTANEO' AS tipo, t.paga_terzi AS paga_terzi , t.id_dominio AS id_dominio FROM tributi t, tipi_tributo tt, tipi_versamento tv WHERE t.id_tipo_tributo = tt.id AND tt.cod_tributo = tv.cod_tipo_versamento AND t.on_line = 1;
INSERT INTO tipi_vers_domini (id_tipo_versamento,codifica_iuv,tipo,paga_terzi,id_dominio) SELECT tv.id AS id_tipo_versamento, t.cod_tributo_iuv AS codifica_iuv, NULL AS tipo, t.paga_terzi AS paga_terzi , t.id_dominio AS id_dominio FROM tributi t, tipi_tributo tt, tipi_versamento tv WHERE t.id_tipo_tributo = tt.id AND tt.cod_tributo = tv.cod_tipo_versamento AND t.on_line IS NULL;

-- genero le entries per il tipo pendenza libero
INSERT INTO tipi_vers_domini (id_dominio, id_tipo_versamento) SELECT id , (SELECT id FROM tipi_versamento WHERE cod_tipo_versamento = 'LIBERO') FROM domini;

-- eliminazione colonne non piu' significative
ALTER TABLE tributi DROP COLUMN paga_terzi;
ALTER TABLE tributi DROP COLUMN on_line;
ALTER TABLE tributi DROP COLUMN cod_tributo_iuv;

ALTER TABLE tipi_tributo DROP COLUMN paga_terzi;
ALTER TABLE tipi_tributo DROP COLUMN on_line;
ALTER TABLE tipi_tributo DROP COLUMN cod_tributo_iuv;

-- aggiunta id_tipo_versamento_dominio alla tabella versamenti
ALTER TABLE versamenti ADD id_tipo_versamento_dominio NUMBER;
UPDATE versamenti SET id_tipo_versamento_dominio = (SELECT tipi_vers_domini.id FROM tipi_vers_domini WHERE versamenti.id_dominio = tipi_vers_domini.id_dominio AND versamenti.id_tipo_versamento = tipi_vers_domini.id_tipo_versamento );

ALTER TABLE versamenti MODIFY (id_tipo_versamento_dominio NOT NULL);
ALTER TABLE versamenti ADD CONSTRAINT fk_vrs_id_tipo_versamento_dominio FOREIGN KEY (id_tipo_versamento_dominio) REFERENCES tipi_vers_domini(id);

-- modifica nomi colonne avvisatura
ALTER TABLE versamenti RENAME COLUMN cod_avvisatura TO avvisatura_cod_avvisatura;
ALTER TABLE versamenti RENAME COLUMN tipo_pagamento TO avvisatura_tipo_pagamento;
ALTER TABLE versamenti RENAME COLUMN da_avvisare TO avvisatura_da_inviare;
ALTER TABLE versamenti ADD avvisatura_abilitata NUMBER;
ALTER TABLE versamenti ADD avvisatura_operazione VARCHAR2(35 CHAR);
ALTER TABLE versamenti ADD avvisatura_modalita VARCHAR2(35 CHAR);

-- 27/03/2019 Tipo Pendenza Abilitato

ALTER TABLE tipi_versamento ADD abilitato NUMBER;
UPDATE tipi_versamento SET abilitato = 1;
ALTER TABLE tipi_versamento MODIFY (abilitato NOT NULL);

ALTER TABLE tipi_vers_domini ADD abilitato NUMBER;
UPDATE tipi_vers_domini SET abilitato = (SELECT tributi.abilitato FROM tributi, tipi_tributo, tipi_versamento WHERE tributi.id_tipo_tributo = tipi_tributo.id AND tipi_tributo.cod_tributo = tipi_versamento.cod_tipo_versamento AND tipi_versamento.id = tipi_vers_domini.id_tipo_versamento AND tipi_vers_domini.id_dominio = tributi.id_dominio);
UPDATE tipi_vers_domini SET abilitato = 1 WHERE tipi_vers_domini.id_tipo_versamento = (SELECT tipi_versamento.id FROM tipi_versamento WHERE tipi_versamento.cod_tipo_versamento = 'LIBERO');
ALTER TABLE tipi_vers_domini MODIFY (abilitato NOT NULL);


-- 02/04/2019 Aggiunto riferimento all'applicazione nella tabella pagamenti portale

ALTER TABLE pagamenti_portale ADD id_applicazione NUMBER;
ALTER TABLE pagamenti_portale ADD CONSTRAINT fk_ppt_id_applicazione FOREIGN KEY (id_applicazione) REFERENCES applicazioni(id);

-- 02/04/2019 Divisione dei diritti sul servizio 'Pagamenti e Pendenze' in 'Pagamenti' e 'Pendenze'
INSERT INTO acl (ruolo,servizio,diritti,id_utenza) SELECT acl.ruolo AS ruolo, 'Pagamenti' AS servizio, acl.diritti AS diritti, acl.id_utenza AS id_utenza FROM acl WHERE acl.servizio = 'Pagamenti e Pendenze';
INSERT INTO acl (ruolo,servizio,diritti,id_utenza) SELECT acl.ruolo AS ruolo, 'Pendenze' AS servizio, acl.diritti AS diritti, acl.id_utenza AS id_utenza FROM acl WHERE acl.servizio = 'Pagamenti e Pendenze';
DELETE FROM acl WHERE servizio = 'Pagamenti e Pendenze';
DELETE FROM acl WHERE servizio = 'Statistiche';

-- 09/04/2019 Unificazione del connettore di verifica e notifica

UPDATE applicazioni SET cod_connettore_esito = CONCAT(SUBSTR(cod_connettore_esito, 0, INSTR(cod_connettore_esito, '_')-1),'_INTEGRAZIONE');
ALTER TABLE applicazioni RENAME COLUMN cod_connettore_esito TO cod_connettore_integrazione;

UPDATE connettori SET cod_connettore = CONCAT(SUBSTR(cod_connettore, 0, INSTR(cod_connettore, '_')-1),'_INTEGRAZIONE') WHERE cod_connettore LIKE '%_ESITO';
DELETE FROM connettori WHERE cod_connettore LIKE '%_VERIFICA';

ALTER TABLE applicazioni DROP COLUMN cod_connettore_verifica;

-- 09/04/2019 ACL sulle API
INSERT INTO acl (servizio,diritti,id_utenza) SELECT 'API Pagamenti' AS Servizio , 'RW' AS diritti, acl.id_utenza FROM acl JOIN applicazioni ON acl.id_utenza = applicazioni.id_utenza WHERE (acl.ruolo IS NULL AND acl.id_utenza IS NOT NULL AND acl.servizio  = 'Pagamenti');
INSERT INTO acl (servizio,diritti,id_utenza) SELECT 'API Pendenze' AS Servizio , 'RW' AS diritti, acl.id_utenza FROM acl JOIN applicazioni ON acl.id_utenza = applicazioni.id_utenza WHERE (acl.ruolo IS NULL AND acl.id_utenza IS NOT NULL AND acl.servizio  = 'Pendenze');
INSERT INTO acl (servizio,diritti,id_utenza) SELECT 'API Ragioneria' AS Servizio , 'RW' AS diritti, acl.id_utenza FROM acl JOIN applicazioni ON acl.id_utenza = applicazioni.id_utenza WHERE (acl.ruolo IS NULL AND acl.id_utenza IS NOT NULL AND acl.servizio  = 'Rendicontazioni e Incassi');

-- 12/04/2019 Informazioni su Json Schema e Dati Allegati per i Tipi Versamento

ALTER TABLE tipi_versamento ADD json_schema CLOB;
ALTER TABLE tipi_versamento ADD dati_allegati CLOB;

ALTER TABLE tipi_vers_domini ADD json_schema CLOB;
ALTER TABLE tipi_vers_domini ADD dati_allegati CLOB;

ALTER TABLE tipi_vers_domini MODIFY (abilitato NULL);


-- 30/04/2019 eliminazione foreign key id_applicazione dalla tabella RPT

ALTER TABLE rpt DROP CONSTRAINT fk_rpt_id_applicazione;
ALTER TABLE rpt DROP COLUMN id_applicazione;

-- 08/05/2019 aggiunto idincasso ai flussi di rendicontazione
ALTER TABLE fr ADD id_incasso NUMBER;
ALTER TABLE fr ADD CONSTRAINT fk_fr_id_incasso FOREIGN KEY (id_incasso) REFERENCES incassi(id);

-- 13/05/2019 aggiunto sct alla tabella incassi

ALTER TABLE incassi ADD sct VARCHAR2(35 CHAR);

-- 13/05/2019 nuova tabella gestione delle stampe

DROP TABLE avvisi;
DROP SEQUENCE seq_avvisi;

CREATE SEQUENCE seq_stampe MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE stampe
(
	data_creazione TIMESTAMP NOT NULL,
	tipo VARCHAR2(16 CHAR) NOT NULL,
	pdf BLOB,
	-- fk/pk columns
	id NUMBER NOT NULL,
	id_versamento NUMBER NOT NULL,
	-- unique constraints
	CONSTRAINT unique_stampe_1 UNIQUE (id_versamento,tipo),
	-- fk/pk keys constraints
	CONSTRAINT fk_stm_id_versamento FOREIGN KEY (id_versamento) REFERENCES versamenti(id),
	CONSTRAINT pk_stampe PRIMARY KEY (id)
);

CREATE TRIGGER trg_stampe
BEFORE
insert on stampe
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_stampe.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;
/


-- 21/05/2019 Aggiunta tabella per memorizzare la configurazione

CREATE SEQUENCE seq_configurazione MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE configurazione
(
	giornale_eventi CLOB,
	-- fk/pk columns
	id NUMBER NOT NULL,
	-- fk/pk keys constraints
	CONSTRAINT pk_configurazione PRIMARY KEY (id)
);

CREATE TRIGGER trg_configurazione
BEFORE
insert on configurazione
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_configurazione.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;
/

-- 3.1-RC2

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
ALTER TABLE tipi_versamento ADD form_tipo VARCHAR2(35);
ALTER TABLE tipi_versamento ADD form_definizione CLOB;
ALTER TABLE tipi_versamento ADD validazione_definizione CLOB;
ALTER TABLE tipi_versamento ADD trasformazione_tipo VARCHAR2(35 CHAR);
ALTER TABLE tipi_versamento ADD trasformazione_definizione CLOB;
ALTER TABLE tipi_versamento ADD cod_applicazione VARCHAR2(35 CHAR);
ALTER TABLE tipi_versamento ADD promemoria_avviso_tipo VARCHAR2(35 CHAR);
ALTER TABLE tipi_versamento ADD promemoria_avviso_pdf NUMBER;
UPDATE tipi_versamento SET promemoria_avviso_pdf = 0;
ALTER TABLE tipi_versamento MODIFY (promemoria_avviso_pdf NOT NULL);
ALTER TABLE tipi_versamento MODIFY promemoria_avviso_pdf DEFAULT 0;
ALTER TABLE tipi_versamento ADD promemoria_avviso_oggetto CLOB;
ALTER TABLE tipi_versamento ADD promemoria_avviso_messaggio CLOB;
ALTER TABLE tipi_versamento ADD promemoria_ricevuta_tipo VARCHAR2(35 CHAR);
ALTER TABLE tipi_versamento ADD promemoria_ricevuta_pdf NUMBER;
UPDATE tipi_versamento SET promemoria_ricevuta_pdf = 0;
ALTER TABLE tipi_versamento MODIFY (promemoria_ricevuta_pdf NOT NULL);
ALTER TABLE tipi_versamento MODIFY promemoria_ricevuta_pdf DEFAULT 0;
ALTER TABLE tipi_versamento ADD promemoria_ricevuta_oggetto CLOB;
ALTER TABLE tipi_versamento ADD promemoria_ricevuta_messaggio CLOB;

ALTER TABLE tipi_vers_domini DROP COLUMN json_schema;
ALTER TABLE tipi_vers_domini DROP COLUMN dati_allegati;
ALTER TABLE tipi_vers_domini ADD form_tipo VARCHAR2(35 CHAR);
ALTER TABLE tipi_vers_domini ADD form_definizione CLOB;
ALTER TABLE tipi_vers_domini ADD validazione_definizione CLOB;
ALTER TABLE tipi_vers_domini ADD trasformazione_tipo VARCHAR2(35 CHAR);
ALTER TABLE tipi_vers_domini ADD trasformazione_definizione CLOB;
ALTER TABLE tipi_vers_domini ADD cod_applicazione VARCHAR2(35 CHAR);
ALTER TABLE tipi_vers_domini ADD promemoria_avviso_tipo VARCHAR2(35 CHAR);
ALTER TABLE tipi_vers_domini ADD promemoria_avviso_pdf NUMBER;
ALTER TABLE tipi_vers_domini ADD promemoria_avviso_oggetto CLOB;
ALTER TABLE tipi_vers_domini ADD promemoria_avviso_messaggio CLOB;
ALTER TABLE tipi_vers_domini ADD promemoria_ricevuta_tipo VARCHAR2(35 CHAR);
ALTER TABLE tipi_vers_domini ADD promemoria_ricevuta_pdf NUMBER;
ALTER TABLE tipi_vers_domini ADD promemoria_ricevuta_oggetto CLOB;
ALTER TABLE tipi_vers_domini ADD promemoria_ricevuta_messaggio CLOB;


-- 24/06/2019 Tabella per la spedizione dei promemoria via mail
CREATE SEQUENCE seq_promemoria MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE promemoria
(
	tipo VARCHAR2(16 CHAR) NOT NULL,
	data_creazione TIMESTAMP NOT NULL,
	stato VARCHAR2(16 CHAR) NOT NULL,
	descrizione_stato VARCHAR2(1024 CHAR),
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

INSERT INTO sonde(nome, classe, soglia_warn, soglia_error) VALUES ('spedizione-promemoria', 'org.openspcoop2.utils.sonde.impl.SondaBatch', 86400000, 172800000);

-- 05/07/2019 Aggiunti ruoli utenza
ALTER TABLE utenze ADD ruoli VARCHAR2(512 CHAR);

-- riporto le acl salvate come ruoli nella tabella operatori
UPDATE utenze SET ruoli = (SELECT profilo from operatori WHERE operatori.id_utenza = utenze.id);

-- elimino colonne non utilizzate tabella operatori
ALTER TABLE operatori DROP COLUMN principal;
ALTER TABLE operatori DROP COLUMN profilo;
ALTER TABLE operatori DROP COLUMN abilitato;

-- 05/07/2019 Aggiunte informazioni direzione e divisione alla tabella versamenti;
ALTER TABLE versamenti ADD divisione VARCHAR2(35 CHAR);
ALTER TABLE versamenti ADD direzione VARCHAR2(35 CHAR);

-- 16/07/2019 Dimensione della colonna descrizione stato della tabella promemoria;
ALTER TABLE promemoria MODIFY descrizione_stato VARCHAR2(1024 CHAR);

-- 26/07/2019 Aggiunti campi per definire la visualizzazione di un tipo pendenza
ALTER TABLE tipi_versamento ADD visualizzazione_definizione CLOB;
ALTER TABLE tipi_vers_domini ADD visualizzazione_definizione CLOB;

-- 05/08/2019 modifica alla struttura della tabella delle configurazioni
ALTER TABLE configurazione ADD nome VARCHAR2(255 CHAR);
ALTER TABLE configurazione RENAME COLUMN giornale_eventi TO valore;
UPDATE configurazione SET nome = 'giornale_eventi';
ALTER TABLE configurazione MODIFY (nome NOT NULL);
ALTER TABLE configurazione ADD CONSTRAINT unique_configurazione_1 UNIQUE (nome);

-- 05/08/2019 aggiunti nuovi campi alla tabella tracciati per gestire il formato csv
ALTER TABLE tracciati ADD cod_tipo_versamento VARCHAR2(35 CHAR);
ALTER TABLE tracciati ADD formato VARCHAR2(10 CHAR);
UPDATE tracciati SET formato = 'JSON';
ALTER TABLE tracciati MODIFY (formato NOT NULL);


-- 06/08/2019 aggiunte colonne per la trasformazione del tracciato csv alla tabella tipo versamento
ALTER TABLE tipi_versamento ADD trac_csv_header_risposta CLOB;
ALTER TABLE tipi_versamento ADD trac_csv_template_richiesta CLOB;
ALTER TABLE tipi_versamento ADD trac_csv_template_risposta CLOB;

ALTER TABLE tipi_vers_domini ADD trac_csv_header_risposta CLOB;
ALTER TABLE tipi_vers_domini ADD trac_csv_template_richiesta CLOB;
ALTER TABLE tipi_vers_domini ADD trac_csv_template_risposta CLOB;

-- 3.1.1

-- 3.1.2

ALTER TABLE batch MODIFY (nodo VARCHAR2(255));

-- 19/12/2019 Miglioramento performance accesso alla lista pendenze

ALTER TABLE versamenti ADD data_pagamento TIMESTAMP;
UPDATE versamenti SET data_pagamento = (SELECT MAX(pagamenti.data_pagamento) FROM pagamenti JOIN singoli_versamenti ON pagamenti.id_singolo_versamento = singoli_versamenti.id WHERE singoli_versamenti.id_versamento = versamenti.id);

ALTER TABLE versamenti ADD importo_pagato BINARY_DOUBLE;
UPDATE versamenti SET importo_pagato = 0;
UPDATE versamenti SET importo_pagato = (SELECT SUM(CASE WHEN pagamenti.importo_pagato IS NOT NULL THEN pagamenti.importo_pagato ELSE 0 END) FROM pagamenti JOIN singoli_versamenti ON pagamenti.id_singolo_versamento = singoli_versamenti.id WHERE singoli_versamenti.id_versamento = versamenti.id) WHERE versamenti.stato_versamento = 'ESEGUITO';
-- situazione in cui non c'e' il pagamento per il versamento eseguito
UPDATE versamenti SET importo_pagato = 0 WHERE importo_pagato is null;
ALTER TABLE versamenti MODIFY (importo_pagato NOT NULL);

ALTER TABLE versamenti ADD importo_incassato BINARY_DOUBLE;
UPDATE versamenti SET importo_incassato = 0;
UPDATE versamenti SET importo_incassato = (SELECT SUM(CASE WHEN pagamenti.stato = 'INCASSATO' THEN pagamenti.importo_pagato ELSE 0 END) FROM pagamenti JOIN singoli_versamenti ON pagamenti.id_singolo_versamento = singoli_versamenti.id WHERE singoli_versamenti.id_versamento = versamenti.id) WHERE versamenti.stato_versamento = 'ESEGUITO';
-- situazione in cui non c'e' il pagamento per il versamento eseguito
UPDATE versamenti SET importo_incassato = 0 WHERE importo_incassato is null;
ALTER TABLE versamenti MODIFY (importo_incassato NOT NULL);

ALTER TABLE versamenti ADD stato_pagamento VARCHAR2(35 CHAR);
UPDATE versamenti SET stato_pagamento = 'NON_PAGATO';
UPDATE versamenti SET stato_pagamento= (SELECT MAX(CASE  WHEN pagamenti.stato IS NULL THEN 'NON_PAGATO' WHEN pagamenti.stato = 'INCASSATO' THEN 'INCASSATO' ELSE 'PAGATO' END) FROM pagamenti JOIN singoli_versamenti ON pagamenti.id_singolo_versamento = singoli_versamenti.id WHERE singoli_versamenti.id_versamento = versamenti.id) WHERE versamenti.stato_versamento = 'ESEGUITO';
-- situazione in cui non c'e' il pagamento per il versamento eseguito
UPDATE versamenti SET stato_pagamento = 'NON_PAGATO' WHERE stato_pagamento is null;
ALTER TABLE versamenti MODIFY (stato_pagamento NOT NULL);

ALTER TABLE versamenti ADD iuv_pagamento VARCHAR2(35 CHAR);
UPDATE versamenti SET iuv_pagamento = (SELECT MAX(pagamenti.iuv) FROM pagamenti JOIN singoli_versamenti ON pagamenti.id_singolo_versamento = singoli_versamenti.id WHERE singoli_versamenti.id_versamento = versamenti.id);

-- 14/01/2020 Indici sulla tabella dei versamenti
CREATE INDEX idx_tipi_versamento_tipo ON tipi_versamento (tipo);

CREATE INDEX idx_vrs_data_creaz ON versamenti (data_creazione DESC);
CREATE INDEX idx_vrs_stato_vrs ON versamenti (stato_versamento);
CREATE INDEX idx_vrs_deb_identificativo ON versamenti (debitore_identificativo);

-- 15/01/2020 Indici sulla tabella delle notifiche
CREATE INDEX idx_ntf_da_spedire ON notifiche (stato,data_prossima_spedizione DESC);

-- 22/01/2020 Aggiornamento indice tabella notifiche;
DROP INDEX idx_ntf_da_spedire;
CREATE INDEX idx_ntf_da_spedire ON notifiche (id_applicazione,stato,data_prossima_spedizione);

-- 22/01/2020 Nuovi indici tabelle monitoraggio
CREATE INDEX idx_vrs_numero_avviso ON versamenti (numero_avviso);
CREATE INDEX idx_vrs_auth ON versamenti (id_dominio,id_tipo_versamento,id_uo);

CREATE INDEX idx_prt_stato ON pagamenti_portale (stato);
ALTER TABLE pagamenti_portale DROP CONSTRAINT unique_pagamenti_portale_1;
CREATE INDEX idx_prt_id_sessione ON pagamenti_portale (id_sessione);

CREATE INDEX idx_ppv_fk_prt ON pag_port_versamenti (id_pagamento_portale);
CREATE INDEX idx_ppv_fk_vrs ON pag_port_versamenti (id_versamento);

CREATE INDEX idx_rpt_fk_prt ON rpt (id_pagamento_portale);

DROP INDEX index_iuv_1;
CREATE INDEX idx_iuv_rifversamento ON iuv (cod_versamento_ente,id_applicazione,tipo_iuv);

-- CREATE INDEX idx_pag_fk_rpt ON pagamenti (id_rpt);
CREATE INDEX idx_pag_fk_sng ON pagamenti (id_singolo_versamento);

CREATE INDEX idx_evt_data ON eventi (data);
CREATE INDEX idx_evt_fk_vrs ON eventi (cod_applicazione,cod_versamento_ente);
CREATE INDEX idx_evt_id_sessione ON eventi (id_sessione);

-- 24/01/2020
ALTER TABLE versamenti DROP CONSTRAINT unique_versamenti_1;
CREATE INDEX idx_vrs_id_pendenza ON versamenti (cod_versamento_ente,id_applicazione);

-- Controllare
-- ALTER TABLE versamenti DROP CONSTRAINT fk_vrs_id_applicazione;
ALTER TABLE versamenti DROP CONSTRAINT fk_vrs_id_dominio;
ALTER TABLE versamenti DROP CONSTRAINT fk_vrs_id_tipo_versamento_dominio;
ALTER TABLE versamenti DROP CONSTRAINT fk_vrs_id_tipo_versamento;
ALTER TABLE versamenti DROP CONSTRAINT fk_vrs_id_tracciato;
-- Controllare
-- ALTER TABLE versamenti DROP CONSTRAINT fk_vrs_id_uo;

ALTER TABLE singoli_versamenti DROP CONSTRAINT unique_singoli_versamenti_1;
CREATE INDEX idx_sng_id_voce ON singoli_versamenti (id_versamento,cod_singolo_versamento_ente,indice_dati);

-- Controllare
-- ALTER TABLE singoli_versamenti DROP CONSTRAINT fk_sng_id_iban_accredito;
ALTER TABLE singoli_versamenti DROP CONSTRAINT fk_sng_id_iban_appoggio;
-- Controllare
-- ALTER TABLE singoli_versamenti DROP CONSTRAINT fk_sng_id_tributo;
-- ALTER TABLE singoli_versamenti DROP CONSTRAINT fk_sng_id_versamento;

ALTER TABLE rpt DROP CONSTRAINT unique_rpt_1;
ALTER TABLE rpt DROP CONSTRAINT unique_rpt_2;
CREATE INDEX idx_rpt_cod_msg_richiesta ON rpt (cod_msg_richiesta);
CREATE INDEX idx_rpt_id_transazione ON rpt (iuv,ccp,cod_dominio);

ALTER TABLE rpt DROP CONSTRAINT fk_rpt_id_pagamento_portale;
-- Controllare
-- ALTER TABLE rpt DROP CONSTRAINT fk_rpt_id_versamento;

ALTER TABLE pagamenti DROP CONSTRAINT unique_pagamenti_1;
CREATE INDEX idx_pag_id_riscossione ON pagamenti (cod_dominio,iuv,iur,indice_dati);

ALTER TABLE pagamenti DROP CONSTRAINT fk_pag_id_incasso;
ALTER TABLE pagamenti DROP CONSTRAINT fk_pag_id_rpt;
ALTER TABLE pagamenti DROP CONSTRAINT fk_pag_id_rr;
ALTER TABLE pagamenti DROP CONSTRAINT fk_pag_id_singolo_versamento;

ALTER TABLE pagamenti_portale DROP CONSTRAINT fk_ppt_id_applicazione;

ALTER TABLE pag_port_versamenti DROP CONSTRAINT fk_ppv_id_pagamento_portale;
ALTER TABLE pag_port_versamenti DROP CONSTRAINT fk_ppv_id_versamento;

-- 24/01/2020 Personalizzazione testo libero della causaleVersamento
ALTER TABLE singoli_versamenti ADD descrizione_causale_rpt VARCHAR2(140 CHAR);


-- 10/02/2020 Nuovi vincoli unique versamenti, rpt, riscossioni e singoliversamenti
ALTER TABLE versamenti ADD CONSTRAINT unique_versamenti_1 UNIQUE (cod_versamento_ente,id_applicazione);

DROP INDEX idx_sng_id_voce;
CREATE UNIQUE INDEX idx_sng_id_voce ON singoli_versamenti (id_versamento, indice_dati);
-- ALTER TABLE singoli_versamenti ADD CONSTRAINT unique_sng_id_voce UNIQUE USING INDEX idx_sng_id_voce;
-- L'esecuzione viene completata con esito: NOTICE:  ALTER TABLE / ADD CONSTRAINT USING INDEX will rename index "idx_sng_id_voce" to "unique_sng_id_voce"

DROP INDEX idx_rpt_id_transazione;
CREATE UNIQUE INDEX idx_rpt_id_transazione ON rpt (iuv, ccp, cod_dominio);
-- ALTER TABLE rpt ADD CONSTRAINT unique_rpt_id_transazione UNIQUE USING INDEX idx_rpt_id_transazione;
-- L'esecuzione viene completata con esito: NOTICE:  ALTER TABLE / ADD CONSTRAINT USING INDEX will rename index "idx_rpt_id_transazione" to "unique_rpt_id_transazione"

DROP INDEX idx_pag_id_riscossione;
CREATE UNIQUE INDEX idx_pag_id_riscossione ON pagamenti (cod_dominio, iuv, iur, indice_dati);
-- ALTER TABLE pagamenti ADD CONSTRAINT unique_pag_id_riscossione UNIQUE USING INDEX idx_pag_id_riscossione;
-- L'esecuzione viene completata con esito: NOTICE:  ALTER TABLE / ADD CONSTRAINT USING INDEX will rename index "idx_pag_id_riscossione" to "unique_pag_id_riscossione"


-- 3.2

-- 29/08/2019 Autorizzazione utenze per UO
ALTER TABLE utenze_domini ADD id_uo NUMBER;
ALTER TABLE utenze_domini ADD CONSTRAINT fk_nzd_id_uo FOREIGN KEY (id_uo) REFERENCES uo(id);
ALTER TABLE utenze_domini MODIFY (id_dominio NULL);

-- 26/09/2019 Identificativo univoco di creazione del versamento
ALTER TABLE versamenti ADD id_sessione VARCHAR2(35 CHAR);

-- 07/11/2019 Abilitazione dei promemoria per tipo pendenza
ALTER TABLE tipi_versamento MODIFY promemoria_avviso_pdf NULL;
ALTER TABLE tipi_versamento MODIFY promemoria_avviso_pdf DEFAULT NULL;
ALTER TABLE tipi_versamento MODIFY promemoria_ricevuta_pdf NULL;
ALTER TABLE tipi_versamento MODIFY promemoria_ricevuta_pdf DEFAULT NULL;

ALTER TABLE tipi_versamento ADD promemoria_avviso_abilitato NUMBER;
UPDATE tipi_versamento SET promemoria_avviso_abilitato = 0;
ALTER TABLE tipi_versamento MODIFY promemoria_avviso_abilitato NOT NULL;
ALTER TABLE tipi_versamento MODIFY promemoria_avviso_abilitato DEFAULT 0;

ALTER TABLE tipi_versamento ADD promemoria_ricevuta_abilitato NUMBER;
UPDATE tipi_versamento SET promemoria_ricevuta_abilitato = 0;
ALTER TABLE tipi_versamento MODIFY promemoria_ricevuta_abilitato NOT NULL;
ALTER TABLE tipi_versamento MODIFY promemoria_ricevuta_abilitato DEFAULT 0;

UPDATE tipi_versamento SET promemoria_avviso_abilitato = 1 WHERE promemoria_avviso_oggetto IS NOT NULL AND promemoria_avviso_messaggio IS NOT NULL;
UPDATE tipi_versamento SET promemoria_ricevuta_abilitato = 1 WHERE promemoria_ricevuta_oggetto IS NOT NULL AND promemoria_ricevuta_messaggio IS NOT NULL;
ALTER TABLE tipi_versamento ADD trac_csv_tipo VARCHAR2(35 CHAR);

ALTER TABLE tipi_vers_domini ADD promemoria_avviso_abilitato NUMBER;
ALTER TABLE tipi_vers_domini ADD promemoria_ricevuta_abilitato NUMBER;
ALTER TABLE tipi_vers_domini ADD trac_csv_tipo VARCHAR2(35 CHAR);

-- 27/11/2019 Aggiunti riferimenti Incasso, Fr e Tracciato alla tabella eventi
ALTER TABLE eventi ADD id_fr NUMBER;
ALTER TABLE eventi ADD id_incasso NUMBER;
ALTER TABLE eventi ADD id_tracciato NUMBER;
ALTER TABLE eventi ADD CONSTRAINT fk_evt_id_fr FOREIGN KEY (id_fr) REFERENCES fr(id);
ALTER TABLE eventi ADD CONSTRAINT fk_evt_id_incasso FOREIGN KEY (id_incasso) REFERENCES incassi(id);
ALTER TABLE eventi ADD CONSTRAINT fk_evt_id_tracciato FOREIGN KEY (id_tracciato) REFERENCES tracciati(id);

-- 19/12/2019 Miglioramento performance accesso alla lista pendenze

-- ALTER TABLE versamenti ADD data_pagamento TIMESTAMP;
UPDATE versamenti SET data_pagamento = (SELECT MAX(pagamenti.data_pagamento) FROM pagamenti JOIN singoli_versamenti ON pagamenti.id_singolo_versamento = singoli_versamenti.id WHERE singoli_versamenti.id_versamento = versamenti.id);

-- ALTER TABLE versamenti ADD importo_pagato BINARY_DOUBLE;
UPDATE versamenti SET importo_pagato = 0;
UPDATE versamenti SET importo_pagato = (SELECT SUM(CASE WHEN pagamenti.importo_pagato IS NOT NULL THEN pagamenti.importo_pagato ELSE 0 END) FROM pagamenti JOIN singoli_versamenti ON pagamenti.id_singolo_versamento = singoli_versamenti.id WHERE singoli_versamenti.id_versamento = versamenti.id) WHERE versamenti.stato_versamento = 'ESEGUITO';
-- ALTER TABLE versamenti MODIFY (importo_pagato NOT NULL);

-- ALTER TABLE versamenti ADD importo_incassato BINARY_DOUBLE;
UPDATE versamenti SET importo_incassato = 0;
UPDATE versamenti SET importo_incassato = (SELECT SUM(CASE WHEN pagamenti.stato = 'INCASSATO' THEN pagamenti.importo_pagato ELSE 0 END) FROM pagamenti JOIN singoli_versamenti ON pagamenti.id_singolo_versamento = singoli_versamenti.id WHERE singoli_versamenti.id_versamento = versamenti.id) WHERE versamenti.stato_versamento = 'ESEGUITO';
-- ALTER TABLE versamenti MODIFY (importo_incassato NOT NULL);

-- ALTER TABLE versamenti ADD stato_pagamento VARCHAR2(35 CHAR);
UPDATE versamenti SET stato_pagamento = 'NON_PAGATO';
UPDATE versamenti SET stato_pagamento= (SELECT MAX(CASE  WHEN pagamenti.stato IS NULL THEN 'NON_PAGATO' WHEN pagamenti.stato = 'INCASSATO' THEN 'INCASSATO' ELSE 'PAGATO' END) FROM pagamenti JOIN singoli_versamenti ON pagamenti.id_singolo_versamento = singoli_versamenti.id WHERE singoli_versamenti.id_versamento = versamenti.id) WHERE versamenti.stato_versamento = 'ESEGUITO';
-- ALTER TABLE versamenti MODIFY (stato_pagamento NOT NULL);

-- ALTER TABLE versamenti ADD iuv_pagamento VARCHAR2(35 CHAR);
UPDATE versamenti SET iuv_pagamento = (SELECT MAX(pagamenti.iuv) FROM pagamenti JOIN singoli_versamenti ON pagamenti.id_singolo_versamento = singoli_versamenti.id WHERE singoli_versamenti.id_versamento = versamenti.id);

-- 23/01/2020 Configurazioni servizio di reset cache anagrafica

INSERT INTO sonde(nome, classe, soglia_warn, soglia_error) VALUES ('reset-cache', 'org.openspcoop2.utils.sonde.impl.SondaBatch', 86400000, 172800000);
INSERT INTO batch(cod_batch) VALUES ('cache-anagrafica');

-- 30/01/2020 Gestione password utenze interna
ALTER TABLE utenze ADD password VARCHAR2(255 CHAR);

-- 31/01/2020 Aggiorno utenze con password provvisoria, valore non cifrato: Password1!
-- Personalizzare il valore del principal per impostare la password dell'utente amministratore con il quale 
-- impostare le altre utenze da cruscotto di gestione.
-- UPDATE utenze SET password = '$1$jil82b4n$GRX4A2H91f7L7dJ3kL2Vc.' where principal='gpadmin';

-- 03/02/2020 Rilasciato vincolo not null per i dati richiesta di un'operazione di un tracciato
ALTER TABLE operazioni MODIFY (dati_richiesta NULL);

-- 05/02/2020 Eliminata colonna AnnoRiferimento dalla tabella SingoliVersamenti
ALTER TABLE singoli_versamenti DROP COLUMN anno_riferimento;

-- 3.2.1

-- 30/03/2020 Colonne di ricerca upper case per versamenti e pagamenti portale

ALTER TABLE versamenti ADD src_iuv VARCHAR2(35 CHAR);
UPDATE versamenti SET src_iuv = UPPER(COALESCE(iuv_pagamento, iuv_versamento));

ALTER TABLE versamenti ADD src_debitore_identificativo VARCHAR2(35 CHAR);
UPDATE versamenti SET src_debitore_identificativo = UPPER(debitore_identificativo);
ALTER TABLE versamenti MODIFY (src_debitore_identificativo NOT NULL);

DROP INDEX idx_vrs_deb_identificativo;
DROP INDEX idx_vrs_numero_avviso;
CREATE INDEX idx_vrs_deb_identificativo ON versamenti (src_debitore_identificativo);
CREATE INDEX idx_vrs_iuv ON versamenti (src_iuv);

ALTER TABLE pagamenti_portale ADD src_versante_identificativo VARCHAR2(35 CHAR);
UPDATE pagamenti_portale SET src_versante_identificativo = UPPER(versante_identificativo);

CREATE INDEX idx_prt_id_sessione_psp ON pagamenti_portale (id_sessione_psp);
CREATE INDEX idx_prt_versante_identif ON pagamenti_portale (src_versante_identificativo);

-- 3.3

-- 18/12/2019 Eliminate colonne dati WISP dalla tabella PagamentiPortale.
ALTER TABLE pagamenti_portale DROP COLUMN wisp_id_dominio;
ALTER TABLE pagamenti_portale DROP COLUMN wisp_key_pa;
ALTER TABLE pagamenti_portale DROP COLUMN wisp_key_wisp;
ALTER TABLE pagamenti_portale DROP COLUMN wisp_html;

-- 03/03/2020 Modifiche alla tabelle TipiVersamento e TipiVersamentoDominio
-- 1) Aggiunte Colonne per la configurazione dell'avvisatura con AppIO

ALTER TABLE tipi_versamento ADD avv_app_io_prom_avv_abilitato NUMBER;
UPDATE tipi_versamento SET avv_app_io_prom_avv_abilitato = 0;
ALTER TABLE tipi_versamento MODIFY (avv_app_io_prom_avv_abilitato NOT NULL);
ALTER TABLE tipi_versamento MODIFY avv_app_io_prom_avv_abilitato DEFAULT 0;
ALTER TABLE tipi_versamento ADD avv_app_io_prom_avv_tipo VARCHAR2(35 CHAR);
ALTER TABLE tipi_versamento ADD avv_app_io_prom_avv_oggetto CLOB;
ALTER TABLE tipi_versamento ADD avv_app_io_prom_avv_messaggio CLOB;
ALTER TABLE tipi_versamento ADD avv_app_io_prom_ric_abilitato NUMBER;
UPDATE tipi_versamento SET avv_app_io_prom_ric_abilitato = 0;
ALTER TABLE tipi_versamento MODIFY (avv_app_io_prom_ric_abilitato NOT NULL);
ALTER TABLE tipi_versamento MODIFY avv_app_io_prom_ric_abilitato DEFAULT 0;
ALTER TABLE tipi_versamento ADD avv_app_io_prom_ric_tipo VARCHAR2(35 CHAR);
ALTER TABLE tipi_versamento ADD avv_app_io_prom_ric_oggetto CLOB;
ALTER TABLE tipi_versamento ADD avv_app_io_prom_ric_messaggio CLOB;
ALTER TABLE tipi_versamento ADD avv_app_io_prom_ric_eseguiti NUMBER;
ALTER TABLE tipi_versamento ADD avv_app_io_prom_scad_abilitato NUMBER;
UPDATE tipi_versamento SET avv_app_io_prom_scad_abilitato = 0;
ALTER TABLE tipi_versamento MODIFY (avv_app_io_prom_scad_abilitato NOT NULL);
ALTER TABLE tipi_versamento MODIFY avv_app_io_prom_scad_abilitato DEFAULT 0;
ALTER TABLE tipi_versamento ADD avv_app_io_prom_scad_preavviso NUMBER;
ALTER TABLE tipi_versamento ADD avv_app_io_prom_scad_tipo VARCHAR2(35 CHAR);
ALTER TABLE tipi_versamento ADD avv_app_io_prom_scad_oggetto CLOB;
ALTER TABLE tipi_versamento ADD avv_app_io_prom_scad_messaggio CLOB;

ALTER TABLE tipi_vers_domini ADD app_io_api_key VARCHAR2(255 CHAR);
ALTER TABLE tipi_vers_domini ADD avv_app_io_prom_avv_abilitato NUMBER;
ALTER TABLE tipi_vers_domini ADD avv_app_io_prom_avv_tipo VARCHAR2(35 CHAR);
ALTER TABLE tipi_vers_domini ADD avv_app_io_prom_avv_oggetto CLOB;
ALTER TABLE tipi_vers_domini ADD avv_app_io_prom_avv_messaggio CLOB;
ALTER TABLE tipi_vers_domini ADD avv_app_io_prom_ric_abilitato NUMBER;
ALTER TABLE tipi_vers_domini ADD avv_app_io_prom_ric_tipo VARCHAR2(35 CHAR);
ALTER TABLE tipi_vers_domini ADD avv_app_io_prom_ric_oggetto CLOB;
ALTER TABLE tipi_vers_domini ADD avv_app_io_prom_ric_messaggio CLOB;
ALTER TABLE tipi_vers_domini ADD avv_app_io_prom_ric_eseguiti NUMBER;
ALTER TABLE tipi_vers_domini ADD avv_app_io_prom_scad_abilitato NUMBER;
ALTER TABLE tipi_vers_domini ADD avv_app_io_prom_scad_preavviso NUMBER;
ALTER TABLE tipi_vers_domini ADD avv_app_io_prom_scad_tipo VARCHAR2(35 CHAR);
ALTER TABLE tipi_vers_domini ADD avv_app_io_prom_scad_oggetto CLOB;
ALTER TABLE tipi_vers_domini ADD avv_app_io_prom_scad_messaggio CLOB;

-- 2) Aggiunte Colonne per la configurazione dell'interfaccia caricamento pendenze nei portali pagamento

ALTER TABLE tipi_versamento ADD pag_form_tipo VARCHAR2(35 CHAR);
ALTER TABLE tipi_versamento ADD pag_form_definizione CLOB;
ALTER TABLE tipi_versamento ADD pag_form_impaginazione CLOB;
ALTER TABLE tipi_versamento ADD pag_validazione_def CLOB;
ALTER TABLE tipi_versamento ADD pag_trasformazione_tipo VARCHAR2(35 CHAR);
ALTER TABLE tipi_versamento ADD pag_trasformazione_def CLOB;
ALTER TABLE tipi_versamento ADD pag_cod_applicazione VARCHAR2(35 CHAR);
ALTER TABLE tipi_versamento ADD pag_abilitato NUMBER;
UPDATE tipi_versamento SET pag_abilitato = 0;
ALTER TABLE tipi_versamento MODIFY (pag_abilitato NOT NULL);
ALTER TABLE tipi_versamento MODIFY pag_abilitato DEFAULT 0;

ALTER TABLE tipi_vers_domini ADD pag_form_tipo VARCHAR2(35 CHAR);
ALTER TABLE tipi_vers_domini ADD pag_form_definizione CLOB;
ALTER TABLE tipi_vers_domini ADD pag_form_impaginazione CLOB;
ALTER TABLE tipi_vers_domini ADD pag_validazione_def CLOB;
ALTER TABLE tipi_vers_domini ADD pag_trasformazione_tipo VARCHAR2(35 CHAR);
ALTER TABLE tipi_vers_domini ADD pag_trasformazione_def CLOB;
ALTER TABLE tipi_vers_domini ADD pag_cod_applicazione VARCHAR2(35 CHAR);
ALTER TABLE tipi_vers_domini ADD pag_abilitato NUMBER;

-- 3) Rinominate Colonne per la configurazione dell'interfaccia caricamento pendenze nei portali backoffice

ALTER TABLE tipi_versamento RENAME COLUMN form_tipo TO bo_form_tipo;
ALTER TABLE tipi_versamento RENAME COLUMN form_definizione TO bo_form_definizione;
ALTER TABLE tipi_versamento RENAME COLUMN validazione_definizione TO bo_validazione_def;
ALTER TABLE tipi_versamento RENAME COLUMN trasformazione_tipo TO bo_trasformazione_tipo;
ALTER TABLE tipi_versamento RENAME COLUMN trasformazione_definizione TO bo_trasformazione_def;
ALTER TABLE tipi_versamento RENAME COLUMN cod_applicazione TO bo_cod_applicazione;
ALTER TABLE tipi_versamento ADD bo_abilitato NUMBER;
UPDATE tipi_versamento SET bo_abilitato = 0;
UPDATE tipi_versamento SET bo_abilitato = 1 WHERE (bo_form_tipo IS NOT NULL OR bo_validazione_def IS NOT NULL OR bo_trasformazione_tipo IS NOT NULL OR bo_cod_applicazione IS NOT NULL);
ALTER TABLE tipi_versamento MODIFY (bo_abilitato NOT NULL);
ALTER TABLE tipi_versamento MODIFY bo_abilitato DEFAULT 0;

ALTER TABLE tipi_vers_domini RENAME COLUMN form_tipo TO bo_form_tipo;
ALTER TABLE tipi_vers_domini RENAME COLUMN form_definizione TO bo_form_definizione;
ALTER TABLE tipi_vers_domini RENAME COLUMN validazione_definizione TO bo_validazione_def;
ALTER TABLE tipi_vers_domini RENAME COLUMN trasformazione_tipo TO bo_trasformazione_tipo;
ALTER TABLE tipi_vers_domini RENAME COLUMN trasformazione_definizione TO bo_trasformazione_def;
ALTER TABLE tipi_vers_domini RENAME COLUMN cod_applicazione TO bo_cod_applicazione;
ALTER TABLE tipi_vers_domini ADD bo_abilitato NUMBER;
UPDATE tipi_vers_domini SET bo_abilitato = 1 WHERE (bo_form_tipo IS NOT NULL OR bo_validazione_def IS NOT NULL OR bo_trasformazione_tipo IS NOT NULL OR bo_cod_applicazione IS NOT NULL);

-- 4) Rinominate Colonne per la configurazione dell'avvisatura via mail 

ALTER TABLE tipi_versamento RENAME COLUMN promemoria_avviso_abilitato TO avv_mail_prom_avv_abilitato;
ALTER TABLE tipi_versamento RENAME COLUMN promemoria_avviso_tipo TO avv_mail_prom_avv_tipo;
ALTER TABLE tipi_versamento RENAME COLUMN promemoria_avviso_pdf TO avv_mail_prom_avv_pdf;
ALTER TABLE tipi_versamento RENAME COLUMN promemoria_avviso_oggetto TO avv_mail_prom_avv_oggetto;
ALTER TABLE tipi_versamento RENAME COLUMN promemoria_avviso_messaggio TO avv_mail_prom_avv_messaggio;
ALTER TABLE tipi_versamento RENAME COLUMN promemoria_ricevuta_abilitato TO avv_mail_prom_ric_abilitato;
ALTER TABLE tipi_versamento RENAME COLUMN promemoria_ricevuta_tipo TO avv_mail_prom_ric_tipo;
ALTER TABLE tipi_versamento RENAME COLUMN promemoria_ricevuta_pdf TO avv_mail_prom_ric_pdf;
ALTER TABLE tipi_versamento RENAME COLUMN promemoria_ricevuta_oggetto TO avv_mail_prom_ric_oggetto;
ALTER TABLE tipi_versamento RENAME COLUMN promemoria_ricevuta_messaggio TO avv_mail_prom_ric_messaggio;
ALTER TABLE tipi_versamento ADD avv_mail_prom_ric_eseguiti NUMBER;
ALTER TABLE tipi_versamento ADD avv_mail_prom_scad_abilitato NUMBER;
UPDATE tipi_versamento SET avv_mail_prom_scad_abilitato = 0;
ALTER TABLE tipi_versamento MODIFY (avv_mail_prom_scad_abilitato NOT NULL);
ALTER TABLE tipi_versamento MODIFY avv_mail_prom_scad_abilitato DEFAULT 0;
ALTER TABLE tipi_versamento ADD avv_mail_prom_scad_preavviso NUMBER;
ALTER TABLE tipi_versamento ADD avv_mail_prom_scad_tipo VARCHAR2(35 CHAR);
ALTER TABLE tipi_versamento ADD avv_mail_prom_scad_oggetto CLOB;
ALTER TABLE tipi_versamento ADD avv_mail_prom_scad_messaggio CLOB;

ALTER TABLE tipi_vers_domini RENAME COLUMN promemoria_avviso_abilitato TO avv_mail_prom_avv_abilitato;
ALTER TABLE tipi_vers_domini RENAME COLUMN promemoria_avviso_tipo TO avv_mail_prom_avv_tipo;
ALTER TABLE tipi_vers_domini RENAME COLUMN promemoria_avviso_pdf TO avv_mail_prom_avv_pdf;
ALTER TABLE tipi_vers_domini RENAME COLUMN promemoria_avviso_oggetto TO avv_mail_prom_avv_oggetto;
ALTER TABLE tipi_vers_domini RENAME COLUMN promemoria_avviso_messaggio TO avv_mail_prom_avv_messaggio;
ALTER TABLE tipi_vers_domini RENAME COLUMN promemoria_ricevuta_abilitato TO avv_mail_prom_ric_abilitato;
ALTER TABLE tipi_vers_domini RENAME COLUMN promemoria_ricevuta_tipo TO avv_mail_prom_ric_tipo;
ALTER TABLE tipi_vers_domini RENAME COLUMN promemoria_ricevuta_pdf TO avv_mail_prom_ric_pdf;
ALTER TABLE tipi_vers_domini RENAME COLUMN promemoria_ricevuta_oggetto TO avv_mail_prom_ric_oggetto;
ALTER TABLE tipi_vers_domini RENAME COLUMN promemoria_ricevuta_messaggio TO avv_mail_prom_ric_messaggio;
ALTER TABLE tipi_vers_domini ADD avv_mail_prom_ric_eseguiti NUMBER;
ALTER TABLE tipi_vers_domini ADD avv_mail_prom_scad_abilitato NUMBER;
ALTER TABLE tipi_versamento MODIFY avv_mail_prom_scad_abilitato DEFAULT 0;
ALTER TABLE tipi_vers_domini ADD avv_mail_prom_scad_preavviso NUMBER;
ALTER TABLE tipi_vers_domini ADD avv_mail_prom_scad_tipo VARCHAR2(35 CHAR);
ALTER TABLE tipi_vers_domini ADD avv_mail_prom_scad_oggetto CLOB;
ALTER TABLE tipi_vers_domini ADD avv_mail_prom_scad_messaggio CLOB;


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

INSERT INTO sonde(nome, classe, soglia_warn, soglia_error) VALUES ('update-ntfy-appio', 'org.openspcoop2.utils.sonde.impl.SondaBatch', 86400000, 172800000);
INSERT INTO sonde(nome, classe, soglia_warn, soglia_error) VALUES ('check-ntfy-appio', 'org.openspcoop2.utils.sonde.impl.SondaCoda', 10, 100);

-- 08/04/2020 Nuovi valori configurazione generale
INSERT INTO configurazione (NOME,VALORE) VALUES ('app_io_batch', '{"abilitato": false, "url": null, "timeToLive": 3600 }');
INSERT INTO configurazione (NOME,VALORE) VALUES ('avvisatura_mail', '{"promemoriaAvviso": { "tipo": "freemarker", "oggetto": "\"UHJvbWVtb3JpYSBwYWdhbWVudG86ICR7dmVyc2FtZW50by5nZXRDYXVzYWxlVmVyc2FtZW50bygpLmdldFNpbXBsZSgpfQ==\"", "messaggio": "\"R2VudGlsZSAke3ZlcnNhbWVudG8uZ2V0QW5hZ3JhZmljYURlYml0b3JlKCkuZ2V0UmFnaW9uZVNvY2lhbGUoKX0sCgpsZSBub3RpZmljaGlhbW8gY2hlIMOoIHN0YXRhIGVtZXNzYSB1bmEgcmljaGllc3RhIGRpIHBhZ2FtZW50byBhIHN1byBjYXJpY286ICR7dmVyc2FtZW50by5nZXRDYXVzYWxlVmVyc2FtZW50bygpLmdldFNpbXBsZSgpfQoKPCNpZiB2ZXJzYW1lbnRvLmdldE51bWVyb0F2dmlzbygpP2hhc19jb250ZW50PgpQdcOyIGVmZmV0dHVhcmUgaWwgcGFnYW1lbnRvIHRyYW1pdGUgbCdhcHAgbW9iaWxlIElPIG9wcHVyZSBwcmVzc28gdW5vIGRlaSBwcmVzdGF0b3JpIGRpIHNlcnZpemkgZGkgcGFnYW1lbnRvIGFkZXJlbnRpIGFsIGNpcmN1aXRvIHBhZ29QQSB1dGlsaXp6YW5kbyBsJ2F2dmlzbyBkaSBwYWdhbWVudG8gYWxsZWdhdG8uCjwjZWxzZT4KUHVvJyBlZmZldHR1YXJlIGlsIHBhZ2FtZW50byBvbi1saW5lIHByZXNzbyBpbCBwb3J0YWxlIGRlbGwnZW50ZSBjcmVkaXRvcmU6ICR7ZG9taW5pby5nZXRSYWdpb25lU29jaWFsZSgpfSAKPC8jaWY+CgpEaXN0aW50aSBzYWx1dGku\"", "allegaPdf": true }, "promemoriaRicevuta": { "tipo": "freemarker", "oggetto": "\"PCNpZiBycHQuZ2V0RXNpdG9QYWdhbWVudG8oKS5nZXRDb2RpZmljYSgpID0gMD4KTm90aWZpY2EgcGFnYW1lbnRvIGVzZWd1aXRvOiAke3JwdC5nZXRDb2REb21pbmlvKCl9LyR7cnB0LmdldEl1digpfS8ke3JwdC5nZXRDY3AoKX0KPCNlbHNlaWYgcnB0LmdldEVzaXRvUGFnYW1lbnRvKCkuZ2V0Q29kaWZpY2EoKSA9IDE+Ck5vdGlmaWNhIHBhZ2FtZW50byBub24gZXNlZ3VpdG86ICR7cnB0LmdldENvZERvbWluaW8oKX0vJHtycHQuZ2V0SXV2KCl9LyR7cnB0LmdldENjcCgpfQo8I2Vsc2VpZiBycHQuZ2V0RXNpdG9QYWdhbWVudG8oKS5nZXRDb2RpZmljYSgpID0gMj4KTm90aWZpY2EgcGFnYW1lbnRvIGVzZWd1aXRvIHBhcnppYWxtZW50ZTogJHtycHQuZ2V0Q29kRG9taW5pbygpfS8ke3JwdC5nZXRJdXYoKX0vJHtycHQuZ2V0Q2NwKCl9CjwjZWxzZWlmIHJwdC5nZXRFc2l0b1BhZ2FtZW50bygpLmdldENvZGlmaWNhKCkgPSAzPgpOb3RpZmljYSBkZWNvcnJlbnphIHRlcm1pbmkgcGFnYW1lbnRvOiAke3JwdC5nZXRDb2REb21pbmlvKCl9LyR7cnB0LmdldEl1digpfS8ke3JwdC5nZXRDY3AoKX0KPCNlbHNlaWYgcnB0LmdldEVzaXRvUGFnYW1lbnRvKCkuZ2V0Q29kaWZpY2EoKSA9IDQ+Ck5vdGlmaWNhIGRlY29ycmVuemEgdGVybWluaSBwYWdhbWVudG86ICR7cnB0LmdldENvZERvbWluaW8oKX0vJHtycHQuZ2V0SXV2KCl9LyR7cnB0LmdldENjcCgpfQo8LyNpZj4=\"", "messaggio": "\"PCNhc3NpZ24gZGF0YVJpY2hpZXN0YSA9IHJwdC5nZXREYXRhTXNnUmljaGllc3RhKCk/c3RyaW5nKCJ5eXl5LU1NLWRkIEhIOm1tOnNzIik+CklsIHBhZ2FtZW50byBkaSAiJHt2ZXJzYW1lbnRvLmdldENhdXNhbGVWZXJzYW1lbnRvKCkuZ2V0U2ltcGxlKCl9IiBlZmZldHR1YXRvIGlsICR7ZGF0YVJpY2hpZXN0YX0gcmlzdWx0YSBjb25jbHVzbyBjb24gZXNpdG8gJHtycHQuZ2V0RXNpdG9QYWdhbWVudG8oKS5uYW1lKCl9OgoKRW50ZSBjcmVkaXRvcmU6ICR7ZG9taW5pby5nZXRSYWdpb25lU29jaWFsZSgpfSAoJHtkb21pbmlvLmdldENvZERvbWluaW8oKX0pCklzdGl0dXRvIGF0dGVzdGFudGU6ICR7cnB0LmdldERlbm9taW5hemlvbmVBdHRlc3RhbnRlKCl9ICgke3JwdC5nZXRJZGVudGlmaWNhdGl2b0F0dGVzdGFudGUoKX0pCklkZW50aWZpY2F0aXZvIHVuaXZvY28gdmVyc2FtZW50byAoSVVWKTogJHtycHQuZ2V0SXV2KCl9CkNvZGljZSBjb250ZXN0byBwYWdhbWVudG8gKENDUCk6ICR7cnB0LmdldENjcCgpfQpJbXBvcnRvIHBhZ2F0bzogJHtycHQuZ2V0SW1wb3J0b1RvdGFsZVBhZ2F0bygpfQoKRGlzdGludGkgc2FsdXRpLg==\"", "allegaPdf": true , "soloEseguiti": true }, "promemoriaScadenza": { "tipo": "freemarker", "oggetto": "\"UHJvbWVtb3JpYSBwYWdhbWVudG86ICR7dmVyc2FtZW50by5nZXRDYXVzYWxlVmVyc2FtZW50bygpLmdldFNpbXBsZSgpfQ==\"", "messaggio": "\"R2VudGlsZSAke3ZlcnNhbWVudG8uZ2V0QW5hZ3JhZmljYURlYml0b3JlKCkuZ2V0UmFnaW9uZVNvY2lhbGUoKX0sCgpsZSBub3RpZmljaGlhbW8gY2hlIMOoIHN0YXRhIGVtZXNzYSB1bmEgcmljaGllc3RhIGRpIHBhZ2FtZW50byBhIHN1byBjYXJpY286ICR7dmVyc2FtZW50by5nZXRDYXVzYWxlVmVyc2FtZW50bygpLmdldFNpbXBsZSgpfQoKPCNpZiB2ZXJzYW1lbnRvLmdldE51bWVyb0F2dmlzbygpP2hhc19jb250ZW50PgpQdcOyIGVmZmV0dHVhcmUgaWwgcGFnYW1lbnRvIHRyYW1pdGUgbCdhcHAgbW9iaWxlIElPIG9wcHVyZSBwcmVzc28gdW5vIGRlaSBwcmVzdGF0b3JpIGRpIHNlcnZpemkgZGkgcGFnYW1lbnRvIGFkZXJlbnRpIGFsIGNpcmN1aXRvIHBhZ29QQSB1dGlsaXp6YW5kbyBsJ2F2dmlzbyBkaSBwYWdhbWVudG8gYWxsZWdhdG8uCjwjZWxzZT4KUHVvJyBlZmZldHR1YXJlIGlsIHBhZ2FtZW50byBvbi1saW5lIHByZXNzbyBpbCBwb3J0YWxlIGRlbGwnZW50ZSBjcmVkaXRvcmU6ICR7ZG9taW5pby5nZXRSYWdpb25lU29jaWFsZSgpfSAKPC8jaWY+CgpEaXN0aW50aSBzYWx1dGku\"", "preavviso": 10 } }' );
INSERT INTO configurazione (NOME,VALORE) VALUES ('avvisatura_app_io', '{"promemoriaAvviso": { "tipo": "freemarker", "oggetto": "\"UHJvbWVtb3JpYSBwYWdhbWVudG86ICR7dmVyc2FtZW50by5nZXRDYXVzYWxlVmVyc2FtZW50bygpLmdldFNpbXBsZSgpfQ==\"", "messaggio": "\"R2VudGlsZSAke3ZlcnNhbWVudG8uZ2V0QW5hZ3JhZmljYURlYml0b3JlKCkuZ2V0UmFnaW9uZVNvY2lhbGUoKX0sCgpsZSBub3RpZmljaGlhbW8gY2hlIMOoIHN0YXRhIGVtZXNzYSB1bmEgcmljaGllc3RhIGRpIHBhZ2FtZW50byBhIHN1byBjYXJpY286ICR7dmVyc2FtZW50by5nZXRDYXVzYWxlVmVyc2FtZW50bygpLmdldFNpbXBsZSgpfQoKPCNpZiB2ZXJzYW1lbnRvLmdldE51bWVyb0F2dmlzbygpP2hhc19jb250ZW50PgpQdcOyIGVmZmV0dHVhcmUgaWwgcGFnYW1lbnRvIHRyYW1pdGUgbCdhcHAgbW9iaWxlIElPIG9wcHVyZSBwcmVzc28gdW5vIGRlaSBwcmVzdGF0b3JpIGRpIHNlcnZpemkgZGkgcGFnYW1lbnRvIGFkZXJlbnRpIGFsIGNpcmN1aXRvIHBhZ29QQSB1dGlsaXp6YW5kbyBsJ2F2dmlzbyBkaSBwYWdhbWVudG8gYWxsZWdhdG8uCjwjZWxzZT4KUHVvJyBlZmZldHR1YXJlIGlsIHBhZ2FtZW50byBvbi1saW5lIHByZXNzbyBpbCBwb3J0YWxlIGRlbGwnZW50ZSBjcmVkaXRvcmU6ICR7ZG9taW5pby5nZXRSYWdpb25lU29jaWFsZSgpfSAKPC8jaWY+CgpEaXN0aW50aSBzYWx1dGku\"" }, "promemoriaRicevuta": { "tipo": "freemarker", "oggetto": "\"PCNpZiBycHQuZ2V0RXNpdG9QYWdhbWVudG8oKS5nZXRDb2RpZmljYSgpID0gMD4KTm90aWZpY2EgcGFnYW1lbnRvIGVzZWd1aXRvOiAke3JwdC5nZXRDb2REb21pbmlvKCl9LyR7cnB0LmdldEl1digpfS8ke3JwdC5nZXRDY3AoKX0KPCNlbHNlaWYgcnB0LmdldEVzaXRvUGFnYW1lbnRvKCkuZ2V0Q29kaWZpY2EoKSA9IDE+Ck5vdGlmaWNhIHBhZ2FtZW50byBub24gZXNlZ3VpdG86ICR7cnB0LmdldENvZERvbWluaW8oKX0vJHtycHQuZ2V0SXV2KCl9LyR7cnB0LmdldENjcCgpfQo8I2Vsc2VpZiBycHQuZ2V0RXNpdG9QYWdhbWVudG8oKS5nZXRDb2RpZmljYSgpID0gMj4KTm90aWZpY2EgcGFnYW1lbnRvIGVzZWd1aXRvIHBhcnppYWxtZW50ZTogJHtycHQuZ2V0Q29kRG9taW5pbygpfS8ke3JwdC5nZXRJdXYoKX0vJHtycHQuZ2V0Q2NwKCl9CjwjZWxzZWlmIHJwdC5nZXRFc2l0b1BhZ2FtZW50bygpLmdldENvZGlmaWNhKCkgPSAzPgpOb3RpZmljYSBkZWNvcnJlbnphIHRlcm1pbmkgcGFnYW1lbnRvOiAke3JwdC5nZXRDb2REb21pbmlvKCl9LyR7cnB0LmdldEl1digpfS8ke3JwdC5nZXRDY3AoKX0KPCNlbHNlaWYgcnB0LmdldEVzaXRvUGFnYW1lbnRvKCkuZ2V0Q29kaWZpY2EoKSA9IDQ+Ck5vdGlmaWNhIGRlY29ycmVuemEgdGVybWluaSBwYWdhbWVudG86ICR7cnB0LmdldENvZERvbWluaW8oKX0vJHtycHQuZ2V0SXV2KCl9LyR7cnB0LmdldENjcCgpfQo8LyNpZj4=\"", "messaggio": "\"PCNhc3NpZ24gZGF0YVJpY2hpZXN0YSA9IHJwdC5nZXREYXRhTXNnUmljaGllc3RhKCk/c3RyaW5nKCJ5eXl5LU1NLWRkIEhIOm1tOnNzIik+CklsIHBhZ2FtZW50byBkaSAiJHt2ZXJzYW1lbnRvLmdldENhdXNhbGVWZXJzYW1lbnRvKCkuZ2V0U2ltcGxlKCl9IiBlZmZldHR1YXRvIGlsICR7ZGF0YVJpY2hpZXN0YX0gcmlzdWx0YSBjb25jbHVzbyBjb24gZXNpdG8gJHtycHQuZ2V0RXNpdG9QYWdhbWVudG8oKS5uYW1lKCl9OgoKRW50ZSBjcmVkaXRvcmU6ICR7ZG9taW5pby5nZXRSYWdpb25lU29jaWFsZSgpfSAoJHtkb21pbmlvLmdldENvZERvbWluaW8oKX0pCklzdGl0dXRvIGF0dGVzdGFudGU6ICR7cnB0LmdldERlbm9taW5hemlvbmVBdHRlc3RhbnRlKCl9ICgke3JwdC5nZXRJZGVudGlmaWNhdGl2b0F0dGVzdGFudGUoKX0pCklkZW50aWZpY2F0aXZvIHVuaXZvY28gdmVyc2FtZW50byAoSVVWKTogJHtycHQuZ2V0SXV2KCl9CkNvZGljZSBjb250ZXN0byBwYWdhbWVudG8gKENDUCk6ICR7cnB0LmdldENjcCgpfQpJbXBvcnRvIHBhZ2F0bzogJHtycHQuZ2V0SW1wb3J0b1RvdGFsZVBhZ2F0bygpfQoKRGlzdGludGkgc2FsdXRpLg==\"", "soloEseguiti": true }, "promemoriaScadenza": { "tipo": "freemarker", "oggetto": "\"UHJvbWVtb3JpYSBwYWdhbWVudG86ICR7dmVyc2FtZW50by5nZXRDYXVzYWxlVmVyc2FtZW50bygpLmdldFNpbXBsZSgpfQ==\"", "messaggio": "\"R2VudGlsZSAke3ZlcnNhbWVudG8uZ2V0QW5hZ3JhZmljYURlYml0b3JlKCkuZ2V0UmFnaW9uZVNvY2lhbGUoKX0sCgpsZSBub3RpZmljaGlhbW8gY2hlIMOoIHN0YXRhIGVtZXNzYSB1bmEgcmljaGllc3RhIGRpIHBhZ2FtZW50byBhIHN1byBjYXJpY286ICR7dmVyc2FtZW50by5nZXRDYXVzYWxlVmVyc2FtZW50bygpLmdldFNpbXBsZSgpfQoKPCNpZiB2ZXJzYW1lbnRvLmdldE51bWVyb0F2dmlzbygpP2hhc19jb250ZW50PgpQdcOyIGVmZmV0dHVhcmUgaWwgcGFnYW1lbnRvIHRyYW1pdGUgbCdhcHAgbW9iaWxlIElPIG9wcHVyZSBwcmVzc28gdW5vIGRlaSBwcmVzdGF0b3JpIGRpIHNlcnZpemkgZGkgcGFnYW1lbnRvIGFkZXJlbnRpIGFsIGNpcmN1aXRvIHBhZ29QQSB1dGlsaXp6YW5kbyBsJ2F2dmlzbyBkaSBwYWdhbWVudG8gYWxsZWdhdG8uCjwjZWxzZT4KUHVvJyBlZmZldHR1YXJlIGlsIHBhZ2FtZW50byBvbi1saW5lIHByZXNzbyBpbCBwb3J0YWxlIGRlbGwnZW50ZSBjcmVkaXRvcmU6ICR7ZG9taW5pby5nZXRSYWdpb25lU29jaWFsZSgpfSAKPC8jaWY+CgpEaXN0aW50aSBzYWx1dGku\"", "preavviso": 10 } }' );



-- 21/04/2020 Gestione rateizzazione pagamenti

CREATE SEQUENCE seq_documenti MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE documenti
(
	cod_documento VARCHAR2(35 CHAR) NOT NULL,
	descrizione VARCHAR2(255 CHAR) NOT NULL,
	-- fk/pk columns
	id NUMBER NOT NULL,
	id_dominio NUMBER NOT NULL,
	id_applicazione NUMBER NOT NULL,
	-- unique constraints
	CONSTRAINT unique_documenti_1 UNIQUE (cod_documento,id_applicazione),
	-- fk/pk keys constraints
	CONSTRAINT fk_doc_id_dominio FOREIGN KEY (id_dominio) REFERENCES domini(id),
	CONSTRAINT fk_doc_id_applicazione FOREIGN KEY (id_applicazione) REFERENCES applicazioni(id),
	CONSTRAINT pk_documenti PRIMARY KEY (id)
);

CREATE TRIGGER trg_documenti
BEFORE
insert on documenti
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_documenti.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;
/

ALTER TABLE versamenti ADD cod_rata NUMBER;
ALTER TABLE versamenti ADD id_documento NUMBER;
-- N.B. -> nello script generale questa constraint viene eliminata
-- ALTER TABLE versamenti ADD CONSTRAINT fk_vrs_id_documento FOREIGN KEY (id_documento) REFERENCES documenti(id);

-- 21/04/2020 Gestione spedizione promemoria per i documenti

ALTER TABLE promemoria MODIFY (id_versamento NULL);
ALTER TABLE promemoria ADD id_documento NUMBER;
ALTER TABLE promemoria ADD CONSTRAINT fk_prm_id_documento FOREIGN KEY (id_documento) REFERENCES documenti(id);

-- 21/04/2020 Gestione spedizione stampe per i documenti

ALTER TABLE stampe DROP CONSTRAINT unique_stampe_1;
ALTER TABLE stampe MODIFY (id_versamento NULL);
ALTER TABLE stampe ADD id_documento NUMBER;
ALTER TABLE stampe ADD CONSTRAINT fk_stm_id_documento FOREIGN KEY (id_documento) REFERENCES documenti(id);
ALTER TABLE stampe ADD CONSTRAINT unique_stampe_1 UNIQUE (id_versamento,id_documento,tipo);

-- 28/04/2020 Aggiunte colonne RagioneSocialeDominio e RagioneSocialePsp alla tabella FR

ALTER TABLE fr ADD ragione_sociale_psp VARCHAR2(70 CHAR);
ALTER TABLE fr ADD ragione_sociale_dominio VARCHAR2(70 CHAR);

-- Gestione del tipo versamento all'interno della tabella versamenti
ALTER TABLE versamenti ADD tipo VARCHAR2(35 CHAR);
UPDATE versamenti SET tipo = 'DOVUTO';
UPDATE versamenti SET tipo = 'SPONTANEO' where id_tipo_versamento in (SELECT id FROM tipi_versamento WHERE tipi_versamento.tipo = 'SPONTANEO');
ALTER TABLE versamenti MODIFY (tipo NOT NULL);

ALTER TABLE tipi_vers_domini DROP COLUMN tipo;

DROP INDEX idx_tipi_versamento_tipo;
ALTER TABLE tipi_versamento DROP COLUMN tipo;

-- 15/05/2020 Eliminazione colonne deprecate stato domini e stazioni
ALTER TABLE stazioni DROP COLUMN ndp_stato;
ALTER TABLE stazioni DROP COLUMN ndp_operazione;
ALTER TABLE stazioni DROP COLUMN ndp_descrizione;
ALTER TABLE stazioni DROP COLUMN ndp_data;

ALTER TABLE domini DROP COLUMN ndp_stato;
ALTER TABLE domini DROP COLUMN ndp_operazione;
ALTER TABLE domini DROP COLUMN ndp_descrizione;
ALTER TABLE domini DROP COLUMN ndp_data;

-- 19/05/2020 Aggiunta gestione della descrizione degli IBAN
ALTER TABLE iban_accredito ADD descrizione VARCHAR2(255 CHAR);
ALTER TABLE iban_accredito DROP COLUMN attivato;

-- 25/05/2020 Ottimizzazione della gestione dell'avvisatura

ALTER TABLE versamenti ADD data_notifica_avviso TIMESTAMP;
ALTER TABLE versamenti ADD avviso_notificato NUMBER;
ALTER TABLE versamenti ADD avv_mail_data_prom_scadenza TIMESTAMP;
ALTER TABLE versamenti ADD avv_mail_prom_scad_notificato NUMBER;
ALTER TABLE versamenti ADD avv_app_io_data_prom_scadenza TIMESTAMP;
ALTER TABLE versamenti ADD avv_app_io_prom_scad_notificat NUMBER;

CREATE INDEX idx_vrs_prom_avviso ON versamenti (avviso_notificato,data_notifica_avviso DESC);
CREATE INDEX idx_vrs_avv_mail_prom_scad ON versamenti (avv_mail_prom_scad_notificato,avv_mail_data_prom_scadenza DESC);
CREATE INDEX idx_vrs_avv_io_prom_scad ON versamenti (avv_app_io_prom_scad_notificat,avv_app_io_data_prom_scadenza DESC);

insert into sonde(nome, classe, soglia_warn, soglia_error) values ('gestione-promemoria', 'org.openspcoop2.utils.sonde.impl.SondaBatch', 86400000, 172800000);

-- 02/06/2020 Batch check promemoria
insert into sonde(nome, classe, soglia_warn, soglia_error) values ('check-promemoria', 'org.openspcoop2.utils.sonde.impl.SondaCoda', 10, 100);
insert into sonde(nome, classe, soglia_warn, soglia_error) values ('check-gestione-promemoria', 'org.openspcoop2.utils.sonde.impl.SondaCoda', 10, 100);

-- 15/06/2020 Modificato tipo della colonna cod rata per supportare pagamenti con soglia
ALTER TABLE versamenti MODIFY cod_rata VARCHAR2(35 CHAR); 

-- 07/07/2020 Operazione di un tracciato riferisce l'eventuale stampa dell'avviso
ALTER TABLE operazioni ADD id_stampa NUMBER;
ALTER TABLE operazioni ADD CONSTRAINT fk_ope_id_stampa FOREIGN KEY (id_stampa) REFERENCES stampe(id);

-- 08/07/2020 Zip delle stampe di un tracciato salvato su db
ALTER TABLE tracciati ADD zip_stampe BLOB;

-- 16/07/2020 Eliminata gestione avvisatura al nodo

ALTER TABLE versamenti DROP COLUMN avvisatura_abilitata;
ALTER TABLE versamenti DROP COLUMN avvisatura_da_inviare;
ALTER TABLE versamenti DROP COLUMN avvisatura_operazione;
ALTER TABLE versamenti DROP COLUMN avvisatura_modalita;
ALTER TABLE versamenti DROP COLUMN avvisatura_tipo_pagamento;
ALTER TABLE versamenti DROP COLUMN avvisatura_cod_avvisatura;
-- ALTER TABLE versamenti DROP CONSTRAINT fk_vrs_id_tracciato;
ALTER TABLE versamenti DROP COLUMN id_tracciato;

DROP TRIGGER trg_esiti_avvisatura;
DROP TABLE esiti_avvisatura;
DROP SEQUENCE seq_esiti_avvisatura;

ALTER TABLE operazioni ADD id_versamento NUMBER;
ALTER TABLE operazioni ADD CONSTRAINT fk_ope_id_versamento FOREIGN KEY (id_versamento) REFERENCES versamenti(id);

-- 06/08/2020 Aggiunto Intestatario Conto Accredito
ALTER TABLE iban_accredito ADD intestatario VARCHAR2(255 CHAR);

-- 3.4

-- 17/11/2020 Storicizzazione dei flussi di rendicontazione
ALTER TABLE fr ADD obsoleto NUMBER;
UPDATE fr SET obsoleto = 0;
ALTER TABLE fr MODIFY (obsoleto NOT NULL);
UPDATE fr SET data_ora_flusso = data_acquisizione WHERE data_ora_flusso IS NULL;
ALTER TABLE fr MODIFY (data_ora_flusso NOT NULL);

ALTER TABLE fr DROP CONSTRAINT unique_fr_1;
ALTER TABLE fr ADD CONSTRAINT unique_fr_1 UNIQUE (cod_flusso,data_ora_flusso);


-- 07/12/2020 Livello di severita errori
ALTER TABLE pagamenti_portale ADD severita NUMBER;

ALTER TABLE eventi ADD severita NUMBER;

 -- 01/02/2021 Gestione dei tracciati notifiche mypivot  
   
CREATE SEQUENCE seq_trac_notif_pag MINVALUE 1 MAXVALUE 9223372036854775807 START WITH 1 INCREMENT BY 1 CACHE 2 NOCYCLE;

CREATE TABLE trac_notif_pag
(
	nome_file VARCHAR2(255 CHAR) NOT NULL,
	tipo VARCHAR2(20 CHAR) NOT NULL,
	versione VARCHAR2(20 CHAR) NOT NULL,
	stato VARCHAR2(20 CHAR) NOT NULL,
	data_creazione TIMESTAMP NOT NULL,
	data_rt_da TIMESTAMP NOT NULL,
	data_rt_a TIMESTAMP NOT NULL,
	data_caricamento TIMESTAMP,
	data_completamento TIMESTAMP,
	raw_contenuto BLOB,
	bean_dati CLOB,
	-- fk/pk columns
	id NUMBER NOT NULL,
	id_dominio NUMBER NOT NULL,
	-- fk/pk keys constraints
	CONSTRAINT fk_tnp_id_dominio FOREIGN KEY (id_dominio) REFERENCES domini(id),
	CONSTRAINT pk_trac_notif_pag PRIMARY KEY (id)
);

CREATE TRIGGER trg_trac_notif_pag
BEFORE
insert on trac_notif_pag
for each row
begin
   IF (:new.id IS NULL) THEN
      SELECT seq_trac_notif_pag.nextval INTO :new.id
                FROM DUAL;
   END IF;
end;
/

ALTER TABLE rpt ADD id_tracciato_mypivot NUMBER;
ALTER TABLE rpt ADD id_tracciato_secim NUMBER;
ALTER TABLE rpt ADD CONSTRAINT fk_rpt_id_tracciato_mypivot FOREIGN KEY (id_tracciato_mypivot) REFERENCES trac_notif_pag(id);
ALTER TABLE rpt ADD CONSTRAINT fk_rpt_id_tracciato_secim FOREIGN KEY (id_tracciato_secim) REFERENCES trac_notif_pag(id);

ALTER TABLE rpt DROP CONSTRAINT fk_rpt_id_tracciato_mypivot ;
ALTER TABLE rpt DROP CONSTRAINT fk_rpt_id_tracciato_secim;
ALTER TABLE rpt DROP COLUMN id_tracciato_mypivot ;
ALTER TABLE rpt DROP COLUMN id_tracciato_secim ;

ALTER TABLE domini ADD cod_connettore_my_pivot VARCHAR2(255 CHAR);
ALTER TABLE domini ADD cod_connettore_secim VARCHAR2(255 CHAR);

insert into sonde(nome, classe, soglia_warn, soglia_error) values ('check-elab-trac-notif-pag', 'org.openspcoop2.utils.sonde.impl.SondaCoda', 1, 1);
insert into sonde(nome, classe, soglia_warn, soglia_error) values ('elaborazione-trac-notif-pag', 'org.openspcoop2.utils.sonde.impl.SondaBatch', 86400000, 172800000);
insert into sonde(nome, classe, soglia_warn, soglia_error) values ('check-spedizione-trac-notif-pag', 'org.openspcoop2.utils.sonde.impl.SondaCoda', 1, 1);
insert into sonde(nome, classe, soglia_warn, soglia_error) values ('spedizione-trac-notif-pag', 'org.openspcoop2.utils.sonde.impl.SondaBatch', 86400000, 172800000);

-- 22/02/2021 Adeguamento della vista Pagamenti/Riscossioni per le API-Ragioneria

-- 26/02/2020 Aggiunta colonna connettore_govpay alla tabella domini.
ALTER TABLE domini ADD cod_connettore_gov_pay VARCHAR2(255 CHAR);

-- 10/03/2021 Supporto gestione avvisi multilingua
ALTER TABLE versamenti ADD proprieta CLOB;

-- 31/03/2021 Vincolo di univocita' documento
ALTER TABLE documenti DROP CONSTRAINT unique_documenti_1;
ALTER TABLE documenti ADD CONSTRAINT unique_documenti_1 UNIQUE (cod_documento,id_applicazione,id_dominio);

-- 20/04/2021 Aggiunto campo opzionale autStampaPoste alla tabella IBAN Accredito
ALTER TABLE iban_accredito ADD aut_stampa_poste VARCHAR2(255 CHAR);

-- 3.5 

-- 27/04/2021 Aggiunta colonna contabilita alla tabella singoli versamenti
ALTER TABLE singoli_versamenti ADD contabilita CLOB;


-- 11/05/2021 Aggiunto identificativo tracciato notifica pagamenti
ALTER TABLE trac_notif_pag ADD identificativo VARCHAR2(255 CHAR);
UPDATE trac_notif_pag SET identificativo = nome_file;
ALTER TABLE trac_notif_pag MODIFY (identificativo NOT NULL);


-- 17/05/2021 Indice sulla colonna iuv della tabella eventi
CREATE INDEX idx_evt_iuv ON eventi (iuv);

-- 20/05/2021 Aggiunta colonna connettore hyper_sic_apk alla tabella domini
ALTER TABLE domini ADD cod_connettore_hyper_sic_apk VARCHAR2(255 CHAR);

-- 07/06/2021 Aggiunta colonna con la versione dell'RPT
ALTER TABLE rpt ADD versione VARCHAR2(35 CHAR);
UPDATE rpt SET versione = 'SANP_230';
ALTER TABLE rpt MODIFY (versione NOT NULL);

-- 07/06/2021 Aggiunta colonna intermediato alla tabella domini
ALTER TABLE domini ADD intermediato NUMBER;
UPDATE domini SET intermediato = 1;
ALTER TABLE domini MODIFY (intermediato NOT NULL);

-- 08/06/2021 Stazione di un dominio opzionale
ALTER TABLE domini MODIFY (id_stazione NULL);


-- 22/06/2021 API-Rendicontazione V3, nuovi campi tabella incassi

ALTER TABLE incassi ADD identificativo VARCHAR2(35 CHAR);
UPDATE incassi SET identificativo = trn;
ALTER TABLE incassi MODIFY (identificativo NOT NULL);

ALTER TABLE incassi ADD stato VARCHAR2(35 CHAR);
UPDATE incassi SET stato = 'ACQUISITO';
ALTER TABLE incassi MODIFY (stato NOT NULL);

ALTER TABLE incassi ADD iuv VARCHAR2(35 CHAR);
ALTER TABLE incassi ADD cod_flusso_rendicontazione VARCHAR2(35 CHAR);
ALTER TABLE incassi ADD descrizione_stato VARCHAR2(255 CHAR);

ALTER TABLE incassi DROP CONSTRAINT unique_incassi_1;
ALTER TABLE incassi ADD CONSTRAINT unique_incassi_1 UNIQUE (cod_dominio,identificativo);

ALTER TABLE incassi MODIFY (causale NULL);

insert into sonde(nome, classe, soglia_warn, soglia_error) values ('riconciliazioni', 'org.openspcoop2.utils.sonde.impl.SondaBatch', 86400000, 172800000);
insert into sonde(nome, classe, soglia_warn, soglia_error) values ('check-riconciliazioni', 'org.openspcoop2.utils.sonde.impl.SondaCoda', 10, 100);


-- 14/07/2021 Batch per la chiusura delle RPT scadute
insert into sonde(nome, classe, soglia_warn, soglia_error) values ('rpt-scadute', 'org.openspcoop2.utils.sonde.impl.SondaBatch', 86400000, 172800000);
insert into sonde(nome, classe, soglia_warn, soglia_error) values ('check-rpt-scadute', 'org.openspcoop2.utils.sonde.impl.SondaCoda', 10, 100);

-- 20/07/2021 Fix anomalie per rendicontazione senza RT

update rendicontazioni set stato='OK', anomalie=null where dbms_lob.compare(anomalie, '007101#Il pagamento riferito dalla rendicontazione non risulta presente in base dati.') = 0 and esito=9;
update fr set stato='ACCETTATA', descrizione_stato = null where stato='ANOMALA' and id not in (select fr.id from fr join rendicontazioni on rendicontazioni.id_fr=fr.id where fr.stato='ANOMALA' and rendicontazioni.stato='ANOMALA');


-- 21/07/2021 Identificativo dominio nel singolo versamento per gestire le pendenze multibeneficiario
ALTER TABLE singoli_versamenti ADD id_dominio NUMBER;
ALTER TABLE singoli_versamenti ADD CONSTRAINT fk_sng_id_dominio FOREIGN KEY (id_dominio) REFERENCES domini(id);

-- 15/10/2021 Identificativo RPT nella tabella notificheAppIo per gestire l'invio delle ricevute di pagamento
ALTER TABLE notifiche_app_io ADD id_rpt NUMBER;
ALTER TABLE notifiche_app_io ADD CONSTRAINT fk_nai_id_rpt FOREIGN KEY (id_rpt) REFERENCES rpt(id);

-- 20/12/2021 Patch per gestione delle rendicontazioni che non venivano messe in stato anomala quando non viene trovato il versamento corrispondente.
UPDATE rendicontazioni SET stato='ANOMALA', anomalie='007111#Il versamento risulta sconosciuto' WHERE stato='OK' AND id_singolo_versamento IS null;

-- 21/12/2021 Patch per la gestione del riferimento al pagamento di una rendicontazione che arriva prima della ricevuta.
UPDATE rendicontazioni SET id_pagamento = (SELECT pagamenti.id 
	FROM fr, pagamenti 
	WHERE fr.id=rendicontazioni.id_fr 
	AND pagamenti.cod_dominio=fr.cod_dominio 
	AND rendicontazioni.iuv=pagamenti.iuv 
	AND rendicontazioni.iur=pagamenti.iur 
	AND rendicontazioni.id_pagamento IS NULL);

UPDATE rendicontazioni SET stato='OK', anomalie=null where dbms_lob.compare(anomalie, to_clob('007111#Il versamento risulta sconosciuto')) = 0 AND id_singolo_versamento IS NOT null;

-- 30/12/2021 Patch rendicontazioni con riferimenti assenti

update rendicontazioni set id_singolo_versamento = 
	(SELECT pagamenti.id_singolo_versamento from pagamenti where rendicontazioni.id_pagamento=pagamenti.id and rendicontazioni.id_singolo_versamento is null);

UPDATE rendicontazioni SET stato='OK', anomalie=null where stato='ANOMALA' and dbms_lob.compare(anomalie, to_clob('007111#Il versamento risulta sconosciuto')) = 0 AND id_singolo_versamento IS not null;

update rendicontazioni set id_singolo_versamento= (SELECT singoli_versamenti.id
        FROM fr, versamenti, domini, singoli_versamenti
        WHERE fr.id=rendicontazioni.id_fr
        AND fr.cod_dominio=domini.cod_dominio
        AND domini.id=versamenti.id_dominio AND rendicontazioni.iuv=versamenti.iuv_versamento and singoli_versamenti.id_versamento=versamenti.id and rendicontazioni.id_singolo_versamento is null);

update rendicontazioni set stato='ANOMALA', anomalie='007101#Il pagamento riferito dalla rendicontazione non risulta presente in base dati.' where id_pagamento is null and esito=0;


-- 25/01/2022 Flusso Rendicontazione univoco per dominio
ALTER TABLE fr DROP CONSTRAINT unique_fr_1;
ALTER TABLE fr ADD CONSTRAINT unique_fr_1 UNIQUE (cod_flusso,data_ora_flusso);


-- 27/01/2022 Indice su id_fr nella tabella eventi
CREATE INDEX idx_evt_fk_fr ON eventi (id_fr);


-- CREAZIONE VISTE NELLA VERSIONE FINALE 3.5

CREATE VIEW versamenti_incassi AS
    SELECT
    versamenti.id,
    versamenti.cod_versamento_ente,
    versamenti.nome,
    versamenti.importo_totale,
    versamenti.stato_versamento,
    versamenti.descrizione_stato,
    versamenti.aggiornabile,
    versamenti.data_creazione,
    versamenti.data_validita,
    versamenti.data_scadenza,
    versamenti.data_ora_ultimo_aggiornamento,
    versamenti.causale_versamento,
    versamenti.debitore_tipo,
    versamenti.debitore_identificativo,
    versamenti.debitore_anagrafica,
    versamenti.debitore_indirizzo,
    versamenti.debitore_civico,
    versamenti.debitore_cap,
    versamenti.debitore_localita,
    versamenti.debitore_provincia,
    versamenti.debitore_nazione,
    versamenti.debitore_email,
    versamenti.debitore_telefono,
    versamenti.debitore_cellulare,
    versamenti.debitore_fax,
    versamenti.tassonomia_avviso,
    versamenti.tassonomia,
    versamenti.cod_lotto,
    versamenti.cod_versamento_lotto,
    versamenti.cod_anno_tributario,
    versamenti.cod_bundlekey,
    versamenti.dati_allegati,
    versamenti.incasso,
    versamenti.anomalie,
    versamenti.iuv_versamento,
    versamenti.numero_avviso,
    versamenti.id_dominio,
    versamenti.id_tipo_versamento,
    versamenti.id_tipo_versamento_dominio,
    versamenti.id_uo,
    versamenti.id_applicazione,
    versamenti.divisione,
    versamenti.direzione,	
    versamenti.id_sessione,
    versamenti.ack,
    versamenti.anomalo,
    versamenti.data_pagamento,
    versamenti.importo_pagato,
    versamenti.importo_incassato,
    versamenti.stato_pagamento,
    versamenti.iuv_pagamento,
    versamenti.src_iuv,
    versamenti.src_debitore_identificativo,
    versamenti.cod_rata,
    documenti.cod_documento,
    versamenti.tipo,
    versamenti.proprieta,
    documenti.descrizione AS doc_descrizione,
    (CASE WHEN versamenti.stato_versamento = 'NON_ESEGUITO' AND versamenti.data_validita > CURRENT_DATE THEN 0 ELSE 1 END) AS smart_order_rank,
    (ABS((date_to_unix_for_smart_order(CURRENT_DATE) * 1000) - (date_to_unix_for_smart_order(COALESCE(versamenti.data_pagamento, versamenti.data_validita, versamenti.data_creazione))) *1000)) AS smart_order_date
    FROM versamenti LEFT JOIN documenti ON versamenti.id_documento = documenti.id;

-- VISTE REPORTISTICA

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
    singoli_versamenti.id_tributo AS id_tributo,
    versamenti.debitore_anagrafica AS debitore_anagrafica,
    fr.cod_psp AS cod_psp,
    fr.ragione_sociale_psp AS ragione_sociale_psp,
    versamenti.cod_rata AS cod_rata,
    versamenti.id_documento AS id_documento,
    versamenti.causale_versamento AS causale_versamento,
    versamenti.importo_totale AS importo_versamento,
    versamenti.numero_avviso AS numero_avviso,
    versamenti.iuv_pagamento AS iuv_pagamento,
    versamenti.data_scadenza AS data_scadenza,
    versamenti.data_creazione AS data_creazione,
    singoli_versamenti.contabilita AS contabilita
   FROM fr
     JOIN rendicontazioni ON rendicontazioni.id_fr = fr.id
     JOIN versamenti ON versamenti.iuv_versamento = rendicontazioni.iuv
     JOIN domini ON versamenti.id_dominio = domini.id AND domini.cod_dominio = fr.cod_dominio
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
    singoli_versamenti.id_tributo AS id_tributo,
    versamenti.debitore_anagrafica AS debitore_anagrafica,
    fr.cod_psp AS cod_psp,
    fr.ragione_sociale_psp AS ragione_sociale_psp,
    versamenti.cod_rata AS cod_rata,
    versamenti.id_documento AS id_documento,
    versamenti.causale_versamento AS causale_versamento,
    versamenti.importo_totale AS importo_versamento,
    versamenti.numero_avviso AS numero_avviso,
    versamenti.iuv_pagamento AS iuv_pagamento,
    versamenti.data_scadenza AS data_scadenza,
    versamenti.data_creazione AS data_creazione,
    singoli_versamenti.contabilita AS contabilita
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
    tipi_tributo.cod_tributo AS cod_entrata,
    tipi_versamento.descrizione AS descr_tipo_versamento,
    a.debitore_anagrafica,
    a.cod_psp,
    a.ragione_sociale_psp,
    a.cod_rata,
    a.id_documento,
    a.causale_versamento,
    a.importo_versamento,
    a.numero_avviso,
    a.iuv_pagamento,
    a.data_scadenza,
    a.data_creazione,
    a.contabilita
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
            v_riscossioni_senza_rpt.id_tributo,
		    v_riscossioni_senza_rpt.debitore_anagrafica,
		    v_riscossioni_senza_rpt.cod_psp,
		    v_riscossioni_senza_rpt.ragione_sociale_psp,
		    v_riscossioni_senza_rpt.cod_rata,
		    v_riscossioni_senza_rpt.id_documento,
		    v_riscossioni_senza_rpt.causale_versamento,
		    v_riscossioni_senza_rpt.importo_versamento,
		    v_riscossioni_senza_rpt.numero_avviso,
		    v_riscossioni_senza_rpt.iuv_pagamento,
		    v_riscossioni_senza_rpt.data_scadenza,
		    v_riscossioni_senza_rpt.data_creazione,
		    v_riscossioni_senza_rpt.contabilita
           FROM v_riscossioni_senza_rpt
        UNION ALL
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
            v_riscossioni_con_rpt.id_tributo,
		    v_riscossioni_con_rpt.debitore_anagrafica,
		    v_riscossioni_con_rpt.cod_psp,
		    v_riscossioni_con_rpt.ragione_sociale_psp,
		    v_riscossioni_con_rpt.cod_rata,
		    v_riscossioni_con_rpt.id_documento,
		    v_riscossioni_con_rpt.causale_versamento,
		    v_riscossioni_con_rpt.importo_versamento,
		    v_riscossioni_con_rpt.numero_avviso,
		    v_riscossioni_con_rpt.iuv_pagamento,
		    v_riscossioni_con_rpt.data_scadenza,
		    v_riscossioni_con_rpt.data_creazione,
		    v_riscossioni_con_rpt.contabilita
           FROM v_riscossioni_con_rpt) a
     JOIN applicazioni ON a.id_applicazione = applicazioni.id 
     LEFT JOIN tipi_versamento ON a.id_tipo_versamento = tipi_versamento.id 
     LEFT JOIN tributi ON a.id_tributo = tributi.id 
     LEFT JOIN tipi_tributo ON tributi.id_tipo_tributo = tipi_tributo.id;





-- Vista pagamenti_portale

CREATE VIEW v_pagamenti_portale AS
 SELECT 
  pagamenti_portale.cod_canale,
  pagamenti_portale.nome,
  pagamenti_portale.importo,
  pagamenti_portale.versante_identificativo,
  pagamenti_portale.src_versante_identificativo,
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
  pagamenti_portale.severita,
  versamenti.debitore_identificativo as debitore_identificativo,
  versamenti.src_debitore_identificativo as src_debitore_identificativo,
  versamenti.id_dominio as id_dominio, 
  versamenti.id_uo as id_uo, 
  versamenti.id_tipo_versamento as id_tipo_versamento,
  versamenti.cod_versamento_ente as cod_versamento_ente,
  versamenti.src_iuv as src_iuv
FROM pagamenti_portale 
JOIN pag_port_versamenti ON pagamenti_portale.id = pag_port_versamenti.id_pagamento_portale 
JOIN versamenti ON versamenti.id=pag_port_versamenti.id_versamento;

-- Vista Eventi per Versamenti

CREATE VIEW v_eventi_vers_rendicontazioni AS (
        SELECT DISTINCT 
               versamenti.cod_versamento_ente as cod_versamento_ente,
               applicazioni.cod_applicazione as cod_applicazione,
               eventi.id
        FROM eventi 
        JOIN rendicontazioni ON rendicontazioni.id_fr = eventi.id_fr
        JOIN pagamenti ON pagamenti.id = rendicontazioni.id_pagamento
        JOIN singoli_versamenti ON pagamenti.id_singolo_versamento=singoli_versamenti.id
        JOIN versamenti ON singoli_versamenti.id_versamento=versamenti.id
        JOIN applicazioni ON versamenti.id_applicazione = applicazioni.id
);

CREATE VIEW v_eventi_vers_pagamenti AS (
	SELECT DISTINCT 
               versamenti.cod_versamento_ente as cod_versamento_ente,
               applicazioni.cod_applicazione as cod_applicazione,
               eventi.id
        FROM versamenti
        JOIN applicazioni ON versamenti.id_applicazione = applicazioni.id
        JOIN pag_port_versamenti ON versamenti.id = pag_port_versamenti.id_versamento
        JOIN pagamenti_portale ON pag_port_versamenti.id_pagamento_portale = pagamenti_portale.id
        JOIN eventi ON eventi.id_sessione = pagamenti_portale.id_sessione
);

CREATE VIEW v_eventi_vers_riconciliazioni AS (
        SELECT DISTINCT 
               versamenti.cod_versamento_ente as cod_versamento_ente,
               applicazioni.cod_applicazione as cod_applicazione,
               eventi.id
        FROM eventi
        JOIN pagamenti ON pagamenti.id_incasso = eventi.id_incasso
        JOIN singoli_versamenti ON pagamenti.id_singolo_versamento=singoli_versamenti.id
        JOIN versamenti ON singoli_versamenti.id_versamento=versamenti.id
        JOIN applicazioni ON versamenti.id_applicazione = applicazioni.id
);

CREATE VIEW v_eventi_vers_tracciati AS (
        SELECT DISTINCT 
               versamenti.cod_versamento_ente as cod_versamento_ente,
               applicazioni.cod_applicazione as cod_applicazione,
               eventi.id
        FROM eventi
        JOIN operazioni ON operazioni.id_tracciato = eventi.id_tracciato
        JOIN versamenti ON operazioni.id_applicazione = versamenti.id_applicazione AND operazioni.cod_versamento_ente = versamenti.cod_versamento_ente
        JOIN applicazioni ON versamenti.id_applicazione = applicazioni.id
);


CREATE VIEW v_eventi_vers_base AS (
        SELECT DISTINCT 
               cod_versamento_ente,
               cod_applicazione,
               id
        FROM eventi
        UNION SELECT * FROM v_eventi_vers_pagamenti
        UNION SELECT * FROM v_eventi_vers_rendicontazioni
        UNION SELECT * FROM v_eventi_vers_riconciliazioni
        UNION SELECT * FROM v_eventi_vers_tracciati
        );


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
               v_eventi_vers_base.cod_versamento_ente,
               v_eventi_vers_base.cod_applicazione,
               eventi.iuv,
               eventi.cod_dominio,
               eventi.ccp,
               eventi.id_sessione,
	       eventi.severita,
               eventi.id
               FROM v_eventi_vers_base JOIN eventi ON v_eventi_vers_base.id = eventi.id
         );  

-- Vista Rendicontazioni

CREATE VIEW v_rendicontazioni_ext AS
 SELECT fr.cod_psp AS fr_cod_psp,
    fr.cod_dominio AS fr_cod_dominio,
    fr.cod_flusso AS fr_cod_flusso,
    fr.stato AS fr_stato,
    fr.descrizione_stato AS fr_descrizione_stato,
    fr.iur AS fr_iur,
    fr.data_ora_flusso AS fr_data_ora_flusso,
    fr.data_regolamento AS fr_data_regolamento,
    fr.data_acquisizione AS fr_data_acquisizione,
    fr.numero_pagamenti AS fr_numero_pagamenti,
    fr.importo_totale_pagamenti AS fr_importo_totale_pagamenti,
    fr.cod_bic_riversamento AS fr_cod_bic_riversamento,
    fr.id AS fr_id,
    fr.id_incasso AS fr_id_incasso,
    fr.ragione_sociale_psp AS fr_ragione_sociale_psp,
    fr.ragione_sociale_dominio AS fr_ragione_sociale_dominio,
    fr.obsoleto AS fr_obsoleto,
    rendicontazioni.iuv AS rnd_iuv,
    rendicontazioni.iur AS rnd_iur,
    rendicontazioni.indice_dati AS rnd_indice_dati,
    rendicontazioni.importo_pagato AS rnd_importo_pagato,
    rendicontazioni.esito AS rnd_esito,
    rendicontazioni.data AS rnd_data,
    rendicontazioni.stato AS rnd_stato,
    rendicontazioni.anomalie AS rnd_anomalie,
    rendicontazioni.id,
    rendicontazioni.id_pagamento AS rnd_id_pagamento,
    singoli_versamenti.cod_singolo_versamento_ente AS sng_cod_sing_vers_ente,
    singoli_versamenti.importo_singolo_versamento AS sng_importo_singolo_versamento,
    singoli_versamenti.descrizione AS sng_descrizione,
    singoli_versamenti.dati_allegati AS sng_dati_allegati,
    singoli_versamenti.stato_singolo_versamento AS sng_stato_singolo_versamento,
    singoli_versamenti.indice_dati AS sng_indice_dati,
    singoli_versamenti.descrizione_causale_rpt AS sng_descrizione_causale_rpt,
    singoli_versamenti.contabilita AS sng_contabilita,
    singoli_versamenti.id_tributo AS sng_id_tributo,
    versamenti.cod_versamento_ente AS vrs_cod_versamento_ente,
    versamenti.importo_totale AS vrs_importo_totale,
    versamenti.debitore_identificativo AS vrs_debitore_identificativo,
    versamenti.debitore_anagrafica AS vrs_debitore_anagrafica,
    versamenti.tassonomia AS vrs_tassonomia,
    versamenti.divisione AS vrs_divisione,
    versamenti.direzione AS vrs_direzione,
    versamenti.id_tipo_versamento AS vrs_id_tipo_versamento,
    versamenti.id_tipo_versamento_dominio AS vrs_id_tipo_versamento_dominio,
    versamenti.id_dominio AS vrs_id_dominio,
    versamenti.id_uo AS vrs_id_uo,
    versamenti.id_applicazione AS vrs_id_applicazione,
    versamenti.id AS vrs_id,
    versamenti.nome AS vrs_nome,
    versamenti.stato_versamento AS vrs_stato_versamento,
    versamenti.descrizione_stato AS vrs_descrizione_stato,
    versamenti.aggiornabile AS vrs_aggiornabile,
    versamenti.data_creazione AS vrs_data_creazione,
    versamenti.data_validita AS vrs_data_validita,
    versamenti.data_scadenza AS vrs_data_scadenza,
    versamenti.data_ora_ultimo_aggiornamento AS vrs_data_ora_ultimo_agg,
    versamenti.causale_versamento AS vrs_causale_versamento,
    versamenti.debitore_tipo AS vrs_debitore_tipo,
    versamenti.debitore_indirizzo AS vrs_debitore_indirizzo,
    versamenti.debitore_civico AS vrs_debitore_civico,
    versamenti.debitore_cap AS vrs_debitore_cap,
    versamenti.debitore_localita AS vrs_debitore_localita,
    versamenti.debitore_provincia AS vrs_debitore_provincia,
    versamenti.debitore_nazione AS vrs_debitore_nazione,
    versamenti.debitore_email AS vrs_debitore_email,
    versamenti.debitore_telefono AS vrs_debitore_telefono,
    versamenti.debitore_cellulare AS vrs_debitore_cellulare,
    versamenti.debitore_fax AS vrs_debitore_fax,
    versamenti.tassonomia_avviso AS vrs_tassonomia_avviso,
    versamenti.cod_lotto AS vrs_cod_lotto,
    versamenti.cod_versamento_lotto AS vrs_cod_versamento_lotto,
    versamenti.cod_anno_tributario AS vrs_cod_anno_tributario,
    versamenti.cod_bundlekey AS vrs_cod_bundlekey,
    versamenti.dati_allegati AS vrs_dati_allegati,
    versamenti.incasso AS vrs_incasso,
    versamenti.anomalie AS vrs_anomalie,
    versamenti.iuv_versamento AS vrs_iuv_versamento,
    versamenti.numero_avviso AS vrs_numero_avviso,
    versamenti.ack AS vrs_ack,
    versamenti.anomalo AS vrs_anomalo,
    versamenti.id_sessione AS vrs_id_sessione,
    versamenti.data_pagamento AS vrs_data_pagamento,
    versamenti.importo_pagato AS vrs_importo_pagato,
    versamenti.importo_incassato AS vrs_importo_incassato,
    versamenti.stato_pagamento AS vrs_stato_pagamento,
    versamenti.iuv_pagamento AS vrs_iuv_pagamento,
    versamenti.cod_rata as vrs_cod_rata,
    versamenti.id_documento as vrs_id_documento,
    versamenti.tipo as vrs_tipo,
    versamenti.proprieta as vrs_proprieta,
    pagamenti.cod_dominio AS pag_cod_dominio,             
	pagamenti.iuv AS pag_iuv,                     
	pagamenti.indice_dati AS pag_indice_dati,             
	pagamenti.importo_pagato AS pag_importo_pagato,          
	pagamenti.data_acquisizione AS pag_data_acquisizione,       
	pagamenti.iur AS pag_iur,                     
	pagamenti.data_pagamento AS pag_data_pagamento,          
	pagamenti.commissioni_psp AS pag_commissioni_psp,         
	pagamenti.tipo_allegato AS pag_tipo_allegato,           
	pagamenti.allegato AS pag_allegato,                
	pagamenti.data_acquisizione_revoca AS pag_data_acquisizione_revoca,
	pagamenti.causale_revoca AS pag_causale_revoca,          
	pagamenti.dati_revoca AS pag_dati_revoca,             
	pagamenti.importo_revocato AS pag_importo_revocato,        
	pagamenti.esito_revoca AS pag_esito_revoca,            
	pagamenti.dati_esito_revoca AS pag_dati_esito_revoca,       
	pagamenti.stato AS pag_stato,                  
	pagamenti.tipo AS pag_tipo,                  
	rpt.iuv AS rpt_iuv,
	rpt.ccp AS rpt_ccp,
	incassi.trn AS rnc_trn
   FROM fr
     JOIN rendicontazioni ON rendicontazioni.id_fr = fr.id
     LEFT JOIN singoli_versamenti ON rendicontazioni.id_singolo_versamento = singoli_versamenti.id
     LEFT JOIN versamenti ON versamenti.id = singoli_versamenti.id_versamento 
     LEFT JOIN pagamenti on rendicontazioni.id_pagamento = pagamenti.id 
     LEFT JOIN rpt on pagamenti.id_rpt = rpt.id
     LEFT JOIN incassi on pagamenti.id_incasso = incassi.id;

-- Vista Rpt Versamento
CREATE VIEW v_rpt_versamenti AS
 SELECT 
rpt.cod_carrello as cod_carrello,                   
rpt.iuv as iuv,                            
rpt.ccp as ccp,                            
rpt.cod_dominio as cod_dominio,                    
rpt.cod_msg_richiesta as cod_msg_richiesta,              
rpt.data_msg_richiesta as data_msg_richiesta,             
rpt.stato as stato,                          
rpt.descrizione_stato as descrizione_stato,              
rpt.cod_sessione as cod_sessione,                   
rpt.cod_sessione_portale as cod_sessione_portale,           
rpt.psp_redirect_url as psp_redirect_url,               
rpt.xml_rpt as xml_rpt,                        
rpt.data_aggiornamento_stato as data_aggiornamento_stato,       
rpt.callback_url as callback_url,                   
rpt.modello_pagamento as modello_pagamento,              
rpt.cod_msg_ricevuta as cod_msg_ricevuta,               
rpt.data_msg_ricevuta as data_msg_ricevuta,              
rpt.cod_esito_pagamento as cod_esito_pagamento,            
rpt.importo_totale_pagato as importo_totale_pagato,          
rpt.xml_rt as xml_rt,                         
rpt.cod_canale as cod_canale,                     
rpt.cod_psp as cod_psp,                        
rpt.cod_intermediario_psp as cod_intermediario_psp,          
rpt.tipo_versamento as tipo_versamento,                
rpt.tipo_identificativo_attestante as tipo_identificativo_attestante, 
rpt.identificativo_attestante as identificativo_attestante,      
rpt.denominazione_attestante as denominazione_attestante,       
rpt.cod_stazione as cod_stazione,                   
rpt.cod_transazione_rpt as cod_transazione_rpt,            
rpt.cod_transazione_rt as cod_transazione_rt,             
rpt.stato_conservazione as stato_conservazione,            
rpt.descrizione_stato_cons as descrizione_stato_cons,         
rpt.data_conservazione as data_conservazione,             
rpt.bloccante as bloccante,  
rpt.versione as versione,                      
rpt.id as id,                             
rpt.id_pagamento_portale as id_pagamento_portale, 
    versamenti.cod_versamento_ente AS vrs_cod_versamento_ente,
    versamenti.importo_totale AS vrs_importo_totale,
    versamenti.debitore_identificativo AS vrs_debitore_identificativo,
    versamenti.debitore_anagrafica AS vrs_debitore_anagrafica,
    versamenti.tassonomia AS vrs_tassonomia,
    versamenti.divisione AS vrs_divisione,
    versamenti.direzione AS vrs_direzione,
    versamenti.id_tipo_versamento AS vrs_id_tipo_versamento,
    versamenti.id_tipo_versamento_dominio AS vrs_id_tipo_versamento_dominio,
    versamenti.id_dominio AS vrs_id_dominio,
    versamenti.id_uo AS vrs_id_uo,
    versamenti.id_applicazione AS vrs_id_applicazione,
    versamenti.id AS vrs_id,
    versamenti.nome AS vrs_nome,
    versamenti.stato_versamento AS vrs_stato_versamento,
    versamenti.descrizione_stato AS vrs_descrizione_stato,
    versamenti.aggiornabile AS vrs_aggiornabile,
    versamenti.data_creazione AS vrs_data_creazione,
    versamenti.data_validita AS vrs_data_validita,
    versamenti.data_scadenza AS vrs_data_scadenza,
    versamenti.data_ora_ultimo_aggiornamento AS vrs_data_ora_ultimo_agg,
    versamenti.causale_versamento AS vrs_causale_versamento,
    versamenti.debitore_tipo AS vrs_debitore_tipo,
    versamenti.debitore_indirizzo AS vrs_debitore_indirizzo,
    versamenti.debitore_civico AS vrs_debitore_civico,
    versamenti.debitore_cap AS vrs_debitore_cap,
    versamenti.debitore_localita AS vrs_debitore_localita,
    versamenti.debitore_provincia AS vrs_debitore_provincia,
    versamenti.debitore_nazione AS vrs_debitore_nazione,
    versamenti.debitore_email AS vrs_debitore_email,
    versamenti.debitore_telefono AS vrs_debitore_telefono,
    versamenti.debitore_cellulare AS vrs_debitore_cellulare,
    versamenti.debitore_fax AS vrs_debitore_fax,
    versamenti.tassonomia_avviso AS vrs_tassonomia_avviso,
    versamenti.cod_lotto AS vrs_cod_lotto,
    versamenti.cod_versamento_lotto AS vrs_cod_versamento_lotto,
    versamenti.cod_anno_tributario AS vrs_cod_anno_tributario,
    versamenti.cod_bundlekey AS vrs_cod_bundlekey,
    versamenti.dati_allegati AS vrs_dati_allegati,
    versamenti.incasso AS vrs_incasso,
    versamenti.anomalie AS vrs_anomalie,
    versamenti.iuv_versamento AS vrs_iuv_versamento,
    versamenti.numero_avviso AS vrs_numero_avviso,
    versamenti.ack AS vrs_ack,
    versamenti.anomalo AS vrs_anomalo,
    versamenti.id_sessione AS vrs_id_sessione,
    versamenti.data_pagamento AS vrs_data_pagamento,
    versamenti.importo_pagato AS vrs_importo_pagato,
    versamenti.importo_incassato AS vrs_importo_incassato,
    versamenti.stato_pagamento AS vrs_stato_pagamento,
    versamenti.iuv_pagamento AS vrs_iuv_pagamento,
    versamenti.src_debitore_identificativo as vrs_src_debitore_identificativ,
    versamenti.cod_rata as vrs_cod_rata,
    versamenti.id_documento as vrs_id_documento,
    versamenti.tipo as vrs_tipo,
    versamenti.proprieta as vrs_proprieta
FROM rpt JOIN versamenti ON versamenti.id = rpt.id_versamento;

-- Vista Versamenti Documenti
CREATE VIEW v_versamenti AS 
SELECT versamenti.id,
    versamenti.cod_versamento_ente,
    versamenti.nome,
    versamenti.importo_totale,
    versamenti.stato_versamento,
    versamenti.descrizione_stato,
    versamenti.aggiornabile,
    versamenti.tassonomia,
    versamenti.tassonomia_avviso,
    versamenti.data_creazione,
    versamenti.data_validita,
    versamenti.data_scadenza,
    versamenti.data_ora_ultimo_aggiornamento,
    versamenti.causale_versamento,
    versamenti.debitore_identificativo,
    versamenti.debitore_tipo,
    versamenti.debitore_anagrafica,
    versamenti.debitore_indirizzo,
    versamenti.debitore_civico,
    versamenti.debitore_cap,
    versamenti.debitore_localita,
    versamenti.debitore_provincia,
    versamenti.debitore_nazione,
    versamenti.debitore_email,
    versamenti.debitore_telefono,
    versamenti.debitore_cellulare,
    versamenti.debitore_fax,
    versamenti.cod_lotto,
    versamenti.cod_versamento_lotto,
    versamenti.cod_anno_tributario,
    versamenti.cod_bundlekey,
    versamenti.dati_allegati,
    versamenti.incasso,
    versamenti.anomalie,
    versamenti.iuv_versamento,
    versamenti.numero_avviso,
    versamenti.ack,
    versamenti.anomalo,
    versamenti.direzione,
    versamenti.divisione,
    versamenti.id_sessione,
    versamenti.importo_pagato,
    versamenti.data_pagamento,
    versamenti.importo_incassato,
    versamenti.stato_pagamento,
    versamenti.iuv_pagamento,
    versamenti.src_debitore_identificativo,
    versamenti.src_iuv,
    versamenti.cod_rata,
    versamenti.tipo,
    versamenti.data_notifica_avviso,
    versamenti.avviso_notificato,
    versamenti.avv_mail_data_prom_scadenza,
    versamenti.avv_mail_prom_scad_notificato,
    versamenti.avv_app_io_data_prom_scadenza,
    versamenti.avv_app_io_prom_scad_notificat,
    versamenti.id_applicazione,
    versamenti.id_dominio,
    versamenti.id_uo,
    versamenti.id_tipo_versamento,
    versamenti.id_tipo_versamento_dominio,
    versamenti.id_documento,
    versamenti.proprieta,
    documenti.cod_documento,
    documenti.descrizione AS doc_descrizione
    FROM versamenti LEFT JOIN documenti ON versamenti.id_documento = documenti.id;



   
-- Vista Pagamenti/Riscossioni

CREATE VIEW v_pagamenti AS
SELECT 
	pagamenti.id AS id,
	pagamenti.cod_dominio AS cod_dominio,             
	pagamenti.iuv AS iuv,                     
	pagamenti.indice_dati AS indice_dati,             
	pagamenti.importo_pagato AS importo_pagato,          
	pagamenti.data_acquisizione AS data_acquisizione,       
	pagamenti.iur AS iur,                     
	pagamenti.data_pagamento AS data_pagamento,          
	pagamenti.commissioni_psp AS commissioni_psp,         
	pagamenti.tipo_allegato AS tipo_allegato,           
	pagamenti.allegato AS allegato,                
	pagamenti.data_acquisizione_revoca AS data_acquisizione_revoca,
	pagamenti.causale_revoca AS causale_revoca,          
	pagamenti.dati_revoca AS dati_revoca,             
	pagamenti.importo_revocato AS importo_revocato,        
	pagamenti.esito_revoca AS esito_revoca,            
	pagamenti.dati_esito_revoca AS dati_esito_revoca,       
	pagamenti.stato AS stato,                  
	pagamenti.tipo AS tipo,                  
	pagamenti.id_rpt AS id_rpt,                  
	pagamenti.id_singolo_versamento AS id_singolo_versamento,                  
	pagamenti.id_rr AS id_rr,                  
	pagamenti.id_incasso AS id_incasso,       
	versamenti.cod_versamento_ente AS vrs_cod_versamento_ente,      
	versamenti.tassonomia AS vrs_tassonomia,
	versamenti.divisione AS vrs_divisione,
	versamenti.direzione AS vrs_direzione,
	versamenti.id_tipo_versamento AS vrs_id_tipo_versamento,
	versamenti.id_tipo_versamento_dominio AS vrs_id_tipo_versamento_dominio,
	versamenti.id_dominio AS vrs_id_dominio,
	versamenti.id_uo AS vrs_id_uo,
	versamenti.id_applicazione AS vrs_id_applicazione,
	versamenti.id AS vrs_id,  
	versamenti.id_documento as vrs_id_documento,  
	singoli_versamenti.cod_singolo_versamento_ente AS sng_cod_sing_vers_ente,
	rpt.iuv AS rpt_iuv,
	rpt.ccp AS rpt_ccp,
	incassi.trn AS rnc_trn
	FROM pagamenti JOIN singoli_versamenti ON pagamenti.id_singolo_versamento = singoli_versamenti.id
	     JOIN versamenti ON singoli_versamenti.id_versamento = versamenti.id JOIN rpt ON pagamenti.id_rpt = rpt.id LEFT JOIN incassi ON pagamenti.id_incasso = incassi.id;
	     

-- Vista Versamenti non rendicontati	     
CREATE VIEW v_vrs_non_rnd AS 
 SELECT singoli_versamenti.cod_singolo_versamento_ente AS sng_cod_sing_vers_ente,
    singoli_versamenti.importo_singolo_versamento AS sng_importo_singolo_versamento,
    singoli_versamenti.descrizione AS sng_descrizione,
    singoli_versamenti.dati_allegati AS sng_dati_allegati,
    singoli_versamenti.stato_singolo_versamento AS sng_stato_singolo_versamento,
    singoli_versamenti.indice_dati AS sng_indice_dati,
    singoli_versamenti.descrizione_causale_rpt AS sng_descrizione_causale_rpt,
    singoli_versamenti.contabilita AS sng_contabilita,
    singoli_versamenti.id_tributo AS sng_id_tributo,
    versamenti.cod_versamento_ente AS vrs_cod_versamento_ente,
    versamenti.importo_totale AS vrs_importo_totale,
    versamenti.debitore_identificativo AS vrs_debitore_identificativo,
    versamenti.debitore_anagrafica AS vrs_debitore_anagrafica,
    versamenti.tassonomia AS vrs_tassonomia,
    versamenti.divisione AS vrs_divisione,
    versamenti.direzione AS vrs_direzione,
    versamenti.id_tipo_versamento AS vrs_id_tipo_versamento,
    versamenti.id_tipo_versamento_dominio AS vrs_id_tipo_versamento_dominio,
    versamenti.id_dominio AS vrs_id_dominio,
    versamenti.id_uo AS vrs_id_uo,
    versamenti.id_applicazione AS vrs_id_applicazione,
    versamenti.id AS vrs_id,
    versamenti.nome AS vrs_nome,
    versamenti.stato_versamento AS vrs_stato_versamento,
    versamenti.descrizione_stato AS vrs_descrizione_stato,
    versamenti.aggiornabile AS vrs_aggiornabile,
    versamenti.data_creazione AS vrs_data_creazione,
    versamenti.data_validita AS vrs_data_validita,
    versamenti.data_scadenza AS vrs_data_scadenza,
    versamenti.data_ora_ultimo_aggiornamento AS vrs_data_ora_ultimo_agg,
    versamenti.causale_versamento AS vrs_causale_versamento,
    versamenti.debitore_tipo AS vrs_debitore_tipo,
    versamenti.debitore_indirizzo AS vrs_debitore_indirizzo,
    versamenti.debitore_civico AS vrs_debitore_civico,
    versamenti.debitore_cap AS vrs_debitore_cap,
    versamenti.debitore_localita AS vrs_debitore_localita,
    versamenti.debitore_provincia AS vrs_debitore_provincia,
    versamenti.debitore_nazione AS vrs_debitore_nazione,
    versamenti.debitore_email AS vrs_debitore_email,
    versamenti.debitore_telefono AS vrs_debitore_telefono,
    versamenti.debitore_cellulare AS vrs_debitore_cellulare,
    versamenti.debitore_fax AS vrs_debitore_fax,
    versamenti.tassonomia_avviso AS vrs_tassonomia_avviso,
    versamenti.cod_lotto AS vrs_cod_lotto,
    versamenti.cod_versamento_lotto AS vrs_cod_versamento_lotto,
    versamenti.cod_anno_tributario AS vrs_cod_anno_tributario,
    versamenti.cod_bundlekey AS vrs_cod_bundlekey,
    versamenti.dati_allegati AS vrs_dati_allegati,
    versamenti.incasso AS vrs_incasso,
    versamenti.anomalie AS vrs_anomalie,
    versamenti.iuv_versamento AS vrs_iuv_versamento,
    versamenti.numero_avviso AS vrs_numero_avviso,
    versamenti.ack AS vrs_ack,
    versamenti.anomalo AS vrs_anomalo,
    versamenti.id_sessione AS vrs_id_sessione,
    versamenti.data_pagamento AS vrs_data_pagamento,
    versamenti.importo_pagato AS vrs_importo_pagato,
    versamenti.importo_incassato AS vrs_importo_incassato,
    versamenti.stato_pagamento AS vrs_stato_pagamento,
    versamenti.iuv_pagamento AS vrs_iuv_pagamento,
    versamenti.cod_rata AS vrs_cod_rata,
    versamenti.id_documento AS vrs_id_documento,
    versamenti.tipo AS vrs_tipo,
    versamenti.proprieta AS vrs_proprieta,
    pagamenti.id AS id,
    pagamenti.cod_dominio AS pag_cod_dominio,
    pagamenti.iuv AS pag_iuv,
    pagamenti.indice_dati AS pag_indice_dati,
    pagamenti.importo_pagato AS pag_importo_pagato,
    pagamenti.data_acquisizione AS pag_data_acquisizione,
    pagamenti.iur AS pag_iur,
    pagamenti.data_pagamento AS pag_data_pagamento,
    pagamenti.commissioni_psp AS pag_commissioni_psp,
    pagamenti.tipo_allegato AS pag_tipo_allegato,
    pagamenti.allegato AS pag_allegato,
    pagamenti.data_acquisizione_revoca AS pag_data_acquisizione_revoca,
    pagamenti.causale_revoca AS pag_causale_revoca,
    pagamenti.dati_revoca AS pag_dati_revoca,
    pagamenti.importo_revocato AS pag_importo_revocato,
    pagamenti.esito_revoca AS pag_esito_revoca,
    pagamenti.dati_esito_revoca AS pag_dati_esito_revoca,
    pagamenti.stato AS pag_stato,
    pagamenti.tipo AS pag_tipo,
    pagamenti.id_incasso AS pag_id_incasso,
    rpt.iuv AS rpt_iuv,
    rpt.ccp AS rpt_ccp,
    incassi.trn AS rnc_trn
   FROM pagamenti
     LEFT JOIN rendicontazioni ON rendicontazioni.id_pagamento = pagamenti.id
     LEFT JOIN singoli_versamenti ON pagamenti.id_singolo_versamento = singoli_versamenti.id
     LEFT JOIN versamenti ON versamenti.id = singoli_versamenti.id_versamento
     LEFT JOIN rpt ON pagamenti.id_rpt = rpt.id
     LEFT JOIN incassi ON pagamenti.id_incasso = incassi.id
  WHERE rendicontazioni.id IS NULL;


-- INIZIALIZZAZIONE DELLA TABELLA CONFIGURAZIONI
DELETE FROM configurazione;

INSERT INTO configurazione (nome,valore) VALUES ('giornale_eventi','{"apiEnte":{"letture":{"log":"SEMPRE","dump":"SOLO_ERRORE"},"scritture":{"log":"SEMPRE","dump":"SOLO_ERRORE"}},"apiPagamento":{"letture":{"log":"MAI","dump":"MAI"},"scritture":{"log":"SEMPRE","dump":"SOLO_ERRORE"}},"apiRagioneria":{"letture":{"log":"MAI","dump":"MAI"},"scritture":{"log":"SEMPRE","dump":"SOLO_ERRORE"}},"apiBackoffice":{"letture":{"log":"MAI","dump":"MAI"},"scritture":{"log":"SEMPRE","dump":"SEMPRE"}},"apiPagoPA":{"letture":{"log":"SEMPRE","dump":"SEMPRE"},"scritture":{"log":"SEMPRE","dump":"SEMPRE"}},"apiPendenze":{"letture":{"log":"MAI","dump":"MAI"},"scritture":{"log":"SEMPRE","dump":"SOLO_ERRORE"}},"apiBackendIO":{"letture":{"log":"SEMPRE","dump":"SEMPRE"},"scritture":{"log":"SEMPRE","dump":"SEMPRE"}}}');
INSERT INTO configurazione (NOME,VALORE) values ('hardening', '{"abilitato": false, "googleCatpcha": {"serverURL":"https://www.google.com/recaptcha/api/siteverify","siteKey":"CHANGE_ME","secretKey":"CHANGE_ME","soglia":1.0,"responseParameter":"gRecaptchaResponse","denyOnFail":true,"readTimeout":5000,"connectionTimeout":5000}}');
INSERT INTO configurazione (NOME,VALORE) values ('mail_batch', '{"abilitato": false, "mailserver": {"host": null, "port": null, "username": null, "password": null, "from": null, "readTimeout": 120000, "connectionTimeout": 10000 }}');
INSERT INTO configurazione (NOME,VALORE) values ('app_io_batch', '{"abilitato": false, "url": null, "timeToLive": 3600 }');
INSERT INTO configurazione (NOME,VALORE) values ('avvisatura_mail', '{"promemoriaAvviso": { "tipo": "freemarker", "oggetto": "\"UHJvbWVtb3JpYSBwYWdhbWVudG86ICR7dmVyc2FtZW50by5nZXRDYXVzYWxlVmVyc2FtZW50bygpLmdldFNpbXBsZSgpfQ==\"", "messaggio": "\"R2VudGlsZSAke3ZlcnNhbWVudG8uZ2V0QW5hZ3JhZmljYURlYml0b3JlKCkuZ2V0UmFnaW9uZVNvY2lhbGUoKX0sCgpsZSBub3RpZmljaGlhbW8gY2hlIMOoIHN0YXRhIGVtZXNzYSB1bmEgcmljaGllc3RhIGRpIHBhZ2FtZW50byBhIHN1byBjYXJpY286ICR7dmVyc2FtZW50by5nZXRDYXVzYWxlVmVyc2FtZW50bygpLmdldFNpbXBsZSgpfQoKPCNpZiB2ZXJzYW1lbnRvLmdldE51bWVyb0F2dmlzbygpP2hhc19jb250ZW50PgpQdcOyIGVmZmV0dHVhcmUgaWwgcGFnYW1lbnRvIHRyYW1pdGUgbCdhcHAgbW9iaWxlIElPIG9wcHVyZSBwcmVzc28gdW5vIGRlaSBwcmVzdGF0b3JpIGRpIHNlcnZpemkgZGkgcGFnYW1lbnRvIGFkZXJlbnRpIGFsIGNpcmN1aXRvIHBhZ29QQSB1dGlsaXp6YW5kbyBsJ2F2dmlzbyBkaSBwYWdhbWVudG8gYWxsZWdhdG8uCjwjZWxzZT4KUHVvJyBlZmZldHR1YXJlIGlsIHBhZ2FtZW50byBvbi1saW5lIHByZXNzbyBpbCBwb3J0YWxlIGRlbGwnZW50ZSBjcmVkaXRvcmU6ICR7ZG9taW5pby5nZXRSYWdpb25lU29jaWFsZSgpfSAKPC8jaWY+CgpEaXN0aW50aSBzYWx1dGku\"", "allegaPdf": true }, "promemoriaRicevuta": { "tipo": "freemarker", "oggetto": "\"PCNpZiBycHQuZ2V0RXNpdG9QYWdhbWVudG8oKS5nZXRDb2RpZmljYSgpID0gMD4KTm90aWZpY2EgcGFnYW1lbnRvIGVzZWd1aXRvOiAke3JwdC5nZXRDb2REb21pbmlvKCl9LyR7cnB0LmdldEl1digpfS8ke3JwdC5nZXRDY3AoKX0KPCNlbHNlaWYgcnB0LmdldEVzaXRvUGFnYW1lbnRvKCkuZ2V0Q29kaWZpY2EoKSA9IDE+Ck5vdGlmaWNhIHBhZ2FtZW50byBub24gZXNlZ3VpdG86ICR7cnB0LmdldENvZERvbWluaW8oKX0vJHtycHQuZ2V0SXV2KCl9LyR7cnB0LmdldENjcCgpfQo8I2Vsc2VpZiBycHQuZ2V0RXNpdG9QYWdhbWVudG8oKS5nZXRDb2RpZmljYSgpID0gMj4KTm90aWZpY2EgcGFnYW1lbnRvIGVzZWd1aXRvIHBhcnppYWxtZW50ZTogJHtycHQuZ2V0Q29kRG9taW5pbygpfS8ke3JwdC5nZXRJdXYoKX0vJHtycHQuZ2V0Q2NwKCl9CjwjZWxzZWlmIHJwdC5nZXRFc2l0b1BhZ2FtZW50bygpLmdldENvZGlmaWNhKCkgPSAzPgpOb3RpZmljYSBkZWNvcnJlbnphIHRlcm1pbmkgcGFnYW1lbnRvOiAke3JwdC5nZXRDb2REb21pbmlvKCl9LyR7cnB0LmdldEl1digpfS8ke3JwdC5nZXRDY3AoKX0KPCNlbHNlaWYgcnB0LmdldEVzaXRvUGFnYW1lbnRvKCkuZ2V0Q29kaWZpY2EoKSA9IDQ+Ck5vdGlmaWNhIGRlY29ycmVuemEgdGVybWluaSBwYWdhbWVudG86ICR7cnB0LmdldENvZERvbWluaW8oKX0vJHtycHQuZ2V0SXV2KCl9LyR7cnB0LmdldENjcCgpfQo8LyNpZj4=\"", "messaggio": "\"PCNhc3NpZ24gZGF0YVJpY2hpZXN0YSA9IHJwdC5nZXREYXRhTXNnUmljaGllc3RhKCk/c3RyaW5nKCJ5eXl5LU1NLWRkIEhIOm1tOnNzIik+CklsIHBhZ2FtZW50byBkaSAiJHt2ZXJzYW1lbnRvLmdldENhdXNhbGVWZXJzYW1lbnRvKCkuZ2V0U2ltcGxlKCl9IiBlZmZldHR1YXRvIGlsICR7ZGF0YVJpY2hpZXN0YX0gcmlzdWx0YSBjb25jbHVzbyBjb24gZXNpdG8gJHtycHQuZ2V0RXNpdG9QYWdhbWVudG8oKS5uYW1lKCl9OgoKRW50ZSBjcmVkaXRvcmU6ICR7ZG9taW5pby5nZXRSYWdpb25lU29jaWFsZSgpfSAoJHtkb21pbmlvLmdldENvZERvbWluaW8oKX0pCklzdGl0dXRvIGF0dGVzdGFudGU6ICR7cnB0LmdldERlbm9taW5hemlvbmVBdHRlc3RhbnRlKCl9ICgke3JwdC5nZXRJZGVudGlmaWNhdGl2b0F0dGVzdGFudGUoKX0pCklkZW50aWZpY2F0aXZvIHVuaXZvY28gdmVyc2FtZW50byAoSVVWKTogJHtycHQuZ2V0SXV2KCl9CkNvZGljZSBjb250ZXN0byBwYWdhbWVudG8gKENDUCk6ICR7cnB0LmdldENjcCgpfQpJbXBvcnRvIHBhZ2F0bzogJHtycHQuZ2V0SW1wb3J0b1RvdGFsZVBhZ2F0bygpfQoKRGlzdGludGkgc2FsdXRpLg==\"", "allegaPdf": true , "soloEseguiti": true }, "promemoriaScadenza": { "tipo": "freemarker", "oggetto": "\"UHJvbWVtb3JpYSBwYWdhbWVudG86ICR7dmVyc2FtZW50by5nZXRDYXVzYWxlVmVyc2FtZW50bygpLmdldFNpbXBsZSgpfQ==\"", "messaggio": "\"R2VudGlsZSAke3ZlcnNhbWVudG8uZ2V0QW5hZ3JhZmljYURlYml0b3JlKCkuZ2V0UmFnaW9uZVNvY2lhbGUoKX0sCgpsZSBub3RpZmljaGlhbW8gY2hlIMOoIHN0YXRhIGVtZXNzYSB1bmEgcmljaGllc3RhIGRpIHBhZ2FtZW50byBhIHN1byBjYXJpY286ICR7dmVyc2FtZW50by5nZXRDYXVzYWxlVmVyc2FtZW50bygpLmdldFNpbXBsZSgpfQoKPCNpZiB2ZXJzYW1lbnRvLmdldE51bWVyb0F2dmlzbygpP2hhc19jb250ZW50PgpQdcOyIGVmZmV0dHVhcmUgaWwgcGFnYW1lbnRvIHRyYW1pdGUgbCdhcHAgbW9iaWxlIElPIG9wcHVyZSBwcmVzc28gdW5vIGRlaSBwcmVzdGF0b3JpIGRpIHNlcnZpemkgZGkgcGFnYW1lbnRvIGFkZXJlbnRpIGFsIGNpcmN1aXRvIHBhZ29QQSB1dGlsaXp6YW5kbyBsJ2F2dmlzbyBkaSBwYWdhbWVudG8gYWxsZWdhdG8uCjwjZWxzZT4KUHVvJyBlZmZldHR1YXJlIGlsIHBhZ2FtZW50byBvbi1saW5lIHByZXNzbyBpbCBwb3J0YWxlIGRlbGwnZW50ZSBjcmVkaXRvcmU6ICR7ZG9taW5pby5nZXRSYWdpb25lU29jaWFsZSgpfSAKPC8jaWY+CgpEaXN0aW50aSBzYWx1dGku\"", "preavviso": 10 } }' );
INSERT INTO configurazione (NOME,VALORE) values ('avvisatura_app_io', '{"promemoriaAvviso": { "tipo": "freemarker", "oggetto": "\"UHJvbWVtb3JpYSBwYWdhbWVudG86ICR7dmVyc2FtZW50by5nZXRDYXVzYWxlVmVyc2FtZW50bygpLmdldFNpbXBsZSgpfQ==\"", "messaggio": "\"R2VudGlsZSAke3ZlcnNhbWVudG8uZ2V0QW5hZ3JhZmljYURlYml0b3JlKCkuZ2V0UmFnaW9uZVNvY2lhbGUoKX0sCgpsZSBub3RpZmljaGlhbW8gY2hlIMOoIHN0YXRhIGVtZXNzYSB1bmEgcmljaGllc3RhIGRpIHBhZ2FtZW50byBhIHN1byBjYXJpY286ICR7dmVyc2FtZW50by5nZXRDYXVzYWxlVmVyc2FtZW50bygpLmdldFNpbXBsZSgpfQoKPCNpZiB2ZXJzYW1lbnRvLmdldE51bWVyb0F2dmlzbygpP2hhc19jb250ZW50PgpQdcOyIGVmZmV0dHVhcmUgaWwgcGFnYW1lbnRvIHRyYW1pdGUgbCdhcHAgbW9iaWxlIElPIG9wcHVyZSBwcmVzc28gdW5vIGRlaSBwcmVzdGF0b3JpIGRpIHNlcnZpemkgZGkgcGFnYW1lbnRvIGFkZXJlbnRpIGFsIGNpcmN1aXRvIHBhZ29QQSB1dGlsaXp6YW5kbyBsJ2F2dmlzbyBkaSBwYWdhbWVudG8gYWxsZWdhdG8uCjwjZWxzZT4KUHVvJyBlZmZldHR1YXJlIGlsIHBhZ2FtZW50byBvbi1saW5lIHByZXNzbyBpbCBwb3J0YWxlIGRlbGwnZW50ZSBjcmVkaXRvcmU6ICR7ZG9taW5pby5nZXRSYWdpb25lU29jaWFsZSgpfSAKPC8jaWY+CgpEaXN0aW50aSBzYWx1dGku\"" }, "promemoriaRicevuta": { "tipo": "freemarker", "oggetto": "\"PCNpZiBycHQuZ2V0RXNpdG9QYWdhbWVudG8oKS5nZXRDb2RpZmljYSgpID0gMD4KTm90aWZpY2EgcGFnYW1lbnRvIGVzZWd1aXRvOiAke3JwdC5nZXRDb2REb21pbmlvKCl9LyR7cnB0LmdldEl1digpfS8ke3JwdC5nZXRDY3AoKX0KPCNlbHNlaWYgcnB0LmdldEVzaXRvUGFnYW1lbnRvKCkuZ2V0Q29kaWZpY2EoKSA9IDE+Ck5vdGlmaWNhIHBhZ2FtZW50byBub24gZXNlZ3VpdG86ICR7cnB0LmdldENvZERvbWluaW8oKX0vJHtycHQuZ2V0SXV2KCl9LyR7cnB0LmdldENjcCgpfQo8I2Vsc2VpZiBycHQuZ2V0RXNpdG9QYWdhbWVudG8oKS5nZXRDb2RpZmljYSgpID0gMj4KTm90aWZpY2EgcGFnYW1lbnRvIGVzZWd1aXRvIHBhcnppYWxtZW50ZTogJHtycHQuZ2V0Q29kRG9taW5pbygpfS8ke3JwdC5nZXRJdXYoKX0vJHtycHQuZ2V0Q2NwKCl9CjwjZWxzZWlmIHJwdC5nZXRFc2l0b1BhZ2FtZW50bygpLmdldENvZGlmaWNhKCkgPSAzPgpOb3RpZmljYSBkZWNvcnJlbnphIHRlcm1pbmkgcGFnYW1lbnRvOiAke3JwdC5nZXRDb2REb21pbmlvKCl9LyR7cnB0LmdldEl1digpfS8ke3JwdC5nZXRDY3AoKX0KPCNlbHNlaWYgcnB0LmdldEVzaXRvUGFnYW1lbnRvKCkuZ2V0Q29kaWZpY2EoKSA9IDQ+Ck5vdGlmaWNhIGRlY29ycmVuemEgdGVybWluaSBwYWdhbWVudG86ICR7cnB0LmdldENvZERvbWluaW8oKX0vJHtycHQuZ2V0SXV2KCl9LyR7cnB0LmdldENjcCgpfQo8LyNpZj4=\"", "messaggio": "\"PCNhc3NpZ24gZGF0YVJpY2hpZXN0YSA9IHJwdC5nZXREYXRhTXNnUmljaGllc3RhKCk/c3RyaW5nKCJ5eXl5LU1NLWRkIEhIOm1tOnNzIik+CklsIHBhZ2FtZW50byBkaSAiJHt2ZXJzYW1lbnRvLmdldENhdXNhbGVWZXJzYW1lbnRvKCkuZ2V0U2ltcGxlKCl9IiBlZmZldHR1YXRvIGlsICR7ZGF0YVJpY2hpZXN0YX0gcmlzdWx0YSBjb25jbHVzbyBjb24gZXNpdG8gJHtycHQuZ2V0RXNpdG9QYWdhbWVudG8oKS5uYW1lKCl9OgoKRW50ZSBjcmVkaXRvcmU6ICR7ZG9taW5pby5nZXRSYWdpb25lU29jaWFsZSgpfSAoJHtkb21pbmlvLmdldENvZERvbWluaW8oKX0pCklzdGl0dXRvIGF0dGVzdGFudGU6ICR7cnB0LmdldERlbm9taW5hemlvbmVBdHRlc3RhbnRlKCl9ICgke3JwdC5nZXRJZGVudGlmaWNhdGl2b0F0dGVzdGFudGUoKX0pCklkZW50aWZpY2F0aXZvIHVuaXZvY28gdmVyc2FtZW50byAoSVVWKTogJHtycHQuZ2V0SXV2KCl9CkNvZGljZSBjb250ZXN0byBwYWdhbWVudG8gKENDUCk6ICR7cnB0LmdldENjcCgpfQpJbXBvcnRvIHBhZ2F0bzogJHtycHQuZ2V0SW1wb3J0b1RvdGFsZVBhZ2F0bygpfQoKRGlzdGludGkgc2FsdXRpLg==\"", "soloEseguiti": true }, "promemoriaScadenza": { "tipo": "freemarker", "oggetto": "\"UHJvbWVtb3JpYSBwYWdhbWVudG86ICR7dmVyc2FtZW50by5nZXRDYXVzYWxlVmVyc2FtZW50bygpLmdldFNpbXBsZSgpfQ==\"", "messaggio": "\"R2VudGlsZSAke3ZlcnNhbWVudG8uZ2V0QW5hZ3JhZmljYURlYml0b3JlKCkuZ2V0UmFnaW9uZVNvY2lhbGUoKX0sCgpsZSBub3RpZmljaGlhbW8gY2hlIMOoIHN0YXRhIGVtZXNzYSB1bmEgcmljaGllc3RhIGRpIHBhZ2FtZW50byBhIHN1byBjYXJpY286ICR7dmVyc2FtZW50by5nZXRDYXVzYWxlVmVyc2FtZW50bygpLmdldFNpbXBsZSgpfQoKPCNpZiB2ZXJzYW1lbnRvLmdldE51bWVyb0F2dmlzbygpP2hhc19jb250ZW50PgpQdcOyIGVmZmV0dHVhcmUgaWwgcGFnYW1lbnRvIHRyYW1pdGUgbCdhcHAgbW9iaWxlIElPIG9wcHVyZSBwcmVzc28gdW5vIGRlaSBwcmVzdGF0b3JpIGRpIHNlcnZpemkgZGkgcGFnYW1lbnRvIGFkZXJlbnRpIGFsIGNpcmN1aXRvIHBhZ29QQSB1dGlsaXp6YW5kbyBsJ2F2dmlzbyBkaSBwYWdhbWVudG8gYWxsZWdhdG8uCjwjZWxzZT4KUHVvJyBlZmZldHR1YXJlIGlsIHBhZ2FtZW50byBvbi1saW5lIHByZXNzbyBpbCBwb3J0YWxlIGRlbGwnZW50ZSBjcmVkaXRvcmU6ICR7ZG9taW5pby5nZXRSYWdpb25lU29jaWFsZSgpfSAKPC8jaWY+CgpEaXN0aW50aSBzYWx1dGku\"", "preavviso": 10 } }' );
INSERT INTO configurazione (NOME,VALORE) values ('tracciato_csv', 
to_clob('{"tipo":"freemarker","intestazione":"idA2A,idPendenza,idDominio,tipoPendenza,numeroAvviso,pdfAvviso,tipoSoggettoPagatore,identificativoPagatore,anagraficaPagatore,indirizzoPagatore,civicoPagatore,capPagatore,localitaPagatore,provinciaPagatore,nazionePagatore,emailPagatore,cellularePagatore,errore","richiesta":"\"PCNhc3NpZ24gY3N2VXRpbHMgPSBjbGFzc1siaXQuZ292cGF5LmNvcmUudXRpbHMuQ1NWVXRpbHMiXS5nZXRJbnN0YW5jZSgpPgoKPCNhc3NpZ24gY3N2UmVjb3JkID0gY3N2VXRpbHMuZ2V0Q1NWUmVjb3JkKGxpbmVhQ3N2UmljaGllc3RhKT4KPCNpZiBjc3ZVdGlscy5pc09wZXJhemlvbmVBbm51bGxhbWVudG8oY3N2UmVjb3JkLCAxMik+Cgk8I2Fzc2lnbiB0bXA9Y29udGV4dD9hcGkucHV0KCJ0aXBvT3BlcmF6aW9uZSIsICJERUwiKSEvPgp7CgkiaWRBMkEiOiAke2NzdlV0aWxzLnRvSnNvblZhbHVlKGNzdlJlY29yZCwgMCl9LAoJImlkUGVuZGVuemEiOiAke2NzdlV0aWxzLnRvSnNvblZhbHVlKGNzdlJlY29yZCwgMSl9Cn0KPCNlbHNlPgo8I2lmICFjc3ZVdGlscy5pc0VtcHR5KGNzdlJlY29yZCwgODIpPgogIDwjYXNzaWduIGRhdGFBdnZpc29TdHJpbmcgPSBjc3ZSZWNvcmQuZ2V0KDgyKT4KICA8I2lmIGRhdGFBdnZpc29TdHJpbmcuZXF1YWxzKCJNQUkiKT4KICAJPCNhc3NpZ24gdG1wPWNvbnRleHQ/YXBpLnB1dCgiYXZ2aXNhdHVyYSIsIGZhbHNlKSEvPgogIDwjZWxzZT4KICAJPCNhc3NpZ24gc2RmVXRpbHMgPSBjbGFzc1siaXQuZ292cGF5LmNvcmUudXRpbHMuU2ltcGxlRGF0ZUZvcm1hdFV0aWxzIl0uZ2V0SW5zdGFuY2UoKT4KICAJPCNhc3NpZ24gZGF0YUF2dmlzYXR1cmFEYXRlID0gc2RmVXRpbHMuZ2V0RGF0YUF2dmlzYXR1cmEoZGF0YUF2dmlzb1N0cmluZywgImRhdGFBdnZpc2F0dXJhIikgPgogIAk8I2Fzc2lnbiBjb250ZXh0VE1QID0gY29udGV4dCA+CiAgCTwjYXNzaWduIHRtcD1jb250ZXh0P2FwaS5wdXQoImRhdGFBdnZpc2F0dXJhIiwgZGF0YUF2dmlzYXR1cmFEYXRlKSEvPgogIDwvI2lmPgo8LyNpZj4KewoJImlkQTJBIjogJHtjc3ZVdGlscy50b0pzb25WYWx1ZShjc3ZSZWNvcmQsIDApfSwKCSJpZFBlbmRlbnphIjogJHtjc3ZVdGlscy50b0pzb25WYWx1ZShjc3ZSZWNvcmQsIDEpfSwKCSJpZERvbWluaW8iOiAke2NzdlV0aWxzLnRvSnNvblZhbHVlKGNzdlJlY29yZCwgMil9LAoJIm51bWVyb0F2dmlzbyI6ICR7Y3N2VXRpbHMudG9Kc29uVmFsdWUoY3N2UmVjb3JkLCAzKX0sCQoJImlkVGlwb1BlbmRlbnphIjogJHtjc3ZVdGlscy50b0pzb25WYWx1ZShjc3ZSZWNvcmQsIDQpfSwKCSJpZFVuaXRhT3BlcmF0aXZhIjogJHtjc3ZVdGlscy50b0pzb25WYWx1ZShjc3ZSZWNvcmQsIDUpfSwKIAkiY2F1c2FsZSI6ICR7Y3N2VXRpbHMudG9Kc29uVmFsdWUoY3N2UmVjb3JkLCA2KX0sCiAJImFubm9SaWZlcmltZW50byI6ICR7Y3N2VXRpbHMudG9Kc29uVmFsdWUoY3N2UmVjb3JkLCA3KX0sCiAJImNhcnRlbGxhUGFnYW1lbnRvIjogJHtjc3ZVdGlscy50b0pzb25WYWx1ZShjc3ZSZWNvcmQsIDgpfSwKIAk8I2lmICFjc3ZVdGlscy5pc0VtcHR5KGNzdlJlY29yZCwgOSk+ImRhdGlBbGxlZ2F0aSI6ICR7Y3N2UmVjb3JkLmdldCg5KX0sPC8jaWY+CiAJImRpcmV6aW9uZSI6ICR7Y3N2VXRpbHMudG9Kc29uVmFsdWUoY3N2UmVjb3JkLCAxMCl9LAogCSJkaXZpc2lvbmUiOiAke2NzdlV0aWxzLnRvSnNvblZhbHVlKGNzdlJlY29yZCwgMTEpfSwKIAkiaW1wb3J0byI6ICR7Y3N2VXRpbHMudG9Kc29uVmFsdWUoY3N2UmVjb3JkLCAxMil9LAoJImRhdGFWYWxpZGl0YSI6ICR7Y3N2VXRpbHMudG9Kc29uVmFsdWUoY3N2UmVjb3JkLCAxMyl9LAoJImRhdGFTY2FkZW56YSI6ICR7Y3N2VXRpbHMudG9Kc29uVmFsdWUoY3N2UmVjb3JkLCAxNCl9LAoJInRhc3Nvbm9taWFBdnZpc28iOiAke2NzdlV0aWxzLnRvSnNvblZhbHVlKGNzdlJlY29yZCwgMTUpfSwKCTwjaWYgIWNzdlV0aWxzLmlzRW1wdHkoY3N2UmVjb3JkLCA4Myk+CgkiZG9jdW1lbnRvIjogewoJCSJpZGVudGlmaWNhdGl2byI6ICR7Y3N2VXRpbHMudG9Kc29uVmFsdWUoY3N2UmVjb3JkLCA4Myl9LAoJCTwjaWYgIWNzdlV0aWxzLmlzRW1wdHkoY3N2UmVjb3JkLCA4NSk+CgkJCQoJCQk8I2Fzc2lnbiByYXRhU3RyaW5nID0gY3N2UmVjb3JkLmdldCg4NSk+CgkJCTwjaWYgcmF0YVN0cmluZz9tYXRjaGVzKCdbMC05XSonKT4KCQkJCSJyYXRhIjogJHtjc3ZVdGlscy50b0pzb25WYWx1ZShjc3ZSZWNvcmQsIDg1KX0sCQoJCQk8I2Vsc2U+CgkJCQkic29nbGlhIjogewoJCQkJCSJ0aXBvIjogIiR7cmF0YVN0cmluZ1swLi40XX0iLAoJCQkJCSJnaW9ybmkiOiAke3JhdGFTdHJpbmdbNS4uXX0gCgkJCQl9LAkgIAoJCQk8LyNpZj4KCQk8LyNpZj4KCQkiZGVzY3JpemlvbmUiOiAke2NzdlV0aWxzLnRvSnNvblZhbHVlKGNzdlJlY29yZCwgODQpfQoJfSwKCTwvI2lmPgoJPCNpZiAhY3N2VXRpbHMuaXNFbXB0eShjc3ZSZWNvcmQsIDg2KT4KCQk8I2Fzc2lnbiBsaW5ndWFTZWNvbmRhcmlhU3RyaW5nID0gY3N2UmVjb3JkLmdldCg4NikhLz4KCQk8I2Fzc2lnbiBtYXBwZWRBcnJheV9zdHJpbmcgPSBsaW5ndWFTZWNvbmRhcmlhU3RyaW5nP3NwbGl0KCJ8IikgIS8+CgkJCgkicHJvcHJpZXRhIjogewoJCSJsaW5ndWFTZWNvbmRhcmlhIiA6ICIke21hcHBlZEFycmF5X3N0cmluZ1swXX0iCgkJPCNpZiAobWFwcGVkQXJyYXlfc3RyaW5nP3NpemUgPiAxKT4KCQkJLCAibGluZ3VhU2Vjb25kYXJpYUNhdXNhbGUiIDogIiR7bWFwcGVkQXJyYXlfc3RyaW5nWzFdfSIKCQk8LyNpZj4KCX0sCgk8LyNpZj4KCSJzb2dnZXR0b1BhZ2F0b3JlIjogewoJCSJ0aXBvIjogJHtjc3ZVdGlscy50b0pzb25WYWx1ZShjc3ZSZWNvcmQsIDE2KX0sCgkJImlkZW50aWZpY2F0aXZvIjogJHtjc3ZVdGlscy50b') ||
to_clob('0pzb25WYWx1ZShjc3ZSZWNvcmQsIDE3KX0sCgkJImFuYWdyYWZpY2EiOiAke2NzdlV0aWxzLnRvSnNvblZhbHVlKGNzdlJlY29yZCwgMTgpfSwKCQkiaW5kaXJpenpvIjogJHtjc3ZVdGlscy50b0pzb25WYWx1ZShjc3ZSZWNvcmQsIDE5KX0sCgkJImNpdmljbyI6ICR7Y3N2VXRpbHMudG9Kc29uVmFsdWUoY3N2UmVjb3JkLCAyMCl9LAoJCSJjYXAiOiAke2NzdlV0aWxzLnRvSnNvblZhbHVlKGNzdlJlY29yZCwgMjEpfSwKCQkibG9jYWxpdGEiOiAke2NzdlV0aWxzLnRvSnNvblZhbHVlKGNzdlJlY29yZCwgMjIpfSwKCQkicHJvdmluY2lhIjogJHtjc3ZVdGlscy50b0pzb25WYWx1ZShjc3ZSZWNvcmQsIDIzKX0sCgkJIm5hemlvbmUiOiAke2NzdlV0aWxzLnRvSnNvblZhbHVlKGNzdlJlY29yZCwgMjQpfSwKCQkiZW1haWwiOiAke2NzdlV0aWxzLnRvSnNvblZhbHVlKGNzdlJlY29yZCwgMjUpfSwKCQkiY2VsbHVsYXJlIjogJHtjc3ZVdGlscy50b0pzb25WYWx1ZShjc3ZSZWNvcmQsIDI2KX0KCX0sCgkidm9jaSI6IFsKCQl7CgkJCSJpZFZvY2VQZW5kZW56YSI6ICR7Y3N2VXRpbHMudG9Kc29uVmFsdWUoY3N2UmVjb3JkLCAyNyl9LAoJCQkiaW1wb3J0byI6ICR7Y3N2VXRpbHMudG9Kc29uVmFsdWUoY3N2UmVjb3JkLCAyOCl9LAoJCQkiZGVzY3JpemlvbmUiOiAke2NzdlV0aWxzLnRvSnNvblZhbHVlKGNzdlJlY29yZCwgMjkpfSwKCQkJPCNpZiAhY3N2VXRpbHMuaXNFbXB0eShjc3ZSZWNvcmQsIDM0KT4KCQkJImNvZEVudHJhdGEiOiAke2NzdlV0aWxzLnRvSnNvblZhbHVlKGNzdlJlY29yZCwgMzQpfQoJCQk8I2Vsc2VpZiAhY3N2VXRpbHMuaXNFbXB0eShjc3ZSZWNvcmQsIDM1KT4KCQkJInRpcG9Cb2xsbyI6ICR7Y3N2VXRpbHMudG9Kc29uVmFsdWUoY3N2UmVjb3JkLCAzNSl9LAogICAgICAgCQkiaGFzaERvY3VtZW50byI6ICR7Y3N2VXRpbHMudG9Kc29uVmFsdWUoY3N2UmVjb3JkLCAzNil9LAogICAgICAJCSJwcm92aW5jaWFSZXNpZGVuemEiOiAke2NzdlV0aWxzLnRvSnNvblZhbHVlKGNzdlJlY29yZCwgMzcpfQoJCQk8I2Vsc2U+CgkJCSJpYmFuQWNjcmVkaXRvIjogJHtjc3ZVdGlscy50b0pzb25WYWx1ZShjc3ZSZWNvcmQsIDMwKX0sCgkJCSJpYmFuQXBwb2dnaW8iOiAke2NzdlV0aWxzLnRvSnNvblZhbHVlKGNzdlJlY29yZCwgMzEpfSwKCQkJInRpcG9Db250YWJpbGl0YSI6ICR7Y3N2VXRpbHMudG9Kc29uVmFsdWUoY3N2UmVjb3JkLCAzMil9LAoJCQkiY29kaWNlQ29udGFiaWxpdGEiOiAke2NzdlV0aWxzLnRvSnNvblZhbHVlKGNzdlJlY29yZCwgMzMpfQoJCQk8LyNpZj4KCgkJfQoJCTwjaWYgIWNzdlV0aWxzLmlzRW1wdHkoY3N2UmVjb3JkLCAzOCk+CgkJLHsKCQkJImlkVm9jZVBlbmRlbnphIjogJHtjc3ZVdGlscy50b0pzb25WYWx1ZShjc3ZSZWNvcmQsIDM4KX0sCgkJCSJpbXBvcnRvIjogJHtjc3ZVdGlscy50b0pzb25WYWx1ZShjc3ZSZWNvcmQsIDM5KX0sCgkJCSJkZXNjcml6aW9uZSI6ICR7Y3N2VXRpbHMudG9Kc29uVmFsdWUoY3N2UmVjb3JkLCA0MCl9LAoJCQk8I2lmICFjc3ZVdGlscy5pc0VtcHR5KGNzdlJlY29yZCwgNDUpPgoJCQkiY29kRW50cmF0YSI6ICR7Y3N2VXRpbHMudG9Kc29uVmFsdWUoY3N2UmVjb3JkLCA0NSl9CgkJCTwjZWxzZWlmICFjc3ZVdGlscy5pc0VtcHR5KGNzdlJlY29yZCwgNDYpPgoJCQkidGlwb0JvbGxvIjogJHtjc3ZVdGlscy50b0pzb25WYWx1ZShjc3ZSZWNvcmQsIDQ2KX0sCiAgICAgICAJCSJoYXNoRG9jdW1lbnRvIjogJHtjc3ZVdGlscy50b0pzb25WYWx1ZShjc3ZSZWNvcmQsIDQ3KX0sCiAgICAgIAkJInByb3ZpbmNpYVJlc2lkZW56YSI6ICR7Y3N2VXRpbHMudG9Kc29uVmFsdWUoY3N2UmVjb3JkLCA0OCl9CgkJCTwjZWxzZT4KCQkJImliYW5BY2NyZWRpdG8iOiAke2NzdlV0aWxzLnRvSnNvblZhbHVlKGNzdlJlY29yZCwgNDEpfSwKCQkJImliYW5BcHBvZ2dpbyI6ICR7Y3N2VXRpbHMudG9Kc29uVmFsdWUoY3N2UmVjb3JkLCA0Mil9LAoJCQkidGlwb0NvbnRhYmlsaXRhIjogJHtjc3ZVdGlscy50b0pzb25WYWx1ZShjc3ZSZWNvcmQsIDQzKX0sCgkJCSJjb2RpY2VDb250YWJpbGl0YSI6ICR7Y3N2VXRpbHMudG9Kc29uVmFsdWUoY3N2UmVjb3JkLCA0NCl9CgkJCTwvI2lmPgoJCX0KCQk8LyNpZj4KCQk8I2lmICFjc3ZVdGlscy5pc0VtcHR5KGNzdlJlY29yZCwgNDkpPgoJCSx7CgkJCSJpZFZvY2VQZW5kZW56YSI6ICR7Y3N2VXRpbHMudG9Kc29uVmFsdWUoY3N2UmVjb3JkLCA0OSl9LAoJCQkiaW1wb3J0byI6ICR7Y3N2VXRpbHMudG9Kc29uVmFsdWUoY3N2UmVjb3JkLCA1MCl9LAoJCQkiZGVzY3JpemlvbmUiOiAke2NzdlV0aWxzLnRvSnNvblZhbHVlKGNzdlJlY29yZCwgNTEpfSwKCQkJPCNpZiAhY3N2VXRpbHMuaXNFbXB0eShjc3ZSZWNvcmQsIDU2KT4KCQkJImNvZEVudHJhdGEiOiAke2NzdlV0aWxzLnRvSnNvblZhbHVlKGNzdlJlY29yZCwgNTYpfQoJCQk8I2Vsc2VpZiAhY3N2VXRpbHMuaXNFbXB0eShjc3ZSZWNvcmQsIDU3KT4KCQkJInRpcG9Cb2xsbyI6ICR7Y3N2VXRpbHMudG9Kc29uVmFsdWUoY3N2UmVjb3JkLCA1Nyl9LAogICAgICAgCQkiaGFzaERvY3VtZW50byI6ICR7Y3N2VXRpbHMudG9Kc29uVmFsdWUoY3N2UmVjb3JkLCA1OCl9LAogICAgICAJCSJwcm92aW5jaWFSZXNpZGVuemEiOiAke2NzdlV0aWxzLnRvSnNvblZhbHVlKGNzdlJlY29yZCwgNTkpfQoJCQk8I2Vsc2U+CgkJCSJpYmFuQWNjcmVkaXRvIjogJHtjc3ZVdGlscy50b0pzb25WYWx1ZShjc3ZSZWNvcmQsIDUyKX0sCgkJCSJpYmFuQXBwb2dnaW8iOiAke2NzdlV0aWxzLnRvSnNvblZhbHVlKGNzdlJlY29yZCwgNTMpfSwKCQkJInRpcG9Db250YWJpbGl0YSI6ICR7Y3N2VXRpbHMudG9Kc29uVmFsdWUoY3N2UmVjb3JkLCA1NCl9LAoJCQkiY29kaWNlQ29udGFiaWxpdGEiOiAke2NzdlV0aWxzLnRvSnNvblZhbHVlKGNzdlJlY29yZCwgNTUpfQoJCQk8LyNpZj4KCQl9CgkJPC8jaWY+CgkJPCNpZiAhY3N2VXRpbHMu') ||
to_clob('aXNFbXB0eShjc3ZSZWNvcmQsIDYwKT4KCQksewoJCQkiaWRWb2NlUGVuZGVuemEiOiAke2NzdlV0aWxzLnRvSnNvblZhbHVlKGNzdlJlY29yZCwgNjApfSwKCQkJImltcG9ydG8iOiAke2NzdlV0aWxzLnRvSnNvblZhbHVlKGNzdlJlY29yZCwgNjEpfSwKCQkJImRlc2NyaXppb25lIjogJHtjc3ZVdGlscy50b0pzb25WYWx1ZShjc3ZSZWNvcmQsIDYyKX0sCgkJCTwjaWYgIWNzdlV0aWxzLmlzRW1wdHkoY3N2UmVjb3JkLCA2Nyk+CgkJCSJjb2RFbnRyYXRhIjogJHtjc3ZVdGlscy50b0pzb25WYWx1ZShjc3ZSZWNvcmQsIDY3KX0KCQkJPCNlbHNlaWYgIWNzdlV0aWxzLmlzRW1wdHkoY3N2UmVjb3JkLCA2OCk+CgkJCSJ0aXBvQm9sbG8iOiAke2NzdlV0aWxzLnRvSnNvblZhbHVlKGNzdlJlY29yZCwgNjgpfSwKICAgICAgIAkJImhhc2hEb2N1bWVudG8iOiAke2NzdlV0aWxzLnRvSnNvblZhbHVlKGNzdlJlY29yZCwgNjkpfSwKICAgICAgCQkicHJvdmluY2lhUmVzaWRlbnphIjogJHtjc3ZVdGlscy50b0pzb25WYWx1ZShjc3ZSZWNvcmQsIDcwKX0KCQkJPCNlbHNlPgoJCQkiaWJhbkFjY3JlZGl0byI6ICR7Y3N2VXRpbHMudG9Kc29uVmFsdWUoY3N2UmVjb3JkLCA2Myl9LAoJCQkiaWJhbkFwcG9nZ2lvIjogJHtjc3ZVdGlscy50b0pzb25WYWx1ZShjc3ZSZWNvcmQsIDY0KX0sCgkJCSJ0aXBvQ29udGFiaWxpdGEiOiAke2NzdlV0aWxzLnRvSnNvblZhbHVlKGNzdlJlY29yZCwgNjUpfSwKCQkJImNvZGljZUNvbnRhYmlsaXRhIjogJHtjc3ZVdGlscy50b0pzb25WYWx1ZShjc3ZSZWNvcmQsIDY2KX0KCQkJPC8jaWY+CgkJfQoJCTwvI2lmPgoJCTwjaWYgIWNzdlV0aWxzLmlzRW1wdHkoY3N2UmVjb3JkLCA3MSk+CgkJLHsKCQkJImlkVm9jZVBlbmRlbnphIjogJHtjc3ZVdGlscy50b0pzb25WYWx1ZShjc3ZSZWNvcmQsIDcxKX0sCgkJCSJpbXBvcnRvIjogJHtjc3ZVdGlscy50b0pzb25WYWx1ZShjc3ZSZWNvcmQsIDcyKX0sCgkJCSJkZXNjcml6aW9uZSI6ICR7Y3N2VXRpbHMudG9Kc29uVmFsdWUoY3N2UmVjb3JkLCA3Myl9LAoJCQk8I2lmICFjc3ZVdGlscy5pc0VtcHR5KGNzdlJlY29yZCwgNzgpPgoJCQkiY29kRW50cmF0YSI6ICR7Y3N2VXRpbHMudG9Kc29uVmFsdWUoY3N2UmVjb3JkLCA3OCl9CgkJCTwjZWxzZWlmICFjc3ZVdGlscy5pc0VtcHR5KGNzdlJlY29yZCwgNzkpPgoJCQkidGlwb0JvbGxvIjogJHtjc3ZVdGlscy50b0pzb25WYWx1ZShjc3ZSZWNvcmQsIDc5KX0sCiAgICAgICAJCSJoYXNoRG9jdW1lbnRvIjogJHtjc3ZVdGlscy50b0pzb25WYWx1ZShjc3ZSZWNvcmQsIDgwKX0sCiAgICAgIAkJInByb3ZpbmNpYVJlc2lkZW56YSI6ICR7Y3N2VXRpbHMudG9Kc29uVmFsdWUoY3N2UmVjb3JkLCA4MSl9CgkJCTwjZWxzZT4KCQkJImliYW5BY2NyZWRpdG8iOiAke2NzdlV0aWxzLnRvSnNvblZhbHVlKGNzdlJlY29yZCwgNzQpfSwKCQkJImliYW5BcHBvZ2dpbyI6ICR7Y3N2VXRpbHMudG9Kc29uVmFsdWUoY3N2UmVjb3JkLCA3NSl9LAoJCQkidGlwb0NvbnRhYmlsaXRhIjogJHtjc3ZVdGlscy50b0pzb25WYWx1ZShjc3ZSZWNvcmQsIDc2KX0sCgkJCSJjb2RpY2VDb250YWJpbGl0YSI6ICR7Y3N2VXRpbHMudG9Kc29uVmFsdWUoY3N2UmVjb3JkLCA3Nyl9CgkJCTwvI2lmPgoJCX0KCQk8LyNpZj4KCgldCn0KPC8jaWY+\"","risposta":"\"PCNhc3NpZ24gY3N2VXRpbHMgPSBjbGFzc1siaXQuZ292cGF5LmNvcmUudXRpbHMuQ1NWVXRpbHMiXS5nZXRJbnN0YW5jZSgpIC8+CjwjaWYgZXNpdG9PcGVyYXppb25lID09ICJFU0VHVUlUT19PSyI+CjwjYXNzaWduIGlkQTJBID0gYXBwbGljYXppb25lLmdldENvZEFwcGxpY2F6aW9uZSgpISAvPgo8I2Fzc2lnbiBpZFBlbmRlbnphID0gdmVyc2FtZW50by5nZXRDb2RWZXJzYW1lbnRvRW50ZSgpISAvPgo8I2Fzc2lnbiBpZERvbWluaW8gPSBkb21pbmlvLmdldENvZERvbWluaW8oKSAvPgo8I2Fzc2lnbiB0aXBvUGVuZGVuemEgPSBpZFRpcG9WZXJzYW1lbnRvIC8+CjwjYXNzaWduIG51bWVyb0F2dmlzbyA9IHZlcnNhbWVudG8uZ2V0TnVtZXJvQXZ2aXNvKCkhIC8+CjwjaWYgbnVtZXJvQXZ2aXNvP2hhc19jb250ZW50PgoJPCNhc3NpZ24gcGRmQXZ2aXNvID0gaWREb21pbmlvICsgIl8iICsgbnVtZXJvQXZ2aXNvICsgIi5wZGYiIC8+CjwvI2lmPgo8I2Fzc2lnbiBpZERvY3VtZW50byA9IHZlcnNhbWVudG8uZ2V0SWREb2N1bWVudG8oKSEgLz4KPCNpZiBkb2N1bWVudG8/aGFzX2NvbnRlbnQ+CiAgICA8I2Fzc2lnbiBudW1lcm9Eb2N1bWVudG8gPSBkb2N1bWVudG8uZ2V0Q29kRG9jdW1lbnRvKCkgLz4KCTwjYXNzaWduIHBkZkF2dmlzbyA9IGlkRG9taW5pbyArICJfRE9DXyIgKyBudW1lcm9Eb2N1bWVudG8gKyAiLnBkZiIgLz4KPC8jaWY+CjwjYXNzaWduIHRpcG8gPSB2ZXJzYW1lbnRvLmdldEFuYWdyYWZpY2FEZWJpdG9yZSgpLmdldFRpcG8oKS50b1N0cmluZygpIC8+CjwjYXNzaWduIGlkZW50aWZpY2F0aXZvID0gdmVyc2FtZW50by5nZXRBbmFncmFmaWNhRGViaXRvcmUoKS5nZXRDb2RVbml2b2NvKCkhIC8+CjwjYXNzaWduIGFuYWdyYWZpY2EgPSB2ZXJzYW1lbnRvLmdldEFuYWdyYWZpY2FEZWJpdG9yZSgpLmdldFJhZ2lvbmVTb2NpYWxlKCkhIC8+CjwjYXNzaWduIGluZGlyaXp6byA9IHZlcnNhbWVudG8uZ2V0QW5hZ3JhZmljYURlYml0b3JlKCkuZ2V0SW5kaXJpenpvKCkhIC8+CjwjYXNzaWduIGNpdmljbyA9IHZlcnNhbWVudG8uZ2V0QW5hZ3JhZmljYURlYml0b3JlKCkuZ2V0Q2l2aWNvKCkhIC8+CjwjYXNzaWduIGNhcCA9IHZlcnNhbWVudG8uZ2V0QW5hZ3JhZmljYURlYml0b3JlKCkuZ2V0Q2FwKCkhIC8+CjwjYXNzaWduIGxvY2FsaXRhID0gdmVyc2FtZW50by5nZXRBbmFncmFmaWNhRGViaXRvcmUoKS5nZXRMb2NhbGl0YSgpISAvPgo8I2Fzc2lnbiBwcm92aW5jaWEgPSB2ZXJzYW1lbnRvLmdldEFuYWdyYWZpY2FEZWJpdG9yZSgpLmdldFByb3ZpbmNpYSgpISAvPgo8I2Fzc2lnbiBuYXppb') ||
to_clob('25lID0gdmVyc2FtZW50by5nZXRBbmFncmFmaWNhRGViaXRvcmUoKS5nZXROYXppb25lKCkhIC8+CjwjYXNzaWduIGVtYWlsID0gdmVyc2FtZW50by5nZXRBbmFncmFmaWNhRGViaXRvcmUoKS5nZXRFbWFpbCgpISAvPgo8I2Fzc2lnbiBjZWxsdWxhcmUgPSB2ZXJzYW1lbnRvLmdldEFuYWdyYWZpY2FEZWJpdG9yZSgpLmdldENlbGx1bGFyZSgpISAvPgo8I2lmIHRpcG9PcGVyYXppb25lID09ICJBREQiPgoJPCNhc3NpZ24gY3N2UmVjb3JkID0gY3N2VXRpbHMudG9Dc3YoaWRBMkEsIGlkUGVuZGVuemEsIGlkRG9taW5pbywgdGlwb1BlbmRlbnphLCBudW1lcm9BdnZpc28sIHBkZkF2dmlzbywgdGlwbywgaWRlbnRpZmljYXRpdm8sIGFuYWdyYWZpY2EsIGluZGlyaXp6bywgY2l2aWNvLCBjYXAsIGxvY2FsaXRhLCBwcm92aW5jaWEsIG5hemlvbmUsIGVtYWlsLCBjZWxsdWxhcmUsICIiKSAvPgo8I2Vsc2U+Cgk8I2Fzc2lnbiBjc3ZSZWNvcmQgPSBjc3ZVdGlscy50b0NzdihpZEEyQSwgaWRQZW5kZW56YSwgaWREb21pbmlvLCB0aXBvUGVuZGVuemEsIG51bWVyb0F2dmlzbywgcGRmQXZ2aXNvLCB0aXBvLCBpZGVudGlmaWNhdGl2bywgYW5hZ3JhZmljYSwgaW5kaXJpenpvLCBjaXZpY28sIGNhcCwgbG9jYWxpdGEsIHByb3ZpbmNpYSwgbmF6aW9uZSwgZW1haWwsIGNlbGx1bGFyZSwgZGVzY3JpemlvbmVFc2l0b09wZXJhemlvbmUpIC8+CjwvI2lmPgo8I2Vsc2U+CjwjYXNzaWduIGNzdlJlY29yZCA9IGNzdlV0aWxzLnRvQ3N2KCIiLCAiIiwgIiIsICIiLCAiIiwgIiIsICIiLCAiIiwgIiIsICIiLCAiIiwgIiIsICIiLCAiIiwgIiIsICIiLCAiIiwgZGVzY3JpemlvbmVFc2l0b09wZXJhemlvbmUpIC8+CjwvI2lmPgoke2NzdlJlY29yZH0=\""}')); 


-- password utenza amministratore di default
UPDATE utenze SET password = '$1$jil82b4n$GRX4A2H91f7L7dJ3kL2Vc.' where principal='gpadmin';
-- diritti * all'utenza amministratore
UPDATE utenze SET autorizzazione_domini_star = 1 where principal='gpadmin'; 
UPDATE utenze SET autorizzazione_tipi_vers_star = 1 where principal='gpadmin';





