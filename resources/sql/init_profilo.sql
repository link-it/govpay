INSERT INTO categorie_tributi 
(`ID_TRIBUTO`  ,`DE_TRB`         ,`CD_ADE`,`TP_ENTRATA`,`FL_PREDETERM`,`FL_INIZIATIVA`,`SOGG_ESCLUSI`,`CDPAGAMENTOSPONTANEO`,`STATO`,`PR_VERSIONE`,`OP_INSERIMENTO`,`TS_INSERIMENTO` ,`OP_AGGIORNAMENTO`,`TS_AGGIORNAMENTO`,`VERSION`) VALUES
('Categoria000','Debito Generico',NULL    ,NULL        ,NULL          ,NULL           ,NULL          ,NULL                  ,'A'    ,0            ,'SCRIPT'        ,CURRENT_TIMESTAMP,NULL              ,CURRENT_TIMESTAMP ,0        )
;
INSERT INTO categorie_enti 
(`TP_ENTE`    ,`DE_ENTE`      ,`STATO`,`PR_VERSIONE`,`OP_INSERIMENTO`,`TS_INSERIMENTO` ,`OP_AGGIORNAMENTO`,`TS_AGGIORNAMENTO`,`VERSION`) VALUES
('TipoEnte000','Ente Generico','A'    ,1            ,'SCRIPT'        ,CURRENT_TIMESTAMP,NULL              ,NULL              ,0        )
;
INSERT INTO indirizzi_postali 
(`ID`,`CAP`  ,`CASELLAPOSTALE`,`CODICEFISCALE`,`COMUNE`,`FAX`,`FLAGRESIDENTE`,`INDIRIZZO`,`NAZIONE`,`PARTITAIVA` ,`PROVINCIA`,`RECORDLOCK`,`TELEFONO`,`TELEFONOCELLULARE`,`EMAIL`,`NUMEROCIVICO`,`PR_VERSIONE`,`OP_INSERIMENTO`,`TS_INSERIMENTO` ,`OP_AGGIORNAMENTO`,`TS_AGGIORNAMENTO`,`VERSION`) VALUES
(1   ,'12345',''              ,'01234567890'  ,'Comune'      ,'080012345678'   ,NULL           ,'Viale dei Giardini'         ,'IT'       ,'01234567890','RO'       ,NULL        ,'080087654321'       ,'3331234567'                 ,'info@comune.it'     ,'1'            ,NULL         ,'SCRIPT'        ,CURRENT_TIMESTAMP,NULL              ,CURRENT_TIMESTAMP ,0        )
;
INSERT INTO intestatari 
(`ABI`,`ABIACCENTRATORE`,`BOLLOVIRTUALE`,`CAB`,`CHIAVESSB`,`CODICEPOSTEL`,`CODICESOFTWARE`,`DENOMINAZIONE`,`FUNZIONIABILITATE`,`GRUPPO`,`ID_INDIRIZZIPOSTALI`,`INTESTATARIO`    ,`LAPL`       ,`RAGIONESOCIALE`   ,`RAPL`,`RCZ`,`RECORDLOCK`,`SIA`,`STATO`,`TIPOINTESTATARIO`,`TIPOSICUREZZA`,`UFFPOSTALE`,`AZIENDAMIGRATA`,`IMPORTOMAXFLUSSO`,`NONRESIDENTE`,`TMB_PRIMA_ATT`  ,`TMB_ULTIMA_ATT` ,`CODICECUC`,`EMAIL`,`ISSR`,`CATEGORIA`,`SOTTOCATEGORIA`,`PR_VERSIONE`,`OP_INSERIMENTO`,`TS_INSERIMENTO` ,`OP_AGGIORNAMENTO`,`TS_AGGIORNAMENTO`,`VERSION`,`FLAG_COMUNICAZIONI`) VALUES
(NULL ,NULL             ,NULL           ,NULL ,NULL       ,NULL          ,NULL            ,NULL           ,NULL               ,NULL    ,1                    ,'ENTEBENEFICIARIO','01234567890','ENTE BENEFICIARIO',NULL  ,NULL ,NULL        ,NULL ,NULL   ,NULL              ,NULL           ,NULL        ,NULL            ,NULL              ,NULL          ,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,NULL       ,NULL   ,NULL  ,'EN'       ,NULL            ,1            ,'SCRIPT'        ,CURRENT_TIMESTAMP,NULL              ,CURRENT_TIMESTAMP ,0        ,NULL                )
;
INSERT INTO operatori 
(`OPERATORE`,`INTESTATARIO`    ,`CELLULARE`,`CODICEFIRMATARIO`,`DESCRIZIONE`,`NULL_COLL_FALL`,`ULTIMOCOLLEGAMENTO`,`BLOCCATO`,`FLAGABILITATO`,`EMAIL`,`NOME`          ,`PASSWORD`                                ,`USERNAME`     ,`DATASCADENZA`   ,`DATA_COLL_FALL`,`DATABLOCCO`,`ABILRAPP`,`PASSWORDDISPO`,`NUMCOLLFALLITIDISP`,`BLOCCATODISP`,`SCADPSWDISP`    ,`FUNZIONIABILITATE`,`PROFILAZIONEESTESA`,`CODICEFISCALE`,`COGNOME`    ,`PR_VERSIONE`,`OP_INSERIMENTO`,`TS_INSERIMENTO` ,`OP_AGGIORNAMENTO`,`TS_AGGIORNAMENTO`,`VERSION`) VALUES
('ADMIN'    ,'ENTEBENEFICIARIO',NULL       ,NULL              ,NULL         ,0               ,CURRENT_TIMESTAMP   ,0         ,NULL           ,NULL   ,'Amministratore','a84f686711affa7fc0609742104b0a548232cd53','ADMINISTRATOR',DATE '2050-01-01',NULL            ,NULL        ,NULL      ,NULL           ,NULL                ,NULL          ,CURRENT_TIMESTAMP,NULL               ,NULL                ,'ADMINISTRATOR','Piattaforma',1            ,'SCRIPT'        ,CURRENT_TIMESTAMP,NULL              ,CURRENT_TIMESTAMP ,0        )
;
INSERT INTO intestatari_operatori 
(`INTESTATARIO`    ,`OPERATORE`,`TP_OPERATORE`,`BLOCCATO`,`PR_VERSIONE`,`OP_INSERIMENTO`,`TS_INSERIMENTO` ,`OP_AGGIORNAMENTO`,`TS_AGGIORNAMENTO`,`VERSION`) VALUES
('ENTEBENEFICIARIO','ADMIN'    ,'AC'          ,0         ,1            ,'SCRIPT'        ,CURRENT_TIMESTAMP,NULL              ,CURRENT_TIMESTAMP ,0        )
;
INSERT INTO enti 
(`ID_ENTE`             ,`CD_ENTE`         ,`TP_ENTE`    ,`INTESTATARIO`    ,`DENOM`            ,`PROVINCIA`,`STATO`,`MAX_NUM_TRIBUTI`,`PR_VERSIONE`,`OP_INSERIMENTO`,`TS_INSERIMENTO` ,`OP_AGGIORNAMENTO`,`TS_AGGIORNAMENTO`,`VERSION`) VALUES
('00000000000000000000','EnteBeneficiario','TipoEnte000','ENTEBENEFICIARIO','Ente Beneficiario',NULL       ,'A'    ,1                ,1            ,'SCRIPT'        ,CURRENT_TIMESTAMP,NULL              ,NULL              ,0        )
;
INSERT INTO proprieta_connettori 
(ID_CONNETTORE,NOME_PROPRIETA      ,VALORE_PROPRIETA,OP_INSERIMENTO,TS_INSERIMENTO            ,OP_AGGIORNAMENTO,TS_AGGIORNAMENTO,VERSION) VALUES
(1            ,'TIPOAUTENTICAZIONE','NONE'     ,'SCRIPT'      ,{ts '2014-11-21 18:57:07'},null            ,null            ,0      )
;
INSERT INTO proprieta_connettori 
(ID_CONNETTORE,NOME_PROPRIETA,VALORE_PROPRIETA                                                    ,OP_INSERIMENTO,TS_INSERIMENTO            ,OP_AGGIORNAMENTO,TS_AGGIORNAMENTO,VERSION) VALUES
(1            ,'URL'         ,'http://localhost:8080/govpay-ndp-sym/PagamentiTelematiciRPTservice','SCRIPT'      ,{ts '2014-11-21 18:57:07'},null            ,null            ,0      )
;
INSERT INTO proprieta_connettori
(ID_CONNETTORE,NOME_PROPRIETA,VALORE_PROPRIETA                                                    ,OP_INSERIMENTO,TS_INSERIMENTO            ,OP_AGGIORNAMENTO,TS_AGGIORNAMENTO,VERSION) VALUES
(1            ,'AZIONEINURL' ,'false',                                                              'SCRIPT'      ,{ts '2014-11-21 18:57:07'},null            ,null            ,0      )
;
INSERT INTO intermediari 
(ID_INTERMEDIARIO,ID_CONNETTORE_PDD,OP_INSERIMENTO,TS_INSERIMENTO             ,NOME_SOGGETTO_SPC ) VALUES
('01234567890'   ,1                ,'SCRIPT'      ,{ts '2014-11-21 18:59:02.'},'SimulatoreNdp')
;
INSERT INTO domini 
(ID_DOMINIO   ,ID_ENTE               ,OP_INSERIMENTO,TS_INSERIMENTO             ) VALUES
('01234567890','00000000000000000000','SCRIPT'      ,{ts '2014-11-21 19:01:13.'})
;

