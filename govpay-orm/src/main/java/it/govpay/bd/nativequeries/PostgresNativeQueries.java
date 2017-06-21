package it.govpay.bd.nativequeries;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import it.govpay.bd.reportistica.statistiche.TipoIntervallo;
import it.govpay.bd.reportistica.statistiche.filters.TransazioniFilter;

public class PostgresNativeQueries extends NativeQueries {

	@Override
	public String getEstrattiContoQuery() {
		return "SELECT id_pagamento, id_singolo_versamento, id_versamento, data_pagamento, importo_dovuto, importo_pagato, iuv, iur1, f.iur as iur2, f.cod_flusso as cod_flusso_rendicontazione, f.cod_bic_riversamento as cod_bic_riversamento, cod_versamento_ente, stato_versamento, cod_singolo_versamento_ente, stato_singolo_versamento, iban_accredito, debitore_identificativo, note, causale_versamento FROM ( SELECT id_pagamento, id_singolo_versamento, id_versamento, id_fr_applicazione, data_pagamento, importo_pagato, iur1, iuv, iban_accredito, cod_singolo_versamento_ente, note, cod_versamento_ente, stato_versamento, causale_versamento, debitore_identificativo, importo_dovuto, stato_singolo_versamento FROM ( ( select p.id as id_pagamento, p.id_singolo_versamento as id_singolo_versamento, p.id_fr_applicazione as id_fr_applicazione, p.data_pagamento as data_pagamento, p.importo_pagato as importo_pagato, p.iur as iur1, r.iuv as iuv, p.iban_accredito as iban_accredito, sv.cod_singolo_versamento_ente as cod_singolo_versamento_ente, sv.note as note, v.id as id_versamento, v.cod_versamento_ente as cod_versamento_ente, v.stato_versamento as stato_versamento, v.causale_versamento as causale_versamento, v.debitore_identificativo as debitore_identificativo, sv.importo_singolo_versamento as importo_dovuto, sv.stato_singolo_versamento as stato_singolo_versamento from pagamenti p join rpt r on r.id=p.id_rpt join singoli_versamenti sv on sv.id= p.id_singolo_versamento join versamenti v on v.id = sv.id_versamento $PLACE_HOLDER_1$ ) UNION ALL ( select rsr.id as id_pagamento, rsr.id_singolo_versamento as id_singolo_versamento, rsr.id_fr_applicazione as id_fr_applicazione, rsr.rendicontazione_data as data_pagamento, rsr.importo_pagato as importo_pagato, rsr.iur as iur1, iu.iuv as iuv, '' as iban_accredito, sv.cod_singolo_versamento_ente as cod_singolo_versamento_ente, sv.note as note, v.id as id_versamento, v.cod_versamento_ente as cod_versamento_ente, v.stato_versamento as stato_versamento, v.causale_versamento as causale_versamento, v.debitore_identificativo as debitore_identificativo, sv.importo_singolo_versamento as importo_dovuto, sv.stato_singolo_versamento as stato_singolo_versamento from rendicontazioni_senza_rpt rsr join iuv iu on iu.id=rsr.id_iuv join singoli_versamenti sv on sv.id= rsr.id_singolo_versamento join versamenti v on v.id = sv.id_versamento $PLACE_HOLDER_2$ ) ) as subquery1 order by iuv ASC, data_pagamento ASC ) pag left outer join fr_applicazioni fra on pag.id_fr_applicazione=fra.id left outer join fr f on f.id=fra.id_fr order by cod_flusso_rendicontazione, iuv, data_pagamento OFFSET ? LIMIT ?";
	}

	@Override
	public String getEstrattiContoCountQuery() {
		return "SELECT sum (count_id_pagamento) as totale_pagamenti FROM ( ( select count (p.id) as count_id_pagamento from pagamenti p join rpt r on r.id=p.id_rpt join singoli_versamenti sv on sv.id= p.id_singolo_versamento join versamenti v on v.id = sv.id_versamento $PLACE_HOLDER_1$ ) UNION ALL ( select count (rsr.id) as count_id_pagamento from rendicontazioni_senza_rpt rsr join iuv iu on iu.id=rsr.id_iuv join singoli_versamenti sv on sv.id= rsr.id_singolo_versamento join versamenti v on v.id = sv.id_versamento $PLACE_HOLDER_2$ ) ) as subquery1";
	}

