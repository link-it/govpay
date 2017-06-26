package it.govpay.bd.nativequeries;

import java.util.Date;

import it.govpay.bd.reportistica.statistiche.filters.TransazioniFilter;
import it.govpay.model.reportistica.statistiche.TipoIntervallo;

public class OracleNativeQueries extends NativeQueries {

	@Override
	public String getEstrattiContoQuery() {
		return "SELECT id_pagamento, id_singolo_versamento, id_versamento, data_pagamento, importo_dovuto, importo_pagato, iuv, iur1, iur2, cod_flusso_rendicontazione, cod_bic_riversamento, cod_versamento_ente, stato_versamento, cod_singolo_versamento_ente, stato_singolo_versamento, iban_accredito, debitore_identificativo, note, causale_versamento FROM ( SELECT id_pagamento, id_singolo_versamento, id_versamento, data_pagamento, importo_dovuto, importo_pagato, iuv, iur1, f.iur as iur2, f.cod_flusso as cod_flusso_rendicontazione, f.cod_bic_riversamento as cod_bic_riversamento, cod_versamento_ente, stato_versamento, cod_singolo_versamento_ente, stato_singolo_versamento, iban_accredito, debitore_identificativo, note, causale_versamento, ROW_NUMBER() OVER ( ORDER BY iuv,data_pagamento) AS rowNumber FROM ( SELECT id_pagamento, id_singolo_versamento, id_fr_applicazione, data_pagamento, importo_pagato, iur1, iuv, iban_accredito, cod_singolo_versamento_ente, note, id_versamento, cod_versamento_ente, stato_versamento, causale_versamento, debitore_identificativo, importo_dovuto, stato_singolo_versamento FROM ( ( select p.id as id_pagamento, p.id_singolo_versamento as id_singolo_versamento, p.id_fr_applicazione as id_fr_applicazione, p.data_pagamento as data_pagamento, p.importo_pagato as importo_pagato, p.iur as iur1, r.iuv as iuv, p.iban_accredito as iban_accredito, sv.cod_singolo_versamento_ente as cod_singolo_versamento_ente, sv.note as note, v.id as id_versamento, v.cod_versamento_ente as cod_versamento_ente, v.stato_versamento as stato_versamento, v.causale_versamento as causale_versamento, v.debitore_identificativo as debitore_identificativo, sv.importo_singolo_versamento as importo_dovuto, sv.stato_singolo_versamento as stato_singolo_versamento from pagamenti p join rpt r on r.id=p.id_rpt join singoli_versamenti sv on sv.id= p.id_singolo_versamento join versamenti v on v.id = sv.id_versamento $PLACE_HOLDER_1$ ) UNION ALL ( select rsr.id as id_pagamento, rsr.id_singolo_versamento as id_singolo_versamento, rsr.id_fr_applicazione as id_fr_applicazione, rsr.rendicontazione_data as data_pagamento, rsr.importo_pagato as importo_pagato, rsr.iur as iur1, iu.iuv as iuv, '' as iban_accredito, sv.cod_singolo_versamento_ente as cod_singolo_versamento_ente, sv.note as note, v.id as id_versamento, v.cod_versamento_ente as cod_versamento_ente, v.stato_versamento as stato_versamento, v.causale_versamento as causale_versamento, v.debitore_identificativo as debitore_identificativo, sv.importo_singolo_versamento as importo_dovuto, sv.stato_singolo_versamento as stato_singolo_versamento from rendicontazioni_senza_rpt rsr join iuv iu on iu.id=rsr.id_iuv join singoli_versamenti sv on sv.id= rsr.id_singolo_versamento join versamenti v on v.id = sv.id_versamento $PLACE_HOLDER_2$ ) ) order by iuv ASC, data_pagamento ASC ) pag left outer join fr_applicazioni fra on pag.id_fr_applicazione=fra.id left outer join fr f on f.id=fra.id_fr ) WHERE ( rowNumber > ? AND rowNumber <= ? ) ORDER BY cod_flusso_rendicontazione, iuv, data_pagamento";
	}

