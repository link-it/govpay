ALTER TABLE versamenti MODIFY COLUMN tassonomia_avviso VARCHAR(35) NULL;

ALTER TABLE pagamenti_portale ADD COLUMN ack BOOLEAN NOT NULL DEFAULT FALSE;
ALTER TABLE pagamenti_portale ADD COLUMN note LONGTEXT;

ALTER TABLE pagamenti_portale ADD COLUMN tipo INT;
UPDATE pagamenti_portale set tipo=1;
ALTER TABLE pagamenti_portale MODIFY COLUMN tipo INT NOT NULL;

ALTER TABLE pagamenti_portale MODIFY COLUMN url_ritorno VARCHAR(1024) NULL;