	@Override
	public String getRendicontazionePagamentoQuery() {
		return " SELECT " +
				" fr.cod_flusso,fr.stato,fr.descrizione_stato,fr.iur,fr.data_ora_flusso,fr.data_regolamento,fr.data_acquisizione,fr.numero_pagamenti,fr.importo_totale_pagamenti,fr.cod_bic_riversamento,fr.xml,fr.id,fr.cod_psp,fr.cod_dominio, " +
				" versamenti.cod_versamento_ente,versamenti.importo_totale,versamenti.stato_versamento,versamenti.descrizione_stato,versamenti.aggiornabile,versamenti.data_creazione,versamenti.data_scadenza,versamenti.data_ora_ultimo_aggiornamento,versamenti.causale_versamento,versamenti.debitore_identificativo,versamenti.debitore_anagrafica,versamenti.debitore_indirizzo,versamenti.debitore_civico,versamenti.debitore_cap,versamenti.debitore_localita,versamenti.debitore_provincia,versamenti.debitore_nazione,versamenti.debitore_telefono,versamenti.debitore_cellulare,versamenti.debitore_fax,versamenti.debitore_email,versamenti.cod_lotto,versamenti.cod_versamento_lotto,versamenti.cod_anno_tributario,versamenti.cod_bundlekey,versamenti.id,versamenti.id_uo,versamenti.id_applicazione, " +
				" s1.r_iuv,s1.r_iur,s1.r_importo_pagato,s1.r_esito,s1.r_data,s1.r_stato,s1.r_anomalie,s1.r_id,s1.r_id_fr,s1.r_id_pagamento, " +
				" s1.p_importo_pagato,s1.p_data_acquisizione,s1.p_iur,s1.p_data_pagamento,s1.p_commissioni_psp,s1.p_tipo_allegato,s1.p_allegato,s1.p_data_acquisizione_revoca,s1.p_causale_revoca,s1.p_dati_revoca,s1.p_importo_revocato,s1.p_esito_revoca,s1.p_dati_esito_revoca,s1.p_id,s1.p_id_rpt,s1.p_id_singolo_versamento,s1.p_id_rr,s1.p_iban_accredito,s1.p_cod_dominio,s1.p_iuv, " +
				" s1.sv_cod_singolo_versamento_ente,s1.sv_stato_singolo_versamento,s1.sv_importo_singolo_versamento,s1.sv_tipo_bollo,s1.sv_hash_documento,s1.sv_provincia_residenza,s1.sv_tipo_contabilita,s1.sv_codice_contabilita,s1.sv_note,s1.sv_id,s1.sv_id_versamento,s1.sv_id_tributo,s1.sv_id_iban_accredito, s1.tipo " +
				" FROM " +
				" (( " +
				" SELECT (CASE WHEN id_rpt is not null THEN CASE WHEN iban_accredito is not null THEN 'PAGAMENTO' ELSE 'PAGAMENTO_MBT' END ELSE 'PAGAMENTO_SENZA_RPT' END) as tipo, " +
				" r.iuv as r_iuv,r.iur as r_iur,r.importo_pagato as r_importo_pagato,r.esito as r_esito,r.data as r_data,r.stato as r_stato,r.anomalie as r_anomalie,r.id as r_id,r.id_fr as r_id_fr,r.id_pagamento as r_id_pagamento, " +
				" p.importo_pagato as p_importo_pagato,p.data_acquisizione as p_data_acquisizione,p.iur as p_iur,p.data_pagamento as p_data_pagamento,p.commissioni_psp as p_commissioni_psp,p.tipo_allegato as p_tipo_allegato,p.allegato as p_allegato,p.data_acquisizione_revoca as p_data_acquisizione_revoca,p.causale_revoca as p_causale_revoca,p.dati_revoca as p_dati_revoca,p.importo_revocato as p_importo_revocato,p.esito_revoca as p_esito_revoca,p.dati_esito_revoca as p_dati_esito_revoca,p.id as p_id,p.id_rpt as p_id_rpt,p.id_singolo_versamento as p_id_singolo_versamento,p.id_rr as p_id_rr,p.iban_accredito as p_iban_accredito,p.cod_dominio as p_cod_dominio,p.iuv as p_iuv, sv.cod_singolo_versamento_ente as sv_cod_singolo_versamento_ente,sv.stato_singolo_versamento as sv_stato_singolo_versamento,sv.importo_singolo_versamento as sv_importo_singolo_versamento,sv.tipo_bollo as sv_tipo_bollo,sv.hash_documento as sv_hash_documento,sv.provincia_residenza as sv_provincia_residenza,sv.tipo_contabilita as sv_tipo_contabilita,sv.codice_contabilita as sv_codice_contabilita,sv.note as sv_note,sv.id as sv_id,sv.id_versamento as sv_id_versamento,sv.id_tributo as sv_id_tributo,sv.id_iban_accredito as sv_id_iban_accredito " +
				"  " +
				" from pagamenti p join singoli_versamenti sv on sv.id= p.id_singolo_versamento " +
				" RIGHT JOIN rendicontazioni r on p.id = r.id_pagamento where (r.esito=0 or r.esito=9) $PLACEHOLDER_IN$ " +
				" ) " +
				" UNION " +
				" ( " +
				" SELECT 'STORNO' as tipo, r.iuv as r_iuv,r.iur as r_iur,r.importo_pagato as r_importo_pagato,r.esito as r_esito,r.data as r_data,r.stato as r_stato,r.anomalie as r_anomalie,r.id as r_id,r.id_fr as r_id_fr,r.id_pagamento as r_id_pagamento, p.importo_pagato as p_importo_pagato,p.data_acquisizione as p_data_acquisizione,p.iur as p_iur,p.data_pagamento as p_data_pagamento,p.commissioni_psp as p_commissioni_psp,p.tipo_allegato as p_tipo_allegato,p.allegato as p_allegato,p.data_acquisizione_revoca as p_data_acquisizione_revoca,p.causale_revoca as p_causale_revoca,p.dati_revoca as p_dati_revoca,p.importo_revocato as p_importo_revocato,p.esito_revoca as p_esito_revoca,p.dati_esito_revoca as p_dati_esito_revoca,p.id as p_id,p.id_rpt as p_id_rpt,p.id_singolo_versamento as p_id_singolo_versamento,p.id_rr as p_id_rr,p.iban_accredito as p_iban_accredito,p.cod_dominio as p_cod_dominio,p.iuv as p_iuv, sv.cod_singolo_versamento_ente as sv_cod_singolo_versamento_ente,sv.stato_singolo_versamento as sv_stato_singolo_versamento,sv.importo_singolo_versamento as sv_importo_singolo_versamento,sv.tipo_bollo as sv_tipo_bollo,sv.hash_documento as sv_hash_documento,sv.provincia_residenza as sv_provincia_residenza,sv.tipo_contabilita as sv_tipo_contabilita,sv.codice_contabilita as sv_codice_contabilita,sv.note as sv_note,sv.id as sv_id,sv.id_versamento as sv_id_versamento,sv.id_tributo as sv_id_tributo,sv.id_iban_accredito as sv_id_iban_accredito " +
				" FROM pagamenti p join singoli_versamenti sv on sv.id= p.id_singolo_versamento and id_rr is not null " +
				" RIGHT JOIN rendicontazioni r on p.id = r.id_pagamento where r.esito=3 $PLACEHOLDER_IN$ " +
				" ) ) as s1 " +
				" left join fr on fr.id = s1.r_id_fr left join versamenti on versamenti.id = s1.sv_id_versamento $PLACEHOLDER_OUT$ order by fr.cod_flusso, s1.p_data_pagamento $PLACEHOLDER_OFFSET_LIMIT$";
	}

