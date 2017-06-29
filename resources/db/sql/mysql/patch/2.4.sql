--GP-523

CREATE TABLE tracciati
(
       -- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
       data_caricamento TIMESTAMP(3) NOT NULL DEFAULT 0,
       -- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
       data_ultimo_aggiornamento TIMESTAMP(3) NOT NULL DEFAULT 0,
       stato VARCHAR(255) NOT NULL,
       linea_elaborazione BIGINT NOT NULL,
       descrizione_stato VARCHAR(1024),
       num_linee_totali BIGINT NOT NULL,
       num_operazioni_ok BIGINT NOT NULL,
       num_operazioni_ko BIGINT NOT NULL,
       nome_file VARCHAR(255) NOT NULL,
       raw_data_richiesta MEDIUMBLOB NOT NULL,
       raw_data_risposta MEDIUMBLOB,
       -- fk/pk columns
       id BIGINT AUTO_INCREMENT,
       id_operatore BIGINT,
       id_applicazione BIGINT,
       -- check constraints
       CONSTRAINT chk_tracciati_1 CHECK (stato IN ('ANNULLATO','NUOVO','IN_CARICAMENTO','CARICAMENTO_OK','CARICAMENTO_KO')),
       -- fk/pk keys constraints
       CONSTRAINT fk_tracciati_1 FOREIGN KEY (id_operatore) REFERENCES operatori(id),
       CONSTRAINT fk_tracciati_2 FOREIGN KEY (id_applicazione) REFERENCES applicazioni(id),
       CONSTRAINT pk_tracciati PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;




CREATE TABLE operazioni
(
       tipo_operazione VARCHAR(255) NOT NULL,
       linea_elaborazione BIGINT NOT NULL,
       stato VARCHAR(255) NOT NULL,
       dati_richiesta MEDIUMBLOB NOT NULL,
       dati_risposta MEDIUMBLOB,
       dettaglio_esito VARCHAR(255),
       cod_versamento_ente VARCHAR(255),
       -- fk/pk columns
       id BIGINT AUTO_INCREMENT,
       id_tracciato BIGINT NOT NULL,
       id_applicazione BIGINT,
       -- check constraints
       CONSTRAINT chk_operazioni_1 CHECK (stato IN ('NON_VALIDO','ESEGUITO_OK','ESEGUITO_KO')),
       -- fk/pk keys constraints
       CONSTRAINT fk_operazioni_1 FOREIGN KEY (id_tracciato) REFERENCES tracciati(id),
       CONSTRAINT fk_operazioni_2 FOREIGN KEY (id_applicazione) REFERENCES applicazioni(id),
       CONSTRAINT pk_operazioni PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;


--GP-524

CREATE TABLE audit
(
       -- Precisione ai millisecondi supportata dalla versione 5.6.4, se si utilizza una versione precedente non usare il suffisso '(3)'
       data TIMESTAMP(3) NOT NULL DEFAULT 0,
       id_oggetto BIGINT NOT NULL,
       tipo_oggetto VARCHAR(255) NOT NULL,
       oggetto LONGTEXT NOT NULL,
       -- fk/pk columns
       id BIGINT AUTO_INCREMENT,
       id_operatore BIGINT NOT NULL,
       -- fk/pk keys constraints
       CONSTRAINT fk_audit_1 FOREIGN KEY (id_operatore) REFERENCES operatori(id),
       CONSTRAINT pk_audit PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;

--GP-526

CREATE TABLE ruoli
(
       cod_ruolo VARCHAR(35) NOT NULL,
       descrizione VARCHAR(255) NOT NULL,
       -- fk/pk columns
       id BIGINT AUTO_INCREMENT,
       -- unique constraints
       CONSTRAINT unique_ruoli_1 UNIQUE (cod_ruolo),
       -- fk/pk keys constraints
       CONSTRAINT pk_ruoli PRIMARY KEY (id)
)ENGINE INNODB CHARACTER SET latin1 COLLATE latin1_general_cs;

-- index
CREATE INDEX index_ruoli_1 ON ruoli (cod_ruolo);


ALTER TABLE acl ADD COLUMN diritti INT;
ALTER TABLE acl MODIFY COLUMN cod_servizio VARCHAR(35);

ALTER TABLE acl ADD COLUMN id_ruolo BIGINT;
ALTER TABLE acl ADD CONSTRAINT fk_acl_6 FOREIGN KEY (id_ruolo) REFERENCES ruoli(id);
ALTER TABLE domini ADD COLUMN logo MEDIUMBLOB;

ALTER TABLE operatori MODIFY COLUMN profilo VARCHAR(1024);

ALTER TABLE acl ADD COLUMN admin BOOLEAN NOT NULL DEFAULT false;

UPDATE operatori SET profilo = 'Amministratore' where profilo = 'A';
UPDATE operatori SET profilo = 'Operatore' where profilo = 'E';

INSERT INTO ruoli (cod_ruolo,descrizione) VALUES ('Amministratore', 'Ruolo Amministratore');
INSERT INTO ruoli (cod_ruolo,descrizione) VALUES ('Operatore', 'Ruolo Operatore');

INSERT INTO acl (cod_tipo,cod_servizio,diritti,id_ruolo) select 'D', 'A_PPA', 2, id from ruoli where cod_ruolo ='Amministratore';
INSERT INTO acl (cod_tipo,cod_servizio,diritti,id_ruolo) select 'D', 'A_CON', 2, id from ruoli where cod_ruolo ='Amministratore';
INSERT INTO acl (cod_tipo,cod_servizio,diritti,id_ruolo) select 'D', 'A_APP', 2, id from ruoli where cod_ruolo ='Amministratore';
INSERT INTO acl (cod_tipo,cod_servizio,diritti,id_ruolo) select 'D', 'A_USR', 2, id from ruoli where cod_ruolo ='Amministratore';
INSERT INTO acl (cod_tipo,cod_servizio,diritti,id_ruolo,admin) select 'D', 'G_PAG', 2, id, true from ruoli where cod_ruolo ='Amministratore';
INSERT INTO acl (cod_tipo,cod_servizio,diritti,id_ruolo) select 'D', 'G_RND', 2, id from ruoli where cod_ruolo ='Amministratore';
INSERT INTO acl (cod_tipo,cod_servizio,diritti,id_ruolo) select 'D', 'GDE', 2, id from ruoli where cod_ruolo ='Amministratore';
INSERT INTO acl (cod_tipo,cod_servizio,diritti,id_ruolo) select 'D', 'MAN', 2, id from ruoli where cod_ruolo ='Amministratore';
INSERT INTO acl (cod_tipo,cod_servizio,diritti,id_ruolo) select 'D', 'STAT', 2, id from ruoli where cod_ruolo ='Amministratore';
INSERT INTO acl (cod_tipo,cod_servizio,diritti,id_ruolo) select 'D', 'G_PAG', 1, id from ruoli where cod_ruolo ='Operatore';
INSERT INTO acl (cod_tipo,cod_servizio,diritti,id_ruolo) select 'D', 'G_RND', 1, id from ruoli where cod_ruolo ='Operatore';
INSERT INTO acl (cod_tipo,cod_servizio,diritti,id_ruolo) select 'D', 'GDE', 1, id from ruoli where cod_ruolo ='Operatore';

insert into sonde(nome, classe, soglia_warn, soglia_error) values ('caricamento-tracciati', 'org.openspcoop2.utils.sonde.impl.SondaBatch', 3600000, 21600000);
insert into sonde(nome, classe, soglia_warn, soglia_error) values ('check-tracciati', 'org.openspcoop2.utils.sonde.impl.SondaCoda', 1, 1);
insert into sonde(nome, classe, soglia_warn, soglia_error) values ('cons-req', 'org.openspcoop2.utils.sonde.impl.SondaBatch', 86400000, 172800000);
insert into sonde(nome, classe, soglia_warn, soglia_error) values ('cons-esito', 'org.openspcoop2.utils.sonde.impl.SondaBatch', 86400000, 172800000);
