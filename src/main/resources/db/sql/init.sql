-- Censimento dell'utenza amministratore

INSERT INTO utenze (principal,principal_originale) VALUES ('@PRINCIPAL@','@PRINCIPAL@');
INSERT INTO operatori (nome, id_utenza) VALUES ('@RAGIONE_SOCIALE@', (select id from utenze where principal = '@PRINCIPAL@'));
INSERT INTO acl(ruolo,id_utenza,servizio,diritti) VALUES (null,(select id from utenze where principal = '@PRINCIPAL@'),'Anagrafica Applicazioni','RW');
INSERT INTO acl(ruolo,id_utenza,servizio,diritti) VALUES (null,(select id from utenze where principal = '@PRINCIPAL@'),'Anagrafica Creditore','RW');
INSERT INTO acl(ruolo,id_utenza,servizio,diritti) VALUES (null,(select id from utenze where principal = '@PRINCIPAL@'),'Rendicontazioni e Incassi','RW');
INSERT INTO acl(ruolo,id_utenza,servizio,diritti) VALUES (null,(select id from utenze where principal = '@PRINCIPAL@'),'Pagamenti','RW');
INSERT INTO acl(ruolo,id_utenza,servizio,diritti) VALUES (null,(select id from utenze where principal = '@PRINCIPAL@'),'Pendenze','RW');
INSERT INTO acl(ruolo,id_utenza,servizio,diritti) VALUES (null,(select id from utenze where principal = '@PRINCIPAL@'),'Giornale degli Eventi','RW');
INSERT INTO acl(ruolo,id_utenza,servizio,diritti) VALUES (null,(select id from utenze where principal = '@PRINCIPAL@'),'Configurazione e manutenzione','RW');
INSERT INTO acl(ruolo,id_utenza,servizio,diritti) VALUES (null,(select id from utenze where principal = '@PRINCIPAL@'),'Anagrafica PagoPA','RW');
INSERT INTO acl(ruolo,id_utenza,servizio,diritti) VALUES (null,(select id from utenze where principal = '@PRINCIPAL@'),'Anagrafica Ruoli','RW');
-- Censimento del ruolo amministratore

INSERT INTO acl(ruolo,id_utenza,servizio,diritti) VALUES ('Amministratore',null,'Anagrafica Applicazioni','RW');
INSERT INTO acl(ruolo,id_utenza,servizio,diritti) VALUES ('Amministratore',null,'Anagrafica Creditore','RW');
INSERT INTO acl(ruolo,id_utenza,servizio,diritti) VALUES ('Amministratore',null,'Rendicontazioni e Incassi','RW');
INSERT INTO acl(ruolo,id_utenza,servizio,diritti) VALUES ('Amministratore',null,'Pagamenti','RW');
INSERT INTO acl(ruolo,id_utenza,servizio,diritti) VALUES ('Amministratore',null,'Pendenze','RW');
INSERT INTO acl(ruolo,id_utenza,servizio,diritti) VALUES ('Amministratore',null,'Giornale degli Eventi','RW');
INSERT INTO acl(ruolo,id_utenza,servizio,diritti) VALUES ('Amministratore',null,'Configurazione e manutenzione','RW');
INSERT INTO acl(ruolo,id_utenza,servizio,diritti) VALUES ('Amministratore',null,'Anagrafica PagoPA','RW');
INSERT INTO acl(ruolo,id_utenza,servizio,diritti) VALUES ('Amministratore',null,'Anagrafica Ruoli','RW');

-- Censimento del ruolo operatore

INSERT INTO acl(ruolo,id_utenza,servizio,diritti) VALUES ('Operatore',null,'Rendicontazioni e Incassi','R');
INSERT INTO acl(ruolo,id_utenza,servizio,diritti) VALUES ('Operatore',null,'Pagamenti','R');
INSERT INTO acl(ruolo,id_utenza,servizio,diritti) VALUES ('Operatore',null,'Pendenze','R');
INSERT INTO acl(ruolo,id_utenza,servizio,diritti) VALUES ('Operatore',null,'Giornale degli Eventi','R');