	@Override
	public String getRendicontazionePagamentoCountQuery() {
		return  " SELECT count(*) " +
				" FROM " +
				" (( " +
				" SELECT r.id_fr as r_id_fr, sv.id_versamento as sv_id_versamento " +
				" from pagamenti p join singoli_versamenti sv on sv.id= p.id_singolo_versamento " +
				" RIGHT JOIN rendicontazioni r on p.id = r.id_pagamento where (r.esito=0 or r.esito=9) $PLACEHOLDER_IN$ " +
				" ) " +
				" UNION " +
				" ( " +
				" SELECT r.id_fr as r_id_fr, sv.id_versamento as sv_id_versamento " +
				" FROM pagamenti p join singoli_versamenti sv on sv.id= p.id_singolo_versamento and id_rr is not null " +
				" RIGHT JOIN rendicontazioni r on p.id = r.id_pagamento where r.esito=3 $PLACEHOLDER_IN$ " +
				" ) ) as s1 " +
				" left join fr on fr.id = s1.r_id_fr left join versamenti on versamenti.id = s1.sv_id_versamento $PLACEHOLDER_OUT$ ";
	}

	@Override
	public String getPagamentoRendicontazioneQuery() {
		return " SELECT " +
				" fr.cod_flusso,fr.stato,fr.descrizione_stato,fr.iur,fr.data_ora_flusso,fr.data_regolamento,fr.data_acquisizione,fr.numero_pagamenti,fr.importo_totale_pagamenti,fr.cod_bic_riversamento,fr.xml,fr.id,fr.cod_psp,fr.cod_dominio, " +
				" versamenti.cod_versamento_ente,versamenti.importo_totale,versamenti.stato_versamento,versamenti.descrizione_stato,versamenti.aggiornabile,versamenti.data_creazione,versamenti.data_scadenza,versamenti.data_ora_ultimo_aggiornamento,versamenti.causale_versamento,versamenti.debitore_identificativo,versamenti.debitore_anagrafica,versamenti.debitore_indirizzo,versamenti.debitore_civico,versamenti.debitore_cap,versamenti.debitore_localita,versamenti.debitore_provincia,versamenti.debitore_nazione,versamenti.debitore_telefono,versamenti.debitore_cellulare,versamenti.debitore_fax,versamenti.debitore_email,versamenti.cod_lotto,versamenti.cod_versamento_lotto,versamenti.cod_anno_tributario,versamenti.cod_bundlekey,versamenti.id,versamenti.id_uo,versamenti.id_applicazione, " +
				" s1.r_iuv,s1.r_iur,s1.r_importo_pagato,s1.r_esito,s1.r_data,s1.r_stato,s1.r_anomalie,s1.r_id,s1.r_id_fr,s1.r_id_pagamento, " +
				" s1.p_importo_pagato,s1.p_data_acquisizione,s1.p_iur,s1.p_data_pagamento,s1.p_commissioni_psp,s1.p_tipo_allegato,s1.p_allegato,s1.p_data_acquisizione_revoca,s1.p_causale_revoca,s1.p_dati_revoca,s1.p_importo_revocato,s1.p_esito_revoca,s1.p_dati_esito_revoca,s1.p_id,s1.p_id_rpt,s1.p_id_singolo_versamento,s1.p_id_rr,s1.p_iban_accredito,s1.p_cod_dominio,s1.p_iuv, " +
				" s1.sv_cod_singolo_versamento_ente,s1.sv_stato_singolo_versamento,s1.sv_importo_singolo_versamento,s1.sv_tipo_bollo,s1.sv_hash_documento,s1.sv_provincia_residenza,s1.sv_tipo_contabilita,s1.sv_codice_contabilita,s1.sv_note,s1.sv_id,s1.sv_id_versamento,s1.sv_id_tributo,s1.sv_id_iban_accredito, s1.tipo " +
				" FROM " +
				" (( " +
				" SELECT (CASE WHEN id_rpt is not null THEN CASE WHEN iban_accredito is not null THEN 'PAGAMENTO' ELSE 'PAGAMENTO_MBT' END ELSE 'PAGAMENTO_SENZA_RPT' END) as tipo, " +
				" r.iuv as r_iuv,r.iur as r_iur,r.importo_pagato as r_importo_pagato,r.esito as r_esito,r.data as r_data,r.stato as r_stato,r.anomalie as r_anomalie,r.id as r_id,r.id_fr as r_id_fr,r.id_pagamento as r_id_pagamento, " +
				" p.importo_pagato as p_importo_pagato,p.data_acquisizione as p_data_acquisizione,p.iur as p_iur,p.data_pagamento as p_data_pagamento,p.commissioni_psp as p_commissioni_psp,p.tipo_allegato as p_tipo_allegato,p.allegato as p_allegato,p.data_acquisizione_revoca as p_data_acquisizione_revoca,p.causale_revoca as p_causale_revoca,p.dati_revoca as p_dati_revoca,p.importo_revocato as p_importo_revocato,p.esito_revoca as p_esito_revoca,p.dati_esito_revoca as p_dati_esito_revoca,p.id as p_id,p.id_rpt as p_id_rpt,p.id_singolo_versamento as p_id_singolo_versamento,p.id_rr as p_id_rr,p.iban_accredito as p_iban_accredito,p.cod_dominio as p_cod_dominio,p.iuv as p_iuv, sv.cod_singolo_versamento_ente as sv_cod_singolo_versamento_ente,sv.stato_singolo_versamento as sv_stato_singolo_versamento,sv.importo_singolo_versamento as sv_importo_singolo_versamento,sv.tipo_bollo as sv_tipo_bollo,sv.hash_documento as sv_hash_documento,sv.provincia_residenza as sv_provincia_residenza,sv.tipo_contabilita as sv_tipo_contabilita,sv.codice_contabilita as sv_codice_contabilita,sv.note as sv_note,sv.id as sv_id,sv.id_versamento as sv_id_versamento,sv.id_tributo as sv_id_tributo,sv.id_iban_accredito as sv_id_iban_accredito " +
				"  " +
				" from pagamenti p join singoli_versamenti sv on sv.id= p.id_singolo_versamento " +
				" LEFT JOIN rendicontazioni r on p.id = r.id_pagamento and (r.esito=0 or r.esito=9) $PLACEHOLDER_IN$ " +
				" ) " +
				" UNION " +
				" ( " +
				" SELECT 'STORNO' as tipo, r.iuv as r_iuv,r.iur as r_iur,r.importo_pagato as r_importo_pagato,r.esito as r_esito,r.data as r_data,r.stato as r_stato,r.anomalie as r_anomalie,r.id as r_id,r.id_fr as r_id_fr,r.id_pagamento as r_id_pagamento, p.importo_pagato as p_importo_pagato,p.data_acquisizione as p_data_acquisizione,p.iur as p_iur,p.data_pagamento as p_data_pagamento,p.commissioni_psp as p_commissioni_psp,p.tipo_allegato as p_tipo_allegato,p.allegato as p_allegato,p.data_acquisizione_revoca as p_data_acquisizione_revoca,p.causale_revoca as p_causale_revoca,p.dati_revoca as p_dati_revoca,p.importo_revocato as p_importo_revocato,p.esito_revoca as p_esito_revoca,p.dati_esito_revoca as p_dati_esito_revoca,p.id as p_id,p.id_rpt as p_id_rpt,p.id_singolo_versamento as p_id_singolo_versamento,p.id_rr as p_id_rr,p.iban_accredito as p_iban_accredito,p.cod_dominio as p_cod_dominio,p.iuv as p_iuv, sv.cod_singolo_versamento_ente as sv_cod_singolo_versamento_ente,sv.stato_singolo_versamento as sv_stato_singolo_versamento,sv.importo_singolo_versamento as sv_importo_singolo_versamento,sv.tipo_bollo as sv_tipo_bollo,sv.hash_documento as sv_hash_documento,sv.provincia_residenza as sv_provincia_residenza,sv.tipo_contabilita as sv_tipo_contabilita,sv.codice_contabilita as sv_codice_contabilita,sv.note as sv_note,sv.id as sv_id,sv.id_versamento as sv_id_versamento,sv.id_tributo as sv_id_tributo,sv.id_iban_accredito as sv_id_iban_accredito " +
				" FROM pagamenti p join singoli_versamenti sv on sv.id= p.id_singolo_versamento and id_rr is not null " +
				" LEFT JOIN rendicontazioni r on p.id = r.id_pagamento and r.esito=3 $PLACEHOLDER_IN$ " +
				" ) ) as s1 " +
				" left join fr on fr.id = s1.r_id_fr left join versamenti on versamenti.id = s1.sv_id_versamento $PLACEHOLDER_OUT$ order by s1.p_data_pagamento DESC, fr.cod_flusso $PLACEHOLDER_OFFSET_LIMIT$";
	}

