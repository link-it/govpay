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

import it.govpay.orm.IdCanale;

import org.openspcoop2.generic_project.beans.AbstractModel;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.Field;
import org.openspcoop2.generic_project.beans.ComplexField;


/**     
 * Model IdCanale 
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class IdCanaleModel extends AbstractModel<IdCanale> {

	public IdCanaleModel(){
	
		super();
	
		this.ID_PSP = new it.govpay.orm.model.IdPspModel(new Field("idPsp",it.govpay.orm.IdPsp.class,"id-canale",IdCanale.class));
		this.COD_CANALE = new Field("codCanale",java.lang.String.class,"id-canale",IdCanale.class);
		this.TIPO_VERSAMENTO = new Field("tipoVersamento",java.lang.String.class,"id-canale",IdCanale.class);
	
	}
	
	public IdCanaleModel(IField father){
	
		super(father);
	
		this.ID_PSP = new it.govpay.orm.model.IdPspModel(new ComplexField(father,"idPsp",it.govpay.orm.IdPsp.class,"id-canale",IdCanale.class));
		this.COD_CANALE = new ComplexField(father,"codCanale",java.lang.String.class,"id-canale",IdCanale.class);
		this.TIPO_VERSAMENTO = new ComplexField(father,"tipoVersamento",java.lang.String.class,"id-canale",IdCanale.class);
	
	}
	
	

	public it.govpay.orm.model.IdPspModel ID_PSP = null;
	 
	public IField COD_CANALE = null;
	 
	public IField TIPO_VERSAMENTO = null;
	 

	@Override
	public Class<IdCanale> getModeledClass(){
		return IdCanale.class;
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