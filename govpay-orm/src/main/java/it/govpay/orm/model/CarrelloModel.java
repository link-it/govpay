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

import it.govpay.orm.Carrello;

import org.openspcoop2.generic_project.beans.AbstractModel;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.Field;
import org.openspcoop2.generic_project.beans.ComplexField;


/**     
 * Model Carrello 
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class CarrelloModel extends AbstractModel<Carrello> {

	public CarrelloModel(){
	
		super();
	
		this.ID_RPT = new it.govpay.orm.model.IdRptModel(new Field("idRpt",it.govpay.orm.IdRpt.class,"Carrello",Carrello.class));
		this.COD_CARRELLO = new Field("codCarrello",java.lang.String.class,"Carrello",Carrello.class);
	
	}
	
	public CarrelloModel(IField father){
	
		super(father);
	
		this.ID_RPT = new it.govpay.orm.model.IdRptModel(new ComplexField(father,"idRpt",it.govpay.orm.IdRpt.class,"Carrello",Carrello.class));
		this.COD_CARRELLO = new ComplexField(father,"codCarrello",java.lang.String.class,"Carrello",Carrello.class);
	
	}
	
	

	public it.govpay.orm.model.IdRptModel ID_RPT = null;
	 
	public IField COD_CARRELLO = null;
	 

	@Override
	public Class<Carrello> getModeledClass(){
		return Carrello.class;
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