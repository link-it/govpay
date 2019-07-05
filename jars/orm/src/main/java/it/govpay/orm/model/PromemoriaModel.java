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

import it.govpay.orm.Promemoria;

import org.openspcoop2.generic_project.beans.AbstractModel;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.Field;
import org.openspcoop2.generic_project.beans.ComplexField;


/**     
 * Model Promemoria 
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class PromemoriaModel extends AbstractModel<Promemoria> {

	public PromemoriaModel(){
	
		super();
	
		this.ID_VERSAMENTO = new it.govpay.orm.model.IdVersamentoModel(new Field("idVersamento",it.govpay.orm.IdVersamento.class,"Promemoria",Promemoria.class));
		this.ID_RPT = new it.govpay.orm.model.IdRptModel(new Field("idRPT",it.govpay.orm.IdRpt.class,"Promemoria",Promemoria.class));
		this.TIPO = new Field("tipo",java.lang.String.class,"Promemoria",Promemoria.class);
		this.DATA_CREAZIONE = new Field("dataCreazione",java.util.Date.class,"Promemoria",Promemoria.class);
		this.STATO = new Field("stato",java.lang.String.class,"Promemoria",Promemoria.class);
		this.DESCRIZIONE_STATO = new Field("descrizioneStato",java.lang.String.class,"Promemoria",Promemoria.class);
		this.DEBITORE_EMAIL = new Field("debitoreEmail",java.lang.String.class,"Promemoria",Promemoria.class);
		this.MESSAGGIO_CONTENT_TYPE = new Field("messaggioContentType",java.lang.String.class,"Promemoria",Promemoria.class);
		this.OGGETTO = new Field("oggetto",java.lang.String.class,"Promemoria",Promemoria.class);
		this.MESSAGGIO = new Field("messaggio",java.lang.String.class,"Promemoria",Promemoria.class);
		this.ALLEGA_PDF = new Field("allegaPdf",boolean.class,"Promemoria",Promemoria.class);
		this.DATA_AGGIORNAMENTO_STATO = new Field("dataAggiornamentoStato",java.util.Date.class,"Promemoria",Promemoria.class);
		this.DATA_PROSSIMA_SPEDIZIONE = new Field("dataProssimaSpedizione",java.util.Date.class,"Promemoria",Promemoria.class);
		this.TENTATIVI_SPEDIZIONE = new Field("tentativiSpedizione",java.lang.Long.class,"Promemoria",Promemoria.class);
	
	}
	
	public PromemoriaModel(IField father){
	
		super(father);
	
		this.ID_VERSAMENTO = new it.govpay.orm.model.IdVersamentoModel(new ComplexField(father,"idVersamento",it.govpay.orm.IdVersamento.class,"Promemoria",Promemoria.class));
		this.ID_RPT = new it.govpay.orm.model.IdRptModel(new ComplexField(father,"idRPT",it.govpay.orm.IdRpt.class,"Promemoria",Promemoria.class));
		this.TIPO = new ComplexField(father,"tipo",java.lang.String.class,"Promemoria",Promemoria.class);
		this.DATA_CREAZIONE = new ComplexField(father,"dataCreazione",java.util.Date.class,"Promemoria",Promemoria.class);
		this.STATO = new ComplexField(father,"stato",java.lang.String.class,"Promemoria",Promemoria.class);
		this.DESCRIZIONE_STATO = new ComplexField(father,"descrizioneStato",java.lang.String.class,"Promemoria",Promemoria.class);
		this.DEBITORE_EMAIL = new ComplexField(father,"debitoreEmail",java.lang.String.class,"Promemoria",Promemoria.class);
		this.MESSAGGIO_CONTENT_TYPE = new ComplexField(father,"messaggioContentType",java.lang.String.class,"Promemoria",Promemoria.class);
		this.OGGETTO = new ComplexField(father,"oggetto",java.lang.String.class,"Promemoria",Promemoria.class);
		this.MESSAGGIO = new ComplexField(father,"messaggio",java.lang.String.class,"Promemoria",Promemoria.class);
		this.ALLEGA_PDF = new ComplexField(father,"allegaPdf",boolean.class,"Promemoria",Promemoria.class);
		this.DATA_AGGIORNAMENTO_STATO = new ComplexField(father,"dataAggiornamentoStato",java.util.Date.class,"Promemoria",Promemoria.class);
		this.DATA_PROSSIMA_SPEDIZIONE = new ComplexField(father,"dataProssimaSpedizione",java.util.Date.class,"Promemoria",Promemoria.class);
		this.TENTATIVI_SPEDIZIONE = new ComplexField(father,"tentativiSpedizione",java.lang.Long.class,"Promemoria",Promemoria.class);
	
	}
	
	

	public it.govpay.orm.model.IdVersamentoModel ID_VERSAMENTO = null;
	 
	public it.govpay.orm.model.IdRptModel ID_RPT = null;
	 
	public IField TIPO = null;
	 
	public IField DATA_CREAZIONE = null;
	 
	public IField STATO = null;
	 
	public IField DESCRIZIONE_STATO = null;
	 
	public IField DEBITORE_EMAIL = null;
	 
	public IField MESSAGGIO_CONTENT_TYPE = null;
	 
	public IField OGGETTO = null;
	 
	public IField MESSAGGIO = null;
	 
	public IField ALLEGA_PDF = null;
	 
	public IField DATA_AGGIORNAMENTO_STATO = null;
	 
	public IField DATA_PROSSIMA_SPEDIZIONE = null;
	 
	public IField TENTATIVI_SPEDIZIONE = null;
	 

	@Override
	public Class<Promemoria> getModeledClass(){
		return Promemoria.class;
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
