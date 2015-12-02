INSERT INTO anagrafiche (ragione_sociale,cod_univoco) VALUES ('@RAGIONA_SOCIALE@','@CODICE_FISCALE@');
INSERT INTO operatori (principal,profilo,id_anagrafica) VALUES ('@PRINCIPAL@','A',(select id from anagrafiche WHERE cod_univoco='@CODICE_FISCALE@'));