	@Override
	public String getPagamentoRendicontazioneCountQuery() {
		return  " SELECT count(*) " +
				" FROM " +
				" (( " +
				" SELECT r.id_fr as r_id_fr, sv.id_versamento as sv_id_versamento " +
				" from pagamenti p join singoli_versamenti sv on sv.id= p.id_singolo_versamento " +
				" LEFT JOIN rendicontazioni r on p.id = r.id_pagamento and (r.esito=0 or r.esito=9) $PLACEHOLDER_IN$ " +
				" ) " +
				" UNION " +
				" ( " +
				" SELECT r.id_fr as r_id_fr, sv.id_versamento as sv_id_versamento " +
				" FROM pagamenti p join singoli_versamenti sv on sv.id= p.id_singolo_versamento and id_rr is not null " +
				" LEFT JOIN rendicontazioni r on p.id = r.id_pagamento and r.esito=3 $PLACEHOLDER_IN$ " +
				" ) ) as s1 " +
				" left join fr on fr.id = s1.r_id_fr left join versamenti on versamenti.id = s1.sv_id_versamento $PLACEHOLDER_OUT$ ";
	}

	@Override
	public String getFrQuery() {
		return "select fr.cod_flusso,fr.stato,fr.descrizione_stato,fr.iur,fr.data_ora_flusso,fr.data_regolamento,fr.data_acquisizione,fr.numero_pagamenti,fr.importo_totale_pagamenti,fr.cod_bic_riversamento,fr.xml,fr.id,fr.cod_psp,fr.cod_dominio, ok, anomale, altro_intermediario " +
				" from (select  fr.cod_flusso,fr.stato,fr.descrizione_stato,fr.iur,fr.data_ora_flusso,fr.data_regolamento,fr.data_acquisizione,fr.numero_pagamenti,fr.importo_totale_pagamenti,fr.cod_bic_riversamento,fr.xml,fr.id,fr.cod_psp,fr.cod_dominio, " +
				" sum(CASE WHEN r.stato='OK' THEN 1 ELSE 0 END) as ok, " +
				" sum(CASE WHEN r.stato='ANOMALA' THEN 1 ELSE 0 END) as ANOMALE, " +
				" sum(CASE WHEN r.stato='ALTRO_INTERMEDIARIO' THEN 1 ELSE 0 END) as ALTRO_INTERMEDIARIO " +
				" from fr left join rendicontazioni r on fr.id=r.id_fr $PLACEHOLDER_JOIN$ $PLACEHOLDER_WHERE_IN$ " +
				" group by fr.id) as fr $PLACEHOLDER_WHERE_OUT$  order by fr.data_ora_flusso DESC $PLACEHOLDER_OFFSET_LIMIT$";
	}

