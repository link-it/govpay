INSERT INTO operatori (principal,profilo,nome) VALUES ('@PRINCIPAL@','A','@RAGIONE_SOCIALE@');
INSERT INTO tipi_tributo (cod_tributo, descrizione) VALUES ('BOLLOT', 'Marca da Bollo Telematica');
insert into sonde(nome, classe, soglia_warn, soglia_error) values ('update-psp', 'org.openspcoop2.utils.sonde.impl.SondaBatch', 86400000, 172800000);
insert into sonde(nome, classe, soglia_warn, soglia_error) values ('update-rnd', 'org.openspcoop2.utils.sonde.impl.SondaBatch', 18000000, 86400000);
insert into sonde(nome, classe, soglia_warn, soglia_error) values ('update-pnd', 'org.openspcoop2.utils.sonde.impl.SondaBatch', 7200000, 43200000);
insert into sonde(nome, classe, soglia_warn, soglia_error) values ('update-ntfy', 'org.openspcoop2.utils.sonde.impl.SondaBatch', 86400000, 172800000);
insert into sonde(nome, classe, soglia_warn, soglia_error) values ('update-conto', 'org.openspcoop2.utils.sonde.impl.SondaBatch', 86400000, 172800000);
insert into sonde(nome, classe, soglia_warn, soglia_error) values ('check-ntfy', 'org.openspcoop2.utils.sonde.impl.SondaCoda', 10, 100);