-- Scadenzario Demo

INSERT INTO proprieta_connettori 
(ID_CONNETTORE,NOME_PROPRIETA      ,VALORE_PROPRIETA,OP_INSERIMENTO,TS_INSERIMENTO            ,OP_AGGIORNAMENTO,TS_AGGIORNAMENTO,VERSION) VALUES
(2            ,'TIPOAUTENTICAZIONE','NONE'     ,'SCRIPT'      ,{ts '2014-11-21 18:57:07'},null            ,null            ,0      )
;
INSERT INTO proprieta_connettori 
(ID_CONNETTORE,NOME_PROPRIETA,VALORE_PROPRIETA               ,OP_INSERIMENTO,TS_INSERIMENTO            ,OP_AGGIORNAMENTO,TS_AGGIORNAMENTO,VERSION) VALUES
(2            ,'URL'         ,'http://localhost:8080/govpayDemo/demo/Scadenzario','SCRIPT'      ,{ts '2014-11-21 18:57:07'},null            ,null            ,0      )
;
INSERT INTO proprieta_connettori 
(ID_CONNETTORE,NOME_PROPRIETA      ,VALORE_PROPRIETA,OP_INSERIMENTO,TS_INSERIMENTO            ,OP_AGGIORNAMENTO,TS_AGGIORNAMENTO,VERSION) VALUES
(3            ,'TIPOAUTENTICAZIONE','NONE'     ,'SCRIPT'      ,{ts '2014-11-21 18:57:07'},null            ,null            ,0      )
;
INSERT INTO proprieta_connettori 
(ID_CONNETTORE,NOME_PROPRIETA,VALORE_PROPRIETA               ,OP_INSERIMENTO,TS_INSERIMENTO            ,OP_AGGIORNAMENTO,TS_AGGIORNAMENTO,VERSION) VALUES
(3            ,'URL'         ,'http://localhost:8080/govpayDemo/demo/Scadenzario','SCRIPT'      ,{ts '2014-11-21 18:57:07'},null            ,null            ,0      )
;
INSERT INTO stazioni_intermediario 
(ID_STAZIONE     ,ID_INTERMEDIARIO, PASSWORD, OP_INSERIMENTO,TS_INSERIMENTO             ) VALUES
('01234567890_01','01234567890'   , 'p4ssw0rd_1','SCRIPT'      ,{ts '2014-11-21 19:04:04.'})
;
INSERT INTO sil 
(`ID_ENTE`             ,`ID_SYSTEM`,`ID_STAZIONE`   ,`DE_SYSTEM`,`STATO`,`PR_VERSIONE`,`OP_INSERIMENTO`,`TS_INSERIMENTO` ,`OP_AGGIORNAMENTO`,`TS_AGGIORNAMENTO`,`VERSION`) VALUES
('00000000000000000000','SC_SIM'   ,'01234567890_01','ScadenzarioSimulato'      ,'A'    ,0            ,'SCRIPT'        ,CURRENT_TIMESTAMP,NULL              ,NULL              ,0        )
;
INSERT INTO tributi_ente 
(`ID_ENTE`             ,`CD_TRB_ENTE`    ,`ID_TRIBUTO`  ,`ID_SYSTEM`,`DE_TRB`         ,`FL_INIZIATIVA`,`FL_PREDETERM`,`SOGG_ESCLUSI`,`IBAN`                   ,`STATO`,`PR_VERSIONE`,`OP_INSERIMENTO`,`TS_INSERIMENTO` ,`OP_AGGIORNAMENTO`,`TS_AGGIORNAMENTO`,`VERSION`,`FL_NOTIFICA_PAGAMENTO`,`INFO_TRIBUTO`,`URL_UPD_SERVICE`,`URL_INFO_SERVICE`,`FL_RICEVUTA_ANONIMO`,`FL_NASCOSTO_FE`,`ISTRUZIONI_PAGAMENTO`) VALUES
('00000000000000000000','DEBITO_GENERICO','Categoria000','SC_SIM'      ,'Debito Generico','Y'            ,'N'           ,NULL          ,'INSERIRE_IBAN_ACCREDITO','A'    ,0            ,'SCRIPT'        ,CURRENT_TIMESTAMP,NULL              ,NULL              ,0        ,'N'                    ,NULL          ,''               ,''                ,'Y'                  ,'N'             ,''                    )
;
UPDATE sil SET ID_CONNETTORE_NOTIFICA=2,ID_CONNETTORE_PAGATTESA=3
WHERE (ID_ENTE='00000000000000000000' AND ID_SYSTEM='SC_SIM')
;