	@Override
	public String getFrCountQuery() {
		return "select count(*) " +
				" from (select  fr.id, " +
				" sum(CASE WHEN r.stato='OK' THEN 1 ELSE 0 END) as ok, " +
				" sum(CASE WHEN r.stato='ANOMALA' THEN 1 ELSE 0 END) as ANOMALE, " +
				" sum(CASE WHEN r.stato='ALTRO_INTERMEDIARIO' THEN 1 ELSE 0 END) as ALTRO_INTERMEDIARIO " +
				" from fr left join rendicontazioni r on fr.id=r.id_fr $PLACEHOLDER_JOIN$ $PLACEHOLDER_WHERE_IN$ " +
				" group by fr.id) as fr $PLACEHOLDER_WHERE_OUT$";
	}

	@Override
	public String getStatisticheTransazioniPerEsitoQuery(TipoIntervallo tipoIntervallo, Date data, int limit, TransazioniFilter filtro) {
		
		String sql = "select date_trunc(?, rpt.data_msg_richiesta), "
				+ "SUM(CASE WHEN stato = 'RT_ACCETTATA_PA' THEN 1 ELSE 0 END) as successo, "
				+ "SUM(CASE WHEN stato in ('RPT_ERRORE_INVIO_A_NODO','RPT_RIFIUTATA_NODO','RPT_RIFIUTATA_PSP','RPT_ERRORE_INVIO_A_PSP','RT_RIFIUTATA_NODO','RT_RIFIUTATA_PA','RT_ESITO_SCONOSCIUTO_PA','INTERNO_NODO') THEN 1 ELSE 0 END) as errore, "
				+ "SUM(CASE WHEN stato not in ('RPT_ERRORE_INVIO_A_NODO','RPT_RIFIUTATA_NODO','RPT_RIFIUTATA_PSP','RPT_ERRORE_INVIO_A_PSP','RT_RIFIUTATA_NODO','RT_RIFIUTATA_PA','RT_ESITO_SCONOSCIUTO_PA','INTERNO_NODO','RT_ACCETTATA_PA') THEN 1 ELSE 0 END) as in_corso, "
				+ "from rpt right join (SELECT data FROM generate_series(?, ?, ?) as data) elencodate on date_trunc(?, rpt.data_msg_richiesta) = date_trunc(?, elencodate.data)"
				+ "where date_trunc(?, data_msg_richiesta) <= date_trunc(?, ?) ";
		
		if(filtro.getCodDominio() != null) {
			sql += "AND cod_dominio = ? ";
		}
		
		if(filtro.getCodPsp() != null) {
			sql += "AND cod_psp = ? ";
		}
		
		sql += "group by data order by data desc limit ?";
		
		return sql;
	}
	