-- Censimento Tributo Bollo

INSERT INTO tipi_tributo (cod_tributo, tipo_contabilita, cod_contabilita, descrizione) VALUES ('BOLLOT', '9', 'MBT', 'Marca da Bollo Telematica');

-- Censimento Tipo Pendenza Libera
INSERT INTO tipi_versamento (cod_tipo_versamento, descrizione, tipo, paga_terzi, abilitato) VALUES ('LIBERO', 'Pendenza libera', 'DOVUTO', false, true);

-- Censimento Tipo Pendenza Bollo
INSERT INTO tipi_versamento (cod_tipo_versamento, descrizione, tipo, paga_terzi, abilitato) VALUES ('BOLLOT', 'Marca da Bollo Telematica', 'DOVUTO', false, true);

-- Configurazione delle sonde

insert into sonde(nome, classe, soglia_warn, soglia_error) values ('update-psp', 'org.openspcoop2.utils.sonde.impl.SondaBatch', 86400000, 172800000);
insert into sonde(nome, classe, soglia_warn, soglia_error) values ('update-rnd', 'org.openspcoop2.utils.sonde.impl.SondaBatch', 18000000, 86400000);
insert into sonde(nome, classe, soglia_warn, soglia_error) values ('update-pnd', 'org.openspcoop2.utils.sonde.impl.SondaBatch', 7200000, 43200000);
insert into sonde(nome, classe, soglia_warn, soglia_error) values ('update-ntfy', 'org.openspcoop2.utils.sonde.impl.SondaBatch', 86400000, 172800000);
insert into sonde(nome, classe, soglia_warn, soglia_error) values ('check-ntfy', 'org.openspcoop2.utils.sonde.impl.SondaCoda', 10, 100);
insert into sonde(nome, classe, soglia_warn, soglia_error) values ('generazione-avvisi', 'org.openspcoop2.utils.sonde.impl.SondaBatch', 3600000, 21600000);
insert into sonde(nome, classe, soglia_warn, soglia_error) values ('avvisatura-digitale', 'org.openspcoop2.utils.sonde.impl.SondaBatch', 3600000, 21600000);
insert into sonde(nome, classe, soglia_warn, soglia_error) values ('esito-avvisatura-digitale', 'org.openspcoop2.utils.sonde.impl.SondaBatch', 3600000, 21600000);
insert into sonde(nome, classe, soglia_warn, soglia_error) values ('caricamento-tracciati', 'org.openspcoop2.utils.sonde.impl.SondaBatch', 3600000, 21600000);
insert into sonde(nome, classe, soglia_warn, soglia_error) values ('check-tracciati', 'org.openspcoop2.utils.sonde.impl.SondaCoda', 1, 1);
insert into sonde(nome, classe, soglia_warn, soglia_error) values ('avvisatura-digitale-immediata', 'org.openspcoop2.utils.sonde.impl.SondaBatch', 86400000, 172800000);

-- Configurazione Generale 
INSERT INTO configurazione (giornale_eventi) values ('{"apiEnte":{"letture":{"log":"MAI","dump":"MAI"},"scritture":{"log":"SOLO_ERRORE","dump":"SOLO_ERRORE"}},"apiPagamento":{"letture":{"log":"MAI","dump":"MAI"},"scritture":{"log":"SOLO_ERRORE","dump":"SOLO_ERRORE"}},"apiRagioneria":{"letture":{"log":"MAI","dump":"MAI"},"scritture":{"log":"SOLO_ERRORE","dump":"SOLO_ERRORE"}},"apiBackoffice":{"letture":{"log":"MAI","dump":"MAI"},"scritture":{"log":"SOLO_ERRORE","dump":"SOLO_ERRORE"}},"apiPagoPA":{"letture":{"log":"SEMPRE","dump":"SOLO_ERRORE"},"scritture":{"log":"SEMPRE","dump":"SOLO_ERRORE"}}}');