-- Scadenzario Testsuite

INSERT INTO proprieta_connettori 
(ID_CONNETTORE,NOME_PROPRIETA      ,VALORE_PROPRIETA,OP_INSERIMENTO,TS_INSERIMENTO            ,OP_AGGIORNAMENTO,TS_AGGIORNAMENTO,VERSION) VALUES
(4            ,'TIPOAUTENTICAZIONE','NONE'     ,'SCRIPT'      ,{ts '2014-11-21 18:57:07'},null            ,null            ,0      )
;
INSERT INTO proprieta_connettori 
(ID_CONNETTORE,NOME_PROPRIETA,VALORE_PROPRIETA               ,OP_INSERIMENTO,TS_INSERIMENTO            ,OP_AGGIORNAMENTO,TS_AGGIORNAMENTO,VERSION) VALUES
(4            ,'URL'         ,'http://localhost:6789/notifica','SCRIPT'      ,{ts '2014-11-21 18:57:07'},null            ,null            ,0      )
;
INSERT INTO proprieta_connettori 
(ID_CONNETTORE,NOME_PROPRIETA      ,VALORE_PROPRIETA,OP_INSERIMENTO,TS_INSERIMENTO            ,OP_AGGIORNAMENTO,TS_AGGIORNAMENTO,VERSION) VALUES
(5            ,'TIPOAUTENTICAZIONE','NONE'          ,'SCRIPT'      ,{ts '2014-11-21 18:57:07'},null            ,null            ,0      )
;
INSERT INTO proprieta_connettori 
(ID_CONNETTORE,NOME_PROPRIETA,VALORE_PROPRIETA               ,OP_INSERIMENTO,TS_INSERIMENTO            ,OP_AGGIORNAMENTO,TS_AGGIORNAMENTO,VERSION) VALUES
(5            ,'URL'         ,'http://localhost:6789/scadenzario','SCRIPT'      ,{ts '2014-11-21 18:57:07'},null            ,null            ,0      )
;
INSERT INTO stazioni_intermediario 
(ID_STAZIONE     ,ID_INTERMEDIARIO,PASSWORD    ,OP_INSERIMENTO,TS_INSERIMENTO             ) VALUES
('01234567890_02','01234567890'   ,'p4ssw0rd_2','SCRIPT'      ,{ts '2014-11-21 19:04:04.'})
;
INSERT INTO sil 
(`ID_ENTE`             ,`ID_SYSTEM`,`ID_STAZIONE`   ,`DE_SYSTEM`            ,`STATO`,`PR_VERSIONE`,`OP_INSERIMENTO`,`TS_INSERIMENTO` ,`OP_AGGIORNAMENTO`,`TS_AGGIORNAMENTO`,`VERSION`) VALUES
('00000000000000000000','SC_TS1'   ,'01234567890_02','ScadenzarioTestsuite1','A'    ,0            ,'SCRIPT'        ,CURRENT_TIMESTAMP,NULL              ,NULL              ,0        )
;
INSERT INTO tributi_ente 
(`ID_ENTE`             ,`CD_TRB_ENTE`,`ID_TRIBUTO`  ,`ID_SYSTEM`,`DE_TRB`             ,`FL_INIZIATIVA`,`FL_PREDETERM`,`SOGG_ESCLUSI`,`IBAN`                   ,`STATO`,`PR_VERSIONE`,`OP_INSERIMENTO`,`TS_INSERIMENTO` ,`OP_AGGIORNAMENTO`,`TS_AGGIORNAMENTO`,`VERSION`,`FL_NOTIFICA_PAGAMENTO`,`INFO_TRIBUTO`,`URL_UPD_SERVICE`,`URL_INFO_SERVICE`,`FL_RICEVUTA_ANONIMO`,`FL_NASCOSTO_FE`,`ISTRUZIONI_PAGAMENTO`) VALUES
('00000000000000000000','DEBITO1_TS1','Categoria000','SC_TS1'   ,'Debito1 Testsuite 1','Y'            ,'N'           ,NULL          ,'INSERIRE_IBAN_ACCREDITO','A'    ,0            ,'SCRIPT'        ,CURRENT_TIMESTAMP,NULL              ,NULL              ,0        ,'N'                    ,NULL          ,''               ,''                ,'Y'                  ,'N'             ,''                    )
;
UPDATE sil SET ID_CONNETTORE_NOTIFICA=4,ID_CONNETTORE_PAGATTESA=5
WHERE (ID_ENTE='00000000000000000000' AND ID_SYSTEM='SC_TS1')
;