	@Override
	public String getEstrattiContoCountQuery() {
		return "SELECT sum (count_id_pagamento) as totale_pagamenti FROM ( ( select count (p.id) as count_id_pagamento from pagamenti p join rpt r on r.id=p.id_rpt join singoli_versamenti sv on sv.id= p.id_singolo_versamento join versamenti v on v.id = sv.id_versamento $PLACE_HOLDER_1$ ) UNION ALL ( select count (rsr.id) as count_id_pagamento from rendicontazioni_senza_rpt rsr join iuv iu on iu.id=rsr.id_iuv join singoli_versamenti sv on sv.id= rsr.id_singolo_versamento join versamenti v on v.id = sv.id_versamento $PLACE_HOLDER_2$ ) )";
	}

	@Override
	public String getRendicontazionePagamentoQuery() {
		return "SELECT " +
				" fr_cod_flusso,fr_stato,fr_descrizione_stato,fr_iur,fr_data_ora_flusso,fr_data_regolamento,fr_data_acquisizione,fr_numero_pagamenti,fr_importo_totale_pagamenti,fr_cod_bic_riversamento,fr_xml,fr_id,fr_cod_psp,fr_cod_dominio, " +
				" v_cod_versamento_ente,v_importo_totale,v_stato_versamento,v_descrizione_stato,v_aggiornabile,v_data_creazione,v_data_scadenza,v_data_ora_ultimo_aggiorn,v_causale_versamento,v_debitore_identificativo,v_debitore_anagrafica,v_debitore_indirizzo,v_debitore_civico,v_debitore_cap,v_debitore_localita,v_debitore_provincia,v_debitore_nazione,v_debitore_telefono,v_debitore_cellulare,v_debitore_fax,v_debitore_email,v_cod_lotto,v_cod_versamento_lotto,v_cod_anno_tributario,v_cod_bundlekey,v_id,v_id_uo,v_id_applicazione, " +
				" r_iuv,r_iur,r_importo_pagato,r_esito,r_data,r_stato,r_anomalie,r_id,r_id_fr,r_id_pagamento, " +
				" p_importo_pagato,p_data_acquisizione,p_iur,p_data_pagamento,p_commissioni_psp,p_tipo_allegato,p_allegato,p_data_acquisizione_revoca,p_causale_revoca,p_dati_revoca,p_importo_revocato,p_esito_revoca,p_dati_esito_revoca,p_id,p_id_rpt,p_id_singolo_versamento,p_id_rr,p_iban_accredito,p_cod_dominio,p_iuv, " +
				" sv_cod_singolo_versamento_ente,sv_stato_singolo_versamento,sv_importo_singolo_versamento,sv_tipo_bollo,sv_hash_documento,sv_provincia_residenza,sv_tipo_contabilita,sv_codice_contabilita,sv_note,sv_id,sv_id_versamento,sv_id_tributo,sv_id_iban_accredito,tipo " +
				" FROM (SELECT " +
				" fr.cod_flusso as fr_cod_flusso,fr.stato as fr_stato,fr.descrizione_stato as fr_descrizione_stato,fr.iur as fr_iur,fr.data_ora_flusso as fr_data_ora_flusso,fr.data_regolamento as fr_data_regolamento,fr.data_acquisizione as fr_data_acquisizione,fr.numero_pagamenti as fr_numero_pagamenti,fr.importo_totale_pagamenti as fr_importo_totale_pagamenti,fr.cod_bic_riversamento as fr_cod_bic_riversamento,fr.xml as fr_xml,fr.id as fr_id,fr.cod_psp as fr_cod_psp,fr.cod_dominio as fr_cod_dominio, " +
				" versamenti.cod_versamento_ente as v_cod_versamento_ente,versamenti.importo_totale as v_importo_totale,versamenti.stato_versamento as v_stato_versamento,versamenti.descrizione_stato as v_descrizione_stato,versamenti.aggiornabile as v_aggiornabile,versamenti.data_creazione as v_data_creazione,versamenti.data_scadenza as v_data_scadenza,versamenti.data_ora_ultimo_aggiornamento as v_data_ora_ultimo_aggiorn,versamenti.causale_versamento as v_causale_versamento,versamenti.debitore_identificativo as v_debitore_identificativo,versamenti.debitore_anagrafica as v_debitore_anagrafica,versamenti.debitore_indirizzo as v_debitore_indirizzo,versamenti.debitore_civico as v_debitore_civico,versamenti.debitore_cap as v_debitore_cap,versamenti.debitore_localita as v_debitore_localita,versamenti.debitore_provincia as v_debitore_provincia,versamenti.debitore_nazione as v_debitore_nazione,versamenti.debitore_telefono as v_debitore_telefono,versamenti.debitore_cellulare as v_debitore_cellulare,versamenti.debitore_fax as v_debitore_fax,versamenti.debitore_email as v_debitore_email,versamenti.cod_lotto as v_cod_lotto,versamenti.cod_versamento_lotto as v_cod_versamento_lotto,versamenti.cod_anno_tributario as v_cod_anno_tributario,versamenti.cod_bundlekey as v_cod_bundlekey,versamenti.id as v_id,versamenti.id_uo as v_id_uo,versamenti.id_applicazione as v_id_applicazione, " +
				" s1.r_iuv,s1.r_iur,s1.r_importo_pagato,s1.r_esito,s1.r_data,s1.r_stato,s1.r_anomalie,s1.r_id,s1.r_id_fr,s1.r_id_pagamento, " +
				" s1.p_importo_pagato,s1.p_data_acquisizione,s1.p_iur,s1.p_data_pagamento,s1.p_commissioni_psp,s1.p_tipo_allegato,s1.p_allegato,s1.p_data_acquisizione_revoca,s1.p_causale_revoca,s1.p_dati_revoca,s1.p_importo_revocato,s1.p_esito_revoca,s1.p_dati_esito_revoca,s1.p_id,s1.p_id_rpt,s1.p_id_singolo_versamento,s1.p_id_rr,s1.p_iban_accredito,s1.p_cod_dominio,s1.p_iuv, " +
				" s1.sv_cod_singolo_versamento_ente,s1.sv_stato_singolo_versamento,s1.sv_importo_singolo_versamento,s1.sv_tipo_bollo,s1.sv_hash_documento,s1.sv_provincia_residenza,s1.sv_tipo_contabilita,s1.sv_codice_contabilita,s1.sv_note,s1.sv_id,s1.sv_id_versamento,s1.sv_id_tributo,s1.sv_id_iban_accredito, s1.tipo, ROW_NUMBER() OVER ( ORDER BY p_data_pagamento)  AS rowNumber" +
				" FROM " +
				" (( " +
				" SELECT (CASE WHEN id_rpt is not null THEN CASE WHEN iban_accredito is not null THEN 'PAGAMENTO' ELSE 'PAGAMENTO_MBT' END ELSE 'PAGAMENTO_SENZA_RPT' END) as tipo, " +
				" r.iuv as r_iuv,r.iur as r_iur,r.importo_pagato as r_importo_pagato,r.esito as r_esito,r.data as r_data,r.stato as r_stato,r.anomalie as r_anomalie,r.id as r_id,r.id_fr as r_id_fr,r.id_pagamento as r_id_pagamento, " +
				" p.importo_pagato as p_importo_pagato,p.data_acquisizione as p_data_acquisizione,p.iur as p_iur,p.data_pagamento as p_data_pagamento,p.commissioni_psp as p_commissioni_psp,p.tipo_allegato as p_tipo_allegato,p.allegato as p_allegato,p.data_acquisizione_revoca as p_data_acquisizione_revoca,p.causale_revoca as p_causale_revoca,p.dati_revoca as p_dati_revoca,p.importo_revocato as p_importo_revocato,p.esito_revoca as p_esito_revoca,p.dati_esito_revoca as p_dati_esito_revoca,p.id as p_id,p.id_rpt as p_id_rpt,p.id_singolo_versamento as p_id_singolo_versamento,p.id_rr as p_id_rr,p.iban_accredito as p_iban_accredito,p.cod_dominio as p_cod_dominio,p.iuv as p_iuv, sv.cod_singolo_versamento_ente as sv_cod_singolo_versamento_ente,sv.stato_singolo_versamento as sv_stato_singolo_versamento,sv.importo_singolo_versamento as sv_importo_singolo_versamento,sv.tipo_bollo as sv_tipo_bollo,sv.hash_documento as sv_hash_documento,sv.provincia_residenza as sv_provincia_residenza,sv.tipo_contabilita as sv_tipo_contabilita,sv.codice_contabilita as sv_codice_contabilita,sv.note as sv_note,sv.id as sv_id,sv.id_versamento as sv_id_versamento,sv.id_tributo as sv_id_tributo,sv.id_iban_accredito as sv_id_iban_accredito " +
				"  " +
				" from pagamenti p join singoli_versamenti sv on sv.id= p.id_singolo_versamento " +
				" RIGHT JOIN rendicontazioni r on p.id = r.id_pagamento where (r.esito=0 or r.esito=9) $PLACEHOLDER_IN$ " +
				" ) " +
				" UNION ALL " +
				" ( " +
				" SELECT 'STORNO' as tipo, r.iuv as r_iuv,r.iur as r_iur,r.importo_pagato as r_importo_pagato,r.esito as r_esito,r.data as r_data,r.stato as r_stato,r.anomalie as r_anomalie,r.id as r_id,r.id_fr as r_id_fr,r.id_pagamento as r_id_pagamento, p.importo_pagato as p_importo_pagato,p.data_acquisizione as p_data_acquisizione,p.iur as p_iur,p.data_pagamento as p_data_pagamento,p.commissioni_psp as p_commissioni_psp,p.tipo_allegato as p_tipo_allegato,p.allegato as p_allegato,p.data_acquisizione_revoca as p_data_acquisizione_revoca,p.causale_revoca as p_causale_revoca,p.dati_revoca as p_dati_revoca,p.importo_revocato as p_importo_revocato,p.esito_revoca as p_esito_revoca,p.dati_esito_revoca as p_dati_esito_revoca,p.id as p_id,p.id_rpt as p_id_rpt,p.id_singolo_versamento as p_id_singolo_versamento,p.id_rr as p_id_rr,p.iban_accredito as p_iban_accredito,p.cod_dominio as p_cod_dominio,p.iuv as p_iuv, sv.cod_singolo_versamento_ente as sv_cod_singolo_versamento_ente,sv.stato_singolo_versamento as sv_stato_singolo_versamento,sv.importo_singolo_versamento as sv_importo_singolo_versamento,sv.tipo_bollo as sv_tipo_bollo,sv.hash_documento as sv_hash_documento,sv.provincia_residenza as sv_provincia_residenza,sv.tipo_contabilita as sv_tipo_contabilita,sv.codice_contabilita as sv_codice_contabilita,sv.note as sv_note,sv.id as sv_id,sv.id_versamento as sv_id_versamento,sv.id_tributo as sv_id_tributo,sv.id_iban_accredito as sv_id_iban_accredito " +
				" FROM pagamenti p join singoli_versamenti sv on sv.id= p.id_singolo_versamento and id_rr is not null " +
				" RIGHT JOIN rendicontazioni r on p.id = r.id_pagamento where r.esito=3 $PLACEHOLDER_IN$ " +
				" ) ) s1 " +
				" left join fr on fr.id = s1.r_id_fr left join versamenti on versamenti.id = s1.sv_id_versamento $PLACEHOLDER_OUT$) $PLACEHOLDER_OFFSET_LIMIT$ order by fr_cod_flusso, p_data_pagamento";
	}
	
