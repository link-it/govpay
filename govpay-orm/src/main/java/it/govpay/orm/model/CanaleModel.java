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

import it.govpay.orm.Canale;

import org.openspcoop2.generic_project.beans.AbstractModel;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.Field;
import org.openspcoop2.generic_project.beans.ComplexField;


/**     
 * Model Canale 
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class CanaleModel extends AbstractModel<Canale> {

	public CanaleModel(){
	
		super();
	
		this.ID_PSP = new it.govpay.orm.model.IdPspModel(new Field("idPsp",it.govpay.orm.IdPsp.class,"Canale",Canale.class));
		this.COD_CANALE = new Field("codCanale",java.lang.String.class,"Canale",Canale.class);
		this.COD_INTERMEDIARIO = new Field("codIntermediario",java.lang.String.class,"Canale",Canale.class);
		this.TIPO_VERSAMENTO = new Field("tipoVersamento",java.lang.String.class,"Canale",Canale.class);
		this.MODELLO_PAGAMENTO = new Field("modelloPagamento",java.lang.Integer.class,"Canale",Canale.class);
		this.DISPONIBILITA = new Field("disponibilita",java.lang.String.class,"Canale",Canale.class);
		this.DESCRIZIONE = new Field("descrizione",java.lang.String.class,"Canale",Canale.class);
		this.CONDIZIONI = new Field("condizioni",java.lang.String.class,"Canale",Canale.class);
		this.URL_INFO = new Field("urlInfo",java.lang.String.class,"Canale",Canale.class);
		this.ABILITATO = new Field("abilitato",boolean.class,"Canale",Canale.class);
	
	}
	
	public CanaleModel(IField father){
	
		super(father);
	
		this.ID_PSP = new it.govpay.orm.model.IdPspModel(new ComplexField(father,"idPsp",it.govpay.orm.IdPsp.class,"Canale",Canale.class));
		this.COD_CANALE = new ComplexField(father,"codCanale",java.lang.String.class,"Canale",Canale.class);
		this.COD_INTERMEDIARIO = new ComplexField(father,"codIntermediario",java.lang.String.class,"Canale",Canale.class);
		this.TIPO_VERSAMENTO = new ComplexField(father,"tipoVersamento",java.lang.String.class,"Canale",Canale.class);
		this.MODELLO_PAGAMENTO = new ComplexField(father,"modelloPagamento",java.lang.Integer.class,"Canale",Canale.class);
		this.DISPONIBILITA = new ComplexField(father,"disponibilita",java.lang.String.class,"Canale",Canale.class);
		this.DESCRIZIONE = new ComplexField(father,"descrizione",java.lang.String.class,"Canale",Canale.class);
		this.CONDIZIONI = new ComplexField(father,"condizioni",java.lang.String.class,"Canale",Canale.class);
		this.URL_INFO = new ComplexField(father,"urlInfo",java.lang.String.class,"Canale",Canale.class);
		this.ABILITATO = new ComplexField(father,"abilitato",boolean.class,"Canale",Canale.class);
	
	}
	
	

	public it.govpay.orm.model.IdPspModel ID_PSP = null;
	 
	public IField COD_CANALE = null;
	 
	public IField COD_INTERMEDIARIO = null;
	 
	public IField TIPO_VERSAMENTO = null;
	 
	public IField MODELLO_PAGAMENTO = null;
	 
	public IField DISPONIBILITA = null;
	 
	public IField DESCRIZIONE = null;
	 
	public IField CONDIZIONI = null;
	 
	public IField URL_INFO = null;
	 
	public IField ABILITATO = null;
	 

	@Override
	public Class<Canale> getModeledClass(){
		return Canale.class;
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