/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2015 Link.it srl (http://www.link.it).
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
		this.ID_PSP = new it.govpay.orm.model.IdPspModel(new Field("idPsp",it.govpay.orm.IdPsp.class,"RPT",RPT.class));
		this.ID_CANALE = new it.govpay.orm.model.IdCanaleModel(new Field("idCanale",it.govpay.orm.IdCanale.class,"RPT",RPT.class));
		this.ID_PORTALE = new it.govpay.orm.model.IdPortaleModel(new Field("idPortale",it.govpay.orm.IdPortale.class,"RPT",RPT.class));
		this.ID_TRACCIATO_XML = new it.govpay.orm.model.IdTracciatoModel(new Field("idTracciatoXML",it.govpay.orm.IdTracciato.class,"RPT",RPT.class));
		this.ID_STAZIONE = new it.govpay.orm.model.IdStazioneModel(new Field("idStazione",it.govpay.orm.IdStazione.class,"RPT",RPT.class));
		this.COD_CARRELLO = new Field("codCarrello",java.lang.String.class,"RPT",RPT.class);
		this.IUV = new Field("iuv",java.lang.String.class,"RPT",RPT.class);
		this.CCP = new Field("ccp",java.lang.String.class,"RPT",RPT.class);
		this.COD_DOMINIO = new Field("codDominio",java.lang.String.class,"RPT",RPT.class);
		this.TIPO_VERSAMENTO = new Field("tipoVersamento",java.lang.String.class,"RPT",RPT.class);
		this.ID_ANAGRAFICA_VERSANTE = new it.govpay.orm.model.IdAnagraficaModel(new Field("idAnagraficaVersante",it.govpay.orm.IdAnagrafica.class,"RPT",RPT.class));
		this.DATA_ORA_MSG_RICHIESTA = new Field("dataOraMsgRichiesta",java.util.Date.class,"RPT",RPT.class);
		this.DATA_ORA_CREAZIONE = new Field("dataOraCreazione",java.util.Date.class,"RPT",RPT.class);
		this.COD_MSG_RICHIESTA = new Field("codMsgRichiesta",java.lang.String.class,"RPT",RPT.class);
		this.IBAN_ADDEBITO = new Field("ibanAddebito",java.lang.String.class,"RPT",RPT.class);
		this.AUTENTICAZIONE_SOGGETTO = new Field("autenticazioneSoggetto",java.lang.String.class,"RPT",RPT.class);
		this.FIRMA_RT = new Field("firmaRT",java.lang.String.class,"RPT",RPT.class);
		this.STATO = new Field("stato",java.lang.String.class,"RPT",RPT.class);
		this.DESCRIZIONE_STATO = new Field("descrizioneStato",java.lang.String.class,"RPT",RPT.class);
		this.COD_FAULT = new Field("codFault",java.lang.String.class,"RPT",RPT.class);
		this.CALLBACK_URL = new Field("callbackURL",java.lang.String.class,"RPT",RPT.class);
		this.COD_SESSIONE = new Field("codSessione",java.lang.String.class,"RPT",RPT.class);
		this.PSP_REDIRECT_URL = new Field("pspRedirectURL",java.lang.String.class,"RPT",RPT.class);
	
	}
	
	public RPTModel(IField father){
	
		super(father);
	
		this.ID_VERSAMENTO = new it.govpay.orm.model.IdVersamentoModel(new ComplexField(father,"idVersamento",it.govpay.orm.IdVersamento.class,"RPT",RPT.class));
		this.ID_PSP = new it.govpay.orm.model.IdPspModel(new ComplexField(father,"idPsp",it.govpay.orm.IdPsp.class,"RPT",RPT.class));
		this.ID_CANALE = new it.govpay.orm.model.IdCanaleModel(new ComplexField(father,"idCanale",it.govpay.orm.IdCanale.class,"RPT",RPT.class));
		this.ID_PORTALE = new it.govpay.orm.model.IdPortaleModel(new ComplexField(father,"idPortale",it.govpay.orm.IdPortale.class,"RPT",RPT.class));
		this.ID_TRACCIATO_XML = new it.govpay.orm.model.IdTracciatoModel(new ComplexField(father,"idTracciatoXML",it.govpay.orm.IdTracciato.class,"RPT",RPT.class));
		this.ID_STAZIONE = new it.govpay.orm.model.IdStazioneModel(new ComplexField(father,"idStazione",it.govpay.orm.IdStazione.class,"RPT",RPT.class));
		this.COD_CARRELLO = new ComplexField(father,"codCarrello",java.lang.String.class,"RPT",RPT.class);
		this.IUV = new ComplexField(father,"iuv",java.lang.String.class,"RPT",RPT.class);
		this.CCP = new ComplexField(father,"ccp",java.lang.String.class,"RPT",RPT.class);
		this.COD_DOMINIO = new ComplexField(father,"codDominio",java.lang.String.class,"RPT",RPT.class);
		this.TIPO_VERSAMENTO = new ComplexField(father,"tipoVersamento",java.lang.String.class,"RPT",RPT.class);
		this.ID_ANAGRAFICA_VERSANTE = new it.govpay.orm.model.IdAnagraficaModel(new ComplexField(father,"idAnagraficaVersante",it.govpay.orm.IdAnagrafica.class,"RPT",RPT.class));
		this.DATA_ORA_MSG_RICHIESTA = new ComplexField(father,"dataOraMsgRichiesta",java.util.Date.class,"RPT",RPT.class);
		this.DATA_ORA_CREAZIONE = new ComplexField(father,"dataOraCreazione",java.util.Date.class,"RPT",RPT.class);
		this.COD_MSG_RICHIESTA = new ComplexField(father,"codMsgRichiesta",java.lang.String.class,"RPT",RPT.class);
		this.IBAN_ADDEBITO = new ComplexField(father,"ibanAddebito",java.lang.String.class,"RPT",RPT.class);
		this.AUTENTICAZIONE_SOGGETTO = new ComplexField(father,"autenticazioneSoggetto",java.lang.String.class,"RPT",RPT.class);
		this.FIRMA_RT = new ComplexField(father,"firmaRT",java.lang.String.class,"RPT",RPT.class);
		this.STATO = new ComplexField(father,"stato",java.lang.String.class,"RPT",RPT.class);
		this.DESCRIZIONE_STATO = new ComplexField(father,"descrizioneStato",java.lang.String.class,"RPT",RPT.class);
		this.COD_FAULT = new ComplexField(father,"codFault",java.lang.String.class,"RPT",RPT.class);
		this.CALLBACK_URL = new ComplexField(father,"callbackURL",java.lang.String.class,"RPT",RPT.class);
		this.COD_SESSIONE = new ComplexField(father,"codSessione",java.lang.String.class,"RPT",RPT.class);
		this.PSP_REDIRECT_URL = new ComplexField(father,"pspRedirectURL",java.lang.String.class,"RPT",RPT.class);
	
	}
	
	

	public it.govpay.orm.model.IdVersamentoModel ID_VERSAMENTO = null;
	 
	public it.govpay.orm.model.IdPspModel ID_PSP = null;
	 
	public it.govpay.orm.model.IdCanaleModel ID_CANALE = null;
	 
	public it.govpay.orm.model.IdPortaleModel ID_PORTALE = null;
	 
	public it.govpay.orm.model.IdTracciatoModel ID_TRACCIATO_XML = null;
	 
	public it.govpay.orm.model.IdStazioneModel ID_STAZIONE = null;
	 
	public IField COD_CARRELLO = null;
	 
	public IField IUV = null;
	 
	public IField CCP = null;
	 
	public IField COD_DOMINIO = null;
	 
	public IField TIPO_VERSAMENTO = null;
	 
	public it.govpay.orm.model.IdAnagraficaModel ID_ANAGRAFICA_VERSANTE = null;
	 
	public IField DATA_ORA_MSG_RICHIESTA = null;
	 
	public IField DATA_ORA_CREAZIONE = null;
	 
	public IField COD_MSG_RICHIESTA = null;
	 
	public IField IBAN_ADDEBITO = null;
	 
	public IField AUTENTICAZIONE_SOGGETTO = null;
	 
	public IField FIRMA_RT = null;
	 
	public IField STATO = null;
	 
	public IField DESCRIZIONE_STATO = null;
	 
	public IField COD_FAULT = null;
	 
	public IField CALLBACK_URL = null;
	 
	public IField COD_SESSIONE = null;
	 
	public IField PSP_REDIRECT_URL = null;
	 

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