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

import it.govpay.orm.Operatore;

import org.openspcoop2.generic_project.beans.AbstractModel;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.Field;
import org.openspcoop2.generic_project.beans.ComplexField;


/**     
 * Model Operatore 
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class OperatoreModel extends AbstractModel<Operatore> {

	public OperatoreModel(){
	
		super();
	
		this.ID_UTENZA = new it.govpay.orm.model.IdUtenzaModel(new Field("idUtenza",it.govpay.orm.IdUtenza.class,"Operatore",Operatore.class));
		this.NOME = new Field("nome",java.lang.String.class,"Operatore",Operatore.class);
		this.ABILITATO = new Field("abilitato",boolean.class,"Operatore",Operatore.class);
	
	}
	
	public OperatoreModel(IField father){
	
		super(father);
	
		this.ID_UTENZA = new it.govpay.orm.model.IdUtenzaModel(new ComplexField(father,"idUtenza",it.govpay.orm.IdUtenza.class,"Operatore",Operatore.class));
		this.NOME = new ComplexField(father,"nome",java.lang.String.class,"Operatore",Operatore.class);
		this.ABILITATO = new ComplexField(father,"abilitato",boolean.class,"Operatore",Operatore.class);
	
	}
	
	

	public it.govpay.orm.model.IdUtenzaModel ID_UTENZA = null;
	 
	public IField NOME = null;
	 
	public IField ABILITATO = null;
	 

	@Override
	public Class<Operatore> getModeledClass(){
		return Operatore.class;
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