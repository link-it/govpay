-- 29/08/2019 Autorizzazione utenze per UO
ALTER TABLE utenze_domini ADD COLUMN id_uo NUMBER;
ALTER TABLE utenze_domini ADD CONSTRAINT fk_nzd_id_uo FOREIGN KEY (id_uo) REFERENCES uo(id);
ALTER TABLE utenze_domini MODIFY (id_dominio NULL);
