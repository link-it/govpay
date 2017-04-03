--GP-493
ALTER TABLE sonde ADD COLUMN data_ok TIMESTAMP(3);
UPDATE sonde SET dati_check=null;