	@Override
	public String getRendicontazionePagamentoCountQuery() {
		return  "SELECT count(*) " +
				" FROM " +
				" (( " +
				" SELECT r.id_fr as r_id_fr, sv.id_versamento as sv_id_versamento " +
				" from pagamenti p join singoli_versamenti sv on sv.id= p.id_singolo_versamento " +
				" RIGHT JOIN rendicontazioni r on p.id = r.id_pagamento where (r.esito=0 or r.esito=9) $PLACEHOLDER_IN$ " +
				" ) " +
				" UNION ALL" +
				" ( " +
				" SELECT r.id_fr as r_id_fr, sv.id_versamento as sv_id_versamento " +
				" FROM pagamenti p join singoli_versamenti sv on sv.id= p.id_singolo_versamento and id_rr is not null " +
				" RIGHT JOIN rendicontazioni r on p.id = r.id_pagamento where r.esito=3 $PLACEHOLDER_IN$ " +
				" ) ) s1 " +
				" left join fr on fr.id = s1.r_id_fr left join versamenti on versamenti.id = s1.sv_id_versamento $PLACEHOLDER_OUT$ ";
	}

	@Override
	public String getPagamentoRendicontazioneQuery() {
		return "SELECT " +
				" fr_cod_flusso,fr_stato,fr_descrizione_stato,fr_iur,fr_data_ora_flusso,fr_data_regolamento,fr_data_acquisizione,fr_numero_pagamenti,fr_importo_totale_pagamenti,fr_cod_bic_riversamento,fr_xml,fr_id,fr_cod_psp,fr_cod_dominio, " +
				" v_cod_versamento_ente,v_importo_totale,v_stato_versamento,v_descrizione_stato,v_aggiornabile,v_data_creazione,v_data_scadenza,v_data_ora_ultimo_aggiorn,v_causale_versamento,v_debitore_identificativo,v_debitore_anagrafica,v_debitore_indirizzo,v_debitore_civico,v_debitore_cap,v_debitore_localita,v_debitore_provincia,v_debitore_nazione,v_debitore_telefono,v_debitore_cellulare,v_debitore_fax,v_debitore_email,v_cod_lotto,v_cod_versamento_lotto,v_cod_anno_tributario,v_cod_bundlekey,v_id,v_id_uo,v_id_applicazione, " +
				" r_iuv,r_iur,r_importo_pagato,r_esito,r_data,r_stato,r_anomalie,r_id,r_id_fr,r_id_pagamento, " +
				" p_importo_pagato,p_data_acquisizione,p_iur,p_data_pagamento,p_commissioni_psp,p_tipo_allegato,p_allegato,p_data_acquisizione_revoca,p_causale_revoca,p_dati_revoca,p_importo_revocato,p_esito_revoca,p_dati_esito_revoca,p_id,p_id_rpt,p_id_singolo_versamento,p_id_rr,p_iban_accredito,p_cod_dominio,p_iuv, " +
				" sv_cod_singolo_versamento_ente,sv_stato_singolo_versamento,sv_importo_singolo_versamento,sv_tipo_bollo,sv_hash_documento,sv_provincia_residenza,sv_tipo_contabilita,sv_codice_contabilita,sv_note,sv_id,sv_id_versamento,sv_id_tributo,sv_id_iban_accredito,tipo " +
				" FROM (SELECT " +
				" fr.cod_flusso as fr_cod_flusso,fr.stato as fr_stato,fr.descrizione_stato as fr_descrizione_stato,fr.iur as fr_iur,fr.data_ora_flusso as fr_data_ora_flusso,fr.data_regolamento as fr_data_regolamento,fr.data_acquisizione as fr_data_acquisizione,fr.numero_pagamenti as fr_numero_pagamenti,fr.importo_totale_pagamenti as fr_importo_totale_pagamenti,fr.cod_bic_riversamento as fr_cod_bic_riversamento,fr.xml as fr_xml,fr.id as fr_id,fr.cod_psp as fr_cod_psp,fr.cod_dominio as fr_cod_dominio, " +
				" versamenti.cod_versamento_ente as v_cod_versamento_ente,versamenti.importo_totale as v_importo_totale,versamenti.stato_versamento as v_stato_versamento,versamenti.descrizione_stato as v_descrizione_stato,versamenti.aggiornabile as v_aggiornabile,versamenti.data_creazione as v_data_creazione,versamenti.data_scadenza as v_data_scadenza,versamenti.data_ora_ultimo_aggiornamento as v_data_ora_ultimo_aggiorn,versamenti.causale_versamento as v_causale_versamento,versamenti.debitore_identificativo as v_debitore_identificativo,versamenti.debitore_anagrafica as v_debitore_anagrafica,versamenti.debitore_indirizzo as v_debitore_indirizzo,versamenti.debitore_civico as v_debitore_civico,versamenti.debitore_cap as v_debitore_cap,versamenti.debitore_localita as v_debitore_localita,versamenti.debitore_provincia as v_debitore_provincia,versamenti.debitore_nazione as v_debitore_nazione,versamenti.debitore_telefono as v_debitore_telefono,versamenti.debitore_cellulare as v_debitore_cellulare,versamenti.debitore_fax as v_debitore_fax,versamenti.debitore_email as v_debitore_email,versamenti.cod_lotto as v_cod_lotto,versamenti.cod_versamento_lotto as v_cod_versamento_lotto,versamenti.cod_anno_tributario as v_cod_anno_tributario,versamenti.cod_bundlekey as v_cod_bundlekey,versamenti.id as v_id,versamenti.id_uo as v_id_uo,versamenti.id_applicazione as v_id_applicazione, " +
				" s1.r_iuv,s1.r_iur,s1.r_importo_pagato,s1.r_esito,s1.r_data,s1.r_stato,s1.r_anomalie,s1.r_id,s1.r_id_fr,s1.r_id_pagamento, " +
				" s1.p_importo_pagato,s1.p_data_acquisizione,s1.p_iur,s1.p_data_pagamento,s1.p_commissioni_psp,s1.p_tipo_allegato,s1.p_allegato,s1.p_data_acquisizione_revoca,s1.p_causale_revoca,s1.p_dati_revoca,s1.p_importo_revocato,s1.p_esito_revoca,s1.p_dati_esito_revoca,s1.p_id,s1.p_id_rpt,s1.p_id_singolo_versamento,s1.p_id_rr,s1.p_iban_accredito,s1.p_cod_dominio,s1.p_iuv, " +
				" s1.sv_cod_singolo_versamento_ente,s1.sv_stato_singolo_versamento,s1.sv_importo_singolo_versamento,s1.sv_tipo_bollo,s1.sv_hash_documento,s1.sv_provincia_residenza,s1.sv_tipo_contabilita,s1.sv_codice_contabilita,s1.sv_note,s1.sv_id,s1.sv_id_versamento,s1.sv_id_tributo,s1.sv_id_iban_accredito, s1.tipo, ROW_NUMBER() OVER ( ORDER BY p_data_pagamento) AS rowNumber" +
				" FROM " +
				" (( " +
				" SELECT (CASE WHEN id_rpt is not null THEN CASE WHEN iban_accredito is not null THEN 'PAGAMENTO' ELSE 'PAGAMENTO_MBT' END ELSE 'PAGAMENTO_SENZA_RPT' END) as tipo, " +
				" r.iuv as r_iuv,r.iur as r_iur,r.importo_pagato as r_importo_pagato,r.esito as r_esito,r.data as r_data,r.stato as r_stato,r.anomalie as r_anomalie,r.id as r_id,r.id_fr as r_id_fr,r.id_pagamento as r_id_pagamento, " +
				" p.importo_pagato as p_importo_pagato,p.data_acquisizione as p_data_acquisizione,p.iur as p_iur,p.data_pagamento as p_data_pagamento,p.commissioni_psp as p_commissioni_psp,p.tipo_allegato as p_tipo_allegato,p.allegato as p_allegato,p.data_acquisizione_revoca as p_data_acquisizione_revoca,p.causale_revoca as p_causale_revoca,p.dati_revoca as p_dati_revoca,p.importo_revocato as p_importo_revocato,p.esito_revoca as p_esito_revoca,p.dati_esito_revoca as p_dati_esito_revoca,p.id as p_id,p.id_rpt as p_id_rpt,p.id_singolo_versamento as p_id_singolo_versamento,p.id_rr as p_id_rr,p.iban_accredito as p_iban_accredito,p.cod_dominio as p_cod_dominio,p.iuv as p_iuv, sv.cod_singolo_versamento_ente as sv_cod_singolo_versamento_ente,sv.stato_singolo_versamento as sv_stato_singolo_versamento,sv.importo_singolo_versamento as sv_importo_singolo_versamento,sv.tipo_bollo as sv_tipo_bollo,sv.hash_documento as sv_hash_documento,sv.provincia_residenza as sv_provincia_residenza,sv.tipo_contabilita as sv_tipo_contabilita,sv.codice_contabilita as sv_codice_contabilita,sv.note as sv_note,sv.id as sv_id,sv.id_versamento as sv_id_versamento,sv.id_tributo as sv_id_tributo,sv.id_iban_accredito as sv_id_iban_accredito " +
				"  " +
				" from pagamenti p join singoli_versamenti sv on sv.id= p.id_singolo_versamento " +
				" LEFT JOIN rendicontazioni r on p.id = r.id_pagamento and (r.esito=0 or r.esito=9) $PLACEHOLDER_IN$ " +
				" ) " +
				" UNION ALL " +
				" ( " +
				" SELECT 'STORNO' as tipo, r.iuv as r_iuv,r.iur as r_iur,r.importo_pagato as r_importo_pagato,r.esito as r_esito,r.data as r_data,r.stato as r_stato,r.anomalie as r_anomalie,r.id as r_id,r.id_fr as r_id_fr,r.id_pagamento as r_id_pagamento, p.importo_pagato as p_importo_pagato,p.data_acquisizione as p_data_acquisizione,p.iur as p_iur,p.data_pagamento as p_data_pagamento,p.commissioni_psp as p_commissioni_psp,p.tipo_allegato as p_tipo_allegato,p.allegato as p_allegato,p.data_acquisizione_revoca as p_data_acquisizione_revoca,p.causale_revoca as p_causale_revoca,p.dati_revoca as p_dati_revoca,p.importo_revocato as p_importo_revocato,p.esito_revoca as p_esito_revoca,p.dati_esito_revoca as p_dati_esito_revoca,p.id as p_id,p.id_rpt as p_id_rpt,p.id_singolo_versamento as p_id_singolo_versamento,p.id_rr as p_id_rr,p.iban_accredito as p_iban_accredito,p.cod_dominio as p_cod_dominio,p.iuv as p_iuv, sv.cod_singolo_versamento_ente as sv_cod_singolo_versamento_ente,sv.stato_singolo_versamento as sv_stato_singolo_versamento,sv.importo_singolo_versamento as sv_importo_singolo_versamento,sv.tipo_bollo as sv_tipo_bollo,sv.hash_documento as sv_hash_documento,sv.provincia_residenza as sv_provincia_residenza,sv.tipo_contabilita as sv_tipo_contabilita,sv.codice_contabilita as sv_codice_contabilita,sv.note as sv_note,sv.id as sv_id,sv.id_versamento as sv_id_versamento,sv.id_tributo as sv_id_tributo,sv.id_iban_accredito as sv_id_iban_accredito " +
				" FROM pagamenti p join singoli_versamenti sv on sv.id= p.id_singolo_versamento and id_rr is not null " +
				" LEFT JOIN rendicontazioni r on p.id = r.id_pagamento and r.esito=3 $PLACEHOLDER_IN$ " +
				" ) ) s1 " +
				" left join fr on fr.id = s1.r_id_fr left join versamenti on versamenti.id = s1.sv_id_versamento $PLACEHOLDER_OUT$) $PLACEHOLDER_OFFSET_LIMIT$ order by p_data_pagamento DESC, fr_cod_flusso";
	}
	
