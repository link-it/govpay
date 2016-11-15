--GP-393
ALTER TABLE intermediari ADD COLUMN segregation_code INT;
ALTER TABLE applicazioni ADD COLUMN cod_applicazione_iuv VARCHAR(3);
ALTER TABLE domini ADD COLUMN aux_digit INT NOT NULL DEFAULT 0;
ALTER TABLE domini ADD COLUMN iuv_prefix VARCHAR(255);
ALTER TABLE domini ADD COLUMN iuv_prefix_strict BOOLEAN NOT NULL DEFAULT false;
ALTER TABLE tipi_tributo ADD COLUMN tipo_contabilita VARCHAR(1);
ALTER TABLE tipi_tributo ADD COLUMN cod_contabilita VARCHAR(255);
ALTER TABLE tipi_tributo ADD COLUMN cod_tributo_iuv VARCHAR(255);
ALTER TABLE tributi ADD COLUMN cod_tributo_iuv VARCHAR(255);

ALTER TABLE tributi MODIFY COLUMN tipo_contabilita VARCHAR(1) NULL;
ALTER TABLE tributi MODIFY COLUMN cod_contabilita VARCHAR(255) NULL;
