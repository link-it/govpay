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

import it.govpay.orm.Portale;

import org.openspcoop2.generic_project.beans.AbstractModel;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.Field;
import org.openspcoop2.generic_project.beans.ComplexField;


/**     
 * Model Portale 
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class PortaleModel extends AbstractModel<Portale> {

	public PortaleModel(){
	
		super();
	
		this.COD_PORTALE = new Field("codPortale",java.lang.String.class,"Portale",Portale.class);
		this.DEFAULT_CALLBACK_URL = new Field("defaultCallbackURL",java.lang.String.class,"Portale",Portale.class);
		this.PRINCIPAL = new Field("principal",java.lang.String.class,"Portale",Portale.class);
		this.ABILITATO = new Field("abilitato",boolean.class,"Portale",Portale.class);
		this.PORTALE_APPLICAZIONE = new it.govpay.orm.model.PortaleApplicazioneModel(new Field("PortaleApplicazione",it.govpay.orm.PortaleApplicazione.class,"Portale",Portale.class));
	
	}
	
	public PortaleModel(IField father){
	
		super(father);
	
		this.COD_PORTALE = new ComplexField(father,"codPortale",java.lang.String.class,"Portale",Portale.class);
		this.DEFAULT_CALLBACK_URL = new ComplexField(father,"defaultCallbackURL",java.lang.String.class,"Portale",Portale.class);
		this.PRINCIPAL = new ComplexField(father,"principal",java.lang.String.class,"Portale",Portale.class);
		this.ABILITATO = new ComplexField(father,"abilitato",boolean.class,"Portale",Portale.class);
		this.PORTALE_APPLICAZIONE = new it.govpay.orm.model.PortaleApplicazioneModel(new ComplexField(father,"PortaleApplicazione",it.govpay.orm.PortaleApplicazione.class,"Portale",Portale.class));
	
	}
	
	

	public IField COD_PORTALE = null;
	 
	public IField DEFAULT_CALLBACK_URL = null;
	 
	public IField PRINCIPAL = null;
	 
	public IField ABILITATO = null;
	 
	public it.govpay.orm.model.PortaleApplicazioneModel PORTALE_APPLICAZIONE = null;
	 

	@Override
	public Class<Portale> getModeledClass(){
		return Portale.class;
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