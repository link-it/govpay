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

import it.govpay.orm.IdIbanAccredito;

import org.openspcoop2.generic_project.beans.AbstractModel;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.Field;
import org.openspcoop2.generic_project.beans.ComplexField;


/**     
 * Model IdIbanAccredito 
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class IdIbanAccreditoModel extends AbstractModel<IdIbanAccredito> {

	public IdIbanAccreditoModel(){
	
		super();
	
		this.COD_IBAN = new Field("codIban",java.lang.String.class,"id-iban-accredito",IdIbanAccredito.class);
		this.ID_DOMINIO = new it.govpay.orm.model.IdDominioModel(new Field("idDominio",it.govpay.orm.IdDominio.class,"id-iban-accredito",IdIbanAccredito.class));
	
	}
	
	public IdIbanAccreditoModel(IField father){
	
		super(father);
	
		this.COD_IBAN = new ComplexField(father,"codIban",java.lang.String.class,"id-iban-accredito",IdIbanAccredito.class);
		this.ID_DOMINIO = new it.govpay.orm.model.IdDominioModel(new ComplexField(father,"idDominio",it.govpay.orm.IdDominio.class,"id-iban-accredito",IdIbanAccredito.class));
	
	}
	
	

	public IField COD_IBAN = null;
	 
	public it.govpay.orm.model.IdDominioModel ID_DOMINIO = null;
	 

	@Override
	public Class<IdIbanAccredito> getModeledClass(){
		return IdIbanAccredito.class;
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