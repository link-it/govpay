/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2016 Link.it srl (http://www.link.it).
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
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

	public static DominioModel DOMINIO = new DominioModel();
	
	public static PspModel PSP = new PspModel();
	
	public static CanaleModel CANALE = new CanaleModel();
	
	public static UoModel UO = new UoModel();
	
	public static PortaleModel PORTALE = new PortaleModel();
	
	public static ConnettoreModel CONNETTORE = new ConnettoreModel();
	
	public static IntermediarioModel INTERMEDIARIO = new IntermediarioModel();
	
	public static StazioneModel STAZIONE = new StazioneModel();
	
	public static ApplicazioneModel APPLICAZIONE = new ApplicazioneModel();
	
	public static IbanAccreditoModel IBAN_ACCREDITO = new IbanAccreditoModel();
	
	public static TributoModel TRIBUTO = new TributoModel();
	
	public static TipoTributoModel TIPO_TRIBUTO = new TipoTributoModel();
	
	public static ACLModel ACL = new ACLModel();
	
	public static OperatoreModel OPERATORE = new OperatoreModel();
	
	public static IUVModel IUV = new IUVModel();
	
	public static VersamentoModel VERSAMENTO = new VersamentoModel();
	
	public static EventoModel EVENTO = new EventoModel();
	
	public static SingoloVersamentoModel SINGOLO_VERSAMENTO = new SingoloVersamentoModel();
	
	public static RPTModel RPT = new RPTModel();
	
	public static RRModel RR = new RRModel();
	
	public static PagamentoModel PAGAMENTO = new PagamentoModel();
	
	public static NotificaModel NOTIFICA = new NotificaModel();
	
	public static FRModel FR = new FRModel();
	
	public static FrApplicazioneModel FR_APPLICAZIONE = new FrApplicazioneModel();
	
	public static RendicontazioneSenzaRPTModel RENDICONTAZIONE_SENZA_RPT = new RendicontazioneSenzaRPTModel();
	
	public static RendicontazionePagamentoModel RENDICONTAZIONE_PAGAMENTO = new RendicontazionePagamentoModel();
	
	public static RendicontazionePagamentoSenzaRPTModel RENDICONTAZIONE_PAGAMENTO_SENZA_RPT = new RendicontazionePagamentoSenzaRPTModel();
	
	public static FrFiltroAppModel FR_FILTRO_APP = new FrFiltroAppModel();
	

}