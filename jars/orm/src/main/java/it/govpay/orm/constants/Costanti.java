/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2024 Link.it srl (http://www.link.it).
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3, as published by
 * the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package it.govpay.orm.constants;

public class Costanti {

	private Costanti () {}
	
	public static final String PARAMETER_EXPRESSION = "expression";
	public static final String PARAMETER_PAGINATED_EXPRESSION = "paginatedExpression";
	public static final String PARAMETER_UNION_EXPRESSION = "unionExpression";
	public static final String PARAMETER_UPDATE_MODELS = "updateModels";
	public static final String PARAMETER_OBJ = "obj";
	public static final String PARAMETER_CONDITION = "condition";
	public static final String PARAMETER_ID = "id";
	public static final String PARAMETER_UPDATE_FIELDS = "updateFields";
	public static final String PARAMETER_TABLE_ID = "tableId";
	public static final String PARAMETER_OLD_ID = "oldId";
	public static final String PARAMETER_ID_MAPPING_RESOLUTION_BEHAVIOUR = "idMappingResolutionBehaviour";
	public static final String PARAMETER_RETURN_CLASS_TYPES = "returnClassTypes";

	public static final String MODEL_ACL = "acl";
	public static final String MODEL_ALLEGATO = "allegato";
	public static final String MODEL_APPLICAZIONE = "applicazione";
	public static final String MODEL_AUDIT = "audit";
	public static final String MODEL_BATCH = "batch";
	public static final String MODEL_CONFIGURAZIONE = "configurazione";
	public static final String MODEL_CONNETTORE = "connettore";
	public static final String MODEL_DOCUMENTO = "documento";
	public static final String MODEL_DOMINIO = "dominio";
	public static final String MODEL_EVENTO = "evento";
	public static final String MODEL_FR = "fr";
	public static final String MODEL_IBAN_ACCREDITO = "ibanAccredito";
	public static final String MODEL_INCASSO = "incasso";
	public static final String MODEL_INTERMEDIARIO = "intermediario";
	public static final String MODEL_IUV_SEARCH = "iuvSearch";
	public static final String MODEL_NOTIFICA_APP_IO = "notificaAppIO";
	public static final String MODEL_NOTIFICA = "notifica";
	public static final String MODEL_OPERATORE = "operatore";
	public static final String MODEL_OPERAZIONE = "operazione";
	public static final String MODEL_PAGAMENTO = "pagamento";
	public static final String MODEL_PAGAMENTO_PORTALE = "pagamentoPortale";
	public static final String MODEL_PAGAMENTO_PORTALE_VERSAMENTO = "pagamentoPortaleVersamento";
	public static final String MODEL_PROMEMORIA = "promemoria";
	public static final String MODEL_RENDICONTAZIONE = "rendicontazione";
	public static final String MODEL_RPT = "rpt";
	public static final String MODEL_SINGOLO_VERSAMENTO = "singoloVersamento";
	public static final String MODEL_STAMPA = "stampa";
	public static final String MODEL_STAZIONE = "stazione";
	public static final String MODEL_TIPO_TRIBUTO = "tipoTributo";
	public static final String MODEL_TIPO_VERSAMENTO_DOMINIO = "tipoVersamentoDominio";
	public static final String MODEL_TIPO_VERSAMENTO = "tipoVersamento";
	public static final String MODEL_TRACCIATO = "tracciato";
	public static final String MODEL_TRACCIATO_NOTIFICA_PAGAMENTI = "tracciatoNotificaPagamenti";
	public static final String MODEL_TRIBUTO = "tributo";
	public static final String MODEL_UO = "uo";
	public static final String MODEL_UTENZA_DOMINIO = "utenzaDominio";
	public static final String MODEL_UTENZA = "utenza";
	public static final String MODEL_UTENZA_TIPO_VERSAMENTO = "utenzaTipoVersamento";
	public static final String MODEL_VERSAMENTO_INCASSO = "versamentoIncasso";
	public static final String MODEL_VERSAMENTO = "versamento";
	public static final String MODEL_VISTA_EVENTI_PAGAMENTO = "vistaEventiPagamento";
	public static final String MODEL_VISTA_EVENTI_RPT = "vistaEventiRpt";
	public static final String MODEL_VISTA_EVENTI_VERSAMENTO = "vistaEventiVersamento";
	public static final String MODEL_VISTA_PAGAMENTO = "vistaPagamento";
	public static final String MODEL_VISTA_PAGAMENTO_PORTALE = "vistaPagamentoPortale";
	public static final String MODEL_VISTA_RENDICONTAZIONE = "vistaRendicontazione";
	public static final String MODEL_VISTA_RISCOSSIONI = "vistaRiscossioni";
	public static final String MODEL_VISTA_RPT_VERSAMENTO = "vistaRptVersamento";
	public static final String MODEL_VISTA_VERSAMENTO = "vistaVersamento";
	public static final String MODEL_VISTA_VERSAMENTO_NON_RENDICONTATO = "vistaVersamentoNonRendicontato";


	public static final String HAS_WRONG_TYPE_EXPECT_0 = "has wrong type, expect {0}";
	
	public static final String ERROR_MSG_IS_LESS_EQUALS_0 = "is less equals 0";
	
	
	/*
	 private String modelName = Costanti.MODEL_;
	private String modelClassName = .class.getName();
	private String idModelClassName = .class.getName(); 
	 * */
}
