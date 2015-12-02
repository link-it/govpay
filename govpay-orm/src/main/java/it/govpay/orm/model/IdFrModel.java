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

import it.govpay.orm.IdFr;

import org.openspcoop2.generic_project.beans.AbstractModel;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.Field;
import org.openspcoop2.generic_project.beans.ComplexField;


/**     
 * Model IdFr 
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class IdFrModel extends AbstractModel<IdFr> {

	public IdFrModel(){
	
		super();
	
		this.COD_FLUSSO = new Field("codFlusso",java.lang.String.class,"id-fr",IdFr.class);
		this.ANNO_RIFERIMENTO = new Field("annoRiferimento",int.class,"id-fr",IdFr.class);
	
	}
	
	public IdFrModel(IField father){
	
		super(father);
	
		this.COD_FLUSSO = new ComplexField(father,"codFlusso",java.lang.String.class,"id-fr",IdFr.class);
		this.ANNO_RIFERIMENTO = new ComplexField(father,"annoRiferimento",int.class,"id-fr",IdFr.class);
	
	}
	
	

	public IField COD_FLUSSO = null;
	 
	public IField ANNO_RIFERIMENTO = null;
	 

	@Override
	public Class<IdFr> getModeledClass(){
		return IdFr.class;
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