--GP-493
ALTER TABLE sonde ADD data_ok TIMESTAMP;
UPDATE sonde SET dati_check=null;
