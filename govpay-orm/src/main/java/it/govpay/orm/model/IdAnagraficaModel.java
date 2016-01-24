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

import it.govpay.orm.IdAnagrafica;

import org.openspcoop2.generic_project.beans.AbstractModel;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.Field;
import org.openspcoop2.generic_project.beans.ComplexField;


/**     
 * Model IdAnagrafica 
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class IdAnagraficaModel extends AbstractModel<IdAnagrafica> {

	public IdAnagraficaModel(){
	
		super();
	
		this.ID_ANAGRAFICA = new Field("idAnagrafica",long.class,"id-anagrafica",IdAnagrafica.class);
		this.COD_UNIVOCO = new Field("codUnivoco",java.lang.String.class,"id-anagrafica",IdAnagrafica.class);
	
	}
	
	public IdAnagraficaModel(IField father){
	
		super(father);
	
		this.ID_ANAGRAFICA = new ComplexField(father,"idAnagrafica",long.class,"id-anagrafica",IdAnagrafica.class);
		this.COD_UNIVOCO = new ComplexField(father,"codUnivoco",java.lang.String.class,"id-anagrafica",IdAnagrafica.class);
	
	}
	
	

	public IField ID_ANAGRAFICA = null;
	 
	public IField COD_UNIVOCO = null;
	 

	@Override
	public Class<IdAnagrafica> getModeledClass(){
		return IdAnagrafica.class;
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