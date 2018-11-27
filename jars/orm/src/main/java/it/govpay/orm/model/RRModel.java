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

import it.govpay.orm.RR;

import org.openspcoop2.generic_project.beans.AbstractModel;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.Field;
import org.openspcoop2.generic_project.beans.ComplexField;


/**     
 * Model RR 
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class RRModel extends AbstractModel<RR> {

	public RRModel(){
	
		super();
	
		this.ID_RPT = new it.govpay.orm.model.IdRptModel(new Field("idRpt",it.govpay.orm.IdRpt.class,"RR",RR.class));
		this.COD_DOMINIO = new Field("codDominio",java.lang.String.class,"RR",RR.class);
		this.IUV = new Field("iuv",java.lang.String.class,"RR",RR.class);
		this.CCP = new Field("ccp",java.lang.String.class,"RR",RR.class);
		this.COD_MSG_REVOCA = new Field("codMsgRevoca",java.lang.String.class,"RR",RR.class);
		this.DATA_MSG_REVOCA = new Field("dataMsgRevoca",java.util.Date.class,"RR",RR.class);
		this.DATA_MSG_ESITO = new Field("dataMsgEsito",java.util.Date.class,"RR",RR.class);
		this.STATO = new Field("stato",java.lang.String.class,"RR",RR.class);
		this.DESCRIZIONE_STATO = new Field("descrizioneStato",java.lang.String.class,"RR",RR.class);
		this.IMPORTO_TOTALE_RICHIESTO = new Field("importoTotaleRichiesto",double.class,"RR",RR.class);
		this.COD_MSG_ESITO = new Field("codMsgEsito",java.lang.String.class,"RR",RR.class);
		this.IMPORTO_TOTALE_REVOCATO = new Field("importoTotaleRevocato",java.lang.Double.class,"RR",RR.class);
		this.XML_RR = new Field("xmlRR",byte[].class,"RR",RR.class);
		this.XML_ER = new Field("xmlER",byte[].class,"RR",RR.class);
		this.COD_TRANSAZIONE_RR = new Field("codTransazioneRR",java.lang.String.class,"RR",RR.class);
		this.COD_TRANSAZIONE_ER = new Field("codTransazioneER",java.lang.String.class,"RR",RR.class);
	
	}
	
	public RRModel(IField father){
	
		super(father);
	
		this.ID_RPT = new it.govpay.orm.model.IdRptModel(new ComplexField(father,"idRpt",it.govpay.orm.IdRpt.class,"RR",RR.class));
		this.COD_DOMINIO = new ComplexField(father,"codDominio",java.lang.String.class,"RR",RR.class);
		this.IUV = new ComplexField(father,"iuv",java.lang.String.class,"RR",RR.class);
		this.CCP = new ComplexField(father,"ccp",java.lang.String.class,"RR",RR.class);
		this.COD_MSG_REVOCA = new ComplexField(father,"codMsgRevoca",java.lang.String.class,"RR",RR.class);
		this.DATA_MSG_REVOCA = new ComplexField(father,"dataMsgRevoca",java.util.Date.class,"RR",RR.class);
		this.DATA_MSG_ESITO = new ComplexField(father,"dataMsgEsito",java.util.Date.class,"RR",RR.class);
		this.STATO = new ComplexField(father,"stato",java.lang.String.class,"RR",RR.class);
		this.DESCRIZIONE_STATO = new ComplexField(father,"descrizioneStato",java.lang.String.class,"RR",RR.class);
		this.IMPORTO_TOTALE_RICHIESTO = new ComplexField(father,"importoTotaleRichiesto",double.class,"RR",RR.class);
		this.COD_MSG_ESITO = new ComplexField(father,"codMsgEsito",java.lang.String.class,"RR",RR.class);
		this.IMPORTO_TOTALE_REVOCATO = new ComplexField(father,"importoTotaleRevocato",java.lang.Double.class,"RR",RR.class);
		this.XML_RR = new ComplexField(father,"xmlRR",byte[].class,"RR",RR.class);
		this.XML_ER = new ComplexField(father,"xmlER",byte[].class,"RR",RR.class);
		this.COD_TRANSAZIONE_RR = new ComplexField(father,"codTransazioneRR",java.lang.String.class,"RR",RR.class);
		this.COD_TRANSAZIONE_ER = new ComplexField(father,"codTransazioneER",java.lang.String.class,"RR",RR.class);
	
	}
	
	

	public it.govpay.orm.model.IdRptModel ID_RPT = null;
	 
	public IField COD_DOMINIO = null;
	 
	public IField IUV = null;
	 
	public IField CCP = null;
	 
	public IField COD_MSG_REVOCA = null;
	 
	public IField DATA_MSG_REVOCA = null;
	 
	public IField DATA_MSG_ESITO = null;
	 
	public IField STATO = null;
	 
	public IField DESCRIZIONE_STATO = null;
	 
	public IField IMPORTO_TOTALE_RICHIESTO = null;
	 
	public IField COD_MSG_ESITO = null;
	 
	public IField IMPORTO_TOTALE_REVOCATO = null;
	 
	public IField XML_RR = null;
	 
	public IField XML_ER = null;
	 
	public IField COD_TRANSAZIONE_RR = null;
	 
	public IField COD_TRANSAZIONE_ER = null;
	 

	@Override
	public Class<RR> getModeledClass(){
		return RR.class;
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