	@Override
	public Object[] getStatisticheTransazioniPerEsitoValues(TipoIntervallo tipoIntervallo, Date data, int limit, TransazioniFilter filtro) {
		
		
		Calendar calendar = Calendar.getInstance(); // this would default to now
		calendar.setTime(data);
		String date_trunc = "";
		switch (tipoIntervallo) {
		case MENSILE:
			date_trunc = "month";
			calendar.add(Calendar.MONTH, -limit);
			break;
		case GIORNALIERO:
			date_trunc = "day";
			calendar.add(Calendar.DATE, -limit);
			break;
		case ORARIO:
			date_trunc = "hour";
			calendar.add(Calendar.HOUR, -limit);
			break;
		}	
		
		Date start = calendar.getTime();
		
		List<Object> valori = new ArrayList<Object>();
		valori.add(date_trunc);
		valori.add(start);
		valori.add(data);
		valori.add("1 " + date_trunc);
		valori.add(date_trunc);
		valori.add(date_trunc);
		valori.add(date_trunc);
		valori.add(date_trunc);
		valori.add(data);
		if(filtro.getCodDominio() != null) valori.add(filtro.getCodDominio());
		if(filtro.getCodPsp() != null) valori.add(filtro.getCodPsp());
		valori.add(limit);
		
		return valori.toArray(new Object[]{});
	}

}
