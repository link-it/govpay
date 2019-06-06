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

import it.govpay.orm.IdIncasso;

import org.openspcoop2.generic_project.beans.AbstractModel;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.Field;
import org.openspcoop2.generic_project.beans.ComplexField;


/**     
 * Model IdIncasso 
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class IdIncassoModel extends AbstractModel<IdIncasso> {

	public IdIncassoModel(){
	
		super();
	
		this.TRN = new Field("trn",java.lang.String.class,"id-incasso",IdIncasso.class);
		this.COD_DOMINIO = new Field("codDominio",java.lang.String.class,"id-incasso",IdIncasso.class);
	
	}
	
	public IdIncassoModel(IField father){
	
		super(father);
	
		this.TRN = new ComplexField(father,"trn",java.lang.String.class,"id-incasso",IdIncasso.class);
		this.COD_DOMINIO = new ComplexField(father,"codDominio",java.lang.String.class,"id-incasso",IdIncasso.class);
	
	}
	
	

	public IField TRN = null;
	 
	public IField COD_DOMINIO = null;
	 

	@Override
	public Class<IdIncasso> getModeledClass(){
		return IdIncasso.class;
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