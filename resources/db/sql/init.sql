-- Censimento dell'utenza amministratore

INSERT INTO utenze (principal) VALUES ('@PRINCIPAL@');
INSERT INTO operatori (nome, id_utenza) VALUES ('@RAGIONE_SOCIALE@', select id from utenze where principal = '@PRINCIPAL@');
INSERT INTO acl(ruolo,principal,servizio,diritti) VALUES (null,'@PRINCIPAL@','Anagrafica Applicazioni','RWX');
INSERT INTO acl(ruolo,principal,servizio,diritti) VALUES (null,'@PRINCIPAL@','Anagrafica Creditore','RWX');
INSERT INTO acl(ruolo,principal,servizio,diritti) VALUES (null,'@PRINCIPAL@','Rendicontazioni e Incassi','RWX');
INSERT INTO acl(ruolo,principal,servizio,diritti) VALUES (null,'@PRINCIPAL@','Pagamenti e Pendenze','RWX');
INSERT INTO acl(ruolo,principal,servizio,diritti) VALUES (null,'@PRINCIPAL@','Giornale degli Eventi','RWX');
INSERT INTO acl(ruolo,principal,servizio,diritti) VALUES (null,'@PRINCIPAL@','Configurazione e manutenzione','RWX');
INSERT INTO acl(ruolo,principal,servizio,diritti) VALUES (null,'@PRINCIPAL@','Statistiche','RWX');
INSERT INTO acl(ruolo,principal,servizio,diritti) VALUES (null,'@PRINCIPAL@','Anagrafica PagoPA','RWX');
INSERT INTO acl(ruolo,principal,servizio,diritti) VALUES (null,'@PRINCIPAL@','Anagrafica Ruoli','RWX');
-- Censimento del ruolo amministratore

INSERT INTO acl(ruolo,principal,servizio,diritti) VALUES ('Amministratore',null,'Anagrafica Applicazioni','RWX');
INSERT INTO acl(ruolo,principal,servizio,diritti) VALUES ('Amministratore',null,'Anagrafica Creditore','RWX');
INSERT INTO acl(ruolo,principal,servizio,diritti) VALUES ('Amministratore',null,'Rendicontazioni e Incassi','RWX');
INSERT INTO acl(ruolo,principal,servizio,diritti) VALUES ('Amministratore',null,'Pagamenti e Pendenze','RWX');
INSERT INTO acl(ruolo,principal,servizio,diritti) VALUES ('Amministratore',null,'Giornale degli Eventi','RWX');
INSERT INTO acl(ruolo,principal,servizio,diritti) VALUES ('Amministratore',null,'Configurazione e manutenzione','RWX');
INSERT INTO acl(ruolo,principal,servizio,diritti) VALUES ('Amministratore',null,'Statistiche','RWX');
INSERT INTO acl(ruolo,principal,servizio,diritti) VALUES ('Amministratore',null,'Anagrafica PagoPA','RWX');
INSERT INTO acl(ruolo,principal,servizio,diritti) VALUES ('Amministratore',null,'Anagrafica Ruoli','RWX');

-- Censimento del ruolo operatore

INSERT INTO acl(ruolo,principal,servizio,diritti) VALUES ('Operatore',null,'Rendicontazioni e Incassi','R');
INSERT INTO acl(ruolo,principal,servizio,diritti) VALUES ('Operatore',null,'Pagamenti e Pendenze','R');
INSERT INTO acl(ruolo,principal,servizio,diritti) VALUES ('Operatore',null,'Giornale degli Eventi','R');

-- Censimento Tributo Bollo

INSERT INTO tipi_tributo (cod_tributo, descrizione) VALUES ('BOLLOT', 'Marca da Bollo Telematica');

-- Configurazione delle sonde

insert into sonde(nome, classe, soglia_warn, soglia_error) values ('update-psp', 'org.openspcoop2.utils.sonde.impl.SondaBatch', 86400000, 172800000);
insert into sonde(nome, classe, soglia_warn, soglia_error) values ('update-rnd', 'org.openspcoop2.utils.sonde.impl.SondaBatch', 18000000, 86400000);
insert into sonde(nome, classe, soglia_warn, soglia_error) values ('update-pnd', 'org.openspcoop2.utils.sonde.impl.SondaBatch', 7200000, 43200000);
insert into sonde(nome, classe, soglia_warn, soglia_error) values ('update-ntfy', 'org.openspcoop2.utils.sonde.impl.SondaBatch', 86400000, 172800000);
insert into sonde(nome, classe, soglia_warn, soglia_error) values ('update-conto', 'org.openspcoop2.utils.sonde.impl.SondaBatch', 86400000, 172800000);
insert into sonde(nome, classe, soglia_warn, soglia_error) values ('check-ntfy', 'org.openspcoop2.utils.sonde.impl.SondaCoda', 10, 100);
insert into sonde(nome, classe, soglia_warn, soglia_error) values ('caricamento-tracciati', 'org.openspcoop2.utils.sonde.impl.SondaBatch', 3600000, 21600000);
insert into sonde(nome, classe, soglia_warn, soglia_error) values ('check-tracciati', 'org.openspcoop2.utils.sonde.impl.SondaCoda', 1, 1);
insert into sonde(nome, classe, soglia_warn, soglia_error) values ('cons-req', 'org.openspcoop2.utils.sonde.impl.SondaBatch', 86400000, 172800000);
insert into sonde(nome, classe, soglia_warn, soglia_error) values ('cons-esito', 'org.openspcoop2.utils.sonde.impl.SondaBatch', 86400000, 172800000);
insert into sonde(nome, classe, soglia_warn, soglia_error) values ('generazione-avvisi', 'org.openspcoop2.utils.sonde.impl.SondaBatch', 3600000, 21600000);
