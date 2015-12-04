INSERT INTO anagrafiche (ragione_sociale, cod_univoco) VALUES ('ADMIN','-');
INSERT INTO operatori (principal, profilo, id_anagrafica) VALUES ( 'admin', 'A', (SELECT id FROM anagrafiche WHERE cod_univoco = '-'))
