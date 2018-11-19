CREATE TABLE sonde
(
	nome VARCHAR(35) NOT NULL,
	classe VARCHAR(255) NOT NULL,
	soglia_warn BIGINT NOT NULL,
	soglia_error BIGINT NOT NULL,
	data_warn TIMESTAMP,
	data_error TIMESTAMP,
	data_ultimo_check TIMESTAMP,
	dati_check TEXT,
	stato_ultimo_check INT,
	-- fk/pk columns
	-- fk/pk keys constraints
	CONSTRAINT pk_sonde PRIMARY KEY (nome)
);

insert into sonde(nome, classe, soglia_warn, soglia_error) values ('update-psp', 'org.openspcoop2.utils.sonde.impl.SondaBatch', 86400000, 172800000);
insert into sonde(nome, classe, soglia_warn, soglia_error) values ('update-rnd', 'org.openspcoop2.utils.sonde.impl.SondaBatch', 18000000, 86400000);
insert into sonde(nome, classe, soglia_warn, soglia_error) values ('update-pnd', 'org.openspcoop2.utils.sonde.impl.SondaBatch', 7200000, 43200000);
insert into sonde(nome, classe, soglia_warn, soglia_error) values ('update-ntfy', 'org.openspcoop2.utils.sonde.impl.SondaBatch', 86400000, 172800000);
insert into sonde(nome, classe, soglia_warn, soglia_error) values ('update-conto', 'org.openspcoop2.utils.sonde.impl.SondaBatch', 86400000, 172800000);
insert into sonde(nome, classe, soglia_warn, soglia_error) values ('check-ntfy', 'org.openspcoop2.utils.sonde.impl.SondaCoda', 10, 100);
