package it.govpay.bd.reportistica;

public class EstrattoContoCostanti {

	public static final String NOME_QUERY_ESTRATTI_CONTO = "estrattiConto";
	public static final String NOME_QUERY_ESTRATTI_CONTO_COUNT = "estrattiContoCount";
	
	public static final String PLACE_HOLDER1_QUERY_ESTRATTI_CONTO = "$PLACE_HOLDER_1$";
	public static final String PLACE_HOLDER2_QUERY_ESTRATTI_CONTO = "$PLACE_HOLDER_2$";
	public static final String PLACE_HOLDER_QUERY_ESTRATTI_CONTO_ID_DOMINI = "$PLACE_HOLDER_ID_DOMINI$";
	public static final String PLACE_HOLDER_QUERY_ESTRATTI_CONTO_ID_VERSAMENTI = "$PLACE_HOLDER_ID_VERSAMENTI$";
	
	public static final String PLACE_HOLDER_QUERY_CLAUSOLA_COD_DOMINIO_PAGAMENTI = " join uo u on v.id_uo = u.id join domini d on u.id_dominio = d.id where d.cod_dominio = ? and p.data_acquisizione between ? and ? ";
	public static final String PLACE_HOLDER_QUERY_CLAUSOLA_COD_DOMINIO_RSR = " join uo u on v.id_uo = u.id join domini d on u.id_dominio = d.id where d.cod_dominio = ? and rsr.rendicontazione_data between ? and ? ";
	
	public static final String PLACE_HOLDER_QUERY_CLAUSOLA_COD_DOMINIO_ID_VERSAMENTI = " join uo u on v.id_uo = u.id join domini d on u.id_dominio = d.id where v.id in ( $PLACE_HOLDER_ID_VERSAMENTI$ ) and d.cod_dominio = ? ";
	public static final String PLACE_HOLDER_QUERY_CLAUSOLA_COD_DOMINIO_ID_SINGOLI_VERSAMENTI = " join uo u on v.id_uo = u.id join domini d on u.id_dominio = d.id where sv.id in ( $PLACE_HOLDER_ID_VERSAMENTI$ ) and d.cod_dominio = ? ";
	public static final String PLACE_HOLDER_QUERY_CLAUSOLA_FROM_DOMINI =  " join uo u on v.id_uo = u.id ";
	public static final String PLACE_HOLDER_QUERY_CLAUSOLA_ID_DOMINI =  " and (u.id_dominio in ( $PLACE_HOLDER_ID_DOMINI$ ))";
	
	public static final String COD_VERSAMENTO_HEADER = "Identificativo Versamento";
	public static final String COD_SINGOLO_VERSAMENTO_HEADER = "Identificativo Singolo Versamento";
	public static final String STATO_PAGAMENTO_HEADER = "Stato Pagamento";
	public static final String STATO_VERSAMENTO_HEADER = "Stato Versamento";
	public static final String STATO_SINGOLO_VERSAMENTO_HEADER = "Stato Singolo Versamento";
	public static final String CODICE_FISCALE_DEBITORE_HEADER = "Codice Fiscale Debitore";
	public static final String IMPORTO_PAGATO_HEADER = "Importo Pagato";
	public static final String IMPORTO_DOVUTO_HEADER = "Importo Dovuto";
	public static final String DATA_PAGAMENTO_HEADER = "Data Pagamento";
	public static final String DATA_INCASSO_HEADER = "Data Incasso";
	public static final String CODICE_RIVERSAMENTO_HEADER = "Codice Riversamento";
	public static final String CODICE_RENDICONTAZIONE_HEADER = "Codice Rendicontazione";
	public static final String NOTE_HEADER = "Note";
	public static final String CAUSALE_HEADER = "Causale";
	public static final String BIC_RIVERSAMENTO_HEADER = "Bic Riversamento";
	public static final String ID_REGOLAMENTO_HEADER = "Id Regolamento";
	public static final String IBAN_ACCREDITO_HEADER = "Iban Accredito";
	public static final String IUV_HEADER = "Iuv";
	public static final String IUR_HEADER = "Iur";	
	
}

