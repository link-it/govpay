-- 29/08/2019 Autorizzazione utenze per UO
ALTER TABLE utenze_domini ADD COLUMN id_uo BIGINT;
ALTER TABLE utenze_domini ADD CONSTRAINT fk_nzd_id_uo FOREIGN KEY (id_uo) REFERENCES uo(id);
ALTER TABLE utenze_domini ALTER COLUMN id_dominio DROP NOT NULL;
