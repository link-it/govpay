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

import it.govpay.orm.RPT;

import org.openspcoop2.generic_project.beans.AbstractModel;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.Field;
import org.openspcoop2.generic_project.beans.ComplexField;


/**     
 * Model RPT 
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class RPTModel extends AbstractModel<RPT> {

	public RPTModel(){
	
		super();
	
		this.ID_VERSAMENTO = new it.govpay.orm.model.IdVersamentoModel(new Field("idVersamento",it.govpay.orm.IdVersamento.class,"RPT",RPT.class));
		this.ID_CANALE = new it.govpay.orm.model.IdCanaleModel(new Field("idCanale",it.govpay.orm.IdCanale.class,"RPT",RPT.class));
		this.ID_PORTALE = new it.govpay.orm.model.IdPortaleModel(new Field("idPortale",it.govpay.orm.IdPortale.class,"RPT",RPT.class));
		this.COD_CARRELLO = new Field("codCarrello",java.lang.String.class,"RPT",RPT.class);
		this.IUV = new Field("iuv",java.lang.String.class,"RPT",RPT.class);
		this.CCP = new Field("ccp",java.lang.String.class,"RPT",RPT.class);
		this.COD_DOMINIO = new Field("codDominio",java.lang.String.class,"RPT",RPT.class);
		this.COD_MSG_RICHIESTA = new Field("codMsgRichiesta",java.lang.String.class,"RPT",RPT.class);
		this.DATA_MSG_RICHIESTA = new Field("dataMsgRichiesta",java.util.Date.class,"RPT",RPT.class);
		this.STATO = new Field("stato",java.lang.String.class,"RPT",RPT.class);
		this.DESCRIZIONE_STATO = new Field("descrizioneStato",java.lang.String.class,"RPT",RPT.class);
		this.COD_SESSIONE = new Field("codSessione",java.lang.String.class,"RPT",RPT.class);
		this.COD_SESSIONE_PORTALE = new Field("codSessionePortale",java.lang.String.class,"RPT",RPT.class);
		this.PSP_REDIRECT_URL = new Field("pspRedirectURL",java.lang.String.class,"RPT",RPT.class);
		this.XML_RPT = new Field("xmlRPT",byte[].class,"RPT",RPT.class);
		this.DATA_AGGIORNAMENTO_STATO = new Field("dataAggiornamentoStato",java.util.Date.class,"RPT",RPT.class);
		this.CALLBACK_URL = new Field("callbackURL",java.lang.String.class,"RPT",RPT.class);
		this.MODELLO_PAGAMENTO = new Field("modelloPagamento",java.lang.String.class,"RPT",RPT.class);
		this.COD_MSG_RICEVUTA = new Field("codMsgRicevuta",java.lang.String.class,"RPT",RPT.class);
		this.DATA_MSG_RICEVUTA = new Field("dataMsgRicevuta",java.util.Date.class,"RPT",RPT.class);
		this.FIRMA_RICEVUTA = new Field("firmaRicevuta",java.lang.String.class,"RPT",RPT.class);
		this.COD_ESITO_PAGAMENTO = new Field("codEsitoPagamento",java.lang.Integer.class,"RPT",RPT.class);
		this.IMPORTO_TOTALE_PAGATO = new Field("importoTotalePagato",java.lang.Double.class,"RPT",RPT.class);
		this.XML_RT = new Field("xmlRT",byte[].class,"RPT",RPT.class);
		this.COD_STAZIONE = new Field("codStazione",java.lang.String.class,"RPT",RPT.class);
		this.COD_TRANSAZIONE_RPT = new Field("codTransazioneRPT",java.lang.String.class,"RPT",RPT.class);
		this.COD_TRANSAZIONE_RT = new Field("codTransazioneRT",java.lang.String.class,"RPT",RPT.class);
		this.STATO_CONSERVAZIONE = new Field("statoConservazione",java.lang.String.class,"RPT",RPT.class);
		this.DESCRIZIONE_STATO_CONS = new Field("descrizioneStatoCons",java.lang.String.class,"RPT",RPT.class);
		this.DATA_CONSERVAZIONE = new Field("dataConservazione",java.util.Date.class,"RPT",RPT.class);
	
	}
	
	public RPTModel(IField father){
	
		super(father);
	
		this.ID_VERSAMENTO = new it.govpay.orm.model.IdVersamentoModel(new ComplexField(father,"idVersamento",it.govpay.orm.IdVersamento.class,"RPT",RPT.class));
		this.ID_CANALE = new it.govpay.orm.model.IdCanaleModel(new ComplexField(father,"idCanale",it.govpay.orm.IdCanale.class,"RPT",RPT.class));
		this.ID_PORTALE = new it.govpay.orm.model.IdPortaleModel(new ComplexField(father,"idPortale",it.govpay.orm.IdPortale.class,"RPT",RPT.class));
		this.COD_CARRELLO = new ComplexField(father,"codCarrello",java.lang.String.class,"RPT",RPT.class);
		this.IUV = new ComplexField(father,"iuv",java.lang.String.class,"RPT",RPT.class);
		this.CCP = new ComplexField(father,"ccp",java.lang.String.class,"RPT",RPT.class);
		this.COD_DOMINIO = new ComplexField(father,"codDominio",java.lang.String.class,"RPT",RPT.class);
		this.COD_MSG_RICHIESTA = new ComplexField(father,"codMsgRichiesta",java.lang.String.class,"RPT",RPT.class);
		this.DATA_MSG_RICHIESTA = new ComplexField(father,"dataMsgRichiesta",java.util.Date.class,"RPT",RPT.class);
		this.STATO = new ComplexField(father,"stato",java.lang.String.class,"RPT",RPT.class);
		this.DESCRIZIONE_STATO = new ComplexField(father,"descrizioneStato",java.lang.String.class,"RPT",RPT.class);
		this.COD_SESSIONE = new ComplexField(father,"codSessione",java.lang.String.class,"RPT",RPT.class);
		this.COD_SESSIONE_PORTALE = new ComplexField(father,"codSessionePortale",java.lang.String.class,"RPT",RPT.class);
		this.PSP_REDIRECT_URL = new ComplexField(father,"pspRedirectURL",java.lang.String.class,"RPT",RPT.class);
		this.XML_RPT = new ComplexField(father,"xmlRPT",byte[].class,"RPT",RPT.class);
		this.DATA_AGGIORNAMENTO_STATO = new ComplexField(father,"dataAggiornamentoStato",java.util.Date.class,"RPT",RPT.class);
		this.CALLBACK_URL = new ComplexField(father,"callbackURL",java.lang.String.class,"RPT",RPT.class);
		this.MODELLO_PAGAMENTO = new ComplexField(father,"modelloPagamento",java.lang.String.class,"RPT",RPT.class);
		this.COD_MSG_RICEVUTA = new ComplexField(father,"codMsgRicevuta",java.lang.String.class,"RPT",RPT.class);
		this.DATA_MSG_RICEVUTA = new ComplexField(father,"dataMsgRicevuta",java.util.Date.class,"RPT",RPT.class);
		this.FIRMA_RICEVUTA = new ComplexField(father,"firmaRicevuta",java.lang.String.class,"RPT",RPT.class);
		this.COD_ESITO_PAGAMENTO = new ComplexField(father,"codEsitoPagamento",java.lang.Integer.class,"RPT",RPT.class);
		this.IMPORTO_TOTALE_PAGATO = new ComplexField(father,"importoTotalePagato",java.lang.Double.class,"RPT",RPT.class);
		this.XML_RT = new ComplexField(father,"xmlRT",byte[].class,"RPT",RPT.class);
		this.COD_STAZIONE = new ComplexField(father,"codStazione",java.lang.String.class,"RPT",RPT.class);
		this.COD_TRANSAZIONE_RPT = new ComplexField(father,"codTransazioneRPT",java.lang.String.class,"RPT",RPT.class);
		this.COD_TRANSAZIONE_RT = new ComplexField(father,"codTransazioneRT",java.lang.String.class,"RPT",RPT.class);
		this.STATO_CONSERVAZIONE = new ComplexField(father,"statoConservazione",java.lang.String.class,"RPT",RPT.class);
		this.DESCRIZIONE_STATO_CONS = new ComplexField(father,"descrizioneStatoCons",java.lang.String.class,"RPT",RPT.class);
		this.DATA_CONSERVAZIONE = new ComplexField(father,"dataConservazione",java.util.Date.class,"RPT",RPT.class);
	
	}
	
	

	public it.govpay.orm.model.IdVersamentoModel ID_VERSAMENTO = null;
	 
	public it.govpay.orm.model.IdCanaleModel ID_CANALE = null;
	 
	public it.govpay.orm.model.IdPortaleModel ID_PORTALE = null;
	 
	public IField COD_CARRELLO = null;
	 
	public IField IUV = null;
	 
	public IField CCP = null;
	 
	public IField COD_DOMINIO = null;
	 
	public IField COD_MSG_RICHIESTA = null;
	 
	public IField DATA_MSG_RICHIESTA = null;
	 
	public IField STATO = null;
	 
	public IField DESCRIZIONE_STATO = null;
	 
	public IField COD_SESSIONE = null;
	 
	public IField COD_SESSIONE_PORTALE = null;
	 
	public IField PSP_REDIRECT_URL = null;
	 
	public IField XML_RPT = null;
	 
	public IField DATA_AGGIORNAMENTO_STATO = null;
	 
	public IField CALLBACK_URL = null;
	 
	public IField MODELLO_PAGAMENTO = null;
	 
	public IField COD_MSG_RICEVUTA = null;
	 
	public IField DATA_MSG_RICEVUTA = null;
	 
	public IField FIRMA_RICEVUTA = null;
	 
	public IField COD_ESITO_PAGAMENTO = null;
	 
	public IField IMPORTO_TOTALE_PAGATO = null;
	 
	public IField XML_RT = null;
	 
	public IField COD_STAZIONE = null;
	 
	public IField COD_TRANSAZIONE_RPT = null;
	 
	public IField COD_TRANSAZIONE_RT = null;
	 
	public IField STATO_CONSERVAZIONE = null;
	 
	public IField DESCRIZIONE_STATO_CONS = null;
	 
	public IField DATA_CONSERVAZIONE = null;
	 

	@Override
	public Class<RPT> getModeledClass(){
		return RPT.class;
	}
	
	@Override
	public String toString(){
		if(this.getModeledClass()!=null){
			return this.getModeledClass().getName();
		}else{
			return "N.D.";
		}
	}

}