--GP-393
ALTER TABLE intermediari ADD segregation_code NUMBER;
ALTER TABLE applicazioni ADD cod_applicazione_iuv VARCHAR2(3 CHAR);
ALTER TABLE domini ADD aux_digit NUMBER NOT NULL;
ALTER TABLE domini ADD iuv_prefix VARCHAR2(255 CHAR);
ALTER TABLE domini ADD iuv_prefix_strict NUMBER NOT NULL;
ALTER TABLE domini MODIFY aux_digit DEFAULT 0;
ALTER TABLE domini MODIFY iuv_prefix_strict DEFAULT 0;
ALTER TABLE tipi_tributo ADD tipo_contabilita VARCHAR2(1 CHAR);
ALTER TABLE tipi_tributo ADD cod_contabilita VARCHAR2(255 CHAR);
ALTER TABLE tipi_tributo ADD cod_tributo_iuv VARCHAR2(4 CHAR);
ALTER TABLE tributi ADD cod_tributo_iuv VARCHAR2(4 CHAR);
ALTER TABLE tributi MODIFY (tipo_contabilita NULL);
ALTER TABLE tributi MODIFY (codice_contabilita NULL);

--GP-406
ALTER TABLE versamenti ADD debitore_email VARCHAR2(255 CHAR);
ALTER TABLE versamenti ADD debitore_telefono VARCHAR2(35 CHAR);
ALTER TABLE versamenti ADD debitore_cellulare VARCHAR2(35 CHAR);
ALTER TABLE versamenti ADD debitore_fax VARCHAR2(35 CHAR);
