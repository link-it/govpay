/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2017 Link.it srl (http://www.link.it).
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
package it.govpay.orm.model;

/**     
 * Factory
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class ModelFactory {

	public static ConfigurazioneModel CONFIGURAZIONE = new ConfigurazioneModel();
	
	public static DominioModel DOMINIO = new DominioModel();
	
	public static UoModel UO = new UoModel();
	
	public static ConnettoreModel CONNETTORE = new ConnettoreModel();
	
	public static IntermediarioModel INTERMEDIARIO = new IntermediarioModel();
	
	public static StazioneModel STAZIONE = new StazioneModel();
	
	public static ApplicazioneModel APPLICAZIONE = new ApplicazioneModel();
	
	public static IbanAccreditoModel IBAN_ACCREDITO = new IbanAccreditoModel();
	
	public static TributoModel TRIBUTO = new TributoModel();
	
	public static TipoTributoModel TIPO_TRIBUTO = new TipoTributoModel();
	
	public static AuditModel AUDIT = new AuditModel();
	
	public static ACLModel ACL = new ACLModel();
	
	public static UtenzaDominioModel UTENZA_DOMINIO = new UtenzaDominioModel();
	
	public static UtenzaTipoVersamentoModel UTENZA_TIPO_VERSAMENTO = new UtenzaTipoVersamentoModel();
	
	public static UtenzaModel UTENZA = new UtenzaModel();
	
	public static OperatoreModel OPERATORE = new OperatoreModel();
	
	public static IUVModel IUV = new IUVModel();
	
	public static TipoVersamentoModel TIPO_VERSAMENTO = new TipoVersamentoModel();
	
	public static TipoVersamentoDominioModel TIPO_VERSAMENTO_DOMINIO = new TipoVersamentoDominioModel();
	
	public static VersamentoModel VERSAMENTO = new VersamentoModel();
	
	public static DocumentoModel DOCUMENTO = new DocumentoModel();
	
	public static EventoModel EVENTO = new EventoModel();
	
	public static SingoloVersamentoModel SINGOLO_VERSAMENTO = new SingoloVersamentoModel();
	
	public static RPTModel RPT = new RPTModel();
	
	public static RRModel RR = new RRModel();
	
	public static RendicontazioneModel RENDICONTAZIONE = new RendicontazioneModel();
	
	public static PagamentoPortaleVersamentoModel PAGAMENTO_PORTALE_VERSAMENTO = new PagamentoPortaleVersamentoModel();
	
	public static PagamentoPortaleModel PAGAMENTO_PORTALE = new PagamentoPortaleModel();
	
	public static PagamentoModel PAGAMENTO = new PagamentoModel();
	
	public static NotificaModel NOTIFICA = new NotificaModel();
	
	public static NotificaAppIOModel NOTIFICA_APP_IO = new NotificaAppIOModel();
	
	public static PromemoriaModel PROMEMORIA = new PromemoriaModel();
	
	public static IncassoModel INCASSO = new IncassoModel();
	
	public static FRModel FR = new FRModel();
	
	public static VistaRendicontazioneModel VISTA_RENDICONTAZIONE = new VistaRendicontazioneModel();
	
	public static VistaRptVersamentoModel VISTA_RPT_VERSAMENTO = new VistaRptVersamentoModel();
	
	public static BatchModel BATCH = new BatchModel();
	
	public static OperazioneModel OPERAZIONE = new OperazioneModel();
	
	public static TracciatoModel TRACCIATO = new TracciatoModel();
	
	public static TracciatoNotificaPagamentiModel TRACCIATO_NOTIFICA_PAGAMENTI = new TracciatoNotificaPagamentiModel();
	
	public static StampaModel STAMPA = new StampaModel();
	
	public static VersamentoIncassoModel VERSAMENTO_INCASSO = new VersamentoIncassoModel();
	
	public static VistaRiscossioniModel VISTA_RISCOSSIONI = new VistaRiscossioniModel();
	
	public static VistaPagamentoPortaleModel VISTA_PAGAMENTO_PORTALE = new VistaPagamentoPortaleModel();
	
	public static VistaPagamentoModel VISTA_PAGAMENTO = new VistaPagamentoModel();
	
	public static VistaVersamentoModel VISTA_VERSAMENTO = new VistaVersamentoModel();
	
	public static VistaVersamentoNonRendicontatoModel VISTA_VERSAMENTO_NON_RENDICONTATO = new VistaVersamentoNonRendicontatoModel();
	
	public static AllegatoModel ALLEGATO = new AllegatoModel();
	

}
