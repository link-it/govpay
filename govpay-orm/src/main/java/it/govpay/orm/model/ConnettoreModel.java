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

import it.govpay.orm.Connettore;

import org.openspcoop2.generic_project.beans.AbstractModel;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.Field;
import org.openspcoop2.generic_project.beans.ComplexField;


/**     
 * Model Connettore 
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class ConnettoreModel extends AbstractModel<Connettore> {

	public ConnettoreModel(){
	
		super();
	
		this.COD_CONNETTORE = new Field("codConnettore",java.lang.String.class,"Connettore",Connettore.class);
		this.COD_PROPRIETA = new Field("codProprieta",java.lang.String.class,"Connettore",Connettore.class);
		this.VALORE = new Field("valore",java.lang.String.class,"Connettore",Connettore.class);
	
	}
	
	public ConnettoreModel(IField father){
	
		super(father);
	
		this.COD_CONNETTORE = new ComplexField(father,"codConnettore",java.lang.String.class,"Connettore",Connettore.class);
		this.COD_PROPRIETA = new ComplexField(father,"codProprieta",java.lang.String.class,"Connettore",Connettore.class);
		this.VALORE = new ComplexField(father,"valore",java.lang.String.class,"Connettore",Connettore.class);
	
	}
	
	

	public IField COD_CONNETTORE = null;
	 
	public IField COD_PROPRIETA = null;
	 
	public IField VALORE = null;
	 

	@Override
	public Class<Connettore> getModeledClass(){
		return Connettore.class;
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