	@Override
	public String getPagamentoRendicontazioneCountQuery() {
		return  "SELECT count(*) " +
				" FROM " +
				" (( " +
				" SELECT r.id_fr as r_id_fr, sv.id_versamento as sv_id_versamento " +
				" from pagamenti p join singoli_versamenti sv on sv.id= p.id_singolo_versamento " +
				" LEFT JOIN rendicontazioni r on p.id = r.id_pagamento and (r.esito=0 or r.esito=9) $PLACEHOLDER_IN$ " +
				" ) " +
				" UNION ALL" +
				" ( " +
				" SELECT r.id_fr as r_id_fr, sv.id_versamento as sv_id_versamento " +
				" FROM pagamenti p join singoli_versamenti sv on sv.id= p.id_singolo_versamento and id_rr is not null " +
				" LEFT JOIN rendicontazioni r on p.id = r.id_pagamento and r.esito=3 $PLACEHOLDER_IN$ " +
				" ) ) s1 " +
				" left join fr on fr.id = s1.r_id_fr left join versamenti on versamenti.id = s1.sv_id_versamento $PLACEHOLDER_OUT$ ";
	}

	@Override
	public String getFrQuery() {
		return "select fr.cod_flusso,fr.stato,fr.descrizione_stato,fr.iur,fr.data_ora_flusso,fr.data_regolamento,fr.data_acquisizione,fr.numero_pagamenti,fr.importo_totale_pagamenti,fr.cod_bic_riversamento,fr.xml,fr.id,fr.cod_psp,fr.cod_dominio, ok, anomale, altro_intermediario from ( " +
			" select fr.cod_flusso,fr.stato,fr.descrizione_stato,fr.iur,fr.data_ora_flusso,fr.data_regolamento,fr.data_acquisizione,fr.numero_pagamenti,fr.importo_totale_pagamenti,fr.cod_bic_riversamento,fr.xml,fr.id,fr.cod_psp,fr.cod_dominio, ok, anomale, altro_intermediario, ROW_NUMBER() OVER ( ORDER BY data_ora_flusso DESC) AS rowNumber " +
			" from ( select fr.id, sum(CASE WHEN r.stato='OK' THEN 1 ELSE 0 END) as ok, sum(CASE WHEN r.stato='ANOMALA' THEN 1 ELSE 0 END) as ANOMALE, sum(CASE WHEN r.stato='ALTRO_INTERMEDIARIO' THEN 1 ELSE 0 END) as ALTRO_INTERMEDIARIO " +
			" from fr left join rendicontazioni r on fr.id=r.id_fr $PLACEHOLDER_JOIN$ $PLACEHOLDER_WHERE_IN$ " +
			" group by fr.id) conta join fr on conta.id=fr.id $PLACEHOLDER_WHERE_OUT$ order by fr.data_ora_flusso DESC) fr $PLACEHOLDER_OFFSET_LIMIT$"; 
	}

	@Override
	public String getFrCountQuery() {
		return "select count(*) " +
				" from (select  fr.id, " +
				" sum(CASE WHEN r.stato='OK' THEN 1 ELSE 0 END) as ok, " +
				" sum(CASE WHEN r.stato='ANOMALA' THEN 1 ELSE 0 END) as ANOMALE, " +
				" sum(CASE WHEN r.stato='ALTRO_INTERMEDIARIO' THEN 1 ELSE 0 END) as ALTRO_INTERMEDIARIO " +
				" from fr left join rendicontazioni r on fr.id=r.id_fr $PLACEHOLDER_JOIN$ $PLACEHOLDER_WHERE_IN$ " +
				" group by fr.id) fr $PLACEHOLDER_WHERE_OUT$";
	}

	@Override
	public String getStatisticheTransazioniPerEsitoQuery(TipoIntervallo tipoIntervallo, Date data, int limit,
			TransazioniFilter filtro) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object[] getStatisticheTransazioniPerEsitoValues(TipoIntervallo tipoIntervallo, Date data, int limit,
			TransazioniFilter filtro) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