INSERT INTO proprieta_connettori 
(ID_CONNETTORE,NOME_PROPRIETA      ,VALORE_PROPRIETA,OP_INSERIMENTO,TS_INSERIMENTO            ,OP_AGGIORNAMENTO,TS_AGGIORNAMENTO,VERSION) VALUES
(6            ,'TIPOAUTENTICAZIONE','NONE'     ,'SCRIPT'      ,{ts '2014-11-21 18:57:07'},null            ,null            ,0      )
;
INSERT INTO proprieta_connettori 
(ID_CONNETTORE,NOME_PROPRIETA,VALORE_PROPRIETA               ,OP_INSERIMENTO,TS_INSERIMENTO            ,OP_AGGIORNAMENTO,TS_AGGIORNAMENTO,VERSION) VALUES
(6            ,'URL'         ,'http://localhost:6789/notifica2','SCRIPT'      ,{ts '2014-11-21 18:57:07'},null            ,null            ,0      )
;
INSERT INTO proprieta_connettori 
(ID_CONNETTORE,NOME_PROPRIETA      ,VALORE_PROPRIETA,OP_INSERIMENTO,TS_INSERIMENTO            ,OP_AGGIORNAMENTO,TS_AGGIORNAMENTO,VERSION) VALUES
(7            ,'TIPOAUTENTICAZIONE','NONE'     ,'SCRIPT'      ,{ts '2014-11-21 18:57:07'},null            ,null            ,0      )
;
INSERT INTO proprieta_connettori 
(ID_CONNETTORE,NOME_PROPRIETA,VALORE_PROPRIETA               ,OP_INSERIMENTO,TS_INSERIMENTO            ,OP_AGGIORNAMENTO,TS_AGGIORNAMENTO,VERSION) VALUES
(7            ,'URL'         ,'http://localhost:6789/scadenzario2','SCRIPT'      ,{ts '2014-11-21 18:57:07'},null            ,null            ,0      )
;
INSERT INTO stazioni_intermediario 
(ID_STAZIONE     ,ID_INTERMEDIARIO,PASSWORD    ,OP_INSERIMENTO,TS_INSERIMENTO             ) VALUES
('01234567890_03','01234567890'   ,'p4ssw0rd_3','SCRIPT'      ,{ts '2014-11-21 19:04:04.'})
;
INSERT INTO sil 
(`ID_ENTE`             ,`ID_SYSTEM`,`ID_STAZIONE`   ,`DE_SYSTEM`                  ,`STATO`,`PR_VERSIONE`,`OP_INSERIMENTO`,`TS_INSERIMENTO` ,`OP_AGGIORNAMENTO`,`TS_AGGIORNAMENTO`,`VERSION`) VALUES
('00000000000000000000','SC_TS2'   ,'01234567890_03','ScadenzarioTestsuite2'      ,'A'    ,0            ,'SCRIPT'        ,CURRENT_TIMESTAMP,NULL              ,NULL              ,0        )
;
INSERT INTO tributi_ente 
(`ID_ENTE`             ,`CD_TRB_ENTE`    ,`ID_TRIBUTO`  ,`ID_SYSTEM`,`DE_TRB`         ,`FL_INIZIATIVA`,`FL_PREDETERM`,`SOGG_ESCLUSI`,`IBAN`                   ,`STATO`,`PR_VERSIONE`,`OP_INSERIMENTO`,`TS_INSERIMENTO` ,`OP_AGGIORNAMENTO`,`TS_AGGIORNAMENTO`,`VERSION`,`FL_NOTIFICA_PAGAMENTO`,`INFO_TRIBUTO`,`URL_UPD_SERVICE`,`URL_INFO_SERVICE`,`FL_RICEVUTA_ANONIMO`,`FL_NASCOSTO_FE`,`ISTRUZIONI_PAGAMENTO`) VALUES
('00000000000000000000','DEBITO1_TS2','Categoria000','SC_TS2'      ,'Debito1 Testsuite 2','Y'            ,'N'           ,NULL          ,'INSERIRE_IBAN_ACCREDITO','A'    ,0            ,'SCRIPT'        ,CURRENT_TIMESTAMP,NULL              ,NULL              ,0        ,'N'                    ,NULL          ,''               ,''                ,'Y'                  ,'N'             ,''                    )
;
UPDATE sil SET ID_CONNETTORE_NOTIFICA=6,ID_CONNETTORE_PAGATTESA=7
WHERE (ID_ENTE='00000000000000000000' AND ID_SYSTEM='SC_TS2')
;
