select count(*) as totalePagamenti from rpt,versamenti,singoli_versamenti,rt where rpt.id_versamento=versamenti.id AND singoli_versamenti.id_versamento=rpt.id_versamento AND rt.id_rpt=rpt.id AND rpt.stato='RT_ACCETTATA_PA' AND versamenti.stato_versamento='PAGAMENTO_ESEGUITO' AND singoli_versamenti.stato_singolo_versamento='PAGATO' AND rt.stato='ACCETTATA';
-- AND data_ora_creazione > '2015-11-20 13:10:10.000';

select count(*) as rpt, stato from rpt group by stato;
select count(*) as versamenti, stato_versamento from versamenti group by stato_versamento;
select count(*) as singoli_versamenti ,stato_singolo_versamento from singoli_versamenti group by stato_singolo_versamento;
select count(*) as rt ,stato from rt group by stato;

select count(*) as esiti,stato_spedizione from esiti group by stato_spedizione;

select count(*) as anagrafiche from anagrafiche ;

select count(*) as tracciatixml from tracciatixml ;

select count(*) as canali from canali ;

select count(*) as psp from psp ;

select min(data_ora_creazione) from rpt;

select MAX(data_ora_ultima_spedizione) from esiti;
