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

import it.govpay.orm.OperatoreUo;

import org.openspcoop2.generic_project.beans.AbstractModel;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.Field;
import org.openspcoop2.generic_project.beans.ComplexField;


/**     
 * Model OperatoreUo 
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class OperatoreUoModel extends AbstractModel<OperatoreUo> {

	public OperatoreUoModel(){
	
		super();
	
		this.ID_UO = new it.govpay.orm.model.IdUoModel(new Field("idUo",it.govpay.orm.IdUo.class,"OperatoreUo",OperatoreUo.class));
	
	}
	
	public OperatoreUoModel(IField father){
	
		super(father);
	
		this.ID_UO = new it.govpay.orm.model.IdUoModel(new ComplexField(father,"idUo",it.govpay.orm.IdUo.class,"OperatoreUo",OperatoreUo.class));
	
	}
	
	

	public it.govpay.orm.model.IdUoModel ID_UO = null;
	 

	@Override
	public Class<OperatoreUo> getModeledClass(){
		return OperatoreUo.class;
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